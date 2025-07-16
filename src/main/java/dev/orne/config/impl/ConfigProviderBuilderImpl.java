package dev.orne.config.impl;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.Config;
import dev.orne.config.ConfigCryptoProvider;
import dev.orne.config.ConfigProvider;
import dev.orne.config.ConfigProviderBuilder;

/**
 * Implementation of configuration values cryptography transformations provider
 * builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see ConfigCryptoProvider
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class ConfigProviderBuilderImpl
implements ConfigProviderBuilder {

    private final ConfigProviderImpl provider;

    /**
     * Creates a new instance.
     * 
     * @param defaultConfig The default configuration instance.
     */
    public ConfigProviderBuilderImpl(
            final @NotNull Config defaultConfig) {
        super();
        this.provider = new ConfigProviderImpl(defaultConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ConfigProviderBuilder addConfig(
            final @NotNull Config config) {
        this.provider.registerConfig(config);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ConfigProvider build() {
        return this.provider;
    }
}
