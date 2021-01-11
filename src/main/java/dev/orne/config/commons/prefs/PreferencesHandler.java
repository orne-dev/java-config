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
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.NodeChangeEvent;
import java.util.prefs.NodeChangeListener;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.builder.BuilderParameters;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.configuration2.event.EventListener;
import org.apache.commons.configuration2.event.EventSource;
import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that handles the conversion and communication between
 * {@code Preferences} nodes and a {@code HierarchicalConfiguration} instance.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @param <N> The {@code HierarchicalConfiguration} node type
 * @since 0.2
 */
public class PreferencesHandler<N> {

    /** The class {@code Logger}. */
    private static final Logger LOG = LoggerFactory.getLogger(PreferencesHandler.class);

    /** Prefix for configuration properties. */
    public static final String PROPERTY_PREFIX =
            BuilderParameters.RESERVED_PARAMETER_PREFIX + "PreferencesHandler-";
    /** Property key for the {@code Preferences} tree. */
    public static final String PROP_SYSTEM_TREE = PROPERTY_PREFIX + "systemTree";
    /** Property key for the base class. */
    public static final String PROP_BASE_CLASS = PROPERTY_PREFIX + "baseClass";
    /** Property key for the base path. */
    public static final String PROP_PATH = PROPERTY_PREFIX + "basePath";
    /** Property key for the base node. */
    public static final String PROP_BASE_NODE = PROPERTY_PREFIX + "baseNode";
    /** Property key the automatic configuration update flag. */
    public static final String PROP_AUTO_LOAD = PROPERTY_PREFIX + "autoLoad";
    /** Property key the automatic configuration save flag. */
    public static final String PROP_AUTO_SAVE = PROPERTY_PREFIX + "autoSave";
    /** Property key the event coordinator strategy. */
    public static final String PROP_EVENT_COORDINATOR = PROPERTY_PREFIX + "eventCoordinationStrategy";

    /** Error message for auto load errors. */
    private static final String AUTO_LOAD_ERR = "Error auto loading preferences changes";
    /** Error message for auto save errors. */
    private static final String AUTO_SAVE_ERR = "Error auto saving configuration changes";

    /** The node and property mapper. */
    private final @NotNull PreferencesMapper<N> mapper;
    /** The event coordination strategy. */
    private final @NotNull EventCoordinationStrategy eventCoordinationStrategy;
    /** The listeners manager. */
    private final @NotNull EventListenerManager eventListenerManager;
    /** The base {@code Preferences} node. */
    private Preferences baseNode;
    /** The handled {@code PreferencesBased} configuration.*/
    private PreferencesBased<N> content;
    /** If auto save mode is currently active. */
    private boolean autoSave;
    /** If auto load mode is currently active. */
    private boolean autoLoad;

    /**
     * Creates a new instance with a {@code ByThreadEventCoordinationStrategy}
     * event coordinator strategy.
     * 
     * @param mapper The node and property mapper
     */
    public PreferencesHandler(
            final @NotNull PreferencesMapper<N> mapper) {
        this(mapper, new ByThreadEventCoordinationStrategy());
    }

    /**
     * Creates a new instance.
     * 
     * @param mapper The node and property mapper
     * @param coordinationStrategy The event coordination strategy
     */
    protected PreferencesHandler(
            final @NotNull PreferencesMapper<N> mapper,
            final @NotNull EventCoordinationStrategy coordinationStrategy) {
        this(mapper, coordinationStrategy, null);
    }

    /**
     * Creates a new instance.
     * 
     * @param mapper The node and property mapper
     * @param coordinationStrategy The event coordination strategy
     * @param listenerManager The listeners manager
     */
    protected PreferencesHandler(
            final @NotNull PreferencesMapper<N> mapper,
            final @NotNull EventCoordinationStrategy coordinationStrategy,
            final EventListenerManager listenerManager) {
        super();
        Validate.notNull(mapper);
        Validate.notNull(coordinationStrategy);
        this.mapper = mapper;
        this.eventCoordinationStrategy = coordinationStrategy;
        if (listenerManager == null) {
            this.eventListenerManager = new EventListenerManager();
        } else {
            this.eventListenerManager = listenerManager;
        }
    }

