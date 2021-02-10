package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({ TYPE })
/**
 * Sets the configuration preferences for the class.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 2.0
 * @since 1.0, 2019-07
 * @see Config
 */
public @interface ConfigurationOptions {

    /**
     * @return The preferred configuration classes.
     */
    Class<? extends Config>[] preferredConfigs() default {};

    /**
     * @return {@code true} if the default configuration should be used if one of the
     * preferred ones is not found.
     */
    boolean fallbackToDefaultConfig() default true;

    /**
     * @return {@code true} if the class properties should be configured
     * automatically.
     */
    boolean configureProperties() default true;

    /**
     * @return {@code true} if the class properties that implement {@code Configurable}
     * should be configured automatically.
     */
    boolean configureNestedBeans() default true;
}
