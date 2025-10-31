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
import static org.awaitility.Awaitility.*;
import static org.mockito.BDDMockito.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.config.Config;
import dev.orne.config.ConfigException;
import dev.orne.config.MutableConfig;
import dev.orne.config.WatchableConfig;

/**
 * Unit tests for {@code ConfigSubtype}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0
 */
@Tag("ut")
class ConfigSubtypeTest {

    private static final String VALUE_PROP = "test.value";
    private static final String INT_VALUE_PROP = "test.value.int";

    protected @Mock WatchableConfig.Listener mockListener;
    protected final Duration maxDelay = Duration.ofMillis(1000);
    protected @Captor ArgumentCaptor<Set<String>> changedPropertiesCaptor;

    /**
     * Initializes the mocks used in the tests.
     */
    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test method for {@link Config#as(Config, Class)}.
     */
    @Test
    void testSimpleProxy() {
        final Properties properties = new Properties();
        properties.setProperty(VALUE_PROP, "testValue");
        properties.setProperty(INT_VALUE_PROP, "5");
        final Config config = Config.fromPropertiesFiles()
                .add(properties)
                .build();
        assertThrows(NullPointerException.class, () -> {
            Config.as(null, null);
        });
        assertThrows(NullPointerException.class, () -> {
            Config.as(null, ConfigSubtype.class);
        });
        assertThrows(NullPointerException.class, () -> {
            Config.as(config, null);
        });
        final ConfigSubtype configProxy = Config.as(config, ConfigSubtype.class);
        assertNotNull(configProxy);
        assertFalse(configProxy.isEmpty());
        assertTrue(configProxy.contains(VALUE_PROP));
        assertTrue(configProxy.contains(INT_VALUE_PROP));
        assertEquals("testValue", configProxy.getValue());
        assertEquals(5, configProxy.getInteger(INT_VALUE_PROP));
    }

    /**
     * Test method for {@link Config#as(Config, Class)}.
     */
    @Test
    void testNonInterfaceProxy() {
        final Properties properties = new Properties();
        properties.setProperty(VALUE_PROP, "testValue");
        properties.setProperty(INT_VALUE_PROP, "5");
        final Config config = Config.fromPropertiesFiles()
                .add(properties)
                .build();
        final Class<? extends Config> nonInterfaceType = mock(Config.class).getClass();
        assertThrows(ConfigException.class, () -> {
            Config.as(config, nonInterfaceType);
        });
    }

    /**
     * Test method for {@link Config#as(Config, Class)}.
     */
    @Test
    void testIncompleteProxy() {
        final Properties properties = new Properties();
        properties.setProperty(VALUE_PROP, "testValue");
        properties.setProperty(INT_VALUE_PROP, "5");
        final Config config = Config.fromPropertiesFiles()
                .add(properties)
                .build();
        assertThrows(ConfigException.class, () -> {
            Config.as(config, IncompleteConfigSubtype.class);
        });
    }

    /**
     * Test method for {@link Config#as(Config, Class)}.
     */
    @Test
    void testExtendedInterface() {
        final Properties properties = new Properties();
        properties.setProperty(VALUE_PROP, "testValue");
        properties.setProperty(INT_VALUE_PROP, "5");
        final Config config = Config.fromPropertiesFiles()
                .add(properties)
                .build();
        final ConfigExtraSubtype configProxy = Config.as(config, ConfigExtraSubtype.class);
        assertNotNull(configProxy);
        assertFalse(configProxy.isEmpty());
        assertTrue(configProxy.contains(VALUE_PROP));
        assertTrue(configProxy.contains(INT_VALUE_PROP));
        assertEquals("testValue", configProxy.getValue());
        assertEquals(5, configProxy.getIntValue());
    }

    /**
     * Test method for {@link Config#as(Config, Class)}.
     */
    @Test
    void testMutableInterface() {
        final Properties properties = new Properties();
        properties.setProperty(VALUE_PROP, "testValue");
        properties.setProperty(INT_VALUE_PROP, "5");
        final MutableConfig config = Config.fromPropertiesFiles()
                .add(properties)
                .mutable()
                .build();
        final MutableConfigSubtype configProxy = Config.as(config, MutableConfigSubtype.class);
        assertNotNull(configProxy);
        assertFalse(configProxy.isEmpty());
        assertTrue(configProxy.contains(VALUE_PROP));
        assertTrue(configProxy.contains(INT_VALUE_PROP));
        assertEquals("testValue", configProxy.getValue());
        assertEquals(5, configProxy.getInteger(INT_VALUE_PROP));
        configProxy.setValue("newValue");
        assertEquals("newValue", configProxy.getValue());
    }

    /**
     * Test method for {@link Config#as(Config, Class)}.
     */
    @Test
    void testMutableInvalidInstance() {
        final Properties properties = new Properties();
        properties.setProperty(VALUE_PROP, "testValue");
        properties.setProperty(INT_VALUE_PROP, "5");
        final Config config = Config.fromPropertiesFiles()
                .add(properties)
                .build();
        assertThrows(ConfigException.class, () -> {
            Config.as(config, MutableConfigSubtype.class);
        });
    }

    /**
     * Test method for {@link Config#as(Config, Class)}.
     */
    @Test
    void testWatchableInterface() {
        final Properties properties = new Properties();
        properties.setProperty(VALUE_PROP, "testValue");
        properties.setProperty(INT_VALUE_PROP, "5");
        final WatchableConfig config = Config.fromPropertiesFiles()
                .add(properties)
                .mutable()
                .build();
        final WatchableConfigSubtype configProxy = Config.as(config, WatchableConfigSubtype.class);
        assertNotNull(configProxy);
        assertFalse(configProxy.isEmpty());
        assertTrue(configProxy.contains(VALUE_PROP));
        assertTrue(configProxy.contains(INT_VALUE_PROP));
        assertEquals("testValue", configProxy.getValue());
        assertEquals(5, configProxy.getInteger(INT_VALUE_PROP));
        configProxy.addListener(mockListener);
        configProxy.setValue("newValue");
        await().atMost(maxDelay)
            .untilAsserted(() -> {
                then(mockListener).should(atLeastOnce()).configurationChanged(
                        same(config),
                        changedPropertiesCaptor.capture());
                final Set<String> props = changedPropertiesCaptor.getAllValues()
                        .stream()
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet());
                assertTrue(props.contains(VALUE_PROP));
            });
        assertEquals("newValue", configProxy.getValue());
        assertThrows(NumberFormatException.class, () -> {
            configProxy.getInteger(VALUE_PROP);
        });
    }

