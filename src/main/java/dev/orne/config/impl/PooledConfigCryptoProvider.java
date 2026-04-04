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

import java.lang.ref.SoftReference;
import java.util.NoSuchElementException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import org.apache.commons.lang3.function.FailableFunction;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.PooledSoftReference;
import org.apache.commons.pool2.impl.SoftReferenceObjectPool;
import org.apiguardian.api.API;
import org.springframework.lang.Nullable;

import dev.orne.config.ConfigCryptoEngine;
import dev.orne.config.ConfigCryptoProviderException;

/**
 * Default implementation of {@code ConfigCryptoProvider} based on
 * {@code ConfigCryptoEngine} with pooled {@code Cipher}s.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2020-08
 * @since 0.2
 * @see Cipher
 * @see SecretKey
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class PooledConfigCryptoProvider
extends AbstractConfigCryptoProvider {

    /** The pool of {@code Cipher} instances to use. */
    private final ObjectPool<Cipher> ciphersPool;

    /**
     * Creates a new instance with specified builder configuration options.
     * 
     * @param options The configured builder options.
     */
    public PooledConfigCryptoProvider(
            final CryptoProviderOptions options) {
        super(options);
        this.ciphersPool = new SoftReferenceObjectPool<>(new PooledCipherFactory(options.getEngine()));
    }

    /**
     * Creates a new instance.
     * 
     * @param engine The cryptographic engine to use.
     * @param destroyEngine If the engine must be destroyed with provider.
     * @param secretKey The secret key to use.
     * @param pool The pool of {@code Cipher} instances to use.
     */
    protected PooledConfigCryptoProvider(
            final ConfigCryptoEngine engine,
            final boolean destroyEngine,
            final SecretKey secretKey,
            final ObjectPool<Cipher> pool) {
        super(engine, destroyEngine, secretKey);
        this.ciphersPool = pool;
    }

    /**
     * Returns the pool of {@code Cipher}s to use during encryption and
     * decryption.
     * 
     * @return The pool of {@code Cipher}s to use during encryption and
     * decryption
     */
    protected ObjectPool<Cipher> getCiphersPool() {
        return this.ciphersPool;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String encrypt(
            final @Nullable String value)
    throws ConfigCryptoProviderException {
        checkDestroyed();
        return withCipher(cipher -> encrypt(value, cipher));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String decrypt(
            final @Nullable String value)
    throws ConfigCryptoProviderException {
        checkDestroyed();
        return withCipher(cipher -> decrypt(value, cipher));
    }

    /**
     * Performs the specified operation with a {@code Cipher} instance from the
     * pool, returning the instance to the pool after the operation is completed.
     * 
     * @param operation The operation to perform with the {@code Cipher} instance.
     * @return The result of the operation.
     */
    protected String withCipher(
            final FailableFunction<Cipher, String, ConfigCryptoProviderException> operation) {
        final Cipher cipher;
        try {
            cipher = this.ciphersPool.borrowObject();
        } catch (final IllegalStateException | NoSuchElementException e) {
            throw new ConfigCryptoProviderException(
                    "Cannot allocate cipher from pool", e);
        } catch (final Exception e) {
            throw new ConfigCryptoProviderException(
                    "Error allocating cipher from pool", e);
        }
        ConfigCryptoProviderException operationException = null;
        String result = null;
        try {
            result = operation.apply(cipher);
        } catch (final ConfigCryptoProviderException ccpe) {
            operationException = ccpe;
        } finally {
            try {
                this.ciphersPool.returnObject(cipher);
            } catch (final Exception e) {
                final ConfigCryptoProviderException te = new ConfigCryptoProviderException(
                        "Error returning cipher to pool", e);
                if (operationException != null) {
                    operationException.addSuppressed(te);
                } else {
                    operationException = te;
                }
            }
        }
        if (operationException != null) {
            throw operationException;
        }
        return result;
    }

    /**
     * Implementation of {@code PooledObjectFactory} that creates new instances
     * of {@code Cipher} using an instance of {@code ConfigCryptoEngine}.
     * 
     * @author <a href="mailto:https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2020-08
     * @since 0.2
     * @see PooledObjectFactory
     */
    @API(status = API.Status.INTERNAL, since = "1.0")
    public static class PooledCipherFactory
    extends BasePooledObjectFactory<Cipher> {

        /** The cryptographic engine. */
        private final ConfigCryptoEngine engine;

        /**
         * Creates a new instance.
         * 
         * @param engine The cryptographic engine
         */
        public PooledCipherFactory(
                final ConfigCryptoEngine engine) {
            super();
            this.engine = engine;
        }

        /**
         * Returns the cryptographic engine.
         * 
         * @return The cryptographic engine
         */
        protected ConfigCryptoEngine getEngine() {
            return engine;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Cipher create()
        throws ConfigCryptoProviderException {
            return this.engine.createCipher();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PooledObject<Cipher> wrap(
                final Cipher obj) {
            return new PooledSoftReference<>(new SoftReference<>(obj));
        }
    }
}
