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
import java.time.Instant;

import javax.annotation.Nullable;
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

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    protected Boolean getBooleanParameter(
            @NotBlank
            final String key)
    throws ConfigException {
        final String strValue = getStringParameter(key);
        return strValue == null ? null : Boolean.valueOf(strValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    protected Number getNumberParameter(
            @NotBlank
            final String key)
    throws ConfigException {
        final String strValue = getStringParameter(key);
        return strValue == null ? null : new BigDecimal(strValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    protected Instant getInstantParameter(
            @NotBlank
            final String key)
    throws ConfigException {
        final String strValue = getStringParameter(key);
        return strValue == null ? null : Instant.parse(strValue);
    }
}
