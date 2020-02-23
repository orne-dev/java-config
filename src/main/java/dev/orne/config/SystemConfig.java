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

import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.Validate;

/**
 * Implementation of {@code Config} based on the system properties.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 * @see Config
 * @see System#getProperties()
 */
public class SystemConfig
extends AbstractStringConfig {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean containsParameter(
    		@NotBlank
			final String key) {
		Validate.notNull(key, "Parameter key is required");
		return System.getProperty(key) != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getStringParameter(
    		@NotBlank
			final String key) {
		Validate.notNull(key, "Parameter key is required");
		return System.getProperty(key);
	}
}
