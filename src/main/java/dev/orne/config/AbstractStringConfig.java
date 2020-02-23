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
import java.time.Instant;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;

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
	@Nullable
	protected Boolean getBooleanParameter(
    		@NotBlank
			final String key) {
		final String strValue = getStringParameter(key);
		return strValue == null ? null : Boolean.valueOf(strValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	protected Number getNumberParameter(
    		@NotBlank
			final String key) {
		final String strValue = getStringParameter(key);
		return strValue == null ? null : new BigDecimal(strValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Nullable
	protected Instant getInstantParameter(
    		@NotBlank
			final String key) {
		final String strValue = getStringParameter(key);
		return strValue == null ? null : Instant.parse(strValue);
	}
}
