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

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.Validate;

/**
 * Implementation of {@code Config} based on the system properties.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0, 2019-07
 * @see Config
 * @see System#getProperties()
 */
public class SystemConfig
extends AbstractStringConfig {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean containsParameter(
            @NotBlank
            final String key) {
        Validate.notNull(key, "Parameter key is required");
        return System.getProperty(key) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getStringParameter(
            @NotBlank
            final String key) {
        Validate.notNull(key, "Parameter key is required");
        return System.getProperty(key);
    }
}
