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

import java.util.ArrayList;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Spring Boot auto configuration for Orne Config integration.
 * <p>
 * By default enables all the features as if {@code EnableOrneConfig} was used.
 * Set environment property {@value #ENABLED} to {@code false} to disable
 * the auto configuration.
 * <p>
 * To disable specific features, set the following environment properties
 * to {@code false}:
 * <ul>
 *   <li>{@value #INJECTION_ENABLED}: Disable preferred {@code Config}
 *   injection.</li>
 *  <li>{@value #CONFIGURABLE_ENABLED}: Disable configurable components
 *  support.</li>
 * </ul>
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-11
 * @since 1.0
 * @see EnableOrneConfig
 */
@AutoConfiguration
@ConditionalOnProperty(
        name = SpringBootAutoConfigurer.ENABLED,
        matchIfMissing = true)
@Import(SpringBootAutoConfigurer.FeaturesSelector.class)
@API(status = API.Status.STABLE, since = "1.0")
public class SpringBootAutoConfigurer {

    /** The configuration properties prefix. */
    private static final String PROPERTY_PREFIX =
            "orne.config.spring.boot.starter.";
    /** The property to enable or disable the auto configuration. */
    @API(status = API.Status.STABLE, since = "1.0")
    public static final String ENABLED =
            PROPERTY_PREFIX + "enabled";
    /** The property to enable or disable preferred configuration injection. */
    @API(status = API.Status.STABLE, since = "1.0")
    public static final String INJECTION_ENABLED =
            PROPERTY_PREFIX + "injection.enabled";
    /** The property to enable or disable configurable components. */
    @API(status = API.Status.STABLE, since = "1.0")
    public static final String CONFIGURABLE_ENABLED =
            PROPERTY_PREFIX + "configurable.enabled";

    /**
     * Creates a new instance.
     */
    public SpringBootAutoConfigurer() {
        super();
    }

    /**
     * Import selector for the features to enable based on environment
     * properties.
     * 
     * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0, 2025-11
     * @since 1.0
     */
    @API(status = API.Status.INTERNAL, since = "1.0")
    public static class FeaturesSelector
    implements ImportSelector, EnvironmentAware {

        /** The Spring environment. */
        private Environment environment;

        /**
         * Creates a new instance.
         */
        public FeaturesSelector() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setEnvironment(
                final @NotNull Environment environment) {
            this.environment = environment;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String[] selectImports(
                final AnnotationMetadata importingClassMetadata) {
            final String injectionEnabled =
                    environment.getProperty(INJECTION_ENABLED);
            final String configurableEnabled =
                    environment.getProperty(CONFIGURABLE_ENABLED);
            final ArrayList<String> configurations = new ArrayList<>();
            if (injectionEnabled == null || Boolean.valueOf(injectionEnabled)) {
                configurations.add(ConfigAutowireCandidateResolverConfigurer.class.getName());
            }
            if (configurableEnabled == null || Boolean.valueOf(configurableEnabled)) {
                configurations.add(ConfigurableComponentsConfigurer.class.getName());
            }
            return configurations.toArray(new String[0]);
        }
    }
}
