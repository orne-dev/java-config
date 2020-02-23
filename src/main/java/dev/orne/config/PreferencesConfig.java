package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2020 Orne Developments
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.prefs.Preferences;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Implementation of {@code Config} based on Java {@code Preferences}.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2020-02
 * @see Config
 * @see Preferences
 */
public class PreferencesConfig
extends AbstractMutableStringConfig
implements MutableConfig {

	/** The preferences node to use as storage of configuration parameters. */
	private final Preferences preferences;

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
			@Nullable
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
			@Nullable
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
			@Nullable
			final Class<?> clazz,
			@Nullable
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
			@Nullable
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
			@Nullable
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
			@Nullable
			final Class<?> clazz,
			@Nullable
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
			@NotNull
			final Preferences preferences) {
		super();
		this.preferences = preferences;
	}

	/**
     * {@inheritDoc}
     */
	@Override
	protected boolean containsParameter(
			@NotBlank
			final String key) {
		return this.preferences.get(key, null) != null;
	}

	/**
     * {@inheritDoc}
     */
	@Override
	protected String getRawValue(
			@NotBlank
			final String key) {
		return this.preferences.get(key, null);
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
			remove(value);
		} else {
			this.preferences.put(key, value);
		}
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void remove(
			@NotBlank
			final String key) {
		this.preferences.remove(key);
	}
}
