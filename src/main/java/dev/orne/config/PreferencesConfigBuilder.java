package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.crypto.ConfigCryptoProvider;

/**
 * {@code Preferences} based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see Preferences
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface PreferencesConfigBuilder
extends MutableCapableConfigBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PreferencesConfigBuilder withParent(
            Config parent);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PreferencesConfigBuilder withEncryption(
            ConfigCryptoProvider provider);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PreferencesConfigBuilder withDecoder(
            ValueDecoder encoder);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PreferencesConfigBuilder withVariableResolution();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PreferencesConfigBuilder withDecorator(
            ValueDecorator decorator);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PreferencesMutableConfigBuilder mutable();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PreferencesConfig build();
}
