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

import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

import dev.orne.config.ConfigException;
import dev.orne.config.MutableConfig;
import dev.orne.config.PreferencesMutableConfig;

/**
 * Implementation of {@code MutableConfig} based on Java {@code Preferences}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 0.1
 * @see MutableConfig
 * @see Preferences
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class PreferencesMutableConfigImpl
extends PreferencesConfigImpl
implements PreferencesMutableConfig, PreferenceChangeListener {

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param mutableOptions The mutable configuration builder options.
     * @param preferencesOptions The preferences based configuration builder options.
     */
    public PreferencesMutableConfigImpl(
            final ConfigOptions options,
            final MutableConfigOptions mutableOptions,
            final PreferencesConfigOptions preferencesOptions) {
        super(options, mutableOptions, preferencesOptions);
        getPreferences().addPreferenceChangeListener(this);
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
        try {
            getPreferences().put(key, value);
        } catch (final IllegalStateException ise) {
            throw new ConfigException("Error setting configuration property value", ise);
        }
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
            try {
                getPreferences().remove(key);
            } catch (final IllegalStateException ise) {
                throw new ConfigException("Error removing configuration property", ise);
            }
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
    public void preferenceChange(
            final PreferenceChangeEvent evt) {
        getResolver().ifPresent(r -> r.clearCache());
        try {
            getEvents().notify(this, evt.getKey());
        } catch (final IllegalStateException e) {
            throw new ConfigException("Error accessing configuration", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void notifyLocalChanges(
            final String... keys) {
        // Prevent duplicated notifications for local changes later notified
        // by the preferences change listener.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sync() {
        try {
            getPreferences().sync();
        } catch (final BackingStoreException e) {
            throw new ConfigException("Error synchronizing preferences.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        try {
            getPreferences().flush();
        } catch (final BackingStoreException e) {
            throw new ConfigException("Error flushing preferences.", e);
        }
    }
}
