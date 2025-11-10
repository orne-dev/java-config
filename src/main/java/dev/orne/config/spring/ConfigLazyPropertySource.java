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

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.env.PropertySource;

import dev.orne.config.Config;
import dev.orne.config.ConfigException;

/**
 * Spring {@code PropertySource} that uses a {@code Config} bean lazily loaded
 * from the application context on first access.
 * <p>
 * Allows configuration of the property source during bean factory post
 * processing, before the configuration bean is available.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-08
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class ConfigLazyPropertySource
extends PropertySource<String> {

    /** The bean factory. */
    private final @NotNull BeanFactory beanFactory;
    /** The configuration instance. */
    private Config config;

    /**
     * Creates a new instance.
     * 
     * @param name The name of the property source.
     * @param beanFactory The bean factory to load the configuration bean.
     * @param beanName The name of the configuration bean.
     */
    public ConfigLazyPropertySource(
            final @NotNull String name,
            final @NotNull BeanFactory beanFactory,
            final @NotNull String beanName) {
        super(name, beanName);
        this.beanFactory = Objects.requireNonNull(beanFactory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsProperty(
            final @NotNull String name) {
        return getConfig().contains(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProperty(
            final @NotNull String name) {
        return getConfig().getUndecored(name);
    }

    /**
     * Returns the bean factory.
     * 
     * @return The bean factory.
     */
    protected @NotNull BeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    /**
     * Gets the configuration instance.
     * <p>
     * The configuration instance is retrieved lazily from the bean factory
     * using the bean name provided as source of this property source on first
     * access.
     * 
     * @return The configuration instance.
     */
    protected @NotNull Config getConfig() {
        if (this.config == null) {
            try {
                this.config = this.beanFactory.getBean(getSource(), Config.class);
            } catch (final NoSuchBeanDefinitionException e) {
                throw new ConfigException(
                        "Failed to get configuration bean '" + getSource() + "' for property source '" + getName() + "'", e);
            }
        }
        return this.config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), beanFactory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(
            final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConfigLazyPropertySource other = (ConfigLazyPropertySource) obj;
        return Objects.equals(this.beanFactory, other.beanFactory);
    }
}
