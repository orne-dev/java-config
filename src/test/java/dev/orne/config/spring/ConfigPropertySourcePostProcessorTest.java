package dev.orne.config.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import dev.orne.config.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Unit tests for {@link ConfigPropertySourcePostProcessor}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class ConfigPropertySourcePostProcessorTest {

    private static final String SPRING_CFG_BEAN = "springConfiguration";

    private @Mock ConfigurableEnvironment environment;
    private @Mock ConfigurableListableBeanFactory beanFactory;
    private MutablePropertySources propertySources;
    private ConfigPropertySourcePostProcessor postProcessor;

    /**
     * Initializes the mocks and instance used in the tests.
     */
    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        propertySources = new MutablePropertySources();
        given(environment.getPropertySources()).willReturn(propertySources);
        postProcessor = new ConfigPropertySourcePostProcessor();
        postProcessor.setEnvironment(environment);
    }

    /**
     * Tests that {@code setEnvironment(Environment)} requires a
     * {@link ConfigurableEnvironment} instance.
     */
    @Test
    void givenNonConfigurableEnvironment_whenSetEnvironment_thenThrowException() {
        ConfigPropertySourcePostProcessor processor = new ConfigPropertySourcePostProcessor();
        processor.setEnvironment(mock(Environment.class));
        assertNull(processor.environment);
    }

    /**
     * Tests that {@code setEnvironment(Environment)} sets the environment when a
     * {@link ConfigurableEnvironment} instance is provided.
     */
    @Test
    void givenConfigurableEnvironment_whenSetEnvironment_thenSetEnvironment() {
        ConfigPropertySourcePostProcessor processor = new ConfigPropertySourcePostProcessor();
        processor.setEnvironment(environment);
        assertSame(environment, processor.environment);
    }

    /**
     * Tests that {@code postProcessBeanFactory(BeanFactory)} throws an exception
     * when no environment has been set.
     */
    @Test
    void givenNoEnvironment_whenPostProcessBeanFactory_thenThrowException() {
        ConfigPropertySourcePostProcessor processor = new ConfigPropertySourcePostProcessor();
        assertThrows(BeanInitializationException.class, () -> processor.postProcessBeanFactory(beanFactory));
    }

    /**
     * Tests that {@code postProcessBeanFactory(BeanFactory)} calls
     * {@code processConfigurationBean(...)} for each configuration bean defined in
     * the bean factory.
     */
    @Test
    void givenConfigurationBeanDefinitions_whenPostProcessBeanFactory_thenCallProcessConfigurationBean() {
        final ConfigPropertySourcePostProcessor processor = spy(postProcessor);
        final AnnotationMetadata beanAnnot1 = mock(AnnotationMetadata.class);
        final AnnotatedGenericBeanDefinition beanDef1 = new AnnotatedGenericBeanDefinition(beanAnnot1);
        final BeanDefinition beanDef2 = mock(BeanDefinition.class);
        final AnnotationMetadata beanAnnot3 = mock(AnnotationMetadata.class);
        final AnnotatedGenericBeanDefinition beanDef3 = new AnnotatedGenericBeanDefinition(beanAnnot3);
        given(beanFactory.getBeanDefinitionNames()).willReturn(new String[]{ "bean1", "bean2", "bean3" });
        given(beanFactory.getBeanDefinition("bean1")).willReturn(beanDef1);
        given(beanAnnot1.hasAnnotation(Configuration.class.getName())).willReturn(true);
        given(beanFactory.getBeanDefinition("bean2")).willReturn(beanDef2);
        given(beanFactory.getBeanDefinition("bean3")).willReturn(beanDef3);
        given(beanAnnot3.hasAnnotation(Configuration.class.getName())).willReturn(false);
        willDoNothing().given(processor).processConfigurationBean(any(), any(), any());
        processor.setEnvironment(environment);
        processor.postProcessBeanFactory(beanFactory);
        then(processor).should(times(1)).processConfigurationBean(beanFactory, "bean1", beanAnnot1);
        then(processor).should(never()).processConfigurationBean(any(), eq("bean2"), any());
        then(processor).should(never()).processConfigurationBean(any(), eq("bean3"), any());
    }

    /**
     * Tests that {@code processConfigurationBean(...)} does nothing when the
     * configuration bean has no relevant annotations.
     */
    @Test
    void givenNoSourceAnnotations_whenProcessConfigurationBean_thenDoNothing() {
        final ConfigPropertySourcePostProcessor processor = spy(postProcessor);
        final AnnotationMetadata annot = mock(AnnotationMetadata.class);
        given(annot.getAnnotationAttributes(ConfigPropertySource.class.getName())).willReturn(null);
        given(annot.getAnnotationAttributes(ConfigPropertySources.class.getName())).willReturn(null);
        willDoNothing().given(processor).processSource(any(), any(), any());
        processor.processConfigurationBean(beanFactory, SPRING_CFG_BEAN, annot);
        then(processor).should(never()).processSource(any(), any(), any());
    }

    /**
     * Tests that {@code processConfigurationBean(...)} calls
     * {@code processSource(...)} when the configuration bean has a
     * {@link ConfigPropertySource} annotation.
     */
    @Test
    void givenSourceAnnotation_whenProcessConfigurationBean_thenProcessSource() {
        final ConfigPropertySourcePostProcessor processor = spy(postProcessor);
        final AnnotationMetadata annot = mock(AnnotationMetadata.class);
        willDoNothing().given(processor).processSource(any(), any(), any());
        final AnnotationAttributes attrs = mock(AnnotationAttributes.class);
        given(annot.getAnnotationAttributes(ConfigPropertySource.class.getName())).willReturn(attrs);
        given(annot.getAnnotationAttributes(ConfigPropertySources.class.getName())).willReturn(null);
        processor.processConfigurationBean(beanFactory, SPRING_CFG_BEAN, annot);
        then(processor).should().processSource(same(beanFactory), eq(SPRING_CFG_BEAN), same(attrs));
    }

    /**
     * Tests that {@code processConfigurationBean(...)} calls
     * {@code processSource(...)} for each nested annotation when the
     * configuration bean has a {@link ConfigPropertySources} annotation.
     */
    @Test
    void givenSourcesAnnotation_whenProcessConfigurationBean_thenProcessEachSource() {
        final ConfigPropertySourcePostProcessor processor = spy(postProcessor);
        final AnnotationMetadata annot = mock(AnnotationMetadata.class);
        final AnnotationAttributes aggrAttrs = mock(AnnotationAttributes.class);
        final AnnotationAttributes attrs1 = mock(AnnotationAttributes.class);
        final AnnotationAttributes attrs2 = mock(AnnotationAttributes.class);
        given(aggrAttrs.getAnnotationArray("value")).willReturn(new AnnotationAttributes[]{ attrs1, attrs2 });
        given(annot.getAnnotationAttributes(ConfigPropertySource.class.getName())).willReturn(null);
        given(annot.getAnnotationAttributes(ConfigPropertySources.class.getName())).willReturn(aggrAttrs);
        willDoNothing().given(processor).processSource(any(), any(), any());
        processor.processConfigurationBean(beanFactory, SPRING_CFG_BEAN, annot);
        then(processor).should().processSource(same(beanFactory), eq(SPRING_CFG_BEAN), same(attrs1));
        then(processor).should().processSource(same(beanFactory), eq(SPRING_CFG_BEAN), same(attrs2));
    }

    /**
     * Tests that {@code processConfigurationBean(...)} does nothing when the
     * configuration bean has a {@link ConfigPropertySources} annotation with no
     * nested annotations.
     */
    @Test
    void givenSourcesEmptyAnnotation_whenProcessConfigurationBean_thenDoNothing() {
        final ConfigPropertySourcePostProcessor processor = spy(postProcessor);
        final AnnotationMetadata annot = mock(AnnotationMetadata.class);
        final AnnotationAttributes aggrAttrs = mock(AnnotationAttributes.class);
        given(aggrAttrs.getAnnotationArray("value")).willReturn(new AnnotationAttributes[]{ });
        given(annot.getAnnotationAttributes(ConfigPropertySource.class.getName())).willReturn(null);
        given(annot.getAnnotationAttributes(ConfigPropertySources.class.getName())).willReturn(aggrAttrs);
        willDoNothing().given(processor).processSource(any(), any(), any());
        processor.processConfigurationBean(beanFactory, SPRING_CFG_BEAN, annot);
        then(processor).should(never()).processSource(any(), any(), any());
    }

    /**
     * Tests that {@code processConfigurationBean(...)} calls
     * {@code processSource(...)} for the single annotation and for each nested
     * annotation when the configuration bean has both
     * {@link ConfigPropertySource} and {@link ConfigPropertySources} annotations.
     */
    @Test
    void givenSourceAndSourcesAnnotation_whenProcessConfigurationBean_thenProcessEachSource() {
        final ConfigPropertySourcePostProcessor processor = spy(postProcessor);
        final AnnotationMetadata annot = mock(AnnotationMetadata.class);
        final AnnotationAttributes attrs = mock(AnnotationAttributes.class);
        final AnnotationAttributes aggrAttrs = mock(AnnotationAttributes.class);
        final AnnotationAttributes attrs1 = mock(AnnotationAttributes.class);
        final AnnotationAttributes attrs2 = mock(AnnotationAttributes.class);
        given(aggrAttrs.getAnnotationArray("value")).willReturn(new AnnotationAttributes[]{ attrs1, attrs2 });
        given(annot.getAnnotationAttributes(ConfigPropertySource.class.getName())).willReturn(attrs);
        given(annot.getAnnotationAttributes(ConfigPropertySources.class.getName())).willReturn(aggrAttrs);
        willDoNothing().given(processor).processSource(any(), any(), any());
        processor.processConfigurationBean(beanFactory, SPRING_CFG_BEAN, annot);
        then(processor).should().processSource(same(beanFactory), eq(SPRING_CFG_BEAN), same(attrs));
        then(processor).should().processSource(same(beanFactory), eq(SPRING_CFG_BEAN), same(attrs1));
        then(processor).should().processSource(same(beanFactory), eq(SPRING_CFG_BEAN), same(attrs2));
    }

    /**
     * Tests that {@code processSource(...)} throws an exception when neither
     * name nor type are provided in the annotation.
     */
    @Test
    void givenNoNameOrType_whenProcessSource_thenThrowException() {
        AnnotationAttributes annotation = mock(AnnotationAttributes.class);
        given(annotation.getString("name")).willReturn("");
        given(annotation.getString("value")).willReturn("");
        willReturn(ConfigPropertySource.Unconfigured.class).given(annotation).getClass("type");
        assertThrows(BeanInitializationException.class, () -> postProcessor.processSource(beanFactory, SPRING_CFG_BEAN, annotation));
    }

    /**
     * Tests that {@code processSource(...)} throws an exception when both name
     * and type are provided in the annotation.
     */
    @Test
    void givenNameAndType_whenProcessSource_thenThrowException() {
        AnnotationAttributes annotation = mock(AnnotationAttributes.class);
        given(annotation.getString("name")).willReturn("testConfig");
        given(annotation.getString("value")).willReturn("");
        willReturn(ExtConfig.class).given(annotation).getClass("type");
        assertThrows(BeanInitializationException.class, () -> postProcessor.processSource(beanFactory, SPRING_CFG_BEAN, annotation));
    }

    /**
     * Tests that {@code processSource(...)} throws an exception when no bean
     * is found for the provided name.
     */
    @Test
    void givenNameWithNoBean_whenProcessSource_thenThrowException() {
        AnnotationAttributes annotation = mock(AnnotationAttributes.class);
        given(annotation.getString("name")).willReturn("missingBean");
        willReturn(ConfigPropertySource.Unconfigured.class).given(annotation).getClass("type");
        given(annotation.getBoolean("optional")).willReturn(false);
        given(beanFactory.getMergedBeanDefinition("missingBean")).willThrow(new NoSuchBeanDefinitionException("missingBean"));
        assertThrows(BeanInitializationException.class, () -> postProcessor.processSource(beanFactory, SPRING_CFG_BEAN, annotation));
    }

    /**
     * Tests that {@code processSource(...)} ignores the source when no bean is
     * found for the provided name and the source is marked as optional.
     */
    @Test
    void givenNameWithNoBeanAndOptional_whenProcessSource_thenIgnoreSource() {
        AnnotationAttributes annotation = mock(AnnotationAttributes.class);
        given(annotation.getString("name")).willReturn("missingBean");
        willReturn(ConfigPropertySource.Unconfigured.class).given(annotation).getClass("type");
        given(annotation.getBoolean("optional")).willReturn(true);
        given(beanFactory.getMergedBeanDefinition("missingBean")).willThrow(new NoSuchBeanDefinitionException("missingBean"));
        int initialSize = propertySources.size();
        postProcessor.processSource(beanFactory, SPRING_CFG_BEAN, annotation);
        assertEquals(initialSize, propertySources.size());
    }

    /**
     * Tests that {@code processSource(...)} throws an exception when the bean
     * defined for the provided name is not a {@link Config} instance.
     */
    @Test
    void givenNameWithNoConfigBean_whenProcessSource_thenThrowException() {
        AnnotationAttributes annotation = mock(AnnotationAttributes.class);
        given(annotation.getString("name")).willReturn("notConfig");
        willReturn(ConfigPropertySource.Unconfigured.class).given(annotation).getClass("type");
        given(annotation.getBoolean("optional")).willReturn(false);
        BeanDefinition beanDef = mock(BeanDefinition.class);
        given(beanFactory.getMergedBeanDefinition("notConfig")).willReturn(beanDef);
        given(beanDef.getResolvableType()).willReturn(ResolvableType.forClass(String.class));
        given(beanDef.getBeanClassName()).willReturn(String.class.getName());
        assertThrows(BeanInitializationException.class, () -> postProcessor.processSource(beanFactory, SPRING_CFG_BEAN, annotation));
    }

    /**
     * Tests that {@code processSource(...)} replaces an existing property
     * source when a source with the same name is already defined.
     */
    @Test
    void givenNameWithExistingSource_testProcessSource_thenReplaceSource() {
        AnnotationAttributes annotation = mock(AnnotationAttributes.class);
        given(annotation.getString("name")).willReturn("testConfig");
        willReturn(ConfigPropertySource.Unconfigured.class).given(annotation).getClass("type");
        given(annotation.getBoolean("optional")).willReturn(false);
        BeanDefinition beanDef = mock(BeanDefinition.class);
        given(beanDef.getResolvableType()).willReturn(ResolvableType.forClass(Config.class));
        given(beanFactory.getMergedBeanDefinition("testConfig")).willReturn(beanDef);
        PropertySource<?> oldSource = mock(PropertySource.class);
        given(oldSource.getName()).willReturn(ConfigPropertySource.SOURCE_PREFIX + "testConfig");
        propertySources.addFirst(oldSource);
        int initialSize = propertySources.size();
        postProcessor.processSource(beanFactory, SPRING_CFG_BEAN, annotation);
        assertEquals(initialSize, propertySources.size());
        ConfigLazyPropertySource source =  assertInstanceOf(
                ConfigLazyPropertySource.class,
                propertySources.get(ConfigPropertySource.SOURCE_PREFIX + "testConfig"));
        assertEquals("testConfig", source.getSource());
        assertEquals(beanFactory, source.getBeanFactory());
        assertEquals(ConfigPropertySource.SOURCE_PREFIX + "testConfig", source.getName());
    }

    /**
     * Tests that {@code processSource(...)} adds a new property source when a
     * bean with the provided name does exist.
     */
    @Test
    void givenName_whenProcessSource_thenAddPropertySource() {
        AnnotationAttributes annotation = mock(AnnotationAttributes.class);
        when(annotation.getString("name")).thenReturn("testConfig");
        when(annotation.getBoolean("optional")).thenReturn(false);
        willReturn(ConfigPropertySource.Unconfigured.class).given(annotation).getClass("type");
        BeanDefinition beanDef = mock(BeanDefinition.class);
        when(beanDef.getResolvableType()).thenReturn(ResolvableType.forClass(Config.class));
        when(beanFactory.getMergedBeanDefinition("testConfig")).thenReturn(beanDef);
        int initialSize = propertySources.size();
        postProcessor.processSource(beanFactory, SPRING_CFG_BEAN, annotation);
        assertEquals(initialSize + 1, propertySources.size());
        ConfigLazyPropertySource source =  assertInstanceOf(
                ConfigLazyPropertySource.class,
                propertySources.get(ConfigPropertySource.SOURCE_PREFIX + "testConfig"));
        assertEquals("testConfig", source.getSource());
        assertEquals(beanFactory, source.getBeanFactory());
        assertEquals(ConfigPropertySource.SOURCE_PREFIX + "testConfig", source.getName());
    }

    /**
     * Tests that {@code processSource(...)} throws an exception when multiple
     * beans are found for the provided type.
     */
    @Test
    void givenTypeWithManyBeans_whenProcessSource_thenThrowException() {
        AnnotationAttributes annotation = mock(AnnotationAttributes.class);
        when(annotation.getString("name")).thenReturn("");
        given(annotation.getString("value")).willReturn("");
        when(annotation.getBoolean("optional")).thenReturn(false);
        willReturn(Config.class).given(annotation).getClass("type");
        when(beanFactory.getBeanNamesForType(Config.class)).thenReturn(new String[]{"bean1", "bean2"});
        assertThrows(BeanInitializationException.class, () -> postProcessor.processSource(beanFactory, SPRING_CFG_BEAN, annotation));
    }

    /**
     * Tests that {@code processSource(...)} throws an exception when no bean
     * is found for the provided type.
     */
    @Test
    void givenTypeWithNoBean_whenProcessSource_thenThrowException() {
        AnnotationAttributes annotation = mock(AnnotationAttributes.class);
        when(annotation.getString("name")).thenReturn("");
        given(annotation.getString("value")).willReturn("");
        when(annotation.getBoolean("optional")).thenReturn(false);
        willReturn(Config.class).given(annotation).getClass("type");
        when(beanFactory.getBeanNamesForType(Config.class)).thenReturn(new String[]{});
        assertThrows(BeanInitializationException.class, () -> postProcessor.processSource(beanFactory, SPRING_CFG_BEAN, annotation));
    }

    /**
     * Tests that {@code processSource(...)} ignores the source when no bean
     * is found for the provided type and the source is marked as optional.
     */
    @Test
    void givenTypeWithNoBeanAndOptional_whenProcessSource_thenIgnoreSource() {
        AnnotationAttributes annotation = mock(AnnotationAttributes.class);
        when(annotation.getString("name")).thenReturn("");
        given(annotation.getString("value")).willReturn("");
        when(annotation.getBoolean("optional")).thenReturn(true);
        willReturn(ExtConfig.class).given(annotation).getClass("type");
        when(beanFactory.getBeanNamesForType(ExtConfig.class)).thenReturn(new String[]{});
        int initialSize = propertySources.size();
        postProcessor.processSource(beanFactory, SPRING_CFG_BEAN, annotation);
        assertEquals(initialSize, propertySources.size());
    }

    /**
     * Tests that {@code processSource(...)} replaces an existing property
     * source when a source with the same name is already defined.
     */
    @Test
    void givenTypeWithExistingSource_whenProcessSource_thenReplaceSource() {
        AnnotationAttributes annotation = mock(AnnotationAttributes.class);
        when(annotation.getString("name")).thenReturn("");
        given(annotation.getString("value")).willReturn("");
        when(annotation.getBoolean("optional")).thenReturn(false);
        willReturn(ExtConfig.class).given(annotation).getClass("type");
        when(beanFactory.getBeanNamesForType(ExtConfig.class)).thenReturn(new String[]{ "testConfig" });
        PropertySource<?> oldSource = mock(PropertySource.class);
        given(oldSource.getName()).willReturn(ConfigPropertySource.SOURCE_PREFIX + "testConfig");
        propertySources.addFirst(oldSource);
        int initialSize = propertySources.size();
        postProcessor.processSource(beanFactory, SPRING_CFG_BEAN, annotation);
        assertEquals(initialSize, propertySources.size());
        ConfigLazyPropertySource source =  assertInstanceOf(
                ConfigLazyPropertySource.class,
                propertySources.get(ConfigPropertySource.SOURCE_PREFIX + "testConfig"));
        assertEquals("testConfig", source.getSource());
        assertEquals(beanFactory, source.getBeanFactory());
        assertEquals(ConfigPropertySource.SOURCE_PREFIX + "testConfig", source.getName());
    }

    /**
     * Tests that {@code processSource(...)} adds a new property source when a
     * bean with the provided type does exist.
     */
    @Test
    void givenType_whenProcessSource_thenAddPropertySource() {
        AnnotationAttributes annotation = mock(AnnotationAttributes.class);
        when(annotation.getString("name")).thenReturn("");
        given(annotation.getString("value")).willReturn("");
        when(annotation.getBoolean("optional")).thenReturn(false);
        willReturn(ExtConfig.class).given(annotation).getClass("type");
        when(beanFactory.getBeanNamesForType(ExtConfig.class)).thenReturn(new String[]{ "testConfig" });
        int initialSize = propertySources.size();
        postProcessor.processSource(beanFactory, SPRING_CFG_BEAN, annotation);
        assertEquals(initialSize + 1, propertySources.size());
        ConfigLazyPropertySource source =  assertInstanceOf(
                ConfigLazyPropertySource.class,
                propertySources.get(ConfigPropertySource.SOURCE_PREFIX + "testConfig"));
        assertEquals("testConfig", source.getSource());
        assertEquals(beanFactory, source.getBeanFactory());
        assertEquals(ConfigPropertySource.SOURCE_PREFIX + "testConfig", source.getName());
    }

    interface ExtConfig extends Config {
    }
}
