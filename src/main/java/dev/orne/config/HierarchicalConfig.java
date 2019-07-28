/**
 * 
 */
package dev.orne.config;

/**
 * Interface for classes containing configuration values that
 * delegate in parent instances for missing parameters.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 */
public interface HierarchicalConfig
extends Config {

	/**
	 * Returns the parent instance.
	 * 
	 * @return The parent instance
	 */
	Config getParent();
}
