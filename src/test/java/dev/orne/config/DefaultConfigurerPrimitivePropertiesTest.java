/**
 * 
 */
package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

/**
 * Unit tests for {@code DefaultConfigurer.configureProperties()}
 * with primitive properties.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 */
@Tag("ut")
class DefaultConfigurerPrimitivePropertiesTest {

	private static final String TEST_BOOL_KEY = "test.primitives.boolean";
	private static final boolean DEFAULT_BOOL_VALUE = false;
	private static final boolean CONFIG_BOOL_VALUE = true;
	private static final String TEST_CHAR_KEY = "test.primitives.char";
	private static final char DEFAULT_CHAR_VALUE = 'A';
	private static final char CONFIG_CHAR_VALUE = 'Z';
	private static final String TEST_BYTE_KEY = "test.primitives.byte";
	private static final byte DEFAULT_BYTE_VALUE = -1;
	private static final byte CONFIG_BYTE_VALUE = 43;
	private static final String TEST_SHORT_KEY = "test.primitives.short";
	private static final short DEFAULT_SHORT_VALUE = -1;
	private static final short CONFIG_SHORT_VALUE = 44;
	private static final String TEST_INT_KEY = "test.primitives.int";
	private static final int DEFAULT_INT_VALUE = -1;
	private static final int CONFIG_INT_VALUE = 10;
	private static final String TEST_LONG_KEY = "test.primitives.long";
	private static final long DEFAULT_LONG_VALUE = -1;
	private static final long CONFIG_LONG_VALUE = 563343;
	private static final String TEST_FLOAT_KEY = "test.primitives.float";
	private static final float DEFAULT_FLOAT_VALUE = -1;
	private static final float CONFIG_FLOAT_VALUE = 20.3f;
	private static final String TEST_DOUBLE_KEY = "test.primitives.double";
	private static final double DEFAULT_DOUBLE_VALUE = -1;
	private static final double CONFIG_DOUBLE_VALUE = 643.5;

	/**
	 * Test method for {@link DefaultConfigurer#configureProperties(Configurable, Config)} for
	 * configured values for primitives.
	 */
	@Test
	public void testConfigurePrimitiveProperties() {
		final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
		final Config config = BDDMockito.mock(Config.class);
		
		given(config.contains(TEST_BOOL_KEY)).willReturn(true);
		given(config.get(TEST_BOOL_KEY, Boolean.class)).willReturn(CONFIG_BOOL_VALUE);
		given(config.contains(TEST_CHAR_KEY)).willReturn(true);
		given(config.get(TEST_CHAR_KEY, Character.class)).willReturn(CONFIG_CHAR_VALUE);
		given(config.contains(TEST_BYTE_KEY)).willReturn(true);
		given(config.get(TEST_BYTE_KEY, Byte.class)).willReturn(CONFIG_BYTE_VALUE);
		given(config.contains(TEST_SHORT_KEY)).willReturn(true);
		given(config.get(TEST_SHORT_KEY, Short.class)).willReturn(CONFIG_SHORT_VALUE);
		given(config.contains(TEST_INT_KEY)).willReturn(true);
		given(config.get(TEST_INT_KEY, Integer.class)).willReturn(CONFIG_INT_VALUE);
		given(config.contains(TEST_LONG_KEY)).willReturn(true);
		given(config.get(TEST_LONG_KEY, Long.class)).willReturn(CONFIG_LONG_VALUE);
		given(config.contains(TEST_FLOAT_KEY)).willReturn(true);
		given(config.get(TEST_FLOAT_KEY, Float.class)).willReturn(CONFIG_FLOAT_VALUE);
		given(config.contains(TEST_DOUBLE_KEY)).willReturn(true);
		given(config.get(TEST_DOUBLE_KEY, Double.class)).willReturn(CONFIG_DOUBLE_VALUE);
		
		final DefaultConfigurer configurer = new DefaultConfigurer(configProvider);
		final ConfigurablePrimitivesTestBean bean = new ConfigurablePrimitivesTestBean();
		configurer.configureProperties(bean, config);
		assertEquals(CONFIG_BOOL_VALUE, bean.isBoolProp());
		assertEquals(CONFIG_CHAR_VALUE, bean.getCharProp());
		assertEquals(CONFIG_BYTE_VALUE, bean.getByteProp());
		assertEquals(CONFIG_SHORT_VALUE, bean.getShortProp());
		assertEquals(CONFIG_INT_VALUE, bean.getIntProp());
		assertEquals(CONFIG_LONG_VALUE, bean.getLongProp());
		assertEquals(CONFIG_FLOAT_VALUE, bean.getFloatProp());
		assertEquals(CONFIG_DOUBLE_VALUE, bean.getDoubleProp());
		
		then(config).should(times(1)).contains(TEST_BOOL_KEY);
		then(config).should(times(1)).get(TEST_BOOL_KEY, Boolean.class);
		then(config).should(times(1)).contains(TEST_CHAR_KEY);
		then(config).should(times(1)).get(TEST_CHAR_KEY, Character.class);
		then(config).should(times(1)).contains(TEST_BYTE_KEY);
		then(config).should(times(1)).get(TEST_BYTE_KEY, Byte.class);
		then(config).should(times(1)).contains(TEST_SHORT_KEY);
		then(config).should(times(1)).get(TEST_SHORT_KEY, Short.class);
		then(config).should(times(1)).contains(TEST_INT_KEY);
		then(config).should(times(1)).get(TEST_INT_KEY, Integer.class);
		then(config).should(times(1)).contains(TEST_LONG_KEY);
		then(config).should(times(1)).get(TEST_LONG_KEY, Long.class);
		then(config).should(times(1)).contains(TEST_FLOAT_KEY);
		then(config).should(times(1)).get(TEST_FLOAT_KEY, Float.class);
		then(config).should(times(1)).contains(TEST_DOUBLE_KEY);
		then(config).should(times(1)).get(TEST_DOUBLE_KEY, Double.class);
	}

