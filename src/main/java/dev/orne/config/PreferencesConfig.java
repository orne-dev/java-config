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

import java.util.Objects;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;

/**
 * Implementation of {@code Config} based on Java {@code Preferences}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2019-07
 * @version 2.0, 2025-05
 * @since 0.1
 * @see Config
 * @see Preferences
 */
@API(status = API.Status.STABLE, since = "1.0")
public class PreferencesConfig
implements Config {

    /** Error message for invalid keys. */
    protected static final String INVALID_KEY_ERROR = "Parameter key must be a non blank string";

    /** The preferences node to use as storage of configuration parameters. */
    private final @NotNull Preferences preferences;

    /**
     * Creates a new instance with the configuration parameters of the
     * specified preferences node.
     * 
     * @param preferences The preferences node to use as storage of configuration
     * parameters
     */
    public PreferencesConfig(
            final @NotNull Preferences preferences) {
        super();
        this.preferences = preferences;
    }

    /**
     * Creates a new instance based on the user preferences tree root node.
     * 
     * @return The created instance.
     * @see Preferences#userRoot()
     */
    public static @NotNull PreferencesConfig ofUser() {
        return new PreferencesConfig(
                Preferences.userRoot());
    }

    /**
     * Creates a new instance based on the node with the specified path on the
     * user preferences tree.
     * 
     * @param path The path of the configuration's preferences, relative to
     * user preference tree's root node
     * @return The created instance.
     * @see Preferences#userRoot()
     * @see Preferences#node(String)
     */
    public static @NotNull PreferencesConfig ofUser(
            final @NotNull String path) {
        return new PreferencesConfig(
                Preferences.userRoot().node(path));
    }

    /**
     * Creates a new instance based on the node for the package of the
     * specified class on the user preferences tree.
     * 
     * @param clazz The class which package use when calculating base node
     * @return The created instance.
     * @see Preferences#userNodeForPackage(Class)
     */
    public static @NotNull PreferencesConfig ofUser(
            final @NotNull Class<?> clazz) {
        return new PreferencesConfig(
                Preferences.userNodeForPackage(clazz));
    }

    /**
     * Creates a new instance based on the node with the specified path
     * relative to the node for the package of the specified class on the
     * user preferences tree.
     * 
     * @param clazz The class which package use when calculating base node
     * @param path The path of the configuration's preferences, relative to
     * base node
     * @return The created instance.
     * @see Preferences#userNodeForPackage(Class)
     * @see Preferences#node(String)
     */
    public static @NotNull PreferencesConfig ofUser(
            final @NotNull Class<?> clazz,
            final @NotNull String path) {
        return new PreferencesConfig(
                Preferences.userNodeForPackage(clazz).node(path));
    }

    /**
     * Creates a new instance based on the system preferences tree root node.
     * 
     * @return The created instance.
     * @see Preferences#systemRoot()
     */
    public static @NotNull PreferencesConfig ofSystem() {
        return new PreferencesConfig(
                Preferences.systemRoot());
    }

    /**
     * Creates a new instance based on the node with the specified path on the
     * system preferences tree.
     * 
     * @param path The path of the configuration's preferences, relative to
     * root node.
     * @return The created instance.
     * @see Preferences#systemRoot()
     * @see Preferences#node(String)
     */
    public static @NotNull PreferencesConfig ofSystem(
            final @NotNull String path) {
        return new PreferencesConfig(
                Preferences.systemRoot().node(path));
    }

    /**
     * Creates a new instance based on the node for the package of the
     * specified class on the system preferences tree.
     * 
     * @param clazz The class which package use when calculating base node.
     * @return The created instance.
     * @see Preferences#systemNodeForPackage(Class)
     */
    public static @NotNull PreferencesConfig ofSystem(
            final @NotNull Class<?> clazz) {
        return new PreferencesConfig(
                Preferences.systemNodeForPackage(clazz));
    }

    /**
     * Creates a new instance based on the node with the specified path
     * relative to the node for the package of the specified class on the
     * system preferences tree.
     * 
     * @param clazz The class which package use when calculating base node
     * @param path The path of the configuration's preferences, relative to
     * base node
     * @return The created instance.
     * @see Preferences#systemNodeForPackage(Class)
     * @see Preferences#node(String)
     */
    public static @NotNull PreferencesConfig ofSystem(
            final @NotNull Class<?> clazz,
            final @NotNull String path) {
        return new PreferencesConfig(
                Preferences.systemNodeForPackage(clazz).node(path));
    }

    /**
     * Returns the preferences node to use as storage of configuration
     * parameters.
     * 
     * @return The preferences node.
     */
    protected @NotNull Preferences getPreferences() {
        return this.preferences;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Stream<String> getKeys() {
        try {
            return Stream.of(this.preferences.keys());
        } catch (final IllegalStateException | BackingStoreException e) {
            throw new ConfigException("Error accessing configuration", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(
            final @NotBlank String key) {
        Validate.notBlank(key, INVALID_KEY_ERROR);
        try {
            return this.preferences.get(key, null) != null;
        } catch (final IllegalStateException ise) {
            throw new ConfigException("Error accessing configuration property", ise);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(
            final @NotBlank String key) {
        Validate.notBlank(key, INVALID_KEY_ERROR);
        try {
            return this.preferences.get(key, null);
        } catch (final IllegalStateException ise) {
            throw new ConfigException("Error retrieving configuration property value", ise);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.preferences);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PreferencesConfig other = (PreferencesConfig) obj;
        return Objects.equals(this.preferences, other.preferences);
    }
}
