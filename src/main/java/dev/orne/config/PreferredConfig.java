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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apiguardian.api.API;

/**
 * Sets the preferred {@code Config} subtype(s).
 * Usable both for {@code Configurable} classes and for Spring injection
 * points.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-11
 * @since 0.1
 * @see Configurable
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER })
@API(status = API.Status.STABLE, since = "1.0")
public @interface PreferredConfig {

    /**
     * Sets the preferred configuration classes.
     * 
     * @return The preferred configuration classes.
     */
    Class<? extends Config>[] value() default {};

    /**
     * Sets if the default configuration should be used if one of the
     * preferred ones is not found.
     * 
     * @return {@code true} if the default configuration should be used.
     */
    boolean fallbackToDefaultConfig() default true;
}
