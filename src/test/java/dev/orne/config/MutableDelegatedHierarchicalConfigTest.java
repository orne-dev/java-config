package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2021 Orne Developments
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@code MutableDelegatedHierarchicalConfig}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 */
@Tag("ut")
class MutableDelegatedHierarchicalConfigTest {

    private static final String TEST_KEY = "test.key";

    /**
     * Test method for {@link MutableDelegatedHierarchicalConfig#MutableDelegatedHierarchicalConfig(Config, Config)}.
     */
    @Test
    void testConstructor() {
        final MutableConfig delegated = mock(MutableConfig.class);
        final Config parent = mock(Config.class);
        final MutableDelegatedHierarchicalConfig config =
                new MutableDelegatedHierarchicalConfig(delegated, parent);
        assertSame(delegated, config.getDelegate());
        assertSame(parent, config.getParent());
    }

    /**
     * Test method for {@link MutableDelegatedHierarchicalConfig#MutableDelegatedHierarchicalConfig(Config, Config)}
     * with {@code null} {@code parent} parameter.
     */
    @Test
    void testConstructor_NullParent() {
        final MutableConfig delegated = mock(MutableConfig.class);
        final MutableDelegatedHierarchicalConfig config =
                new MutableDelegatedHierarchicalConfig(delegated, null);
        assertSame(delegated, config.getDelegate());
        assertNull(config.getParent());
    }

    /**
     * Test method for {@link MutableDelegatedHierarchicalConfig#MutableDelegatedHierarchicalConfig(Config, Config)}
     * with {@code null} {@code delegated} parameter.
     */
    @Test
    void testConstructor_NullDelegated() {
        final Config parent = mock(Config.class);
        assertThrows(NullPointerException.class, () -> {
            new MutableDelegatedHierarchicalConfig(null, parent);
        });
    }

    /**
     * Test method for {@link MutableDelegatedHierarchicalConfig#MutableDelegatedHierarchicalConfig(Config, Config)}
     * with {@code null} parameters.
     */
    @Test
    void testConstructor_Nulls() {
        assertThrows(NullPointerException.class, () -> {
            new MutableDelegatedHierarchicalConfig(null, null);
        });
    }

