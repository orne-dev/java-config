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

import java.util.prefs.Preferences;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.PreferencesConfigBuilder;
import dev.orne.config.PreferencesConfigNodeBuilder;

/**
 * Implementation of {@code Preferences} based immutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see PreferencesConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class PreferencesConfigBuilderImpl
extends AbstractConfigBuilderImpl<PreferencesConfigBuilderImpl>
implements PreferencesConfigNodeBuilder<PreferencesConfigBuilderImpl>, PreferencesConfigBuilder<PreferencesConfigBuilderImpl> {

    /** The preferences based configuration options. */
    protected final @NotNull PreferencesConfigOptions preferencesOptions;

    /**
     * Creates a new instance.
     */
    public PreferencesConfigBuilderImpl() {
        super();
        this.preferencesOptions = new PreferencesConfigOptions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PreferencesConfigBuilderImpl ofUserRoot() {
        this.preferencesOptions.setUserPreferences();
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PreferencesConfigBuilderImpl ofUser(
            final @NotNull String path) {
        this.preferencesOptions.setUserPreferences(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PreferencesConfigBuilderImpl ofUser(
            final @NotNull Class<?> clazz) {
        this.preferencesOptions.setUserPreferences(clazz);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PreferencesConfigBuilderImpl ofUser(
            final @NotNull Class<?> clazz,
            final @NotNull String path) {
        this.preferencesOptions.setUserPreferences(clazz, path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PreferencesConfigBuilderImpl ofSystemRoot() {
        this.preferencesOptions.setSystemPreferences();
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PreferencesConfigBuilderImpl ofSystem(
            final @NotNull String path) {
        this.preferencesOptions.setSystemPreferences(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PreferencesConfigBuilderImpl ofSystem(
            final @NotNull Class<?> clazz) {
        this.preferencesOptions.setSystemPreferences(clazz);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PreferencesConfigBuilderImpl ofSystem(
            final @NotNull Class<?> clazz,
            final @NotNull String path) {
        this.preferencesOptions.setSystemPreferences(clazz, path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PreferencesConfigBuilderImpl ofNode(
            final @NotNull Preferences preferences) {
        this.preferencesOptions.setPreferences(preferences);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PreferencesMutableConfigBuilderImpl mutable() {
        return new PreferencesMutableConfigBuilderImpl(
                this.options,
                new MutableConfigOptions(),
                this.preferencesOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PreferencesConfigImpl build() {
        return new PreferencesConfigImpl(
                this.options,
                this.preferencesOptions);
    }
}
