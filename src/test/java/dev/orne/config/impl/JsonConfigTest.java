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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.orne.config.Config;
import dev.orne.config.ConfigBuilder;
import dev.orne.config.JsonConfigBuilder;
import dev.orne.config.ValueDecoder;
import dev.orne.config.ValueDecorator;

/**
 * Unit tests for {@link JsonConfigImpl}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class JsonConfigTest
extends AbstractConfigTest {

    private static final String TEST_COMMON_KEY = "test.type";
    private static final String TEST_VALUES_KEY = "test.values.type";
    private static final String TEST_RESOURCE_KEY = "test.resource.type";
    private static final String TEST_FILE_KEY = "test.file.type";
    private static final String TEST_URL_KEY = "test.url.type";
    
    private static final String TEST_VALUES_TYPE = "values";
    private static final String TEST_RESOURCE_TYPE = "resource";
    private static final String TEST_FILE_TYPE = "file";
    private static final String TEST_URL_TYPE = "url";
    
    /** The Jackson object mapper. */
    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper();
    /** The JSON node factory. */
    private static final JsonNodeFactory NODE_FACTORY =
            JacksonUtils.NODE_FACTORY;
    private static Map<String, String> testValues;
    private static String testResource = "dev/orne/config/impl/test.resource.json";
    private static File testFile;
    private static Path testPath;
    private static URL testUrl;

    private @Mock ValueDecoder mockDecoder;
    private @Mock ValueDecorator mockDecorator;
    private @Mock ObjectNode mockObject;

    /**
     * Prepares the data needed by the tests.
     * @throws IOException Not thrown
     */
    @BeforeAll
    static void prepareTestData()
    throws IOException {
        testValues = new HashMap<>();
        testValues.put(TEST_COMMON_KEY, TEST_VALUES_TYPE);
        testValues.put(TEST_VALUES_KEY, TEST_VALUES_TYPE);
        final ObjectNode fileProps = NODE_FACTORY.objectNode()
                .set("test", NODE_FACTORY.objectNode()
                        .put("type", TEST_FILE_TYPE)
                        .set("file", NODE_FACTORY.objectNode()
                            .put("type", TEST_FILE_TYPE)));
        testFile = File.createTempFile(JsonConfigTest.class.getSimpleName(), ".json");
        try (final FileOutputStream fileOS = new FileOutputStream(testFile)) {
            OBJECT_MAPPER.writeValue(fileOS, fileProps);
        }
        testPath = testFile.toPath();
        testUrl = JsonConfigTest.class.getResource("test.url.json");
    }

    /**
     * Releases data resources created for tests.
     */
    @AfterAll
    static void cleanTestData() {
        testFile.delete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConfigBuilder<?> createBuilder(
            final @NotNull Map<String, String> properties) {
        return Config.fromJsonFiles()
                .add(properties);
    }

    /**
     * Tests empty instance building.
     */
    @Test
    void testEmptyBuilder() {
        final JsonConfigImpl config = assertInstanceOf(
                JsonConfigImpl.class,
                Config.fromJsonFiles()
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getJsonObject());
        assertTrue(config.getJsonObject().isEmpty());
    }

    /**
     * Tests instance building from custom properties map.
     */
    @Test
    void testMapBuilder() {
        final JsonConfigImpl config = assertInstanceOf(
                JsonConfigImpl.class,
                Config.fromJsonFiles()
                    .add(testValues)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getJsonObject());
        assertFalse(config.getJsonObject().isEmpty());
        assertNotSame(testValues, config.getJsonObject());
        assertTrue(config.contains(TEST_COMMON_KEY));
        assertEquals(TEST_VALUES_TYPE, config.get(TEST_COMMON_KEY));
        assertTrue(config.contains(TEST_VALUES_KEY));
        assertEquals(TEST_VALUES_TYPE, config.get(TEST_VALUES_KEY));
        assertFalse(config.contains(TEST_RESOURCE_KEY));
        assertNull(config.get(TEST_RESOURCE_KEY));
        assertFalse(config.contains(TEST_FILE_KEY));
        assertNull(config.get(TEST_FILE_KEY));
        assertFalse(config.contains(TEST_URL_KEY));
        assertNull(config.get(TEST_URL_KEY));
    }

    /**
     * Tests instance building from ClassPath resources.
     */
    @Test
    void testResourceBuilder() {
        final JsonConfigImpl config = assertInstanceOf(
                JsonConfigImpl.class,
                Config.fromJsonFiles()
                    .load(testResource)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getJsonObject());
        assertFalse(config.getJsonObject().isEmpty());
        assertTrue(config.contains(TEST_COMMON_KEY));
        assertEquals(TEST_RESOURCE_TYPE, config.get(TEST_COMMON_KEY));
        assertFalse(config.contains(TEST_VALUES_KEY));
        assertNull(config.get(TEST_VALUES_KEY));
        assertTrue(config.contains(TEST_RESOURCE_KEY));
        assertEquals(TEST_RESOURCE_TYPE, config.get(TEST_RESOURCE_KEY));
        assertFalse(config.contains(TEST_FILE_KEY));
        assertNull(config.get(TEST_FILE_KEY));
        assertFalse(config.contains(TEST_URL_KEY));
        assertNull(config.get(TEST_URL_KEY));
    }

    /**
     * Tests instance building from null ClassPath resources.
     */
    @Test
    void testNullResourceBuilder() {
        final JsonConfigBuilder builder = Config.fromJsonFiles();
        assertThrows(NullPointerException.class, () -> builder.load((String) null));
    }

    /**
     * Tests instance building from missing ClassPath resources.
     */
    @Test
    void testMissingResourceBuilder() {
        final JsonConfigImpl config = assertInstanceOf(
                JsonConfigImpl.class,
                Config.fromJsonFiles()
                    .load("non/existent/resource.json")
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final ObjectNode properties = config.getJsonObject();
        assertNotNull(properties);
        assertTrue(properties.isEmpty());
    }

    /**
     * Tests instance building from file.
     */
    @Test
    void testFileBuilder() {
        final JsonConfigImpl config = assertInstanceOf(
                JsonConfigImpl.class,
                Config.fromJsonFiles()
                    .load(testFile)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getJsonObject());
        assertFalse(config.getJsonObject().isEmpty());
        assertTrue(config.contains(TEST_COMMON_KEY));
        assertEquals(TEST_FILE_TYPE, config.get(TEST_COMMON_KEY));
        assertFalse(config.contains(TEST_VALUES_KEY));
        assertNull(config.get(TEST_VALUES_KEY));
        assertFalse(config.contains(TEST_RESOURCE_KEY));
        assertNull(config.get(TEST_RESOURCE_KEY));
        assertTrue(config.contains(TEST_FILE_KEY));
        assertEquals(TEST_FILE_TYPE, config.get(TEST_FILE_KEY));
        assertFalse(config.contains(TEST_URL_KEY));
        assertNull(config.get(TEST_URL_KEY));
    }

    /**
     * Tests instance building from null file.
     */
    @Test
    void testNullFileBuilder() {
        final JsonConfigBuilder builder = Config.fromJsonFiles();
        assertThrows(NullPointerException.class, () -> builder.load((File) null));
    }

    /**
     * Tests instance building from missing file.
     */
    @Test
    void testMissingFileBuilder() {
        final JsonConfigImpl config = assertInstanceOf(
                JsonConfigImpl.class,
                Config.fromJsonFiles()
                    .load(new File("non/existent/resource.json"))
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final ObjectNode properties = config.getJsonObject();
        assertNotNull(properties);
        assertTrue(properties.isEmpty());
    }

    /**
     * Tests instance building from path.
     */
    @Test
    void testPathBuilder() {
        final JsonConfigImpl config = assertInstanceOf(
                JsonConfigImpl.class,
                Config.fromJsonFiles()
                    .load(testPath)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getJsonObject());
        assertFalse(config.getJsonObject().isEmpty());
        assertTrue(config.contains(TEST_COMMON_KEY));
        assertEquals(TEST_FILE_TYPE, config.get(TEST_COMMON_KEY));
        assertFalse(config.contains(TEST_VALUES_KEY));
        assertNull(config.get(TEST_VALUES_KEY));
        assertFalse(config.contains(TEST_RESOURCE_KEY));
        assertNull(config.get(TEST_RESOURCE_KEY));
        assertTrue(config.contains(TEST_FILE_KEY));
        assertEquals(TEST_FILE_TYPE, config.get(TEST_FILE_KEY));
        assertFalse(config.contains(TEST_URL_KEY));
        assertNull(config.get(TEST_URL_KEY));
    }

    /**
     * Tests instance building from null path.
     */
    @Test
    void testNullPathBuilder() {
        final JsonConfigBuilder builder = Config.fromJsonFiles();
        assertThrows(NullPointerException.class, () -> builder.load((Path) null));
    }

    /**
     * Tests instance building from missing path.
     */
    @Test
    void testMissingPathBuilder() {
        final JsonConfigImpl config = assertInstanceOf(
                JsonConfigImpl.class,
                Config.fromJsonFiles()
                    .load(Paths.get("non/existent/path.json"))
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final ObjectNode properties = config.getJsonObject();
        assertNotNull(properties);
        assertTrue(properties.isEmpty());
    }

    /**
     * Tests instance building from URL.
     */
    @Test
    void testUrlBuilder() {
        final JsonConfigImpl config = assertInstanceOf(
                JsonConfigImpl.class,
                Config.fromJsonFiles()
                    .load(testUrl)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getJsonObject());
        assertFalse(config.getJsonObject().isEmpty());
        assertTrue(config.contains(TEST_COMMON_KEY));
        assertEquals(TEST_URL_TYPE, config.get(TEST_COMMON_KEY));
        assertFalse(config.contains(TEST_VALUES_KEY));
        assertNull(config.get(TEST_VALUES_KEY));
        assertFalse(config.contains(TEST_RESOURCE_KEY));
        assertNull(config.get(TEST_RESOURCE_KEY));
        assertFalse(config.contains(TEST_FILE_KEY));
        assertNull(config.get(TEST_FILE_KEY));
        assertTrue(config.contains(TEST_URL_KEY));
        assertEquals(TEST_URL_TYPE, config.get(TEST_URL_KEY));
    }

    /**
     * Tests instance building from null URL.
     */
    @Test
    void testNullUrlBuilder() {
        final JsonConfigBuilder builder = Config.fromJsonFiles();
        assertThrows(NullPointerException.class, () -> builder.load((URL) null));
    }

    /**
     * Tests instance building from missing URL.
     */
    @Test
    void testMissingUrlBuilder() throws IOException {
        final URL missingUrl = URI
                .create(
                    testUrl.toString()
                        .replace("test.url", "non.existent"))
                .toURL();
        final JsonConfigImpl config = assertInstanceOf(
                JsonConfigImpl.class,
                Config.fromJsonFiles()
                    .load(missingUrl)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final ObjectNode properties = config.getJsonObject();
        assertNotNull(properties);
        assertTrue(properties.isEmpty());
    }

    /**
     * Tests instance building with custom property levels separator.
     */
    @Test
    void testCustomSeparatorBuilder() {
        final String defaultSeparatorKey = "test.default.value";
        final String defaultSeparatorValue = "Default separator value";
        final HashMap<String, String> defaultSeparatorValues = new HashMap<>();
        defaultSeparatorValues.put(defaultSeparatorKey, defaultSeparatorValue);
        final String customSeparator = "/";
        final String defaultSeparatorNewKey = "test/default/value";
        final String customSeparatorKey = "test/custom/value";
        final String customSeparatorValue = "Custom separator value";
        final HashMap<String, String> customSeparatorValues = new HashMap<>();
        customSeparatorValues.put(customSeparatorKey, customSeparatorValue);
        final JsonConfigImpl config = assertInstanceOf(
                JsonConfigImpl.class,
                Config.fromJsonFiles()
                    .add(defaultSeparatorValues)
                    .withSeparator(customSeparator)
                    .add(customSeparatorValues)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getJsonObject());
        assertFalse(config.getJsonObject().isEmpty());
        assertEquals(customSeparator, config.getPropertySeparator());
        assertTrue(config.contains(defaultSeparatorNewKey));
        assertEquals(defaultSeparatorValue, config.get(defaultSeparatorNewKey));
        assertTrue(config.contains(customSeparatorKey));
        assertEquals(customSeparatorValue, config.get(customSeparatorKey));
    }
}
