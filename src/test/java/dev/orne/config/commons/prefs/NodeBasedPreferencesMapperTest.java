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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;
import org.apache.commons.configuration2.tree.ExpressionEngine;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.commons.configuration2.tree.NodeHandler;
import org.apache.commons.configuration2.tree.NodeModel;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@code NodeBasedPreferencesMapper}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see NodeBasedPreferencesMapper
 */
@Tag("ut")
class NodeBasedPreferencesMapperTest {

    /**
     * Test for {@link NodeBasedPreferencesMapper#resolvePropertyKey(PreferencesBased, Preferences, Preferences, String)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testResolvePropertyKey() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final PreferencesBased<ImmutableNode> config = mock(PreferencesBased.class);
        final NodeModel<ImmutableNode> model = mock(NodeModel.class);
        final NodeHandler<ImmutableNode> handler = mock(NodeHandler.class);
        final ExpressionEngine expressionEngine = mock(ExpressionEngine.class);
        final Preferences baseNode = mock(Preferences.class);
        final Preferences node = mock(Preferences.class);
        final ImmutableNode nameNode = new ImmutableNode.Builder().create();
        final String property = "mockProperty";
        final String parentProperty = "mockParentProperty";
        final String mockPropertyKey = "mockProperty";
        
        willReturn(model).given(config).getNodeModel();
        willReturn(handler).given(model).getNodeHandler();
        willReturn(expressionEngine).given(config).getExpressionEngine();
        willReturn(parentProperty).given(mapper).resolveNodeKey(config, baseNode, node);
        willReturn(nameNode).given(mapper).createNodeForName(property);
        willReturn(mockPropertyKey).given(expressionEngine).nodeKey(nameNode, parentProperty, handler);
        
        final String result = mapper.resolvePropertyKey(config, baseNode, node, property);
        assertEquals(mockPropertyKey, result);
        
        then(mapper).should(times(1)).resolveNodeKey(config, baseNode, node);
        then(mapper).should(times(1)).createNodeForName(property);
        then(expressionEngine).should(times(1)).nodeKey(nameNode, parentProperty, handler);
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#resolvePropertyKey(PreferencesBased, Preferences, Preferences, String)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testResolvePropertyKey_ResolveNodeKey_Error() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final PreferencesBased<ImmutableNode> config = mock(PreferencesBased.class);
        final NodeModel<ImmutableNode> model = mock(NodeModel.class);
        final NodeHandler<ImmutableNode> handler = mock(NodeHandler.class);
        final ExpressionEngine expressionEngine = mock(ExpressionEngine.class);
        final Preferences baseNode = mock(Preferences.class);
        final Preferences node = mock(Preferences.class);
        final ImmutableNode nameNode = new ImmutableNode.Builder().create();
        final String property = "mockProperty";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(model).given(config).getNodeModel();
        willReturn(handler).given(model).getNodeHandler();
        willReturn(expressionEngine).given(config).getExpressionEngine();
        willThrow(mockEx).given(mapper).resolveNodeKey(config, baseNode, node);
        willReturn(nameNode).given(mapper).createNodeForName(property);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.resolvePropertyKey(config, baseNode, node, property);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).resolveNodeKey(config, baseNode, node);
        then(mapper).should(atLeast(0)).createNodeForName(property);
        then(expressionEngine).should(never()).nodeKey(any(), any(), any());
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#getValue(Preferences)}.
     */
    @Test
    void testGetValue() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String nodeName = "mockName";
        final Preferences parentNode = mock(Preferences.class);
        final String mockValue = "mockValue";
        
        willReturn(nodeName).given(node).name();
        willReturn(parentNode).given(node).parent();
        willReturn(mockValue).given(mapper).getChildValue(parentNode, nodeName);
        
        final String result = mapper.getValue(node);
        assertEquals(mockValue, result);
        
        then(mapper).should(times(1)).getChildValue(parentNode, nodeName);
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#getValue(Preferences)}.
     */
    @Test
    void testGetValue_NodeDeleted() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String nodeName = "mockName";
        final IllegalStateException mockEx = new IllegalStateException("mock exception");
        
