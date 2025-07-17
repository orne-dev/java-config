package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.Configuration;
import org.apiguardian.api.API;

/**
 * Apache Commons {@code Configuration} based mutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @param <T> The concrete type of the builder.
 * @since 1.0
 * @see Configuration
 * @see MutableConfig
 * @see WatchableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface CommonsMutableConfigBuilder<T extends CommonsMutableConfigBuilder<T>>
extends MutableConfigBuilder<T> {

    /**
     * Selects the delegated Apache Commons configuration.
     * 
     * @param delegate The delegated Apache Commons configuration.
     * @return Next builder, for method chaining.
     */
    @NotNull T ofDelegate(
            final @NotNull Configuration delegate);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull WatchableConfig build();
}
