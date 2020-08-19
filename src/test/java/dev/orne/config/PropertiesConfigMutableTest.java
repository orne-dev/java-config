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
import static org.mockito.Mockito.times;

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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code PropertiesConfig}'s MutableConfig implementation.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class PropertiesConfigMutableTest {

    private static final String TEST_KEY = "test.key";

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code null} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetNull()
    throws ConfigException {
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, null);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNull(config.getStringParameter(TEST_KEY));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code String} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetString()
    throws ConfigException {
        final String expectedValue = "some value";
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getStringParameter(TEST_KEY));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * empty {@code String} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetStringEmpty()
    throws ConfigException {
        final String expectedValue = "";
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getStringParameter(TEST_KEY));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code Locale} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetLocale()
    throws ConfigException {
        final Locale expectedValue = Locale.ENGLISH;
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, Locale.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code Enum} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetEnum()
    throws ConfigException {
        final DayOfWeek expectedValue = DayOfWeek.SATURDAY;
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, DayOfWeek.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code String} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetBooleanTrue()
    throws ConfigException {
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, true);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals("true", config.getStringParameter(TEST_KEY));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code String} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetBooleanFalse()
    throws ConfigException {
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, false);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals("false", config.getStringParameter(TEST_KEY));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code Byte} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetByte()
    throws ConfigException {
        final Byte expectedValue = Byte.MAX_VALUE;
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, Byte.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code Short} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetShort()
    throws ConfigException {
        final Short expectedValue = Short.MIN_VALUE;
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, Short.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code Integer} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetInteger()
    throws ConfigException {
        final Integer expectedValue = Integer.MAX_VALUE;
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, Integer.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code Long} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetLong()
    throws ConfigException {
        final Long expectedValue = Long.MIN_VALUE;
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, Long.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code String} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetFloat()
    throws ConfigException {
        final Float expectedValue = Float.MAX_VALUE;
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, Float.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code Double} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetDouble()
    throws ConfigException {
        final Double expectedValue = Double.MIN_VALUE;
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, Double.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code BigInteger} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetBigInteger()
    throws ConfigException {
        final BigInteger expectedValue = BigInteger.valueOf(Long.MAX_VALUE).pow(2);
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, BigInteger.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code String} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetBigDecimal()
    throws ConfigException {
        final BigDecimal expectedValue = BigDecimal.valueOf(Double.MIN_VALUE).pow(3);
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, BigDecimal.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code Instant} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetInstant()
    throws ConfigException {
        final Instant expectedValue = Instant.now();
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, Instant.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code Year} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetYear()
    throws ConfigException {
        final Year expectedValue = Year.now();
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, Year.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code YearMonth} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetYearMonth()
    throws ConfigException {
        final YearMonth expectedValue = YearMonth.now();
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, YearMonth.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code LocalDate} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetLocalDate()
    throws ConfigException {
        final LocalDate expectedValue = LocalDate.now();
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, LocalDate.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code LocalTime} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetLocalTime()
    throws ConfigException {
        final LocalTime expectedValue = LocalTime.now();
        final String expectedStrValue = expectedValue.format(
                DateTimeFormatter.ISO_LOCAL_TIME);
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedStrValue, config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, LocalTime.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code LocalDateTime} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetLocalDateTime()
    throws ConfigException {
        final LocalDateTime expectedValue = LocalDateTime.now();
        final String expectedStrValue = expectedValue.format(
                DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedStrValue, config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, LocalDateTime.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code ZoneOffset} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetZoneOffset()
    throws ConfigException {
        final ZoneOffset expectedValue = ZoneOffset.ofHours(2);
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, ZoneOffset.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code Duration} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetDuration()
    throws ConfigException {
        final Duration expectedValue = Duration.ofDays(2).plusHours(4);
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, Duration.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code Period} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetPeriod()
    throws ConfigException {
        final Period expectedValue = Period.of(1, 2, 3);
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, Period.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code Date} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetDate()
    throws ConfigException {
        final Date expectedValue = new Date();
        final String expectedStrValue = DateTimeFormatter.ISO_INSTANT.format(
                expectedValue.toInstant());
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedStrValue, config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, Date.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code Calendar} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetCalendar()
    throws ConfigException {
        final ZonedDateTime zdt = ZonedDateTime.now();
        final GregorianCalendar expectedValue = GregorianCalendar.from(zdt);
        final String expectedStrValue = DateTimeFormatter.ISO_ZONED_DATE_TIME.format(
                zdt);
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedStrValue, config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, Calendar.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code URL} value.
     * @throws MalformedURLException Not thrown
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetURL()
    throws MalformedURLException, ConfigException {
        final URL expectedValue = new URL("http://projects.orne.dev/some/path");
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, URL.class));
    }

    /**
     * Test method for {@link PropertiesConfig#set(String, Object)} with
     * {@code URI} value.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetURI()
    throws ConfigException {
        final URI expectedValue = URI.create("/some/path");
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, expectedValue);
        assertTrue(config.containsParameter(TEST_KEY));
        assertNotNull(config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
        assertEquals(expectedValue, config.getParameter(TEST_KEY, URI.class));
    }

    /**
     * Test method for {@link PropertiesConfig#remove(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testRemove()
    throws ConfigException {
        final PropertiesConfig config = new PropertiesConfig();
        config.set(TEST_KEY, "to be deleted");
        config.remove(TEST_KEY);
        assertFalse(config.containsParameter(TEST_KEY));
        assertNull(config.getStringParameter(TEST_KEY));
    }

    /**
     * Test method for {@link PropertiesConfig#setRawValue(String, String)}
     * when value is null.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSetRawValueNull()
    throws ConfigException {
        final PropertiesConfig realConfig = new PropertiesConfig();
        final PropertiesConfig config = spy(realConfig);
        config.set(TEST_KEY, "to be deleted");
        config.setRawValue(TEST_KEY, null);
        
        then(config).should(times(1)).remove(TEST_KEY);
        assertFalse(config.containsParameter(TEST_KEY));
        assertNull(config.getStringParameter(TEST_KEY));
    }
}
