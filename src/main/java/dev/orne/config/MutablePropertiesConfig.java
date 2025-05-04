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

import java.util.Properties;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;

/**
 * Implementation of {@code MutableConfig} based on {@code Properties} files.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-04
 * @since 1.0
 * @see MutableConfig
 * @see Properties
 */
@API(status = API.Status.STABLE, since = "1.0")
public class MutablePropertiesConfig
extends PropertiesConfig
implements WatchableConfig {

    /** The configuration change events handler. */
    private final @NotNull EventsHandler events;

    /**
     * Creates a new instance with the configuration parameters loaded from
     * the sources passed as argument.
     * <p>
     * Supports {@code String} class path resources,
     * files as {@code Path} or {@code File} instances,
     * {@code URL} or {@code Iterable} collections of any of them.
     * 
     * @param sources The sources to load the configuration parameters from.
     */
    public MutablePropertiesConfig(
            final @NotNull Object... sources) {
        this(new Properties(), new EventsHandler(), sources);
    }

    /**
     * Creates a new instance with the configuration parameters loaded from
     * the sources passed as argument.
     * <p>
     * Supports {@code String} class path resources,
     * files as {@code Path} or {@code File} instances,
     * {@code URL} or {@code Iterable} collections of any of them.
     * 
     * @param config The {@code Properties} instance to use as inner container.
     * @param events The configuration change events handler.
     * @param sources The sources to load the configuration parameters from.
     */
    protected MutablePropertiesConfig(
            final @NotNull Properties config,
            final @NotNull EventsHandler events,
            final @NotNull Object... sources) {
        super(config, sources);
        this.events = Objects.requireNonNull(events);
    }

    /**
     * Returns the configuration change events handler.
     * 
     * @return The configuration change events handler.
     */
    protected @NotNull EventsHandler getEvents() {
        return this.events;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void set(
            final @NotBlank String key,
            final String value) {
        Validate.notBlank(key, KEY_BLANK_ERR);
        if (value == null) {
            remove(key);
        } else {
            getProperties().setProperty(key, value);
        }
        this.events.notify(this, key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void remove(
            final @NotBlank String... keys) {
        for (final String key : keys) {
            Validate.notBlank(key, KEY_BLANK_ERR);
            getProperties().remove(key);
        }
        this.events.notify(this, keys);
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
        final MutablePropertiesConfig other = (MutablePropertiesConfig) obj;
        return super.equals(obj)
                && Objects.equals(this.events, other.events);
    }
}
