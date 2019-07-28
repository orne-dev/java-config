/**
 * 
 */
package dev.orne.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigInteger;
import java.time.Instant;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

/**
 * Unit tests for {@code AbstractConfig} for instances that implement
 * {@code HierarchicalConfig}.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 */
@Tag("ut")
class AbstractConfigHierarchicalTest {

	private static final String TEST_KEY = "test.key";
	private static final Class<String> TEST_CLASS = String.class;
	private static final String TEST_STRING_VALUE = "testValue";
	private static final Number TEST_NUMBER_VALUE = BigInteger.valueOf(System.currentTimeMillis());
	private static final Instant TEST_INSTANT_VALUE = Instant.now();

	/**
	 * Test method for {@link AbstractConfig#contains(String)} on
	 * instances with no parent and parameter configured.
	 */
	@Test
	public void testContainsConfigured() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
		given(config.containsParameter(TEST_KEY)).willReturn(true);
		
		assertTrue(config.contains(TEST_KEY));
		
		then(config).should(times(1)).containsParameter(TEST_KEY);
		then(config).should(times(0)).getParent();
	}

	/**
	 * Test method for {@link AbstractConfig#contains(String)} on
	 * instances with no parent and parameter not configured.
	 */
	@Test
	public void testContainsUnconfiguredNoParent() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
		given(config.containsParameter(TEST_KEY)).willReturn(false);
		given(config.getParent()).willReturn(null);
		
		assertFalse(config.contains(TEST_KEY));
		
		then(config).should(times(1)).containsParameter(TEST_KEY);
		then(config).should(times(1)).getParent();
	}

	/**
	 * Test method for {@link AbstractConfig#contains(String)} on
	 * instances with parent and parameter configured on parent.
	 */
	@Test
	public void testContainsUnconfiguredParent() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
		final Config parent = BDDMockito.mock(Config.class);
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
	 */
	@Test
	public void testContainsUnconfiguredParentUnconfigured() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
		final Config parent = BDDMockito.mock(Config.class);
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
	 */
	@Test
	public void testGetConfigured() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetConfiguredNull() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetUnconfiguredNoParent() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetUnconfiguredParent() {
		final Config parent = BDDMockito.mock(Config.class);
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
		given(config.containsParameter(TEST_KEY)).willReturn(false);
		given(config.getParent()).willReturn(parent);
		given(parent.get(TEST_KEY, TEST_CLASS)).willReturn(TEST_STRING_VALUE);
		
		
		final String result = config.get(TEST_KEY, TEST_CLASS);		assertNotNull(result);
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
	 */
	@Test
	public void testGetUnconfiguredParentNull() {
		final Config parent = BDDMockito.mock(Config.class);
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetBooleanConfigured() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetBooleanConfiguredNull() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetBooleanUnconfiguredNoParent() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetBooleanUnconfiguredParent() {
		final Config parent = BDDMockito.mock(Config.class);
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetBooleanUnconfiguredParentNull() {
		final Config parent = BDDMockito.mock(Config.class);
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetStringConfigured() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetStringConfiguredNull() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetStringUnconfiguredNoParent() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetStringUnconfiguredParent() {
		final Config parent = BDDMockito.mock(Config.class);
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetStringUnconfiguredParentNull() {
		final Config parent = BDDMockito.mock(Config.class);
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetNumberConfigured() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetNumberConfiguredNull() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetNumberUnconfiguredNoParent() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetNumberUnconfiguredParent() {
		final Config parent = BDDMockito.mock(Config.class);
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 */
	@Test
	public void testGetNumberUnconfiguredParentNull() {
		final Config parent = BDDMockito.mock(Config.class);
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
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
	 * Test method for {@link AbstractConfig#getInstant(String)} on
	 * instances with no parent and parameter configured.
	 */
	@Test
	public void testGetInstantConfigured() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
		given(config.containsParameter(TEST_KEY)).willReturn(true);
		given(config.getInstantParameter(TEST_KEY)).willReturn(TEST_INSTANT_VALUE);
		
		final Instant result = config.getInstant(TEST_KEY);
		assertNotNull(result);
		assertEquals(TEST_INSTANT_VALUE, result);
		
		then(config).should(times(1)).containsParameter(TEST_KEY);
		then(config).should(times(1)).getInstantParameter(TEST_KEY);
		then(config).should(times(0)).getParent();
	}

	/**
	 * Test method for {@link AbstractConfig#getInstant(String)} on
	 * instances with no parent and parameter configured with null value.
	 */
	@Test
	public void testGetInstantConfiguredNull() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
		given(config.containsParameter(TEST_KEY)).willReturn(true);
		given(config.getInstantParameter(TEST_KEY)).willReturn(null);
		
		final Instant result = config.getInstant(TEST_KEY);
		assertNull(result);
		
		then(config).should(times(1)).containsParameter(TEST_KEY);
		then(config).should(times(1)).getInstantParameter(TEST_KEY);
		then(config).should(times(0)).getParent();
	}

	/**
	 * Test method for {@link AbstractConfig#getInstant(String)} on
	 * instances with no parent and parameter not configured.
	 */
	@Test
	public void testGetInstantUnconfiguredNoParent() {
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
		given(config.containsParameter(TEST_KEY)).willReturn(false);
		given(config.getParent()).willReturn(null);
		
		final Instant result = config.getInstant(TEST_KEY);
		assertNull(result);
		
		then(config).should(times(1)).containsParameter(TEST_KEY);
		then(config).should(times(0)).getInstantParameter(TEST_KEY);
		then(config).should(times(1)).getParent();
	}

	/**
	 * Test method for {@link AbstractConfig#getInstant(String)} on
	 * instances with parent and parameter not configured.
	 */
	@Test
	public void testGetInstantUnconfiguredParent() {
		final Config parent = BDDMockito.mock(Config.class);
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
		given(config.containsParameter(TEST_KEY)).willReturn(false);
		given(config.getParent()).willReturn(parent);
		given(parent.getInstant(TEST_KEY)).willReturn(TEST_INSTANT_VALUE);
		
		
		final Instant result = config.getInstant(TEST_KEY);
		assertNotNull(result);
		assertEquals(TEST_INSTANT_VALUE, result);
		
		then(config).should(times(1)).containsParameter(TEST_KEY);
		then(config).should(times(0)).getInstantParameter(TEST_KEY);
		then(config).should(times(1)).getParent();
		then(parent).should(times(1)).getInstant(TEST_KEY);
		then(parent).shouldHaveNoMoreInteractions();
	}

	/**
	 * Test method for {@link AbstractConfig#getInstant(String)} on
	 * instances with parent and parameter not configured with null
	 * value on parent.
	 */
	@Test
	public void testGetInstantUnconfiguredParentNull() {
		final Config parent = BDDMockito.mock(Config.class);
		final AbstractHierarchicalConfig config = BDDMockito.spy(AbstractHierarchicalConfig.class);
		given(config.containsParameter(TEST_KEY)).willReturn(false);
		given(config.getParent()).willReturn(parent);
		given(parent.getInstant(TEST_KEY)).willReturn(null);
		
		
		final Instant result = config.getInstant(TEST_KEY);
		assertNull(result);
		
		then(config).should(times(1)).containsParameter(TEST_KEY);
		then(config).should(times(0)).getInstantParameter(TEST_KEY);
		then(config).should(times(1)).getParent();
		then(parent).should(times(1)).getInstant(TEST_KEY);
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
