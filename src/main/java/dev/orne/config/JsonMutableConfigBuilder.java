package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * JSON files based mutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @param <S> The concrete type of the builder.
 * @since 1.0
 * @see MutableConfig
 * @see WatchableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface JsonMutableConfigBuilder<S extends JsonMutableConfigBuilder<S>>
extends JsonConfigBaseBuilder<S>, MutableConfigBuilder<S> {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull WatchableConfig build();
}
