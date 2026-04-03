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

import org.apache.commons.lang3.ObjectUtils;
import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

import dev.orne.config.impl.CommonsConfigBuilderImpl;
import dev.orne.config.impl.ConfigSubtype;
import dev.orne.config.impl.ConfigSubset;
import dev.orne.config.impl.EnvironmentConfigBuilderImpl;
import dev.orne.config.impl.JsonConfigBuilderImpl;
import dev.orne.config.impl.PreferencesConfigBuilderImpl;
import dev.orne.config.impl.PropertiesConfigBuilderImpl;
import dev.orne.config.impl.SpringEnvironmentConfigBuilderImpl;
import dev.orne.config.impl.SystemConfigBuilderImpl;
import dev.orne.config.impl.XmlConfigBuilderImpl;
import dev.orne.config.impl.YamlConfigBuilderImpl;

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
     * Creates a new environment variables based configuration builder.
     * 
     * @return The configuration builder.
     */
    static EnvironmentConfigBuilder fromEnvironmentVariables() {
        return new EnvironmentConfigBuilderImpl();
    }

    /**
     * Creates a new system properties based configuration builder.
     * 
     * @return The configuration builder.
     */
    static SystemConfigBuilder fromSystemProperties() {
        return new SystemConfigBuilderImpl();
    }

    /**
     * Creates a new {@code Properties} based configuration builder.
     * 
     * @return The configuration builder.
     */
    static PropertiesConfigBuilder fromProperties() {
        return new PropertiesConfigBuilderImpl();
    }

    /**
     * Creates a new JSON based configuration builder.
     * 
     * @return The configuration builder.
     */
    static JsonConfigBuilder fromJson() {
        return new JsonConfigBuilderImpl();
    }

    /**
     * Creates a new YAML based configuration builder.
     * 
     * @return The configuration builder.
     */
    static YamlConfigBuilder fromYaml() {
        return new YamlConfigBuilderImpl();
    }

    /**
     * Creates a new XML based configuration builder.
     * 
     * @return The configuration builder.
     */
    static XmlConfigBuilder fromXml() {
        return new XmlConfigBuilderImpl();
    }

    /**
     * Creates a new {@code Preferences} based configuration builder.
     * 
     * @return The configuration builder.
     */
    static PreferencesConfigInitialBuilder fromJavaPreferences() {
        return new PreferencesConfigBuilderImpl();
    }

    /**
     * Creates a new Apache Commons Configuration based configuration
     * builder.
     * 
     * @return The configuration builder.
     */
    static CommonsConfigBuilder fromApacheCommons() {
        return new CommonsConfigBuilderImpl();
    }

    /**
     * Creates a new Spring Environment based configuration
     * builder.
     * 
     * @return The configuration builder.
     */
    static SpringEnvironmentConfigInitialBuilder fromSpringEnvironment() {
        return new SpringEnvironmentConfigBuilderImpl();
    }

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
            final Config config,
            final Class<T> type) {
        if (type.isInstance(config)) {
            return type.cast(config);
        } else {
            return ConfigSubtype.create(config, type);
        }
    }

    /**
     * Returns the parent configuration, if any.
     * 
     * @return The parent configuration
     */
    default @Nullable Config getParent() {
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
            String key) {
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
    default Stream<String> getKeys() {
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
    default Stream<String> getKeys(
            final Predicate<String> filter) {
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
    default Stream<String> getKeys(
            final String prefix) {
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
    @Nullable String get(
            String key);

    /**
     * Returns the value of the configuration parameter as {@code String}
     * without applying any decoration or transformation.
     * 
     * @param key The configuration property
     * @return The configuration parameter value as {@code String}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    default @Nullable String getUndecored(
            String key) {
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
    default @Nullable String get(
            String key,
            @Nullable String defaultValue) {
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
    default @Nullable String get(
            String key,
            Supplier<@Nullable String> defaultValue) {
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
    default @Nullable Boolean getBoolean(
            String key) {
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
            String key,
            boolean defaultValue) {
        final Boolean value = getBoolean(key);
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
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
    default @Nullable Boolean getBoolean(
            String key,
            Supplier<@Nullable Boolean> defaultValue) {
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
    default @Nullable Integer getInteger(
            String key) {
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
            String key,
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
    default @Nullable Integer getInteger(
            String key,
            Supplier<@Nullable Integer> defaultValue) {
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
    default @Nullable Long getLong(
            String key) {
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
            String key,
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
    default @Nullable Long getLong(
            String key,
            Supplier<@Nullable Long> defaultValue) {
        return ObjectUtils.getFirstNonNull(() -> getLong(key), defaultValue);
    }

    /**
     * Creates a configuration proxy of the specified type.
     * <p>
     * The configuration proxy will delegate all method calls to this
     * configuration instance, except for the default methods
     * defined in the specified type interface, which will be invoked directly
     * on the interface.
     * 
     * @param <T> The configuration type.
     * @param type The configuration type interface to create a proxy for.
     * @return The proxy of the specified configuration type.
     * @see #as(Config, Class)
     */
    default <T extends Config> T as(
            final Class<T> type) {
        return Config.as(this, type);
    }

    /**
     * Creates a subset configuration containing only the properties
     * with the specified prefix.
     * 
     * @param prefix The prefix for configuration keys.
     * @return The subset configuration.
     */
    default Config subset(
            final String prefix) {
        return ConfigSubset.create(this, prefix);
    }
}
