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

import java.util.Iterator;

import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.Config;
import dev.orne.config.ConfigException;
import dev.orne.config.MutableConfig;

/**
 * Unit tests for {@code CommonsConfiguration}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see CommonsConfiguration
 */
@Tag("ut")
class CommonsConfigurationTest {

    private static final String TEST_KEY = "test.key";

    /**
     * Test method for {@link CommonsConfiguration#CommonsConfiguration(Config)}.
     */
    @Test
    void testConstructor() {
        final MutableConfig delegated = mock(MutableConfig.class);
        final CommonsConfiguration config = new CommonsConfiguration(delegated);
        assertSame(delegated, config.getConfig());
    }

    /**
     * Test method for {@link CommonsConfiguration#CommonsConfiguration(Config)}
     * with {@code null} parameter.
     */
    @Test
    void testConstructorNull() {
        assertThrows(NullPointerException.class, () -> {
            new CommonsConfiguration(null);
        });
    }

    /**
     * Test method for {@link CommonsConfiguration#isEmpty()}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testIsEmpty() throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final CommonsConfiguration config = new CommonsConfiguration(delegated);
        
        final boolean expectedValue = true;
        willReturn(expectedValue).given(delegated).isEmpty();
        
        final boolean result = config.isEmpty();
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).isEmpty();
    }

    /**
     * Test method for {@link CommonsConfiguration#isEmpty()}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testIsEmptyError() throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final CommonsConfiguration config = new CommonsConfiguration(delegated);
        
        final ConfigException mockException = new ConfigException("mock");
        willThrow(mockException).given(delegated).isEmpty();
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            config.isEmpty();
        });
        assertSame(mockException, result.getCause());
        
        then(delegated).should(times(1)).isEmpty();
    }

    /**
     * Test method for {@link CommonsConfiguration#getKeys()}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testsGetKeys() throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final CommonsConfiguration config = new CommonsConfiguration(delegated);
        
        @SuppressWarnings("unchecked")
        final Iterator<String> expectedValue = mock(Iterator.class);
        willReturn(expectedValue).given(delegated).getKeys();
        
        final Iterator<String> result = config.getKeys();
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).getKeys();
    }

    /**
     * Test method for {@link CommonsConfiguration#getKeys()}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testsGetKeysError() throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final CommonsConfiguration config = new CommonsConfiguration(delegated);
        
        final ConfigException mockException = new ConfigException("mock");
        willThrow(mockException).given(delegated).getKeys();
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            config.getKeys();
        });
        assertSame(mockException, result.getCause());
        
        then(delegated).should(times(1)).getKeys();
    }

    /**
     * Test method for {@link CommonsConfiguration#containsKey(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testContainsKey() throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final CommonsConfiguration config = new CommonsConfiguration(delegated);
        
        final boolean expectedValue = true;
        willReturn(expectedValue).given(delegated).contains(TEST_KEY);
        
        final boolean result = config.containsKey(TEST_KEY);
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).contains(TEST_KEY);
    }

    /**
     * Test method for {@link CommonsConfiguration#containsKey(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testContainsKeyError() throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final CommonsConfiguration config = new CommonsConfiguration(delegated);
        
        final ConfigException mockException = new ConfigException("mock");
        willThrow(mockException).given(delegated).contains(TEST_KEY);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            config.containsKey(TEST_KEY);
        });
        assertSame(mockException, result.getCause());
        
        then(delegated).should(times(1)).contains(TEST_KEY);
    }

    /**
     * Test method for {@link CommonsConfiguration#getProperty(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetProperty() throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final CommonsConfiguration config = new CommonsConfiguration(delegated);
        
        final String expectedValue = "mock value";
        willReturn(expectedValue).given(delegated).getString(TEST_KEY);
        
        final Object result = config.getProperty(TEST_KEY);
        assertSame(expectedValue, result);
        
        then(delegated).should(times(1)).getString(TEST_KEY);
    }

    /**
     * Test method for {@link CommonsConfiguration#getProperty(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testGetPropertyError() throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final CommonsConfiguration config = new CommonsConfiguration(delegated);
        
        final ConfigException mockException = new ConfigException("mock");
        willThrow(mockException).given(delegated).getString(TEST_KEY);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            config.getProperty(TEST_KEY);
        });
        assertSame(mockException, result.getCause());
        
        then(delegated).should(times(1)).getString(TEST_KEY);
    }

    /**
     * Test method for {@link CommonsConfiguration#addProperty(String, Object)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testAddProperty() throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final CommonsConfiguration config = new CommonsConfiguration(delegated);
        
        final Object value = new Object();
        config.addProperty(TEST_KEY, value);
        
        then(delegated).should(times(1)).set(TEST_KEY, value);
    }

    /**
     * Test method for {@link CommonsConfiguration#addProperty(String, Object)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testAddPropertyError() throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final CommonsConfiguration config = new CommonsConfiguration(delegated);
        
        final Object value = new Object();
        final ConfigException mockException = new ConfigException("mock");
        willThrow(mockException).given(delegated).set(TEST_KEY, value);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            config.addProperty(TEST_KEY, value);
        });
        assertSame(mockException, result.getCause());
        
        then(delegated).should(times(1)).set(TEST_KEY, value);
    }

    /**
     * Test method for {@link CommonsConfiguration#clearProperty(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testClearProperty() throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final CommonsConfiguration config = new CommonsConfiguration(delegated);
        
        config.clearProperty(TEST_KEY);
        
        then(delegated).should(times(1)).remove(TEST_KEY);
    }

    /**
     * Test method for {@link CommonsConfiguration#clearProperty(String)}.
     * @throws ConfigException Shouldn't happen
     */
    @Test
    void testClearPropertyError() throws ConfigException {
        final MutableConfig delegated = mock(MutableConfig.class);
        final CommonsConfiguration config = new CommonsConfiguration(delegated);
        
        final ConfigException mockException = new ConfigException("mock");
        willThrow(mockException).given(delegated).remove(TEST_KEY);
        
        final ConfigurationRuntimeException result = assertThrows(ConfigurationRuntimeException.class, () -> {
            config.clearProperty(TEST_KEY);
        });
        assertSame(mockException, result.getCause());
        
        then(delegated).should(times(1)).remove(TEST_KEY);
    }
}
