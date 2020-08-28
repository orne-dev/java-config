package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2020 Orne Developments
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code MutableEncryptedConfig}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @since 1.0
 * @version 0.2
 */
@Tag("ut")
class MutableEncryptedConfigTest {

    private static final String TEST_KEY = "test.key";

    /**
     * Test method for {@link MutableEncryptedConfig#MutableEncryptedConfig(MutableConfig, ConfigCryptoProvider)}.
     */
    @Test
    void testConstructor() {
        final MutableConfig delegate = mock(MutableConfig.class);
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        final MutableEncryptedConfig config = new MutableEncryptedConfig(delegate, cryptoProvider);
        assertSame(delegate, config.getDelegate());
        assertSame(cryptoProvider, config.getCryptoProvider());
    }

    /**
     * Test method for {@link MutableEncryptedConfig#MutableEncryptedConfig(MutableConfig, ConfigCryptoProvider)}.
     */
    @Test
    void testConstructorNullConfig() {
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        assertThrows(NullPointerException.class, () -> {
            new MutableEncryptedConfig(null, cryptoProvider);
        });
    }

    /**
     * Test method for {@link MutableEncryptedConfig#MutableEncryptedConfig(MutableConfig, ConfigCryptoProvider)}.
     */
    @Test
    void testConstructorNullProvider() {
        final MutableConfig delegate = mock(MutableConfig.class);
        assertThrows(NullPointerException.class, () -> {
            new MutableEncryptedConfig(delegate, null);
        });
    }

    /**
     * Test method for {@link MutableEncryptedConfig#MutableEncryptedConfig(MutableConfig, ConfigCryptoProvider)}.
     */
    @Test
    void testConstructorNulls() {
        assertThrows(NullPointerException.class, () -> {
            new MutableEncryptedConfig(null, null);
        });
    }

