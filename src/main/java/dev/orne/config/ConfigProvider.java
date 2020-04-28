package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

/**
 * Generic interface for {@code Config} providers.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0, 2019-07
 */
public interface ConfigProvider {

    /**
     * Returns the default {@code Config} instance.
     * 
     * @return The default {@code Config} instance
     */
    @NotNull
    Config getDefaultConfig();

    /**
     * Returns a suitable {@code Config} instance for the configuration
     * options passed as argument. The target class is passed as second
     * argument for implementations that support extra annotations for
     * configuration options.
     * 
     * @param options The configuration options of the target class.
     * @param targetClass The target class for extra annotation retrieval, if
     * supported.
     * @return The selected {@code Config} instance, or {@code null} if no one
     * is suitable
     */
    @Nullable
    Config selectConfig(
            @Nullable
            final ConfigurationOptions options,
            @NotNull
            final Class<?> targetClass);
}
