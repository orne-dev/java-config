package dev.orne.config;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.Configuration;
import org.apiguardian.api.API;

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
     * Empty constructor.
     */
    public CommonsMutableConfigBuilderImpl() {
        super();
        this.commonsOptions = new CommonsConfigOptions();
    }

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
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     * @param delegated The delegated Apache Commons configuration.
     */
    public CommonsMutableConfigBuilderImpl(
            final @NotNull AbstractConfigBuilderImpl<?> copy,
            final @NotNull Configuration delegated) {
        super(copy);
        if (copy instanceof CommonsConfigBuilderImpl) {
            this.commonsOptions = new CommonsConfigOptions(
                    ((CommonsConfigBuilderImpl) copy).commonsOptions);
        } else {
            this.commonsOptions = new CommonsConfigOptions();
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
    public @NotNull CommonsMutableConfig build() {
        return new CommonsMutableConfig(
                this.options,
                this.mutableOptions,
                this.commonsOptions);
    }
}
