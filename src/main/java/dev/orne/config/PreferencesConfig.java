package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2020 Orne Developments
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

import java.util.Iterator;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.iterators.ObjectArrayIterator;
import org.apache.commons.lang3.Validate;

/**
 * Implementation of {@code Config} based on Java {@code Preferences}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 2.0, 2020-04
 * @since 0.1
 * @see Config
 * @see Preferences
 */
public class PreferencesConfig
extends AbstractMutableStringConfig
implements MutableConfig {

    /** Error message for invalid keys. */
    private static final String INVALID_KEY_ERROR = "Parameter key must be a non blank string";

    /** The preferences node to use as storage of configuration parameters. */
    private final @NotNull Preferences preferences;

    /**
     * Creates a new instance based on the user preferences tree root node.
     * 
     * Same as {@code PreferencesConfig(false, null, null)}.
     * 
     * @see #PreferencesConfig(boolean, Class, String)
     */
    public PreferencesConfig() {
        this(false, null, null);
    }

    /**
     * Creates a new instance based on the node with the specified path on the
     * user preferences tree.
     * 
     * Same as {@code PreferencesConfig(false, null, path)}.
     * 
     * @param path The path of the configuration's preferences, relative to
     * user preference tree's root node
     * @see #PreferencesConfig(boolean, Class, String)
     */
    public PreferencesConfig(
            final String path) {
        this(false, null, path);
    }

    /**
     * Creates a new instance based on the node for the package of the
     * specified class on the user preferences tree.
     * 
     * Same as {@code PreferencesConfig(false, class, null)}.
     * 
     * @param clazz The class which package use when calculating base node
     * @see #PreferencesConfig(boolean, Class, String)
     */
    public PreferencesConfig(
            final Class<?> clazz) {
        this(false, clazz, null);
    }

    /**
     * Creates a new instance based on the node with the specified path
     * relative to the node for the package of the specified class on the
     * user preferences tree.
     * 
     * Same as {@code PreferencesConfig(false, clazz, path)}.
     * 
     * @param clazz The class which package use when calculating base node
     * @param path The path of the configuration's preferences, relative to
     * base node
     * @see #PreferencesConfig(boolean, Class, String)
     */
    public PreferencesConfig(
            final Class<?> clazz,
            final String path) {
        this(false, clazz, path);
    }

    /**
     * Creates a new instance based on the system or user preferences tree root
     * node.
     * 
     * Same as {@code PreferencesConfig(system, null, null)}.
     * 
     * @param system If the system ({@code true}) or user ({@code false})
     * preferences tree should be used for the configuration's preferences node
     * @see #PreferencesConfig(boolean, Class, String)
     */
    public PreferencesConfig(
            final boolean system) {
        this(system, null, null);
    }

    /**
     * Creates a new instance based on the node with the specified path on the
     * user or system preferences tree.
     * 
     * Same as {@code PreferencesConfig(system, null, path)}.
     * 
     * @param system If the system ({@code true}) or user ({@code false})
     * preferences tree should be used for the configuration's preferences node
     * @param path The path of the configuration's preferences, relative to
     * base node
     * @see #PreferencesConfig(boolean, Class, String)
     */
    public PreferencesConfig(
            final boolean system,
            final String path) {
        this(system, null, path);
    }

    /**
     * Creates a new instance based on the node for the package of the
     * specified class on the user or system preferences tree.
     * 
     * Same as {@code PreferencesConfig(system, clazz, null)}.
     * 
     * @param system If the system ({@code true}) or user ({@code false})
     * preferences tree should be used for the configuration's preferences node
     * @param clazz The class which package use when calculating base node
     * @see #PreferencesConfig(boolean, Class, String)
     */
    public PreferencesConfig(
            final boolean system,
            final Class<?> clazz) {
        this(system, clazz, null);
    }

    /**
     * Creates a new instance based on the node with the specified path
     * relative to the node for the package of the specified class on the
     * user or system preferences tree.
     * 
     * Preferences node is selected based on the next examples:
     * 
     * <pre>
     * Preferences.userRoot() // false, null, null
     * Preferences.userNodeForPackage(clazz) // false, clazz, null
     * Preferences.userRoot().node(path) // false, null, path
     * Preferences.userNodeForPackage(clazz).node(path) // false, clazz, path
     * Preferences.systemRoot() // true, null, null
     * Preferences.systemNodeForPackage(clazz) // true, clazz, null
     * Preferences.systemRoot().node(path) // true, null, null
     * Preferences.systemNodeForPackage(clazz).node(path) // true, clazz, path
     * </pre>
     * 
     * @param system If the system ({@code true}) or user ({@code false})
     * preferences tree should be used for the configuration's preferences node
     * @param clazz The class which package use when calculating base node
     * @param path The path of the configuration's preferences, relative to
     * base node
     * @see Preferences#systemRoot()
     * @see Preferences#systemNodeForPackage(Class)
     * @see Preferences#userRoot()
     * @see Preferences#userNodeForPackage(Class)
     * @see Preferences#node(String)
     */
    public PreferencesConfig(
            final boolean system,
            final Class<?> clazz,
            final String path) {
        super();
        Preferences node;
        if (system) {
            if (clazz == null) {
                node = Preferences.systemRoot();
            } else {
                node = Preferences.systemNodeForPackage(clazz);
            }
        } else {
            if (clazz == null) {
                node = Preferences.userRoot();
            } else {
                node = Preferences.userNodeForPackage(clazz);
            }
        }
        if (path != null) {
            node = node.node(path);
        }
        this.preferences = node;
    }

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
    public boolean isEmpty()
    throws ConfigException {
        try {
            return this.preferences.keys().length == 0;
        } catch (final IllegalStateException | BackingStoreException e) {
            throw new ConfigException("Error accessing configuration", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Iterator<String> getKeys()
    throws ConfigException {
        try {
            return new ObjectArrayIterator(this.preferences.keys());
        } catch (final IllegalStateException | BackingStoreException e) {
            throw new ConfigException("Error accessing configuration", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean containsParameter(
            final @NotBlank String key)
    throws ConfigException {
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
    protected String getRawValue(
            final @NotBlank String key)
    throws ConfigException {
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
    protected void setRawValue(
            final @NotBlank String key,
            final String value)
    throws ConfigException {
        if (value == null) {
            remove(key);
        } else {
            Validate.notBlank(key, INVALID_KEY_ERROR);
            try {
                this.preferences.put(key, value);
            } catch (final IllegalStateException ise) {
                throw new ConfigException("Error setting configuration property value", ise);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(
            final @NotBlank String key)
    throws ConfigException {
        Validate.notBlank(key, INVALID_KEY_ERROR);
        try {
            this.preferences.remove(key);
        } catch (final IllegalStateException ise) {
            throw new ConfigException("Error retrieving configuration property value", ise);
        }
    }
}
