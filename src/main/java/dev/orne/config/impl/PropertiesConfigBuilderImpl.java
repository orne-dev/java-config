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

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.PropertiesConfigBuilder;

/**
 * Implementation of {@code Properties} based immutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see PropertiesConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class PropertiesConfigBuilderImpl
extends AbstractConfigBuilderImpl<PropertiesConfigBuilder>
implements PropertiesConfigBuilder {

    /** The properties based configuration options. */
    protected final @NotNull PropertiesConfigOptions propertyOptions;

    /**
     * Empty constructor.
     */
    public PropertiesConfigBuilderImpl() {
        super();
        this.propertyOptions = new PropertiesConfigOptions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesConfigBuilder add(
            final @NotNull Properties values) {
        this.propertyOptions.add(values);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesConfigBuilder add(
            final @NotNull Map<String, String> values) {
        this.propertyOptions.add(values);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesConfigBuilder load(
            final @NotNull String path) {
        this.propertyOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesConfigBuilder load(
            final @NotNull Path path) {
        this.propertyOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesConfigBuilder load(
            final @NotNull File file) {
        this.propertyOptions.load(file);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesConfigBuilder load(
            final @NotNull URL url) {
        this.propertyOptions.load(url);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesMutableConfigBuilderImpl mutable() {
        return new PropertiesMutableConfigBuilderImpl(
                this.options,
                new MutableConfigOptions(),
                propertyOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesConfigImpl build() {
        return new PropertiesConfigImpl(this.options, this.propertyOptions);
    }
}
