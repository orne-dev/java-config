package dev.orne.config.commons;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2025 Orne Developments
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
import java.util.stream.Stream;

import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.Config;
import dev.orne.config.ConfigException;

/**
 * Unit tests for {@code DelegatedOrneConfiguration}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see DelegatedOrneConfiguration
 */
@Tag("ut")
class DelegatedOrneConfigurationTest {

    private static final String TEST_KEY = "test.key";

    /**
     * Test method for {@link DelegatedOrneConfiguration#DelegatedOrneConfiguration(Config)}.
     */
    @Test
    void testConstructor() {
        final Config delegated = mock(Config.class);
        final DelegatedOrneConfiguration config = new DelegatedOrneConfiguration(delegated);
        assertSame(delegated, config.getConfig());
    }

    /**
     * Test method for {@link DelegatedOrneConfiguration#DelegatedOrneConfiguration(Config)}
     * with {@code null} parameter.
     */
    @Test
    void testConstructorNull() {
        assertThrows(NullPointerException.class, () -> {
            new DelegatedOrneConfiguration(null);
        });
    }

    /**
     * Test method for {@link DelegatedOrneConfiguration#isEmpty()}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testIsEmpty() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedOrneConfiguration config = new DelegatedOrneConfiguration(delegated);
        
        final boolean expectedValue = true;
        willReturn(expectedValue).given(delegated).isEmpty();
        
        final boolean result = config.isEmpty();
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).isEmpty();
    }

    /**
     * Test method for {@link DelegatedOrneConfiguration#isEmpty()}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testIsEmptyError() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedOrneConfiguration config = new DelegatedOrneConfiguration(delegated);
        
        final ConfigException mockException = new ConfigException("mock");
        willThrow(mockException).given(delegated).isEmpty();
        
        final ConfigurationRuntimeException result = assertThrows(
                ConfigurationRuntimeException.class,
                config::isEmpty);
        assertSame(mockException, result.getCause());
        
        then(delegated).should(times(1)).isEmpty();
    }

    /**
     * Test method for {@link DelegatedOrneConfiguration#getKeys()}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testsGetKeys() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedOrneConfiguration config = new DelegatedOrneConfiguration(delegated);
        
        @SuppressWarnings("unchecked")
        final Iterator<String> expectedValue = mock(Iterator.class);
        @SuppressWarnings("unchecked")
        final Stream<String> delegatedIterable = mock(Stream.class);
        given(delegatedIterable.iterator()).willReturn(expectedValue);
        willReturn(delegatedIterable).given(delegated).getKeys();
        
        final Iterator<String> result = config.getKeys();
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).getKeys();
    }

    /**
     * Test method for {@link DelegatedOrneConfiguration#getKeys()}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testsGetKeysError() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedOrneConfiguration config = new DelegatedOrneConfiguration(delegated);
        
        final ConfigException mockException = new ConfigException("mock");
        willThrow(mockException).given(delegated).getKeys();
        
        final ConfigurationRuntimeException result = assertThrows(
                ConfigurationRuntimeException.class,
                config::getKeys);
        assertSame(mockException, result.getCause());
        
        then(delegated).should(times(1)).getKeys();
    }

    /**
     * Test method for {@link DelegatedOrneConfiguration#containsKey(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testContainsKey() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedOrneConfiguration config = new DelegatedOrneConfiguration(delegated);
        
        final boolean expectedValue = true;
        willReturn(expectedValue).given(delegated).contains(TEST_KEY);
        
        final boolean result = config.containsKey(TEST_KEY);
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).contains(TEST_KEY);
    }

    /**
     * Test method for {@link DelegatedOrneConfiguration#containsKey(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testContainsKeyError() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedOrneConfiguration config = new DelegatedOrneConfiguration(delegated);
        
        final ConfigException mockException = new ConfigException("mock");
        willThrow(mockException).given(delegated).contains(TEST_KEY);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            config.containsKey(TEST_KEY);
        });
        assertSame(mockException, result.getCause());
        
        then(delegated).should(times(1)).contains(TEST_KEY);
    }

    /**
     * Test method for {@link DelegatedOrneConfiguration#containsValue(Object)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testContainsValue() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedOrneConfiguration config = new DelegatedOrneConfiguration(delegated);
        
        final String value = "mock value";
        given(delegated.getKeys()).willAnswer(invocation -> Stream.of(TEST_KEY));
        given(delegated.get(TEST_KEY)).willReturn(value);
        
        assertTrue(config.containsValue(value));
        assertFalse(config.containsValue("missing value"));
    }

    /**
     * Test method for {@link DelegatedOrneConfiguration#getProperty(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetProperty() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedOrneConfiguration config = new DelegatedOrneConfiguration(delegated);
        
        final String expectedValue = "mock value";
        willReturn(expectedValue).given(delegated).get(TEST_KEY);
        
        final Object result = config.getProperty(TEST_KEY);
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).get(TEST_KEY);
    }

    /**
     * Test method for {@link DelegatedOrneConfiguration#getProperty(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetPropertyError() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedOrneConfiguration config = new DelegatedOrneConfiguration(delegated);
        
        final ConfigException mockException = new ConfigException("mock");
        willThrow(mockException).given(delegated).get(TEST_KEY);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            config.getProperty(TEST_KEY);
        });
        assertSame(mockException, result.getCause());
        
        then(delegated).should(times(1)).get(TEST_KEY);
    }

    /**
     * Test method for {@link DelegatedOrneConfiguration#addProperty(String, Object)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testAddProperty() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedOrneConfiguration config = new DelegatedOrneConfiguration(delegated);
        
        assertThrows(UnsupportedOperationException.class, () -> {
            config.addProperty(TEST_KEY, "mock value");
        });
        
        then(delegated).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link DelegatedOrneConfiguration#clearProperty(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testClearProperty() throws ConfigException {
        final Config delegated = mock(Config.class);
        final DelegatedOrneConfiguration config = new DelegatedOrneConfiguration(delegated);
        
        assertThrows(UnsupportedOperationException.class, () -> {
            config.clearProperty(TEST_KEY);
        });
        
        then(delegated).shouldHaveNoInteractions();
    }
}
