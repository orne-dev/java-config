package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Jackson {@code ObjectNode} based mutable configuration builder
 * for YAML files.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @param <S> The concrete type of the builder.
 * @since 1.0
 * @see ObjectNode
 * @see MutableConfig
 * @see WatchableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface YamlMutableConfigBuilder<S extends YamlMutableConfigBuilder<S>>
extends YamlConfigBaseBuilder<S>, MutableConfigBuilder<S> {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull WatchableConfig build();
}
