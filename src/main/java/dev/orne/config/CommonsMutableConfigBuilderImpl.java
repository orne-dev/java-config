package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.Configuration;
import org.apiguardian.api.API;

/**
 * Implementation of {@code Configuration} based mutable configuration builder.
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

    /** The delegated Apache Commons configuration. */
    protected final @NotNull Configuration delegated;

    /**
     * Creates a new instance.
     * 
     * @param delegated The delegated Apache Commons configuration.
     */
    public CommonsMutableConfigBuilderImpl(
            final @NotNull Configuration delegated) {
        super();
        this.delegated = delegated;
    }

    /**
     * Copy constructor.
     * 
     * @param options The configuration options to copy.
     * @param mutableOptions The mutable configuration options to copy.
     * @param delegated The delegated Apache Commons configuration.
     */
    public CommonsMutableConfigBuilderImpl(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions,
            final @NotNull Configuration delegated) {
        super(options, mutableOptions);
        this.delegated = delegated;
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     * @param delegated The delegated Apache Commons configuration.
     */
    public CommonsMutableConfigBuilderImpl(
            final @NotNull AbstractConfigBuilderImpl<?> copy,
            final @NotNull Configuration delegated) {
        super(copy);
        this.delegated = delegated;
    }

    /**
     * Returns the delegated Apache Commons configuration.
     * 
     * @return The delegated Apache Commons configuration.
     */
    public @NotNull Configuration getDelegated() {
        return this.delegated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CommonsMutableConfig build() {
        return new CommonsMutableConfig(
                this.options,
                this.mutableOptions,
                this.delegated);
    }
}
