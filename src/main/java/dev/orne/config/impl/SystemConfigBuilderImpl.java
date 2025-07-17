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

import dev.orne.config.SystemConfigBuilder;

/**
 * Implementation of {@code System} properties configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see SystemConfigImpl
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class SystemConfigBuilderImpl
extends AbstractConfigBuilderImpl<SystemConfigBuilderImpl>
implements SystemConfigBuilder<SystemConfigBuilderImpl> {

    /**
     * Empty constructor.
     */
    public SystemConfigBuilderImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull SystemConfigImpl build() {
        return new SystemConfigImpl(this.options);
    }
}
