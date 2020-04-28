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
import javax.validation.constraints.NotBlank;

/**
 * Interface for classes containing configuration values mutable at runtime.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0, 2019-07
 * @see Config
 */
public interface MutableConfig
extends Config {

    /**
     * Sets the value of the configuration parameter.
     * 
     * @param key The key of the configuration parameter
     * @param value The value to set
     */
    void set(
            @NotBlank
            String key,
            @Nullable
            Object value);

    /**
     * Removes the value of the configuration parameter.
     * 
     * @param key The key of the configuration parameter
     */
    void remove(
            @NotBlank
            String key);
}
