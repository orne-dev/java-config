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

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.Configuration;
import org.apiguardian.api.API;

import dev.orne.config.CommonsMutableConfigBuilder;
import dev.orne.config.MutableConfigBuilder;

/**
 * Implementation of Apache Commons {@code Configuration} based mutable
 * configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see MutableConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class CommonsMutableConfigBuilderImpl
extends AbstractMutableConfigBuilderImpl<CommonsMutableConfigBuilder>
implements CommonsMutableConfigBuilder {

    /** The Apache Commons based configuration options. */
    protected final @NotNull CommonsConfigOptions commonsOptions;

    /**
     * Copy constructor.
     * 
     * @param options The configuration options to copy.
     * @param mutableOptions The mutable configuration options to copy.
     * @param commonsOptions TThe Apache Commons based configuration options to
     * copy.
     */
    public CommonsMutableConfigBuilderImpl(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions,
            final @NotNull CommonsConfigOptions commonsOptions) {
        super(options, mutableOptions);
        this.commonsOptions = new CommonsConfigOptions(Objects.requireNonNull(commonsOptions));
        if (this.commonsOptions.getDelegated() != null
                && !(this.commonsOptions.getDelegated() instanceof Configuration)) {
            throw new IllegalArgumentException(
                    "Delegated configuration must be an instance of "
                            + "org.apache.commons.configuration2.Configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CommonsMutableConfigBuilder ofDelegate(
            final @NotNull Configuration delegate) {
        this.commonsOptions.setDelegated(Objects.requireNonNull(delegate));
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CommonsMutableConfigImpl build() {
        return new CommonsMutableConfigImpl(
                this.options,
                this.mutableOptions,
                this.commonsOptions);
    }
}
