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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.orne.config.Config;
import dev.orne.config.Configurable;
import dev.orne.config.PreferredConfig;

import org.junit.jupiter.api.Tag;

/**
 * Unit tests for {@link EnableOrneConfig}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0
 */
@Tag("ut")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EnableOrneConfigTest.SpringConfig.class)
class EnableOrneConfigTest {

    private static final String CONFIG_SOURCE_BEAN = "configSource";
    private static final String CONFIG_SUBTYPE = "configSubtype";
    private static final String NO_QUALIFIER_BEAN = "noQualifierBean";
    private static final String QUALIFIER_BEAN = "qualifierBean";
    private static final String NO_QUALIFIER_CONFIGURABLE_BEAN = "noQualifierConfigurableBean";
    private static final String QUALIFIER_CONFIGURABLE_BEAN = "qualifierConfigurableBean";

    private static final String CFG_ID_PROP = "cfg.id";
    private static final String DEFAULT_CFG_ID = "env";
    private static final String SUBTYPE_CFG_ID = "subtype";

    private static final String SOURCE_PROP = "config.source.prop";
    private static final String SOURCE_PROP_VALUE = "fromConfig";

    @Autowired
    private Environment environment;
    @Autowired
    @Qualifier(NO_QUALIFIER_BEAN)
    private ConfigConstructorBean constructorBean;
    @Autowired
    @Qualifier(QUALIFIER_BEAN)
    private ConfigConstructorBean qualifierConstructorBean;
    @Autowired
    @Qualifier(NO_QUALIFIER_CONFIGURABLE_BEAN)
    private ConfigurableBean configurableBean;
    @Autowired
    @Qualifier(QUALIFIER_CONFIGURABLE_BEAN)
    private ConfigurableBean qualifierConfigurableBean;

    /**
     * Tests that environment properties are available.
     */
    @Test
    void testEnvironment() {
        assertEquals(DEFAULT_CFG_ID, environment.getProperty(CFG_ID_PROP));
        assertEquals(SOURCE_PROP_VALUE, environment.getProperty(SOURCE_PROP));
    }

    /**
     * Tests that when no qualifier is present, the default configuration is
     * injected to the constructor based configuration bean.
     */
    @Test
    void givenNoQualifier_thenContructorBean_thenDefaultConfigIsInjected() {
            assertEquals(DEFAULT_CFG_ID, constructorBean.cfgId);
    }

    /**
     * Tests that when qualifier is present, the configuration sub-type is
     * injected to the constructor based configuration bean.
     */
    @Test
    void givenQualifier_thenContructorBean_thenDefaultConfigIsInjected() {
        assertEquals(SUBTYPE_CFG_ID, qualifierConstructorBean.cfgId);
    }

    /**
     * Tests that when no qualifier is present, the default configuration is
     * injected to the configurable bean.
     */
    @Test
    void givenNoQualifier_thenConfigurableBean_thenDefaultConfigIsInjected() {
        assertEquals(DEFAULT_CFG_ID, configurableBean.cfgId);
    }

    /**
     * Tests that when qualifier is present, the configuration sub-type is
     * injected to the configurable bean.
     */
    @Test
    void givenQualifier_thenConfigurableBean_thenDefaultConfigIsInjected() {
        assertEquals(SUBTYPE_CFG_ID, qualifierConfigurableBean.cfgId);
    }

    interface ConfigSubtype extends Config {

        default String getConfigId() {
            return get(CFG_ID_PROP);
        }
    }

    @Configuration
    @PropertySource("classpath:dev/orne/config/spring/config.autowire.test.properties")
    @ConfigPropertySource(CONFIG_SOURCE_BEAN)
    @EnableOrneConfig
    static class SpringConfig {

        @Bean(CONFIG_SOURCE_BEAN)
        public Config configSource() {
            return Config.fromProperties()
                    .add(Map.of(
                            SOURCE_PROP, SOURCE_PROP_VALUE))
                    .build();
        }

        @Bean(CONFIG_SUBTYPE)
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

        @Bean(NO_QUALIFIER_CONFIGURABLE_BEAN)
        public ConfigurableBean configurableBean() {
            return new ConfigurableBean();
        }

        @Bean(QUALIFIER_CONFIGURABLE_BEAN)
        public PreferredConfigurableBean preferredConfigurableBean() {
            return new PreferredConfigurableBean();
        }
    }

    static class ConfigConstructorBean {

        private String cfgId;

        public ConfigConstructorBean(
                final Config config) {
            this.cfgId = config.as(ConfigSubtype.class).getConfigId();
        }
    }

    static class ConfigurableBean
    implements Configurable {

        private String cfgId;

        public ConfigurableBean() {
            super();
        }

        @Override
        public void configure(
                final @NotNull Config config) {
            this.cfgId = config.as(ConfigSubtype.class).getConfigId();
        }
    }

    @PreferredConfig(ConfigSubtype.class)
    static class PreferredConfigurableBean
    extends ConfigurableBean{

        public PreferredConfigurableBean() {
            super();
        }
    }
}
