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

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.impl.CommonsConfigBuilderImpl;
import dev.orne.config.impl.EnvironmentConfigBuilderImpl;
import dev.orne.config.impl.JsonConfigBuilderImpl;
import dev.orne.config.impl.PreferencesConfigBuilderImpl;
import dev.orne.config.impl.PropertiesConfigBuilderImpl;
import dev.orne.config.impl.SystemConfigBuilderImpl;
import dev.orne.config.impl.XmlConfigBuilderImpl;
import dev.orne.config.impl.YamlConfigBuilderImpl;

/**
 * Configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @param <S> The concrete type of the builder.
 * @since 1.0
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface ConfigBuilder<S extends ConfigBuilder<S>> {

    /**
     * Creates a new environment variables based configuration builder.
     * 
     * @return The configuration builder.
     */
    static @NotNull EnvironmentConfigBuilder<?> fromEnvironmentVariables() {
        return new EnvironmentConfigBuilderImpl();
    }

    /**
     * Creates a new system properties based configuration builder.
     * 
     * @return The configuration builder.
     */
    static @NotNull SystemConfigBuilder<?> fromSystemProperties() {
        return new SystemConfigBuilderImpl();
    }

    /**
     * Creates a new {@code Properties} based configuration builder.
     * 
     * @return The configuration builder.
     */
    static @NotNull PropertiesConfigBuilder<?> fromPropertiesFiles() {
        return new PropertiesConfigBuilderImpl();
    }

    /**
     * Creates a new JSON based configuration builder.
     * 
     * @return The configuration builder.
     */
    static @NotNull JsonConfigBuilder<?> fromJsonFiles() {
        return new JsonConfigBuilderImpl();
    }

    /**
     * Creates a new YAML based configuration builder.
     * 
     * @return The configuration builder.
     */
    static @NotNull YamlConfigBuilder<?> fromYamlFiles() {
        return new YamlConfigBuilderImpl();
    }

    /**
     * Creates a new XML based configuration builder.
     * 
     * @return The configuration builder.
     */
    static @NotNull XmlConfigBuilder<?> fromXmlFiles() {
        return new XmlConfigBuilderImpl();
    }

    /**
     * Creates a new {@code Preferences} based configuration builder.
     * 
     * @return The configuration builder.
     */
    static @NotNull PreferencesConfigNodeBuilder<?> fromJavaPreferences() {
        return new PreferencesConfigBuilderImpl();
    }

    /**
     * Creates a new Apache Commons Configuration based configuration
     * builder.
     * 
     * @return The configuration builder.
     */
    static @NotNull CommonsConfigBuilder<?> fromApacheCommons() {
        return new CommonsConfigBuilderImpl();
    }

    /**
     * Sets the parent configuration.
     * 
     * @param parent The parent configuration.
     * @return This instance, for method chaining.
     */
    @NotNull S withParent(
            Config parent);

    /**
     * Sets the configuration properties values cryptography
     * transformations provider.
     * 
     * @param provider The cryptography transformations provider.
     * @return This instance, for method chaining.
     */
    @NotNull S withEncryption(
            ConfigCryptoProvider provider);

    /**
     * Sets the configuration properties values decoder.
     * Applied to property values contained in the builded configuration
     * instance (not to the parent configuration properties, if any).
     * 
     * @param encoder The configuration properties values decoder.
     * @return This instance, for method chaining.
     */
    @NotNull S withDecoder(
            ValueDecoder encoder);

    /**
     * Enables configuration property values variable resolution.
     * 
     * @return This instance, for method chaining.
     */
    @NotNull S withVariableResolution();

    /**
     * Sets the configuration properties values decorator.
     * Applied to property values returned by the builded configuration
     * instance (whatever its source).
     * 
     * @param decorator The configuration properties values decorator.
     * @return This instance, for method chaining.
     */
    @NotNull S withDecorator(
            ValueDecorator decorator);

    /**
     * Creates the configuration instance.
     * 
     * @return The configuration instance.
     */
    @NotNull Config build();
}
