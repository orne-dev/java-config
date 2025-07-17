package dev.orne.config.impl;

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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.Config;
import dev.orne.config.ConfigProvider;
import dev.orne.config.ConfigurationOptions;

/**
 * Unit tests for {@code ConfigProviderImpl}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0
 */
@Tag("ut")
class ConfigProviderImplTest {

    /**
     * Test method for {@link ConfigProviderImpl#DefaultConfigProvider(Config)} with
     * {@code null} default configuration.
     */
    @Test
    void testConstructorNull() {
        assertThrows(NullPointerException.class, () -> {
            new ConfigProviderImpl(null);
        });
    }

    /**
     * Test method for {@link ConfigProviderImpl#DefaultConfigProvider(Config)}.
     */
    @Test
    void testConstructor() {
        final Config defaultConfig = mock(Level1Config.class);
        final ConfigProviderImpl provider = assertInstanceOf(
                ConfigProviderImpl.class,
                ConfigProvider.builder(defaultConfig)
                    .build());

        assertNotNull(provider.getDefaultConfig());
        assertSame(defaultConfig, provider.getDefaultConfig());
        assertTrue(provider.isMapped(defaultConfig.getClass()));
        assertTrue(provider.isMapped(Level1Config.class));
        assertTrue(provider.isMapped(Config.class));

        then(defaultConfig).shouldHaveNoInteractions();
    }

    /**
     * Test method for {@link ConfigProviderImpl#registerConfig(Config)}.
     */
    @Test
    void testRegister() {
        final Config defaultConfig = mock(Level1Config.class);
        final Config testConfig = mock(Level2Config.class);
        final ConfigProviderImpl provider = assertInstanceOf(
                ConfigProviderImpl.class,
                ConfigProvider.builder(defaultConfig)
                    .addConfig(testConfig)
                    .build());

        assertNotNull(provider.getDefaultConfig());
        assertSame(defaultConfig, provider.getDefaultConfig());
        assertTrue(provider.isMapped(defaultConfig.getClass()));
        assertTrue(provider.isMapped(Level1Config.class));
        assertTrue(provider.isMapped(Config.class));
        assertTrue(provider.isMapped(testConfig.getClass()));
        assertTrue(provider.isMapped(Level2Config.class));

        then(defaultConfig).shouldHaveNoInteractions();
        then(testConfig).shouldHaveNoInteractions();
    }

    /**
     * Test method for
     * {@link ConfigProviderImpl#selectConfig(ConfigurationOptions, Class)
     * with null options.
     */
    @Test
    void testSelectNoOptions() {
        final Config defaultConfig = mock(Level1Config.class);
        final Config testConfig = mock(Level2Config.class);
        final ConfigProviderImpl provider = assertInstanceOf(
                ConfigProviderImpl.class,
                ConfigProvider.builder(defaultConfig)
                    .addConfig(testConfig)
                    .build());
        final Class<?> targetClass = NoOptionsTestBean.class;
        final ConfigurationOptions options = targetClass.getAnnotation(
                ConfigurationOptions.class);
        
        final Config result = provider.selectConfig(options, targetClass);

        assertNotNull(result);
        assertSame(defaultConfig, result);

        then(defaultConfig).shouldHaveNoInteractions();
        then(testConfig).shouldHaveNoInteractions();
    }

    /**
     * Test method for
     * {@link ConfigProviderImpl#selectConfig(ConfigurationOptions, Class)
     * with default options.
     */
    @Test
    void testSelectDefaultOptions() {
        final Config defaultConfig = mock(Level1Config.class);
        final Config testConfig = mock(Level2Config.class);
        final ConfigProviderImpl provider = assertInstanceOf(
                ConfigProviderImpl.class,
                ConfigProvider.builder(defaultConfig)
                    .addConfig(testConfig)
                    .build());
        final Class<?> targetClass = DefaultOptionsTestBean.class;
        final ConfigurationOptions options = targetClass.getAnnotation(
                ConfigurationOptions.class);
        
        final Config result = provider.selectConfig(options, targetClass);

        assertNotNull(result);
        assertSame(defaultConfig, result);

        then(defaultConfig).shouldHaveNoInteractions();
        then(testConfig).shouldHaveNoInteractions();
    }