	/**
	 * Test method for {@link DefaultConfigurer#configureProperties(Configurable, Config)} for
	 * configured {@code null} for primitives.
	 */
	@Test
	public void testConfigurePrimitivePropertiesNull() {
		final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
		final Config config = BDDMockito.mock(Config.class);
		
		given(config.contains(TEST_BOOL_KEY)).willReturn(true);
		given(config.get(TEST_BOOL_KEY, Boolean.class)).willReturn(null);
		given(config.contains(TEST_CHAR_KEY)).willReturn(true);
		given(config.get(TEST_CHAR_KEY, Character.class)).willReturn(null);
		given(config.contains(TEST_BYTE_KEY)).willReturn(true);
		given(config.get(TEST_BYTE_KEY, Byte.class)).willReturn(null);
		given(config.contains(TEST_SHORT_KEY)).willReturn(true);
		given(config.get(TEST_SHORT_KEY, Short.class)).willReturn(null);
		given(config.contains(TEST_INT_KEY)).willReturn(true);
		given(config.get(TEST_INT_KEY, Integer.class)).willReturn(null);
		given(config.contains(TEST_LONG_KEY)).willReturn(true);
		given(config.get(TEST_LONG_KEY, Long.class)).willReturn(null);
		given(config.contains(TEST_FLOAT_KEY)).willReturn(true);
		given(config.get(TEST_FLOAT_KEY, Float.class)).willReturn(null);
		given(config.contains(TEST_DOUBLE_KEY)).willReturn(true);
		given(config.get(TEST_DOUBLE_KEY, Double.class)).willReturn(null);
		
		final DefaultConfigurer configurer = new DefaultConfigurer(configProvider);
		final ConfigurablePrimitivesTestBean bean = new ConfigurablePrimitivesTestBean();
		configurer.configureProperties(bean, config);
		assertEquals(DEFAULT_BOOL_VALUE, bean.isBoolProp());
		assertEquals(DEFAULT_CHAR_VALUE, bean.getCharProp());
		assertEquals(DEFAULT_BYTE_VALUE, bean.getByteProp());
		assertEquals(DEFAULT_SHORT_VALUE, bean.getShortProp());
		assertEquals(DEFAULT_INT_VALUE, bean.getIntProp());
		assertEquals(DEFAULT_LONG_VALUE, bean.getLongProp());
		assertEquals(DEFAULT_FLOAT_VALUE, bean.getFloatProp());
		assertEquals(DEFAULT_DOUBLE_VALUE, bean.getDoubleProp());
		
		then(config).should(times(1)).contains(TEST_BOOL_KEY);
		then(config).should(times(1)).get(TEST_BOOL_KEY, Boolean.class);
		then(config).should(times(1)).contains(TEST_CHAR_KEY);
		then(config).should(times(1)).get(TEST_CHAR_KEY, Character.class);
		then(config).should(times(1)).contains(TEST_BYTE_KEY);
		then(config).should(times(1)).get(TEST_BYTE_KEY, Byte.class);
		then(config).should(times(1)).contains(TEST_SHORT_KEY);
		then(config).should(times(1)).get(TEST_SHORT_KEY, Short.class);
		then(config).should(times(1)).contains(TEST_INT_KEY);
		then(config).should(times(1)).get(TEST_INT_KEY, Integer.class);
		then(config).should(times(1)).contains(TEST_LONG_KEY);
		then(config).should(times(1)).get(TEST_LONG_KEY, Long.class);
		then(config).should(times(1)).contains(TEST_FLOAT_KEY);
		then(config).should(times(1)).get(TEST_FLOAT_KEY, Float.class);
		then(config).should(times(1)).contains(TEST_DOUBLE_KEY);
		then(config).should(times(1)).get(TEST_DOUBLE_KEY, Double.class);
	}

