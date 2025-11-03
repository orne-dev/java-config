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
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import dev.orne.config.Config;
import dev.orne.config.ConfigCryptoEngine;
import dev.orne.config.ConfigCryptoProvider;
import dev.orne.config.FileWatchableConfig;
import dev.orne.config.MutableConfig;
import dev.orne.config.PreferencesMutableConfig;
import dev.orne.config.ValueDecoder;
import dev.orne.config.ValueDecorator;
import dev.orne.config.ValueEncoder;
import dev.orne.config.WatchableConfig;
import dev.orne.config.test.TestPreferencesFactory;

/**
 * Tests of {@link Config} creation examples of public site.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @see https://orne-dev.github.io/java-config/setup.html
 */
@Tag("ut")
@Tag("examples")
class SetupExamplesTest {

    /**
     * Restores the NOP root {@code Preferences} nodes.
     */
    @AfterEach
    void restoreTestPreferences() {
        TestPreferencesFactory.setSystemRoot(new TestPreferencesFactory.NopPreferences());
        TestPreferencesFactory.setUserRoot(new TestPreferencesFactory.NopPreferences());
    }

    /**
     * Example of {@code Config} setup from environment variables.
     */
    @Test
    void exampleOfEnvironmentVariables() {
        final Config config = Config.fromEnvironmentVariables()
                .build();
        System.getenv().keySet().forEach(key -> {
            assertEquals(System.getenv(key), config.get(key));
        });
    }

    /**
     * Example of {@code Config} setup from system properties.
     */
    @Test
    void exampleOfSystemProperties() {
        final Config config = Config.fromSystemProperties()
                .build();
        System.getProperties().stringPropertyNames().forEach(key -> {
            assertEquals(System.getProperty(key), config.get(key));
        });
    }

