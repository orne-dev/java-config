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
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;

/**
 * <p>Partial implementation of {@code MutableConfig} for implementations with
 * configuration values stored as {@code String}.</p>
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 2.0, 2020-04
 * @since 0.1
 * @see Config
 */
public abstract class AbstractMutableStringConfig
extends AbstractStringConfig
implements MutableConfig {

    /** String representation of {@code null} values. */
    public static final String NULL = "\0";

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    protected String getStringParameter(
            @NotBlank
            final String key)
    throws ConfigException {
        String result = getRawValue(key);
        if (NULL.equals(result)) {
            result = null;
        }
        return result;
    }

    /**
     * Returns the raw stored value of the configuration parameter configured in this
     * instance as {@code String}.
     * 
     * @param key The key of the configuration parameter
     * @return The configuration parameter raw value as {@code String}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    @Nullable
    protected abstract String getRawValue(
            @NotBlank
            String key)
    throws ConfigException;

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(
            @NotBlank
            final String key,
            @Nullable
            final Object value)
    throws ConfigException {
        Validate.notNull(key, "Parameter key is required");
        if (value == null) {
            setRawValue(key, NULL);
        } else if (value instanceof Float) {
            setRawValue(key, BigDecimal.valueOf((Float) value).toString());
        } else if (value instanceof Double) {
            setRawValue(key, BigDecimal.valueOf((Double) value).toString());
        } else if (value instanceof Date) {
            setRawValue(key, ((Date) value).toInstant().toString());
        } else if (value instanceof Calendar) {
            setRawValue(key, ((Calendar) value).toInstant().toString());
        } else {
            setRawValue(key, value.toString());
        }
    }

    /**
     * Sets the raw stored value of the configuration parameter configured in this
     * instance as {@code String}.
     * 
     * @param key The key of the configuration parameter
     * @param value The configuration parameter raw value as {@code String}
     * @throws ConfigException If an error occurs setting the configuration
     * property value
     */
    protected abstract void setRawValue(
            @NotBlank
            String key,
            @NotNull
            String value)
    throws ConfigException;
}
