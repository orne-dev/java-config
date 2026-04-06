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

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

import dev.orne.config.Config;
import dev.orne.config.ConfigCryptoProvider;
import dev.orne.config.ValueDecoder;
import dev.orne.config.ValueDecorator;

/**
 * Options of configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see Config
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class ConfigOptions {

    /** The parent configuration. */
    private @Nullable Config parent;
    /** If parent configuration property values are overridden values of this instance. */
    private boolean overrideParentProperties;
    /** The cryptography transformations provider. */
    private @Nullable ConfigCryptoProvider cryptoProvider;
    /** The configuration values decoder. */
    private @Nullable ValueDecoder decoder;
    /** If configuration property values variable resolution is enabled. */
    private boolean variableResolutionEnabled;
    /** The configuration properties values decorator. */
    private @Nullable ValueDecorator decorator;

    /**
     * Empty constructor.
     */
    public ConfigOptions() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public ConfigOptions(
            final ConfigOptions copy) {
        super();
        this.parent = copy.parent;
        this.overrideParentProperties = copy.overrideParentProperties;
        this.cryptoProvider = copy.cryptoProvider;
        this.decoder = copy.decoder;
        this.variableResolutionEnabled = copy.variableResolutionEnabled;
        this.decorator = copy.decorator;
    }

    /**
     * Returns the parent configuration.
     * 
     * @return The parent configuration.
     */
    public @Nullable Config getParent() {
        return this.parent;
    }

    /**
     * Sets the parent configuration.
     * 
     * @param parent The parent configuration.
     */
    public void setParent(
            final @Nullable Config parent) {
        this.parent = parent;
    }

    /**
     * Returns {@code true} if the configuration properties values from
     * the parent configuration (if any) are overridden by the properties
     * values from this configuration.
     * 
     * @return If the configuration properties values from the parent
     * configuration are overridden by the properties values from this
     * configuration.
     */
    public boolean isOverrideParentProperties() {
        return this.overrideParentProperties;
    }

    /**
     * Sets if the configuration properties values from the parent
     * configuration (if any) are overridden by the properties values
     * from this configuration.
     * 
     * @param override If the configuration properties
     * values from the parent configuration are overridden by the
     * properties values from this configuration.
     */
    public void setOverrideParentProperties(
            final boolean override) {
        this.overrideParentProperties = override;
    }

    /**
     * Returns the cryptography transformations provider.
     * 
     * @return The cryptography transformations provider.
     */
    public @Nullable ConfigCryptoProvider getCryptoProvider() {
        return this.cryptoProvider;
    }

    /**
     * Sets the cryptography transformations provider.
     * 
     * @param provider The cryptography transformations provider.
     */
    public void setCryptoProvider(
            final @Nullable ConfigCryptoProvider provider) {
        this.cryptoProvider = provider;
    }

    /**
     * Returns the configuration values decoder.
     * 
     * @return The configuration values decoder.
     */
    public @Nullable ValueDecoder getDecoder() {
        return this.decoder;
    }

    /**
     * Sets the configuration values decoder.
     * 
     * @param decoder The configuration values decoder.
     */
    public void setDecoder(
            final @Nullable ValueDecoder decoder) {
        this.decoder = decoder;
    }

    /**
     * Returns {@code true} if configuration property values variable
     * resolution is enabled.
     * 
     * @return If configuration property values variable resolution is enabled.
     */
    public boolean isVariableResolutionEnabled() {
        return this.variableResolutionEnabled;
    }

    /**
     * Sets if configuration property values variable resolution is enabled.
     * 
     * @param enabled If configuration property values variable resolution is
     * enabled.
     */
    public void setVariableResolutionEnabled(
            final boolean enabled) {
        this.variableResolutionEnabled = enabled;
    }

    /**
     * Returns the configuration properties values decorator.
     * 
     * @return The configuration properties values decorator.
     */
    public @Nullable ValueDecorator getDecorator() {
        return this.decorator;
    }

    /**
     * Sets the configuration properties values decorator.
     * 
     * @param decorator The configuration properties values decorator.
     */
    public void setDecorator(
            final @Nullable ValueDecorator decorator) {
        this.decorator = decorator;
    }
}
