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

import dev.orne.config.EnvironmentConfigBuilder;

/**
 * Implementation of environment variables based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see EnvironmentConfigImpl
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class EnvironmentConfigBuilderImpl
extends AbstractConfigBuilderImpl<EnvironmentConfigBuilder>
implements EnvironmentConfigBuilder {

    /**
     * Empty constructor.
     */
    public EnvironmentConfigBuilderImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull EnvironmentConfigImpl build() {
        return new EnvironmentConfigImpl(this.options);
    }
}
