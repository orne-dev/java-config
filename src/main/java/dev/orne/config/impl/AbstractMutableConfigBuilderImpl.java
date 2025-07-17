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

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.MutableConfigBuilder;
import dev.orne.config.ValueEncoder;

/**
 * Base abstract implementation of mutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @param <S> The concrete type of the builder.
 * @since 1.0
 * @see MutableConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public abstract class AbstractMutableConfigBuilderImpl<S extends AbstractMutableConfigBuilderImpl<S>>
extends AbstractConfigBuilderImpl<S>
implements MutableConfigBuilder<S> {

    /** The configuration options. */
    protected final @NotNull MutableConfigOptions mutableOptions;

    /**
     * Copy constructor.
     * 
     * @param options The configuration options to copy.
     * @param mutableOptions The mutable configuration options to copy.
     */
    protected AbstractMutableConfigBuilderImpl(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions) {
        super(options);
        this.mutableOptions = new MutableConfigOptions(mutableOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull S withEncoder(
            final ValueEncoder encoder) {
        this.mutableOptions.setEncoder(encoder);
        return thisBuilder();
    }
}
