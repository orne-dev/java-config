package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code MutableDelegatedConfig}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 */
@Tag("ut")
public class MutableDelegatedConfigTest {

    private static final String TEST_KEY = "test.key";

    /**
     * Test method for {@link MutableDelegatedConfig#MutableDelegatedConfig(MutableConfig)}.
     */
    @Test
    public void testConstructor() {
        final MutableConfig delegated = mock(MutableConfig.class);
        final MutableDelegatedConfig config = new MutableDelegatedConfig(delegated);
        assertSame(delegated, config.getDelegate());
    }

    /**
     * Test method for {@link MutableDelegatedConfig#MutableDelegatedConfig(MutableConfig)}
     * with {@code null} parameter.
     */
    @Test
    public void testConstructorNull() {
        assertThrows(NullPointerException.class, () -> {
            new MutableDelegatedConfig(null);
        });
    }

    /**
     * Test method for {@link MutableDelegatedConfig#set(String, Object)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testSet() throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final MutableDelegatedConfig config = new MutableDelegatedConfig(delegated);
        
        final Object value = new Object();
        
        config.set(TEST_KEY, value);
        
        then(delegated).should(times(1)).set(TEST_KEY, value);
    }

    /**
     * Test method for {@link MutableDelegatedConfig#remove(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    public void testRemove() throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final MutableDelegatedConfig config = new MutableDelegatedConfig(delegated);
        
        config.remove(TEST_KEY);
        
        then(delegated).should(times(1)).remove(TEST_KEY);
    }
}
