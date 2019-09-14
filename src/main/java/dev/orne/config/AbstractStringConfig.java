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
import java.time.Instant;

/**
 * <p>Partial implementation of {@code Config} for implementations with
 * configuration values stored as {@code String}.</p>
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 2019-07
 * @see Config
 */
public abstract class AbstractStringConfig
extends AbstractConfig {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Boolean getBooleanParameter(final String key) {
		final String strValue = getStringParameter(key);
		return strValue == null ? null : Boolean.valueOf(strValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Number getNumberParameter(final String key) {
		final String strValue = getStringParameter(key);
		return strValue == null ? null : new BigDecimal(strValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Instant getInstantParameter(final String key) {
		final String strValue = getStringParameter(key);
		return strValue == null ? null : Instant.parse(strValue);
	}
}
