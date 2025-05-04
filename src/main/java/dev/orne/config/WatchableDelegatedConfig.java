package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2020 - 2025 Orne Developments
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

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Delegated {@code WatchableConfig} implementation.
 * Provides configuration property changes listeners support.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-04
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0")
public class WatchableDelegatedConfig
extends MutableDelegatedConfig
implements WatchableConfig {

    /** The configuration change events handler. */
    private final EventsHandler events;

    /**
     * Creates a new instance.
     * 
     * @param delegate The delegate {@code WatchableConfig} instance
     */
    public WatchableDelegatedConfig(
            final @NotNull WatchableConfig delegate) {
        super(delegate);
        this.events = new EventsHandler();
        delegate.addListener((inst, keys) -> events.notify(this, keys));
    }

    /**
     * Returns the delegate {@code WatchableConfig} instance.
     * 
     * @return The delegate {@code WatchableConfig} instance
     */
    @Override
    protected @NotNull WatchableConfig getDelegate() {
        return (WatchableConfig) super.getDelegate();
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
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.events);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WatchableDelegatedConfig other = (WatchableDelegatedConfig) obj;
        return super.equals(obj)
                && Objects.equals(this.events, other.events);
    }
}
