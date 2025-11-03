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

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.Config;
import dev.orne.config.MutableConfig;
import dev.orne.config.WatchableConfig;

/**
 * Unit tests for {@code ConfigSubset}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0
 */
@Tag("ut")
class ConfigSubsetTest {

    /**
     * Test method for {@link Config#subset(String)}.
     */
    @Test
    void testSubset() {
        final HashMap<String, String> values = new HashMap<>();
        values.put("db.host", "localhost");
        values.put("db.port", "5432");
        values.put("db.user", "admin");
        final Config config = Config.fromProperties()
                .add(values)
                .build();
        final Config dbConfig = config.subset("db.");
        assertFalse(dbConfig.isEmpty());
        assertEquals(
                Set.of("host", "port", "user"),
                dbConfig.getKeys().collect(Collectors.toSet()));
        assertEquals(
                Set.of("port"),
                dbConfig.getKeys("po").collect(Collectors.toSet()));
        assertEquals(
                Set.of("user"),
                dbConfig.getKeys(key -> key.endsWith("er")).collect(Collectors.toSet()));
        assertEquals("localhost", dbConfig.get("host"));
        assertEquals(5432, dbConfig.getInteger("port"));
        assertEquals("admin", dbConfig.get("user"));
        final DatabaseConfig dbSubConfig = dbConfig.as(DatabaseConfig.class);
        assertEquals("localhost", dbSubConfig.getHost());
        assertEquals(5432, dbSubConfig.getPort());
        assertEquals("admin", dbSubConfig.getUser());
    }

    /**
     * Test method for chained {@link Config#subset(String)}
     * management.
     */
    @Test
    void testNestedSubset() {
        final HashMap<String, String> values = new HashMap<>();
        values.put("service.name", "myService");
        values.put("service.db.host", "localhost");
        values.put("service.db.port", "5432");
        values.put("service.db.user", "admin");
        final Config config = Config.fromProperties()
                .add(values)
                .build();
        final Config serviceConfig = config.subset("service.");
        assertFalse(serviceConfig.isEmpty());
        assertEquals(
                Set.of("name", "db.host", "db.port", "db.user"),
                serviceConfig.getKeys().collect(Collectors.toSet()));
        final Config dbConfig = serviceConfig.subset("db.");
        assertFalse(dbConfig.isEmpty());
        assertEquals(
                Set.of("host", "port", "user"),
                dbConfig.getKeys().collect(Collectors.toSet()));
        assertEquals("localhost", dbConfig.get("host"));
        assertEquals(5432, dbConfig.getInteger("port"));
        assertEquals("admin", dbConfig.get("user"));
        final DatabaseConfig dbSubConfig = dbConfig.as(DatabaseConfig.class);
        assertEquals("localhost", dbSubConfig.getHost());
        assertEquals(5432, dbSubConfig.getPort());
        assertEquals("admin", dbSubConfig.getUser());
    }

    /**
     * Test method for {@link MutableConfig#subset(String)}.
     */
    @Test
    void testMutableSubset() {
        final HashMap<String, String> values = new HashMap<>();
        values.put("db.host", "localhost");
        values.put("db.port", "5432");
        values.put("db.user", "admin");
        final MutableConfig config = new MutableConfig() {
            @Override
            public @NotNull Stream<String> getKeys() {
                return values.keySet().stream();
            }
            @Override
            public String get(@NotBlank String key) {
                return values.get(key);
            }
            @Override
            public void set(@NotBlank String key, String value) {
                values.put(key, value);
            }
            @Override
            public void remove(@NotBlank String... keys) {
                for (String key : keys) {
                    values.remove(key);
                }
            }
        };
        final MutableConfig dbConfig = config.subset("db.");
        assertEquals("localhost", dbConfig.get("host"));
        assertEquals(5432, dbConfig.getInteger("port"));
        assertEquals("admin", dbConfig.get("user"));
        final DatabaseConfig dbSubConfig = dbConfig.as(DatabaseConfig.class);
        assertEquals("localhost", dbSubConfig.getHost());
        assertEquals(5432, dbSubConfig.getPort());
        assertEquals("admin", dbSubConfig.getUser());
        dbConfig.set("host", "changedhost");
        assertEquals("changedhost", config.get("db.host"));
        assertEquals("changedhost", dbSubConfig.getHost());
    }

