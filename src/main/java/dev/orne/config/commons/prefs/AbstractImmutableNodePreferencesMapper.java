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
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;
import org.apache.commons.configuration2.tree.ImmutableNode;

/**
 * Abstract implementation of {@code PreferencesMapper} for
 * {@code ImmutableNode} based {@code HierarchicalConfiguration} instances.
 * 
 * @author <a href="mailto:https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @see ImmutableNode
 * @since 0.2
 */
public abstract class AbstractImmutableNodePreferencesMapper
extends AbstractPreferencesMapper<ImmutableNode> {

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ImmutableNode loadNodeHierarchy(
            final @NotNull Preferences node,
            final Map<ImmutableNode, ? super Preferences> elemRefs) {
        final ImmutableNode.Builder builder = new ImmutableNode.Builder()
                .name(node.name());
        constructHierarchy(builder, node, elemRefs);
        return builder.create();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull ImmutableNode createNodeForName(
            final @NotNull String name) {
        return new ImmutableNode.Builder()
                .name(name)
                .create();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull String getName(
            final @NotNull ImmutableNode node) {
        return node.getNodeName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getValue(
            final @NotNull ImmutableNode node) {
        return node.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull Collection<String> getAttributesNames(
            final @NotNull ImmutableNode node) {
        return getAttributes(node).keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull Map<String, Object> getAttributes(
            final @NotNull ImmutableNode node) {
        return node.getAttributes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getAttribute(
            final @NotNull ImmutableNode node,
            final @NotNull String name) {
        return node.getAttributes().get(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull Collection<String> getChildrenNames(
            final @NotNull ImmutableNode node) {
        return getChildren(node)
                .stream()
                .map(ImmutableNode::getNodeName)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull Collection<ImmutableNode> getChildren(
            final @NotNull ImmutableNode node) {
        return node.getChildren();
    }

    /**
     * Configures the specified {@code ImmutableNode.Builder} with the 
     * data of the specified {@code Preferences} node.
     * <p>
     * Sets node value based on {@code getValue()} and calls
     * {@code constructChildren()} and {@code constructAttributes()}.
     *
     * @param builder The current {@code HierarchicalConfiguration} node builder
     * @param node The current {@code Preferences} node
     * @param elemRefs A map for assigning references objects to nodes. Can be
     * {@code null}, then reference objects are irrelevant
     * @throws ConfigurationRuntimeException If an error occurs creating the node
     * hierarchy
     * @see #getValue(Preferences)
     * @see #constructChildren(ImmutableNode.Builder, Preferences, Map)
     * @see #constructAttributes(ImmutableNode.Builder, Preferences, Map)
     */
    protected void constructHierarchy(
            final @NotNull ImmutableNode.Builder builder,
            final @NotNull Preferences node,
            final Map<ImmutableNode, ? super Preferences> elemRefs) {
        builder.value(getValue(node));
        constructChildren(builder, node, elemRefs);
        constructAttributes(builder, node, elemRefs);
    }

    /**
     * Configures the specified {@code ImmutableNode.Builder} with the 
     * children of the specified {@code Preferences} node.
     * <p>
     * Adds node children calling {@code constructHierarchy()} for each
     * {@code Preferences} node returned by {@code getChildren()}, adding
     * references to {@code elemRefs} if provided.
     *
     * @param builder The current {@code HierarchicalConfiguration} node builder
     * @param node The current {@code Preferences} node
     * @param elemRefs A map for assigning references objects to nodes. Can be
     * {@code null}, then reference objects are irrelevant
     * @throws ConfigurationRuntimeException If an error occurs creating the node
     * hierarchy
     * @see #getChildren(Preferences)
     */
    protected void constructChildren(
            final @NotNull ImmutableNode.Builder builder,
            final @NotNull Preferences node,
            final Map<ImmutableNode, ? super Preferences> elemRefs) {
        for (final Preferences child : getChildren(node)) {
            final ImmutableNode.Builder childBuilder = new ImmutableNode.Builder()
                    .name(getName(child));
            constructHierarchy(childBuilder, child, elemRefs);
            builder.addChild(childBuilder.create());
        }
    }

    /**
     * Configures the specified {@code ImmutableNode.Builder} with the 
     * attributes of the specified {@code Preferences} node.
     * <p>
     * Sets node attributes based on {@code getAttributes()}.
     *
     * @param builder The current {@code HierarchicalConfiguration} node builder
     * @param node The current {@code Preferences} node
     * @param elemRefs A map for assigning references objects to nodes. Can be
     * {@code null}, then reference objects are irrelevant
     * @throws ConfigurationRuntimeException If an error occurs creating the node
     * hierarchy
     * @see #getAttributes(Preferences)
     */
    protected void constructAttributes(
            final @NotNull ImmutableNode.Builder builder,
            final @NotNull Preferences node,
            final Map<ImmutableNode, ? super Preferences> elemRefs) {
        for (final Map.Entry<String, String> attr : getAttributes(node).entrySet()) {
            builder.addAttribute(attr.getKey(), attr.getValue());
        }
    }
}
