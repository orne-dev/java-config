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

/**
 * <p>
 * Definition of a parameters interface for preferences configurations.
 * </p>
 * <p>
 * This interface defines set methods for additional properties common to all
 * preferences configurations.
 * </p>
 * <p>
 * <strong>Important note:</strong> This interface is not intended to be
 * implemented by client code! It defines a set of available properties and may
 * be extended even in minor releases.
 * </p>
 * 
 * @author <a href="mailto:https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2020-09
 * @param <T> The type of the result of all set methods for method chaining
 * @param <N> The type of nodes of the generated {@code HierarchicalConfiguration}
 * @since 0.2
 */
public interface PreferencesBuilderProperties<T, N> {

    /**
     * Sets the {@code PreferencesMapper} to use when mapping
     * {@code Preferences} nodes to {@code PreferencesBased} nodes.
     * 
     * @param mapper The {@code PreferencesMapper} to use
     * @return a reference to this object for method chaining
     */
    T setPreferencesMapper(PreferencesMapper<N> mapper);

    /**
     * Set the configuration's base preferences node. 
     * 
     * @param baseNode The configuration's base preferences node
     * @return a reference to this object for method chaining
     */
    T setBaseNode(Preferences baseNode);

    /**
     * Sets if the system ({@code true}) or user ({@code false}) preferences
     * tree should be used for the configuration's base preferences node.
     *
     * @param system If the system preferences tree should be used
     * @return a reference to this object for method chaining
     */
    T setSystemScope(boolean system);

    /**
     * Sets the class which package use when calculating base node.
     *
     * @param clazz The class which package use when calculating base node
     * @return a reference to this object for method chaining
     */
    T setBaseClass(Class<?> clazz);

    /**
     * Sets the path of the configuration's preferences, relative to
     * base node.
     *
     * @param path The path of the configuration's preferences
     * @return a reference to this object for method chaining
     */
    T setPath(String path);

    /**
     * Set if the feature of {@code Preferences} modifications auto load should
     * be enabled.
     * 
     * @param enable If the feature should be enabled
     * @return a reference to this object for method chaining
     */
    T setAutoLoad(boolean enable);

    /**
     * Set if the feature of {@code Configuration} modifications auto save
     * should be enabled.
     * 
     * @param enable If the feature should be enabled
     * @return a reference to this object for method chaining
     */
    T setAutoSave(boolean enable);

    /**
     * Sets the event coordination strategy.
     * 
     * @param strategy The event coordination strategy
     * @return a reference to this object for method chaining
     */
    T setEventCoordinationStrategy(EventCoordinationStrategy strategy);
}
