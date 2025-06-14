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

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Default implementation of {@code ConfigCryptoProvider} based on
 * {@code ConfigCryptoEngine} with synchronized {@code Cipher}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2020-08
 * @since 0.2
 * @see ConfigCryptoProvider
 * @see ConfigCryptoEngine
 */
@API(status = API.Status.STABLE, since = "1.0")
public class DefaultConfigCryptoProvider
extends AbstractConfigCryptoProvider {

    /** The {@code Cipher} to use during encryption and decryption. */
    private Cipher cipher;

    /**
     * Creates a new instance with specified builder configuration options.
     * 
     * @param options The configured builder options.
     */
    @API(status = API.Status.INTERNAL, since = "1.0")
    public DefaultConfigCryptoProvider(
            final @NotNull CryptoProviderOptions options) {
        super(options);
    }

    /**
     * Creates a new instance.
     * 
     * @param engine The cryptographic engine to use.
     * @param destroyEngine If the engine must be destroyed with provider.
     * @param secretKey The secret key to use.
     */
    public DefaultConfigCryptoProvider(
            final @NotNull ConfigCryptoEngine engine,
            final boolean destroyEngine,
            final @NotNull SecretKey secretKey) {
        super(engine, destroyEngine, secretKey);
    }

    /**
     * Returns the shared {@code Cipher} to use during encryption and
     * decryption.
     * 
     * @return The {@code Cipher} to use during encryption and decryption
     * @throws ConfigCryptoProviderException If an error occurs creating the
     * shared {@code Cipher}
     */
    protected @NotNull Cipher getCipher()
    throws ConfigCryptoProviderException {
        synchronized (this) {
            if (this.cipher == null) {
                this.cipher = getEngine().createCipher();
            }
            return this.cipher;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String encrypt(
            final String value)
    throws ConfigCryptoProviderException {
        checkDestroyed();
        if (value == null) {
            return value;
        }
        final Cipher opCipher = getCipher();
        synchronized (opCipher) {
            return getEngine().encrypt(value, getSecretKey(), opCipher);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String decrypt(
            final String value)
    throws ConfigCryptoProviderException {
        checkDestroyed();
        if (value == null) {
            return value;
        }
        final Cipher opCipher = getCipher();
        synchronized (opCipher) {
            return getEngine().decrypt(value, getSecretKey(), opCipher);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        this.cipher = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        // Ignore cipher
        return super.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(
            final Object obj) {
        // Ignore cipher
        return super.equals(obj);
    }
}