    /**
     * Test method for chained {@link MutableConfig#subset(String)}
     * management.
     */
    @Test
    void testMutableNestedSubset() {
        final HashMap<String, String> values = new HashMap<>();
        values.put("service.name", "myService");
        values.put("service.db.host", "localhost");
        values.put("service.db.port", "5432");
        values.put("service.db.user", "admin");
        final MutableConfig config = new MutableConfig() {
            @Override
            public @NotNull Stream<String> getKeys() {
                return values.keySet().stream();
            }
            @Override
            public String get(@NotBlank String key) {
                return values.get(key);
            }
            @Override
            public void set(@NotBlank String key, String value) {
                values.put(key, value);
            }
            @Override
            public void remove(@NotBlank String... keys) {
                for (String key : keys) {
                    values.remove(key);
                }
            }
        };
        final MutableConfig serviceConfig = config.subset("service.");
        assertFalse(serviceConfig.isEmpty());
        assertEquals(
                Set.of("name", "db.host", "db.port", "db.user"),
                serviceConfig.getKeys().collect(Collectors.toSet()));
        final MutableConfig dbConfig = serviceConfig.subset("db.");
        assertFalse(dbConfig.isEmpty());
        assertEquals(
                Set.of("host", "port", "user"),
                dbConfig.getKeys().collect(Collectors.toSet()));
        assertEquals("localhost", dbConfig.get("host"));
        assertEquals(5432, dbConfig.getInteger("port"));
        assertEquals("admin", dbConfig.get("user"));
        final DatabaseConfig dbSubConfig = dbConfig.as(DatabaseConfig.class);
        assertEquals("localhost", dbSubConfig.getHost());
        assertEquals(5432, dbSubConfig.getPort());
        assertEquals("admin", dbSubConfig.getUser());
        config.set("service.name", "NewName");
        dbConfig.set("user", "NewUser");
        assertEquals("NewName", serviceConfig.get("name"));
        assertEquals("NewUser", serviceConfig.get("db.user"));
        assertEquals("NewUser", dbConfig.get("user"));
        assertEquals("NewUser", dbSubConfig.getUser());
    }

    /**
     * Test method for {@link WatchabeConfig#subset(String)}.
     */
    @Test
    void testWatchabeSubset() {
        final HashMap<String, String> values = new HashMap<>();
        values.put("service.url", "http://example.com");
        values.put("service.timeout", "5000");
        final WatchableConfig config = Config.fromProperties()
                .add(values)
                .mutable()
                .build();
        final HashSet<String> changedProperties = new HashSet<>();
        config.addListener((cfg, props) -> {
            assertEquals(config, cfg);
            changedProperties.addAll(props);
        });
        final WatchableConfig serviceConfig = config.subset("service.");
        final HashSet<String> changedSubsetProperties = new HashSet<>();
        final WatchableConfig.Listener serviceListener = (cfg, props) -> {
            assertEquals(serviceConfig, cfg);
            changedSubsetProperties.addAll(props);
        };
        serviceConfig.addListener(serviceListener);
        assertEquals("http://example.com", serviceConfig.get("url"));
        assertEquals(5000, serviceConfig.getInteger("timeout"));
        serviceConfig.set("url", "http://changed.com");
        config.set("service.timeout", "6000");
        config.set("debug", true);
        // Verify that both listeners are notified
        await().atMost(Duration.ofSeconds(1)).until(
                () -> changedProperties.size() >= 3 && changedSubsetProperties.size() >= 2);
        assertTrue(changedProperties.contains("service.url"));
        assertTrue(changedProperties.contains("service.timeout"));
        assertTrue(changedProperties.contains("debug"));
        assertTrue(changedSubsetProperties.contains("url"));
        assertTrue(changedSubsetProperties.contains("timeout"));
        changedProperties.clear();
        changedSubsetProperties.clear();
        serviceConfig.removeListener(serviceListener);
        config.set("service.timeout", "5000");
        // Verify that only the main listener is notified
        await().atMost(Duration.ofSeconds(1)).until(
                () -> changedProperties.size() >= 1 && changedSubsetProperties.isEmpty());
        assertTrue(changedProperties.contains("service.timeout"));
        assertTrue(changedSubsetProperties.isEmpty());
    }

