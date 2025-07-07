package dev.orne.config;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Base abstract implementation of watchable mutable configuration properties
 * provider.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-04
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0")
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
    @API(status = API.Status.INTERNAL, since = "1.0")
    protected AbstractWatchableConfig(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions) {
        super(options, mutableOptions);
        this.events = new EventsHandler();
        if (getParent() instanceof WatchableConfig) {
            ((WatchableConfig) getParent()).addListener(
                    (config, keys) -> this.events.notify(this, keys));
        }
    }

    /**
     * Creates a new instance.
     * 
     * @param parent The parent {@code Config} instance.
     * @param decoder The configuration properties values decoder.
     * @param encoder The configuration properties values encoder.
     * @param decorator The configuration properties values decorator.
     */
    protected AbstractWatchableConfig(
            final Config parent,
            final @NotNull ValueDecoder decoder,
            final @NotNull ValueEncoder encoder,
            final @NotNull ValueDecorator decorator) {
        super(parent, decoder, encoder, decorator);
        this.events = new EventsHandler();
        if (parent instanceof WatchableConfig) {
            ((WatchableConfig) parent).addListener(
                    (config, keys) -> this.notifyParentChanges(keys));
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
