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

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import dev.orne.config.Config;
import dev.orne.config.PreferredConfig;

/**
 * Unit tests for {@link EnablePreferredConfigInyection}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0
 */
@Tag("ut")
class EnablePreferredConfigInyectionTest {

    private static final String CFG_ID_PROP = "cfg.id";
    private static final String DEFAULT_CFG_ID = "env";
    private static final String PRIMARY_CFG_ID = "primary";
    private static final String SUBTYPE_CFG_ID = "subtype";
    private static final String SUBTYPE2_CFG_ID = "subtype2";

    /**
     * Tests that when there are no configuration beans and
     * no qualifier is present,
     * the default configuration is injected.
     */
    @Test
    void whenNoConfigAndNoQualifier_thenDefaultConfigIsInjected() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(NoConfigAnnotConfiguration.class);
            context.refresh();
            
            final ConfigConstructorBean bean = context.getBean(ConfigConstructorBean.class);
            assertEquals(DEFAULT_CFG_ID, bean.cfgId);
        }
    }

    /**
     * Tests that when there are no configuration beans and
     * a qualifier is present,
     * the default configuration is injected.
     */
    @Test
    void whenNoConfigAndQualifier_thenDefaultConfigIsInjected() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(NoConfigAnnotConfiguration.class);
            context.refresh();
            
            final ConfigConstructorBean bean = context.getBean(ConfigConstructorBean.class);
            assertEquals(DEFAULT_CFG_ID, bean.cfgId);
        }
    }

    /**
     * Tests that when there are no configuration beans and
     * qualifier is present but no default configuration is allowed,
     * an exception is thrown.
     */
    @Test
    void whenNoConfigAndNoDefaultQualifier_thenExceptionIsThrown() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(NoConfigAnnotNoDefaultConfiguration.class);
            assertThrows(
                    UnsatisfiedDependencyException.class,
                    context::refresh);
        }
    }

    /**
     * Tests that when there is a single configuration bean of preferred type and
     * no qualifier is present,
     * the default configuration is injected.
     */
    @Test
    void whenSubtypeConfigAndNoQualifier_thenDefaultConfigIsInjected() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(SubtypeConfigNoAnnotConfiguration.class);
            context.refresh();
            
            final ConfigConstructorBean bean = context.getBean(ConfigConstructorBean.class);
            assertEquals(DEFAULT_CFG_ID, bean.cfgId);
        }
    }

    /**
     * Tests that when there is a single configuration bean of preferred type and
     * qualifier is present,
     * the preferred configuration is injected.
     */
    @Test
    void whenSubtypeConfigAndQualifier_thenDefaultConfigIsInjected() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(SubtypeConfigAnnotConfiguration.class);
            context.refresh();
            
            final ConfigConstructorBean bean = context.getBean(ConfigConstructorBean.class);
            assertEquals(SUBTYPE_CFG_ID, bean.cfgId);
        }
    }

    /**
     * Tests that when there is a single configuration bean of preferred type and
     * qualifier is present but no default configuration is allowed,
     * the preferred configuration is injected.
     */
    @Test
    void whenSubtypeConfigAndNoDefaultQualifier_thenExceptionIsThrown() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(SubtypeConfigAnnotNoDefaultConfiguration.class);
            context.refresh();
            
            final ConfigConstructorBean bean = context.getBean(ConfigConstructorBean.class);
            assertEquals(SUBTYPE_CFG_ID, bean.cfgId);
        }
    }

    /**
     * Tests that when there are multiple preferred configurations and
     * no qualifier is present,
     * the default configuration is injected.
     */
    @Test
    void whenMultipleConfigsAndNoQualifier_thenDefaultConfigIsInjected() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(MultipleConfigNoAnnotConfiguration.class);
            context.refresh();
            
            final ConfigConstructorBean bean = context.getBean(ConfigConstructorBean.class);
            assertEquals(DEFAULT_CFG_ID, bean.cfgId);
        }
    }

    /**
     * Tests that when there are multiple preferred configurations and
     * qualifier is present,
     * one of the preferred configuration is injected.
     */
    @Test
    void whenMultipleConfigsAndQualifier_thenSubtypeConfigIsInjected() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(MultipleConfigAnnotConfiguration.class);
            context.refresh();
            
            final ConfigConstructorBean bean = context.getBean(ConfigConstructorBean.class);
            assertNotEquals(DEFAULT_CFG_ID, bean.cfgId);
            assertNotEquals(PRIMARY_CFG_ID, bean.cfgId);
        }
    }

    /**
     * Tests that when there are multiple preferred configurations and
     * and qualifier is present but no default configuration is allowed,
     * one of the preferred configuration is injected.
     */
    @Test
    void whenMultipleConfigsAndNoDefaultQualifier_thenPrimaryConfigIsInjected() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(MultipleConfigAnnotNoDefaultConfiguration.class);
            context.refresh();
            
            final ConfigConstructorBean bean = context.getBean(ConfigConstructorBean.class);
            assertNotEquals(DEFAULT_CFG_ID, bean.cfgId);
            assertNotEquals(PRIMARY_CFG_ID, bean.cfgId);
        }
    }

    /**
     * Tests that when there are multiple configurations with customizer and
     * no qualifier is present,
     * the custom default configuration is injected.
     */
    @Test
    void whenCustomizedAndNoQualifier_thenDefaultConfigIsInjected() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(CustomizedConfigNoAnnotConfiguration.class);
            context.refresh();
            
            final ConfigConstructorBean bean = context.getBean(ConfigConstructorBean.class);
            assertEquals(PRIMARY_CFG_ID, bean.cfgId);
        }
    }

    /**
     * Tests that when there are multiple configurations with customizer and
     * qualifier is present,
     * the custom preferred configuration is injected.
     */
    @Test
    void whenCustomizedAndQualifier_thenSubtypeConfigIsInjected() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(CustomizedConfigAnnotConfiguration.class);
            context.refresh();
            
            final ConfigConstructorBean bean = context.getBean(ConfigConstructorBean.class);
            assertEquals(SUBTYPE_CFG_ID, bean.cfgId);
        }
    }

    /**
     * Tests that when there are multiple configurations with customizer and
     * qualifier is present but no default configuration is allowed,
     * the custom preferred configuration is injected.
     */
    @Test
    void whenCustomizedAndNoDefaultQualifier_thenPrimaryConfigIsInjected() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(CustomizedConfigAnnotNoDefaultConfiguration.class);
            context.refresh();
            
            final ConfigConstructorBean bean = context.getBean(ConfigConstructorBean.class);
            assertEquals(SUBTYPE_CFG_ID, bean.cfgId);
        }
    }

    interface ConfigSubtype extends Config {

        default String getConfigId() {
            return get(CFG_ID_PROP);
        }
    }

    interface ConfigSubtype2 extends ConfigSubtype {
        
    }

    @Configuration
    @PropertySource("classpath:dev/orne/config/spring/config.autowire.test.properties")
    @EnablePreferredConfigInyection
    static class NoConfigConfiguration {
        // No extra beans
    }

    static class NoConfigNoAnnotConfiguration
    extends NoConfigConfiguration {

        @Bean
        public ConfigConstructorBean testBean(
                final Config config) {
            return new ConfigConstructorBean(config);
        }
    }

    static class NoConfigAnnotConfiguration
    extends NoConfigConfiguration {

        @Bean
        public ConfigConstructorBean testBean(
                @PreferredConfig(ConfigSubtype.class)
                final Config config) {
            return new ConfigConstructorBean(config);
        }
    }

    static class NoConfigAnnotNoDefaultConfiguration
    extends NoConfigConfiguration {

        @Bean
        public ConfigConstructorBean testBean(
                @PreferredConfig(value = ConfigSubtype.class, fallbackToDefaultConfig = false)
                final Config config) {
            return new ConfigConstructorBean(config);
        }
    }

    static class SubtypeConfigConfiguration
    extends NoConfigConfiguration {

        @Bean
        public ConfigSubtype subtypeConfig() {
            return Config.fromProperties()
                    .add(Map.of(
                            CFG_ID_PROP, SUBTYPE_CFG_ID))
                    .as(ConfigSubtype.class);
        }
    }

    static class SubtypeConfigNoAnnotConfiguration
    extends SubtypeConfigConfiguration {

        @Bean
        public ConfigConstructorBean testBean(
                final Config config) {
            return new ConfigConstructorBean(config);
        }
    }

    static class SubtypeConfigAnnotConfiguration
    extends SubtypeConfigConfiguration {

        @Bean
        public ConfigConstructorBean testBean(
                @PreferredConfig(ConfigSubtype.class)
                final Config config) {
            return new ConfigConstructorBean(config);
        }
    }

    static class SubtypeConfigAnnotNoDefaultConfiguration
    extends SubtypeConfigConfiguration {

        @Bean
        public ConfigConstructorBean testBean(
                @PreferredConfig(value = ConfigSubtype.class, fallbackToDefaultConfig = false)
                final Config config) {
            return new ConfigConstructorBean(config);
        }
    }

    static class MultipleConfigConfiguration
    extends NoConfigConfiguration {

        @Bean
        public Config primaryConfig() {
            return Config.fromProperties()
                    .add(Map.of(
                            CFG_ID_PROP, PRIMARY_CFG_ID))
                    .build();
        }

        @Bean
        public ConfigSubtype subtypeConfig() {
            return Config.fromProperties()
                    .add(Map.of(
                            CFG_ID_PROP, SUBTYPE_CFG_ID))
                    .as(ConfigSubtype.class);
        }

        @Bean
        public ConfigSubtype2 subtypeConfig2() {
            return Config.fromProperties()
                    .add(Map.of(
                            CFG_ID_PROP, SUBTYPE2_CFG_ID))
                    .as(ConfigSubtype2.class);
        }
    }

    static class MultipleConfigNoAnnotConfiguration
    extends MultipleConfigConfiguration {

        @Bean
        public ConfigConstructorBean testBean(
                final Config config) {
            return new ConfigConstructorBean(config);
        }
    }

    static class MultipleConfigAnnotConfiguration
    extends MultipleConfigConfiguration {

        @Bean
        public ConfigConstructorBean testBean(
                @PreferredConfig(ConfigSubtype.class)
                final Config config) {
            return new ConfigConstructorBean(config);
        }
    }

    static class MultipleConfigAnnotNoDefaultConfiguration
    extends MultipleConfigConfiguration {

        @Bean
        public ConfigConstructorBean testBean(
                @PreferredConfig(value = ConfigSubtype.class, fallbackToDefaultConfig = false)
                final Config config) {
            return new ConfigConstructorBean(config);
        }
    }

    static class CustomizedConfigConfiguration
    extends MultipleConfigConfiguration
    implements ConfigProviderCustomizer {

        @Override
        public @NotNull Config configureDefaultConfig(
                final @NotNull Map<String, Config> configs) {
            return primaryConfig();
        }

        @Override
        public void registerAdditionalConfigs(
                final @NotNull ConfigRegistry builder,
                final @NotNull Map<String, Config> configs) {
            // Ensure priority to subtypeConfig over subtypeConfig2
            builder.add(subtypeConfig());
            ConfigProviderCustomizer.super.registerAdditionalConfigs(builder, configs);
        }
    }

    static class CustomizedConfigNoAnnotConfiguration
    extends CustomizedConfigConfiguration {

        @Bean
        public ConfigConstructorBean testBean(
                final Config config) {
            return new ConfigConstructorBean(config);
        }
    }

    static class CustomizedConfigAnnotConfiguration
    extends CustomizedConfigConfiguration {

        @Bean
        public ConfigConstructorBean testBean(
                @PreferredConfig(ConfigSubtype.class)
                final Config config) {
            return new ConfigConstructorBean(config);
        }
    }

    static class CustomizedConfigAnnotNoDefaultConfiguration
    extends CustomizedConfigConfiguration {

        @Bean
        public ConfigConstructorBean testBean(
                @PreferredConfig(value = ConfigSubtype.class, fallbackToDefaultConfig = false)
                final Config config) {
            return new ConfigConstructorBean(config);
        }
    }

    static class ConfigConstructorBean {

        private String cfgId;

        public ConfigConstructorBean(
                final Config config) {
            this.cfgId = config.as(ConfigSubtype.class).getConfigId();
        }
    }
}
