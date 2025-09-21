package dev.orne.config;

import javax.validation.constraints.NotNull;

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

import org.apiguardian.api.API;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * Spring {@code Environment} based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-09
 * @param <T> The concrete type of the builder.
 * @since 1.0
 * @see Environment
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface SpringEnvironmentConfigBuilder<T extends SpringEnvironmentConfigBuilder<T>>
extends ConfigBuilder<T> {

    /**
     * Sets this configuration to support property keys iteration.
     * <p>
     * If this method is not called, the resulting configuration
     * will not support property keys iteration, and calling
     * {@link Config#getKeys()} will throw a
     * {@link NonIterableConfigException}.
     * <p>
     * Note that enabling property keys iteration requires a
     * {@link ConfigurableEnvironment} and may have a performance impact,
     * depending on the configured {@code PropertySources}.
     * 
     * @return This instance, for method chaining.
     * @see Config#getKeys()
     */
    @NotNull T withIterableKeys();
}
