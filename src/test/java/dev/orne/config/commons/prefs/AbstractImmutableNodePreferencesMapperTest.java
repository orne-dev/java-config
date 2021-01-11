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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code AbstractImmutableNodePreferencesMapper}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see AbstractImmutableNodePreferencesMapper
 */
@Tag("ut")
class AbstractImmutableNodePreferencesMapperTest {

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#loadNodeHierarchy(Preferences, Map)}.
     */
    @Test
    void testLoadNodeHierarchy() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final Preferences rootNode = mock(Preferences.class);
        final String rootNodeName = "mockRootNode";
        @SuppressWarnings("unchecked")
        final Map<ImmutableNode, Preferences> elemRefs = mock(Map.class);
        
        willDoNothing().given(mapper).constructHierarchy(any(), any(), any());
        willReturn(rootNodeName).given(rootNode).name();
        
        final ImmutableNode result = mapper.loadNodeHierarchy(rootNode, elemRefs);
        assertNotNull(result);
        assertEquals(rootNodeName, result.getNodeName());
        
        then(mapper).should(times(1)).constructHierarchy(any(), same(rootNode), same(elemRefs));
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#loadNodeHierarchy(Preferences, Map)}.
     */
    @Test
    void testLoadNodeHierarchy_NoReferences() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final Preferences rootNode = mock(Preferences.class);
        final String rootNodeName = "mockRootNode";
        
        willDoNothing().given(mapper).constructHierarchy(any(), any(), any());
        willReturn(rootNodeName).given(rootNode).name();
        
        final ImmutableNode result = mapper.loadNodeHierarchy(rootNode, null);
        assertNotNull(result);
        assertEquals(rootNodeName, result.getNodeName());
        
        then(mapper).should(times(1)).constructHierarchy(any(), same(rootNode), eq(null));
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#loadNodeHierarchy(Preferences, Map)}.
     */
    @Test
    void testLoadNodeHierarchy_ConstructHierarchy_Error() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final Preferences rootNode = mock(Preferences.class);
        final String rootNodeName = "mockRootNode";
        @SuppressWarnings("unchecked")
        final Map<ImmutableNode, Preferences> elemRefs = mock(Map.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willThrow(mockEx).given(mapper).constructHierarchy(any(), any(), any());
        willReturn(rootNodeName).given(rootNode).name();
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.loadNodeHierarchy(rootNode, elemRefs);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).constructHierarchy(any(), same(rootNode), same(elemRefs));
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#createNodeForName(String)}.
     */
    @Test
    void testCreateNodeForName() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final String nodeName = "mockNode";
        
        final ImmutableNode result = mapper.createNodeForName(nodeName);
        assertNotNull(result);
        assertEquals(nodeName, result.getNodeName());
        assertNull(result.getValue());
        assertTrue(result.getAttributes().isEmpty());
        assertTrue(result.getChildren().isEmpty());
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#getName(ImmutableNode)}.
     */
    @Test
    void testGetName() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final String nodeName = "mockNode";
        final ImmutableNode node = new ImmutableNode.Builder()
                .name(nodeName)
                .create();
        
        final String result = mapper.getName(node);
        assertNotNull(result);
        assertEquals(nodeName, result);
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#getValue(ImmutableNode)}.
     */
    @Test
    void testGetValue() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final Object nodeValue = new Object();
        final ImmutableNode node = new ImmutableNode.Builder()
                .value(nodeValue)
                .create();
        
        final Object result = mapper.getValue(node);
        assertNotNull(result);
        assertEquals(nodeValue, result);
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#getAttributesNames(ImmutableNode)}.
     */
    @Test
    void testGetAttributesNames() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final String attrName1 = "mockAttr1";
        final String attrName2 = "mockAttr2";
        final String grandchildAttrName = "mockGrandchildAttr1";
        final ImmutableNode node = new ImmutableNode.Builder()
                .addAttribute(attrName1, "value1")
                .addAttribute(attrName2, "value2")
                .addChild(new ImmutableNode.Builder()
                        .addAttribute(grandchildAttrName, "value3")
                        .create())
                .create();
        
