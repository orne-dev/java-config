package dev.orne.config;

import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2020 Orne Developments
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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.configuration2.event.Event;
import org.apache.commons.configuration2.event.EventSource;
import org.apache.commons.configuration2.reloading.ReloadingEvent;
import org.apiguardian.api.API;

/**
 * Implementation of {@code MutableConfig} based on Apache Commons
 * {@code Configuration}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2020-09
 * @since 0.2
 * @see Configuration
 */
public class CommonsMutableConfig
extends AbstractWatchableConfig {

    /** The delegated Apache Commons configuration. */
    private final @NotNull Configuration config;
    /** If local events must be suppressed. */
    private final boolean localEventsSuppressed;

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param mutableOptions The mutable configuration builder options.
     * @param config The delegated Apache Commons configuration
     */
    @API(status = API.Status.INTERNAL, since = "1.0")
    public CommonsMutableConfig(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions,
            final @NotNull Configuration config) {
        super(options, mutableOptions);
        this.config = Objects.requireNonNull(config);
        if (config instanceof EventSource) {
            this.localEventsSuppressed = true;
            configureCommonsEvents((EventSource) config);
        } else {
            this.localEventsSuppressed = false;
        }
    }

    /**
     * Creates a new instance.
     * 
     * @param parent The parent {@code Config} instance.
     * @param decoder The configuration properties values decoder.
     * @param encoder The configuration properties values encoder.
     * @param decorator The configuration properties values decorator.
     * @param config The delegated Apache Commons configuration
     */
    public CommonsMutableConfig(
            final Config parent,
            final @NotNull ValueDecoder decoder,
            final @NotNull ValueEncoder encoder,
            final @NotNull ValueDecorator decorator,
            final @NotNull Configuration config) {
        super(parent, decoder, encoder, decorator);
        this.config = Objects.requireNonNull(config);
        if (config instanceof EventSource) {
            this.localEventsSuppressed = true;
            configureCommonsEvents((EventSource) config);
        } else {
            this.localEventsSuppressed = false;
        }
    }

    /**
     * Returns the delegated Apache Commons configuration.
     * 
     * @return The delegated Apache Commons configuration
     */
    protected @NotNull Configuration getConfig() {
        return this.config;
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
    protected boolean isEmptyInt() {
        return this.config.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull Stream<String> getKeysInt() {
        final Iterable<String> iterable = this.config::getKeys;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean containsInt(
            final @NotBlank String key) {
        return this.config.containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getInt(
            final @NotBlank String key) {
        return this.config.getString(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setInt(
            final @NotBlank String key,
            final @NotNull String value) {
        this.config.setProperty(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeInt(
            final @NotBlank String... keys) {
        for (final String key : keys) {
            this.config.clearProperty(key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void notifyLocalChanges(
            final @NotNull String... keys) {
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
            final @NotNull EventSource source) {
        final EventSource commonsEvents = (EventSource) config;
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
            final @NotNull Event event) {
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
