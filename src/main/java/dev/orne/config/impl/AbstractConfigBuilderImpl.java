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

import dev.orne.config.Config;
import dev.orne.config.ConfigBuilder;
import dev.orne.config.ConfigCryptoProvider;
import dev.orne.config.ValueDecoder;
import dev.orne.config.ValueDecorator;

/**
 * Base abstract implementation of configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @param <S> The concrete type of the builder.
 * @since 1.0
 * @see ConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public abstract class AbstractConfigBuilderImpl<S extends AbstractConfigBuilderImpl<S>>
implements ConfigBuilder<S> {

    /** The configuration options. */
    protected final @NotNull ConfigOptions options;

    /**
     * Empty constructor.
     */
    protected AbstractConfigBuilderImpl() {
        super();
        this.options = new ConfigOptions();
    }

    /**
     * Copy constructor.
     * 
     * @param options The configuration options to copy.
     */
    protected AbstractConfigBuilderImpl(
            final @NotNull ConfigOptions options) {
        super();
        this.options = new ConfigOptions(options);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull S withParent(
            final Config parent) {
        this.options.setParent(parent);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull S withOverrideParentProperties() {
        this.options.setOverrideParentProperties(true);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull S withEncryption(
            final ConfigCryptoProvider provider) {
        this.options.setCryptoProvider(provider);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull S withDecoder(
            final ValueDecoder decoder) {
        this.options.setDecoder(decoder);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull S withVariableResolution() {
        this.options.setVariableResolutionEnabled(true);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull S withDecorator(
            final ValueDecorator decorator) {
        this.options.setDecorator(decorator);
        return thisBuilder();
    }

    /**
     * Returns this builder instance.
     * 
     * @return This builder instance.
     */
    @SuppressWarnings("unchecked")
    protected @NotNull S thisBuilder() {
        return (S) this;
    }
}
