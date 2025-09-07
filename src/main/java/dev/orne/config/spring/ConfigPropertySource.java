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
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apiguardian.api.API;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import dev.orne.config.Config;

/**
 * Annotation that adds a
 * {@link org.springframework.core.env.PropertySource PropertySource} to Spring's
 * {@link org.springframework.core.env.Environment Environment} that will use
 * the specified {@code Config} context bean.
 * To be used @{@link Configuration} classes in conjunction with
 * {@link ConfigPropertySourcesConfigurer} interface.
 * <p>
 * Example:
 * <pre>
 * {@literal @}Configuration
 * {@literal @}ConfigPropertySource("myConfig")
 * public class AppConfig implements ConfigPropertySourcesConfigurer {
 *    // ...
 *    {@literal @}Bean
 *    public Config myConfig() {
 *        // ...
 *    }
 *    // ...
 * }
 * // Environment.getProperty() will now resolve properties from "myConfig" {@code Config} bean.
 * </pre>
 * <p>
 * NOTE: This annotation is repeatable according to Java 8 conventions.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-08
 * @since 1.0
 * @see PropertySource
 * @see ConfigPropertySourcesConfigurer
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Repeatable(ConfigPropertySources.class)
@API(status = API.Status.STABLE, since = "1.0")
public @interface ConfigPropertySource {

    /**
     * The type of the configuration to use as property source.
     * <p>
     * Alias for {@link #type()}.
     * 
     * @return The configuration type.
     */
    Class<? extends Config> value() default Config.class;

    /**
     * The bean name of the configuration to use as property source.
     * 
     * @return The configuration bean name.
     */
    String name() default "";

    /**
     * The type of the configuration to use as property source.
     * 
     * @return The configuration type.
     */
    Class<? extends Config> type() default Config.class;

    /**
     * Whether to ignore if no bean of the specified type or name is found.
     * <p>
     * If set to {@code true}, and no matching bean is found, the property source
     * will not be added to the environment, but no error will be raised.
     * <p>
     * Default is {@code false}, which will raise an exception if no matching
     * bean is found.
     * 
     * @return Whether to ignore if no matching bean is found.
     */
    boolean ignoreConfigNotFound() default false;
}
