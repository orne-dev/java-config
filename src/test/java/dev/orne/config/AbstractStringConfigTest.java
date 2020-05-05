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
 * @since 0.1
 */
@Tag("ut")
class AbstractStringConfigTest {

    private static final String TEST_KEY = "test.key";

    /**
     * Test method for {@link AbstractStringConfig#getBooleanParameter(String)} for
     * {@code null} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBooleanNull()
    throws ConfigException {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(null);
        
        final Boolean result = config.getBooleanParameter(TEST_KEY);
        assertNull(result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getBooleanParameter(String)} for
     * value "true".
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBooleanTrueLowerCase()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBooleanTrueUpperCase()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBooleanTrueMixedCase()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBooleanFalseLowerCase()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBooleanFalseUpperCase()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBooleanFalseMixedCase()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBooleanOther()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetNumberNull()
    throws ConfigException {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(null);
        
        final Number result = config.getNumberParameter(TEST_KEY);
        assertNull(result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getNumberParameter(String)} for
     * {@code Integer.MIN_VALUE} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetNumberIntegerMin()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetNumberIntegerMax()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetNumberLongMin()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetNumberLongMax()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetNumberFloatMin()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetNumberFloatMax()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetNumberDoubleMin()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetNumberDoubleMax()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetNumberBigDecimal()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetNumberInvalid()
    throws ConfigException {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn("this is not a number");
        
        assertThrows(NumberFormatException.class, new Executable() {
            @Override
            public void execute()
            throws ConfigException {
                config.getNumberParameter(TEST_KEY);
            }
        });
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getInstantParameter(String)}
     * for {@code null} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetInstantNull()
    throws ConfigException {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(null);
        
        final Instant result = config.getInstantParameter(TEST_KEY);
        assertNull(result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractStringConfig#getInstantParameter(String)}
     * for valid ISO-8601 instant value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetInstant()
    throws ConfigException {
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
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetInstantInvalid()
    throws ConfigException {
        final AbstractStringConfig config = BDDMockito.spy(AbstractStringConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn("Invalid instant");
        
        assertThrows(DateTimeParseException.class, new Executable() {
            @Override
            public void execute()
            throws ConfigException {
                config.getInstantParameter(TEST_KEY);
            }
        });
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }
}
