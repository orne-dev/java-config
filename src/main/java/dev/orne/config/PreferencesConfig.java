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
import java.util.prefs.Preferences;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Implementation of {@code Config} based on Java {@code Preferences}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2019-07
 * @version 2.0, 2025-05
 * @since 0.1
 * @see Config
 * @see Preferences
 */
@API(status = API.Status.STABLE, since = "1.0")
public class PreferencesConfig
extends AbstractConfig {

    /** The preferences node to use as storage of configuration properties. */
    private final @NotNull Preferences preferences;

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param preferencesOptions The preferences based configuration builder options.
     */
    @API(status = API.Status.INTERNAL, since = "1.0")
    public PreferencesConfig(
            final @NotNull ConfigOptions options,
            final @NotNull PreferencesConfigOptions preferencesOptions) {
        super(options);
        Objects.requireNonNull(preferencesOptions);
        this.preferences = Objects.requireNonNull(preferencesOptions.getPreferences());
    }

    /**
     * Creates a new instance.
     * 
     * @param parent The parent {@code Config} instance.
     * @param decoder The configuration properties values decoder.
     * @param decorator The configuration properties values decorator.
     * @param preferences The preferences node to use as storage of
     * configuration properties.
     */
    public PreferencesConfig(
            final Config parent,
            final @NotNull ValueDecoder decoder,
            final @NotNull ValueDecorator decorator,
            final @NotNull Preferences preferences) {
        super(parent, decoder, decorator);
        this.preferences = Objects.requireNonNull(preferences);
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
}
