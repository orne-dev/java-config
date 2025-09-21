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

import java.util.Map;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.Config;

/**
 * Implementation of {@code Config} based on the environment variables.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 0.1
 * @see Config
 * @see System#getenv()
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class EnvironmentConfigImpl
extends AbstractConfig {

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     */
    public EnvironmentConfigImpl(
            final @NotNull ConfigOptions options) {
        super(options);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEmptyInt() {
        return getEnvironmentVariables().isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean containsInt(
            final @NotBlank String key) {
        return getEnvironmentVariables().containsKey(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull Stream<String> getKeysInt() {
        return getEnvironmentVariables().keySet()
                .stream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getInt(
            final @NotBlank String key) {
        return getEnvironmentVariables().get(key);
    }

    /**
     * Returns the environment variables.
     * 
     * @return The environment variables.
     * @see System#getenv()
     */
    protected @NotNull Map<String, String> getEnvironmentVariables() {
        return System.getenv();
    }
}
