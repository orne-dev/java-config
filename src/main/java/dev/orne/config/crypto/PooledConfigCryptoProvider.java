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

import java.lang.ref.SoftReference;
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
import org.apiguardian.api.API;

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
@API(status = API.Status.STABLE, since = "1.0")
public class PooledConfigCryptoProvider
extends AbstractConfigCryptoProvider {

    /** The pool of {@code Cipher} instances to use. */
    private final @NotNull ObjectPool<Cipher> ciphersPool;

    /**
     * Creates a new instance with specified builder configuration options.
     * 
     * @param options The configured builder options.
     */
    @API(status = API.Status.INTERNAL, since = "1.0")
    public PooledConfigCryptoProvider(
            final @NotNull CryptoProviderOptions options) {
        super(options);
        this.ciphersPool = new SoftReferenceObjectPool<>(new PooledCipherFactory(options.getEngine()));
    }

    /**
     * Creates a new instance.
     * 
     * @param engine The cryptographic engine to use.
     * @param destroyEngine If the engine must be destroyed with provider.
     * @param secretKey The secret key to use.
     */
    public PooledConfigCryptoProvider(
            final @NotNull ConfigCryptoEngine engine,
            final boolean destroyEngine,
            final @NotNull SecretKey secretKey) {
        this(
                engine,
                destroyEngine,
                secretKey,
                new SoftReferenceObjectPool<>(new PooledCipherFactory(engine)));
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
            final @NotNull ConfigCryptoEngine engine,
            final boolean destroyEngine,
            final @NotNull SecretKey secretKey,
            final @NotNull ObjectPool<Cipher> pool) {
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
    protected @NotNull ObjectPool<Cipher> getCiphersPool() {
        return this.ciphersPool;
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
                return encrypt(value, cipher);
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
                return decrypt(value, cipher);
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
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        // Ignore cipher pool
        return super.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(
            final Object obj) {
        // Ignore cipher pool
        return super.equals(obj);
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
