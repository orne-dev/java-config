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

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

import org.apiguardian.api.API;

import dev.orne.config.PropertiesMutableConfigBuilder;

/**
 * Implementation of {@code Properties} based mutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see PropertiesMutableConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class PropertiesMutableConfigBuilderImpl
extends AbstractMutableConfigBuilderImpl<PropertiesMutableConfigBuilder>
implements PropertiesMutableConfigBuilder {

    /** The properties based configuration options. */
    protected final PropertiesConfigOptions propertyOptions;

    /**
     * Copy constructor.
     * 
     * @param options The configuration options to copy.
     * @param mutableOptions The mutable configuration options to copy.
     * @param propertyOptions The properties based configuration options to
     * copy.
     */
    protected PropertiesMutableConfigBuilderImpl(
            final ConfigOptions options,
            final MutableConfigOptions mutableOptions,
            final PropertiesConfigOptions propertyOptions) {
        super(options, mutableOptions);
        this.propertyOptions = new PropertiesConfigOptions(propertyOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesMutableConfigBuilder add(
            final Properties values) {
        this.propertyOptions.add(values);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesMutableConfigBuilder add(
            final Map<String, String> values) {
        this.propertyOptions.add(values);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesMutableConfigBuilder load(
            final String path) {
        this.propertyOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesMutableConfigBuilder load(
            final Path path) {
        this.propertyOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesMutableConfigBuilder load(
            final File file) {
        this.propertyOptions.load(file);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesMutableConfigBuilder load(
            final URL url) {
        this.propertyOptions.load(url);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertiesMutableConfigImpl build() {
        return new PropertiesMutableConfigImpl(
                this.options,
                this.mutableOptions,
                this.propertyOptions);
    }
}
