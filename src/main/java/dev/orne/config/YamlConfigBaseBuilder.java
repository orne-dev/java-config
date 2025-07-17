package dev.orne.config;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public interface YamlConfigBaseBuilder<S extends YamlConfigBaseBuilder<S>>
extends ConfigBuilder<S> {

    /**
     * Sets the {@code ObjectMapper} instance used for YAML parsing.
     * <p>
     * Nota that this method must be called before any {@code load()} method
     * to ensure the correct parsing of the configuration files.
     * 
     * @param mapper The {@code ObjectMapper} instance used for YAML parsing.
     * @return This instance, for method chaining.
     */
    @NotNull S withMapper(
            @NotNull ObjectMapper mapper);

    /**
     * Sets the configuration nested properties separator.
     * 
     * @param separator The configuration nested properties separator.
     * @return This instance, for method chaining.
     */
    @NotNull S setPropertySeparator(
            @NotEmpty String separator);

    /**
     * Adds the specified configuration properties to the configuration
     * properties.
     * 
     * @param values The configuration properties.
     * @return This instance, for method chaining.
     */
    @NotNull S add(
            @NotNull ObjectNode values);

    /**
     * Loads the configuration properties from the specified ClassLoader
     * resource.
     * 
     * @param path The ClassLoader resource path.
     * @return This instance, for method chaining.
     */
    @NotNull S load(
            @NotNull String path);

    /**
     * Loads the configuration properties from the file in the specified
     * path.
     * 
     * @param path The file path.
     * @return This instance, for method chaining.
     */
    @NotNull S load(
            @NotNull Path path);

    /**
     * Loads the configuration properties from the specified file.
     * 
     * @param file The file to load.
     * @return This instance, for method chaining.
     */
    @NotNull S load(
            @NotNull File file);

    /**
     * Loads the configuration properties from the specified URL.
     * 
     * @param url The URL to load.
     * @return This instance, for method chaining.
     */
    @NotNull S load(
            @NotNull URL url);
}
