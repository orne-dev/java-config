/**
 * 
 */
package dev.orne.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

/**
 * Unit tests for {@code AbstractConfig}.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 */
@Tag("ut")
class AbstractConfigTest {

	private static final String TEST_KEY = "test.key";

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code Boolean} target type.
	 */
	@Test
	public void testGetBoolean() {
		final Boolean boolValue = Boolean.TRUE;
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getBoolean(TEST_KEY)).willReturn(boolValue);
		
		final Boolean result = config.get(TEST_KEY, Boolean.class);
		
		assertNotNull(result);
		assertEquals(boolValue, result);
		
		then(config).should(times(1)).getBoolean(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code String} target type.
	 */
	@Test
	public void testGetString() {
		final String strValue = "test.value";
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getString(TEST_KEY)).willReturn(strValue);
		
		final String result = config.get(TEST_KEY, String.class);
		
		assertNotNull(result);
		assertEquals(strValue, result);
		
		then(config).should(times(1)).getString(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code URL} target type.
	 * 
	 * @throws MalformedURLException Not thrown
	 */
	@Test
	public void testGetURL()
	throws MalformedURLException {
		final String strValue = "http://projects.orne.dev/some/path";
		final URL expectedValue = new URL(strValue);
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getString(TEST_KEY)).willReturn(strValue);
		
		final URL result = config.get(TEST_KEY, URL.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getString(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code URI} target type.
	 */
	@Test
	public void testGetURI() {
		final String strValue = "/some/path";
		final URI expectedValue = URI.create(strValue);
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getString(TEST_KEY)).willReturn(strValue);
		
		final URI result = config.get(TEST_KEY, URI.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getString(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code Character} target type.
	 */
	@Test
	public void testGetChar() {
		final String strValue = "w";
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getString(TEST_KEY)).willReturn(strValue);
		
		final Character result = config.get(TEST_KEY, Character.class);
		
		assertNotNull(result);
		assertEquals('w', result);
		
		then(config).should(times(1)).getString(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code Byte} target type.
	 */
	@Test
	public void testGetByte() {
		final Byte expectedValue = 123;
		final BigDecimal numberValue = BigDecimal.valueOf(expectedValue);
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getNumber(TEST_KEY)).willReturn(numberValue);
		
		final Byte result = config.get(TEST_KEY, Byte.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getNumber(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code Short} target type.
	 */
	@Test
	public void testGetShort() {
		final Short expectedValue = 123;
		final BigDecimal numberValue = BigDecimal.valueOf(expectedValue);
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getNumber(TEST_KEY)).willReturn(numberValue);
		
		final Short result = config.get(TEST_KEY, Short.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getNumber(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code Integer} target type.
	 */
	@Test
	public void testGetInteger() {
		final Integer expectedValue = 123;
		final BigDecimal numberValue = BigDecimal.valueOf(expectedValue);
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getNumber(TEST_KEY)).willReturn(numberValue);
		
		final Integer result = config.get(TEST_KEY, Integer.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getNumber(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code BigInteger} target type.
	 */
	@Test
	public void testGetBigInteger() {
		final BigInteger expectedValue = BigInteger.valueOf(2467894425678L);
		final BigDecimal numberValue = new BigDecimal(expectedValue);
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getNumber(TEST_KEY)).willReturn(numberValue);
		
		final BigInteger result = config.get(TEST_KEY, BigInteger.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getNumber(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code Long} target type.
	 */
	@Test
	public void testGetLong() {
		final Long expectedValue = 123457L;
		final BigDecimal numberValue = BigDecimal.valueOf(expectedValue);
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getNumber(TEST_KEY)).willReturn(numberValue);
		
		final Long result = config.get(TEST_KEY, Long.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getNumber(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code Float} target type.
	 */
	@Test
	public void testGetFloat() {
		final Float expectedValue = 123.6f;
		final BigDecimal numberValue = BigDecimal.valueOf(expectedValue);
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getNumber(TEST_KEY)).willReturn(numberValue);
		
		final Float result = config.get(TEST_KEY, Float.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getNumber(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code Double} target type.
	 */
	@Test
	public void testGetDouble() {
		final Double expectedValue = 7534.33;
		final BigDecimal numberValue = BigDecimal.valueOf(expectedValue);
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getNumber(TEST_KEY)).willReturn(numberValue);
		
		final Double result = config.get(TEST_KEY, Double.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getNumber(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code BigDecimal} target type.
	 */
	@Test
	public void testGetBigDecimal() {
		final BigDecimal expectedValue = BigDecimal.valueOf(2467894425678.34568854);
		final BigDecimal numberValue = expectedValue;
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getNumber(TEST_KEY)).willReturn(numberValue);
		
		final BigDecimal result = config.get(TEST_KEY, BigDecimal.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getNumber(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code Instant} target type.
	 */
	@Test
	public void testGetInstant() {
		final Instant expectedValue = Instant.now();
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getInstant(TEST_KEY)).willReturn(expectedValue);
		
		final Instant result = config.get(TEST_KEY, Instant.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getInstant(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code LocalDate} target type.
	 */
	@Test
	public void testGetLocalDate() {
		final LocalDate expectedValue = LocalDate.now();
		final String strValue = expectedValue.format(DateTimeFormatter.ISO_DATE);
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getString(TEST_KEY)).willReturn(strValue);
		
		final LocalDate result = config.get(TEST_KEY, LocalDate.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getString(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code LocalTime} target type.
	 */
	@Test
	public void testGetLocalTime() {
		final LocalTime expectedValue = LocalTime.now();
		final String strValue = expectedValue.format(DateTimeFormatter.ISO_TIME);
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getString(TEST_KEY)).willReturn(strValue);
		
		final LocalTime result = config.get(TEST_KEY, LocalTime.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getString(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code LocalDateTime} target type.
	 */
	@Test
	public void testGetLocalDateTime() {
		final LocalDateTime expectedValue = LocalDateTime.now();
		final String strValue = expectedValue.format(DateTimeFormatter.ISO_DATE_TIME);
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getString(TEST_KEY)).willReturn(strValue);
		
		final LocalDateTime result = config.get(TEST_KEY, LocalDateTime.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getString(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code Date} target type.
	 */
	@Test
	public void testGetDate() {
		final Instant instantValue = Instant.now();
		final Date expectedValue = Date.from(instantValue);
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getInstant(TEST_KEY)).willReturn(instantValue);
		
		final Date result = config.get(TEST_KEY, Date.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getInstant(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code Calendar} target type.
	 */
	@Test
	public void testGetCalendar() {
		final Instant instantValue = Instant.now();
		final Calendar expectedValue = Calendar.getInstance();
		expectedValue.setTimeInMillis(instantValue.toEpochMilli());
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getInstant(TEST_KEY)).willReturn(instantValue);
		
		final Calendar result = config.get(TEST_KEY, Calendar.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getInstant(TEST_KEY);
	}

	/**
	 * Test method for {@link AbstractConfig#get(String, Class)} with
	 * {@code File} target type.
	 */
	@Test
	public void testGetFile() {
		final String strValue = "/some/file/path";
		final File expectedValue = new File(strValue);
		final AbstractConfig config = BDDMockito.spy(AbstractConfig.class);
		given(config.getString(TEST_KEY)).willReturn(strValue);
		
		final File result = config.get(TEST_KEY, File.class);
		
		assertNotNull(result);
		assertEquals(expectedValue, result);
		
		then(config).should(times(1)).getString(TEST_KEY);
	}
}
