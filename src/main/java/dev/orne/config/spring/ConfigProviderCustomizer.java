package dev.orne.config.spring;

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

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.Config;
import dev.orne.config.ConfigProviderBuilder;

/**
 * Interface for available {@code Config} instances customization
 * on current Spring context.
 * Allows customizing the {@code ConfigProvider} used when enabling
 * {@code Config} injection with {@link EnableOrneConfig} or
 * configurable components with {@link EnableConfigurableComponents}.
 * <p>
 * By default a Spring enviromnent based configuration is used as the
 * default configuration, and all other available {@code Config}
 * instances in the Spring context are registered as additional
 * configurations.
 * <p>
 * To customize this behavior implement this interface as a Spring
 * bean, and provide a default {@code Config} instance:
 * <pre>
 * {@literal @}Configuration
 * class MyConfig implements ConfigProviderCustomizer {
 * 
 *     {@literal @}Override
 *     public Config configureDefaultConfig(
 *             final Map&lt;String, Config&gt; configs) {
 *         // Return the desired default configuration
 *     }
 * }
 * </pre>
 * To customize the additional configurations registration
 * override the {@link #registerAdditionalConfigs(ConfigProviderBuilder, Map)}
 * method.
 * <p>
 * Only one implementation of this interface is allowed per Spring context,
 * and it's not inherited by child contexts.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-11
 * @since 1.0
 * @see EnableOrneConfig
 * @see EnableConfigurableComponents
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface ConfigProviderCustomizer {

    /**
     * Configures the default {@code Config} instance.
     * 
     * @param configs The configurations available in the Spring context.
     * @return The default configuration.
     */
    @NotNull Config configureDefaultConfig(
            @NotNull Map<String, Config> configs);

    /**
     * Registers additional {@code Config} instances.
     * 
     * @param registry The registry of additional configurations.
     * @param configs The configurations available in the Spring context.
     */
    default void registerAdditionalConfigs(
            final @NotNull ConfigRegistry registry,
            final @NotNull Map<String, Config> configs) {
        configs.values().forEach(registry::add);
    }

    /**
     * Registry for additional {@code Config} instances.
     */
    @FunctionalInterface
    interface ConfigRegistry {

        /**
         * Adds a new {@code Config} instance.
         * 
         * @param config The configuration to add.
         */
        void add(
                @NotNull Config config);
    }
}
