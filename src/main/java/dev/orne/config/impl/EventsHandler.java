package dev.orne.config.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.MutableConfig;
import dev.orne.config.WatchableConfig.Listener;

/**
 * Utility class for watchable configuration events handling.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public
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