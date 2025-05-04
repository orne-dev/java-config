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
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;
import org.apache.commons.configuration2.tree.ImmutableNode;

/**
 * Implementation of {@code PreferencesMapper} for
 * {@code ImmutableNode} based {@code HierarchicalConfiguration} instances that
 * maps all {@code Preferences} properties to empty child {@code ImmutableNode}
 * nodes with value.
 * <p>
 * All {@code Preferences} are mapped as child {@code ImmutableNode} with
 * value and no attributes or childs.
 * <p>
 * Any {@code ImmutableNode} can have value.
 * <p>
 * No {@code ImmutableNode} has attributes.
 * 
 * @author <a href="mailto:https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 */
public class NodeBasedPreferencesMapper
extends AbstractImmutableNodePreferencesMapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public String resolvePropertyKey(
            final @NotNull PreferencesBased<ImmutableNode> config,
            final @NotNull Preferences baseNode,
            final @NotNull Preferences node,
            final @NotNull String key) {
        final String nodeKey = resolveNodeKey(
                config,
                baseNode,
                node); 
        return config.getExpressionEngine().nodeKey(
                createNodeForName(key),
                nodeKey,
                config.getNodeModel().getNodeHandler());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getValue(
            final @NotNull Preferences node) {
        try {
            return getChildValue(
                    node.parent(),
                    node.name());
        } catch (final IllegalStateException ise) {
            throw new PreferencesNodeDeletedException(READ_ERR, ise);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setValue(
            final @NotNull Preferences node,
            final String value) {
        try {
            setChildValue(
                    node.parent(),
                    node.name(),
                    value);
        } catch (final IllegalStateException ise) {
            throw new PreferencesNodeDeletedException(WRITE_ERR, ise);
        }
    }

    /**
     * Returns {@code true} is the {@code Preferences} attribute name is the
     * value of a child {@code Preferences} node.
     * 
     * @param node The {@code Preferences} node
     * @param name The {@code Preferences} attribute name
     * @return If the attribute is the value child {@code Preferences} node
     * @throws PreferencesNodeDeletedException If the {@code Preferences} node
     * has been deleted
     * @throws ConfigurationRuntimeException If an error occurs reading the
     * node
     */
    protected boolean isChildValue(
            final @NotNull Preferences node,
            final @NotNull String name) {
        try {
            return node.nodeExists(name);
        } catch (final IllegalStateException ise) {
            throw new PreferencesNodeDeletedException(READ_ERR, ise);
        } catch (final IllegalArgumentException | BackingStoreException e) {
            throw new ConfigurationRuntimeException(READ_ERR, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getChildValue(
            final @NotNull Preferences parent,
            final @NotNull String name) {
        try {
            return parent.get(name, null);
        } catch (final IllegalStateException ise) {
            throw new PreferencesNodeDeletedException(READ_ERR, ise);
        } catch (final IllegalArgumentException ise) {
            throw new ConfigurationRuntimeException(READ_ERR, ise);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setChildValue(
            final @NotNull Preferences parent,
            final @NotNull String name,
            final String value) {
        try {
            if (value == null) {
                parent.remove(name);
            } else {
                parent.put(name, value);
            }
        } catch (final IllegalStateException ise) {
            throw new PreferencesNodeDeletedException(WRITE_ERR, ise);
        } catch (final IllegalArgumentException ise) {
            throw new ConfigurationRuntimeException(WRITE_ERR, ise);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull Collection<String> getAttributesNames(
            final @NotNull Preferences node) {
        return super.getAttributesNames(node).parallelStream()
                .filter(attr -> !isChildValue(node, attr))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void constructAttributes(
            final @NotNull ImmutableNode.Builder builder,
            final @NotNull Preferences node,
            final Map<ImmutableNode, ? super Preferences> elemRefs) {
        for (final Map.Entry<String, String> attr : getAttributes(node).entrySet()) {
            builder.addChild(createPropertyNode(attr.getKey(), attr.getValue()));
        }
    }

    /**
     * Creates a new {@code HierarchicalConfiguration} node for the
     * specified property name and value.
     * 
     * @param name The {@code Preferences} node property name
     * @param value The {@code Preferences} node property value
     * @return The {@code HierarchicalConfiguration} node for the property
     */
    protected @NotNull ImmutableNode createPropertyNode(
            final @NotNull String name,
            final @NotNull String value) {
        return new ImmutableNode.Builder()
                .name(name)
                .value(value)
                .create();
    }
}
