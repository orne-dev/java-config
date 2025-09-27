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

/**
 * Container annotation that aggregates several {@link ConfigPropertySource}
 * annotations.
 * <p>
 * Can be used natively, declaring several nested {@link ConfigPropertySource}
 * annotations.
 * Can also be used in conjunction with Java 8's support for <em>repeatable
 * annotations</em>, where {@link ConfigPropertySource} can simply be declared
 * several times on the same {@linkplain ElementType#TYPE type}, implicitly
 * generating this container annotation.
 *
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-08
 * @since 1.0
 * @see ConfigPropertySource
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@API(status = API.Status.STABLE, since = "1.0")
public @interface ConfigPropertySources {

    /**
     * The aggregated {@link ConfigPropertySource} annotations.
     * 
     * @return The aggregated {@link ConfigPropertySource} annotations.
     */
    ConfigPropertySource[] value();
}
