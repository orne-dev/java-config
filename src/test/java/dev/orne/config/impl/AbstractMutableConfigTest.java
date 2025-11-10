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
import static org.mockito.BDDMockito.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Test;

import dev.orne.config.ConfigCryptoProvider;
import dev.orne.config.MutableConfigBuilder;
import dev.orne.config.ValueDecoder;
import dev.orne.config.ValueDecorator;
import dev.orne.config.ValueEncoder;

/**
 * Basic unit tests for {@code AbstractMutableConfig} subtypes.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
abstract class AbstractMutableConfigTest
extends AbstractConfigTest {

    /**
     * Creates a new configuration builder instance of the tested type
     * with the specified initial configuration properties.
     * 
     * @param properties The initial configuration properties.
     * @return The created builder, with .
     */
    @Override
    protected abstract MutableConfigBuilder<?> createBuilder(
            @NotNull Map<String, String> properties);

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    void testEmpty() {
        super.testEmpty();
        final AbstractMutableConfig config = assertInstanceOf(AbstractMutableConfig.class, createBuilder()
                .build());
        assertSame(ValueEncoder.DEFAULT, config.getEncoder());
    }

    /**
     * Tests instance building with custom encoder.
     */
    @Test
    void testEncoder() {
        final HashMap<String, String> properties = new HashMap<>();
        final ValueEncoder encoder = s -> "Encoded: " + s;
        final AbstractMutableConfig config = assertInstanceOf(AbstractMutableConfig.class,
                createBuilder(properties)
                    .withEncoder(encoder)
                    .build());
        assertNull(config.getParent());
        assertSame(ValueDecoder.DEFAULT, config.getDecoder());
        assertSame(ValueDecorator.DEFAULT, config.getDecorator());
        assertSame(encoder, config.getEncoder());
        assertFalse(config.getResolver().isPresent());
        assertFalse(config.containsInt(TEST_KEY));
        assertFalse(config.contains(TEST_KEY));
        assertNull(config.getInt(TEST_KEY));
        assertNull(config.getUndecored(TEST_KEY));
        assertNull(config.get(TEST_KEY));
        config.set(TEST_KEY, "testValue");
        assertFalse(config.isEmptyInt());
        assertFalse(config.isEmpty());
        assertFalse(config.getKeysInt().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeysInt().collect(Collectors.toSet()).contains(TEST_KEY));
        assertFalse(config.getKeys().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_KEY));
        assertTrue(config.containsInt(TEST_KEY));
        assertTrue(config.contains(TEST_KEY));
        assertEquals("Encoded: testValue", config.getInt(TEST_KEY));
        assertEquals("Encoded: testValue", config.getUndecored(TEST_KEY));
        assertEquals("Encoded: testValue", config.get(TEST_KEY));
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    void testEncryption() {
        super.testEncryption();
        final ConfigCryptoProvider crypto = ConfigCryptoProvider.builder()
                .withAesGcmEngine("secretSalt".getBytes(StandardCharsets.UTF_8))
                .withSecretKey("secretKey".toCharArray())
                .build();
        final HashMap<String, String> properties = new HashMap<>();
        final AbstractMutableConfig config = assertInstanceOf(AbstractMutableConfig.class,
                createBuilder(properties)
                    .withEncryption(crypto)
                    .build());
        assertNotSame(ValueEncoder.DEFAULT, config.getEncoder());
        final String testValue = "testValue";
        config.set(TEST_KEY, testValue);
        assertFalse(config.isEmptyInt());
        assertFalse(config.isEmpty());
        assertFalse(config.getKeysInt().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeysInt().collect(Collectors.toSet()).contains(TEST_KEY));
        assertFalse(config.getKeys().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_KEY));
        assertTrue(config.containsInt(TEST_KEY));
        assertTrue(config.contains(TEST_KEY));
        assertEquals(testValue, crypto.decrypt(config.getInt(TEST_KEY)));
        assertEquals(testValue, config.getUndecored(TEST_KEY));
        assertEquals(testValue, config.get(TEST_KEY));
        assertNull(config.getParent());
    }

    /**
     * Tests instance building with custom encoder and value encryption.
     */
    @Test
    void testEncoderEncryption() {
        final ValueEncoder encoder = s -> "Encoded: " + s;
        final ConfigCryptoProvider crypto = ConfigCryptoProvider.builder()
                .withAesGcmEngine("secretSalt".getBytes(StandardCharsets.UTF_8))
                .withSecretKey("secretKey".toCharArray())
                .build();
        final HashMap<String, String> properties = new HashMap<>();
        final AbstractMutableConfig config = assertInstanceOf(AbstractMutableConfig.class,
                createBuilder(properties)
                    .withEncoder(encoder)
                    .withEncryption(crypto)
                    .build());
        assertNotSame(ValueEncoder.DEFAULT, config.getEncoder());
        final String testValue = "testValue";
        config.set(TEST_KEY, testValue);
        assertFalse(config.isEmptyInt());
        assertFalse(config.isEmpty());
        assertFalse(config.getKeysInt().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeysInt().collect(Collectors.toSet()).contains(TEST_KEY));
        assertFalse(config.getKeys().collect(Collectors.toSet()).isEmpty());
        assertTrue(config.getKeys().collect(Collectors.toSet()).contains(TEST_KEY));
        assertTrue(config.containsInt(TEST_KEY));
        assertTrue(config.contains(TEST_KEY));
        assertEquals("Encoded: " + testValue, crypto.decrypt(config.getInt(TEST_KEY)));
        assertEquals("Encoded: " + testValue, config.getUndecored(TEST_KEY));
        assertEquals("Encoded: " + testValue, config.get(TEST_KEY));
        assertNull(config.getParent());
    }

    /**
     * Tests properties setting.
     */
    @Test
    void testSet() {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        given(mockParent.isEmpty()).willReturn(false);
        given(mockParent.getKeys()).will(mock -> Arrays.asList(TEST_PARENT_KEY, TEST_PARENT_DERIVED_KEY).stream());
        given(mockParent.contains(TEST_PARENT_KEY)).willReturn(true);
        given(mockParent.contains(TEST_PARENT_DERIVED_KEY)).willReturn(true);
        given(mockParent.getUndecored(TEST_PARENT_KEY)).willReturn("testParentValue");
        given(mockParent.getUndecored(TEST_PARENT_DERIVED_KEY)).willReturn("Derived parent value: ${" + TEST_KEY + "}");
        final AbstractMutableConfig config = assertInstanceOf(AbstractMutableConfig.class,
                createBuilder(properties)
                    .withParent(mockParent)
                    .withVariableResolution()
                    .build());
        config.set(TEST_KEY, "newValue");
        assertEquals("newValue", config.getInt(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getInt(TEST_DERIVED_KEY));
        assertNull(config.getInt(TEST_PARENT_KEY));
        assertNull(config.getInt(TEST_PARENT_DERIVED_KEY));
        assertEquals("newValue", config.getUndecored(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getUndecored(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.getUndecored(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.getUndecored(TEST_PARENT_DERIVED_KEY));
        assertEquals("newValue", config.get(TEST_KEY));
        assertEquals("Derived value: testParentValue", config.get(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.get(TEST_PARENT_KEY));
        assertEquals("Derived parent value: newValue", config.get(TEST_PARENT_DERIVED_KEY));
    }

    /**
     * Tests properties setting to null.
     */
    @Test
    void testSetNull() {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        given(mockParent.isEmpty()).willReturn(false);
        given(mockParent.getKeys()).will(mock -> Arrays.asList(TEST_PARENT_KEY, TEST_PARENT_DERIVED_KEY).stream());
        given(mockParent.contains(TEST_PARENT_KEY)).willReturn(true);
        given(mockParent.contains(TEST_PARENT_DERIVED_KEY)).willReturn(true);
        given(mockParent.getUndecored(TEST_PARENT_KEY)).willReturn("testParentValue");
        given(mockParent.getUndecored(TEST_PARENT_DERIVED_KEY)).willReturn("Derived parent value: ${" + TEST_KEY + "}");
        final AbstractMutableConfig config = assertInstanceOf(AbstractMutableConfig.class,
                createBuilder(properties)
                    .withParent(mockParent)
                    .withVariableResolution()
                    .build());
        config.set(TEST_KEY, (String) null);
        assertNull(config.getInt(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getInt(TEST_DERIVED_KEY));
        assertNull(config.getInt(TEST_PARENT_KEY));
        assertNull(config.getInt(TEST_PARENT_DERIVED_KEY));
        assertNull(config.getUndecored(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getUndecored(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.getUndecored(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.getUndecored(TEST_PARENT_DERIVED_KEY));
        assertNull(config.get(TEST_KEY));
        assertEquals("Derived value: testParentValue", config.get(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.get(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.get(TEST_PARENT_DERIVED_KEY));
    }

    /**
     * Tests properties setting to boolean value.
     */
    @Test
    void testSetBoolean() {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        given(mockParent.isEmpty()).willReturn(false);
        given(mockParent.getKeys()).will(mock -> Arrays.asList(TEST_PARENT_KEY, TEST_PARENT_DERIVED_KEY).stream());
        given(mockParent.contains(TEST_PARENT_KEY)).willReturn(true);
        given(mockParent.contains(TEST_PARENT_DERIVED_KEY)).willReturn(true);
        given(mockParent.getUndecored(TEST_PARENT_KEY)).willReturn("testParentValue");
        given(mockParent.getUndecored(TEST_PARENT_DERIVED_KEY)).willReturn("Derived parent value: ${" + TEST_KEY + "}");
        final AbstractMutableConfig config = assertInstanceOf(AbstractMutableConfig.class,
                createBuilder(properties)
                    .withParent(mockParent)
                    .withVariableResolution()
                    .build());
        config.set(TEST_KEY, true);
        assertEquals("true", config.getInt(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getInt(TEST_DERIVED_KEY));
        assertNull(config.getInt(TEST_PARENT_KEY));
        assertNull(config.getInt(TEST_PARENT_DERIVED_KEY));
        assertEquals("true", config.getUndecored(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getUndecored(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.getUndecored(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.getUndecored(TEST_PARENT_DERIVED_KEY));
        assertEquals("true", config.get(TEST_KEY));
        assertEquals("Derived value: testParentValue", config.get(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.get(TEST_PARENT_KEY));
        assertEquals("Derived parent value: true", config.get(TEST_PARENT_DERIVED_KEY));
    }

    /**
     * Tests properties setting to Boolean null value.
     */
    @Test
    void testSetBooleanNull() {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        given(mockParent.isEmpty()).willReturn(false);
        given(mockParent.getKeys()).will(mock -> Arrays.asList(TEST_PARENT_KEY, TEST_PARENT_DERIVED_KEY).stream());
        given(mockParent.contains(TEST_PARENT_KEY)).willReturn(true);
        given(mockParent.contains(TEST_PARENT_DERIVED_KEY)).willReturn(true);
        given(mockParent.getUndecored(TEST_PARENT_KEY)).willReturn("testParentValue");
        given(mockParent.getUndecored(TEST_PARENT_DERIVED_KEY)).willReturn("Derived parent value: ${" + TEST_KEY + "}");
        final AbstractMutableConfig config = assertInstanceOf(AbstractMutableConfig.class,
                createBuilder(properties)
                    .withParent(mockParent)
                    .withVariableResolution()
                    .build());
        config.set(TEST_KEY, (Boolean) null);
        assertNull(config.getInt(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getInt(TEST_DERIVED_KEY));
        assertNull(config.getInt(TEST_PARENT_KEY));
        assertNull(config.getInt(TEST_PARENT_DERIVED_KEY));
        assertNull(config.getUndecored(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getUndecored(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.getUndecored(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.getUndecored(TEST_PARENT_DERIVED_KEY));
        assertNull(config.get(TEST_KEY));
        assertEquals("Derived value: testParentValue", config.get(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.get(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.get(TEST_PARENT_DERIVED_KEY));
    }

    /**
     * Tests properties setting to integer value.
     */
    @Test
    void testSetInteger() {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        given(mockParent.isEmpty()).willReturn(false);
        given(mockParent.getKeys()).will(mock -> Arrays.asList(TEST_PARENT_KEY, TEST_PARENT_DERIVED_KEY).stream());
        given(mockParent.contains(TEST_PARENT_KEY)).willReturn(true);
        given(mockParent.contains(TEST_PARENT_DERIVED_KEY)).willReturn(true);
        given(mockParent.getUndecored(TEST_PARENT_KEY)).willReturn("testParentValue");
        given(mockParent.getUndecored(TEST_PARENT_DERIVED_KEY)).willReturn("Derived parent value: ${" + TEST_KEY + "}");
        final AbstractMutableConfig config = assertInstanceOf(AbstractMutableConfig.class,
                createBuilder(properties)
                    .withParent(mockParent)
                    .withVariableResolution()
                    .build());
        config.set(TEST_KEY, 1000);
        assertEquals("1000", config.getInt(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getInt(TEST_DERIVED_KEY));
        assertNull(config.getInt(TEST_PARENT_KEY));
        assertNull(config.getInt(TEST_PARENT_DERIVED_KEY));
        assertEquals("1000", config.getUndecored(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getUndecored(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.getUndecored(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.getUndecored(TEST_PARENT_DERIVED_KEY));
        assertEquals("1000", config.get(TEST_KEY));
        assertEquals("Derived value: testParentValue", config.get(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.get(TEST_PARENT_KEY));
        assertEquals("Derived parent value: 1000", config.get(TEST_PARENT_DERIVED_KEY));
    }

    /**
     * Tests properties setting to Interger null value.
     */
    @Test
    void testSetIntegerNull() {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        given(mockParent.isEmpty()).willReturn(false);
        given(mockParent.getKeys()).will(mock -> Arrays.asList(TEST_PARENT_KEY, TEST_PARENT_DERIVED_KEY).stream());
        given(mockParent.contains(TEST_PARENT_KEY)).willReturn(true);
        given(mockParent.contains(TEST_PARENT_DERIVED_KEY)).willReturn(true);
        given(mockParent.getUndecored(TEST_PARENT_KEY)).willReturn("testParentValue");
        given(mockParent.getUndecored(TEST_PARENT_DERIVED_KEY)).willReturn("Derived parent value: ${" + TEST_KEY + "}");
        final AbstractMutableConfig config = assertInstanceOf(AbstractMutableConfig.class,
                createBuilder(properties)
                    .withParent(mockParent)
                    .withVariableResolution()
                    .build());
        config.set(TEST_KEY, (Integer) null);
        assertNull(config.getInt(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getInt(TEST_DERIVED_KEY));
        assertNull(config.getInt(TEST_PARENT_KEY));
        assertNull(config.getInt(TEST_PARENT_DERIVED_KEY));
        assertNull(config.getUndecored(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getUndecored(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.getUndecored(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.getUndecored(TEST_PARENT_DERIVED_KEY));
        assertNull(config.get(TEST_KEY));
        assertEquals("Derived value: testParentValue", config.get(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.get(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.get(TEST_PARENT_DERIVED_KEY));
    }

    /**
     * Tests properties setting to long value.
     */
    @Test
    void testSetLong() {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        given(mockParent.isEmpty()).willReturn(false);
        given(mockParent.getKeys()).will(mock -> Arrays.asList(TEST_PARENT_KEY, TEST_PARENT_DERIVED_KEY).stream());
        given(mockParent.contains(TEST_PARENT_KEY)).willReturn(true);
        given(mockParent.contains(TEST_PARENT_DERIVED_KEY)).willReturn(true);
        given(mockParent.getUndecored(TEST_PARENT_KEY)).willReturn("testParentValue");
        given(mockParent.getUndecored(TEST_PARENT_DERIVED_KEY)).willReturn("Derived parent value: ${" + TEST_KEY + "}");
        final AbstractMutableConfig config = assertInstanceOf(AbstractMutableConfig.class,
                createBuilder(properties)
                    .withParent(mockParent)
                    .withVariableResolution()
                    .build());
        config.set(TEST_KEY, 1000L);
        assertEquals("1000", config.getInt(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getInt(TEST_DERIVED_KEY));
        assertNull(config.getInt(TEST_PARENT_KEY));
        assertNull(config.getInt(TEST_PARENT_DERIVED_KEY));
        assertEquals("1000", config.getUndecored(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getUndecored(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.getUndecored(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.getUndecored(TEST_PARENT_DERIVED_KEY));
        assertEquals("1000", config.get(TEST_KEY));
        assertEquals("Derived value: testParentValue", config.get(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.get(TEST_PARENT_KEY));
        assertEquals("Derived parent value: 1000", config.get(TEST_PARENT_DERIVED_KEY));
    }

    /**
     * Tests properties setting to Long null value.
     */
    @Test
    void testSetLongNull() {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        given(mockParent.isEmpty()).willReturn(false);
        given(mockParent.getKeys()).will(mock -> Arrays.asList(TEST_PARENT_KEY, TEST_PARENT_DERIVED_KEY).stream());
        given(mockParent.contains(TEST_PARENT_KEY)).willReturn(true);
        given(mockParent.contains(TEST_PARENT_DERIVED_KEY)).willReturn(true);
        given(mockParent.getUndecored(TEST_PARENT_KEY)).willReturn("testParentValue");
        given(mockParent.getUndecored(TEST_PARENT_DERIVED_KEY)).willReturn("Derived parent value: ${" + TEST_KEY + "}");
        final AbstractMutableConfig config = assertInstanceOf(AbstractMutableConfig.class,
                createBuilder(properties)
                    .withParent(mockParent)
                    .withVariableResolution()
                    .build());
        config.set(TEST_KEY, (Long) null);
        assertNull(config.getInt(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getInt(TEST_DERIVED_KEY));
        assertNull(config.getInt(TEST_PARENT_KEY));
        assertNull(config.getInt(TEST_PARENT_DERIVED_KEY));
        assertNull(config.getUndecored(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getUndecored(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.getUndecored(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.getUndecored(TEST_PARENT_DERIVED_KEY));
        assertNull(config.get(TEST_KEY));
        assertEquals("Derived value: testParentValue", config.get(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.get(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.get(TEST_PARENT_DERIVED_KEY));
    }

    /**
     * Tests properties removing.
     */
    @Test
    void testRemove() {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        given(mockParent.isEmpty()).willReturn(false);
        given(mockParent.getKeys()).will(mock -> Arrays.asList(TEST_PARENT_KEY, TEST_PARENT_DERIVED_KEY).stream());
        given(mockParent.contains(TEST_PARENT_KEY)).willReturn(true);
        given(mockParent.contains(TEST_PARENT_DERIVED_KEY)).willReturn(true);
        given(mockParent.getUndecored(TEST_PARENT_KEY)).willReturn("testParentValue");
        given(mockParent.getUndecored(TEST_PARENT_DERIVED_KEY)).willReturn("Derived parent value: ${" + TEST_KEY + "}");
        final AbstractMutableConfig config = assertInstanceOf(AbstractMutableConfig.class,
                createBuilder(properties)
                    .withParent(mockParent)
                    .withVariableResolution()
                    .build());
        config.remove(TEST_KEY);
        assertNull(config.getInt(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getInt(TEST_DERIVED_KEY));
        assertNull(config.getInt(TEST_PARENT_KEY));
        assertNull(config.getInt(TEST_PARENT_DERIVED_KEY));
        assertNull(config.getUndecored(TEST_KEY));
        assertEquals("Derived value: ${" + TEST_PARENT_KEY + "}", config.getUndecored(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.getUndecored(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.getUndecored(TEST_PARENT_DERIVED_KEY));
        assertNull(config.get(TEST_KEY));
        assertEquals("Derived value: testParentValue", config.get(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.get(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.get(TEST_PARENT_DERIVED_KEY));
    }

    /**
     * Tests multiple properties removing.
     */
    @Test
    void testRemoveMultiple() {
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        given(mockParent.isEmpty()).willReturn(false);
        given(mockParent.getKeys()).will(mock -> Arrays.asList(TEST_PARENT_KEY, TEST_PARENT_DERIVED_KEY).stream());
        given(mockParent.contains(TEST_PARENT_KEY)).willReturn(true);
        given(mockParent.contains(TEST_PARENT_DERIVED_KEY)).willReturn(true);
        given(mockParent.getUndecored(TEST_PARENT_KEY)).willReturn("testParentValue");
        given(mockParent.getUndecored(TEST_PARENT_DERIVED_KEY)).willReturn("Derived parent value: ${" + TEST_KEY + "}");
        final AbstractMutableConfig config = assertInstanceOf(AbstractMutableConfig.class,
                createBuilder(properties)
                    .withParent(mockParent)
                    .withVariableResolution()
                    .build());
        config.remove(TEST_KEY, TEST_DERIVED_KEY);
        assertNull(config.getInt(TEST_KEY));
        assertNull(config.getInt(TEST_DERIVED_KEY));
        assertNull(config.getInt(TEST_PARENT_KEY));
        assertNull(config.getInt(TEST_PARENT_DERIVED_KEY));
        assertNull(config.getUndecored(TEST_KEY));
        assertNull(config.getUndecored(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.getUndecored(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.getUndecored(TEST_PARENT_DERIVED_KEY));
        assertNull(config.get(TEST_KEY));
        assertNull(config.get(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.get(TEST_PARENT_KEY));
        assertEquals("Derived parent value: ${" + TEST_KEY + "}", config.get(TEST_PARENT_DERIVED_KEY));
    }
}
