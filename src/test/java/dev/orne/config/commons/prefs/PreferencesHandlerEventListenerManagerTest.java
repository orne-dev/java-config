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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.prefs.NodeChangeEvent;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.Preferences;

import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.configuration2.event.EventSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@code PreferencesHandler.EventListenerManager}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see PreferencesHandler.EventListenerManager
 */
@Tag("ut")
class PreferencesHandlerEventListenerManagerTest {

    private @Mock PreferencesMapper<Object> mapper;
    private @Mock EventCoordinationStrategy coordinator;
    private @Mock Preferences baseNode;
    private @Mock PreferencesBased<Object> config;
    private @Mock EventSourceConfig eventSourceConfig;
    private @Mock PreferenceChangeEvent preferenceChangeEvent;
    private @Mock NodeChangeEvent nodeChangeEvent;
    private @Mock ConfigurationEvent configurationEvent;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test for {@link PreferencesHandler.EventListenerManager#EventListenerManager()}.
     */
    @Test
    void testConstructor() {
        final PreferencesHandler<Object> handler = new PreferencesHandler<>(mapper, coordinator);
        final PreferencesHandler<Object>.EventListenerManager manager =
                handler.new EventListenerManager();
        assertNotNull(manager.getOnPreferencePropertyChange());
        assertNotNull(manager.getOnPreferencesNodeChange());
        assertNotNull(manager.getOnConfigurationPropertyAdd());
        assertNotNull(manager.getOnConfigurationPropertySet());
        assertNotNull(manager.getOnConfigurationPropertyClear());
        assertNotNull(manager.getOnConfigurationNodesAdd());
        assertNotNull(manager.getOnConfigurationNodeClear());
        assertNotNull(manager.getOnConfigurationClear());
    }

    /**
     * Test for {@link PreferencesHandler.EventListenerManager#installPreferencesListeners(Preferences)}.
     */
    @Test
    void testInstallPreferencesListeners() {
        final PreferencesHandler<Object> handler = new PreferencesHandler<>(mapper, coordinator);
        final PreferencesHandler<Object>.EventListenerManager manager =
                handler.new EventListenerManager();
        
        manager.installPreferencesListeners(baseNode);
        
        then(baseNode).should(times(1)).addPreferenceChangeListener(
                manager.getOnPreferencePropertyChange());
        then(baseNode).should(times(1)).addNodeChangeListener(
                manager.getOnPreferencesNodeChange());
        then(baseNode).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler.EventListenerManager#removePreferencesListeners(Preferences)}.
     */
    @Test
    void testRemovePreferencesListeners() {
        final PreferencesHandler<Object> handler = new PreferencesHandler<>(mapper, coordinator);
        final PreferencesHandler<Object>.EventListenerManager manager =
                handler.new EventListenerManager();
        
        manager.removePreferencesListeners(baseNode);
        
        then(baseNode).should(times(1)).removePreferenceChangeListener(
                manager.getOnPreferencePropertyChange());
        then(baseNode).should(times(1)).removeNodeChangeListener(
                manager.getOnPreferencesNodeChange());
        then(baseNode).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler.EventListenerManager#installConfigurationListeners(Preferences)}.
     */
    @Test
    void testInstallConfigurationListeners_NoEventSource() {
        final PreferencesHandler<Object> handler = new PreferencesHandler<>(mapper, coordinator);
        final PreferencesHandler<Object>.EventListenerManager manager =
                handler.new EventListenerManager();
        
        manager.installConfigurationListeners(config);
        
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesHandler.EventListenerManager#installConfigurationListeners(PreferencesBased)}.
     */
    @Test
    void testInstallConfigurationListeners() {
        final PreferencesHandler<Object> handler = new PreferencesHandler<>(mapper, coordinator);
        final PreferencesHandler<Object>.EventListenerManager manager =
                handler.new EventListenerManager();
        
        manager.installConfigurationListeners(eventSourceConfig);
        
        then(eventSourceConfig).should(times(1)).addEventListener(
                ConfigurationEvent.ADD_PROPERTY,
                manager.getOnConfigurationPropertyAdd());
        then(eventSourceConfig).should(times(1)).addEventListener(
                ConfigurationEvent.SET_PROPERTY,
                manager.getOnConfigurationPropertySet());
        then(eventSourceConfig).should(times(1)).addEventListener(
                ConfigurationEvent.CLEAR_PROPERTY,
                manager.getOnConfigurationPropertyClear());
        then(eventSourceConfig).should(times(1)).addEventListener(
                ConfigurationEvent.ADD_NODES,
                manager.getOnConfigurationNodesAdd());
        then(eventSourceConfig).should(times(1)).addEventListener(
                ConfigurationEvent.CLEAR_TREE,
                manager.getOnConfigurationNodeClear());
        then(eventSourceConfig).should(times(1)).addEventListener(
                ConfigurationEvent.CLEAR,
                manager.getOnConfigurationClear());
        then(eventSourceConfig).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler.EventListenerManager#removeConfigurationListeners(PreferencesBased)}.
     */
    @Test
    void testRemoveConfigurationListeners() {
        final PreferencesHandler<Object> handler = new PreferencesHandler<>(mapper, coordinator);
        final PreferencesHandler<Object>.EventListenerManager manager =
                handler.new EventListenerManager();
        
        manager.removeConfigurationListeners(eventSourceConfig);
        
        then(eventSourceConfig).should(times(1)).removeEventListener(
                ConfigurationEvent.ADD_PROPERTY,
                manager.getOnConfigurationPropertyAdd());
        then(eventSourceConfig).should(times(1)).removeEventListener(
                ConfigurationEvent.SET_PROPERTY,
                manager.getOnConfigurationPropertySet());
        then(eventSourceConfig).should(times(1)).removeEventListener(
                ConfigurationEvent.CLEAR_PROPERTY,
                manager.getOnConfigurationPropertyClear());
        then(eventSourceConfig).should(times(1)).removeEventListener(
                ConfigurationEvent.ADD_NODES,
                manager.getOnConfigurationNodesAdd());
        then(eventSourceConfig).should(times(1)).removeEventListener(
                ConfigurationEvent.CLEAR_TREE,
                manager.getOnConfigurationNodeClear());
        then(eventSourceConfig).should(times(1)).removeEventListener(
                ConfigurationEvent.CLEAR,
                manager.getOnConfigurationClear());
        then(eventSourceConfig).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler.EventListenerManager#removeConfigurationListeners(PreferencesBased)}.
     */
    @Test
    void testRemoveConfigurationListeners_NotEventSource() {
        final PreferencesHandler<Object> handler = new PreferencesHandler<>(mapper, coordinator);
        final PreferencesHandler<Object>.EventListenerManager manager =
                handler.new EventListenerManager();
        
        manager.removeConfigurationListeners(config);
        
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesHandler.EventListenerManager#getOnPreferencePropertyChange()}.
     */
    @Test
    void testDefaultOnPreferencesPropertyChange() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, coordinator));
        final PreferencesHandler<Object>.EventListenerManager manager =
                handler.new EventListenerManager();
        
        
        willDoNothing().given(handler).onPreferenceChangeEvent(any());
        
        manager.getOnPreferencePropertyChange().preferenceChange(preferenceChangeEvent);
        
        then(handler).should(times(1)).onPreferenceChangeEvent(same(preferenceChangeEvent));
        then(handler).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler.EventListenerManager#getOnPreferencesNodeChange()}.
     */
    @Test
    void testDefaultOnPreferencesNodeAdd() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, coordinator));
        final PreferencesHandler<Object>.EventListenerManager manager =
                handler.new EventListenerManager();
        
        willDoNothing().given(handler).onPreferenceNodeAddedEvent(any());
        
        manager.getOnPreferencesNodeChange().childAdded(nodeChangeEvent);
        
        then(handler).should(times(1)).onPreferenceNodeAddedEvent(same(nodeChangeEvent));
        then(handler).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler.EventListenerManager#getOnPreferencesNodeChange()}.
     */
    @Test
    void testDefaultOnPreferencesNodeRemove() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, coordinator));
        final PreferencesHandler<Object>.EventListenerManager manager =
                handler.new EventListenerManager();
        
        willDoNothing().given(handler).onPreferenceNodeRemovedEvent(any());
        
        manager.getOnPreferencesNodeChange().childRemoved(nodeChangeEvent);
        
        then(handler).should(times(1)).onPreferenceNodeRemovedEvent(same(nodeChangeEvent));
        then(handler).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler.EventListenerManager#getOnConfigurationPropertyAdd()}.
     */
    @Test
    void testDefaultOnConfigurationPropertyAdd() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, coordinator));
        final PreferencesHandler<Object>.EventListenerManager manager =
                handler.new EventListenerManager();
        
        willDoNothing().given(handler).onConfigurationAddPropertyEvent(any());
        
        manager.getOnConfigurationPropertyAdd().onEvent(configurationEvent);
        
        then(handler).should(times(1)).onConfigurationAddPropertyEvent(same(configurationEvent));
        then(handler).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler.EventListenerManager#getOnConfigurationPropertySet()}.
     */
    @Test
    void testDefaultOnConfigurationPropertySet() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, coordinator));
        final PreferencesHandler<Object>.EventListenerManager manager =
                handler.new EventListenerManager();
        
        willDoNothing().given(handler).onConfigurationSetPropertyEvent(any());
        
        manager.getOnConfigurationPropertySet().onEvent(configurationEvent);
        
        then(handler).should(times(1)).onConfigurationSetPropertyEvent(same(configurationEvent));
        then(handler).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler.EventListenerManager#getOnConfigurationPropertyClear()}.
     */
    @Test
    void testDefaultOnConfigurationPropertyClear() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, coordinator));
        final PreferencesHandler<Object>.EventListenerManager manager =
                handler.new EventListenerManager();
        
        willDoNothing().given(handler).onConfigurationClearPropertyEvent(any());
        
        manager.getOnConfigurationPropertyClear().onEvent(configurationEvent);
        
        then(handler).should(times(1)).onConfigurationClearPropertyEvent(same(configurationEvent));
        then(handler).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler.EventListenerManager#getOnConfigurationNodesAdd()}.
     */
    @Test
    void testDefaultOnConfigurationNodesAdd() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, coordinator));
        final PreferencesHandler<Object>.EventListenerManager manager =
                handler.new EventListenerManager();
        
        willDoNothing().given(handler).onConfigurationAddNodesEvent(any());
        
        manager.getOnConfigurationNodesAdd().onEvent(configurationEvent);
        
        then(handler).should(times(1)).onConfigurationAddNodesEvent(same(configurationEvent));
        then(handler).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler.EventListenerManager#getOnConfigurationNodeClear()}.
     */
    @Test
    void testDefaultOnConfigurationNodeClear() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, coordinator));
        final PreferencesHandler<Object>.EventListenerManager manager =
                handler.new EventListenerManager();
        
        willDoNothing().given(handler).onConfigurationClearTreeEvent(any());
        
        manager.getOnConfigurationNodeClear().onEvent(configurationEvent);
        
        then(handler).should(times(1)).onConfigurationClearTreeEvent(same(configurationEvent));
        then(handler).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler.EventListenerManager#getOnConfigurationClear()}.
     */
    @Test
    void testDefaultOnConfigurationClear() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, coordinator));
        final PreferencesHandler<Object>.EventListenerManager manager =
                handler.new EventListenerManager();
        
        willDoNothing().given(handler).onConfigurationClearEvent(any());
        
        manager.getOnConfigurationClear().onEvent(configurationEvent);
        
        then(handler).should(times(1)).onConfigurationClearEvent(same(configurationEvent));
        then(handler).shouldHaveNoMoreInteractions();
    }

    private static interface EventSourceConfig
    extends PreferencesBased<Object>, EventSource {}
}
