package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2020 Orne Developments
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

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.beans.Identity;
import dev.orne.beans.TokenIdentity;
import dev.orne.beans.converters.EnumConverter;

/**
 * Unit tests for configuration value conversion based on {@code ConvertUtils}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 */
@Tag("ut")
class ConfigValueConversionTest {

    private static final String NULL_ERR = "Unexpected null conversion result";
    private static final String NON_NULL_ERR = "Unexpected non null conversion result";
    private static final String CLASS_ERR = "Unexpected conversion result class";
    private static final String VALUE_ERR = "Unexpected conversion result class";

    private static ConvertUtilsBean converter;

    @BeforeAll
    public static void createConverter() {
        converter = AbstractConfig.createDefaultConverter();
        converter.register(new EnumConverter<>(TestEnum.class, null), TestEnum.class);
    }

    @Test
    void testStringToBoolean() {
        assertNullConversionValue((String) null, Boolean.class);
        assertConversionValue("true", Boolean.class, true);
        assertConversionValue("TRUE", Boolean.class, true);
        assertConversionValue("false", Boolean.class, false);
        assertConversionValue("FALSE", Boolean.class, false);
    }

    @Test
    void testBooleanToString() {
        assertNullConversionValue((Boolean) null, String.class);
        assertConversionValue(true, String.class, "true");
        assertConversionValue(Boolean.TRUE, String.class, "true");
        assertConversionValue(false, String.class, "false");
        assertConversionValue(Boolean.FALSE, String.class, "false");
    }

    @Test
    void testStringToChar() {
        assertNullConversionValue((String) null, Character.class);
        assertConversionValue("w", Character.class, 'w');
    }

    @Test
    void testCharToString() {
        assertConversionValue('w', String.class, "w");
    }

    @Test
    void testStringToByte() {
        assertNullConversionValue((String) null, Byte.class);
        assertConversionValue("0", Byte.class, Byte.valueOf("0"));
        assertConversionValue("61", Byte.class, Byte.valueOf("61"));
        assertConversionValue(String.valueOf(Byte.MAX_VALUE),
                Byte.class,
                Byte.MAX_VALUE);
        assertConversionValue(String.valueOf(Byte.MIN_VALUE),
                Byte.class,
                Byte.MIN_VALUE);
    }

    @Test
    void testStringToShort() {
        assertNullConversionValue((String) null, Short.class);
        assertConversionValue("0", Short.class, Short.valueOf("0"));
        assertConversionValue("1554", Short.class, Short.valueOf("1554"));
        assertConversionValue(String.valueOf(Short.MAX_VALUE),
                Short.class,
                Short.MAX_VALUE);
        assertConversionValue(String.valueOf(Short.MIN_VALUE),
                Short.class,
                Short.MIN_VALUE);
    }

    @Test
    void testStringToInteger() {
        assertNullConversionValue((String) null, Integer.class);
        assertConversionValue("0", Integer.class, 0);
        assertConversionValue("1554", Integer.class, 1554);
        assertConversionValue(String.valueOf(Integer.MAX_VALUE),
                Integer.class,
                Integer.MAX_VALUE);
        assertConversionValue(String.valueOf(Integer.MIN_VALUE),
                Integer.class,
                Integer.MIN_VALUE);
    }

    @Test
    void testStringToLong() {
        assertNullConversionValue((String) null, Long.class);
        assertConversionValue("0", Long.class, 0l);
        assertConversionValue("1554", Long.class, 1554l);
        assertConversionValue("15546465363", Long.class, 15546465363l);
        assertConversionValue(String.valueOf(Long.MAX_VALUE),
                Long.class,
                Long.MAX_VALUE);
        assertConversionValue(String.valueOf(Long.MIN_VALUE),
                Long.class,
                Long.MIN_VALUE);
    }

    @Test
    void testStringToFloat() {
        assertNullConversionValue((String) null, Float.class);
        assertConversionValue("0", Float.class, 0f);
        assertConversionValue("1554", Float.class, 1554f);
        assertConversionValue("15546465363", Float.class, 15546465363f);
        assertConversionValue("15546.465363", Float.class, 15546.465363f);
        assertConversionValue(String.valueOf(Float.MAX_VALUE),
                Float.class,
                Float.MAX_VALUE);
        assertConversionValue(String.valueOf(Float.MIN_VALUE),
                Float.class,
                Float.MIN_VALUE);
    }