    /**
     * Creates and configured a new {@code PreferencesHandler} instance,
     * using the values of the parameters map for configuration.
     * 
     * @param <N> The {@code HierarchicalConfiguration} node type
     * @param map The parameters map
     * @param mapper The node and property mapper 
     * @return The configured {@code PreferencesHandler} instance
     */
    public static <N> @NotNull PreferencesHandler<N> fromMap(
            final Map<String, ?> map,
            final @NotNull PreferencesMapper<N> mapper) {
        final @NotNull PreferencesHandler<N> handler;
        if (map == null) {
            handler = new PreferencesHandler<>(mapper);
        } else {
            final EventCoordinationStrategy coordinator;
            if (map.containsKey(PROP_EVENT_COORDINATOR)) {
                coordinator = (EventCoordinationStrategy) map.get(PROP_EVENT_COORDINATOR);
            } else {
                coordinator = new ByThreadEventCoordinationStrategy();
            }
            handler = new PreferencesHandler<>(mapper, coordinator);
            if (map.containsKey(PROP_BASE_NODE)) {
                handler.setBaseNode((Preferences) map.get(PROP_BASE_NODE));
            } else {
                handler.setBaseNode(
                        ObjectUtils.defaultIfNull(
                            (Boolean) map.get(PROP_SYSTEM_TREE),
                            false),
                        (Class<?>) map.get(PROP_BASE_CLASS),
                        (String) map.get(PROP_PATH));
            }
            handler.setAutoLoad(ObjectUtils.defaultIfNull(
                    (Boolean) map.get(PROP_AUTO_LOAD),
                    false));
            handler.setAutoSave(ObjectUtils.defaultIfNull(
                    (Boolean) map.get(PROP_AUTO_SAVE),
                    false));
        }
        return handler;
    }

    /**
     * Returns the {@code Preferences} to {@code HierarchicalConfiguration}
     * node mapper.
     * 
     * @return The node an property mapper
     */
    protected @NotNull PreferencesMapper<N> getMapper() {
        return this.mapper;
    }

    /**
     * Returns the event coordination strategy.
     * 
     * @return The event coordination strategy
     */
    protected @NotNull EventCoordinationStrategy getEventCoordinationStrategy() {
        return this.eventCoordinationStrategy;
    }

    /**
     * Returns the {@code Preferences} and {@code PreferencesBased} event
     * listeners manager.
     * 
     * @return The event listeners manager
     */
    protected @NotNull EventListenerManager getEventListenerManager() {
        return this.eventListenerManager;
    }

    /**
     * Returns the base {@code Preferences} node.
     * 
     * @return The base {@code Preferences} node
     */
    public synchronized Preferences getBaseNode() {
        return this.baseNode;
    }

    /**
     * Sets the base {@code Preferences} node.
     * 
     * @param baseNode The base {@code Preferences} node
     */
    public synchronized void setBaseNode(
            final Preferences baseNode) {
        if (this.baseNode != null && this.autoLoad) {
            this.eventListenerManager.removePreferencesListeners(this.baseNode);
        }
        this.baseNode = baseNode;
        if (this.baseNode != null && this.autoLoad) {
            this.eventListenerManager.installPreferencesListeners(this.baseNode);
        }
    }

    /**
     * Returns the base {@code Preferences} node, ensuring that it is set and
     * not {@code null}.
     * 
     * @return The base {@code Preferences} node
     */
    protected synchronized Preferences checkBaseNode() {
        if (this.baseNode == null) {
            throw new IllegalStateException("The base Preferences node has not been set.");
        }
        return this.baseNode;
    }

