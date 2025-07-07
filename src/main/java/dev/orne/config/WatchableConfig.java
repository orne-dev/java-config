package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2025 Orne Developments
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Watchable mutable configuration properties provider.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-04
 * @since 1.0
 * @see MutableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface WatchableConfig
extends MutableConfig {

    /**
     * Registers the specified configuration change events listener.
     * 
     * @param listener The listener to be called on configuration changes.
     * @throws IllegalStateException If the configuration type does not support
     * event listeners.
     * 
     */
    void addListener(
            @NotNull Listener listener);

    /**
     * Unregisters the specified configuration change events listener.
     * 
     * @param listener The listener to previously registered.
     * 
     */
    void removeListener(
            @NotNull Listener listener);

    /**
     * Mutable configuration changes listener.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-05
     * @since 1.0
     */
    @FunctionalInterface
    @API(status = API.Status.STABLE, since = "1.0")
    interface Listener {

        /**
         * Callback for configuration change events.
         * 
         * @param config The modified configuration instance.
         * @param keys The modified configuration properties.
         */
        void configurationChanged(
                @NotNull MutableConfig config,
                @NotNull Set<String> keys);
    }

    /**
     * Utility class for mutable configuration events handling.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-05
     * @since 1.0
     */
    @API(status = API.Status.INTERNAL, since = "1.0")
    class EventsHandler {

        /** The listeners of configuration property changes. */
        private final @NotNull List<@NotNull Listener> listeners =
                new ArrayList<>();

        /**
         * Creates a new instance.
         */
        public EventsHandler() {
            super();
        }

        /**
         * Returns the list of registered configuration changed event listeners.
         * 
         * @return The list of registered configuration changed event listeners.
         */
        protected @NotNull List<@NotNull Listener> getListeners() {
            return this.listeners;
        }

        /**
         * Registers the specified configuration changed event listener.
         * 
         * @param listener The configuration changed event listener.
         */
        public void add(
                final @NotNull Listener listener) {
            this.listeners.add(listener);
        }

        /**
         * Unregisters the specified configuration changed event listener.
         * 
         * @param listener The configuration changed event listener.
         * @return If the specified listener was found and unregistered.
         */
        public boolean remove(
                final @NotNull Listener listener) {
            return this.listeners.remove(listener);
        }


        /**
         * Calls all registered configuration properties changed event callbacks.
         * 
         * @param instance The modified configuration instance.
         * @param keys The changed properties.
         */
        public void notify(
                final @NotNull MutableConfig instance,
                final @NotNull String... keys) {
            if (!this.listeners.isEmpty()) {
                notify(instance, Stream.of(keys).collect(Collectors.toSet()));
            }
        }

        /**
         * Calls all registered configuration properties changed event callbacks.
         * 
         * @param instance The modified configuration instance.
         * @param keys The changed properties.
         */
        public synchronized void notify(
                final @NotNull MutableConfig instance,
                final @NotNull Set<String> keys) {
            if (!this.listeners.isEmpty()) {
                final Set<String> unmodif = Collections.unmodifiableSet(keys);
                for (final Listener listener: this.listeners) {
                    listener.configurationChanged(instance, unmodif);
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hash(this.listeners);
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
            final EventsHandler other = (EventsHandler) obj;
            return super.equals(obj)
                    && Objects.equals(this.listeners, other.listeners);
        }
    }
}
