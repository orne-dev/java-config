package dev.orne.config.spring;

import org.apiguardian.api.API;

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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Spring configuration for {@code ConfigAutowireCandidateResolver}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-11
 * @since 1.0
 */
@Configuration
@Import(ConfigProviderConfigurer.class)
@API(status = API.Status.INTERNAL, since = "1.0")
public class ConfigAutowireCandidateResolverConfigurer
implements BeanFactoryPostProcessor {

    /**
     * Creates a new instance.
     */
    public ConfigAutowireCandidateResolverConfigurer() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postProcessBeanFactory(
            final ConfigurableListableBeanFactory beanFactory)
    throws BeansException {
        if (beanFactory instanceof DefaultListableBeanFactory) {
            final DefaultListableBeanFactory dlbf =
                    (DefaultListableBeanFactory) beanFactory;
            final ConfigAutowireCandidateResolver resolver =
                    new ConfigAutowireCandidateResolver(
                            dlbf.getAutowireCandidateResolver(),
                            () -> beanFactory.getBean(ConfigProviderConfigurer.class)
                                .getConfigProvider());
            dlbf.setAutowireCandidateResolver(resolver);
        }
    }
}
