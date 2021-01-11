package dev.orne.config.commons;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2020 Orne Developments
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

import org.apache.commons.configuration2.Configuration;

import dev.orne.config.ConfigException;
import dev.orne.config.MutableConfig;

/**
 * Implementation of {@code MutableConfig} based on Apache Commons
 * {@code Configuration}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-09
 * @since 0.2
 * @see Configuration
 */
public class CommonsMutableConfig
extends CommonsConfig
implements MutableConfig {

    /**
     * Creates a new instance.
     * 
     * @param config The delegated Apache Commons configuration
     */
    public CommonsMutableConfig(
            final @NotNull Configuration config) {
        super(config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull Configuration getConfig() {
        return (Configuration) super.getConfig();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(
            final @NotBlank String key,
            final Object value)
    throws ConfigException {
        getConfig().setProperty(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(
            final @NotBlank String key)
    throws ConfigException {
        getConfig().clearProperty(key);
    }
}
