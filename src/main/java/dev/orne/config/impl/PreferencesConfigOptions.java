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

import java.util.prefs.Preferences;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Options of {@code Preferences} based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see PreferencesConfigImpl
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class PreferencesConfigOptions {

    /** The preferences node to use as storage of configuration properties. */
    private Preferences preferences;

    /**
     * Empty constructor.
     */
    public PreferencesConfigOptions() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public PreferencesConfigOptions(
            final @NotNull PreferencesConfigOptions copy) {
        super();
        this.preferences = copy.preferences;
    }

    /**
     * Returns the preferences node to use as storage of configuration
     * properties.
     * 
     * @return The preferences node to use as storage of configuration
     * properties.
     */
    public @NotNull Preferences getPreferences() {
        return this.preferences;
    }

    /**
     * Sets the preferences node to use as storage of configuration
     * properties.
     * @param preferences The preferences node to use as storage of configuration
     * properties.
     */
    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    /**
     * Sets the user preferences tree root node.
     * 
     * @see Preferences#userRoot()
     */
    public void setUserPreferences() {
        this.preferences = Preferences.userRoot();
    }

    /**
     * Sets the node with the specified path on the user preferences tree.
     * 
     * @param path The path of the configuration's preferences, relative to
     * user preference tree's root node
     * @see Preferences#userRoot()
     * @see Preferences#node(String)
     */
    public void setUserPreferences(
            final @NotNull String path) {
        this.preferences = Preferences.userRoot().node(path);
    }

    /**
     * Sets the node for the package of the specified class on the user
     * preferences tree.
     * 
     * @param clazz The class which package use when calculating base node
     * @see Preferences#userNodeForPackage(Class)
     */
    public void setUserPreferences(
            final @NotNull Class<?> clazz) {
        this.preferences = Preferences.userNodeForPackage(clazz);
    }

    /**
     * Set the node with the specified path relative to the node for the
     * package of the specified class on the user preferences tree.
     * 
     * @param clazz The class which package use when calculating base node
     * @param path The path of the configuration's preferences, relative to
     * base node
     * @see Preferences#userNodeForPackage(Class)
     * @see Preferences#node(String)
     */
    public void setUserPreferences(
            final @NotNull Class<?> clazz,
            final @NotNull String path) {
        this.preferences = Preferences.userNodeForPackage(clazz).node(path);
    }

    /**
     * Sets the system preferences tree root node.
     * 
     * @see Preferences#systemRoot()
     */
    public void setSystemPreferences() {
        this.preferences = Preferences.systemRoot();
    }

    /**
     * Sets the node with the specified path on the system preferences tree.
     * 
     * @param path The path of the configuration's preferences, relative to
     * root node.
     * @see Preferences#systemRoot()
     * @see Preferences#node(String)
     */
    public void setSystemPreferences(
            final @NotNull String path) {
        this.preferences = Preferences.systemRoot().node(path);
    }

    /**
     * Sets the node for the package of the specified class on the system
     * preferences tree.
     * 
     * @param clazz The class which package use when calculating base node.
     * @see Preferences#systemNodeForPackage(Class)
     */
    public void setSystemPreferences(
            final @NotNull Class<?> clazz) {
        this.preferences = Preferences.systemNodeForPackage(clazz);
    }

    /**
     * Sets the specified path relative to the node for the package of the
     * specified class on the system preferences tree.
     * 
     * @param clazz The class which package use when calculating base node
     * @param path The path of the configuration's preferences, relative to
     * base node
     * @see Preferences#systemNodeForPackage(Class)
     * @see Preferences#node(String)
     */
    public void setSystemPreferences(
            final @NotNull Class<?> clazz,
            final @NotNull String path) {
        this.preferences = Preferences.systemNodeForPackage(clazz).node(path);
    }
}