	/**
	 * Test method for {@link DefaultConfigurer#configureProperties(Configurable, Config)} for
	 * unconfigured values for primitives.
	 */
	@Test
	public void testConfigurePrimitivePropertiesUnconfigured() {
		final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
		final Config config = BDDMockito.mock(Config.class);
		
		given(config.contains(TEST_BOOL_KEY)).willReturn(false);
		given(config.get(TEST_BOOL_KEY, Boolean.class)).willReturn(null);
		given(config.contains(TEST_CHAR_KEY)).willReturn(false);
		given(config.get(TEST_CHAR_KEY, Character.class)).willReturn(null);
		given(config.contains(TEST_BYTE_KEY)).willReturn(false);
		given(config.get(TEST_BYTE_KEY, Byte.class)).willReturn(null);
		given(config.contains(TEST_SHORT_KEY)).willReturn(false);
		given(config.get(TEST_SHORT_KEY, Short.class)).willReturn(null);
		given(config.contains(TEST_INT_KEY)).willReturn(false);
		given(config.get(TEST_INT_KEY, Integer.class)).willReturn(null);
		given(config.contains(TEST_LONG_KEY)).willReturn(false);
		given(config.get(TEST_LONG_KEY, Long.class)).willReturn(null);
		given(config.contains(TEST_FLOAT_KEY)).willReturn(false);
		given(config.get(TEST_FLOAT_KEY, Float.class)).willReturn(null);
		given(config.contains(TEST_DOUBLE_KEY)).willReturn(false);
		given(config.get(TEST_DOUBLE_KEY, Double.class)).willReturn(null);
		
		final DefaultConfigurer configurer = new DefaultConfigurer(configProvider);
		final ConfigurablePrimitivesTestBean bean = new ConfigurablePrimitivesTestBean();
		configurer.configureProperties(bean, config);
		assertEquals(DEFAULT_BOOL_VALUE, bean.isBoolProp());
		assertEquals(DEFAULT_CHAR_VALUE, bean.getCharProp());
		assertEquals(DEFAULT_BYTE_VALUE, bean.getByteProp());
		assertEquals(DEFAULT_SHORT_VALUE, bean.getShortProp());
		assertEquals(DEFAULT_INT_VALUE, bean.getIntProp());
		assertEquals(DEFAULT_LONG_VALUE, bean.getLongProp());
		assertEquals(DEFAULT_FLOAT_VALUE, bean.getFloatProp());
		assertEquals(DEFAULT_DOUBLE_VALUE, bean.getDoubleProp());
		
		then(config).should(times(1)).contains(TEST_BOOL_KEY);
		then(config).should(times(0)).get(TEST_BOOL_KEY, Boolean.class);
		then(config).should(times(1)).contains(TEST_CHAR_KEY);
		then(config).should(times(0)).get(TEST_CHAR_KEY, Character.class);
		then(config).should(times(1)).contains(TEST_BYTE_KEY);
		then(config).should(times(0)).get(TEST_BYTE_KEY, Byte.class);
		then(config).should(times(1)).contains(TEST_SHORT_KEY);
		then(config).should(times(0)).get(TEST_SHORT_KEY, Short.class);
		then(config).should(times(1)).contains(TEST_INT_KEY);
		then(config).should(times(0)).get(TEST_INT_KEY, Integer.class);
		then(config).should(times(1)).contains(TEST_LONG_KEY);
		then(config).should(times(0)).get(TEST_LONG_KEY, Long.class);
		then(config).should(times(1)).contains(TEST_FLOAT_KEY);
		then(config).should(times(0)).get(TEST_FLOAT_KEY, Float.class);
		then(config).should(times(1)).contains(TEST_DOUBLE_KEY);
		then(config).should(times(0)).get(TEST_DOUBLE_KEY, Double.class);
	}

	/**
	 * Test method for {@link DefaultConfigurer#configureProperties(Configurable, Config)} for
	 * configured values for primitive wrappers.
	 */
	@Test
	public void testConfigureWrapperProperties() {
		final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
		final Config config = BDDMockito.mock(Config.class);
		
		given(config.contains(TEST_BOOL_KEY)).willReturn(true);
		given(config.get(TEST_BOOL_KEY, Boolean.class)).willReturn(CONFIG_BOOL_VALUE);
		given(config.contains(TEST_CHAR_KEY)).willReturn(true);
		given(config.get(TEST_CHAR_KEY, Character.class)).willReturn(CONFIG_CHAR_VALUE);
		given(config.contains(TEST_BYTE_KEY)).willReturn(true);
		given(config.get(TEST_BYTE_KEY, Byte.class)).willReturn(CONFIG_BYTE_VALUE);
		given(config.contains(TEST_SHORT_KEY)).willReturn(true);
		given(config.get(TEST_SHORT_KEY, Short.class)).willReturn(CONFIG_SHORT_VALUE);
		given(config.contains(TEST_INT_KEY)).willReturn(true);
		given(config.get(TEST_INT_KEY, Integer.class)).willReturn(CONFIG_INT_VALUE);
		given(config.contains(TEST_LONG_KEY)).willReturn(true);
		given(config.get(TEST_LONG_KEY, Long.class)).willReturn(CONFIG_LONG_VALUE);
		given(config.contains(TEST_FLOAT_KEY)).willReturn(true);
		given(config.get(TEST_FLOAT_KEY, Float.class)).willReturn(CONFIG_FLOAT_VALUE);
		given(config.contains(TEST_DOUBLE_KEY)).willReturn(true);
		given(config.get(TEST_DOUBLE_KEY, Double.class)).willReturn(CONFIG_DOUBLE_VALUE);
		
		final DefaultConfigurer configurer = new DefaultConfigurer(configProvider);
		final ConfigurableWrappersTestBean bean = new ConfigurableWrappersTestBean();
		configurer.configureProperties(bean, config);
		assertEquals(CONFIG_BOOL_VALUE, bean.isBoolProp());
		assertEquals(CONFIG_CHAR_VALUE, bean.getCharProp());
		assertEquals(CONFIG_BYTE_VALUE, bean.getByteProp());
		assertEquals(CONFIG_SHORT_VALUE, bean.getShortProp());
		assertEquals(CONFIG_INT_VALUE, bean.getIntProp());
		assertEquals(CONFIG_LONG_VALUE, bean.getLongProp());
		assertEquals(CONFIG_FLOAT_VALUE, bean.getFloatProp());
		assertEquals(CONFIG_DOUBLE_VALUE, bean.getDoubleProp());
		
		then(config).should(times(1)).contains(TEST_BOOL_KEY);
		then(config).should(times(1)).get(TEST_BOOL_KEY, Boolean.class);
		then(config).should(times(1)).contains(TEST_CHAR_KEY);
		then(config).should(times(1)).get(TEST_CHAR_KEY, Character.class);
		then(config).should(times(1)).contains(TEST_BYTE_KEY);
		then(config).should(times(1)).get(TEST_BYTE_KEY, Byte.class);
		then(config).should(times(1)).contains(TEST_SHORT_KEY);
		then(config).should(times(1)).get(TEST_SHORT_KEY, Short.class);
		then(config).should(times(1)).contains(TEST_INT_KEY);
		then(config).should(times(1)).get(TEST_INT_KEY, Integer.class);
		then(config).should(times(1)).contains(TEST_LONG_KEY);
		then(config).should(times(1)).get(TEST_LONG_KEY, Long.class);
		then(config).should(times(1)).contains(TEST_FLOAT_KEY);
		then(config).should(times(1)).get(TEST_FLOAT_KEY, Float.class);
		then(config).should(times(1)).contains(TEST_DOUBLE_KEY);
		then(config).should(times(1)).get(TEST_DOUBLE_KEY, Double.class);
	}

