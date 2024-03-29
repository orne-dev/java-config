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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.collections.iterators.EnumerationIterator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code PropertiesConfig}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class PropertiesConfigTest {

    private static final String TEST_KEY = "test.key";

    /**
     * Test method for {@link PropertiesConfig#PropertiesConfig(Object...)} with
     * no sources.
     */
    @Test
    void testEmptyConstructor() {
        final PropertiesConfig config = new PropertiesConfig();
        assertNotNull(config.getProperties());
        assertTrue(config.getProperties().isEmpty());
    }

    /**
     * Test method for {@link PropertiesConfig#isEmpty()}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testIsEmpty()
    throws ConfigException {
        final Properties props = mock(Properties.class);
        final PropertiesConfig config = new PropertiesConfig(props);
        
        willReturn(true).given(props).isEmpty();
        
        final boolean result = config.isEmpty();
        assertTrue(result);
        
        then(props).should(times(1)).isEmpty();
        then(props).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PropertiesConfig#isEmpty()}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetKeys()
    throws ConfigException {
        final Properties props = mock(Properties.class);
        final PropertiesConfig config = new PropertiesConfig(props);
        
        @SuppressWarnings("unchecked")
        final Enumeration<Object> mockKeys = mock(Enumeration.class);
        willReturn(mockKeys).given(props).keys();
        
        final Iterator<String> result = config.getKeys();
        assertTrue(result instanceof EnumerationIterator);
        final EnumerationIterator enumIt = (EnumerationIterator) result;
        assertSame(mockKeys, enumIt.getEnumeration());
        
        then(props).should(times(1)).keys();
        then(props).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PropertiesConfig#containsParameter(String)} with
     * non existent property.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testContainsParameterFalse()
    throws ConfigException {
        final PropertiesConfig config = new PropertiesConfig();
        assertFalse(config.containsParameter(TEST_KEY));
    }

    /**
     * Test method for {@link PropertiesConfig#containsParameter(String)} with
     * existent property.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testContainsParameterTrue()
    throws ConfigException {
        final PropertiesConfig config = new PropertiesConfig();
        config.getProperties().setProperty(TEST_KEY, "somaValue");
        assertTrue(config.containsParameter(TEST_KEY));
    }

    /**
     * Test method for {@link PropertiesConfig#containsParameter(String)} with
     * existent {@code null} property.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testContainsParameterTrueNull()
    throws ConfigException {
        final PropertiesConfig config = new PropertiesConfig();
        config.getProperties().setProperty(TEST_KEY, PropertiesConfig.NULL);
        assertTrue(config.containsParameter(TEST_KEY));
    }

    /**
     * Test method for {@link PropertiesConfig#getStringParameter(String)} with
     * non existent property.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetStringMissing()
    throws ConfigException {
        System.clearProperty(TEST_KEY);
        final PropertiesConfig config = new PropertiesConfig();
        final String result = config.getStringParameter(TEST_KEY);
        assertNull(result);
    }

    /**
     * Test method for {@link PropertiesConfig#getStringParameter(String)} with
     * existent {@code null} property.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetStringNullPlaceholderDisabled()
    throws ConfigException {
        final PropertiesConfig config = new PropertiesConfig();
        config.getProperties().setProperty(TEST_KEY, PropertiesConfig.NULL);
        final String result = config.getStringParameter(TEST_KEY);
        assertNotNull(result);
        assertEquals(PropertiesConfig.NULL, result);
    }

    /**
     * Test method for {@link PropertiesConfig#getStringParameter(String)} with
     * existent {@code null} property.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetStringNullPlaceholderEnabled()
    throws ConfigException {
        final PropertiesConfig config = new PropertiesConfig();
        config.setNullPlaceholderEnabled(true);
        config.getProperties().setProperty(TEST_KEY, PropertiesConfig.NULL);
        final String result = config.getStringParameter(TEST_KEY);
        assertNull(result);
    }

    /**
     * Test method for {@link PropertiesConfig#getStringParameter(String)} with
     * existent non {@code null} property.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetString()
    throws ConfigException {
        final String expectedValue = "customValue";
        final PropertiesConfig config = new PropertiesConfig();
        config.getProperties().setProperty(TEST_KEY, expectedValue);
        final String result = config.getStringParameter(TEST_KEY);
        assertNotNull(result);
        assertEquals(expectedValue, result);
    }
}
