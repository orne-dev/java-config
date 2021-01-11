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
import static org.mockito.AdditionalMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apache.commons.configuration2.convert.ConversionHandler;
import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;
import org.apache.commons.configuration2.ex.ConversionException;
import org.apache.commons.configuration2.tree.ExpressionEngine;
import org.apache.commons.configuration2.tree.NodeAddData;
import org.apache.commons.configuration2.tree.NodeHandler;
import org.apache.commons.configuration2.tree.NodeModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junitpioneer.jupiter.CartesianProductTest;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.config.commons.prefs.AbstractPreferencesMapper.PropertyData;

/**
 * Unit tests for {@code AbstractPreferencesMapper}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see AbstractPreferencesMapper
 */
@Tag("ut")
class AbstractPreferencesMapperTest {

    private @Mock PreferencesBased<Object> config;
    private @Mock NodeModel<Object> model;
    private @Mock NodeHandler<Object> handler;
    private @Mock ExpressionEngine expressionEngine;
    private @Mock Preferences baseNode;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    protected void expectNodeModel() {
        willReturn(model).given(config).getNodeModel();
        willReturn(handler).given(model).getNodeHandler();
        willReturn(expressionEngine).given(config).getExpressionEngine();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#resolveNodeKey(PreferencesBased, Preferences, Preferences)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testResolveNodeKey() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final String baseNodeAbsolutePath = "/mock/base/node";
        final Preferences parent = mock(Preferences.class);
        final String parentKey = "mock.parent.key";
        final Preferences node = mock(Preferences.class);
        final Object cfgNode = new Object();
        final String nodeAbsolutePath = "/mock/base/node/parent1/parent2/parent3/node";
        final String nodeName = "mockNode";
        final String expectedResult = "mock.result.key";
        
        expectNodeModel();
        willReturn(expectedResult).given(expressionEngine).nodeKey(cfgNode, parentKey, handler);
        willReturn(baseNodeAbsolutePath).given(baseNode).absolutePath();
        willReturn(nodeAbsolutePath).given(node).absolutePath();
        willReturn(parent).given(node).parent();
        willReturn(nodeName).given(mapper).getName(node);
        willReturn(cfgNode).given(mapper).createNodeForName(nodeName);
        willReturn(parentKey).given(mapper).resolveNodeKey(config, baseNode, parent);
        
        final String result = mapper.resolveNodeKey(config, baseNode, node);
        assertEquals(expectedResult, result);
        
        then(node).should(atLeastOnce()).absolutePath();
        then(node).should(atLeastOnce()).parent();
        then(node).shouldHaveNoMoreInteractions();
        then(mapper).should(times(1)).resolveNodeKey(config, baseNode, parent);
        then(expressionEngine).should(times(1)).nodeKey(cfgNode, parentKey, handler);
    }