	/**
	 * Test method for {@link DefaultConfigurer#configureProperties(Configurable, Config)} for
	 * configured {@code null} values for primitive wrappers.
	 */
	@Test
	public void testConfigureWrapperPropertiesNull() {
		final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
		final Config config = BDDMockito.mock(Config.class);
		
		given(config.contains(TEST_BOOL_KEY)).willReturn(true);
		given(config.get(TEST_BOOL_KEY, Boolean.class)).willReturn(null);
		given(config.contains(TEST_CHAR_KEY)).willReturn(true);
		given(config.get(TEST_CHAR_KEY, Character.class)).willReturn(null);
		given(config.contains(TEST_BYTE_KEY)).willReturn(true);
		given(config.get(TEST_BYTE_KEY, Byte.class)).willReturn(null);
		given(config.contains(TEST_SHORT_KEY)).willReturn(true);
		given(config.get(TEST_SHORT_KEY, Short.class)).willReturn(null);
		given(config.contains(TEST_INT_KEY)).willReturn(true);
		given(config.get(TEST_INT_KEY, Integer.class)).willReturn(null);
		given(config.contains(TEST_LONG_KEY)).willReturn(true);
		given(config.get(TEST_LONG_KEY, Long.class)).willReturn(null);
		given(config.contains(TEST_FLOAT_KEY)).willReturn(true);
		given(config.get(TEST_FLOAT_KEY, Float.class)).willReturn(null);
		given(config.contains(TEST_DOUBLE_KEY)).willReturn(true);
		given(config.get(TEST_DOUBLE_KEY, Double.class)).willReturn(null);
		
		final DefaultConfigurer configurer = new DefaultConfigurer(configProvider);
		final ConfigurableWrappersTestBean bean = new ConfigurableWrappersTestBean();
		configurer.configureProperties(bean, config);
		assertNull(bean.isBoolProp());
		assertNull(bean.getCharProp());
		assertNull(bean.getByteProp());
		assertNull(bean.getShortProp());
		assertNull(bean.getIntProp());
		assertNull(bean.getLongProp());
		assertNull(bean.getFloatProp());
		assertNull(bean.getDoubleProp());
		
		then(config).should(times(1)).contains(TEST_BOOL_KEY);
		then(config).should(times(1)).get(TEST_BOOL_KEY, Boolean.class);
		then(config).should(times(1)).contains(TEST_CHAR_KEY);
		then(config).should(times(1)).get(TEST_CHAR_KEY, Character.class);
		then(config).should(times(1)).contains(TEST_BYTE_KEY);
		then(config).should(times(1)).get(TEST_BYTE_KEY, Byte.class);
		then(config).should(times(1)).contains(TEST_SHORT_KEY);
		then(config).should(times(1)).get(TEST_SHORT_KEY, Short.class);
		then(config).should(times(1)).contains(TEST_INT_KEY);
		then(config).should(times(1)).get(TEST_INT_KEY, Integer.class);
		then(config).should(times(1)).contains(TEST_LONG_KEY);
		then(config).should(times(1)).get(TEST_LONG_KEY, Long.class);
		then(config).should(times(1)).contains(TEST_FLOAT_KEY);
		then(config).should(times(1)).get(TEST_FLOAT_KEY, Float.class);
		then(config).should(times(1)).contains(TEST_DOUBLE_KEY);
		then(config).should(times(1)).get(TEST_DOUBLE_KEY, Double.class);
	}

