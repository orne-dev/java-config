package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * {@code Preferences} based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @param <T> The concrete type of the builder.
 * @since 1.0
 * @see Preferences
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface PreferencesConfigBuilder<T extends PreferencesConfigBuilder<T>>
extends MutableCapableConfigBuilder<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PreferencesMutableConfigBuilder<?> mutable();
}
