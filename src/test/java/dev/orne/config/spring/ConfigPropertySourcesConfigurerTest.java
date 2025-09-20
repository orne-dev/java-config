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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;

/**
 * Unit tests for {@link ConfigPropertySourcesConfigurer}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class ConfigPropertySourcesConfigurerTest {

    /**
     * Tests that {@link ConfigPropertySourcesConfigurer#configPropertySourcesPostProcessor()}
     * creates a non-null {@link BeanFactoryPostProcessor}.
     */
    @Test
    void testConfigPropertySourcesPostProcessor_createsBeanFactoryPostProcessor() {
        final ConfigPropertySourcesConfigurer configurer = new ConfigPropertySourcesConfigurer() {};
        final BeanFactoryPostProcessor result = configurer.configPropertySourcesPostProcessor();
        assertNotNull(result);
    }
}
