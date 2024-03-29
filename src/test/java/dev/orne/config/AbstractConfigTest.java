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

import java.time.DayOfWeek;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code AbstractConfig}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class AbstractConfigTest {

    private static final String TEST_KEY = "test.key";

    /**
     * Test method for {@link AbstractConfig#getBoolean(String)} of existent
     * property.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBoolean()
    throws ConfigException {
        final Boolean boolValue = Boolean.TRUE;
        final AbstractConfig config = spy(AbstractConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(true);
        given(config.getBooleanParameter(TEST_KEY)).willReturn(boolValue);
        
        final Boolean result = config.getBoolean(TEST_KEY);
        
        assertNotNull(result);
        assertEquals(boolValue, result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(1)).getBooleanParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getBoolean(String)} of absent
     * property.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBooleanMissing()
    throws ConfigException {
        final AbstractConfig config = spy(AbstractConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        
        final Boolean result = config.getBoolean(TEST_KEY);
        
        assertNull(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(never()).getBooleanParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getString(String)} of existent
     * property.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetString()
    throws ConfigException {
        final String strValue = "test.value";
        final AbstractConfig config = spy(AbstractConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(true);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final String result = config.getString(TEST_KEY);
        
        assertNotNull(result);
        assertEquals(strValue, result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getString(String)} of absent
     * property.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetStringMissing()
    throws ConfigException {
        final AbstractConfig config = spy(AbstractConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        
        final String result = config.getString(TEST_KEY);
        
        assertNull(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(never()).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#get(String, Class)} with
     * {@code Enum} target type of existent property.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetEnum()
    throws ConfigException {
        final DayOfWeek expectedValue = DayOfWeek.SATURDAY;
        final String strValue = expectedValue.toString();
        final AbstractConfig config = spy(AbstractConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(true);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final DayOfWeek result = config.get(TEST_KEY, DayOfWeek.class);
        
        assertNotNull(result);
        assertSame(expectedValue, result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#get(String, Class)} with
     * {@code Enum} target type of absent property.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetEnumMissing()
    throws ConfigException {
        final AbstractConfig config = spy(AbstractConfig.class);
        given(config.containsParameter(TEST_KEY)).willReturn(false);
        
        final DayOfWeek result = config.get(TEST_KEY, DayOfWeek.class);
        
        assertNull(result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(never()).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#setConverter(ConvertUtilsBean)}.
     */
    @Test
    public void testSetConverter()
    throws ConfigException {
        final ConvertUtilsBean converter = mock(ConvertUtilsBean.class);
        final AbstractConfig config = spy(AbstractConfig.class);
        config.setConverter(converter);
        
        assertSame(converter, config.getConverter());
    }

    /**
     * Test method for {@link AbstractConfig#convertValue(Object, Class)}.
     */
    @Test
    public void testConvertValue()
    throws ConfigException {
        final Object rawValue = new Object();
        final Class<?> targetClass = Object.class;
        final Object expectedValue = new Object();
        
        final ConvertUtilsBean converter = mock(ConvertUtilsBean.class);
        final AbstractConfig config = spy(AbstractConfig.class);
        config.setConverter(converter);
        given(converter.convert(rawValue, targetClass)).willReturn(expectedValue);
        
        final Object result = config.convertValue(rawValue, targetClass);
        
        assertNotNull(result);
        assertSame(expectedValue, result);
        
        then(converter).should(times(1)).convert(rawValue, targetClass);
    }

    /**
     * Test method for {@link AbstractConfig#convertValue(Object, Class)}.
     */
    @Test
    public void testConvertValueNull()
    throws ConfigException {
        final Object rawValue = null;
        final Class<?> targetClass = Object.class;
        
        final ConvertUtilsBean converter = mock(ConvertUtilsBean.class);
        final AbstractConfig config = spy(AbstractConfig.class);
        config.setConverter(converter);
        
        final Object result = config.convertValue(rawValue, targetClass);
        
        assertNull(result);
        
        then(converter).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link AbstractConfig#convertValue(Object, Class)}.
     */
    @Test
    public void testConvertValueString()
    throws ConfigException {
        final Object rawValue = "raw value";
        final Class<?> targetClass = String.class;
        final Object expectedValue = rawValue;
        
        final ConvertUtilsBean converter = mock(ConvertUtilsBean.class);
        final AbstractConfig config = spy(AbstractConfig.class);
        config.setConverter(converter);
        
        final Object result = config.convertValue(rawValue, targetClass);
        
        assertNotNull(result);
        assertSame(expectedValue, result);
        
        then(converter).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link AbstractConfig#convertValue(Object, Class)}.
     */
    @Test
    public void testConvertValueObjectToString()
    throws ConfigException {
        final Object rawValue = new Object();
        final Class<?> targetClass = String.class;
        final Object expectedValue = "mock value";
        
        final ConvertUtilsBean converter = mock(ConvertUtilsBean.class);
        final AbstractConfig config = spy(AbstractConfig.class);
        config.setConverter(converter);
        given(converter.convert(rawValue, targetClass)).willReturn(expectedValue);
        
        final Object result = config.convertValue(rawValue, targetClass);
        
        assertNotNull(result);
        assertSame(expectedValue, result);
        
        then(converter).should(times(1)).convert(rawValue, targetClass);
    }

    /**
     * Test method for {@link AbstractConfig#convertValue(Object, Class)}.
     */
    @Test
    public void testConvertValueEnum()
    throws ConfigException {
        final Object rawValue = TestEnum.VALUEB.name();
        final Class<?> targetClass = TestEnum.class;
        final TestEnum expectedValue = TestEnum.VALUEB;
        
        final ConvertUtilsBean converter = mock(ConvertUtilsBean.class);
        final AbstractConfig config = spy(AbstractConfig.class);
        config.setConverter(converter);
        given(converter.convert(expectedValue, targetClass)).willReturn(expectedValue);
        
        final Object result = config.convertValue(rawValue, targetClass);
        
        assertNotNull(result);
        assertSame(expectedValue, result);
        
        then(converter).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link AbstractConfig#convertValue(Object, Class)}.
     */
    @Test
    public void testConvertValueObjectToEnum()
    throws ConfigException {
        final Object rawValue = new Object();
        final Class<?> targetClass = TestEnum.class;
        final Object expectedValue = TestEnum.VALUEB;
        
        final ConvertUtilsBean converter = mock(ConvertUtilsBean.class);
        final AbstractConfig config = spy(AbstractConfig.class);
        config.setConverter(converter);
        given(converter.convert(rawValue, targetClass)).willReturn(expectedValue);
        
        final Object result = config.convertValue(rawValue, targetClass);
        
        assertNotNull(result);
        assertSame(expectedValue, result);
        
        then(converter).should(times(1)).convert(rawValue, targetClass);
    }

    /**
     * Test method for {@link AbstractConfig#convertValue(Object, Class)}.
     */
    @Test
    public void testConvertValueException()
    throws ConfigException {
        final Object rawValue = new Object();
        final Class<?> targetClass = Object.class;
        final ConversionException convException = new ConversionException("mock exception");
        
        final ConvertUtilsBean converter = mock(ConvertUtilsBean.class);
        final AbstractConfig config = spy(AbstractConfig.class);
        config.setConverter(converter);
        given(converter.convert(rawValue, targetClass)).willThrow(convException);
        
        assertThrows(ConfigException.class, () -> {
            config.convertValue(rawValue, targetClass);
        });
        
        then(converter).should(times(1)).convert(rawValue, targetClass);
    }

    /**
     * Test method for {@link AbstractConfig#get(String, Class)}.
     */
    @Test
    public void testGet()
    throws ConfigException {
        final String strValue = "mock.value";
        final Class<?> targetClass = Object.class;
        final Object expectedValue = new Object();
        
        final ConvertUtilsBean converter = mock(ConvertUtilsBean.class);
        final AbstractConfig config = spy(AbstractConfig.class);
        config.setConverter(converter);
        willReturn(true).given(config).containsParameter(TEST_KEY);
        willReturn(strValue).given(config).getStringParameter(TEST_KEY);
        willReturn(expectedValue).given(converter).convert((Object) strValue, targetClass);
        
        final Object result = config.get(TEST_KEY, Object.class);
        
        assertNotNull(result);
        assertSame(expectedValue, result);
        
        then(config).should(times(1)).containsParameter(TEST_KEY);
        then(config).should(times(1)).getStringParameter(TEST_KEY);
        then(converter).should(times(1)).convert((Object) strValue, targetClass);
    }

    public static enum TestEnum {
        VALUEA,
        VALUEB,
        VALUEC;
    }
}
