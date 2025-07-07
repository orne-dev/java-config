package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.crypto.ConfigCryptoProvider;

/**
 * Base abstract implementation of configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see ConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public abstract class AbstractConfigBuilderImpl<T extends AbstractConfigBuilderImpl<T>>
implements ConfigBuilder {

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
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    protected AbstractConfigBuilderImpl(
            final @NotNull AbstractConfigBuilderImpl<?> copy) {
        this(copy.options);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull T withParent(
            final Config parent) {
        this.options.setParent(parent);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull T withEncryption(
            final ConfigCryptoProvider provider) {
        this.options.setCryptoProvider(provider);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull T withDecoder(
            final ValueDecoder decoder) {
        this.options.setDecoder(decoder);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull T withVariableResolution() {
        this.options.setVariableResolutionEnabled(true);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull T withDecorator(
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
    protected @NotNull T thisBuilder() {
        return (T) this;
    }
}
