package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
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

import java.math.BigInteger;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code AbstractConfig} for instances that implement
 * {@code HierarchicalConfig}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class AbstractConfigHierarchicalTest {

    private static final String TEST_KEY = "test.key";
    private static final Class<String> TEST_CLASS = String.class;
    private static final String TEST_STRING_VALUE = "testValue";
    private static final Number TEST_NUMBER_VALUE = BigInteger.valueOf(System.currentTimeMillis());

    /**
     * Test method for {@link AbstractConfig#contains(String)} on
     * instances with no parent and parameter configured.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testContainsConfigured()
    throws ConfigException {
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(true);
        
        assertTrue(config.contains(TEST_KEY));
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(0)).getParent();
    }

    /**
     * Test method for {@link AbstractConfig#contains(String)} on
     * instances with no parent and parameter not configured.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testContainsUnconfiguredNoParent()
    throws ConfigException {
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        given(config.getParent()).willReturn(null);
        
        assertFalse(config.contains(TEST_KEY));
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(1)).getParent();
    }

    /**
     * Test method for {@link AbstractConfig#contains(String)} on
     * instances with parent and parameter configured on parent.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testContainsUnconfiguredParent()
    throws ConfigException {
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        final Config parent = mock(Config.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        given(config.getParent()).willReturn(parent);
        given(parent.contains(TEST_KEY)).willReturn(true);
        
        assertTrue(config.contains(TEST_KEY));
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(1)).getParent();
        then(parent).should(times(1)).contains(TEST_KEY);
        then(parent).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link AbstractConfig#contains(String)} on
     * instances with parent and parameter not configured.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testContainsUnconfiguredParentUnconfigured()
    throws ConfigException {
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        final Config parent = mock(Config.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        given(config.getParent()).willReturn(parent);
        given(parent.contains(TEST_KEY)).willReturn(false);
        
        assertFalse(config.contains(TEST_KEY));
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(1)).getParent();
        then(parent).should(times(1)).contains(TEST_KEY);
        then(parent).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link AbstractConfig#get(String, Class)} on
     * instances with no parent and parameter configured.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetConfigured()
    throws ConfigException {
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(true);
        given(config.getParameter(TEST_KEY, TEST_CLASS)).willReturn(TEST_STRING_VALUE);
        clearInvocations(config); // The previous call counts as invocation for some reason
        
        final String result = config.get(TEST_KEY, TEST_CLASS);
        assertNotNull(result);
        assertEquals(TEST_STRING_VALUE, result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(1)).getParameter(TEST_KEY, TEST_CLASS);
        then(config).should(times(0)).getParent();
    }

    /**
     * Test method for {@link AbstractConfig#get(String, Class)} on
     * instances with no parent and parameter configured with null value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetConfiguredNull()
    throws ConfigException {
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(true);
        given(config.getParameter(TEST_KEY, TEST_CLASS)).willReturn(null);
        clearInvocations(config); // The previous call counts as invocation for some reason
        
        final String result = config.get(TEST_KEY, TEST_CLASS);
        assertNull(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(1)).getParameter(TEST_KEY, TEST_CLASS);
        then(config).should(times(0)).getParent();
    }

    /**
     * Test method for {@link AbstractConfig#get(String, Class)} on
     * instances with no parent and parameter not configured.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetUnconfiguredNoParent()
    throws ConfigException {
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        given(config.getParent()).willReturn(null);
        
        final String result = config.get(TEST_KEY, TEST_CLASS);
        assertNull(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(0)).getParameter(TEST_KEY, TEST_CLASS);
        then(config).should(times(1)).getParent();
    }

    /**
     * Test method for {@link AbstractConfig#get(String, Class)} on
     * instances with parent and parameter not configured.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetUnconfiguredParent()
    throws ConfigException {
        final Config parent = mock(Config.class);
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        given(config.getParent()).willReturn(parent);
        given(parent.get(TEST_KEY, TEST_CLASS)).willReturn(TEST_STRING_VALUE);
        
        
        final String result = config.get(TEST_KEY, TEST_CLASS);     assertNotNull(result);
        assertEquals(TEST_STRING_VALUE, result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(0)).getParameter(TEST_KEY, TEST_CLASS);
        then(config).should(times(1)).getParent();
        then(parent).should(times(1)).get(TEST_KEY, TEST_CLASS);
        then(parent).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link AbstractConfig#get(String, Class)} on
     * instances with parent and parameter not configured with null
     * value on parent.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetUnconfiguredParentNull()
    throws ConfigException {
        final Config parent = mock(Config.class);
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        given(config.getParent()).willReturn(parent);
        given(parent.get(TEST_KEY, TEST_CLASS)).willReturn(null);
        
        
        final String result = config.get(TEST_KEY, TEST_CLASS);
        assertNull(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(0)).getParameter(TEST_KEY, TEST_CLASS);
        then(config).should(times(1)).getParent();
        then(parent).should(times(1)).get(TEST_KEY, TEST_CLASS);
        then(parent).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link AbstractConfig#getBoolean(String)} on
     * instances with no parent and parameter configured.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBooleanConfigured()
    throws ConfigException {
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(true);
        given(config.getBooleanParameter(TEST_KEY)).willReturn(true);
        
        final Boolean result = config.getBoolean(TEST_KEY);
        assertNotNull(result);
        assertTrue(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(1)).getBooleanParameter(TEST_KEY);
        then(config).should(times(0)).getParent();
    }

    /**
     * Test method for {@link AbstractConfig#getBoolean(String)} on
     * instances with no parent and parameter configured with null value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBooleanConfiguredNull()
    throws ConfigException {
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(true);
        given(config.getBooleanParameter(TEST_KEY)).willReturn(null);
        
        final Boolean result = config.getBoolean(TEST_KEY);
        assertNull(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(1)).getBooleanParameter(TEST_KEY);
        then(config).should(times(0)).getParent();
    }

    /**
     * Test method for {@link AbstractConfig#getBoolean(String)} on
     * instances with no parent and parameter not configured.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBooleanUnconfiguredNoParent()
    throws ConfigException {
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        given(config.getParent()).willReturn(null);
        
        final Boolean result = config.getBoolean(TEST_KEY);
        assertNull(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(0)).getBooleanParameter(TEST_KEY);
        then(config).should(times(1)).getParent();
    }

    /**
     * Test method for {@link AbstractConfig#getBoolean(String)} on
     * instances with parent and parameter not configured.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBooleanUnconfiguredParent()
    throws ConfigException {
        final Config parent = mock(Config.class);
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        given(config.getParent()).willReturn(parent);
        given(parent.getBoolean(TEST_KEY)).willReturn(true);
        
        
        final Boolean result = config.getBoolean(TEST_KEY);
        assertNotNull(result);
        assertTrue(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(0)).getBooleanParameter(TEST_KEY);
        then(config).should(times(1)).getParent();
        then(parent).should(times(1)).getBoolean(TEST_KEY);
        then(parent).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link AbstractConfig#getBoolean(String)} on
     * instances with parent and parameter not configured with null
     * value on parent.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBooleanUnconfiguredParentNull()
    throws ConfigException {
        final Config parent = mock(Config.class);
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        given(config.getParent()).willReturn(parent);
        given(parent.getBoolean(TEST_KEY)).willReturn(null);
        
        
        final Boolean result = config.getBoolean(TEST_KEY);
        assertNull(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(0)).getBooleanParameter(TEST_KEY);
        then(config).should(times(1)).getParent();
        then(parent).should(times(1)).getBoolean(TEST_KEY);
        then(parent).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link AbstractConfig#getString(String)} on
     * instances with no parent and parameter configured.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetStringConfigured()
    throws ConfigException {
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(true);
        given(config.getStringParameter(TEST_KEY)).willReturn(TEST_STRING_VALUE);
        
        final String result = config.getString(TEST_KEY);
        assertNotNull(result);
        assertEquals(TEST_STRING_VALUE, result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(1)).getStringParameter(TEST_KEY);
        then(config).should(times(0)).getParent();
    }

    /**
     * Test method for {@link AbstractConfig#getString(String)} on
     * instances with no parent and parameter configured with null value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetStringConfiguredNull()
    throws ConfigException {
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(true);
        given(config.getStringParameter(TEST_KEY)).willReturn(null);
        
        final String result = config.getString(TEST_KEY);
        assertNull(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(1)).getStringParameter(TEST_KEY);
        then(config).should(times(0)).getParent();
    }

    /**
     * Test method for {@link AbstractConfig#getString(String)} on
     * instances with no parent and parameter not configured.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetStringUnconfiguredNoParent()
    throws ConfigException {
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        given(config.getParent()).willReturn(null);
        
        final String result = config.getString(TEST_KEY);
        assertNull(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(0)).getStringParameter(TEST_KEY);
        then(config).should(times(1)).getParent();
    }

    /**
     * Test method for {@link AbstractConfig#getString(String)} on
     * instances with parent and parameter not configured.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetStringUnconfiguredParent()
    throws ConfigException {
        final Config parent = mock(Config.class);
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        given(config.getParent()).willReturn(parent);
        given(parent.getString(TEST_KEY)).willReturn(TEST_STRING_VALUE);
        
        
        final String result = config.getString(TEST_KEY);
        assertNotNull(result);
        assertEquals(TEST_STRING_VALUE, result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(0)).getStringParameter(TEST_KEY);
        then(config).should(times(1)).getParent();
        then(parent).should(times(1)).getString(TEST_KEY);
        then(parent).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link AbstractConfig#getString(String)} on
     * instances with parent and parameter not configured with null
     * value on parent.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetStringUnconfiguredParentNull()
    throws ConfigException {
        final Config parent = mock(Config.class);
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        given(config.getParent()).willReturn(parent);
        given(parent.getString(TEST_KEY)).willReturn(null);
        
        
        final String result = config.getString(TEST_KEY);
        assertNull(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(0)).getStringParameter(TEST_KEY);
        then(config).should(times(1)).getParent();
        then(parent).should(times(1)).getString(TEST_KEY);
        then(parent).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link AbstractConfig#getNumber(String)} on
     * instances with no parent and parameter configured.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetNumberConfigured()
    throws ConfigException {
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(true);
        given(config.getNumberParameter(TEST_KEY)).willReturn(TEST_NUMBER_VALUE);
        
        final Number result = config.getNumber(TEST_KEY);
        assertNotNull(result);
        assertEquals(TEST_NUMBER_VALUE, result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(1)).getNumberParameter(TEST_KEY);
        then(config).should(times(0)).getParent();
    }

    /**
     * Test method for {@link AbstractConfig#getNumber(String)} on
     * instances with no parent and parameter configured with null value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetNumberConfiguredNull()
    throws ConfigException {
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(true);
        given(config.getNumberParameter(TEST_KEY)).willReturn(null);
        
        final Number result = config.getNumber(TEST_KEY);
        assertNull(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(1)).getNumberParameter(TEST_KEY);
        then(config).should(times(0)).getParent();
    }

    /**
     * Test method for {@link AbstractConfig#getNumber(String)} on
     * instances with no parent and parameter not configured.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetNumberUnconfiguredNoParent()
    throws ConfigException {
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        given(config.getParent()).willReturn(null);
        
        final Number result = config.getNumber(TEST_KEY);
        assertNull(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(0)).getStringParameter(TEST_KEY);
        then(config).should(times(1)).getParent();
    }

    /**
     * Test method for {@link AbstractConfig#getNumber(String)} on
     * instances with parent and parameter not configured.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetNumberUnconfiguredParent()
    throws ConfigException {
        final Config parent = mock(Config.class);
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        given(config.getParent()).willReturn(parent);
        given(parent.getNumber(TEST_KEY)).willReturn(TEST_NUMBER_VALUE);
        
        
        final Number result = config.getNumber(TEST_KEY);
        assertNotNull(result);
        assertEquals(TEST_NUMBER_VALUE, result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(0)).getNumberParameter(TEST_KEY);
        then(config).should(times(1)).getParent();
        then(parent).should(times(1)).getNumber(TEST_KEY);
        then(parent).shouldHaveNoMoreInteractions();
    }

    /**
     * Test method for {@link AbstractConfig#getNumber(String)} on
     * instances with parent and parameter not configured with null
     * value on parent.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetNumberUnconfiguredParentNull()
    throws ConfigException {
        final Config parent = mock(Config.class);
        final AbstractHierarchicalConfig config = spy(AbstractHierarchicalConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        given(config.getParent()).willReturn(parent);
        given(parent.getNumber(TEST_KEY)).willReturn(null);
        
        
        final Number result = config.getNumber(TEST_KEY);
        assertNull(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(0)).getNumberParameter(TEST_KEY);
        then(config).should(times(1)).getParent();
        then(parent).should(times(1)).getNumber(TEST_KEY);
        then(parent).shouldHaveNoMoreInteractions();
    }

    /**
     * Test class.
     */
    public static abstract class AbstractHierarchicalConfig
    extends AbstractConfig
    implements HierarchicalConfig {
        // No extra methods
    }
}
