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

import java.util.Collection;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;

/**
 * Interface that maps {@code Preferences} nodes to
 * {@code HierarchicalConfiguration} and back.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @param <N> The {@code HierarchicalConfiguration} node type
 * @since 0.2
 * @see Preferences
 * @see HierarchicalConfiguration
 */
public interface PreferencesMapper<N> {

    /**
     * Resolves the {@code HierarchicalConfiguration} key of the specified
     * {@code Preferences} node.
     * 
     * @param config The {@code Preferences} based {@code Configuration}
     * @param baseNode The configuration's base {@code Preferences} node
     * @param node The {@code Preferences} node
     * @return The {@code HierarchicalConfiguration} key
     * @throws ConfigurationRuntimeException If an error occurs resolving
     * the key
     */
    String resolveNodeKey(
            @NotNull PreferencesBased<N> config,
            @NotNull Preferences baseNode,
            @NotNull Preferences node);

    /**
     * Resolves the {@code HierarchicalConfiguration} key of the specified
     * {@code Preferences} node property.
     * 
     * @param config The {@code Preferences} based {@code Configuration}
     * @param baseNode The configuration's base {@code Preferences} node
     * @param node The {@code Preferences} node
     * @param key The {@code Preferences} node property
     * @return The {@code HierarchicalConfiguration} key
     * @throws ConfigurationRuntimeException If an error occurs resolving
     * the key
     */
    String resolvePropertyKey(
            @NotNull PreferencesBased<N> config,
            @NotNull Preferences baseNode,
            @NotNull Preferences node,
            @NotNull String key);

    /**
     * Sets the specified {@code HierarchicalConfiguration} property value
     * in the {@code Preferences} tree.
     * 
     * @param config The {@code Preferences} based {@code Configuration}
     * @param baseNode The configuration's base {@code Preferences} node
     * @param name The {@code HierarchicalConfiguration} property key
     * @param value The {@code HierarchicalConfiguration} property value
     * @throws ConfigurationRuntimeException If an error occurs setting
     * the property value
     */
    void setProperty(
            @NotNull PreferencesBased<N> config,
            @NotNull Preferences baseNode,
            @NotNull String name,
            Object value);

    /**
     * Adds the specified {@code HierarchicalConfiguration} nodes as child
     * of the specified parent node in the {@code Preferences} tree.
     * 
     * @param config The {@code Preferences} based {@code Configuration}
     * @param baseNode The configuration's base {@code Preferences} node
     * @param name The key of the parent {@code HierarchicalConfiguration} node
     * @param newNodes The {@code HierarchicalConfiguration} nodes to add
     * @throws ConfigurationRuntimeException If an error occurs adding
     * the new nodes
     */
    void addNodes(
            @NotNull PreferencesBased<N> config,
            @NotNull Preferences baseNode,
            @NotNull String name,
            @NotNull Collection<N> newNodes);

    /**
     * Removes the specified {@code HierarchicalConfiguration} property value
     * in the {@code Preferences} tree.
     * 
     * @param config The {@code Preferences} based {@code Configuration}
     * @param baseNode The configuration's base {@code Preferences} node
     * @param name The {@code HierarchicalConfiguration} property key
     * @param value The {@code HierarchicalConfiguration} property value
     * @throws ConfigurationRuntimeException If an error occurs removing
     * the property value
     */
    void removeProperty(
            @NotNull PreferencesBased<N> config,
            @NotNull Preferences baseNode,
            @NotNull String name);

    /**
     * Removes the specified {@code HierarchicalConfiguration} node
     * in the {@code Preferences} tree.
     * <p>
     * Removes the node value, it's attributes and all child nodes.
     * 
     * @param config The {@code Preferences} based {@code Configuration}
     * @param baseNode The configuration's base {@code Preferences} node
     * @param name The {@code HierarchicalConfiguration} property key
     * @param value The {@code HierarchicalConfiguration} property value
     * @throws ConfigurationRuntimeException If an error occurs removing
     * the node
     */
    void removeNode(
            @NotNull PreferencesBased<N> config,
            @NotNull Preferences baseNode,
            String name);

    /**
     * Creates a {@code HierarchicalConfiguration} node hierarchy
     * that mirrors the specified {@code Preferences} tree content.
     * 
     * @param node The root {@code Preferences} node
     * @param elemRefs A map for linking {@code Preferences} nodes to
     * {@code HierarchicalConfiguration} nodes. If {@code null} then references
     * are irrelevant
     * @return The {@code HierarchicalConfiguration} node
     * @throws ConfigurationRuntimeException If an error occurs reading
     * the {@code Preferences} tree content
     */
    @NotNull N loadNodeHierarchy(
            @NotNull Preferences node,
            Map<N, ? super Preferences> elemRefs);

    /**
     * Saves the specified {@code HierarchicalConfiguration} node hierarchy
     * content in the specified {@code Preferences} node.
     * 
     * @param config The {@code Preferences} based {@code Configuration}
     * @param preferences The target {@code Preferences} node
     * @throws ConfigurationRuntimeException If an error occurs writing
     * the {@code Preferences} tree content
     */
    void saveNodeHierarchy(
            @NotNull PreferencesBased<N> config,
            @NotNull Preferences preferences);
}
