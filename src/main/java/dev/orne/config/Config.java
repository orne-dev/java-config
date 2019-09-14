package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
