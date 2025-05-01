package dev.orne.config;

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
import java.util.Properties;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@code Config} based on {@code Properties} files.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2019-07
 * @version 2.0, 2025-04
 * @since 0.1
 * @see Config
 * @see Properties
 */
@API(status = API.Status.STABLE, since = "1.0")
public class PropertiesConfig
implements Config {

    /** The class logger. */
    private static final Logger LOG =
            LoggerFactory.getLogger(PropertiesConfig.class);

    /** Error message for blank property keys. */
    protected static final String KEY_BLANK_ERR =
            "Property key must be a non blank string";
    /** Error message for unsupported source types. */
    private static final String SOURCE_TYPE_ERR =
            "Ignored unsupported configuration source type: {}";
    /** Error message for not found resources. */
    private static final String RESOURCE_NOT_FOUND_ERR =
            "Configuration resource not found: {}";
    /** Properties file read error message. */
    private static final String READ_ERR =
            "Error reading configuration resource: {}";

    /** Current configuration parameters. */
    private final @NotNull Properties config;

    /**
     * Creates a new instance with the configuration parameters loaded from
     * the sources passed as argument.
     * <p>
     * Supports {@code String} class path resources,
     * files as {@code Path} or {@code File} instances,
     * {@code URL} or {@code Iterable} collections of any of them.
     * 
     * @param sources The sources to load the configuration parameters from.
     */
    public PropertiesConfig(
            final @NotNull Object... sources) {
        this(new Properties(), sources);
    }

    /**
     * Creates a new instance with the configuration parameters loaded from
     * the sources passed as argument.
     * <p>
     * Supports {@code String} class path resources,
     * files as {@code Path} or {@code File} instances,
     * {@code URL} or {@code Iterable} collections of any of them.
     * 
     * @param config The {@code Properties} instance to use as inner container.
     * @param sources The sources to load the configuration parameters from.
     */
    protected PropertiesConfig(
            final @NotNull Properties config,
            final @NotNull Object... sources) {
        super();
        this.config = config;
        load(sources);
    }

    /**
     * Loads the configuration parameters from the sources passed as argument.
     * 
     * @param sources The sources to load the configuration parameters from.
     */
    protected final void load(
            final @NotNull Object... sources) {
        Validate.notNull(sources, "Parameter sources is required");
        for (final Object source : sources) {
            loadSource(source);
        }
    }

    /**
     * Loads the configuration parameters from the source passed as argument.
     * <p>
     * Supports {@code String} class path resources,
     * files as {@code Path} or {@code File} instances,
     * {@code URL} or {@code Iterable} collections of any of them.
     * 
     * @param source The source to load the configuration parameters from.
     */
    protected final void loadSource(
            final @NotNull Object source) {
        Validate.notNull(source, "Parameter source is required");
        if (source instanceof String) {
            loadFromResource((String) source);
        } else if (source instanceof Path) {
            loadFromPath((Path) source);
        } else if (source instanceof File) {
            loadFromFile((File) source);
        } else if (source instanceof URL) {
            loadFromURL((URL) source);
        } else if (source instanceof Properties) {
            loadFromValues((Properties) source);
        } else if (source instanceof Iterable) {
            for (final Object nestedSource : ((Iterable<?>) source)) {
                loadSource(nestedSource);
            }
        } else {
            LOG.warn(SOURCE_TYPE_ERR, source);
        }
    }

    /**
     * Loads the configuration parameters from the {@code Properties} instance
     * passed as argument.
     * 
     * @param values The configuration parameters to load
     */
    protected final void loadFromValues(
            final @NotNull Properties values) {
        this.config.putAll(values);
    }

    /**
     * Loads the configuration parameters from a file present in the classpath
     * under the path passed as argument.
     * 
     * @param path The path of the classpath resource to load
     */
    protected final void loadFromResource(
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
                loadFromURL(resources.nextElement());
            }
        } catch (final IOException e) {
            LOG.warn(READ_ERR, path, e);
        }
    }

    /**
     * Loads the configuration parameters contained in the file in the
     * specified path.
     * 
     * @param path The file to load the parameters from.
     */
    protected final void loadFromPath(
            final @NotNull Path path) {
        if (!Files.exists(path)) {
            LOG.warn(RESOURCE_NOT_FOUND_ERR, path);
        }
        try (final InputStream fileIS = Files.newInputStream(path)) {
            this.config.load(fileIS);
        } catch (final IOException e) {
            LOG.warn(READ_ERR, path, e);
        }
    }

    /**
     * Loads the configuration parameters contained in the {@code File} passed
     * as argument.
     * 
     * @param file The file to load the parameters from
     */
    protected final void loadFromFile(
            final @NotNull File file) {
        try (final InputStream fileIS = new FileInputStream(file)) {
            this.config.load(fileIS);
        } catch (final FileNotFoundException e) {
            LOG.warn(RESOURCE_NOT_FOUND_ERR, file, e);
        } catch (final IOException e) {
            LOG.warn(READ_ERR, file, e);
        }
    }

    /**
     * Loads the configuration parameters returned from the {@code URL} passed
     * as argument.
     * 
     * @param url The URL to load the parameters from
     */
    protected final void loadFromURL(
            final @NotNull URL url) {
        try {
            final InputStream urlIS = url.openStream();
            try {
                this.config.load(urlIS);
            } finally {
                urlIS.close();
            }
        } catch (final IOException e) {
            LOG.warn(READ_ERR, url, e);
        }
    }

    /**
     * Returns the internal {@code Properties} instance.
     * 
     * @return The configuration properties
     */
    protected @NotNull Properties getProperties() {
        return this.config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return this.config.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Set<String> getKeys() {
        return this.config.stringPropertyNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(
            final @NotBlank String key) {
        Validate.notBlank(key, KEY_BLANK_ERR);
        return this.config.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(
            final @NotBlank String key) {
        Validate.notBlank(key, KEY_BLANK_ERR);
        return this.config.getProperty(key);
    }
}
