package dev.orne.config.crypto;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2020 - 2025 Orne Developments
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

import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Base abstract implementation of {@code ConfigCryptoProvider} based on
 * {@code ConfigCryptoEngine}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see ConfigCryptoProvider
 * @see ConfigCryptoEngine
 */
@API(status = API.Status.STABLE, since = "1.0")
public abstract class AbstractConfigCryptoProvider
implements ConfigCryptoProvider {

    /** The cryptographic engine. */
    private final @NotNull ConfigCryptoEngine engine;
    /** If the engine must be destroyed with provider. */
    private final boolean destroyEngine;
    /** The secret key to use. */
    private final @NotNull SecretKey secretKey;
    /** If the provider has been destroyed. */
    private boolean destroyed;

    /**
     * Creates a new instance with specified builder configuration options.
     * 
     * @param options The configured builder options.
     */
    @API(status = API.Status.INTERNAL, since = "1.0")
    protected AbstractConfigCryptoProvider(
            final @NotNull CryptoProviderOptions options) {
        this.engine = options.getEngine();
        this.destroyEngine = options.isDestroyEngine();
        this.secretKey = options.getKey();
    }

    /**
     * Creates a new instance.
     * 
     * @param engine The cryptographic engine to use.
     * @param destroyEngine If the engine must be destroyed with provider.
     * @param secretKey The secret key to use.
     */
    protected AbstractConfigCryptoProvider(
            final @NotNull ConfigCryptoEngine engine,
            final boolean destroyEngine,
            final @NotNull SecretKey secretKey) {
        super();
        this.engine = engine;
        this.destroyEngine = destroyEngine;
        this.secretKey = secretKey;
    }

    /**
     * Returns the cryptographic engine.
     * 
     * @return The cryptographic engine.
     */
    protected @NotNull ConfigCryptoEngine getEngine() {
        return this.engine;
    }

    /**
     * Returns the secret key to use during encryption and decryption.
     * 
     * @return The secret key.
     */
    protected @NotNull SecretKey getSecretKey() {
        return this.secretKey;
    }

    /**
     * Returns {@code true} if the engine must be destroyed with provider.
     * 
     * @return If the engine must be destroyed with provider.
     */
    protected boolean isDestroyEngine() {
        return this.destroyEngine;
    }

    /**
     * Encrypts the specified plain configuration value.
     * 
     * @param value The plain configuration value
     * @param cipher The cipher to use.
     * @return The encrypted configuration value
     * @throws ConfigCryptoProviderException If an exception occurs during the
     * encryption process
     */
    public String encrypt(
            final String value,
            final Cipher cipher)
    throws ConfigCryptoProviderException {
        checkDestroyed();
        if (value == null) {
            return value;
        }
        return this.engine.encrypt(value, this.secretKey, cipher);
    }

    /**
     * Decrypts the specified encrypted configuration value.
     * 
     * @param value The encrypted configuration value
     * @param cipher The cipher to use.
     * @return The plain configuration value
     * @throws ConfigCryptoProviderException If an exception occurs during the
     * decryption process
     */
    public String decrypt(
            final String value,
            final Cipher cipher)
    throws ConfigCryptoProviderException {
        checkDestroyed();
        return this.engine.decrypt(value, this.secretKey, cipher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        this.destroyed = true;
        try {
            this.secretKey.destroy();
        } catch (DestroyFailedException e) {
            throw new ConfigCryptoProviderException("Error destroying secret key", e);
        }
        if (this.destroyEngine) {
            this.engine.destroy();
        }
    }

    /**
     * Returns {@code true} if the cryptographic provider has been destroyed.
     * 
     * @return If the cryptographic provider has been destroyed.
     */
    public boolean isDestroyed() {
        return this.destroyed;
    }

    /**
     * Checks if the cryptographic provider has been destroyed.
     * 
     * @throws IllegalStateException If the cryptographic provider has been
     * destroyed.
     */
    protected void checkDestroyed() {
        if (this.destroyed) {
            throw new IllegalStateException("The cryptographic engine has been destroyed.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.engine, this.destroyEngine, this.secretKey, this.destroyed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractConfigCryptoProvider other = (AbstractConfigCryptoProvider) obj;
        return Objects.equals(this.engine, other.engine)
                && Objects.equals(this.destroyEngine, other.destroyEngine)
                && Objects.equals(this.secretKey, other.secretKey)
                && Objects.equals(this.destroyed, other.destroyed);
    }
}
