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
import java.util.WeakHashMap;

import javax.validation.constraints.NotNull;

import org.apache.commons.text.StringSubstitutor;
import org.apiguardian.api.API;

import dev.orne.config.Config;
import dev.orne.config.ValueDecorator;

/**
 * {@code StringSubstitutor} based configuration property values variable
 * resolver.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-04
 * @since 1.0
 * @see StringSubstitutor
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class VariableResolver
implements ValueDecorator {

    /** The configuration properties variables substitutor. */
    private final @NotNull StringSubstitutor substitutor;
    /** The resolved configuration properties cache. */
    private final WeakHashMap<String, String> cache = new WeakHashMap<>();

    /**
     * Creates a new instance base on the specified configuration.
     * 
     * @param config The configuration instance.
     */
    public VariableResolver(
            final @NotNull Config config) {
        this(new StringSubstitutor(config::get));
    }

    /**
     * Creates a new instance using the specified string substitutor.
     * 
     * @param substitutor The string substitutor to use.
     */
    public VariableResolver(
            final @NotNull StringSubstitutor substitutor) {
        this.substitutor = Objects.requireNonNull(substitutor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String decorate(
            final String value) {
        if (value == null) {
            return null;
        } else {
            return this.cache.computeIfAbsent(value, substitutor::replace);
        }
    }

    /**
     * Clears the resolved values cache.
     */
    public void clearCache() {
        this.cache.clear();
    }
}