    @Test
    void testStringToDouble() {
        assertNullConversionValue((String) null, Double.class);
        assertConversionValue("0", Double.class, 0d);
        assertConversionValue("1554", Double.class, 1554d);
        assertConversionValue("15546465363", Double.class, 15546465363d);
        assertConversionValue("15546.465363", Double.class, 15546.465363d);
        assertConversionValue("1.554454455444E12",
                Double.class,
                1.554454455444E12d);
        assertConversionValue(String.valueOf(Double.MAX_VALUE),
                Double.class,
                Double.MAX_VALUE);
        assertConversionValue(String.valueOf(Double.MIN_VALUE),
                Double.class,
                Double.MIN_VALUE);
    }

    @Test
    void testStringToBigInteger() {
        assertNullConversionValue((String) null, BigInteger.class);
        assertConversionValue("0", BigInteger.class, BigInteger.ZERO);
        assertConversionValue("1554", BigInteger.class, BigInteger.valueOf(1554));
        assertConversionValue("15546465363",
                BigInteger.class,
                BigInteger.valueOf(15546465363l));
        assertConversionValue(String.valueOf(Long.MAX_VALUE),
                BigInteger.class,
                BigInteger.valueOf(Long.MAX_VALUE));
        assertConversionValue(String.valueOf(Long.MIN_VALUE),
                BigInteger.class,
                BigInteger.valueOf(Long.MIN_VALUE));
    }

    @Test
    void testStringToBigDecimal() {
        assertNullConversionValue((String) null, BigDecimal.class);
        assertConversionValue("0", BigDecimal.class, BigDecimal.ZERO);
        assertConversionValue("1554",
                BigDecimal.class,
                BigDecimal.valueOf(1554));
        assertConversionValue("15546465363",
                BigDecimal.class,
                BigDecimal.valueOf(15546465363d));
        assertConversionValue("15546.465363",
                BigDecimal.class,
                BigDecimal.valueOf(15546.465363d));
        assertConversionValue(String.valueOf(Double.MAX_VALUE),
                BigDecimal.class,
                BigDecimal.valueOf(Double.MAX_VALUE));
        assertConversionValue(String.valueOf(Double.MIN_VALUE),
                BigDecimal.class,
                BigDecimal.valueOf(Double.MIN_VALUE));
        assertConversionValue("1545555446.4554455E-18",
                BigDecimal.class,
                new BigDecimal("1545555446.4554455E-18"));
    }

    @Test
    void testNumberToString() {
        assertNullConversionValue((Number) null, String.class);
        assertConversionValue(0, String.class, "0");
        assertConversionValue(BigDecimal.ZERO, String.class, "0");
        assertConversionValue(BigInteger.ZERO, String.class, "0");
        assertConversionValue((byte) 15, String.class, "15");
        assertConversionValue((short) 114, String.class, "114");
        assertConversionValue(155445, String.class, "155445");
        assertConversionValue(155448848555l, String.class, "155448848555");
        assertConversionValue(155445.14, String.class, "155445.14");
        assertConversionValue(
                1.554454455444E12d,
                String.class,
                "1.554454455444E12");
        assertConversionValue(
                new BigInteger("54889745456464646546464444546465464654654646464654"),
                String.class,
                "54889745456464646546464444546465464654654646464654");
        assertConversionValue(
                new BigDecimal("1554454455444.5445541455445E36"),
                String.class,
                "1.5544544554445445541455445E+48");
    }

    @Test
    void testStringToEnum() {
        assertNullConversionValue((String) null, TestEnum.class);
        assertConversionValue("VALUEA", TestEnum.class, TestEnum.VALUEA);
    }

    @Test
    void testEnumToString() {
        assertNullConversionValue((TestEnum) null, String.class);
        assertConversionValue(TestEnum.VALUEA, String.class, "VALUEA");
    }

