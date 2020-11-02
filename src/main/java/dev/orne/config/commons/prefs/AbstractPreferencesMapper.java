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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;
import org.apache.commons.configuration2.ex.ConversionException;
import org.apache.commons.configuration2.tree.NodeAddData;
import org.apache.commons.configuration2.tree.NodeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation of {@code PreferencesMapper}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @param <N> The {@code HierarchicalConfiguration} node type
 * @since 0.2
 */
public abstract class AbstractPreferencesMapper<N>
implements PreferencesMapper<N> {

    /** The class {@code Logger}. */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractPreferencesMapper.class);

    protected static final String READ_ERR =
            "Error reading Preferences";
    protected static final String WRITE_ERR =
            "Error writing Preferences";
    protected static final String CONV_ERR =
            "Error converting configuration value to Preferences value";

    /**
     * {@inheritDoc}
     */
    @Override
    public String resolveNodeKey(
            final @NotNull PreferencesBased<N> config,
            final @NotNull Preferences baseNode,
            final @NotNull Preferences node) {
        final String result;
        if (baseNode.absolutePath().equals(node.absolutePath())) {
            result = null;
        } else {
            final Preferences parent;
            try {
                parent = node.parent();
            } catch (final IllegalStateException ise) {
                throw new PreferencesNodeDeletedException(READ_ERR, ise);
            }
            final String parentKey = resolveNodeKey(
                    config,
                    baseNode,
                    parent);
            result = config.getExpressionEngine().nodeKey(
                    createNodeForName(getName(node)),
                    parentKey,
                    config.getNodeModel().getNodeHandler());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String resolvePropertyKey(
            final @NotNull PreferencesBased<N> config,
            final @NotNull Preferences baseNode,
            final @NotNull Preferences node,
            final @NotNull String key) {
        final String nodeKey = resolveNodeKey(
                config,
                baseNode,
                node); 
        return config.getExpressionEngine().attributeKey(nodeKey, key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProperty(
            final @NotNull PreferencesBased<N> config,
            final @NotNull Preferences baseNode,
            final @NotNull String name,
            Object value) {
        final PropertyData propertyData = getPropertyData(config, name);
        final Preferences node = resolvePreferencesNode(baseNode, propertyData.getPath());
        final String strValue = convertValue(config, value);
        if (propertyData.isAttribute()) {
            setAttribute(
                    node,
                    propertyData.getName(),
                    strValue);
        } else {
            setChildValue(
                    node,
                    propertyData.getName(),
                    strValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNodes(
            final @NotNull PreferencesBased<N> config,
            final @NotNull Preferences baseNode,
            final @NotNull String name,
            final @NotNull Collection<N> newNodes) {
        final PropertyData propertyData = getPropertyData(config, name);
        Preferences node = resolvePreferencesNode(baseNode, propertyData.getPath());
        if (!propertyData.isAttribute()) {
            final String parentNodeName = getName(createNodeForName(propertyData.getName()));
            try {
                node = node.node(parentNodeName);
            } catch (final IllegalStateException ise) {
                throw new PreferencesNodeDeletedException(WRITE_ERR, ise);
            } catch (final IllegalArgumentException iae) {
                throw new ConfigurationRuntimeException(WRITE_ERR, iae);
            }
        }
        for (final N newNode : newNodes) {
            saveChildNode(config, node, newNode);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeProperty(
            final @NotNull PreferencesBased<N> config,
            final @NotNull Preferences baseNode,
            final @NotNull String name) {
        final PropertyData propertyData = getPropertyData(config, name);
        final Preferences node = resolvePreferencesNode(baseNode, propertyData.getPath());
        if (propertyData.isAttribute()) {
            setAttribute(
                    node,
                    propertyData.getName(),
                    null);
        } else {
            setChildValue(
                    node,
                    propertyData.getName(),
                    null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNode(
            final @NotNull PreferencesBased<N> config,
            final @NotNull Preferences baseNode,
            final String name) {
        final PropertyData propertyData = getPropertyData(config, name);
        final Preferences node = resolvePreferencesNode(baseNode, propertyData.getPath());
        if (propertyData.isAttribute()) {
            setValue(node, null);
            try {
                node.removeNode();
            } catch (final IllegalStateException ignore) {
                // Thats the point
            } catch (final BackingStoreException bse) {
                throw new ConfigurationRuntimeException(WRITE_ERR, bse);
            }
        } else {
            setChildValue(node, propertyData.getName(), null);
            removeChild(node, propertyData.getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveNodeHierarchy(
            final @NotNull PreferencesBased<N> config,
            final @NotNull Preferences preferences) {
        final @NotNull N rootNode = config.getNodeModel().getNodeHandler().getRootNode();
        setValue(
                preferences,
                convertValue(config, getValue(rootNode)));
        setAttributes(
                config,
                preferences,
                getAttributes(rootNode));
        saveChildren(
                config,
                preferences,
                getChildren(rootNode));
    }

    /**
     * Creates an empty {@code HierarchicalConfiguration} node with the
     * specified node name.
     * 
     * @param name The {@code HierarchicalConfiguration} node name
     * @return The {@code HierarchicalConfiguration} node
     * @throws ConfigurationRuntimeException If an error occurs creating then
     * node
     */
    protected abstract @NotNull N createNodeForName(
            @NotNull String name);

    /**
     * Returns the {@code Preferences} name of the specified
     * {@code HierarchicalConfiguration} node.
     * 
     * @param node The {@code HierarchicalConfiguration} node
     * @return The node name
     */
    protected abstract @NotNull String getName(
            @NotNull N node);

    /**
     * Returns the {@code HierarchicalConfiguration} node name of the specified
     * {@code Preferences} node.
     * 
     * @param node The {@code Preferences} node
     * @return The node name
     * @throws ConfigurationRuntimeException If an error occurs retrieving the
     * node name
     */
    protected @NotNull String getName(
            @NotNull Preferences node) {
        return node.name();
    }

    /**
     * Returns the value of the specified {@code HierarchicalConfiguration}
     * node.
     * 
     * @param node The {@code HierarchicalConfiguration} node
     * @return The node value
     */
    protected abstract Object getValue(
            @NotNull N node);

    /**
     * Gets the node value of the specified {@code Preferences} node.
     * 
     * @param node The {@code Preferences} node
     * @return The node value
     * @throws PreferencesNodeDeletedException If the {@code Preferences} node
     * has been deleted
     * @throws ConfigurationRuntimeException If an error occurs retrieving the
     * node value
     */
    protected abstract String getValue(
            final @NotNull Preferences node);

    /**
     * Sets the node value of the specified {@code Preferences} node.
     * 
     * @param node The {@code Preferences} node
     * @param value The node value
     * @throws PreferencesNodeDeletedException If the {@code Preferences} node
     * has been deleted
     * @throws ConfigurationRuntimeException If an error occurs setting the
     * node value
     */
    protected abstract void setValue(
            final @NotNull Preferences node,
            final String value);

    /**
     * Gets the node value of the specified {@code Preferences} child node.
     * 
     * @param parent The parent {@code Preferences} node
     * @param name The target {@code Preferences} node name
     * @return The node value
     * @throws PreferencesNodeDeletedException If the parent
     * {@code Preferences} node has been deleted
     * @throws ConfigurationRuntimeException If an error occurs retrieving the
     * node value
     */
    protected abstract String getChildValue(
            final @NotNull Preferences parent,
            final @NotNull String name);

    /**
     * Sets the node value of the specified {@code Preferences} node.
     * 
     * @param parent The parent {@code Preferences} node
     * @param name The target {@code Preferences} node name
     * @param value The node value
     * @throws PreferencesNodeDeletedException If the parent
     * {@code Preferences} node has been deleted
     * @throws ConfigurationRuntimeException If an error occurs setting the
     * node value
     */
    protected abstract void setChildValue(
            final @NotNull Preferences parent,
            final @NotNull String name,
            final String value);

    /**
     * Returns the attributes names of the specified
     * {@code HierarchicalConfiguration} node.
     * 
     * @param node The {@code HierarchicalConfiguration} node
     * @return The node attributes names
     */
    protected abstract @NotNull Collection<String> getAttributesNames(
            final @NotNull N node);

    /**
     * Returns the attributes names of the specified {@code Preferences} node.
     * 
     * @param node The {@code Preferences} node
     * @return The node attributes names
     * @throws PreferencesNodeDeletedException If the {@code Preferences} node
     * has been deleted
     * @throws ConfigurationRuntimeException If an error occurs retrieving the
     * node attribute names
     */
    protected @NotNull Collection<String> getAttributesNames(
            final @NotNull Preferences node) {
        try {
            return Arrays.asList(node.keys());
        } catch (final IllegalStateException ise) {
            throw new PreferencesNodeDeletedException(READ_ERR, ise);
        } catch (final BackingStoreException bse) {
            throw new ConfigurationRuntimeException(READ_ERR, bse);
        }
    }

    /**
     * Returns the attributes of the specified
     * {@code HierarchicalConfiguration} node.
     * 
     * @param node The {@code HierarchicalConfiguration} node
     * @return The node attributes
     */
    protected abstract @NotNull Map<String, Object> getAttributes(
            final @NotNull N node);

    /**
     * Returns the attributes of the specified {@code Preferences} node.
     * 
     * @param node The {@code Preferences} node
     * @return The node attributes
     * @throws PreferencesNodeDeletedException If the {@code Preferences} node
     * has been deleted
     * @throws ConfigurationRuntimeException If an error occurs retrieving the
     * node attributes
     */
    protected @NotNull Map<String, String> getAttributes(
            final @NotNull Preferences node) {
        return getAttributesNames(node).parallelStream()
                .collect(Collectors.toMap(
                        key -> key,
                        key -> getAttribute(node, key)));
    }

    /**
     * Set the specified {@code Preferences} node preferences to represent the
     * specified attributes. Old preferences for missing attributes must be
     * deleted.
     * 
     * @param config The {@code Preferences} based {@code Configuration}
     * @param node The {@code Preferences} node
     * @param attributes The {@code HierarchicalConfiguration} node attributes
     * to set
     * @throws PreferencesNodeDeletedException If the {@code Preferences} node
     * has been deleted
     * @throws ConfigurationRuntimeException If an error occurs setting the
     * node attributes
     */
    protected void setAttributes(
            final @NotNull PreferencesBased<N> config,
            final @NotNull Preferences node,
            final @NotNull Map<String, Object> attributes) {
        final List<String> oldKeys = new ArrayList<>(getAttributesNames(node));
        for (final Map.Entry<String, Object> attr : attributes.entrySet()) {
            setAttribute(
                    node,
                    attr.getKey(),
                    convertValue(config, attr.getValue()));
            oldKeys.remove(attr.getKey());
        }
        for (final String oldKey : oldKeys) {
            setAttribute(node, oldKey, null);
        }
    }

    /**
     * Returns the node attribute of the specified
     * {@code HierarchicalConfiguration} node.
     * 
     * @param node The {@code HierarchicalConfiguration} node
     * @param name The attribute name
     * @return The node attribute value
     */
    protected abstract Object getAttribute(
            @NotNull N node,
            @NotNull String name);

    /**
     * Returns the node attribute of the specified {@code Preferences} node.
     * 
     * @param node The {@code Preferences} node
     * @param name The attribute name
     * @return The node attribute value
     * @throws PreferencesNodeDeletedException If the {@code Preferences} node
     * has been deleted
     * @throws ConfigurationRuntimeException If an error occurs retrieving the
     * node attribute
     */
    protected String getAttribute(
            @NotNull Preferences node,
            @NotNull String name) {
        return getAttribute(node, name, null);
    }

    /**
     * Returns the node attribute of the specified {@code Preferences} node.
     * 
     * @param node The {@code Preferences} node
     * @param name The attribute name
     * @param def The value to be returned in the event that this preference
     * node has no value associated for the specified attribute name
     * @return The node attribute value
     * @throws PreferencesNodeDeletedException If the {@code Preferences} node
     * has been deleted
     * @throws ConfigurationRuntimeException If an error occurs retrieving the
     * node attribute
     */
    protected String getAttribute(
            @NotNull Preferences node,
            @NotNull String name,
            String def) {
        try {
            return node.get(name, def);
        } catch (final IllegalStateException ise) {
            throw new PreferencesNodeDeletedException(READ_ERR, ise);
        }
    }

    /**
     * Sets the value of the specified {@code Preferences} node attribute.
     * 
     * @param node The {@code Preferences} node
     * @param name The attribute name
     * @param value The attribute value
     * @throws PreferencesNodeDeletedException If the {@code Preferences} node
     * has been deleted
     * @throws ConfigurationRuntimeException If an error occurs setting the
     * node attribute
     */
    protected void setAttribute(
            final @NotNull Preferences node,
            final @NotNull String name,
            final String value) {
        try {
            if (value == null) {
                node.remove(name);
            } else {
                node.put(name, value);
            }
        } catch (final IllegalArgumentException ise) {
            throw new ConfigurationRuntimeException(WRITE_ERR, ise);
        } catch (final IllegalStateException ise) {
            throw new PreferencesNodeDeletedException(WRITE_ERR, ise);
        }
    }

    /**
     * Returns the child node names of the specified
     * {@code HierarchicalConfiguration} node.
     * 
     * @param node The {@code HierarchicalConfiguration} node
     * @return The child nodes
     */
    protected abstract @NotNull Collection<String> getChildrenNames(
            @NotNull N node);

    /**
     * Returns the child node names of the specified {@code Preferences} node.
     * 
     * @param node The {@code Preferences} node
     * @return The child nodes
     * @throws PreferencesNodeDeletedException If the {@code Preferences} node
     * has been deleted
     * @throws ConfigurationRuntimeException If an error occurs retrieving the
     * node children names
     */
    protected @NotNull Collection<String> getChildrenNames(
            @NotNull Preferences node) {
        try {
            return Arrays.asList(node.childrenNames());
        } catch (final IllegalStateException ise) {
            throw new PreferencesNodeDeletedException(READ_ERR, ise);
        } catch (final BackingStoreException bse) {
            throw new ConfigurationRuntimeException(READ_ERR, bse);
        }
    }

    /**
     * Returns the child nodes of the specified
     * {@code HierarchicalConfiguration} node.
     * 
     * @param node The {@code HierarchicalConfiguration} node
     * @return The child nodes
     */
    protected abstract @NotNull Collection<N> getChildren(
            @NotNull N node);

    /**
     * Returns the child nodes of the specified {@code Preferences} node.
     * 
     * @param node The {@code Preferences} node
     * @return The child nodes
     * @throws PreferencesNodeDeletedException If the {@code Preferences} node
     * has been deleted
     * @throws ConfigurationRuntimeException If an error occurs retrieving the
     * node children
     */
    protected @NotNull Collection<Preferences> getChildren(
            @NotNull Preferences node) {
        final Collection<String> names = getChildrenNames(node);
        final List<Preferences> result = new ArrayList<>(names.size());
        for (final String name : names) {
            try {
                result.add(node.node(name));
            } catch (final IllegalStateException ise) {
                throw new PreferencesNodeDeletedException(READ_ERR, ise);
            }
        }
        return result;
    }

    /**
     * Removes the specified child node from the specified {@code Preferences}
     * node.
     * 
     * @param node The {@code Preferences} node
     * @param name The child node name
     * @throws PreferencesNodeDeletedException If the {@code Preferences} node
     * has been deleted
     * @throws ConfigurationRuntimeException If an error occurs removing the
     * child node
     */
    protected void removeChild(
            final @NotNull Preferences node,
            final @NotNull String name) {
        try {
            if (node.nodeExists(name)) {
                node.node(name).removeNode();
            }
        } catch (final IllegalStateException ise) {
            throw new PreferencesNodeDeletedException(WRITE_ERR, ise);
        } catch (final IllegalArgumentException | BackingStoreException e) {
            throw new ConfigurationRuntimeException(WRITE_ERR, e);
        }
    }

    /**
     * Saves the specified {@code HierarchicalConfiguration} nodes as child
     * nodes of the specified {@code Preferences} node. Old child nodes
     * must be deleted.
     * 
     * @param config The {@code Preferences} based {@code Configuration}
     * @param parent The {@code Preferences} node
     * @param childs The {@code HierarchicalConfiguration} child nodes
     * @throws PreferencesNodeDeletedException If the parent
     * {@code Preferences} node has been deleted
     * @throws ConfigurationRuntimeException If an error occurs saving the
     * node children
     */
    protected void saveChildren(
            final @NotNull PreferencesBased<N> config,
            final @NotNull Preferences parent,
            final @NotNull Collection<N> childs) {
        final List<String> oldChilds = new ArrayList<>(getChildrenNames(parent));
        for (final N child : childs) {
            saveChildNode(config, parent, child);
            oldChilds.remove(getName(child));
        }
        for (final String oldChild : oldChilds) {
            removeChild(parent, oldChild);
        }
    }

    /**
     * Saves the specified {@code HierarchicalConfiguration} node as child
     * node of the specified {@code Preferences} node.
     * 
     * @param config The {@code Preferences} based {@code Configuration}
     * @param parent The {@code Preferences} node
     * @param child The {@code HierarchicalConfiguration} child node
     * @throws PreferencesNodeDeletedException If the parent
     * {@code Preferences} node has been deleted
     * @throws ConfigurationRuntimeException If an error occurs saving the
     * child node
     */
    protected void saveChildNode(
            final @NotNull PreferencesBased<N> config,
            final @NotNull Preferences parent,
            final @NotNull N child) {
        final String childName = getName(child);
        setChildValue(
                parent,
                childName,
                convertValue(config, getValue(child)));
        final Map<String, Object> attrs = getAttributes(child);
        final Collection<N> grandchilds = getChildren(child);
        try {
            if (!attrs.isEmpty() || !grandchilds.isEmpty() || parent.nodeExists(childName)) {
                final Preferences childNode = parent.node(childName);
                setAttributes(
                        config,
                        childNode,
                        getAttributes(child));
                saveChildren(
                        config,
                        childNode,
                        getChildren(child));
            }
        } catch (final IllegalStateException ise) {
            throw new PreferencesNodeDeletedException(WRITE_ERR, ise);
        } catch (final IllegalArgumentException | BackingStoreException e) {
            throw new ConfigurationRuntimeException(WRITE_ERR, e);
        }
    }

    /**
     * Converts the {@code HierarchicalConfiguration} value to a
     * {@code Preferences} value.
     * 
     * @param config The {@code Preferences} based {@code Configuration}
     * @param value The {@code HierarchicalConfiguration} value
     * @return The {@code Preferences} value
     * possible
     */
    protected String convertValue(
            final @NotNull PreferencesBased<N> config,
            final Object value) {
        try {
            return config.getConversionHandler().to(value, String.class, null);
        } catch (final ConversionException ce) {
            LOG.warn(CONV_ERR, ce);
            return value == null ? null : value.toString();
        }
    }

    /**
     * Returns the data of the specified {@code HierarchicalConfiguration}
     * property.
     * 
     * @param config The {@code Preferences} based {@code Configuration}
     * @param property The {@code HierarchicalConfiguration} property
     * @return The data of the property in the {@code HierarchicalConfiguration}
     */
    protected PropertyData getPropertyData(
            final @NotNull PreferencesBased<N> config,
            final @NotNull String property) {
        final NodeHandler<N> handler = config.getNodeModel().getNodeHandler();
        final NodeAddData<N> addData = config.getExpressionEngine().prepareAdd(
                handler.getRootNode(),
                property,
                handler);
        final List<String> path = new ArrayList<>(
                getNodePath(handler, addData.getParent()));
        path.addAll(addData.getPathNodes());
        return new PropertyData(
                path,
                addData.getNewNodeName(),
                addData.isAttribute());
    }

    /**
     * Returns the names of the node path to the specified
     * {@code HierarchicalConfiguration} node, relative to
     * {@code HierarchicalConfiguration}'s root node.
     * 
     * @param handler The {@code HierarchicalConfiguration}'s node handler
     * @param node The {@code HierarchicalConfiguration} node
     * @return The names of the node path
     */
    protected @NotNull List<String> getNodePath(
            final @NotNull NodeHandler<N> handler,
            final N node) {
        final List<String> result;
        if (node.equals(handler.getRootNode())) {
            result = new ArrayList<>();
        } else {
            result = getNodePath(
                    handler,
                    handler.getParent(node));
            result.add(getName(node));
        }
        return result;
    }

    /**
     * Resolves the {@code Preferences} node from the specified node names
     * relative to the specified {@code Preferences} node.
     * 
     * @param baseNode The base {@code Preferences} node
     * @param names The node names, relative to the base {@code Preferences} node
     * @return The resolved {@code Preferences} node
     * @throws PreferencesNodeDeletedException If the {@code Preferences} node
     * has been deleted
     * @throws ConfigurationRuntimeException If an error occurs resolving the
     * node
     */
    protected @NotNull Preferences resolvePreferencesNode(
            final @NotNull Preferences baseNode,
            final @NotNull String... names) {
        return resolvePreferencesNode(baseNode, Arrays.asList(names));
    }

    /**
     * Resolves the {@code Preferences} node from the specified node path
     * relative to the specified {@code Preferences} node.
     * 
     * @param baseNode The base {@code Preferences} node
     * @param path The node path, relative to the base {@code Preferences} node
     * @return The resolved {@code Preferences} node
     * @throws PreferencesNodeDeletedException If the {@code Preferences} node
     * has been deleted
     * @throws ConfigurationRuntimeException If an error occurs resolving the
     * node
     */
    protected @NotNull Preferences resolvePreferencesNode(
            final @NotNull Preferences baseNode,
            final @NotNull Collection<String> path) {
        try {
            final Iterator<String> it = path.iterator();
            Preferences node = baseNode;
            while (it.hasNext()) {
                node = node.node(it.next());
            }
            return node;
        } catch (final IllegalStateException ise) {
            throw new PreferencesNodeDeletedException(READ_ERR, ise);
        } catch (final IllegalArgumentException iae) {
            throw new ConfigurationRuntimeException(READ_ERR, iae);
        }
    }

    /**
     * Bean with the data of a {@code HierarchicalConfiguration} property.
     * <p>
     * Contains:
     * <ul>
     * <li>The names of the node path to the parent node of the property's
     * final target, relative to {@code HierarchicalConfiguration}'s root
     * node.</li>
     * <li>The name of the property's final target.</li>
     * <li>If the property's final target is an attribute.</li>
     * </ul>
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0
     * @since 0.2
     */
    protected static class PropertyData {

        /**
         * The names of the node path to the parent node of the property's
         * final target.
         */
        private final @NotNull List<String> path;
        /** The name of the property's final target. */
        private final String name;
        /** If the property's final target is an attribute. */
        private final boolean attribute;

        /**
         * Creates a new instance.
         * 
         * @param path
         * @param name
         * @param attribute
         */
        public PropertyData(
                final @NotNull List<String> path,
                final String name,
                final boolean attribute) {
            super();
            this.path = path;
            this.name = name;
            this.attribute = attribute;
        }

        /**
         * Returns names of the node path to the parent node of the property's
         * final target.
         * 
         * @return The names of the node path to the parent node of the
         * property's final target
         */
        public List<String> getPath() {
            return Collections.unmodifiableList(this.path);
        }

        /**
         * Returns the name of the property's final target.
         * 
         * @return The name of the property's final target
         */
        public String getName() {
            return this.name;
        }

        /**
         * Returns {@code true} if the property's final target is an attribute.
         * 
         * @return If the property's final target is an attribute
         */
        public boolean isAttribute() {
            return this.attribute;
        }
    }
}
