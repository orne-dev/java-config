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
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Unit tests for {@link ConfigurableComponentsConfigurer}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0
 */
@Tag("ut")
class ConfigurableComponentsConfigurerTest {

    private @Mock AnnotationMetadata annotationMetadata;
    private @Mock ConfigProvider configProvider;
    private @Mock ConfigProviderConfigurer springConfigProvider;
    private ConfigurableComponentsConfigurer configurer;

    /**
     * Initializes the mocks and instance used in the tests.
     */
    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        given(springConfigProvider.getConfigProvider()).willReturn(configProvider);
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
     * Tests that when {@code exposedConfigurer} has not been set,
     * {@code configurableComponentsConfigurer} returns an empty optional.
     */
    @Test
    void givenNotExposedConfigurer_whenConfigurableComponentsConfigurer_thenDontExposeConfigurer() {
        assertNull(configurer.configurableComponentsConfigurer());
    }

    /**
     * Tests that when {@code exposedConfigurer} has been set,
     * {@code configurableComponentsConfigurer} returns a non-empty optional.
     */
    @Test
    void givenExposedConfigurer_whenConfigurableComponentsConfigurer_thenExposeConfigurer() {
        configurer.exposedConfigurer = mock(Configurer.class);
        assertNotNull(configurer.configurableComponentsConfigurer());
        assertSame(
                configurer.exposedConfigurer,
                configurer.configurableComponentsConfigurer());
    }

    /**
     * Tests that when {@code exposeConfigurer} annotation property is false,
     * {@code configurableComponentsPostProcessor()} creates a new one
     * but does not expose.
     */
    @Test
    void givenExposeConfigurerFalse_thenConfigurableComponentsPostProcessor_thenCreatesNewConfigurer() {
        configurer.annotationData = mock(AnnotationAttributes.class);
        given(configurer.annotationData.getBoolean("exposeConfigurer")).willReturn(false);
        configurer.springConfigProvider = springConfigProvider;
        final ConfigurableComponentsPostProcessor result = assertInstanceOf(
                ConfigurableComponentsPostProcessor.class,
                configurer.configurableComponentsPostProcessor());
        assertNotNull(result.getConfigurer());
        assertNull(configurer.exposedConfigurer);
    }

    /**
     * Tests that when {@code exposeConfigurer} annotation property is false,
     * {@code configurableComponentsPostProcessor()} creates a new one
     * and saves it to be exposed.
     */
    @Test
    void givenExposeConfigurerTrue_thenConfigurableComponentsPostProcessor_thenCreatesAndExposesNewConfigurer() {
        configurer.annotationData = mock(AnnotationAttributes.class);
        given(configurer.annotationData.getBoolean("exposeConfigurer")).willReturn(true);
        configurer.springConfigProvider = springConfigProvider;
        final ConfigurableComponentsPostProcessor result = assertInstanceOf(
                ConfigurableComponentsPostProcessor.class,
                configurer.configurableComponentsPostProcessor());
        assertNotNull(result.getConfigurer());
        assertSame(result.getConfigurer(), configurer.exposedConfigurer);
    }

    interface ExtConfig extends Config {
    }
}
