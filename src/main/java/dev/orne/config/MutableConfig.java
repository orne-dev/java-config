/**
 * 
 */
package dev.orne.config;

/**
 * Interface for classes containing configuration values mutable at runtime.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 * @see Config
 */
public interface MutableConfig
extends Config {

    /**
     * Sets the value of the configuration parameter.
	 * 
	 * @param key The key of the configuration parameter
	 * @param value The value to set
     */
    void set(String key, Object value);

    /**
     * Removes the value of the configuration parameter.
	 * 
	 * @param key The key of the configuration parameter
     */
    void remove(String key);
}
