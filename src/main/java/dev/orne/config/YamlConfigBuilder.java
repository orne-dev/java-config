package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Jackson {@code ObjectNode} based configuration builder
 * for YAML files.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @param <S> The concrete type of the builder.
 * @since 1.0
 * @see ObjectNode
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface YamlConfigBuilder<S extends YamlConfigBuilder<S>>
extends YamlConfigBaseBuilder<S>, MutableCapableConfigBuilder<S> {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlMutableConfigBuilder<?> mutable();
}
