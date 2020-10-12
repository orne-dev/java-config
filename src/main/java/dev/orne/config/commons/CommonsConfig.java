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

import java.util.Iterator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.commons.lang3.Validate;

import dev.orne.config.Config;
import dev.orne.config.ConfigException;

/**
 * Implementation of {@code Config} based on Apache Commons
 * {@code ImmutableConfiguration}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-09
 * @since 0.2
 * @see ImmutableConfiguration
 */
public class CommonsConfig
implements Config {

    /** The delegated Apache Commons configuration. */
    private final @NotNull ImmutableConfiguration config;

    /**
     * Creates a new instance.
     * 
     * @param config The delegated Apache Commons configuration
     */
    public CommonsConfig(
            final @NotNull ImmutableConfiguration config) {
        super();
        this.config = Validate.notNull(config);
    }

    /**
     * Returns the delegated Apache Commons configuration.
     * 
     * @return The delegated Apache Commons configuration
     */
    protected @NotNull ImmutableConfiguration getConfig() {
        return this.config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty()
    throws ConfigException {
        return this.config.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<String> getKeys()
    throws ConfigException {
        return this.config.getKeys();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(
            final @NotBlank String key)
    throws ConfigException {
        return this.config.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T get(
            final @NotBlank String key,
            final @NotNull Class<T> type)
    throws ConfigException {
        return this.config.get(type, key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBoolean(
            final @NotBlank String key)
    throws ConfigException {
        return this.config.getBoolean(key, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(
            final @NotBlank String key)
    throws ConfigException {
        return this.config.getString(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Number getNumber(
            final @NotBlank String key)
    throws ConfigException {
        return this.config.getBigDecimal(key);
    }
}