    /**
     * Sets the base {@code Preferences} node, based in the provided
     * parameters.
     * <p>
     * 
     * @param system If the base node should be resolved based in system root
     * {@code Preferences} node ({@code true}) or the user root
     * {@code Preferences} node
     * @param clazz The class whose package use as base node path (can be
     * {@code null})
     * @param path The path of the base node (can be
     * {@code null})
     */
    public synchronized void setBaseNode(
            final boolean system,
            final Class<?> clazz,
            final String path) {
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
        setBaseNode(node);
    }

    /**
     * Returns the handled {@code PreferencesBased} configuration.
     * 
     * @return The handled {@code PreferencesBased} configuration
     */
    public synchronized PreferencesBased<N> getContent() {
        return this.content;
    }

    /**
     * Sets the handled {@code PreferencesBased} configuration.
     * 
     * @param content The handled {@code PreferencesBased} configuration
     */
    public synchronized void setContent(
            final PreferencesBased<N> content) {
        if (this.content != null && this.autoSave) {
            this.eventListenerManager.removeConfigurationListeners(this.content);
        }
        this.content = content;
        if (this.content != null && this.autoSave) {
            this.eventListenerManager.installConfigurationListeners(this.content);
        }
    }

    /**
     * Returns the handled {@code PreferencesBased} configuration, ensuring
     * that it is set and not {@code null}.
     * 
     * @return The handled {@code PreferencesBased} configuration
     */
    protected synchronized PreferencesBased<N> checkContent() {
        if (this.content == null) {
            throw new IllegalStateException("The PreferenceBased configuration has not been set.");
        }
        return this.content;
    }

    /**
     * Returns a flag whether auto save mode is currently active.
     *
     * @return <b>true</b> if auto save is enabled, <b>false</b> otherwise
     */
    public synchronized boolean isAutoSave() {
        return this.autoSave;
    }

    /**
     * Checks whether an auto save operation has to be performed based on the
     * passed in event and the current state of this object.
     *
     * @param event the configuration change event
     * @return <b>true</b> if a save operation should be performed, <b>false</b>
     *         otherwise
     */
    protected synchronized boolean autoSaveRequired(
            final ConfigurationEvent event) {
        return this.autoSave &&
                this.baseNode != null &&
                this.content != null;
    }

    /**
     * Enables or disables auto save mode. If auto save mode is enabled, every
     * update of the managed configuration causes it to be saved automatically;
     * so changes are directly written to disk.
     *
     * @param enabled <b>true</b> if auto save mode is to be enabled,
     *        <b>false</b> otherwise
     */
    public synchronized void setAutoSave(final boolean enabled) {
        if (this.content != null && this.autoSave != enabled) {
            if (enabled) {
                this.eventListenerManager.installConfigurationListeners(this.content);
            } else {
                this.eventListenerManager.removeConfigurationListeners(this.content);
            }
        }
        this.autoSave = enabled;
    }

    /**
     * Returns a flag whether auto load mode is currently active.
     *
     * @return <b>true</b> if auto load is enabled, <b>false</b> otherwise
     */
    public synchronized boolean isAutoLoad() {
        return this.autoLoad;
    }

    /**
     * Checks whether an auto save operation has to be performed based on the
     * passed in event and the current state of this object.
     *
     * @param event The Preferences change event
     * @return <b>true</b> if a save operation should be performed, <b>false</b>
     *         otherwise
     */
    protected synchronized boolean autoLoadRequired(
            final EventObject event) {
        return this.autoLoad &&
                this.baseNode != null &&
                this.content != null;
    }

    /**
     * Enables or disables auto save mode. If auto save mode is enabled, every
     * update of the managed configuration causes it to be saved automatically;
     * so changes are directly written to disk.
     *
     * @param enabled <b>true</b> if auto save mode is to be enabled,
     *        <b>false</b> otherwise
     */
    public synchronized void setAutoLoad(final boolean enabled) {
        if (this.baseNode != null && this.autoLoad != enabled) {
            if (enabled) {
                this.eventListenerManager.installPreferencesListeners(this.baseNode);
            } else {
                this.eventListenerManager.removePreferencesListeners(this.baseNode);
            }
        }
        this.autoLoad = enabled;
    }

