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

import java.util.Objects;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Implementation of {@code MutableConfig} based on Java {@code Preferences}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 0.1
 * @see MutableConfig
 * @see Preferences
 */
@API(status = API.Status.STABLE, since = "1.0")
public class PreferencesMutableConfig
extends AbstractWatchableConfig
implements PreferenceChangeListener {

    /** The preferences node to use as storage of configuration properties. */
    private final @NotNull Preferences preferences;

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param mutableOptions The mutable configuration builder options.
     * @param preferencesOptions The preferences based configuration builder options.
     */
    @API(status = API.Status.INTERNAL, since = "1.0")
    public PreferencesMutableConfig(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions,
            final @NotNull PreferencesConfigOptions preferencesOptions) {
        super(options, mutableOptions);
        Objects.requireNonNull(preferencesOptions);
        this.preferences = Objects.requireNonNull(preferencesOptions.getPreferences());
        this.preferences.addPreferenceChangeListener(this);
    }

    /**
     * Creates a new instance.
     * 
     * @param parent The parent {@code Config} instance.
     * @param decoder The configuration properties values decoder.
     * @param encoder The configuration properties values encoder.
     * @param decorator The configuration properties values decorator.
     * @param preferences The preferences node to use as storage of
     * configuration properties.
     */
    public PreferencesMutableConfig(
            final Config parent,
            final @NotNull ValueDecoder decoder,
            final @NotNull ValueEncoder encoder,
            final @NotNull ValueDecorator decorator,
            final @NotNull Preferences preferences) {
        super(parent, decoder, encoder, decorator);
        this.preferences = Objects.requireNonNull(preferences);
        this.preferences.addPreferenceChangeListener(this);
    }

    /**
     * Returns the preferences node to use as storage of configuration
     * parameters.
     * 
     * @return The preferences node.
     */
    protected @NotNull Preferences getPreferences() {
        return this.preferences;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEmptyInt() {
        try {
            return this.preferences.keys().length == 0;
        } catch (final IllegalStateException | BackingStoreException ise) {
            throw new ConfigException("Error accessing configuration", ise);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean containsInt(@NotBlank String key) {
        try {
            return this.preferences.get(key, null) != null;
        } catch (final IllegalStateException ise) {
            throw new ConfigException("Error accessing configuration property", ise);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull Stream<String> getKeysInt() {
        try {
            return Stream.of(this.preferences.keys());
        } catch (final IllegalStateException | BackingStoreException e) {
            throw new ConfigException("Error accessing configuration", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getInt(@NotBlank String key) {
        try {
            return this.preferences.get(key, null);
        } catch (final IllegalStateException ise) {
            throw new ConfigException("Error retrieving configuration property value", ise);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setInt(
            final @NotBlank String key,
            final @NotNull String value) {
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
    protected void removeInt(
            final @NotBlank String... keys) {
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
    public void preferenceChange(
            final @NotNull PreferenceChangeEvent evt) {
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
            final @NotNull String... keys) {
        // Prevent duplicated notifications for local changes later notified
        // by the preferences change listener.
    }

    /**
     * Synchronizes the configuration properties from the source preferences
     * node.
     * 
     * @throws ConfigException If an error occurs synchronizing the
     * configuration properties.
     * @see Preferences#sync()
     */
    public void sync() {
        try {
            getPreferences().sync();
        } catch (final BackingStoreException e) {
            throw new ConfigException("Error synchronizing preferences.", e);
        }
    }

    /**
     * Saves the current configuration properties to the source preferences
     * node.
     * 
     * @throws ConfigException If an error occurs writing the configuration
     * properties.
     * @see Preferences#flush()
     */
    public void flush() {
        try {
            getPreferences().flush();
        } catch (final BackingStoreException e) {
            throw new ConfigException("Error flushing preferences.", e);
        }
    }
}
