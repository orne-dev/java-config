package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
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

import java.security.Permission;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/**
 * Unit tests for {@code SystemConfig}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class SystemConfigTest
extends AbstractConfigTest {

    private static String propOriginalValue;
    private static String derivedPropOriginalValue;

    private @Mock ValueDecoder mockDecoder;
    private @Mock ValueDecorator mockDecorator;
    private @Mock Properties mockProperties;

    /**
     * Saves the value of the test property.
     */
    @BeforeAll
    static void backupSystemPropertyValue() {
        propOriginalValue = System.getProperty(TEST_KEY);
        derivedPropOriginalValue = System.getProperty(TEST_DERIVED_KEY);
    }

    /**
     * Restores the original value of the test property or
     * removes it if not set.
     */
    @AfterAll
    static void restoreSystemPropertyValue() {
        if (propOriginalValue == null) {
            System.clearProperty(TEST_KEY);
        } else {
            System.setProperty(TEST_KEY, propOriginalValue);
        }
        if (derivedPropOriginalValue == null) {
            System.clearProperty(TEST_DERIVED_KEY);
        } else {
            System.setProperty(TEST_DERIVED_KEY, derivedPropOriginalValue);
        }
    }

    @Override
    protected ConfigBuilder createBuilder(
            final @NotNull Map<String, String> properties) {
        System.getProperties().putAll(properties);
        return Config.ofSystemProperties();
    }

    /**
     * Tests empty instance building.
     */
    @Test
    void testBuilder() {
        SystemConfig config = Config.ofSystemProperties()
                .build();
        assertNotNull(config);
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertSame(System.getProperties(), config.getSystemProperties());
    }

    /**
     * Test method for {@link SystemConfig#SystemConfig(Config, ValueDecoder, ValueDecorator).
     */
    @Test
    void testComponentsConstructor() {
        assertThrows(NullPointerException.class, () -> {
            new SystemConfig(null, null, null);
        });
        assertThrows(NullPointerException.class, () -> {
            new SystemConfig(mockParent, null, mockDecorator);
        });
        assertThrows(NullPointerException.class, () -> {
            new SystemConfig(mockParent, mockDecoder, null);
        });
        assertDoesNotThrow(() -> {
            new SystemConfig(null, mockDecoder, mockDecorator);
        });
        
        final SystemConfig config = new SystemConfig(mockParent, mockDecoder, mockDecorator);
        assertSame(mockParent, config.getParent());
        assertSame(mockDecoder, config.getDecoder());
        assertSame(mockDecorator, config.getDecorator());
    }

    /**
     * Test method for {@link SystemConfig#getSystemProperties()} when
     * {@code SecurityException} is thrown by {@code System.getProperty()}.
     */
    @Test
    void testGetSystemPropertiesSecurityException() {
        final SystemConfig config = Config.ofSystemProperties()
                .build();
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

    /**
     * Test method for {@link SystemConfig#isEmptyInt()}.
     */
    @Test
    void testIsEmptyInt() {
        final SystemConfig config = spy(Config.ofSystemProperties()
                .build());
        given(config.getSystemProperties()).willReturn(mockProperties);
        given(mockProperties.isEmpty()).willReturn(true);
        
        final boolean result = config.isEmptyInt();
        assertTrue(result);
        
        then(mockProperties).should().isEmpty();
        then(mockProperties).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link SystemConfig#getKeysInt()}.
     */
    @Test
    void testGetKeysInt() {
        final SystemConfig config = spy(Config.ofSystemProperties()
                .build());
        given(config.getSystemProperties()).willReturn(mockProperties);
        @SuppressWarnings("unchecked")
        final Set<String> mockKeys = mock(Set.class);
        @SuppressWarnings("unchecked")
        final Stream<String> mockKeysStream = mock(Stream.class);
        given(mockProperties.stringPropertyNames()).willReturn(mockKeys);
        given(mockKeys.stream()).willReturn(mockKeysStream);
        
        final Stream<String> result = config.getKeysInt();
        assertSame(mockKeysStream, result);
        
        then(mockProperties).should().stringPropertyNames();
        then(mockProperties).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link SystemConfig#containsInt(String)} with
     * non existent property.
     */
    @Test
    void testContainsInt() {
        final SystemConfig config = spy(Config.ofSystemProperties()
                .build());
        given(config.getSystemProperties()).willReturn(mockProperties);
        given(mockProperties.containsKey(TEST_KEY)).willReturn(true);
        
        final boolean result = config.containsInt(TEST_KEY);
        assertTrue(result);
        
        then(mockProperties).should().containsKey(TEST_KEY);
        then(mockProperties).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link SystemConfig#getInt(String)} with
     * non existent property.
     */
    @Test
    void testGetInt() {
        final SystemConfig config = spy(Config.ofSystemProperties()
                .build());
        given(config.getSystemProperties()).willReturn(mockProperties);
        final String testValue = "mockValue";
        given(mockProperties.getProperty(TEST_KEY)).willReturn(testValue);
        
        final String result = config.getInt(TEST_KEY);
        assertSame(testValue, result);
        
        then(mockProperties).should().getProperty(TEST_KEY);
        then(mockProperties).shouldHaveNoMoreInteractions();
    }
}
