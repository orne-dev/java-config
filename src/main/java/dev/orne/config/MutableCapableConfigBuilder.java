package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Configuration builder that can be made mutable.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see MutableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface MutableCapableConfigBuilder
extends ConfigBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull MutableCapableConfigBuilder withParent(
            Config parent);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull MutableCapableConfigBuilder withEncryption(
            ConfigCryptoProvider provider);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull MutableCapableConfigBuilder withDecoder(
            ValueDecoder encoder);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull MutableCapableConfigBuilder withVariableResolution();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull MutableCapableConfigBuilder withDecorator(
            ValueDecorator decorator);

    /**
     * Makes the configuration instance mutable.
     * 
     * @return This instance, for method chaining.
     */
    @NotNull MutableConfigBuilder mutable();
}
