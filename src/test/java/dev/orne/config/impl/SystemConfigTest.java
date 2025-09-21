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
import java.util.Map;
import java.util.Properties;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.AfterEach;
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
 * Unit tests for {@link SystemConfigImpl}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class SystemConfigTest
extends AbstractConfigTest {

    private @Mock ValueDecoder mockDecoder;
    private @Mock ValueDecorator mockDecorator;
    private @Mock Properties mockProperties;

    /**
     * Restores the original value of the test property or
     * removes it if not set.
     */
    @AfterEach
    void restoreSystemPropertyValue() {
        System.clearProperty(TEST_KEY);
        System.clearProperty(TEST_DERIVED_KEY);
        System.clearProperty(TEST_PARENT_KEY);
        System.clearProperty(TEST_PARENT_DERIVED_KEY);
    }

    /**
     * {@{inheritDoc}
     */
    @Override
    protected ConfigBuilder<?> createBuilder(
            final @NotNull Map<String, String> properties) {
        System.getProperties().putAll(properties);
        return ConfigBuilder.fromSystemProperties();
    }

    /**
     * Test method for {@link SystemConfigImpl#getSystemProperties()} when
     * {@code SecurityException} is thrown by {@code System.getProperties()}
     * and {@code System.getProperty()}.
     */
    @Test
    @DisabledForJreRange(min = JRE.JAVA_17)
    void testGetSystemPropertiesSecurityException() {
        final SystemConfigImpl config = assertInstanceOf(
                SystemConfigImpl.class,
                ConfigBuilder.fromSystemProperties()
                    .build());
        assertSame(System.getProperties(), config.getSystemProperties());
        final SecurityManager sm = new SecurityManager() {
            @Override
            public void checkPropertiesAccess() {
                throw new SecurityException("Mock exception");
            }
            @Override
            public void checkPermission(Permission perm) {
                if (!"setSecurityManager".equals(perm.getName())) {
                    super.checkPermission(perm);
                }
            }
            
        };
        System.setSecurityManager(sm);
        try {
            assertThrows(ConfigException.class, config::getSystemProperties);
        } finally {
            System.setSecurityManager(null);
        }
    }
}