    /**
     * Test method for {@link MutableEncryptedConfig#convertValueToString(Object)} for
     * {@code null} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testConvertToStringNull()
    throws ConfigException {
        final MutableConfig delegate = mock(MutableConfig.class);
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        final ConvertUtilsBean converters = mock(ConvertUtilsBean.class);
        final MutableEncryptedConfig realConfig = new MutableEncryptedConfig(delegate, cryptoProvider);
        realConfig.setConverter(converters);
        final MutableEncryptedConfig config = spy(realConfig);
        
        final String result = config.convertValueToString(null);
        assertNotNull(result);
        assertEquals(AbstractStringConfig.NULL, result);
        
        then(converters).should(never()).lookup(any());
        then(converters).should(never()).convert(any());
    }

    /**
     * Test method for {@link MutableEncryptedConfig#convertValueToString(Object)} for
     * {@code String} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testConvertToStringFromString()
    throws ConfigException {
        final MutableConfig delegate = mock(MutableConfig.class);
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        final ConvertUtilsBean converters = mock(ConvertUtilsBean.class);
        final MutableEncryptedConfig realConfig = new MutableEncryptedConfig(delegate, cryptoProvider);
        realConfig.setConverter(converters);
        final MutableEncryptedConfig config = spy(realConfig);
        
        final String value = "test value";
        final String result = config.convertValueToString(value);
        assertNotNull(result);
        assertSame(value, result);
        
        then(converters).should(never()).lookup(any());
        then(converters).should(never()).convert(any());
    }

    /**
     * Test method for {@link MutableEncryptedConfig#convertValueToString(Object)} for
     * {@code null} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testConvertToStringExplicitConverter()
    throws ConfigException {
        final MutableConfig delegate = mock(MutableConfig.class);
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        final ConvertUtilsBean converters = mock(ConvertUtilsBean.class);
        final Converter converter = mock(Converter.class);
        final MutableEncryptedConfig realConfig = new MutableEncryptedConfig(delegate, cryptoProvider);
        realConfig.setConverter(converters);
        final MutableEncryptedConfig config = spy(realConfig);
        
        final TestType value = new TestType();
        final String expectedValue = "mock result";
        willReturn(converter).given(converters).lookup(TestType.class);
        willReturn(expectedValue).given(converter).convert(String.class, value);
        
        final String result = config.convertValueToString(value);
        assertNotNull(result);
        assertSame(expectedValue, result);
        
        then(converters).should(times(1)).lookup(TestType.class);
        then(converters).should(never()).convert(any());
        then(converter).should(times(1)).convert(String.class, value);
    }

    /**
     * Test method for {@link MutableEncryptedConfig#convertValueToString(Object)} for
     * {@code null} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testConvertToStringExplicitParentConverter()
    throws ConfigException {
        final MutableConfig delegate = mock(MutableConfig.class);
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        final ConvertUtilsBean converters = mock(ConvertUtilsBean.class);
        final Converter converter = mock(Converter.class);
        final MutableEncryptedConfig realConfig = new MutableEncryptedConfig(delegate, cryptoProvider);
        realConfig.setConverter(converters);
        final MutableEncryptedConfig config = spy(realConfig);
        
        final ExtendedTestType value = new ExtendedTestType();
        final String expectedValue = "mock result";
        willReturn(null).given(converters).lookup(ExtendedTestType.class);
        willReturn(converter).given(converters).lookup(TestType.class);
        willReturn(expectedValue).given(converter).convert(String.class, value);
        
        final String result = config.convertValueToString(value);
        assertNotNull(result);
        assertSame(expectedValue, result);
        
        then(converters).should(times(1)).lookup(ExtendedTestType.class);
        then(converters).should(times(1)).lookup(TestType.class);
        then(converters).should(never()).convert(any());
        then(converter).should(times(1)).convert(String.class, value);
    }

    /**
     * Test method for {@link MutableEncryptedConfig#convertValueToString(Object)} for
     * {@code null} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testConvertToStringNoExplicitConverter()
    throws ConfigException {
        final MutableConfig delegate = mock(MutableConfig.class);
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        final ConvertUtilsBean converters = mock(ConvertUtilsBean.class);
        final MutableEncryptedConfig realConfig = new MutableEncryptedConfig(delegate, cryptoProvider);
        realConfig.setConverter(converters);
        final MutableEncryptedConfig config = spy(realConfig);
        
        final TestType value = new TestType();
        final String expectedValue = "mock result";
        willReturn(null).given(converters).lookup(any());
        willReturn(expectedValue).given(converters).convert(value);
        
        final String result = config.convertValueToString(value);
        assertNotNull(result);
        assertSame(expectedValue, result);
        
        then(converters).should(atLeastOnce()).lookup(any());
        then(converters).should(times(1)).convert(value);
    }

    /**
     * Test method for {@link MutableEncryptedConfig#set(String, Object)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testSet() throws ConfigException {
        final MutableConfig delegate = mock(MutableConfig.class);
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        final MutableEncryptedConfig config = spy(new MutableEncryptedConfig(delegate, cryptoProvider));
        
        final Object value = new Object();
        final String strValue = "mock raw value";
        final String encryptedValue = "mock encrypted value";
        willReturn(strValue).given(config).convertValueToString(value);
        willReturn(encryptedValue).given(cryptoProvider).encrypt(strValue);
        
        config.set(TEST_KEY, value);
        
        then(config).should(times(1)).convertValueToString(value);
        then(cryptoProvider).should(times(1)).encrypt(strValue);
        then(delegate).should(times(1)).set(TEST_KEY, encryptedValue);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link MutableEncryptedConfig#remove(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testRemove() throws ConfigException {
        final MutableConfig delegate = mock(MutableConfig.class);
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        final MutableEncryptedConfig config = spy(new MutableEncryptedConfig(delegate, cryptoProvider));
        
        config.remove(TEST_KEY);
        
        then(cryptoProvider).shouldHaveNoInteractions();
        then(delegate).should(times(1)).remove(TEST_KEY);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    protected static class TestType {
        // No extra methods
    }

    protected static class ExtendedTestType
    extends TestType {
        // No extra methods
    }
}
