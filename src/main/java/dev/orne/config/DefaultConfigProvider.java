package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;

/**
 * Default implementation of {@code ConfigProvider}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
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
            @NotNull
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
    public void registerConfig(
            @NotNull
            final Config config) {
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
            @NotNull
            final Class<?> type,
            @NotNull
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
    @NotNull
    public Config getDefaultConfig() {
        return this.defaultConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Config selectConfig(
            @Nullable
            final ConfigurationOptions options,
            @NotNull
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
    protected boolean isMapped(
            @NotNull
            final Class<?> type) {
        return this.mappings.containsKey(type);
    }
}
