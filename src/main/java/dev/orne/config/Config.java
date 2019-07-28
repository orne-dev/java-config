/**
 * 
 */
package dev.orne.config;

import java.time.Instant;

/**
 * Interface for classes containing configuration values.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 */
public interface Config {

	/**
	 * Returns {@code true} if the parameter with the key passed as argument
	 * has been configured.
	 * 
	 * @param key The key of the configuration parameter
	 * @return Returns {@code true} if the parameter has been configured
	 */
	boolean contains(String key);

	/**
	 * Returns the value of the configuration parameter.
	 * 
	 * @param key The key of the configuration parameter
	 * @param type The target type of the parameter
	 * @return The configuration parameter value converted to the target type
	 */
	<T> T get(String key, Class<T> type);

	/**
	 * Returns the value of the configuration parameter as {@code Boolean}.
	 * 
	 * @param key The key of the configuration parameter
	 * @return The configuration parameter value as {@code Boolean}
	 */
	Boolean getBoolean(String key);

	/**
	 * Returns the value of the configuration parameter as {@code String}.
	 * 
	 * @param key The key of the configuration parameter
	 * @return The configuration parameter value as {@code String}
	 */
	String getString(String key);

	/**
	 * Returns the value of the configuration parameter as {@code Number}.
	 * 
	 * @param key The key of the configuration parameter
	 * @return The configuration parameter value as {@code Number}
	 */
	Number getNumber(String key);

	/**
	 * Returns the value of the configuration parameter as {@code Instant}.
	 * 
	 * @param key The key of the configuration parameter
	 * @return The configuration parameter value as {@code Instant}
	 */
	Instant getInstant(String key);
}
