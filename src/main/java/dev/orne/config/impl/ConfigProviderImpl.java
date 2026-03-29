package dev.orne.config.impl;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2025 Orne Developments
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

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

import dev.orne.config.Config;
import dev.orne.config.ConfigProvider;
import dev.orne.config.PreferredConfig;

/**
 * Default implementation of {@code ConfigProvider}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2019-07
 * @version 2.0, 2025-07
 * @since 0.1
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class ConfigProviderImpl
implements ConfigProvider {

    /** Error message for null default configuration. */
    private static final String NULL_DEFAULT_ERR =
            "Default config is required.";
    /** Error message for null configuration register attempt. */
    private static final String NULL_CONFIG_ERR =
            "Cannot register a null configuration.";
    /** Error message for null preferred configuration. */
    private static final String NULL_PREFERRED_ERR =
            "Unexpected null preferred configuration.";

    /** The default configuration. */
    private final Config defaultConfig;
    /** The available configurations mappings. */
    private final Map<Class<?>, Config> mappings;

    /**
     * Creates a new instance.
     * 
     * @param defaultConfig The default configuration
     */
    public ConfigProviderImpl(
            final Config defaultConfig) {
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
     */
    public void registerConfig(
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
            final Class<?> type,
            final Config config) {
        if (Proxy.isProxyClass(type)) {
            for (final Class<?> iface : type.getInterfaces()) {
                if (Config.class.isAssignableFrom(iface)) {
                    mapConfigType(iface, config);
                }
            }
        } else if (this.mappings.putIfAbsent(type, config) == null) {
            for (final Class<?> iface : type.getInterfaces()) {
                if (Config.class.isAssignableFrom(iface)) {
                    mapConfigType(iface, config);
                }
            }
            final Class<?> superType = type.getSuperclass();
            if (superType != null
                    && Config.class.isAssignableFrom(superType)) {
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
    public @Nullable Config selectConfig(
            final @Nullable PreferredConfig preferences) {
        Config result = null;
        if (preferences == null || preferences.value().length == 0) {
            result = this.defaultConfig;
        } else {
            for (final Class<?> preferred : preferences.value()) {
                Validate.notNull(preferred, NULL_PREFERRED_ERR);
                if (this.mappings.containsKey(preferred)) {
                    result = this.mappings.get(preferred);
                    break;
                }
            }
            if (result == null && preferences.fallbackToDefaultConfig()) {
                result = this.defaultConfig;
            }
        }
        return result;
    }

    public Optional<Config> getConfig(
            final Class<? extends Config> type) {
        return Optional.ofNullable(this.mappings.get(type));
    }
}
