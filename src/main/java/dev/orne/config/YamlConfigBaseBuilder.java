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
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * YAML files based configuration base builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @param <S> The concrete type of the builder.
 * @since 1.0
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface YamlConfigBaseBuilder<S extends YamlConfigBaseBuilder<S>>
extends ConfigBuilder<S> {

    /** The default configuration nested properties separator. */
    public static final String DEFAULT_SEPARATOR = ".";

    /**
     * Sets the configuration nested properties separator.
     * 
     * @param separator The configuration nested properties separator.
     * @return This instance, for method chaining.
     */
    @NotNull S withSeparator(
            @NotEmpty String separator);

    /**
     * Adds the specified custom properties to the configuration
     * properties.
     * <p>
     * Note that property keys will be processed with the configured
     * nested properties separator.
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
