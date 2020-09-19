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

import javax.validation.constraints.NotBlank;

/**
 * Interface for classes containing configuration values mutable at runtime.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 2.0, 2020-04
 * @since 0.1
 * @see Config
 */
public interface MutableConfig
extends Config {

    /**
     * Sets the value of the configuration parameter.
     * 
     * @param key The key of the configuration parameter
     * @param value The value to set
     * @throws ConfigException If an error occurs setting the configuration
     * property value
     */
    void set(
            @NotBlank String key,
            Object value)
    throws ConfigException;

    /**
     * Removes the value of the configuration parameter.
     * 
     * @param key The key of the configuration parameter
     * @throws ConfigException If an error occurs removing the configuration
     * property
     */
    void remove(
            @NotBlank String key)
    throws ConfigException;
}
