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
import dev.orne.config.Configurable;
import dev.orne.config.ConfigurationOptions;

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
 * {@literal @}EnableConfigurableComponents(type = MyConfig.class)
 * class AppConfig {
 *    ...
 *    {@literal @}Bean
 *    public Config myConfig() {
 *        ...
 *    }
 *    ...
 *    {@literal @}Bean
 *    public MyComponent myConfigurableComponent() {
 *        ...
 *    }
 *    ...
 * }
 * </pre>
 * <p>
 * Allows to specify the default configuration to apply when multiple
 * {@code Config} beans are found in the application context.
 * <p>
 * Example:
 * <pre>
 * class MyComponent implements Configurable {
 *     ...
 * }
 * 
 * {@literal @}Configuration
 * {@literal @}EnableConfigurableComponents(MyConfig.class)
 * class AppConfig {
 *    ...
 *    {@literal @}Bean
 *    public MyConfig myConfig() {
 *        return Config.as(
 *            ...,
 *            MyConfig.class);
 *    }
 *    ...
 *    {@literal @}Bean
 *    public AltConfig altConfig() {
 *        return Config.as(
 *            ...,
 *            AltConfig.class);
 *    }
 *    ...
 *    {@literal @}Bean
 *    public MyComponent myConfigurableComponent() {
 *        ...
 *    }
 *    ...
 * }
 * </pre>
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-08
 * @since 1.0
 * @see Configurable
 * @see ConfigurationOptions
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Import(ConfigurableComponentsConfigurer.class)
@API(status = API.Status.STABLE, since = "1.0")
public @interface EnableConfigurableComponents {

    /**
     * The type of the default configuration to apply to the annotated classes.
     * <p>
     * Alias for {@link #type()}.
     * 
     * @return The default configuration type.
     */
    Class<? extends Config> value() default Config.class;

    /**
     * The bean name of the default configuration to apply to the annotated
     * classes.
     * 
     * @return The default configuration bean name.
     */
    String name() default "";

    /**
     * The type of the default configuration to apply to the annotated classes.
     * 
     * @return The default configuration type.
     */
    Class<? extends Config> type() default Config.class;

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
