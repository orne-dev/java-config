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

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.impl.EventsHandler;

/**
 * A watchable mutable configuration that delegates all operations to another
 * configuration.
 * This class is useful for creating a proxy or wrapper around an existing
 * configuration instance, allowing for additional behavior or modifications
 * without changing the original configuration.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-08
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0")
public class DelegatedWatchableConfig
extends DelegatedMutableConfig
implements WatchableConfig {

    /** The configuration change events handler. */
    private final @NotNull EventsHandler events;

    /**
     * Creates a new instance.
     *
     * @param delegate The configuration to delegate to.
     */
    public DelegatedWatchableConfig(
            final @NotNull WatchableConfig delegate) {
        super(delegate);
        this.events = new EventsHandler();
        delegate.addListener(
                (config, keys) -> notifyDelegateChanges(keys));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull WatchableConfig getDelegate() {
        return (WatchableConfig) super.getDelegate();
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
     * Notifies configuration properties changed through delegate configuration
     * to all registered listeners.
     * 
     * @param keys The changed configuration properties.
     */
    protected void notifyDelegateChanges(
            final @NotNull Set<String> keys) {
        this.events.notify(this, keys);
    }
}
