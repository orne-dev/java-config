package dev.orne.config.impl;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2025 Orne Developments
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
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import dev.orne.config.MutableConfigBuilder;
import dev.orne.config.ValueDecoder;
import dev.orne.config.ValueDecorator;
import dev.orne.config.ValueEncoder;

/**
 * Unit tests for {@link YamlMutableConfigImpl}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class YamlMutableConfigTest
extends AbstractWatchableConfigTest {

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
    private static ObjectNode testObject;
    private static String testResource = "dev/orne/config/impl/test.resource.yaml";
    private static File testFile;
    private static Path testPath;
    private static URL testUrl;

    private @Mock ValueDecoder mockDecoder;
    private @Mock ValueEncoder mockEncoder;
    private @Mock ValueDecorator mockDecorator;
    private @Mock ObjectNode mockJsonObject;

    /**
     * Prepares the data needed by the tests.
     * @throws IOException Not thrown
     */
    @BeforeAll
    static void prepareTestData()
    throws IOException {
        testObject = NODE_FACTORY.objectNode()
                .set("test", NODE_FACTORY.objectNode()
                    .put("type", TEST_VALUES_TYPE)
                    .set("values", NODE_FACTORY.objectNode()
                        .put("type", TEST_VALUES_TYPE)));
        final ObjectNode fileProps = NODE_FACTORY.objectNode()
                .set("test", NODE_FACTORY.objectNode()
                        .put("type", TEST_FILE_TYPE)
                        .set("file", NODE_FACTORY.objectNode()
                            .put("type", TEST_FILE_TYPE)));
        testFile = File.createTempFile(YamlMutableConfigTest.class.getSimpleName(), ".yaml");
        try (final FileOutputStream fileOS = new FileOutputStream(testFile)) {
            OBJECT_MAPPER.writeValue(fileOS, fileProps);
        }
        testPath = testFile.toPath();
        testUrl = YamlMutableConfigTest.class.getResource("test.url.yaml");
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
    protected MutableConfigBuilder createBuilder(
            final @NotNull Map<String, String> properties) {
        final ObjectNode data = NODE_FACTORY.objectNode();
        properties.forEach((key, value) -> {
            JacksonUtils.setNodeValue(
                    data,
                    JsonConfigImpl.DEFAULT_PROPERTY_SEPARATOR,
                    key,
                    value);
        });
        return Config.fromYamlFiles()
                .mutable()
                .add(data);
    }

    /**
     * Tests empty instance building.
     */
    @Test
    void testEmptyBuilder() {
        final YamlMutableConfigImpl config = assertInstanceOf(
                YamlMutableConfigImpl.class,
                Config.fromYamlFiles()
                    .mutable()
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getJsonObject());
        assertTrue(config.getJsonObject().isEmpty());
    }

    /**
     * Tests instance building from properties.
     */
    @Test
    void testObjectNodeBuilder() {
        final YamlMutableConfigImpl config = assertInstanceOf(
                YamlMutableConfigImpl.class,
                Config.fromYamlFiles()
                    .mutable()
                    .add(testObject)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getJsonObject());
        assertFalse(config.getJsonObject().isEmpty());
        assertNotSame(testObject, config.getJsonObject());
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
        assertThrows(NullPointerException.class, () -> {
            Config.fromYamlFiles()
                .mutable()
                .load((String) null)
                .build();
        });

        final YamlMutableConfigImpl config = assertInstanceOf(
                YamlMutableConfigImpl.class,
                Config.fromYamlFiles()
                    .mutable()
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
     * Tests instance building from missing ClassPath resources.
     */
    @Test
    void testMissingResourceBuilder() {
        final YamlMutableConfigImpl config = assertInstanceOf(
                YamlMutableConfigImpl.class,
                Config.fromYamlFiles()
                    .mutable()
                    .load("non/existent/resource.yaml")
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
        assertThrows(NullPointerException.class, () -> {
            Config.fromYamlFiles()
                .mutable()
                .load((File) null)
                .build();
        });

        final YamlMutableConfigImpl config = assertInstanceOf(
                YamlMutableConfigImpl.class,
                Config.fromYamlFiles()
                    .mutable()
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
     * Tests instance building from missing file.
     */
    @Test
    void testMissingFileBuilder() {
        final YamlMutableConfigImpl config = assertInstanceOf(
                YamlMutableConfigImpl.class,
                Config.fromYamlFiles()
                    .mutable()
                    .load(new File("non/existent/resource.yaml"))
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
        assertThrows(NullPointerException.class, () -> {
            Config.fromYamlFiles()
                .mutable()
                .load((Path) null)
                .build();
        });

        final YamlMutableConfigImpl config = assertInstanceOf(
                YamlMutableConfigImpl.class,
                Config.fromYamlFiles()
                    .mutable()
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
     * Tests instance building from missing path.
     */
    @Test
    void testMissingPathBuilder() {
        final YamlMutableConfigImpl config = assertInstanceOf(
                YamlMutableConfigImpl.class,
                Config.fromYamlFiles()
                    .mutable()
                    .load(Paths.get("non/existent/path.yaml"))
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
        assertThrows(NullPointerException.class, () -> {
            Config.fromYamlFiles()
                .mutable()
                .load((URL) null)
                .build();
        });
        
        final YamlMutableConfigImpl config = assertInstanceOf(
                YamlMutableConfigImpl.class,
                Config.fromYamlFiles()
                    .mutable()
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
     * Tests instance building from missing URL.
     */
    @Test
    void testMissingUrlBuilder() throws IOException {
        final YamlMutableConfigImpl config = assertInstanceOf(
                YamlMutableConfigImpl.class,
                Config.fromYamlFiles()
                    .mutable()
                    .load(new URL(testUrl.toString().replace("test.url", "non.existent")))
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final ObjectNode properties = config.getJsonObject();
        assertNotNull(properties);
        assertTrue(properties.isEmpty());
    }
}
