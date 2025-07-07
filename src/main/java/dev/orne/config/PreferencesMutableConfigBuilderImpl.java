package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

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
     * Empty constructor.
     */
    protected PreferencesMutableConfigBuilderImpl() {
        super();
        this.preferencesOptions = new PreferencesConfigOptions();
    }

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
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    protected PreferencesMutableConfigBuilderImpl(
            final @NotNull AbstractConfigBuilderImpl<?> copy) {
        super(copy);
        if (copy instanceof PreferencesConfigBuilderImpl) {
            this.preferencesOptions = new PreferencesConfigOptions(
                    ((PreferencesConfigBuilderImpl) copy).preferencesOptions);
        } else if (copy instanceof PreferencesMutableConfigBuilderImpl) {
            this.preferencesOptions = new PreferencesConfigOptions(
                    ((PreferencesMutableConfigBuilderImpl) copy).preferencesOptions);
        } else {
            this.preferencesOptions = new PreferencesConfigOptions();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PreferencesMutableConfig build() {
        return new PreferencesMutableConfig(
                this.options,
                this.mutableOptions,
                this.preferencesOptions);
    }
}
