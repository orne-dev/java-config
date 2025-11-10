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
import org.springframework.core.env.Environment;

/**
 * Options of Spring {@code Environment} based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-09
 * @since 1.0
 * @see SpringEnvironmentConfigImpl
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class SpringEnvironmentConfigOptions {

    /** The Spring environment. */
    private @NotNull Environment environment;
    /** If the configuration instance must support property keys iteration. */
    private boolean iterableKeys;

    /**
     * Empty constructor.
     */
    public SpringEnvironmentConfigOptions() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public SpringEnvironmentConfigOptions(
            final @NotNull SpringEnvironmentConfigOptions copy) {
        super();
        this.environment = copy.environment;
        this.iterableKeys = copy.iterableKeys;
    }

    /**
     * Returns the Spring environment.
     * 
     * @return The Spring environment.
     */
    public @NotNull Environment getEnvironment() {
        return this.environment;
    }

    /**
     * Sets the Spring environment.
     * 
     * @param env The Spring environment.
     */
    public void setEnvironment(
            final Environment env) {
        this.environment = env;
    }
    /**
     * Returns if the configuration instance must support property keys
     * iteration.
     * 
     * @return {@code true} if the configuration instance must support
     *         property keys iteration, {@code false} otherwise.
     */
    public boolean isIterableKeys() {
        return this.iterableKeys;
    }

    /**
     * Sets if the configuration instance must support property keys
     * iteration.
     * <p>
     * Note that extracting property keys from the
     * {@code EnumerablePropertySources} of the environment can be an
     * expensive operation, so this option is {@code false} by
     * default.
     * 
     * @param iterable {@code true} if the configuration instance must
     *        support property keys iteration, {@code false} otherwise.
     */
    public void setIterableKeys(
            final boolean iterable) {
        this.iterableKeys = iterable;
    }
}
