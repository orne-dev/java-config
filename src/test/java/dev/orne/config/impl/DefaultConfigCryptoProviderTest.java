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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.ConfigCryptoEngine;
import dev.orne.config.ConfigCryptoProvider;
import dev.orne.config.ConfigCryptoProviderException;

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

    private static final byte[] SALT = "mock salt bytes".getBytes(StandardCharsets.UTF_8);
    private static final char[] MOCK_PASS = "mock pass".toCharArray();

    /**
     * Test for {@link ConfigCryptoProvider#builder()}.
     */
    @Test
    void testBuilder()
    throws ConfigCryptoProviderException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final SecretKey key = mock(SecretKey.class);
        willReturn(key).given(engine).createSecretKey(MOCK_PASS);
        DefaultConfigCryptoProvider provider = assertInstanceOf(
                DefaultConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withEngine(engine)
                    .withSecretKey(key)
                    .build());
        assertSame(engine, provider.getEngine());
        assertSame(key, provider.getSecretKey());
        assertFalse(provider.isDestroyEngine());
        assertFalse(provider.isDestroyed());
        provider = assertInstanceOf(
                DefaultConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withEngine(engine, true)
                    .withSecretKey(key)
                    .build());
        assertSame(engine, provider.getEngine());
        assertSame(key, provider.getSecretKey());
        assertTrue(provider.isDestroyEngine());
        assertFalse(provider.isDestroyed());
        provider = assertInstanceOf(
                DefaultConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withEngine(engine)
                    .withSecretKey(MOCK_PASS)
                    .build());
        assertSame(engine, provider.getEngine());
        assertSame(key, provider.getSecretKey());
        assertFalse(provider.isDestroyEngine());
        assertFalse(provider.isDestroyed());
        then(engine).should().createSecretKey(MOCK_PASS);
        then(engine).shouldHaveNoMoreInteractions();
        then(key).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ConfigCryptoProvider#builder()}.
     */
    @Test
    void testBuilderNewEngine()
    throws ConfigCryptoProviderException {
        final SecretKey key = mock(SecretKey.class);
        DefaultConfigCryptoProvider provider = assertInstanceOf(
                DefaultConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withAesGcmEngine(SALT)
                    .withSecretKey(key)
                    .build());
        assertInstanceOf(ConfigCryptoAesGcmEngine.class, provider.getEngine());
        assertSame(key, provider.getSecretKey());
        assertTrue(provider.isDestroyEngine());
        assertFalse(provider.isDestroyed());
        then(key).shouldHaveNoInteractions();
        provider = assertInstanceOf(
                DefaultConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withAesGcmEngine(SALT, false)
                    .withSecretKey(key)
                    .build());
        assertInstanceOf(ConfigCryptoAesGcmEngine.class, provider.getEngine());
        assertSame(key, provider.getSecretKey());
        assertFalse(provider.isDestroyEngine());
        assertFalse(provider.isDestroyed());
        then(key).shouldHaveNoInteractions();
        provider = assertInstanceOf(
                DefaultConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withAesGcmEngine(SALT)
                    .withSecretKey(MOCK_PASS)
                    .build());
        assertInstanceOf(ConfigCryptoAesGcmEngine.class, provider.getEngine());
        assertEquals(provider.getEngine().createSecretKey(MOCK_PASS), provider.getSecretKey());
        assertTrue(provider.isDestroyEngine());
        assertFalse(provider.isDestroyed());
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
        final DefaultConfigCryptoProvider provider = assertInstanceOf(
                DefaultConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withEngine(engine)
                    .withSecretKey(key)
                    .build());
        then(engine).should(never()).createCipher();
        Cipher result = provider.getCipher();
        assertSame(cipher, result);
        then(engine).should().createCipher();
        result = provider.getCipher();
        assertSame(cipher, result);
        then(engine).should().createCipher();
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
        final DefaultConfigCryptoProvider provider = spy(assertInstanceOf(
                DefaultConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withEngine(engine)
                    .withSecretKey(key)
                    .build()));
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
        then(engine).should().createCipher();
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
        final DefaultConfigCryptoProvider provider = spy(assertInstanceOf(
                DefaultConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withEngine(engine)
                    .withSecretKey(key)
                    .build()));
        willReturn(cipher).given(provider).getCipher();
        
        final String result = provider.encrypt(plainText);
        
        assertSame(cryptText, result);
        then(provider).should().getCipher();
        then(engine).should().encrypt(plainText, key, cipher);
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
        final DefaultConfigCryptoProvider provider = spy(assertInstanceOf(
                DefaultConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withEngine(engine)
                    .withSecretKey(key)
                    .build()));
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
        final DefaultConfigCryptoProvider provider = spy(assertInstanceOf(
                DefaultConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withEngine(engine)
                    .withSecretKey(key)
                    .build()));
        willReturn(cipher).given(provider).getCipher();
        
        final String result = provider.decrypt(cryptText);
        
        assertSame(plainText, result);
        then(provider).should().getCipher();
        then(engine).should().decrypt(cryptText, key, cipher);
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
        final DefaultConfigCryptoProvider provider = spy(assertInstanceOf(
                DefaultConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withEngine(engine)
                    .withSecretKey(key)
                    .build()));
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

    /**
     * Test for {@link DefaultConfigCryptoProvider#destroy()}.
     */
    @Test
    void testDestroy()
    throws ConfigCryptoProviderException, DestroyFailedException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final SecretKey key = mock(SecretKey.class);
        final Cipher cipher = mock(Cipher.class);
        final DefaultConfigCryptoProvider provider = assertInstanceOf(
                DefaultConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withEngine(engine)
                    .withSecretKey(key)
                    .build());
        willReturn(cipher).given(engine).createCipher();
        assertNotNull(provider.getCipher());
        assertFalse(provider.isDestroyEngine());
        assertFalse(provider.isDestroyed());
        then(key).shouldHaveNoInteractions();
        then(engine).should().createCipher();
        then(engine).shouldHaveNoMoreInteractions();
        provider.destroy();
        assertFalse(provider.isDestroyEngine());
        assertTrue(provider.isDestroyed());
        then(key).should().destroy();
        then(key).shouldHaveNoMoreInteractions();
        then(engine).should().createCipher();
        then(engine).shouldHaveNoMoreInteractions();
        assertThrows(IllegalStateException.class, () -> provider.encrypt("Mock text"));
        assertThrows(IllegalStateException.class, () -> provider.decrypt("Mock text"));
    }

    /**
     * Test for {@link DefaultConfigCryptoProvider#destroy()}.
     */
    @Test
    void testDestroyEngine()
    throws ConfigCryptoProviderException, DestroyFailedException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final SecretKey key = mock(SecretKey.class);
        final Cipher cipher = mock(Cipher.class);
        final DefaultConfigCryptoProvider provider = assertInstanceOf(
                DefaultConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withEngine(engine, true)
                    .withSecretKey(key)
                    .build());
        willReturn(cipher).given(engine).createCipher();
        assertNotNull(provider.getCipher());
        assertTrue(provider.isDestroyEngine());
        assertFalse(provider.isDestroyed());
        then(key).shouldHaveNoInteractions();
        then(engine).should().createCipher();
        then(engine).shouldHaveNoMoreInteractions();
        provider.destroy();
        assertTrue(provider.isDestroyEngine());
        assertTrue(provider.isDestroyed());
        then(key).should().destroy();
        then(key).shouldHaveNoMoreInteractions();
        then(engine).should().createCipher();
        then(engine).should().destroy();
        then(engine).shouldHaveNoMoreInteractions();
        assertThrows(IllegalStateException.class, () -> provider.encrypt("Mock text"));
        assertThrows(IllegalStateException.class, () -> provider.decrypt("Mock text"));
    }
}
