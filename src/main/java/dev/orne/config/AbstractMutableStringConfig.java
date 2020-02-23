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

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;

/**
 * <p>Partial implementation of {@code MutableConfig} for implementations with
 * configuration values stored as {@code String}.</p>
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 * @see Config
 */
public abstract class AbstractMutableStringConfig
extends AbstractStringConfig
implements MutableConfig {

    /** String representation of {@code null} values. */
    public static final String NULL = "\0";

    /**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	protected String getStringParameter(
    		@NotBlank
			final String key) {
		String result = getRawValue(key);
		if (NULL.equals(result)) {
			result = null;
		}
		return result;
	}

	/**
	 * Returns the raw stored value of the configuration parameter configured in this
	 * instance as {@code String}.
	 * 
	 * @param key The key of the configuration parameter
	 * @return The configuration parameter raw value as {@code String}
	 */
	@Nullable
	protected abstract String getRawValue(
    		@NotBlank
			String key);

	/**
     * {@inheritDoc}
     */
	@Override
	public void set(
    		@NotBlank
			final String key,
			@Nullable
			final Object value) {
		Validate.notNull(key, "Parameter key is required");
		if (value == null) {
			setRawValue(key, NULL);
		} else if (value instanceof Float) {
			setRawValue(key, BigDecimal.valueOf((Float) value).toString());
		} else if (value instanceof Double) {
			setRawValue(key, BigDecimal.valueOf((Double) value).toString());
		} else if (value instanceof Date) {
			setRawValue(key, ((Date) value).toInstant().toString());
		} else if (value instanceof Calendar) {
			setRawValue(key, ((Calendar) value).toInstant().toString());
		} else {
			setRawValue(key, value.toString());
		}
	}

	/**
	 * Sets the raw stored value of the configuration parameter configured in this
	 * instance as {@code String}.
	 * 
	 * @param key The key of the configuration parameter
	 * @param value The configuration parameter raw value as {@code String}
	 */
	protected abstract void setRawValue(
    		@NotBlank
			String key,
			@NotNull
			String value);
}