	/**
	 * Test method for {@link DefaultConfigurer#configureProperties(Configurable, Config)} for
	 * unconfigured values for primitive wrappers.
	 */
	@Test
	public void testConfigureWrapperPropertiesUnconfigured() {
		final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
		final Config config = BDDMockito.mock(Config.class);
		
		given(config.contains(TEST_BOOL_KEY)).willReturn(false);
		given(config.get(TEST_BOOL_KEY, Boolean.class)).willReturn(null);
		given(config.contains(TEST_CHAR_KEY)).willReturn(false);
		given(config.get(TEST_CHAR_KEY, Character.class)).willReturn(null);
		given(config.contains(TEST_BYTE_KEY)).willReturn(false);
		given(config.get(TEST_BYTE_KEY, Byte.class)).willReturn(null);
		given(config.contains(TEST_SHORT_KEY)).willReturn(false);
		given(config.get(TEST_SHORT_KEY, Short.class)).willReturn(null);
		given(config.contains(TEST_INT_KEY)).willReturn(false);
		given(config.get(TEST_INT_KEY, Integer.class)).willReturn(null);
		given(config.contains(TEST_LONG_KEY)).willReturn(false);
		given(config.get(TEST_LONG_KEY, Long.class)).willReturn(null);
		given(config.contains(TEST_FLOAT_KEY)).willReturn(false);
		given(config.get(TEST_FLOAT_KEY, Float.class)).willReturn(null);
		given(config.contains(TEST_DOUBLE_KEY)).willReturn(false);
		given(config.get(TEST_DOUBLE_KEY, Double.class)).willReturn(null);
		
		final DefaultConfigurer configurer = new DefaultConfigurer(configProvider);
		final ConfigurableWrappersTestBean bean = new ConfigurableWrappersTestBean();
		configurer.configureProperties(bean, config);
		assertEquals(DEFAULT_BOOL_VALUE, bean.isBoolProp());
		assertEquals(DEFAULT_CHAR_VALUE, bean.getCharProp());
		assertEquals(DEFAULT_BYTE_VALUE, bean.getByteProp());
		assertEquals(DEFAULT_SHORT_VALUE, bean.getShortProp());
		assertEquals(DEFAULT_INT_VALUE, bean.getIntProp());
		assertEquals(DEFAULT_LONG_VALUE, bean.getLongProp());
		assertEquals(DEFAULT_FLOAT_VALUE, bean.getFloatProp());
		assertEquals(DEFAULT_DOUBLE_VALUE, bean.getDoubleProp());
		
		then(config).should(times(1)).contains(TEST_BOOL_KEY);
		then(config).should(times(0)).get(TEST_BOOL_KEY, Boolean.class);
		then(config).should(times(1)).contains(TEST_CHAR_KEY);
		then(config).should(times(0)).get(TEST_CHAR_KEY, Character.class);
		then(config).should(times(1)).contains(TEST_BYTE_KEY);
		then(config).should(times(0)).get(TEST_BYTE_KEY, Byte.class);
		then(config).should(times(1)).contains(TEST_SHORT_KEY);
		then(config).should(times(0)).get(TEST_SHORT_KEY, Short.class);
		then(config).should(times(1)).contains(TEST_INT_KEY);
		then(config).should(times(0)).get(TEST_INT_KEY, Integer.class);
		then(config).should(times(1)).contains(TEST_LONG_KEY);
		then(config).should(times(0)).get(TEST_LONG_KEY, Long.class);
		then(config).should(times(1)).contains(TEST_FLOAT_KEY);
		then(config).should(times(0)).get(TEST_FLOAT_KEY, Float.class);
		then(config).should(times(1)).contains(TEST_DOUBLE_KEY);
		then(config).should(times(0)).get(TEST_DOUBLE_KEY, Double.class);
	}

	/**
	 * Test method for {@link DefaultConfigurer#configureProperties(Configurable, Config)} for
	 * configured values for properties with mixed permissions.
	 */
	@Test
	public void testConfigurePropertiesPermissions() {
		final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
		final Config config = BDDMockito.mock(Config.class);
		
		given(config.contains(TEST_BOOL_KEY)).willReturn(true);
		given(config.get(TEST_BOOL_KEY, Boolean.class)).willReturn(CONFIG_BOOL_VALUE);
		given(config.contains(TEST_CHAR_KEY)).willReturn(true);
		given(config.get(TEST_CHAR_KEY, Character.class)).willReturn(CONFIG_CHAR_VALUE);
		given(config.contains(TEST_BYTE_KEY)).willReturn(true);
		given(config.get(TEST_BYTE_KEY, Byte.class)).willReturn(CONFIG_BYTE_VALUE);
		given(config.contains(TEST_SHORT_KEY)).willReturn(true);
		given(config.get(TEST_SHORT_KEY, Short.class)).willReturn(CONFIG_SHORT_VALUE);
		given(config.contains(TEST_INT_KEY)).willReturn(true);
		given(config.get(TEST_INT_KEY, Integer.class)).willReturn(CONFIG_INT_VALUE);
		given(config.contains(TEST_LONG_KEY)).willReturn(true);
		given(config.get(TEST_LONG_KEY, Long.class)).willReturn(CONFIG_LONG_VALUE);
		given(config.contains(TEST_FLOAT_KEY)).willReturn(true);
		given(config.get(TEST_FLOAT_KEY, Float.class)).willReturn(CONFIG_FLOAT_VALUE);
		given(config.contains(TEST_DOUBLE_KEY)).willReturn(true);
		given(config.get(TEST_DOUBLE_KEY, Double.class)).willReturn(CONFIG_DOUBLE_VALUE);
		
		final DefaultConfigurer configurer = new DefaultConfigurer(configProvider);
		final ConfigurableMixedPrivateTestBean bean = new ConfigurableMixedPrivateTestBean();
		configurer.configureProperties(bean, config);
		assertEquals(CONFIG_BOOL_VALUE, bean.isBoolProp());
		assertEquals(CONFIG_CHAR_VALUE, bean.getCharProp());
		assertEquals(DEFAULT_BYTE_VALUE, bean.getByteProp());
		assertEquals(DEFAULT_SHORT_VALUE, bean.getShortProp());
		assertEquals(DEFAULT_INT_VALUE, bean.getIntProp());
		assertEquals(DEFAULT_LONG_VALUE, bean.getLongProp());
		assertEquals(DEFAULT_FLOAT_VALUE, bean.getFloatProp());
		assertEquals(DEFAULT_DOUBLE_VALUE, bean.getDoubleProp());
		
		then(config).should(times(1)).contains(TEST_BOOL_KEY);
		then(config).should(times(1)).get(TEST_BOOL_KEY, Boolean.class);
		then(config).should(times(1)).contains(TEST_CHAR_KEY);
		then(config).should(times(1)).get(TEST_CHAR_KEY, Character.class);
		then(config).should(times(1)).contains(TEST_BYTE_KEY);
		then(config).should(times(1)).get(TEST_BYTE_KEY, Byte.class);
		then(config).should(times(1)).contains(TEST_SHORT_KEY);
		then(config).should(times(1)).get(TEST_SHORT_KEY, Short.class);
		then(config).should(times(1)).contains(TEST_INT_KEY);
		then(config).should(times(1)).get(TEST_INT_KEY, Integer.class);
		then(config).should(times(1)).contains(TEST_LONG_KEY);
		then(config).should(times(1)).get(TEST_LONG_KEY, Long.class);
		then(config).should(times(1)).contains(TEST_FLOAT_KEY);
		then(config).should(times(1)).get(TEST_FLOAT_KEY, Float.class);
		then(config).should(times(1)).contains(TEST_DOUBLE_KEY);
		then(config).should(times(1)).get(TEST_DOUBLE_KEY, Double.class);
	}

