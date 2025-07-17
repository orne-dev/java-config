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

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apiguardian.api.API;

/**
 * Options of Apache Commons Configuration based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see CommonsConfigImpl
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class CommonsConfigOptions {

    /** The delegated Apache Commons configuration. */
    protected ImmutableConfiguration delegated;

    /**
     * Empty constructor.
     */
    public CommonsConfigOptions() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public CommonsConfigOptions(
            final @NotNull CommonsConfigOptions copy) {
        super();
        this.delegated = copy.delegated;
    }

    /**
     * Returns the delegated Apache Commons configuration.
     * 
     * @return The delegated Apache Commons configuration.
     */
    public ImmutableConfiguration getDelegated() {
        return this.delegated;
    }

    /**
     * Sets the delegated Apache Commons configuration.
     * 
     * @param delegated The delegated Apache Commons configuration.
     */
    public void setDelegated(
            final ImmutableConfiguration delegated) {
        this.delegated = delegated;
    }
}
