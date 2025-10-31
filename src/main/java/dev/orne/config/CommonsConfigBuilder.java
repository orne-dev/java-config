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

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apiguardian.api.API;

/**
 * Apache Commons {@code ImmutableConfiguration} based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see ImmutableConfiguration
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface CommonsConfigBuilder
extends MutableCapableConfigBuilder<CommonsConfigBuilder> {

    /**
     * Selects the delegated Apache Commons configuration.
     * 
     * @param delegate The delegated Apache Commons configuration.
     * @return Next builder, for method chaining.
     */
    @NotNull CommonsConfigBuilder ofDelegate(
            @NotNull ImmutableConfiguration delegate);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull CommonsMutableConfigBuilder mutable();
}
