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

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.Validate;

/**
 * Implementation of {@code Config} based on the system properties.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 2.0, 2020-04
 * @since 0.1
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
            final String key)
    throws ConfigException {
        try {
            return getSystemProperty(key) != null;
        } catch (final SecurityException se) {
            throw new ConfigException("Error accessing configuration property", se);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getRawValue(
            @NotBlank
            final String key)
    throws ConfigException {
        try {
            return getSystemProperty(key);
        } catch (final SecurityException se) {
            throw new ConfigException("Error retrieving configuration property value", se);
        }
    }

    /**
     * Returns system property value.
     * 
     * @param key The key of the system property
     * @return The value of the system property, or {@code null} if not set
     * @throws SecurityException If a security manager exists and its
     * {@code checkPropertyAccess} method doesn't allow access to the specified
     * system property.
     */
    protected String getSystemProperty(
            final String key) {
        Validate.notBlank(key, "Parameter key mus be a non blank string");
        return System.getProperty(key);
    }
}
