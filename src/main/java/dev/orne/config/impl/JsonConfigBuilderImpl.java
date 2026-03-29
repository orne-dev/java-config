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

import org.apiguardian.api.API;

import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.orne.config.JsonConfigBuilder;

/**
 * Implementation of Jackson {@code ObjectNode} based immutable configuration
 * builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see JsonConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class JsonConfigBuilderImpl
extends AbstractConfigBuilderImpl<JsonConfigBuilder>
implements JsonConfigBuilder {

    /** The JSON based configuration options. */
    protected final JsonConfigOptions jsonOptions;

    /**
     * Empty constructor.
     */
    public JsonConfigBuilderImpl() {
        super();
        this.jsonOptions = new JsonConfigOptions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonConfigBuilder withSeparator(
            final String separator) {
        this.jsonOptions.setPropertySeparator(separator);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonConfigBuilder add(
            final Map<String, String> values) {
        if (!values.isEmpty()) {
            final ObjectNode data = JacksonUtils.NODE_FACTORY.objectNode();
            values.forEach((key, value) -> JacksonUtils.setNodeValue(
                    data,
                    this.jsonOptions.getPropertySeparator(),
                    key,
                    value));
            this.jsonOptions.add(data);
        }
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonConfigBuilder load(
            final String path) {
        this.jsonOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonConfigBuilder load(
            final Path path) {
        this.jsonOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonConfigBuilder load(
            final File file) {
        this.jsonOptions.load(file);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonConfigBuilder load(
            final URL url) {
        this.jsonOptions.load(url);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonMutableConfigBuilderImpl mutable() {
        return new JsonMutableConfigBuilderImpl(
                this.options,
                new MutableConfigOptions(),
                jsonOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonConfigImpl build() {
        return new JsonConfigImpl(this.options, this.jsonOptions);
    }
}
