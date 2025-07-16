package dev.orne.config.impl;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.PreferencesMutableConfigBuilder;

/**
 * Implementation of {@code Preferences} based mutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see PreferencesMutableConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class PreferencesMutableConfigBuilderImpl
extends AbstractMutableConfigBuilderImpl<PreferencesMutableConfigBuilderImpl>
implements PreferencesMutableConfigBuilder {

    /** The preferences based configuration options. */
    protected final @NotNull PreferencesConfigOptions preferencesOptions;

    /**
     * Copy constructor.
     * 
     * @param options The configuration options to copy.
     * @param mutableOptions The mutable configuration options to copy.
     * @param preferencesOptions The preferences based configuration options to
     * copy.
     */
    protected PreferencesMutableConfigBuilderImpl(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions,
            final @NotNull PreferencesConfigOptions preferencesOptions) {
        super(options, mutableOptions);
        this.preferencesOptions = new PreferencesConfigOptions(preferencesOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PreferencesMutableConfigImpl build() {
        return new PreferencesMutableConfigImpl(
                this.options,
                this.mutableOptions,
                this.preferencesOptions);
    }
}