    /**
     * Test method for {@link WatchabeConfig#subset(String)}.
     */
    @Test
    void testWatchabeNestedSubset() {
        final HashMap<String, String> values = new HashMap<>();
        values.put("service.url", "http://example.com");
        values.put("service.timeout", "5000");
        values.put("service.db.host", "localhost");
        values.put("service.db.port", "5432");
        values.put("service.db.user", "admin");
        final WatchableConfig config = Config.fromProperties()
                .add(values)
                .mutable()
                .build();
        final HashSet<String> changedProperties = new HashSet<>();
        config.addListener((cfg, props) -> {
            assertEquals(config, cfg);
            changedProperties.addAll(props);
        });
        final WatchableConfig serviceConfig = config.subset("service.");
        final HashSet<String> changedServiceProperties = new HashSet<>();
        serviceConfig.addListener((cfg, props) -> {
            assertEquals(serviceConfig, cfg);
            changedServiceProperties.addAll(props);
        });
        final WatchableConfig dbConfig = serviceConfig.subset("db.");
        final HashSet<String> changedDbProperties = new HashSet<>();
        dbConfig.addListener((cfg, props) -> {
            assertEquals(dbConfig, cfg);
            changedDbProperties.addAll(props);
        });
        config.set("service.timeout", "6000");
        serviceConfig.set("url", "http://changed.com");
        dbConfig.set("user", "changed");
        // Verify that all listeners are notified
        await().atMost(Duration.ofSeconds(1)).until(
                () -> changedProperties.size() >= 3
                        && changedServiceProperties.size() >= 3
                        && changedDbProperties.size() >= 1);
        assertTrue(changedProperties.contains("service.url"));
        assertTrue(changedProperties.contains("service.timeout"));
        assertTrue(changedProperties.contains("service.db.user"));
        assertTrue(changedServiceProperties.contains("url"));
        assertTrue(changedServiceProperties.contains("timeout"));
        assertTrue(changedServiceProperties.contains("db.user"));
        assertTrue(changedDbProperties.contains("user"));
    }

    /**
     * Test method for {@link Config#as(Config, Class)}.
     */
    @Test
    void testHashCodeEqualToString() {
        final HashMap<String, String> values = new HashMap<>();
        values.put("db.host", "localhost");
        values.put("db.port", "5432");
        values.put("db.user", "admin");
        final Config config = Config.fromProperties()
                .add(values)
                .build();
        final Config proxy = config.subset("db.");
        assertFalse(proxy.equals(null));
        assertTrue(proxy.equals(proxy));
        assertTrue(proxy.equals(proxy));
        final Config equalProxy = config.subset("db.");
        assertEquals(proxy.hashCode(), equalProxy.hashCode());
        assertEquals(proxy.toString(), equalProxy.toString());
        assertEquals(proxy, equalProxy);
        final Config altPrefixProxy = config.subset("alt.");
        assertNotEquals(proxy.hashCode(), altPrefixProxy.hashCode());
        assertNotEquals(proxy.toString(), altPrefixProxy.toString());
        assertNotEquals(proxy, altPrefixProxy);
        final HashMap<String, String> altValues = new HashMap<>();
        altValues.put("db.host", "example.org");
        altValues.put("db.port", "5432");
        altValues.put("db.user", "admin");
        final Config altConfig = Config.fromProperties()
                .add(altValues)
                .build();
        final Config altProxy = altConfig.subset("db.");
        assertNotEquals(proxy.hashCode(), altProxy.hashCode());
        assertNotEquals(proxy.toString(), altProxy.toString());
        assertNotEquals(proxy, altProxy);
    }

    interface DatabaseConfig extends Config {
        default String getHost() {
            return get("host");
        }
        default int getPort() {
            return getInteger("port");
        }
        default String getUser() {
            return get("user");
        }
    }
}
