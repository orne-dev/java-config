/**
 * 
 */
package dev.orne.config;

/**
 * Generic interface for the bean configurator.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 */
public interface Configurer {

	/**
	 * Configures the bean passed as argument.
	 * 
	 * @param bean The bean to configure
	 */
	void configure(Configurable bean);
}
