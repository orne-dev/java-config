package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

/**
 * Default implementation of {@code Configurer}.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 * @see Configurer
 */
public class DefaultConfigurer
implements Configurer {

	/** The class logger. */
	private static final Logger LOG = LoggerFactory.getLogger(DefaultConfigurer.class);

	/** The configuration provider. */
	private final ConfigProvider configProvider;

	/**
	 * Creates a new instance.
	 * 
	 * @param configProvider The configuration provider
	 */
	public DefaultConfigurer(
			final ConfigProvider configProvider) {
		Validate.notNull(configProvider, "A valid configuration provider is required.");
		this.configProvider = configProvider;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configure(final Configurable bean) {
		Validate.notNull(bean, "A not null bean is required.");
		final Class<?> componentClass = bean.getClass();
		final ConfigurationOptions metadata = componentClass.getAnnotation(
				ConfigurationOptions.class);
		final Config config;
		if (metadata == null) {
			config = this.configProvider.getDefaultConfig();
		} else {
			config = this.configProvider.selectConfig(
					metadata, componentClass);
		}
		if (config != null) {
			if (metadata == null ? true : metadata.configureProperties()) {
				configureProperties(bean, config);
			}
			bean.configure(config);
			if (metadata == null ? true : metadata.configureNestedBeans()) {
				configureNestedBeans(bean, config);
			}
		}
	}

	/**
	 * Configures the properties of the bean passed as argument with the
	 * selected configuration.
	 * 
	 * @param bean The bean which properties configure
	 * @param config The configuration to use
	 */
	protected void configureProperties(
			final Configurable bean,
			final Config config) {
		final Collection<Field> fields = scanConfigurableProperties(bean.getClass());
		for (final Field field : fields) {
			configureProperty(bean, field, config);
		}
	}

	/**
	 * Scans the fields annotated with {@code ConfigurableProperty} in the
	 * bean class passed as argument.
	 * 
	 * @param targetClass The bean class to scan for configurable fields
	 * @return The configurable fields detected
	 */
	protected Collection<Field> scanConfigurableProperties(
			final Class<?> targetClass) {
		final Set<Field> configurableFields = new HashSet<>();
		Class<?> currentClass = targetClass;
		while (currentClass != null) {
			for (final Field field : currentClass.getDeclaredFields()) {
				if (field.isAnnotationPresent(ConfigurableProperty.class)) {
					configurableFields.add(field);
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return configurableFields;
	}

	/**
	 * Configures the requested property in the bean with the provided
	 * configuration.
	 * 
	 * @param bean The instance of the bean
	 * @param field The property of the bean to configure
	 * @param config The configuration to use
	 */
	protected void configureProperty(
			final Object bean,
			final Field field,
			final Config config) {
		final ConfigurableProperty metadata = field.getAnnotation(ConfigurableProperty.class);
		final String key = metadata.value();
		final Class<?> type = field.getType();
		if (config.contains(key)) {
			if (type.isPrimitive()) {
				final Class<?> wrapperType = ClassUtils.primitiveToWrapper(type);
				final Object wrapperValue = config.get(key, wrapperType);
				if (wrapperValue == null) {
					LOG.warn("Null value in key '{}' for type {}", key, type);
				} else {
					try {
						PropertyUtils.setProperty(bean, field.getName(), wrapperValue);
					} catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
						LOG.error(MessageFormatter.format(
								"Error setting property '{}' on bean of class {}",
								field.getName(),
								bean.getClass()).getMessage(), e);
					}
				}
			} else {
				final Object value = config.get(key, type);
				try {
					PropertyUtils.setProperty(bean, field.getName(), value);
				} catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					LOG.error(MessageFormatter.format(
							"Error setting property '{}' on bean of class {}",
							field.getName(),
							bean.getClass()).getMessage(), e);
				}
			}
		}
	}

	/**
	 * Configures the nested beans of the bean passed as argument with the
	 * selected configuration.
	 * 
	 * @param bean The bean which nested beans configure
	 * @param config The configuration to use
	 */
	protected void configureNestedBeans(
			final Configurable bean,
			final Config config) {
		final Collection<Configurable> nestedBeans = scanNestedComponents(bean);
		for (final Configurable nestedBean : nestedBeans) {
			if (!nestedBean.isConfigured()) {
				configure(nestedBean);
			}
		}
	}

	/**
	 * Scans the fields of the bean passed as argument for instances of
	 * {@code Configurable} not configured.
	 * 
	 * @param bean The bean to scan for unconfigured nested beans
	 * @return The nested beans detected
	 */
	protected Collection<Configurable> scanNestedComponents(
			final Object bean) {
		final Set<Configurable> nestedComponents = new HashSet<>();
		Class<?> currentClass = bean.getClass();
		while (currentClass != null) {
			for (final Field field : currentClass.getDeclaredFields()) {
				try {
					final Object fieldValue = PropertyUtils.getProperty(bean, field.getName());
					if (fieldValue != null && fieldValue instanceof Configurable) {
						final Configurable nestedComponent = (Configurable) fieldValue;
						nestedComponents.add(nestedComponent);
					}
				} catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					LOG.error(MessageFormatter.format(
							"Error accessing property '{}' on bean of class {}",
							field.getName(),
							currentClass).getMessage(), e);
				}
			}
			currentClass = currentClass.getSuperclass();
		}
		return nestedComponents;
	}
}
