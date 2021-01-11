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

import java.math.BigDecimal;
import java.util.Iterator;

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.ConfigException;

/**
 * Unit tests for {@code CommonsConfig}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see CommonsConfig
 */
@Tag("ut")
class CommonsConfigTest {

    private static final String TEST_KEY = "test.key";

    /**
     * Test method for {@link CommonsConfig#CommonsConfig(ImmutableConfiguration)}.
     */
    @Test
    void testConstructor() {
        final ImmutableConfiguration delegated = mock(ImmutableConfiguration.class);
        final CommonsConfig config = new CommonsConfig(delegated);
        assertSame(delegated, config.getConfig());
    }

    /**
     * Test method for {@link CommonsConfig#CommonsConfig(ImmutableConfiguration)}
     * with {@code null} parameter.
     */
    @Test
    void testConstructorNull() {
        assertThrows(NullPointerException.class, () -> {
            new CommonsConfig(null);
        });
    }

    /**
     * Test method for {@link CommonsConfig#isEmpty()}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testIsEmpty() throws ConfigException {
        final ImmutableConfiguration delegated = mock(ImmutableConfiguration.class);
        final CommonsConfig config = new CommonsConfig(delegated);
        
        final boolean expectedValue = true;
        willReturn(expectedValue).given(delegated).isEmpty();
        
        final boolean result = config.isEmpty();
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).isEmpty();
    }

    /**
     * Test method for {@link CommonsConfig#getKeys()}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testsGetKeys() throws ConfigException {
        final ImmutableConfiguration delegated = mock(ImmutableConfiguration.class);
        final CommonsConfig config = new CommonsConfig(delegated);
        
        @SuppressWarnings("unchecked")
        final Iterator<String> expectedValue = mock(Iterator.class);
        willReturn(expectedValue).given(delegated).getKeys();
        
        final Iterator<String> result = config.getKeys();
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).getKeys();
    }

    /**
     * Test method for {@link CommonsConfig#contains(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testContainsParameter() throws ConfigException {
        final ImmutableConfiguration delegated = mock(ImmutableConfiguration.class);
        final CommonsConfig config = new CommonsConfig(delegated);
        
        final boolean expectedValue = true;
        willReturn(expectedValue).given(delegated).containsKey(TEST_KEY);
        
        final boolean result = config.contains(TEST_KEY);
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).containsKey(TEST_KEY);
    }

    /**
     * Test method for {@link CommonsConfig#get(String, Class)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetParameter() throws ConfigException {
        final ImmutableConfiguration delegated = mock(ImmutableConfiguration.class);
        final CommonsConfig config = new CommonsConfig(delegated);
        
        final Object expectedValue = new Object();
        willReturn(expectedValue).given(delegated).get(Object.class, TEST_KEY);
        
        final Object result = config.get(TEST_KEY, Object.class);
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).get(Object.class, TEST_KEY);
    }

    /**
     * Test method for {@link CommonsConfig#getBoolean(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetBoolean() throws ConfigException {
        final ImmutableConfiguration delegated = mock(ImmutableConfiguration.class);
        final CommonsConfig config = new CommonsConfig(delegated);
        
        final boolean expectedValue = true;
        willReturn(expectedValue).given(delegated).getBoolean(TEST_KEY, null);
        
        final boolean result = config.getBoolean(TEST_KEY);
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).getBoolean(TEST_KEY, null);
    }

    /**
     * Test method for {@link CommonsConfig#getNumber(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetNumber() throws ConfigException {
        final ImmutableConfiguration delegated = mock(ImmutableConfiguration.class);
        final CommonsConfig config = new CommonsConfig(delegated);
        
        final BigDecimal expectedValue = BigDecimal.valueOf(15445);
        willReturn(expectedValue).given(delegated).getBigDecimal(TEST_KEY);
        
        final Number result = config.getNumber(TEST_KEY);
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).getBigDecimal(TEST_KEY);
    }

    /**
     * Test method for {@link CommonsConfig#getString(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetStringParameter() throws ConfigException {
        final ImmutableConfiguration delegated = mock(ImmutableConfiguration.class);
        final CommonsConfig config = new CommonsConfig(delegated);
        
        final String expectedValue = "mock value";
        willReturn(expectedValue).given(delegated).getString(TEST_KEY);
        
        final String result = config.getString(TEST_KEY);
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).getString(TEST_KEY);
    }

}
