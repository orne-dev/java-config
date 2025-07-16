package dev.orne.config.impl;

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

import java.util.Objects;
import java.util.Properties;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.Config;

/**
 * {@code Properties} based configuration.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2019-07
 * @version 2.0, 2025-04
 * @since 0.1
 * @see Config
 * @see Properties
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class PropertiesConfigImpl
extends AbstractConfig {

    /** The configuration properties. */
    private final @NotNull Properties config;

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param propertyOptions The properties based configuration builder options.
     */
    public PropertiesConfigImpl(
            final @NotNull ConfigOptions options,
            final @NotNull PropertiesConfigOptions propertyOptions) {
        super(options);
        Objects.requireNonNull(propertyOptions);
        this.config = Objects.requireNonNull(propertyOptions.getProperties());
    }

    /**
     * Returns the internal {@code Properties} instance.
     * 
     * @return The configuration properties.
     */
    protected @NotNull Properties getProperties() {
        return this.config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEmptyInt() {
        return this.config.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean containsInt(
            final @NotBlank String key) {
        return this.config.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull Stream<String> getKeysInt() {
        return this.config.stringPropertyNames().stream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getInt(
            final @NotBlank String key) {
        return this.config.getProperty(key);
    }
}
