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

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Interface to be added to {@code @Configuration} classes to enable
 * processing of {@code @ConfigPropertySource} annotations during bean
 * factory post processing.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-08
 * @since 1.0
 * @see ConfigPropertySource
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface ConfigPropertySourcesConfigurer {

    public static final String POST_PROCESSOR = "orneConfigPropertySourcesPostProcessor";

    @Bean(POST_PROCESSOR)
    public default @NotNull BeanFactoryPostProcessor configPropertySourcesPostProcessor(
            final @NotNull ConfigurableEnvironment environment,
            final @NotNull Set<AnnotationAttributes> annotationsData,
            final @NotNull BeanFactory beanFactory) {
        return new ConfigPropertySourcePostProcessor();
    }
}
