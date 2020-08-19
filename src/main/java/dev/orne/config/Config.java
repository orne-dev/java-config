package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
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

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Interface for classes containing configuration values.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 2.0, 2020-04
 * @since 0.1
 */
public interface Config {

    /**
     * Returns {@code true} if the parameter with the key passed as argument
     * has been configured.
     * 
     * @param key The key of the configuration parameter
     * @return Returns {@code true} if the parameter has been configured
     * @throws ConfigException If an error occurs accessing the configuration
     * property
     */
    boolean contains(
            @NotBlank
            String key)
    throws ConfigException;

    /**
     * Returns the value of the configuration parameter.
     * 
     * @param <T> The target type of the parameter
     * @param key The key of the configuration parameter
     * @param type The target type of the parameter
     * @return The configuration parameter value converted to the target type
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    @Nullable
    <T> T get(
            @NotBlank
            String key,
            @NotNull
            Class<T> type)
    throws ConfigException;

    /**
     * Returns the value of the configuration parameter as {@code Boolean}.
     * 
     * @param key The key of the configuration parameter
     * @return The configuration parameter value as {@code Boolean}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    @Nullable
    Boolean getBoolean(
            @NotBlank
            String key)
    throws ConfigException;

    /**
     * Returns the value of the configuration parameter as {@code String}.
     * 
     * @param key The key of the configuration parameter
     * @return The configuration parameter value as {@code String}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    @Nullable
    String getString(
            @NotBlank
            String key)
    throws ConfigException;

    /**
     * Returns the value of the configuration parameter as {@code Number}.
     * 
     * @param key The key of the configuration parameter
     * @return The configuration parameter value as {@code Number}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    @Nullable
    Number getNumber(
            @NotBlank
            String key)
    throws ConfigException;
}
