package dev.orne.config;

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

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests of {@link DelegatedConfig}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class DelegatedConfigTest {

    protected DelegatedConfig createInstance() {
        return new DelegatedConfig(mock(Config.class));
    }

    /**
     * Test of {@link DelegatedConfig#getParent()}.
     */
    @Test
    void testGetParent() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        final Config parent = mock(Config.class);
        given(delegate.getParent()).willReturn(parent);
        final Config result = instance.getParent();
        assertSame(parent, result);
        then(delegate).should().getParent();
    }

    /**
     * Test of {@link DelegatedConfig#isEmpty()}.
     */
    @Test
    void testIsEmpty() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        given(delegate.isEmpty()).willReturn(true);
        final boolean result = instance.isEmpty();
        assertTrue(result);
        then(delegate).should().isEmpty();
    }

    /**
     * Test of {@link DelegatedConfig#contains(String)}.
     */
    @Test
    void testContains() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        given(delegate.contains("key")).willReturn(true);
        final boolean result = instance.contains("key");
        assertTrue(result);
        then(delegate).should().contains("key");
    }

    /**
     * Test of {@link DelegatedConfig#getKeys()}.
     */
    @Test
    void testGetKeys() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        @SuppressWarnings("unchecked")
        final Stream<String> keys = mock(Stream.class);
        given(delegate.getKeys()).willReturn(keys);
        final Stream<String> result = instance.getKeys();
        assertSame(keys, result);
        then(delegate).should().getKeys();
    }

    /**
     * Test of {@link DelegatedConfig#getKeys(Predicate)}.
     */
    @Test
    void testGetKeysPredicate() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        @SuppressWarnings("unchecked")
        final Predicate<String> filter = mock(Predicate.class);
        @SuppressWarnings("unchecked")
        final Stream<String> keys = mock(Stream.class);
        given(delegate.getKeys(filter)).willReturn(keys);
        final Stream<String> result = instance.getKeys(filter);
        assertSame(keys, result);
        then(delegate).should().getKeys(filter);
    }

    /**
     * Test of {@link DelegatedConfig#getKeys(String)}.
     */
    @Test
    void testGetKeysFilter() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        @SuppressWarnings("unchecked")
        final Stream<String> keys = mock(Stream.class);
        given(delegate.getKeys("filter")).willReturn(keys);
        final Stream<String> result = instance.getKeys("filter");
        assertSame(keys, result);
        then(delegate).should().getKeys("filter");
    }

    /**
     * Test of {@link DelegatedConfig#get(String)}.
     */
    @Test
    void testGet() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        given(delegate.get("key")).willReturn("value");
        final String result = instance.get("key");
        assertEquals("value", result);
        then(delegate).should().get("key");
    }

    /**
     * Test of {@link DelegatedConfig#getUndecored(String)}.
     */
    @Test
    void testGetUndecored() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        given(delegate.getUndecored("key")).willReturn("value");
        final String result = instance.getUndecored("key");
        assertEquals("value", result);
        then(delegate).should().getUndecored("key");
    }

    /**
     * Test of {@link DelegatedConfig#get(String, String)}.
     */
    @Test
    void testGetDefault() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        given(delegate.get("key", "default")).willReturn("value");
        final String result = instance.get("key", "default");
        assertEquals("value", result);
        then(delegate).should().get("key", "default");
    }

    /**
     * Test of {@link DelegatedConfig#get(String, Supplier)}.
     */
    @Test
    void testGetSupplier() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        final Supplier<String> supplier = () -> "default";
        given(delegate.get("key", supplier)).willReturn("value");
        final String result = instance.get("key", supplier);
        assertEquals("value", result);
        then(delegate).should().get("key", supplier);
    }

    /**
     * Test of {@link DelegatedConfig#getBoolean(String)}.
     */
    @Test
    void testGetBoolean() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        given(delegate.getBoolean("key")).willReturn(true);
        final Boolean result = instance.getBoolean("key");
        assertTrue(result);
        then(delegate).should().getBoolean("key");
    }

    /**
     * Test of {@link DelegatedConfig#getBoolean(String, boolean)}.
     */
    @Test
    void testGetBooleanDefault() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        given(delegate.getBoolean("key", false)).willReturn(true);
        final Boolean result = instance.getBoolean("key", false);
        assertTrue(result);
        then(delegate).should().getBoolean("key", false);
    }

    /**
     * Test of {@link DelegatedConfig#getBoolean(String, Supplier)}.
     */
    @Test
    void testGetBooleanSupplier() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        final Supplier<Boolean> supplier = () -> false;
        given(delegate.getBoolean("key", supplier)).willReturn(true);
        final Boolean result = instance.getBoolean("key", supplier);
        assertTrue(result);
        then(delegate).should().getBoolean("key", supplier);
    }

    /**
     * Test of {@link DelegatedConfig#getInteger(String)}.
     */
    @Test
    void testGetInteger() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        given(delegate.getInteger("key")).willReturn(20);
        final Integer result = instance.getInteger("key");
        assertEquals(20, result);
        then(delegate).should().getInteger("key");
    }

    /**
     * Test of {@link DelegatedConfig#getInteger(String, int)}.
     */
    @Test
    void testGetIntegerDefault() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        given(delegate.getInteger("key", 10)).willReturn(20);
        final Integer result = instance.getInteger("key", 10);
        assertEquals(20, result);
        then(delegate).should().getInteger("key", 10);
    }

    /**
     * Test of {@link DelegatedConfig#getInteger(String, Supplier)}.
     */
    @Test
    void testGetIntegerSupplier() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        final Supplier<Integer> supplier = () -> 10;
        given(delegate.getInteger("key", supplier)).willReturn(20);
        final Integer result = instance.getInteger("key", supplier);
        assertEquals(20, result);
        then(delegate).should().getInteger("key", supplier);
    }

    /**
     * Test of {@link DelegatedConfig#getLong(String)}.
     */
    @Test
    void testGetLong() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        given(delegate.getLong("key")).willReturn(20L);
        final Long result = instance.getLong("key");
        assertEquals(20L, result);
        then(delegate).should().getLong("key");
    }

    /**
     * Test of {@link DelegatedConfig#getLong(String, long)}.
     */
    @Test
    void testGetLongDefault() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        given(delegate.getLong("key", 10L)).willReturn(20L);
        final Long result = instance.getLong("key", 10L);
        assertEquals(20L, result);
        then(delegate).should().getLong("key", 10L);
    }

    /**
     * Test of {@link DelegatedConfig#getLong(String, Supplier)}.
     */
    @Test
    void testGetLongSupplier() {
        final DelegatedConfig instance = createInstance();
        final Config delegate = instance.getDelegate();
        final Supplier<Long> supplier = () -> 10L;
        given(delegate.getLong("key", supplier)).willReturn(20L);
        final Long result = instance.getLong("key", supplier);
        assertEquals(20L, result);
        then(delegate).should().getLong("key", supplier);
    }
}
