package dev.orne.config;

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
