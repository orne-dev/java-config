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

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import dev.orne.config.Config;
import dev.orne.config.ConfigProvider;
import dev.orne.config.ConfigProviderBuilder;

/**
 * Bean factory post processor that provides a {@code ConfigProvider}
 * customized per Spring context configuration.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-11
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class ConfigProviderConfigurer
implements EnvironmentAware, BeanFactoryPostProcessor {

    /** The class logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ConfigProviderConfigurer.class);

    /** The name of the automatic {@code Config} bean. */
    public static final String AUTO_CONFIG = "orneConfigAutomaticConfig";
    /** The name of the automatic {@code ConfigProvider} bean. */
    public static final String AUTO_CONFIG_PROVIDER = "orneConfigAutomaticConfigProvider";

    /** The Spring environment. */
    protected Environment environment;
    /** The bean factory. */
    protected ListableBeanFactory beanFactory;
    /** The application provided configuration provider bean name. */
    protected String appConfigProviderBeanName;
    /** The application provided configuration provider customizer bean name. */
    protected String customizerBeanName;
    /** The application provided configuration provider. */
    protected ConfigProvider configProvider;

    /**
     * Creates a new instance.
     */
    public ConfigProviderConfigurer() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnvironment(
            final Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanFactory(
            final @NotNull ConfigurableListableBeanFactory beanFactory)
    throws BeansException {
        this.beanFactory = beanFactory;
        final String[] providerNames = beanFactory.getBeanNamesForType(ConfigProvider.class);
        final String[] customizerNames = beanFactory.getBeanNamesForType(ConfigProviderCustomizer.class);
        if (providerNames.length > 1) {
            throw new BeanInitializationException(providerNames.length + " implementations of " +
                    "ConfigProvider were found when only 1 is supported. " +
                    "Refactor the configuration such that ConfigProvider is " +
                    "implemented only once or not at all.");
        }
        if (customizerNames.length > 1) {
            throw new BeanInitializationException(customizerNames.length + " implementations of " +
                    "ConfigProviderCustomizer were found when only 1 is supported. " +
                    "Refactor the configuration such that ConfigProvider is " +
                    "implemented only once or not at all.");
        }
        if (providerNames.length == 1) {
            this.appConfigProviderBeanName = providerNames[0];
            if (customizerNames.length == 1) {
                LOG.warn("A ConfigProviderCustomizer was found ({}) but will be ignored " +
                        "because a ConfigProvider implementation is also provided ({}).",
                        customizerNames[0],
                        this.appConfigProviderBeanName);
            }
        } else if (customizerNames.length == 1) {
            this.customizerBeanName = customizerNames[0];
        }
    }

    /**
     * Creates and exposes the automatically created {@code Config} bean,
     * if any.
     * 
     * @return The automatically created {@code ConfigProvider} bean.
     */
    public synchronized @NotNull ConfigProvider getConfigProvider() {
        if (this.configProvider == null) {
            if (this.appConfigProviderBeanName == null) {
                LOG.debug("Creating default ConfigProvider bean...");
                this.configProvider = createConfigProvider();
            } else {
                LOG.debug("Using application provided ConfigProvider: {}",
                        this.appConfigProviderBeanName);
                this.configProvider = this.beanFactory.getBean(
                        this.appConfigProviderBeanName,
                        ConfigProvider.class);
            }
        }
        return this.configProvider;
    }

    /**
     * Creates a new configuration provider.
     * 
     * @return The new configuration provider.
     */
    protected @NotNull ConfigProvider createConfigProvider() {
        final Map<String, Config> configs = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                beanFactory,
                Config.class);
        final ConfigProviderBuilder builder;
        if (this.customizerBeanName == null) {
            builder = ConfigProvider.builder(createDefaultConfig());
            configs.values().stream().forEach(builder::addConfig);
        } else {
            LOG.debug("Using application provided ConfigProvider customizer: {}",
                    this.customizerBeanName);
            final ConfigProviderCustomizer customizer = beanFactory.getBean(
                    this.customizerBeanName,
                    ConfigProviderCustomizer.class);
            builder = ConfigProvider.builder(
                    customizer.configureDefaultConfig(configs));
            customizer.registerAdditionalConfigs(builder, configs);
        }
        return builder.build();
    }

    /**
     * Creates a Spring based default configuration.
     * 
     * @return The default configuration.
     */
    protected @NotNull Config createDefaultConfig() {
        LOG.info("Creating default Spring Environment based Config...");
        return Config.fromSpringEnvironment()
                .ofEnvironment(this.environment)
                .withIterableKeys()
                .build();
    }
}
