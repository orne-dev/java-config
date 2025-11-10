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

import javax.crypto.SecretKey;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.ConfigCryptoEngine;
import dev.orne.config.ConfigCryptoProvider;
import dev.orne.config.CryptoProviderBuilder;

/**
 * Configuration values cryptography transformations provider builder options.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see CryptoProviderBuilder
 * @see ConfigCryptoProvider
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class CryptoProviderOptions {

    /** The cryptography engine to use. */
    protected ConfigCryptoEngine engine;
    /** If the cryptography engine must be destroyed on provider destruction. */
    protected boolean destroyEngine;
    /** The secret key to use. */
    protected SecretKey key;
    /** If multiple pooled {@code Cipher} instances should be used. */
    protected boolean pooled;

    /**
     * Creates a new instance.
     */
    public CryptoProviderOptions() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public CryptoProviderOptions(
            final @NotNull CryptoProviderOptions copy) {
        super();
        this.engine = copy.engine;
        this.destroyEngine = copy.destroyEngine;
        this.key = copy.key;
        this.pooled = copy.pooled;
    }

    /**
     * Returns the cryptography engine to use.
     * 
     * @return The cryptography engine to use.
     */
    public ConfigCryptoEngine getEngine() {
        return this.engine;
    }

    /**
     * Sets the cryptography engine to use.
     * 
     * @param engine The cryptography engine to use.
     */
    public void setEngine(ConfigCryptoEngine engine) {
        this.engine = engine;
    }

    /**
     * Returns {@code true} if the cryptography engine must be destroyed on
     * provider destruction.
     * 
     * @return If the cryptography engine must be destroyed on provider
     * destruction.
     */
    public boolean isDestroyEngine() {
        return destroyEngine;
    }

    /**
     * Sets if the cryptography engine must be destroyed on provider
     * destruction.
     * 
     * @param destroyEngine If the cryptography engine must be destroyed on
     * provider destruction.
     */
    public void setDestroyEngine(boolean destroyEngine) {
        this.destroyEngine = destroyEngine;
    }

    /**
     * Returns the secret key to use.
     * 
     * @return The secret key to use.
     */
    public SecretKey getKey() {
        return key;
    }

    /**
     * Sets the secret key to use.
     * 
     * @param key The secret key to use.
     */
    public void setKey(SecretKey key) {
        this.key = key;
    }

    /**
     * Returns {@code true} if multiple pooled {@code Cipher} instances should
     * be used.
     * 
     * @return If multiple pooled {@code Cipher} instances should be used.
     */
    public boolean isPooled() {
        return pooled;
    }

    /**
     * Sets if multiple pooled {@code Cipher} instances should be used.
     * 
     * @param pooled If multiple pooled {@code Cipher} instances should be
     * used.
     */
    public void setPooled(boolean pooled) {
        this.pooled = pooled;
    }
}
