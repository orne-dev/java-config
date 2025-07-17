package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Configuration builder that can be made mutable.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @param <S> The concrete type of the builder.
 * @since 1.0
 * @see MutableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface MutableCapableConfigBuilder<S extends MutableCapableConfigBuilder<S>>
extends ConfigBuilder<S> {

    /**
     * Makes the configuration instance mutable.
     * 
     * @return This instance, for method chaining.
     */
    @NotNull MutableConfigBuilder<?> mutable();
}
