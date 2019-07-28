/**
 * 
 */
package dev.orne.config;

import org.apache.commons.lang3.Validate;

/**
 * Implementation of {@code Config} based on the system properties.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 * @see Config
 * @see System#getProperties()
 */
public class SystemConfig
extends AbstractStringConfig {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean containsParameter(final String key) {
		Validate.notNull(key, "Parameter key is required");
		return System.getProperty(key) != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getStringParameter(String key) {
		Validate.notNull(key, "Parameter key is required");
		return System.getProperty(key);
	}
}
