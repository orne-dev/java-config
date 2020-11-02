package dev.orne.config.commons.prefs;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2021 Orne Developments
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

import java.util.prefs.Preferences;

import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;

/**
 * Exception thrown when a {@code Preferences} node has been deleted by another
 * Thread.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-11
 * @since 0.2
 * @see Preferences
 */
public class PreferencesNodeDeletedException
extends ConfigurationRuntimeException {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new {@code PreferencesNodeDeletedException} without
     * specified detail message.
     */
    public PreferencesNodeDeletedException() {
        super();
    }

    /**
     * Constructs a new {@code PreferencesNodeDeletedException} with
     * specified detail message.
     * 
     * @param message The error message
     */
    public PreferencesNodeDeletedException(
            final String message) {
        super(message);
    }

    /**
     * Constructs a new {@code PreferencesNodeDeletedException} with
     * specified detail message using {@link String#format(String,Object...)}.
     * 
     * @param message The error message
     * @param args The arguments to the error message
     * @see String#format(String,Object...)
     */
    public PreferencesNodeDeletedException(
            final String message,
            final Object... args) {
        super(message, args);
    }

    /**
     * Constructs a new {@code PreferencesNodeDeletedException} with
     * specified nested {@code Throwable}.
     * 
     * @param cause The exception or error that caused this exception to be
     * thrown
     */
    public PreferencesNodeDeletedException(
            final Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code PreferencesNodeDeletedException} with
     * specified detail message and nested {@code Throwable}.
     *
     * @param message The error message
     * @param cause The exception or error that caused this exception to be
     * thrown
     */
    public PreferencesNodeDeletedException(
            final String message,
            final Throwable cause) {
        super(message, cause);
    }
}
