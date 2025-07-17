package dev.orne.config.impl;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apiguardian.api.API;

import dev.orne.config.CommonsConfigBuilder;
import dev.orne.config.ConfigBuilder;

/**
 * Implementation of Apache Commons {@code ImmutableConfiguration} based
 * immutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see ConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class CommonsConfigBuilderImpl
extends AbstractConfigBuilderImpl<CommonsConfigBuilderImpl>
implements CommonsConfigBuilder<CommonsConfigBuilderImpl> {

    /** The Apache Commons based configuration options. */
    protected final @NotNull CommonsConfigOptions commonsOptions;

    /**
     * Empty constructor.
     */
    public CommonsConfigBuilderImpl() {
        super();
        this.commonsOptions = new CommonsConfigOptions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CommonsConfigBuilderImpl ofDelegate(
            final @NotNull ImmutableConfiguration delegate) {
        this.commonsOptions.setDelegated(Objects.requireNonNull(delegate));
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CommonsMutableConfigBuilderImpl mutable() {
        return new CommonsMutableConfigBuilderImpl(
                this.options,
                new MutableConfigOptions(),
                this.commonsOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CommonsConfigImpl build() {
        return new CommonsConfigImpl(
                this.options,
                this.commonsOptions);
    }
}
