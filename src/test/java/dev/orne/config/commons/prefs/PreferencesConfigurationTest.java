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

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.tree.ExpressionEngine;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.commons.configuration2.tree.InMemoryNodeModel;
import org.apache.commons.configuration2.tree.NodeHandler;
import org.apache.commons.configuration2.tree.NodeModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@code PreferencesConfiguration}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see PreferencesConfiguration
 */
@Tag("ut")
class PreferencesConfigurationTest {

    private @Mock HierarchicalConfiguration<ImmutableNode> copy;
    private @Mock NodeModel<ImmutableNode> model;
    private @Mock InMemoryNodeModel inMemoryModel;
    private @Mock NodeHandler<ImmutableNode> handler;
    private @Mock ExpressionEngine expressionEngine;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test for {@link PreferencesConfiguration#PreferencesConfiguration()}.
     */
    @Test
    void testConstructor() {
        final PreferencesConfiguration config = new PreferencesConfiguration();
        assertNotNull(config.getExpressionEngine());
        assertNotNull(config.getListDelimiterHandler());
        assertNotNull(config.getConversionHandler());
        assertNotNull(config.getInterpolator());
        assertNotNull(config.getSynchronizer());
        assertNotNull(config.getLogger());
        assertNull(config.getConfigurationDecoder());
        assertNotNull(config.getNodeModel());
        assertNotNull(config.getNodeModel().getRootNode());
        final ImmutableNode rootNode = config.getNodeModel().getRootNode();
        assertNull(rootNode.getNodeName());
        assertNull(rootNode.getValue());
        assertTrue(rootNode.getAttributes().isEmpty());
        assertTrue(rootNode.getChildren().isEmpty());
    }

    /**
     * Test for {@link PreferencesConfiguration#PreferencesConfiguration(HierarchicalConfiguration)}.
     */
    @Test
    void testConstructor_Copy() {
        final ImmutableNode rootNode = new ImmutableNode.Builder()
                .name("mock root node")
                .create();
        willReturn(model).given(copy).getNodeModel();
        willReturn(handler).given(model).getNodeHandler();
        willReturn(rootNode).given(handler).getRootNode();

        final PreferencesConfiguration config = new PreferencesConfiguration(copy);
        assertNotNull(config.getExpressionEngine());
        assertNotNull(config.getListDelimiterHandler());
        assertNotNull(config.getConversionHandler());
        assertNotNull(config.getInterpolator());
        assertNotNull(config.getSynchronizer());
        assertNotNull(config.getLogger());
        assertNull(config.getConfigurationDecoder());
        assertNotNull(config.getNodeModel());
        assertSame(rootNode, config.getNodeModel().getRootNode());

        then(copy).should().getNodeModel();
        then(model).should().getNodeHandler();
        then(handler).should().getRootNode();
        then(copy).shouldHaveNoMoreInteractions();
        then(model).shouldHaveNoMoreInteractions();
        then(handler).shouldHaveNoMoreInteractions();
    }

    /**
     * Test for {@link PreferencesConfiguration#PreferencesConfiguration(NodeModel)}.
     */
    @Test
    void testConstructor_Model() {
        final PreferencesConfiguration config = new PreferencesConfiguration(inMemoryModel);
        assertNotNull(config.getExpressionEngine());
        assertNotNull(config.getListDelimiterHandler());
        assertNotNull(config.getConversionHandler());
        assertNotNull(config.getInterpolator());
        assertNotNull(config.getSynchronizer());
        assertNotNull(config.getLogger());
        assertNull(config.getConfigurationDecoder());
        assertSame(inMemoryModel, config.getNodeModel());

        then(inMemoryModel).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link PreferencesConfiguration#load(ImmutableNode)}.
     */
    @Test
    void testLoad() {
        final PreferencesConfiguration config = new PreferencesConfiguration(inMemoryModel);

        final ImmutableNode node = new ImmutableNode.Builder()
                .name("mock loaded node name")
                .value("mock loaded node value")
                .addAttribute("attr1", "mock loaded node attr value")
                .addChild(new ImmutableNode.Builder()
                        .name("mock loaded node child name")
                        .value("mock loaded node child value")
                        .create())
                .create();
        willDoNothing().given(inMemoryModel).mergeRoot(any(), any(), any(), any(), any());
        config.load(node);

        final ArgumentCaptor<ImmutableNode> mergedNodeCaptor =
                ArgumentCaptor.forClass(ImmutableNode.class);
        then(inMemoryModel).should().mergeRoot(
                mergedNodeCaptor.capture(),
                eq(null),
                eq(null),
                eq(null),
                same(config));
        then(inMemoryModel).shouldHaveNoMoreInteractions();

        final ImmutableNode mergedNode = mergedNodeCaptor.getValue();
        assertNull(mergedNode.getNodeName());
        assertEquals(node.getValue(), mergedNode.getValue());
        assertEquals(node.getAttributes(), mergedNode.getAttributes());
        assertEquals(node.getChildren(), mergedNode.getChildren());
    }
}
