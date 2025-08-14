package dev.orne.config;

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

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.ObjectUtils;
import org.apiguardian.api.API;

import dev.orne.config.impl.ConfigProxy;

/**
 * Configuration properties provider.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2019-07
 * @version 2.0, 2025-05
 * @since 0.1
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface Config {

    /**
     * Creates a configuration proxy of the specified type.
     * <p>
     * The configuration proxy will delegate all method calls to the
     * underlying configuration instance, except for the default methods
     * defined in the specified type interface, which will be invoked directly
     * on the interface.
     * 
     * @param <T> The configuration type.
     * @param config The proxied configuration instance.
     * @param type The configuration type interface to create a proxy for.
     * @return The proxy of the specified configuration type.
     */
    static <T extends Config> T as(
            final @NotNull Config config,
            final @NotNull Class<T> type) {
        return ConfigProxy.create(config, type);
    }

    /**
     * Returns the parent configuration, if any.
     * 
     * @return The parent configuration
     */
    default Config getParent() {
        return null;
    }

    /**
     * Returns {@code true} if the configuration contains no property.
     * 
     * @return Returns {@code true} if the configuration contains no property.
     * @throws NonIterableConfigException If the configuration property keys
     * cannot be iterated.
     * @throws ConfigException If an error occurs accessing the configuration.
     */
    default boolean isEmpty() {
        return !getKeys().iterator().hasNext();
    }

    /**
     * Returns {@code true} if the property with the key passed as argument
     * has been configured.
     * 
     * @param key The configuration property.
     * @return Returns {@code true} if the property has been configured.
     */
    default boolean contains(
            @NotBlank String key) {
        return get(key) != null;
    }

    /**
     * Returns the configuration property keys contained in this configuration.
     * 
     * @return The configuration property keys.
     * @throws NonIterableConfigException If the configuration property keys
     * cannot be iterated.
     * @throws ConfigException If an error occurs accessing the configuration.
     */
    default @NotNull Stream<String> getKeys() {
        throw new NonIterableConfigException(
                "Configuration property keys cannot be iterated.");
    }

    /**
     * Returns the configuration property keys contained in this configuration
     * that match the specified predicate.
     * 
     * @param filter The predicate to filter the property keys with.
     * @return The configuration property keys that match the predicate.
     * @throws NonIterableConfigException If the configuration property keys
     * cannot be iterated.
     * @throws ConfigException If an error occurs accessing the configuration.
     */
    default @NotNull Stream<String> getKeys(
            final @NotNull Predicate<String> filter) {
        return getKeys().filter(filter);
    }

    /**
     * Returns the configuration property keys contained in this configuration
     * that start with the specified prefix.
     * 
     * @param prefix The predicate to filter the property keys with.
     * @return The configuration property keys that match the predicate.
     * @throws NonIterableConfigException If the configuration property keys
     * cannot be iterated.
     * @throws ConfigException If an error occurs accessing the configuration.
     */
    default @NotNull Stream<String> getKeys(
            final @NotNull String prefix) {
        return getKeys(key -> key.startsWith(prefix));
    }

    /**
     * Returns the value of the configuration parameter as {@code String}.
     * 
     * @param key The configuration property
     * @return The configuration parameter value as {@code String}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    String get(
            @NotBlank String key);

    /**
     * Returns the value of the configuration parameter as {@code String}
     * without applying any decoration or transformation.
     * 
     * @param key The configuration property
     * @return The configuration parameter value as {@code String}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    default String getUndecored(
            @NotBlank String key) {
        return get(key);
    }

    /**
     * Returns the value of the configuration parameter as {@code String}.
     * 
     * @param key The key of the configuration parameter
     * @param defaultValue The default value to return if the configuration
     * parameter is not set or is {@code null}.
     * @return The configuration parameter value as {@code String}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    default String get(
            @NotBlank String key,
            String defaultValue) {
        return ObjectUtils.firstNonNull(get(key), defaultValue);
    }

    /**
     * Returns the value of the configuration parameter as {@code String}.
     * 
     * @param key The key of the configuration parameter
     * @param defaultValue The default value supplier if the configuration
     * parameter is not set or is {@code null}.
     * @return The configuration parameter value as {@code String}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    default String get(
            @NotBlank String key,
            @NotNull Supplier<String> defaultValue) {
        return ObjectUtils.getFirstNonNull(() -> get(key), defaultValue);
    }

    /**
     * Returns the value of the configuration parameter as {@code Boolean}.
     * 
     * @param key The key of the configuration parameter
     * @return The configuration parameter value as {@code Boolean}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    default Boolean getBoolean(
            @NotBlank String key) {
        final String value = get(key);
        return value == null ? null : Boolean.parseBoolean(value); 
    }

    /**
     * Returns the value of the configuration parameter as {@code Boolean}.
     * 
     * @param key The key of the configuration parameter
     * @param defaultValue The default value to return if the configuration
     * parameter is not set or is {@code null}.
     * @return The configuration parameter value as {@code Boolean}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    default boolean getBoolean(
            @NotBlank String key,
            boolean defaultValue) {
        return ObjectUtils.firstNonNull(getBoolean(key), defaultValue);
    }

    /**
     * Returns the value of the configuration parameter as {@code Boolean}.
     * 
     * @param key The key of the configuration parameter
     * @param defaultValue The default value supplier if the configuration
     * parameter is not set or is {@code null}.
     * @return The configuration parameter value as {@code Boolean}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    default Boolean getBoolean(
            @NotBlank String key,
            @NotNull Supplier<Boolean> defaultValue) {
        return ObjectUtils.getFirstNonNull(() -> getBoolean(key), defaultValue);
    }

    /**
     * Returns the integer value of the specified configuration property,
     * with variable substitution.
     * 
     * @param key The configuration property.
     * @return The value configuration property, if any.
     * @throws NumberFormatException If the configuration value cannot be
     * parsed as an integer.
     */
    default Integer getInteger(
            @NotNull String key) {
        final String strValue = get(key);
        return strValue == null ? null : Integer.valueOf(strValue);
    }

    /**
     * Returns the integer value of the specified configuration property,
     * with variable substitution.
     * 
     * @param key The configuration property.
     * @param defaultValue The default value to return if the configuration
     * parameter is not set or is {@code null}.
     * @return The value configuration property, if any.
     * @throws NumberFormatException If the configuration value cannot be
     * parsed as an integer.
     */
    default int getInteger(
            @NotNull String key,
            int defaultValue) {
        return Integer.parseInt(get(key, String.valueOf(defaultValue)));
    }

    /**
     * Returns the value of the configuration parameter as {@code Boolean}.
     * 
     * @param key The key of the configuration parameter
     * @param defaultValue The default value supplier if the configuration
     * parameter is not set or is {@code null}.
     * @return The configuration parameter value as {@code Boolean}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    default Integer getInteger(
            @NotBlank String key,
            @NotNull Supplier<Integer> defaultValue) {
        return ObjectUtils.getFirstNonNull(() -> getInteger(key), defaultValue);
    }

    /**
     * Returns the integer value of the specified configuration property,
     * with variable substitution.
     * 
     * @param key The configuration property.
     * @return The value configuration property, if any.
     * @throws NumberFormatException If the configuration value cannot be
     * parsed as an long.
     */
    default Long getLong(
            @NotNull String key) {
        final String strValue = get(key);
        return strValue == null ? null : Long.valueOf(strValue);
    }

    /**
     * Returns the integer value of the specified configuration property,
     * with variable substitution.
     * 
     * @param key The configuration property.
     * @param defaultValue The default value to return if the configuration
     * parameter is not set or is {@code null}.
     * @return The value configuration property, if any.
     * @throws NumberFormatException If the configuration value cannot be
     * parsed as an long.
     */
    default long getLong(
            @NotNull String key,
            long defaultValue) {
        return Long.parseLong(get(key, String.valueOf(defaultValue)));
    }

    /**
     * Returns the value of the configuration parameter as {@code Boolean}.
     * 
     * @param key The key of the configuration parameter
     * @param defaultValue The default value supplier if the configuration
     * parameter is not set or is {@code null}.
     * @return The configuration parameter value as {@code Boolean}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    default Long getLong(
            @NotBlank String key,
            @NotNull Supplier<Long> defaultValue) {
        return ObjectUtils.getFirstNonNull(() -> getLong(key), defaultValue);
    }
}
