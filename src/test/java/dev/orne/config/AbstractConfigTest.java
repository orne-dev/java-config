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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.config.crypto.ConfigCryptoProvider;

/**
 * Basic unit tests for {@code AbstractConfig} subtypes.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
abstract class AbstractConfigTest {

    protected static final String TEST_KEY = "test.key";
    protected static final String TEST_DERIVED_KEY = "test.derived.key";
    protected static final String TEST_PARENT_KEY = "test.parent.key";
    protected static final String TEST_PARENT_DERIVED_KEY = "test.parent.derived.key";

    protected @Mock Config mockParent;

    /**
     * Initializes the mocks used in the tests.
     */
    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Creates a new configuration builder instance of the tested type.
     * 
     * @return The created builder, with .
     */
    protected ConfigBuilder createBuilder() {
        return createBuilder(new HashMap<>());
    }

    /**
     * Creates a new configuration builder instance of the tested type
     * with the specified initial configuration properties.
     * 
     * @param properties The initial configuration properties.
     * @return The created builder, with initial configuration properties
     * populated.
     */
    protected abstract ConfigBuilder createBuilder(
            @NotNull Map<String, String> properties);

    /**
     * Tests empty instance building.
     */
    @Test
    void testEmpty() {
        final AbstractConfig config = assertInstanceOf(AbstractConfig.class, createBuilder()
                .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertFalse(config.containsInt(TEST_KEY));
        assertFalse(config.contains(TEST_KEY));
        assertNull(config.getInt(TEST_KEY));
        assertNull(config.getUndecored(TEST_KEY));
        assertNull(config.get(TEST_KEY));
    }

    /**
     * Tests instance building with parent.
     */
    @Test
    void testParent() {
        final AbstractConfig config = assertInstanceOf(AbstractConfig.class, createBuilder()
                .withParent(mockParent)
                .build());
        assertSame(mockParent, config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        given(mockParent.isEmpty()).willReturn(false);
        given(mockParent.getKeys()).will(mock -> Arrays.asList(TEST_PARENT_KEY).stream());
        given(mockParent.contains(TEST_PARENT_KEY)).willReturn(true);
        given(mockParent.getUndecored(TEST_PARENT_KEY)).willReturn("testParentValue");
        assertFalse(config.isEmpty());
        assertFalse(config.getKeys().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_PARENT_KEY));
        assertFalse(config.containsInt(TEST_PARENT_KEY));
        assertTrue(config.contains(TEST_PARENT_KEY));
        assertNull(config.getInt(TEST_PARENT_KEY));
        assertEquals("testParentValue", config.getUndecored(TEST_PARENT_KEY));
        assertEquals("testParentValue", config.get(TEST_PARENT_KEY));
    }

    /**
     * Tests instance building with custom decoder.
     */
    @Test
    void testDecoder() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        final ValueDecoder decoder = s -> "Decoded: " + s;
        final AbstractConfig config = assertInstanceOf(AbstractConfig.class,
                createBuilder(properties)
                    .withDecoder(decoder)
                    .build());
        assertNull(config.getParent());
        assertSame(decoder, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertFalse(config.isEmptyInt());
        assertFalse(config.isEmpty());
        assertFalse(config.getKeysInt().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeysInt().collect(Collectors.toSet()).contains(TEST_KEY));
        assertFalse(config.getKeys().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_KEY));
        assertTrue(config.containsInt(TEST_KEY));
        assertTrue(config.contains(TEST_KEY));
        assertEquals("testValue", config.getInt(TEST_KEY));
        assertEquals("Decoded: testValue", config.getUndecored(TEST_KEY));
        assertEquals("Decoded: testValue", config.get(TEST_KEY));
    }

    /**
     * Tests instance building with custom decoder.
     */
    @Test
    void testParentDecoder() {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        given(mockParent.isEmpty()).willReturn(false);
        given(mockParent.getKeys()).will(mock -> Arrays.asList(TEST_PARENT_KEY).stream());
        given(mockParent.contains(TEST_PARENT_KEY)).willReturn(true);
        given(mockParent.getUndecored(TEST_PARENT_KEY)).willReturn("testParentValue");
        final ValueDecoder decoder = s -> "Decoded: " + s;
        final AbstractConfig config = assertInstanceOf(AbstractConfig.class,
                createBuilder(properties)
                    .withParent(mockParent)
                    .withDecoder(decoder)
                    .build());
        assertSame(mockParent, config.getParent());
        assertSame(decoder, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertFalse(config.isEmptyInt());
        assertFalse(config.isEmpty());
        assertFalse(config.getKeysInt().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeysInt().collect(Collectors.toSet()).contains(TEST_KEY));
        assertFalse(config.getKeys().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_KEY));
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_PARENT_KEY));
        assertTrue(config.containsInt(TEST_KEY));
        assertFalse(config.containsInt(TEST_PARENT_KEY));
        assertTrue(config.contains(TEST_KEY));
        assertTrue(config.contains(TEST_PARENT_KEY));
        assertEquals("testValue", config.getInt(TEST_KEY));
        assertNull(config.getInt(TEST_PARENT_KEY));
        assertEquals("Decoded: testValue", config.getUndecored(TEST_KEY));
        assertEquals("testParentValue", config.getUndecored(TEST_PARENT_KEY));
        assertEquals("Decoded: testValue", config.get(TEST_KEY));
        assertEquals("testParentValue", config.get(TEST_PARENT_KEY));
    }

    /**
     * Tests instance building with value encryption.
     */
    @Test
    void testEncryption() {
        final ConfigCryptoProvider crypto = ConfigCryptoProvider.builder()
                .withAesGcmEngine("secretSalt".getBytes(StandardCharsets.UTF_8))
                .withSecretKey("secretKey".toCharArray())
                .build();
        final HashMap<String, String> properties = new HashMap<>();
        final String unencryptedValue = "testValue";
        final String encryptedValue = crypto.encrypt(unencryptedValue);
        properties.put(TEST_KEY, encryptedValue);
        final AbstractConfig config = assertInstanceOf(AbstractConfig.class,
                createBuilder(properties)
                    .withEncryption(crypto)
                    .build());
        assertNull(config.getParent());
        assertNotSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertFalse(config.isEmptyInt());
        assertFalse(config.isEmpty());
        assertFalse(config.getKeysInt().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeysInt().collect(Collectors.toSet()).contains(TEST_KEY));
        assertFalse(config.getKeys().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_KEY));
        assertTrue(config.containsInt(TEST_KEY));
        assertTrue(config.contains(TEST_KEY));
        assertEquals(encryptedValue, config.getInt(TEST_KEY));
        assertEquals(unencryptedValue, config.getUndecored(TEST_KEY));
        assertEquals(unencryptedValue, config.get(TEST_KEY));
        assertNull(config.getParent());
    }

    /**
     * Tests instance building with value encryption.
     */
    @Test
    void testParentEncryption() {
        final ConfigCryptoProvider crypto = ConfigCryptoProvider.builder()
                .withAesGcmEngine("secretSalt".getBytes(StandardCharsets.UTF_8))
                .withSecretKey("secretKey".toCharArray())
                .build();
        final HashMap<String, String> properties = new HashMap<>();
        final String unencryptedValue = "testValue";
        final String encryptedValue = crypto.encrypt(unencryptedValue);
        properties.put(TEST_KEY, encryptedValue);
        given(mockParent.isEmpty()).willReturn(false);
        given(mockParent.getKeys()).will(mock -> Arrays.asList(TEST_PARENT_KEY).stream());
        given(mockParent.contains(TEST_PARENT_KEY)).willReturn(true);
        given(mockParent.getUndecored(TEST_PARENT_KEY)).willReturn("testParentValue");
        final AbstractConfig config = assertInstanceOf(AbstractConfig.class,
                createBuilder(properties)
                    .withParent(mockParent)
                    .withEncryption(crypto)
                    .build());
        assertSame(mockParent, config.getParent());
        assertNotSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertFalse(config.isEmptyInt());
        assertFalse(config.isEmpty());
        assertFalse(config.getKeysInt().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeysInt().collect(Collectors.toSet()).contains(TEST_KEY));
        assertFalse(config.getKeys().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_KEY));
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_PARENT_KEY));
        assertTrue(config.containsInt(TEST_KEY));
        assertFalse(config.containsInt(TEST_PARENT_KEY));
        assertTrue(config.contains(TEST_KEY));
        assertTrue(config.contains(TEST_PARENT_KEY));
        assertEquals(encryptedValue, config.getInt(TEST_KEY));
        assertNull(config.getInt(TEST_PARENT_KEY));
        assertEquals(unencryptedValue, config.getUndecored(TEST_KEY));
        assertEquals("testParentValue", config.getUndecored(TEST_PARENT_KEY));
        assertEquals(unencryptedValue, config.get(TEST_KEY));
        assertEquals("testParentValue", config.get(TEST_PARENT_KEY));
    }

    /**
     * Tests instance building with custom decorator.
     */
    @Test
    void testDecorator() {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        final ValueDecorator decorator = s -> "Decorated: " + s;
        final AbstractConfig config = assertInstanceOf(AbstractConfig.class,
                createBuilder(properties)
                    .withDecorator(decorator)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(decorator, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertFalse(config.isEmptyInt());
        assertFalse(config.isEmpty());
        assertFalse(config.getKeysInt().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeysInt().collect(Collectors.toSet()).contains(TEST_KEY));
        assertFalse(config.getKeys().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_KEY));
        assertTrue(config.containsInt(TEST_KEY));
        assertTrue(config.contains(TEST_KEY));
        assertEquals("testValue", config.getInt(TEST_KEY));
        assertEquals("testValue", config.getUndecored(TEST_KEY));
        assertEquals("Decorated: testValue", config.get(TEST_KEY));
    }

    /**
     * Tests instance building with custom decorator.
     */
    @Test
    void testParentDecorator() {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        given(mockParent.isEmpty()).willReturn(false);
        given(mockParent.getKeys()).will(mock -> Arrays.asList(TEST_PARENT_KEY).stream());
        given(mockParent.contains(TEST_PARENT_KEY)).willReturn(true);
        given(mockParent.getUndecored(TEST_PARENT_KEY)).willReturn("testParentValue");
        final ValueDecorator decorator = s -> "Decorated: " + s;
        final AbstractConfig config = assertInstanceOf(AbstractConfig.class,
                createBuilder(properties)
                    .withParent(mockParent)
                    .withDecorator(decorator)
                    .build());
        assertSame(mockParent, config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(decorator, config.getDecorator());
        assertFalse(config.getResolver().isPresent());
        assertFalse(config.isEmptyInt());
        assertFalse(config.isEmpty());
        assertFalse(config.getKeysInt().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_KEY));
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_PARENT_KEY));
        assertTrue(config.containsInt(TEST_KEY));
        assertFalse(config.containsInt(TEST_PARENT_KEY));
        assertTrue(config.contains(TEST_KEY));
        assertTrue(config.contains(TEST_PARENT_KEY));
        assertEquals("testValue", config.getInt(TEST_KEY));
        assertNull(config.getInt(TEST_PARENT_KEY));
        assertEquals("testValue", config.getUndecored(TEST_KEY));
        assertEquals("testParentValue", config.getUndecored(TEST_PARENT_KEY));
        assertEquals("Decorated: testValue", config.get(TEST_KEY));
        assertEquals("Decorated: testParentValue", config.get(TEST_PARENT_KEY));
    }

    /**
     * Tests instance building with variable resolution.
     */
    @Test
    void testVariableResolution() {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_KEY + "}");
        final AbstractConfig config = assertInstanceOf(AbstractConfig.class,
                createBuilder(properties)
                    .withVariableResolution()
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertNotSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertTrue(config.getResolver().isPresent());
        assertFalse(config.isEmptyInt());
        assertFalse(config.isEmpty());
        assertFalse(config.getKeysInt().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeysInt().collect(Collectors.toSet()).contains(TEST_KEY));
        assertTrue(config.getKeysInt().collect(Collectors.toSet()).contains(TEST_DERIVED_KEY));
        assertFalse(config.getKeys().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_KEY));
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_DERIVED_KEY));
        assertTrue(config.containsInt(TEST_KEY));
        assertTrue(config.containsInt(TEST_DERIVED_KEY));
        assertTrue(config.contains(TEST_KEY));
        assertTrue(config.contains(TEST_DERIVED_KEY));
        assertEquals("testValue", config.getInt(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_KEY + "}", config.getInt(TEST_DERIVED_KEY));
        assertEquals("testValue", config.getUndecored(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_KEY + "}", config.getUndecored(TEST_DERIVED_KEY));
        assertEquals("testValue", config.get(TEST_KEY));
        assertEquals("Derived value: testValue", config.get(TEST_DERIVED_KEY));
    }

    /**
     * Tests instance building with parent variable resolution.
     */
    @Test
    void testParentVariableResolution() {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        given(mockParent.isEmpty()).willReturn(false);
        given(mockParent.getKeys()).will(mock -> Arrays.asList(TEST_PARENT_KEY, TEST_PARENT_DERIVED_KEY).stream());
        given(mockParent.contains(TEST_PARENT_KEY)).willReturn(true);
        given(mockParent.contains(TEST_PARENT_DERIVED_KEY)).willReturn(true);
        given(mockParent.getUndecored(TEST_PARENT_KEY)).willReturn("testParentValue");
        given(mockParent.getUndecored(TEST_PARENT_DERIVED_KEY)).willReturn("Derived parent value: ${" + TEST_KEY + "}");
        final AbstractConfig config = assertInstanceOf(AbstractConfig.class,
                createBuilder(properties)
                    .withParent(mockParent)
                    .withVariableResolution()
                    .build());
        assertSame(mockParent, config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertNotSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertTrue(config.getResolver().isPresent());
        assertFalse(config.isEmptyInt());
        assertFalse(config.isEmpty());
        assertFalse(config.getKeysInt().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeysInt().collect(Collectors.toSet()).contains(TEST_KEY));
        assertTrue(config.getKeysInt().collect(Collectors.toSet()).contains(TEST_DERIVED_KEY));
        assertFalse(config.getKeys().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_KEY));
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_DERIVED_KEY));
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_PARENT_KEY));
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_PARENT_DERIVED_KEY));
        assertTrue(config.containsInt(TEST_KEY));
        assertTrue(config.containsInt(TEST_DERIVED_KEY));
        assertTrue(config.contains(TEST_KEY));
        assertTrue(config.contains(TEST_DERIVED_KEY));
        assertEquals("testValue", config.getInt(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getInt(TEST_DERIVED_KEY));
        assertNull(config.getInt(TEST_PARENT_KEY));
        assertNull(config.getInt(TEST_PARENT_DERIVED_KEY));
        assertEquals("testValue", config.getUndecored(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getUndecored(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.getUndecored(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.getUndecored(TEST_PARENT_DERIVED_KEY));
        assertEquals("testValue", config.get(TEST_KEY));
        assertEquals("Derived value: testParentValue", config.get(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.get(TEST_PARENT_KEY));
        assertEquals("Derived parent value: testValue", config.get(TEST_PARENT_DERIVED_KEY));
    }
}
