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

import java.util.prefs.Preferences;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;

/**
 * Implementation of {@code MutableConfig} based on Java {@code Preferences}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 0.1
 * @see MutableConfig
 * @see Preferences
 */
@API(status = API.Status.STABLE, since = "1.0")
public class MutablePreferencesConfig
extends PreferencesConfig
implements MutableConfig {

    /**
     * Creates a new instance with the configuration parameters of the
     * specified preferences node.
     * 
     * @param preferences The preferences node to use as storage of configuration
     * parameters
     */
    public MutablePreferencesConfig(
            final @NotNull Preferences preferences) {
        super(preferences);
    }

    /**
     * Creates a new instance based on the user preferences tree root node.
     * 
     * @return The created instance.
     * @see Preferences#userRoot()
     */
    public static @NotNull MutablePreferencesConfig ofUser() {
        return new MutablePreferencesConfig(
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
    public static @NotNull MutablePreferencesConfig ofUser(
            final @NotNull String path) {
        return new MutablePreferencesConfig(
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
    public static @NotNull MutablePreferencesConfig ofUser(
            final @NotNull Class<?> clazz) {
        return new MutablePreferencesConfig(
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
    public static @NotNull MutablePreferencesConfig ofUser(
            final @NotNull Class<?> clazz,
            final @NotNull String path) {
        return new MutablePreferencesConfig(
                Preferences.userNodeForPackage(clazz).node(path));
    }

    /**
     * Creates a new instance based on the system preferences tree root node.
     * 
     * @return The created instance.
     * @see Preferences#systemRoot()
     */
    public static @NotNull MutablePreferencesConfig ofSystem() {
        return new MutablePreferencesConfig(
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
    public static @NotNull MutablePreferencesConfig ofSystem(
            final @NotNull String path) {
        return new MutablePreferencesConfig(
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
    public static @NotNull MutablePreferencesConfig ofSystem(
            final @NotNull Class<?> clazz) {
        return new MutablePreferencesConfig(
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
    public static @NotNull MutablePreferencesConfig ofSystem(
            final @NotNull Class<?> clazz,
            final @NotNull String path) {
        return new MutablePreferencesConfig(
                Preferences.systemNodeForPackage(clazz).node(path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void set(
            final @NotBlank String key,
            final String value) {
        if (value == null) {
            remove(key);
        } else {
            Validate.notBlank(key, INVALID_KEY_ERROR);
            try {
                getPreferences().put(key, value);
            } catch (final IllegalStateException ise) {
                throw new ConfigException("Error setting configuration property value", ise);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void remove(
            final @NotBlank String... keys) {
        for (final String key : keys) {
            Validate.notBlank(key, INVALID_KEY_ERROR);
            try {
                getPreferences().remove(key);
            } catch (final IllegalStateException ise) {
                throw new ConfigException("Error retrieving configuration property value", ise);
            }
        }
    }
}
