package dev.orne.config.examples;

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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import dev.orne.config.Config;
import dev.orne.config.Configurable;
import dev.orne.config.ConfigurableProperty;
import dev.orne.config.PreferredConfig;
import dev.orne.config.spring.ConfigPropertySource;
import dev.orne.config.spring.ConfigProviderCustomizer;
import dev.orne.config.spring.EnableConfigurableComponents;
import dev.orne.config.spring.EnableOrneConfig;
import dev.orne.config.spring.EnablePreferredConfigInjection;

/**
 * Tests of Spring integration usage examples of public site.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @see https://orne-dev.github.io/java-config/spring.html
 */
@Tag("ut")
@Tag("examples")
class SpringExamplesTest {

    private static final String CONFIG_SOURCE_BEAN = "configSource";
    private static final String NO_QUALIFIER_BEAN = "noQualifierBean";
    private static final String QUALIFIER_BEAN = "qualifierBean";
    private static final String NO_QUALIFIER_CONFIGURABLE_BEAN = "noQualifierConfigurableBean";
    private static final String QUALIFIER_CONFIGURABLE_BEAN = "qualifierConfigurableBean";

    private static final String CFG_ID_PROP = "cfg.id";
    private static final String DEFAULT_CFG_ID = "env";
    private static final String CUSTOM_CFG_ID = "custom";
    private static final String SUBTYPE_CFG_ID = "subtype";
    private static final String CUSTOM_SUBTYPE_CFG_ID = "customSubtype";

    private static final String SOURCE_PROP = "config.source.prop";
    private static final String SOURCE_PROP_VALUE = "fromConfig";

    /**
     * Example of full features activation.
     */
    @Test
    void exampleOfEnableFullFeatures() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(FullFeaturesExample.class);
            context.refresh();
            
            assertEquals(DEFAULT_CFG_ID, context.getEnvironment().getProperty(CFG_ID_PROP));
            assertEquals(SOURCE_PROP_VALUE, context.getEnvironment().getProperty(SOURCE_PROP));
            final ConfigConstructorBean defConstructorBean = context.getBean(
                    NO_QUALIFIER_BEAN,
                    ConfigConstructorBean.class);
            assertEquals(DEFAULT_CFG_ID, defConstructorBean.cfgId);
            final ConfigConstructorBean prefConstructorBean = context.getBean(
                    QUALIFIER_BEAN,
                    ConfigConstructorBean.class);
            assertEquals(SUBTYPE_CFG_ID, prefConstructorBean.cfgId);

