/**
 * 
 */
package dev.orne.config;

import java.net.URI;
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

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.LocaleUtils;

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
	public boolean contains(final String key) {
		boolean found = containsParameter(key);
		if (!found && this instanceof HierarchicalConfig) {
			final Config parent = ((HierarchicalConfig) this).getParent();
			if (parent != null) {
				found = parent.contains(key);
			}
		}
		return found;
	}

	/**
	 * Returns {@code true} if the parameter with the key passed as argument
	 * has been configured in this instance.
	 * 
	 * @param key The key of the configuration parameter
	 * @return Returns {@code true} if the parameter has been configured
	 */
	protected abstract boolean containsParameter(String key);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T get(final String key, final Class<T> type) {
		T value = null;
		if (containsParameter(key)) {
			value = getParameter(key, type);
		} else if (this instanceof HierarchicalConfig) {
			final Config parent = ((HierarchicalConfig) this).getParent();
			if (parent != null) {
				value = parent.get(key, type);
			}
		}
		return value;
	}

	/**
	 * Returns the value of the configuration parameter configured in this
	 * instance.
	 * 
	 * @param key The key of the configuration parameter
	 * @param type The target type of the parameter
	 * @return The configuration parameter value converted to the target type
	 */
	protected <T> T getParameter(final String key, final Class<T> type) {
		final T result;
    	if (String.class.equals(type)) {
    		result = type.cast(getStringParameter(key));
    	} else if (Boolean.class.equals(type)) {
    		result = type.cast(getBooleanParameter(key));
    	} else if (Number.class.isAssignableFrom(type)) {
    		result = type.cast(ConvertUtils.convert(getNumberParameter(key), type));
    	} else if (type.isEnum()) {
    		result = type.cast(getEnum(type, getStringParameter(key)));
    	} else if (Locale.class.equals(type)) {
    		result = type.cast(LocaleUtils.toLocale(getStringParameter(key)));
    	} else if (Instant.class.equals(type)) {
    		result = type.cast(getInstantParameter(key));
    	} else if (Year.class.equals(type)) {
    		result = type.cast(Year.parse(getStringParameter(key)));
    	} else if (YearMonth.class.equals(type)) {
    		result = type.cast(YearMonth.parse(getStringParameter(key)));
    	} else if (LocalDate.class.equals(type)) {
    		result = type.cast(LocalDate.parse(getStringParameter(key), DateTimeFormatter.ISO_DATE));
    	} else if (LocalTime.class.equals(type)) {
    		result = type.cast(LocalTime.parse(getStringParameter(key), DateTimeFormatter.ISO_TIME));
    	} else if (LocalDateTime.class.equals(type)) {
    		result = type.cast(LocalDateTime.parse(getStringParameter(key), DateTimeFormatter.ISO_DATE_TIME));
    	} else if (ZoneOffset.class.equals(type)) {
    		result = type.cast(ZoneOffset.of(getStringParameter(key)));
    	} else if (Duration.class.equals(type)) {
    		result = type.cast(Duration.parse(getStringParameter(key)));
    	} else if (Period.class.equals(type)) {
    		result = type.cast(Period.parse(getStringParameter(key)));
    	} else if (Date.class.equals(type)) {
    		result = type.cast(Date.from(getInstantParameter(key)));
    	} else if (Calendar.class.equals(type)) {
    		final Calendar calendar = Calendar.getInstance();
    		calendar.setTimeInMillis(getInstantParameter(key).toEpochMilli());
    		result = type.cast(calendar);
    	} else if (URI.class.equals(type)) {
    		result = type.cast(URI.create(getStringParameter(key)));
    	} else {
    		result = type.cast(ConvertUtils.convert(getStringParameter(key), type));
    	}
    	return result;
	}

	/**
	 * Returns the constant of the enumeration type for the name passed as
	 * argument.
	 * 
	 * @param type The enumeration type. Must be a subcass of {¢ode Enum}.
	 * @param name The name of the requested constant
	 * @return The constant with the requested name
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Enum<?> getEnum(final Class<?> type, final String name) {
		final Class<? extends Enum> enumType = (Class<? extends Enum>) type;
		return Enum.valueOf(enumType, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getBoolean(final String key) {
		Boolean value = null;
		if (containsParameter(key)) {
			value = getBooleanParameter(key);
		} else if (this instanceof HierarchicalConfig) {
			final Config parent = ((HierarchicalConfig) this).getParent();
			if (parent != null) {
				value = parent.getBoolean(key);
			}
		}
		return value;
	}

	/**
	 * Returns the value of the configuration parameter configured in this
	 * instance as {@code Boolean}.
	 * 
	 * @param key The key of the configuration parameter
	 * @return The configuration parameter value as {@code Boolean}
	 */
	protected abstract Boolean getBooleanParameter(String key);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getString(final String key) {
		String value = null;
		if (containsParameter(key)) {
			value = getStringParameter(key);
		} else if (this instanceof HierarchicalConfig) {
			final Config parent = ((HierarchicalConfig) this).getParent();
			if (parent != null) {
				value = parent.getString(key);
			}
		}
		return value;
	}

	/**
	 * Returns the value of the configuration parameter configured in this
	 * instance as {@code String}.
	 * 
	 * @param key The key of the configuration parameter
	 * @return The configuration parameter value as {@code String}
	 */
	protected abstract String getStringParameter(String key);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Number getNumber(final String key) {
		Number value = null;
		if (containsParameter(key)) {
			value = getNumberParameter(key);
		} else if (this instanceof HierarchicalConfig) {
			final Config parent = ((HierarchicalConfig) this).getParent();
			if (parent != null) {
				value = parent.getNumber(key);
			}
		}
		return value;
	}

	/**
	 * Returns the value of the configuration parameter configured in this
	 * instance as {@code Number}.
	 * 
	 * @param key The key of the configuration parameter
	 * @return The configuration parameter value as {@code Number}
	 */
	protected abstract Number getNumberParameter(String key);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Instant getInstant(final String key) {
		Instant value = null;
		if (containsParameter(key)) {
			value = getInstantParameter(key);
		} else if (this instanceof HierarchicalConfig) {
			final Config parent = ((HierarchicalConfig) this).getParent();
			if (parent != null) {
				value = parent.getInstant(key);
			}
		}
		return value;
	}

	/**
	 * Returns the value of the configuration parameter configured in this
	 * instance as {@code Instant}.
	 * 
	 * @param key The key of the configuration parameter
	 * @return The configuration parameter value as {@code Instant}
	 */
	protected abstract Instant getInstantParameter(String key);
}
