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

import java.util.HashMap;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.orne.config.Config;
import dev.orne.config.Configurable;
import dev.orne.config.ConfigurableProperty;
import dev.orne.config.ConfigurationOptions;

/**
 * Validation tests for {@code EnableConfigurableComponents} and
 * {@code ConfigPropertySource} usage.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0
 * @see EnableConfigurableComponents
 * @see ConfigPropertySource
 */
@Tag("ut")
@ExtendWith(SpringExtension.class)
@ContextHierarchy({
    @ContextConfiguration(classes = SpringConfigTest.SpringConfig.class),
    @ContextConfiguration(classes = SpringConfigTest.ChildSpringConfig.class)
})
class SpringConfigTest {

    static final String PARENT_BEAN = "parentContextBean";
    static final String PARENT_NO_PROPS_BEAN = "parentContextNoPropsBean";
    static final String PARENT_NO_NESTED_BEAN = "parentContextNoNestedBean";
    static final String PARENT_PARENT_CFG_BEAN = "parentContextParentCfgBean";
    static final String PARENT_CHILD_CFG_BEAN = "parentContextChildCfgBean";
    static final String PARENT_CHILD_STRICT_CFG_BEAN = "parentContextStrictChildCfgBean";
    static final String CHILD_BEAN = "childContextBean";
    static final String CHILD_NO_PROPS_BEAN = "childContextNoPropsBean";
    static final String CHILD_NO_NESTED_BEAN = "childContextNoNestedBean";
    static final String CHILD_PARENT_CFG_BEAN = "childContextParentCfgBean";
    static final String CHILD_CHILD_CFG_BEAN = "childContextChildCfgBean";
    static final String CHILD_CHILD_STRICT_CFG_BEAN = "childContextStrictChildCfgBean";
    static final String CLASSIC_PROP_KEY = "spring.classic.prop";
    static final String CLASSIC_PROP_PARENT_VALUE = "parentClassicSpringValue";

    @Autowired
    private Environment environment;
    @Autowired
    private GrandparentConfig grandpaConfig;
    @Autowired
    private ParentConfig parentConfig;
    @Autowired
    private ChildConfig childConfig;
    @Autowired
    private SibblingConfig sibblingConfig;
    @Autowired
    @Qualifier(PARENT_BEAN)
    private ConfigurableBean parentContextBean;
    @Autowired
    @Qualifier(PARENT_NO_PROPS_BEAN)
    private ConfigurableBean parentContextNoPropsBean;
    @Autowired
    @Qualifier(PARENT_NO_NESTED_BEAN)
    private ConfigurableBean parentContextNoNestedBean;
    @Autowired
    @Qualifier(PARENT_PARENT_CFG_BEAN)
    private ConfigurableBean parentContextParentCfgBean;
    @Autowired
    @Qualifier(PARENT_CHILD_CFG_BEAN)
    private ConfigurableBean parentContextChildCfgBean;
    @Autowired
    @Qualifier(PARENT_CHILD_STRICT_CFG_BEAN)
    private ConfigurableBean parentContextStrictChildCfgBean;
    @Autowired
    @Qualifier(CHILD_BEAN)
    private ConfigurableBean childContextBean;
    @Autowired
    @Qualifier(CHILD_NO_PROPS_BEAN)
    private ConfigurableBean childContextNoPropsBean;
    @Autowired
    @Qualifier(CHILD_NO_NESTED_BEAN)
    private ConfigurableBean childContextNoNestedBean;
    @Autowired
    @Qualifier(CHILD_PARENT_CFG_BEAN)
    private ConfigurableBean childContextParentCfgBean;
    @Autowired
    @Qualifier(CHILD_CHILD_CFG_BEAN)
    private ConfigurableBean childContextChildCfgBean;
    @Autowired
    @Qualifier(CHILD_CHILD_STRICT_CFG_BEAN)
    private ConfigurableBean childContextStrictChildCfgBean;


