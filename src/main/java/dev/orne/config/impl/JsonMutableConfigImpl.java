package dev.orne.config.impl;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2025 Orne Developments
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
import java.util.WeakHashMap;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.orne.config.Config;

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
extends AbstractWatchableConfig {

    /** The JSON object with the configuration properties. */
    private final @NotNull ObjectNode jsonObject;
    /** The configuration nested properties separator. */
    private final @NotEmpty String propertySeparator;
    /** The property key to JSON pointers cache. */
    private final WeakHashMap<String, JsonPointer> cache = new WeakHashMap<>();

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param mutableOptions The mutable configuration builder options.
     * @param jsonOptions The JSON based configuration builder options.
     */
    public JsonMutableConfigImpl(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions,
            final @NotNull JsonConfigOptions jsonOptions) {
        super(options, mutableOptions);
        Objects.requireNonNull(jsonOptions);
        this.jsonObject = jsonOptions.getJsonObject();
        this.propertySeparator = jsonOptions.getPropertySeparator();
    }

    /**
     * Returns the JSON object with the configuration properties.
     * 
     * @return The JSON object with the configuration properties.
     */
    protected @NotNull ObjectNode getJsonObject() {
        return this.jsonObject;
    }

    /**
     * Returns the configuration nested properties separator.
     * 
     * @return The configuration nested properties separator.
     */
    protected @NotEmpty String getPropertySeparator() {
        return this.propertySeparator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEmptyInt() {
        return this.jsonObject.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull Stream<String> getKeysInt() {
        return this.jsonObject.propertyStream()
                .flatMap(entry ->
                    JsonConfigImpl.keysFlattener(
                        this.propertySeparator,
                        "",
                        entry.getKey(),
                        entry.getValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean containsInt(
            final @NotBlank String key) {
        final JsonNode node = this.jsonObject.at(propertyToPointer(key));
        return !node.isMissingNode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getInt(
            final @NotBlank String key) {
        final JsonNode node = this.jsonObject.at(propertyToPointer(key));
        if (node.isValueNode()) {
            return node.asText();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setInt(
            final @NotBlank String key,
            final @NotNull String value) {
        final JsonPointer pointer = propertyToPointer(key);
        JacksonUtils.setNodeValue(this.jsonObject, pointer, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeInt(
            final @NotBlank String... keys) {
        for (final String key : keys) {
            final JsonPointer pointer = propertyToPointer(key);
            JacksonUtils.removeNode(this.jsonObject, pointer);
        }
    }

    /**
     * Resolves configuration keys to JSON pointer expressions by replacing
     * properties separator with the JSON pointer segment separator.
     * 
     * @param key The configuration key.
     * @return The JSON pointer expression.
     */
    protected @NotBlank JsonPointer propertyToPointer(
            final @NotBlank String key) {
        Objects.requireNonNull(key);
        return cache.computeIfAbsent(key, k ->
                JacksonUtils.propertyToPointer(key, this.propertySeparator));
    }
}
