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

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

/**
 * Generic interface for {@code Config} providers.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 */
public interface ConfigProvider {

	/**
	 * Returns the default {@code Config} instance.
	 * 
	 * @return The default {@code Config} instance
	 */
	@NotNull
	Config getDefaultConfig();

	/**
	 * Returns a suitable {@code Config} instance for the configuration
	 * options passed as argument. The target class is passed as second
	 * argument for implementations that support extra annotations for
	 * configuration options.
	 * 
	 * @param options The configuration options of the target class.
	 * @param targetClass The target class for extra annotation retrieval, if
	 * supported.
	 * @return The selected {@code Config} instance, or {@code null} if no one
	 * is suitable
	 */
	@Nullable
	Config selectConfig(
			@Nullable
			final ConfigurationOptions options,
			@NotNull
			final Class<?> targetClass);
}
