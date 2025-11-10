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
import dev.orne.config.CryptoProviderEngineBuilder;
import dev.orne.config.CryptoProviderKeyBuilder;

/**
 * Implementation of configuration values cryptography transformations provider
 * builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see ConfigCryptoProvider
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class CryptoProviderBuilderImpl
implements CryptoProviderEngineBuilder, CryptoProviderKeyBuilder, CryptoProviderBuilder{

    /** The cryptography transformations provider options. */
    private final CryptoProviderOptions options;

    /**
     * Empty constructor.
     */
    public CryptoProviderBuilderImpl() {
        super();
        this.options = new CryptoProviderOptions();
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public CryptoProviderBuilderImpl(
            final @NotNull CryptoProviderBuilderImpl copy) {
        super();
        this.options = new CryptoProviderOptions(copy.options);
    }

    /**
     * Copy constructor.
     * 
     * @param options The cryptography transformations provider options to copy.
     */
    public CryptoProviderBuilderImpl(
            final @NotNull CryptoProviderOptions options) {
        super();
        this.options = new CryptoProviderOptions(options);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CryptoProviderBuilderImpl withEngine(
            final @NotNull ConfigCryptoEngine engine,
            final boolean destroyEngine) {
        this.options.setEngine(engine);
        this.options.setDestroyEngine(destroyEngine);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CryptoProviderBuilderImpl withAesGcmEngine(
            final @NotNull byte[] salt,
            final boolean destroyEngine) {
        this.options.setEngine(new ConfigCryptoAesGcmEngine(salt));
        this.options.setDestroyEngine(destroyEngine);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CryptoProviderBuilderImpl withSecretKey(
            final @NotNull char[] password) {
        this.options.setKey(
                this.options.engine.createSecretKey(password));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CryptoProviderBuilderImpl withSecretKey(
            final @NotNull SecretKey key) {
        this.options.setKey(key);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CryptoProviderBuilderImpl pooled() {
        this.options.setPooled(true);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ConfigCryptoProvider build() {
        final ConfigCryptoProvider instance;
        if (this.options.isPooled()) {
            instance = new PooledConfigCryptoProvider(this.options);
        } else {
            instance = new DefaultConfigCryptoProvider(this.options);
        }
        return instance;
    }
}