	/**
	 * Test method for {@link DefaultConfigurer#configureProperties(Configurable, Config)} for
	 * configured {@code null} values for properties with mixed permissions.
	 */
	@Test
	public void testConfigurePropertiesPermissionsNull() {
		final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
		final Config config = BDDMockito.mock(Config.class);
		
		given(config.contains(TEST_BOOL_KEY)).willReturn(true);
		given(config.get(TEST_BOOL_KEY, Boolean.class)).willReturn(null);
		given(config.contains(TEST_CHAR_KEY)).willReturn(true);
		given(config.get(TEST_CHAR_KEY, Character.class)).willReturn(null);
		given(config.contains(TEST_BYTE_KEY)).willReturn(true);
		given(config.get(TEST_BYTE_KEY, Byte.class)).willReturn(null);
		given(config.contains(TEST_SHORT_KEY)).willReturn(true);
		given(config.get(TEST_SHORT_KEY, Short.class)).willReturn(null);
		given(config.contains(TEST_INT_KEY)).willReturn(true);
		given(config.get(TEST_INT_KEY, Integer.class)).willReturn(null);
		given(config.contains(TEST_LONG_KEY)).willReturn(true);
		given(config.get(TEST_LONG_KEY, Long.class)).willReturn(null);
		given(config.contains(TEST_FLOAT_KEY)).willReturn(true);
		given(config.get(TEST_FLOAT_KEY, Float.class)).willReturn(null);
		given(config.contains(TEST_DOUBLE_KEY)).willReturn(true);
		given(config.get(TEST_DOUBLE_KEY, Double.class)).willReturn(null);
		
		final DefaultConfigurer configurer = new DefaultConfigurer(configProvider);
		final ConfigurableMixedPrivateTestBean bean = new ConfigurableMixedPrivateTestBean();
		configurer.configureProperties(bean, config);
		assertNull(bean.isBoolProp());
		assertEquals(DEFAULT_CHAR_VALUE, bean.getCharProp());
		assertEquals(DEFAULT_BYTE_VALUE, bean.getByteProp());
		assertEquals(DEFAULT_SHORT_VALUE, bean.getShortProp());
		assertEquals(DEFAULT_INT_VALUE, bean.getIntProp());
		assertEquals(DEFAULT_LONG_VALUE, bean.getLongProp());
		assertEquals(DEFAULT_FLOAT_VALUE, bean.getFloatProp());
		assertEquals(DEFAULT_DOUBLE_VALUE, bean.getDoubleProp());
		
		then(config).should(times(1)).contains(TEST_BOOL_KEY);
		then(config).should(times(1)).get(TEST_BOOL_KEY, Boolean.class);
		then(config).should(times(1)).contains(TEST_CHAR_KEY);
		then(config).should(times(1)).get(TEST_CHAR_KEY, Character.class);
		then(config).should(times(1)).contains(TEST_BYTE_KEY);
		then(config).should(times(1)).get(TEST_BYTE_KEY, Byte.class);
		then(config).should(times(1)).contains(TEST_SHORT_KEY);
		then(config).should(times(1)).get(TEST_SHORT_KEY, Short.class);
		then(config).should(times(1)).contains(TEST_INT_KEY);
		then(config).should(times(1)).get(TEST_INT_KEY, Integer.class);
		then(config).should(times(1)).contains(TEST_LONG_KEY);
		then(config).should(times(1)).get(TEST_LONG_KEY, Long.class);
		then(config).should(times(1)).contains(TEST_FLOAT_KEY);
		then(config).should(times(1)).get(TEST_FLOAT_KEY, Float.class);
		then(config).should(times(1)).contains(TEST_DOUBLE_KEY);
		then(config).should(times(1)).get(TEST_DOUBLE_KEY, Double.class);
	}

