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

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;

/**
 * <p>Partial implementation of {@code Config} for implementations with
 * configuration values stored as {@code String}.</p>
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 2.0, 2020-04
 * @since 0.1
 * @see Config
 */
public abstract class AbstractStringConfig
extends AbstractConfig {

    /** String representation of {@code null} values. */
    public static final String NULL = "\0";

    /** If {@link #NULL} values should be converted to {@code null}. */
    private boolean nullPlaceholderEnabled;

    /**
     * Returns {@code true} if {@link #NULL} values should be converted to
     * {@code null}.
     * 
     * @return If {@link #NULL} values should be converted to {@code null}
     */
    public boolean isNullPlaceholderEnabled() {
        return this.nullPlaceholderEnabled;
    }

    /**
     * Sets if {@link #NULL} values should be converted to {@code null}.
     * 
     * @param enabled If {@link #NULL} values should be converted to
     * {@code null}
     */
    public void setNullPlaceholderEnabled(final boolean enabled) {
        this.nullPlaceholderEnabled = enabled;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getStringParameter(
            final @NotBlank String key)
    throws ConfigException {
        String result = getRawValue(key);
        if (this.nullPlaceholderEnabled && NULL.equals(result)) {
            result = null;
        }
        return result;
    }

    /**
     * Returns the raw stored value of the configuration parameter configured
     * in this instance as {@code String}.
     * 
     * @param key The key of the configuration parameter
     * @return The configuration parameter raw value as {@code String}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    protected abstract String getRawValue(
            @NotBlank String key)
    throws ConfigException;

    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean getBooleanParameter(
            final @NotBlank String key)
    throws ConfigException {
        return getParameter(key, Boolean.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Number getNumberParameter(
            final @NotBlank String key)
    throws ConfigException {
        return getParameter(key, BigDecimal.class);
    }
}
