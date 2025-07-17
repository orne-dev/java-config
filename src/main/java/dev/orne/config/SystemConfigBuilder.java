package dev.orne.config;

import org.apiguardian.api.API;

/**
 * {@code System} properties based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @param <S> The concrete type of the builder.
 * @since 1.0
 * @see System
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface SystemConfigBuilder<S extends SystemConfigBuilder<S>>
extends ConfigBuilder<S> {
    // No extra methods
}
