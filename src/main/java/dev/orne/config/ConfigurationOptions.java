package dev.orne.config;

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
