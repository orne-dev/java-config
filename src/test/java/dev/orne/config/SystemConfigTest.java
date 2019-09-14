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