	/**
	 * Test method for {@link DefaultConfigurer#configureProperties(Configurable, Config)} for
	 * unconfigured values for properties with mixed permissions.
	 */
	@Test
	public void testConfigurePropertiesPermissionsUnconfigured() {
		final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
		final Config config = BDDMockito.mock(Config.class);
		
		given(config.contains(TEST_BOOL_KEY)).willReturn(false);
		given(config.get(TEST_BOOL_KEY, Boolean.class)).willReturn(null);
		given(config.contains(TEST_CHAR_KEY)).willReturn(false);
		given(config.get(TEST_CHAR_KEY, Character.class)).willReturn(null);
		given(config.contains(TEST_BYTE_KEY)).willReturn(false);
		given(config.get(TEST_BYTE_KEY, Byte.class)).willReturn(null);
		given(config.contains(TEST_SHORT_KEY)).willReturn(false);
		given(config.get(TEST_SHORT_KEY, Short.class)).willReturn(null);
		given(config.contains(TEST_INT_KEY)).willReturn(false);
		given(config.get(TEST_INT_KEY, Integer.class)).willReturn(null);
		given(config.contains(TEST_LONG_KEY)).willReturn(false);
		given(config.get(TEST_LONG_KEY, Long.class)).willReturn(null);
		given(config.contains(TEST_FLOAT_KEY)).willReturn(false);
		given(config.get(TEST_FLOAT_KEY, Float.class)).willReturn(null);
		given(config.contains(TEST_DOUBLE_KEY)).willReturn(false);
		given(config.get(TEST_DOUBLE_KEY, Double.class)).willReturn(null);
		
		final DefaultConfigurer configurer = new DefaultConfigurer(configProvider);
		final ConfigurableMixedPrivateTestBean bean = new ConfigurableMixedPrivateTestBean();
		configurer.configureProperties(bean, config);
		assertEquals(DEFAULT_BOOL_VALUE, bean.isBoolProp());
		assertEquals(DEFAULT_CHAR_VALUE, bean.getCharProp());
		assertEquals(DEFAULT_BYTE_VALUE, bean.getByteProp());
		assertEquals(DEFAULT_SHORT_VALUE, bean.getShortProp());
		assertEquals(DEFAULT_INT_VALUE, bean.getIntProp());
		assertEquals(DEFAULT_LONG_VALUE, bean.getLongProp());
		assertEquals(DEFAULT_FLOAT_VALUE, bean.getFloatProp());
		assertEquals(DEFAULT_DOUBLE_VALUE, bean.getDoubleProp());
		
		then(config).should(times(1)).contains(TEST_BOOL_KEY);
		then(config).should(times(0)).get(TEST_BOOL_KEY, Boolean.class);
		then(config).should(times(1)).contains(TEST_CHAR_KEY);
		then(config).should(times(0)).get(TEST_CHAR_KEY, Character.class);
		then(config).should(times(1)).contains(TEST_BYTE_KEY);
		then(config).should(times(0)).get(TEST_BYTE_KEY, Byte.class);
		then(config).should(times(1)).contains(TEST_SHORT_KEY);
		then(config).should(times(0)).get(TEST_SHORT_KEY, Short.class);
		then(config).should(times(1)).contains(TEST_INT_KEY);
		then(config).should(times(0)).get(TEST_INT_KEY, Integer.class);
		then(config).should(times(1)).contains(TEST_LONG_KEY);
		then(config).should(times(0)).get(TEST_LONG_KEY, Long.class);
		then(config).should(times(1)).contains(TEST_FLOAT_KEY);
		then(config).should(times(0)).get(TEST_FLOAT_KEY, Float.class);
		then(config).should(times(1)).contains(TEST_DOUBLE_KEY);
		then(config).should(times(0)).get(TEST_DOUBLE_KEY, Double.class);
	}

	public static class ConfigurablePrimitivesTestBean
	implements Configurable {
		@ConfigurableProperty(TEST_BOOL_KEY)
		private boolean boolProp = DEFAULT_BOOL_VALUE;
		@ConfigurableProperty(TEST_CHAR_KEY)
		private char charProp = DEFAULT_CHAR_VALUE;
		@ConfigurableProperty(TEST_BYTE_KEY)
		private byte byteProp = DEFAULT_BYTE_VALUE;
		@ConfigurableProperty(TEST_SHORT_KEY)
		private short shortProp = DEFAULT_SHORT_VALUE;
		@ConfigurableProperty(TEST_INT_KEY)
		private int intProp = DEFAULT_INT_VALUE;
		@ConfigurableProperty(TEST_LONG_KEY)
		private long longProp = DEFAULT_LONG_VALUE;
		@ConfigurableProperty(TEST_FLOAT_KEY)
		private float floatProp = DEFAULT_FLOAT_VALUE;
		@ConfigurableProperty(TEST_DOUBLE_KEY)
		private double doubleProp = DEFAULT_DOUBLE_VALUE;
		private boolean configured;
		@Override
		public void configure(Config config) {
			this.configured = true;
		}
		@Override
		public boolean isConfigured() {
			return this.configured;
		}
		public boolean isBoolProp() {
			return boolProp;
		}
		public void setBoolProp(boolean boolProp) {
			this.boolProp = boolProp;
		}
		public char getCharProp() {
			return charProp;
		}
		public void setCharProp(char charProp) {
			this.charProp = charProp;
		}
		public byte getByteProp() {
			return byteProp;
		}
		public void setByteProp(byte byteProp) {
			this.byteProp = byteProp;
		}
		public short getShortProp() {
			return shortProp;
		}
		public void setShortProp(short shortProp) {
			this.shortProp = shortProp;
		}
		public int getIntProp() {
			return intProp;
		}
		public void setIntProp(int intProp) {
			this.intProp = intProp;
		}
		public long getLongProp() {
			return longProp;
		}
		public void setLongProp(long longProp) {
			this.longProp = longProp;
		}
		public float getFloatProp() {
			return floatProp;
		}
		public void setFloatProp(float floatProp) {
			this.floatProp = floatProp;
		}
		public double getDoubleProp() {
			return doubleProp;
		}
		public void setDoubleProp(double doubleProp) {
			this.doubleProp = doubleProp;
		}
	}

