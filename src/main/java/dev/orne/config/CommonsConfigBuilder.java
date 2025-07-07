package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apiguardian.api.API;

import dev.orne.config.crypto.ConfigCryptoProvider;

/**
 * Apache Commons {@code ImmutableConfiguration} based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see ImmutableConfiguration
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface CommonsConfigBuilder
extends MutableCapableConfigBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull CommonsConfigBuilder withParent(
            Config parent);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull CommonsConfigBuilder withEncryption(
            ConfigCryptoProvider provider);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull CommonsConfigBuilder withDecoder(
            ValueDecoder encoder);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull CommonsConfigBuilder withVariableResolution();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull CommonsConfigBuilder withDecorator(
            ValueDecorator decorator);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull CommonsMutableConfigBuilder mutable();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull CommonsConfig build();
}
