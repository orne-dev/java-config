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
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
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
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
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
