package dev.orne.config.impl;

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
implements PreferencesConfigNodeBuilder, PreferencesConfigBuilder {

    /** The preferences based configuration options. */
    protected final @NotNull PreferencesConfigOptions preferencesOptions;

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
    public @NotNull PreferencesConfigBuilder ofNode(
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
