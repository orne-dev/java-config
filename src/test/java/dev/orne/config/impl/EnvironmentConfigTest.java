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

import java.security.Permission;
import java.util.Collections;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.mockito.Mock;

import dev.orne.config.ConfigBuilder;
import dev.orne.config.ConfigException;
import dev.orne.config.ValueDecoder;
import dev.orne.config.ValueDecorator;

/**
 * Unit tests for {@link EnvironmentConfigImpl}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class EnvironmentConfigTest
extends AbstractConfigTest {

    private @Mock ValueDecoder mockDecoder;
    private @Mock ValueDecorator mockDecorator;
    private @Mock Map<String, String> mockValues;

    /**
     * {@{inheritDoc}
     */
    @Override
    protected ConfigBuilder<?> createBuilder(
            final @NotNull Map<String, String> properties) {
        final Map<String, String> values = Collections.unmodifiableMap(properties);
        assertInstanceOf(
                EnvironmentConfigBuilderImpl.class,
                ConfigBuilder.fromEnvironmentVariables());
        return new MockBuilder(values);
    }

    /**
     * Test method for {@link EnvironmentConfigImpl#getEnvironmentVariables()}
     * real result.
     */
    @Test
    @DisabledForJreRange(min = JRE.JAVA_17)
    void testGetEnvironmentVariables() {
        final EnvironmentConfigImpl config = assertInstanceOf(
                EnvironmentConfigImpl.class,
                ConfigBuilder.fromEnvironmentVariables()
                    .build());
        assertSame(System.getenv(), config.getEnvironmentVariables());
    }

    /**
     * Test method for {@link EnvironmentConfigImpl#getEnvironmentVariables()} when
     * {@code SecurityException} is thrown by {@code System.getenv()} and
     * {@code System.getenv(String)}.
     */
    @Test
    @DisabledForJreRange(min = JRE.JAVA_17)
    void testGetEnvironmentVariablesSecurityException() {
        final EnvironmentConfigImpl config = assertInstanceOf(
                EnvironmentConfigImpl.class,
                ConfigBuilder.fromEnvironmentVariables()
                    .build());
        assertSame(System.getenv(), config.getEnvironmentVariables());
        final SecurityManager sm = new SecurityManager() {
            @Override
            public void checkPermission(Permission perm) {
                if (perm.getName().startsWith("getenv.")) {
                    
                    throw new SecurityException("Mock exception");
                } else if (!"setSecurityManager".equals(perm.getName())) {
                    super.checkPermission(perm);
                } 
            }
            
        };
        try {
            System.setSecurityManager(sm);
            assertThrows(ConfigException.class, config::getEnvironmentVariables);
        } finally {
            System.setSecurityManager(null);
        }
    }

    /**
     * Extension of {@link EnvironmentConfigImpl} to mock the environment
     * variables, as cannot be modified in the test environment.
     */
    static class MockConfig extends EnvironmentConfigImpl {

        /** The test environment variables. */
        private final Map<String, String> mockValues;

        /**
         * Creates a new instance.
         * 
         * @param options The configuration builder options.
         * @param values The test environment variables.
         */
        public MockConfig(
                final ConfigOptions options,
                final @NotNull Map<String, String> values) {
            super(options);
            this.mockValues = values;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected @NotNull Map<String, String> getEnvironmentVariables() {
            return this.mockValues;
        }
    }

    /**
     * Extension of {@link EnvironmentConfigBuilderImpl} to mock the environment
     * variables, as cannot be modified in the test environment.
     */
    static class MockBuilder extends EnvironmentConfigBuilderImpl {

        /** The test environment variables. */
        private final Map<String, String> mockValues;

        /**
         * Creates a new instance.
         * 
         * @param values The test environment variables.
         */
        public MockBuilder(
                final @NotNull Map<String, String> values) {
            super();
            this.mockValues = values;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull EnvironmentConfigImpl build() {
            return new MockConfig(this.options, this.mockValues);
        }
    }
}