    /**
     * Test method for {@link Config#as(Config, Class)}.
     */
    @Test
    void testWatchableInvalidInstance() {
        final Properties properties = new Properties();
        properties.setProperty(VALUE_PROP, "testValue");
        properties.setProperty(INT_VALUE_PROP, "5");
        final Config config = Config.fromPropertiesFiles()
                .add(properties)
                .build();
        assertThrows(ConfigException.class, () -> {
            Config.as(config, WatchableConfigSubtype.class);
        });
    }

    /**
     * Test method for {@link Config#as(Config, Class)}.
     */
    @Test
    void testErrorHandling() {
        final Properties properties = new Properties();
        properties.setProperty(VALUE_PROP, "testValue");
        properties.setProperty(INT_VALUE_PROP, "5");
        final Config config = Config.fromPropertiesFiles()
                .add(properties)
                .build();
        final ExConfigSubtype configProxy = Config.as(config, ExConfigSubtype.class);
        assertThrows(CustomRuntimeException.class, configProxy::throwRuntime);
        assertThrows(CustomDeclaredException.class, configProxy::throwException);
        assertThrows(CustomError.class, configProxy::throwError);
    }

    /**
     * Test method for {@link Config#as(Config, Class)} when instance
     * is already of the requested type.
     */
    @Test
    void testProxyNotNeeded() {
        final HashMap<String, String> values = new HashMap<>();
        final ConfigSubtype config = values::get;
        assertSame(config, config.as(ConfigSubtype.class));
        assertSame(config, Config.as(config, ConfigSubtype.class));
    }

    /**
     * Test method for {@link Config#as(Config, Class)}.
     */
    @Test
    void testHashCodeEqualToString() {
        final Properties properties = new Properties();
        properties.setProperty(VALUE_PROP, "testValue");
        properties.setProperty(INT_VALUE_PROP, "5");
        final Config config = Config.fromPropertiesFiles()
                .add(properties)
                .build();
        final ConfigSubtype proxy = Config.as(config, ConfigSubtype.class);
        assertNotEquals(proxy, null);
        assertEquals(proxy, proxy);
        assertNotEquals(proxy, config);
        final ConfigSubtype equalProxy = Config.as(config, ConfigSubtype.class);
        assertEquals(proxy.hashCode(), equalProxy.hashCode());
        assertEquals(proxy.toString(), equalProxy.toString());
        assertEquals(proxy, equalProxy);
        final ConfigExtraSubtype extraProxy = Config.as(config, ConfigExtraSubtype.class);
        assertNotEquals(proxy.hashCode(), extraProxy.hashCode());
        assertNotEquals(proxy.toString(), extraProxy.toString());
        assertNotEquals(proxy, extraProxy);
        final Properties altProperties = new Properties();
        altProperties.setProperty(VALUE_PROP, "testValue");
        final Config altConfig = Config.fromPropertiesFiles()
                .add(altProperties)
                .build();
        final ConfigSubtype altProxy = Config.as(altConfig, ConfigSubtype.class);
        assertNotEquals(proxy.hashCode(), altProxy.hashCode());
        assertNotEquals(proxy.toString(), altProxy.toString());
        assertNotEquals(proxy, altProxy);
    }

    interface ConfigSubtype extends Config {
        static ConfigSubtype staticMethod() {
            throw new UnsupportedOperationException(
                    "Unimplemented config subtype static method.");
        }
        default String getValue() {
            return get(VALUE_PROP);
        }
    }

    interface IncompleteConfigSubtype extends Config {

        String unimplementedMethod();

        default String getValue() {
            return get(VALUE_PROP);
        }
    }

    interface ConfigExtraSubtype extends ConfigSubtype {
        default Integer getIntValue() {
            return getInteger(INT_VALUE_PROP);
        }
    }

    interface MutableConfigSubtype extends MutableConfig {
        default String getValue() {
            return get(VALUE_PROP);
        }
        default void setValue(String value) {
            set(VALUE_PROP, value);
        }
    }

    interface WatchableConfigSubtype extends WatchableConfig {
        default String getValue() {
            return get(VALUE_PROP);
        }
        default void setValue(String value) {
            set(VALUE_PROP, value);
        }
    }

    interface ExConfigSubtype extends Config {
        default String throwRuntime() {
            throw new CustomRuntimeException();
        }
        default String throwException()
        throws Exception {
            throw new CustomDeclaredException();
        }
        default String throwError() {
            throw new CustomError();
        }
    }

    static class CustomRuntimeException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        public CustomRuntimeException() {
            super();
        }
    }

    static class CustomDeclaredException extends Exception {
        private static final long serialVersionUID = 1L;
        public CustomDeclaredException() {
            super();
        }
    }

    static class CustomError extends Error {
        private static final long serialVersionUID = 1L;
        public CustomError() {
            super();
        }
    }
}
