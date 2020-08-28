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

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

/**
 * Unit tests for {@code AbstractMutableStringConfig}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class AbstractMutableStringConfigTest {

    /**
     * Test method for {@link AbstractMutableStringConfig#convertValueToString(Object)} for
     * {@code null} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testConvertToStringNull()
    throws ConfigException {
        final ConvertUtilsBean converters = mock(ConvertUtilsBean.class);
        final AbstractMutableStringConfig config = BDDMockito.spy(AbstractMutableStringConfig.class);
        config.setConverter(converters);
        
        final String result = config.convertValueToString(null);
        assertNull(result);
        
        then(converters).should(never()).lookup(any());
        then(converters).should(never()).convert(any());
    }

    /**
     * Test method for {@link AbstractMutableStringConfig#convertValueToString(Object)} for
     * {@code String} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testConvertToStringFromString()
    throws ConfigException {
        final ConvertUtilsBean converters = mock(ConvertUtilsBean.class);
        final AbstractMutableStringConfig config = BDDMockito.spy(AbstractMutableStringConfig.class);
        config.setConverter(converters);
        
        final String value = "test value";
        final String result = config.convertValueToString(value);
        assertNotNull(result);
        assertSame(value, result);
        
        then(converters).should(never()).lookup(any());
        then(converters).should(never()).convert(any());
    }

    /**
     * Test method for {@link AbstractMutableStringConfig#convertValueToString(Object)} for
     * {@code null} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testConvertToStringExplicitConverter()
    throws ConfigException {
        final ConvertUtilsBean converters = mock(ConvertUtilsBean.class);
        final Converter converter = mock(Converter.class);
        final AbstractMutableStringConfig config = BDDMockito.spy(AbstractMutableStringConfig.class);
        config.setConverter(converters);
        
        final TestType value = new TestType();
        final String expectedValue = "mock result";
        willReturn(converter).given(converters).lookup(TestType.class);
        willReturn(expectedValue).given(converter).convert(String.class, value);
        
        final String result = config.convertValueToString(value);
        assertNotNull(result);
        assertSame(expectedValue, result);
        
        then(converters).should(times(1)).lookup(TestType.class);
        then(converters).should(never()).convert(any());
        then(converter).should(times(1)).convert(String.class, value);
    }

    /**
     * Test method for {@link AbstractMutableStringConfig#convertValueToString(Object)} for
     * {@code null} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testConvertToStringExplicitParentConverter()
    throws ConfigException {
        final ConvertUtilsBean converters = mock(ConvertUtilsBean.class);
        final Converter converter = mock(Converter.class);
        final AbstractMutableStringConfig config = BDDMockito.spy(AbstractMutableStringConfig.class);
        config.setConverter(converters);
        
        final ExtendedTestType value = new ExtendedTestType();
        final String expectedValue = "mock result";
        willReturn(null).given(converters).lookup(ExtendedTestType.class);
        willReturn(converter).given(converters).lookup(TestType.class);
        willReturn(expectedValue).given(converter).convert(String.class, value);
        
        final String result = config.convertValueToString(value);
        assertNotNull(result);
        assertSame(expectedValue, result);
        
        then(converters).should(times(1)).lookup(ExtendedTestType.class);
        then(converters).should(times(1)).lookup(TestType.class);
        then(converters).should(never()).convert(any());
        then(converter).should(times(1)).convert(String.class, value);
    }

    /**
     * Test method for {@link AbstractMutableStringConfig#convertValueToString(Object)} for
     * {@code null} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testConvertToStringNoExplicitConverter()
    throws ConfigException {
        final ConvertUtilsBean converters = mock(ConvertUtilsBean.class);
        final AbstractMutableStringConfig config = BDDMockito.spy(AbstractMutableStringConfig.class);
        config.setConverter(converters);
        
        final TestType value = new TestType();
        final String expectedValue = "mock result";
        willReturn(null).given(converters).lookup(any());
        willReturn(expectedValue).given(converters).convert(value);
        
        final String result = config.convertValueToString(value);
        assertNotNull(result);
        assertSame(expectedValue, result);
        
        then(converters).should(atLeastOnce()).lookup(any());
        then(converters).should(times(1)).convert(value);
    }

    /**
     * Test method for {@link AbstractMutableStringConfig#convertValueToString(Object)} for
     * {@code null} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testConvertToStringError()
    throws ConfigException {
        final ConvertUtilsBean converters = mock(ConvertUtilsBean.class);
        final AbstractMutableStringConfig config = BDDMockito.spy(AbstractMutableStringConfig.class);
        config.setConverter(converters);
        
        final TestType value = new TestType();
        final ConversionException mockException = new ConversionException("Mock exception");
        willReturn(null).given(converters).lookup(any());
        willThrow(mockException).given(converters).convert(value);
        
        assertThrows(ConfigException.class, () -> {
            config.convertValueToString(value);
        });
        
        then(converters).should(atLeastOnce()).lookup(any());
        then(converters).should(times(1)).convert(value);
    }

    protected static class TestType {
        // No extra methods
    }

    protected static class ExtendedTestType
    extends TestType {
        // No extra methods
    }
}
