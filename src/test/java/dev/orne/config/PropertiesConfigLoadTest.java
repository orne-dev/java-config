/**
 * 
 */
package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
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
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code PropertiesConfig} loading process.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class PropertiesConfigLoadTest {

    private static final String TEST_COMMON_KEY = "test.type";
    private static final String TEST_VALUES_KEY = "test.values.type";
    private static final String TEST_RESOURCE_KEY = "test.resource.type";
    private static final String TEST_FILE_KEY = "test.file.type";
    private static final String TEST_URL_KEY = "test.url.type";
    
    private static final String TEST_VALUES_TYPE = "values";
    private static final String TEST_RESOURCE_TYPE = "resource";
    private static final String TEST_FILE_TYPE = "file";
    private static final String TEST_URL_TYPE = "url";
    
    private static Properties TEST_PROPERTIES;
    private static String TEST_RESOURCE = "dev/orne/config/test.resource.properties";
    private static File TEST_FILE;
    private static URL TEST_URL;

    /**
     * Prepares the data needed by the tests.
     * @throws IOException Not thrown
     */
    @BeforeAll
    public static void prepareTestData()
    throws IOException {
        TEST_PROPERTIES = new Properties();
        TEST_PROPERTIES.setProperty(TEST_COMMON_KEY, TEST_VALUES_TYPE);
        TEST_PROPERTIES.setProperty(TEST_VALUES_KEY, TEST_VALUES_TYPE);
        final Properties fileProps = new Properties();
        fileProps.setProperty(TEST_COMMON_KEY, TEST_FILE_TYPE);
        fileProps.setProperty(TEST_FILE_KEY, TEST_FILE_TYPE);
        TEST_FILE = File.createTempFile(PropertiesConfigLoadTest.class.getSimpleName(), ".properties");
        final FileOutputStream fileOS = new FileOutputStream(TEST_FILE);
        fileProps.store(fileOS, null);
        fileOS.close();
        TEST_URL = PropertiesConfigLoadTest.class.getResource("test.url.properties");
    }

    /**
     * Releases data resources created for tests.
     */
    @AfterAll
    public static void cleanTestData() {
        TEST_FILE.delete();
    }

    /**
     * Test method for {@link PropertiesConfig#PropertiesConfig(Object...)} with
     * values source.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testLoadProperties()
    throws ConfigException {
        final PropertiesConfig config = new PropertiesConfig(new Object[] { TEST_PROPERTIES });
        assertNotNull(config.getProperties());
        assertFalse(config.getProperties().isEmpty());
        assertTrue(config.containsParameter(TEST_COMMON_KEY));
        assertNotNull(config.getStringParameter(TEST_COMMON_KEY));
        assertEquals(TEST_VALUES_TYPE, config.getStringParameter(TEST_COMMON_KEY));
        assertTrue(config.containsParameter(TEST_VALUES_KEY));
        assertNotNull(config.getStringParameter(TEST_VALUES_KEY));
        assertEquals(TEST_VALUES_TYPE, config.getStringParameter(TEST_VALUES_KEY));
        assertFalse(config.containsParameter(TEST_RESOURCE_KEY));
        assertNull(config.getStringParameter(TEST_RESOURCE_KEY));
        assertFalse(config.containsParameter(TEST_FILE_KEY));
        assertNull(config.getStringParameter(TEST_FILE_KEY));
        assertFalse(config.containsParameter(TEST_URL_KEY));
        assertNull(config.getStringParameter(TEST_URL_KEY));
    }

    /**
     * Test method for {@link PropertiesConfig#PropertiesConfig(Object...)} with
     * classpath resource source.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testLoadResource()
    throws ConfigException {
        final PropertiesConfig config = new PropertiesConfig(TEST_RESOURCE);
        assertNotNull(config.getProperties());
        assertFalse(config.getProperties().isEmpty());
        assertTrue(config.containsParameter(TEST_COMMON_KEY));
        assertNotNull(config.getStringParameter(TEST_COMMON_KEY));
        assertEquals(TEST_RESOURCE_TYPE, config.getStringParameter(TEST_COMMON_KEY));
        assertFalse(config.containsParameter(TEST_VALUES_KEY));
        assertNull(config.getStringParameter(TEST_VALUES_KEY));
        assertTrue(config.containsParameter(TEST_RESOURCE_KEY));
        assertNotNull(config.getStringParameter(TEST_RESOURCE_KEY));
        assertEquals(TEST_RESOURCE_TYPE, config.getStringParameter(TEST_RESOURCE_KEY));
        assertFalse(config.containsParameter(TEST_FILE_KEY));
        assertNull(config.getStringParameter(TEST_FILE_KEY));
        assertFalse(config.containsParameter(TEST_URL_KEY));
        assertNull(config.getStringParameter(TEST_URL_KEY));
    }

    /**
     * Test method for {@link PropertiesConfig#PropertiesConfig(Object...)} with
     * classpath resource source that fails to load.
     * @throws IOException Should be captured
     */
    @Test
    public void testLoadResourceFail()
    throws IOException {
        final Properties mockProperties = mock(Properties.class);
        willThrow(new IOException())
            .given(mockProperties)
            .load(any(InputStream.class));
        final PropertiesConfig config = new PropertiesConfig(
                mockProperties, TEST_RESOURCE);
        assertSame(mockProperties, config.getProperties());
        then(mockProperties)
            .should(times(1))
            .load(any(InputStream.class));
    }

    /**
     * Test method for {@link PropertiesConfig#PropertiesConfig(Object...)} with
     * file source.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testLoadFile()
    throws ConfigException {
        final PropertiesConfig config = new PropertiesConfig(TEST_FILE);
        assertNotNull(config.getProperties());
        assertFalse(config.getProperties().isEmpty());
        assertTrue(config.containsParameter(TEST_COMMON_KEY));
        assertNotNull(config.getStringParameter(TEST_COMMON_KEY));
        assertEquals(TEST_FILE_TYPE, config.getStringParameter(TEST_COMMON_KEY));
        assertFalse(config.containsParameter(TEST_VALUES_KEY));
        assertNull(config.getStringParameter(TEST_VALUES_KEY));
        assertFalse(config.containsParameter(TEST_RESOURCE_KEY));
        assertNull(config.getStringParameter(TEST_RESOURCE_KEY));
        assertTrue(config.containsParameter(TEST_FILE_KEY));
        assertNotNull(config.getStringParameter(TEST_FILE_KEY));
        assertEquals(TEST_FILE_TYPE, config.getStringParameter(TEST_FILE_KEY));
        assertFalse(config.containsParameter(TEST_URL_KEY));
        assertNull(config.getStringParameter(TEST_URL_KEY));
    }

    /**
     * Test method for {@link PropertiesConfig#PropertiesConfig(Object...)} with
     * file source that fails to load.
     * @throws IOException Should be captured
     */
    @Test
    public void testLoadFileFail()
    throws IOException {
        final Properties mockProperties = mock(Properties.class);
        willThrow(new IOException())
            .given(mockProperties)
            .load(any(InputStream.class));
        final PropertiesConfig config = new PropertiesConfig(
                mockProperties, TEST_FILE);
        assertSame(mockProperties, config.getProperties());
        then(mockProperties)
            .should(times(1))
            .load(any(InputStream.class));
    }

    /**
     * Test method for {@link PropertiesConfig#PropertiesConfig(Object...)} with
     * URL.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testLoadURL()
    throws ConfigException {
        final PropertiesConfig config = new PropertiesConfig(TEST_URL);
        assertNotNull(config.getProperties());
        assertFalse(config.getProperties().isEmpty());
        assertTrue(config.containsParameter(TEST_COMMON_KEY));
        assertNotNull(config.getStringParameter(TEST_COMMON_KEY));
        assertEquals(TEST_URL_TYPE, config.getStringParameter(TEST_COMMON_KEY));
        assertFalse(config.containsParameter(TEST_VALUES_KEY));
        assertNull(config.getStringParameter(TEST_VALUES_KEY));
        assertFalse(config.containsParameter(TEST_RESOURCE_KEY));
        assertNull(config.getStringParameter(TEST_RESOURCE_KEY));
        assertFalse(config.containsParameter(TEST_FILE_KEY));
        assertNull(config.getStringParameter(TEST_FILE_KEY));
        assertTrue(config.containsParameter(TEST_URL_KEY));
        assertNotNull(config.getStringParameter(TEST_URL_KEY));
        assertEquals(TEST_URL_TYPE, config.getStringParameter(TEST_URL_KEY));
    }

    /**
     * Test method for {@link PropertiesConfig#PropertiesConfig(Object...)} with
     * URL that raises an IOException.
     * 
     * @throws IOException Should be captured
     */
    @Test
    public void testLoadURLFail()
    throws IOException {
        final URL faillingURL = new URL("http://should.fail.test/");
        
        final PropertiesConfig config = new PropertiesConfig(faillingURL);
        assertNotNull(config.getProperties());
        assertTrue(config.getProperties().isEmpty());
    }

    /**
     * Test method for {@link PropertiesConfig#PropertiesConfig(Object...)} with
     * an unsupported type.
     */
    @Test
    public void testLoadUnsupported() {
        final InputStream is = mock(InputStream.class);

        final PropertiesConfig config = new PropertiesConfig(is);
        assertNotNull(config.getProperties());
        assertTrue(config.getProperties().isEmpty());

        then(is).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PropertiesConfig#PropertiesConfig(Object...)} with
     * multiple sources.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testLoadMultiple()
    throws ConfigException {
        final PropertiesConfig config = new PropertiesConfig(
                new Object[] { TEST_PROPERTIES, TEST_RESOURCE, TEST_FILE, TEST_URL });
        assertNotNull(config.getProperties());
        assertFalse(config.getProperties().isEmpty());
        assertTrue(config.containsParameter(TEST_COMMON_KEY));
        assertNotNull(config.getStringParameter(TEST_COMMON_KEY));
        assertEquals(TEST_URL_TYPE, config.getStringParameter(TEST_COMMON_KEY));
        assertTrue(config.containsParameter(TEST_VALUES_KEY));
        assertNotNull(config.getStringParameter(TEST_VALUES_KEY));
        assertEquals(TEST_VALUES_TYPE, config.getStringParameter(TEST_VALUES_KEY));
        assertTrue(config.containsParameter(TEST_RESOURCE_KEY));
        assertNotNull(config.getStringParameter(TEST_RESOURCE_KEY));
        assertEquals(TEST_RESOURCE_TYPE, config.getStringParameter(TEST_RESOURCE_KEY));
        assertTrue(config.containsParameter(TEST_FILE_KEY));
        assertNotNull(config.getStringParameter(TEST_FILE_KEY));
        assertEquals(TEST_FILE_TYPE, config.getStringParameter(TEST_FILE_KEY));
        assertTrue(config.containsParameter(TEST_URL_KEY));
        assertNotNull(config.getStringParameter(TEST_URL_KEY));
        assertEquals(TEST_URL_TYPE, config.getStringParameter(TEST_URL_KEY));
    }

    /**
     * Test method for {@link PropertiesConfig#PropertiesConfig(Object...)} with
     * an instance of Iterable of all simple types.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testLoadIterable()
    throws ConfigException {
        final PropertiesConfig config = new PropertiesConfig(
                Arrays.asList(TEST_PROPERTIES, TEST_RESOURCE, TEST_FILE, TEST_URL));
        assertNotNull(config.getProperties());
        assertFalse(config.getProperties().isEmpty());
        assertTrue(config.containsParameter(TEST_COMMON_KEY));
        assertNotNull(config.getStringParameter(TEST_COMMON_KEY));
        assertEquals(TEST_URL_TYPE, config.getStringParameter(TEST_COMMON_KEY));
        assertTrue(config.containsParameter(TEST_VALUES_KEY));
        assertNotNull(config.getStringParameter(TEST_VALUES_KEY));
        assertEquals(TEST_VALUES_TYPE, config.getStringParameter(TEST_VALUES_KEY));
        assertTrue(config.containsParameter(TEST_RESOURCE_KEY));
        assertNotNull(config.getStringParameter(TEST_RESOURCE_KEY));
        assertEquals(TEST_RESOURCE_TYPE, config.getStringParameter(TEST_RESOURCE_KEY));
        assertTrue(config.containsParameter(TEST_FILE_KEY));
        assertNotNull(config.getStringParameter(TEST_FILE_KEY));
        assertEquals(TEST_FILE_TYPE, config.getStringParameter(TEST_FILE_KEY));
        assertTrue(config.containsParameter(TEST_URL_KEY));
        assertNotNull(config.getStringParameter(TEST_URL_KEY));
        assertEquals(TEST_URL_TYPE, config.getStringParameter(TEST_URL_KEY));
    }
}
