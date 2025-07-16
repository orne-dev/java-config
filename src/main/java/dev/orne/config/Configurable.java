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

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Interface for classes suitable for configuration.
 * Allows to be configured by an instance of {@code Config} at runtime.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2019-07
 * @since 0.1
 * @see Config
 * @see ConfigurationOptions
 * @see ConfigurableProperty
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface Configurable {

    /**
     * Sets the config to apply to this property.
     * 
     * @param config The config to apply
     */
    void configure(
            @NotNull Config config);

    /**
     * Returns {@code true} if this instance is already configured.
     * 
     * @return {@code true} if this instance is already configured
     */
    boolean isConfigured();
}
