package dev.orne.config;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

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

/**
 * Configuration properties provider with properties mutable at runtime.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2015-10
 * @since 1.0
 * @see MutableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface FileMutableConfig
extends MutableConfig {

    /**
     * Saves the configuration properties to the specified destination file.
     * If the file format requires it, the saved file will have the default
     * encoding (usually UTF-8).
     * 
     * @param destination The destination file.
     * @throws IOException If an I/O error occurs.
     */
    default void save(
            @NotNull File destination)
    throws IOException {
        save(destination.toPath());
    }

    /**
     * Saves the configuration properties to the specified destination path.
     * If the file format requires it, the saved file will have the default
     * encoding (usually UTF-8).
     * 
     * @param destination The destination path.
     * @throws IOException If an I/O error occurs.
     */
    default void save(
            @NotNull Path destination)
    throws IOException {
        try (final OutputStream output = Files.newOutputStream(
                destination,
                StandardOpenOption.WRITE)) {
            save(output);
        }
    }

    /**
     * Saves the configuration properties to the specified output stream.
     * If the file format requires it, the saved content will be encoded
     * using the default encoding (usually UTF-8).
     * 
     * @param destination The destination output stream.
     * @throws IOException If an I/O error occurs.
     */
    default void save(
            @NotNull OutputStream destination)
    throws IOException {
        save(destination, StandardCharsets.UTF_8);
    }

    /**
     * Saves the configuration properties to the specified output stream
     * using the specified encoding.
     * 
     * @param destination The destination output stream.
     * @param encoding The encoding to use.
     * @throws IOException If an I/O error occurs.
     */
    default void save(
            @NotNull OutputStream destination,
            @NotNull Charset encoding)
    throws IOException {
        try (final Writer writer = new OutputStreamWriter(
                destination,
                encoding)) {
            save(writer);
        }
    }

    /**
     * Saves the configuration properties to the specified writer.
     * 
     * @param destination The destination writer.
     * @throws IOException If an I/O error occurs.
     */
    void save(
            @NotNull Writer destination)
    throws IOException;
}