    @Test
    void testEnvironment() {
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                environment.getProperty(GrandparentConfig.GRANDPA_CFG_PROP));
        assertEquals(
                ChildSpringConfig.PARENT_PROP_VALUE,
                environment.getProperty(ParentConfig.PARENT_CFG_PROP));
        assertEquals(
                ChildSpringConfig.CHILD_PROP_VALUE,
                environment.getProperty(ChildConfig.CHILD_CFG_PROP));
        assertEquals(
                ChildSpringConfig.SIBBLING_PROP_VALUE,
                environment.getProperty(SibblingConfig.SIBBLING_CFG_PROP));
    }

    @Test
    void testGrandpaConfig() {
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                grandpaConfig.get(GrandparentConfig.GRANDPA_CFG_PROP));
    }

    @Test
    void testParentConfig() {
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                parentConfig.get(GrandparentConfig.GRANDPA_CFG_PROP));
        assertEquals(
                SpringConfig.PARENT_PROP_VALUE,
                parentConfig.getParentProp());
    }

    @Test
    void testChildConfig() {
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                childConfig.get(GrandparentConfig.GRANDPA_CFG_PROP));
        assertEquals(
                ChildSpringConfig.PARENT_PROP_VALUE,
                childConfig.get(ParentConfig.PARENT_CFG_PROP));
        assertEquals(
                ChildSpringConfig.CHILD_PROP_VALUE,
                childConfig.getChildProp());
        assertNull(
                childConfig.get(SibblingConfig.SIBBLING_CFG_PROP));
    }

    @Test
    void testSibblingConfig() {
        assertNull(
                sibblingConfig.get(GrandparentConfig.GRANDPA_CFG_PROP));
        assertNull(
                sibblingConfig.get(ParentConfig.PARENT_CFG_PROP));
        assertNull(
                sibblingConfig.get(ChildConfig.CHILD_CFG_PROP));
        assertEquals(
                ChildSpringConfig.SIBBLING_PROP_VALUE,
                sibblingConfig.getSibblingProp());
    }

    @Test
    void testParentContextBean() {
        assertParentConfigBean(parentContextBean);
    }

    @Test
    void testParentContextNoPropsBean() {
        assertEquals(
                CLASSIC_PROP_PARENT_VALUE,
                parentContextNoPropsBean.getClassicPropValue());
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                parentContextNoPropsBean.getGrandpaPropValue());
        assertEquals(
                SpringConfig.PARENT_PROP_VALUE,
                parentContextNoPropsBean.getParentPropValue());
        assertNull(parentContextNoPropsBean.getChildPropValue());
        assertNull(parentContextNoNestedBean.getSibblingPropValue());
        assertTrue(parentContextNoPropsBean.isConfigured());
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                parentContextNoPropsBean.getGrandpaCfgProp());
        assertNull(parentContextNoPropsBean.getParentCfgProp());
        assertNull(parentContextNoPropsBean.getChildCfgProp());
        assertNull(parentContextNoPropsBean.getSibblingCfgProp());
        assertTrue(parentContextNoPropsBean.getNested().isConfigured());
        assertEquals(
                SpringConfig.PARENT_PROP_VALUE,
                parentContextNoPropsBean.getNested().getParentCfgProp());
    }

    @Test
    void testParentContextNoNestedBean() {
        assertEquals(
                CLASSIC_PROP_PARENT_VALUE,
                parentContextNoNestedBean.getClassicPropValue());
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                parentContextNoNestedBean.getGrandpaPropValue());
        assertEquals(
                SpringConfig.PARENT_PROP_VALUE,
                parentContextNoNestedBean.getParentPropValue());
        assertNull(parentContextNoNestedBean.getChildPropValue());
        assertNull(parentContextNoNestedBean.getSibblingPropValue());
        assertTrue(parentContextNoNestedBean.isConfigured());
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                parentContextNoNestedBean.getGrandpaCfgProp());
        assertEquals(
                SpringConfig.PARENT_PROP_VALUE,
                parentContextNoNestedBean.getParentCfgProp());
        assertNull(parentContextNoNestedBean.getChildCfgProp());
        assertNull(parentContextNoNestedBean.getSibblingCfgProp());
        assertFalse(parentContextNoNestedBean.getNested().isConfigured());
        assertNull(parentContextNoNestedBean.getNested().getParentCfgProp());
    }

    @Test
    void testParentContextParentCfgBean() {
        assertParentConfigBean(parentContextParentCfgBean);
    }

    @Test
    void testParentContextChildCfgBean() {
        assertParentConfigBean(parentContextChildCfgBean);
    }

    @Test
    void testParentContextStrictChildCfgBean() {
        assertEquals(
                CLASSIC_PROP_PARENT_VALUE,
                parentContextStrictChildCfgBean.getClassicPropValue());
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                parentContextStrictChildCfgBean.getGrandpaPropValue());
        assertEquals(
                SpringConfig.PARENT_PROP_VALUE,
                parentContextStrictChildCfgBean.getParentPropValue());
        assertNull(parentContextStrictChildCfgBean.getChildPropValue());
        assertNull(parentContextStrictChildCfgBean.getSibblingPropValue());
        assertFalse(parentContextStrictChildCfgBean.isConfigured());
        assertNull(parentContextStrictChildCfgBean.getGrandpaCfgProp());
        assertNull(parentContextStrictChildCfgBean.getParentCfgProp());
        assertNull(parentContextStrictChildCfgBean.getChildCfgProp());
        assertNull(parentContextStrictChildCfgBean.getSibblingCfgProp());
        assertFalse(parentContextStrictChildCfgBean.getNested().isConfigured());
        assertNull(parentContextStrictChildCfgBean.getNested().getParentCfgProp());
    }

    @Test
    void testChildContextBean() {
        assertChildConfigBean(childContextBean);
    }

    @Test
    void testChildContextNoPropsBean() {
        assertEquals(
                CLASSIC_PROP_PARENT_VALUE,
                childContextNoPropsBean.getClassicPropValue());
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                childContextNoPropsBean.getGrandpaPropValue());
        assertEquals(
                ChildSpringConfig.PARENT_PROP_VALUE,
                childContextNoPropsBean.getParentPropValue());
        assertEquals(
                ChildSpringConfig.CHILD_PROP_VALUE,
                childContextNoPropsBean.getChildPropValue());
        assertEquals(
                ChildSpringConfig.SIBBLING_PROP_VALUE,
                childContextNoNestedBean.getSibblingPropValue());
        assertTrue(childContextNoPropsBean.isConfigured());
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                childContextNoPropsBean.getGrandpaCfgProp());
        assertNull(childContextNoPropsBean.getParentCfgProp());
        assertNull(childContextNoPropsBean.getChildCfgProp());
        assertNull(childContextNoPropsBean.getSibblingCfgProp());
        assertTrue(childContextNoPropsBean.getNested().isConfigured());
        assertEquals(
                ChildSpringConfig.PARENT_PROP_VALUE,
                childContextNoPropsBean.getNested().getParentCfgProp());
    }

    @Test
    void testChildContextNoNestedBean() {
        assertEquals(
                CLASSIC_PROP_PARENT_VALUE,
                childContextNoNestedBean.getClassicPropValue());
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                childContextNoNestedBean.getGrandpaPropValue());
        assertEquals(
                ChildSpringConfig.PARENT_PROP_VALUE,
                childContextNoNestedBean.getParentPropValue());
        assertEquals(
                ChildSpringConfig.CHILD_PROP_VALUE,
                childContextNoNestedBean.getChildPropValue());
        assertEquals(
                ChildSpringConfig.SIBBLING_PROP_VALUE,
                childContextNoNestedBean.getSibblingPropValue());
        assertTrue(childContextNoNestedBean.isConfigured());
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                childContextNoNestedBean.getGrandpaCfgProp());
        assertEquals(
                ChildSpringConfig.PARENT_PROP_VALUE,
                childContextNoNestedBean.getParentCfgProp());
        assertEquals(
                ChildSpringConfig.CHILD_PROP_VALUE,
                childContextNoNestedBean.getChildCfgProp());
        assertNull(childContextNoNestedBean.getSibblingCfgProp());
        assertFalse(childContextNoNestedBean.getNested().isConfigured());
        assertNull(childContextNoNestedBean.getNested().getParentCfgProp());
    }

    @Test
    void testChildContextParentCfgBean() {
        assertEquals(
                CLASSIC_PROP_PARENT_VALUE,
                childContextParentCfgBean.getClassicPropValue());
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                childContextParentCfgBean.getGrandpaPropValue());
        assertEquals(
                ChildSpringConfig.PARENT_PROP_VALUE,
                childContextNoNestedBean.getParentPropValue());
        assertEquals(
                ChildSpringConfig.CHILD_PROP_VALUE,
                childContextNoNestedBean.getChildPropValue());
        assertEquals(
                ChildSpringConfig.SIBBLING_PROP_VALUE,
                childContextNoNestedBean.getSibblingPropValue());
        assertTrue(childContextParentCfgBean.isConfigured());
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                childContextParentCfgBean.getGrandpaCfgProp());
        assertEquals(
                SpringConfig.PARENT_PROP_VALUE,
                childContextParentCfgBean.getParentCfgProp());
        assertNull(childContextParentCfgBean.getChildCfgProp());
        assertNull(childContextParentCfgBean.getSibblingCfgProp());
        assertTrue(childContextParentCfgBean.getNested().isConfigured());
        assertEquals(
                ChildSpringConfig.PARENT_PROP_VALUE,
                childContextParentCfgBean.getNested().getParentCfgProp());
    }

    @Test
    void testChildContextChildCfgBean() {
        assertChildConfigBean(childContextChildCfgBean);
    }

    @Test
    void testChildContextStrictChildCfgBean() {
        assertChildConfigBean(childContextStrictChildCfgBean);
    }

    private void assertParentConfigBean(
            final ConfigurableBean bean) {
        assertEquals(
                CLASSIC_PROP_PARENT_VALUE,
                bean.getClassicPropValue());
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                bean.getGrandpaPropValue());
        assertEquals(
                SpringConfig.PARENT_PROP_VALUE,
                bean.getParentPropValue());
        assertNull(bean.getChildPropValue());
        assertNull(bean.getSibblingPropValue());
        assertTrue(bean.isConfigured());
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                bean.getGrandpaCfgProp());
        assertEquals(
                SpringConfig.PARENT_PROP_VALUE,
                bean.getParentCfgProp());
        assertNull(bean.getChildCfgProp());
        assertNull(bean.getSibblingCfgProp());
        assertTrue(bean.getNested().isConfigured());
        assertEquals(
                SpringConfig.PARENT_PROP_VALUE,
                bean.getNested().getParentCfgProp());
    }

    private void assertChildConfigBean(
            final ConfigurableBean bean) {
        assertEquals(
                CLASSIC_PROP_PARENT_VALUE,
                bean.getClassicPropValue());
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                bean.getGrandpaPropValue());
        assertEquals(
                ChildSpringConfig.PARENT_PROP_VALUE,
                bean.getParentPropValue());
        assertEquals(
                ChildSpringConfig.CHILD_PROP_VALUE,
                bean.getChildPropValue());
        assertEquals(
                ChildSpringConfig.SIBBLING_PROP_VALUE,
                bean.getSibblingPropValue());
        assertTrue(bean.isConfigured());
        assertEquals(
                SpringConfig.GRANDPA_PROP_VALUE,
                bean.getGrandpaCfgProp());
        assertEquals(
                ChildSpringConfig.PARENT_PROP_VALUE,
                bean.getParentCfgProp());
        assertEquals(
                ChildSpringConfig.CHILD_PROP_VALUE,
                bean.getChildCfgProp());
        assertNull(bean.getSibblingCfgProp());
        assertTrue(bean.getNested().isConfigured());
        assertEquals(
                ChildSpringConfig.PARENT_PROP_VALUE,
                bean.getNested().getParentCfgProp());
    }

    public static interface GrandparentConfig
    extends Config {

        static final String GRANDPA_CFG_PROP = "grandparent.prop";

        default String getGrandpaProp() {
            return get(GRANDPA_CFG_PROP);
        }
    }

    public static interface ParentConfig
    extends Config {

        static final String PARENT_CFG_PROP = "parent.prop";

        default String getParentProp() {
            return get(PARENT_CFG_PROP);
        }
    }

    public static interface ChildConfig
    extends Config {

        static final String CHILD_CFG_PROP = "child.prop";

        default String getChildProp() {
            return get(CHILD_CFG_PROP);
        }
    }

    public static interface SibblingConfig
    extends Config {

        static final String SIBBLING_CFG_PROP = "sibbling.prop";

        default String getSibblingProp() {
            return get(SIBBLING_CFG_PROP);
        }
    }

    public static class ConfigurableBean
    implements Configurable {

        private final String classicPropValue;
        private final String grandpaPropValue;
        private final String parentPropValue;
        private final String childPropValue;
        private final String sibblingPropValue;
        private boolean configured = false;
        private String grandpaCfgProp;
        @ConfigurableProperty(ParentConfig.PARENT_CFG_PROP)
        private String parentCfgProp;
        @ConfigurableProperty(ChildConfig.CHILD_CFG_PROP)
        private String childCfgProp;
        @ConfigurableProperty(SibblingConfig.SIBBLING_CFG_PROP)
        private String sibblingCfgProp;
        private final NestedConfigurableBean nested = new NestedConfigurableBean();

        public ConfigurableBean(
                String classicPropValue,
                String grandpaPropValue,
                String parentPropValue,
                String childPropValue,
                String sibblingPropValue) {
            super();
            this.classicPropValue = classicPropValue;
            this.grandpaPropValue = grandpaPropValue;
            this.parentPropValue = parentPropValue;
            this.childPropValue = childPropValue;
            this.sibblingPropValue = sibblingPropValue;
        }

        @Override
        public void configure(@NotNull Config config) {
            this.grandpaCfgProp = config.get(GrandparentConfig.GRANDPA_CFG_PROP);
            configured = true;
        }

        @Override
        public boolean isConfigured() {
            return configured;
        }

        public String getClassicPropValue() {
            return classicPropValue;
        }

        public String getGrandpaPropValue() {
            return grandpaPropValue;
        }

        public String getParentPropValue() {
            return parentPropValue;
        }

        public String getChildPropValue() {
            return childPropValue;
        }

        public String getSibblingPropValue() {
            return sibblingPropValue;
        }

        public String getGrandpaCfgProp() {
            return grandpaCfgProp;
        }

        public void setGrandpaCfgProp(String value) {
            this.grandpaCfgProp = value;
        }

        public String getParentCfgProp() {
            return parentCfgProp;
        }

        public void setParentCfgProp(String value) {
            this.parentCfgProp = value;
        }

        public String getChildCfgProp() {
            return childCfgProp;
        }

        public void setChildCfgProp(String value) {
            this.childCfgProp = value;
        }

        public String getSibblingCfgProp() {
            return sibblingCfgProp;
        }

        public void setSibblingCfgProp(String value) {
            this.sibblingCfgProp = value;
        }

        public NestedConfigurableBean getNested() {
            return nested;
        }
    }

    public static class NestedConfigurableBean
    implements Configurable {

        private boolean configured = false;
        @ConfigurableProperty(ParentConfig.PARENT_CFG_PROP)
        private String parentCfgProp;

        @Override
        public void configure(@NotNull Config config) {
            configured = true;
        }

        @Override
        public boolean isConfigured() {
            return configured;
        }

        public String getParentCfgProp() {
            return parentCfgProp;
        }

        public void setParentCfgProp(String value) {
            this.parentCfgProp = value;
        }
    }

    @ConfigurationOptions(configureProperties = false)
    public static class NoPropertiesConfigurableBean
    extends ConfigurableBean {
        public NoPropertiesConfigurableBean(
                String classicPropValue,
                String grandpaPropValue,
                String parentPropValue,
                String childPropValue,
                String sibblingPropValue) {
            super(classicPropValue, grandpaPropValue, parentPropValue, childPropValue, sibblingPropValue);
        }
    }

    @ConfigurationOptions(configureNestedBeans = false)
    public static class NoNestedConfigurableBean
    extends ConfigurableBean {
        public NoNestedConfigurableBean(
                String classicPropValue,
                String grandpaPropValue,
                String parentPropValue,
                String childPropValue,
                String sibblingPropValue) {
            super(classicPropValue, grandpaPropValue, parentPropValue, childPropValue, sibblingPropValue);
        }
    }

    @ConfigurationOptions(preferredConfigs = ParentConfig.class)
    public static class ParentConfigConfigurableBean
    extends ConfigurableBean {
        public ParentConfigConfigurableBean(
                String classicPropValue,
                String grandpaPropValue,
                String parentPropValue,
                String childPropValue,
                String sibblingPropValue) {
            super(classicPropValue, grandpaPropValue, parentPropValue, childPropValue, sibblingPropValue);
        }
    }

    @ConfigurationOptions(preferredConfigs = ChildConfig.class)
    public static class ChildConfigConfigurableBean
    extends ConfigurableBean {
        public ChildConfigConfigurableBean(
                String classicPropValue,
                String grandpaPropValue,
                String parentPropValue,
                String childPropValue,
                String sibblingPropValue) {
            super(classicPropValue, grandpaPropValue, parentPropValue, childPropValue, sibblingPropValue);
        }
    }

    @ConfigurationOptions(
            preferredConfigs = ChildConfig.class,
            fallbackToDefaultConfig = false)
    public static class ChildStrictConfigurableBean
    extends ConfigurableBean {
        public ChildStrictConfigurableBean(
                String classicPropValue,
                String grandpaPropValue,
                String parentPropValue,
                String childPropValue,
                String sibblingPropValue) {
            super(classicPropValue, grandpaPropValue, parentPropValue, childPropValue, sibblingPropValue);
        }
    }

    @Configuration
    @PropertySource("classpath:dev/orne/config/spring/spring.config.test.properties")
    @ConfigPropertySource(type = ParentConfig.class)
    @EnableConfigurableComponents(type = ParentConfig.class)
    static class SpringConfig
    implements ConfigPropertySourcesConfigurer {

        static final String GRANDPA_PROP_VALUE = "grandpaCodeValue";
        static final String PARENT_PROP_VALUE = "parentPropValue";

        @Bean
        public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }

        @Bean
        public GrandparentConfig grandparentConfig() {
            final HashMap<String, String> props = new HashMap<>();
            props.put(GrandparentConfig.GRANDPA_CFG_PROP, GRANDPA_PROP_VALUE);
            return Config.as(
                    Config.fromPropertiesFiles()
                        .add(props)
                        .build(),
                    GrandparentConfig.class);
        }

        @Bean
        public ParentConfig parentConfig(
                final GrandparentConfig parent) {
            final HashMap<String, String> props = new HashMap<>();
            props.put(ParentConfig.PARENT_CFG_PROP, PARENT_PROP_VALUE);
            return Config.as(
                    Config.fromPropertiesFiles()
                        .withParent(parent)
                        .add(props)
                        .build(),
                    ParentConfig.class);
        }

        @Bean(name = PARENT_BEAN)
        public ConfigurableBean parentContextBean(
                @Value("${" + CLASSIC_PROP_KEY + ":#{null}}")
                final String classicPropValue,
                @Value("${" + GrandparentConfig.GRANDPA_CFG_PROP + ":#{null}}")
                final String grandpaPropValue,
                @Value("${" + ParentConfig.PARENT_CFG_PROP + ":#{null}}")
                final String parentPropValue,
                @Value("${" + ChildConfig.CHILD_CFG_PROP + ":#{null}}")
                final String childPropValue,
                @Value("${" + SibblingConfig.SIBBLING_CFG_PROP + ":#{null}}")
                final String sibblingPropValue) {
            return new ConfigurableBean(
                    classicPropValue,
                    grandpaPropValue,
                    parentPropValue,
                    childPropValue,
                    sibblingPropValue);
        }

        @Bean(name = PARENT_NO_PROPS_BEAN)
        public NoPropertiesConfigurableBean parentContextNoPropsBean(
                @Value("${" + CLASSIC_PROP_KEY + ":#{null}}")
                final String classicPropValue,
                @Value("${" + GrandparentConfig.GRANDPA_CFG_PROP + ":#{null}}")
                final String grandpaPropValue,
                @Value("${" + ParentConfig.PARENT_CFG_PROP + ":#{null}}")
                final String parentPropValue,
                @Value("${" + ChildConfig.CHILD_CFG_PROP + ":#{null}}")
                final String childPropValue,
                @Value("${" + SibblingConfig.SIBBLING_CFG_PROP + ":#{null}}")
                final String sibblingPropValue) {
            return new NoPropertiesConfigurableBean(
                    classicPropValue,
                    grandpaPropValue,
                    parentPropValue,
                    childPropValue,
                    sibblingPropValue);
        }

        @Bean(name = PARENT_NO_NESTED_BEAN)
        public NoNestedConfigurableBean parentContextNoNestedBean(
                @Value("${" + CLASSIC_PROP_KEY + ":#{null}}")
                final String classicPropValue,
                @Value("${" + GrandparentConfig.GRANDPA_CFG_PROP + ":#{null}}")
                final String grandpaPropValue,
                @Value("${" + ParentConfig.PARENT_CFG_PROP + ":#{null}}")
                final String parentPropValue,
                @Value("${" + ChildConfig.CHILD_CFG_PROP + ":#{null}}")
                final String childPropValue,
                @Value("${" + SibblingConfig.SIBBLING_CFG_PROP + ":#{null}}")
                final String sibblingPropValue) {
            return new NoNestedConfigurableBean(
                    classicPropValue,
                    grandpaPropValue,
                    parentPropValue,
                    childPropValue,
                    sibblingPropValue);
        }

        @Bean(name = PARENT_PARENT_CFG_BEAN)
        public ParentConfigConfigurableBean parentContextParentCfgBean(
                @Value("${" + CLASSIC_PROP_KEY + ":#{null}}")
                final String classicPropValue,
                @Value("${" + GrandparentConfig.GRANDPA_CFG_PROP + ":#{null}}")
                final String grandpaPropValue,
                @Value("${" + ParentConfig.PARENT_CFG_PROP + ":#{null}}")
                final String parentPropValue,
                @Value("${" + ChildConfig.CHILD_CFG_PROP + ":#{null}}")
                final String childPropValue,
                @Value("${" + SibblingConfig.SIBBLING_CFG_PROP + ":#{null}}")
                final String sibblingPropValue) {
            return new ParentConfigConfigurableBean(
                    classicPropValue,
                    grandpaPropValue,
                    parentPropValue,
                    childPropValue,
                    sibblingPropValue);
        }

        @Bean(name = PARENT_CHILD_CFG_BEAN)
        public ChildConfigConfigurableBean parentContextChildCfgBean(
                @Value("${" + CLASSIC_PROP_KEY + ":#{null}}")
                final String classicPropValue,
                @Value("${" + GrandparentConfig.GRANDPA_CFG_PROP + ":#{null}}")
                final String grandpaPropValue,
                @Value("${" + ParentConfig.PARENT_CFG_PROP + ":#{null}}")
                final String parentPropValue,
                @Value("${" + ChildConfig.CHILD_CFG_PROP + ":#{null}}")
                final String childPropValue,
                @Value("${" + SibblingConfig.SIBBLING_CFG_PROP + ":#{null}}")
                final String sibblingPropValue) {
            return new ChildConfigConfigurableBean(
                    classicPropValue,
                    grandpaPropValue,
                    parentPropValue,
                    childPropValue,
                    sibblingPropValue);
        }

        @Bean(name = PARENT_CHILD_STRICT_CFG_BEAN)
        public ChildStrictConfigurableBean parentContextStrictChildCfgBean(
                @Value("${" + CLASSIC_PROP_KEY + ":#{null}}")
                final String classicPropValue,
                @Value("${" + GrandparentConfig.GRANDPA_CFG_PROP + ":#{null}}")
                final String grandpaPropValue,
                @Value("${" + ParentConfig.PARENT_CFG_PROP + ":#{null}}")
                final String parentPropValue,
                @Value("${" + ChildConfig.CHILD_CFG_PROP + ":#{null}}")
                final String childPropValue,
                @Value("${" + SibblingConfig.SIBBLING_CFG_PROP + ":#{null}}")
                final String sibblingPropValue) {
            return new ChildStrictConfigurableBean(
                    classicPropValue,
                    grandpaPropValue,
                    parentPropValue,
                    childPropValue,
                    sibblingPropValue);
        }
    }


    @Configuration
    @PropertySource("classpath:dev/orne/config/spring/spring.config.test.child.properties")
    @ConfigPropertySource(type = ChildConfig.class)
    @ConfigPropertySource(type = SibblingConfig.class)
    @EnableConfigurableComponents(type = ChildConfig.class)
    static class ChildSpringConfig
    implements ConfigPropertySourcesConfigurer {

        static final String PARENT_PROP_VALUE = "parentPropChildValue";
        static final String CHILD_PROP_VALUE = "childPropValue";
        static final String SIBBLING_PROP_VALUE = "sibblingPropValue";

        @Bean
        public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }

        @Bean
        public ChildConfig childConfig(
                final ParentConfig parent) {
            final HashMap<String, String> props = new HashMap<>();
            props.put(ParentConfig.PARENT_CFG_PROP, PARENT_PROP_VALUE);
            props.put(ChildConfig.CHILD_CFG_PROP, CHILD_PROP_VALUE);
            return Config.as(
                    Config.fromPropertiesFiles()
                        .withParent(parent)
                        .withOverrideParentProperties()
                        .add(props)
                        .build(),
                    ChildConfig.class);
        }

        @Bean
        public SibblingConfig sibblingConfig(
                final ParentConfig parent) {
            final HashMap<String, String> props = new HashMap<>();
            props.put(SibblingConfig.SIBBLING_CFG_PROP, SIBBLING_PROP_VALUE);
            return Config.as(
                    Config.fromPropertiesFiles()
                        .add(props)
                        .build(),
                        SibblingConfig.class);
        }

        @Bean(name = CHILD_BEAN)
        public ConfigurableBean childContextBean(
                @Value("${" + CLASSIC_PROP_KEY + ":#{null}}")
                final String classicPropValue,
                @Value("${" + GrandparentConfig.GRANDPA_CFG_PROP + ":#{null}}")
                final String grandpaPropValue,
                @Value("${" + ParentConfig.PARENT_CFG_PROP + ":#{null}}")
                final String parentPropValue,
                @Value("${" + ChildConfig.CHILD_CFG_PROP + ":#{null}}")
                final String childPropValue,
                @Value("${" + SibblingConfig.SIBBLING_CFG_PROP + ":#{null}}")
                final String sibblingPropValue) {
            return new ConfigurableBean(
                    classicPropValue,
                    grandpaPropValue,
                    parentPropValue,
                    childPropValue,
                    sibblingPropValue);
        }

        @Bean(name = CHILD_NO_PROPS_BEAN)
        public NoPropertiesConfigurableBean childContextNoPropsBean(
                @Value("${" + CLASSIC_PROP_KEY + ":#{null}}")
                final String classicPropValue,
                @Value("${" + GrandparentConfig.GRANDPA_CFG_PROP + ":#{null}}")
                final String grandpaPropValue,
                @Value("${" + ParentConfig.PARENT_CFG_PROP + ":#{null}}")
                final String parentPropValue,
                @Value("${" + ChildConfig.CHILD_CFG_PROP + ":#{null}}")
                final String childPropValue,
                @Value("${" + SibblingConfig.SIBBLING_CFG_PROP + ":#{null}}")
                final String sibblingPropValue) {
            return new NoPropertiesConfigurableBean(
                    classicPropValue,
                    grandpaPropValue,
                    parentPropValue,
                    childPropValue,
                    sibblingPropValue);
        }

        @Bean(name = CHILD_NO_NESTED_BEAN)
        public NoNestedConfigurableBean childContextNoNestedBean(
                @Value("${" + CLASSIC_PROP_KEY + ":#{null}}")
                final String classicPropValue,
                @Value("${" + GrandparentConfig.GRANDPA_CFG_PROP + ":#{null}}")
                final String grandpaPropValue,
                @Value("${" + ParentConfig.PARENT_CFG_PROP + ":#{null}}")
                final String parentPropValue,
                @Value("${" + ChildConfig.CHILD_CFG_PROP + ":#{null}}")
                final String childPropValue,
                @Value("${" + SibblingConfig.SIBBLING_CFG_PROP + ":#{null}}")
                final String sibblingPropValue) {
            return new NoNestedConfigurableBean(
                    classicPropValue,
                    grandpaPropValue,
                    parentPropValue,
                    childPropValue,
                    sibblingPropValue);
        }

        @Bean(name = CHILD_PARENT_CFG_BEAN)
        public ParentConfigConfigurableBean childContextParentCfgBean(
                @Value("${" + CLASSIC_PROP_KEY + ":#{null}}")
                final String classicPropValue,
                @Value("${" + GrandparentConfig.GRANDPA_CFG_PROP + ":#{null}}")
                final String grandpaPropValue,
                @Value("${" + ParentConfig.PARENT_CFG_PROP + ":#{null}}")
                final String parentPropValue,
                @Value("${" + ChildConfig.CHILD_CFG_PROP + ":#{null}}")
                final String childPropValue,
                @Value("${" + SibblingConfig.SIBBLING_CFG_PROP + ":#{null}}")
                final String sibblingPropValue) {
            return new ParentConfigConfigurableBean(
                    classicPropValue,
                    grandpaPropValue,
                    parentPropValue,
                    childPropValue,
                    sibblingPropValue);
        }

        @Bean(name = CHILD_CHILD_CFG_BEAN)
        public ChildConfigConfigurableBean childContextChildCfgBean(
                @Value("${" + CLASSIC_PROP_KEY + ":#{null}}")
                final String classicPropValue,
                @Value("${" + GrandparentConfig.GRANDPA_CFG_PROP + ":#{null}}")
                final String grandpaPropValue,
                @Value("${" + ParentConfig.PARENT_CFG_PROP + ":#{null}}")
                final String parentPropValue,
                @Value("${" + ChildConfig.CHILD_CFG_PROP + ":#{null}}")
                final String childPropValue,
                @Value("${" + SibblingConfig.SIBBLING_CFG_PROP + ":#{null}}")
                final String sibblingPropValue) {
            return new ChildConfigConfigurableBean(
                    classicPropValue,
                    grandpaPropValue,
                    parentPropValue,
                    childPropValue,
                    sibblingPropValue);
        }

        @Bean(name = CHILD_CHILD_STRICT_CFG_BEAN)
        public ChildStrictConfigurableBean childContextStrictChildCfgBean(
                @Value("${" + CLASSIC_PROP_KEY + ":#{null}}")
                final String classicPropValue,
                @Value("${" + GrandparentConfig.GRANDPA_CFG_PROP + ":#{null}}")
                final String grandpaPropValue,
                @Value("${" + ParentConfig.PARENT_CFG_PROP + ":#{null}}")
                final String parentPropValue,
                @Value("${" + ChildConfig.CHILD_CFG_PROP + ":#{null}}")
                final String childPropValue,
                @Value("${" + SibblingConfig.SIBBLING_CFG_PROP + ":#{null}}")
                final String sibblingPropValue) {
            return new ChildStrictConfigurableBean(
                    classicPropValue,
                    grandpaPropValue,
                    parentPropValue,
                    childPropValue,
                    sibblingPropValue);
        }
    }
}
