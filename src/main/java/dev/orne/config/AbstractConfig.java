/**
 * 
 */
package dev.orne.config;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.beanutils.ConvertUtils;

/**
 * Basic abstract implementation of {@code Config}. Provides basic
 * types support to calls to {@link #get(String, Class)}.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 */
public abstract class AbstractConfig
implements Config {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T get(final String key, final Class<T> type) {
		final T result;
    	if (String.class.equals(type)) {
    		result = type.cast(getString(key));
    	} else if (Boolean.class.equals(type)) {
    		result = type.cast(getBoolean(key));
    	} else if (Number.class.isAssignableFrom(type)) {
    		result = type.cast(ConvertUtils.convert(getNumber(key), type));
    	} else if (Instant.class.equals(type)) {
    		result = type.cast(getInstant(key));
    	} else if (LocalDate.class.equals(type)) {
    		result = type.cast(LocalDate.parse(getString(key), DateTimeFormatter.ISO_DATE));
    	} else if (LocalTime.class.equals(type)) {
    		result = type.cast(LocalTime.parse(getString(key), DateTimeFormatter.ISO_TIME));
    	} else if (LocalDateTime.class.equals(type)) {
    		result = type.cast(LocalDateTime.parse(getString(key), DateTimeFormatter.ISO_DATE_TIME));
    	} else if (Date.class.equals(type)) {
    		result = type.cast(Date.from(getInstant(key)));
    	} else if (Calendar.class.equals(type)) {
    		final Calendar calendar = Calendar.getInstance();
    		calendar.setTimeInMillis(getInstant(key).toEpochMilli());
    		result = type.cast(calendar);
    	} else if (URI.class.equals(type)) {
    		result = type.cast(URI.create(getString(key)));
    	} else {
    		result = type.cast(ConvertUtils.convert(getString(key), type));
    	}
    	return result;
	}
}
