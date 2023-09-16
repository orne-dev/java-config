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

import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.NodeChangeEvent;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.Preferences;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.configuration2.event.EventType;
import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.cartesian.CartesianTest;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.config.TestPreferencesFactory;

/**
 * Unit tests for {@code PreferencesHandler}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see PreferencesHandler
 */
@Tag("ut")
class PreferencesHandlerTest {

    private @Mock PreferencesMapper<Object> mapper;
    private @Mock EventCoordinationStrategy coordinator;
    private @Mock Preferences baseNode;
    private @Mock PreferencesBased<Object> config;
    private @Mock PreferencesHandler<Object>.EventListenerManager eventListenerManager;
    private @Mock PreferenceChangeEvent preferenceChangeEvent;
    private @Mock NodeChangeEvent nodeChangeEvent;
    private @Mock ConfigurationEvent configurationEvent;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Restores the NOP root {@code Preferences} nodes.
     */
    @AfterEach
    public void restoreTestPreferences() {
        TestPreferencesFactory.setSystemRoot(new TestPreferencesFactory.NopPreferences());
        TestPreferencesFactory.setUserRoot(new TestPreferencesFactory.NopPreferences());
    }

    /**
     * Test for {@link PreferencesHandler#PreferencesHandler(PreferencesMapper)}.
     */
    @Test
    void testConstructor() {
        final PreferencesHandler<Object> handler = new PreferencesHandler<>(mapper);
        assertSame(mapper, handler.getMapper());
        assertTrue(handler.getEventCoordinationStrategy() instanceof ByThreadEventCoordinationStrategy);
        assertNull(handler.getBaseNode());
        assertNull(handler.getContent());
        assertFalse(handler.isAutoLoad());
        assertFalse(handler.isAutoSave());
        assertNotNull(handler.getEventListenerManager());
    }

    /**
     * Test for {@link PreferencesHandler#PreferencesHandler(PreferencesMapper)}.
     */
    @Test
    void testConstructor_Null() {
        assertThrows(NullPointerException.class, () -> {
            new PreferencesHandler<>(null);
        });
    }

    /**
     * Test for {@link PreferencesHandler#PreferencesHandler(PreferencesMapper, EventCoordinationStrategy)}.
     */
    @CartesianTest
    void testConstructor_Coordinator(
            final boolean nullMapper,
            final boolean nullCoordinator) {
        final PreferencesMapper<Object> testMapper = nullMapper ? null : this.mapper;
        final EventCoordinationStrategy testCoordinator = nullCoordinator ? null : this.coordinator;
        if (nullMapper || nullCoordinator) {
            assertThrows(NullPointerException.class, () -> {
                new PreferencesHandler<>(testMapper, testCoordinator);
            });
        } else {
            final PreferencesHandler<Object> handler = new PreferencesHandler<>(testMapper, testCoordinator);
            assertSame(testMapper, handler.getMapper());
            assertSame(testCoordinator, handler.getEventCoordinationStrategy());
            assertNull(handler.getBaseNode());
            assertNull(handler.getContent());
            assertFalse(handler.isAutoLoad());
            assertFalse(handler.isAutoSave());
            assertNotNull(handler.getEventListenerManager());
        }
    }

    /**
     * Test for {@link PreferencesHandler#PreferencesHandler(PreferencesMapper, EventCoordinationStrategy)}.
     */
    @CartesianTest
    void testConstructor_CoordinatorManager(
            final boolean nullMapper,
            final boolean nullCoordinator,
            final boolean nullListenerManager) {
        final PreferencesMapper<Object> testMapper = nullMapper ? null : this.mapper;
        final EventCoordinationStrategy testCoordinator = nullCoordinator ? null : this.coordinator;
        final PreferencesHandler<Object>.EventListenerManager testListenerManager = nullListenerManager ? null : this.eventListenerManager;
        if (nullMapper || nullCoordinator) {
            assertThrows(NullPointerException.class, () -> {
                new PreferencesHandler<>(testMapper, testCoordinator, testListenerManager);
            });
        } else {
            final PreferencesHandler<Object> handler = new PreferencesHandler<>(testMapper, testCoordinator, testListenerManager);
            assertSame(testMapper, handler.getMapper());
            assertSame(testCoordinator, handler.getEventCoordinationStrategy());
            assertNull(handler.getBaseNode());
            assertNull(handler.getContent());
            assertFalse(handler.isAutoLoad());
            assertFalse(handler.isAutoSave());
            if (nullListenerManager) {
                assertNotNull(handler.getEventListenerManager());
            } else {
                assertSame(testListenerManager, handler.getEventListenerManager());
            }
        }
    }

    /**
     * Test for {@link PreferencesHandler#fromMap(java.util.Map, PreferencesMapper)}.
     */
    @CartesianTest
    void testFromMap(
            final boolean withBaseNode,
            final boolean withBaseNodeScope,
            final boolean withBaseNodeClass,
            final boolean withBaseNodePath,
            final boolean withAutoLoad,
            final boolean withAutoSave,
            final boolean withEventCoordinator) {
        @SuppressWarnings("unchecked")
        final Map<String, ?> map = mock(Map.class);
        final Preferences mockRoot = mock(Preferences.class);
        final Preferences classPrefs = mock(Preferences.class);
        final String classPath = "/dev/orne/config/commons/prefs";
        final String testPath = "test/path";
        Preferences resultBaseNode = null;
        
        if (withBaseNode) {
            willReturn(true).given(map).containsKey(PreferencesHandler.PROP_BASE_NODE);
            willReturn(baseNode).given(map).get(PreferencesHandler.PROP_BASE_NODE);
            resultBaseNode = baseNode;
        } else {
            resultBaseNode = mockRoot;
            if (withBaseNodeScope) {
                willReturn(true).given(map).containsKey(PreferencesHandler.PROP_SYSTEM_TREE);
                willReturn(true).given(map).get(PreferencesHandler.PROP_SYSTEM_TREE);
                TestPreferencesFactory.setSystemRoot(mockRoot);
            } else {
                TestPreferencesFactory.setUserRoot(mockRoot);
            }
            if (withBaseNodeClass) {
                willReturn(true).given(map).containsKey(PreferencesHandler.PROP_BASE_CLASS);
                willReturn(PreferencesHandler.class).given(map).get(PreferencesHandler.PROP_BASE_CLASS);
                willReturn(classPrefs).given(mockRoot).node(classPath);
                resultBaseNode = classPrefs;
            }
            if (withBaseNodePath) {
                willReturn(true).given(map).containsKey(PreferencesHandler.PROP_PATH);
                willReturn(testPath).given(map).get(PreferencesHandler.PROP_PATH);
                willReturn(baseNode).given(resultBaseNode).node(testPath);
                resultBaseNode = baseNode;
            }
        }
        if (withAutoLoad) {
            willReturn(true).given(map).containsKey(PreferencesHandler.PROP_AUTO_LOAD);
            willReturn(true).given(map).get(PreferencesHandler.PROP_AUTO_LOAD);
        }
        if (withAutoSave) {
            willReturn(true).given(map).containsKey(PreferencesHandler.PROP_AUTO_SAVE);
            willReturn(true).given(map).get(PreferencesHandler.PROP_AUTO_SAVE);
        }
        if (withEventCoordinator) {
            willReturn(true).given(map).containsKey(PreferencesHandler.PROP_EVENT_COORDINATOR);
            willReturn(coordinator).given(map).get(PreferencesHandler.PROP_EVENT_COORDINATOR);
        }
        
        final PreferencesHandler<Object> handler = PreferencesHandler.fromMap(map, mapper);
        
        assertSame(mapper, handler.getMapper());
        if (withEventCoordinator) {
            assertSame(coordinator, handler.getEventCoordinationStrategy());
        } else {
            assertTrue(handler.getEventCoordinationStrategy() instanceof ByThreadEventCoordinationStrategy);
        }
        assertEquals(resultBaseNode, handler.getBaseNode());
        assertNull(handler.getContent());
        assertEquals(withAutoLoad, handler.isAutoLoad());
        assertEquals(withAutoSave, handler.isAutoSave());
        assertNotNull(handler.getEventListenerManager());
    }

