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
import java.io.Writer;

import org.apiguardian.api.API;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.orne.config.Config;
import dev.orne.config.FileWatchableConfig;

/**
 * Jackson {@code ObjectNode} based mutable configuration.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see Config
 * @see ObjectNode
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class JsonMutableConfigImpl
extends JsonConfigImpl
implements FileWatchableConfig {

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param mutableOptions The mutable configuration builder options.
     * @param jsonOptions The JSON based configuration builder options.
     */
    public JsonMutableConfigImpl(
            final ConfigOptions options,
            final MutableConfigOptions mutableOptions,
            final JsonConfigOptions jsonOptions) {
        super(options, mutableOptions, jsonOptions);
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
        final JsonPointer pointer = propertyToPointer(key);
        JacksonUtils.setNodeValue(getJsonObject(), pointer, value);
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
            final JsonPointer pointer = propertyToPointer(key);
            JacksonUtils.removeNode(getJsonObject(), pointer);
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
            final Writer destination)
    throws IOException {
        new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValue(destination, getJsonObject());
    }
}
