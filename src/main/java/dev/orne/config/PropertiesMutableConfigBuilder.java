package dev.orne.config;

import java.util.Properties;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * {@code Properties} based mutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @param <S> The concrete type of the builder.
 * @since 1.0
 * @see Properties
 * @see MutableConfig
 * @see WatchableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface PropertiesMutableConfigBuilder<S extends PropertiesMutableConfigBuilder<S>>
extends PropertiesConfigBaseBuilder<S>, MutableConfigBuilder<S> {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull WatchableConfig build();
}
