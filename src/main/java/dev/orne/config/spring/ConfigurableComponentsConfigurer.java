package dev.orne.config.spring;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2025 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import dev.orne.config.Config;
import dev.orne.config.Configurer;
import dev.orne.config.ConfigProvider;
import dev.orne.config.ConfigProviderBuilder;

/**
 * Spring configuration for {@code ConfigurableComponentsPostProcessor}
 * based on {@code EnableConfigurableComponents} annotation.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-08
 * @since 1.0
 */
@Configuration
@API(status = API.Status.INTERNAL, since = "1.0")
public class ConfigurableComponentsConfigurer
implements ImportAware, BeanFactoryAware, InitializingBean {

    /** The name of the {@code Configurer} bean. */
    public static final String CONFIGURER = "orneConfigConfigurableComponentsConfigurer";
    /** The name of the {@code ConfigurableComponentsPostProcessor} bean. */
    public static final String POST_PROCESSOR = "orneConfigConfigurableComponentsPostProcessor";

    /** The bean factory. */
    protected ListableBeanFactory beanFactory;
    /** The annotation data for the configuration. */
    protected AnnotationAttributes annotationData;
    /** The application provided configuration provider. */
    protected ConfigProvider configProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBeanFactory(
            final @NotNull BeanFactory beanFactory)
    throws BeansException {
        if (!(beanFactory instanceof ListableBeanFactory)) {
            throw new BeanInitializationException(
                    "@EnableConfigurableComponents requires a ListableBeanFactory");
        }
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setImportMetadata(
            final @NotNull AnnotationMetadata importMetadata) {
        annotationData = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableConfigurableComponents.class.getName()));
        if (annotationData == null) {
            throw new BeanInitializationException(
                    "@EnableConfigurableComponents is not present on importing class " + importMetadata.getClassName());
        }
    }

    /**
     * Sets the application provided {@code ConfigProvider}, if available.
     * <p>
     * If multiple configuration providers are found, an exception is thrown.
     * 
     * @param configurations The {@code ConfigProvider} available in Spring context.
     * @throws BeanInitializationException If multiple configuration providers
     *         are found.
     */
    @Autowired
    protected void setConfigProvider(
            final @NotNull ObjectProvider<ConfigProvider> configurations) {
        List<ConfigProvider> candidates = configurations.stream().collect(Collectors.toList());
        if (!candidates.isEmpty()) {
            if (candidates.size() > 1) {
                throw new BeanInitializationException(candidates.size() + " implementations of " +
                        "ConfigProvider were found when only 1 was expected. " +
                        "Refactor the configuration such that ConfigProvider is " +
                        "implemented only once or not at all.");
            }
            this.configProvider = candidates.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {
        if (this.configProvider == null) {
            if (annotationData == null) {
                throw new BeanInitializationException(
                        "Annotation data is not set. Ensure that this class is used with @EnableOrneConfig.");
            }
            final Map<String, Config> configs = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                    beanFactory,
                    Config.class);
            if (configs.isEmpty()) {
                throw new BeanInitializationException(
                        "No Config beans found. Ensure that at least one Config bean is defined in the application context.");
            }
            final Config defaultConfig = getDefaultConfig(configs);
            final ConfigProviderBuilder builder = ConfigProvider.builder(defaultConfig);
            configs.values().stream().forEach(builder::addConfig);
            this.configProvider = builder.build();
        }
    }

    /**
     * Retrieves the default configuration based on the annotation data.
     * 
     * @param configs The available configuration beans.
     * @return The default configuration.
     * @throws BeanInitializationException If no suitable configuration bean
     *         is found or if multiple beans are found when a specific type is
     *         requested.
     */
    protected @NotNull Config getDefaultConfig(
            final Map<String, Config> configs) {
        final String configName = annotationData.getString("name");
        Config defaultConfig;
        if (configName.isEmpty()) {
            Class<? extends Config> configType = annotationData.getClass("type");
            if (Config.class.equals(configType)) {
                configType = annotationData.getClass("value");
            }
            try {
                defaultConfig = this.beanFactory.getBean(configType);
            } catch (final NoUniqueBeanDefinitionException e) {
                throw new BeanInitializationException(
                        "Multiple Config beans found of type '" + configType.getName() + "'. " +
                        "Specify a unique bean name with \"name\" to disambiguate.",
                        e);
            } catch (final NoSuchBeanDefinitionException e) {
                throw new BeanInitializationException(
                        "No Config bean found of type '" + configType.getName() + "'. " +
                        "Ensure that a Config bean of the specified type is defined.",
                        e);
            }
        } else {
            defaultConfig = configs.get(configName);
            if (defaultConfig == null) {
                throw new BeanInitializationException(
                        "No Config bean found with name '" + configName + "'. " +
                        "Ensure that a Config bean is defined with the specified name.");
            }
        }
        return defaultConfig;
    }

    /**
     * Exposes the Spring properties placeholder configurer configured with
     * default configuration properties.
     * 
     * @return The properties placeholder configurer.
     */
    @Bean(name=CONFIGURER)
    public @NotNull Optional<Configurer> configurableComponentsConfigurer() {
        if (this.annotationData.getBoolean("exposeConfigurer")) {
            return Optional.of(Configurer.fromProvider(this.configProvider));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Exposes the Spring properties placeholder configurer configured with
     * default configuration properties.
     * 
     * @return The properties placeholder configurer.
     */
    @Bean(name=POST_PROCESSOR)
    public @NotNull ConfigurableComponentsPostProcessor configurableComponentsPostProcessor(
            final @NotNull Optional<Configurer> configurer) {
        if (configurer.isPresent()) {
            return new ConfigurableComponentsPostProcessor(configurer.get());
        } else {
            return new ConfigurableComponentsPostProcessor(
                    Configurer.fromProvider(this.configProvider));
        }
    }
}
