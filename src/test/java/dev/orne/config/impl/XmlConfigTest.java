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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.w3c.dom.Document;

import dev.orne.config.ConfigBuilder;
import dev.orne.config.ValueDecoder;
import dev.orne.config.ValueDecorator;
import dev.orne.config.XmlConfigBuilder;

/**
 * Unit tests for {@link XmlConfigImpl}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class XmlConfigTest
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
    
    private static DocumentBuilder docBuilder;
    private static Document testValues;
    private static String testResource = "dev/orne/config/impl/test.resource.xml";
    private static File testFile;
    private static Path testPath;
    private static URL testUrl;

    private @Mock ValueDecoder mockDecoder;
    private @Mock ValueDecorator mockDecorator;

    /**
     * Prepares the data needed by the tests.
     */
    @BeforeAll
    static void prepareTestData()
    throws IOException, TransformerException, ParserConfigurationException {
        docBuilder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
        testValues = docBuilder.newDocument();
        testValues.appendChild(testValues.createElement("config"));
        XmlUtils.setValue(
                testValues,
                TEST_COMMON_KEY,
                XmlConfigBuilder.DEFAULT_SEPARATOR,
                XmlConfigBuilder.DEFAULT_ATTRIBUTE_PREFIX,
                TEST_VALUES_TYPE);
        XmlUtils.setValue(
                testValues,
                TEST_VALUES_KEY,
                XmlConfigBuilder.DEFAULT_SEPARATOR,
                XmlConfigBuilder.DEFAULT_ATTRIBUTE_PREFIX,
                TEST_VALUES_TYPE);
        final Document fileValues = docBuilder.newDocument();
        fileValues.appendChild(fileValues.createElement("config"));
        XmlUtils.setValue(
                fileValues,
                TEST_COMMON_KEY,
                XmlConfigBuilder.DEFAULT_SEPARATOR,
                XmlConfigBuilder.DEFAULT_ATTRIBUTE_PREFIX,
                TEST_FILE_TYPE);
        XmlUtils.setValue(
                fileValues,
                TEST_FILE_KEY,
                XmlConfigBuilder.DEFAULT_SEPARATOR,
                XmlConfigBuilder.DEFAULT_ATTRIBUTE_PREFIX,
                TEST_FILE_TYPE);
        testFile = File.createTempFile(XmlConfigTest.class.getSimpleName(), ".xml");
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer();
        try (final FileOutputStream fileOS = new FileOutputStream(testFile)) {
            final DOMSource source = new DOMSource(fileValues);
            final StreamResult result = new StreamResult(fileOS);
            transformer.transform(source, result);
        }
        testPath = testFile.toPath();
        testUrl = XmlConfigTest.class.getResource("test.url.xml");
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
        return ConfigBuilder.fromXmlFiles()
                .withEmptyDocument("config")
                .add(properties);
    }

    /**
     * Tests empty instance building.
     */
    @Test
    void testEmptyBuilder() {
        final XmlConfigImpl config = assertInstanceOf(
                XmlConfigImpl.class,
                ConfigBuilder.fromXmlFiles()
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getDocument());
        assertTrue(config.isEmpty());
    }

    /**
     * Tests instance building from from custom document.
     */
    @Test
    void testDocBuilder() {
        final XmlConfigImpl config = assertInstanceOf(
                XmlConfigImpl.class,
                ConfigBuilder.fromXmlFiles()
                    .add(testValues)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getDocument());
        assertNotSame(testValues, config.getDocument());
        assertFalse(config.isEmpty());
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
     * Tests instance building from ClassPath resource.
     */
    @Test
    void testResourceBuilder() {
        final XmlConfigImpl config = assertInstanceOf(
                XmlConfigImpl.class,
                ConfigBuilder.fromXmlFiles()
                    .load(testResource)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getDocument());
        assertFalse(config.isEmpty());
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
     * Tests instance building from null ClassPath resource.
     */
    @Test
    void testNullResourceBuilder() {
        final XmlConfigBuilder<?> builder = ConfigBuilder.fromXmlFiles();
        assertThrows(NullPointerException.class, () -> builder.load((String) null));
    }

    /**
     * Tests instance building from missing ClassPath resource.
     */
    @Test
    void testMissingResourceBuilder() {
        final XmlConfigImpl config = assertInstanceOf(
                XmlConfigImpl.class,
                ConfigBuilder.fromXmlFiles()
                    .load("non/existent/resource.xml")
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final Document doc = config.getDocument();
        assertNotNull(doc);
        assertTrue(config.isEmpty());
    }

    /**
     * Tests instance building from file.
     */
    @Test
    void testFileBuilder() {
        final XmlConfigImpl config = assertInstanceOf(
                XmlConfigImpl.class,
                ConfigBuilder.fromXmlFiles()
                    .load(testFile)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getDocument());
        assertFalse(config.isEmpty());
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
        final XmlConfigBuilder<?> builder = ConfigBuilder.fromXmlFiles();
        assertThrows(NullPointerException.class, () -> builder.load((File) null));
    }

    /**
     * Tests instance building from missing file.
     */
    @Test
    void testMissingFileBuilder() {
        final XmlConfigImpl config = assertInstanceOf(
                XmlConfigImpl.class,
                ConfigBuilder.fromXmlFiles()
                    .load(new File("non/existent/resource.xml"))
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final Document doc = config.getDocument();
        assertNotNull(doc);
        assertTrue(config.isEmpty());
    }

    /**
     * Tests instance building from path.
     */
    @Test
    void testPathBuilder() {
        final XmlConfigImpl config = assertInstanceOf(
                XmlConfigImpl.class,
                ConfigBuilder.fromXmlFiles()
                    .load(testPath)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getDocument());
        assertFalse(config.isEmpty());
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
        final XmlConfigBuilder<?> builder = ConfigBuilder.fromXmlFiles();
        assertThrows(NullPointerException.class, () -> builder.load((Path) null));
    }

    /**
     * Tests instance building from missing path.
     */
    @Test
    void testMissingPathBuilder() {
        final XmlConfigImpl config = assertInstanceOf(
                XmlConfigImpl.class,
                ConfigBuilder.fromXmlFiles()
                    .load(Paths.get("non/existent/path.xml"))
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final Document doc = config.getDocument();
        assertNotNull(doc);
        assertTrue(config.isEmpty());
    }

    /**
     * Tests instance building from URL.
     */
    @Test
    void testUrlBuilder() {
        final XmlConfigImpl config = assertInstanceOf(
                XmlConfigImpl.class,
                ConfigBuilder.fromXmlFiles()
                    .load(testUrl)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getDocument());
        assertFalse(config.isEmpty());
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
        final XmlConfigBuilder<?> builder = ConfigBuilder.fromXmlFiles();
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
        final XmlConfigImpl config = assertInstanceOf(
                XmlConfigImpl.class,
                ConfigBuilder.fromXmlFiles()
                    .load(missingUrl)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        final Document doc = config.getDocument();
        assertNotNull(doc);
        assertTrue(config.isEmpty());
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
        final XmlConfigImpl config = assertInstanceOf(
                XmlConfigImpl.class,
                ConfigBuilder.fromXmlFiles()
                    .withEmptyDocument("config")
                    .add(defaultSeparatorValues)
                    .withSeparator(customSeparator)
                    .add(customSeparatorValues)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertNotNull(config.getDocument());
        assertFalse(config.isEmpty());
        assertEquals(customSeparator, config.getPropertySeparator());
        assertTrue(config.contains(defaultSeparatorNewKey));
        assertEquals(defaultSeparatorValue, config.get(defaultSeparatorNewKey));
        assertTrue(config.contains(customSeparatorKey));
        assertEquals(customSeparatorValue, config.get(customSeparatorKey));
    }
}