        final Collection<String> result = mapper.getAttributesNames(node);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(attrName1));
        assertTrue(result.contains(attrName2));
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#getAttributes(ImmutableNode)}.
     */
    @Test
    void testGetAttributes() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final String attrName1 = "mockAttr1";
        final Object attrValue1 = new Object();
        final String attrName2 = "mockAttr2";
        final Object attrValue2 = new Object();
        final String grandchildAttrName = "mockGrandchildAttr1";
        final ImmutableNode node = new ImmutableNode.Builder()
                .addAttribute(attrName1, attrValue1)
                .addAttribute(attrName2, attrValue2)
                .addChild(new ImmutableNode.Builder()
                        .addAttribute(grandchildAttrName, "value3")
                        .create())
                .create();
        
        final Map<String, Object> result = mapper.getAttributes(node);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey(attrName1));
        assertSame(attrValue1, result.get(attrName1));
        assertTrue(result.containsKey(attrName2));
        assertSame(attrValue2, result.get(attrName2));
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#getAttribute(ImmutableNode, String)}.
     */
    @Test
    void testGetAttribute() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final String attrName1 = "mockAttr1";
        final Object attrValue1 = new Object();
        final String attrName2 = "mockAttr2";
        final ImmutableNode node = new ImmutableNode.Builder()
                .addAttribute(attrName1, attrValue1)
                .addAttribute(attrName2, "value2")
                .create();
        
        final Object result = mapper.getAttribute(node, attrName1);
        assertNotNull(result);
        assertSame(attrValue1, result);
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#getChildrenNames(ImmutableNode)}.
     */
    @Test
    void testGetChildrenNames() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final String childName1 = "mockChild1";
        final String childName2 = "mockChild2";
        final String grandchildName = "mockGrandchild1";
        final ImmutableNode node = new ImmutableNode.Builder()
                .addChild(new ImmutableNode.Builder()
                        .name(childName1)
                        .create())
                .addChild(new ImmutableNode.Builder()
                        .name(childName2)
                        .addChild(new ImmutableNode.Builder()
                                .name(grandchildName)
                                .create())
                        .create())
                .create();
        
        final Collection<String> result = mapper.getChildrenNames(node);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(childName1));
        assertTrue(result.contains(childName2));
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#getChildren(ImmutableNode)}.
     */
    @Test
    void testGetChildren() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final String childName1 = "mockChild1";
        final String grandchildName = "mockGrandchild1";
        final ImmutableNode child1 = new ImmutableNode.Builder()
                .name(childName1)
                .create();
        final String childName2 = "mockChild2";
        final ImmutableNode child2 = new ImmutableNode.Builder()
                .name(childName2)
                .addChild(new ImmutableNode.Builder()
                        .name(grandchildName)
                        .create())
                .create();
        final ImmutableNode node = new ImmutableNode.Builder()
                .addChild(child1)
                .addChild(child2)
                .create();
        
        final Collection<ImmutableNode> result = mapper.getChildren(node);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(child1));
        assertTrue(result.contains(child2));
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#constructHierarchy(ImmutableNode.Builder, Preferences, Map)}.
     */
    @Test
    void testConstructHierarchy() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final ImmutableNode.Builder builder = new ImmutableNode.Builder();
        final Preferences prefs = mock(Preferences.class);
        @SuppressWarnings("unchecked")
        final Map<ImmutableNode, Preferences> elemRefs = mock(Map.class);
        final String nodeValue = "mockValue";
        
        willReturn(nodeValue).given(mapper).getValue(prefs);
        willDoNothing().given(mapper).constructChildren(any(), any(), any());
        willDoNothing().given(mapper).constructAttributes(any(), any(), any());
        
        mapper.constructHierarchy(builder, prefs, elemRefs);
        final ImmutableNode result = builder.create();
        assertEquals(nodeValue, result.getValue());
        
        then(mapper).should(times(1)).getValue(prefs);
        then(mapper).should(times(1)).constructChildren(builder, prefs, elemRefs);
        then(mapper).should(times(1)).constructAttributes(builder, prefs, elemRefs);
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#constructHierarchy(ImmutableNode.Builder, Preferences, Map)}.
     */
    @Test
    void testConstructHierarchy_NullValue() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final ImmutableNode.Builder builder = new ImmutableNode.Builder();
        final Preferences prefs = mock(Preferences.class);
        @SuppressWarnings("unchecked")
        final Map<ImmutableNode, Preferences> elemRefs = mock(Map.class);
        
        willReturn(null).given(mapper).getValue(prefs);
        willDoNothing().given(mapper).constructChildren(any(), any(), any());
        willDoNothing().given(mapper).constructAttributes(any(), any(), any());
        
        mapper.constructHierarchy(builder, prefs, elemRefs);
        final ImmutableNode result = builder.create();
        assertNull(result.getValue());
        
        then(mapper).should(times(1)).getValue(prefs);
        then(mapper).should(times(1)).constructChildren(builder, prefs, elemRefs);
        then(mapper).should(times(1)).constructAttributes(builder, prefs, elemRefs);
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#constructHierarchy(ImmutableNode.Builder, Preferences, Map)}.
     */
    @Test
    void testConstructHierarchy_GetValue_Error() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final ImmutableNode.Builder builder = new ImmutableNode.Builder();
        final Preferences prefs = mock(Preferences.class);
        @SuppressWarnings("unchecked")
        final Map<ImmutableNode, Preferences> elemRefs = mock(Map.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willThrow(mockEx).given(mapper).getValue(prefs);
        willDoNothing().given(mapper).constructChildren(any(), any(), any());
        willDoNothing().given(mapper).constructAttributes(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.constructHierarchy(builder, prefs, elemRefs);
        });
        assertSame(mockEx, result);
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#constructHierarchy(ImmutableNode.Builder, Preferences, Map)}.
     */
    @Test
    void testConstructHierarchy_ConstructChildren_Error() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final ImmutableNode.Builder builder = new ImmutableNode.Builder();
        final Preferences prefs = mock(Preferences.class);
        @SuppressWarnings("unchecked")
        final Map<ImmutableNode, Preferences> elemRefs = mock(Map.class);
        final String nodeValue = "mockValue";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(nodeValue).given(mapper).getValue(prefs);
        willThrow(mockEx).given(mapper).constructChildren(any(), any(), any());
        willDoNothing().given(mapper).constructAttributes(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.constructHierarchy(builder, prefs, elemRefs);
        });
        assertSame(mockEx, result);
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#constructHierarchy(ImmutableNode.Builder, Preferences, Map)}.
     */
    @Test
    void testConstructHierarchy_ConstructAttributes_Error() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final ImmutableNode.Builder builder = new ImmutableNode.Builder();
        final Preferences prefs = mock(Preferences.class);
        @SuppressWarnings("unchecked")
        final Map<ImmutableNode, Preferences> elemRefs = mock(Map.class);
        final String nodeValue = "mockValue";
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(nodeValue).given(mapper).getValue(prefs);
        willDoNothing().given(mapper).constructChildren(any(), any(), any());
        willThrow(mockEx).given(mapper).constructAttributes(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.constructHierarchy(builder, prefs, elemRefs);
        });
        assertSame(mockEx, result);
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#constructChildren(ImmutableNode.Builder, Preferences, Map)}.
     */
    @Test
    void testConstructChildren() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final ImmutableNode.Builder builder = new ImmutableNode.Builder();
        final Preferences prefs = mock(Preferences.class);
        final Preferences child1 = mock(Preferences.class);
        final String child1Name = "mockName1";
        final Preferences child2 = mock(Preferences.class);
        final String child2Name = "mockName2";
        final Collection<Preferences> children = Arrays.asList(child1, child2);
        @SuppressWarnings("unchecked")
        final Map<ImmutableNode, Preferences> elemRefs = mock(Map.class);
        
        willReturn(children).given(mapper).getChildren(prefs);
        willReturn(child1Name).given(mapper).getName(child1);
        willReturn(child2Name).given(mapper).getName(child2);
        willDoNothing().given(mapper).constructHierarchy(any(), any(), any());
        
        mapper.constructChildren(builder, prefs, elemRefs);
        final ImmutableNode result = builder.create();
        assertEquals(2, result.getChildren().size());
        assertNotNull(result.getChildren(child1Name));
        assertNotNull(result.getChildren(child2Name));
        
        then(mapper).should(times(1)).getChildren(prefs);
        then(mapper).should(times(1)).constructHierarchy(not(same(builder)), same(child1), same(elemRefs));
        then(mapper).should(times(1)).constructHierarchy(not(same(builder)), same(child2), same(elemRefs));
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#constructChildren(ImmutableNode.Builder, Preferences, Map)}.
     */
    @Test
    void testConstructChildren_Empty() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final ImmutableNode.Builder builder = new ImmutableNode.Builder();
        final Preferences prefs = mock(Preferences.class);
        final Collection<Preferences> children = Collections.emptyList();
        @SuppressWarnings("unchecked")
        final Map<ImmutableNode, Preferences> elemRefs = mock(Map.class);
        
        willReturn(children).given(mapper).getChildren(prefs);
        willDoNothing().given(mapper).constructHierarchy(any(), any(), any());
        
        mapper.constructChildren(builder, prefs, elemRefs);
        final ImmutableNode result = builder.create();
        assertTrue(result.getChildren().isEmpty());
        
        then(mapper).should(times(1)).getChildren(prefs);
        then(mapper).should(never()).constructHierarchy(any(), any(), any());
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#constructChildren(ImmutableNode.Builder, Preferences, Map)}.
     */
    @Test
    void testConstructChildren_GetChildren_Error() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final ImmutableNode.Builder builder = new ImmutableNode.Builder();
        final Preferences prefs = mock(Preferences.class);
        @SuppressWarnings("unchecked")
        final Map<ImmutableNode, Preferences> elemRefs = mock(Map.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willThrow(mockEx).given(mapper).getChildren(prefs);
        willDoNothing().given(mapper).constructHierarchy(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.constructChildren(builder, prefs, elemRefs);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getChildren(prefs);
        then(mapper).should(never()).constructHierarchy(any(), any(), any());
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#constructChildren(ImmutableNode.Builder, Preferences, Map)}.
     */
    @Test
    void testConstructChildren_GetName_Error() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final ImmutableNode.Builder builder = new ImmutableNode.Builder();
        final Preferences prefs = mock(Preferences.class);
        final Preferences child1 = mock(Preferences.class);
        final Preferences child2 = mock(Preferences.class);
        final Collection<Preferences> children = Arrays.asList(child1, child2);
        @SuppressWarnings("unchecked")
        final Map<ImmutableNode, Preferences> elemRefs = mock(Map.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(children).given(mapper).getChildren(prefs);
        willThrow(mockEx).given(mapper).getName(child1);
        willDoNothing().given(mapper).constructHierarchy(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.constructChildren(builder, prefs, elemRefs);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getChildren(prefs);
        then(mapper).should(never()).constructHierarchy(any(), any(), any());
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#constructChildren(ImmutableNode.Builder, Preferences, Map)}.
     */
    @Test
    void testConstructChildren_ConstructHierarchy_Error() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
        final ImmutableNode.Builder builder = new ImmutableNode.Builder();
        final Preferences prefs = mock(Preferences.class);
        final Preferences child1 = mock(Preferences.class);
        final String child1Name = "mockName1";
        final Preferences child2 = mock(Preferences.class);
        final String child2Name = "mockName2";
        final Collection<Preferences> children = Arrays.asList(child1, child2);
        @SuppressWarnings("unchecked")
        final Map<ImmutableNode, Preferences> elemRefs = mock(Map.class);
        final ConfigurationRuntimeException mockEx = new ConfigurationRuntimeException("mock exception");
        
        willReturn(children).given(mapper).getChildren(prefs);
        willReturn(child1Name).given(mapper).getName(child1);
        willReturn(child2Name).given(mapper).getName(child2);
        willThrow(mockEx).given(mapper).constructHierarchy(any(), any(), any());
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            mapper.constructChildren(builder, prefs, elemRefs);
        });
        assertSame(mockEx, result);
        
        then(mapper).should(times(1)).getChildren(prefs);
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#constructAttributes(ImmutableNode.Builder, Preferences, Map)}.
     */
    @Test
    void testConstructAttributes() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
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
        
        willReturn(attrs).given(mapper).getAttributes(prefs);
        
        mapper.constructAttributes(builder, prefs, elemRefs);
        final ImmutableNode result = builder.create();
        assertEquals(attrs, result.getAttributes());
        
        then(mapper).should(times(1)).getAttributes(prefs);
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#constructAttributes(ImmutableNode.Builder, Preferences, Map)}.
     */
    @Test
    void testConstructAttributes_Empty() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
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
    }

    /**
     * Test for {@link AbstractImmutableNodePreferencesMapper#constructAttributes(ImmutableNode.Builder, Preferences, Map)}.
     */
    @Test
    void testConstructAttributes_GetAttributes_Error() {
        final AbstractImmutableNodePreferencesMapper mapper = spy(AbstractImmutableNodePreferencesMapper.class);
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
    }
}
