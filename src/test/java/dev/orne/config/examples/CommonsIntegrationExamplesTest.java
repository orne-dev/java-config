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

import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ConfigurationUtils;
import org.apache.commons.configuration2.ImmutableConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.Config;
import dev.orne.config.MutableConfig;
import dev.orne.config.commons.DelegatedOrneConfiguration;
import dev.orne.config.commons.DelegatedOrneMutableConfiguration;

/**
 * Integration tests of Apache Commons integration examples of public site.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @see https://orne-dev.github.io/java-config/commons.html
 */
@Tag("ut")
class CommonsIntegrationExamplesTest {

    /**
     * Example of using Config instance as Apache Commons
     * {@code ImmutableConfiguration}.
     */
    @Test
    void exampleOfConfigAsApacheImmutableConfiguration() {
        final Map<String, String> map = new HashMap<>();
        map.put("host", "www.example.org");
        map.put("port", "8080");
        map.put("enabled", "true");
        map.put("timeout", "5000");

        final Config config = map::get;

        final ImmutableConfiguration apacheConfig =
                new DelegatedOrneConfiguration(config);
        
        assertEquals("www.example.org", apacheConfig.getString("host"));
        assertEquals(8080, apacheConfig.getInt("port"));
        assertTrue(apacheConfig.getBoolean("enabled"));
        assertEquals(5000L, apacheConfig.getLong("timeout"));

        assertEquals("localhost", apacheConfig.getString("missing.host", "localhost"));
        assertEquals(8080, apacheConfig.getInt("missing.port", 8080));
        assertTrue(apacheConfig.getBoolean("missing.enabled", true));
        assertEquals(3000L, apacheConfig.getLong("missing.timeout", 3000L));
    }

    /**
     * Example of using MutableConfig instance as Apache Commons
     * {@code ImmutableConfiguration}.
     */
    @Test
    void exampleOfMutableConfigAsApacheConfiguration() {
        final Map<String, String> map = new HashMap<>();
        map.put("host", "www.example.org");
        map.put("port", "8080");
        map.put("enabled", "true");
        map.put("timeout", "5000");

        final MutableConfig config = Config.fromPropertiesFiles()
                .add(map)
                .mutable()
                .build();

        final Configuration apacheConfig =
                new DelegatedOrneMutableConfiguration(config);

        assertEquals("www.example.org", apacheConfig.getString("host"));
        assertEquals(8080, apacheConfig.getInt("port"));
        assertTrue(apacheConfig.getBoolean("enabled"));
        assertEquals(5000L, apacheConfig.getLong("timeout"));

        assertEquals("localhost", apacheConfig.getString("missing.host", "localhost"));
        assertEquals(8080, apacheConfig.getInt("missing.port", 8080));
        assertTrue(apacheConfig.getBoolean("missing.enabled", true));
        assertEquals(3000L, apacheConfig.getLong("missing.timeout", 3000L));

        apacheConfig.setProperty("host", "www.changed.org");
        assertEquals("www.changed.org", config.get("host"));
        apacheConfig.clearProperty("host");
        assertNull(config.get("host"));
    }

    /**
     * Example of using Apache Commons {@code ImmutableConfiguration}
     * as `Config` source.
     */
    @Test
    void exampleOfApacheImmutableConfigurationAsConfig() {
        final BaseConfiguration baseConfig = new BaseConfiguration();
        baseConfig.addProperty("host", "www.example.org");
        baseConfig.addProperty("port", 8080);
        baseConfig.addProperty("enabled", true);
        baseConfig.addProperty("timeout", 5000L);

        final ImmutableConfiguration apacheConfig = ConfigurationUtils.unmodifiableConfiguration(baseConfig);

        final Config config = Config.fromApacheCommons()
                .ofDelegate(apacheConfig)
                .build();

        assertEquals("www.example.org", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertTrue(config.getBoolean("enabled"));
        assertEquals(5000L, config.getLong("timeout"));
    }

    /**
     * Example of using Apache Commons {@code Configuration}
     * as `MutableConfig` source.
     */
    @Test
    void exampleOfApacheConfigurationAsMutableConfig() {
        final BaseConfiguration apacheConfig = new BaseConfiguration();
        apacheConfig.addProperty("host", "www.example.org");
        apacheConfig.addProperty("port", 8080);
        apacheConfig.addProperty("enabled", true);
        apacheConfig.addProperty("timeout", 5000L);

        final MutableConfig config = Config.fromApacheCommons()
                .mutable()
                .ofDelegate(apacheConfig)
                .build();

        assertEquals("www.example.org", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertTrue(config.getBoolean("enabled"));
        assertEquals(5000L, config.getLong("timeout"));
        config.set("host", "www.changed.org");
        assertEquals("www.changed.org", config.get("host"));
        assertEquals("www.changed.org", apacheConfig.getString("host"));
        config.remove("host");
        assertNull(config.get("host"));
        assertNull(apacheConfig.getString("host"));
    }
}