    /**
     * Test method for {@link MutableDelegatedHierarchicalConfig#contains(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void testContains(
            final int flags)
    throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final Config parent = mock(Config.class);
        final MutableDelegatedHierarchicalConfig config =
                new MutableDelegatedHierarchicalConfig(delegated, parent);
        
        final boolean delegateContains = (flags & 1) != 0;
        final boolean parentContains = (flags & 2) != 0;
        willReturn(delegateContains).given(delegated).contains(TEST_KEY);
        willReturn(parentContains).given(parent).contains(TEST_KEY);
        
        final boolean result = config.contains(TEST_KEY);
        assertSame(delegateContains || parentContains, result);
        
        then(delegated).should(times(1)).contains(TEST_KEY);
        if (!delegateContains) {
            then(delegated).should(times(1)).contains(TEST_KEY);
        }
    }

    /**
     * Test method for {@link MutableDelegatedHierarchicalConfig#get(String, Class)}.
     * @throws ConfigException Shouldn't happen
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void testGet(
            final int flags)
    throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final Config parent = mock(Config.class);
        final MutableDelegatedHierarchicalConfig config =
                new MutableDelegatedHierarchicalConfig(delegated, parent);
        
        final boolean delegateContains = (flags & 1) != 0;
        final boolean parentContains = (flags & 2) != 0;
        final Object expectedValue = new Object();
        willReturn(delegateContains).given(delegated).contains(TEST_KEY);
        willReturn(delegateContains ? expectedValue : null).given(delegated).get(TEST_KEY, Object.class);
        willReturn(parentContains ? expectedValue : null).given(parent).get(TEST_KEY, Object.class);
        
        final Object result = config.get(TEST_KEY, Object.class);
        if (delegateContains || parentContains) {
            assertSame(expectedValue, result);
        } else {
            assertNull(result);
        }
        
        then(delegated).should(times(1)).contains(TEST_KEY);
        if (delegateContains) {
            then(delegated).should(times(1)).get(TEST_KEY, Object.class);
            then(parent).should(never()).get(TEST_KEY, Object.class);
        } else {
            then(delegated).should(never()).get(TEST_KEY, Object.class);
            then(parent).should(times(1)).get(TEST_KEY, Object.class);
        }
    }

    /**
     * Test method for {@link MutableDelegatedHierarchicalConfig#getBoolean(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void testGetBoolean(
            final int flags)
    throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final Config parent = mock(Config.class);
        final MutableDelegatedHierarchicalConfig config =
                new MutableDelegatedHierarchicalConfig(delegated, parent);
        
        final boolean delegateContains = (flags & 1) != 0;
        final boolean parentContains = (flags & 2) != 0;
        final boolean expectedValue = true;
        willReturn(delegateContains).given(delegated).contains(TEST_KEY);
        willReturn(delegateContains ? expectedValue : null).given(delegated).getBoolean(TEST_KEY);
        willReturn(parentContains ? expectedValue : null).given(parent).getBoolean(TEST_KEY);
        
        final Boolean result = config.getBoolean(TEST_KEY);
        if (delegateContains || parentContains) {
            assertSame(expectedValue, result);
        } else {
            assertNull(result);
        }
        
        then(delegated).should(times(1)).contains(TEST_KEY);
        if (delegateContains) {
            then(delegated).should(times(1)).getBoolean(TEST_KEY);
            then(parent).should(never()).getBoolean(TEST_KEY);
        } else {
            then(delegated).should(never()).getBoolean(TEST_KEY);
            then(parent).should(times(1)).getBoolean(TEST_KEY);
        }
    }

    /**
     * Test method for {@link MutableDelegatedHierarchicalConfig#getNumber(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void testGetNumber(
            final int flags)
    throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final Config parent = mock(Config.class);
        final MutableDelegatedHierarchicalConfig config =
                new MutableDelegatedHierarchicalConfig(delegated, parent);
        
        final boolean delegateContains = (flags & 1) != 0;
        final boolean parentContains = (flags & 2) != 0;
        final Number expectedValue = 15445;
        willReturn(delegateContains).given(delegated).contains(TEST_KEY);
        willReturn(delegateContains ? expectedValue : null).given(delegated).getNumber(TEST_KEY);
        willReturn(parentContains ? expectedValue : null).given(parent).getNumber(TEST_KEY);
        
        final Number result = config.getNumber(TEST_KEY);
        if (delegateContains || parentContains) {
            assertSame(expectedValue, result);
        } else {
            assertNull(result);
        }
        
        then(delegated).should(times(1)).contains(TEST_KEY);
        if (delegateContains) {
            then(delegated).should(times(1)).getNumber(TEST_KEY);
            then(parent).should(never()).getNumber(TEST_KEY);
        } else {
            then(delegated).should(never()).getNumber(TEST_KEY);
            then(parent).should(times(1)).getNumber(TEST_KEY);
        }
    }

    /**
     * Test method for {@link MutableDelegatedHierarchicalConfig#getString(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void testGetString(
            final int flags)
    throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final Config parent = mock(Config.class);
        final MutableDelegatedHierarchicalConfig config =
                new MutableDelegatedHierarchicalConfig(delegated, parent);
        
        final boolean delegateContains = (flags & 1) != 0;
        final boolean parentContains = (flags & 2) != 0;
        final String expectedValue = "mock value";
        willReturn(delegateContains).given(delegated).contains(TEST_KEY);
        willReturn(delegateContains ? expectedValue : null).given(delegated).getString(TEST_KEY);
        willReturn(parentContains ? expectedValue : null).given(parent).getString(TEST_KEY);
        
        final String result = config.getString(TEST_KEY);
        if (delegateContains || parentContains) {
            assertSame(expectedValue, result);
        } else {
            assertNull(result);
        }
        
        then(delegated).should(times(1)).contains(TEST_KEY);
        if (delegateContains) {
            then(delegated).should(times(1)).getString(TEST_KEY);
            then(parent).should(never()).getString(TEST_KEY);
        } else {
            then(delegated).should(never()).getString(TEST_KEY);
            then(parent).should(times(1)).getString(TEST_KEY);
        }
    }
}
