/**
 * 
 */
package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

/**
 * Unit tests for {@code DefaultConfigurer.configure()}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0, 2019-07
 */
@Tag("ut")
class DefaultConfigurerConfigureTest {

    /**
     * Test method for {@link DefaultConfigurer#configure(Configurable)} for
     * beans without options.
     */
    @Test
    public void testConfigureNoOptions() {
        final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
        final Config config = BDDMockito.mock(Config.class);
        final DefaultConfigurer configurer = BDDMockito.spy(new DefaultConfigurer(configProvider));
        final ConnfigurableNoOptionsTestBean bean = BDDMockito.spy(new ConnfigurableNoOptionsTestBean());

        given(configProvider.getDefaultConfig()).willReturn(config);

        configurer.configure(bean);

        then(configProvider).should(times(1)).getDefaultConfig();
        then(configProvider).should(times(0)).selectConfig(any(), any());
        then(configurer).should(times(1)).configureProperties(bean, config);
        then(configurer).should(times(1)).configureNestedBeans(bean, config);
        then(bean).should(times(1)).configure(config);
    }

    /**
     * Test method for {@link DefaultConfigurer#configure(Configurable)} for
     * beans without options an no default config.
     */
    @Test
    public void testConfigureNoOptionsNoDefaultConfig() {
        final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
        final Config config = BDDMockito.mock(Config.class);
        final DefaultConfigurer configurer = BDDMockito.spy(new DefaultConfigurer(configProvider));
        final ConnfigurableNoOptionsTestBean bean = BDDMockito.spy(new ConnfigurableNoOptionsTestBean());

        given(configProvider.getDefaultConfig()).willReturn(null);

        configurer.configure(bean);

        then(configProvider).should(times(1)).getDefaultConfig();
        then(configProvider).should(times(0)).selectConfig(any(), any());
        then(configurer).should(times(0)).configureProperties(bean, config);
        then(configurer).should(times(0)).configureNestedBeans(bean, config);
        then(bean).should(times(0)).configure(config);
    }

    /**
     * Test method for {@link DefaultConfigurer#configure(Configurable)} for
     * beans without options.
     */
    @Test
    public void testConfigureDefaultOptions() {
        final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
        final Config config = BDDMockito.mock(Config.class);
        final DefaultConfigurer configurer = BDDMockito.spy(new DefaultConfigurer(configProvider));
        final ConnfigurableDefaultOptionsTestBean bean = BDDMockito.spy(new ConnfigurableDefaultOptionsTestBean());

        given(configProvider.selectConfig(any(), any())).willReturn(config);

        configurer.configure(bean);

        then(configProvider).should(times(0)).getDefaultConfig();
        then(configProvider).should(times(1)).selectConfig(any(), eq(bean.getClass()));
        then(configurer).should(times(1)).configureProperties(bean, config);
        then(configurer).should(times(1)).configureNestedBeans(bean, config);
        then(bean).should(times(1)).configure(config);
    }

    /**
     * Test method for {@link DefaultConfigurer#configure(Configurable)} for
     * beans without options an no default config.
     */
    @Test
    public void testConfigureDefaultOptionsNoDefaultConfig() {
        final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
        final Config config = BDDMockito.mock(Config.class);
        final DefaultConfigurer configurer = BDDMockito.spy(new DefaultConfigurer(configProvider));
        final ConnfigurableDefaultOptionsTestBean bean = BDDMockito.spy(new ConnfigurableDefaultOptionsTestBean());

        given(configProvider.selectConfig(any(), any())).willReturn(null);

        configurer.configure(bean);

        then(configProvider).should(times(0)).getDefaultConfig();
        then(configProvider).should(times(1)).selectConfig(any(), eq(bean.getClass()));
        then(configurer).should(times(0)).configureProperties(bean, config);
        then(configurer).should(times(0)).configureNestedBeans(bean, config);
        then(bean).should(times(0)).configure(config);
    }

    /**
     * Test method for {@link DefaultConfigurer#configure(Configurable)} for
     * beans without options.
     */
    @Test
    public void testConfigureNoPropertiesOptions() {
        final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
        final Config config = BDDMockito.mock(Config.class);
        final DefaultConfigurer configurer = BDDMockito.spy(new DefaultConfigurer(configProvider));
        final ConnfigurableNoPropertiesTestBean bean = BDDMockito.spy(new ConnfigurableNoPropertiesTestBean());

        given(configProvider.selectConfig(any(), any())).willReturn(config);

        configurer.configure(bean);

        then(configProvider).should(times(0)).getDefaultConfig();
        then(configProvider).should(times(1)).selectConfig(any(), eq(bean.getClass()));
        then(configurer).should(times(0)).configureProperties(bean, config);
        then(configurer).should(times(1)).configureNestedBeans(bean, config);
        then(bean).should(times(1)).configure(config);
    }

