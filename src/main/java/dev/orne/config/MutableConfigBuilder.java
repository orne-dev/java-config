package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Mutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @param <S> The concrete type of the builder.
 * @since 1.0
 * @see MutableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface MutableConfigBuilder<S extends MutableConfigBuilder<S>>
extends ConfigBuilder<S> {

    /**
     * Sets the configuration properties values encoder.
     * Applied when setting property values.
     * 
     * @param encoder The configuration properties values encoder.
     * @return This instance, for method chaining.
     */
    @NotNull S withEncoder(
            ValueEncoder encoder);

    /**
     * Creates the configuration instance.
     * 
     * @return The configuration instance.
     */
    @Override
    @NotNull MutableConfig build();
}
