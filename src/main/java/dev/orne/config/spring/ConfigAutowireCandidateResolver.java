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

import java.util.function.Supplier;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.AutowireCandidateResolver;

import dev.orne.config.Config;
import dev.orne.config.ConfigProvider;
import dev.orne.config.PreferredConfig;

/**
 * Autowire candidate resolver that provides {@code Config} instances
 * from a {@code ConfigProvider}.
 * <p>
 * Supports the {@code PreferredConfig} annotation to select
 * the desired configuration.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-11
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class ConfigAutowireCandidateResolver
implements AutowireCandidateResolver {

    /** The parent autowire candidate resolver. */
    public final @NotNull AutowireCandidateResolver parent;
    /** The configuration provider supplier. */
    public final @NotNull Supplier<ConfigProvider> configProvider;

    /**
     * Creates a new instance.
     * 
     * @param parent The parent autowire candidate resolver.
     * @param configProvider The configuration provider supplier.
     */
    public ConfigAutowireCandidateResolver(
            final @NotNull AutowireCandidateResolver parent,
            final @NotNull Supplier<ConfigProvider> configProvider) {
        super();
        this.parent = parent;
        this.configProvider = configProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAutowireCandidate(
            final BeanDefinitionHolder bdHolder,
            final DependencyDescriptor descriptor) {
        return parent.isAutowireCandidate(bdHolder, descriptor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRequired(
            final DependencyDescriptor descriptor) {
        return parent.isRequired(descriptor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasQualifier(
            final DependencyDescriptor descriptor) {
        return parent.hasQualifier(descriptor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getSuggestedValue(
            final DependencyDescriptor descriptor) {
        if (!Config.class.equals(descriptor.getDeclaredType())) {
            return parent.getSuggestedValue(descriptor);
        }
        final PreferredConfig annot = descriptor.getAnnotation(PreferredConfig.class);
        if (annot == null) {
            return configProvider.get().getDefaultConfig();
        } else {
            return configProvider.get().selectConfig(annot);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getLazyResolutionProxyIfNecessary(
            final DependencyDescriptor descriptor,
            final String beanName) {
        return parent.getLazyResolutionProxyIfNecessary(descriptor, beanName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AutowireCandidateResolver cloneIfNecessary() {
        return parent.cloneIfNecessary();
    }
}