	public static class ConfigurableWrappersTestBean
	implements Configurable {
		@ConfigurableProperty(TEST_BOOL_KEY)
		private Boolean boolProp = DEFAULT_BOOL_VALUE;
		@ConfigurableProperty(TEST_CHAR_KEY)
		private Character charProp = DEFAULT_CHAR_VALUE;
		@ConfigurableProperty(TEST_BYTE_KEY)
		private Byte byteProp = DEFAULT_BYTE_VALUE;
		@ConfigurableProperty(TEST_SHORT_KEY)
		private Short shortProp = DEFAULT_SHORT_VALUE;
		@ConfigurableProperty(TEST_INT_KEY)
		private Integer intProp = DEFAULT_INT_VALUE;
		@ConfigurableProperty(TEST_LONG_KEY)
		private Long longProp = DEFAULT_LONG_VALUE;
		@ConfigurableProperty(TEST_FLOAT_KEY)
		private Float floatProp = DEFAULT_FLOAT_VALUE;
		@ConfigurableProperty(TEST_DOUBLE_KEY)
		private Double doubleProp = DEFAULT_DOUBLE_VALUE;
		private boolean configured;
		@Override
		public void configure(Config config) {
			this.configured = true;
		}
		@Override
		public boolean isConfigured() {
			return this.configured;
		}
		public Boolean isBoolProp() {
			return boolProp;
		}
		public void setBoolProp(Boolean boolProp) {
			this.boolProp = boolProp;
		}
		public Character getCharProp() {
			return charProp;
		}
		public void setCharProp(Character charProp) {
			this.charProp = charProp;
		}
		public Byte getByteProp() {
			return byteProp;
		}
		public void setByteProp(Byte byteProp) {
			this.byteProp = byteProp;
		}
		public Short getShortProp() {
			return shortProp;
		}
		public void setShortProp(Short shortProp) {
			this.shortProp = shortProp;
		}
		public Integer getIntProp() {
			return intProp;
		}
		public void setIntProp(Integer intProp) {
			this.intProp = intProp;
		}
		public Long getLongProp() {
			return longProp;
		}
		public void setLongProp(Long longProp) {
			this.longProp = longProp;
		}
		public Float getFloatProp() {
			return floatProp;
		}
		public void setFloatProp(Float floatProp) {
			this.floatProp = floatProp;
		}
		public Double getDoubleProp() {
			return doubleProp;
		}
		public void setDoubleProp(Double doubleProp) {
			this.doubleProp = doubleProp;
		}
	}

	public static class ConfigurableMixedPrivateTestBean
	implements Configurable {
		@ConfigurableProperty(TEST_BOOL_KEY)
		private Boolean boolProp = DEFAULT_BOOL_VALUE;
		@ConfigurableProperty(TEST_CHAR_KEY)
		private char charProp = DEFAULT_CHAR_VALUE;
		@ConfigurableProperty(TEST_BYTE_KEY)
		private Byte byteProp = DEFAULT_BYTE_VALUE;
		@ConfigurableProperty(TEST_SHORT_KEY)
		private short shortProp = DEFAULT_SHORT_VALUE;
		@ConfigurableProperty(TEST_INT_KEY)
		private Integer intProp = DEFAULT_INT_VALUE;
		@ConfigurableProperty(TEST_LONG_KEY)
		private long longProp = DEFAULT_LONG_VALUE;
		@ConfigurableProperty(TEST_FLOAT_KEY)
		private Float floatProp = DEFAULT_FLOAT_VALUE;
		@ConfigurableProperty(TEST_DOUBLE_KEY)
		private double doubleProp = DEFAULT_DOUBLE_VALUE;
		private boolean configured;
		@Override
		public void configure(Config config) {
			this.configured = true;
		}
		@Override
		public boolean isConfigured() {
			return this.configured;
		}
		public Boolean isBoolProp() {
			return boolProp;
		}
		public void setBoolProp(Boolean boolProp) {
			this.boolProp = boolProp;
		}
		public char getCharProp() {
			return charProp;
		}
		public void setCharProp(char charProp) {
			this.charProp = charProp;
		}
		Byte getByteProp() {
			return byteProp;
		}
		void setByteProp(Byte byteProp) {
			this.byteProp = byteProp;
		}
		short getShortProp() {
			return shortProp;
		}
		void setShortProp(short shortProp) {
			this.shortProp = shortProp;
		}
		protected Integer getIntProp() {
			return intProp;
		}
		protected void setIntProp(Integer intProp) {
			this.intProp = intProp;
		}
		protected long getLongProp() {
			return longProp;
		}
		protected void setLongProp(long longProp) {
			this.longProp = longProp;
		}
		@SuppressWarnings("unused")
		private Float getFloatProp() {
			return floatProp;
		}
		@SuppressWarnings("unused")
		private void setFloatProp(Float floatProp) {
			this.floatProp = floatProp;
		}
		@SuppressWarnings("unused")
		private double getDoubleProp() {
			return doubleProp;
		}
		@SuppressWarnings("unused")
		private void setDoubleProp(double doubleProp) {
			this.doubleProp = doubleProp;
		}
	}
}
