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

import java.util.Iterator;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code DelegatedConfig}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 */
@Tag("ut")
class DelegatedConfigTest {

    private static final String TEST_KEY = "test.key";

    /**
     * Test method for {@link DelegatedConfig#DelegatedConfig(Config)}.
     */
    @Test
    void testConstructor() {
        final Config delegated = mock(Config.class);
        final DelegatedConfig config = new DelegatedConfig(delegated);
        assertSame(delegated, config.getDelegate());
    }

    /**
     * Test method for {@link DelegatedConfig#DelegatedConfig(Config)}
     * with {@code null} parameter.
     */
    @Test
    void testConstructorNull() {
        assertThrows(NullPointerException.class, () -> {
            new DelegatedConfig(null);
        });
    }

    /**
     * Test method for {@link DelegatedConfig#isEmpty()}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testIsEmpty() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedConfig config = new DelegatedConfig(delegated);
        
        final boolean expectedValue = true;
        willReturn(expectedValue).given(delegated).isEmpty();
        
        final boolean result = config.isEmpty();
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).isEmpty();
    }

    /**
     * Test method for {@link DelegatedConfig#getKeys()}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testsGetKeys() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedConfig config = new DelegatedConfig(delegated);
        
        @SuppressWarnings("unchecked")
        final Iterator<String> expectedValue = mock(Iterator.class);
        willReturn(expectedValue).given(delegated).getKeys();
        
        final Iterator<String> result = config.getKeys();
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).getKeys();
    }

    /**
     * Test method for {@link DelegatedConfig#containsParameter(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testContainsParameter() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedConfig config = new DelegatedConfig(delegated);
        
        final boolean expectedValue = true;
        willReturn(expectedValue).given(delegated).contains(TEST_KEY);
        
        final boolean result = config.containsParameter(TEST_KEY);
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).contains(TEST_KEY);
    }

    /**
     * Test method for {@link DelegatedConfig#getParameter(String, Class)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetParameter() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedConfig config = new DelegatedConfig(delegated);
        
        final Object expectedValue = new Object();
        willReturn(expectedValue).given(delegated).get(TEST_KEY, Object.class);
        
        final Object result = config.getParameter(TEST_KEY, Object.class);
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).get(TEST_KEY, Object.class);
    }

    /**
     * Test method for {@link DelegatedConfig#getBooleanParameter(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetBooleanParameter() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedConfig config = new DelegatedConfig(delegated);
        
        final boolean expectedValue = true;
        willReturn(expectedValue).given(delegated).getBoolean(TEST_KEY);
        
        final boolean result = config.getBooleanParameter(TEST_KEY);
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).getBoolean(TEST_KEY);
    }

    /**
     * Test method for {@link DelegatedConfig#getNumberParameter(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetNumberParameter() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedConfig config = new DelegatedConfig(delegated);
        
        final Number expectedValue = 15445;
        willReturn(expectedValue).given(delegated).getNumber(TEST_KEY);
        
        final Number result = config.getNumberParameter(TEST_KEY);
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).getNumber(TEST_KEY);
    }

    /**
     * Test method for {@link DelegatedConfig#getStringParameter(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetStringParameter() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedConfig config = new DelegatedConfig(delegated);
        
        final String expectedValue = "mock value";
        willReturn(expectedValue).given(delegated).getString(TEST_KEY);
        
        final String result = config.getStringParameter(TEST_KEY);
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).getString(TEST_KEY);
    }

}