    /**
     * Synchronizes the {@code Preferences} base node with changes performed
     * by other threads.
     */
    protected void synchronizeBaseNode() {
        try {
            checkBaseNode().sync();
        } catch (final BackingStoreException bse) {
            throw new ConfigurationRuntimeException(
                    "Error synchronizing Preferences base node",
                    bse);
        }
    }

    /**
     * Flushes changed made to the {@code Preferences} base node.
     */
    protected void flushBaseNode() {
        try {
            checkBaseNode().flush();
        } catch (final BackingStoreException bse) {
            throw new ConfigurationRuntimeException(
                    "Error flushing Preferences base node",
                    bse);
        }
    }

    /**
     * Loads the content of the {@code Preferences} base node in
     * the handled {@code PreferencesBased} instance.
     */
    public synchronized void load() {
        getEventCoordinationStrategy().preventEvents(() -> {
            checkBaseNode();
            checkContent();
            synchronizeBaseNode();
            final Map<N, Object> elemRefMap = new HashMap<>();
            final N newRoot = this.mapper.loadNodeHierarchy(
                    checkBaseNode(),
                    elemRefMap);
            this.content.load(newRoot);
        });
    }

    /**
     * Refresh the content of the handled {@code PreferencesBased} instance
     * with the updated content of the {@code Preferences} base node.
     */
    public synchronized void refresh() {
        getEventCoordinationStrategy().preventEvents(() -> {
            checkBaseNode();
            checkContent().clear();
            load();
        });
    }

    /**
     * Saves the configuration to the base {@code Preferences} node set.
     * The {@code Preferences} node is cleared and only the configuration
     * content is kept.
     * 
     * @throws ConfigurationRuntimeException If an error occurs saving the
     * {@code Preferences} content
     */
    public synchronized void save() {
        getEventCoordinationStrategy().preventEvents(() -> {
            synchronizeBaseNode();
            this.mapper.saveNodeHierarchy(
                    checkContent(),
                    checkBaseNode());
        });
    }

    /**
     * Callback for {@code Preferences} properties changed events.
     * 
     * @param event The event to handle
     */
    protected void onPreferenceChangeEvent(
            final @NotNull PreferenceChangeEvent event) {
        getEventCoordinationStrategy().handlePreferencesEvent(
                event,
                PreferencesHandler.this::handlePreferenceChangeEvent);
    }

    /**
     * Handler for {@code Preferences} properties changed events.
     * 
     * @param event The event to handle
     */
    protected void handlePreferenceChangeEvent(
            final @NotNull PreferenceChangeEvent event) {
        if (autoLoadRequired(event)) {
            try {
                final String key = this.mapper.resolvePropertyKey(
                        checkContent(),
                        this.baseNode,
                        event.getNode(),
                        event.getKey());
                this.content.setProperty(key, event.getNewValue());
            } catch (final RuntimeException re) {
                LOG.error(AUTO_LOAD_ERR, re);
            }
        }
    }

    /**
     * Callback for {@code Preferences} node added events.
     * 
     * @param event The event to handle
     */
    protected void onPreferenceNodeAddedEvent(
            final @NotNull NodeChangeEvent event) {
        getEventCoordinationStrategy().handlePreferencesEvent(
                event,
                PreferencesHandler.this::handlePreferenceNodeAddedEvent);
    }

    /**
     * Handler for {@code Preferences} node added events.
     * 
     * @param event The event to handle
     */
    protected void handlePreferenceNodeAddedEvent(
            final @NotNull NodeChangeEvent event) {
        if (autoLoadRequired(event)) {
            try {
                final String key = this.mapper.resolveNodeKey(
                        this.content,
                        this.baseNode,
                        event.getParent());
                final N node = this.mapper.loadNodeHierarchy(
                        event.getChild(),
                        null);
                this.content.addNodes(key, Collections.singletonList(node));
            } catch (final RuntimeException re) {
                LOG.error(AUTO_LOAD_ERR, re);
            }
        }
    }

