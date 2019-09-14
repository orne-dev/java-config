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

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

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
	protected String getStringParameter(final String key) {
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
	protected abstract String getRawValue(String key);

	/**
     * {@inheritDoc}
     */
	@Override
	public void set(final String key, final Object value) {
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
	protected abstract void setRawValue(String key, String value);
}
