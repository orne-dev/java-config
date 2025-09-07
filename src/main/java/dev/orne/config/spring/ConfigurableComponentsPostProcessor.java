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

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import dev.orne.config.Configurer;
import dev.orne.config.Configurable;

/**
 * Spring bean post processor for automatic configuration of
 * {@code Configurable} beans.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-08
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class ConfigurableComponentsPostProcessor
implements BeanPostProcessor {

    /** The configurable components configurer. */
    private final @NotNull Configurer configurer;

    /**
     * Creates a new instance.
     * 
     * @param configurer The configurable components configurer.
     */
    public ConfigurableComponentsPostProcessor(
            final @NotNull Configurer configurer) {
        this.configurer = configurer;
    }

    /**
     * Returns the configurable components configurer.
     * 
     * @return The configurable components configurer.
     */
    protected @NotNull Configurer getConfigurer() {
        return this.configurer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object postProcessBeforeInitialization(
            final @NotNull Object bean,
            final @NotNull String beanName)
    throws BeansException {
        if (bean instanceof Configurable) {
            this.configurer.configure((Configurable) bean);
        }
        return bean;
    }
}
