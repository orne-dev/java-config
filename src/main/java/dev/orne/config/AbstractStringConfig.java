/**
 * 
 */
package dev.orne.config;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * <p>Partial implementation of {@code Config} for implementations with
 * configuration values stored as {@code String}.</p>
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 2019-07
 * @see Config
 */
public abstract class AbstractStringConfig
extends AbstractConfig {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Boolean getBooleanParameter(final String key) {
		final String strValue = getStringParameter(key);
		return strValue == null ? null : Boolean.valueOf(strValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Number getNumberParameter(final String key) {
		final String strValue = getStringParameter(key);
		return strValue == null ? null : new BigDecimal(strValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Instant getInstantParameter(final String key) {
		final String strValue = getStringParameter(key);
		return strValue == null ? null : Instant.parse(strValue);
	}
}
