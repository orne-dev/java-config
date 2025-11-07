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

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.Config;
import dev.orne.config.ConfigProvider;
import dev.orne.config.Configurable;
import dev.orne.config.ConfigurableProperty;
import dev.orne.config.ConfigurationOptions;
import dev.orne.config.Configurer;
import dev.orne.config.PreferredConfig;

/**
 * Tests of {@link Configurable} usage examples of public site.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @see https://orne-dev.github.io/java-config/configurable.html
 */
@Tag("ut")
@Tag("examples")
class ConfigurableExamplesTest {

    /**
     * Example of override both {@code Configurable} methods.
     */
    @Test
    void exampleOfFullOverrideConfigurable() {
        final Config config = Config.fromProperties()
                .add(Map.of(
                        "host", "www.example.org",
                        "port", "8080"))
                .build();
        final ConfigProvider provider = ConfigProvider.builder(config)
                .build();
        final Configurer configurer = Configurer.fromProvider(provider);
        final FullOverrideTest component = new FullOverrideTest();
        assertFalse(component.isConfigured());
        configurer.configure(component);
        assertTrue(component.isConfigured());
        assertEquals("www.example.org", component.host);
        assertEquals(8080, component.port);
    }

    /**
     * Example of configurable properties.
     */
    @Test
    void exampleOfConfigurableProperties() {
        final Config config = Config.fromProperties()
                .add(Map.of(
                        "host", "www.example.org",
                        "port", "8080"))
                .build();
        final ConfigProvider provider = ConfigProvider.builder(config)
                .build();
        final Configurer configurer = Configurer.fromProvider(provider);
        final ConfigurablePropertiesTest component = new ConfigurablePropertiesTest();
        configurer.configure(component);
        assertEquals("www.example.org", component.host);
        assertEquals(8080, component.port);
    }

    /**
     * Example of preferred configuration.
     */
    @Test
    void exampleOfPreferredConfig() {
        final Config config = Config.fromProperties()
                .add(Map.of(
                        "host", "api.example.org",
                        "port", "80"))
                .build();
        final MyConfig myconfig = Config.fromProperties()
                .add(Map.of(
                        "host", "www.example.org",
                        "port", "8080"))
                .as(MyConfig.class);
        final ConfigProvider provider = ConfigProvider.builder(config)
                .addConfig(myconfig)
                .build();
        final Configurer configurer = Configurer.fromProvider(provider);
        final PreferredConfigTest component = new PreferredConfigTest();
        configurer.configure(component);
        assertEquals("www.example.org", component.host);
        assertEquals(8080, component.port);
    }

    /**
     * Example of disable properties configuration.
     */
    @Test
    void exampleOfDisablePropertiesConfiguration() {
        final Config config = Config.fromProperties()
                .add(Map.of(
                        "alt.host", "www.example.org",
                        "alt.port", "8080"))
                .build();
        final ConfigProvider provider = ConfigProvider.builder(config)
                .build();
        final Configurer configurer = Configurer.fromProvider(provider);
        final DisablePropertiesConfigurationTest component = new DisablePropertiesConfigurationTest();
        configurer.configure(component);
        assertEquals("www.example.org", component.getHost());
        assertEquals(8080, component.getPort());
    }

    /**
     * Example of enable nested components configuration.
     */
    @Test
    void exampleOfEnableNestedBeansConfiguration() {
        final Config config = Config.fromProperties()
                .add(Map.of(
                        "host", "www.example.org",
                        "port", "8080",
                        "user", "admin"))
                .build();
        final ConfigProvider provider = ConfigProvider.builder(config)
                .build();
        final Configurer configurer = Configurer.fromProvider(provider);
        final EnableNestedBeansConfigurationTest component = new EnableNestedBeansConfigurationTest();
        configurer.configure(component);
        assertEquals("admin", component.user);
        assertEquals("www.example.org", component.nested.getHost());
        assertEquals(8080, component.nested.getPort());
    }

    public static class FullOverrideTest implements Configurable {

        private String host;
        private int port;
        private boolean configured = false;

        @Override
        public void configure(Config config) {
            this.host = config.get("host", "localhost");
            this.port = config.getInteger("port", 8080);
            this.configured = true;
        }

        @Override
        public boolean isConfigured() {
            return this.configured;
        }
    }

    public static class ConfigurablePropertiesTest implements Configurable {

        @ConfigurableProperty("host")
        private String host;
        @ConfigurableProperty("port")
        private int port;

        public String getHost() {
            return host;
        }
        public void setHost(String host) {
            this.host = host;
        }
        public int getPort() {
            return port;
        }
        public void setPort(int port) {
            this.port = port;
        }
    }

    interface MySuperConfig extends Config {}

    interface MyConfig extends MySuperConfig {
        final String HOST_PROP = "host";
        final String PORT_PROP = "port";
        default String getHost() {
            return get(HOST_PROP, "localhost");
        }
        default int getPort() {
            return getInteger(PORT_PROP, 8080);
        }
    }

    @PreferredConfig(MyConfig.class)
    public static class PreferredConfigTest implements Configurable {

        private String host;
        private int port;

        public void configure(Config config) {
            MyConfig myConfig = config.as(MyConfig.class);
            this.host = myConfig.getHost();
            this.port = myConfig.getPort();
       }
    }

    @ConfigurationOptions(configureProperties = false)
    public static class DisablePropertiesConfigurationTest extends ConfigurablePropertiesTest {

        @Override
        public void configure(@NotNull Config config) {
            setHost(config.get("alt.host"));
            setPort(config.getInteger("alt.port"));
        }
    }

    @ConfigurationOptions(configureNestedBeans = true)
    public static class EnableNestedBeansConfigurationTest implements Configurable {

        @ConfigurableProperty("user")
        private String user;
        private ConfigurablePropertiesTest nested = new ConfigurablePropertiesTest();
        public String getUser() {
            return user;
        }
        public void setUser(String user) {
            this.user = user;
        }
        public ConfigurablePropertiesTest getNested() {
            return nested;
        }
        public void setNested(ConfigurablePropertiesTest nested) {
            this.nested = nested;
        }
    }
}
