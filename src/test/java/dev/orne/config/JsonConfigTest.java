package dev.orne.config;

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
import static org.mockito.BDDMockito.*;

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

/**
 * Unit tests for {@link JsonConfig}.
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
    private static ObjectNode testObject;
    private static String testResource = "dev/orne/config/test.resource.json";
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
    protected ConfigBuilder createBuilder(
            final @NotNull Map<String, String> properties) {
        final ObjectNode data = NODE_FACTORY.objectNode();
        properties.forEach((key, value) -> {
            JacksonUtils.setNodeValue(
                    data,
                    JsonConfig.DEFAULT_PROPERTY_SEPARATOR,
                    key,
                    value);
        });
        return Config.fromJsonFiles()
                .add(data);
    }

    /**
     * Tests empty instance building.
     */
    @Test
    void testEmptyBuilder() {
        final JsonConfig config = Config.fromJsonFiles()
                .build();
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
        final JsonConfig config = Config.fromJsonFiles()
                .add(testObject)
                .build();
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
            Config.fromJsonFiles()
                .load((String) null)
                .build();
        });

        final JsonConfig config = Config.fromJsonFiles()
                .load(testResource)
                .build();
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
        final JsonConfig config = Config.fromJsonFiles()
                .load("non/existent/resource.json")
                .build();
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
            Config.fromJsonFiles()
                .load((File) null)
                .build();
        });

        final JsonConfig config = Config.fromJsonFiles()
                .load(testFile)
                .build();
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
        final JsonConfig config = Config.fromJsonFiles()
                .load(new File("non/existent/resource.json"))
                .build();
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
            Config.fromJsonFiles()
                .load((Path) null)
                .build();
        });

        final JsonConfig config = Config.fromJsonFiles()
                .load(testPath)
                .build();
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
        final JsonConfig config = Config.fromJsonFiles()
                .load(Paths.get("non/existent/path.json"))
                .build();
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
            Config.fromJsonFiles()
                .load((URL) null)
                .build();
        });
        
        final JsonConfig config = Config.fromJsonFiles()
                .load(testUrl)
                .build();
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
        final JsonConfig config = Config.fromJsonFiles()
                .load(new URL(testUrl.toString().replace("test.url", "non.existent")))
                .build();
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final ObjectNode properties = config.getJsonObject();
        assertNotNull(properties);
        assertTrue(properties.isEmpty());
    }

    /**
     * Test method for {@link JsonConfig#JsonConfig(ConfigOptions, JsonConfigOptions)}.
     */
    @Test
    void testOptionsConstructor() {
        final ConfigOptions baseOptions = mock(ConfigOptions.class);
        final JsonConfigOptions jsonOptions = mock(JsonConfigOptions.class);
        final String mockSeparator = "mock.separator";
        
        given(baseOptions.getParent()).willReturn(mockParent);
        given(baseOptions.getDecoder()).willReturn(mockDecoder);
        given(baseOptions.getDecorator()).willReturn(mockDecorator);
        given(jsonOptions.getJsonObject()).willReturn(mockObject);
        given(jsonOptions.getPropertySeparator()).willReturn(mockSeparator);
        
        assertThrows(NullPointerException.class, () -> {
            new JsonConfig(null, null);
        });
        assertThrows(NullPointerException.class, () -> {
            new JsonConfig(null, jsonOptions);
        });
        assertThrows(NullPointerException.class, () -> {
            new JsonConfig(baseOptions, null);
        });
        
        final JsonConfig config = new JsonConfig(baseOptions, jsonOptions);
        assertSame(mockParent, config.getParent());
        assertSame(mockDecoder, config.getDecoder());
        assertSame(mockDecorator, config.getDecorator());
        assertSame(mockObject, config.getJsonObject());
        assertSame(mockSeparator, config.getPropertySeparator());
    }

    /**
     * Test method for {@link JsonConfig#JsonConfig(Config, ValueDecoder, ValueDecorator, Properties).
     */
    @Test
    void testComponentsConstructor() {
        final String mockSeparator = "mock.separator";
        assertThrows(NullPointerException.class, () -> {
            new JsonConfig(null, null, null, null, null);
        });
        assertThrows(NullPointerException.class, () -> {
            new JsonConfig(mockParent, null, mockDecorator, mockObject, mockSeparator);
        });
        assertThrows(NullPointerException.class, () -> {
            new JsonConfig(mockParent, mockDecoder, null, mockObject, mockSeparator);
        });
        assertThrows(NullPointerException.class, () -> {
            new JsonConfig(mockParent, mockDecoder, mockDecorator, null, mockSeparator);
        });
        assertThrows(NullPointerException.class, () -> {
            new JsonConfig(mockParent, mockDecoder, mockDecorator, mockObject, null);
        });
        assertDoesNotThrow(() -> {
            new JsonConfig(null, mockDecoder, mockDecorator, mockObject, mockSeparator);
        });
        
        final JsonConfig config = new JsonConfig(
                mockParent,
                mockDecoder,
                mockDecorator,
                mockObject,
                mockSeparator);
        assertSame(mockParent, config.getParent());
        assertSame(mockDecoder, config.getDecoder());
        assertSame(mockDecorator, config.getDecorator());
        assertSame(mockObject, config.getJsonObject());
        assertSame(mockSeparator, config.getPropertySeparator());
    }
}
