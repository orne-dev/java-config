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

import java.util.Optional;

/**
 * Unit tests for {@link ConfigurableComponentsConfigurer}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
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
     * Tests that when {@code exposeConfigurer} annotation property is false,
     * {@code configurableComponentsConfigurer} returns an empty optional.
     */
    @Test
    void givenExposeConfigurerFalse_whenConfigurableComponentsConfigurer_thenDontExposeConfigurer() {
        configurer.annotationData = mock(AnnotationAttributes.class);
        configurer.springConfigProvider = springConfigProvider;
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
        configurer.springConfigProvider = springConfigProvider;
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
        configurer.springConfigProvider = springConfigProvider;
        final ConfigurableComponentsPostProcessor result = assertInstanceOf(
                ConfigurableComponentsPostProcessor.class,
                configurer.configurableComponentsPostProcessor(
                        Optional.of(customConfigurer)));
        assertSame(customConfigurer, result.getConfigurer());
    }

    /**
     * Tests that when no custom {@code Configurer} is provided,
     * {@code configurableComponentsPostProcessor()} creates a new one.
     */
    @Test
    void givenNoCustomConfigurer_thenConfigurableComponentsPostProcessor_thenCreateNewConfigurer() {
        configurer.annotationData = mock(AnnotationAttributes.class);
        configurer.springConfigProvider = springConfigProvider;
        final ConfigurableComponentsPostProcessor result = assertInstanceOf(
                ConfigurableComponentsPostProcessor.class,
                configurer.configurableComponentsPostProcessor(
                        Optional.empty()));
        assertNotNull(result.getConfigurer());
    }

    interface ExtConfig extends Config {
    }
}
