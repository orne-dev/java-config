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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code PropertiesConfig}'s MutableConfig implementation.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 */
@Tag("ut")
class PropertiesConfigMutableTest {

	private static final String TEST_KEY = "test.key";

	/**
	 * Test method for {@link PropertiesConfig#set(String, Object)} with
	 * {@code null} value.
	 */
	@Test
	public void testSetNull() {
		final PropertiesConfig config = new PropertiesConfig();
		config.set(TEST_KEY, null);
		assertTrue(config.containsParameter(TEST_KEY));
		assertNull(config.getStringParameter(TEST_KEY));
	}

	/**
	 * Test method for {@link PropertiesConfig#set(String, Object)} with
	 * {@code String} value.
	 */
	@Test
	public void testSetString() {
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
	 */
	@Test
	public void testSetStringEmpty() {
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
	 */
	@Test
	public void testSetLocale() {
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
	 */
	@Test
	public void testSetEnum() {
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
	 */
	@Test
	public void testSetBooleanTrue() {
		final PropertiesConfig config = new PropertiesConfig();
		config.set(TEST_KEY, true);
		assertTrue(config.containsParameter(TEST_KEY));
		assertNotNull(config.getStringParameter(TEST_KEY));
		assertEquals("true", config.getStringParameter(TEST_KEY));
	}

	/**
	 * Test method for {@link PropertiesConfig#set(String, Object)} with
	 * {@code String} value.
	 */
	@Test
	public void testSetBooleanFalse() {
		final PropertiesConfig config = new PropertiesConfig();
		config.set(TEST_KEY, false);
		assertTrue(config.containsParameter(TEST_KEY));
		assertNotNull(config.getStringParameter(TEST_KEY));
		assertEquals("false", config.getStringParameter(TEST_KEY));
	}

	/**
	 * Test method for {@link PropertiesConfig#set(String, Object)} with
	 * {@code Byte} value.
	 */
	@Test
	public void testSetByte() {
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
	 */
	@Test
	public void testSetShort() {
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
	 */
	@Test
	public void testSetInteger() {
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
	 */
	@Test
	public void testSetLong() {
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
	 */
	@Test
	public void testSetFloat() {
		final Float expectedValue = Float.MAX_VALUE;
		final PropertiesConfig config = new PropertiesConfig();
		config.set(TEST_KEY, expectedValue);
		assertTrue(config.containsParameter(TEST_KEY));
		assertNotNull(config.getStringParameter(TEST_KEY));
		assertEquals(BigDecimal.valueOf(expectedValue).toString(), config.getStringParameter(TEST_KEY));
		assertEquals(expectedValue, config.getParameter(TEST_KEY, Float.class));
	}

	/**
	 * Test method for {@link PropertiesConfig#set(String, Object)} with
	 * {@code Double} value.
	 */
	@Test
	public void testSetDouble() {
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
	 */
	@Test
	public void testSetBigInteger() {
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
	 */
	@Test
	public void testSetBigDecimal() {
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
	 */
	@Test
	public void testSetInstant() {
		final Instant expectedValue = Instant.now();
		final PropertiesConfig config = new PropertiesConfig();
		config.set(TEST_KEY, expectedValue);
		assertTrue(config.containsParameter(TEST_KEY));
		assertNotNull(config.getStringParameter(TEST_KEY));
		assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
		assertEquals(expectedValue, config.getInstantParameter(TEST_KEY));
		assertEquals(expectedValue, config.getParameter(TEST_KEY, Instant.class));
	}

	/**
	 * Test method for {@link PropertiesConfig#set(String, Object)} with
	 * {@code Year} value.
	 */
	@Test
	public void testSetYear() {
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
	 */
	@Test
	public void testSetYearMonth() {
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
	 */
	@Test
	public void testSetLocalDate() {
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
	 */
	@Test
	public void testSetLocalTime() {
		final LocalTime expectedValue = LocalTime.now();
		final PropertiesConfig config = new PropertiesConfig();
		config.set(TEST_KEY, expectedValue);
		assertTrue(config.containsParameter(TEST_KEY));
		assertNotNull(config.getStringParameter(TEST_KEY));
		assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
		assertEquals(expectedValue, config.getParameter(TEST_KEY, LocalTime.class));
	}

	/**
	 * Test method for {@link PropertiesConfig#set(String, Object)} with
	 * {@code LocalDateTime} value.
	 */
	@Test
	public void testSetLocalDateTime() {
		final LocalDateTime expectedValue = LocalDateTime.now();
		final PropertiesConfig config = new PropertiesConfig();
		config.set(TEST_KEY, expectedValue);
		assertTrue(config.containsParameter(TEST_KEY));
		assertNotNull(config.getStringParameter(TEST_KEY));
		assertEquals(expectedValue.toString(), config.getStringParameter(TEST_KEY));
		assertEquals(expectedValue, config.getParameter(TEST_KEY, LocalDateTime.class));
	}

	/**
	 * Test method for {@link PropertiesConfig#set(String, Object)} with
	 * {@code ZoneOffset} value.
	 */
	@Test
	public void testSetZoneOffset() {
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
	 */
	@Test
	public void testSetDuration() {
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
	 */
	@Test
	public void testSetPeriod() {
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
	 */
	@Test
	public void testSetDate() {
		final Date expectedValue = new Date();
		final PropertiesConfig config = new PropertiesConfig();
		config.set(TEST_KEY, expectedValue);
		assertTrue(config.containsParameter(TEST_KEY));
		assertNotNull(config.getStringParameter(TEST_KEY));
		assertEquals(expectedValue.toInstant().toString(), config.getStringParameter(TEST_KEY));
		assertEquals(expectedValue, config.getParameter(TEST_KEY, Date.class));
	}

	/**
	 * Test method for {@link PropertiesConfig#set(String, Object)} with
	 * {@code Calendar} value.
	 */
	@Test
	public void testSetCalendar() {
		final Calendar expectedValue = Calendar.getInstance();
		final PropertiesConfig config = new PropertiesConfig();
		config.set(TEST_KEY, expectedValue);
		assertTrue(config.containsParameter(TEST_KEY));
		assertNotNull(config.getStringParameter(TEST_KEY));
		assertEquals(expectedValue.toInstant().toString(), config.getStringParameter(TEST_KEY));
		assertEquals(expectedValue, config.getParameter(TEST_KEY, Calendar.class));
	}

	/**
	 * Test method for {@link PropertiesConfig#set(String, Object)} with
	 * {@code URL} value.
	 * @throws MalformedURLException Not thrown
	 */
	@Test
	public void testSetURL()
	throws MalformedURLException {
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
	 */
	@Test
	public void testSetURI() {
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
	 */
	@Test
	public void testRemove() {
		final PropertiesConfig config = new PropertiesConfig();
		config.set(TEST_KEY, "to be deleted");
		config.remove(TEST_KEY);
		assertFalse(config.containsParameter(TEST_KEY));
		assertNull(config.getStringParameter(TEST_KEY));
	}
}
