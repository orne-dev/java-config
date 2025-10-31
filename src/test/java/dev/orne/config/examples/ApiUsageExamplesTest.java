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

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.Config;

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
     * Example of using Config interface as a functional interface.
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