    @Test
    void testStringToLocale() {
        assertNullConversionValue((String) null, Locale.class);
        assertConversionValue("en", Locale.class, Locale.ENGLISH);
        assertConversionValue("fr-CA", Locale.class, Locale.CANADA_FRENCH);
    }

    @Test
    void testLocaleToString() {
        assertNullConversionValue((Locale) null, String.class);
        assertConversionValue(Locale.ENGLISH, String.class, "en");
        assertConversionValue(Locale.CANADA_FRENCH, String.class, "fr-CA");
    }

    @Test
    void testStringToURL() throws MalformedURLException {
        assertNullConversionValue((String) null, URL.class);
        final String strValue = "http://projects.orne.dev/some/path";
        final URL expectedValue = new URL(strValue);
        assertConversionValue(strValue, URL.class, expectedValue);
    }

    @Test
    void testURLToString() throws MalformedURLException {
        assertNullConversionValue((URL) null, String.class);
        final String expectedValue = "http://projects.orne.dev/some/path";
        final URL value = new URL(expectedValue);
        assertConversionValue(value, String.class, expectedValue);
    }

    @Test
    void testStringToURI() {
        assertNullConversionValue((String) null, URI.class);
        final String strValue = "/some/path";
        final URI expectedValue = URI.create(strValue);
        assertConversionValue(strValue, URI.class, expectedValue);
    }

    @Test
    void testURIToString() {
        assertNullConversionValue((URI) null, String.class);
        final String expectedValue = "/some/path";
        final URI value = URI.create(expectedValue);
        assertConversionValue(value, String.class, expectedValue);
    }

    @Test
    void testStringToFile() {
        assertNullConversionValue((String) null, URI.class);
        final String strValue = "/some/path";
        final File expectedValue = new File(strValue);
        assertConversionValue(strValue, File.class, expectedValue);
    }

    @Test
    void testFileToString() {
        assertNullConversionValue((File) null, String.class);
        final String expectedValue =
                File.separatorChar + "some" +
                File.separatorChar  + "path";
        final File value = new File("/some/path");
        assertConversionValue(value, String.class, expectedValue);
    }

    @Test
    void testStringToInstant() {
        assertNullConversionValue((String) null, Instant.class);
        assertConversionValue(
                "2007-12-03T10:15:30.00Z",
                Instant.class,
                Instant.parse("2007-12-03T10:15:30.00Z"));
        assertConversionValue(
                "2007-12-03T10:15:30Z",
                Instant.class,
                Instant.parse("2007-12-03T10:15:30.00Z"));
        assertConversionValue(
                "2007-12-03T10:15:30.405Z",
                Instant.class,
                Instant.parse("2007-12-03T10:15:30.405Z"));
    }

    @Test
    void testInstantToString() {
        assertNullConversionValue((Instant) null, String.class);
        assertConversionValue(
                Instant.parse("2007-12-03T10:15:30.00Z"),
                String.class,
                "2007-12-03T10:15:30Z");
        assertConversionValue(
                Instant.parse("2007-12-03T10:15:30.40Z"),
                String.class,
                "2007-12-03T10:15:30.400Z");
    }

    @Test
    void testStringToDate() {
        assertNullConversionValue((String) null, Date.class);
        assertConversionValue("2007-12-03T10:15:30.00Z", Date.class,
                Date.from(Instant.parse("2007-12-03T10:15:30.00Z")));
        assertConversionValue("2007-12-03T10:15:30Z", Date.class,
                Date.from(Instant.parse("2007-12-03T10:15:30.00Z")));
        assertConversionValue("2007-12-03T10:15:30.405Z", Date.class,
                Date.from(Instant.parse("2007-12-03T10:15:30.405Z")));
    }

    @Test
    void testDateToString() {
        assertNullConversionValue((Instant) null, String.class);
        assertConversionValue(
                Date.from(Instant.parse("2007-12-03T10:15:30.00Z")),
                String.class,
                "2007-12-03T10:15:30Z");
        assertConversionValue(
                Date.from(Instant.parse("2007-12-03T10:15:30.40Z")),
                String.class,
                "2007-12-03T10:15:30.400Z");
    }