    /**
     * Callback for {@code Preferences} node removed events.
     * 
     * @param event The event to handle
     */
    protected void onPreferenceNodeRemovedEvent(
            final @NotNull NodeChangeEvent event) {
        getEventCoordinationStrategy().handlePreferencesEvent(
                event,
                PreferencesHandler.this::handlePreferenceNodeRemovedEvent);
    }

    /**
     * Handler for {@code Preferences} node removed events.
     * 
     * @param event The event to handle
     */
    protected void handlePreferenceNodeRemovedEvent(
            final @NotNull NodeChangeEvent event) {
        if (autoLoadRequired(event)) {
            try {
                final String key = this.mapper.resolveNodeKey(
                        this.content,
                        this.baseNode,
                        event.getChild());
                this.content.clearTree(key);
            } catch (final RuntimeException re) {
                LOG.error(AUTO_LOAD_ERR, re);
            }
        }
    }

    /**
     * Callback for {@code PreferencesBased} property added events.
     * 
     * @param event The event to handle
     */
    protected void onConfigurationAddPropertyEvent(
            final @NotNull ConfigurationEvent event) {
        getEventCoordinationStrategy().handleConfigurationEvent(
                event,
                this::handleConfigurationSetPropertyEvent);
    }

    /**
     * Callback for {@code PreferencesBased} property changed events.
     * 
     * @param event The event to handle
     */
    protected void onConfigurationSetPropertyEvent(
            final @NotNull ConfigurationEvent event) {
        getEventCoordinationStrategy().handleConfigurationEvent(
                event,
                this::handleConfigurationSetPropertyEvent);
    }

    /**
     * Handler for {@code PreferencesBased} property value set events.
     * 
     * @param event The event to handle
     */
    protected void handleConfigurationSetPropertyEvent(
            final @NotNull ConfigurationEvent event) {
        if (!event.isBeforeUpdate() && autoSaveRequired(event)) {
            try {
                this.mapper.setProperty(
                        this.content,
                        this.baseNode,
                        event.getPropertyName(),
                        event.getPropertyValue());
                flushBaseNode();
            } catch (final RuntimeException re) {
                LOG.error(AUTO_SAVE_ERR, re);
            }
        }
    }

    /**
     * Callback for {@code PreferencesBased} property removed events.
     * 
     * @param event The event to handle
     */
    protected void onConfigurationClearPropertyEvent(
            final @NotNull ConfigurationEvent event) {
        getEventCoordinationStrategy().handleConfigurationEvent(
                event,
                this::handleConfigurationClearPropertyEvent);
    }

    /**
     * Handler for {@code PreferencesBased} property removed events.
     * 
     * @param event The event to handle
     */
    protected void handleConfigurationClearPropertyEvent(
            final @NotNull ConfigurationEvent event) {
        if (event.isBeforeUpdate() && autoSaveRequired(event)) {
            try {
                this.mapper.removeProperty(
                        this.content,
                        this.baseNode,
                        event.getPropertyName());
                flushBaseNode();
            } catch (final RuntimeException re) {
                LOG.error(AUTO_SAVE_ERR, re);
            }
        }
    }

    /**
     * Callback for {@code PreferencesBased} nodes added events.
     * 
     * @param event The event to handle
     */
    protected void onConfigurationAddNodesEvent(
            final @NotNull ConfigurationEvent event) {
        getEventCoordinationStrategy().handleConfigurationEvent(
                event,
                this::handleConfigurationAddNodesEvent);
    }

    /**
     * Handler for {@code PreferencesBased} nodes added events.
     * 
     * @param event The event to handle
     */
    protected void handleConfigurationAddNodesEvent(
            final @NotNull ConfigurationEvent event) {
        if (!event.isBeforeUpdate() && autoSaveRequired(event)) {
            try {
                @SuppressWarnings("unchecked")
                final Collection<N> newNodes =
                    (Collection<N>) event.getPropertyValue();
                this.mapper.addNodes(
                        this.content,
                        this.baseNode,
                        event.getPropertyName(),
                        newNodes);
                flushBaseNode();
            } catch (final RuntimeException re) {
                LOG.error(AUTO_SAVE_ERR, re);
            }
        }
    }