            final ConfigurableBean defConfigurableBean = context.getBean(
                    NO_QUALIFIER_CONFIGURABLE_BEAN,
                    ConfigurableBean.class);
            assertEquals(DEFAULT_CFG_ID, defConfigurableBean.cfgId);
            assertEquals(DEFAULT_CFG_ID, defConfigurableBean.envCfgId);
            assertEquals(DEFAULT_CFG_ID, defConfigurableBean.propCfgId);
            final ConfigurableBean prefConfigurableBean = context.getBean(
                    QUALIFIER_CONFIGURABLE_BEAN,
                    ConfigurableBean.class);
            assertEquals(SUBTYPE_CFG_ID, prefConfigurableBean.cfgId);
            assertEquals(DEFAULT_CFG_ID, prefConfigurableBean.envCfgId);
            assertEquals(SUBTYPE_CFG_ID, prefConfigurableBean.propCfgId);
        }
    }

    /**
     * Example of full features activation with configuration provider customization.
     */
    @Test
    void exampleOfCustomEnableFullFeatures() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(CustomizedFullFeaturesExample.class);
            context.refresh();
            
            assertEquals(DEFAULT_CFG_ID, context.getEnvironment().getProperty(CFG_ID_PROP));
            assertEquals(SOURCE_PROP_VALUE, context.getEnvironment().getProperty(SOURCE_PROP));
            final ConfigConstructorBean defConstructorBean = context.getBean(
                    NO_QUALIFIER_BEAN,
                    ConfigConstructorBean.class);
            assertEquals(CUSTOM_CFG_ID, defConstructorBean.cfgId);
            final ConfigConstructorBean prefConstructorBean = context.getBean(
                    QUALIFIER_BEAN,
                    ConfigConstructorBean.class);
            assertEquals(CUSTOM_SUBTYPE_CFG_ID, prefConstructorBean.cfgId);

            final ConfigurableBean defConfigurableBean = context.getBean(
                    NO_QUALIFIER_CONFIGURABLE_BEAN,
                    ConfigurableBean.class);
            assertEquals(CUSTOM_CFG_ID, defConfigurableBean.cfgId);
            assertEquals(DEFAULT_CFG_ID, defConfigurableBean.envCfgId);
            assertEquals(CUSTOM_CFG_ID, defConfigurableBean.propCfgId);
            final ConfigurableBean prefConfigurableBean = context.getBean(
                    QUALIFIER_CONFIGURABLE_BEAN,
                    ConfigurableBean.class);
            assertEquals(CUSTOM_SUBTYPE_CFG_ID, prefConfigurableBean.cfgId);
            assertEquals(DEFAULT_CFG_ID, prefConfigurableBean.envCfgId);
            assertEquals(CUSTOM_SUBTYPE_CFG_ID, prefConfigurableBean.propCfgId);
        }
    }

    /**
     * Example of using Config instance as a PropertySource.
     */
    @Test
    void exampleOfConfigPropertySource() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(ConfigPropertySourceExample.class);
            context.refresh();
            
            assertEquals(SOURCE_PROP_VALUE, context.getEnvironment().getProperty(SOURCE_PROP));
        }
    }

    /**
     * Example of full features activation.
     */
    @Test
    void exampleOfPreferredConfigInjection() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(EnablePreferredConfigInjectionExample.class);
            context.refresh();
            
            assertEquals(DEFAULT_CFG_ID, context.getEnvironment().getProperty(CFG_ID_PROP));
            final ConfigConstructorBean defConstructorBean = context.getBean(
                    NO_QUALIFIER_BEAN,
                    ConfigConstructorBean.class);
            assertEquals(DEFAULT_CFG_ID, defConstructorBean.cfgId);
            final ConfigConstructorBean prefConstructorBean = context.getBean(
                    QUALIFIER_BEAN,
                    ConfigConstructorBean.class);
            assertEquals(SUBTYPE_CFG_ID, prefConstructorBean.cfgId);
        }
    }

    /**
     * Example of full features activation.
     */
    @Test
    void exampleOfEnableConfigurableComponents() {
        try (final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(EnableConfigurableExample.class);
            context.refresh();
            
            assertEquals(DEFAULT_CFG_ID, context.getEnvironment().getProperty(CFG_ID_PROP));
            final ConfigurableBean defConfigurableBean = context.getBean(
                    NO_QUALIFIER_CONFIGURABLE_BEAN,
                    ConfigurableBean.class);
            assertEquals(DEFAULT_CFG_ID, defConfigurableBean.cfgId);
            assertEquals(DEFAULT_CFG_ID, defConfigurableBean.envCfgId);
            assertEquals(DEFAULT_CFG_ID, defConfigurableBean.propCfgId);
            final ConfigurableBean prefConfigurableBean = context.getBean(
                    QUALIFIER_CONFIGURABLE_BEAN,
                    ConfigurableBean.class);
            assertEquals(SUBTYPE_CFG_ID, prefConfigurableBean.cfgId);
            assertEquals(DEFAULT_CFG_ID, prefConfigurableBean.envCfgId);
            assertEquals(SUBTYPE_CFG_ID, prefConfigurableBean.propCfgId);
        }
    }

    interface ConfigSubtype extends Config {

        default String getConfigId() {
            return get(CFG_ID_PROP);
        }
    }

    static class ConfigConstructorBean {

        private String cfgId;

        public ConfigConstructorBean(
                final Config config) {
            this.cfgId = config.as(ConfigSubtype.class).getConfigId();
        }
    }

    public static class ConfigurableBean
    implements Configurable {

        private String cfgId;
        @Autowired
        @Value("${" + CFG_ID_PROP + ":#{null}}")
        private String envCfgId;
        @ConfigurableProperty(CFG_ID_PROP)
        private String propCfgId;

        public ConfigurableBean() {
            super();
        }

        @Override
        public void configure(
                final @NotNull Config config) {
            this.cfgId = config.as(ConfigSubtype.class).getConfigId();
        }

        public void setCfgId(String cfgId) {
            this.cfgId = cfgId;
        }

        public void setEnvCfgId(String envCfgId) {
            this.envCfgId = envCfgId;
        }

        public void setPropCfgId(String propCfgId) {
            this.propCfgId = propCfgId;
        }
    }

    @PreferredConfig(ConfigSubtype.class)
    public static class PreferredConfigurableBean
    extends ConfigurableBean{

        public PreferredConfigurableBean() {
            super();
        }
    }

    @Configuration
    @EnableOrneConfig
    @PropertySource("classpath:dev/orne/config/spring/config.autowire.test.properties")
    @ConfigPropertySource(CONFIG_SOURCE_BEAN)
    static class FullFeaturesExample {

        @Bean(CONFIG_SOURCE_BEAN)
        public Config configSource() {
            return Config.fromProperties()
                    .add(Map.of(
                            SOURCE_PROP, SOURCE_PROP_VALUE))
                    .build();
        }

        @Bean
        public ConfigSubtype subtypeConfig(
                Config defaultConfig) {
            return Config.fromProperties()
                    .withParent(defaultConfig)
                    .withOverrideParentProperties()
                    .add(Map.of(
                            CFG_ID_PROP, SUBTYPE_CFG_ID))
                    .as(ConfigSubtype.class);
        }

        @Bean(NO_QUALIFIER_BEAN)
        public ConfigConstructorBean defaultConfigConstructorBean(
                final Config config) {
            return new ConfigConstructorBean(config);
        }

        @Bean(QUALIFIER_BEAN)
        public ConfigConstructorBean configConstructorBean(
                @Value("${" + CFG_ID_PROP + ":#{null}}")
                final String envId,
                @PreferredConfig(ConfigSubtype.class)
                final Config config) {
            return new ConfigConstructorBean(config);
        }

        @Bean(NO_QUALIFIER_CONFIGURABLE_BEAN)
        public ConfigurableBean configurableBean() {
            return new ConfigurableBean();
        }

        @Bean(QUALIFIER_CONFIGURABLE_BEAN)
        public PreferredConfigurableBean preferredConfigurableBean() {
            return new PreferredConfigurableBean();
        }
    }

    @Configuration
    @EnableOrneConfig
    @PropertySource("classpath:dev/orne/config/spring/config.autowire.test.properties")
    @ConfigPropertySource(CONFIG_SOURCE_BEAN)
    static class CustomizedFullFeaturesExample
    extends FullFeaturesExample
    implements ConfigProviderCustomizer {

        @Override
        public Config configureDefaultConfig(
                @NotNull Map<String, Config> configs) {
            return Config.fromProperties()
                    .add(Map.of(
                            CFG_ID_PROP, CUSTOM_CFG_ID))
                    .build();
        }

        @Override
        public void registerAdditionalConfigs(
                ConfigRegistry regitry,
                Map<String, Config> configs) {
            regitry.add(
                    Config.fromProperties()
                        .withParent(configs.get("subtypeConfig"))
                        .withOverrideParentProperties()
                        .add(Map.of(
                                CFG_ID_PROP, CUSTOM_SUBTYPE_CFG_ID))
                        .as(ConfigSubtype.class));
            ConfigProviderCustomizer.super.registerAdditionalConfigs(regitry, configs);
        }
    }

    @Configuration
    @ConfigPropertySource(CONFIG_SOURCE_BEAN)
    static class ConfigPropertySourceExample {

        @Bean(CONFIG_SOURCE_BEAN)
        public Config configSource() {
            return Config.fromProperties()
                    .add(Map.of(
                            SOURCE_PROP, SOURCE_PROP_VALUE))
                    .build();
        }
    }

    @Configuration
    @PropertySource("classpath:dev/orne/config/spring/config.autowire.test.properties")
    @EnablePreferredConfigInjection
    static class EnablePreferredConfigInjectionExample {

        @Bean
        public ConfigSubtype subtypeConfig(
                Config defaultConfig) {
            return Config.fromProperties()
                    .withParent(defaultConfig)
                    .withOverrideParentProperties()
                    .add(Map.of(
                            CFG_ID_PROP, SUBTYPE_CFG_ID))
                    .as(ConfigSubtype.class);
        }

        @Bean(NO_QUALIFIER_BEAN)
        public ConfigConstructorBean defaultConfigConstructorBean(
                final Config config) {
            return new ConfigConstructorBean(config);
        }

        @Bean(QUALIFIER_BEAN)
        public ConfigConstructorBean configConstructorBean(
                @PreferredConfig(ConfigSubtype.class)
                final Config config) {
            return new ConfigConstructorBean(config);
        }
    }

    @Configuration
    @PropertySource("classpath:dev/orne/config/spring/config.autowire.test.properties")
    @EnableConfigurableComponents
    static class EnableConfigurableExample {

        @Bean
        public ConfigSubtype subtypeConfig() {
            // No parent config here, requires injection
            return Config.fromProperties()
                    .add(Map.of(
                            CFG_ID_PROP, SUBTYPE_CFG_ID))
                    .as(ConfigSubtype.class);
        }

        @Bean(NO_QUALIFIER_CONFIGURABLE_BEAN)
        public ConfigurableBean configurableBean() {
            return new ConfigurableBean();
        }

        @Bean(QUALIFIER_CONFIGURABLE_BEAN)
        public PreferredConfigurableBean preferredConfigurableBean() {
            return new PreferredConfigurableBean();
        }
    }
}