        willReturn(nodeName).given(node).name();
        willThrow(mockEx).given(node).parent();
        
        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.getValue(node);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(mapper).should(never()).getChildValue(any(), any());
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#getValue(Preferences)}.
     */
    @Test
    void testGetValue_GetChildValue_Error() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String nodeName = "mockName";
        final Preferences parentNode = mock(Preferences.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(nodeName).given(node).name();
        willReturn(parentNode).given(node).parent();
        willThrow(mockEx).given(mapper).getChildValue(parentNode, nodeName);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.getValue(node);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getChildValue(parentNode, nodeName);
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#setValue(Preferences, String)}.
     */
    @Test
    void testSetValue() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String value = "mockValue";
        final String nodeName = "mockName";
        final Preferences parentNode = mock(Preferences.class);
        
        willReturn(nodeName).given(node).name();
        willReturn(parentNode).given(node).parent();
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        
        mapper.setValue(node, value);
        
        then(mapper).should(times(1)).setChildValue(parentNode, nodeName, value);
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#setValue(Preferences, String)}.
     */
    @Test
    void testSetValue_NodeDeleted() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String value = "mockValue";
        final String nodeName = "mockName";
        final IllegalStateException mockEx = new IllegalStateException("mock exception");
        
        willReturn(nodeName).given(node).name();
        willThrow(mockEx).given(node).parent();
        
        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.setValue(node, value);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(mapper).should(never()).setChildValue(any(), any(), any());
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#setValue(Preferences, String)}.
     */
    @Test
    void testGetValue_SetChildValue_Error() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String value = "mockValue";
        final String nodeName = "mockName";
        final Preferences parentNode = mock(Preferences.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(nodeName).given(node).name();
        willReturn(parentNode).given(node).parent();
        willThrow(mockEx).given(mapper).setChildValue(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.setValue(node, value);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).setChildValue(parentNode, nodeName, value);
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#isChildValue(Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @ValueSource(booleans = {true, false})
    @ParameterizedTest
    void testIsChildValue(
            final boolean childNodeExists)
    throws BackingStoreException {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences parent = mock(Preferences.class);
        final String name = "mockName";
        
        willReturn(childNodeExists).given(parent).nodeExists(name);
        
        final boolean result = mapper.isChildValue(parent, name);
        assertEquals(childNodeExists, result);
        
        then(parent).should(times(1)).nodeExists(name);
        then(parent).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#isChildValue(Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testIsChildValue_BackingStoreError()
    throws BackingStoreException {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences parent = mock(Preferences.class);
        final String name = "mockName";
        final BackingStoreException mockEx = new BackingStoreException("mock exception");
        
        willThrow(mockEx).given(parent).nodeExists(name);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.isChildValue(parent, name);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(parent).should(times(1)).nodeExists(name);
        then(parent).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#isChildValue(Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testIsChildValue_NodeDeleted()
    throws BackingStoreException {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences parent = mock(Preferences.class);
        final String name = "mockName";
        final IllegalStateException mockEx = new IllegalStateException("mock exception");
        
        willThrow(mockEx).given(parent).nodeExists(name);
        
        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.isChildValue(parent, name);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(parent).should(times(1)).nodeExists(name);
        then(parent).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#isChildValue(Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testIsChildValue_InvalidKey()
    throws BackingStoreException {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences parent = mock(Preferences.class);
        final String name = "mockName";
        final IllegalArgumentException mockEx = new IllegalArgumentException("mock exception");
        
        willThrow(mockEx).given(parent).nodeExists(name);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.isChildValue(parent, name);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(parent).should(times(1)).nodeExists(name);
        then(parent).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#getChildValue(Preferences, String)}.
     */
    @Test
    void testGetChildValue() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences parent = mock(Preferences.class);
        final String name = "mockName";
        final String mockValue = "mockValue";
        
        willReturn(mockValue).given(parent).get(name, null);
        
        final String result = mapper.getChildValue(parent, name);
        assertEquals(mockValue, result);
        
        then(parent).should(times(1)).get(name, null);
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#getChildValue(Preferences, String)}.
     */
    @Test
    void testGetChildValue_NodeDeleted() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences parent = mock(Preferences.class);
        final String name = "mockName";
        final IllegalStateException mockEx = new IllegalStateException("mock exception");
        
        willThrow(mockEx).given(parent).get(name, null);
        
        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.getChildValue(parent, name);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(parent).should(times(1)).get(name, null);
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#getChildValue(Preferences, String)}.
     */
    @Test
    void testGetChildValue_InvalidKey() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences parent = mock(Preferences.class);
        final String name = "mockName";
        final IllegalArgumentException mockEx = new IllegalArgumentException("mock exception");
        
        willThrow(mockEx).given(parent).get(name, null);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.getChildValue(parent, name);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(parent).should(times(1)).get(name, null);
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#setChildValue(Preferences, String, String)}.
     */
    @Test
    void testSetChildValue() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences parent = mock(Preferences.class);
        final String name = "mockName";
        final String value = "mockValue";
        
        mapper.setChildValue(parent, name, value);
        
        then(parent).should(times(1)).put(name, value);
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#setChildValue(Preferences, String, String)}.
     */
    @Test
    void testSetChildValue_NodeDeleted() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences parent = mock(Preferences.class);
        final String name = "mockName";
        final String value = "mockValue";
        final IllegalStateException mockEx = new IllegalStateException("mock exception");
        
        willThrow(mockEx).given(parent).put(any(), any());
        
        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.setChildValue(parent, name, value);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(parent).should(times(1)).put(name, value);
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#setChildValue(Preferences, String, String)}.
     */
    @Test
    void testSetChildValue_InvalidKey() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences parent = mock(Preferences.class);
        final String name = "mockName";
        final String value = "mockValue";
        final IllegalArgumentException mockEx = new IllegalArgumentException("mock exception");
        
        willThrow(mockEx).given(parent).put(any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.setChildValue(parent, name, value);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(parent).should(times(1)).put(name, value);
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#setChildValue(Preferences, String, String)}.
     */
    @Test
    void testSetChildValue_Null() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences parent = mock(Preferences.class);
        final String name = "mockName";
        
        mapper.setChildValue(parent, name, null);
        
        then(parent).should(times(1)).remove(name);
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#setChildValue(Preferences, String, String)}.
     */
    @Test
    void testSetChildValue_Null_NodeDeleted() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences parent = mock(Preferences.class);
        final String name = "mockName";
        final IllegalStateException mockEx = new IllegalStateException("mock exception");
        
        willThrow(mockEx).given(parent).remove(any());
        
        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.setChildValue(parent, name, null);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(parent).should(times(1)).remove(name);
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#setChildValue(Preferences, String, String)}.
     */
    @Test
    void testSetChildValue_Null_InvalidKey() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences parent = mock(Preferences.class);
        final String name = "mockName";
        final IllegalArgumentException mockEx = new IllegalArgumentException("mock exception");
        
        willThrow(mockEx).given(parent).remove(any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.setChildValue(parent, name, null);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(parent).should(times(1)).remove(name);
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getAttributesNames(Preferences)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testGetAttributesNames()
    throws BackingStoreException {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr1 = "mockAttr1";
        final String mockAttr2 = "mockAttr2";
        final String mockAttr3 = "mockAttr3";
        final String[] attrNames = new String[] { mockAttr1, mockAttr2, mockAttr3 };
        
        willReturn(attrNames).given(node).keys();
        willReturn(false).given(mapper).isChildValue(node, mockAttr1);
        willReturn(true).given(mapper).isChildValue(node, mockAttr2);
        willReturn(false).given(mapper).isChildValue(node, mockAttr3);
        
        final Collection<String> result = mapper.getAttributesNames(node);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(mockAttr1));
        assertTrue(result.contains(mockAttr3));
        
        then(node).should(times(1)).keys();
        then(node).shouldHaveNoMoreInteractions();
        then(mapper).should(times(1)).isChildValue(node, mockAttr1);
        then(mapper).should(times(1)).isChildValue(node, mockAttr2);
        then(mapper).should(times(1)).isChildValue(node, mockAttr3);
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#getAttributesNames(Preferences)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testGetAttributesNames_Keys_BackingStoreError()
    throws BackingStoreException {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final BackingStoreException mockEx = new BackingStoreException("mock exception");
        
        willThrow(mockEx).given(node).keys();
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.getAttributesNames(node);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(node).should(times(1)).keys();
        then(node).shouldHaveNoMoreInteractions();
        then(mapper).should(never()).isChildValue(any(), any());
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#getAttributesNames(Preferences)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testGetAttributesNames_Keys_NodeDeleted()
    throws BackingStoreException {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final IllegalStateException mockEx = new IllegalStateException("mock exception");
        
        willThrow(mockEx).given(node).keys();
        
        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.getAttributesNames(node);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(node).should(times(1)).keys();
        then(node).shouldHaveNoMoreInteractions();
        then(mapper).should(never()).isChildValue(any(), any());
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#getAttributesNames(Preferences)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testGetAttributesNames_NodeExists_Error()
    throws BackingStoreException {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String[] attrNames = new String[] { "mockAttr1", "mockAttr2", "mockAttr3" };
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(attrNames).given(node).keys();
        willThrow(mockEx).given(mapper).isChildValue(any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.getAttributesNames(node);
        });
        assertTrue(mockEx == result || mockEx == result.getCause());
        
        then(node).should(times(1)).keys();
        then(node).shouldHaveNoMoreInteractions();
        then(mapper).should(atLeastOnce()).isChildValue(same(node), any());
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#constructAttributes(ImmutableNode.Builder, Preferences, Map)}.
     */
    @Test
    void testConstructAttributes() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final ImmutableNode.Builder builder = new ImmutableNode.Builder();
        final Preferences prefs = mock(Preferences.class);
        final String child1Name = "mockName1";
        final String child1Value = "mockValue1";
        final ImmutableNode child1Node = new ImmutableNode.Builder().name(child1Name).create();
        final String child2Name = "mockName2";
        final String child2Value = "mockValue2";
        final ImmutableNode child2Node = new ImmutableNode.Builder().name(child2Name).create();
        final Map<String, String> attrs = new HashMap<>();
        attrs.put(child1Name, child1Value);
        attrs.put(child2Name, child2Value);
        @SuppressWarnings("unchecked")
        final Map<ImmutableNode, Preferences> elemRefs = mock(Map.class);
        
        willReturn(attrs).given(mapper).getAttributes(prefs);
        willReturn(child1Node).given(mapper).createPropertyNode(child1Name, child1Value);
        willReturn(child2Node).given(mapper).createPropertyNode(child2Name, child2Value);
        
        mapper.constructAttributes(builder, prefs, elemRefs);
        final ImmutableNode result = builder.create();
        assertTrue(result.getAttributes().isEmpty());
        assertEquals(2, result.getChildren().size());
        assertNotNull(result.getChildren(child1Name));
        assertEquals(1, result.getChildren(child1Name).size());
        assertSame(child1Node, result.getChildren(child1Name).get(0));
        assertNotNull(result.getChildren(child2Name));
        assertEquals(1, result.getChildren(child2Name).size());
        assertSame(child2Node, result.getChildren(child2Name).get(0));
        
        then(mapper).should(times(1)).getAttributes(prefs);
        then(mapper).should(times(1)).createPropertyNode(child1Name, child1Value);
        then(mapper).should(times(1)).createPropertyNode(child2Name, child2Value);
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#constructAttributes(ImmutableNode.Builder, Preferences, Map)}.
     */
    @Test
    void testConstructAttributes_Empty() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final ImmutableNode.Builder builder = new ImmutableNode.Builder();
        final Preferences prefs = mock(Preferences.class);
        final Map<String, String> attrs = Collections.emptyMap();
        @SuppressWarnings("unchecked")
        final Map<ImmutableNode, Preferences> elemRefs = mock(Map.class);
        
        willReturn(attrs).given(mapper).getAttributes(prefs);
        
        mapper.constructAttributes(builder, prefs, elemRefs);
        final ImmutableNode result = builder.create();
        assertTrue(result.getAttributes().isEmpty());
        
        then(mapper).should(times(1)).getAttributes(prefs);
        then(mapper).should(never()).createPropertyNode(any(), any());
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#constructAttributes(ImmutableNode.Builder, Preferences, Map)}.
     */
    @Test
    void testConstructAttributes_GetAttributes_Error() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final ImmutableNode.Builder builder = new ImmutableNode.Builder();
        final Preferences prefs = mock(Preferences.class);
        final String child1Name = "mockName1";
        final String child1Value = "mockValue1";
        final String child2Name = "mockName2";
        final String child2Value = "mockValue2";
        final Map<String, String> attrs = new HashMap<>();
        attrs.put(child1Name, child1Value);
        attrs.put(child2Name, child2Value);
        @SuppressWarnings("unchecked")
        final Map<ImmutableNode, Preferences> elemRefs = mock(Map.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willThrow(mockEx).given(mapper).getAttributes(prefs);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.constructAttributes(builder, prefs, elemRefs);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getAttributes(prefs);
        then(mapper).should(never()).createPropertyNode(any(), any());
    }

    /**
     * Test for {@link NodeBasedPreferencesMapper#createPropertyNode(String, String)}.
     */
    @Test
    void testCreatePropertyNode() {
        final NodeBasedPreferencesMapper mapper = spy(NodeBasedPreferencesMapper.class);
        final String name = "mockName";
        final String value = "mockValue";
        
        final ImmutableNode result = mapper.createPropertyNode(name, value);
        assertNotNull(result);
        assertEquals(name, result.getNodeName());
        assertEquals(value, result.getValue());
        assertTrue(result.getAttributes().isEmpty());
        assertTrue(result.getChildren().isEmpty());
    }
}
