package dev.orne.config.commons;

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

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;

import dev.orne.config.ConfigException;
import dev.orne.config.MutableConfig;

/**
* Implementation of Apache Commons {@code Configuration} based on
* {@code MutableConfig}.
* 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
* @version 1.0, 2020-20
* @since 0.2
* @see Configuration
* @see MutableConfig
*/
public class DelegatedOrneMutableConfiguration
extends DelegatedOrneConfiguration {

    /**
     * Creates a new instance.
     * 
     * @param config The delegated Orne configuration
     */
    public DelegatedOrneMutableConfiguration(
            final @NotNull MutableConfig config) {
        super(config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull MutableConfig getConfig() {
        return (MutableConfig) super.getConfig();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPropertyDirect(
            final String key,
            final Object value) {
        try {
            getConfig().set(key, String.valueOf(value));
        } catch (final ConfigException ce) {
            throw new ConfigurationRuntimeException(ce);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void clearPropertyDirect(
            final String key) {
        try {
            getConfig().remove(key);
        } catch (final ConfigException ce) {
            throw new ConfigurationRuntimeException(ce);
        }
    }
}
