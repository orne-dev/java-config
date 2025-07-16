package dev.orne.config.impl;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2020 Orne Developments
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
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apiguardian.api.API;

/**
 * Implementation of {@code Config} based on Apache Commons
 * {@code ImmutableConfiguration}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2020-09
 * @since 0.2
 * @see ImmutableConfiguration
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class CommonsConfigImpl
extends AbstractConfig {

    /** The delegated Apache Commons configuration. */
    private final @NotNull ImmutableConfiguration config;

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param commonsOptions The Apache Commons based configuration options.
     */
    public CommonsConfigImpl(
            final @NotNull ConfigOptions options,
            final @NotNull CommonsConfigOptions commonsOptions) {
        super(options);
        Objects.requireNonNull(commonsOptions);
        this.config = Objects.requireNonNull(commonsOptions.getDelegated());
    }

    /**
     * Returns the delegated Apache Commons configuration.
     * 
     * @return The delegated Apache Commons configuration
     */
    protected @NotNull ImmutableConfiguration getConfig() {
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
        final Iterable<String> iterable = this.config::getKeys;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getInt(
            final @NotBlank String key) {
        return this.config.getString(key);
    }
}
