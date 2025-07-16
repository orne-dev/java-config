package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Mutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see MutableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface MutableConfigBuilder
extends ConfigBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull MutableConfigBuilder withParent(
            Config parent);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull MutableConfigBuilder withEncryption(
            ConfigCryptoProvider provider);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull MutableConfigBuilder withDecoder(
            ValueDecoder encoder);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull MutableConfigBuilder withVariableResolution();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull MutableConfigBuilder withDecorator(
            ValueDecorator decorator);

    /**
     * Sets the configuration properties values encoder.
     * Applied when setting property values.
     * 
     * @param encoder The configuration properties values encoder.
     * @return This instance, for method chaining.
     */
    @NotNull MutableConfigBuilder withEncoder(
            ValueEncoder encoder);

    /**
     * Creates the configuration instance.
     * 
     * @return The configuration instance.
     */
    @Override
    @NotNull MutableConfig build();
}
