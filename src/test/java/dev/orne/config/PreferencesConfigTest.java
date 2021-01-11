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

import java.util.Iterator;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apache.commons.collections.iterators.ObjectArrayIterator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code PreferencesConfig}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 */
@Tag("ut")
class PreferencesConfigTest {

    private static final String TEST_KEY = "test.key";

    /**
     * Restores the NOP root {@code Preferences} nodes.
     */
    @AfterEach
    public void restoreTestPreferences() {
        TestPreferencesFactory.setSystemRoot(new TestPreferencesFactory.NopPreferences());
        TestPreferencesFactory.setUserRoot(new TestPreferencesFactory.NopPreferences());
    }

    /**
     * Test method for {@link PreferencesConfig#PreferencesConfig()}.
     */
    @Test
    void testConstructor() {
        final Preferences preferences = mock(Preferences.class);
        TestPreferencesFactory.setUserRoot(preferences);
        final PreferencesConfig config = new PreferencesConfig();
        assertSame(preferences, config.getPreferences());
    }

    /**
     * Test method for {@link PreferencesConfig#PreferencesConfig(boolean)}.
     */
    @Test
    void testConstructorUser() {
        final Preferences preferences = mock(Preferences.class);
        TestPreferencesFactory.setUserRoot(preferences);
        final PreferencesConfig config = new PreferencesConfig(false);
        assertSame(preferences, config.getPreferences());
    }

    /**
     * Test method for {@link PreferencesConfig#PreferencesConfig(boolean)}.
     */
    @Test
    void testConstructorSystem() {
        final Preferences preferences = mock(Preferences.class);
        TestPreferencesFactory.setSystemRoot(preferences);
        final PreferencesConfig config = new PreferencesConfig(true);
        assertSame(preferences, config.getPreferences());
    }

