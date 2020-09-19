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

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.validation.constraints.NotNull;

/**
 * Default implementation of {@code ConfigCryptoProvider} based on
 * {@code ConfigCryptoEngine} with synchronized {@code Cipher}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-08
 * @since 0.2
 * @see ConfigCryptoProvider
 * @see ConfigCryptoEngine
 */
public class DefaultConfigCryptoProvider
implements ConfigCryptoProvider {

    /** The cryptographic engine. */
    private final ConfigCryptoEngine engine;
    /** The {@code Cipher} to use during encryption and decryption. */
    private Cipher cipher;
    /** The secret key to use during encryption and decryption. */
    private final SecretKey secretKey;

    /**
     * Creates a new instance for the specified algorithm and secret key.
     * 
     * @param engine The cryptographic engine
     * @param password The password to use to build the secret key
     * @throws ConfigCryptoProviderException If an error occurs creating the
     * secret key
     */
    public DefaultConfigCryptoProvider(
            final ConfigCryptoEngine engine,
            final String password)
    throws ConfigCryptoProviderException {
        this.engine = engine;
        this.secretKey = engine.createSecretKey(password);
    }

    /**
     * Creates a new instance for the specified algorithm and secret key.
     * 
     * @param engine The cryptographic engine
     * @param secretKey The secret key to use during encryption and decryption
     */
    public DefaultConfigCryptoProvider(
            final ConfigCryptoEngine engine,
            final SecretKey secretKey) {
        this.engine = engine;
        this.secretKey = secretKey;
    }

    /**
     * Returns the cryptographic engine.
     * 
     * @return The cryptographic engine
     */
    protected ConfigCryptoEngine getEngine() {
        return this.engine;
    }

    /**
     * Returns the secret key to use during encryption and decryption.
     * 
     * @return The secret key to use during encryption and decryption
     */
    protected SecretKey getSecretKey() {
        return this.secretKey;
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
                this.cipher = this.engine.createCipher();
            }
            return this.cipher;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String encrypt(
            @NotNull
            final String value)
    throws ConfigCryptoProviderException {
        final Cipher opCipher = getCipher();
        synchronized (opCipher) {
            return this.engine.encrypt(value, this.secretKey, opCipher);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String decrypt(
            @NotNull
            final String value)
    throws ConfigCryptoProviderException {
        final Cipher opCipher = getCipher();
        synchronized (opCipher) {
            return this.engine.decrypt(value, this.secretKey, opCipher);
        }
    }
}