    /**
     * Example of {@code Config} setup from Java {@code Properties}.
     */
    @Test
    void exampleOfJavaProperties() {
        final Map<String, String> localValues = Map.of(
                "host", "www.example.org");
        final Config config = Config.fromProperties()
                .load("example/config.properties")
                .add(localValues)
                .build();
        assertEquals("www.example.org", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertEquals(300000L, config.getLong("timeout"));
        assertTrue(config.getBoolean("enabled"));
    }

    /**
     * Example of {@code Config} setup from Java {@code Properties}.
     */
    @Test
    void exampleOfMutableJavaProperties()
    throws IOException {
        final Map<String, String> localValues = Map.of(
                "host", "www.example.org");
        final FileWatchableConfig config = Config.fromProperties()
                .load("example/config.properties")
                .add(localValues)
                .mutable()
                .build();
        config.set("host", "example.com");
        config.remove("timeout");
        assertEquals("example.com", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertNull(config.getLong("timeout"));
        assertTrue(config.getBoolean("enabled"));
        final Path output = Files.createTempFile("savedConfig", ".properties");
        try {
            config.save(output);
        } finally {
            Files.deleteIfExists(output);
        }
    }

    /**
     * Example of {@code Config} setup from JSON.
     */
    @Test
    void exampleOfJson() {
        final Map<String, String> localValues = Map.of(
                "host", "www.example.org");
        final Config config = Config.fromJsonFiles()
                .load("example/config.json")
                .add(localValues)
                .build();
        assertEquals("www.example.org", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertEquals(300000L, config.getLong("timeout"));
        assertTrue(config.getBoolean("enabled"));
    }

    /**
     * Example of {@code Config} setup from JSON.
     */
    @Test
    void exampleOfMutableJson()
    throws IOException {
        final Map<String, String> localValues = Map.of(
                "host", "www.example.org");
        final FileWatchableConfig config = Config.fromJsonFiles()
                .load("example/config.json")
                .add(localValues)
                .mutable()
                .build();
        config.set("host", "example.com");
        config.remove("timeout");
        assertEquals("example.com", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertNull(config.getLong("timeout"));
        assertTrue(config.getBoolean("enabled"));
        final Path output = Files.createTempFile("savedConfig", ".json");
        try {
            config.save(output);
        } finally {
            Files.deleteIfExists(output);
        }
    }

    /**
     * Example of {@code Config} setup from YAML.
     */
    @Test
    void exampleOfYaml() {
        final Map<String, String> localValues = Map.of(
                "host", "www.example.org");
        final Config config = Config.fromYamlFiles()
                .load("example/config.yml")
                .add(localValues)
                .build();
        assertEquals("www.example.org", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertEquals(300000L, config.getLong("timeout"));
        assertTrue(config.getBoolean("enabled"));
    }

    /**
     * Example of {@code Config} setup from YAML.
     */
    @Test
    void exampleOfMutableYaml()
    throws IOException {
        final Map<String, String> localValues = Map.of(
                "host", "www.example.org");
        final FileWatchableConfig config = Config.fromYamlFiles()
                .load("example/config.yml")
                .add(localValues)
                .mutable()
                .build();
        config.set("host", "example.com");
        config.remove("timeout");
        assertEquals("example.com", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertNull(config.getLong("timeout"));
        assertTrue(config.getBoolean("enabled"));
        final Path output = Files.createTempFile("savedConfig", ".yml");
        try {
            config.save(output);
        } finally {
            Files.deleteIfExists(output);
        }
    }

    /**
     * Example of {@code Config} setup from YAML.
     */
    @Test
    void exampleOfXml() {
        final Map<String, String> localValues = Map.of(
                "host", "www.example.org");
        final Config config = Config.fromXmlFiles()
                .load("example/config.xml")
                .add(localValues)
                .build();
        assertEquals("www.example.org", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertEquals(300000L, config.getLong("timeout"));
        assertTrue(config.getBoolean("enabled"));
    }

    /**
     * Example of {@code Config} setup from YAML.
     */
    @Test
    void exampleOfMutableXml()
    throws IOException {
        final Map<String, String> localValues = Map.of(
                "host", "www.example.org");
        final FileWatchableConfig config = Config.fromXmlFiles()
                .load("example/config.xml")
                .add(localValues)
                .mutable()
                .build();
        config.set("host", "example.com");
        config.remove("timeout");
        assertEquals("example.com", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertNull(config.getLong("timeout"));
        assertTrue(config.getBoolean("enabled"));
        final Path output = Files.createTempFile("savedConfig", ".xml");
        try {
            config.save(output);
        } finally {
            Files.deleteIfExists(output);
        }
    }

    /**
     * Example of {@code Config} setup from Spring {@code Environment}.
     */
    @Test
    void exampleOfSpringEnvironment() {
        final Map<String, String> mockValues = Map.of(
                "host", "www.example.org",
                "port", "8080",
                "timeout", "300000",
                "enabled", "true");
        final Environment env = mock(Environment.class);
        given(env.getProperty(any())).will(call -> {
            return mockValues.get(call.getArgument(0));
        });
        final Config config = Config.fromSpringEnvironment()
                .ofEnvironment(env)
                .build();
        assertEquals("www.example.org", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertEquals(300000L, config.getLong("timeout"));
        assertTrue(config.getBoolean("enabled"));
    }

    /**
     * Example of {@code Config} setup from Spring {@code Environment}.
     */
    @Test
    void exampleOfSpringConfigurableEnvironment() {
        final Map<String, String> mockValues = Map.of(
                "host", "www.example.org",
                "port", "8080",
                "timeout", "300000",
                "enabled", "true");
        final MutablePropertySources sources = mock(MutablePropertySources.class);
        final EnumerablePropertySource<?> source = mock(EnumerablePropertySource.class);
        final ConfigurableEnvironment env = mock(ConfigurableEnvironment.class);
        given(env.getProperty(any())).will(call -> {
            return mockValues.get(call.getArgument(0));
        });
        given(env.getPropertySources()).willReturn(sources);
        given(sources.stream()).willAnswer(invocation -> Stream.of(source));
        given(source.getPropertyNames()).willReturn(mockValues.keySet().toArray(String[]::new));
        final Config config = Config.fromSpringEnvironment()
                .ofEnvironment(env)
                .withIterableKeys()
                .build();
        assertEquals("www.example.org", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertEquals(300000L, config.getLong("timeout"));
        assertTrue(config.getBoolean("enabled"));
        assertEquals(mockValues.keySet(), config.getKeys().collect(Collectors.toSet()));
    }

    /**
     * Example of {@code Config} setup from Java {@code Preferences}.
     */
    @Test
    void exampleOfJavaPreferences() {
        final Map<String, String> mockValues = Map.of(
                "host", "www.example.org",
                "port", "8080",
                "timeout", "300000",
                "enabled", "true");
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setUserRoot(rootPreferences);
        final TestPreferencesFactory.InMemoryPreferences preferences =
                new TestPreferencesFactory.InMemoryPreferences();
        preferences.setAttributes(mockValues);
        final String path = "/test/path";
        willReturn(preferences).given(rootPreferences).node(path);
        final Config config = Config.fromJavaPreferences()
                .ofUser(path)
                .build();
        assertEquals("www.example.org", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertEquals(300000L, config.getLong("timeout"));
        assertTrue(config.getBoolean("enabled"));
    }

    /**
     * Example of {@code Config} setup from Java {@code Preferences}.
     */
    @Test
    void exampleOfMutableJavaPreferences()
    throws BackingStoreException {
        final Map<String, String> mockValues = Map.of(
                "host", "www.example.org",
                "port", "8080",
                "timeout", "300000",
                "enabled", "true");
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setUserRoot(rootPreferences);
        final TestPreferencesFactory.InMemoryPreferences preferences =
                new TestPreferencesFactory.InMemoryPreferences();
        preferences.setAttributes(mockValues);
        final String path = "/test/path";
        willReturn(preferences).given(rootPreferences).node(path);
        final PreferencesMutableConfig config = Config.fromJavaPreferences()
                .ofUser(path)
                .mutable()
                .build();
        config.set("host", "example.com");
        config.remove("timeout");
        assertEquals("example.com", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertNull(config.getLong("timeout"));
        assertTrue(config.getBoolean("enabled"));
        config.sync();
        config.flush();
    }

    /**
     * Example of {@code Config} setup from Apache Commons
     * {@code ImmutableConfiguration}.
     */
    @Test
    void exampleOfCommons() {
        final PropertiesConfiguration delegated;
        try {
            delegated = new Configurations()
                    .propertiesBuilder()
                    .getConfiguration();
            delegated.setProperty("host", "www.example.org");
            delegated.setProperty("port", "8080");
            delegated.setProperty("timeout", "300000");
            delegated.setProperty("enabled", "true");
        } catch (final ConfigurationException e) {
            throw new AssertionError("Error creating delegated configuration", e);
        }
        final Config config = Config.fromApacheCommons()
                .ofDelegate(delegated)
                .build();
        assertEquals("www.example.org", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertEquals(300000L, config.getLong("timeout"));
        assertTrue(config.getBoolean("enabled"));
    }

    /**
     * Example of {@code Config} setup from Apache Commons
     * {@code Configuration}.
     */
    @Test
    void exampleOfMutableCommons() {
        final PropertiesConfiguration delegated;
        try {
            delegated = new Configurations()
                    .propertiesBuilder()
                    .getConfiguration();
            delegated.setProperty("host", "www.example.org");
            delegated.setProperty("port", "8080");
            delegated.setProperty("timeout", "300000");
            delegated.setProperty("enabled", "true");
        } catch (final ConfigurationException e) {
            throw new AssertionError("Error creating delegated configuration", e);
        }
        final WatchableConfig config = Config.fromApacheCommons()
                .ofDelegate(delegated)
                .mutable()
                .build();
        config.set("host", "example.com");
        config.remove("timeout");
        assertEquals("example.com", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertNull(config.getLong("timeout"));
        assertTrue(config.getBoolean("enabled"));
    }

    /**
     * Example of {@code Config} hierarchy setup.
     */
    @Test
    void exampleOfConfigHierarchy() {
        final Config baseConfig = Config.fromProperties()
                .load("example/base.properties")
                .withParent(Config.fromSystemProperties()
                    .withParent(Config.fromEnvironmentVariables())
                    .build())
                .build();
        final Config config = Config.fromProperties()
                .load("example/dev.properties")
                .withParent(baseConfig)
                .build();
        assertEquals("service.example.com", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertEquals("user", config.get("username"));
        assertEquals(300000L, config.getLong("timeout"));
        assertTrue(config.getBoolean("enabled"));
        assertEquals("dev.example.com", config.get("dev.host"));
    }

    /**
     * Example of {@code Config} hierarchy setup.
     */
    @Test
    void exampleOfConfigHierarchyParentOverride() {
        final Config baseConfig = Config.fromProperties()
                .load("example/base.properties")
                .withParent(Config.fromSystemProperties()
                    .withParent(Config.fromEnvironmentVariables())
                    .build())
                .build();
        final Config config = Config.fromProperties()
                .load("example/dev.properties")
                .withParent(baseConfig)
                .withOverrideParentProperties()
                .build();
        assertEquals("service.dev.example.com", config.get("host"));
        assertEquals(8080, config.getInteger("port"));
        assertEquals("devuser", config.get("username"));
        assertEquals(300000L, config.getLong("timeout"));
        assertTrue(config.getBoolean("enabled"));
        assertEquals("dev.example.com", config.get("dev.host"));
    }

    /**
     * Example of {@code Config} decoders setup.
     */
    @Test
    void exampleOfDecoders() {
        final ValueDecoder parentDecode = value -> {
            return "decoded_parent[" + value + "]";
        };
        final Config parent = Config.fromProperties()
                .add(Map.of(
                    "api.key", "my_encoded_api_key"))
                .withDecoder(parentDecode)
                .build();
        final ValueDecoder decode = value -> {
            return "decoded[" + value + "]";
        };
        final Config config = Config.fromProperties()
                .add(Map.of(
                    "api.child.key", "my_encoded_child_api_key"))
                .withParent(parent)
                .withDecoder(decode)
                .build();
        assertEquals("decoded[my_encoded_child_api_key]", config.get("api.child.key"));
        assertEquals("decoded_parent[my_encoded_api_key]", config.get("api.key"));
    }

    /**
     * Example of {@code Config} decoders setup.
     */
    @Test
    void exampleOfEncoders() {
        final ValueEncoder parentEncode = value -> {
            return "encoded_parent[" + value + "]";
        };
        final MutableConfig parent = Config.fromProperties()
                .mutable()
                .withEncoder(parentEncode)
                .build();
        final ValueEncoder encode = value -> {
            return "encoded[" + value + "]";
        };
        final MutableConfig config = Config.fromProperties()
                .withParent(parent)
                .mutable()
                .withEncoder(encode)
                .build();
        config.set("api.key", "my_plain_api_key");
        assertEquals("encoded[my_plain_api_key]", config.get("api.key"));
        parent.set("api.key", "my_plain_api_key");
        assertEquals("encoded_parent[my_plain_api_key]", parent.get("api.key"));
        assertEquals("encoded_parent[my_plain_api_key]", config.get("api.key"));
    }

    /**
     * Example of {@code Config} cryptographic transformations setup.
     */
    @Test
    void exampleOfCrypto() {
        final ConfigCryptoProvider crypto = ConfigCryptoProvider.builder()
                .withAesGcmEngine("secretSalt".getBytes(StandardCharsets.UTF_8))
                .withSecretKey("secretKey".toCharArray())
                .build();
        final MutableConfig config = Config.fromProperties()
                    .add(Map.of(
                        "api.key", crypto.encrypt("my_old_plain_api_key")))
                    .withEncryption(crypto)
                    .mutable()
                    .build();
        assertEquals("my_old_plain_api_key", config.get("api.key"));
        config.set("api.key", "my_plain_api_key");
        assertEquals("my_plain_api_key", config.get("api.key"));
    }

    /**
     * Example of {@code Config} cryptographic custom engine setup.
     */
    @Test
    void exampleOfCryptoCustomEngine() {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final Cipher cipher = mock(Cipher.class);
        final SecretKey key = mock(SecretKey.class);
        given(engine.createSecretKey(any())).willReturn(key);
        given(engine.createCipher()).willReturn(cipher);
        given(engine.encrypt(any(), any(), any()))
            .will(call -> {
                assertEquals(key, call.getArgument(1));
                assertEquals(cipher, call.getArgument(2));
                final String value = call.getArgument(0);
                return "encrypted[" + value + "]";
            });
        given(engine.decrypt(any(), any(), any()))
            .will(call -> {
                assertEquals(key, call.getArgument(1));
                assertEquals(cipher, call.getArgument(2));
                final String value = call.getArgument(0);
                assertTrue(value.startsWith("encrypted["));
                assertTrue(value.endsWith("]"));
                return value.substring(10, value.length() - 1);
            });
        final ConfigCryptoProvider crypto = ConfigCryptoProvider.builder()
                .withEngine(engine)
                .withSecretKey("secretKey".toCharArray())
                .build();
        final MutableConfig config = Config.fromProperties()
                .add(Map.of(
                    "api.key", crypto.encrypt("my_old_plain_api_key")))
                .withEncryption(crypto)
                .mutable()
                .build();
        assertEquals("my_old_plain_api_key", config.get("api.key"));
        config.set("api.key", "my_plain_api_key");
        assertEquals("my_plain_api_key", config.get("api.key"));
    }

    /**
     * Example of {@code Config} cryptographic read-only transformations setup.
     */
    @Test
    void exampleOfCryptoReadOnly() {
        final ConfigCryptoProvider crypto = ConfigCryptoProvider.builder()
                .withAesGcmEngine("secretSalt".getBytes(StandardCharsets.UTF_8))
                .withSecretKey("secretKey".toCharArray())
                .build();
        final Config config = Config.fromProperties()
                .add(Map.of(
                    "api.key", crypto.encrypt("my_old_plain_api_key")))
                .withEncryption(crypto)
                .build();
        assertEquals("my_old_plain_api_key", config.get("api.key"));
    }

    /**
     * Example of {@code Config} cryptographic and encoder/decoder chain setup.
     */
    @Test
    void exampleOfCryptoAndEncoderDecoder() {
        final ConfigCryptoProvider crypto = ConfigCryptoProvider.builder()
                .withAesGcmEngine("secretSalt".getBytes(StandardCharsets.UTF_8))
                .withSecretKey("secretKey".toCharArray())
                .build();
        final ValueDecoder decode = value -> {
            return "decoded[" + value + "]";
        };
        final ValueEncoder encode = value -> {
            return "encoded[" + value + "]";
        };
        final MutableConfig config = Config.fromProperties()
                .add(Map.of(
                    "api.key", crypto.encrypt("my_old_plain_api_key")))
                .withEncryption(crypto)
                .withDecoder(decode)
                .mutable()
                .withEncoder(encode)
                .build();
        assertEquals("decoded[my_old_plain_api_key]", config.get("api.key"));
        config.set("api.key", "my_plain_api_key");
        assertEquals("decoded[encoded[my_plain_api_key]]", config.get("api.key"));
    }

    /**
     * Example of {@code Config} decorators setup.
     */
    @Test
    void exampleOfDecorators() {
        ValueDecorator parentDecorate = value -> {
            return "parentDecorated[" + value + "]";
        };
        Config parent = Config.fromProperties()
                .add(Map.of(
                    "host", "example.com"))
                .withDecorator(parentDecorate)
                .build();
        ValueDecorator decorate = value -> {
            return "decorated[" + value + "]";
        };
        Config config = Config.fromProperties()
                .withParent(parent)
                .withDecorator(decorate)
                .build();
        assertEquals("decorated[example.com]", config.get("host"));
        assertEquals("parentDecorated[example.com]", parent.get("host"));
    }

    /**
     * Example of {@code Config} variable resolution setup.
     */
    @Test
    void exampleOfVariableResolution() {
        Config config = Config.fromProperties()
                .add(Map.of(
                    "host", "example.com",
                    "port", "80"))
                .withParent(Config.fromProperties()
                    .add(Map.of(
                        "service.url", "http://${host}:${port}/api",
                        "host", "localhost",
                        "port", "8080")))
                .withOverrideParentProperties()
                .withVariableResolution()
                .build();
        assertEquals("http://example.com:80/api", config.get("service.url"));
        assertEquals("http://${host}:${port}/api", config.getParent().get("service.url"));
    }

    /**
     * Example of {@code Config} variable resolution setup.
     */
    @Test
    void exampleOfVariableResolutionAndDecorators() {
        ValueDecorator decorate = value -> {
            return "decorated[" + value + "]";
        };
        Config config = Config.fromProperties()
                .add(Map.of(
                    "service.url", "http://${host}:${port}/api",
                    "host", "localhost",
                    "port", "8080"))
                .withVariableResolution()
                .withDecorator(decorate)
                .build();
        assertEquals("decorated[http://decorated[localhost]:decorated[8080]/api]", config.get("service.url"));
    }
}
