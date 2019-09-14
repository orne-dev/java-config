/**
 * 
 */
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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code PropertiesConfig}.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 */
@Tag("ut")
class PropertiesConfigTest {

	private static final String TEST_KEY = "test.key";

	/**
	 * Test method for {@link PropertiesConfig#PropertiesConfig(Object...)} with
	 * no sources.
	 */
	@Test
	public void testEmptyConstructor() {
		final PropertiesConfig config = new PropertiesConfig();
		assertNotNull(config.getProperties());
		assertTrue(config.getProperties().isEmpty());
	}

	/**
	 * Test method for {@link PropertiesConfig#containsParameter(String)} with
	 * non existent property.
	 */
	@Test
	public void testContainsParameterFalse() {
		final PropertiesConfig config = new PropertiesConfig();
		assertFalse(config.containsParameter(TEST_KEY));
	}

	/**
	 * Test method for {@link PropertiesConfig#containsParameter(String)} with
	 * existent property.
	 */
	@Test
	public void testContainsParameterTrue() {
		final PropertiesConfig config = new PropertiesConfig();
		config.getProperties().setProperty(TEST_KEY, "somaValue");
		assertTrue(config.containsParameter(TEST_KEY));
	}

	/**
	 * Test method for {@link PropertiesConfig#containsParameter(String)} with
	 * existent {@code null} property.
	 */
	@Test
	public void testContainsParameterTrueNull() {
		final PropertiesConfig config = new PropertiesConfig();
		config.getProperties().setProperty(TEST_KEY, PropertiesConfig.NULL);
		assertTrue(config.containsParameter(TEST_KEY));
	}

	/**
	 * Test method for {@link PropertiesConfig#getStringParameter(String)} with
	 * non existent property.
	 */
	@Test
	public void testGetStringMissing() {
		System.clearProperty(TEST_KEY);
		final PropertiesConfig config = new PropertiesConfig();
		final String result = config.getStringParameter(TEST_KEY);
		assertNull(result);
	}

	/**
	 * Test method for {@link PropertiesConfig#getStringParameter(String)} with
	 * existent {@code null} property.
	 */
	@Test
	public void testGetStringNull() {
		final PropertiesConfig config = new PropertiesConfig();
		config.getProperties().setProperty(TEST_KEY, PropertiesConfig.NULL);
		final String result = config.getStringParameter(TEST_KEY);
		assertNull(result);
	}

	/**
	 * Test method for {@link PropertiesConfig#getStringParameter(String)} with
	 * existent non {@code null} property.
	 */
	@Test
	public void testGetString() {
		final String expectedValue = "customValue";
		final PropertiesConfig config = new PropertiesConfig();
		config.getProperties().setProperty(TEST_KEY, expectedValue);
		final String result = config.getStringParameter(TEST_KEY);
		assertNotNull(result);
		assertEquals(expectedValue, result);
	}
}
