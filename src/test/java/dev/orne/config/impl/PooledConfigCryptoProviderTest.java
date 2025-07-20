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
import java.util.NoSuchElementException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.SoftReferenceObjectPool;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import dev.orne.config.ConfigCryptoEngine;
import dev.orne.config.ConfigCryptoProvider;
import dev.orne.config.ConfigCryptoProviderException;
import dev.orne.config.impl.PooledConfigCryptoProvider.PooledCipherFactory;

/**
 * Unit tests for {@code PooledConfigCryptoProvider}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see PooledConfigCryptoProvider
 */
@Tag("ut")
class PooledConfigCryptoProviderTest {


    private static final byte[] SALT = "mock salt bytes".getBytes(StandardCharsets.UTF_8);
    private static final char[] MOCK_PASS = "mock pass".toCharArray();

    /**
     * Test for {@link PooledConfigCryptoProvider#PooledConfigCryptoProvider(CryptoProviderOptions)}.
     */
    @Test
    void testBuilderAesEnginePass()
    throws ConfigCryptoProviderException {
        final PooledConfigCryptoProvider provider = assertInstanceOf(
                PooledConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withAesGcmEngine(SALT)
                    .withSecretKey(MOCK_PASS)
                    .pooled()
                    .build());
        assertInstanceOf(ConfigCryptoAesGcmEngine.class, provider.getEngine());
        assertEquals(provider.getEngine().createSecretKey(MOCK_PASS), provider.getSecretKey());
        assertTrue(provider.isDestroyEngine());
        assertFalse(provider.isDestroyed());
        final SoftReferenceObjectPool<?> pool = assertInstanceOf(
                SoftReferenceObjectPool.class,
                provider.getCiphersPool());
        final PooledCipherFactory factory = assertInstanceOf(
                PooledCipherFactory.class,
                pool.getFactory());
        assertSame(provider.getEngine(), factory.getEngine());
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#PooledConfigCryptoProvider(CryptoProviderOptions)}.
     */
    @Test
    void testBuilderAesEngine()
    throws ConfigCryptoProviderException {
        final SecretKey key = mock(SecretKey.class);
        final PooledConfigCryptoProvider provider = assertInstanceOf(
                PooledConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withAesGcmEngine(SALT)
                    .withSecretKey(key)
                    .pooled()
                    .build());
        assertInstanceOf(ConfigCryptoAesGcmEngine.class, provider.getEngine());
        assertSame(key, provider.getSecretKey());
        assertTrue(provider.isDestroyEngine());
        assertFalse(provider.isDestroyed());
        final SoftReferenceObjectPool<?> pool = assertInstanceOf(
                SoftReferenceObjectPool.class,
                provider.getCiphersPool());
        final PooledCipherFactory factory = assertInstanceOf(
                PooledCipherFactory.class,
                pool.getFactory());
        assertSame(provider.getEngine(), factory.getEngine());
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#PooledConfigCryptoProvider(CryptoProviderOptions)}.
     */
    @Test
    void testBuilderAesEngineKeyDoNotDestroy()
    throws ConfigCryptoProviderException {
        final SecretKey key = mock(SecretKey.class);
        final PooledConfigCryptoProvider provider = assertInstanceOf(
                PooledConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withAesGcmEngine(SALT, false)
                    .withSecretKey(key)
                    .pooled()
                    .build());
        assertInstanceOf(ConfigCryptoAesGcmEngine.class, provider.getEngine());
        assertSame(key, provider.getSecretKey());
        assertFalse(provider.isDestroyEngine());
        assertFalse(provider.isDestroyed());
        final SoftReferenceObjectPool<?> pool = assertInstanceOf(
                SoftReferenceObjectPool.class,
                provider.getCiphersPool());
        final PooledCipherFactory factory = assertInstanceOf(
                PooledCipherFactory.class,
                pool.getFactory());
        assertSame(provider.getEngine(), factory.getEngine());
    }

    /**
     * Test for {@link ConfigCryptoProvider#builder()}.
     */
    @Test
    void testBuilderCustomEnginePass()
    throws ConfigCryptoProviderException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final SecretKey key = mock(SecretKey.class);
        willReturn(key).given(engine).createSecretKey(MOCK_PASS);
        final PooledConfigCryptoProvider provider = assertInstanceOf(
                PooledConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withEngine(engine)
                    .withSecretKey(MOCK_PASS)
                    .pooled()
                    .build());
        assertSame(engine, provider.getEngine());
        assertSame(key, provider.getSecretKey());
        assertFalse(provider.isDestroyEngine());
        assertFalse(provider.isDestroyed());
        final SoftReferenceObjectPool<?> pool = assertInstanceOf(
                SoftReferenceObjectPool.class,
                provider.getCiphersPool());
        final PooledCipherFactory factory = assertInstanceOf(
                PooledCipherFactory.class,
                pool.getFactory());
        assertSame(provider.getEngine(), factory.getEngine());
        then(engine).should().createSecretKey(MOCK_PASS);
        then(engine).shouldHaveNoMoreInteractions();
        then(key).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ConfigCryptoProvider#builder()}.
     */
    @Test
    void testBuilderCustomEngineKey()
    throws ConfigCryptoProviderException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final SecretKey key = mock(SecretKey.class);
        willReturn(key).given(engine).createSecretKey(MOCK_PASS);
        final PooledConfigCryptoProvider provider = assertInstanceOf(
                PooledConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withEngine(engine)
                    .withSecretKey(key)
                    .pooled()
                    .build());
        assertSame(engine, provider.getEngine());
        assertSame(key, provider.getSecretKey());
        assertFalse(provider.isDestroyEngine());
        assertFalse(provider.isDestroyed());
        final SoftReferenceObjectPool<?> pool = assertInstanceOf(
                SoftReferenceObjectPool.class,
                provider.getCiphersPool());
        final PooledCipherFactory factory = assertInstanceOf(
                PooledCipherFactory.class,
                pool.getFactory());
        assertSame(provider.getEngine(), factory.getEngine());
        then(engine).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link ConfigCryptoProvider#builder()}.
     */
    @Test
    void testBuilderCustomEngineKeyDestroy()
    throws ConfigCryptoProviderException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final SecretKey key = mock(SecretKey.class);
        willReturn(key).given(engine).createSecretKey(MOCK_PASS);
        final PooledConfigCryptoProvider provider = assertInstanceOf(
                PooledConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withEngine(engine, true)
                    .withSecretKey(key)
                    .pooled()
                    .build());
        assertSame(engine, provider.getEngine());
        assertSame(key, provider.getSecretKey());
        assertTrue(provider.isDestroyEngine());
        assertFalse(provider.isDestroyed());
        final SoftReferenceObjectPool<?> pool = assertInstanceOf(
                SoftReferenceObjectPool.class,
                provider.getCiphersPool());
        final PooledCipherFactory factory = assertInstanceOf(
                PooledCipherFactory.class,
                pool.getFactory());
        assertSame(provider.getEngine(), factory.getEngine());
        then(engine).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PooledCipherFactory#PooledCipherFactory(ConfigCryptoEngine)}.
     */
    @Test
    void testPooledCipherFactoryConstructor() {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final PooledCipherFactory factory = new PooledCipherFactory(engine);
        assertSame(engine, factory.getEngine());
    }

    /**
     * Test for {@link PooledCipherFactory#create()}.
     */
    @Test
    void testPooledCipherFactoryCreate()
    throws ConfigCryptoProviderException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final Cipher cipher = mock(Cipher.class);
        final PooledCipherFactory factory = new PooledCipherFactory(engine);
        willReturn(cipher).given(engine).createCipher();
        final Cipher result = factory.create();
        assertSame(cipher, result);
        then(engine).should(times(1)).createCipher();
        then(engine).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PooledCipherFactory#create()}.
     */
    @Test
    void testPooledCipherFactoryCreateFail()
    throws ConfigCryptoProviderException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final PooledCipherFactory factory = new PooledCipherFactory(engine);
        final ConfigCryptoProviderException mockException = new ConfigCryptoProviderException();
        willThrow(mockException).given(engine).createCipher();
        final ConfigCryptoProviderException result = assertThrows(
                ConfigCryptoProviderException.class,
                factory::create);
        assertSame(mockException, result);
        then(engine).should(times(1)).createCipher();
        then(engine).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PooledCipherFactory#wrap(Cipher)}.
     */
    @Test
    void testPooledCipherFactoryWrap()
    throws ConfigCryptoProviderException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final Cipher cipher = mock(Cipher.class);
        final PooledCipherFactory factory = new PooledCipherFactory(engine);
        final PooledObject<Cipher> result = factory.wrap(cipher);
        assertNotNull(result);
        assertSame(cipher, result.getObject());
        then(engine).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#encrypt(String)}.
     */
    @Test
    void testEncrypt()
    throws Exception {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String plainText = "mock plain text";
        final String cryptText = "mock encrypted text";
        final SecretKey key = mock(SecretKey.class);
        @SuppressWarnings("unchecked")
        final ObjectPool<Cipher> pool = mock(ObjectPool.class);
        final Cipher cipher = mock(Cipher.class);
        willReturn(cryptText).given(engine).encrypt(plainText, key, cipher);
        final PooledConfigCryptoProvider provider = new PooledConfigCryptoProvider(
                engine,
                false,
                key,
                pool);
        willReturn(cipher).given(pool).borrowObject();
        
        final String result = provider.encrypt(plainText);
        
        assertSame(cryptText, result);
        final InOrder inOrder = inOrder(pool, engine);
        inOrder.verify(pool, times(1)).borrowObject();
        inOrder.verify(engine, times(1)).encrypt(plainText, key, cipher);
        inOrder.verify(pool, times(1)).returnObject(cipher);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#encrypt(String)}.
     */
    @Test
    void testEncryptBorrowFailIllegalState()
    throws Exception {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String plainText = "mock plain text";
        final SecretKey key = mock(SecretKey.class);
        @SuppressWarnings("unchecked")
        final ObjectPool<Cipher> pool = mock(ObjectPool.class);
        final IllegalStateException mockException = new IllegalStateException();
        final Cipher cipher = mock(Cipher.class);
        final PooledConfigCryptoProvider provider = new PooledConfigCryptoProvider(
                engine,
                false,
                key,
                pool);
        willThrow(mockException).given(pool).borrowObject();
        
        final IllegalStateException result = assertThrows(IllegalStateException.class, () -> {
            provider.encrypt(plainText);
        });
        
        assertSame(mockException, result);
        final InOrder inOrder = inOrder(pool, engine);
        inOrder.verify(pool, times(1)).borrowObject();
        inOrder.verify(engine, never()).encrypt(plainText, key, cipher);
        inOrder.verify(pool, never()).returnObject(cipher);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#encrypt(String)}.
     */
    @Test
    void testEncryptBorrowFailNoSuchElement()
    throws Exception {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String plainText = "mock plain text";
        final SecretKey key = mock(SecretKey.class);
        @SuppressWarnings("unchecked")
        final ObjectPool<Cipher> pool = mock(ObjectPool.class);
        final NoSuchElementException mockException = new NoSuchElementException();
        final Cipher cipher = mock(Cipher.class);
        final PooledConfigCryptoProvider provider = new PooledConfigCryptoProvider(
                engine,
                false,
                key,
                pool);
        willThrow(mockException).given(pool).borrowObject();
        
        final ConfigCryptoProviderException result = assertThrows(ConfigCryptoProviderException.class, () -> {
            provider.encrypt(plainText);
        });
        
        assertSame(mockException, result.getCause());
        final InOrder inOrder = inOrder(pool, engine);
        inOrder.verify(pool, times(1)).borrowObject();
        inOrder.verify(engine, never()).encrypt(plainText, key, cipher);
        inOrder.verify(pool, never()).returnObject(cipher);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#encrypt(String)}.
     */
    @Test
    void testEncryptBorrowFailFactory()
    throws Exception {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String plainText = "mock plain text";
        final SecretKey key = mock(SecretKey.class);
        @SuppressWarnings("unchecked")
        final ObjectPool<Cipher> pool = mock(ObjectPool.class);
        final Exception mockException = new Exception();
        final Cipher cipher = mock(Cipher.class);
        final PooledConfigCryptoProvider provider = new PooledConfigCryptoProvider(
                engine,
                false,
                key,
                pool);
        willThrow(mockException).given(pool).borrowObject();
        
        final ConfigCryptoProviderException result = assertThrows(ConfigCryptoProviderException.class, () -> {
            provider.encrypt(plainText);
        });
        
        assertSame(mockException, result.getCause());
        final InOrder inOrder = inOrder(pool, engine);
        inOrder.verify(pool, times(1)).borrowObject();
        inOrder.verify(engine, never()).encrypt(plainText, key, cipher);
        inOrder.verify(pool, never()).returnObject(cipher);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#encrypt(String)}.
     */
    @Test
    void testEncryptEncryptFail()
    throws Exception {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String plainText = "mock plain text";
        final SecretKey key = mock(SecretKey.class);
        @SuppressWarnings("unchecked")
        final ObjectPool<Cipher> pool = mock(ObjectPool.class);
        final Cipher cipher = mock(Cipher.class);
        final ConfigCryptoProviderException mockException = new ConfigCryptoProviderException();
        willThrow(mockException).given(engine).encrypt(plainText, key, cipher);
        final PooledConfigCryptoProvider provider = new PooledConfigCryptoProvider(
                engine,
                false,
                key,
                pool);
        willReturn(cipher).given(pool).borrowObject();
        
        final ConfigCryptoProviderException result = assertThrows(ConfigCryptoProviderException.class, () -> {
            provider.encrypt(plainText);
        });
        
        assertSame(mockException, result);
        final InOrder inOrder = inOrder(pool, engine);
        inOrder.verify(pool, times(1)).borrowObject();
        inOrder.verify(engine, times(1)).encrypt(plainText, key, cipher);
        inOrder.verify(pool, times(1)).returnObject(cipher);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#encrypt(String)}.
     */
    @Test
    void testEncryptReturnFail()
    throws Exception {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String plainText = "mock plain text";
        final String cryptText = "mock encrypted text";
        final SecretKey key = mock(SecretKey.class);
        @SuppressWarnings("unchecked")
        final ObjectPool<Cipher> pool = mock(ObjectPool.class);
        final Cipher cipher = mock(Cipher.class);
        final IllegalStateException mockException = new IllegalStateException();
        willReturn(cryptText).given(engine).encrypt(plainText, key, cipher);
        final PooledConfigCryptoProvider provider = new PooledConfigCryptoProvider(
                engine,
                false,
                key,
                pool);
        willReturn(cipher).given(pool).borrowObject();
        willThrow(mockException).given(pool).returnObject(cipher);
        
        final IllegalStateException result = assertThrows(IllegalStateException.class, () -> {
            provider.encrypt(plainText);
        });
        
        assertSame(mockException, result);
        final InOrder inOrder = inOrder(pool, engine);
        inOrder.verify(pool, times(1)).borrowObject();
        inOrder.verify(engine, times(1)).encrypt(plainText, key, cipher);
        inOrder.verify(pool, times(1)).returnObject(cipher);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#encrypt(String)}.
     */
    @Test
    void testEncryptEncryptReturnFail()
    throws Exception {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String plainText = "mock plain text";
        final SecretKey key = mock(SecretKey.class);
        @SuppressWarnings("unchecked")
        final ObjectPool<Cipher> pool = mock(ObjectPool.class);
        final Cipher cipher = mock(Cipher.class);
        final ConfigCryptoProviderException mockException = new ConfigCryptoProviderException();
        final IllegalStateException mockException2 = new IllegalStateException();
        willThrow(mockException).given(engine).encrypt(plainText, key, cipher);
        final PooledConfigCryptoProvider provider = new PooledConfigCryptoProvider(
                engine,
                false,
                key,
                pool);
        willReturn(cipher).given(pool).borrowObject();
        willThrow(mockException2).given(pool).returnObject(cipher);
        
        final ConfigCryptoProviderException result = assertThrows(ConfigCryptoProviderException.class, () -> {
            provider.encrypt(plainText);
        });
        
        assertSame(mockException, result);
        assertEquals(1, result.getSuppressed().length);
        assertSame(mockException2, result.getSuppressed()[0]);
        final InOrder inOrder = inOrder(pool, engine);
        inOrder.verify(pool, times(1)).borrowObject();
        inOrder.verify(engine, times(1)).encrypt(plainText, key, cipher);
        inOrder.verify(pool, times(1)).returnObject(cipher);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#decrypt(String)}.
     */
    @Test
    void testDecrypt()
    throws Exception {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String plainText = "mock plain text";
        final String cryptText = "mock encrypted text";
        final SecretKey key = mock(SecretKey.class);
        @SuppressWarnings("unchecked")
        final ObjectPool<Cipher> pool = mock(ObjectPool.class);
        final Cipher cipher = mock(Cipher.class);
        willReturn(plainText).given(engine).decrypt(cryptText, key, cipher);
        final PooledConfigCryptoProvider provider = new PooledConfigCryptoProvider(
                engine,
                false,
                key,
                pool);
        willReturn(cipher).given(pool).borrowObject();
        
        final String result = provider.decrypt(cryptText);
        
        assertSame(plainText, result);
        final InOrder inOrder = inOrder(pool, engine);
        inOrder.verify(pool, times(1)).borrowObject();
        inOrder.verify(engine, times(1)).decrypt(cryptText, key, cipher);
        inOrder.verify(pool, times(1)).returnObject(cipher);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#decrypt(String)}.
     */
    @Test
    void testDecryptBorrowFailIllegalState()
    throws Exception {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String cryptText = "mock encrypted text";
        final SecretKey key = mock(SecretKey.class);
        @SuppressWarnings("unchecked")
        final ObjectPool<Cipher> pool = mock(ObjectPool.class);
        final Cipher cipher = mock(Cipher.class);
        final IllegalStateException mockException = new IllegalStateException();
        final PooledConfigCryptoProvider provider = new PooledConfigCryptoProvider(
                engine,
                false,
                key,
                pool);
        willThrow(mockException).given(pool).borrowObject();
        
        final IllegalStateException result = assertThrows(IllegalStateException.class, () -> {
            provider.decrypt(cryptText);
        });
        
        assertSame(mockException, result);
        final InOrder inOrder = inOrder(pool, engine);
        inOrder.verify(pool, times(1)).borrowObject();
        inOrder.verify(engine, never()).decrypt(cryptText, key, cipher);
        inOrder.verify(pool, never()).returnObject(cipher);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#decrypt(String)}.
     */
    @Test
    void testDecryptBorrowFailNoSuchElement()
    throws Exception {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String cryptText = "mock encrypted text";
        final SecretKey key = mock(SecretKey.class);
        @SuppressWarnings("unchecked")
        final ObjectPool<Cipher> pool = mock(ObjectPool.class);
        final Cipher cipher = mock(Cipher.class);
        final NoSuchElementException mockException = new NoSuchElementException();
        final PooledConfigCryptoProvider provider = new PooledConfigCryptoProvider(
                engine,
                false,
                key,
                pool);
        willThrow(mockException).given(pool).borrowObject();
        
        final ConfigCryptoProviderException result = assertThrows(ConfigCryptoProviderException.class, () -> {
            provider.decrypt(cryptText);
        });
        
        assertSame(mockException, result.getCause());
        final InOrder inOrder = inOrder(pool, engine);
        inOrder.verify(pool, times(1)).borrowObject();
        inOrder.verify(engine, never()).decrypt(cryptText, key, cipher);
        inOrder.verify(pool, never()).returnObject(cipher);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#decrypt(String)}.
     */
    @Test
    void testDecryptBorrowFailFactory()
    throws Exception {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String cryptText = "mock encrypted text";
        final SecretKey key = mock(SecretKey.class);
        @SuppressWarnings("unchecked")
        final ObjectPool<Cipher> pool = mock(ObjectPool.class);
        final Cipher cipher = mock(Cipher.class);
        final Exception mockException = new Exception();
        final PooledConfigCryptoProvider provider = new PooledConfigCryptoProvider(
                engine,
                false,
                key,
                pool);
        willThrow(mockException).given(pool).borrowObject();
        
        final ConfigCryptoProviderException result = assertThrows(ConfigCryptoProviderException.class, () -> {
            provider.decrypt(cryptText);
        });
        
        assertSame(mockException, result.getCause());
        final InOrder inOrder = inOrder(pool, engine);
        inOrder.verify(pool, times(1)).borrowObject();
        inOrder.verify(engine, never()).decrypt(cryptText, key, cipher);
        inOrder.verify(pool, never()).returnObject(cipher);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#decrypt(String)}.
     */
    @Test
    void testDecryptDecryptFail()
    throws Exception {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String cryptText = "mock encrypted text";
        final SecretKey key = mock(SecretKey.class);
        @SuppressWarnings("unchecked")
        final ObjectPool<Cipher> pool = mock(ObjectPool.class);
        final Cipher cipher = mock(Cipher.class);
        final ConfigCryptoProviderException mockException = new ConfigCryptoProviderException();
        willThrow(mockException).given(engine).decrypt(cryptText, key, cipher);
        final PooledConfigCryptoProvider provider = new PooledConfigCryptoProvider(
                engine,
                false,
                key,
                pool);
        willReturn(cipher).given(pool).borrowObject();
        
        final ConfigCryptoProviderException result = assertThrows(ConfigCryptoProviderException.class, () -> {
            provider.decrypt(cryptText);
        });
        
        assertSame(mockException, result);
        final InOrder inOrder = inOrder(pool, engine);
        inOrder.verify(pool, times(1)).borrowObject();
        inOrder.verify(engine, times(1)).decrypt(cryptText, key, cipher);
        inOrder.verify(pool, times(1)).returnObject(cipher);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#decrypt(String)}.
     */
    @Test
    void testDecryptReturnFail()
    throws Exception {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String plainText = "mock plain text";
        final String cryptText = "mock encrypted text";
        final SecretKey key = mock(SecretKey.class);
        @SuppressWarnings("unchecked")
        final ObjectPool<Cipher> pool = mock(ObjectPool.class);
        final Cipher cipher = mock(Cipher.class);
        final IllegalStateException mockException = new IllegalStateException();
        willReturn(plainText).given(engine).decrypt(cryptText, key, cipher);
        final PooledConfigCryptoProvider provider = new PooledConfigCryptoProvider(
                engine,
                false,
                key,
                pool);
        willReturn(cipher).given(pool).borrowObject();
        willThrow(mockException).given(pool).returnObject(cipher);
        
        final IllegalStateException result = assertThrows(IllegalStateException.class, () -> {
            provider.decrypt(cryptText);
        });
        
        assertSame(mockException, result);
        final InOrder inOrder = inOrder(pool, engine);
        inOrder.verify(pool, times(1)).borrowObject();
        inOrder.verify(engine, times(1)).decrypt(cryptText, key, cipher);
        inOrder.verify(pool, times(1)).returnObject(cipher);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#decrypt(String)}.
     */
    @Test
    void testDecryptDecryptReturnFail()
    throws Exception {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final String cryptText = "mock encrypted text";
        final SecretKey key = mock(SecretKey.class);
        @SuppressWarnings("unchecked")
        final ObjectPool<Cipher> pool = mock(ObjectPool.class);
        final Cipher cipher = mock(Cipher.class);
        final ConfigCryptoProviderException mockException = new ConfigCryptoProviderException();
        final IllegalStateException mockException2 = new IllegalStateException();
        willThrow(mockException).given(engine).decrypt(cryptText, key, cipher);
        final PooledConfigCryptoProvider provider = new PooledConfigCryptoProvider(
                engine,
                false,
                key,
                pool);
        willReturn(cipher).given(pool).borrowObject();
        willThrow(mockException2).given(pool).returnObject(cipher);
        
        final ConfigCryptoProviderException result = assertThrows(ConfigCryptoProviderException.class, () -> {
            provider.decrypt(cryptText);
        });
        
        assertSame(mockException, result);
        assertEquals(1, result.getSuppressed().length);
        assertSame(mockException2, result.getSuppressed()[0]);
        final InOrder inOrder = inOrder(pool, engine);
        inOrder.verify(pool, times(1)).borrowObject();
        inOrder.verify(engine, times(1)).decrypt(cryptText, key, cipher);
        inOrder.verify(pool, times(1)).returnObject(cipher);
        inOrder.verifyNoMoreInteractions();
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#destroy()}.
     */
    @Test
    void testDestroy()
    throws ConfigCryptoProviderException, DestroyFailedException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final SecretKey key = mock(SecretKey.class);
        final PooledConfigCryptoProvider provider = assertInstanceOf(
                PooledConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withEngine(engine)
                    .withSecretKey(key)
                    .pooled()
                    .build());
        assertFalse(provider.isDestroyEngine());
        assertFalse(provider.isDestroyed());
        SoftReferenceObjectPool<?> pool = assertInstanceOf(
                SoftReferenceObjectPool.class,
                provider.getCiphersPool());
        PooledCipherFactory factory = assertInstanceOf(
                PooledCipherFactory.class,
                pool.getFactory());
        assertSame(provider.getEngine(), factory.getEngine());
        then(key).shouldHaveNoInteractions();
        then(engine).shouldHaveNoInteractions();
        provider.destroy();
        assertFalse(provider.isDestroyEngine());
        assertTrue(provider.isDestroyed());
        then(key).should().destroy();
        then(key).shouldHaveNoMoreInteractions();
        then(engine).shouldHaveNoInteractions();
        assertThrows(IllegalStateException.class, () -> provider.encrypt("Mock text"));
        assertThrows(IllegalStateException.class, () -> provider.decrypt("Mock text"));
    }

    /**
     * Test for {@link PooledConfigCryptoProvider#destroy()}.
     */
    @Test
    void testDestroyEngine()
    throws ConfigCryptoProviderException, DestroyFailedException {
        final ConfigCryptoEngine engine = mock(ConfigCryptoEngine.class);
        final SecretKey key = mock(SecretKey.class);
        final PooledConfigCryptoProvider provider = assertInstanceOf(
                PooledConfigCryptoProvider.class,
                ConfigCryptoProvider.builder()
                    .withEngine(engine, true)
                    .withSecretKey(key)
                    .pooled()
                    .build());
        assertTrue(provider.isDestroyEngine());
        assertFalse(provider.isDestroyed());
        SoftReferenceObjectPool<?> pool = assertInstanceOf(
                SoftReferenceObjectPool.class,
                provider.getCiphersPool());
        PooledCipherFactory factory = assertInstanceOf(
                PooledCipherFactory.class,
                pool.getFactory());
        assertSame(provider.getEngine(), factory.getEngine());
        then(key).shouldHaveNoInteractions();
        then(engine).shouldHaveNoInteractions();
        provider.destroy();
        assertTrue(provider.isDestroyEngine());
        assertTrue(provider.isDestroyed());
        then(key).should().destroy();
        then(key).shouldHaveNoMoreInteractions();
        then(engine).should().destroy();
        then(engine).shouldHaveNoMoreInteractions();
        assertThrows(IllegalStateException.class, () -> provider.encrypt("Mock text"));
        assertThrows(IllegalStateException.class, () -> provider.decrypt("Mock text"));
    }
}