    /**
     * Callback for {@code PreferencesBased} node removed events.
     * 
     * @param event The event to handle
     */
    protected void onConfigurationClearTreeEvent(
            final @NotNull ConfigurationEvent event) {
        getEventCoordinationStrategy().handleConfigurationEvent(
                event,
                this::handleConfigurationClearTreeEvent);
    }

    /**
     * Handler for {@code PreferencesBased} node removed events.
     * 
     * @param event The event to handle
     */
    protected void handleConfigurationClearTreeEvent(
            final @NotNull ConfigurationEvent event) {
        if (event.isBeforeUpdate() && autoSaveRequired(event)) {
            try {
                this.mapper.removeNode(
                        this.content,
                        this.baseNode,
                        event.getPropertyName());
                flushBaseNode();
            } catch (final RuntimeException re) {
                LOG.error(AUTO_SAVE_ERR, re);
            }
        }
    }

    /**
     * Callback for {@code PreferencesBased} cleared events.
     * 
     * @param event The event to handle
     */
    protected void onConfigurationClearEvent(
            final @NotNull ConfigurationEvent event) {
        getEventCoordinationStrategy().handleConfigurationEvent(
                event,
                this::handleConfigurationClearEvent);
    }

    /**
     * Handler for {@code PreferencesBased} cleared events.
     * 
     * @param event The event to handle
     */
    protected void handleConfigurationClearEvent(
            final @NotNull ConfigurationEvent event) {
        if (event.isBeforeUpdate() && autoSaveRequired(event)) {
            try {
                this.mapper.removeNode(
                        this.content,
                        this.baseNode,
                        null);
                flushBaseNode();
            } catch (final RuntimeException re) {
                LOG.error(AUTO_SAVE_ERR, re);
            }
        }
    }

    /**
     * Manager for {@code Preferences} and {@code PreferencesBased} event
     * listeners.
     * <p>
     * Should delegate event management in {@code PreferencesHandler} instance.
     * 
     * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
     * @version 1.0
     * @since 0.2
     */
    protected class EventListenerManager {

        /** The {@code Preferences} properties change listener. */
        private final @NotNull PreferenceChangeListener onPreferencePropertyChange;
        /** The {@code Preferences} nodes change listener. */
        private final @NotNull NodeChangeListener onPreferencesNodeChange;
        /** The {@code Configuration} properties add listener. */
        private final @NotNull EventListener<ConfigurationEvent> onConfigurationPropertyAdd;
        /** The {@code Configuration} properties set listener. */
        private final @NotNull EventListener<ConfigurationEvent> onConfigurationPropertySet;
        /** The {@code Configuration} properties clear listener. */
        private final @NotNull EventListener<ConfigurationEvent> onConfigurationPropertyClear;
        /** The {@code HierarchicalConfiguration} nodes add listener. */
        private final @NotNull EventListener<ConfigurationEvent> onConfigurationNodesAdd;
        /** The {@code HierarchicalConfiguration} nodes clear listener. */
        private final @NotNull EventListener<ConfigurationEvent> onConfigurationNodeClear;
        /** The {@code HierarchicalConfiguration} clear listener. */
        private final @NotNull EventListener<ConfigurationEvent> onConfigurationClear;

        /**
         * Creates a new instance.
         */
        public EventListenerManager() {
            this.onPreferencePropertyChange = createOnPreferencePropertyChange();
            this.onPreferencesNodeChange = createOnPreferenceNodeChange();
            this.onConfigurationPropertyAdd = createOnConfigurationPropertyAdd();
            this.onConfigurationPropertySet = createOnConfigurationPropertySet();
            this.onConfigurationPropertyClear = createOnConfigurationPropertyClear();
            this.onConfigurationNodesAdd = createOnConfigurationNodesAdd();
            this.onConfigurationNodeClear = createOnConfigurationNodeClear();
            this.onConfigurationClear = createOnConfigurationClear();
        }