    /**
     * Test for {@link AbstractPreferencesMapper#resolveNodeKey(PreferencesBased, Preferences, Preferences)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testResolveNodeKey_BaseNode() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final String baseNodeAbsolutePath = "/mock/base/node";
        final Preferences node = mock(Preferences.class);
        final Object cfgNode = new Object();
        final String nodeAbsolutePath = baseNodeAbsolutePath;
        final String nodeName = "mockNode";
        
        expectNodeModel();
        willReturn(baseNodeAbsolutePath).given(baseNode).absolutePath();
        willReturn(nodeAbsolutePath).given(node).absolutePath();
        willReturn(nodeName).given(mapper).getName(node);
        willReturn(cfgNode).given(mapper).createNodeForName(nodeName);
        
        final String result = mapper.resolveNodeKey(config, baseNode, node);
        assertNull(result);
        
        then(node).should(atLeastOnce()).absolutePath();
        then(node).should(never()).parent();
        then(node).shouldHaveNoMoreInteractions();
        then(mapper).should(never()).resolveNodeKey(eq(config), eq(baseNode), not(eq(node)));
        then(expressionEngine).should(never()).nodeKey(any(), any(), any());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#resolveNodeKey(PreferencesBased, Preferences, Preferences)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testResolveNodeKey_DirectChild() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final String baseNodeAbsolutePath = "/mock/base/node";
        final Preferences node = mock(Preferences.class);
        final Object cfgNode = new Object();
        final String nodeAbsolutePath = "/mock/base/node/mockNode";
        final String nodeName = "mockNode";
        final String expectedResult = "mock.result.key";
        
        expectNodeModel();
        willReturn(expectedResult).given(expressionEngine).nodeKey(cfgNode, null, handler);
        willReturn(baseNodeAbsolutePath).given(baseNode).absolutePath();
        willReturn(nodeAbsolutePath).given(node).absolutePath();
        willReturn(baseNode).given(node).parent();
        willReturn(nodeName).given(mapper).getName(node);
        willReturn(cfgNode).given(mapper).createNodeForName(nodeName);
        willReturn(null).given(mapper).resolveNodeKey(config, baseNode, baseNode);
        
        final String result = mapper.resolveNodeKey(config, baseNode, node);
        assertEquals(expectedResult, result);
        
        then(node).should(atLeastOnce()).absolutePath();
        then(node).should(atLeastOnce()).parent();
        then(node).shouldHaveNoMoreInteractions();
        then(mapper).should(times(1)).resolveNodeKey(config, baseNode, baseNode);
        then(expressionEngine).should(times(1)).nodeKey(cfgNode, null, handler);
    }

    /**
     * Test for {@link AbstractPreferencesMapper#resolveNodeKey(PreferencesBased, Preferences, Preferences)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testResolveNodeKey_NodeDeleted() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final String baseNodeAbsolutePath = "/mock/base/node";
        final Preferences node = mock(Preferences.class);
        final Object cfgNode = new Object();
        final String nodeAbsolutePath = "/mock/base/node/mockNode";
        final String nodeName = "mockNode";
        final IllegalStateException mockEx = new IllegalStateException("mock exception");
        
        expectNodeModel();
        willReturn(baseNodeAbsolutePath).given(baseNode).absolutePath();
        willReturn(nodeAbsolutePath).given(node).absolutePath();
        willThrow(mockEx).given(node).parent();
        willReturn(nodeName).given(mapper).getName(node);
        willReturn(cfgNode).given(mapper).createNodeForName(nodeName);
        willReturn(null).given(mapper).resolveNodeKey(config, baseNode, baseNode);
        
        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.resolveNodeKey(config, baseNode, node);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(node).should(atLeastOnce()).absolutePath();
        then(node).should(atLeastOnce()).parent();
        then(node).shouldHaveNoMoreInteractions();
        then(mapper).should(never()).resolveNodeKey(any(), any(), not(eq(node)));
        then(expressionEngine).should(never()).nodeKey(any(), any(), any());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#resolvePropertyKey(PreferencesBased, Preferences, Preferences, String)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testResolvePropertyKey() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String nodeKey = "mock.parent.key";
        final String property = "mockProperty";
        final String expectedResult = "mock.result.key";
        
        expectNodeModel();
        willReturn(expectedResult).given(expressionEngine).attributeKey(nodeKey, property);
        willReturn(nodeKey).given(mapper).resolveNodeKey(config, baseNode, node);
        
        final String result = mapper.resolvePropertyKey(config, baseNode, node, property);
        assertEquals(expectedResult, result);
        
        then(node).shouldHaveNoInteractions();
        then(mapper).should(times(1)).resolveNodeKey(config, baseNode, node);
        then(expressionEngine).should(times(1)).attributeKey(nodeKey, property);
    }

    /**
     * Test for {@link AbstractPreferencesMapper#resolvePropertyKey(PreferencesBased, Preferences, Preferences, String)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testResolvePropertyKey_GetNodeKey_Error() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String property = "mockProperty";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        expectNodeModel();
        willThrow(mockEx).given(mapper).resolveNodeKey(config, baseNode, node);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.resolvePropertyKey(config, baseNode, node, property);
        });
        assertSame(mockEx, result);
        
        then(node).shouldHaveNoInteractions();
        then(mapper).should(times(1)).resolveNodeKey(config, baseNode, node);
        then(expressionEngine).should(never()).attributeKey(any(), any());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setProperty(PreferencesBased, Preferences, String, Object)}.
     */
    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    @SuppressWarnings("unchecked")
    void testSetProperty(
            final boolean isAttr) {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences parentNode = mock(Preferences.class);
        final String property = "mock.property.key";
        final Object value = new Object();
        final String strValue = "mockValue";
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(isAttr).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(parentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willReturn(strValue).given(mapper).convertValue(config, value);
        willDoNothing().given(mapper).setAttribute(any(), any(), any());
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        
        mapper.setProperty(config, baseNode, property, value);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(atLeastOnce()).convertValue(config, value);
        if (isAttr) {
            then(mapper).should(times(1)).setAttribute(parentNode, prefName, strValue);
            then(mapper).should(never()).setChildValue(any(), any(), any());
        } else {
            then(mapper).should(never()).setAttribute(any(), any(), any());
            then(mapper).should(times(1)).setChildValue(parentNode, prefName, strValue);
        }
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setProperty(PreferencesBased, Preferences, String, Object)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testSetProperty_GetPropertyData_Error() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final String property = "mock.property.key";
        final Object value = new Object();
        final String strValue = "mockValue";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willThrow(mockEx).given(mapper).getPropertyData(config, property);
        willReturn(strValue).given(mapper).convertValue(config, value);
        willDoNothing().given(mapper).setAttribute(any(), any(), any());
        willThrow(mockEx).given(mapper).setChildValue(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.setProperty(config, baseNode, property, value);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(never()).resolvePreferencesNode(any(), (Collection<String>) any());
        then(mapper).should(atLeast(0)).convertValue(config, value);
        then(mapper).should(never()).setAttribute(any(), any(), any());
        then(mapper).should(never()).setChildValue(any(), any(), any());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setProperty(PreferencesBased, Preferences, String, Object)}.
     */
    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    @SuppressWarnings("unchecked")
    void testSetProperty_ResolvePreferencesNode_Error(
            final boolean isAttr) {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final String property = "mock.property.key";
        final Object value = new Object();
        final String strValue = "mockValue";
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(isAttr).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willThrow(mockEx).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willReturn(strValue).given(mapper).convertValue(config, value);
        willDoNothing().given(mapper).setAttribute(any(), any(), any());
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.setProperty(config, baseNode, property, value);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(atLeast(0)).convertValue(config, value);
        then(mapper).should(never()).setAttribute(any(), any(), any());
        then(mapper).should(never()).setChildValue(any(), any(), any());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setProperty(PreferencesBased, Preferences, String, Object)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testSetProperty_SetAttribute_Error() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences parentNode = mock(Preferences.class);
        final String property = "mock.property.key";
        final Object value = new Object();
        final String strValue = "mockValue";
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(true).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(parentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willReturn(strValue).given(mapper).convertValue(config, value);
        willThrow(mockEx).given(mapper).setAttribute(any(), any(), any());
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.setProperty(config, baseNode, property, value);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(atLeastOnce()).convertValue(config, value);
        then(mapper).should(times(1)).setAttribute(parentNode, prefName, strValue);
        then(mapper).should(never()).setChildValue(any(), any(), any());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setProperty(PreferencesBased, Preferences, String, Object)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testSetProperty_SetChildValue_Error() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences parentNode = mock(Preferences.class);
        final String property = "mock.property.key";
        final Object value = new Object();
        final String strValue = "mockValue";
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(false).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(parentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willReturn(strValue).given(mapper).convertValue(config, value);
        willDoNothing().given(mapper).setAttribute(any(), any(), any());
        willThrow(mockEx).given(mapper).setChildValue(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.setProperty(config, baseNode, property, value);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(atLeastOnce()).convertValue(config, value);
        then(mapper).should(never()).setAttribute(any(), any(), any());
        then(mapper).should(times(1)).setChildValue(parentNode, prefName, strValue);
    }

    /**
     * Test for {@link AbstractPreferencesMapper#addNodes(PreferencesBased, Preferences, String, Collection)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testAddNodes() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences grandparentNode = mock(Preferences.class);
        final String parentCfgName = "mockPrefName";
        final Object parentNameCfgNode = new Object();
        final String parentName = "mockParentName";
        final Preferences parentNode = mock(Preferences.class);
        final String property = "mock.parent.key";
        final Object newNode1 = new Object();
        final Object newNode2 = new Object();
        final Object newNode3 = new Object();
        final List<Object> newNodes = Arrays.asList(newNode1, newNode2, newNode3);
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(parentCfgName).given(propertyData).getName();
        willReturn(false).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(grandparentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willReturn(parentNameCfgNode).given(mapper).createNodeForName(parentCfgName);
        willReturn(parentName).given(mapper).getName(parentNameCfgNode);
        willReturn(parentNode).given(grandparentNode).node(parentName);
        willDoNothing().given(mapper).saveChildNode(any(), any(), any());
        
        mapper.addNodes(config, baseNode, property, newNodes);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(times(1)).saveChildNode(config, parentNode, newNode1);
        then(mapper).should(times(1)).saveChildNode(config, parentNode, newNode2);
        then(mapper).should(times(1)).saveChildNode(config, parentNode, newNode3);
        then(baseNode).shouldHaveNoInteractions();
        then(grandparentNode).should(times(1)).node(parentName);
        then(grandparentNode).shouldHaveNoMoreInteractions();
        then(parentNode).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#addNodes(PreferencesBased, Preferences, String, Collection)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testAddNodes_AttributeKey() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences parentNode = mock(Preferences.class);
        final String property = "mock.parent.key";
        final Object newNode1 = new Object();
        final Object newNode2 = new Object();
        final Object newNode3 = new Object();
        final List<Object> newNodes = Arrays.asList(newNode1, newNode2, newNode3);
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(true).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(parentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willDoNothing().given(mapper).saveChildNode(any(), any(), any());
        
        mapper.addNodes(config, baseNode, property, newNodes);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(times(1)).saveChildNode(config, parentNode, newNode1);
        then(mapper).should(times(1)).saveChildNode(config, parentNode, newNode2);
        then(mapper).should(times(1)).saveChildNode(config, parentNode, newNode3);
        then(baseNode).shouldHaveNoInteractions();
        then(parentNode).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#addNodes(PreferencesBased, Preferences, String, Collection)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testAddNodes_GetPropertyData_Error() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final String property = "mock.parent.key";
        final Object newNode1 = new Object();
        final Object newNode2 = new Object();
        final Object newNode3 = new Object();
        final List<Object> newNodes = Arrays.asList(newNode1, newNode2, newNode3);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willThrow(mockEx).given(mapper).getPropertyData(config, property);
        willDoNothing().given(mapper).saveChildNode(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.addNodes(config, baseNode, property, newNodes);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(never()).resolvePreferencesNode(any(), (Collection<String>) any());
        then(mapper).should(never()).saveChildNode(any(), any(), any());
        then(baseNode).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#addNodes(PreferencesBased, Preferences, String, Collection)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testAddNodes_ResolvePreferencesNode_Error() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final String parentCfgName = "mockPrefName";
        final Object parentNameCfgNode = new Object();
        final String parentName = "mockParentName";
        final String property = "mock.parent.key";
        final Object newNode1 = new Object();
        final Object newNode2 = new Object();
        final Object newNode3 = new Object();
        final List<Object> newNodes = Arrays.asList(newNode1, newNode2, newNode3);
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(parentCfgName).given(propertyData).getName();
        willReturn(false).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willThrow(mockEx).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willReturn(parentNameCfgNode).given(mapper).createNodeForName(parentCfgName);
        willReturn(parentName).given(mapper).getName(parentNameCfgNode);
        willDoNothing().given(mapper).saveChildNode(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.addNodes(config, baseNode, property, newNodes);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(never()).saveChildNode(any(), any(), any());
        then(baseNode).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#addNodes(PreferencesBased, Preferences, String, Collection)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testAddNodes_NodeDeleted() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences grandparentNode = mock(Preferences.class);
        final String parentCfgName = "mockPrefName";
        final Object parentNameCfgNode = new Object();
        final String parentName = "mockParentName";
        final String property = "mock.parent.key";
        final Object newNode1 = new Object();
        final Object newNode2 = new Object();
        final Object newNode3 = new Object();
        final List<Object> newNodes = Arrays.asList(newNode1, newNode2, newNode3);
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final IllegalStateException mockEx = new IllegalStateException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(parentCfgName).given(propertyData).getName();
        willReturn(false).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(grandparentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willReturn(parentNameCfgNode).given(mapper).createNodeForName(parentCfgName);
        willReturn(parentName).given(mapper).getName(parentNameCfgNode);
        willThrow(mockEx).given(grandparentNode).node(parentName);
        willDoNothing().given(mapper).saveChildNode(any(), any(), any());
        
        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.addNodes(config, baseNode, property, newNodes);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(never()).saveChildNode(any(), any(), any());
        then(baseNode).shouldHaveNoInteractions();
        then(grandparentNode).should(times(1)).node(parentName);
        then(grandparentNode).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#addNodes(PreferencesBased, Preferences, String, Collection)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testAddNodes_InvalidKey() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences grandparentNode = mock(Preferences.class);
        final String parentCfgName = "mockPrefName";
        final Object parentNameCfgNode = new Object();
        final String parentName = "mockParentName";
        final String property = "mock.parent.key";
        final Object newNode1 = new Object();
        final Object newNode2 = new Object();
        final Object newNode3 = new Object();
        final List<Object> newNodes = Arrays.asList(newNode1, newNode2, newNode3);
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final IllegalArgumentException mockEx = new IllegalArgumentException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(parentCfgName).given(propertyData).getName();
        willReturn(false).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(grandparentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willReturn(parentNameCfgNode).given(mapper).createNodeForName(parentCfgName);
        willReturn(parentName).given(mapper).getName(parentNameCfgNode);
        willThrow(mockEx).given(grandparentNode).node(parentName);
        willDoNothing().given(mapper).saveChildNode(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.addNodes(config, baseNode, property, newNodes);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(never()).saveChildNode(any(), any(), any());
        then(baseNode).shouldHaveNoInteractions();
        then(grandparentNode).should(times(1)).node(parentName);
        then(grandparentNode).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#addNodes(PreferencesBased, Preferences, String, Collection)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testAddNodes_SaveChildNode_Error() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences grandparentNode = mock(Preferences.class);
        final String parentCfgName = "mockPrefName";
        final Object parentNameCfgNode = new Object();
        final String parentName = "mockParentName";
        final Preferences parentNode = mock(Preferences.class);
        final String property = "mock.parent.key";
        final Object newNode1 = new Object();
        final Object newNode2 = new Object();
        final Object newNode3 = new Object();
        final List<Object> newNodes = Arrays.asList(newNode1, newNode2, newNode3);
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(parentCfgName).given(propertyData).getName();
        willReturn(false).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(grandparentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willReturn(parentNameCfgNode).given(mapper).createNodeForName(parentCfgName);
        willReturn(parentName).given(mapper).getName(parentNameCfgNode);
        willReturn(parentNode).given(grandparentNode).node(parentName);
        willThrow(mockEx).given(mapper).saveChildNode(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.addNodes(config, baseNode, property, newNodes);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(times(1)).saveChildNode(any(), any(), any());
        then(baseNode).shouldHaveNoInteractions();
        then(grandparentNode).should(times(1)).node(parentName);
        then(grandparentNode).shouldHaveNoMoreInteractions();
        then(parentNode).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeProperty(PreferencesBased, Preferences, String)}.
     */
    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    @SuppressWarnings("unchecked")
    void testRemoveProperty(
            final boolean isAttr) {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences parentNode = mock(Preferences.class);
        final String property = "mock.property.key";
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(isAttr).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(parentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willReturn(null).given(mapper).convertValue(config, null);
        willDoNothing().given(mapper).setAttribute(any(), any(), any());
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        
        mapper.removeProperty(config, baseNode, property);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        if (isAttr) {
            then(mapper).should(times(1)).setAttribute(parentNode, prefName, null);
            then(mapper).should(never()).setChildValue(any(), any(), any());
        } else {
            then(mapper).should(never()).setAttribute(any(), any(), any());
            then(mapper).should(times(1)).setChildValue(parentNode, prefName, null);
        }
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setProperty(PreferencesBased, Preferences, String, Object)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testRemoveProperty_GetPropertyData_Error() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final String property = "mock.property.key";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willThrow(mockEx).given(mapper).getPropertyData(config, property);
        willReturn(null).given(mapper).convertValue(config, null);
        willDoNothing().given(mapper).setAttribute(any(), any(), any());
        willThrow(mockEx).given(mapper).setChildValue(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.removeProperty(config, baseNode, property);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(never()).resolvePreferencesNode(any(), (Collection<String>) any());
        then(mapper).should(never()).setAttribute(any(), any(), any());
        then(mapper).should(never()).setChildValue(any(), any(), any());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setProperty(PreferencesBased, Preferences, String, Object)}.
     */
    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    @SuppressWarnings("unchecked")
    void testRemoveProperty_ResolvePreferencesNode_Error(
            final boolean isAttr) {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final String property = "mock.property.key";
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(isAttr).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willThrow(mockEx).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willReturn(null).given(mapper).convertValue(config, null);
        willDoNothing().given(mapper).setAttribute(any(), any(), any());
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.removeProperty(config, baseNode, property);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(never()).setAttribute(any(), any(), any());
        then(mapper).should(never()).setChildValue(any(), any(), any());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setProperty(PreferencesBased, Preferences, String, Object)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testRemoveProperty_SetAttribute_Error() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences parentNode = mock(Preferences.class);
        final String property = "mock.property.key";
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(true).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(parentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willReturn(null).given(mapper).convertValue(config, null);
        willThrow(mockEx).given(mapper).setAttribute(any(), any(), any());
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.removeProperty(config, baseNode, property);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(times(1)).setAttribute(parentNode, prefName, null);
        then(mapper).should(never()).setChildValue(any(), any(), any());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setProperty(PreferencesBased, Preferences, String, Object)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testRemoveProperty_SetChildValue_Error() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences parentNode = mock(Preferences.class);
        final String property = "mock.property.key";
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(false).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(parentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willReturn(null).given(mapper).convertValue(config, null);
        willDoNothing().given(mapper).setAttribute(any(), any(), any());
        willThrow(mockEx).given(mapper).setChildValue(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.removeProperty(config, baseNode, property);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(never()).setAttribute(any(), any(), any());
        then(mapper).should(times(1)).setChildValue(parentNode, prefName, null);
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeNode(PreferencesBased, Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    @SuppressWarnings("unchecked")
    void testRemoveNode(
            final boolean isAttr)
    throws BackingStoreException {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences parentNode = mock(Preferences.class);
        final String property = "mock.property.key";
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(isAttr).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(parentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willDoNothing().given(mapper).setValue(any(), any());
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).removeChild(any(), any());
        
        mapper.removeNode(config, baseNode, property);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        if (isAttr) {
            then(mapper).should(times(1)).setValue(parentNode, null);
            then(parentNode).should(times(1)).removeNode();
            then(parentNode).shouldHaveNoMoreInteractions();
            then(mapper).should(never()).setChildValue(any(), any(), any());
            then(mapper).should(never()).removeChild(any(), any());
        } else {
            then(mapper).should(never()).setValue(any(), any());
            then(parentNode).shouldHaveNoInteractions();
            then(mapper).should(times(1)).setChildValue(parentNode, prefName, null);
            then(mapper).should(times(1)).removeChild(parentNode, prefName);
        }
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeNode(PreferencesBased, Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    @SuppressWarnings("unchecked")
    void testRemoveNode_GetPropertyData_Error(
            final boolean isAttr)
    throws BackingStoreException {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final String property = "mock.property.key";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willThrow(mockEx).given(mapper).getPropertyData(config, property);
        willDoNothing().given(mapper).setValue(any(), any());
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).removeChild(any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.removeNode(config, baseNode, property);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(never()).resolvePreferencesNode(any(), any(), any());
        then(mapper).should(never()).setValue(any(), any());
        then(mapper).should(never()).setChildValue(any(), any(), any());
        then(mapper).should(never()).removeChild(any(), any());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeNode(PreferencesBased, Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    @SuppressWarnings("unchecked")
    void testRemoveNode_ResolvePreferencesNode_Error(
            final boolean isAttr)
    throws BackingStoreException {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final String property = "mock.property.key";
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(isAttr).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willThrow(mockEx).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willDoNothing().given(mapper).setValue(any(), any());
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).removeChild(any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.removeNode(config, baseNode, property);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(never()).setValue(any(), any());
        then(mapper).should(never()).setChildValue(any(), any(), any());
        then(mapper).should(never()).removeChild(any(), any());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeNode(PreferencesBased, Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    @SuppressWarnings("unchecked")
    void testRemoveNode_RootNode()
    throws BackingStoreException {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences parentNode = mock(Preferences.class);
        final String property = "mock.property.key";
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        final UnsupportedOperationException mockEx = new UnsupportedOperationException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(true).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(parentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willDoNothing().given(mapper).setValue(any(), any());
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).removeChild(any(), any());
        willThrow(mockEx).given(parentNode).removeNode();
        
        final UnsupportedOperationException result = assertThrows(UnsupportedOperationException.class, () -> {
            mapper.removeNode(config, baseNode, property);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(atMost(1)).setValue(parentNode, null);
        then(parentNode).should(times(1)).removeNode();
        then(parentNode).shouldHaveNoMoreInteractions();
        then(mapper).should(never()).setChildValue(any(), any(), any());
        then(mapper).should(never()).removeChild(any(), any());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeNode(PreferencesBased, Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    @SuppressWarnings("unchecked")
    void testRemoveNode_NodeDeleted()
    throws BackingStoreException {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences parentNode = mock(Preferences.class);
        final String property = "mock.property.key";
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        final IllegalStateException mockEx = new IllegalStateException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(true).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(parentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willDoNothing().given(mapper).setValue(any(), any());
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).removeChild(any(), any());
        willThrow(mockEx).given(parentNode).removeNode();
        
        mapper.removeNode(config, baseNode, property);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(atMost(1)).setValue(parentNode, null);
        then(parentNode).should(times(1)).removeNode();
        then(parentNode).shouldHaveNoMoreInteractions();
        then(mapper).should(never()).setChildValue(any(), any(), any());
        then(mapper).should(never()).removeChild(any(), any());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeNode(PreferencesBased, Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    @SuppressWarnings("unchecked")
    void testRemoveNode_BackingStoreException()
    throws BackingStoreException {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences parentNode = mock(Preferences.class);
        final String property = "mock.property.key";
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        final BackingStoreException mockEx = new BackingStoreException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(true).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(parentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willDoNothing().given(mapper).setValue(any(), any());
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).removeChild(any(), any());
        willThrow(mockEx).given(parentNode).removeNode();
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.removeNode(config, baseNode, property);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(atMost(1)).setValue(parentNode, null);
        then(parentNode).should(times(1)).removeNode();
        then(parentNode).shouldHaveNoMoreInteractions();
        then(mapper).should(never()).setChildValue(any(), any(), any());
        then(mapper).should(never()).removeChild(any(), any());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeNode(PreferencesBased, Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    @SuppressWarnings("unchecked")
    void testRemoveNode_SetValue_Error()
    throws BackingStoreException {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences parentNode = mock(Preferences.class);
        final String property = "mock.property.key";
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(true).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(parentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willThrow(mockEx).given(mapper).setValue(any(), any());
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).removeChild(any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.removeNode(config, baseNode, property);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(times(1)).setValue(parentNode, null);
        then(parentNode).should(atMost(1)).removeNode();
        then(parentNode).shouldHaveNoMoreInteractions();
        then(mapper).should(never()).setChildValue(any(), any(), any());
        then(mapper).should(never()).removeChild(any(), any());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeNode(PreferencesBased, Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    @SuppressWarnings("unchecked")
    void testRemoveNode_SetChildValue_Error()
    throws BackingStoreException {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences parentNode = mock(Preferences.class);
        final String property = "mock.property.key";
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(false).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(parentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willDoNothing().given(mapper).setValue(any(), any());
        willThrow(mockEx).given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).removeChild(any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.removeNode(config, baseNode, property);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(never()).setValue(any(), any());
        then(parentNode).shouldHaveNoInteractions();
        then(mapper).should(times(1)).setChildValue(parentNode, prefName, null);
        then(mapper).should(atMost(1)).removeChild(parentNode, prefName);
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeNode(PreferencesBased, Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    @SuppressWarnings("unchecked")
    void testRemoveNode_RemoveChild_Error()
    throws BackingStoreException {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences parentNode = mock(Preferences.class);
        final String property = "mock.property.key";
        final PropertyData propertyData = mock(PropertyData.class);
        final List<String> nodePath = mock(List.class);
        final String prefName = "mockPrefName";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(nodePath).given(propertyData).getPath();
        willReturn(prefName).given(propertyData).getName();
        willReturn(false).given(propertyData).isAttribute();
        willReturn(propertyData).given(mapper).getPropertyData(config, property);
        willReturn(parentNode).given(mapper).resolvePreferencesNode(baseNode, nodePath);
        willDoNothing().given(mapper).setValue(any(), any());
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willThrow(mockEx).given(mapper).removeChild(any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.removeNode(config, baseNode, property);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getPropertyData(config, property);
        then(mapper).should(times(1)).resolvePreferencesNode(baseNode, nodePath);
        then(mapper).should(never()).setValue(any(), any());
        then(parentNode).shouldHaveNoInteractions();
        then(mapper).should(atMost(1)).setChildValue(parentNode, prefName, null);
        then(mapper).should(times(1)).removeChild(parentNode, prefName);
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveNodeHierarchy(PreferencesBased, Preferences, Object)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testSaveNodeHierarchy() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences baseNode = mock(Preferences.class);
        final Object node = new Object();
        final Object nodeValue = new Object();
        final String nodeStrValue = "mockValue";
        final Map<String, Object> nodeAttrs = mock(Map.class);
        final Collection<Object> nodeChildren = mock(Collection.class);
        
        expectNodeModel();
        willReturn(node).given(handler).getRootNode();
        willReturn(nodeValue).given(mapper).getValue(node);
        willReturn(nodeStrValue).given(mapper).convertValue(config, nodeValue);
        willReturn(nodeAttrs).given(mapper).getAttributes(node);
        willReturn(nodeChildren).given(mapper).getChildren(node);
        willDoNothing().given(mapper).setValue(any(), any());
        willDoNothing().given(mapper).setAttributes(any(), any(), any());
        willDoNothing().given(mapper).saveChildren(any(), any(), any());
        
        mapper.saveNodeHierarchy(config, baseNode);
        
        then(mapper).should(times(1)).setValue(baseNode, nodeStrValue);
        then(mapper).should(times(1)).setAttributes(config, baseNode, nodeAttrs);
        then(mapper).should(times(1)).saveChildren(config, baseNode, nodeChildren);
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveNodeHierarchy(PreferencesBased, Preferences, Object)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testSaveNodeHierarchy_SetValue_Error() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Object node = new Object();
        final Object nodeValue = new Object();
        final String nodeStrValue = "mockValue";
        final Map<String, Object> nodeAttrs = mock(Map.class);
        final Collection<Object> nodeChildren = mock(Collection.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        expectNodeModel();
        willReturn(node).given(handler).getRootNode();
        willReturn(nodeValue).given(mapper).getValue(node);
        willReturn(nodeStrValue).given(mapper).convertValue(config, nodeValue);
        willReturn(nodeAttrs).given(mapper).getAttributes(node);
        willReturn(nodeChildren).given(mapper).getChildren(node);
        willThrow(mockEx).given(mapper).setValue(any(), any());
        willDoNothing().given(mapper).setAttributes(any(), any(), any());
        willDoNothing().given(mapper).saveChildren(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.saveNodeHierarchy(config, baseNode);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).setValue(baseNode, nodeStrValue);
        then(mapper).should(atMost(1)).setAttributes(config, baseNode, nodeAttrs);
        then(mapper).should(atMost(1)).saveChildren(config, baseNode, nodeChildren);
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveNodeHierarchy(PreferencesBased, Preferences, Object)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testSaveNodeHierarchy_SetAttributes_Error() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences baseNode = mock(Preferences.class);
        final Object node = new Object();
        final Object nodeValue = new Object();
        final String nodeStrValue = "mockValue";
        final Map<String, Object> nodeAttrs = mock(Map.class);
        final Collection<Object> nodeChildren = mock(Collection.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        expectNodeModel();
        willReturn(node).given(handler).getRootNode();
        willReturn(nodeValue).given(mapper).getValue(node);
        willReturn(nodeStrValue).given(mapper).convertValue(config, nodeValue);
        willReturn(nodeAttrs).given(mapper).getAttributes(node);
        willReturn(nodeChildren).given(mapper).getChildren(node);
        willDoNothing().given(mapper).setValue(any(), any());
        willThrow(mockEx).given(mapper).setAttributes(any(), any(), any());
        willDoNothing().given(mapper).saveChildren(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.saveNodeHierarchy(config, baseNode);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(atMost(1)).setValue(baseNode, nodeStrValue);
        then(mapper).should(times(1)).setAttributes(config, baseNode, nodeAttrs);
        then(mapper).should(atMost(1)).saveChildren(config, baseNode, nodeChildren);
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveNodeHierarchy(PreferencesBased, Preferences, Object)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testSaveNodeHierarchy_SaveChildren_Error() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences baseNode = mock(Preferences.class);
        final Object node = new Object();
        final Object nodeValue = new Object();
        final String nodeStrValue = "mockValue";
        final Map<String, Object> nodeAttrs = mock(Map.class);
        final Collection<Object> nodeChildren = mock(Collection.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        expectNodeModel();
        willReturn(node).given(handler).getRootNode();
        willReturn(nodeValue).given(mapper).getValue(node);
        willReturn(nodeStrValue).given(mapper).convertValue(config, nodeValue);
        willReturn(nodeAttrs).given(mapper).getAttributes(node);
        willReturn(nodeChildren).given(mapper).getChildren(node);
        willDoNothing().given(mapper).setValue(any(), any());
        willDoNothing().given(mapper).setAttributes(any(), any(), any());
        willThrow(mockEx).given(mapper).saveChildren(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.saveNodeHierarchy(config, baseNode);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(atMost(1)).setValue(baseNode, nodeStrValue);
        then(mapper).should(atMost(1)).setAttributes(config, baseNode, nodeAttrs);
        then(mapper).should(times(1)).saveChildren(config, baseNode, nodeChildren);
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getName(Preferences)}.
     */
    @Test
    void testGetName() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockName = "mockName";
        
        willReturn(mockName).given(node).name();
        
        final String result = mapper.getName(node);
        assertEquals(mockName, result);
        
        then(node).should(times(1)).name();
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getAttributesNames(Preferences)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testGetAttributesNames()
    throws BackingStoreException {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr1 = "mockAttr1";
        final String mockAttr2 = "mockAttr2";
        final String[] attrNames = new String[] { mockAttr1, mockAttr2 };
        
        willReturn(attrNames).given(node).keys();
        
        final Collection<String> result = mapper.getAttributesNames(node);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(mockAttr1));
        assertTrue(result.contains(mockAttr2));
        
        then(node).should(times(1)).keys();
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getAttributesNames(Preferences)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testGetAttributesNames_BackingStoreError()
    throws BackingStoreException {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
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
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getAttributesNames(Preferences)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testGetAttributesNames_NodeDeleted()
    throws BackingStoreException {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
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
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getAttributes(Preferences)}.
     */
    @Test
    void testGetAttributes() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr1 = "mockAttr1";
        final String mockValue1 = "mockValue1";
        final String mockAttr2 = "mockAttr2";
        final String mockValue2 = "mockValue2";
        final Collection<String> attrNames = Arrays.asList(mockAttr1, mockAttr2);
        
        willReturn(attrNames).given(mapper).getAttributesNames(node);
        willReturn(mockValue1).given(mapper).getAttribute(node, mockAttr1);
        willReturn(mockValue2).given(mapper).getAttribute(node, mockAttr2);
        
        final Map<String, String> result = mapper.getAttributes(node);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey(mockAttr1));
        assertEquals(mockValue1, result.get(mockAttr1));
        assertTrue(result.containsKey(mockAttr2));
        assertEquals(mockValue2, result.get(mockAttr2));
        
        then(mapper).should(times(1)).getAttributesNames(node);
        then(mapper).should(times(1)).getAttribute(node, mockAttr1);
        then(mapper).should(times(1)).getAttribute(node, mockAttr2);
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getAttributes(Preferences)}.
     */
    @Test
    void testGetAttributes_GetAttributesNames_Error() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willThrow(mockEx).given(mapper).getAttributesNames(node);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.getAttributes(node);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getAttributesNames(node);
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getAttributes(Preferences)}.
     */
    @Test
    void testGetAttributes_GetAttribute_Error() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr1 = "mockAttr1";
        final String mockAttr2 = "mockAttr2";
        final Collection<String> attrNames = Arrays.asList(mockAttr1, mockAttr2);
        final PreferencesNodeDeletedException mockEx = new PreferencesNodeDeletedException("mock exception");
        
        willReturn(attrNames).given(mapper).getAttributesNames(node);
        willThrow(mockEx).given(mapper).getAttribute(same(node), any());
        
        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.getAttributes(node);
        });
        assertTrue(mockEx == result || mockEx == result.getCause());
        
        then(mapper).should(times(1)).getAttributesNames(node);
        then(mapper).should(atLeastOnce()).getAttribute(same(node), any());
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setAttributes(PreferencesBased, Preferences, Map)}.
     */
    @Test
    void testSetAttributes() {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr1 = "mockAttr1";
        final Object mockValue1 = new Object();
        final String mockStrValue1 = "mockStrValue1";
        final String mockAttr2 = "mockAttr2";
        final Object mockValue2 = new Object();
        final String mockStrValue2 = "mockStrValue2";
        final String mockAttr3 = "mockAttr3";
        final Collection<String> attrNames = Arrays.asList(mockAttr2, mockAttr3);
        final Map<String, Object> attrs = new HashMap<>();
        attrs.put(mockAttr1, mockValue1);
        attrs.put(mockAttr2, mockValue2);
        
        willReturn(attrNames).given(mapper).getAttributesNames(node);
        willReturn(mockStrValue1).given(mapper).convertValue(config, mockValue1);
        willReturn(mockStrValue2).given(mapper).convertValue(config, mockValue2);
        willDoNothing().given(mapper).setAttribute(any(), any(), any());
        
        mapper.setAttributes(config, node, attrs);
        
        then(mapper).should(times(1)).getAttributesNames(node);
        then(mapper).should(times(1)).convertValue(config, mockValue1);
        then(mapper).should(times(1)).convertValue(config, mockValue2);
        then(mapper).should(times(1)).setAttribute(node, mockAttr1, mockStrValue1);
        then(mapper).should(times(1)).setAttribute(node, mockAttr2, mockStrValue2);
        then(mapper).should(times(1)).setAttribute(node, mockAttr3, null);
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setAttributes(PreferencesBased, Preferences, Map)}.
     */
    @Test
    void testSetAttributes_GetAttributeNames_Error() {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr1 = "mockAttr1";
        final Object mockValue1 = new Object();
        final String mockStrValue1 = "mockStrValue1";
        final String mockAttr2 = "mockAttr2";
        final Object mockValue2 = new Object();
        final String mockStrValue2 = "mockStrValue2";
        final Map<String, Object> attrs = new HashMap<>();
        attrs.put(mockAttr1, mockValue1);
        attrs.put(mockAttr2, mockValue2);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willThrow(mockEx).given(mapper).getAttributesNames(node);
        willReturn(mockStrValue1).given(mapper).convertValue(config, mockValue1);
        willReturn(mockStrValue2).given(mapper).convertValue(config, mockValue2);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.setAttributes(config, node, attrs);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getAttributesNames(node);
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setAttributes(PreferencesBased, Preferences, Map)}.
     */
    @Test
    void testSetAttributes_SetAttribute_Error() {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr1 = "mockAttr1";
        final Object mockValue1 = new Object();
        final String mockStrValue1 = "mockStrValue1";
        final String mockAttr2 = "mockAttr2";
        final Object mockValue2 = new Object();
        final String mockStrValue2 = "mockStrValue2";
        final String mockAttr3 = "mockAttr3";
        final Collection<String> attrNames = Arrays.asList(mockAttr2, mockAttr3);
        final Map<String, Object> attrs = new HashMap<>();
        attrs.put(mockAttr1, mockValue1);
        attrs.put(mockAttr2, mockValue2);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(attrNames).given(mapper).getAttributesNames(node);
        willThrow(mockEx).given(mapper).setAttribute(same(node), any(), any());
        willReturn(mockStrValue1).given(mapper).convertValue(config, mockValue1);
        willReturn(mockStrValue2).given(mapper).convertValue(config, mockValue2);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.setAttributes(config, node, attrs);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getAttributesNames(node);
        then(mapper).should(atLeastOnce()).setAttribute(same(node), any(), any());
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getAttribute(Preferences, String)}.
     */
    @Test
    void testGetAttribute() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr = "mockAttr";
        final String mockValue = "mockValue";

        willReturn(mockValue).given(mapper).getAttribute(node, mockAttr, null);

        final String result = mapper.getAttribute(node, mockAttr);
        assertEquals(mockValue, result);

        then(mapper).should(times(1)).getAttribute(node, mockAttr, null);
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getAttribute(Preferences, String)}.
     */
    @Test
    void testGetAttribute_Error() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr = "mockAttr";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");

        willThrow(mockEx).given(mapper).getAttribute(node, mockAttr, null);

        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.getAttribute(node, mockAttr);
        });
        assertSame(mockEx, result);

        then(mapper).should(times(1)).getAttribute(node, mockAttr, null);
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getAttribute(Preferences, String, String)}.
     */
    @Test
    void testGetAttributeDefault() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr = "mockAttr";
        final String mockValue = "mockValue";
        final String defaultValue = "mockDefaultValue";

        willReturn(mockValue).given(node).get(mockAttr, defaultValue);

        final String result = mapper.getAttribute(node, mockAttr, defaultValue);
        assertEquals(mockValue, result);

        then(node).should(times(1)).get(mockAttr, defaultValue);
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getAttribute(Preferences, String, String)}.
     */
    @Test
    void testGetAttributeDefault_NodeDeleted() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr = "mockAttr";
        final String defaultValue = "mockDefaultValue";
        final IllegalStateException mockEx = new IllegalStateException("mock exception");

        willThrow(mockEx).given(node).get(mockAttr, defaultValue);

        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.getAttribute(node, mockAttr, defaultValue);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());

        then(node).should(times(1)).get(mockAttr, defaultValue);
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setAttribute(Preferences, String, String)}.
     */
    @Test
    void testSetAttribute() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr = "mockAttr";
        final String mockValue = "mockValue";

        mapper.setAttribute(node, mockAttr, mockValue);

        then(node).should(times(1)).put(mockAttr, mockValue);
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setAttribute(Preferences, String, String)}.
     */
    @Test
    void testSetAttribute_Null() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr = "mockAttr";

        mapper.setAttribute(node, mockAttr, null);

        then(node).should(times(1)).remove(mockAttr);
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setAttribute(Preferences, String, String)}.
     */
    @Test
    void testSetAttribute_NodeDeleted() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr = "mockAttr";
        final String mockValue = "mockValue";
        final IllegalStateException mockEx = new IllegalStateException("mock exception");

        willThrow(mockEx).given(node).put(mockAttr, mockValue);

        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.setAttribute(node, mockAttr, mockValue);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());

        then(node).should(times(1)).put(mockAttr, mockValue);
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setAttribute(Preferences, String, String)}.
     */
    @Test
    void testSetAttribute_InvalidKey() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr = "mockAttr";
        final String mockValue = "mockValue";
        final IllegalArgumentException mockEx = new IllegalArgumentException("mock exception");

        willThrow(mockEx).given(node).put(mockAttr, mockValue);

        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.setAttribute(node, mockAttr, mockValue);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());

        then(node).should(times(1)).put(mockAttr, mockValue);
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setAttribute(Preferences, String, String)}.
     */
    @Test
    void testSetAttribute_Null_NodeDeleted() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr = "mockAttr";
        final IllegalStateException mockEx = new IllegalStateException("mock exception");

        willThrow(mockEx).given(node).remove(mockAttr);

        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.setAttribute(node, mockAttr, null);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());

        then(node).should(times(1)).remove(mockAttr);
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setAttribute(Preferences, String, String)}.
     */
    @Test
    void testSetAttribute_Null_InvalidKey() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockAttr = "mockAttr";
        final IllegalArgumentException mockEx = new IllegalArgumentException("mock exception");

        willThrow(mockEx).given(node).remove(mockAttr);

        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.setAttribute(node, mockAttr, null);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());

        then(node).should(times(1)).remove(mockAttr);
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getChildrenNames(Preferences)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testGetChildrenNames()
    throws BackingStoreException {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockNodeName1 = "mockNode1";
        final String mockNodeName2 = "mockNode2";
        final String[] childrenNames = new String[] { mockNodeName1, mockNodeName2 };
        
        willReturn(childrenNames).given(node).childrenNames();
        
        final Collection<String> result = mapper.getChildrenNames(node);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(mockNodeName1));
        assertTrue(result.contains(mockNodeName2));
        
        then(node).should(times(1)).childrenNames();
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getChildrenNames(Preferences)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testGetChildrenNames_BackingStoreError()
    throws BackingStoreException {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final BackingStoreException mockEx = new BackingStoreException("mock exception");
        
        willThrow(mockEx).given(node).childrenNames();
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.getChildrenNames(node);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(node).should(times(1)).childrenNames();
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getChildrenNames(Preferences)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testGetChildrenNames_NodeDeleted()
    throws BackingStoreException {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final IllegalStateException mockEx = new IllegalStateException("mock exception");
        
        willThrow(mockEx).given(node).childrenNames();
        
        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.getChildrenNames(node);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(node).should(times(1)).childrenNames();
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getChildren(Preferences)}.
     */
    @Test
    void testGetChildren() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockNodeName1 = "mockNode1";
        final Preferences mockChildNode1 = mock(Preferences.class);
        final String mockNodeName2 = "mockNode2";
        final Preferences mockChildNode2 = mock(Preferences.class);
        final Collection<String> childrenNames = Arrays.asList(mockNodeName1, mockNodeName2);
        
        willReturn(childrenNames).given(mapper).getChildrenNames(node);
        willReturn(mockChildNode1).given(node).node(mockNodeName1);
        willReturn(mockChildNode2).given(node).node(mockNodeName2);
        
        final Collection<Preferences> result = mapper.getChildren(node);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(mockChildNode1));
        assertTrue(result.contains(mockChildNode2));
        
        then(mapper).should(times(1)).getChildrenNames(node);
        then(node).should(times(1)).node(mockNodeName1);
        then(node).should(times(1)).node(mockNodeName2);
        then(node).shouldHaveNoMoreInteractions();
        then(mockChildNode1).shouldHaveNoInteractions();
        then(mockChildNode2).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getChildren(Preferences)}.
     */
    @Test
    void testGetChildren_GetChildrenNames_Error() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willThrow(mockEx).given(mapper).getChildrenNames(node);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.getChildren(node);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getChildrenNames(node);
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getChildren(Preferences)}.
     */
    @Test
    void testGetChildren_NodeDeleted() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockNodeName1 = "mockNode1";
        final String mockNodeName2 = "mockNode2";
        final Collection<String> childrenNames = Arrays.asList(mockNodeName1, mockNodeName2);
        final IllegalStateException mockEx = new IllegalStateException("mock exception");
        
        willReturn(childrenNames).given(mapper).getChildrenNames(node);
        willThrow(mockEx).given(node).node(any());
        
        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.getChildren(node);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(mapper).should(times(1)).getChildrenNames(node);
        then(node).should(atLeastOnce()).node(any());
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeChild(Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testRemoveChild()
    throws BackingStoreException {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockNodeName = "mockNode";
        final Preferences mockChildNode = mock(Preferences.class);
        
        willReturn(true).given(node).nodeExists(mockNodeName);
        willReturn(mockChildNode).given(node).node(mockNodeName);
        
        mapper.removeChild(node, mockNodeName);
        
        then(node).should(times(1)).nodeExists(mockNodeName);
        then(node).should(times(1)).node(mockNodeName);
        then(node).shouldHaveNoMoreInteractions();
        then(mockChildNode).should(times(1)).removeNode();
        then(mockChildNode).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeChild(Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testRemoveChild_MissingNode()
    throws BackingStoreException {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockNodeName = "mockNode";
        
        willReturn(false).given(node).nodeExists(mockNodeName);
        
        mapper.removeChild(node, mockNodeName);
        
        then(node).should(times(1)).nodeExists(mockNodeName);
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setAttribute(Preferences, String, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testRemoveChild_NodeExists_BackingStoreException()
    throws BackingStoreException {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockNodeName = "mockNode";
        final BackingStoreException mockEx = new BackingStoreException("mock exception");

        willThrow(mockEx).given(node).nodeExists(mockNodeName);

        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.removeChild(node, mockNodeName);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());

        then(node).should(times(1)).nodeExists(mockNodeName);
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setAttribute(Preferences, String, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testRemoveChild_NodeExists_NodeDeleted()
    throws BackingStoreException {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockNodeName = "mockNode";
        final IllegalStateException mockEx = new IllegalStateException("mock exception");

        willThrow(mockEx).given(node).nodeExists(mockNodeName);

        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.removeChild(node, mockNodeName);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());

        then(node).should(times(1)).nodeExists(mockNodeName);
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#setAttribute(Preferences, String, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testRemoveChild_NodeExists_InvalidKey()
    throws BackingStoreException {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockNodeName = "mockNode";
        final IllegalArgumentException mockEx = new IllegalArgumentException("mock exception");

        willThrow(mockEx).given(node).nodeExists(mockNodeName);

        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.removeChild(node, mockNodeName);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());

        then(node).should(times(1)).nodeExists(mockNodeName);
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeChild(Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testRemoveChild_Node_NodeDeleted()
    throws BackingStoreException {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockNodeName = "mockNode";
        final IllegalStateException mockEx = new IllegalStateException("mock exception");

        willReturn(true).given(node).nodeExists(mockNodeName);
        willThrow(mockEx).given(node).node(mockNodeName);

        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.removeChild(node, mockNodeName);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());

        then(node).should(times(1)).nodeExists(mockNodeName);
        then(node).should(times(1)).node(mockNodeName);
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeChild(Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testRemoveChild_Node_InvalidKey()
    throws BackingStoreException {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockNodeName = "mockNode";
        final IllegalArgumentException mockEx = new IllegalArgumentException("mock exception");

        willReturn(true).given(node).nodeExists(mockNodeName);
        willThrow(mockEx).given(node).node(mockNodeName);

        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.removeChild(node, mockNodeName);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());

        then(node).should(times(1)).nodeExists(mockNodeName);
        then(node).should(times(1)).node(mockNodeName);
        then(node).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeChild(Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testRemoveChild_RemoveNode_BackingStoreException()
    throws BackingStoreException {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockNodeName = "mockNode";
        final Preferences mockChildNode = mock(Preferences.class);
        final BackingStoreException mockEx = new BackingStoreException("mock exception");

        willReturn(true).given(node).nodeExists(mockNodeName);
        willReturn(mockChildNode).given(node).node(mockNodeName);
        willThrow(mockEx).given(mockChildNode).removeNode();

        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.removeChild(node, mockNodeName);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());

        then(node).should(times(1)).nodeExists(mockNodeName);
        then(node).should(times(1)).node(mockNodeName);
        then(node).shouldHaveNoMoreInteractions();
        then(mockChildNode).should(times(1)).removeNode();
        then(mockChildNode).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeChild(Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testRemoveChild_RemoveNode_NodeDeleted()
    throws BackingStoreException {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockNodeName = "mockNode";
        final Preferences mockChildNode = mock(Preferences.class);
        final IllegalStateException mockEx = new IllegalStateException("mock exception");

        willReturn(true).given(node).nodeExists(mockNodeName);
        willReturn(mockChildNode).given(node).node(mockNodeName);
        willThrow(mockEx).given(mockChildNode).removeNode();

        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.removeChild(node, mockNodeName);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());

        then(node).should(times(1)).nodeExists(mockNodeName);
        then(node).should(times(1)).node(mockNodeName);
        then(node).shouldHaveNoMoreInteractions();
        then(mockChildNode).should(times(1)).removeNode();
        then(mockChildNode).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#removeChild(Preferences, String)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testRemoveChild_RemoveNode_RootNode()
    throws BackingStoreException {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String mockNodeName = "mockNode";
        final Preferences mockChildNode = mock(Preferences.class);
        final UnsupportedOperationException mockEx = new UnsupportedOperationException("mock exception");

        willReturn(true).given(node).nodeExists(mockNodeName);
        willReturn(mockChildNode).given(node).node(mockNodeName);
        willThrow(mockEx).given(mockChildNode).removeNode();

        final UnsupportedOperationException result = assertThrows(UnsupportedOperationException.class, () -> {
            mapper.removeChild(node, mockNodeName);
        });
        assertSame(mockEx, result);

        then(node).should(times(1)).nodeExists(mockNodeName);
        then(node).should(times(1)).node(mockNodeName);
        then(node).shouldHaveNoMoreInteractions();
        then(mockChildNode).should(times(1)).removeNode();
        then(mockChildNode).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildren(PreferencesBased, Preferences, Collection)}.
     */
    @Test
    void testSaveChildren() {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode1 = new Object();
        final Object childNode2 = new Object();
        final Collection<Object> nodes = Arrays.asList(childNode1, childNode2);
        final String mockNodeName1 = "mockNode1";
        final String mockNodeName2 = "mockNode2";
        final Collection<String> childrenNames = Arrays.asList(mockNodeName1, mockNodeName2);
        final String childNode2Name = "mockNode3";
        
        willReturn(childrenNames).given(mapper).getChildrenNames(node);
        willReturn(mockNodeName2).given(mapper).getName(childNode1);
        willReturn(childNode2Name).given(mapper).getName(childNode2);
        willDoNothing().given(mapper).saveChildNode(any(), any(), any());
        willDoNothing().given(mapper).removeChild(any(), any());
        
        mapper.saveChildren(config, node, nodes);
        
        then(mapper).should(times(1)).getChildrenNames(node);
        then(mapper).should(times(1)).saveChildNode(config, node, childNode1);
        then(mapper).should(times(1)).saveChildNode(config, node, childNode2);
        then(mapper).should(times(1)).removeChild(node, mockNodeName1);
        then(config).shouldHaveNoInteractions();
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildren(PreferencesBased, Preferences, Collection)}.
     */
    @Test
    void testSaveChildren_GetChildrenNames_Error() {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode1 = new Object();
        final Object childNode2 = new Object();
        final Collection<Object> nodes = Arrays.asList(childNode1, childNode2);
        final String mockNodeName2 = "mockNode2";
        final String childNode2Name = "mockNode3";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willThrow(mockEx).given(mapper).getChildrenNames(node);
        willReturn(mockNodeName2).given(mapper).getName(childNode1);
        willReturn(childNode2Name).given(mapper).getName(childNode2);
        willDoNothing().given(mapper).saveChildNode(any(), any(), any());
        willDoNothing().given(mapper).removeChild(any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.saveChildren(config, node, nodes);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getChildrenNames(node);
        then(mapper).should(never()).saveChildNode(any(), any(), any());
        then(mapper).should(never()).removeChild(any(), any());
        then(config).shouldHaveNoInteractions();
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildren(PreferencesBased, Preferences, Collection)}.
     */
    @Test
    void testSaveChildren_SaveChildNode_Error() {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode1 = new Object();
        final Object childNode2 = new Object();
        final Collection<Object> nodes = Arrays.asList(childNode1, childNode2);
        final String mockNodeName1 = "mockNode1";
        final String mockNodeName2 = "mockNode2";
        final Collection<String> childrenNames = Arrays.asList(mockNodeName1, mockNodeName2);
        final String childNode2Name = "mockNode3";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(childrenNames).given(mapper).getChildrenNames(node);
        willReturn(mockNodeName2).given(mapper).getName(childNode1);
        willReturn(childNode2Name).given(mapper).getName(childNode2);
        willThrow(mockEx).given(mapper).saveChildNode(any(), any(), any());
        willDoNothing().given(mapper).removeChild(any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.saveChildren(config, node, nodes);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getChildrenNames(node);
        then(mapper).should(atLeastOnce()).saveChildNode(any(), any(), any());
        then(config).shouldHaveNoInteractions();
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildren(PreferencesBased, Preferences, Collection)}.
     */
    @Test
    void testSaveChildren_RemoveChild_Error() {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode1 = new Object();
        final Object childNode2 = new Object();
        final Collection<Object> nodes = Arrays.asList(childNode1, childNode2);
        final String mockNodeName1 = "mockNode1";
        final String mockNodeName2 = "mockNode2";
        final Collection<String> childrenNames = Arrays.asList(mockNodeName1, mockNodeName2);
        final String childNode2Name = "mockNode3";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(childrenNames).given(mapper).getChildrenNames(node);
        willReturn(mockNodeName2).given(mapper).getName(childNode1);
        willReturn(childNode2Name).given(mapper).getName(childNode2);
        willDoNothing().given(mapper).saveChildNode(any(), any(), any());
        willThrow(mockEx).given(mapper).removeChild(any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.saveChildren(config, node, nodes);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getChildrenNames(node);
        then(mapper).should(atLeastOnce()).removeChild(node, mockNodeName1);
        then(config).shouldHaveNoInteractions();
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildNode(PreferencesBased, Preferences, Object)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @CartesianProductTest
    void testSaveChildNode(
            final boolean nullValue,
            final boolean attrsEmpty,
            final boolean chidrenEmpty,
            final boolean childExists)
    throws BackingStoreException {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode = new Object();
        final String childNodeName = "mockChildNode";
        final Object childNodeValue = nullValue ? null : new Object();
        final String childNodeStrValue = "mockChildValue";
        @SuppressWarnings("unchecked")
        final Map<String, Object> childNodeAttrs = mock(Map.class);
        @SuppressWarnings("unchecked")
        final Collection<Object> childNodeChilds = mock(Collection.class);
        final Preferences childPrefs = mock(Preferences.class);
        
        willReturn(childNodeName).given(mapper).getName(childNode);
        willReturn(childNodeValue).given(mapper).getValue(childNode);
        willReturn(childNodeStrValue).given(mapper).convertValue(config, childNodeValue);
        willReturn(childNodeAttrs).given(mapper).getAttributes(childNode);
        willReturn(childNodeChilds).given(mapper).getChildren(childNode);
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).setAttributes(any(), any(), any());
        willDoNothing().given(mapper).saveChildren(any(), any(), any());
        willReturn(attrsEmpty).given(childNodeAttrs).isEmpty();
        willReturn(chidrenEmpty).given(childNodeChilds).isEmpty();
        willReturn(childExists).given(node).nodeExists(childNodeName);
        willReturn(childPrefs).given(node).node(childNodeName);
        
        mapper.saveChildNode(config, node, childNode);
        
        if (!attrsEmpty || !chidrenEmpty || childExists) {
            then(mapper).should(times(1)).setAttributes(config, childPrefs, childNodeAttrs);
            then(mapper).should(times(1)).saveChildren(config, childPrefs, childNodeChilds);
            then(node).should(atLeastOnce()).node(childNodeName);
        } else {
            then(mapper).should(never()).setAttributes(config, childPrefs, childNodeAttrs);
            then(mapper).should(never()).saveChildren(any(), any(), any());
            then(node).should(never()).node(childNodeName);
        }
        then(mapper).should(times(1)).setChildValue(node, childNodeName, childNodeStrValue);
        then(node).should(atLeast(0)).nodeExists(childNodeName);
        then(node).shouldHaveNoMoreInteractions();
        then(config).shouldHaveNoInteractions();
    }

    static CartesianProductTest.Sets testSaveChildNode() {
        return new CartesianProductTest.Sets()
                .add(false, true)
                .add(false, true)
                .add(false, true)
                .add(false, true);
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildNode(PreferencesBased, Preferences, Object)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testSaveChildNode_GetName_Error()
    throws BackingStoreException {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode = new Object();
        final Object childNodeValue = new Object();
        final String childNodeStrValue = "mockChildValue";
        @SuppressWarnings("unchecked")
        final Map<String, Object> childNodeAttrs = mock(Map.class);
        @SuppressWarnings("unchecked")
        final Collection<Object> childNodeChilds = mock(Collection.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willThrow(mockEx).given(mapper).getName(childNode);
        willReturn(childNodeValue).given(mapper).getValue(childNode);
        willReturn(childNodeStrValue).given(mapper).convertValue(config, childNodeValue);
        willReturn(childNodeAttrs).given(mapper).getAttributes(childNode);
        willReturn(childNodeChilds).given(mapper).getChildren(childNode);
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).setAttributes(any(), any(), any());
        willDoNothing().given(mapper).saveChildren(any(), any(), any());
        willReturn(false).given(childNodeAttrs).isEmpty();
        willReturn(false).given(childNodeChilds).isEmpty();
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.saveChildNode(config, node, childNode);
        });
        assertSame(mockEx, result);
        
        then(node).shouldHaveNoInteractions();
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildNode(PreferencesBased, Preferences, Object)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testSaveChildNode_GetValue_Error()
    throws BackingStoreException {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode = new Object();
        final String childNodeName = "mockChildNode";
        @SuppressWarnings("unchecked")
        final Map<String, Object> childNodeAttrs = mock(Map.class);
        @SuppressWarnings("unchecked")
        final Collection<Object> childNodeChilds = mock(Collection.class);
        final Preferences childPrefs = mock(Preferences.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(childNodeName).given(mapper).getName(childNode);
        willThrow(mockEx).given(mapper).getValue(childNode);
        willReturn(childNodeAttrs).given(mapper).getAttributes(childNode);
        willReturn(childNodeChilds).given(mapper).getChildren(childNode);
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).setAttributes(any(), any(), any());
        willDoNothing().given(mapper).saveChildren(any(), any(), any());
        willReturn(false).given(childNodeAttrs).isEmpty();
        willReturn(false).given(childNodeChilds).isEmpty();
        willReturn(true).given(node).nodeExists(childNodeName);
        willReturn(childPrefs).given(node).node(childNodeName);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.saveChildNode(config, node, childNode);
        });
        assertSame(mockEx, result);
        
        then(node).should(atLeast(0)).nodeExists(childNodeName);
        then(node).should(atLeast(0)).node(childNodeName);
        then(node).shouldHaveNoMoreInteractions();
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildNode(PreferencesBased, Preferences, Object)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testSaveChildNode_GetAttributes_Error()
    throws BackingStoreException {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode = new Object();
        final String childNodeName = "mockChildNode";
        final Object childNodeValue = new Object();
        final String childNodeStrValue = "mockChildValue";
        @SuppressWarnings("unchecked")
        final Collection<Object> childNodeChilds = mock(Collection.class);
        final Preferences childPrefs = mock(Preferences.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(childNodeName).given(mapper).getName(childNode);
        willReturn(childNodeValue).given(mapper).getValue(childNode);
        willReturn(childNodeStrValue).given(mapper).convertValue(config, childNodeValue);
        willThrow(mockEx).given(mapper).getAttributes(childNode);
        willReturn(childNodeChilds).given(mapper).getChildren(childNode);
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).setAttributes(any(), any(), any());
        willDoNothing().given(mapper).saveChildren(any(), any(), any());
        willReturn(false).given(childNodeChilds).isEmpty();
        willReturn(true).given(node).nodeExists(childNodeName);
        willReturn(childPrefs).given(node).node(childNodeName);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.saveChildNode(config, node, childNode);
        });
        assertSame(mockEx, result);
        
        then(node).should(atLeast(0)).nodeExists(childNodeName);
        then(node).should(atLeast(0)).node(childNodeName);
        then(node).shouldHaveNoMoreInteractions();
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildNode(PreferencesBased, Preferences, Object)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testSaveChildNode_GetChildren_Error()
    throws BackingStoreException {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode = new Object();
        final String childNodeName = "mockChildNode";
        final Object childNodeValue = new Object();
        final String childNodeStrValue = "mockChildValue";
        @SuppressWarnings("unchecked")
        final Map<String, Object> childNodeAttrs = mock(Map.class);
        final Preferences childPrefs = mock(Preferences.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(childNodeName).given(mapper).getName(childNode);
        willReturn(childNodeValue).given(mapper).getValue(childNode);
        willReturn(childNodeStrValue).given(mapper).convertValue(config, childNodeValue);
        willReturn(childNodeAttrs).given(mapper).getAttributes(childNode);
        willThrow(mockEx).given(mapper).getChildren(childNode);
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).setAttributes(any(), any(), any());
        willDoNothing().given(mapper).saveChildren(any(), any(), any());
        willReturn(false).given(childNodeAttrs).isEmpty();
        willReturn(true).given(node).nodeExists(childNodeName);
        willReturn(childPrefs).given(node).node(childNodeName);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.saveChildNode(config, node, childNode);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).setChildValue(node, childNodeName, childNodeStrValue);
        then(node).shouldHaveNoInteractions();
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildNode(PreferencesBased, Preferences, Object)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testSaveChildNode_SetChildValue_Error()
    throws BackingStoreException {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode = new Object();
        final String childNodeName = "mockChildNode";
        final Object childNodeValue = new Object();
        final String childNodeStrValue = "mockChildValue";
        @SuppressWarnings("unchecked")
        final Map<String, Object> childNodeAttrs = mock(Map.class);
        @SuppressWarnings("unchecked")
        final Collection<Object> childNodeChilds = mock(Collection.class);
        final Preferences childPrefs = mock(Preferences.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(childNodeName).given(mapper).getName(childNode);
        willReturn(childNodeValue).given(mapper).getValue(childNode);
        willReturn(childNodeStrValue).given(mapper).convertValue(config, childNodeValue);
        willReturn(childNodeAttrs).given(mapper).getAttributes(childNode);
        willReturn(childNodeChilds).given(mapper).getChildren(childNode);
        willThrow(mockEx).given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).setAttributes(any(), any(), any());
        willDoNothing().given(mapper).saveChildren(any(), any(), any());
        willReturn(false).given(childNodeAttrs).isEmpty();
        willReturn(false).given(childNodeChilds).isEmpty();
        willReturn(true).given(node).nodeExists(childNodeName);
        willReturn(childPrefs).given(node).node(childNodeName);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.saveChildNode(config, node, childNode);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).setChildValue(node, childNodeName, childNodeStrValue);
        then(node).should(atLeast(0)).nodeExists(childNodeName);
        then(node).should(atLeast(0)).node(childNodeName);
        then(node).shouldHaveNoMoreInteractions();
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildNode(PreferencesBased, Preferences, Object)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testSaveChildNode_SetAttributes_Error()
    throws BackingStoreException {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode = new Object();
        final String childNodeName = "mockChildNode";
        final Object childNodeValue = new Object();
        final String childNodeStrValue = "mockChildValue";
        @SuppressWarnings("unchecked")
        final Map<String, Object> childNodeAttrs = mock(Map.class);
        @SuppressWarnings("unchecked")
        final Collection<Object> childNodeChilds = mock(Collection.class);
        final Preferences childPrefs = mock(Preferences.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(childNodeName).given(mapper).getName(childNode);
        willReturn(childNodeValue).given(mapper).getValue(childNode);
        willReturn(childNodeStrValue).given(mapper).convertValue(config, childNodeValue);
        willReturn(childNodeAttrs).given(mapper).getAttributes(childNode);
        willReturn(childNodeChilds).given(mapper).getChildren(childNode);
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willThrow(mockEx).given(mapper).setAttributes(any(), any(), any());
        willDoNothing().given(mapper).saveChildren(any(), any(), any());
        willReturn(false).given(childNodeAttrs).isEmpty();
        willReturn(false).given(childNodeChilds).isEmpty();
        willReturn(true).given(node).nodeExists(childNodeName);
        willReturn(childPrefs).given(node).node(childNodeName);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.saveChildNode(config, node, childNode);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).setAttributes(config, childPrefs, childNodeAttrs);
        then(node).should(atLeast(0)).nodeExists(childNodeName);
        then(node).should(atLeast(0)).node(childNodeName);
        then(node).shouldHaveNoMoreInteractions();
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildNode(PreferencesBased, Preferences, Object)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testSaveChildNode_SaveChildren_Error()
    throws BackingStoreException {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode = new Object();
        final String childNodeName = "mockChildNode";
        final Object childNodeValue = new Object();
        final String childNodeStrValue = "mockChildValue";
        @SuppressWarnings("unchecked")
        final Map<String, Object> childNodeAttrs = mock(Map.class);
        @SuppressWarnings("unchecked")
        final Collection<Object> childNodeChilds = mock(Collection.class);
        final Preferences childPrefs = mock(Preferences.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(childNodeName).given(mapper).getName(childNode);
        willReturn(childNodeValue).given(mapper).getValue(childNode);
        willReturn(childNodeStrValue).given(mapper).convertValue(config, childNodeValue);
        willReturn(childNodeAttrs).given(mapper).getAttributes(childNode);
        willReturn(childNodeChilds).given(mapper).getChildren(childNode);
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).setAttributes(any(), any(), any());
        willThrow(mockEx).given(mapper).saveChildren(any(), any(), any());
        willReturn(false).given(childNodeAttrs).isEmpty();
        willReturn(false).given(childNodeChilds).isEmpty();
        willReturn(true).given(node).nodeExists(childNodeName);
        willReturn(childPrefs).given(node).node(childNodeName);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.saveChildNode(config, node, childNode);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).saveChildren(config, childPrefs, childNodeChilds);
        then(node).should(atLeast(0)).nodeExists(childNodeName);
        then(node).should(atLeast(0)).node(childNodeName);
        then(node).shouldHaveNoMoreInteractions();
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildNode(PreferencesBased, Preferences, Object)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testSaveChildNode_NodeExists_BackingStoreException()
    throws BackingStoreException {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode = new Object();
        final String childNodeName = "mockChildNode";
        final Object childNodeValue = new Object();
        final String childNodeStrValue = "mockChildValue";
        @SuppressWarnings("unchecked")
        final Map<String, Object> childNodeAttrs = mock(Map.class);
        @SuppressWarnings("unchecked")
        final Collection<Object> childNodeChilds = mock(Collection.class);
        final Preferences childPrefs = mock(Preferences.class);
        final BackingStoreException mockEx = new BackingStoreException("mock exception");
        
        willReturn(childNodeName).given(mapper).getName(childNode);
        willReturn(childNodeValue).given(mapper).getValue(childNode);
        willReturn(childNodeStrValue).given(mapper).convertValue(config, childNodeValue);
        willReturn(childNodeAttrs).given(mapper).getAttributes(childNode);
        willReturn(childNodeChilds).given(mapper).getChildren(childNode);
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).setAttributes(any(), any(), any());
        willDoNothing().given(mapper).saveChildren(any(), any(), any());
        willReturn(true).given(childNodeAttrs).isEmpty();
        willReturn(true).given(childNodeChilds).isEmpty();
        willThrow(mockEx).given(node).nodeExists(childNodeName);
        willReturn(childPrefs).given(node).node(childNodeName);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.saveChildNode(config, node, childNode);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(node).should(atLeast(1)).nodeExists(childNodeName);
        then(node).should(atLeast(0)).node(childNodeName);
        then(node).shouldHaveNoMoreInteractions();
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildNode(PreferencesBased, Preferences, Object)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testSaveChildNode_NodeExists_NodeDeleted()
    throws BackingStoreException {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode = new Object();
        final String childNodeName = "mockChildNode";
        final Object childNodeValue = new Object();
        final String childNodeStrValue = "mockChildValue";
        @SuppressWarnings("unchecked")
        final Map<String, Object> childNodeAttrs = mock(Map.class);
        @SuppressWarnings("unchecked")
        final Collection<Object> childNodeChilds = mock(Collection.class);
        final Preferences childPrefs = mock(Preferences.class);
        final IllegalStateException mockEx = new IllegalStateException("mock exception");
        
        willReturn(childNodeName).given(mapper).getName(childNode);
        willReturn(childNodeValue).given(mapper).getValue(childNode);
        willReturn(childNodeStrValue).given(mapper).convertValue(config, childNodeValue);
        willReturn(childNodeAttrs).given(mapper).getAttributes(childNode);
        willReturn(childNodeChilds).given(mapper).getChildren(childNode);
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).setAttributes(any(), any(), any());
        willDoNothing().given(mapper).saveChildren(any(), any(), any());
        willReturn(true).given(childNodeAttrs).isEmpty();
        willReturn(true).given(childNodeChilds).isEmpty();
        willThrow(mockEx).given(node).nodeExists(childNodeName);
        willReturn(childPrefs).given(node).node(childNodeName);
        
        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.saveChildNode(config, node, childNode);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(node).should(atLeast(1)).nodeExists(childNodeName);
        then(node).should(atLeast(0)).node(childNodeName);
        then(node).shouldHaveNoMoreInteractions();
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildNode(PreferencesBased, Preferences, Object)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testSaveChildNode_NodeExists_InvalidKey()
    throws BackingStoreException {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode = new Object();
        final String childNodeName = "mockChildNode";
        final Object childNodeValue = new Object();
        final String childNodeStrValue = "mockChildValue";
        @SuppressWarnings("unchecked")
        final Map<String, Object> childNodeAttrs = mock(Map.class);
        @SuppressWarnings("unchecked")
        final Collection<Object> childNodeChilds = mock(Collection.class);
        final Preferences childPrefs = mock(Preferences.class);
        final IllegalArgumentException mockEx = new IllegalArgumentException("mock exception");
        
        willReturn(childNodeName).given(mapper).getName(childNode);
        willReturn(childNodeValue).given(mapper).getValue(childNode);
        willReturn(childNodeStrValue).given(mapper).convertValue(config, childNodeValue);
        willReturn(childNodeAttrs).given(mapper).getAttributes(childNode);
        willReturn(childNodeChilds).given(mapper).getChildren(childNode);
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).setAttributes(any(), any(), any());
        willDoNothing().given(mapper).saveChildren(any(), any(), any());
        willReturn(true).given(childNodeAttrs).isEmpty();
        willReturn(true).given(childNodeChilds).isEmpty();
        willThrow(mockEx).given(node).nodeExists(childNodeName);
        willReturn(childPrefs).given(node).node(childNodeName);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.saveChildNode(config, node, childNode);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(node).should(atLeast(1)).nodeExists(childNodeName);
        then(node).should(atLeast(0)).node(childNodeName);
        then(node).shouldHaveNoMoreInteractions();
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildNode(PreferencesBased, Preferences, Object)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testSaveChildNode_Node_NodeDeleted()
    throws BackingStoreException {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode = new Object();
        final String childNodeName = "mockChildNode";
        final Object childNodeValue = new Object();
        final String childNodeStrValue = "mockChildValue";
        @SuppressWarnings("unchecked")
        final Map<String, Object> childNodeAttrs = mock(Map.class);
        @SuppressWarnings("unchecked")
        final Collection<Object> childNodeChilds = mock(Collection.class);
        final IllegalStateException mockEx = new IllegalStateException("mock exception");
        
        willReturn(childNodeName).given(mapper).getName(childNode);
        willReturn(childNodeValue).given(mapper).getValue(childNode);
        willReturn(childNodeStrValue).given(mapper).convertValue(config, childNodeValue);
        willReturn(childNodeAttrs).given(mapper).getAttributes(childNode);
        willReturn(childNodeChilds).given(mapper).getChildren(childNode);
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).setAttributes(any(), any(), any());
        willThrow(mockEx).given(mapper).saveChildren(any(), any(), any());
        willReturn(false).given(childNodeAttrs).isEmpty();
        willReturn(false).given(childNodeChilds).isEmpty();
        willReturn(true).given(node).nodeExists(childNodeName);
        willThrow(mockEx).given(node).node(childNodeName);
        
        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.saveChildNode(config, node, childNode);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(node).should(atLeast(0)).nodeExists(childNodeName);
        then(node).should(atLeast(1)).node(childNodeName);
        then(node).shouldHaveNoMoreInteractions();
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#saveChildNode(PreferencesBased, Preferences, Object)}.
     * @throws BackingStoreException Shouldn't happen
     */
    @Test
    void testSaveChildNode_Node_InvalidKey()
    throws BackingStoreException {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final Object childNode = new Object();
        final String childNodeName = "mockChildNode";
        final Object childNodeValue = new Object();
        final String childNodeStrValue = "mockChildValue";
        @SuppressWarnings("unchecked")
        final Map<String, Object> childNodeAttrs = mock(Map.class);
        @SuppressWarnings("unchecked")
        final Collection<Object> childNodeChilds = mock(Collection.class);
        final IllegalArgumentException mockEx = new IllegalArgumentException("mock exception");
        
        willReturn(childNodeName).given(mapper).getName(childNode);
        willReturn(childNodeValue).given(mapper).getValue(childNode);
        willReturn(childNodeStrValue).given(mapper).convertValue(config, childNodeValue);
        willReturn(childNodeAttrs).given(mapper).getAttributes(childNode);
        willReturn(childNodeChilds).given(mapper).getChildren(childNode);
        willDoNothing().given(mapper).setChildValue(any(), any(), any());
        willDoNothing().given(mapper).setAttributes(any(), any(), any());
        willDoNothing().given(mapper).saveChildren(any(), any(), any());
        willReturn(false).given(childNodeAttrs).isEmpty();
        willReturn(false).given(childNodeChilds).isEmpty();
        willReturn(true).given(node).nodeExists(childNodeName);
        willThrow(mockEx).given(node).node(childNodeName);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.saveChildNode(config, node, childNode);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
        
        then(node).should(atLeast(0)).nodeExists(childNodeName);
        then(node).should(atLeast(1)).node(childNodeName);
        then(node).shouldHaveNoMoreInteractions();
        then(config).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#convertValue(PreferencesBased, Object)}.
     */
    @Test
    void testConvertValue() {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final ConversionHandler handler = mock(ConversionHandler.class);
        final Object value = new Object();
        final String strValue = "mockValue";
        
        willReturn(handler).given(config).getConversionHandler();
        willReturn(strValue).given(handler).to(value, String.class, null);
        
        final String result = mapper.convertValue(config, value);
        assertEquals(strValue, result);
        
        then(config).should(times(1)).getConversionHandler();
        then(config).shouldHaveNoMoreInteractions();
        then(handler).should(times(1)).to(value, String.class, null);
        then(handler).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#convertValue(PreferencesBased, Object)}.
     */
    @Test
    void testConvertValue_ConversionException() {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final ConversionHandler handler = mock(ConversionHandler.class);
        final Object value = new Object();
        final ConversionException mockEx = new ConversionException("mock exception");
        
        willReturn(handler).given(config).getConversionHandler();
        willThrow(mockEx).given(handler).to(value, String.class, null);
        
        final String result = mapper.convertValue(config, value);
        assertEquals(value.toString(), result);
        
        then(config).should(times(1)).getConversionHandler();
        then(config).shouldHaveNoMoreInteractions();
        then(handler).should(times(1)).to(value, String.class, null);
        then(handler).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#convertValue(PreferencesBased, Object)}.
     */
    @Test
    void testConvertValue_ConversionException_Null() {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final ConversionHandler handler = mock(ConversionHandler.class);
        final Object value = null;
        final ConversionException mockEx = new ConversionException("mock exception");
        
        willReturn(handler).given(config).getConversionHandler();
        willThrow(mockEx).given(handler).to(value, String.class, null);
        
        final String result = mapper.convertValue(config, value);
        assertNull(result);
        
        then(config).should(times(1)).getConversionHandler();
        then(config).shouldHaveNoMoreInteractions();
        then(handler).should(times(1)).to(value, String.class, null);
        then(handler).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getPropertyData(PreferencesBased, String)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testGetPropertyData() {
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final NodeAddData<Object> addData = mock(NodeAddData.class);
        final List<String> nodePath = Arrays.asList("parent", "path");
        final Object rootNode = new Object();
        final String key = "mockKey";
        final Object addDataParent = new Object();
        final List<String> addDataPathNodes = Arrays.asList("extra", "path", "nodes");
        final String addDataNewNodeName = "mockNodeName";
        final boolean addDataIsAttr = true;
        final List<String> expectedPath = Arrays.asList("parent", "path", "extra", "path", "nodes");
        
        expectNodeModel();
        willReturn(rootNode).given(handler).getRootNode();
        willReturn(addData).given(expressionEngine).prepareAdd(rootNode, key, handler);
        willReturn(addDataParent).given(addData).getParent();
        willReturn(addDataPathNodes).given(addData).getPathNodes();
        willReturn(addDataNewNodeName).given(addData).getNewNodeName();
        willReturn(addDataIsAttr).given(addData).isAttribute();
        willReturn(nodePath).given(mapper).getNodePath(handler, addDataParent);
        
        final PropertyData result = mapper.getPropertyData(config, key);
        assertNotNull(result);
        assertEquals(expectedPath, result.getPath());
        assertEquals(addDataNewNodeName, result.getName());
        assertEquals(addDataIsAttr, result.isAttribute());
        
        then(config).should(atLeastOnce()).getNodeModel();
        then(config).should(atLeastOnce()).getExpressionEngine();
        then(config).shouldHaveNoMoreInteractions();
        then(model).should(atLeastOnce()).getNodeHandler();
        then(model).shouldHaveNoMoreInteractions();
        then(handler).should(atLeastOnce()).getRootNode();
        then(handler).shouldHaveNoMoreInteractions();
        then(expressionEngine).should(times(1)).prepareAdd(rootNode, key, handler);
        then(expressionEngine).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getNodePath(NodeHandler, Object)}.
     */
    @Test
    void testGetNodePath() {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Object node = new Object();
        final Object parentNode = new Object();
        final Object rootNode = new Object();
        final String nodeName = "mockName";
        final List<String> parentPath = new ArrayList<>();
        parentPath.addAll(Arrays.asList("mock", "parent", "path", "nodes"));
        final List<String> expectedPath = Arrays.asList("mock", "parent", "path", "nodes", "mockName");
        
        willReturn(rootNode).given(handler).getRootNode();
        willReturn(parentNode).given(handler).getParent(node);
        willReturn(nodeName).given(mapper).getName(node);
        willReturn(parentPath).given(mapper).getNodePath(handler, parentNode);
        
        final List<String> result = mapper.getNodePath(handler, node);
        assertNotNull(result);
        assertSame(result, parentPath);
        assertEquals(expectedPath, result);
        
        then(handler).should(atLeastOnce()).getRootNode();
        then(handler).should(atLeastOnce()).getParent(node);
        then(handler).shouldHaveNoMoreInteractions();
        then(mapper).should(times(1)).getNodePath(handler, parentNode);
    }

    /**
     * Test for {@link AbstractPreferencesMapper#getNodePath(NodeHandler, Object)}.
     */
    @Test
    void testGetNodePath_Root() {
        @SuppressWarnings("unchecked")
        final AbstractPreferencesMapper<Object> mapper = spy(AbstractPreferencesMapper.class);
        final Object node = new Object();
        final Object rootNode = node;
        final String nodeName = "mockName";
        
        willReturn(rootNode).given(handler).getRootNode();
        willReturn(null).given(handler).getParent(node);
        willReturn(nodeName).given(mapper).getName(node);
        
        final List<String> result = mapper.getNodePath(handler, node);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        then(handler).should(atLeastOnce()).getRootNode();
        then(handler).should(atLeast(0)).getParent(node);
        then(handler).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link AbstractPreferencesMapper#resolvePreferencesNode(Preferences, Collection)}.
     */
    @Test
    void testResolvePreferencesNode() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences baseNode = mock(Preferences.class);
        final Preferences pathNode1 = mock(Preferences.class);
        final Preferences pathNode2 = mock(Preferences.class);
        final Preferences pathNode3 = mock(Preferences.class);
        final String pathNodeName1 = "node1";
        final String pathNodeName2 = "node2";
        final String pathNodeName3 = "node3";
        final Collection<String> names = Arrays.asList(pathNodeName1, pathNodeName2, pathNodeName3);
        willReturn(pathNode1).given(baseNode).node(pathNodeName1);
        willReturn(pathNode2).given(pathNode1).node(pathNodeName2);
        willReturn(pathNode3).given(pathNode2).node(pathNodeName3);
        
        final Preferences result = mapper.resolvePreferencesNode(baseNode, names);
        assertNotNull(result);
        assertSame(pathNode3, result);
    }

    /**
     * Test for {@link AbstractPreferencesMapper#resolvePreferencesNode(Preferences, Collection)}.
     */
    @Test
    void testResolvePreferencesNode_NodeDeleted() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences baseNode = mock(Preferences.class);
        final String pathNodeName1 = "node1";
        final String pathNodeName2 = "node2";
        final String pathNodeName3 = "node3";
        final Collection<String> names = Arrays.asList(pathNodeName1, pathNodeName2, pathNodeName3);
        final IllegalStateException mockEx = new IllegalStateException("mock exception");
        
        willThrow(mockEx).given(baseNode).node(any());
        
        final PreferencesNodeDeletedException result = assertThrows(PreferencesNodeDeletedException.class, () -> {
            mapper.resolvePreferencesNode(baseNode, names);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#resolvePreferencesNode(Preferences, Collection)}.
     */
    @Test
    void testResolvePreferencesNode_InvalidKey() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences baseNode = mock(Preferences.class);
        final String pathNodeName1 = "node1";
        final String pathNodeName2 = "node2";
        final String pathNodeName3 = "node3";
        final Collection<String> names = Arrays.asList(pathNodeName1, pathNodeName2, pathNodeName3);
        final IllegalArgumentException mockEx = new IllegalArgumentException("mock exception");
        
        willThrow(mockEx).given(baseNode).node(any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.resolvePreferencesNode(baseNode, names);
        });
        assertNotNull(result.getCause());
        assertSame(mockEx, result.getCause());
    }

    /**
     * Test for {@link AbstractPreferencesMapper#resolvePreferencesNode(Preferences, String...)}.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testResolvePreferencesNode_Varargs() {
        final AbstractPreferencesMapper<?> mapper = spy(AbstractPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String[] names = new String[] { };
        final Preferences mockResult = mock(Preferences.class);
        
        willReturn(mockResult).given(mapper).resolvePreferencesNode(same(node), any(Collection.class));
        
        final Preferences result = mapper.resolvePreferencesNode(node, names);
        assertNotNull(result);
        assertSame(mockResult, result);
        
        final ArgumentCaptor<Collection<String>> captor = ArgumentCaptor.forClass(Collection.class);
        then(mapper).should(times(1)).resolvePreferencesNode(any(), any(String[].class));
        then(mapper).should(times(1)).resolvePreferencesNode(any(), captor.capture());
        then(mapper).shouldHaveNoMoreInteractions();
        assertArrayEquals(names, captor.getValue().toArray(new String[0]));
    }
}
