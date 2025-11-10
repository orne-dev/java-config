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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import dev.orne.config.Config;
import dev.orne.config.spring.boot.NopSpringBootApp;

@Tag("it")
@SpringBootTest(
        classes = NopSpringBootApp.class,
        properties = {
            "spring.main.banner-mode=off",
            "app.property=someValue"
        })
class SpringBootAutoConfigurationIT {

    private static String originalLoggingSystem;
    private @Autowired ApplicationContext context;
    private @Autowired Config defaultConfig;

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
     * Tests that Spring auto configuration works as expected.
     */
    @Test
    void autoConfigures() {
        assertNotNull(context.getBean(ConfigAutowireCandidateResolverConfigurer.class));
        assertNotNull(context.getBean(ConfigurableComponentsConfigurer.class));
        assertNotNull(defaultConfig);
        assertEquals("someValue", defaultConfig.get("app.property"));
    }
}
