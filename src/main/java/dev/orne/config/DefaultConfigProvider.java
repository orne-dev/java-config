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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;

/**
 * Default implementation of {@code ConfigProvider}.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 */
public class DefaultConfigProvider
implements ConfigProvider {

	/** Error message for null default configuration. */
	private static final String NULL_DEFAULT_ERR =
			"Default config is required.";
	/** Error message for null configuration register attempt. */
	private static final String NULL_CONFIG_ERR =
			"Cannot register a null configuration.";
	/** Error message for null preferred configuration. */
	private static final String NULL_PREFERRED_ERR =
			"Unexpected null preferred configuration for class ";

	/** The default configuration. */
	private final Config defaultConfig;
	/** The available configurations mappings. */
	private final Map<Class<?>, Config> mappings;

	/**
	 * Creates a new instance.
	 * 
	 * @param defaultConfig The default configuration
	 */
	public DefaultConfigProvider(
			final Config defaultConfig) {
		super();
		Validate.notNull(defaultConfig, NULL_DEFAULT_ERR);
		this.defaultConfig = defaultConfig;
		this.mappings = new HashMap<Class<?>, Config>();
		mapConfigType(defaultConfig.getClass(), defaultConfig);
	}

	/**
	 * Registers a new configuration available for bean configuration.
	 * 
	 * @param config The configuration to register
	 */
	public void registerConfig(final Config config) {
		Validate.notNull(config, NULL_CONFIG_ERR);
		mapConfigType(config.getClass(), config);
	}

	/**
	 * Maps configuration type and all its interfaces to the instance
	 * passed as argument. If the type has super class different of
	 * {@code Object} repeats the mapping for it.
	 * 
	 * @param type The type of configuration to scan
	 * @param config The configuration instance
	 */
	protected void mapConfigType(
			final Class<?> type,
			final Config config) {
		if (!this.mappings.containsKey(type)) {
			this.mappings.put(type, config);
			for (final Class<?> iface : type.getInterfaces()) {
				mapConfigType(iface, config);
			}
			final Class<?> superType = type.getSuperclass();
			if (superType != null && !Object.class.equals(superType)) {
				mapConfigType(superType, config);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Config getDefaultConfig() {
		return this.defaultConfig;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Config selectConfig(
			final ConfigurationOptions options,
			final Class<?> targetClass) {
		Config result = null;
		if (options == null || options.preferedConfigs() == null) {
			result = this.defaultConfig;
		} else {
			for (final Class<?> prefered : options.preferedConfigs()) {
				Validate.notNull(prefered, NULL_PREFERRED_ERR + targetClass);
				if (this.mappings.containsKey(prefered)) {
					result = this.mappings.get(prefered);
					break;
				}
			}
			if (result == null && options.fallbackToDefaultConfig()) {
				result = this.defaultConfig;
			}
		}
		return result;
	}

	/**
	 * Returns {@code true} if a configuration is mapped for the requested
	 * class.
	 * 
	 * @param type The requested class
	 * @return {@code true} if a configuration is mapped for the requested
	 * class
	 */
	protected boolean isMapped(final Class<?> type) {
		return this.mappings.containsKey(type);
	}
}
