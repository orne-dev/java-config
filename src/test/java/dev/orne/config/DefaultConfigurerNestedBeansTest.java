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
 * Unit tests for {@code DefaultConfigurer.configureNestedBeans()}.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 */
@Tag("ut")
class DefaultConfigurerNestedBeansTest {

	/**
	 * Test method for {@link DefaultConfigurer#configureNestedBeans(Configurable, Config)} for
	 * null nested beans.
	 */
	@Test
	public void testConfigureNullNestedBeans() {
		final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
		final Config config = BDDMockito.mock(Config.class);
		final DefaultConfigurer configurer = BDDMockito.spy(new DefaultConfigurer(configProvider));
		final NestedBeansTestBean bean = new NestedBeansTestBean();
		
		configurer.configureNestedBeans(bean, config);
		
		then(configurer).should(times(0)).configure(any());
	}

	/**
	 * Test method for {@link DefaultConfigurer#configureNestedBeans(Configurable, Config)} for
	 * unconfigured nested beans.
	 */
	@Test
	public void testConfigureNestedBeansUnconfigured() {
		final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
		final Configurable nestedBean1 = BDDMockito.mock(Configurable.class, "bean1");
		final Configurable nestedBean2 = BDDMockito.mock(Configurable.class, "bean2");
		final Config config = BDDMockito.mock(Config.class);
		final DefaultConfigurer configurer = BDDMockito.spy(new DefaultConfigurer(configProvider));
		final NestedBeansTestBean bean = new NestedBeansTestBean();
		bean.setNestedBean1(nestedBean1);
		bean.setNestedBean2(nestedBean2);
		
		given(nestedBean1.isConfigured()).willReturn(false);
		given(nestedBean2.isConfigured()).willReturn(false);
		
		configurer.configureNestedBeans(bean, config);
		
		then(nestedBean1).should(times(1)).isConfigured();
		then(nestedBean2).should(times(1)).isConfigured();
		then(configurer).should(times(1)).configure(nestedBean1);
		then(configurer).should(times(1)).configure(nestedBean2);
	}

	/**
	 * Test method for {@link DefaultConfigurer#configureNestedBeans(Configurable, Config)} for
	 * configured nested beans.
	 */
	@Test
	public void testConfigureNestedBeansConfigured() {
		final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
		final Configurable nestedBean1 = BDDMockito.mock(Configurable.class, "bean1");
		final Configurable nestedBean2 = BDDMockito.mock(Configurable.class, "bean2");
		final Config config = BDDMockito.mock(Config.class);
		final DefaultConfigurer configurer = BDDMockito.spy(new DefaultConfigurer(configProvider));
		final NestedBeansTestBean bean = new NestedBeansTestBean();
		bean.setNestedBean1(nestedBean1);
		bean.setNestedBean2(nestedBean2);
		
		given(nestedBean1.isConfigured()).willReturn(true);
		given(nestedBean2.isConfigured()).willReturn(true);
		
		configurer.configureNestedBeans(bean, config);
		
		then(nestedBean1).should(times(1)).isConfigured();
		then(nestedBean2).should(times(1)).isConfigured();
		then(configurer).should(times(0)).configure(nestedBean1);
		then(configurer).should(times(0)).configure(nestedBean2);
	}

	/**
	 * Test method for {@link DefaultConfigurer#configureNestedBeans(Configurable, Config)} for
	 * mixed nested beans.
	 */
	@Test
	public void testConfigureNestedBeansMixed() {
		final ConfigProvider configProvider = BDDMockito.mock(ConfigProvider.class);
		final Configurable nestedBean1 = BDDMockito.mock(Configurable.class, "bean1");
		final Configurable nestedBean2 = BDDMockito.mock(Configurable.class, "bean2");
		final Config config = BDDMockito.mock(Config.class);
		final DefaultConfigurer configurer = BDDMockito.spy(new DefaultConfigurer(configProvider));
		final NestedBeansTestBean bean = new NestedBeansTestBean();
		bean.setNestedBean1(nestedBean1);
		bean.setNestedBean2(nestedBean2);
		
		given(nestedBean1.isConfigured()).willReturn(true);
		given(nestedBean2.isConfigured()).willReturn(false);
		
		configurer.configureNestedBeans(bean, config);
		
		then(nestedBean1).should(times(1)).isConfigured();
		then(nestedBean2).should(times(1)).isConfigured();
		then(configurer).should(times(0)).configure(nestedBean1);
		then(configurer).should(times(1)).configure(nestedBean2);
	}

	public static class NestedBeansTestBean
	implements Configurable {
		private Configurable nestedBean1;
		private Configurable nestedBean2;
		private boolean configured;
		@Override
		public void configure(Config config) {
			this.configured = true;
		}
		@Override
		public boolean isConfigured() {
			return this.configured;
		}
		public Configurable getNestedBean1() {
			return nestedBean1;
		}
		public void setNestedBean1(Configurable nestedBean1) {
			this.nestedBean1 = nestedBean1;
		}
		public Configurable getNestedBean2() {
			return nestedBean2;
		}
		public void setNestedBean2(Configurable nestedBean2) {
			this.nestedBean2 = nestedBean2;
		}
	}
}
