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
extends AbstractMutableConfigBuilderImpl<PreferencesMutableConfigBuilder>
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
