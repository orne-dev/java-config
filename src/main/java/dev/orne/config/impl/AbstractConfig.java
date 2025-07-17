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
import java.util.Optional;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;

import dev.orne.config.Config;
import dev.orne.config.ConfigException;
import dev.orne.config.NonIterableConfigException;
import dev.orne.config.ValueDecoder;
import dev.orne.config.ValueDecorator;
import dev.orne.config.WatchableConfig;

/**
 * Base abstract implementation of configuration properties provider.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-04
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public abstract class AbstractConfig
implements Config {

    /** Error message for blank property keys. */
    protected static final String KEY_BLANK_ERR =
            "Property key must be a non blank string";

    /** The parent configuration. */
    private final Config parent;
    /** The configuration properties values decoder. */
    private final @NotNull ValueDecoder decoder;
    /** The configuration properties values decorator. */
    private final @NotNull ValueDecorator decorator;
    /** The configuration properties values variable resolver. */
    private final VariableResolver resolver;

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     */
    protected AbstractConfig(
            final @NotNull ConfigOptions options) {
        super();
        Objects.requireNonNull(options);
        if (options.getCryptoProvider() != null) {
            if (options.getDecoder() == null) {
                options.setDecoder(options.getCryptoProvider()::decrypt);
            } else {
                options.setDecoder(
                        options.getDecoder().compose(options.getCryptoProvider()::decrypt));
            }
        }
        if (options.getDecoder() == null) {
            options.setDecoder(ValueDecoder.DEFAULT);
        }
        if (options.isVariableResolutionEnabled()) {
            this.resolver = new VariableResolver(this);
            if (options.getDecorator() == null) {
                options.setDecorator(resolver::decorate);
            } else {
                options.setDecorator(options.getDecorator().compose(resolver::decorate));
            }
        } else {
            this.resolver = null;
        }
        if (options.getDecorator() == null) {
            options.setDecorator(ValueDecorator.DEFAULT);
        }
        this.parent = options.getParent();
        this.decoder = options.getDecoder();
        this.decorator = options.getDecorator();
        if (getParent() instanceof WatchableConfig && this.resolver != null) {
            ((WatchableConfig) getParent()).addListener(
                    (config, keys) -> this.resolver.clearCache());
        }
    }

    /**
     * Returns the parent configuration, if any.
     * 
     * @return The parent configuration
     */
    @Override
    public Config getParent() {
        return this.parent;
    }

    /**
     * Returns the read configuration properties values decoder.
     * 
     * @return The configuration properties values decoder.
     */
    protected @NotNull ValueDecoder getDecoder() {
        return this.decoder;
    }

    /**
     * Returns the read configuration properties values decorator.
     * 
     * @return The configuration properties values decorator.
     */
    protected @NotNull ValueDecorator getDecorator() {
        return this.decorator;
    }

    /**
     * Returns the configuration properties values variable resolver.
     * 
     * @return The configuration properties values variable resolver.
     */
    protected @NotNull Optional<VariableResolver> getResolver() {
        return Optional.ofNullable(this.resolver);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return isEmptyInt() &&
                (this.parent == null || this.parent.isEmpty());
    }

    /**
     * Returns {@code true} if this configuration instance contains no
     * property.
     * <p>
     * Parent configuration
     * 
     * @return Returns {@code true} if the configuration contains no property.
     * @throws NonIterableConfigException If the configuration property keys
     * cannot be iterated.
     * @throws ConfigException If an error occurs accessing the configuration.
     */
    protected abstract boolean isEmptyInt();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(
            final @NotBlank String key) {
        Validate.notBlank(key, KEY_BLANK_ERR);
        return containsInt(key) || 
                (this.parent != null && this.parent.contains(key));
    }

    /**
     * Returns {@code true} if the property with the key passed as argument
     * has been configured in this configuration instance.
     * 
     * @param key The configuration property.
     * @return Returns {@code true} if the property has been configured.
     */
    protected abstract boolean containsInt(
            @NotBlank String key);

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Stream<String> getKeys() {
        if (this.parent == null) {
            return getKeysInt();
        } else {
            return Stream.concat(getKeysInt(), this.parent.getKeys());
        }
    }

    /**
     * Returns the configuration property keys contained in this configuration
     * instance .
     * 
     * @return The configuration property keys.
     * @throws NonIterableConfigException If the configuration property keys
     * cannot be iterated.
     * @throws ConfigException If an error occurs accessing the configuration.
     */
    protected abstract @NotNull Stream<String> getKeysInt();

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(
            final @NotBlank String key) {
        Validate.notBlank(key, KEY_BLANK_ERR);
        return this.decorator.decorate(getUndecored(key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUndecored(
            final @NotBlank String key) {
        final String value;
        if (containsInt(key)) {
            value = this.decoder.decode(getInt(key));
        } else if (this.parent != null) {
            value = this.parent.getUndecored(key);
        } else {
            value = null;
        }
        return value;
    }

    /**
     * Returns the value of the configuration property as {@code String}.
     * 
     * @param key The configuration property.
     * @return The configuration parameter value.
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value.
     */
    protected abstract String getInt(
            final @NotBlank String key);
}
