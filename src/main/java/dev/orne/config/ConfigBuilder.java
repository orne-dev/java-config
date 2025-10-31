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
     * Sets the parent configuration.
     * 
     * @param parent The parent configuration.
     * @return This instance, for method chaining.
     */
    @NotNull S withParent(
            Config parent);

    /**
     * Sets the parent configuration.
     * <p>
     * This is a shortcut for {@code withParent(parent.build())}
     * that allows less verbose configuration hierarchies building.
     * 
     * @param parent The parent configuration.
     * @return This instance, for method chaining.
     */
    default @NotNull S withParent(
            final @NotNull ConfigBuilder<?> parent) {
        return withParent(parent.build());
    }

    /**
     * Sets whether the configuration properties values from the parent
     * configuration (if any) must be overridden by the properties values
     * from this configuration.
     * <p>
     * By default, parent configuration properties values are not overridden.
     * Thus, if a property is defined both in current configuration and in
     * the parent configuration, the value from the parent configuration
     * will be returned.
     * 
     * @return This instance, for method chaining.
     */
    @NotNull S withOverrideParentProperties();

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

    /**
     * Creates the configuration instance as an instance of the specified
     * configuration type.
     * <p>
     * This is a shortcut for {@code Config.as(build(), configType)}.
     * 
     * @param configType The configuration sub-type.
     * @return The configuration instance.
     * @see Config#as(Config, Class)
     */
    default @NotNull Config as(
            final @NotNull Class<? extends Config> configType) {
        return Config.as(build(), configType);
    }
}
