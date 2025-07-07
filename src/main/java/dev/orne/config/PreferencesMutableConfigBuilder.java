package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.crypto.ConfigCryptoProvider;

/**
 * {@code Preferences} based mutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see Preferences
 * @see MutableConfig
 * @see WatchableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface PreferencesMutableConfigBuilder
extends MutableConfigBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PreferencesMutableConfigBuilder withParent(
            Config parent);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PreferencesMutableConfigBuilder withEncryption(
            ConfigCryptoProvider provider);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PreferencesMutableConfigBuilder withDecoder(
            ValueDecoder encoder);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PreferencesMutableConfigBuilder withVariableResolution();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PreferencesMutableConfigBuilder withDecorator(
            ValueDecorator decorator);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PreferencesMutableConfig build();
}
