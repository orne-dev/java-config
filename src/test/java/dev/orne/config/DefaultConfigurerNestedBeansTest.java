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

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

/**
 * Unit tests for {@code DefaultConfigurer.configureNestedBeans()}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
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
