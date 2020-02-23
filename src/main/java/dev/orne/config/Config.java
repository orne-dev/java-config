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

import java.time.Instant;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Interface for classes containing configuration values.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 */
public interface Config {

	/**
	 * Returns {@code true} if the parameter with the key passed as argument
	 * has been configured.
	 * 
	 * @param key The key of the configuration parameter
	 * @return Returns {@code true} if the parameter has been configured
	 */
	boolean contains(
    		@NotBlank
			String key);

	/**
	 * Returns the value of the configuration parameter.
	 * 
	 * @param key The key of the configuration parameter
	 * @param type The target type of the parameter
	 * @return The configuration parameter value converted to the target type
	 */
	@Nullable
	<T> T get(
    		@NotBlank
			String key,
    		@NotNull
			Class<T> type);

	/**
	 * Returns the value of the configuration parameter as {@code Boolean}.
	 * 
	 * @param key The key of the configuration parameter
	 * @return The configuration parameter value as {@code Boolean}
	 */
	@Nullable
	Boolean getBoolean(
    		@NotBlank
			String key);

	/**
	 * Returns the value of the configuration parameter as {@code String}.
	 * 
	 * @param key The key of the configuration parameter
	 * @return The configuration parameter value as {@code String}
	 */
	@Nullable
	String getString(
    		@NotBlank
			String key);

	/**
	 * Returns the value of the configuration parameter as {@code Number}.
	 * 
	 * @param key The key of the configuration parameter
	 * @return The configuration parameter value as {@code Number}
	 */
	@Nullable
	Number getNumber(
    		@NotBlank
			String key);

	/**
	 * Returns the value of the configuration parameter as {@code Instant}.
	 * 
	 * @param key The key of the configuration parameter
	 * @return The configuration parameter value as {@code Instant}
	 */
	@Nullable
	Instant getInstant(
    		@NotBlank
			String key);
}
