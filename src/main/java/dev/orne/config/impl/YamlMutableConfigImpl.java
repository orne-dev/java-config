package dev.orne.config.impl;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.orne.config.Config;

/**
 * Jackson {@code ObjectNode} based mutable configuration
 * for YAML files.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see Config
 * @see ObjectNode
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class YamlMutableConfigImpl
extends JsonMutableConfigImpl {

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param mutableOptions The mutable configuration builder options.
     * @param yamlOptions The YAML based configuration builder options.
     */
    public YamlMutableConfigImpl(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions,
            final @NotNull YamlConfigOptions yamlOptions) {
        super(options, mutableOptions, yamlOptions);
    }
}
