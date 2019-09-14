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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code SystemConfig}.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 */
@Tag("ut")
class SystemConfigTest {

	private static final String TEST_KEY = "test.key";

	private static String propOriginalValue;

	/**
	 * Saves the value of the test property.
	 */
	@BeforeAll
	public static void backupSystemPropertyValue() {
		propOriginalValue = System.getProperty(TEST_KEY);
	}

	/**
	 * Restores the original value of the test property or
	 * removes it if not set.
	 */
	@AfterAll
	public static void restoreSystemPropertyValue() {
		if (propOriginalValue == null) {
			System.clearProperty(TEST_KEY);
		} else {
			System.setProperty(TEST_KEY, propOriginalValue);
		}
	}

	/**
	 * Test method for {@link SystemConfig#containsParameter(String)} with {@code null}
	 * system property.
	 */
	@Test
	public void testContainsParameterFalse() {
		System.clearProperty(TEST_KEY);
		final SystemConfig config = new SystemConfig();
		
		assertFalse(config.containsParameter(TEST_KEY));
	}

	/**
	 * Test method for {@link SystemConfig#containsParameter(String)} with non
	 * {@code null} system property.
	 */
	@Test
	public void testContainsParameterTrue() {
		System.setProperty(TEST_KEY, "somaValue");
		final SystemConfig config = new SystemConfig();
		
		assertTrue(config.containsParameter(TEST_KEY));
	}

	/**
	 * Test method for {@link SystemConfig#getStringParameter(String)} with
	 * {@code null} system property.
	 */
	@Test
	public void testGetStringNull() {
		System.clearProperty(TEST_KEY);
		final SystemConfig config = new SystemConfig();
		
		final String result = config.getStringParameter(TEST_KEY);
		assertNull(result);
	}

	/**
	 * Test method for {@link SystemConfig#getStringParameter(String)} with non
	 * {@code null} system property.
	 */
	@Test
	public void testGetString() {
		final String expectedValue = "customValue";
		System.setProperty(TEST_KEY, expectedValue);
		final SystemConfig config = new SystemConfig();
		
		final String result = config.getStringParameter(TEST_KEY);
		assertNotNull(result);
		assertEquals(expectedValue, result);
	}
}
