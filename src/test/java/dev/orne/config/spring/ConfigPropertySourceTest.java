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

import java.util.Map;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import dev.orne.config.Config;

/**
 * Unit tests for {@link ConfigPropertySource}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0
 */
@Tag("ut")
class ConfigPropertySourceTest {

    private static final String SIMPLE_PROP_KEY = "test.property";
    private static final String SIMPLE_PROP_VALUE = "testValue";
    private static final String COMPOSED_PROP_KEY = "test.composed";
    private static final String COMPOSED_PROP_PART1_KEY = "test.composed.part1";
    private static final String COMPOSED_PROP_PART2_KEY = "test.composed.part2";
    private static final String COMPOSED_PROP_VALUE = "${" +COMPOSED_PROP_PART1_KEY + "}${" + COMPOSED_PROP_PART2_KEY + "}";
    private static final String COMPOSED_PROP_PART1_VALUE = "part1";
    private static final String COMPOSED_PROP_PART2_VALUE = "part2";
    private static final String EXPECTED_COMPOSED_PROP_VALUE = COMPOSED_PROP_PART1_VALUE + COMPOSED_PROP_PART2_VALUE;

    /**
     * Test that properties defined in a single {@link ConfigPropertySource}
     * are available in the Spring environment.
     */
    @Test
    void whenSingleAnnotation_thenPropertiesAvailablesInEnvironment() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(SingleAnnotationConfiguration.class);
            context.refresh();
            final Environment env = context.getEnvironment();
            assertEquals(SIMPLE_PROP_VALUE, env.getProperty(SIMPLE_PROP_KEY));
            assertEquals(EXPECTED_COMPOSED_PROP_VALUE, env.getProperty(COMPOSED_PROP_KEY));
        }
    }

    /**
     * Test that properties defined in multiple {@link ConfigPropertySource}
     * annotations are available in the Spring environment.
     */
    @Test
    void whenMultipleAnnotation_thenPropertiesAvailablesInEnvironment() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(MultipleAnnotationConfiguration.class);
            context.refresh();
            final Environment env = context.getEnvironment();
            assertEquals(SIMPLE_PROP_VALUE, env.getProperty(SIMPLE_PROP_KEY));
            assertEquals(EXPECTED_COMPOSED_PROP_VALUE, env.getProperty(COMPOSED_PROP_KEY));
        }
    }

    /**
     * Test that properties defined in a single {@link ConfigPropertySource}
     * in a parent context and another in a child context are available
     * in the child Spring environment.
     */
    @Test
    void whenSingleAnnotationInHierarchy_thenPropertiesAvailablesInEnvironment() {
        try (final AnnotationConfigApplicationContext parentContext = new AnnotationConfigApplicationContext();
                final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            parentContext.register(ParentSingleAnnotationConfiguration.class);
            parentContext.refresh();
            final Environment parentEnv = parentContext.getEnvironment();
            assertEquals(SIMPLE_PROP_VALUE, parentEnv.getProperty(SIMPLE_PROP_KEY));
            assertNull(parentEnv.getProperty(COMPOSED_PROP_KEY));
            context.setParent(parentContext);
            context.register(ChildSingleAnnotationConfiguration.class);
            context.refresh();
            final Environment env = context.getEnvironment();
            assertEquals(SIMPLE_PROP_VALUE, env.getProperty(SIMPLE_PROP_KEY));
            assertEquals(EXPECTED_COMPOSED_PROP_VALUE, env.getProperty(COMPOSED_PROP_KEY));
        }
    }

    /**
     * Test that properties defined in multiple {@link ConfigPropertySource}
     * annotations in a context hierarchy are available
     * in the child Spring environment.
     */
    @Test
    void whenMultipleAnnotationInHierarchy_thenPropertiesAvailablesInEnvironment() {
        try (final AnnotationConfigApplicationContext parentContext = new AnnotationConfigApplicationContext();
                final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            parentContext.register(ParentMultipleAnnotationConfiguration.class);
            parentContext.refresh();
            final Environment parentEnv = parentContext.getEnvironment();
            assertEquals(SIMPLE_PROP_VALUE, parentEnv.getProperty(SIMPLE_PROP_KEY));
            context.register(ChildMultipleAnnotationConfiguration.class);
            context.setParent(parentContext);
            context.refresh();
            final Environment env = context.getEnvironment();
            assertEquals(SIMPLE_PROP_VALUE, env.getProperty(SIMPLE_PROP_KEY));
            assertEquals(COMPOSED_PROP_PART1_VALUE, env.getProperty(COMPOSED_PROP_PART1_KEY));
            assertEquals(COMPOSED_PROP_PART2_VALUE, env.getProperty(COMPOSED_PROP_PART2_KEY));
            assertEquals(EXPECTED_COMPOSED_PROP_VALUE, env.getProperty(COMPOSED_PROP_KEY));
        }
    }

    @Configuration
    @ConfigPropertySource("configSource")
    static class SingleAnnotationConfiguration {
        @Bean("configSource")
        public Config singleSource() {
            return Config.fromProperties()
                    .add(Map.of(
                            SIMPLE_PROP_KEY, SIMPLE_PROP_VALUE,
                            COMPOSED_PROP_KEY, COMPOSED_PROP_VALUE,
                            COMPOSED_PROP_PART1_KEY, COMPOSED_PROP_PART1_VALUE,
                            COMPOSED_PROP_PART2_KEY, COMPOSED_PROP_PART2_VALUE))
                    .build();
        }
    }

    @Configuration
    @ConfigPropertySource("configSource1")
    @ConfigPropertySource("configSource2")
    static class MultipleAnnotationConfiguration {
        @Bean("configSource1")
        public Config configPropertySource1() {
            return Config.fromProperties()
                    .add(Map.of(
                            SIMPLE_PROP_KEY, SIMPLE_PROP_VALUE))
                    .build();
        }
        @Bean("configSource2")
        public Config configPropertySource2() {
            return Config.fromProperties()
                    .add(Map.of(
                            COMPOSED_PROP_KEY, COMPOSED_PROP_VALUE,
                            COMPOSED_PROP_PART1_KEY, COMPOSED_PROP_PART1_VALUE,
                            COMPOSED_PROP_PART2_KEY, COMPOSED_PROP_PART2_VALUE))
                    .build();
        }
    }

    @Configuration
    @ConfigPropertySource("parentConfigSource")
    static class ParentSingleAnnotationConfiguration {
        @Bean("parentConfigSource")
        public Config singleSource() {
            return Config.fromProperties()
                    .add(Map.of(
                            SIMPLE_PROP_KEY, SIMPLE_PROP_VALUE))
                    .build();
        }
    }

    @Configuration
    @ConfigPropertySource("childConfigSource")
    static class ChildSingleAnnotationConfiguration {
        @Bean("childConfigSource")
        public Config singleSource() {
            return Config.fromProperties()
                    .add(Map.of(
                            COMPOSED_PROP_KEY, COMPOSED_PROP_VALUE,
                            COMPOSED_PROP_PART1_KEY, COMPOSED_PROP_PART1_VALUE,
                            COMPOSED_PROP_PART2_KEY, COMPOSED_PROP_PART2_VALUE))
                    .build();
        }
    }

    @Configuration
    @ConfigPropertySource("parentConfigSource1")
    @ConfigPropertySource("parentConfigSource2")
    static class ParentMultipleAnnotationConfiguration {
        @Bean("parentConfigSource1")
        public Config configPropertySource1() {
            return Config.fromProperties()
                    .add(Map.of(
                            SIMPLE_PROP_KEY, SIMPLE_PROP_VALUE))
                    .build();
        }
        @Bean("parentConfigSource2")
        public Config configPropertySource2() {
            return Config.fromProperties()
                    .add(Map.of(
                            COMPOSED_PROP_KEY, COMPOSED_PROP_VALUE))
                    .build();
        }
    }

    @Configuration
    @ConfigPropertySource("childConfigSource1")
    @ConfigPropertySource("childConfigSource2")
    static class ChildMultipleAnnotationConfiguration {
        @Bean("childConfigSource1")
        public Config configPropertySource1() {
            return Config.fromProperties()
                    .add(Map.of(
                            COMPOSED_PROP_PART1_KEY, COMPOSED_PROP_PART1_VALUE))
                    .build();
        }
        @Bean("childConfigSource2")
        public Config configPropertySource2() {
            return Config.fromProperties()
                    .add(Map.of(
                            COMPOSED_PROP_PART2_KEY, COMPOSED_PROP_PART2_VALUE))
                    .build();
        }
    }
}
