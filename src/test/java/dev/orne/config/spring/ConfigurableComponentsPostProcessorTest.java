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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import dev.orne.config.Configurer;
import dev.orne.config.Configurable;

/**
 * Unit tests for {@link ConfigurableComponentsPostProcessor}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class ConfigurableComponentsPostProcessorTest {

    /**
     * Test that when processing a {@code Configurable} bean before
     * initialization, the {@code Configurer} is used to configure the bean.
     */
    @Test
    void givenConfigurableBean_thenPostProcessBeforeInitialization_thenConfigureBean() {
        final Configurer configurer = mock(Configurer.class);
        final ConfigurableComponentsPostProcessor postProcessor = new ConfigurableComponentsPostProcessor(configurer);
        assertSame(configurer, postProcessor.getConfigurer());
        final Configurable configurable = mock(Configurable.class);
        final Object result = postProcessor.postProcessBeforeInitialization(configurable, "beanName");
        then(configurer).should().configure(configurable);
        assertSame(configurable, result);
    }

    /**
     * Test that when processing a non-{@code Configurable} bean before
     * initialization, the {@code Configurer} is not used and the bean is
     * returned as-is.
     */
    @Test
    void givenNonConfigurableBean_thenPostProcessBeforeInitialization_thenIgnoreBean() {
        final Configurer configurer = mock(Configurer.class);
        final ConfigurableComponentsPostProcessor postProcessor = new ConfigurableComponentsPostProcessor(configurer);
        assertSame(configurer, postProcessor.getConfigurer());
        final Object bean = new Object();
        final Object result = postProcessor.postProcessBeforeInitialization(bean, "beanName");
        then(configurer).should(never()).configure(any());
        assertSame(bean, result);
    }
}