    /**
     * Test method for
     * {@link ConfigProviderImpl#selectConfig(ConfigurationOptions, Class)
     * with preferred configuration.
     */
    @Test
    void testSelectPreferredOptions() {
        final Config defaultConfig = mock(Level1Config.class);
        final Config testConfig = spy(new Level2ConfigImpl());
        final ConfigProviderImpl provider = assertInstanceOf(
                ConfigProviderImpl.class,
                ConfigProvider.builder(defaultConfig)
                    .addConfig(testConfig)
                    .build());
        final Class<?> targetClass = Level2TestBean.class;
        final ConfigurationOptions options = targetClass.getAnnotation(
                ConfigurationOptions.class);
        
        final Config result = provider.selectConfig(options, targetClass);

        assertNotNull(result);
        assertSame(testConfig, result);

        then(defaultConfig).shouldHaveNoInteractions();
        then(testConfig).shouldHaveNoInteractions();
    }

    /**
     * Test method for
     * {@link ConfigProviderImpl#selectConfig(ConfigurationOptions, Class)
     * with preferred not found configuration.
     */
    @Test
    void testSelectPreferredOptionsNoFound() {
        final Config defaultConfig = mock(Level1Config.class);
        final ConfigProviderImpl provider = new ConfigProviderImpl(defaultConfig);
        final Class<?> targetClass = Level2TestBean.class;
        final ConfigurationOptions options = targetClass.getAnnotation(
                ConfigurationOptions.class);
        
        final Config result = provider.selectConfig(options, targetClass);

        assertNotNull(result);
        assertSame(defaultConfig, result);

        then(defaultConfig).shouldHaveNoInteractions();
    }

    /**
     * Test method for
     * {@link ConfigProviderImpl#selectConfig(ConfigurationOptions, Class)
     * with preferred configuration with no default accepted.
     */
    @Test
    void testSelectPreferredNoDefaultOptions() {
        final Config defaultConfig = mock(Level1Config.class);
        final Config testConfig = spy(new Level2ConfigImpl());
        final ConfigProviderImpl provider = new ConfigProviderImpl(defaultConfig);
        provider.registerConfig(testConfig);
        final Class<?> targetClass = Level2NoDefaultTestBean.class;
        final ConfigurationOptions options = targetClass.getAnnotation(
                ConfigurationOptions.class);
        
        final Config result = provider.selectConfig(options, targetClass);

        assertNotNull(result);
        assertSame(testConfig, result);

        then(defaultConfig).shouldHaveNoInteractions();
        then(testConfig).shouldHaveNoInteractions();
    }

    /**
     * Test method for
     * {@link ConfigProviderImpl#selectConfig(ConfigurationOptions, Class)
     * with preferred not found configuration with no default accepted.
     */
    @Test
    void testSelectPreferredNoDefaultOptionsNoFound() {
        final Config defaultConfig = mock(Level1Config.class);
        final ConfigProviderImpl provider = new ConfigProviderImpl(defaultConfig);
        final Class<?> targetClass = Level2NoDefaultTestBean.class;
        final ConfigurationOptions options = targetClass.getAnnotation(
                ConfigurationOptions.class);
        
        final Config result = provider.selectConfig(options, targetClass);

        assertNull(result);

        then(defaultConfig).shouldHaveNoInteractions();
    }

    public static interface Level1Config
    extends Config {
        // Empty class
    }

    public static interface Level2Config
    extends Level1Config {
        // Empty class
    }

    public static class Level2ConfigImpl
    implements Level2Config {
        @Override
        public String get(String key) {
            return null;
        }
    }

    public static class NoOptionsTestBean {
        // Empty class
    }

    @ConfigurationOptions
    public static class DefaultOptionsTestBean {
        // Empty class
    }

    @ConfigurationOptions(preferredConfigs=Level2Config.class)
    public static class Level2TestBean {
        // Empty class
    }

    @ConfigurationOptions(
            preferredConfigs=Level2Config.class,
            fallbackToDefaultConfig=false)
    public static class Level2NoDefaultTestBean {
        // Empty class
    }
}
