package dev.orne.config.impl;

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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.config.Config;
import dev.orne.config.ConfigProvider;
import dev.orne.config.Configurable;
import dev.orne.config.ConfigurationOptions;
import dev.orne.config.Configurer;

/**
 * Unit tests for {@code ConfigurerImpl.configure()}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0, 2019-07
 */
@Tag("ut")
class ConfigurerImplConfigureTest {

    protected @Mock ConfigProvider configProvider;
    protected @Mock Config config;

    /**
     * Initializes the mocks used in the tests.
     */
    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test method for {@link ConfigurerImpl#configure(Configurable)} for
     * beans without options.
     */
    @Test
    void testConfigureNoOptions() {
        final ConfigurerImpl configurer = spy(assertInstanceOf(
                ConfigurerImpl.class,
                Configurer.fromProvider(configProvider)));
        final ConnfigurableNoOptionsTestBean bean = spy(new ConnfigurableNoOptionsTestBean());

        given(configProvider.getDefaultConfig()).willReturn(config);

        configurer.configure(bean);

        then(configProvider).should(times(1)).getDefaultConfig();
        then(configProvider).should(times(0)).selectConfig(any(), any());
        then(configurer).should(times(1)).configureProperties(bean, config);
        then(configurer).should(times(1)).configureNestedBeans(bean, config);
        then(bean).should(times(1)).configure(config);
    }

    /**
     * Test method for {@link ConfigurerImpl#configure(Configurable)} for
     * beans without options an no default config.
     */
    @Test
    void testConfigureNoOptionsNoDefaultConfig() {
        final ConfigurerImpl configurer = spy(assertInstanceOf(
                ConfigurerImpl.class,
                Configurer.fromProvider(configProvider)));
        final ConnfigurableNoOptionsTestBean bean = spy(new ConnfigurableNoOptionsTestBean());

        given(configProvider.getDefaultConfig()).willReturn(null);

        configurer.configure(bean);

        then(configProvider).should(times(1)).getDefaultConfig();
        then(configProvider).should(times(0)).selectConfig(any(), any());
        then(configurer).should(times(0)).configureProperties(bean, config);
        then(configurer).should(times(0)).configureNestedBeans(bean, config);
        then(bean).should(times(0)).configure(config);
    }

    /**
     * Test method for {@link ConfigurerImpl#configure(Configurable)} for
     * beans without options.
     */
    @Test
    void testConfigureDefaultOptions() {
        final ConfigurerImpl configurer = spy(assertInstanceOf(
                ConfigurerImpl.class,
                Configurer.fromProvider(configProvider)));
        final ConnfigurableDefaultOptionsTestBean bean = spy(new ConnfigurableDefaultOptionsTestBean());

        given(configProvider.selectConfig(any(), any())).willReturn(config);

        configurer.configure(bean);

        then(configProvider).should(times(0)).getDefaultConfig();
        then(configProvider).should(times(1)).selectConfig(any(), eq(bean.getClass()));
        then(configurer).should(times(1)).configureProperties(bean, config);
        then(configurer).should(times(1)).configureNestedBeans(bean, config);
        then(bean).should(times(1)).configure(config);
    }

    /**
     * Test method for {@link ConfigurerImpl#configure(Configurable)} for
     * beans without options an no default config.
     */
    @Test
    void testConfigureDefaultOptionsNoDefaultConfig() {
        final ConfigurerImpl configurer = spy(assertInstanceOf(
                ConfigurerImpl.class,
                Configurer.fromProvider(configProvider)));
        final ConnfigurableDefaultOptionsTestBean bean = spy(new ConnfigurableDefaultOptionsTestBean());

        given(configProvider.selectConfig(any(), any())).willReturn(null);

        configurer.configure(bean);

        then(configProvider).should(times(0)).getDefaultConfig();
        then(configProvider).should(times(1)).selectConfig(any(), eq(bean.getClass()));
        then(configurer).should(times(0)).configureProperties(bean, config);
        then(configurer).should(times(0)).configureNestedBeans(bean, config);
        then(bean).should(times(0)).configure(config);
    }

    /**
     * Test method for {@link ConfigurerImpl#configure(Configurable)} for
     * beans without options.
     */
    @Test
    void testConfigureNoPropertiesOptions() {
        final ConfigurerImpl configurer = spy(assertInstanceOf(
                ConfigurerImpl.class,
                Configurer.fromProvider(configProvider)));
        final ConnfigurableNoPropertiesTestBean bean = spy(new ConnfigurableNoPropertiesTestBean());

        given(configProvider.selectConfig(any(), any())).willReturn(config);

        configurer.configure(bean);

        then(configProvider).should(times(0)).getDefaultConfig();
        then(configProvider).should(times(1)).selectConfig(any(), eq(bean.getClass()));
        then(configurer).should(times(0)).configureProperties(bean, config);
        then(configurer).should(times(1)).configureNestedBeans(bean, config);
        then(bean).should(times(1)).configure(config);
    }

    /**
     * Test method for {@link ConfigurerImpl#configure(Configurable)} for
     * beans without options an no default config.
     */
    @Test
    void testConfigureNoPropertiesOptionsNoDefaultConfig() {
        final ConfigurerImpl configurer = spy(assertInstanceOf(
                ConfigurerImpl.class,
                Configurer.fromProvider(configProvider)));
        final ConnfigurableNoPropertiesTestBean bean = spy(new ConnfigurableNoPropertiesTestBean());

        given(configProvider.selectConfig(any(), any())).willReturn(null);

        configurer.configure(bean);

        then(configProvider).should(times(0)).getDefaultConfig();
        then(configProvider).should(times(1)).selectConfig(any(), eq(bean.getClass()));
        then(configurer).should(times(0)).configureProperties(bean, config);
        then(configurer).should(times(0)).configureNestedBeans(bean, config);
        then(bean).should(times(0)).configure(config);
    }

    /**
     * Test method for {@link ConfigurerImpl#configure(Configurable)} for
     * beans without options.
     */
    @Test
    void testConfigureNoNestedOptions() {
        final ConfigurerImpl configurer = spy(assertInstanceOf(
                ConfigurerImpl.class,
                Configurer.fromProvider(configProvider)));
        final ConnfigurableNoNestedBeansTestBean bean = spy(new ConnfigurableNoNestedBeansTestBean());

        given(configProvider.selectConfig(any(), any())).willReturn(config);

        configurer.configure(bean);

        then(configProvider).should(times(0)).getDefaultConfig();
        then(configProvider).should(times(1)).selectConfig(any(), eq(bean.getClass()));
        then(configurer).should(times(1)).configureProperties(bean, config);
        then(configurer).should(times(0)).configureNestedBeans(bean, config);
        then(bean).should(times(1)).configure(config);
    }

    /**
     * Test method for {@link ConfigurerImpl#configure(Configurable)} for
     * beans without options an no default config.
     */
    @Test
    void testConfigureNoNestedOptionsNoDefaultConfig() {
        final ConfigurerImpl configurer = spy(assertInstanceOf(
                ConfigurerImpl.class,
                Configurer.fromProvider(configProvider)));
        final ConnfigurableNoNestedBeansTestBean bean = spy(new ConnfigurableNoNestedBeansTestBean());

        given(configProvider.selectConfig(any(), any())).willReturn(null);

        configurer.configure(bean);

        then(configProvider).should(times(0)).getDefaultConfig();
        then(configProvider).should(times(1)).selectConfig(any(), eq(bean.getClass()));
        then(configurer).should(times(0)).configureProperties(bean, config);
        then(configurer).should(times(0)).configureNestedBeans(bean, config);
        then(bean).should(times(0)).configure(config);
    }

    /**
     * Test method for {@link ConfigurerImpl#configure(Configurable)} for
     * beans without options.
     */
    @Test
    void testConfigureOnlyConfigureOptions() {
        final ConfigurerImpl configurer = spy(assertInstanceOf(
                ConfigurerImpl.class,
                Configurer.fromProvider(configProvider)));
        final ConnfigurableOnlyConfigureTestBean bean = spy(new ConnfigurableOnlyConfigureTestBean());

        given(configProvider.selectConfig(any(), any())).willReturn(config);

        configurer.configure(bean);

        then(configProvider).should(times(0)).getDefaultConfig();
        then(configProvider).should(times(1)).selectConfig(any(), eq(bean.getClass()));
        then(configurer).should(times(0)).configureProperties(bean, config);
        then(configurer).should(times(0)).configureNestedBeans(bean, config);
        then(bean).should(times(1)).configure(config);
    }

    /**
     * Test method for {@link ConfigurerImpl#configure(Configurable)} for
     * beans without options an no default config.
     */
    @Test
    void testConfigureOnlyConfigureOptionsNoDefaultConfig() {
        final ConfigurerImpl configurer = spy(assertInstanceOf(
                ConfigurerImpl.class,
                Configurer.fromProvider(configProvider)));
        final ConnfigurableOnlyConfigureTestBean bean = spy(new ConnfigurableOnlyConfigureTestBean());

        given(configProvider.selectConfig(any(), any())).willReturn(null);

        configurer.configure(bean);

        then(configProvider).should(times(0)).getDefaultConfig();
        then(configProvider).should(times(1)).selectConfig(any(), eq(bean.getClass()));
        then(configurer).should(times(0)).configureProperties(bean, config);
        then(configurer).should(times(0)).configureNestedBeans(bean, config);
        then(bean).should(times(0)).configure(config);
    }

    public static class ConnfigurableNoOptionsTestBean
    implements Configurable {
        private boolean configured;
        @Override
        public void configure(Config config) {
            this.configured = true;
        }
        @Override
        public boolean isConfigured() {
            return this.configured;
        }
    }

    @ConfigurationOptions
    public static class ConnfigurableDefaultOptionsTestBean
    implements Configurable {
        private boolean configured;
        @Override
        public void configure(Config config) {
            this.configured = true;
        }
        @Override
        public boolean isConfigured() {
            return this.configured;
        }
    }

    @ConfigurationOptions(configureProperties=false)
    public static class ConnfigurableNoPropertiesTestBean
    implements Configurable {
        private boolean configured;
        @Override
        public void configure(Config config) {
            this.configured = true;
        }
        @Override
        public boolean isConfigured() {
            return this.configured;
        }
    }

    @ConfigurationOptions(configureNestedBeans=false)
    public static class ConnfigurableNoNestedBeansTestBean
    implements Configurable {
        private boolean configured;
        @Override
        public void configure(Config config) {
            this.configured = true;
        }
        @Override
        public boolean isConfigured() {
            return this.configured;
        }
    }

    @ConfigurationOptions(configureProperties=false, configureNestedBeans=false)
    public static class ConnfigurableOnlyConfigureTestBean
    implements Configurable {
        private boolean configured;
        @Override
        public void configure(Config config) {
            this.configured = true;
        }
        @Override
        public boolean isConfigured() {
            return this.configured;
        }
    }
}
