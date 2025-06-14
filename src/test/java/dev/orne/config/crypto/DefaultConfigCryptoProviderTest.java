package dev.orne.config.crypto;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.crypto.ConfigCryptoEngine;
import dev.orne.config.crypto.ConfigCryptoProviderException;
import dev.orne.config.crypto.DefaultConfigCryptoProvider;

/**
 * Unit tests for {@code DefaultConfigCryptoProvider}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see DefaultConfigCryptoProvider
 */
@Tag("ut")
class DefaultConfigCryptoProviderTest {

    /**
     * Test for {@link DefaultConfigCryptoProvider#DefaultConfigCryptoProvider(ConfigCryptoEngine, boolean, SecretKey)}.
     */
    @Test
    void testConstructor()
    throws ConfigCryptoProviderException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final SecretKey key = mock(SecretKey.class);
        final DefaultConfigCryptoProvider provider = new DefaultConfigCryptoProvider(
                engine,
                false,
                key);
        assertSame(engine, provider.getEngine());
        assertSame(key, provider.getSecretKey());
    }

    /**
     * Test for {@link DefaultConfigCryptoProvider#DefaultConfigCryptoProvider(ConfigCryptoEngine, SecretKey)}.
     */
    @Test
    void testConstructorKey()
    throws ConfigCryptoProviderException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final SecretKey key = mock(SecretKey.class);
        final DefaultConfigCryptoProvider provider = new DefaultConfigCryptoProvider(
                engine,
                key);
        assertSame(engine, provider.getEngine());
        assertSame(key, provider.getSecretKey());
        then(engine).should(never()).createSecretKey(any());
    }

    /**
     * Test for {@link DefaultConfigCryptoProvider#getCipher()}.
     */
    @Test
    void testGetCipher()
    throws ConfigCryptoProviderException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final SecretKey key = mock(SecretKey.class);
        final Cipher cipher = mock(Cipher.class);
        willReturn(cipher).given(engine).createCipher();
        final DefaultConfigCryptoProvider provider = new DefaultConfigCryptoProvider(
                engine,
                key);
        then(engine).should(never()).createCipher();
        Cipher result = provider.getCipher();
        assertSame(cipher, result);
        then(engine).should(times(1)).createCipher();
        result = provider.getCipher();
        assertSame(cipher, result);
        then(engine).should(times(1)).createCipher();
    }

    /**
     * Test for {@link DefaultConfigCryptoProvider#getCipher()}.
     */
    @Test
    void testGetCipherSync()
    throws ConfigCryptoProviderException, InterruptedException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final SecretKey key = mock(SecretKey.class);
        final Cipher cipher = mock(Cipher.class);
        willReturn(cipher).given(engine).createCipher();
        final DefaultConfigCryptoProvider provider = spy(new DefaultConfigCryptoProvider(
                engine,
                key));
        then(engine).should(never()).createCipher();
        
        final int threadCount = 10;
        final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        final Semaphore semaphore = new Semaphore(0);
        final Semaphore mainSemaphore = new Semaphore(0);
        final List<Exception> exceptions = new ArrayList<Exception>();
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    semaphore.acquire();
                    final Cipher result = provider.getCipher();
                    assertSame(cipher, result);
                    mainSemaphore.release();
                } catch (final InterruptedException | ConfigCryptoProviderException e) {
                    exceptions.add(e);
                }
            });
        }
        semaphore.release(threadCount);
        mainSemaphore.acquire(threadCount);
        then(provider).should(times(threadCount)).getCipher();
        then(engine).should(times(1)).createCipher();
        assertEquals(Collections.emptyList(), exceptions);
    }

    /**
     * Test for {@link DefaultConfigCryptoProvider#encrypt(String)}.
     */
    @Test
    void testEncrypt()
    throws ConfigCryptoProviderException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String plainText = "mock plain text";
        final String cryptText = "mock encrypted text";
        final SecretKey key = mock(SecretKey.class);
        final Cipher cipher = mock(Cipher.class);
        willReturn(cryptText).given(engine).encrypt(plainText, key, cipher);
        final DefaultConfigCryptoProvider provider = spy(new DefaultConfigCryptoProvider(
                engine,
                key));
        willReturn(cipher).given(provider).getCipher();
        
        final String result = provider.encrypt(plainText);
        
        assertSame(cryptText, result);
        then(provider).should(times(1)).getCipher();
        then(engine).should(times(1)).encrypt(plainText, key, cipher);
    }

    /**
     * Test for {@link DefaultConfigCryptoProvider#encrypt(String)}.
     */
    @Test
    void testGetEncryptSync()
    throws ConfigCryptoProviderException, InterruptedException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String plainText = "mock plain text";
        final String cryptText = "mock encrypted text";
        final SecretKey key = mock(SecretKey.class);
        final Cipher cipher = mock(Cipher.class);
        willReturn(cryptText).given(engine).encrypt(plainText, key, cipher);
        final DefaultConfigCryptoProvider provider = spy(new DefaultConfigCryptoProvider(
                engine,
                key));
        willReturn(cipher).given(provider).getCipher();
        
        final int threadCount = 10;
        final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        final Semaphore semaphore = new Semaphore(0);
        final Semaphore mainSemaphore = new Semaphore(0);
        final List<Exception> exceptions = new ArrayList<Exception>();
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    semaphore.acquire();
                    final String result = provider.encrypt(plainText);
                    
                    assertSame(cryptText, result);
                    mainSemaphore.release();
                } catch (final InterruptedException | ConfigCryptoProviderException e) {
                    exceptions.add(e);
                }
            });
        }
        semaphore.release(threadCount);
        mainSemaphore.acquire(threadCount);
        then(provider).should(times(threadCount)).getCipher();
        then(engine).should(times(threadCount)).encrypt(plainText, key, cipher);
        assertEquals(Collections.emptyList(), exceptions);
    }

    /**
     * Test for {@link DefaultConfigCryptoProvider#decrypt(String)}.
     */
    @Test
    void testDecrypt()
    throws ConfigCryptoProviderException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String plainText = "mock plain text";
        final String cryptText = "mock encrypted text";
        final SecretKey key = mock(SecretKey.class);
        final Cipher cipher = mock(Cipher.class);
        willReturn(plainText).given(engine).decrypt(cryptText, key, cipher);
        final DefaultConfigCryptoProvider provider = spy(new DefaultConfigCryptoProvider(
                engine,
                key));
        willReturn(cipher).given(provider).getCipher();
        
        final String result = provider.decrypt(cryptText);
        
        assertSame(plainText, result);
        then(provider).should(times(1)).getCipher();
        then(engine).should(times(1)).decrypt(cryptText, key, cipher);
    }

    /**
     * Test for {@link DefaultConfigCryptoProvider#decrypt(String)}.
     */
    @Test
    void testGetDecryptSync()
    throws ConfigCryptoProviderException, InterruptedException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String plainText = "mock plain text";
        final String cryptText = "mock encrypted text";
        final SecretKey key = mock(SecretKey.class);
        final Cipher cipher = mock(Cipher.class);
        willReturn(plainText).given(engine).decrypt(cryptText, key, cipher);
        final DefaultConfigCryptoProvider provider = spy(new DefaultConfigCryptoProvider(
                engine,
                key));
        willReturn(cipher).given(provider).getCipher();
        
        final int threadCount = 10;
        final ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        final Semaphore semaphore = new Semaphore(0);
        final Semaphore mainSemaphore = new Semaphore(0);
        final List<Exception> exceptions = new ArrayList<Exception>();
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    semaphore.acquire();
                    final String result = provider.decrypt(cryptText);
                    
                    assertSame(plainText, result);
                    mainSemaphore.release();
                } catch (final InterruptedException | ConfigCryptoProviderException e) {
                    exceptions.add(e);
                }
            });
        }
        semaphore.release(threadCount);
        mainSemaphore.acquire(threadCount);
        then(provider).should(times(threadCount)).getCipher();
        then(engine).should(times(threadCount)).decrypt(cryptText, key, cipher);
        assertEquals(Collections.emptyList(), exceptions);
    }
}
