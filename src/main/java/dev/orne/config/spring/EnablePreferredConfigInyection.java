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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.springframework.context.annotation.Import;

import dev.orne.config.Config;
import dev.orne.config.PreferredConfig;

/**
 * Annotation for enable injection of available {@code Config} instances
 * based on {@code PreferredConfig} annotations. 
 * <p>
 * Example:
 * <pre>
 * {@literal @}Configuration
 * {@literal @}EnablePreferredConfigInyection
 * class AppConfig {
 *     ...
 *     {@literal @}Bean
 *     public AltConfig altConfig() {
 *         return Config.as(
 *             ...,
 *             AltConfig.class);
 *     }
 *     ...
 *     {@literal @}Bean
 *     public MyComponent myComponent(Config config) {
 *         // Will be injected with default Spring environment based Config instance
 *         ...
 *     }
 *     ...
 *     {@literal @}Bean
 *     public MyComponent myComponent(
 *             {@literal @}PreferredConfig(AltConfig.class) Config config) {
 *         // Will be injected with altConfig() instance
 *         ...
 *     }
 *     ...
 * }
 * </pre>
 * <p>
 * See {@link ConfigProviderCustomizer} for details on the
 * {@code ConfigProvider} configuration and how to customize it.
 * <p>
 * Specifying preferred configuration(s) is also supported in additional
 * {@code @Autowired} capable injection points, allowing to use in
 * scanned components as well.
 * <p>
 * Example:
 * <pre>
 * {@literal @}Component
 * class MyComponent {
 *     ...
 *     {@literal @}Autowired
 *     public inyectedByMethod(
 *             {@literal @}PreferredConfig(AltConfig.class) Config config) {
 *         ...
 *     }
 *     ...
 * }
 * </pre>
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-11
 * @since 1.0
 * @see Config
 * @see PreferredConfig
 * @see ConfigProviderCustomizer
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Import(ConfigAutowireCandidateResolverConfigurer.class)
@API(status = API.Status.STABLE, since = "1.0")
public @interface EnablePreferredConfigInyection {
    // No extra attributes
}
