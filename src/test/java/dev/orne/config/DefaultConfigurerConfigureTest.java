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
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

/**
 * Unit tests for {@code DefaultConfigurer.configure()}.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
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
