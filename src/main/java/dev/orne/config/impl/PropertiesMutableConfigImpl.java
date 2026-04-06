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

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import java.util.Properties;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

import dev.orne.config.FileWatchableConfig;
import dev.orne.config.MutableConfig;

/**
 * {@code Properties} based mutable configuration.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-04
 * @since 1.0
 * @see MutableConfig
 * @see Properties
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class PropertiesMutableConfigImpl
extends PropertiesConfigImpl
implements FileWatchableConfig {

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param mutableOptions The mutable configuration builder options.
     * @param propertyOptions The properties based configuration builder options.
     */
    public PropertiesMutableConfigImpl(
            final ConfigOptions options,
            final MutableConfigOptions mutableOptions,
            final PropertiesConfigOptions propertyOptions) {
        super(options, mutableOptions, propertyOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(
            final String key,
            final @Nullable String value) {
        super.set(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setInt(
            final String key,
            final String value) {
        getProperties().setProperty(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(
            final String... keys) {
        super.remove(keys);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeInt(
            final String... keys) {
        for (final String key : keys) {
            getProperties().remove(key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(
            final Listener listener) {
        super.addListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(
            final Listener listener) {
        super.removeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(
            final OutputStream destination)
    throws IOException {
        getProperties().store(destination, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(
            final Writer destination)
    throws IOException {
        getProperties().store(destination, null);
    }
}
