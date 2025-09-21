package dev.orne.config.impl;

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

import java.util.Map;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import dev.orne.config.ConfigBuilder;
import dev.orne.config.ConfigException;
import dev.orne.config.NonIterableConfigException;
import dev.orne.config.SpringEnvironmentConfigBuilder;

/**
 * Unit tests for {@code SpringEnvironmentConfigBuilderImpl} and
 * {@code SpringEnvironmentConfigImpl}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0
 */
@Tag("ut")
class SpringEnvironmentConfigTest
extends AbstractConfigTest {

    private @Mock ConfigurableEnvironment environment;
    private @Mock MutablePropertySources sources;
    private @Mock EnumerablePropertySource<?> source;

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConfigBuilder<?> createBuilder(
            final @NotNull Map<String, String> properties) {
        given(environment.containsProperty(any())).willAnswer(invocation -> {
            final String key = invocation.getArgument(0);
            return properties.containsKey(key);
        });
        given(environment.getProperty(any())).willAnswer(invocation -> {
            final String key = invocation.getArgument(0);
            return properties.get(key);
        });
        given(environment.getPropertySources()).willReturn(sources);
        given(sources.stream()).willAnswer(invocation -> Stream.of(source));
        given(source.getPropertyNames()).willReturn(properties.keySet().toArray(String[]::new));
        return ConfigBuilder.fromSpringEnvironment()
                .ofEnvironment(environment)
                .withIterableKeys();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isIterable() {
        return true;
    }

    /**
     * Tests instance building from Spring environment.
     */
    @Test
    void testEnvironmentBuilder() {
        final SpringEnvironmentConfigImpl config = assertInstanceOf(
                SpringEnvironmentConfigImpl.class,
                ConfigBuilder.fromSpringEnvironment()
                    .ofEnvironment(environment)
                    .build());
        assertSame(environment, config.getEnvironment());
    }

    /**
     * Test for exception when trying to enable property keys iteration
     * with a non {@code ConfigurableEnvironment} environment.
     */
    @Test
    void givenNonConfigurableEnvironment_whenWithIterableKeys_thenThrowsException() {
        final Environment env = mock(Environment.class);
        final SpringEnvironmentConfigBuilder<?> builder = ConfigBuilder.fromSpringEnvironment()
                    .ofEnvironment(env);
        assertThrows(ConfigException.class, builder::withIterableKeys);
    }

    /**
     * Test for exception when trying to get property keys from a
     * non-iterable configuration.
     */
    @Test
    void givenNonIterableConfig_whenGetKeys_thenThrowsException() {
        final SpringEnvironmentConfigImpl config = assertInstanceOf(
                SpringEnvironmentConfigImpl.class,
                ConfigBuilder.fromSpringEnvironment()
                    .ofEnvironment(environment)
                    .build());
        assertThrows(NonIterableConfigException.class, config::getKeys);
    }
}
