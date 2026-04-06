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

import java.util.Objects;

import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

import dev.orne.config.ConfigException;
import dev.orne.config.MutableConfig;
import dev.orne.config.ValueEncoder;

/**
 * Base abstract implementation of mutable configuration properties provider.
 * <p>
 * Extending classes must add {@code MutableConfig} interface and
 * override {@code set} and {@code remove} methods
 * making them public and delegating to the protected methods of this class.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-04
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public abstract class AbstractMutableConfig
extends AbstractConfig {

    /** The configuration properties values encoder. */
    private final ValueEncoder encoder;

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     */
    protected AbstractMutableConfig(
            final ConfigOptions options) {
        this(options, new MutableConfigOptions());
    }

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param mutableOptions The mutable configuration builder options.
     */
    protected AbstractMutableConfig(
            final ConfigOptions options,
            final MutableConfigOptions mutableOptions) {
        super(options);
        Objects.requireNonNull(mutableOptions);
        if (options.getCryptoProvider() != null) {
            if (mutableOptions.getEncoder() == null) {
                mutableOptions.setEncoder(options.getCryptoProvider()::encrypt);
            } else {
                mutableOptions.setEncoder(
                        mutableOptions.getEncoder().andThen(options.getCryptoProvider()::encrypt));
            }
        }
        if (mutableOptions.getEncoder() == null) {
            mutableOptions.setEncoder(ValueEncoder.DEFAULT);
        }
        this.encoder = mutableOptions.getEncoder();
    }

    /**
     * Returns the configuration properties values encoder.
     * 
     * @return The configuration properties values encoder.
     */
    protected ValueEncoder getEncoder() {
        return this.encoder;
    }

    /**
     * Sets the value of the specified configuration property.
     * 
     * @param key The configuration property.
     * @param value The value to set
     * @throws ConfigException If an error occurs setting the configuration
     * property value
     * @see MutableConfig#set(String, String)
     */
    protected void set(
            final String key,
            final @Nullable String value) {
        Validate.notBlank(key, KEY_BLANK_ERR);
        String encoded = this.encoder.encode(value);
        if (encoded == null) {
            removeInt(key);
        } else {
            setInt(key, encoded);
        }
        getResolver().ifPresent(r -> r.clearCache());
    }

    /**
     * Sets the value of the specified configuration property.
     * 
     * @param key The configuration property.
     * @param value The value to set
     * @throws ConfigException If an error occurs setting the configuration
     * property value
     */
    protected void setInt(
            String key,
            String value) {
        throw new UnsupportedOperationException(
                "Configuration instance is not mutable");
    }

    /**
     * Removes the specified configuration properties.
     * 
     * @param keys The configuration properties.
     * @throws ConfigException If an error occurs removing the configuration
     * properties.
     * @see MutableConfig#remove(String...)
     */
    protected void remove(
            final String... keys) {
        for (final String key : keys) {
            Validate.notBlank(key, KEY_BLANK_ERR);
        }
        removeInt(keys);
        getResolver().ifPresent(r -> r.clearCache());
    }

    /**
     * Removes the specified configuration properties.
     * 
     * @param keys The configuration properties.
     * @throws ConfigException If an error occurs removing the configuration
     * properties.
     */
    protected void removeInt(
            String... keys) {
        throw new UnsupportedOperationException(
                "Configuration instance is not mutable");
    }
}
