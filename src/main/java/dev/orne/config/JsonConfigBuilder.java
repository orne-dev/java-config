package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Jackson {@code ObjectNode} based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @param <S> The concrete type of the builder.
 * @since 1.0
 * @see ObjectNode
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface JsonConfigBuilder<S extends JsonConfigBuilder<S>>
extends JsonConfigBaseBuilder<S>, MutableCapableConfigBuilder<S> {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonMutableConfigBuilder<?> mutable();
}
