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
import dev.orne.config.Configurer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Unit tests for {@link ConfigurableComponentsConfigurer}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class ConfigurableComponentsConfigurerTest {

    private @Mock ListableBeanFactory beanFactory;
    private @Mock AnnotationMetadata annotationMetadata;
    private @Mock ObjectProvider<ConfigProvider> objectProvider;
    private ConfigurableComponentsConfigurer configurer;

    /**
     * Initializes the mocks and instance used in the tests.
     */
    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        configurer = new ConfigurableComponentsConfigurer();
    }

    /**
     * Tests that when the {@link EnableConfigurableComponents} annotation is
     * present, the annotation metadata is correctly set in the bean.
     */
    @Test
    void givenAnnotation_whenSetImportMetadata_thenSetAnnotationMetadata() {
        final AnnotationAttributes attrs = mock(AnnotationAttributes.class);
        when(annotationMetadata.getAnnotationAttributes(EnableConfigurableComponents.class.getName())).thenReturn(attrs);
        configurer.setImportMetadata(annotationMetadata);
        assertSame(attrs, configurer.annotationData);
    }

    /**
     * Tests that when the {@link EnableConfigurableComponents} annotation is
     * not present, an {@code BeanInitializationException} is thrown.
     */
    @Test
    void givenNoAnnotation_whenSetImportMetadata_thenThrowException() {
        when(annotationMetadata.getAnnotationAttributes(anyString())).thenReturn(null);
        assertThrows(BeanInitializationException.class, () -> configurer.setImportMetadata(annotationMetadata));
    }

    /**
     * Tests that when a {@code ListableBeanFactory} is provided, it is correctly
     * set in the bean.
     */
    @Test
    void givenListableBeanFactory_whenSetBeanFactory_thenSetBeanFactory() {
        configurer.setBeanFactory(beanFactory);
        assertSame(beanFactory, configurer.beanFactory);
    }

    /**
     * Tests that when a non-{@code ListableBeanFactory} is provided, an
     * {@code BeanInitializationException} is thrown.
     */
    @Test
    void givenNonListableBeanFactory_whenSetBeanFactory_thenThrowException() {
        final BeanFactory factory = mock(BeanFactory.class);
        assertThrows(BeanInitializationException.class, () -> configurer.setBeanFactory(factory));
    }

    /**
     * Tests that when multiple {@code ConfigProvider} beans are present, a
     * {@code BeanInitializationException} is thrown.
     */
    @Test
    void givenMultipleProviders_whenSetConfigProvider_thenThrowException() {
        ConfigProvider cp1 = mock(ConfigProvider.class);
        ConfigProvider cp2 = mock(ConfigProvider.class);
        when(objectProvider.stream()).thenReturn(Stream.of(cp1, cp2));
        assertThrows(BeanInitializationException.class, () -> configurer.setConfigProvider(objectProvider));
    }

    /**
     * Tests that when a single {@code ConfigProvider} bean is present, it is
     * correctly set in the bean.
     */
    @Test
    void givenSingleProvider_whenSetConfigProvider_thenSetConfigProvider() {
        ConfigProvider cp = mock(ConfigProvider.class);
        when(objectProvider.stream()).thenReturn(java.util.stream.Stream.of(cp));
        configurer.setConfigProvider(objectProvider);
        assertSame(cp, configurer.configProvider);
    }

    /**
     * Tests that when no annotation metadata is set, {@code afterPropertiesSet}
     * throws a {@code BeanInitializationException}.
     */
    @Test
    void givenNoAnnotationMetadata_whenAfterPropertiesSet_thenThrowException() {
        configurer.beanFactory = beanFactory;
        assertThrows(BeanInitializationException.class, () -> configurer.afterPropertiesSet());
    }

    /**
     * Tests that when no {@code Config} beans are present in Spring context
     * and no custom {@code ConfigProvider} is set, {@code afterPropertiesSet}
     * throws a {@code BeanInitializationException}.
     */
    @Test
    void givenNoConfigBeans_whenAfterPropertiesSet_thenThrowException() {
        configurer.annotationData = mock(AnnotationAttributes.class);
        when(beanFactory.getBeanNamesForType(Config.class)).thenReturn(new String[]{});
        configurer.beanFactory = beanFactory;
        when(beanFactory.getBeanNamesForType(Config.class)).thenReturn(new String[]{});
        when(beanFactory.getBeansOfType(Config.class)).thenReturn(Collections.emptyMap());
        configurer.configProvider = null;
        assertThrows(BeanInitializationException.class, () -> configurer.afterPropertiesSet());
    }

    /**
     * Tests that when a custom {@code ConfigProvider} is set, it is not
     * overridden in {@code afterPropertiesSet}.
     */
    @Test
    void givenConfigProvider_whenAfterPropertiesSet_thenDoNothing() {
        final ConfigProvider customProvider = mock(ConfigProvider.class);
        configurer.annotationData = mock(AnnotationAttributes.class);
        configurer.beanFactory = beanFactory;
        configurer.configProvider = customProvider;
        assertDoesNotThrow(() -> configurer.afterPropertiesSet());
        assertSame(customProvider, configurer.configProvider);
    }

    /**
     * Tests that when no custom {@code ConfigProvider} is set but there are
     * {@code Config} beans in Spring context, a default
     * {@code ConfigProvider} is created in {@code afterPropertiesSet}.
     */
    @Test
    void givenNoConfigProvider_whenAfterPropertiesSet_thenCreateConfigProvider() {
        configurer.annotationData = mock(AnnotationAttributes.class);
        Config config = mock(Config.class);
        Map<String, Config> configs = new HashMap<>();
        configs.put("default", config);
        configurer.beanFactory = beanFactory;
        when(beanFactory.getBeansOfType(Config.class)).thenReturn(configs);
        when(configurer.annotationData.getString("name")).thenReturn("default");
        configurer.configProvider = null;
        assertDoesNotThrow(() -> configurer.afterPropertiesSet());
        assertNotNull(configurer.configProvider);
    }

    /**
     * Tests that when a config bean name is specified and exists, it is
     * returned by {@code getDefaultConfig}.
     */
    @Test
    void givenExistingConfigBeanName_whenGetDefaultConfig_thenReturnConfigBean() {
        configurer.annotationData = mock(AnnotationAttributes.class);
        Config config = mock(Config.class);
        Map<String, Config> configs = new HashMap<>();
        configs.put("beanName", config);
        when(configurer.annotationData.getString("name")).thenReturn("beanName");
        assertEquals(config, configurer.getDefaultConfig(configs));
    }

    /**
     * Tests that when a config bean name is specified but does not exist, a
     * {@code BeanInitializationException} is thrown by
     * {@code getDefaultConfig}.
     */
    @Test
    void givenMisssingConfigBeanName_whenGetDefaultConfig_thenThrowException() {
        configurer.annotationData = mock(AnnotationAttributes.class);
        Map<String, Config> configs = new HashMap<>();
        when(configurer.annotationData.getString("name")).thenReturn("missing");
        assertThrows(BeanInitializationException.class, () -> configurer.getDefaultConfig(configs));
    }

    /**
     * Tests that when no config bean name is specified and there is a single
     * config bean of the type specified in {@code type} annotation property,
     * it is returned by {@code getDefaultConfig}.
     */
    @Test
    void givenSingleConfigBeanType_whenGetDefaultConfig_thenReturnConfigBean() {
        configurer.annotationData = mock(AnnotationAttributes.class);
        ExtConfig config = mock(ExtConfig.class);
        when(configurer.annotationData.getString("name")).thenReturn("");
        willReturn(ExtConfig.class).given(configurer.annotationData).getClass("type");
        willReturn(Config.class).given(configurer.annotationData).getClass("value");
        configurer.beanFactory = beanFactory;
        when(beanFactory.getBean(ExtConfig.class)).thenReturn(config);
        Map<String, Config> configs = new HashMap<>();
        assertEquals(config, configurer.getDefaultConfig(configs));
    }

    /**
     * Tests that when no config bean name is specified and there is a single
     * config bean of the type specified in {@code value} annotation property,
     * it is returned by {@code getDefaultConfig}.
     */
    @Test
    void givenSingleConfigBeanTypeValue_whenGetDefaultConfig_thenReturnConfigBean() {
        configurer.annotationData = mock(AnnotationAttributes.class);
        ExtConfig config = mock(ExtConfig.class);
        when(configurer.annotationData.getString("name")).thenReturn("");
        willReturn(Config.class).given(configurer.annotationData).getClass("type");
        willReturn(ExtConfig.class).given(configurer.annotationData).getClass("value");
        configurer.beanFactory = beanFactory;
        when(beanFactory.getBean(ExtConfig.class)).thenReturn(config);
        Map<String, Config> configs = new HashMap<>();
        assertEquals(config, configurer.getDefaultConfig(configs));
    }

    /**
     * Tests that when no config bean name is specified and there are multiple
     * config beans of the type specified in {@code type} annotation property,
     * a {@code BeanInitializationException} is thrown by
     * {@code getDefaultConfig}.
     */
    @Test
    void givenMultipleConfigBeanType_whenGetDefaultConfig_thenThrowException() {
        configurer.annotationData = mock(AnnotationAttributes.class);
        when(configurer.annotationData.getString("name")).thenReturn("");
        willReturn(Config.class).given(configurer.annotationData).getClass("type");
        willReturn(Config.class).given(configurer.annotationData).getClass("value");
        configurer.beanFactory = beanFactory;
        when(beanFactory.getBean(Config.class)).thenThrow(new NoUniqueBeanDefinitionException(Config.class, 2, "Ambiguous"));
        Map<String, Config> configs = new HashMap<>();
        assertThrows(BeanInitializationException.class, () -> configurer.getDefaultConfig(configs));
    }

    /**
     * Tests that when no config bean name is specified and there are no
     * config beans of the type specified in {@code type} annotation property,
     * a {@code BeanInitializationException} is thrown by
     * {@code getDefaultConfig}.
     */
    @Test
    void givenMissingConfigBeanType_whenGetDefaultConfig_thenThrowException() {
        configurer.annotationData = mock(AnnotationAttributes.class);
        when(configurer.annotationData.getString("name")).thenReturn("");
        willReturn(Config.class).given(configurer.annotationData).getClass("type");
        willReturn(Config.class).given(configurer.annotationData).getClass("value");
        configurer.beanFactory = beanFactory;
        when(beanFactory.getBean(Config.class)).thenThrow(new NoSuchBeanDefinitionException(Config.class));
        Map<String, Config> configs = new HashMap<>();
        assertThrows(BeanInitializationException.class, () -> configurer.getDefaultConfig(configs));
    }

    /**
     * Tests that when {@code exposeConfigurer} annotation property is false,
     * {@code configurableComponentsConfigurer} returns an empty optional.
     */
    @Test
    void givenExposeConfigurerFalse_whenConfigurableComponentsConfigurer_thenDontExposeConfigurer() {
        configurer.annotationData = mock(AnnotationAttributes.class);
        configurer.beanFactory = beanFactory;
        configurer.configProvider = mock(ConfigProvider.class);
        when(configurer.annotationData.getBoolean("exposeConfigurer")).thenReturn(false);
        assertTrue(configurer.configurableComponentsConfigurer().isEmpty());
    }

    /**
     * Tests that when {@code exposeConfigurer} annotation property is true,
     * {@code configurableComponentsConfigurer} returns a non-empty optional.
     */
    @Test
    void givenExposeConfigurerTrue_whenConfigurableComponentsConfigurer_thenExposeConfigurer() {
        configurer.annotationData = mock(AnnotationAttributes.class);
        configurer.beanFactory = beanFactory;
        configurer.configProvider = mock(ConfigProvider.class);
        when(configurer.annotationData.getBoolean("exposeConfigurer")).thenReturn(true);
        assertTrue(configurer.configurableComponentsConfigurer().isPresent());
    }

    /**
     * Tests that when a custom {@code Configurer} is provided,
     * {@code configurableComponentsPostProcessor()} uses it.
     */
    @Test
    void givenCustomConfigurer_thenConfigurableComponentsPostProcessor_thenUseCustomConfigurer() {
        final Configurer customConfigurer = mock(Configurer.class);
        configurer.annotationData = mock(AnnotationAttributes.class);
        configurer.beanFactory = beanFactory;
        configurer.configProvider = mock(ConfigProvider.class);
        final ConfigurableComponentsPostProcessor result = assertInstanceOf(
                ConfigurableComponentsPostProcessor.class,
                configurer.configurableComponentsPostProcessor(Optional.of(customConfigurer)));
        assertSame(customConfigurer, result.getConfigurer());
    }

    /**
     * Tests that when no custom {@code Configurer} is provided,
     * {@code configurableComponentsPostProcessor()} creates a new one.
     */
    @Test
    void givenNoCustomConfigurer_thenConfigurableComponentsPostProcessor_thenCreateNewConfigurer() {
        configurer.annotationData = mock(AnnotationAttributes.class);
        configurer.beanFactory = beanFactory;
        configurer.configProvider = mock(ConfigProvider.class);
        final ConfigurableComponentsPostProcessor result = assertInstanceOf(
                ConfigurableComponentsPostProcessor.class,
                configurer.configurableComponentsPostProcessor(Optional.empty()));
        assertNotNull(result.getConfigurer());
    }

    interface ExtConfig extends Config {
    }
}
