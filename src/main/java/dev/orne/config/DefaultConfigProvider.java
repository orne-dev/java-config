package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;

/**
 * Default implementation of {@code ConfigProvider}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 2.0, 2020-04
 * @since 0.1
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
            final @NotNull Config defaultConfig) {
        super();
        Validate.notNull(defaultConfig, NULL_DEFAULT_ERR);
        this.defaultConfig = defaultConfig;
        this.mappings = new HashMap<>();
        mapConfigType(defaultConfig.getClass(), defaultConfig);
    }

    /**
     * Registers a new configuration available for bean configuration.
     * 
     * @param config The configuration to register
     * @return This instance for method chaining
     */
    public DefaultConfigProvider registerConfig(
            final @NotNull Config config) {
        Validate.notNull(config, NULL_CONFIG_ERR);
        mapConfigType(config.getClass(), config);
        return this;
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
            final @NotNull Class<?> type,
            final @NotNull Config config) {
        this.mappings.computeIfAbsent(type, key -> {
            for (final Class<?> iface : type.getInterfaces()) {
                mapConfigType(iface, config);
            }
            final Class<?> superType = type.getSuperclass();
            if (superType != null && !Object.class.equals(superType)) {
                mapConfigType(superType, config);
            }
            return config;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Config getDefaultConfig() {
        return this.defaultConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Config selectConfig(
            final ConfigurationOptions options,
            final @NotNull Class<?> targetClass) {
        Config result = null;
        if (options == null || options.preferredConfigs().length == 0) {
            result = this.defaultConfig;
        } else {
            for (final Class<?> preferred : options.preferredConfigs()) {
                Validate.notNull(preferred, NULL_PREFERRED_ERR + targetClass);
                if (this.mappings.containsKey(preferred)) {
                    result = this.mappings.get(preferred);
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
    protected boolean isMapped(
            final @NotNull Class<?> type) {
        return this.mappings.containsKey(type);
    }
}
