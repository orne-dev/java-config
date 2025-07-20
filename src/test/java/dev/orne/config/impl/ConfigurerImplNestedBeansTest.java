package dev.orne.config.impl;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

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

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dev.orne.config.Config;
import dev.orne.config.ConfigProvider;
import dev.orne.config.Configurable;
import dev.orne.config.Configurer;

/**
 * Unit tests for {@code ConfigurerImpl.configureNestedBeans()}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0, 2019-07
 */
@Tag("ut")
class ConfigurerImplNestedBeansTest {

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
     * Test method for {@link ConfigurerImpl#configureNestedBeans(Configurable, Config)} for
     * null nested beans.
     */
    @Test
    void testConfigureNullNestedBeans() {
        final ConfigurerImpl configurer = spy(assertInstanceOf(
                ConfigurerImpl.class,
                Configurer.fromProvider(configProvider)));
        final NestedBeansTestBean bean = new NestedBeansTestBean();
        
        configurer.configureNestedBeans(bean, config);
        
        then(configurer).should(times(0)).configure(any());
    }

    /**
     * Test method for {@link ConfigurerImpl#configureNestedBeans(Configurable, Config)} for
     * unconfigured nested beans.
     */
    @Test
    void testConfigureNestedBeansUnconfigured() {
        final Configurable nestedBean1 = mock(Configurable.class, "bean1");
        final Configurable nestedBean2 = mock(Configurable.class, "bean2");
        final ConfigurerImpl configurer = spy(assertInstanceOf(
                ConfigurerImpl.class,
                Configurer.fromProvider(configProvider)));
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
     * Test method for {@link ConfigurerImpl#configureNestedBeans(Configurable, Config)} for
     * configured nested beans.
     */
    @Test
    void testConfigureNestedBeansConfigured() {
        final Configurable nestedBean1 = mock(Configurable.class, "bean1");
        final Configurable nestedBean2 = mock(Configurable.class, "bean2");
        final ConfigurerImpl configurer = spy(assertInstanceOf(
                ConfigurerImpl.class,
                Configurer.fromProvider(configProvider)));
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
     * Test method for {@link ConfigurerImpl#configureNestedBeans(Configurable, Config)} for
     * mixed nested beans.
     */
    @Test
    void testConfigureNestedBeansMixed() {
        final Configurable nestedBean1 = mock(Configurable.class, "bean1");
        final Configurable nestedBean2 = mock(Configurable.class, "bean2");
        final ConfigurerImpl configurer = spy(assertInstanceOf(
                ConfigurerImpl.class,
                Configurer.fromProvider(configProvider)));
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
