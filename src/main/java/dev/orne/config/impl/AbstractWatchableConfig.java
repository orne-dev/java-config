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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.WatchableConfig;

/**
 * Base abstract implementation of watchable mutable configuration properties
 * provider.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-04
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public abstract class AbstractWatchableConfig
extends AbstractMutableConfig
implements WatchableConfig {

    /** The configuration change events handler. */
    private final @NotNull EventsHandler events;

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param mutableOptions The mutable configuration builder options.
     */
    protected AbstractWatchableConfig(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions) {
        super(options, mutableOptions);
        this.events = new EventsHandler();
        if (getParent() instanceof WatchableConfig) {
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
    public void set(
            final @NotBlank String key,
            final String value) {
        super.set(key, value);
        notifyLocalChanges(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotBlank String... keys) {
        super.remove(keys);
        notifyLocalChanges(keys);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(
            final @NotNull Listener listener) {
        this.events.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(
            final @NotNull Listener listener) {
        this.events.remove(listener);
    }

    /**
     * Notifies configuration properties changed through this instance
     * to all registered listeners.
     * 
     * @param keys The changed local properties.
     */
    protected void notifyLocalChanges(
            final @NotNull String... keys) {
        this.events.notify(this, keys);
    }

    /**
     * Notifies configuration properties changed through parent configuration
     * to all registered listeners.
     * 
     * @param keys The changed parent properties.
     */
    protected void notifyParentChanges(
            final @NotNull Set<String> keys) {
        this.events.notify(this, keys);
    }
}
