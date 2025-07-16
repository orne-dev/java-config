package dev.orne.config.impl;

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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

import dev.orne.config.Config;
import dev.orne.config.ConfigException;
import dev.orne.config.ConfigProvider;
import dev.orne.config.Configurable;
import dev.orne.config.ConfigurableProperty;
import dev.orne.config.ConfigurationOptions;
import dev.orne.config.Configurer;

/**
 * Default implementation of {@code Configurer}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2019-07
 * @version 2.0, 2025-07
 * @since 0.1
 * @see Configurer
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class ConfigurerImpl
implements Configurer {

    /** The class logger. */
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurerImpl.class);

    /** The configuration provider. */
    private final @NotNull ConfigProvider configProvider;
    /** The value converter. */
    private final @NotNull ConvertUtilsBean converter;

    /**
     * Creates a new instance.
     * 
     * @param configProvider The configuration provider
     */
    public ConfigurerImpl(
            final @NotNull ConfigProvider configProvider) {
        this(configProvider, defaultConverter());
    }

    /**
     * Creates a new instance.
     * 
     * @param configProvider The configuration provider
     * @param converter The value converter.
     */
    public ConfigurerImpl(
            final @NotNull ConfigProvider configProvider,
            final @NotNull ConvertUtilsBean converter) {
        Validate.notNull(configProvider, "A valid configuration provider is required.");
        this.configProvider = configProvider;
        Validate.notNull(configProvider, "A valid value converter is required.");
        this.converter = converter;
    }

    /**
     * <p>Creates a new value converter configured with the default settings.</p>
     * 
     * <p>This converter is configured to:</p>
     * <ul>
     * <li>Return {@code null} for {@code null} values.
     * <li>Return {@code null} for values of incompatible types.
     * <li>Return empty arrays for {@code null} values.
     * <li>Return empty collections for {@code null} values.
     * </ul>
     * 
     * @return A new value converter configured with the default settings
     */
    public static @NotNull ConvertUtilsBean defaultConverter() {
        final ConvertUtilsBean result = new ConvertUtilsBean();
        result.register(false, true, 0);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(
            final @NotNull Configurable bean) {
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
            if (metadata == null || metadata.configureProperties()) {
                configureProperties(bean, config);
            }
            bean.configure(config);
            if (metadata == null || metadata.configureNestedBeans()) {
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
            final @NotNull Configurable bean,
            final @NotNull Config config) {
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
    protected @NotNull Collection<Field> scanConfigurableProperties(
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
            final @NotNull Object bean,
            final @NotNull Field field,
            final @NotNull Config config) {
        final ConfigurableProperty metadata = field.getAnnotation(ConfigurableProperty.class);
        final String key = metadata.value();
        final Class<?> type = field.getType();
        try {
            if (config.contains(key)) {
                final String strValue = config.get(key);
                if (type.isPrimitive()) {
                    final Class<?> wrapperType = ClassUtils.primitiveToWrapper(type);
                    final Object wrapperValue = convertValue(strValue, wrapperType);
                    if (wrapperValue == null) {
                        LOG.warn("Null value in key '{}' for type {}", key, type);
                    } else {
                        setPropertyValue(bean, field, wrapperValue);
                    }
                } else {
                    setPropertyValue(bean, field, convertValue(strValue, type));
                }
            }
        } catch (final ConfigException ce) {
            LOG.error(String.format("Error configuring property '%s' on bean of class %s",
                    field.getName(),
                    bean.getClass()), ce);
        }
    }

    /**
     * Converts the configuration property value to the specified target type.
     * 
     * @param <T> The target type.
     * @param value The configuration property value.
     * @param type The target type.
     * @return The converted configuration value.
     * throws ConfigException If an error occurs converting the value.
     */
    protected <T> T convertValue(
            final String value,
            final Class<T> type) {
        try {
            return type.cast(this.converter.convert(value, type));
        } catch (final ConversionException e) {
            throw new ConfigException("Error converting configuration property value", e);
        }
    }

    /**
     * Sets the specified property of the specified bean with the specified
     * value.
     * 
     * @param bean The instance of the bean
     * @param field The property of the bean to set
     * @param value The value to set
     */
    protected void setPropertyValue(
            final @NotNull Object bean,
            final @NotNull Field field,
            final Object value) {
        try {
            PropertyUtils.setProperty(bean, field.getName(), value);
        } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOG.error(String.format(
                    "Error setting property '%s' on bean of class %s",
                    field.getName(),
                    bean.getClass()), e);
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
            final @NotNull Configurable bean,
            final @NotNull Config config) {
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
    protected @NotNull Collection<Configurable> scanNestedComponents(
            final @NotNull Object bean) {
        final Set<Configurable> nestedComponents = new HashSet<>();
        Class<?> currentClass = bean.getClass();
        while (currentClass != null) {
            for (final Field field : currentClass.getDeclaredFields()) {
                try {
                    final Object fieldValue = PropertyUtils.getProperty(bean, field.getName());
                    if (fieldValue instanceof Configurable) {
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
