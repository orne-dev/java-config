package dev.orne.config.commons;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2020 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.apache.commons.configuration2.Configuration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.ConfigException;

/**
 * Unit tests for {@code CommonsMutableConfig}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see CommonsMutableConfig
 */
@Tag("ut")
class CommonsMutableConfigTest {

    private static final String TEST_KEY = "test.key";

    /**
     * Test method for {@link CommonsMutableConfig#CommonsMutableConfig(Configuration)}.
     */
    @Test
    void testConstructor() {
        final Configuration delegated = mock(Configuration.class);
        final CommonsMutableConfig config = new CommonsMutableConfig(delegated);
        assertSame(delegated, config.getConfig());
    }

    /**
     * Test method for {@link CommonsMutableConfig#CommonsMutableConfig(Configuration)}
     * with {@code null} parameter.
     */
    @Test
    void testConstructorNull() {
        assertThrows(NullPointerException.class, () -> {
            new CommonsMutableConfig(null);
        });
    }

    /**
     * Test method for {@link CommonsMutableConfig#set(String, Object)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testSet() throws ConfigException {
        final Configuration delegated = mock(Configuration.class);
        final CommonsMutableConfig config = new CommonsMutableConfig(delegated);
        
        final Object value = new Object();
        config.set(TEST_KEY, value);
        
        then(delegated).should(times(1)).setProperty(TEST_KEY, value);
    }

    /**
     * Test method for {@link CommonsMutableConfig#remove(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testRemove() throws ConfigException {
        final Configuration delegated = mock(Configuration.class);
        final CommonsMutableConfig config = new CommonsMutableConfig(delegated);
        
        config.remove(TEST_KEY);
        
        then(delegated).should(times(1)).clearProperty(TEST_KEY);
    }
}