    @Test
    void testStringToCalendar() {
        assertNullConversionValue((String) null, Calendar.class);
        assertConversionValue("2007-12-03T10:15:30.00Z", Calendar.class,
                GregorianCalendar.from(ZonedDateTime.parse("2007-12-03T10:15:30.00Z")));
        assertConversionValue("2007-12-03T10:15:30Z", Calendar.class,
                GregorianCalendar.from(ZonedDateTime.parse("2007-12-03T10:15:30.00Z")));
        assertConversionValue("2007-12-03T10:15:30.405Z", Calendar.class,
                GregorianCalendar.from(ZonedDateTime.parse("2007-12-03T10:15:30.405Z")));
    }

    @Test
    void testCalendarToString() {
        assertNullConversionValue((Calendar) null, String.class);
        assertConversionValue(
                GregorianCalendar.from(ZonedDateTime.parse("2007-12-03T10:15:30.00Z")),
                String.class,
                "2007-12-03T10:15:30Z[UTC]");
        assertConversionValue(
                GregorianCalendar.from(ZonedDateTime.parse("2007-12-03T10:15:30.40Z")),
                String.class,
                "2007-12-03T10:15:30.4Z[UTC]");
    }

    @Test
    void testStringToLocalDateTime() {
        assertNullConversionValue((String) null, LocalDateTime.class);
        assertConversionValue(
                "1500-03-12T15:47:22",
                LocalDateTime.class,
                LocalDateTime.of(1500,3,12,15,47,22));
        assertConversionValue(
                "-0300-05-02T12:00:00",
                LocalDateTime.class,
                LocalDateTime.of(-300,5,2,12,0,0));
        assertConversionValue(
                "4800-08-28T03:02:01",
                LocalDateTime.class,
                LocalDateTime.of(4800,8,28,3,2,1));
    }

    @Test
    void testLocalDateTimeToString() {
        assertNullConversionValue((LocalDateTime) null, String.class);
        assertConversionValue(
                LocalDateTime.of(1500,3,12,15,47,22),
                String.class,
                "1500-03-12T15:47:22");
        assertConversionValue(
                LocalDateTime.of(-300,5,2,12,0,0),
                String.class,
                "-0300-05-02T12:00:00");
        assertConversionValue(
                LocalDateTime.of(4800,8,28,3,2,1),
                String.class,
                "4800-08-28T03:02:01");
    }

    @Test
    void testStringToLocalDate() {
        assertNullConversionValue((String) null, LocalDate.class);
        assertConversionValue("1500-03-12", LocalDate.class, LocalDate.of(1500,3,12));
        assertConversionValue("-0300-05-02", LocalDate.class, LocalDate.of(-300,5,2));
        assertConversionValue("4800-08-28", LocalDate.class, LocalDate.of(4800,8,28));
    }

    @Test
    void testLocalDateToString() {
        assertNullConversionValue((LocalDate) null, String.class);
        assertConversionValue(LocalDate.of(1500,3,12), String.class, "1500-03-12");
        assertConversionValue(LocalDate.of(-300,5,2), String.class, "-0300-05-02");
        assertConversionValue(LocalDate.of(4800,8,28), String.class, "4800-08-28");
    }

    @Test
    void testStringToYearMonth() {
        assertNullConversionValue((String) null, Year.class);
        assertConversionValue("1500-03", YearMonth.class, YearMonth.of(1500,3));
        assertConversionValue("-0300-05", YearMonth.class, YearMonth.of(-300,5));
        assertConversionValue("4800-08", YearMonth.class, YearMonth.of(4800,8));
    }

    @Test
    void testMonthYearToString() {
        assertNullConversionValue((YearMonth) null, String.class);
        assertConversionValue(YearMonth.of(1500,3), String.class, "1500-03");
        assertConversionValue(YearMonth.of(-300,5), String.class, "-0300-05");
        assertConversionValue(YearMonth.of(4800,8), String.class, "4800-08");
    }

