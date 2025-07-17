package dev.orne.config;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * {@code Properties} based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @param <S> The concrete type of the builder.
 * @since 1.0
 * @see Properties
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface PropertiesConfigBaseBuilder<S extends PropertiesConfigBaseBuilder<S>>
extends ConfigBuilder<S> {

    /**
     * Adds the specified configuration properties to the configuration
     * properties.
     * 
     * @param values The configuration properties.
     * @return This instance, for method chaining.
     */
    @NotNull S add(
            @NotNull Properties values);

    /**
     * Adds the specified configuration properties to the configuration
     * properties.
     * 
     * @param values The configuration properties.
     * @return This instance, for method chaining.
     */
    @NotNull S add(
            @NotNull Map<String, String> values);

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