    /**
     * Test method for {@link PreferencesConfig#PreferencesConfig(Class)}.
     */
    @Test
    void testConstructorClass() {
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setUserRoot(rootPreferences);
        final Preferences preferences = mock(Preferences.class);
        
        willReturn(preferences).given(rootPreferences).node("/dev/orne/config");
        
        final PreferencesConfig config = new PreferencesConfig(PreferencesConfigTest.class);
        assertSame(preferences, config.getPreferences());
        
        then(rootPreferences).should(times(1)).node("/dev/orne/config");
        then(rootPreferences).shouldHaveNoMoreInteractions();
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#PreferencesConfig(boolean, Class)}.
     */
    @Test
    void testConstructorUserClass() {
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setUserRoot(rootPreferences);
        final Preferences preferences = mock(Preferences.class);
        
        willReturn(preferences).given(rootPreferences).node("/dev/orne/config");
        
        final PreferencesConfig config = new PreferencesConfig(false, PreferencesConfigTest.class);
        assertSame(preferences, config.getPreferences());
        
        then(rootPreferences).should(times(1)).node("/dev/orne/config");
        then(rootPreferences).shouldHaveNoMoreInteractions();
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#PreferencesConfig(boolean, Class)}.
     */
    @Test
    void testConstructorSystemClass() {
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setSystemRoot(rootPreferences);
        final Preferences preferences = mock(Preferences.class);
        
        willReturn(preferences).given(rootPreferences).node("/dev/orne/config");
        
        final PreferencesConfig config = new PreferencesConfig(true, PreferencesConfigTest.class);
        assertSame(preferences, config.getPreferences());
        
        then(rootPreferences).should(times(1)).node("/dev/orne/config");
        then(rootPreferences).shouldHaveNoMoreInteractions();
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#PreferencesConfig(String)}.
     */
    @Test
    void testConstructorPath() {
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setUserRoot(rootPreferences);
        final Preferences preferences = mock(Preferences.class);
        
        final String path = "/test/path";
        willReturn(preferences).given(rootPreferences).node(path);
        
        final PreferencesConfig config = new PreferencesConfig(path);
        assertSame(preferences, config.getPreferences());
        
        then(rootPreferences).should(times(1)).node(path);
        then(rootPreferences).shouldHaveNoMoreInteractions();
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#PreferencesConfig(boolean, String)}.
     */
    @Test
    void testConstructorUserPath() {
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setUserRoot(rootPreferences);
        final Preferences preferences = mock(Preferences.class);
        
        final String path = "/test/path";
        willReturn(preferences).given(rootPreferences).node(path);
        
        final PreferencesConfig config = new PreferencesConfig(false, path);
        assertSame(preferences, config.getPreferences());
        
        then(rootPreferences).should(times(1)).node(path);
        then(rootPreferences).shouldHaveNoMoreInteractions();
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#PreferencesConfig(boolean, String)}.
     */
    @Test
    void testConstructorSystemPath() {
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setSystemRoot(rootPreferences);
        final Preferences preferences = mock(Preferences.class);
        
        final String path = "/test/path";
        willReturn(preferences).given(rootPreferences).node(path);
        
        final PreferencesConfig config = new PreferencesConfig(true, path);
        assertSame(preferences, config.getPreferences());
        
        then(rootPreferences).should(times(1)).node(path);
        then(rootPreferences).shouldHaveNoMoreInteractions();
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#PreferencesConfig(Class, String)}.
     */
    @Test
    void testConstructorClassPath() {
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setUserRoot(rootPreferences);
        final Preferences classPreferences = mock(Preferences.class);
        final Preferences preferences = mock(Preferences.class);
        
        final String classPackage = "/dev/orne/config";
        final String path = "/test/path";
        willReturn(classPreferences).given(rootPreferences).node(classPackage);
        willReturn(preferences).given(classPreferences).node(path);
        
        final PreferencesConfig config = new PreferencesConfig(PreferencesConfigTest.class, path);
        assertSame(preferences, config.getPreferences());
        
        then(rootPreferences).should(times(1)).node(classPackage);
        then(rootPreferences).shouldHaveNoMoreInteractions();
        then(classPreferences).should(times(1)).node(path);
        then(classPreferences).shouldHaveNoMoreInteractions();
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#PreferencesConfig(boolean, Class, String)}.
     */
    @Test
    void testConstructorUserClassPath() {
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setUserRoot(rootPreferences);
        final Preferences classPreferences = mock(Preferences.class);
        final Preferences preferences = mock(Preferences.class);
        
        final String classPackage = "/dev/orne/config";
        final String path = "/test/path";
        willReturn(classPreferences).given(rootPreferences).node(classPackage);
        willReturn(preferences).given(classPreferences).node(path);
        
        final PreferencesConfig config = new PreferencesConfig(false, PreferencesConfigTest.class, path);
        assertSame(preferences, config.getPreferences());
        
        then(rootPreferences).should(times(1)).node(classPackage);
        then(rootPreferences).shouldHaveNoMoreInteractions();
        then(classPreferences).should(times(1)).node(path);
        then(classPreferences).shouldHaveNoMoreInteractions();
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#PreferencesConfig(boolean, Class, String)}.
     */
    @Test
    void testConstructorSystemClassPath() {
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setSystemRoot(rootPreferences);
        final Preferences classPreferences = mock(Preferences.class);
        final Preferences preferences = mock(Preferences.class);
        
        final String classPackage = "/dev/orne/config";
        final String path = "/test/path";
        willReturn(classPreferences).given(rootPreferences).node(classPackage);
        willReturn(preferences).given(classPreferences).node(path);
        
        final PreferencesConfig config = new PreferencesConfig(true, PreferencesConfigTest.class, path);
        assertSame(preferences, config.getPreferences());
        
        then(rootPreferences).should(times(1)).node(classPackage);
        then(rootPreferences).shouldHaveNoMoreInteractions();
        then(classPreferences).should(times(1)).node(path);
        then(classPreferences).shouldHaveNoMoreInteractions();
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#PreferencesConfig(Preferences)}.
     */
    @Test
    void testConstructorPreferences() {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        assertSame(preferences, config.getPreferences());
    }

    /**
     * Test method for {@link PreferencesConfig#isEmpty()}.
     * @throws ConfigException Shouldn't happen
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testIsEmpty()
    throws ConfigException, BackingStoreException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        willReturn(new String[0]).given(preferences).keys();
        
        final boolean result = config.isEmpty();
        assertTrue(result);
        
        then(preferences).should(times(1)).keys();
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#isEmpty()}.
     * @throws ConfigException Shouldn't happen
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testIsEmptyFalse()
    throws ConfigException, BackingStoreException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        final String[] mockKeys = new String[] { TEST_KEY };
        willReturn(mockKeys).given(preferences).keys();
        
        final boolean result = config.isEmpty();
        assertFalse(result);
        
        then(preferences).should(times(1)).keys();
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#isEmpty()}.
     * @throws ConfigException Shouldn't happen
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testIsEmptyBackingStoreException()
    throws ConfigException, BackingStoreException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        final BackingStoreException mockException = new BackingStoreException("mock");
        willThrow(mockException).given(preferences).keys();
        
        final ConfigException result = assertThrows(ConfigException.class, () -> {
            config.isEmpty();
        });
        assertSame(mockException, result.getCause());
        
        then(preferences).should(times(1)).keys();
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#isEmpty()}.
     * @throws ConfigException Shouldn't happen
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testIsEmptyIllegalStateException()
    throws ConfigException, BackingStoreException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        final IllegalStateException mockException = new IllegalStateException();
        willThrow(mockException).given(preferences).keys();
        
        final ConfigException result = assertThrows(ConfigException.class, () -> {
            config.isEmpty();
        });
        assertSame(mockException, result.getCause());
        
        then(preferences).should(times(1)).keys();
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#isEmpty()}.
     * @throws ConfigException Shouldn't happen
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testGetKeys()
    throws ConfigException, BackingStoreException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        final String[] mockKeys = new String[] {
                TEST_KEY,
                "itKey1",
                "itKey2",
                "itKey3",
                "itKey4"
        };
        willReturn(mockKeys).given(preferences).keys();
        
        final Iterator<String> result = config.getKeys();
        assertTrue(result instanceof ObjectArrayIterator);
        final ObjectArrayIterator arrayIt = (ObjectArrayIterator) result;
        assertSame(mockKeys, arrayIt.getArray());
        assertEquals(0, arrayIt.getStartIndex());
        assertEquals(mockKeys.length, arrayIt.getEndIndex());
        
        then(preferences).should(times(1)).keys();
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#isEmpty()}.
     * @throws ConfigException Shouldn't happen
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testGetKeysBackingStoreException()
    throws ConfigException, BackingStoreException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        final BackingStoreException mockException = new BackingStoreException("mock");
        willThrow(mockException).given(preferences).keys();
        
        final ConfigException result = assertThrows(ConfigException.class, () -> {
            config.getKeys();
        });
        assertSame(mockException, result.getCause());
        
        then(preferences).should(times(1)).keys();
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#isEmpty()}.
     * @throws ConfigException Shouldn't happen
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testGetKeysIllegalStateException()
    throws ConfigException, BackingStoreException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        final IllegalStateException mockException = new IllegalStateException();
        willThrow(mockException).given(preferences).keys();
        
        final ConfigException result = assertThrows(ConfigException.class, () -> {
            config.getKeys();
        });
        assertSame(mockException, result.getCause());
        
        then(preferences).should(times(1)).keys();
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#containsParameter(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testContainsParameter()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        final String value = "mock value";
        willReturn(value).given(preferences).get(TEST_KEY, null);
        
        final boolean result = config.containsParameter(TEST_KEY);
        assertTrue(result);
        
        then(preferences).should(times(1)).get(TEST_KEY, null);
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#containsParameter(String)}
     * with {@code null} key.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testContainsParameterNullKey()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        assertThrows(NullPointerException.class, () -> {
            config.containsParameter(null);
        });
        
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#containsParameter(String)}
     * with blank key.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testContainsParameterBlankKey()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        assertThrows(IllegalArgumentException.class, () -> {
            config.containsParameter("");
        });
        
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#containsParameter(String)}
     * with {@code null} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testContainsParameterNullValue()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        willReturn(null).given(preferences).get(TEST_KEY, null);
        
        final boolean result = config.containsParameter(TEST_KEY);
        assertFalse(result);
        
        then(preferences).should(times(1)).get(TEST_KEY, null);
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#containsParameter(String)}
     * when {@code Preferences} throws an error.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testContainsParameterError()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        willThrow(IllegalStateException.class).given(preferences).get(TEST_KEY, null);
        
        assertThrows(ConfigException.class, () -> {
            config.containsParameter(TEST_KEY);
        });
        
        then(preferences).should(times(1)).get(TEST_KEY, null);
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#getStringParameter(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetStringParameterValue()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        final String value = "mock value";
        willReturn(value).given(preferences).get(TEST_KEY, null);
        
        final String result = config.getStringParameter(TEST_KEY);
        assertSame(value, result);
        
        then(preferences).should(times(1)).get(TEST_KEY, null);
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#getStringParameter(String)}
     * with {@code null} key.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetStringParameterNullKey()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        assertThrows(NullPointerException.class, () -> {
            config.getStringParameter(null);
        });
        
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#getStringParameter(String)}
     * with blank key.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetStringParameterBlankKey()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        assertThrows(IllegalArgumentException.class, () -> {
            config.getStringParameter("");
        });
        
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#getStringParameter(String)}
     * with {@code null} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetStringParameterNullValue()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        willReturn(null).given(preferences).get(TEST_KEY, null);
        
        final String result = config.getStringParameter(TEST_KEY);
        assertNull(result);
        
        then(preferences).should(times(1)).get(TEST_KEY, null);
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#getStringParameter(String)}
     * when {@code Preferences} throws an error.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetStringParameterError()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        willThrow(IllegalStateException.class).given(preferences).get(TEST_KEY, null);
        
        assertThrows(ConfigException.class, () -> {
            config.getStringParameter(TEST_KEY);
        });
        
        then(preferences).should(times(1)).get(TEST_KEY, null);
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#setRawValue(String, String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testSetRawValue()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        final String value = "mock value";
        config.setRawValue(TEST_KEY, value);
        
        then(preferences).should(times(1)).put(TEST_KEY, value);
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#setRawValue(String, String)}
     * with {@code null} key.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testSetRawValueNullKey()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        final String value = "mock value";
        assertThrows(NullPointerException.class, () -> {
            config.setRawValue(null, value);
        });
        
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#setRawValue(String, String)}
     * with blank key.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testSetRawValueBlankKey()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        final String value = "mock value";
        assertThrows(IllegalArgumentException.class, () -> {
            config.setRawValue("", value);
        });
        
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#setRawValue(String, String)}
     * with {@code null} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testSetRawValueNullValue()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        config.setRawValue(TEST_KEY, null);
        
        then(preferences).should(times(1)).remove(TEST_KEY);
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#setRawValue(String, String)}
     * when {@code Preferences} throws an error.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testSetRawValueError()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        final String value = "mock value";
        willThrow(IllegalStateException.class).given(preferences).put(TEST_KEY, value);
        
        assertThrows(ConfigException.class, () -> {
            config.setRawValue(TEST_KEY, value);
        });
        
        then(preferences).should(times(1)).put(TEST_KEY, value);
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#remove(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testRemove()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        config.remove(TEST_KEY);
        
        then(preferences).should(times(1)).remove(TEST_KEY);
        then(preferences).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#remove(String)}
     * with {@code null} key.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testRemoveNullKey()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        assertThrows(NullPointerException.class, () -> {
            config.remove(null);
        });
        
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#remove(String)}
     * with blank key.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testRemoveBlankKey()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        assertThrows(IllegalArgumentException.class, () -> {
            config.remove("");
        });
        
        then(preferences).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link PreferencesConfig#remove(String)}
     * when {@code Preferences} throws an error.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testRemoveError()
    throws ConfigException {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfig config = new PreferencesConfig(preferences);
        
        willThrow(IllegalStateException.class).given(preferences).remove(TEST_KEY);
        
        assertThrows(ConfigException.class, () -> {
            config.remove(TEST_KEY);
        });
        
        then(preferences).should(times(1)).remove(TEST_KEY);
        then(preferences).shouldHaveNoMoreInteractions();
    }
}
