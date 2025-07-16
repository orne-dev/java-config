package dev.orne.config.impl;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.Configuration;
import org.apiguardian.api.API;

import dev.orne.config.CommonsMutableConfigBuilder;
import dev.orne.config.MutableConfigBuilder;

/**
 * Implementation of Apache Commons {@code Configuration} based mutable
 * configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see MutableConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class CommonsMutableConfigBuilderImpl
extends AbstractMutableConfigBuilderImpl<CommonsMutableConfigBuilderImpl>
implements CommonsMutableConfigBuilder {

    /** The Apache Commons based configuration options. */
    protected final @NotNull CommonsConfigOptions commonsOptions;

    /**
     * Copy constructor.
     * 
     * @param options The configuration options to copy.
     * @param mutableOptions The mutable configuration options to copy.
     * @param commonsOptions TThe Apache Commons based configuration options to
     * copy.
     */
    public CommonsMutableConfigBuilderImpl(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions,
            final @NotNull CommonsConfigOptions commonsOptions) {
        super(options, mutableOptions);
        this.commonsOptions = new CommonsConfigOptions(Objects.requireNonNull(commonsOptions));
        if (this.commonsOptions.getDelegated() != null
                && !(this.commonsOptions.getDelegated() instanceof Configuration)) {
            throw new IllegalArgumentException(
                    "Delegated configuration must be an instance of "
                            + "org.apache.commons.configuration2.Configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CommonsMutableConfigBuilderImpl ofDelegate(
            final @NotNull Configuration delegate) {
        this.commonsOptions.setDelegated(Objects.requireNonNull(delegate));
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CommonsMutableConfigImpl build() {
        return new CommonsMutableConfigImpl(
                this.options,
                this.mutableOptions,
                this.commonsOptions);
    }
}
