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

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import dev.orne.config.Config;

/**
 * Spring bean factory post processor that adds to the Spring environment
 * the property sources defined by {@code @ConfigPropertySource} annotations
 * on {@code @Configuration} beans.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-08
 * @since 1.0
 * @see ConfigPropertySource
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class ConfigPropertySourcePostProcessor
implements EnvironmentAware, BeanFactoryPostProcessor {

    /** The class logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ConfigPropertySourcePostProcessor.class);

    /** The prefix for the names of the property sources. */
    public static final String SOURCE_PREFIX = "orneConfigPropertySource.";

    /** The Spring environment. */
    protected ConfigurableEnvironment environment;

    /**
     * Creates a new instance.
     */
    public ConfigPropertySourcePostProcessor() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnvironment(
            final @NotNull Environment environment) {
        if (environment instanceof ConfigurableEnvironment) {
            this.environment = (ConfigurableEnvironment) environment;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postProcessBeanFactory(
            final ConfigurableListableBeanFactory beanFactory)
    throws BeansException {
        if (this.environment == null) {
            throw new BeanInitializationException(
                    "@ConfigPropertySource cannot be used without a ConfigurableEnvironment");
        }
        for (final String beanName : beanFactory.getBeanDefinitionNames()) {
            final BeanDefinition def = beanFactory.getBeanDefinition(beanName);
            if (def instanceof AnnotatedGenericBeanDefinition) {
                final AnnotatedGenericBeanDefinition agbd = (AnnotatedGenericBeanDefinition) def;
                final AnnotationMetadata annotations = agbd.getMetadata();
                if (annotations.hasAnnotation(Configuration.class.getName())) {
                    processConfigurationBean(beanFactory, beanName, annotations);
                }
            }
        }
    }

    /**
     * Process the {@code @ConfigPropertySource} annotations on the given
     * configuration bean and add the corresponding property sources to the
     * environment.
     * 
     * @param beanFactory The bean factory to retrieve configuration beans from.
     * @param configurationBean The name of the configuration bean.
     * @param annotations The {@code Configuration} bean annotation metadata
     *                    to process.
     * @throws BeanInitializationException If the configuration bean cannot be
     *         found and {@code ignoreConfigNotFound} is {@code false}, or if
     *         multiple configuration beans of the specified type are found.
     */
    protected void processConfigurationBean(
            final ConfigurableListableBeanFactory beanFactory,
            final String configurationBean,
            final AnnotationMetadata annotations) {
        processSingleAnnotations(beanFactory, configurationBean, annotations);
        processAggregateAnnotations(beanFactory, configurationBean, annotations);
    }

    /**
     * Process single {@code @ConfigPropertySource} annotations on the given
     * class and add the corresponding property sources to the environment.
     * 
     * @param beanFactory The bean factory to retrieve configuration beans from.
     * @param amd The {@code Configuration} bean annotation metadata to process.
     * @throws BeanInitializationException If the configuration bean cannot be
     *         found and {@code ignoreConfigNotFound} is {@code false}, or if
     *         multiple configuration beans of the specified type are found.
     */
    protected void processSingleAnnotations(
            final ConfigurableListableBeanFactory beanFactory,
            final String configurationBean,
            final AnnotationMetadata annotations) {
        AnnotationAttributes annotation = AnnotationAttributes.fromMap(
                annotations.getAnnotationAttributes(ConfigPropertySource.class.getName()));
        if (annotation != null) {
            processSource(beanFactory, configurationBean, annotation);
        }
    }

    /**
     * Process aggregated {@code @ConfigPropertySources} annotations on the
     * given class and add the corresponding property sources to the
     * environment.
     * 
     * @param beanFactory The bean factory to retrieve configuration beans from.
     * @param amd The {@code Configuration} bean annotation metadata to process.
     * @throws BeanInitializationException If the configuration bean cannot be
     *         found and {@code ignoreConfigNotFound} is {@code false}, or if
     *         multiple configuration beans of the specified type are found.
     */
    protected void processAggregateAnnotations(
            final @NotNull ConfigurableListableBeanFactory beanFactory,
            final @NotNull String configurationBean,
            final @NotNull AnnotationMetadata annotations) {
        AnnotationAttributes aggregator = AnnotationAttributes.fromMap(
                annotations.getAnnotationAttributes(ConfigPropertySources.class.getName()));
        if (aggregator != null) {
            final AnnotationAttributes[] childs = aggregator.getAnnotationArray(
                    "value");
            for (final AnnotationAttributes annotation: childs) {
                processSource(beanFactory, configurationBean, annotation);
            }
        }
    }

    /**
     * Process a single {@code @ConfigPropertySource} annotation and add the
     * corresponding property source to the environment.
     * 
     * @param beanFactory The bean factory to retrieve configuration beans from.
     * @param annotation The {@code ConfigPropertySource} annotation data
     *                   to process.
     * @throws BeanInitializationException If the configuration bean cannot be
     *         found and {@code ignoreConfigNotFound} is {@code false}, or if
     *         multiple configuration beans of the specified type are found.
     */
    protected void processSource(
            final @NotNull ConfigurableListableBeanFactory beanFactory,
            final @NotNull String configurationBean,
            final @NotNull AnnotationAttributes annotation) {
        final String configName = annotation.getString("name");
        final boolean ignoreMissing = annotation.getBoolean("ignoreConfigNotFound");
        Class<? extends Config> configType = annotation.getClass("type");
        if (Config.class.equals(configType)) {
            configType = annotation.getClass("value");
        }
        final String sourceName;
        final String beanName;
        if (configName.isEmpty()) {
            if (Config.class.equals(configType)) {
                throw new BeanInitializationException(String.format(
                        "No Config type or name specified in @ConfigPropertySource on '%s'. "
                        + "Either specify a unique Config bean name with \"name\", "
                        + "or a Config bean type with \"type\" or \"value\".",
                        configurationBean));
            }
            sourceName = SOURCE_PREFIX + configType.getName();
            beanName = findBeanByType(beanFactory, configType, ignoreMissing);
        } else {
            sourceName = SOURCE_PREFIX + configName;
            beanName = validateConfigName(beanFactory, configName, ignoreMissing);
        }
        if (beanName != null) {
            if (this.environment.getPropertySources().contains(sourceName)) {
                LOG.debug("Property source '{}' already exists, skipping addition.",
                        sourceName);
            } else {
                final ConfigLazyPropertySource propertySource = new ConfigLazyPropertySource(
                        sourceName, beanFactory, beanName);
                this.environment.getPropertySources().addFirst(propertySource);
            }
        }
    }

    /**
     * Validates the the specified bean name corresponds to a {@code Config}
     * bean, and returns the name if valid.
     * 
     * @param beanFactory The bean factory to retrieve configuration beans from.
     * @param configName The name of the configuration bean.
     * @param ignoreMissing Whether to ignore a missing configuration bean.
     * @return The configuration bean name, or {@code null} if not found and
     *         {@code ignoreMissing} is {@code true}.
     * @throws BeanInitializationException If no configuration bean is found
     *         and {@code ignoreMissing} is {@code false}.
     */
    protected String validateConfigName(
            final @NotNull ConfigurableListableBeanFactory beanFactory,
            final @NotNull String configName,
            final boolean ignoreMissing) {
        try {
            final BeanDefinition configDef = beanFactory.getBeanDefinition(configName);
            final ResolvableType beanType = configDef.getResolvableType();
            if (beanType.as(Config.class) != ResolvableType.NONE) {
                return configName;
            } else {
                throw new BeanInitializationException(
                        "Bean with name '" + configName + "' is not of type Config (" + configDef.getBeanClassName() + "). " +
                        "Ensure that the specified bean is a Config bean.");
            }
        } catch (final NoSuchBeanDefinitionException e) {
            if (ignoreMissing) {
                LOG.debug("No Config bean found with name '{}', but ignoring missing as requested.",
                        configName);
                return null;
            } else {
                throw new BeanInitializationException(
                        "No Config bean found with name '" + configName + "'. " +
                        "Ensure that a Config bean is defined with the specified name.",
                        e);
            }
        }
    }

    /**
     * Finds a unique bean of the specified configuration type, and returns
     * its name.
     * <p>
     * Does consider prototypes and objects created by FactoryBeans, which
     * means that FactoryBeans will get initialized.
     * If the object created by the FactoryBean doesn't match, the raw
     * FactoryBean itself will be matched against the type.
     * 
     * @param beanFactory The bean factory to retrieve configuration beans from.
     * @param configType The type of the configuration bean.
     * @param ignoreMissing Whether to ignore a missing configuration bean.
     * @return The configuration bean name, or {@code null} if not found and
     *         {@code ignoreMissing} is {@code true}.
     * @throws BeanInitializationException If multiple configuration beans of
     *         the specified type are found, or if no configuration bean is
     *         found and {@code ignoreMissing} is {@code false}.
     */
    protected String findBeanByType(
            final @NotNull ConfigurableListableBeanFactory beanFactory,
            final @NotNull Class<? extends Config> configType,
            final boolean ignoreMissing) {
        final String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
                beanFactory,
                configType);
        if (beanNames.length > 1) {
            throw new BeanInitializationException(
                    "Multiple Config beans found of type '" + configType.getName() + "'. " +
                    "Specify a unique bean name with \"name\" to disambiguate.");
        } else if (beanNames.length == 0) {
            if (ignoreMissing) {
                LOG.debug("No Config bean found of type '{}', but ignoring missing as requested.",
                        configType.getName());
                return null;
            } else {
                throw new BeanInitializationException(
                        "No Config bean found of type '" + configType.getName() + "'. " +
                        "Ensure that a Config bean of the specified type is defined.");
            }
        } else {
            return beanNames[0];
        }
    }
}
