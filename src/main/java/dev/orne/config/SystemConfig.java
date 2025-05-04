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

import java.util.stream.Stream;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;

/**
 * Implementation of {@code Config} based on the system properties.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2019-07
 * @version 2.0, 2025-04
 * @since 0.1
 * @see Config
 * @see System#getProperties()
 */
@API(status = API.Status.STABLE, since = "1.0")
public class SystemConfig
implements Config {

    /**
     * Protected constructor for extension.
     * Use 
     */
    protected SystemConfig() {
        super();
    }

    /**
     * Returns the shared system properties based configuration instance.
     * 
     * @return The shared instance.
     */
    public @NotNull SystemConfig getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(
            final @NotBlank String key) {
        return System.getProperties().contains(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Stream<String> getKeys() {
        return System.getProperties().stringPropertyNames().stream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return System.getProperties().isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(
            final @NotBlank String key) {
        return System.getProperty(Validate.notBlank(
                key,
                "Parameter key must be a non blank string"));
    }

    /**
     * Singleton instance holder for lazy instance creation.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-04
     */
    @API(status = API.Status.INTERNAL, since = "1.0")
    private static final class SingletonHolder {

        /** Shared instance. */
        private static final SystemConfig INSTANCE = new SystemConfig();

        /**
         * No instances allowed.
         */
        private SingletonHolder() {
            // Utility class
        }
    }
}
