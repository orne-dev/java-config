package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

/**
 * Implementation of {@code Config} based on {@code Properties} files.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0, 2019-07
 * @see Config
 * @see Properties
 */
public class PropertiesConfig
extends AbstractMutableStringConfig {

    /** The class logger. */
    private static final Logger LOG =
            LoggerFactory.getLogger(PropertiesConfig.class);

    /** Mensaje de error para tipos de origenes no soportados. */
    private static final String SOURCE_TYPE_ERR =
            "Unsupported configuration source type ({}). Ignored.";
    /** Mensaje de error para origenes no encontrados. */
    private static final String RESOURCES_ERR =
            "Could not find resources ({}).";
    /** Mensaje de error para errores de lectura en fichero de propiedades. */
    /** Properties file read error message. */
    private static final String PROP_FILE_ERR =
            "Could not read configuration file ({}).";

    /** Current configuration parameters. */
    private final Properties config = new Properties();

    /**
     * Creates a new instance with the configuration parameters loaded from
     * the sources passed as argument.
     * 
     * Supports classpath resources, files, URL or {@code Iterable} objects
     * of any of them.
     * 
     * @param sources The sources to load the configuration parameters from
     */
    public PropertiesConfig(
            @NotNull
            final Object... sources) {
        super();
        load(sources);
    }

    /**
     * Loads the configuration parameters from the sources passed as argument.
     * 
     * @param sources The sources to load the configuration parameters from
     */
    protected final void load(
            @NotNull
            final Object... sources) {
        Validate.notNull(sources, "Parameter sources is required");
        for (final Object source : sources) {
            loadSource(source);
        }
    }

    /**
     * Loads the configuration parameters from the source passed as argument.
     * Supports classpath resources, files, URL or an {@code Iterable} of any
     * of them.
     * 
     * @param source The source to load the configuration parameters from
     */
    protected final void loadSource(
            @NotNull
            final Object source) {
        Validate.notNull(source, "Parameter source is required");
        if (source instanceof String) {
            loadFromResource((String) source);
        } else if (source instanceof File) {
            loadFromFile((File) source);
        } else if (source instanceof URL) {
            loadFromURL((URL) source);
        } else if (source instanceof Properties) {
            loadFromValues((Properties) source);
        } else if (source instanceof Iterable) {
            final Iterator<?> subIt = ((Iterable<?>) source).iterator();
            while (subIt.hasNext()) {
                loadSource(subIt.next());
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
            @NotNull
            final Properties values) {
        this.config.putAll(values);
    }

    /**
     * Loads the configuration parameters from a file present in the classpath
     * under the path passed as argument.
     * 
     * @param path The path of the classpath resource to load
     */
    protected final void loadFromResource(
            @NotNull
            final String path) {
        try {
            final Enumeration<URL> resources = 
                    Thread.currentThread()
                            .getContextClassLoader()
                            .getResources(path);
            while (resources.hasMoreElements()) {
                loadFromURL(resources.nextElement());
            }
        } catch (final IOException ioe) {
            LOG.warn(MessageFormatter.format(RESOURCES_ERR, path).getMessage(),
                    ioe);
        }
    }

    /**
     * Loads the configuration parameters contained in the {@code File} passed
     * as argument.
     * 
     * @param file The file to load the parameters from
     */
    protected final void loadFromFile(
            @NotNull
            final File file) {
        try {
            final InputStream fileIS = new FileInputStream(file);
            try {
                this.config.load(fileIS);
            } finally {
                fileIS.close();
            }
        } catch (final IOException ioe) {
            LOG.warn(MessageFormatter.format(PROP_FILE_ERR, file).getMessage(),
                    ioe);
        }
    }

    /**
     * Loads the configuration parameters returned from the {@code URL} passed
     * as argument.
     * 
     * @param url The URL to load the parameters from
     */
    protected final void loadFromURL(
            @NotNull
            final URL url) {
        try {
            final InputStream urlIS = url.openStream();
            try {
                this.config.load(urlIS);
            } finally {
                urlIS.close();
            }
        } catch (final IOException ioe) {
            LOG.warn(MessageFormatter.format(PROP_FILE_ERR, url).getMessage(),
                    ioe);
        }
    }

    /**
     * Returns the internal {@code Properties} instance.
     * 
     * @return The configuration properties
     */
    @NotNull
    protected Properties getProperties() {
        return this.config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean containsParameter(
            @NotBlank
            final String key) {
        Validate.notNull(key, "Parameter key is required");
        return this.config.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    protected String getRawValue(
            @NotBlank
            final String key) {
        Validate.notNull(key, "Parameter key is required");
        return this.config.getProperty(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setRawValue(
            @NotBlank
            final String key,
            @Nullable
            final String value) {
        if (value == null) {
            remove(key);
        } else {
            this.config.setProperty(key, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(
            @NotBlank
            final String key) {
        Validate.notNull(key, "Parameter key is required");
        this.config.remove(key);
    }
}
