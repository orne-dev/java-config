package dev.orne.config.examples;

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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.Config;
import dev.orne.config.MutableConfig;
import dev.orne.config.WatchableConfig;

/**
 * Integration tests of {@link Config} API usage examples of public site.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @see https://orne-dev.github.io/java-config/config.html
 */
@Tag("ut")
class ApiUsageExamplesTest {

    /**
     * Example of {@code Config} usage as a functional interface.
     */
    @Test
    void exampleOfConfigUsage() {
        final Map<String, String> map = new HashMap<>();
        map.put("host", "www.example.org");
        map.put("port", "8080");
        map.put("enabled", "true");
        map.put("timeout", "5000");

        final Config config = map::get;

        assertEquals("www.example.org", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertTrue(config.getBoolean("enabled"));
        assertEquals(5000L, config.getLong("timeout"));

        assertEquals("localhost", config.get("missing.host", "localhost"));
        assertEquals(8080, config.getInteger("missing.port", 8080));
        assertTrue(config.getBoolean("missing.enabled", true));
        assertEquals(3000L, config.getLong("missing.timeout", 3000L));
    }

    /**
     * Example of {@code Config} key retrieval usage.
     */
    @Test
    void exampleOfConfigKeyUsage() {
        final Map<String, String> map = new HashMap<>();
        map.put("host", "www.example.org");
        map.put("port", "8080");
        map.put("db.url", "jdbc:mysql://localhost:3306/mydb");
        map.put("db.username", "user");
        map.put("db.password", "password");

        final Config config = Config.fromPropertiesFiles()
                .add(map)
                .build();

        assertFalse(config.isEmpty());
        assertEquals(
                Set.of("host", "port", "db.url", "db.username", "db.password"),
                config.getKeys().collect(Collectors.toSet()));
        assertEquals(
                Set.of("db.url", "db.username", "db.password"),
                config.getKeys("db.").collect(Collectors.toSet()));
        assertEquals(
                Set.of("port", "db.password"),
                config.getKeys(key -> key.contains("or")).collect(Collectors.toSet()));
    }

    /**
     * Example of {@code MutableConfig} usage.
     */
    @Test
    void exampleOfMutableConfigUsage() {
        final Map<String, String> map = new HashMap<>();
        map.put("obsolete.property", "olf.value");

        final MutableConfig config = Config.fromPropertiesFiles()
                .add(map)
                .mutable()
                .build();
        config.set("host", "localhost");
        config.set("port", 8080);
        config.set("enabled", true);
        config.set("timeout", 3000L);
        config.remove("obsolete.property");

        assertEquals("localhost", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertTrue(config.getBoolean("enabled"));
        assertEquals(3000L, config.getLong("timeout"));
        assertNull(config.get("obsolete.property"));
    }

    /**
     * Example of {@code WatchableConfig} usage.
     */
    @Test
    void exampleOfWatchableConfigUsage() {
        final Map<String, String> map = new HashMap<>();
        map.put("obsolete.property", "olf.value");

        final WatchableConfig config = Config.fromPropertiesFiles()
                .add(map)
                .mutable()
                .build();

        final HashSet<String> changedProperties = new HashSet<>();
        WatchableConfig.Listener listener = (instance, props) -> {
            changedProperties.addAll(props);
        };
        config.addListener(listener);

        config.set("host", "localhost");
        config.set("port", 8080);
        config.set("enabled", true);
        config.set("timeout", 3000L);
        config.remove("obsolete.property");

        await().atMost(Duration.ofSeconds(1)).untilAsserted(() -> {
            assertTrue(changedProperties.contains("host"));
            assertTrue(changedProperties.contains("port"));
            assertTrue(changedProperties.contains("enabled"));
            assertTrue(changedProperties.contains("timeout"));
            assertTrue(changedProperties.contains("obsolete.property"));
        });

        assertEquals("localhost", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertTrue(config.getBoolean("enabled"));
        assertEquals(3000L, config.getLong("timeout"));
        assertNull(config.get("obsolete.property"));
        
        changedProperties.clear();
        
        config.removeListener(listener);
        
        config.set("host", "changedhost");
        assertEquals("changedhost", config.get("host"));
        assertTrue(changedProperties.isEmpty());
    }

    /**
     * Example of usage of Config subtypes.
     */
    @Test
    void exampleOfSubtype() {
        final Map<String, String> map = new HashMap<>();
        map.put("db.url", "jdbc:mysql://localhost:3306/mydb");
        map.put("db.username", "user");
        map.put("db.password", "password");

        final Config config = map::get;
        final DatabaseConfig dbConfig = config.as(DatabaseConfig.class);
        assertEquals("jdbc:mysql://localhost:3306/mydb", dbConfig.getUrl());
        assertEquals("user", dbConfig.getUsername());
        assertEquals("password", dbConfig.getPassword());
    }

    /**
     * Example of usage of Config subsets.
     */
    @Test
    void exampleOfSubset() {
        final Map<String, String> map = new HashMap<>();
        map.put("security.db.url", "jdbc:mysql://localhost:3306/mydb");
        map.put("security.db.username", "user");
        map.put("security.db.password", "password");

        final Config config = map::get;
        final Config securityConfig = config.subset("security.");
        final DatabaseConfig dbConfig = securityConfig.as(DatabaseConfig.class);
        assertEquals("jdbc:mysql://localhost:3306/mydb", dbConfig.getUrl());
        assertEquals("user", dbConfig.getUsername());
        assertEquals("password", dbConfig.getPassword());
    }

    interface DatabaseConfig extends Config {
        default String getUrl() {
            return get("db.url");
        }
        default String getUsername() {
            return get("db.username");
        }
        default String getPassword() {
            return get("db.password");
        }
    }
}
