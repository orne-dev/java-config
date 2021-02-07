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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

/**
 * Unit tests for {@code DefaultConfigurer.configureProperties()}
 * with primitive properties.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testConfigurePrimitiveProperties()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testConfigurePrimitivePropertiesNull()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testConfigurePrimitivePropertiesUnconfigured()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testConfigureWrapperProperties()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testConfigureWrapperPropertiesNull()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testConfigureWrapperPropertiesUnconfigured()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testConfigurePropertiesPermissions()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testConfigurePropertiesPermissionsNull()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testConfigurePropertiesPermissionsUnconfigured()
    throws ConfigException {
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

    /**
     * Test method for {@link DefaultConfigurer#configureProperties(Configurable, Config)} for
     * configured values for properties with mixed permissions.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testConfigureWithBeanError()
    throws ConfigException {
        final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
        final Config config = BDDMockito.mock(Config.class);
        
        given(config.contains(TEST_BOOL_KEY)).willReturn(true);
        given(config.get(TEST_BOOL_KEY, Boolean.class)).willReturn(CONFIG_BOOL_VALUE);
        
        final DefaultConfigurer configurer = new DefaultConfigurer(configProvider);
        final ConfigurableSetterErrorTestBean bean = new ConfigurableSetterErrorTestBean();
        configurer.configureProperties(bean, config);
        
        assertNull(bean.isBoolProp());
        
        then(config).should(times(1)).contains(TEST_BOOL_KEY);
        then(config).should(times(1)).get(TEST_BOOL_KEY, Boolean.class);
    }

    /**
     * Test method for {@link DefaultConfigurer#configureProperties(Configurable, Config)} for
     * configured values for properties with mixed permissions.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testConfigureWithConfigError()
    throws ConfigException {
        final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
        final Config config = BDDMockito.mock(Config.class);
        
        given(config.contains(TEST_BOOL_KEY)).willReturn(true);
        given(config.get(TEST_BOOL_KEY, Boolean.class)).willThrow(ConfigException.class);
        given(config.contains(TEST_CHAR_KEY)).willReturn(true);
        given(config.get(TEST_CHAR_KEY, Character.class)).willThrow(ConfigException.class);
        given(config.contains(TEST_BYTE_KEY)).willReturn(true);
        given(config.get(TEST_BYTE_KEY, Byte.class)).willThrow(ConfigException.class);
        given(config.contains(TEST_SHORT_KEY)).willReturn(true);
        given(config.get(TEST_SHORT_KEY, Short.class)).willThrow(ConfigException.class);
        given(config.contains(TEST_INT_KEY)).willReturn(true);
        given(config.get(TEST_INT_KEY, Integer.class)).willThrow(ConfigException.class);
        given(config.contains(TEST_LONG_KEY)).willReturn(true);
        given(config.get(TEST_LONG_KEY, Long.class)).willThrow(ConfigException.class);
        given(config.contains(TEST_FLOAT_KEY)).willReturn(true);
        given(config.get(TEST_FLOAT_KEY, Float.class)).willThrow(ConfigException.class);
        given(config.contains(TEST_DOUBLE_KEY)).willReturn(true);
        given(config.get(TEST_DOUBLE_KEY, Double.class)).willThrow(ConfigException.class);
        
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
        protected Float getFloatProp() {
            return floatProp;
        }
        protected void setFloatProp(Float floatProp) {
            this.floatProp = floatProp;
        }
        protected double getDoubleProp() {
            return doubleProp;
        }
        protected void setDoubleProp(double doubleProp) {
            this.doubleProp = doubleProp;
        }
    }
    public static class ConfigurableSetterErrorTestBean
    implements Configurable {
        @ConfigurableProperty(TEST_BOOL_KEY)
        private Boolean boolProp = null;
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
            throw new IllegalStateException();
        }
    }
}
