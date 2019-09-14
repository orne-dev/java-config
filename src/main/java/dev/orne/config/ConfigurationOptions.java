package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 * @see Config
 */
public @interface ConfigurationOptions {

	/**
	 * The preferred configuration classes.
	 */
	Class<? extends Config>[] preferedConfigs() default {};

	/**
	 * {@code true} if the default configuration should be used if one of the
	 * preferred ones is not found.
	 */
	boolean fallbackToDefaultConfig() default true;

	/**
	 * {@code true} if the class properties should be configured
	 * automatically.
	 */
	boolean configureProperties() default true;

	/**
	 * {@code true} if the class properties that implement {@code Configurable}
	 * should be configured automatically.
	 */
	boolean configureNestedBeans() default true;
}