    @Test
    void testStringToYear() {
        assertNullConversionValue((String) null, Year.class);
        assertConversionValue("1500", Year.class, Year.parse("1500"));
        assertConversionValue("-0300", Year.class, Year.parse("-0300"));
        assertConversionValue("4800", Year.class, Year.parse("4800"));
    }

    @Test
    void testYearToString() {
        assertNullConversionValue((Year) null, String.class);
        assertConversionValue(Year.parse("1500"), String.class, "1500");
        assertConversionValue(Year.parse("-0400"), String.class, "-0400");
        assertConversionValue(Year.parse("4800"), String.class, "4800");
    }

    @Test
    void testStringToLocalTime() {
        assertNullConversionValue((String) null, LocalTime.class);
        assertConversionValue("15:47:22", LocalTime.class, LocalTime.of(15,47,22));
        assertConversionValue("12:00", LocalTime.class, LocalTime.of(12,0,0));
        assertConversionValue("03:02:01.1500", LocalTime.class, LocalTime.of(3,2,1,150000000));
    }

    @Test
    void testLocalTimeToString() {
        assertNullConversionValue((LocalTime) null, String.class);
        assertConversionValue(LocalTime.of(15,47,22), String.class, "15:47:22");
        assertConversionValue(LocalTime.of(12,0,0), String.class, "12:00:00");
        assertConversionValue(LocalTime.of(3,2,1,1500), String.class, "03:02:01.0000015");
    }

    @Test
    void testStringToZoneOffset() {
        assertNullConversionValue((String) null, URI.class);
        final ZoneOffset expectedValue = ZoneOffset.ofHours(3);
        final String strValue = expectedValue.toString();
        assertConversionValue(strValue, ZoneOffset.class, expectedValue);
    }

    @Test
    void testZoneOffsetToString() {
        assertNullConversionValue((ZoneOffset) null, String.class);
        final ZoneOffset value = ZoneOffset.ofHours(3);
        final String expectedValue = value.toString();
        assertConversionValue(value, String.class, expectedValue);
    }

    @Test
    void testStringToPeriod() {
        assertNullConversionValue((String) null, URI.class);
        final Period expectedValue = Period.of(1, 2, 3);
        final String strValue = expectedValue.toString();
        assertConversionValue(strValue, Period.class, expectedValue);
    }

    @Test
    void testPeriodToString() {
        assertNullConversionValue((Period) null, String.class);
        final Period value = Period.of(1, 2, 3);
        final String expectedValue = value.toString();
        assertConversionValue(value, String.class, expectedValue);
    }

    @Test
    void testStringToDuration() {
        assertNullConversionValue((String) null, URI.class);
        final Duration expectedValue = Duration.ofDays(3).plusHours(10);
        final String strValue = expectedValue.toString();
        assertConversionValue(strValue, Duration.class, expectedValue);
    }

    @Test
    void testDurationToString() {
        assertNullConversionValue((Duration) null, String.class);
        final Duration value = Duration.ofDays(3).plusHours(10);
        final String expectedValue = value.toString();
        assertConversionValue(value, String.class, expectedValue);
    }

    @Test
    void testStringToIdentity() {
        assertNullConversionValue((String) null, Identity.class);
        assertConversionValue("mock token", Identity.class, TokenIdentity.fromToken("mock token"));
    }

    @Test
    void testIdentityToString() {
        assertNullConversionValue((Identity) null, String.class);
        assertConversionValue(TokenIdentity.fromToken("mock token"), String.class, "mock token");
    }

    private static <T> void assertNullConversionValue(
            final Object value,
            final Class<T> targetType) {
        final Object result = converter.convert(value, targetType);
        assertNull(result, NON_NULL_ERR);
    }

    private static <T> void assertConversionValue(
            final Object value,
            final Class<T> targetType,
            final T expectedResult) {
        final Object result = converter.convert(value, targetType);
        assertNotNull(result, NULL_ERR);
        if (!targetType.isAssignableFrom(result.getClass())) {
            assertEquals(targetType, result.getClass(), CLASS_ERR);
        }
        assertEquals(expectedResult, result, VALUE_ERR);
    }

    public static enum TestEnum {
        VALUEA,
        VALUEB,
        VALUEC;
    }
}
