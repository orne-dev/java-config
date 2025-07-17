package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * {@code Preferences} based mutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @param <S> The concrete type of the builder.
 * @since 1.0
 * @see Preferences
 * @see MutableConfig
 * @see WatchableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface PreferencesMutableConfigBuilder<S extends PreferencesMutableConfigBuilder<S>>
extends MutableConfigBuilder<S> {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull WatchableConfig build();
}
