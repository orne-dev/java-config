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

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.Config;
import dev.orne.config.ConfigCryptoProvider;
import dev.orne.config.ConfigProvider;
import dev.orne.config.ConfigProviderBuilder;

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
public class ConfigProviderBuilderImpl
implements ConfigProviderBuilder {

    private final ConfigProviderImpl provider;

    /**
     * Creates a new instance.
     * 
     * @param defaultConfig The default configuration instance.
     */
    public ConfigProviderBuilderImpl(
            final @NotNull Config defaultConfig) {
        super();
        this.provider = new ConfigProviderImpl(defaultConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ConfigProviderBuilder addConfig(
            final @NotNull Config config) {
        this.provider.registerConfig(config);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ConfigProvider build() {
        return this.provider;
    }
}
