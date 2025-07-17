package dev.orne.config.impl;

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
