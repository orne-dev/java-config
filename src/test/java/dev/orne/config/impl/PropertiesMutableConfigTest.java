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
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import dev.orne.config.Config;
import dev.orne.config.MutableConfigBuilder;
import dev.orne.config.PropertiesMutableConfigBuilder;
import dev.orne.config.ValueDecoder;
import dev.orne.config.ValueDecorator;
import dev.orne.config.ValueEncoder;

/**
 * Unit tests for {@link PropertiesMutableConfigImpl}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class PropertiesMutableConfigTest
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
    
    private static Properties testProperties;
    private static String testResource = "dev/orne/config/impl/test.resource.properties";
    private static File testFile;
    private static Path testPath;
    private static URL testUrl;

    private @Mock ValueDecoder mockDecoder;
    private @Mock ValueDecorator mockDecorator;
    private @Mock ValueEncoder mockEncoder;
    private @Mock Properties mockProperties;

    /**
     * Prepares the data needed by the tests.
     * @throws IOException Not thrown
     */
    @BeforeAll
    static void prepareTestData()
    throws IOException {
        testProperties = new Properties();
        testProperties.setProperty(TEST_COMMON_KEY, TEST_VALUES_TYPE);
        testProperties.setProperty(TEST_VALUES_KEY, TEST_VALUES_TYPE);
        final Properties fileProps = new Properties();
        fileProps.setProperty(TEST_COMMON_KEY, TEST_FILE_TYPE);
        fileProps.setProperty(TEST_FILE_KEY, TEST_FILE_TYPE);
        testFile = File.createTempFile(PropertiesConfigTest.class.getSimpleName(), ".properties");
        try (final FileOutputStream fileOS = new FileOutputStream(testFile)) {
            fileProps.store(fileOS, null);
        }
        testPath = testFile.toPath();
        testUrl = PropertiesConfigTest.class.getResource("test.url.properties");
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
    protected MutableConfigBuilder<?> createBuilder(
            final @NotNull Map<String, String> properties) {
        return Config.fromProperties()
                .add(properties)
                .mutable();
    }

    /**
     * Tests empty instance building.
     */
    @Test
    void testEmptyBuilder() {
        final PropertiesMutableConfigImpl config = assertInstanceOf(
                PropertiesMutableConfigImpl.class,
                Config.fromProperties()
                    .mutable()
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getProperties());
        assertTrue(config.getProperties().isEmpty());
    }

    /**
     * Tests instance building from properties.
     */
    @Test
    void testPropertiesBuilder() {
        final PropertiesMutableConfigImpl config = assertInstanceOf(
                PropertiesMutableConfigImpl.class,
                Config.fromProperties()
                    .mutable()
                    .add(testProperties)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final Properties properties = config.getProperties();
        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertNotSame(testProperties, properties);
        assertTrue(properties.containsKey(TEST_COMMON_KEY));
        assertEquals(TEST_VALUES_TYPE, properties.getProperty(TEST_COMMON_KEY));
        assertTrue(properties.containsKey(TEST_VALUES_KEY));
        assertEquals(TEST_VALUES_TYPE, properties.getProperty(TEST_VALUES_KEY));
        assertFalse(properties.containsKey(TEST_RESOURCE_KEY));
        assertNull(properties.getProperty(TEST_RESOURCE_KEY));
        assertFalse(properties.containsKey(TEST_FILE_KEY));
        assertNull(properties.getProperty(TEST_FILE_KEY));
        assertFalse(properties.containsKey(TEST_URL_KEY));
        assertNull(properties.getProperty(TEST_URL_KEY));
    }

    /**
     * Tests instance building from map.
     */
    @Test
    void testMapBuilder() {
        final Map<String, String> data = new HashMap<>();
        data.put(TEST_KEY, "testValue");
        final PropertiesMutableConfigImpl config = assertInstanceOf(
                PropertiesMutableConfigImpl.class,
                Config.fromProperties()
                    .mutable()
                    .add(data)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getProperties());
        assertFalse(config.getProperties().isEmpty());
        assertEquals(1, config.getProperties().size());
        assertEquals("testValue", config.getProperties().getProperty(TEST_KEY));
    }

    /**
     * Tests instance building from ClassPath resources.
     */
    @Test
    void testResourceBuilder() {
        final PropertiesMutableConfigImpl config = assertInstanceOf(
                PropertiesMutableConfigImpl.class,
                Config.fromProperties()
                    .mutable()
                    .load(testResource)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final Properties properties = config.getProperties();
        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertTrue(properties.containsKey(TEST_COMMON_KEY));
        assertEquals(TEST_RESOURCE_TYPE, properties.getProperty(TEST_COMMON_KEY));
        assertFalse(properties.containsKey(TEST_VALUES_KEY));
        assertNull(properties.getProperty(TEST_VALUES_KEY));
        assertTrue(properties.containsKey(TEST_RESOURCE_KEY));
        assertNotNull(properties.getProperty(TEST_RESOURCE_KEY));
        assertEquals(TEST_RESOURCE_TYPE, properties.getProperty(TEST_RESOURCE_KEY));
        assertFalse(properties.containsKey(TEST_FILE_KEY));
        assertNull(properties.getProperty(TEST_FILE_KEY));
        assertFalse(properties.containsKey(TEST_URL_KEY));
        assertNull(properties.getProperty(TEST_URL_KEY));
    }

    /**
     * Tests instance building from null ClassPath resource.
     */
    @Test
    void testNullResourceBuilder() {
        final PropertiesMutableConfigBuilder builder = Config.fromProperties()
                .mutable();
        assertThrows(NullPointerException.class, () -> builder.load((String) null));
    }

    /**
     * Tests instance building from missing ClassPath resources.
     */
    @Test
    void testMissingResourceBuilder() {
        final PropertiesMutableConfigImpl config = assertInstanceOf(
                PropertiesMutableConfigImpl.class,
                Config.fromProperties()
                    .mutable()
                    .load("non/existent/resource.properties")
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final Properties properties = config.getProperties();
        assertNotNull(properties);
        assertTrue(properties.isEmpty());
    }

    /**
     * Tests instance building from file.
     */
    @Test
    void testFileBuilder() {
        final PropertiesMutableConfigImpl config = assertInstanceOf(
                PropertiesMutableConfigImpl.class,
                Config.fromProperties()
                    .mutable()
                    .load(testFile)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final Properties properties = config.getProperties();
        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertTrue(properties.containsKey(TEST_COMMON_KEY));
        assertNotNull(properties.getProperty(TEST_COMMON_KEY));
        assertEquals(TEST_FILE_TYPE, properties.getProperty(TEST_COMMON_KEY));
        assertFalse(properties.containsKey(TEST_VALUES_KEY));
        assertNull(properties.getProperty(TEST_VALUES_KEY));
        assertFalse(properties.containsKey(TEST_RESOURCE_KEY));
        assertNull(properties.getProperty(TEST_RESOURCE_KEY));
        assertTrue(properties.containsKey(TEST_FILE_KEY));
        assertNotNull(properties.getProperty(TEST_FILE_KEY));
        assertEquals(TEST_FILE_TYPE, properties.getProperty(TEST_FILE_KEY));
        assertFalse(properties.containsKey(TEST_URL_KEY));
        assertNull(properties.getProperty(TEST_URL_KEY));
    }

    /**
     * Tests instance building from null file.
     */
    @Test
    void testNullFileBuilder() {
        final PropertiesMutableConfigBuilder builder = Config.fromProperties()
                .mutable();
        assertThrows(NullPointerException.class, () -> builder.load((File) null));
    }

    /**
     * Tests instance building from missing file.
     */
    @Test
    void testMissingFileBuilder() {
        final PropertiesMutableConfigImpl config = assertInstanceOf(
                PropertiesMutableConfigImpl.class,
                Config.fromProperties()
                    .mutable()
                    .load(new File("non/existent/resource.properties"))
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final Properties properties = config.getProperties();
        assertNotNull(properties);
        assertTrue(properties.isEmpty());
    }

    /**
     * Tests instance building from path.
     */
    @Test
    void testPathBuilder() {
        final PropertiesMutableConfigImpl config = assertInstanceOf(
                PropertiesMutableConfigImpl.class,
                Config.fromProperties()
                    .mutable()
                    .load(testPath)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final Properties properties = config.getProperties();
        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertTrue(properties.containsKey(TEST_COMMON_KEY));
        assertNotNull(properties.getProperty(TEST_COMMON_KEY));
        assertEquals(TEST_FILE_TYPE, properties.getProperty(TEST_COMMON_KEY));
        assertFalse(properties.containsKey(TEST_VALUES_KEY));
        assertNull(properties.getProperty(TEST_VALUES_KEY));
        assertFalse(properties.containsKey(TEST_RESOURCE_KEY));
        assertNull(properties.getProperty(TEST_RESOURCE_KEY));
        assertTrue(properties.containsKey(TEST_FILE_KEY));
        assertNotNull(properties.getProperty(TEST_FILE_KEY));
        assertEquals(TEST_FILE_TYPE, properties.getProperty(TEST_FILE_KEY));
        assertFalse(properties.containsKey(TEST_URL_KEY));
        assertNull(properties.getProperty(TEST_URL_KEY));
    }

    /**
     * Tests instance building from null path.
     */
    @Test
    void testNullPathBuilder() {
        final PropertiesMutableConfigBuilder builder = Config.fromProperties()
                .mutable();
        assertThrows(NullPointerException.class, () -> builder.load((Path) null));
    }

    /**
     * Tests instance building from missing path.
     */
    @Test
    void testMissingPathBuilder() {
        final PropertiesMutableConfigImpl config = assertInstanceOf(
                PropertiesMutableConfigImpl.class,
                Config.fromProperties()
                    .mutable()
                    .load(Paths.get("non/existent/path.properties"))
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final Properties properties = config.getProperties();
        assertNotNull(properties);
        assertTrue(properties.isEmpty());
    }

    /**
     * Tests instance building from URL.
     */
    @Test
    void testUrlBuilder() {
        final PropertiesMutableConfigImpl config = assertInstanceOf(
                PropertiesMutableConfigImpl.class,
                Config.fromProperties()
                    .mutable()
                    .load(testUrl)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final Properties properties = config.getProperties();
        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertTrue(properties.containsKey(TEST_COMMON_KEY));
        assertNotNull(properties.getProperty(TEST_COMMON_KEY));
        assertEquals(TEST_URL_TYPE, properties.getProperty(TEST_COMMON_KEY));
        assertFalse(properties.containsKey(TEST_VALUES_KEY));
        assertNull(properties.getProperty(TEST_VALUES_KEY));
        assertFalse(properties.containsKey(TEST_RESOURCE_KEY));
        assertNull(properties.getProperty(TEST_RESOURCE_KEY));
        assertFalse(properties.containsKey(TEST_FILE_KEY));
        assertNull(properties.getProperty(TEST_FILE_KEY));
        assertTrue(properties.containsKey(TEST_URL_KEY));
        assertNotNull(properties.getProperty(TEST_URL_KEY));
        assertEquals(TEST_URL_TYPE, properties.getProperty(TEST_URL_KEY));
    }

    /**
     * Tests instance building from null URL.
     */
    @Test
    void testNullUrlBuilder() {
        final PropertiesMutableConfigBuilder builder = Config.fromProperties()
                .mutable();
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
        final PropertiesMutableConfigImpl config = assertInstanceOf(
                PropertiesMutableConfigImpl.class,
                Config.fromProperties()
                    .mutable()
                    .load(missingUrl)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final Properties properties = config.getProperties();
        assertNotNull(properties);
        assertTrue(properties.isEmpty());
    }

    /**
     * Tests instance saving to OutputStream.
     */
    @Test
    void testSaveOutputStream()
    throws IOException {
        final PropertiesMutableConfigImpl config = assertInstanceOf(
                PropertiesMutableConfigImpl.class,
                Config.fromProperties()
                    .mutable()
                    .add(testProperties)
                    .build());
        final File tmp = File.createTempFile(PropertiesConfigTest.class.getSimpleName(), ".properties");
        try {
            try (final FileOutputStream fos = new FileOutputStream(tmp)) {
                config.save(fos);
            }
            final PropertiesConfigImpl reload = assertInstanceOf(
                    PropertiesConfigImpl.class,
                    Config.fromProperties()
                        .load(tmp)
                        .build());
            assertEquals(config.getProperties(), reload.getProperties());
        } finally {
            tmp.delete();
        }
    }

    /**
     * Tests instance saving to Writer.
     */
    @Test
    void testSaveWriter()
    throws IOException {
        final PropertiesMutableConfigImpl config = assertInstanceOf(
                PropertiesMutableConfigImpl.class,
                Config.fromProperties()
                    .mutable()
                    .add(testProperties)
                    .build());
        final File tmp = File.createTempFile(PropertiesConfigTest.class.getSimpleName(), ".properties");
        try {
            try (final FileOutputStream fos = new FileOutputStream(tmp);
                    final OutputStreamWriter writer = new OutputStreamWriter(fos)) {
                config.save(writer);
            }
            final PropertiesConfigImpl reload = assertInstanceOf(
                    PropertiesConfigImpl.class,
                    Config.fromProperties()
                        .load(tmp)
                        .build());
            assertEquals(config.getProperties(), reload.getProperties());
        } finally {
            tmp.delete();
        }
    }
}
