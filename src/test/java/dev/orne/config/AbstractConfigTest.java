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

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

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
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code Boolean} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBoolean()
    throws ConfigException {
        final Boolean boolValue = Boolean.TRUE;
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getBooleanParameter(TEST_KEY)).willReturn(boolValue);
        
        final Boolean result = config.getParameter(TEST_KEY, Boolean.class);
        
        assertNotNull(result);
        assertEquals(boolValue, result);
        
        then(config).should(times(1)).getBooleanParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code String} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetString()
    throws ConfigException {
        final String strValue = "test.value";
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final String result = config.getParameter(TEST_KEY, String.class);
        
        assertNotNull(result);
        assertEquals(strValue, result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code Locale} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetLocale()
    throws ConfigException {
        final Locale expectedValue = Locale.ENGLISH;
        final String strValue = expectedValue.toString();
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final Locale result = config.getParameter(TEST_KEY, Locale.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code Enum} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetEnum()
    throws ConfigException {
        final DayOfWeek expectedValue = DayOfWeek.SATURDAY;
        final String strValue = expectedValue.toString();
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final DayOfWeek result = config.getParameter(TEST_KEY, DayOfWeek.class);
        
        assertNotNull(result);
        assertSame(expectedValue, result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code URL} target type.
     * 
     * @throws MalformedURLException Not thrown
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetURL()
    throws MalformedURLException, ConfigException {
        final String strValue = "http://projects.orne.dev/some/path";
        final URL expectedValue = new URL(strValue);
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final URL result = config.getParameter(TEST_KEY, URL.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code URI} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetURI()
    throws ConfigException {
        final String strValue = "/some/path";
        final URI expectedValue = URI.create(strValue);
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final URI result = config.getParameter(TEST_KEY, URI.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code Character} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetChar()
    throws ConfigException {
        final String strValue = "w";
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final Character result = config.getParameter(TEST_KEY, Character.class);
        
        assertNotNull(result);
        assertEquals('w', result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code Byte} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetByte()
    throws ConfigException {
        final Byte expectedValue = 123;
        final BigDecimal numberValue = BigDecimal.valueOf(expectedValue);
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getNumberParameter(TEST_KEY)).willReturn(numberValue);
        
        final Byte result = config.getParameter(TEST_KEY, Byte.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getNumberParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code Short} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetShort()
    throws ConfigException {
        final Short expectedValue = 123;
        final BigDecimal numberValue = BigDecimal.valueOf(expectedValue);
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getNumberParameter(TEST_KEY)).willReturn(numberValue);
        
        final Short result = config.getParameter(TEST_KEY, Short.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getNumberParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code Integer} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetInteger()
    throws ConfigException {
        final Integer expectedValue = 123;
        final BigDecimal numberValue = BigDecimal.valueOf(expectedValue);
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getNumberParameter(TEST_KEY)).willReturn(numberValue);
        
        final Integer result = config.getParameter(TEST_KEY, Integer.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getNumberParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code BigInteger} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBigInteger()
    throws ConfigException {
        final BigInteger expectedValue = BigInteger.valueOf(2467894425678L);
        final BigDecimal numberValue = new BigDecimal(expectedValue);
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getNumberParameter(TEST_KEY)).willReturn(numberValue);
        
        final BigInteger result = config.getParameter(TEST_KEY, BigInteger.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getNumberParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code Long} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetLong()
    throws ConfigException {
        final Long expectedValue = 123457L;
        final BigDecimal numberValue = BigDecimal.valueOf(expectedValue);
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getNumberParameter(TEST_KEY)).willReturn(numberValue);
        
        final Long result = config.getParameter(TEST_KEY, Long.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getNumberParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code Float} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetFloat()
    throws ConfigException {
        final Float expectedValue = 123.6f;
        final BigDecimal numberValue = BigDecimal.valueOf(expectedValue);
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getNumberParameter(TEST_KEY)).willReturn(numberValue);
        
        final Float result = config.getParameter(TEST_KEY, Float.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getNumberParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code Double} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetDouble()
    throws ConfigException {
        final Double expectedValue = 7534.33;
        final BigDecimal numberValue = BigDecimal.valueOf(expectedValue);
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getNumberParameter(TEST_KEY)).willReturn(numberValue);
        
        final Double result = config.getParameter(TEST_KEY, Double.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getNumberParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code BigDecimal} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetBigDecimal()
    throws ConfigException {
        final BigDecimal expectedValue = BigDecimal.valueOf(2467894425678.34568854);
        final BigDecimal numberValue = expectedValue;
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getNumberParameter(TEST_KEY)).willReturn(numberValue);
        
        final BigDecimal result = config.getParameter(TEST_KEY, BigDecimal.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getNumberParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code Instant} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetInstant()
    throws ConfigException {
        final Instant expectedValue = Instant.now();
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getInstantParameter(TEST_KEY)).willReturn(expectedValue);
        
        final Instant result = config.getParameter(TEST_KEY, Instant.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getInstantParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code Year} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetYear()
    throws ConfigException {
        final Year expectedValue = Year.now();
        final String strValue = expectedValue.toString();
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final Year result = config.getParameter(TEST_KEY, Year.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code YearMonth} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetYearMonth()
    throws ConfigException {
        final YearMonth expectedValue = YearMonth.now();
        final String strValue = expectedValue.toString();
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final YearMonth result = config.getParameter(TEST_KEY, YearMonth.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code LocalDate} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetLocalDate()
    throws ConfigException {
        final LocalDate expectedValue = LocalDate.now();
        final String strValue = expectedValue.format(DateTimeFormatter.ISO_DATE);
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final LocalDate result = config.getParameter(TEST_KEY, LocalDate.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code LocalTime} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetLocalTime()
    throws ConfigException {
        final LocalTime expectedValue = LocalTime.now();
        final String strValue = expectedValue.format(DateTimeFormatter.ISO_TIME);
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final LocalTime result = config.getParameter(TEST_KEY, LocalTime.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code LocalDateTime} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetLocalDateTime()
    throws ConfigException {
        final LocalDateTime expectedValue = LocalDateTime.now();
        final String strValue = expectedValue.format(DateTimeFormatter.ISO_DATE_TIME);
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final LocalDateTime result = config.getParameter(TEST_KEY, LocalDateTime.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code ZoneOffset} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetZoneOffset()
    throws ConfigException {
        final ZoneOffset expectedValue = ZoneOffset.ofHours(3);
        final String strValue = expectedValue.getId();
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final ZoneOffset result = config.getParameter(TEST_KEY, ZoneOffset.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code Period} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetPeriod()
    throws ConfigException {
        final Period expectedValue = Period.of(1, 2, 3);
        final String strValue = expectedValue.toString();
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final Period result = config.getParameter(TEST_KEY, Period.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code Duration} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetDuration()
    throws ConfigException {
        final Duration expectedValue = Duration.ofDays(3).plusHours(10);
        final String strValue = expectedValue.toString();
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final Duration result = config.getParameter(TEST_KEY, Duration.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code Date} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetDate()
    throws ConfigException {
        final Instant instantValue = Instant.now();
        final Date expectedValue = Date.from(instantValue);
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getInstantParameter(TEST_KEY)).willReturn(instantValue);
        
        final Date result = config.getParameter(TEST_KEY, Date.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getInstantParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code Calendar} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetCalendar()
    throws ConfigException {
        final Instant instantValue = Instant.now();
        final Calendar expectedValue = Calendar.getInstance();
        expectedValue.setTimeInMillis(instantValue.toEpochMilli());
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getInstantParameter(TEST_KEY)).willReturn(instantValue);
        
        final Calendar result = config.getParameter(TEST_KEY, Calendar.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getInstantParameter(TEST_KEY);
    }

    /**
     * Test method for {@link AbstractConfig#getParameter(String, Class)} with
     * {@code File} target type.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testGetFile()
    throws ConfigException {
        final String strValue = "/some/file/path";
        final File expectedValue = new File(strValue);
        final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
        given(config.getStringParameter(TEST_KEY)).willReturn(strValue);
        
        final File result = config.getParameter(TEST_KEY, File.class);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
        
        then(config).should(times(1)).getStringParameter(TEST_KEY);
    }
}
