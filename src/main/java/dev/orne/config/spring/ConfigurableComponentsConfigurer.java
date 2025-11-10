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
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import dev.orne.config.Configurer;

/**
 * Spring configuration for {@code ConfigurableComponentsPostProcessor}
 * based on {@code EnableConfigurableComponents} annotation.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-08
 * @since 1.0
 */
@Configuration
@Import(ConfigProviderConfigurer.class)
@API(status = API.Status.INTERNAL, since = "1.0")
public class ConfigurableComponentsConfigurer
implements ImportAware {

    /** The name of the {@code Configurer} bean. */
    public static final String CONFIGURER = "orneConfigConfigurableComponentsConfigurer";
    /** The name of the {@code ConfigurableComponentsPostProcessor} bean. */
    public static final String POST_PROCESSOR = "orneConfigConfigurableComponentsPostProcessor";

    /** The annotation data for the configuration. */
    protected AnnotationAttributes annotationData;
    /** The {@code ConfigProvider} supplier for current Spring context. */
    protected ConfigProviderConfigurer springConfigProvider;
    /** The exposed configurable components {@code Configurer}, if any. */
    protected Configurer exposedConfigurer;

    /**
     * Creates a new instance.
     */
    public ConfigurableComponentsConfigurer() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setImportMetadata(
            final @NotNull AnnotationMetadata importMetadata) {
        annotationData = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableConfigurableComponents.class.getName()));
        if (annotationData == null) {
            throw new BeanInitializationException(
                    "@EnableConfigurableComponents is not present on importing class " + importMetadata.getClassName());
        }
    }

    /**
     * Sets the {@code ConfigProvider} supplier for current Spring context.
     * 
     * @param provider The {@code ConfigProvider} supplier for current Spring
     * context.
     */
    @Autowired
    public void setSpringConfigProvider(
            final ConfigProviderConfigurer provider) {
        this.springConfigProvider = provider;
    }

    /**
     * Exposes the configurable components configurer.
     * 
     * @return The configurable components configurer.
     */
    @Bean(name=CONFIGURER)
    public Configurer configurableComponentsConfigurer() {
        return this.exposedConfigurer;
    }

    /**
     * Exposes the Spring configurable components post-processor.
     * 
     * @param configurer The application provided or exposed configurer, if
     * any.
     * @return The configurable components post-processor.
     */
    @Bean(name=POST_PROCESSOR)
    public @NotNull ConfigurableComponentsPostProcessor configurableComponentsPostProcessor() {
        final Configurer configurer = Configurer.fromProvider(
                this.springConfigProvider.getConfigProvider());
        if (this.annotationData.getBoolean("exposeConfigurer")) {
            this.exposedConfigurer = configurer;
        }
        return new ConfigurableComponentsPostProcessor(configurer);
    }
}
