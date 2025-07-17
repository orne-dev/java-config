package dev.orne.config.impl;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2025 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Options of {@code Properties} based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see PropertiesConfigImpl
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class PropertiesConfigOptions {

    /** The class logger. */
    private static final Logger LOG =
            LoggerFactory.getLogger(PropertiesConfigOptions.class);

    /** Error message for not found resources. */
    private static final String RESOURCE_NOT_FOUND_ERR =
            "Configuration resource not found: {}";
    /** Properties file read error message. */
    private static final String READ_ERR =
            "Error reading configuration resource: {}";

    /** The configuration properties. */
    private final @NotNull Properties properties;

    /**
     * Empty constructor.
     */
    public PropertiesConfigOptions() {
        super();
        this.properties = new Properties();
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public PropertiesConfigOptions(
            final @NotNull PropertiesConfigOptions copy) {
        super();
        this.properties = new Properties();
        this.properties.putAll(copy.properties);
    }

    /**
     * Returns the configuration properties.
     * 
     * @return The configuration properties.
     */
    public @NotNull Properties getProperties() {
        return this.properties;
    }

    /**
     * Adds the specified configuration properties to the configuration
     * properties.
     * 
     * @param values The configuration properties.
     */
    public void add(
            final @NotNull Properties values) {
        this.properties.putAll(values);
    }

    /**
     * Adds the specified configuration properties to the configuration
     * properties.
     * 
     * @param values The configuration properties.
     * @return This instance, for method chaining.
     */
    public void add(
            final @NotNull Map<@NotEmpty String, @NotNull String> values) {
        this.properties.putAll(values);
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
            this.properties.load(fileIS);
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
            this.properties.load(fileIS);
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
            this.properties.load(urlIS);
        } catch (final IOException e) {
            LOG.warn(READ_ERR, url, e);
        }
    }
}
