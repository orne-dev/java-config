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

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import org.apache.commons.configuration2.tree.ExpressionEngine;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.CartesianProductTest;

/**
 * Unit tests for {@code PreferencesBuilderParametersImpl}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see PreferencesBuilderParametersImpl
 */
@Tag("ut")
class PreferencesBuilderParametersImplTest {

    /**
     * Test for {@link PreferencesBuilderParametersImpl#PreferencesBuilderParametersImpl()}.
     */
    @Test
    void testConstructor() {
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(params, result.get(PreferencesBuilderParametersImpl.PARAM_KEY));
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#setExpressionEngine(ExpressionEngine)}.
     */
    @Test
    void testSetExpressionEngine() {
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        final ExpressionEngine engine = mock(ExpressionEngine.class);
        final PreferencesBuilderParametersImpl chain = params.setExpressionEngine(engine);
        assertSame(params, chain);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertSame(engine, result.get(PreferencesConfiguration.PROP_EXPRESSION_ENGINE));
        
        then(engine).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#setExpressionEngine(ExpressionEngine)}.
     */
    @Test
    void testSetExpressionEngine_Null() {
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        final PreferencesBuilderParametersImpl chain = params.setExpressionEngine(null);
        assertSame(params, chain);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertNull(result.get(PreferencesConfiguration.PROP_EXPRESSION_ENGINE));
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#setPreferencesMapper(PreferencesMapper)}.
     */
    @Test
    void testSetPreferencesMapper() {
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        @SuppressWarnings("unchecked")
        final PreferencesMapper<ImmutableNode> mapper = mock(PreferencesMapper.class);
        final PreferencesBuilderParametersImpl chain = params.setPreferencesMapper(mapper);
        assertSame(params, chain);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertSame(mapper, result.get(PreferencesConfigurationBuilder.PROP_MAPPER));
        
        then(mapper).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#setPreferencesMapper(PreferencesMapper)}.
     */
    @Test
    void testSetPreferencesMapper_Null() {
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        final PreferencesBuilderParametersImpl chain = params.setPreferencesMapper(null);
        assertSame(params, chain);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertNull(result.get(PreferencesConfigurationBuilder.PROP_MAPPER));
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#setSystemScope(boolean)}.
     */
    @ParameterizedTest
    @ValueSource(booleans={ true, false })
    void testSetSystemScope(final boolean value) {
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        final PreferencesBuilderParametersImpl chain = params.setSystemScope(value);
        assertSame(params, chain);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertEquals(value, result.get(PreferencesHandler.PROP_SYSTEM_TREE));
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#setBaseClass(Class)}.
     */
    @Test
    void testSetBaseClass() {
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        final PreferencesBuilderParametersImpl chain = params.setBaseClass(
                PreferencesBuilderParametersImplTest.class);
        assertSame(params, chain);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertSame(PreferencesBuilderParametersImplTest.class, result.get(PreferencesHandler.PROP_BASE_CLASS));
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#setBaseClass(Class)}.
     */
    @Test
    void testSetBaseClass_Null() {
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        final PreferencesBuilderParametersImpl chain = params.setBaseClass(null);
        assertSame(params, chain);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertNull(result.get(PreferencesHandler.PROP_BASE_CLASS));
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#setPath(String)}.
     */
    @Test
    void testSetPath() {
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        final String path = "mock path";
        final PreferencesBuilderParametersImpl chain = params.setPath(path);
        assertSame(params, chain);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertEquals(path, result.get(PreferencesHandler.PROP_PATH));
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#setPath(String)}.
     */
    @Test
    void testSetPath_Null() {
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        final PreferencesBuilderParametersImpl chain = params.setPath(null);
        assertSame(params, chain);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertNull(result.get(PreferencesHandler.PROP_PATH));
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#setBaseNode(Preferences)}.
     */
    @Test
    void testSetBaseNode() {
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        final Preferences node = mock(Preferences.class);
        final PreferencesBuilderParametersImpl chain = params.setBaseNode(node);
        assertSame(params, chain);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertSame(node, result.get(PreferencesHandler.PROP_BASE_NODE));
        
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#setBaseNode(Preferences)}.
     */
    @Test
    void testSetBaseNode_Null() {
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        final PreferencesBuilderParametersImpl chain = params.setBaseNode(null);
        assertSame(params, chain);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertNull(result.get(PreferencesHandler.PROP_BASE_NODE));
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#setAutoLoad(boolean)}.
     */
    @ParameterizedTest
    @ValueSource(booleans={ true, false })
    void testSetAutoLoad(final boolean value) {
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        final PreferencesBuilderParametersImpl chain = params.setAutoLoad(value);
        assertSame(params, chain);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertEquals(value, result.get(PreferencesHandler.PROP_AUTO_LOAD));
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#setAutoSave(boolean)}.
     */
    @ParameterizedTest
    @ValueSource(booleans={ true, false })
    void testSetAutoSave(final boolean value) {
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        final PreferencesBuilderParametersImpl chain = params.setAutoSave(value);
        assertSame(params, chain);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertEquals(value, result.get(PreferencesHandler.PROP_AUTO_SAVE));
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#setEventCoordinationStrategy(EventCoordinationStrategy)}.
     */
    @Test
    void testSetEventCoordinationStrategy() {
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        final EventCoordinationStrategy strategy = mock(EventCoordinationStrategy.class);
        final PreferencesBuilderParametersImpl chain = params.setEventCoordinationStrategy(strategy);
        assertSame(params, chain);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertSame(strategy, result.get(PreferencesHandler.PROP_EVENT_COORDINATOR));
        
        then(strategy).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#setEventCoordinationStrategy(EventCoordinationStrategy)}.
     */
    @Test
    void testSetEventCoordinationStrategy_Null() {
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        final PreferencesBuilderParametersImpl chain = params.setEventCoordinationStrategy(null);
        assertSame(params, chain);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertNull(result.get(PreferencesHandler.PROP_EVENT_COORDINATOR));
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#inheritFrom(Map)}.
     */
    @CartesianProductTest()
    void testInheritFrom(
            final boolean nullExpressionEngine,
            final boolean nullPreferencesMapper,
            final boolean nullSystemScope,
            final boolean nullBaseClass,
            final boolean nullPath,
            final boolean nullBaseNode,
            final boolean nullAutoLoad,
            final boolean nullAutoSave,
            final boolean nullEventCoordStr) {
        final ExpressionEngine engine = nullExpressionEngine ? null : mock(ExpressionEngine.class);
        @SuppressWarnings("unchecked")
        final PreferencesMapper<ImmutableNode> mapper = nullPreferencesMapper ? null : mock(PreferencesMapper.class);
        final Boolean systemScope = nullSystemScope ? null : RandomUtils.nextBoolean();
        final Class<?> baseClass = nullBaseClass ? null : PreferencesBuilderParametersImplTest.class;
        final String path = nullPath ? null : "mock path";
        final Preferences baseNode = nullBaseNode ? null : mock(Preferences.class);
        final Boolean autoLoad = nullAutoLoad ? null : RandomUtils.nextBoolean();
        final Boolean autoSave = nullAutoSave ? null : RandomUtils.nextBoolean();
        final EventCoordinationStrategy eventCoordStr = nullEventCoordStr ? null : mock(EventCoordinationStrategy.class);
        
        final Map<String, Object> map = new HashMap<>();
        map.put(PreferencesConfiguration.PROP_EXPRESSION_ENGINE, engine);
        map.put(PreferencesConfigurationBuilder.PROP_MAPPER, mapper);
        map.put(PreferencesHandler.PROP_SYSTEM_TREE, systemScope);
        map.put(PreferencesHandler.PROP_BASE_CLASS, baseClass);
        map.put(PreferencesHandler.PROP_PATH, path);
        map.put(PreferencesHandler.PROP_BASE_NODE, baseNode);
        map.put(PreferencesHandler.PROP_AUTO_LOAD, autoLoad);
        map.put(PreferencesHandler.PROP_AUTO_SAVE, autoSave);
        map.put(PreferencesHandler.PROP_EVENT_COORDINATOR, eventCoordStr);
        
        final PreferencesBuilderParametersImpl params = new PreferencesBuilderParametersImpl();
        params.inheritFrom(map);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertEquals(engine, result.get(PreferencesConfiguration.PROP_EXPRESSION_ENGINE));
        assertEquals(mapper, result.get(PreferencesConfigurationBuilder.PROP_MAPPER));
        assertEquals(systemScope, result.get(PreferencesHandler.PROP_SYSTEM_TREE));
        assertEquals(baseClass, result.get(PreferencesHandler.PROP_BASE_CLASS));
        assertEquals(path, result.get(PreferencesHandler.PROP_PATH));
        assertEquals(baseNode, result.get(PreferencesHandler.PROP_BASE_NODE));
        assertEquals(autoLoad, result.get(PreferencesHandler.PROP_AUTO_LOAD));
        assertEquals(autoSave, result.get(PreferencesHandler.PROP_AUTO_SAVE));
        assertEquals(eventCoordStr, result.get(PreferencesHandler.PROP_EVENT_COORDINATOR));
    }

    /**
     * Values for {@link #testInheritFrom(boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean)}.
     */
    static CartesianProductTest.Sets testInheritFrom() {
        return new CartesianProductTest.Sets()
                .add(false, true)
                .add(false, true)
                .add(false, true)
                .add(false, true)
                .add(false, true)
                .add(false, true)
                .add(false, true)
                .add(false, true)
                .add(false, true);
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#fromParameters(Map)}.
     */
    @ParameterizedTest
    @ValueSource(booleans={ true, false })
    void testFromParameters(
            final boolean present) {
        final PreferencesBuilderParametersImpl params;
        final Map<String, Object> map = new HashMap<>();
        if (present) {
            params = new PreferencesBuilderParametersImpl();
            map.put(PreferencesBuilderParametersImpl.PARAM_KEY, params);
        } else {
            params = null;
        }
        
        final PreferencesBuilderParametersImpl result =
                PreferencesBuilderParametersImpl.fromParameters(map);
        if (present) {
            assertSame(params, result);
        } else {
            assertNull(result);
        }
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#fromParameters(Map)}.
     */
    @Test
    void testFromParameters_Null() {
        assertThrows(NullPointerException.class, () -> {
            PreferencesBuilderParametersImpl.fromParameters(null);
        });
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#fromParameters(Map, boolean)}.
     */
    @CartesianProductTest()
    void testFromParameters_Create(
            final boolean present,
            final boolean create) {
        final PreferencesBuilderParametersImpl params;
        final Map<String, Object> map = new HashMap<>();
        if (present) {
            params = new PreferencesBuilderParametersImpl();
            map.put(PreferencesBuilderParametersImpl.PARAM_KEY, params);
        } else {
            params = null;
        }
        
        final PreferencesBuilderParametersImpl result =
                PreferencesBuilderParametersImpl.fromParameters(map, create);
        if (present) {
            assertSame(params, result);
        } else if (create) {
            assertNotNull(result);
        } else {
            assertNull(result);
        }
    }

    /**
     * Values for {@link #testFromParameters_Create(boolean, boolean)}.
     */
    static CartesianProductTest.Sets testFromParameters_Create() {
        return new CartesianProductTest.Sets()
                .add(false, true)
                .add(false, true);
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#fromParameters(Map, boolean)}.
     */
    @ParameterizedTest
    @ValueSource(booleans={ true, false })
    void testFromParameters_Create_Null(
            final boolean create) {
        assertThrows(NullPointerException.class, () -> {
            PreferencesBuilderParametersImpl.fromParameters(null, create);
        });
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#fromMap(Map)}.
     */
    @CartesianProductTest()
    void testFromMap(
            final boolean nullExpressionEngine,
            final boolean nullPreferencesMapper,
            final boolean nullSystemScope,
            final boolean nullBaseClass,
            final boolean nullPath,
            final boolean nullBaseNode,
            final boolean nullAutoLoad,
            final boolean nullAutoSave,
            final boolean nullEventCoordStr) {
        final ExpressionEngine engine = nullExpressionEngine ? null : mock(ExpressionEngine.class);
        @SuppressWarnings("unchecked")
        final PreferencesMapper<ImmutableNode> mapper = nullPreferencesMapper ? null : mock(PreferencesMapper.class);
        final Boolean systemScope = nullSystemScope ? null : RandomUtils.nextBoolean();
        final Class<?> baseClass = nullBaseClass ? null : PreferencesBuilderParametersImplTest.class;
        final String path = nullPath ? null : "mock path";
        final Preferences baseNode = nullBaseNode ? null : mock(Preferences.class);
        final Boolean autoLoad = nullAutoLoad ? null : RandomUtils.nextBoolean();
        final Boolean autoSave = nullAutoSave ? null : RandomUtils.nextBoolean();
        final EventCoordinationStrategy eventCoordStr = nullEventCoordStr ? null : mock(EventCoordinationStrategy.class);
        
        final Map<String, Object> map = new HashMap<>();
        map.put(PreferencesConfiguration.PROP_EXPRESSION_ENGINE, engine);
        map.put(PreferencesConfigurationBuilder.PROP_MAPPER, mapper);
        map.put(PreferencesHandler.PROP_SYSTEM_TREE, systemScope);
        map.put(PreferencesHandler.PROP_BASE_CLASS, baseClass);
        map.put(PreferencesHandler.PROP_PATH, path);
        map.put(PreferencesHandler.PROP_BASE_NODE, baseNode);
        map.put(PreferencesHandler.PROP_AUTO_LOAD, autoLoad);
        map.put(PreferencesHandler.PROP_AUTO_SAVE, autoSave);
        map.put(PreferencesHandler.PROP_EVENT_COORDINATOR, eventCoordStr);
        
        final PreferencesBuilderParametersImpl params = PreferencesBuilderParametersImpl.fromMap(map);
        assertNotNull(params);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertSame(params, result.get(PreferencesBuilderParametersImpl.PARAM_KEY));
        assertEquals(engine, result.get(PreferencesConfiguration.PROP_EXPRESSION_ENGINE));
        assertEquals(mapper, result.get(PreferencesConfigurationBuilder.PROP_MAPPER));
        assertEquals(systemScope, result.get(PreferencesHandler.PROP_SYSTEM_TREE));
        assertEquals(baseClass, result.get(PreferencesHandler.PROP_BASE_CLASS));
        assertEquals(path, result.get(PreferencesHandler.PROP_PATH));
        assertEquals(baseNode, result.get(PreferencesHandler.PROP_BASE_NODE));
        assertEquals(autoLoad, result.get(PreferencesHandler.PROP_AUTO_LOAD));
        assertEquals(autoSave, result.get(PreferencesHandler.PROP_AUTO_SAVE));
        assertEquals(eventCoordStr, result.get(PreferencesHandler.PROP_EVENT_COORDINATOR));
    }

    /**
     * Values for {@link #testFromMap(boolean, boolean, boolean, boolean, boolean, boolean, boolean, boolean)}.
     */
    static CartesianProductTest.Sets testFromMap() {
        return new CartesianProductTest.Sets()
                .add(false, true)
                .add(false, true)
                .add(false, true)
                .add(false, true)
                .add(false, true)
                .add(false, true)
                .add(false, true)
                .add(false, true)
                .add(false, true);
    }

    /**
     * Test for {@link PreferencesBuilderParametersImpl#fromMap(Map)}.
     */
    @Test
    void testFromMap_Null() {
        final PreferencesBuilderParametersImpl params = PreferencesBuilderParametersImpl.fromMap(null);
        
        final Map<String, Object> result = params.getParameters();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(params, result.get(PreferencesBuilderParametersImpl.PARAM_KEY));
    }
}
