package dev.orne.config;

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

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.impl.ConfigProviderBuilderImpl;

/**
 * Generic interface for {@code Config} providers.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2019-07
 * @version 1.1, 2025-07
 * @since 0.1
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface ConfigProvider {

    /**
     * Creates a new {@code ConfigProviderBuilder} instance using the
     * specified default configuration.
     * 
     * @param defaultConfig The default configuration instance.
     * @return A new {@code ConfigProviderBuilder} instance
     */
    static @NotNull ConfigProviderBuilder builder(
            final @NotNull Config defaultConfig) {
        return new ConfigProviderBuilderImpl(defaultConfig);
    }

    /**
     * Returns the default {@code Config} instance.
     * 
     * @return The default {@code Config} instance
     */
    @NotNull Config getDefaultConfig();

    /**
     * Returns the registered {@code Config} for the specified type, if any.
     * 
     * @param type The configuration type.
     * @return The registered {@code Config}.
     */
    @NotNull Optional<Config> getConfig(
            @NotNull Class<? extends Config> type);

    /**
     * Returns a suitable {@code Config} instance for the configuration
     * preferences passed as argument.
     * 
     * @param preferences The configuration preferences.
     * @return The selected {@code Config} instance, or {@code null} if no one
     * is suitable
     */
    Config selectConfig(
            PreferredConfig preferences);
}
