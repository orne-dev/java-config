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

import org.apiguardian.api.API;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import dev.orne.config.ConfigException;
import dev.orne.config.SpringEnvironmentConfigBuilder;
import dev.orne.config.SpringEnvironmentConfigInitialBuilder;

/**
 * Implementation of Spring {@code Environment} based immutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-09
 * @since 1.0
 * @see SpringEnvironmentConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class SpringEnvironmentConfigBuilderImpl
extends AbstractConfigBuilderImpl<SpringEnvironmentConfigBuilderImpl>
implements SpringEnvironmentConfigInitialBuilder<SpringEnvironmentConfigBuilderImpl>,
        SpringEnvironmentConfigBuilder<SpringEnvironmentConfigBuilderImpl> {

    /** The Spring Environment configuration options. */
    protected final @NotNull SpringEnvironmentConfigOptions springOptions =
            new SpringEnvironmentConfigOptions();

    /**
     * Creates a new instance.
     */
    public SpringEnvironmentConfigBuilderImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull SpringEnvironmentConfigBuilderImpl ofEnvironment(
            final @NotNull Environment environment) {
        this.springOptions.setEnvironment(Objects.requireNonNull(environment));
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull SpringEnvironmentConfigBuilderImpl withIterableKeys() {
        if (!(this.springOptions.getEnvironment() instanceof ConfigurableEnvironment)) {
            throw new ConfigException(
                    "Iterable property keys support requires a ConfigurableEnvironment");
        }
        this.springOptions.setIterableKeys(true);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull SpringEnvironmentConfigImpl build() {
        return new SpringEnvironmentConfigImpl(
                this.options,
                this.springOptions);
    }
}
