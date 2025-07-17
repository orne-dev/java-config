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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.orne.config.YamlConfigBuilder;

/**
 * Implementation of YAML files based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see YamlConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class YamlConfigBuilderImpl
extends AbstractConfigBuilderImpl<YamlConfigBuilderImpl>
implements YamlConfigBuilder<YamlConfigBuilderImpl> {

    /** The YAML based configuration options. */
    protected final @NotNull YamlConfigOptions yamlOptions;

    /**
     * Empty constructor.
     */
    public YamlConfigBuilderImpl() {
        super();
        this.yamlOptions = new YamlConfigOptions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlConfigBuilderImpl withSeparator(
            final @NotEmpty String separator) {
        this.yamlOptions.setPropertySeparator(separator);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlConfigBuilderImpl add(
            final @NotNull Map<String, String> values) {
        if (!values.isEmpty()) {
            final ObjectNode data = JacksonUtils.NODE_FACTORY.objectNode();
            values.forEach((key, value) -> JacksonUtils.setNodeValue(
                    data,
                    this.yamlOptions.getPropertySeparator(),
                    key,
                    value));
            this.yamlOptions.add(data);
        }
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlConfigBuilderImpl load(
            final @NotNull String path) {
        this.yamlOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlConfigBuilderImpl load(
            final @NotNull Path path) {
        this.yamlOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlConfigBuilderImpl load(
            final @NotNull File file) {
        this.yamlOptions.load(file);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlConfigBuilderImpl load(
            final @NotNull URL url) {
        this.yamlOptions.load(url);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlMutableConfigBuilderImpl mutable() {
        return new YamlMutableConfigBuilderImpl(
                this.options,
                new MutableConfigOptions(),
                yamlOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlConfigImpl build() {
        return new YamlConfigImpl(this.options, this.yamlOptions);
    }
}
