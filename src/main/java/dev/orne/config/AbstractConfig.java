package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
	public boolean contains(
    		@NotBlank
			final String key) {
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
	protected abstract boolean containsParameter(
    		@NotBlank
			String key);

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public <T> T get(
    		@NotBlank
			final String key,
    		@NotNull
			final Class<T> type) {
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
	@Nullable
	protected <T> T getParameter(
    		@NotBlank
			final String key,
    		@NotNull
			final Class<T> type) {
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
	@Nullable
	private Enum<?> getEnum(
    		@NotBlank
			final Class<?> type,
			@Nullable
			final String name) {
		final Class<? extends Enum> enumType = (Class<? extends Enum>) type;
		return Enum.valueOf(enumType, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Boolean getBoolean(
    		@NotBlank
			final String key) {
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
	@Nullable
	protected abstract Boolean getBooleanParameter(
    		@NotBlank
			String key);

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public String getString(
    		@NotBlank
			final String key) {
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
	@Nullable
	protected abstract String getStringParameter(
    		@NotBlank
			String key);

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Number getNumber(
    		@NotBlank
			final String key) {
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
	protected abstract Number getNumberParameter(
    		@NotBlank
			String key);

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	public Instant getInstant(
    		@NotBlank
			final String key) {
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
	@Nullable
	protected abstract Instant getInstantParameter(
    		@NotBlank
			String key);
}