        /**
         * Creates the {@code Preferences} properties change listener.
         * 
         * @return The {@code Preferences} properties change listener
         */
        protected @NotNull PreferenceChangeListener createOnPreferencePropertyChange() {
            return PreferencesHandler.this::onPreferenceChangeEvent;
        }

        /**
         * Returns the {@code Preferences} properties change listener.
         * 
         * @return The {@code Preferences} properties change listener
         */
        public @NotNull PreferenceChangeListener getOnPreferencePropertyChange() {
            return this.onPreferencePropertyChange;
        }

        /**
         * Creates the {@code Preferences} nodes change listener.
         * 
         * @return The {@code Preferences} nodes change listener
         */
        protected @NotNull NodeChangeListener createOnPreferenceNodeChange() {
            return new NodeChangeListener() {
                @Override
                public void childAdded(final @NotNull NodeChangeEvent event) {
                    PreferencesHandler.this.onPreferenceNodeAddedEvent(event);
                }
                @Override
                public void childRemoved(final @NotNull NodeChangeEvent event) {
                    PreferencesHandler.this.onPreferenceNodeRemovedEvent(event);
                }
            };
        }

        /**
         * Returns the {@code Preferences} nodes change listener.
         * 
         * @return The {@code Preferences} nodes change listener
         */
        public @NotNull NodeChangeListener getOnPreferencesNodeChange() {
            return this.onPreferencesNodeChange;
        }

        /**
         * Creates the {@code Configuration} properties add listener.
         * 
         * @return The {@code Configuration} properties add listener
         */
        protected @NotNull EventListener<ConfigurationEvent> createOnConfigurationPropertyAdd() {
            return PreferencesHandler.this::onConfigurationAddPropertyEvent;
        }

        /**
         * Returns the {@code Configuration} properties add listener.
         * 
         * @return The {@code Configuration} properties add listener
         */
        public @NotNull EventListener<ConfigurationEvent> getOnConfigurationPropertyAdd() {
            return this.onConfigurationPropertyAdd;
        }

        /**
         * Creates the {@code Configuration} properties set listener.
         * 
         * @return The {@code Configuration} properties set listener
         */
        protected @NotNull EventListener<ConfigurationEvent> createOnConfigurationPropertySet() {
            return PreferencesHandler.this::onConfigurationSetPropertyEvent;
        }

        /**
         * Returns the {@code Configuration} properties set listener.
         * 
         * @return The {@code Configuration} properties set listener
         */
        public @NotNull EventListener<ConfigurationEvent> getOnConfigurationPropertySet() {
            return this.onConfigurationPropertySet;
        }

        /**
         * Creates the {@code Configuration} properties clear listener.
         * 
         * @return The {@code Configuration} properties clear listener
         */
        protected @NotNull EventListener<ConfigurationEvent> createOnConfigurationPropertyClear() {
            return PreferencesHandler.this::onConfigurationClearPropertyEvent;
        }

        /**
         * Returns the {@code Configuration} properties clear listener.
         * 
         * @return The {@code Configuration} properties clear listener
         */
        public @NotNull EventListener<ConfigurationEvent> getOnConfigurationPropertyClear() {
            return this.onConfigurationPropertyClear;
        }

        /**
         * Creates the {@code HierarchicalConfiguration} nodes add listener.
         * 
         * @return The {@code HierarchicalConfiguration} nodes add listener
         */
        protected @NotNull EventListener<ConfigurationEvent> createOnConfigurationNodesAdd() {
            return PreferencesHandler.this::onConfigurationAddNodesEvent;
        }

        /**
         * Returns the {@code HierarchicalConfiguration} nodes add listener.
         * 
         * @return The {@code HierarchicalConfiguration} nodes add listener
         */
        public @NotNull EventListener<ConfigurationEvent> getOnConfigurationNodesAdd() {
            return this.onConfigurationNodesAdd;
        }

