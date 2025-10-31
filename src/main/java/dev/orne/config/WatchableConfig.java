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

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.impl.ConfigSubset;

/**
 * Watchable mutable configuration properties provider.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-04
 * @since 1.0
 * @see MutableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface WatchableConfig
extends MutableConfig {

    /**
     * Registers the specified configuration change events listener.
     * 
     * @param listener The listener to be called on configuration changes.
     * @throws IllegalStateException If the configuration type does not support
     * event listeners.
     * 
     */
    void addListener(
            @NotNull Listener listener);

    /**
     * Unregisters the specified configuration change events listener.
     * 
     * @param listener The listener to previously registered.
     * 
     */
    void removeListener(
            @NotNull Listener listener);

    /**
     * Creates a subset configuration containing only the properties
     * with the specified prefix.
     * 
     * @param prefix The prefix for configuration keys.
     * @return The subset configuration.
     */
    @Override
    default @NotNull WatchableConfig subset(
            final @NotNull String prefix) {
        return ConfigSubset.create(this, prefix);
    }

    /**
     * Mutable configuration changes listener.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-05
     * @since 1.0
     */
    @FunctionalInterface
    @API(status = API.Status.STABLE, since = "1.0")
    interface Listener {

        /**
         * Callback for configuration change events.
         * 
         * @param config The modified configuration instance.
         * @param keys The modified configuration properties.
         */
        void configurationChanged(
                @NotNull MutableConfig config,
                @NotNull Set<String> keys);
    }
}
