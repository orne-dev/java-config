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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests of {@code Config} as functional interface.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class ConfigTest {

    protected static final String TEST_KEY = "test.key";
    protected static final String NUMBER_KEY = "number.key";
    protected static final String BOOLEAN_KEY = "boolean.key";
    protected static final String TEST_MISSING_KEY = "test.missing.key";
    protected static final String TEST_VALUE = "testValue";
    protected static final String NUMBER_VALUE = "1000";
    protected static final String BOOLEAN_VALUE = "true";
    protected static final String DEFAULT_VALUE = "defaultValue";

    /**
     * Tests for default {@link Config#getParent()} implementation.
     */
    @Test
    void testParent() {
        final Config config = key -> {
            throw new AssertionError("Should not be called");
        };
        assertNull(config.getParent());
    }

    /**
     * Tests for default {@link Config#contains(String)} implementation.
     */
    @Test
    void testContains() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, TEST_VALUE);
        properties.put(NUMBER_KEY, NUMBER_VALUE);
        properties.put(BOOLEAN_KEY, BOOLEAN_VALUE);
        final Config config = properties::get;
        assertTrue(config.contains(TEST_KEY));
        assertTrue(config.contains(NUMBER_KEY));
        assertTrue(config.contains(BOOLEAN_KEY));
        assertFalse(config.contains(TEST_MISSING_KEY));
    }

    /**
     * Tests for default {@link Config#isEmpty(String)} and keys related
     * methods implementations.
     */
    @Test
    void testKeyMethodsDefault() {
        final Config config = key -> {
            throw new AssertionError("Should not be called");
        };
        assertThrows(NonIterableConfigException.class, config::isEmpty);
        assertThrows(NonIterableConfigException.class, config::getKeys);
        assertThrows(NonIterableConfigException.class, () -> config.getKeys("number"));
        assertThrows(NonIterableConfigException.class, () -> config.getKeys(key -> key.endsWith("lean.key")));
    }

    /**
     * Tests for default {@link Config#isEmpty(String)} and keys related
     * methods implementations.
     */
    @Test
    void testKeyMethodsOverride() {
        final Config config = new Config() {
            @Override
            public String get(@NotBlank String key) {
                throw new AssertionError("Should not be called");
            }
            @Override
            public @NotNull Stream<String> getKeys() {
                return Stream.of(TEST_KEY, NUMBER_KEY, BOOLEAN_KEY);
            }
        };
        assertFalse(config.isEmpty());
        assertEquals(Arrays.asList(TEST_KEY, NUMBER_KEY, BOOLEAN_KEY), config.getKeys().collect(Collectors.toList()));
        assertEquals(Arrays.asList(NUMBER_KEY), config.getKeys("number").collect(Collectors.toList()));
        assertEquals(Arrays.asList(BOOLEAN_KEY), config.getKeys(key -> key.endsWith("lean.key")).collect(Collectors.toList()));
    }

    /**
     * Tests for default {@link Config#isEmpty(String)} and keys related
     * methods implementations.
     */
    @Test
    void testKeyMethodsEmptyOverride() {
        final Config config = new Config() {
            @Override
            public String get(@NotBlank String key) {
                throw new AssertionError("Should not be called");
            }
            @Override
            public @NotNull Stream<String> getKeys() {
                return Stream.of();
            }
        };
        assertTrue(config.isEmpty());
        assertTrue(config.getKeys().collect(Collectors.toList()).isEmpty());
        assertTrue(config.getKeys("number").collect(Collectors.toList()).isEmpty());
        assertTrue(config.getKeys(key -> key.endsWith("lean.key")).collect(Collectors.toList()).isEmpty());
    }

    /**
     * Tests for default {@link Config#getUndecored(String)} implementation.
     */
    @Test
    void testGetUndecored() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, TEST_VALUE);
        properties.put(NUMBER_KEY, NUMBER_VALUE);
        properties.put(BOOLEAN_KEY, BOOLEAN_VALUE);
        final Config config = properties::get;
        assertEquals(TEST_VALUE, config.getUndecored(TEST_KEY));
        assertEquals(NUMBER_VALUE, config.getUndecored(NUMBER_KEY));
        assertEquals(BOOLEAN_VALUE, config.getUndecored(BOOLEAN_KEY));
        assertNull(config.getUndecored(TEST_MISSING_KEY));
    }

    /**
     * Tests for default {@link Config#get(String)} and String values related
     * methods implementations.
     */
    @Test
    void testGetString() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, TEST_VALUE);
        properties.put(NUMBER_KEY, NUMBER_VALUE);
        properties.put(BOOLEAN_KEY, BOOLEAN_VALUE);
        final Config config = properties::get;
        assertEquals(TEST_VALUE, config.get(TEST_KEY));
        assertEquals(NUMBER_VALUE, config.get(NUMBER_KEY));
        assertEquals(BOOLEAN_VALUE, config.get(BOOLEAN_KEY));
        assertNull(config.get(TEST_MISSING_KEY));
        assertEquals(TEST_VALUE, config.get(TEST_KEY, DEFAULT_VALUE));
        assertEquals(NUMBER_VALUE, config.get(NUMBER_KEY, DEFAULT_VALUE));
        assertEquals(BOOLEAN_VALUE, config.get(BOOLEAN_KEY, DEFAULT_VALUE));
        assertEquals(DEFAULT_VALUE, config.get(TEST_MISSING_KEY, DEFAULT_VALUE));
        assertEquals(TEST_VALUE, config.get(TEST_KEY, () -> DEFAULT_VALUE));
        assertEquals(NUMBER_VALUE, config.get(NUMBER_KEY, () -> DEFAULT_VALUE));
        assertEquals(BOOLEAN_VALUE, config.get(BOOLEAN_KEY, () -> DEFAULT_VALUE));
        assertEquals(DEFAULT_VALUE, config.get(TEST_MISSING_KEY, () -> DEFAULT_VALUE));
    }

    /**
     * Tests for default {@link Config#getBoolean(String)} and Boolean values
     * related methods implementations.
     */
    @Test
    void testGetBoolean() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, TEST_VALUE);
        properties.put(NUMBER_KEY, NUMBER_VALUE);
        properties.put(BOOLEAN_KEY, BOOLEAN_VALUE);
        final Config config = properties::get;
        assertFalse(config.getBoolean(TEST_KEY));
        assertFalse(config.getBoolean(NUMBER_KEY));
        assertTrue(config.getBoolean(BOOLEAN_KEY));
        assertNull(config.getBoolean(TEST_MISSING_KEY));
        assertFalse(config.getBoolean(TEST_KEY, true));
        assertFalse(config.getBoolean(NUMBER_KEY, true));
        assertTrue(config.getBoolean(BOOLEAN_KEY, false));
        assertTrue(config.getBoolean(TEST_MISSING_KEY, true));
        assertFalse(config.getBoolean(TEST_KEY, () -> true));
        assertFalse(config.getBoolean(NUMBER_KEY, () -> true));
        assertTrue(config.getBoolean(BOOLEAN_KEY, () -> false));
        assertTrue(config.getBoolean(TEST_MISSING_KEY, () -> true));
    }

    /**
     * Tests for default {@link Config#getInteger(String)} and Integer values
     * related methods implementations.
     */
    @Test
    void testGetInteger() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, TEST_VALUE);
        properties.put(NUMBER_KEY, NUMBER_VALUE);
        properties.put(BOOLEAN_KEY, BOOLEAN_VALUE);
        final Config config = properties::get;
        assertThrows(NumberFormatException.class, () -> config.getInteger(TEST_KEY));
        assertEquals(1000, config.getInteger(NUMBER_KEY));
        assertThrows(NumberFormatException.class, () -> config.getInteger(BOOLEAN_KEY));
        assertNull(config.getInteger(TEST_MISSING_KEY));
        assertThrows(NumberFormatException.class, () -> config.getInteger(TEST_KEY, 2000));
        assertEquals(1000, config.getInteger(NUMBER_KEY, 2000));
        assertThrows(NumberFormatException.class, () -> config.getInteger(BOOLEAN_KEY, 2000));
        assertEquals(2000, config.getInteger(TEST_MISSING_KEY, 2000));
        assertThrows(NumberFormatException.class, () -> config.getInteger(TEST_KEY, () -> 2000));
        assertEquals(1000, config.getInteger(NUMBER_KEY, () -> 2000));
        assertThrows(NumberFormatException.class, () -> config.getInteger(BOOLEAN_KEY, () -> 2000));
        assertEquals(2000, config.getInteger(TEST_MISSING_KEY, () -> 2000));
    }

    /**
     * Tests for default {@link Config#getLong(String)} and Long values
     * related methods implementations.
     */
    @Test
    void testGetLong() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, TEST_VALUE);
        properties.put(NUMBER_KEY, NUMBER_VALUE);
        properties.put(BOOLEAN_KEY, BOOLEAN_VALUE);
        final Config config = properties::get;
        assertThrows(NumberFormatException.class, () -> config.getLong(TEST_KEY));
        assertEquals(1000L, config.getLong(NUMBER_KEY));
        assertThrows(NumberFormatException.class, () -> config.getLong(BOOLEAN_KEY));
        assertNull(config.getLong(TEST_MISSING_KEY));
        assertThrows(NumberFormatException.class, () -> config.getLong(TEST_KEY, 2000L));
        assertEquals(1000L, config.getLong(NUMBER_KEY, 2000L));
        assertThrows(NumberFormatException.class, () -> config.getLong(BOOLEAN_KEY, 2000L));
        assertEquals(2000L, config.getLong(TEST_MISSING_KEY, 2000L));
        assertThrows(NumberFormatException.class, () -> config.getLong(TEST_KEY, () -> 2000L));
        assertEquals(1000, config.getLong(NUMBER_KEY, () -> 2000L));
        assertThrows(NumberFormatException.class, () -> config.getLong(BOOLEAN_KEY, () -> 2000L));
        assertEquals(2000L, config.getLong(TEST_MISSING_KEY, () -> 2000L));
    }
}
