package dev.orne.config.spring;

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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.env.PropertySource;

import dev.orne.config.Config;
import dev.orne.config.ConfigException;

/**
 * Unit tests for {@link ConfigLazyPropertySource}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class ConfigLazyPropertySourceTest {

    private @Mock BeanFactory beanFactory;
    private @Mock Config config;
    private ConfigLazyPropertySource propertySource;
    private final String beanName = "testConfig";
    private final String propertySourceName = "testSource";

    /**
     * Initializes the mocks and instance used in the tests.
     */
    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        propertySource = new ConfigLazyPropertySource(propertySourceName, beanFactory, beanName);
    }

    /**
     * Tests that when existing bean for name, {@code containsProperty()}
     * delegates to the {@link Config} bean.
     */
    @Test
    void givenExistingBeanForName_whenContainsProperty_thenDelegatesToConfigBean() {
        when(beanFactory.getBean(beanName, Config.class)).thenReturn(config);
        when(config.contains("key")).thenReturn(true);
        assertTrue(propertySource.containsProperty("key"));
    }

    /**
     * Tests that when missing bean for name, {@code containsProperty()}
     * throws a {@link ConfigException}.
     */
    @Test
    void givenMissingBeanForName_whenContainsProperty_thenThrowsException() {
        when(beanFactory.getBean(beanName, Config.class)).thenThrow(new NoSuchBeanDefinitionException(beanName));
        ConfigException ex = assertThrows(
                ConfigException.class,
                () -> propertySource.containsProperty("key"));
        assertTrue(ex.getMessage().contains(beanName));
    }

    /**
     * Tests that when existing bean for name, {@code getProperty()}
     * delegates to the {@link Config} bean.
     */
    @Test
    void givenExistingBeanForName_whenGetProperty_thenDelegatesToConfigBean() {
        when(beanFactory.getBean(beanName, Config.class)).thenReturn(config);
        when(config.getUndecored("key")).thenReturn("value");
        assertEquals("value", propertySource.getProperty("key"));
    }

    /**
     * Tests that when missing bean for name, {@code getProperty()}
     * throws a {@link ConfigException}.
     */
    @Test
    void givenMissingBeanForName_whenGetProperty_thenThrowsException() {
        when(beanFactory.getBean(beanName, Config.class)).thenThrow(new NoSuchBeanDefinitionException(beanName));
        ConfigException ex = assertThrows(
                ConfigException.class,
                () -> propertySource.getProperty("key"));
        assertTrue(ex.getMessage().contains(beanName));
    }

    /**
     * Tests that when existing bean for name, multiple calls to
     * {@code containsProperty()} or {@code getProperty()} only
     * retrieves the bean once from the {@link BeanFactory}.
     */
    @Test
    void givenExistingBeanForName_whenMultipleCalls_thenCachesConfigBean() {
        when(beanFactory.getBean(beanName, Config.class)).thenReturn(config);
        propertySource.containsProperty("key");
        propertySource.getProperty("key");
        propertySource.containsProperty("key2");
        propertySource.getProperty("key2");
        then(beanFactory).should(times(1)).getBean(beanName, Config.class);
    }

    /**
     * Tests {@code equals()} and {@code hashCode()} methods implementations.
     */
    @Test
    void testEqualsAndHashCode() {
        assertFalse(propertySource.equals(null));
        assertTrue(propertySource.equals(propertySource));
        assertFalse(propertySource.equals(new PropertySource<>(propertySourceName, beanName) {
            @Override
            public Object getProperty(String name) { return null; }
        }));
        ConfigLazyPropertySource other = new ConfigLazyPropertySource(propertySourceName, beanFactory, beanName);
        assertEquals(propertySource, other);
        assertEquals(propertySource.hashCode(), other.hashCode());
    }
}
