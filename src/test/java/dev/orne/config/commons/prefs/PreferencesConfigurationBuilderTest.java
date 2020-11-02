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

import org.apache.commons.configuration2.Initializable;
import org.apache.commons.configuration2.builder.BuilderParameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.ExpressionEngine;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.config.TestPreferencesFactory;

/**
 * Unit tests for {@code PreferencesConfigurationBuilder}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see PreferencesConfigurationBuilder
 */
@Tag("ut")
class PreferencesConfigurationBuilderTest {

    private @Mock PreferencesMapper<ImmutableNode> mapper;
    private @Mock PreferencesHandler<ImmutableNode> handler;
    private @Mock ExpressionEngine expressionEngine;
    private @Mock Preferences baseNode;
    private @Mock EventCoordinationStrategy eventCoordStr;
    private @Mock InitializableConfig mockConfig;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#PreferencesConfigurationBuilder(Class)}.
     */
    @Test
    void testConstructor() {
        final TestBuilder builder = new TestBuilder(PreferencesConfiguration.class);
        assertSame(PreferencesConfiguration.class, builder.getResultClass());
        assertNotNull(builder.getInnerParameters());
        assertTrue(builder.getInnerParameters().isEmpty());
        assertFalse(builder.isAllowFailOnInit());
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#PreferencesConfigurationBuilder(Class)}.
     */
    @Test
    void testConstructor_NullClass() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TestBuilder(null);
        });
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#PreferencesConfigurationBuilder(Class)}.
     */
    @Test
    void testConstructor_Params() {
        final Map<String, Object> params = new HashMap<>();
        params.put("mock param", "mock param value");
        final TestBuilder builder = new TestBuilder(
                PreferencesConfiguration.class,
                params);
        assertSame(PreferencesConfiguration.class, builder.getResultClass());
        assertEquals(params, builder.getInnerParameters());
        assertFalse(builder.isAllowFailOnInit());
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#PreferencesConfigurationBuilder(Class)}.
     */
    @Test
    void testConstructor_Params_NullClass() {
        final Map<String, Object> params = new HashMap<>();
        params.put("mock param", "mock param value");
        assertThrows(IllegalArgumentException.class, () -> {
            new TestBuilder(null, params);
        });
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#PreferencesConfigurationBuilder(Class)}.
     */
    @Test
    void testConstructor_Params_NullParams() {
        final TestBuilder builder = new TestBuilder(PreferencesConfiguration.class, null);
        assertSame(PreferencesConfiguration.class, builder.getResultClass());
        assertNotNull(builder.getInnerParameters());
        assertTrue(builder.getInnerParameters().isEmpty());
        assertFalse(builder.isAllowFailOnInit());
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#PreferencesConfigurationBuilder(Class, Map, boolean)}.
     */
    @Test
    void testConstructor_Params_Fail() {
        final Map<String, Object> params = new HashMap<>();
        params.put("mock param", "mock param value");
        final TestBuilder builder = new TestBuilder(
                PreferencesConfiguration.class,
                params,
                true);
        assertSame(PreferencesConfiguration.class, builder.getResultClass());
        assertEquals(params, builder.getInnerParameters());
        assertTrue(builder.isAllowFailOnInit());
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#PreferencesConfigurationBuilder(Class, Map, boolean)}.
     */
    @Test
    void testConstructor_Params_Fail_NullClass() {
        final Map<String, Object> params = new HashMap<>();
        params.put("mock param", "mock param value");
        assertThrows(IllegalArgumentException.class, () -> {
            new TestBuilder(null, params, true);
        });
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#PreferencesConfigurationBuilder(Class, Map, boolean)}.
     */
    @Test
    void testConstructor_Params_Fail_NullParams() {
        final TestBuilder builder = new TestBuilder(
                PreferencesConfiguration.class,
                null,
                true);
        assertSame(PreferencesConfiguration.class, builder.getResultClass());
        assertNotNull(builder.getInnerParameters());
        assertTrue(builder.getInnerParameters().isEmpty());
        assertTrue(builder.isAllowFailOnInit());
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#configure(BuilderParameters...)}.
     */
    @Test
    void testConfigure() {
        final Map<String, Object> mockParamsMap = new HashMap<>();
        mockParamsMap.put("mock param", "mock param value");
        final BuilderParameters params = mock(BuilderParameters.class);
        willReturn(mockParamsMap).given(params).getParameters();
        
        final TestBuilder builder = new TestBuilder(PreferencesConfiguration.class);
        final PreferencesConfigurationBuilder result = builder.configure(params);
        assertEquals(mockParamsMap, builder.getInnerParameters());
        assertSame(builder, result);
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#createDefaultPreferencesMapper()}.
     */
    @Test
    void testCreateDefaultPreferencesMapper() {
        final PreferencesConfigurationBuilder builder = new PreferencesConfigurationBuilder(
                PreferencesConfiguration.class);
        final PreferencesMapper<ImmutableNode> result = builder.createDefaultPreferencesMapper();
        assertNotNull(result);
        assertTrue(result instanceof NodeBasedPreferencesMapper);
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#fetchMapperFromParameters()}.
     */
    @Test
    void testFetchMapperFromParameters() {
        final PreferencesConfigurationBuilder builder = spy(new PreferencesConfigurationBuilder(
                PreferencesConfiguration.class));
        willReturn(mapper).given(builder).createDefaultPreferencesMapper();
        
        final PreferencesMapper<ImmutableNode> result = builder.fetchMapperFromParameters();
        assertSame(mapper, result);
        
        then(builder).should().createDefaultPreferencesMapper();
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#fetchMapperFromParameters()}.
     */
    @Test
    void testFetchMapperFromParameters_Configured() {
        final Map<String, Object> params = new HashMap<>();
        params.put(PreferencesConfigurationBuilder.PROP_MAPPER, mapper);
        
        final PreferencesConfigurationBuilder builder = spy(new PreferencesConfigurationBuilder(
                PreferencesConfiguration.class,
                params));
        final PreferencesMapper<ImmutableNode> result = builder.fetchMapperFromParameters();
        assertSame(mapper, result);
        
        then(builder).should(never()).createDefaultPreferencesMapper();
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#fetchMapperFromParameters()}.
     */
    @Test
    void testFetchMapperFromParameters_WrongType() {
        final Map<String, Object> params = new HashMap<>();
        params.put(PreferencesConfigurationBuilder.PROP_MAPPER, new Object());
        
        final PreferencesConfigurationBuilder builder = spy(new PreferencesConfigurationBuilder(
                PreferencesConfiguration.class,
                params));
        assertThrows(ClassCastException.class, () -> {
            builder.fetchMapperFromParameters();
        });
        then(builder).should(never()).createDefaultPreferencesMapper();
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#getPreferencesHandler()}.
     */
    @Test
    void testGetPreferencesHandler() {
        final PreferencesConfigurationBuilder builder = spy(new PreferencesConfigurationBuilder(
                PreferencesConfiguration.class));
        willReturn(mapper).given(builder).fetchMapperFromParameters();
        
        final PreferencesHandler<ImmutableNode> result = builder.getPreferencesHandler();
        assertNotNull(result);
        assertSame(mapper, result.getMapper());
        assertTrue(result.getEventCoordinationStrategy() instanceof ByThreadEventCoordinationStrategy);
        assertSame(TestPreferencesFactory.getUserRoot(), result.getBaseNode());
        assertNull(result.getContent());
        assertFalse(result.isAutoLoad());
        assertFalse(result.isAutoSave());
        assertNotNull(result.getEventListenerManager());
        
        then(builder).should().fetchMapperFromParameters();
        assertSame(result, builder.getPreferencesHandler());
        then(builder).should().fetchMapperFromParameters();
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#getPreferencesHandler()}.
     */
    @Test
    void testGetPreferencesHandler_FromMap() {
        final Map<String, Object> params = new HashMap<>();
        params.put(PreferencesConfiguration.PROP_EXPRESSION_ENGINE, expressionEngine);
        params.put(PreferencesConfigurationBuilder.PROP_MAPPER, mapper);
        params.put(PreferencesHandler.PROP_BASE_NODE, baseNode);
        params.put(PreferencesHandler.PROP_AUTO_LOAD, true);
        params.put(PreferencesHandler.PROP_AUTO_SAVE, true);
        params.put(PreferencesHandler.PROP_EVENT_COORDINATOR, eventCoordStr);
        final PreferencesConfigurationBuilder builder = new PreferencesConfigurationBuilder(
                PreferencesConfiguration.class,
                params);
        final PreferencesHandler<ImmutableNode> result = builder.getPreferencesHandler();
        assertNotNull(result);
        assertSame(mapper, result.getMapper());
        assertSame(eventCoordStr, result.getEventCoordinationStrategy());
        assertSame(baseNode, result.getBaseNode());
        assertNull(result.getContent());
        assertTrue(result.isAutoLoad());
        assertTrue(result.isAutoSave());
        assertNotNull(result.getEventListenerManager());
        assertSame(result, builder.getPreferencesHandler());
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#resetParameters()}.
     */
    @Test
    void testResetParameters() {
        final Map<String, Object> params = new HashMap<>();
        params.put("mock param", "mock param value");
        final TestBuilder builder = new TestBuilder(
                PreferencesConfiguration.class, params);
        
        final PreferencesHandler<ImmutableNode> originalHandler = builder.getPreferencesHandler();
        builder.resetParameters();
        assertNotNull(builder.getInnerParameters());
        assertTrue(builder.getInnerParameters().isEmpty());
        assertNotSame(originalHandler, builder.getPreferencesHandler());
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#initResultInstance(PreferencesConfiguration)}.
     */
    @Test
    void testInitResultInstance()
    throws ConfigurationException {
        final PreferencesConfigurationBuilder builder = spy(new PreferencesConfigurationBuilder(
                PreferencesConfiguration.class));
        willReturn(handler).given(builder).getPreferencesHandler();
        
        builder.initResultInstance(mockConfig);
        
        final InOrder order = inOrder(builder, handler, mockConfig);
        then(mockConfig).should(order).initialize();
        then(handler).should(order).setContent(mockConfig);
        then(handler).should(order).load();
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#resetResult()}.
     */
    @Test
    void testResetResult() {
        final PreferencesConfigurationBuilder builder = spy(new PreferencesConfigurationBuilder(
                PreferencesConfiguration.class));
        final PreferencesHandler<ImmutableNode> originalHandler = builder.getPreferencesHandler();
        originalHandler.setContent(mockConfig);
        builder.resetResult();
        assertNull(originalHandler.getContent());
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#refresh()}.
     */
    @Test
    void testRefresh() {
        final PreferencesConfigurationBuilder builder = spy(new PreferencesConfigurationBuilder(
                PreferencesConfiguration.class));
        willReturn(handler).given(builder).getPreferencesHandler();
        builder.refresh();
        then(handler).should().refresh();
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#save()}.
     */
    @Test
    void testSave() {
        final PreferencesConfigurationBuilder builder = spy(new PreferencesConfigurationBuilder(
                PreferencesConfiguration.class));
        willReturn(handler).given(builder).getPreferencesHandler();
        builder.save();
        then(handler).should().save();
    }

    /**
     * Test for {@link PreferencesConfigurationBuilder#params()}.
     */
    @Test
    void testParams() {
        final PreferencesBuilderParameters result = PreferencesConfigurationBuilder.params();
        assertTrue(result instanceof PreferencesBuilderParametersImpl);
    }

    static class TestBuilder
    extends PreferencesConfigurationBuilder {

        public TestBuilder(
                final Class<? extends PreferencesConfiguration> resCls,
                final Map<String, Object> params,
                final boolean allowFailOnInit) {
            super(resCls, params, allowFailOnInit);
        }

        public TestBuilder(
                final Class<? extends PreferencesConfiguration> resCls,
                final Map<String, Object> params) {
            super(resCls, params);
        }

        public TestBuilder(
                final Class<? extends PreferencesConfiguration> resCls) {
            super(resCls);
        }

        public Map<String, Object> getInnerParameters() {
            return getParameters();
        }
    }

    static class InitializableConfig
    extends PreferencesConfiguration
    implements Initializable {
        @Override
        public void initialize() {
            // NOP
        }
    }
}
