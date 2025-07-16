package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * Options of Jackson {@code ObjectNode} based configuration builder
 * for YAML files.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see YamlConfig
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class YamlConfigOptions
extends JsonConfigOptions {

    /**
     * Empty constructor.
     */
    public YamlConfigOptions() {
        super();
        setMapper(new ObjectMapper(new YAMLFactory()));
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public YamlConfigOptions(
            final @NotNull YamlConfigOptions copy) {
        super(copy);
    }
}
