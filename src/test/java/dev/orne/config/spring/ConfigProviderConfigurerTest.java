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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import dev.orne.config.Config;
import dev.orne.config.ConfigProvider;
import dev.orne.config.impl.ConfigProviderImpl;
import dev.orne.config.impl.SpringEnvironmentConfigImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * Unit tests for {@link ConfigProviderConfigurer}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0
 */
@Tag("ut")
class ConfigProviderConfigurerTest {

    private @Mock ConfigurableEnvironment environment;
    private @Mock ConfigurableListableBeanFactory beanFactory;
    private @Mock AnnotationMetadata annotationMetadata;
    private @Mock ObjectProvider<ConfigProvider> objectProvider;
    private @Mock ObjectProvider<ConfigProviderCustomizer> customizers;
    private ConfigProviderConfigurer configurer;

    /**
     * Initializes the mocks and instance used in the tests.
     */
    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        configurer = new ConfigProviderConfigurer();
    }

    /**
     * Tests that when multiple {@code ConfigProvider} beans are present, a
     * {@code BeanInitializationException} is thrown.
     */
    @Test
    void givenMultipleAppProviders_whenPostProcessBeanFactory_thenThrowException() {
        when(beanFactory.getBeanNamesForType(ConfigProvider.class))
                .thenReturn(new String[] { "appCfgProvider1", "appCfgProvider2" });
        when(beanFactory.getBeanNamesForType(ConfigProviderCustomizer.class))
                .thenReturn(new String[] {});
        assertThrows(BeanInitializationException.class, () -> configurer.postProcessBeanFactory(beanFactory));
    }

    /**
     * Tests that when multiple {@code ConfigProvider} beans are present, a
     * {@code BeanInitializationException} is thrown.
     */
    @Test
    void givenMultipleCustomizers_whenPostProcessBeanFactory_thenThrowException() {
        when(beanFactory.getBeanNamesForType(ConfigProvider.class))
                .thenReturn(new String[] {});
        when(beanFactory.getBeanNamesForType(ConfigProviderCustomizer.class))
                .thenReturn(new String[] { "appCfgProviderCustomizer1", "appCfgProviderCustomizer2" });
        assertThrows(BeanInitializationException.class, () -> configurer.postProcessBeanFactory(beanFactory));
    }

    /**
     * Tests that when a single {@code ConfigProvider} bean is present, it is
     * correctly set in the bean.
     */
    @Test
    void givenSingleProvider_whenPostProcessBeanFactory_thenSetConfigProviderBeanName() {
        when(beanFactory.getBeanNamesForType(ConfigProvider.class))
                .thenReturn(new String[] { "appCfgProvider" });
        when(beanFactory.getBeanNamesForType(ConfigProviderCustomizer.class))
                .thenReturn(new String[] { "appCfgProviderCustomizer" });
        configurer.postProcessBeanFactory(beanFactory);
        assertSame(beanFactory, configurer.beanFactory);
        assertEquals("appCfgProvider", configurer.appConfigProviderBeanName);
        assertNull(configurer.customizerBeanName);
        assertNull(configurer.configProvider);
    }

    /**
     * Tests that when a single {@code ConfigProviderCustomizer} bean is present,
     * it is correctly set in the bean.
     */
    @Test
    void givenSingleCustomizer_whenPostProcessBeanFactory_thenSetConfigProviderBeanName() {
        when(beanFactory.getBeanNamesForType(ConfigProvider.class))
                .thenReturn(new String[] {});
        when(beanFactory.getBeanNamesForType(ConfigProviderCustomizer.class))
                .thenReturn(new String[] { "appCfgProviderCustomizer" });
        configurer.postProcessBeanFactory(beanFactory);
        assertSame(beanFactory, configurer.beanFactory);
        assertNull(configurer.appConfigProviderBeanName);
        assertEquals("appCfgProviderCustomizer", configurer.customizerBeanName);
        assertNull(configurer.configProvider);
    }

    /**
     * Tests that when no {@code ConfigProvider} or
     * {@code ConfigProviderCustomizer} bean is present, it is
     * correctly set in the bean.
     */
    @Test
    void givenNoProviderOrCustomizer_whenPostProcessBeanFactory_thenSetConfigProvider() {
        when(beanFactory.getBeanNamesForType(ConfigProvider.class))
                .thenReturn(new String[] {});
        when(beanFactory.getBeanNamesForType(ConfigProviderCustomizer.class))
                .thenReturn(new String[] {});
        configurer.postProcessBeanFactory(beanFactory);
        assertSame(beanFactory, configurer.beanFactory);
        assertNull(configurer.appConfigProviderBeanName);
        assertNull(configurer.customizerBeanName);
        assertNull(configurer.configProvider);
    }

    /**
     * Tests that {@code createDefaultConfig()} creates a new configuration
     * based on Spring {@code Environment}.
     */
    @Test
    void givenEnvironment_whenCreateDefaultConfig_thenCreatesSpringEnvConfig() {
        configurer.environment = environment;
        final SpringEnvironmentConfigImpl config = assertInstanceOf(
                SpringEnvironmentConfigImpl.class,
                configurer.createDefaultConfig());
        assertNotNull(config);
        assertSame(environment, config.getEnvironment());
    }

    /**
     * Tests that when no {@code Config} beans are present in Spring context
     * and no custom {@code ConfigProviderCustomizer} is set,
     * {@code createConfigProvider()} creates a {@code ConfigProvider}
     * that uses the default configuration for all sub-types.
     */
    @Test
    void givenNoCustomizerAndNoConfigBeans_whenCreateConfigProvider_thenCreatesProviderWithDefaultConfig() {
        final Config defaultConfig = mock(Config.class);
        final Map<String, Config> configBeans = Map.of();
        configurer.environment = environment;
        configurer.beanFactory = beanFactory;
        configurer.customizerBeanName = null;
        configurer.configProvider = null;
        final ConfigProviderConfigurer cfg = spy(configurer);
        willReturn(defaultConfig).given(cfg).createDefaultConfig();
        when(beanFactory.getBeansOfType(Config.class)).thenReturn(configBeans);
        final ConfigProvider provider = cfg.createConfigProvider();
        assertNotNull(provider);
        assertSame(defaultConfig, provider.getDefaultConfig());
        assertTrue(provider.getConfig(Config.class).isPresent());
        assertSame(defaultConfig, provider.getConfig(Config.class).get());
        assertFalse(provider.getConfig(ExtConfig.class).isPresent());
        assertFalse(provider.getConfig(ExtConfig2.class).isPresent());
    }

    /**
     * Tests that when {@code Config} beans are present in Spring context
     * and no custom {@code ConfigProviderCustomizer} is set,
     * {@code createConfigProvider()} creates a {@code ConfigProvider}
     * that uses the default configuration as default and registers the
     * additional configurations.
     */
    @Test
    void givenNoCustomizerAndConfigBeans_whenCreateConfigProvider_thenCreatesProviderWithDefaultConfig() {
        final Config defaultConfig = mock(Config.class);
        final ExtConfig extraConfig1 = mock(ExtConfig.class);
        final ExtConfig2 extraConfig2 = mock(ExtConfig2.class);
        final Map<String, Config> configBeans = Map.of(
                "config1", extraConfig1,
                "config2", extraConfig2);
        configurer.environment = environment;
        configurer.beanFactory = beanFactory;
        configurer.customizerBeanName = null;
        final ConfigProviderConfigurer cfg = spy(configurer);
        willReturn(defaultConfig).given(cfg).createDefaultConfig();
        when(beanFactory.getBeansOfType(Config.class)).thenReturn(configBeans);
        final ConfigProvider provider = cfg.createConfigProvider();
        assertNotNull(provider);
        assertSame(defaultConfig, provider.getDefaultConfig());
        assertTrue(provider.getConfig(Config.class).isPresent());
        assertSame(defaultConfig, provider.getConfig(Config.class).get());
        assertTrue(provider.getConfig(ExtConfig.class).isPresent());
        assertSame(extraConfig1, provider.getConfig(ExtConfig.class).get());
        assertTrue(provider.getConfig(ExtConfig2.class).isPresent());
        assertSame(extraConfig2, provider.getConfig(ExtConfig2.class).get());
        assertNull(configurer.configProvider);
    }

    /**
     * Tests that when no {@code Config} beans are present in Spring context
     * and no custom {@code ConfigProviderCustomizer} is set,
     * {@code createConfigProvider()} uses customizer to create the provider.
     */
    @Test
    void givenCustomizerAndNoConfigBeans_whenCreateConfigProvider_thenUsesCustomizerToCreateProvider() {
        final Config defaultConfig = mock(Config.class);
        final Map<String, Config> configBeans = Map.of();
        final ConfigProviderCustomizer customizer = mock(ConfigProviderCustomizer.class);
        configurer.environment = environment;
        configurer.beanFactory = beanFactory;
        configurer.customizerBeanName = "appCustomizer";
        when(beanFactory.getBean("appCustomizer", ConfigProviderCustomizer.class)).thenReturn(customizer);
        when(customizer.configureDefaultConfig(configBeans)).thenReturn(defaultConfig);
        when(beanFactory.getBeansOfType(Config.class)).thenReturn(configBeans);
        final ConfigProvider provider = configurer.createConfigProvider();
        assertNotNull(provider);
        assertSame(defaultConfig, provider.getDefaultConfig());
        assertTrue(provider.getConfig(Config.class).isPresent());
        assertSame(defaultConfig, provider.getConfig(Config.class).get());
        assertFalse(provider.getConfig(ExtConfig.class).isPresent());
        assertFalse(provider.getConfig(ExtConfig2.class).isPresent());
        then(customizer).should().registerAdditionalConfigs(any(ConfigProviderCustomizer.ConfigRegistry.class), eq(configBeans));
        assertNull(configurer.configProvider);
    }

    /**
     * Tests that when {@code Config} beans are present in Spring context
     * and custom {@code ConfigProviderCustomizer} is set,
     * {@code createConfigProvider()} uses customizer to create the provider.
     */
    @Test
    void givenCustomizerAndConfigBeans_whenCreateConfigProvider_thenUsesCustomizerToCreateProvider() {
        final Config defaultConfig = mock(Config.class);
        final ExtConfig extraConfig1 = mock(ExtConfig.class);
        final ExtConfig2 extraConfig2 = mock(ExtConfig2.class);
        final Map<String, Config> configBeans = Map.of(
                "config1", extraConfig1,
                "config2", extraConfig2);
        final ConfigProviderCustomizer customizer = mock(ConfigProviderCustomizer.class);
        configurer.environment = environment;
        configurer.beanFactory = beanFactory;
        configurer.customizerBeanName = "appCustomizer";
        when(beanFactory.getBean("appCustomizer", ConfigProviderCustomizer.class)).thenReturn(customizer);
        when(customizer.configureDefaultConfig(configBeans)).thenReturn(defaultConfig);
        when(beanFactory.getBeansOfType(Config.class)).thenReturn(configBeans);
        final ConfigProvider provider = configurer.createConfigProvider();
        assertNotNull(provider);
        assertSame(defaultConfig, provider.getDefaultConfig());
        assertTrue(provider.getConfig(Config.class).isPresent());
        assertSame(defaultConfig, provider.getConfig(Config.class).get());
        assertFalse(provider.getConfig(ExtConfig.class).isPresent());
        assertFalse(provider.getConfig(ExtConfig2.class).isPresent());
        then(customizer).should().registerAdditionalConfigs(any(ConfigProviderCustomizer.ConfigRegistry.class), eq(configBeans));
        assertNull(configurer.configProvider);
    }

    /**
     * Tests that when no {@code ConfigProvider} is set
     */
    @Test
    void givenNoProviderBeanName_whenGetConfigProvider_thenCreatesProvider() {
        ConfigProviderImpl provider = mock(ConfigProviderImpl.class);
        ConfigProviderConfigurer spied = spy(configurer);
        willReturn(provider).given(spied).createConfigProvider();
        final ConfigProvider result = spied.getConfigProvider();
        assertSame(provider, result);
        assertSame(provider, spied.configProvider);
        then(spied).should().createConfigProvider();
    }

    /**
     * Tests that when {@code Config} beans are present in Spring context
     * and custom {@code ConfigProviderCustomizer} is set,
     * {@code createConfigProvider()} uses customizer to create the provider.
     */
    @Test
    void givenProviderBeanName_whenGetConfigProvider_thenRetrievesProvider() {
        ConfigProvider provider = mock(ConfigProvider.class);
        when(beanFactory.getBean("appCfgProvider", ConfigProvider.class)).thenReturn(provider);
        configurer.beanFactory = beanFactory;
        configurer.appConfigProviderBeanName = "appCfgProvider";
        final ConfigProvider result = configurer.getConfigProvider();
        assertSame(provider, result);
        assertSame(provider, configurer.configProvider);
    }

    @Test
    void givenProvider_whenGetConfigProvider_thenReturnsProvider() {
        ConfigProvider provider = mock(ConfigProvider.class);
        configurer.configProvider = provider;
        configurer.appConfigProviderBeanName = "appCfgProvider";
        final ConfigProvider result = configurer.getConfigProvider();
        assertSame(provider, result);
        assertSame(provider, configurer.configProvider);
    }

    interface ExtConfig extends Config {
    }

    interface ExtConfig2 extends Config {
    }
}
