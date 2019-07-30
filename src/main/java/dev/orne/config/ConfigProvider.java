/**
 * 
 */
package dev.orne.config;

/**
 * Generic interface for {@code Config} providers.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 */
public interface ConfigProvider {

	/**
	 * Returns the default {@code Config} instance.
	 * 
	 * @return The default {@code Config} instance
	 */
	Config getDefaultConfig();

	/**
	 * Returns a suitable {@code Config} instance for the configuration
	 * options passed as argument. The target class is passed as second
	 * argument for implementations that support extra annotations for
	 * configuration options.
	 * 
	 * @param options The configuration options of the target class.
	 * @param targetClass The target class for extra annotation retrieval, if
	 * supported.
	 * @return The selected {@code Config} instance, or {@code null} if no one
	 * is suitable
	 */
	Config selectConfig(
			final ConfigurationOptions options,
			final Class<?> targetClass);
}
