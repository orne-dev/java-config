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

import java.util.Set;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

import dev.orne.config.WatchableConfig;

/**
 * Base abstract implementation of watchable mutable configuration properties
 * provider.
 * <p>
 * Extending classes must add {@code WatchableConfig} interface and
 * override {@code addListener} and {@code removeListener} methods
 * making them public and delegating to the protected methods of this class.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-04
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public abstract class AbstractWatchableConfig
extends AbstractMutableConfig {

    /** The configuration change events handler. */
    private final EventsHandler events;

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     */
    protected AbstractWatchableConfig(
            final ConfigOptions options) {
        this(options, new MutableConfigOptions());
    }

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param mutableOptions The mutable configuration builder options.
     */
    protected AbstractWatchableConfig(
            final ConfigOptions options,
            final MutableConfigOptions mutableOptions) {
        super(options, mutableOptions);
        this.events = new EventsHandler();
        if (this instanceof WatchableConfig
                && getParent() instanceof WatchableConfig) {
            ((WatchableConfig) getParent()).addListener(
                    (config, keys) -> notifyParentChanges(keys));
        }
    }

    /**
     * Returns the configuration change events handler, if supported by
     * delegated configuration.
     * 
     * @return The configuration change events handler.
     */
    protected EventsHandler getEvents() {
        return this.events;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void set(
            final String key,
            final @Nullable String value) {
        super.set(key, value);
        if (this instanceof WatchableConfig) {
            notifyLocalChanges(key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void remove(
            final String... keys) {
        super.remove(keys);
        if (this instanceof WatchableConfig) {
            notifyLocalChanges(keys);
        }
    }

    /**
     * Registers the specified configuration change events listener.
     * 
     * @param listener The listener to be called on configuration changes.
     * @throws IllegalStateException If the configuration type does not support
     * event listeners.
     * @see WatchableConfig#addListener(Listener)
     */
    protected void addListener(
            final WatchableConfig.Listener listener) {
        this.events.add(listener);
    }

    /**
     * Unregisters the specified configuration change events listener.
     * 
     * @param listener The listener to previously registered.
     * @see WatchableConfig#removeListener(Listener)
     */
    protected void removeListener(
            final WatchableConfig.Listener listener) {
        this.events.remove(listener);
    }

    /**
     * Notifies configuration properties changed through this instance
     * to all registered listeners.
     * 
     * @param keys The changed local properties.
     */
    protected void notifyLocalChanges(
            final String... keys) {
        if (this instanceof WatchableConfig) {
            this.events.notify((WatchableConfig) this, keys);
        } else {
            throw new IllegalStateException(
                    "Configuration does not support event listeners");
        }
    }

    /**
     * Notifies configuration properties changed through parent configuration
     * to all registered listeners.
     * 
     * @param keys The changed parent properties.
     */
    protected void notifyParentChanges(
            final Set<String> keys) {
        if (this instanceof WatchableConfig) {
            this.events.notify((WatchableConfig) this, keys);
        } else {
            throw new IllegalStateException(
                    "Configuration does not support event listeners");
        }
    }
}
