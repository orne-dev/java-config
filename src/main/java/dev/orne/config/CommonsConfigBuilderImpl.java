package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apiguardian.api.API;

/**
 * Implementation of {@code ImmutableConfiguration} based immutable
 * configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see ConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class CommonsConfigBuilderImpl
extends AbstractConfigBuilderImpl<CommonsConfigBuilderImpl>
implements CommonsConfigDelegateBuilder, CommonsConfigBuilder {

    /** The delegated Apache Commons configuration. */
    protected @NotNull ImmutableConfiguration delegated;

    /**
     * Creates a new instance.
     */
    public CommonsConfigBuilderImpl() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param options The configuration options to copy.
     */
    public CommonsConfigBuilderImpl(
            final @NotNull ConfigOptions options) {
        super(options);
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public CommonsConfigBuilderImpl(
            final @NotNull AbstractConfigBuilderImpl<?> copy) {
        super(copy);
        if (copy instanceof CommonsConfigBuilderImpl) {
            this.delegated = ((CommonsConfigBuilderImpl) copy).delegated;
        }
    }

    /**
     * Returns the delegated Apache Commons configuration.
     * 
     * @return The delegated Apache Commons configuration.
     */
    public @NotNull ImmutableConfiguration getDelegated() {
        return this.delegated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CommonsConfigBuilder ofDelegate(
            final @NotNull ImmutableConfiguration delegate) {
        this.delegated = delegate;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CommonsMutableConfigBuilder mutable() {
        return new CommonsMutableConfigBuilderImpl(
                this,
                (Configuration) this.delegated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CommonsConfig build() {
        return new CommonsConfig(
                this.options,
                this.delegated);
    }
}
