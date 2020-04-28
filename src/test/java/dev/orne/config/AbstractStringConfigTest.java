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
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.BDDMockito;

/**
 * Unit tests for {@code AbstractStringConfig}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0, 2019-07
 */
@Tag("ut")
class AbstractStringConfigTest {

    private static final String TEST_KEY = "test.key";

    /**
     * Test method for {@link AbstractStringConfig#getBooleanParameter(String)} for
     * {@code null} value.
     */
    @Test
    public void testGetBooleanNull() {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(null);
        
        final Boolean result = config.getBooleanParameter(TEST_KEY);
        assertNull(result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getBooleanParameter(String)} for
     * value "true".
     */
    @Test
    public void testGetBooleanTrueLowerCase() {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn("true");
        
        final Boolean result = config.getBooleanParameter(TEST_KEY);
        assertNotNull(result);
        assertTrue(result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getBooleanParameter(String)} for
     * value "TRUE".
     */
    @Test
    public void testGetBooleanTrueUpperCase() {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn("TRUE");
        
        final Boolean result = config.getBooleanParameter(TEST_KEY);
        assertNotNull(result);
        assertTrue(result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getBooleanParameter(String)} for
     * value "TrUe".
     */
    @Test
    public void testGetBooleanTrueMixedCase() {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn("TrUe");
        
        final Boolean result = config.getBooleanParameter(TEST_KEY);
        assertNotNull(result);
        assertTrue(result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getBooleanParameter(String)} for
     * value "true".
     */
    @Test
    public void testGetBooleanFalseLowerCase() {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn("false");
        
        final Boolean result = config.getBooleanParameter(TEST_KEY);
        assertNotNull(result);
        assertFalse(result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getBooleanParameter(String)} for
     * value "TRUE".
     */
    @Test
    public void testGetBooleanFalseUpperCase() {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn("FALSE");
        
        final Boolean result = config.getBooleanParameter(TEST_KEY);
        assertNotNull(result);
        assertFalse(result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getBooleanParameter(String)} for
     * value "fAlSe".
     */
    @Test
    public void testGetBooleanFalseMixedCase() {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn("fAlSe");
        
        final Boolean result = config.getBooleanParameter(TEST_KEY);
        assertNotNull(result);
        assertFalse(result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getBooleanParameter(String)} for
     * value "fAlSe".
     */
    @Test
    public void testGetBooleanOther() {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn("anyOtherValue");
        
        final Boolean result = config.getBooleanParameter(TEST_KEY);
        assertNotNull(result);
        assertFalse(result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getNumberParameter(String)} for
     * {@code null} value.
     */
    @Test
    public void testGetNumberNull() {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(null);
        
        final Number result = config.getNumberParameter(TEST_KEY);
        assertNull(result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getNumberParameter(String)} for
     * {@code Integer.MIN_VALUE} value.
     */
    @Test
    public void testGetNumberIntegerMin() {
        final Integer expectedValue = Integer.MIN_VALUE;
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(String.valueOf(expectedValue));
        
        final Number result = config.getNumberParameter(TEST_KEY);
        assertNotNull(result);
        assertEquals(expectedValue, result.intValue());
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getNumberParameter(String)} for
     * {@code Integer.MAX_VALUE} value.
     */
    @Test
    public void testGetNumberIntegerMax() {
        final Integer expectedValue = Integer.MAX_VALUE;
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(String.valueOf(expectedValue));
        
        final Number result = config.getNumberParameter(TEST_KEY);
        assertNotNull(result);
        assertEquals(expectedValue, result.intValue());
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getNumberParameter(String)} for
     * {@code Long.MIN_VALUE} value.
     */
    @Test
    public void testGetNumberLongMin() {
        final Long expectedValue = Long.MIN_VALUE;
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(String.valueOf(expectedValue));
        
        final Number result = config.getNumberParameter(TEST_KEY);
        assertNotNull(result);
        assertEquals(expectedValue, result.longValue());
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getNumberParameter(String)} for
     * {@code Long.MAX_VALUE} value.
     */
    @Test
    public void testGetNumberLongMax() {
        final Long expectedValue = Long.MAX_VALUE;
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(String.valueOf(expectedValue));
        
        final Number result = config.getNumberParameter(TEST_KEY);
        assertNotNull(result);
        assertEquals(expectedValue, result.longValue());
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getNumberParameter(String)} for
     * {@code Float.MIN_VALUE} value.
     */
    @Test
    public void testGetNumberFloatMin() {
        final Float expectedValue = Float.MIN_VALUE;
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(String.valueOf(expectedValue));
        
        final Number result = config.getNumberParameter(TEST_KEY);
        assertNotNull(result);
        assertEquals(expectedValue, result.floatValue());
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getNumberParameter(String)} for
     * {@code Float.MAX_VALUE} value.
     */
    @Test
    public void testGetNumberFloatMax() {
        final Float expectedValue = Float.MAX_VALUE;
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(String.valueOf(expectedValue));
        
        final Number result = config.getNumberParameter(TEST_KEY);
        assertNotNull(result);
        assertEquals(expectedValue, result.floatValue());
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getNumberParameter(String)} for
     * {@code Double.MIN_VALUE} value.
     */
    @Test
    public void testGetNumberDoubleMin() {
        final Double expectedValue = Double.MIN_VALUE;
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(String.valueOf(expectedValue));
        
        final Number result = config.getNumberParameter(TEST_KEY);
        assertNotNull(result);
        assertEquals(expectedValue, result.doubleValue());
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getNumberParameter(String)} for
     * {@code Double.MAX_VALUE} value.
     */
    @Test
    public void testGetNumberDoubleMax() {
        final Double expectedValue = Double.MAX_VALUE;
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(String.valueOf(expectedValue));
        
        final Number result = config.getNumberParameter(TEST_KEY);
        assertNotNull(result);
        assertEquals(expectedValue, result.doubleValue());
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getNumberParameter(String)}
     * for {@code BigDecimal} value.
     */
    @Test
    public void testGetNumberBigDecimal() {
        final BigDecimal expectedValue = new BigDecimal(
                "67349158364348316463214976392463847.348638463746238764873648736248E-3");
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(String.valueOf(expectedValue));
        
        final Number result = config.getNumberParameter(TEST_KEY);
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getNumberParameter(String)}
     * for {@code BigDecimal} value.
     */
    @Test
    public void testGetNumberInvalid() {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn("this is not a number");
        
        assertThrows(NumberFormatException.class, new Executable() {
            @Override
            public void execute() {
                config.getNumberParameter(TEST_KEY);
            }
        });
        
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getInstantParameter(String)}
     * for {@code null} value.
     */
    @Test
    public void testGetInstantNull() {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(null);
        
        final Instant result = config.getInstantParameter(TEST_KEY);
        assertNull(result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getInstantParameter(String)}
     * for valid ISO-8601 instant value.
     */
    @Test
    public void testGetInstant() {
        final Instant expectedValue = Instant.now();
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(expectedValue.toString());
        
        final Instant result = config.getInstantParameter(TEST_KEY);
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getInstantParameter(String)}
     * for invalid ISO-8601 instant value.
     */
    @Test
    public void testGetInstantInvalid() {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn("Invalid instant");
        
        assertThrows(DateTimeParseException.class, new Executable() {
            @Override
            public void execute() {
                config.getInstantParameter(TEST_KEY);
            }
        });
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }
}
