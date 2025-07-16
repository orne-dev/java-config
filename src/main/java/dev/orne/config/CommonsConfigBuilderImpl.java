package dev.orne.config;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apiguardian.api.API;

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
implements CommonsConfigBuilder {

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
     * Copy constructor.
     * 
     * @param options The configuration options to copy.
     * @param commonsOptions TThe Apache Commons based configuration options to
     * copy.
     */
    public CommonsConfigBuilderImpl(
            final @NotNull ConfigOptions options,
            final @NotNull CommonsConfigOptions commonsOptions) {
        super(options);
        this.commonsOptions = new CommonsConfigOptions(commonsOptions);
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
    public @NotNull CommonsConfigBuilderImpl ofDelegate(
            final @NotNull ImmutableConfiguration delegate) {
        this.commonsOptions.setDelegated(Objects.requireNonNull(delegate));
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CommonsMutableConfigBuilder mutable() {
        return new CommonsMutableConfigBuilderImpl(
                this.options,
                new MutableConfigOptions(),
                this.commonsOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CommonsConfig build() {
        return new CommonsConfig(
                this.options,
                this.commonsOptions);
    }
}