    /**
     * Test method for {@link DefaultConfigurer#configure(Configurable)} for
     * beans without options an no default config.
     */
    @Test
    public void testConfigureNoPropertiesOptionsNoDefaultConfig() {
        final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
        final Config config = BDDMockito.mock(Config.class);
        final DefaultConfigurer configurer = BDDMockito.spy(new DefaultConfigurer(configProvider));
        final ConnfigurableNoPropertiesTestBean bean = BDDMockito.spy(new ConnfigurableNoPropertiesTestBean());

        given(configProvider.selectConfig(any(), any())).willReturn(null);

        configurer.configure(bean);

        then(configProvider).should(times(0)).getDefaultConfig();
        then(configProvider).should(times(1)).selectConfig(any(), eq(bean.getClass()));
        then(configurer).should(times(0)).configureProperties(bean, config);
        then(configurer).should(times(0)).configureNestedBeans(bean, config);
        then(bean).should(times(0)).configure(config);
    }

    /**
     * Test method for {@link DefaultConfigurer#configure(Configurable)} for
     * beans without options.
     */
    @Test
    public void testConfigureNoNestedOptions() {
        final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
        final Config config = BDDMockito.mock(Config.class);
        final DefaultConfigurer configurer = BDDMockito.spy(new DefaultConfigurer(configProvider));
        final ConnfigurableNoNestedBeansTestBean bean = BDDMockito.spy(new ConnfigurableNoNestedBeansTestBean());

        given(configProvider.selectConfig(any(), any())).willReturn(config);

        configurer.configure(bean);

        then(configProvider).should(times(0)).getDefaultConfig();
        then(configProvider).should(times(1)).selectConfig(any(), eq(bean.getClass()));
        then(configurer).should(times(1)).configureProperties(bean, config);
        then(configurer).should(times(0)).configureNestedBeans(bean, config);
        then(bean).should(times(1)).configure(config);
    }

    /**
     * Test method for {@link DefaultConfigurer#configure(Configurable)} for
     * beans without options an no default config.
     */
    @Test
    public void testConfigureNoNestedOptionsNoDefaultConfig() {
        final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
        final Config config = BDDMockito.mock(Config.class);
        final DefaultConfigurer configurer = BDDMockito.spy(new DefaultConfigurer(configProvider));
        final ConnfigurableNoNestedBeansTestBean bean = BDDMockito.spy(new ConnfigurableNoNestedBeansTestBean());

        given(configProvider.selectConfig(any(), any())).willReturn(null);

        configurer.configure(bean);

        then(configProvider).should(times(0)).getDefaultConfig();
        then(configProvider).should(times(1)).selectConfig(any(), eq(bean.getClass()));
        then(configurer).should(times(0)).configureProperties(bean, config);
        then(configurer).should(times(0)).configureNestedBeans(bean, config);
        then(bean).should(times(0)).configure(config);
    }

    /**
     * Test method for {@link DefaultConfigurer#configure(Configurable)} for
     * beans without options.
     */
    @Test
    public void testConfigureOnlyConfigureOptions() {
        final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
        final Config config = BDDMockito.mock(Config.class);
        final DefaultConfigurer configurer = BDDMockito.spy(new DefaultConfigurer(configProvider));
        final ConnfigurableOnlyConfigureTestBean bean = BDDMockito.spy(new ConnfigurableOnlyConfigureTestBean());

        given(configProvider.selectConfig(any(), any())).willReturn(config);

        configurer.configure(bean);

        then(configProvider).should(times(0)).getDefaultConfig();
        then(configProvider).should(times(1)).selectConfig(any(), eq(bean.getClass()));
        then(configurer).should(times(0)).configureProperties(bean, config);
        then(configurer).should(times(0)).configureNestedBeans(bean, config);
        then(bean).should(times(1)).configure(config);
    }

    /**
     * Test method for {@link DefaultConfigurer#configure(Configurable)} for
     * beans without options an no default config.
     */
    @Test
    public void testConfigureOnlyConfigureOptionsNoDefaultConfig() {
        final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
        final Config config = BDDMockito.mock(Config.class);
        final DefaultConfigurer configurer = BDDMockito.spy(new DefaultConfigurer(configProvider));
        final ConnfigurableOnlyConfigureTestBean bean = BDDMockito.spy(new ConnfigurableOnlyConfigureTestBean());

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
