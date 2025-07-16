package dev.orne.config.commons;

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

import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.ConfigCryptoProvider;
import dev.orne.config.ConfigCryptoProviderException;

/**
 * Unit tests for {@code DelegatedOrneConfigurationDecoder}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see DelegatedOrneConfigurationDecoder
 */
@Tag("ut")
class DelegatedOrneConfigurationDecoderTest {

    /**
     * Test method for {@link DelegatedOrneConfigurationDecoder#ProviderConfigurationDecoder(ConfigCryptoProvider)}.
     */
    @Test
    void testConstructor() {
        final ConfigCryptoProvider provider = mock(ConfigCryptoProvider.class);
        final DelegatedOrneConfigurationDecoder decoder = new DelegatedOrneConfigurationDecoder(provider);
        assertSame(provider, decoder.getProvider());
    }

    /**
     * Test method for {@link DelegatedOrneConfigurationDecoder#ProviderConfigurationDecoder(ConfigCryptoProvider)}
     * with {@code null} parameter.
     */
    @Test
    void testConstructorNull() {
        assertThrows(NullPointerException.class, () -> {
            new DelegatedOrneConfigurationDecoder(null);
        });
    }

    /**
     * Test method for {@link DelegatedOrneConfigurationDecoder#decode(String)}.
     */
    @Test
    void testDecode()
    throws ConfigCryptoProviderException {
        final ConfigCryptoProvider provider = mock(ConfigCryptoProvider.class);
        final DelegatedOrneConfigurationDecoder decoder = new DelegatedOrneConfigurationDecoder(provider);
        final String value = "mock value";
        final String mockResult = "mock result";
        willReturn(mockResult).given(provider).decrypt(value);
        final String result = decoder.decode(value);
        then(provider).should().decrypt(value);
        assertEquals(mockResult, result);
    }

    /**
     * Test method for {@link DelegatedOrneConfigurationDecoder#decode(String)}.
     */
    @Test
    void testDecode_Error()
    throws ConfigCryptoProviderException {
        final ConfigCryptoProvider provider = mock(ConfigCryptoProvider.class);
        final DelegatedOrneConfigurationDecoder decoder = new DelegatedOrneConfigurationDecoder(provider);
        final String value = "mock value";
        final ConfigCryptoProviderException mockError = new ConfigCryptoProviderException();
        willThrow(mockError).given(provider).decrypt(value);
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            decoder.decode(value);
        });
        then(provider).should().decrypt(value);
        assertSame(mockError, result.getCause());
    }
}
