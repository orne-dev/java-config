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

import dev.orne.config.Configurable;

/**
 * Annotation for automatic configuration of {@code Configurable} beans.
 * <p>
 * Example:
 * <pre>
 * class MyComponent implements Configurable {
 *     ...
 * }
 * 
 * {@literal @}Configuration
 * {@literal @}EnableConfigurableComponents
 * class AppConfig {
 *    ...
 *    {@literal @}Bean
 *    public MyComponent myConfigurableComponent() {
 *        ...
 *    }
 *    ...
 * }
 * </pre>
 * <p>
 * See {@link ConfigProviderCustomizer} for details on the
 * {@code ConfigProvider} configuration and how to customize it.
 * <p>
 * By default, the {@code Configurer} used to configure the
 * {@code Configurable} components is not exposed as a bean. To
 * expose it, set the {@code exposeConfigurer} attribute to
 * {@code true}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-08
 * @since 1.0
 * @see Configurable
 * @see ConfigProviderCustomizer
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Import(ConfigurableComponentsConfigurer.class)
@API(status = API.Status.STABLE, since = "1.0")
public @interface EnableConfigurableComponents {

    /**
     * Whether to expose the {@code Configurer} bean used to configure the
     * components.
     * <p>
     * If {@code true}, a bean of type {@code Configurer} will be registered
     * in the application context with the name
     * {@value ConfigurableComponentsConfigurer#CONFIGURER}.
     * <p>
     * Default is {@code false}.
     * 
     * @return Whether to expose the {@code Configurer} bean.
     */
    boolean exposeConfigurer() default false;
}