        /**
         * Creates the {@code HierarchicalConfiguration} nodes clear listener.
         * 
         * @return The {@code HierarchicalConfiguration} nodes clear listener
         */
        protected @NotNull EventListener<ConfigurationEvent> createOnConfigurationNodeClear() {
            return PreferencesHandler.this::onConfigurationClearTreeEvent;
        }

        /**
         * Returns the {@code HierarchicalConfiguration} nodes clear listener.
         * 
         * @return The {@code HierarchicalConfiguration} nodes clear listener
         */
        public @NotNull EventListener<ConfigurationEvent> getOnConfigurationNodeClear() {
            return this.onConfigurationNodeClear;
        }

        /**
         * Creates the {@code HierarchicalConfiguration} clear listener.
         * 
         * @return The {@code HierarchicalConfiguration} clear listener
         */
        protected @NotNull EventListener<ConfigurationEvent> createOnConfigurationClear() {
            return PreferencesHandler.this::onConfigurationClearEvent;
        }

        /**
         * Returns the {@code HierarchicalConfiguration} clear listener.
         * 
         * @return The {@code HierarchicalConfiguration} clear listener
         */
        public @NotNull EventListener<ConfigurationEvent> getOnConfigurationClear() {
            return this.onConfigurationClear;
        }

        /**
         * Installs the {@code Preferences} listeners.
         * 
         * @param target The {@code Preferences} to listen
         */
        public void installPreferencesListeners(
                final @NotNull Preferences target) {
            target.addPreferenceChangeListener(this.onPreferencePropertyChange);
            target.addNodeChangeListener(this.onPreferencesNodeChange);
        }

        /**
         * Removes the {@code Preferences} listeners.
         * 
         * @param target The {@code Preferences} to stop listening
         */
        public void removePreferencesListeners(
                final @NotNull Preferences target) {
            target.removePreferenceChangeListener(this.onPreferencePropertyChange);
            target.removeNodeChangeListener(this.onPreferencesNodeChange);
        }

        /**
         * Installs the {@code PreferencesBased} listeners.
         * 
         * @param target The {@code PreferencesBased} to listen
         */
        public void installConfigurationListeners(
                final @NotNull PreferencesBased<N> target) {
            if (target instanceof EventSource) {
                final EventSource source = (EventSource) target;
                source.addEventListener(
                        ConfigurationEvent.ADD_PROPERTY,
                        this.onConfigurationPropertyAdd);
                source.addEventListener(
                        ConfigurationEvent.SET_PROPERTY,
                        this.onConfigurationPropertySet);
                source.addEventListener(
                        ConfigurationEvent.CLEAR_PROPERTY,
                        this.onConfigurationPropertyClear);
                source.addEventListener(
                        ConfigurationEvent.ADD_NODES,
                        this.onConfigurationNodesAdd);
                source.addEventListener(
                        ConfigurationEvent.CLEAR_TREE,
                        this.onConfigurationNodeClear);
                source.addEventListener(
                        ConfigurationEvent.CLEAR,
                        this.onConfigurationClear);
            }
        }

        /**
         * Removes the {@code PreferencesBased} listeners.
         * 
         * @param target The {@code PreferencesBased} to stop listening
         */
        public void removeConfigurationListeners(
                final @NotNull PreferencesBased<N> target) {
            if (target instanceof EventSource) {
                final EventSource source = (EventSource) target;
                source.removeEventListener(
                        ConfigurationEvent.ADD_PROPERTY,
                        this.onConfigurationPropertyAdd);
                source.removeEventListener(
                        ConfigurationEvent.SET_PROPERTY,
                        this.onConfigurationPropertySet);
                source.removeEventListener(
                        ConfigurationEvent.CLEAR_PROPERTY,
                        this.onConfigurationPropertyClear);
                source.removeEventListener(
                        ConfigurationEvent.ADD_NODES,
                        this.onConfigurationNodesAdd);
                source.removeEventListener(
                        ConfigurationEvent.CLEAR_TREE,
                        this.onConfigurationNodeClear);
                source.removeEventListener(
                        ConfigurationEvent.CLEAR,
                        this.onConfigurationClear);
            }
        }
    }
}
