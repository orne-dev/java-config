package dev.orne.config;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Jackson {@code ObjectNode} based configuration
 * for YAML files.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see Config
 * @see ObjectNode
 */
@API(status = API.Status.STABLE, since = "1.0")
public class YamlConfig
extends JsonConfig {

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param yamlOptions The YAML based configuration builder options.
     */
    @API(status = API.Status.INTERNAL, since = "1.0")
    public YamlConfig(
            final @NotNull ConfigOptions options,
            final @NotNull JsonConfigOptions yamlOptions) {
        super(options, yamlOptions);
    }

    /**
     * Creates a new instance.
     * 
     * @param parent The parent {@code Config} instance.
     * @param decoder The configuration properties values decoder.
     * @param decorator The configuration properties values decorator.
     * @param jsonObject The JSON object with the configuration properties.
     * @param propertySeparator The configuration key to JSON pointer resolver.
     */
    public YamlConfig(
            final Config parent,
            final @NotNull ValueDecoder decoder,
            final @NotNull ValueDecorator decorator,
            final @NotNull ObjectNode jsonObject,
            final @NotEmpty String propertySeparator) {
        super(parent, decoder, decorator, jsonObject, propertySeparator);
    }
}
