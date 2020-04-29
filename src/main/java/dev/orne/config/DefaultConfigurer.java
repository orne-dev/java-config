package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

/**
 * Default implementation of {@code Configurer}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 2.0, 2020-04
 * @since 0.1
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
            @NotNull
            final ConfigProvider configProvider) {
        Validate.notNull(configProvider, "A valid configuration provider is required.");
        this.configProvider = configProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(
            @NotNull
            final Configurable bean) {
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
            @NotNull
            final Configurable bean,
            @NotNull
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
    @NotNull
    protected Collection<Field> scanConfigurableProperties(
            @Nullable
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
            @NotNull
            final Object bean,
            @NotNull
            final Field field,
            @NotNull
            final Config config) {
        final ConfigurableProperty metadata = field.getAnnotation(ConfigurableProperty.class);
        final String key = metadata.value();
        final Class<?> type = field.getType();
        try {
            if (config.contains(key)) {
                if (type.isPrimitive()) {
                    final Class<?> wrapperType = ClassUtils.primitiveToWrapper(type);
                    final Object wrapperValue = config.get(key, wrapperType);
                    if (wrapperValue == null) {
                        LOG.warn("Null value in key '{}' for type {}", key, type);
                    } else {
                        setPropertyValue(bean, field, wrapperValue);
                    }
                } else {
                    final Object value = config.get(key, type);
                    setPropertyValue(bean, field, value);
                }
            }
        } catch (final ConfigException ce) {
            LOG.error(String.format("Error configuring property '%s' on bean of class %s",
                    field.getName(),
                    bean.getClass()), ce);
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
            @NotNull
            final Object bean,
            @NotNull
            final Field field,
            @Nullable
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
            @NotNull
            final Configurable bean,
            @NotNull
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
    @NotNull
    protected Collection<Configurable> scanNestedComponents(
            @NotNull
            final Object bean) {
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
