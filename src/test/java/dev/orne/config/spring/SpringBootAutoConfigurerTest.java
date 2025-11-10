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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

/**
 * Unit tests for {@link SpringBootAutoConfigurer}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0
 */
@Tag("ut")
class SpringBootAutoConfigurerTest {

    private static String originalLoggingSystem;
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    SpringBootAutoConfigurer.class))
            .withPropertyValues(
                    "spring.main.banner-mode=off"
            );

    @BeforeAll
    static void disableLogging() {
        originalLoggingSystem = System.getProperty(LoggingSystem.SYSTEM_PROPERTY);
        // Disable logging during tests
        System.setProperty(LoggingSystem.SYSTEM_PROPERTY, LoggingSystem.NONE);
    }

    @AfterAll
    static void restoreLogging() {
        if (originalLoggingSystem == null) {
            System.clearProperty(LoggingSystem.SYSTEM_PROPERTY);
        } else {
            System.setProperty(LoggingSystem.SYSTEM_PROPERTY, originalLoggingSystem);
        }
    }

    /**
     * Tests that by default all features are enabled.
     */
    @Test
    void testDefaultEnabledFeatures() {
        contextRunner.run(context -> {
            assertNotNull(context.getBean(ConfigAutowireCandidateResolverConfigurer.class));
            assertNotNull(context.getBean(ConfigurableComponentsConfigurer.class));
        });
    }

    /**
     * Tests that disabling the auto-configuration disables all features.
     */
    @Test
    void testDisableAutoConfig() {
        contextRunner.withPropertyValues(
                SpringBootAutoConfigurer.ENABLED + "=false").run(context -> {
            assertThrows(
                    NoSuchBeanDefinitionException.class,
                    () -> context.getBean(ConfigAutowireCandidateResolverConfigurer.class));
            assertThrows(
                    NoSuchBeanDefinitionException.class,
                    () -> context.getBean(ConfigurableComponentsConfigurer.class));
        });
    }

    /**
     * Tests that disabling injection feature works.
     */
    @Test
    void testDisableInjection() {
        contextRunner.withPropertyValues(
                SpringBootAutoConfigurer.INJECTION_ENABLED + "=false").run(context -> {
            assertThrows(
                    NoSuchBeanDefinitionException.class,
                    () -> context.getBean(ConfigAutowireCandidateResolverConfigurer.class));
            assertNotNull(context.getBean(ConfigurableComponentsConfigurer.class));
        });
    }

    /**
     * Tests that disabling configurable components feature works.
     */
    @Test
    void testDisableConfigurableBeans() {
        contextRunner.withPropertyValues(
                SpringBootAutoConfigurer.CONFIGURABLE_ENABLED + "=false").run(context -> {
            assertNotNull(context.getBean(ConfigAutowireCandidateResolverConfigurer.class));
            assertThrows(
                    NoSuchBeanDefinitionException.class,
                    () -> context.getBean(ConfigurableComponentsConfigurer.class));
        });
    }
}