    /**
     * Test for {@link PreferencesHandler#fromMap(java.util.Map, PreferencesMapper)}.
     */
    @Test
    void testFromMap_NullMap() {
        final PreferencesHandler<Object> handler = PreferencesHandler.fromMap(null, mapper);
        assertSame(mapper, handler.getMapper());
        assertTrue(handler.getEventCoordinationStrategy() instanceof ByThreadEventCoordinationStrategy);
        assertNull(handler.getBaseNode());
        assertNull(handler.getContent());
        assertFalse(handler.isAutoLoad());
        assertFalse(handler.isAutoSave());
        assertNotNull(handler.getEventListenerManager());
    }

    /**
     * Test for {@link PreferencesHandler#fromMap(java.util.Map, PreferencesMapper)}.
     */
    @Test
    void testFromMap_NullMapper() {
        @SuppressWarnings("unchecked")
        final Map<String, ?> map = mock(Map.class);
        assertThrows(NullPointerException.class, () -> {
            PreferencesHandler.fromMap(map, null);
        });
    }

    /**
     * Test for {@link PreferencesHandler#setBaseNode(Preferences)}.
     */
    @ParameterizedTest
    @ValueSource(booleans={ false, true })
    void testSetBaseNode(
            final boolean autoLoadEnabled) {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, coordinator, eventListenerManager));
        
        if (autoLoadEnabled) {
            handler.setAutoLoad(true);
        }
        handler.setBaseNode(baseNode);
        assertSame(baseNode, handler.getBaseNode());
        if (autoLoadEnabled) {
            then(eventListenerManager).should().installPreferencesListeners(baseNode);
        } else {
            then(eventListenerManager).should(never()).installPreferencesListeners(any());
        }
        handler.setBaseNode(null);
        assertNull(handler.getBaseNode());
        if (autoLoadEnabled) {
            then(eventListenerManager).should().removePreferencesListeners(baseNode);
        } else {
            then(eventListenerManager).should(never()).removePreferencesListeners(any());
        }
        
        then(baseNode).shouldHaveNoInteractions();
        then(mapper).shouldHaveNoInteractions();
        then(coordinator).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#setBaseNode(boolean, Class, String)}.
     */
    @CartesianTest
    void testSetBaseNode_Components(
            final boolean systemScope,
            final boolean withBaseClass,
            final boolean withPath) {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, coordinator, eventListenerManager));
        final Preferences mockRoot = mock(Preferences.class);
        final Preferences classPrefs = mock(Preferences.class);
        final String classPath = "/dev/orne/config/commons/prefs";
        final String testPath = "test/path";
        Preferences result = mockRoot;
        
        willDoNothing().given(handler).setBaseNode(any());
        if (systemScope) {
            TestPreferencesFactory.setSystemRoot(mockRoot);
        } else {
            TestPreferencesFactory.setUserRoot(mockRoot);
        }
        if (withBaseClass) {
            willReturn(classPrefs).given(mockRoot).node(classPath);
            result = classPrefs;
        }
        if (withPath) {
            willReturn(baseNode).given(result).node(testPath);
            result = baseNode;
        }
        
        handler.setBaseNode(
                systemScope,
                withBaseClass ? PreferencesHandlerTest.class : null,
                withPath ? testPath : null);
        
        if (withBaseClass) {
            then(mockRoot).should().node(classPath);
            if (withPath) {
                then(classPrefs).should().node(testPath);
            }
        } else if (withPath) {
            then(mockRoot).should().node(testPath);
        }
        then(handler).should().setBaseNode(result);
    }

    /**
     * Test for {@link PreferencesHandler#checkBaseNode()}.
     */
    @Test
    void testCheckBaseNode() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, coordinator));
        
        handler.setBaseNode(baseNode);
        assertSame(baseNode, handler.checkBaseNode());
        handler.setBaseNode(null);
        assertThrows(IllegalStateException.class, handler::checkBaseNode);
    }

    /**
     * Test for {@link PreferencesHandler#setContent(PreferencesBased)}.
     */
    @ParameterizedTest
    @ValueSource(booleans={ false, true })
    void testSetContent(
            final boolean autoSaveEnabled) {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, coordinator, eventListenerManager));
        
        if (autoSaveEnabled) {
            handler.setAutoSave(true);
        }
        handler.setContent(config);
        assertSame(config, handler.getContent());
        if (autoSaveEnabled) {
            then(eventListenerManager).should().installConfigurationListeners(config);
        } else {
            then(eventListenerManager).should(never()).installConfigurationListeners(any());
        }
        handler.setContent(null);
        assertNull(handler.getContent());
        if (autoSaveEnabled) {
            then(eventListenerManager).should().removeConfigurationListeners(config);
        } else {
            then(eventListenerManager).should(never()).removeConfigurationListeners(any());
        }
        
        then(config).shouldHaveNoInteractions();
        then(mapper).shouldHaveNoInteractions();
        then(coordinator).shouldHaveNoInteractions();
        then(eventListenerManager).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#checkContent()}.
     */
    @Test
    void testCheckContent() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, coordinator));
        
        handler.setContent(config);
        assertSame(config, handler.checkContent());
        handler.setContent(null);
        assertThrows(IllegalStateException.class, handler::checkContent);
    }

    /**
     * Test for {@link PreferencesHandler#setAutoLoad(boolean)}.
     */
    @ParameterizedTest
    @ValueSource(booleans={ false, true })
    void testSetAutoLoad(
            final boolean withBaseNode) {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, coordinator, eventListenerManager));
        
        if (withBaseNode) {
            handler.setBaseNode(baseNode);
        }
        handler.setAutoLoad(true);
        assertTrue(handler.isAutoLoad());
        if (withBaseNode) {
            then(eventListenerManager).should().installPreferencesListeners(baseNode);
        } else {
            then(eventListenerManager).should(never()).installPreferencesListeners(any());
        }
        handler.setAutoLoad(false);
        assertFalse(handler.isAutoLoad());
        if (withBaseNode) {
            then(eventListenerManager).should().removePreferencesListeners(baseNode);
        } else {
            then(eventListenerManager).should(never()).removePreferencesListeners(any());
        }
        
        then(baseNode).shouldHaveNoInteractions();
        then(mapper).shouldHaveNoInteractions();
        then(coordinator).shouldHaveNoInteractions();
        then(eventListenerManager).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#autoLoadRequired(EventObject)}.
     */
    @CartesianTest
    void testAutoLoadRequired(
            final boolean autoLoadEnabled,
            final boolean withBaseNode,
            final boolean withConfig) {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        
        if (autoLoadEnabled) {
            handler.setAutoLoad(true);
        }
        if (withBaseNode) {
            handler.setBaseNode(baseNode);
        }
        if (withConfig) {
            handler.setContent(config);
        }
        
        if (autoLoadEnabled && withBaseNode && withConfig) {
            assertTrue(handler.autoLoadRequired(configurationEvent));
        } else {
            assertFalse(handler.autoLoadRequired(configurationEvent));
        }
    }

    /**
     * Test for {@link PreferencesHandler#setAutoSave(boolean)}.
     */
    @ParameterizedTest
    @ValueSource(booleans={ false, true })
    void testSetAutoSave(
            final boolean withConfig) {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, coordinator, eventListenerManager));
        
        if (withConfig) {
            handler.setContent(config);
        }
        handler.setAutoSave(true);
        assertTrue(handler.isAutoSave());
        if (withConfig) {
            then(eventListenerManager).should().installConfigurationListeners(config);
        } else {
            then(eventListenerManager).should(never()).installConfigurationListeners(any());
        }
        handler.setAutoSave(false);
        assertFalse(handler.isAutoSave());
        if (withConfig) {
            then(eventListenerManager).should().removeConfigurationListeners(config);
        } else {
            then(eventListenerManager).should(never()).removeConfigurationListeners(any());
        }
        
        then(config).shouldHaveNoInteractions();
        then(mapper).shouldHaveNoInteractions();
        then(coordinator).shouldHaveNoInteractions();
        then(eventListenerManager).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#autoSaveRequired(EventObject)}.
     */
    @CartesianTest
    void testAutoSaveRequired(
            final boolean autoSaveEnabled,
            final boolean withBaseNode,
            final boolean withConfig) {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        
        if (autoSaveEnabled) {
            handler.setAutoSave(true);
        }
        if (withBaseNode) {
            handler.setBaseNode(baseNode);
        }
        if (withConfig) {
            handler.setContent(config);
        }
        
        if (autoSaveEnabled && withBaseNode && withConfig) {
            assertTrue(handler.autoSaveRequired(configurationEvent));
        } else {
            assertFalse(handler.autoSaveRequired(configurationEvent));
        }
    }

    /**
     * Test for {@link PreferencesHandler#synchronizeBaseNode()}.
     * @throws BackingStoreException Should not happen
     */
    @Test
    void testSynchronizeBaseNode()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        
        handler.synchronizeBaseNode();
        
        then(baseNode).should().sync();
        then(baseNode).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#synchronizeBaseNode()}.
     * @throws BackingStoreException Should not happen
     */
    @Test
    void testSynchronizeBaseNode_NullBaseNode()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        
        assertThrows(IllegalStateException.class, () -> {
            handler.synchronizeBaseNode();
        });
    }

    /**
     * Test for {@link PreferencesHandler#synchronizeBaseNode()}.
     * @throws BackingStoreException Should not happen
     */
    @Test
    void testSynchronizeBaseNode_BackingStoreException()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        final BackingStoreException mockEx = new BackingStoreException("mock exception");
        
        willThrow(mockEx).given(baseNode).sync();
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            handler.synchronizeBaseNode();
        });
        assertNotNull(result);
        assertSame(mockEx, result.getCause());
        
        then(baseNode).should().sync();
        then(baseNode).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#flushBaseNode()}.
     * @throws BackingStoreException Should not happen
     */
    @Test
    void testFlushBaseNode()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        
        handler.flushBaseNode();
        
        then(baseNode).should().flush();
        then(baseNode).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#flushBaseNode()}.
     * @throws BackingStoreException Should not happen
     */
    @Test
    void testFlushBaseNode_NullBaseNode()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        
        assertThrows(IllegalStateException.class, () -> {
            handler.flushBaseNode();
        });
    }

    /**
     * Test for {@link PreferencesHandler#flushBaseNode()}.
     * @throws BackingStoreException Should not happen
     */
    @Test
    void testFlushBaseNode_BackingStoreException()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        final BackingStoreException mockEx = new BackingStoreException("mock exception");
        
        willThrow(mockEx).given(baseNode).flush();
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            handler.flushBaseNode();
        });
        assertNotNull(result);
        assertSame(mockEx, result.getCause());
        
        then(baseNode).should().flush();
        then(baseNode).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#load()}.
     * @throws BackingStoreException Should not happen
     */
    @Test
    void testLoad()
    throws BackingStoreException {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        final Object newRoot = new Object();
        
        willReturn(newRoot).given(mapper).loadNodeHierarchy(same(baseNode), any());
        
        handler.load();
        
        final InOrder inOrder = inOrder(testCoordinator, mapper, baseNode, config);
        then(testCoordinator).should(inOrder).preventEvents(any());
        then(baseNode).should(inOrder).sync();
        then(mapper).should(inOrder).loadNodeHierarchy(same(baseNode), any());
        then(config).should(inOrder).load(newRoot);
        then(mapper).shouldHaveNoMoreInteractions();
        then(baseNode).shouldHaveNoMoreInteractions();
        then(config).shouldHaveNoMoreInteractions();
        
    }

    /**
     * Test for {@link PreferencesHandler#load()}.
     * @throws BackingStoreException Should not happen
     */
    @Test
    void testLoad_NullBaseNode()
    throws BackingStoreException {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        handler.setContent(config);
        
        assertThrows(IllegalStateException.class, () -> {
            handler.load();
        });
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#load()}.
     * @throws BackingStoreException Should not happen
     */
    @Test
    void testLoad_NullContent()
    throws BackingStoreException {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        handler.setBaseNode(baseNode);
        
        assertThrows(IllegalStateException.class, () -> {
            handler.load();
        });
        
        then(baseNode).should(atLeast(0)).sync();
        then(baseNode).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#refresh()}.
     * @throws BackingStoreException Should not happen
     */
    @Test
    void testRefresh()
    throws BackingStoreException {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        final Object newRoot = new Object();
        
        willReturn(newRoot).given(mapper).loadNodeHierarchy(same(baseNode), any());
        
        handler.refresh();
        
        final InOrder inOrder = inOrder(testCoordinator, mapper, baseNode, config);
        then(testCoordinator).should(inOrder).preventEvents(any());
        then(config).should(inOrder).clear();
        then(baseNode).should(inOrder).sync();
        then(mapper).should(inOrder).loadNodeHierarchy(same(baseNode), any());
        then(config).should(inOrder).load(newRoot);
        then(mapper).shouldHaveNoMoreInteractions();
        then(baseNode).shouldHaveNoMoreInteractions();
        then(config).shouldHaveNoMoreInteractions();
        
    }

    /**
     * Test for {@link PreferencesHandler#refresh()}.
     * @throws BackingStoreException Should not happen
     */
    @Test
    void testRefresh_NullBaseNode()
    throws BackingStoreException {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        handler.setContent(config);
        
        assertThrows(IllegalStateException.class, () -> {
            handler.refresh();
        });
        
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#refresh()}.
     * @throws BackingStoreException Should not happen
     */
    @Test
    void testRefresh_NullContent()
    throws BackingStoreException {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        handler.setBaseNode(baseNode);
        
        assertThrows(IllegalStateException.class, () -> {
            handler.refresh();
        });
        
        then(baseNode).should(atLeast(0)).sync();
        then(baseNode).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#save()}.
     * @throws BackingStoreException Should not happen
     */
    @Test
    void testSave()
    throws BackingStoreException {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        
        willDoNothing().given(mapper).saveNodeHierarchy(config, baseNode);
        
        handler.save();
        
        final InOrder inOrder = inOrder(testCoordinator, mapper, baseNode, config);
        then(testCoordinator).should(inOrder).preventEvents(any());
        then(baseNode).should(inOrder).sync();
        then(mapper).should(inOrder).saveNodeHierarchy(config, baseNode);
        then(mapper).shouldHaveNoMoreInteractions();
        then(baseNode).shouldHaveNoMoreInteractions();
        then(config).shouldHaveNoMoreInteractions();
        
    }

    /**
     * Test for {@link PreferencesHandler#save()}.
     * @throws BackingStoreException Should not happen
     */
    @Test
    void testSave_NullBaseNode()
    throws BackingStoreException {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        handler.setContent(config);
        
        assertThrows(IllegalStateException.class, () -> {
            handler.save();
        });
        
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#save()}.
     * @throws BackingStoreException Should not happen
     */
    @Test
    void testSave_NullContent()
    throws BackingStoreException {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        handler.setBaseNode(baseNode);
        
        assertThrows(IllegalStateException.class, () -> {
            handler.save();
        });
        
        then(baseNode).should(atLeast(0)).sync();
        then(baseNode).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#onPreferenceChangeEvent(PreferenceChangeEvent)}.
     */
    @Test
    void testOnPreferenceChangeEvent() {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        
        willDoNothing().given(handler).handlePreferenceChangeEvent(any());
        
        handler.onPreferenceChangeEvent(preferenceChangeEvent);
        
        final InOrder inOrder = inOrder(testCoordinator, handler);
        then(testCoordinator).should(inOrder).handlePreferencesEvent(same(preferenceChangeEvent), any());
        then(handler).should(inOrder).handlePreferenceChangeEvent(preferenceChangeEvent);
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceChangeEvent(PreferenceChangeEvent)}.
     */
    @Test
    void testHandlePreferenceChangeEvent() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoLoad(true);
        final Preferences eventNode = mock(Preferences.class);
        final String eventProperty = "mockPrefProperty";
        final String eventValue = "mockPrefValue";
        final String configProperty = "mockConfigProperty";
        
        willReturn(eventNode).given(preferenceChangeEvent).getNode();
        willReturn(eventProperty).given(preferenceChangeEvent).getKey();
        willReturn(eventValue).given(preferenceChangeEvent).getNewValue();
        willReturn(configProperty).given(mapper).resolvePropertyKey(config, baseNode, eventNode, eventProperty);
        
        handler.handlePreferenceChangeEvent(preferenceChangeEvent);
        
        then(config).should().setProperty(configProperty, eventValue);
        shouldNotModify(baseNode);
        then(eventNode).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceChangeEvent(PreferenceChangeEvent)}.
     */
    @Test
    void testHandlePreferenceChangeEvent_NoConfig() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setAutoLoad(true);
        final Preferences eventNode = mock(Preferences.class);
        final String eventProperty = "mockPrefProperty";
        final String eventValue = "mockPrefValue";
        
        willReturn(eventNode).given(preferenceChangeEvent).getNode();
        willReturn(eventProperty).given(preferenceChangeEvent).getKey();
        willReturn(eventValue).given(preferenceChangeEvent).getNewValue();
        
        handler.handlePreferenceChangeEvent(preferenceChangeEvent);
        
        shouldNotModify(config);
        shouldNotModify(baseNode);
        then(eventNode).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceChangeEvent(PreferenceChangeEvent)}.
     */
    @Test
    void testHandlePreferenceChangeEvent_NoBaseNode() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setContent(config);
        handler.setAutoLoad(true);
        final Preferences eventNode = mock(Preferences.class);
        final String eventProperty = "mockPrefProperty";
        final String eventValue = "mockPrefValue";
        
        willReturn(eventNode).given(preferenceChangeEvent).getNode();
        willReturn(eventProperty).given(preferenceChangeEvent).getKey();
        willReturn(eventValue).given(preferenceChangeEvent).getNewValue();
        
        handler.handlePreferenceChangeEvent(preferenceChangeEvent);
        
        shouldNotModify(config);
        shouldNotModify(baseNode);
        then(eventNode).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceChangeEvent(PreferenceChangeEvent)}.
     */
    @Test
    void testHandlePreferenceChangeEvent_NoAutoLoad() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        final Preferences eventNode = mock(Preferences.class);
        final String eventProperty = "mockPrefProperty";
        final String eventValue = "mockPrefValue";
        final String configProperty = "mockConfigProperty";
        
        willReturn(eventNode).given(preferenceChangeEvent).getNode();
        willReturn(eventProperty).given(preferenceChangeEvent).getKey();
        willReturn(eventValue).given(preferenceChangeEvent).getNewValue();
        willReturn(configProperty).given(mapper).resolvePropertyKey(config, baseNode, eventNode, eventProperty);
        
        handler.handlePreferenceChangeEvent(preferenceChangeEvent);
        
        shouldNotModify(config);
        shouldNotModify(baseNode);
        then(eventNode).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceChangeEvent(PreferenceChangeEvent)}.
     */
    @Test
    void testHandlePreferenceChangeEvent_ResolveError() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoLoad(true);
        final Preferences eventNode = mock(Preferences.class);
        final String eventProperty = "mockPrefProperty";
        final String eventValue = "mockPrefValue";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException();
        
        willReturn(eventNode).given(preferenceChangeEvent).getNode();
        willReturn(eventProperty).given(preferenceChangeEvent).getKey();
        willReturn(eventValue).given(preferenceChangeEvent).getNewValue();
        willThrow(mockEx).given(mapper).resolvePropertyKey(any(), any(), any(), any());
        
        handler.handlePreferenceChangeEvent(preferenceChangeEvent);
        
        then(mapper).should().resolvePropertyKey(config, baseNode, eventNode, eventProperty);
        shouldNotModify(config);
        shouldNotModify(baseNode);
        then(eventNode).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceChangeEvent(PreferenceChangeEvent)}.
     */
    @Test
    void testHandlePreferenceChangeEvent_SetError() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoLoad(true);
        final Preferences eventNode = mock(Preferences.class);
        final String eventProperty = "mockPrefProperty";
        final String eventValue = "mockPrefValue";
        final String configProperty = "mockConfigProperty";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException();
        
        willReturn(eventNode).given(preferenceChangeEvent).getNode();
        willReturn(eventProperty).given(preferenceChangeEvent).getKey();
        willReturn(eventValue).given(preferenceChangeEvent).getNewValue();
        willReturn(configProperty).given(mapper).resolvePropertyKey(config, baseNode, eventNode, eventProperty);
        willThrow(mockEx).given(config).setProperty(any(), any());
        
        handler.handlePreferenceChangeEvent(preferenceChangeEvent);
        
        then(config).should().setProperty(configProperty, eventValue);
        shouldNotModify(baseNode);
        then(eventNode).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesHandler#onPreferenceNodeAddedEvent(NodeChangeEvent)}.
     */
    @Test
    void testOnPreferenceNodeAddedEvent() {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        
        willDoNothing().given(handler).handlePreferenceNodeAddedEvent(any());
        
        handler.onPreferenceNodeAddedEvent(nodeChangeEvent);
        
        final InOrder inOrder = inOrder(testCoordinator, handler);
        then(testCoordinator).should(inOrder).handlePreferencesEvent(same(nodeChangeEvent), any());
        then(handler).should(inOrder).handlePreferenceNodeAddedEvent(nodeChangeEvent);
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceNodeAddedEvent(NodeChangeEvent)}.
     */
    @Test
    void testHandlePreferenceNodeAddedEvent() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoLoad(true);
        final Preferences eventNode = mock(Preferences.class);
        final Preferences childNode = mock(Preferences.class);
        final String configProperty = "mockConfigProperty";
        final Object newNode = new Object();
        
        willReturn(eventNode).given(nodeChangeEvent).getParent();
        willReturn(childNode).given(nodeChangeEvent).getChild();
        willReturn(configProperty).given(mapper).resolveNodeKey(config, baseNode, eventNode);
        willReturn(newNode).given(mapper).loadNodeHierarchy(same(childNode), any());
        
        handler.handlePreferenceNodeAddedEvent(nodeChangeEvent);
        
        then(config).should().addNodes(
                same(configProperty),
                argThat(arg -> arg.size() == 1 && arg.contains(newNode)));
        shouldNotModify(baseNode, eventNode, childNode);
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceNodeAddedEvent(NodeChangeEvent)}.
     */
    @Test
    void testHandlePreferenceNodeAddedEvent_NoConfig() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setAutoLoad(true);
        final Preferences eventNode = mock(Preferences.class);
        final Preferences childNode = mock(Preferences.class);
        final String configProperty = "mockConfigProperty";
        final Object newNode = new Object();
        
        willReturn(eventNode).given(nodeChangeEvent).getParent();
        willReturn(childNode).given(nodeChangeEvent).getChild();
        willReturn(configProperty).given(mapper).resolveNodeKey(config, baseNode, eventNode);
        willReturn(newNode).given(mapper).loadNodeHierarchy(same(childNode), any());
        
        handler.handlePreferenceNodeAddedEvent(nodeChangeEvent);
        
        shouldNotModify(config);
        shouldNotModify(baseNode, eventNode, childNode);
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceNodeAddedEvent(NodeChangeEvent)}.
     */
    @Test
    void testHandlePreferenceNodeAddedEvent_NoBaseNode() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setContent(config);
        handler.setAutoLoad(true);
        final Preferences eventNode = mock(Preferences.class);
        final Preferences childNode = mock(Preferences.class);
        final String configProperty = "mockConfigProperty";
        final Object newNode = new Object();
        
        willReturn(eventNode).given(nodeChangeEvent).getParent();
        willReturn(childNode).given(nodeChangeEvent).getChild();
        willReturn(configProperty).given(mapper).resolveNodeKey(config, baseNode, eventNode);
        willReturn(newNode).given(mapper).loadNodeHierarchy(same(childNode), any());
        
        handler.handlePreferenceNodeAddedEvent(nodeChangeEvent);
        
        shouldNotModify(config);
        shouldNotModify(baseNode, eventNode, childNode);
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceNodeAddedEvent(NodeChangeEvent)}.
     */
    @Test
    void testHandlePreferenceNodeAddedEvent_NoAutoLoad() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        final Preferences eventNode = mock(Preferences.class);
        final Preferences childNode = mock(Preferences.class);
        final String configProperty = "mockConfigProperty";
        final Object newNode = new Object();
        
        willReturn(eventNode).given(nodeChangeEvent).getParent();
        willReturn(childNode).given(nodeChangeEvent).getChild();
        willReturn(configProperty).given(mapper).resolveNodeKey(config, baseNode, eventNode);
        willReturn(newNode).given(mapper).loadNodeHierarchy(same(childNode), any());
        
        handler.handlePreferenceNodeAddedEvent(nodeChangeEvent);
        
        shouldNotModify(config);
        shouldNotModify(baseNode, eventNode, childNode);
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceNodeAddedEvent(NodeChangeEvent)}.
     */
    @Test
    void testHandlePreferenceNodeAddedEvent_ResolveError() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoLoad(true);
        final Preferences eventNode = mock(Preferences.class);
        final Preferences childNode = mock(Preferences.class);
        final Object newNode = new Object();
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException();
        
        willReturn(eventNode).given(nodeChangeEvent).getParent();
        willReturn(childNode).given(nodeChangeEvent).getChild();
        willThrow(mockEx).given(mapper).resolveNodeKey(config, baseNode, eventNode);
        willReturn(newNode).given(mapper).loadNodeHierarchy(same(childNode), any());
        
        handler.handlePreferenceNodeAddedEvent(nodeChangeEvent);
        
        then(mapper).should().resolveNodeKey(config, baseNode, eventNode);
        shouldNotModify(config);
        shouldNotModify(baseNode, eventNode, childNode);
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceNodeAddedEvent(NodeChangeEvent)}.
     */
    @Test
    void testHandlePreferenceNodeAddedEvent_LoadError() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoLoad(true);
        final Preferences eventNode = mock(Preferences.class);
        final Preferences childNode = mock(Preferences.class);
        final String configProperty = "mockConfigProperty";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException();
        
        willReturn(eventNode).given(nodeChangeEvent).getParent();
        willReturn(childNode).given(nodeChangeEvent).getChild();
        willReturn(configProperty).given(mapper).resolveNodeKey(config, baseNode, eventNode);
        willThrow(mockEx).given(mapper).loadNodeHierarchy(same(childNode), any());
        
        handler.handlePreferenceNodeAddedEvent(nodeChangeEvent);
        
        then(mapper).should().loadNodeHierarchy(same(childNode), any());
        shouldNotModify(config);
        shouldNotModify(baseNode, eventNode, childNode);
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceNodeAddedEvent(NodeChangeEvent)}.
     */
    @Test
    void testHandlePreferenceNodeAddedEvent_SetError() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoLoad(true);
        final Preferences eventNode = mock(Preferences.class);
        final Preferences childNode = mock(Preferences.class);
        final String configProperty = "mockConfigProperty";
        final Object newNode = new Object();
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException();
        
        willReturn(eventNode).given(nodeChangeEvent).getParent();
        willReturn(childNode).given(nodeChangeEvent).getChild();
        willReturn(configProperty).given(mapper).resolveNodeKey(config, baseNode, eventNode);
        willReturn(newNode).given(mapper).loadNodeHierarchy(same(childNode), any());
        willThrow(mockEx).given(config).addNodes(any(), any());
        
        handler.handlePreferenceNodeAddedEvent(nodeChangeEvent);
        
        then(config).should().addNodes(
                same(configProperty),
                argThat(arg -> arg.size() == 1 && arg.contains(newNode)));
        shouldNotModify(baseNode, eventNode, childNode);
    }

    /**
     * Test for {@link PreferencesHandler#onPreferenceNodeRemovedEvent(NodeChangeEvent)}.
     */
    @Test
    void testOnPreferenceNodeRemovedEvent() {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        
        willDoNothing().given(handler).handlePreferenceNodeRemovedEvent(any());
        
        handler.onPreferenceNodeRemovedEvent(nodeChangeEvent);
        
        final InOrder inOrder = inOrder(testCoordinator, handler);
        then(testCoordinator).should(inOrder).handlePreferencesEvent(same(nodeChangeEvent), any());
        then(handler).should(inOrder).handlePreferenceNodeRemovedEvent(nodeChangeEvent);
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceNodeRemovedEvent(NodeChangeEvent)}.
     */
    @Test
    void testHandlePreferenceNodeRemovedEvent() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoLoad(true);
        final Preferences eventNode = mock(Preferences.class);
        final Preferences childNode = mock(Preferences.class);
        final String configProperty = "mockConfigProperty";
        
        willReturn(eventNode).given(nodeChangeEvent).getParent();
        willReturn(childNode).given(nodeChangeEvent).getChild();
        willReturn(configProperty).given(mapper).resolveNodeKey(config, baseNode, childNode);
        
        handler.handlePreferenceNodeRemovedEvent(nodeChangeEvent);
        
        then(config).should().clearTree(configProperty);
        shouldNotModify(baseNode, eventNode, childNode);
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceNodeRemovedEvent(NodeChangeEvent)}.
     */
    @Test
    void testHandlePreferenceNodeRemovedEvent_NoConfig() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setAutoLoad(true);
        final Preferences eventNode = mock(Preferences.class);
        final Preferences childNode = mock(Preferences.class);
        
        willReturn(eventNode).given(nodeChangeEvent).getParent();
        willReturn(childNode).given(nodeChangeEvent).getChild();
        
        handler.handlePreferenceNodeRemovedEvent(nodeChangeEvent);
        
        shouldNotModify(config);
        shouldNotModify(baseNode, eventNode, childNode);
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceNodeRemovedEvent(NodeChangeEvent)}.
     */
    @Test
    void testHandlePreferenceNodeRemovedEvent_NoBaseNode() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setContent(config);
        handler.setAutoLoad(true);
        final Preferences eventNode = mock(Preferences.class);
        final Preferences childNode = mock(Preferences.class);
        
        willReturn(eventNode).given(nodeChangeEvent).getParent();
        willReturn(childNode).given(nodeChangeEvent).getChild();
        
        handler.handlePreferenceNodeRemovedEvent(nodeChangeEvent);
        
        shouldNotModify(config);
        shouldNotModify(baseNode, eventNode, childNode);
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceNodeRemovedEvent(NodeChangeEvent)}.
     */
    @Test
    void testHandlePreferenceNodeRemovedEvent_NoAutoLoad() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        final Preferences eventNode = mock(Preferences.class);
        final Preferences childNode = mock(Preferences.class);
        final String configProperty = "mockConfigProperty";
        
        willReturn(eventNode).given(nodeChangeEvent).getParent();
        willReturn(childNode).given(nodeChangeEvent).getChild();
        willReturn(configProperty).given(mapper).resolveNodeKey(config, baseNode, childNode);
        
        handler.handlePreferenceNodeRemovedEvent(nodeChangeEvent);
        
        shouldNotModify(config);
        shouldNotModify(baseNode, eventNode, childNode);
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceNodeRemovedEvent(NodeChangeEvent)}.
     */
    @Test
    void testHandlePreferenceNodeRemovedEvent_ResolveError() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoLoad(true);
        final Preferences eventNode = mock(Preferences.class);
        final Preferences childNode = mock(Preferences.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException();
        
        willReturn(eventNode).given(nodeChangeEvent).getParent();
        willReturn(childNode).given(nodeChangeEvent).getChild();
        willThrow(mockEx).given(mapper).resolveNodeKey(config, baseNode, childNode);
        
        handler.handlePreferenceNodeRemovedEvent(nodeChangeEvent);
        
        shouldNotModify(config);
        shouldNotModify(baseNode, eventNode, childNode);
    }

    /**
     * Test for {@link PreferencesHandler#handlePreferenceNodeRemovedEvent(NodeChangeEvent)}.
     */
    @Test
    void testHandlePreferenceNodeRemovedEvent_RemoveError() {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoLoad(true);
        final Preferences eventNode = mock(Preferences.class);
        final Preferences childNode = mock(Preferences.class);
        final String configProperty = "mockConfigProperty";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException();
        
        willReturn(eventNode).given(nodeChangeEvent).getParent();
        willReturn(childNode).given(nodeChangeEvent).getChild();
        willReturn(configProperty).given(mapper).resolveNodeKey(config, baseNode, childNode);
        willThrow(mockEx).given(config).clearTree(configProperty);
        
        handler.handlePreferenceNodeRemovedEvent(nodeChangeEvent);
        
        then(config).should().clearTree(configProperty);
        shouldNotModify(baseNode, eventNode, childNode);
    }

    /**
     * Test for {@link PreferencesHandler#onConfigurationAddPropertyEvent(NodeChangeEvent)}.
     */
    @Test
    void testOnConfigurationAddPropertyEvent() {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        
        willDoNothing().given(handler).handleConfigurationSetPropertyEvent(any());
        
        handler.onConfigurationAddPropertyEvent(configurationEvent);
        
        final InOrder inOrder = inOrder(testCoordinator, handler);
        then(testCoordinator).should(inOrder).handleConfigurationEvent(same(configurationEvent), any());
        then(handler).should(inOrder).handleConfigurationSetPropertyEvent(configurationEvent);
    }

    /**
     * Test for {@link PreferencesHandler#onConfigurationSetPropertyEvent(NodeChangeEvent)}.
     */
    @Test
    void testOnConfigurationSetPropertyEvent() {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        
        willDoNothing().given(handler).handleConfigurationSetPropertyEvent(any());
        
        handler.onConfigurationSetPropertyEvent(configurationEvent);
        
        final InOrder inOrder = inOrder(testCoordinator, handler);
        then(testCoordinator).should(inOrder).handleConfigurationEvent(same(configurationEvent), any());
        then(handler).should(inOrder).handleConfigurationSetPropertyEvent(configurationEvent);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationSetPropertyEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @CartesianTest
    void testHandleConfigurationSetPropertyEvent(
            final boolean beforeUpdate,
            final boolean addEvent)
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoSave(true);
        final EventType<ConfigurationEvent> eventType = addEvent ?
                ConfigurationEvent.ADD_PROPERTY :
                ConfigurationEvent.SET_PROPERTY;
        final String eventProperty = "mockConfigProperty";
        final Object eventValue = new Object();
        
        willReturn(eventType).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(eventValue).given(configurationEvent).getPropertyValue();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(beforeUpdate).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationSetPropertyEvent(configurationEvent);
        
        if (!beforeUpdate) {
            final InOrder inOrder = inOrder(mapper, baseNode);
            then(mapper).should(inOrder).setProperty(config, baseNode, eventProperty, eventValue);
            then(mapper).shouldHaveNoMoreInteractions();
            then(baseNode).should(inOrder).flush();
        } else {
            then(mapper).shouldHaveNoInteractions();
            shouldNotModify(baseNode);
        }
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationSetPropertyEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationSetPropertyEvent_NoConfig()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setAutoSave(true);
        final String eventProperty = "mockConfigProperty";
        final Object eventValue = new Object();
        
        willReturn(ConfigurationEvent.SET_PROPERTY).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(eventValue).given(configurationEvent).getPropertyValue();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(false).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationSetPropertyEvent(configurationEvent);
        
        then(mapper).shouldHaveNoInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationSetPropertyEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationSetPropertyEvent_NoBaseNode()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setContent(config);
        handler.setAutoSave(true);
        final String eventProperty = "mockConfigProperty";
        final Object eventValue = new Object();
        
        willReturn(ConfigurationEvent.SET_PROPERTY).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(eventValue).given(configurationEvent).getPropertyValue();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(false).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationSetPropertyEvent(configurationEvent);
        
        then(mapper).shouldHaveNoInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationSetPropertyEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationSetPropertyEvent_NoAutoSave()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        final String eventProperty = "mockConfigProperty";
        final Object eventValue = new Object();
        
        willReturn(ConfigurationEvent.SET_PROPERTY).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(eventValue).given(configurationEvent).getPropertyValue();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(false).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationSetPropertyEvent(configurationEvent);
        
        then(mapper).shouldHaveNoInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationSetPropertyEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationSetPropertyEvent_SetError()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoSave(true);
        final String eventProperty = "mockConfigProperty";
        final Object eventValue = new Object();
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException();
        
        willReturn(ConfigurationEvent.SET_PROPERTY).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(eventValue).given(configurationEvent).getPropertyValue();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(false).given(configurationEvent).isBeforeUpdate();
        willThrow(mockEx).given(mapper).setProperty(config, baseNode, eventProperty, eventValue);
        
        handler.handleConfigurationSetPropertyEvent(configurationEvent);
        
        then(mapper).should().setProperty(config, baseNode, eventProperty, eventValue);
        then(mapper).shouldHaveNoMoreInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#onConfigurationClearPropertyEvent(NodeChangeEvent)}.
     */
    @Test
    void testOnConfigurationClearPropertyEvent() {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        
        willDoNothing().given(handler).handleConfigurationClearPropertyEvent(any());
        
        handler.onConfigurationClearPropertyEvent(configurationEvent);
        
        final InOrder inOrder = inOrder(testCoordinator, handler);
        then(testCoordinator).should(inOrder).handleConfigurationEvent(same(configurationEvent), any());
        then(handler).should(inOrder).handleConfigurationClearPropertyEvent(configurationEvent);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationClearPropertyEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @ParameterizedTest
    @ValueSource(booleans={ false, true })
    void testHandleConfigurationClearPropertyEvent(
            final boolean beforeUpdate)
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoSave(true);
        final String eventProperty = "mockConfigProperty";
        
        willReturn(ConfigurationEvent.CLEAR_PROPERTY).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(beforeUpdate).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationClearPropertyEvent(configurationEvent);
        
        if (beforeUpdate) {
            final InOrder inOrder = inOrder(mapper, baseNode);
            then(mapper).should(inOrder).removeProperty(config, baseNode, eventProperty);
            then(mapper).shouldHaveNoMoreInteractions();
            then(baseNode).should(inOrder).flush();
        } else {
            then(mapper).shouldHaveNoInteractions();
            shouldNotModify(baseNode);
        }
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationClearPropertyEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationClearPropertyEvent_NoConfig()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setAutoSave(true);
        final String eventProperty = "mockConfigProperty";
        
        willReturn(ConfigurationEvent.CLEAR_PROPERTY).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(false).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationClearPropertyEvent(configurationEvent);
        
        then(mapper).shouldHaveNoMoreInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationClearPropertyEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationClearPropertyEvent_NoBaseNode()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setContent(config);
        handler.setAutoSave(true);
        final String eventProperty = "mockConfigProperty";
        
        willReturn(ConfigurationEvent.CLEAR_PROPERTY).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(false).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationClearPropertyEvent(configurationEvent);
        
        then(mapper).shouldHaveNoMoreInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationClearPropertyEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationClearPropertyEvent_NoAutoSave()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        final String eventProperty = "mockConfigProperty";
        
        willReturn(ConfigurationEvent.CLEAR_PROPERTY).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(false).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationClearPropertyEvent(configurationEvent);
        
        then(mapper).shouldHaveNoMoreInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationClearPropertyEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationClearPropertyEvent_RemoveError()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoSave(true);
        final String eventProperty = "mockConfigProperty";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException();
        
        willReturn(ConfigurationEvent.CLEAR_PROPERTY).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(true).given(configurationEvent).isBeforeUpdate();
        willThrow(mockEx).given(mapper).removeProperty(config, baseNode, eventProperty);
        
        handler.handleConfigurationClearPropertyEvent(configurationEvent);
        
        then(mapper).should().removeProperty(config, baseNode, eventProperty);
        then(mapper).shouldHaveNoMoreInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#onConfigurationAddNodesEvent(NodeChangeEvent)}.
     */
    @Test
    void testOnConfigurationAddNodeEvent() {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        
        willDoNothing().given(handler).handleConfigurationAddNodesEvent(any());
        
        handler.onConfigurationAddNodesEvent(configurationEvent);
        
        final InOrder inOrder = inOrder(testCoordinator, handler);
        then(testCoordinator).should(inOrder).handleConfigurationEvent(same(configurationEvent), any());
        then(handler).should(inOrder).handleConfigurationAddNodesEvent(configurationEvent);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationAddNodeEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @ParameterizedTest
    @ValueSource(booleans={ false, true })
    void testHandleConfigurationAddNodeEvent(
            final boolean beforeUpdate)
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoSave(true);
        final String eventProperty = "mockConfigProperty";
        final Collection<Object> newNodes = Arrays.asList(new Object(), new Object());
        
        willReturn(ConfigurationEvent.ADD_NODES).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(newNodes).given(configurationEvent).getPropertyValue();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(beforeUpdate).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationAddNodesEvent(configurationEvent);
        
        if (!beforeUpdate) {
            final InOrder inOrder = inOrder(mapper, baseNode);
            then(mapper).should(inOrder).addNodes(config, baseNode, eventProperty, newNodes);
            then(mapper).shouldHaveNoMoreInteractions();
            then(baseNode).should(inOrder).flush();
        } else {
            then(mapper).shouldHaveNoInteractions();
            shouldNotModify(baseNode);
        }
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationAddNodeEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationAddNodeEvent_NoConfig()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setAutoSave(true);
        final String eventProperty = "mockConfigProperty";
        final Collection<Object> newNodes = Arrays.asList(new Object(), new Object());
        
        willReturn(ConfigurationEvent.ADD_NODES).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(newNodes).given(configurationEvent).getPropertyValue();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(true).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationAddNodesEvent(configurationEvent);
        
        then(mapper).shouldHaveNoInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationAddNodeEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationAddNodeEvent_NoBaseNode()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setContent(config);
        handler.setAutoSave(true);
        final String eventProperty = "mockConfigProperty";
        final Collection<Object> newNodes = Arrays.asList(new Object(), new Object());
        
        willReturn(ConfigurationEvent.ADD_NODES).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(newNodes).given(configurationEvent).getPropertyValue();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(true).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationAddNodesEvent(configurationEvent);
        
        then(mapper).shouldHaveNoInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationAddNodeEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationAddNodeEvent_NoAutoSave()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        final String eventProperty = "mockConfigProperty";
        final Collection<Object> newNodes = Arrays.asList(new Object(), new Object());
        
        willReturn(ConfigurationEvent.ADD_NODES).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(newNodes).given(configurationEvent).getPropertyValue();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(true).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationAddNodesEvent(configurationEvent);
        
        then(mapper).shouldHaveNoInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationAddNodeEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationAddNodeEvent_AddError()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoSave(true);
        final String eventProperty = "mockConfigProperty";
        final Collection<Object> newNodes = Arrays.asList(new Object(), new Object());
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException();
        
        willReturn(ConfigurationEvent.ADD_NODES).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(newNodes).given(configurationEvent).getPropertyValue();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(false).given(configurationEvent).isBeforeUpdate();
        willThrow(mockEx).given(mapper).addNodes(config, baseNode, eventProperty, newNodes);
        
        handler.handleConfigurationAddNodesEvent(configurationEvent);
        
        then(mapper).should().addNodes(config, baseNode, eventProperty, newNodes);
        then(mapper).shouldHaveNoMoreInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#onConfigurationClearTreeEvent(NodeChangeEvent)}.
     */
    @Test
    void testOnConfigurationClearTreeEvent() {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        
        willDoNothing().given(handler).handleConfigurationClearTreeEvent(any());
        
        handler.onConfigurationClearTreeEvent(configurationEvent);
        
        final InOrder inOrder = inOrder(testCoordinator, handler);
        then(testCoordinator).should(inOrder).handleConfigurationEvent(same(configurationEvent), any());
        then(handler).should(inOrder).handleConfigurationClearTreeEvent(configurationEvent);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationClearTreeEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @ParameterizedTest
    @ValueSource(booleans={ false, true })
    void testHandleConfigurationClearTreeEvent(
            final boolean beforeUpdate)
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoSave(true);
        final String eventProperty = "mockConfigProperty";
        
        willReturn(ConfigurationEvent.CLEAR_TREE).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(beforeUpdate).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationClearTreeEvent(configurationEvent);
        
        if (beforeUpdate) {
            final InOrder inOrder = inOrder(mapper, baseNode);
            then(mapper).should(inOrder).removeNode(config, baseNode, eventProperty);
            then(mapper).shouldHaveNoMoreInteractions();
            then(baseNode).should(inOrder).flush();
        } else {
            then(mapper).shouldHaveNoInteractions();
            shouldNotModify(baseNode);
        }
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationClearTreeEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationClearTreeEvent_NoConfig()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setAutoSave(true);
        final String eventProperty = "mockConfigProperty";
        
        willReturn(ConfigurationEvent.CLEAR_TREE).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(true).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationClearTreeEvent(configurationEvent);
        
        then(mapper).shouldHaveNoInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationClearTreeEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationClearTreeEvent_NoBaseNode()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setContent(config);
        handler.setAutoSave(true);
        final String eventProperty = "mockConfigProperty";
        
        willReturn(ConfigurationEvent.CLEAR_TREE).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(true).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationClearTreeEvent(configurationEvent);
        
        then(mapper).shouldHaveNoInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationClearTreeEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationClearTreeEvent_NoAutoSave()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        final String eventProperty = "mockConfigProperty";
        
        willReturn(ConfigurationEvent.CLEAR_TREE).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(true).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationClearTreeEvent(configurationEvent);
        
        then(mapper).shouldHaveNoInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationClearTreeEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationClearTreeEvent_RemoveError()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoSave(true);
        final String eventProperty = "mockConfigProperty";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException();
        
        willReturn(ConfigurationEvent.CLEAR_TREE).given(configurationEvent).getEventType();
        willReturn(eventProperty).given(configurationEvent).getPropertyName();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(true).given(configurationEvent).isBeforeUpdate();
        willThrow(mockEx).given(mapper).removeNode(config, baseNode, eventProperty);
        
        handler.handleConfigurationClearTreeEvent(configurationEvent);
        
        then(mapper).should().removeNode(config, baseNode, eventProperty);
        then(mapper).shouldHaveNoMoreInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#onConfigurationClearEvent(NodeChangeEvent)}.
     */
    @Test
    void testOnConfigurationClearEvent() {
        final ByThreadEventCoordinationStrategy testCoordinator = spy(new ByThreadEventCoordinationStrategy());
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper, testCoordinator));
        
        willDoNothing().given(handler).handleConfigurationClearEvent(any());
        
        handler.onConfigurationClearEvent(configurationEvent);
        
        final InOrder inOrder = inOrder(testCoordinator, handler);
        then(testCoordinator).should(inOrder).handleConfigurationEvent(same(configurationEvent), any());
        then(handler).should(inOrder).handleConfigurationClearEvent(configurationEvent);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationClearEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @ParameterizedTest
    @ValueSource(booleans={ false, true })
    void testHandleConfigurationClearEvent(
            final boolean beforeUpdate)
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoSave(true);
        
        willReturn(ConfigurationEvent.CLEAR).given(configurationEvent).getEventType();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(beforeUpdate).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationClearEvent(configurationEvent);
        
        if (beforeUpdate) {
            final InOrder inOrder = inOrder(mapper, baseNode);
            then(mapper).should(inOrder).removeNode(config, baseNode, null);
            then(mapper).shouldHaveNoMoreInteractions();
            then(baseNode).should(inOrder).flush();
        } else {
            then(mapper).shouldHaveNoInteractions();
            shouldNotModify(baseNode);
        }
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationClearEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationClearEvent_NoConfig()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setAutoSave(true);
        
        willReturn(ConfigurationEvent.CLEAR).given(configurationEvent).getEventType();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(true).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationClearEvent(configurationEvent);
        
        then(mapper).shouldHaveNoMoreInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationClearEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationClearEvent_NoBaseNode()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setContent(config);
        handler.setAutoSave(true);
        
        willReturn(ConfigurationEvent.CLEAR).given(configurationEvent).getEventType();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(true).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationClearEvent(configurationEvent);
        
        then(mapper).shouldHaveNoMoreInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationClearEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationClearEvent_NoAutoSave()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        
        willReturn(ConfigurationEvent.CLEAR).given(configurationEvent).getEventType();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(true).given(configurationEvent).isBeforeUpdate();
        
        handler.handleConfigurationClearEvent(configurationEvent);
        
        then(mapper).shouldHaveNoMoreInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    /**
     * Test for {@link PreferencesHandler#handleConfigurationClearEvent(ConfigurationEvent)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testHandleConfigurationClearEvent_RemoveError()
    throws BackingStoreException {
        final PreferencesHandler<Object> handler = spy(new PreferencesHandler<>(mapper));
        handler.setBaseNode(baseNode);
        handler.setContent(config);
        handler.setAutoSave(true);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException();
        
        willReturn(ConfigurationEvent.CLEAR).given(configurationEvent).getEventType();
        willReturn(config).given(configurationEvent).getSource();
        willReturn(true).given(configurationEvent).isBeforeUpdate();
        willThrow(mockEx).given(mapper).removeNode(config, baseNode, null);
        
        handler.handleConfigurationClearEvent(configurationEvent);
        
        then(mapper).should().removeNode(config, baseNode, null);
        then(mapper).shouldHaveNoMoreInteractions();
        shouldNotModify(baseNode);
        shouldNotModify(config);
    }

    private void shouldNotModify(
            final @NotNull Preferences... prefs) {
        try {
            for (final Preferences pref : prefs) {
                then(pref).should(never()).clear();
                then(pref).should(never()).node(any());
                then(pref).should(never()).put(any(), any());
                then(pref).should(never()).putBoolean(any(), anyBoolean());
                then(pref).should(never()).putByteArray(any(), any());
                then(pref).should(never()).putDouble(any(), anyDouble());
                then(pref).should(never()).putFloat(any(), anyFloat());
                then(pref).should(never()).putInt(any(), anyInt());
                then(pref).should(never()).putLong(any(), anyLong());
                then(pref).should(never()).remove(any());
                then(pref).should(never()).removeNode();
            }
        } catch (BackingStoreException bse) {
            throw new RuntimeException(bse);
        }
    }

    private void shouldNotModify(
            final PreferencesBased<Object> config) {
        then(config).should(never()).clear();
        then(config).should(never()).load(any());
        then(config).should(never()).addNodes(any(), any());
        then(config).should(never()).addProperty(any(), any());
        then(config).should(never()).setProperty(any(), any());
        then(config).should(never()).clearProperty(any());
        then(config).should(never()).clearTree(any());
    }
}
