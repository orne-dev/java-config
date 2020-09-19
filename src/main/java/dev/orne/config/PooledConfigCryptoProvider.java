package dev.orne.config;

import java.lang.ref.SoftReference;

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

import java.util.NoSuchElementException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.validation.constraints.NotNull;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.PooledSoftReference;
import org.apache.commons.pool2.impl.SoftReferenceObjectPool;

/**
 * Default implementation of {@code ConfigCryptoProvider} based on
 * {@code ConfigCryptoEngine} with synchronized {@code Cipher}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-08
 * @since 0.2
 * @see Cipher
 * @see SecretKey
 */
public class PooledConfigCryptoProvider
implements ConfigCryptoProvider {

    /** The cryptographic engine. */
    private final @NotNull ConfigCryptoEngine engine;
    /** The pool of {@code Cipher} to use during encryption and decryption. */
    private final @NotNull ObjectPool<Cipher> ciphersPool;
    /** The secret key to use during encryption and decryption. */
    private final @NotNull SecretKey secretKey;

    /**
     * Creates a new instance with the specified cryptographic engine and
     * password. A new {@code ObjectPool} is created for the {@code Cipher}s
     * reused between threads.
     * 
     * @param engine The cryptographic engine
     * @param secretKey The secret key to use during encryption and decryption
     * @throws ConfigCryptoProviderException If an error occurs creating the
     * secret key
     */
    public PooledConfigCryptoProvider(
            final @NotNull ConfigCryptoEngine engine,
            final @NotNull String password)
    throws ConfigCryptoProviderException {
        this(engine, engine.createSecretKey(password));
    }

    /**
     * Creates a new instance with the specified cryptographic engine and
     * secret key. A new {@code ObjectPool} is created for the {@code Cipher}s
     * reused between threads.
     * 
     * @param engine The cryptographic engine
     * @param secretKey The secret key to use during encryption and decryption
     */
    public PooledConfigCryptoProvider(
            final @NotNull ConfigCryptoEngine engine,
            final @NotNull SecretKey secretKey) {
        this(engine, new SoftReferenceObjectPool<>(new PooledCipherFactory(engine)), secretKey);
    }

    /**
     * Creates a new instance with the specified cryptographic engine, secret
     * key and pool of {@code Cipher}s.
     * 
     * @param engine The cryptographic engine
     * @param pool The pool of {@code Cipher} to use during encryption and decryption
     * @param secretKey The secret key to use during encryption and decryption
     */
    public PooledConfigCryptoProvider(
            final @NotNull ConfigCryptoEngine engine,
            final @NotNull ObjectPool<Cipher> pool,
            final @NotNull SecretKey secretKey) {
        this.engine = engine;
        this.ciphersPool = pool;
        this.secretKey = secretKey;
    }

    /**
     * Returns the cryptographic engine.
     * 
     * @return The cryptographic engine
     */
    protected @NotNull ConfigCryptoEngine getEngine() {
        return this.engine;
    }

    /**
     * Returns the pool of {@code Cipher}s to use during encryption and
     * decryption.
     * 
     * @return The pool of {@code Cipher}s to use during encryption and
     * decryption
     */
    protected @NotNull ObjectPool<Cipher> getCiphersPool() {
        return this.ciphersPool;
    }

    /**
     * Returns the secret key to use during encryption and decryption.
     * 
     * @return The secret key to use during encryption and decryption
     */
    protected @NotNull SecretKey getSecretKey() {
        return this.secretKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String encrypt(
            final @NotNull String value)
    throws ConfigCryptoProviderException {
        ConfigCryptoProviderException encryptException = null;
        try {
            final Cipher cipher = this.ciphersPool.borrowObject();
            try {
                return this.engine.encrypt(value, this.secretKey, cipher);
            } catch (final ConfigCryptoProviderException ccpe) {
                encryptException = ccpe;
                throw ccpe;
            } finally {
                this.ciphersPool.returnObject(cipher);
            }
        } catch (final IllegalStateException ise) {
            if (encryptException == null) {
                throw ise;
            } else {
                encryptException.addSuppressed(ise);
                throw encryptException;
            }
        } catch (final ConfigCryptoProviderException ccpe) {
            throw ccpe;
        } catch (final NoSuchElementException nsee) {
            throw new ConfigCryptoProviderException("", nsee);
        } catch (final Exception e) {
            throw new ConfigCryptoProviderException("", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String decrypt(
            final @NotNull String value)
    throws ConfigCryptoProviderException {
        ConfigCryptoProviderException decryptException = null;
        try {
            final Cipher cipher = this.ciphersPool.borrowObject();
            try {
                return this.engine.decrypt(value, this.secretKey, cipher);
            } catch (final ConfigCryptoProviderException ccpe) {
                decryptException = ccpe;
                throw ccpe;
            } finally {
                this.ciphersPool.returnObject(cipher);
            }
        } catch (final IllegalStateException ise) {
            if (decryptException == null) {
                throw ise;
            } else {
                decryptException.addSuppressed(ise);
                throw decryptException;
            }
        } catch (final ConfigCryptoProviderException ccpe) {
            throw ccpe;
        } catch (final NoSuchElementException nsee) {
            throw new ConfigCryptoProviderException("", nsee);
        } catch (final Exception e) {
            throw new ConfigCryptoProviderException("", e);
        }
    }

    /**
     * Implementation of {@code PooledObjectFactory} that creates new instances
     * of {@code Cipher} using an instance of {@code ConfigCryptoEngine}.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0, 2020-08
     * @since 0.2
     * @see PooledObjectFactory
     */
    public static class PooledCipherFactory
    extends BasePooledObjectFactory<Cipher> {

        /** The cryptographic engine. */
        private final @NotNull ConfigCryptoEngine engine;

        /**
         * Creates a new instance.
         * 
         * @param engine The cryptographic engine
         */
        public PooledCipherFactory(
                final @NotNull ConfigCryptoEngine engine) {
            super();
            this.engine = engine;
        }

        /**
         * Returns the cryptographic engine.
         * 
         * @return The cryptographic engine
         */
        protected @NotNull ConfigCryptoEngine getEngine() {
            return engine;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull Cipher create()
        throws ConfigCryptoProviderException {
            return this.engine.createCipher();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public @NotNull PooledObject<Cipher> wrap(final @NotNull Cipher obj) {
            return new PooledSoftReference<>(new SoftReference<>(obj));
        }
    }
}
