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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.impl.ConfigSubset;

/**
 * Configuration properties provider with properties mutable at runtime.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2019-07
 * @version 2.0, 2025-04
 * @since 0.1
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface MutableConfig
extends Config {

    /**
     * Sets the value of the specified configuration property.
     * 
     * @param key The configuration property.
     * @param value The value to set
     * @throws ConfigException If an error occurs setting the configuration
     * property value
     */
    void set(
            @NotBlank String key,
            String value);

    /**
     * Sets the value of the configuration parameter.
     * 
     * @param key The configuration property.
     * @param value The value to set
     * @throws ConfigException If an error occurs setting the configuration
     * property value
     */
    default void set(
            @NotBlank String key,
            Boolean value) {
        set(key, value == null ? null : String.valueOf(value));
    }

    /**
     * Sets the value of the configuration parameter.
     * 
     * @param key The configuration property.
     * @param value The value to set
     * @throws ConfigException If an error occurs setting the configuration
     * property value
     */
    default void set(
            @NotBlank String key,
            Integer value) {
        set(key, value == null ? null : String.valueOf(value));
    }

    /**
     * Sets the value of the configuration parameter.
     * 
     * @param key The configuration property.
     * @param value The value to set
     * @throws ConfigException If an error occurs setting the configuration
     * property value
     */
    default void set(
            @NotBlank String key,
            Long value) {
        set(key, value == null ? null : String.valueOf(value));
    }

    /**
     * Removes the specified configuration properties.
     * 
     * @param keys The configuration properties.
     * @throws ConfigException If an error occurs removing the configuration
     * properties.
     */
    void remove(
            @NotBlank String... keys);

    /**
     * Creates a subset configuration containing only the properties
     * with the specified prefix.
     * 
     * @param prefix The prefix for configuration keys.
     * @return The subset configuration.
     */
    default @NotNull MutableConfig subset(
            final @NotNull String prefix) {
        return ConfigSubset.create(this, prefix);
    }
}
