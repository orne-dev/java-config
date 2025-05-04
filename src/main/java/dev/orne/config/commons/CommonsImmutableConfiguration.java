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
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;

import dev.orne.config.Config;
import dev.orne.config.ConfigException;

/**
* Implementation of Apache Commons {@code ImmutableConfiguration} based on
* {@code Config}.
* 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
* @version 1.0, 2020-20
* @since 0.2
* @see ImmutableConfiguration
* @see Config
*/
public class CommonsImmutableConfiguration
extends AbstractConfiguration
implements ImmutableConfiguration {

    /** Message for attempts of modifying R/O configuration. */
    private static final String WRITE_OP_EX_MSG =
            "The underlaying configuration is inmutable.";

    /** The delegated Orne configuration. */
    private final @NotNull Config config;

    /**
     * Creates a new instance.
     * 
     * @param config The delegated Orne configuration
     */
    public CommonsImmutableConfiguration(
            final @NotNull Config config) {
        super();
        this.config = Objects.requireNonNull(config);
    }

    /**
     * Returns the delegated Orne configuration.
     * 
     * @return The delegated Orne configuration
     */
    protected @NotNull Config getConfig() {
        return this.config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEmptyInternal() {
        try {
            return this.config.isEmpty();
        } catch (final ConfigException ce) {
            throw new ConfigurationRuntimeException(ce);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean containsKeyInternal(
            final String key) {
        try {
            return this.config.contains(key);
        } catch (final ConfigException ce) {
            throw new ConfigurationRuntimeException(ce);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean containsValueInternal(
            final Object value) {
        return this.config.getKeys()
                .anyMatch(key -> Objects.equals(value, getProperty(key)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Iterator<String> getKeysInternal() {
        try {
            return this.config.getKeys().iterator();
        } catch (final ConfigException ce) {
            throw new ConfigurationRuntimeException(ce);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getPropertyInternal(
            final String key) {
        try {
            return this.config.get(key);
        } catch (final ConfigException ce) {
            throw new ConfigurationRuntimeException(ce);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPropertyDirect(
            final String key,
            final Object value) {
        throw new UnsupportedOperationException(WRITE_OP_EX_MSG);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void clearPropertyDirect(
            final String key) {
        throw new UnsupportedOperationException(WRITE_OP_EX_MSG);
    }
}
