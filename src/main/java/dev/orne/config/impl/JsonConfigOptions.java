package dev.orne.config.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Objects;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.orne.config.JsonConfigBaseBuilder;

/**
 * Options of Jackson {@code ObjectNode} based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see JsonConfigImpl
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class JsonConfigOptions {

    /** The class logger. */
    private static final Logger LOG =
            LoggerFactory.getLogger(JsonConfigOptions.class);

    /** Error message for not found resources. */
    private static final String RESOURCE_NOT_FOUND_ERR =
            "Configuration resource not found: {}";
    /** JSON file read error message. */
    private static final String READ_ERR =
            "Error reading configuration resource: {}";
    /** JSON merge error message. */
    private static final String MERGE_ERR =
            "Error mergin configuration JSON objects";

    /** The {@code ObjectMapper} instance used for JSON parsing. */
    private @NotNull ObjectMapper mapper = new ObjectMapper();
    /** The JSON object with the configuration properties. */
    private final @NotNull ObjectNode jsonObject;
    /** The configuration nested properties separator. */
    private @NotEmpty String propertySeparator;

    /**
     * Empty constructor.
     */
    public JsonConfigOptions() {
        super();
        this.mapper = new ObjectMapper();
        this.mapper.setDefaultMergeable(true);
        this.jsonObject = JacksonUtils.NODE_FACTORY.objectNode();
        this.propertySeparator = JsonConfigBaseBuilder.DEFAULT_SEPARATOR;
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public JsonConfigOptions(
            final @NotNull JsonConfigOptions copy) {
        super();
        this.mapper = copy.mapper;
        this.jsonObject = copy.jsonObject.deepCopy();
        this.propertySeparator = copy.propertySeparator;
    }

    /**
     * Returns the {@code ObjectMapper} instance used for JSON parsing.
     * 
     * @return The {@code ObjectMapper} instance used for JSON parsing.
     */
    public @NotNull ObjectMapper getMapper() {
        return this.mapper;
    }

    /**
     * Sets the {@code ObjectMapper} instance used for JSON parsing.
     * 
     * @param mapper The {@code ObjectMapper} instance used for JSON parsing.
     */
    public void setMapper(
            final @NotNull ObjectMapper mapper) {
        this.mapper = Objects.requireNonNull(mapper);
    }

    /**
     * Returns the JSON object with the configuration properties.
     * 
     * @return The JSON object with the configuration properties.
     */
    public @NotNull ObjectNode getJsonObject() {
        return this.jsonObject;
    }

    /**
     * Returns the configuration nested properties separator.
     * 
     * @return The configuration nested properties separator.
     */
    public @NotEmpty String getPropertySeparator() {
        return this.propertySeparator;
    }

    /**
     * Sets the configuration nested properties separator.
     * 
     * @param separator The configuration nested properties separator.
     */
    public void setPropertySeparator(
            final @NotEmpty String separator) {
        Validate.notBlank(separator, "Property separator cannot be blank");
        this.propertySeparator = separator;
    }

    /**
     * Adds the specified configuration properties to the configuration
     * properties.
     * 
     * @param values The configuration properties.
     */
    public void add(
            final @NotNull ObjectNode values) {
        try {
            this.mapper.updateValue(this.jsonObject, values);
        } catch (final IOException e) {
            LOG.warn(MERGE_ERR, e);
        }
    }

    /**
     * Loads the configuration properties from the specified ClassLoader
     * resource.
     * 
     * @param path The ClassLoader resource path.
     * @return This instance, for method chaining.
     */
    public void load(
            final @NotNull String path) {
        try {
            final Enumeration<URL> resources =
                    Thread.currentThread()
                            .getContextClassLoader()
                            .getResources(path);
            if (!resources.hasMoreElements()) {
                LOG.warn(RESOURCE_NOT_FOUND_ERR, path);
            }
            while (resources.hasMoreElements()) {
                load(resources.nextElement());
            }
        } catch (final IOException e) {
            LOG.warn(READ_ERR, path, e);
        }
    }

    /**
     * Loads the configuration properties from the file in the specified
     * path.
     * 
     * @param path The file path.
     * @return This instance, for method chaining.
     */
    public void load(
            final @NotNull Path path) {
        if (!Files.exists(path)) {
            LOG.warn(RESOURCE_NOT_FOUND_ERR, path);
        }
        try (final InputStream fileIS = Files.newInputStream(path)) {
            final ObjectReader updater = this.mapper.readerForUpdating(this.jsonObject);
            updater.readTree(fileIS);
        } catch (final IOException e) {
            LOG.warn(READ_ERR, path, e);
        }
    }

    /**
     * Loads the configuration properties from the specified file.
     * 
     * @param file The file to load.
     * @return This instance, for method chaining.
     */
    public void load(
            final @NotNull File file) {
        try (final InputStream fileIS = new FileInputStream(file)) {
            final ObjectReader updater = this.mapper.readerForUpdating(this.jsonObject);
            updater.readTree(fileIS);
        } catch (final FileNotFoundException e) {
            LOG.warn(RESOURCE_NOT_FOUND_ERR, file, e);
        } catch (final IOException e) {
            LOG.warn(READ_ERR, file, e);
        }
    }

    /**
     * Loads the configuration properties from the specified URL.
     * 
     * @param url The URL to load.
     * @return This instance, for method chaining.
     */
    public void load(
            final @NotNull URL url) {
        try (final InputStream urlIS = url.openStream()) {
            final ObjectReader updater = this.mapper.readerForUpdating(this.jsonObject);
            updater.readTree(urlIS);
        } catch (final IOException e) {
            LOG.warn(READ_ERR, url, e);
        }
    }
}
