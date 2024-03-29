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

import java.util.Iterator;
import java.util.Properties;

import javax.validation.constraints.NotBlank;

import org.apache.commons.collections.IteratorUtils;
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
    public boolean isEmpty()
    throws ConfigException {
        try {
            return getSystemProperties().isEmpty();
        } catch (final SecurityException se) {
            throw new ConfigException("Error accessing configuration", se);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Iterator<String> getKeys()
    throws ConfigException {
        try {
            return IteratorUtils.asIterator(getSystemProperties().keys());
        } catch (final SecurityException se) {
            throw new ConfigException("Error accessing configuration", se);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean containsParameter(
            final @NotBlank String key)
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
            final @NotBlank String key)
    throws ConfigException {
        try {
            return getSystemProperty(key);
        } catch (final SecurityException se) {
            throw new ConfigException("Error retrieving configuration property value", se);
        }
    }

    /**
     * Returns system properties.
     * 
     * @return The system properties
     * @throws SecurityException If a security manager exists and its
     * {@code checkPropertyAccess} method doesn't allow access to the
     * system properties.
     * @see System#getProperties()
     */
    protected Properties getSystemProperties() {
        return System.getProperties();
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
            final @NotBlank String key) {
        return System.getProperty(Validate.notBlank(
                key,
                "Parameter key mus be a non blank string"));
    }
}
