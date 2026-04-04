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

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.configuration2.event.Event;
import org.apache.commons.configuration2.event.EventSource;
import org.apache.commons.configuration2.reloading.ReloadingEvent;
import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

import dev.orne.config.WatchableConfig;

/**
 * Implementation of {@code MutableConfig} based on Apache Commons
 * {@code Configuration}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2020-09
 * @since 0.2
 * @see Configuration
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class CommonsMutableConfigImpl
extends CommonsConfigImpl
implements WatchableConfig {

    /** If local events must be suppressed. */
    private final boolean localEventsSuppressed;

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param mutableOptions The mutable configuration builder options.
     * @param commonsOptions The Apache Commons based configuration options.
     */
    public CommonsMutableConfigImpl(
            final ConfigOptions options,
            final MutableConfigOptions mutableOptions,
            final CommonsConfigOptions commonsOptions) {
        super(options, mutableOptions, commonsOptions);
        Objects.requireNonNull(commonsOptions);
        if (!(commonsOptions.getDelegated() instanceof Configuration)) {
            throw new IllegalArgumentException(
                    "The delegated configuration must be an mutable instance of Apache Commons Configuration");
        }
        if (commonsOptions.getDelegated() instanceof EventSource) {
            this.localEventsSuppressed = true;
            configureCommonsEvents((EventSource) commonsOptions.getDelegated());
        } else {
            this.localEventsSuppressed = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Configuration getConfig() {
        return (Configuration) super.getConfig();
    }

    /**
     * Returns {@code true} if local events must be suppressed.
     * 
     * @return If local events must be suppressed.
     */
    protected boolean isLocalEventsSuppressed() {
        return this.localEventsSuppressed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(
            final String key,
            final @Nullable String value) {
        super.set(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setInt(
            final String key,
            final String value) {
        getConfig().setProperty(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(
            final String... keys) {
        super.remove(keys);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeInt(
            final String... keys) {
        for (final String key : keys) {
            getConfig().clearProperty(key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(
            final Listener listener) {
        super.addListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(
            final Listener listener) {
        super.removeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void notifyLocalChanges(
            final String... keys) {
        if (!this.localEventsSuppressed) {
            super.notifyLocalChanges(keys);
        }
    }

    /**
     * Configures reception of Apache Commons events.
     * 
     * @param source The Apache Commons events source.
     */
    protected void configureCommonsEvents(
            final EventSource source) {
        final EventSource commonsEvents = (EventSource) getConfig();
        commonsEvents.addEventListener(
                ConfigurationEvent.ADD_PROPERTY,
                this::processCommonsEvent);
        commonsEvents.addEventListener(
                ConfigurationEvent.CLEAR_PROPERTY,
                this::processCommonsEvent);
        commonsEvents.addEventListener(
                ConfigurationEvent.SET_PROPERTY,
                this::processCommonsEvent);
        commonsEvents.addEventListener(
                ConfigurationEvent.CLEAR,
                this::processCommonsEvent);
    }

    /**
     * Processes the specified Apache Commons event.
     * 
     * @param event The Apache Commons event.
     */
    protected void processCommonsEvent(
            final Event event) {
        if (event instanceof ConfigurationEvent) {
            final ConfigurationEvent propEvent = (ConfigurationEvent) event;
            if (propEvent.isBeforeUpdate()) {
                getResolver().ifPresent(VariableResolver::clearCache);
            } else if (propEvent.getPropertyName() != null) {
                getEvents().notify(this, propEvent.getPropertyName());
            }
        } else if (event instanceof ReloadingEvent) {
            getResolver().ifPresent(VariableResolver::clearCache);
        }
    }
}
