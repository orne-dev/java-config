package dev.orne.config.impl;

import java.util.Properties;

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

import java.util.stream.Stream;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.Config;
import dev.orne.config.ConfigException;

/**
 * Implementation of {@code Config} based on the system properties.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2019-07
 * @version 2.0, 2025-04
 * @since 0.1
 * @see Config
 * @see System#getProperties()
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class SystemConfigImpl
extends AbstractConfig {

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     */
    public SystemConfigImpl(
            final @NotNull ConfigOptions options) {
        super(options);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEmptyInt() {
        return getSystemProperties().isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean containsInt(
            final @NotBlank String key) {
        return getSystemProperties().containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull Stream<String> getKeysInt() {
        return getSystemProperties().stringPropertyNames().stream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getInt(
            final @NotBlank String key) {
        return getSystemProperties().getProperty(key);
    }

    /**
     * Returns system properties.
     * 
     * @return The system properties
     * @throws ConfigException If a security manager exists and its
     * {@code checkPropertyAccess} method doesn't allow access to the
     * system properties.
     * @see System#getProperties()
     */
    protected Properties getSystemProperties() {
        try {
            return System.getProperties();
        } catch (final SecurityException e) {
            throw new ConfigException("Cannot access system properties", e);
        }
    }
}
