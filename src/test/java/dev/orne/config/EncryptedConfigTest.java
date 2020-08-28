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
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code EncryptedConfig}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @since 1.0
 * @version 0.2
 */
@Tag("ut")
class EncryptedConfigTest {

    private static final String TEST_KEY = "test.key";

    /**
     * Test method for {@link EncryptedConfig#EncryptedConfig(Config, ConfigCryptoProvider)}.
     */
    @Test
    void testConstructor() {
        final Config delegate = mock(Config.class);
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        final EncryptedConfig config = new EncryptedConfig(delegate, cryptoProvider);
        assertSame(delegate, config.getDelegate());
        assertSame(cryptoProvider, config.getCryptoProvider());
    }

    /**
     * Test method for {@link EncryptedConfig#EncryptedConfig(Config, ConfigCryptoProvider)}.
     */
    @Test
    void testConstructorNullConfig() {
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        assertThrows(NullPointerException.class, () -> {
            new EncryptedConfig(null, cryptoProvider);
        });
    }

    /**
     * Test method for {@link EncryptedConfig#EncryptedConfig(Config, ConfigCryptoProvider)}.
     */
    @Test
    void testConstructorNullProvider() {
        final Config delegate = mock(Config.class);
        assertThrows(NullPointerException.class, () -> {
            new EncryptedConfig(delegate, null);
        });
    }

    /**
     * Test method for {@link EncryptedConfig#EncryptedConfig(Config, ConfigCryptoProvider)}.
     */
    @Test
    void testConstructorNulls() {
        assertThrows(NullPointerException.class, () -> {
            new EncryptedConfig(null, null);
        });
    }

    /**
     * Test method for {@link EncryptedConfig#getParameter(String, Class)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetParameter() throws ConfigException {
        final Config delegate = mock(Config.class);
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        final EncryptedConfig config = spy(new EncryptedConfig(delegate, cryptoProvider));
        
        final String encryptedValue = "mock encrypted value";
        final String rawValue = "mock raw value";
        final Class<?> targetClass = Object.class;
        final Object expectedValue = new Object();
        willReturn(encryptedValue).given(delegate).getString(TEST_KEY);
        willReturn(rawValue).given(cryptoProvider).decrypt(encryptedValue);
        willReturn(expectedValue).given(config).convertValue(rawValue, targetClass);
        
        final Object result = config.getParameter(TEST_KEY, targetClass);
        assertSame(expectedValue, result);
        
        then(delegate).should(times(1)).getString(TEST_KEY);
        then(cryptoProvider).should(times(1)).decrypt(encryptedValue);
        then(config).should(times(1)).convertValue(rawValue, targetClass);
    }

    /**
     * Test method for {@link EncryptedConfig#getParameter(String, Class)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetParameterNullPlaceholder() throws ConfigException {
        final Config delegate = mock(Config.class);
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        final EncryptedConfig config = spy(new EncryptedConfig(delegate, cryptoProvider));
        
        final String encryptedValue = "mock encrypted value";
        final String rawValue = AbstractStringConfig.NULL;
        final Class<?> targetClass = Object.class;
        willReturn(encryptedValue).given(delegate).getString(TEST_KEY);
        willReturn(rawValue).given(cryptoProvider).decrypt(encryptedValue);
        
        final Object result = config.getParameter(TEST_KEY, targetClass);
        assertNull(result);
        
        then(delegate).should(times(1)).getString(TEST_KEY);
        then(cryptoProvider).should(times(1)).decrypt(encryptedValue);
        then(config).should(never()).convertValue(any(), any());
    }

    /**
     * Test method for {@link EncryptedConfig#getStringParameter(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetStringParameter() throws ConfigException {
        final Config delegate = mock(Config.class);
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        final EncryptedConfig config = spy(new EncryptedConfig(delegate, cryptoProvider));
        
        final String encryptedValue = "mock encrypted value";
        final String rawValue = "mock raw value";
        final Object expectedValue = rawValue;
        willReturn(encryptedValue).given(delegate).getString(TEST_KEY);
        willReturn(rawValue).given(cryptoProvider).decrypt(encryptedValue);
        
        final Object result = config.getStringParameter(TEST_KEY);
        assertSame(expectedValue, result);
        
        then(delegate).should(times(1)).getString(TEST_KEY);
        then(cryptoProvider).should(times(1)).decrypt(encryptedValue);
        then(config).should(never()).convertValue(any(), any());
    }

    /**
     * Test method for {@link EncryptedConfig#getStringParameter(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetStringParameterNullPlaceholder() throws ConfigException {
        final Config delegate = mock(Config.class);
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        final EncryptedConfig config = spy(new EncryptedConfig(delegate, cryptoProvider));
        
        final String encryptedValue = "mock encrypted value";
        final String rawValue = AbstractStringConfig.NULL;
        willReturn(encryptedValue).given(delegate).getString(TEST_KEY);
        willReturn(rawValue).given(cryptoProvider).decrypt(encryptedValue);
        
        final Object result = config.getStringParameter(TEST_KEY);
        assertNull(result);
        
        then(delegate).should(times(1)).getString(TEST_KEY);
        then(cryptoProvider).should(times(1)).decrypt(encryptedValue);
        then(config).should(never()).convertValue(any(), any());
    }

    /**
     * Test method for {@link EncryptedConfig#getBooleanParameter(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetBooleanParameter() throws ConfigException {
        final Config delegate = mock(Config.class);
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        final EncryptedConfig config = spy(new EncryptedConfig(delegate, cryptoProvider));
        
        final String encryptedValue = "mock encrypted value";
        final String rawValue = "mock raw value";
        final Class<?> targetClass = Boolean.class;
        final Object expectedValue = Boolean.TRUE;
        willReturn(encryptedValue).given(delegate).getString(TEST_KEY);
        willReturn(rawValue).given(cryptoProvider).decrypt(encryptedValue);
        willReturn(expectedValue).given(config).convertValue(rawValue, targetClass);
        
        final Object result = config.getBooleanParameter(TEST_KEY);
        assertSame(expectedValue, result);
        
        then(delegate).should(times(1)).getString(TEST_KEY);
        then(cryptoProvider).should(times(1)).decrypt(encryptedValue);
        then(config).should(times(1)).convertValue(rawValue, targetClass);
    }

    /**
     * Test method for {@link EncryptedConfig#getNumberParameter(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetNumberParameter() throws ConfigException {
        final Config delegate = mock(Config.class);
        final ConfigCryptoProvider cryptoProvider = mock(ConfigCryptoProvider.class);
        final EncryptedConfig config = spy(new EncryptedConfig(delegate, cryptoProvider));
        
        final String encryptedValue = "mock encrypted value";
        final String rawValue = "mock raw value";
        final Class<?> targetClass = BigDecimal.class;
        final Object expectedValue = BigDecimal.TEN;
        willReturn(encryptedValue).given(delegate).getString(TEST_KEY);
        willReturn(rawValue).given(cryptoProvider).decrypt(encryptedValue);
        willReturn(expectedValue).given(config).convertValue(rawValue, targetClass);
        
        final Object result = config.getNumberParameter(TEST_KEY);
        assertSame(expectedValue, result);
        
        then(delegate).should(times(1)).getString(TEST_KEY);
        then(cryptoProvider).should(times(1)).decrypt(encryptedValue);
        then(config).should(times(1)).convertValue(rawValue, targetClass);
    }
}
