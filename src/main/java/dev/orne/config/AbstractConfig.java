package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.beanutils.ConvertUtilsBean;

import dev.orne.beans.converters.OrneBeansConverters;

/**
 * Basic abstract implementation of {@code Config}. Provides basic
 * types support to calls to {@link #get(String, Class)}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 2.0, 2020-04
 * @since 0.1
 */
public abstract class AbstractConfig
implements Config {

    /** The value converter. */
    @Nonnull
    private ConvertUtilsBean converter;

    /**
     * Creates a new instance. Sets {@code converter} to the result of
     * {@link #createDefaultConverter()}.
     */
    public AbstractConfig() {
        super();
        this.converter = createDefaultConverter();
    }

    /**
     * Returns the value converter.
     * 
     * @return The value converter
     */
    public ConvertUtilsBean getConverter() {
        return this.converter;
    }

    /**
     * Sets the value converter.
     * 
     * @param converter The value converter
     */
    public void setConverter(final ConvertUtilsBean converter) {
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
    public static ConvertUtilsBean createDefaultConverter() {
        final ConvertUtilsBean result = new ConvertUtilsBean();
        result.register(false, true, 0);
        OrneBeansConverters.register(result, true);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(
            @NotBlank
            final String key)
    throws ConfigException {
        boolean found = containsParameter(key);
        if (!found && this instanceof HierarchicalConfig) {
            final Config parent = ((HierarchicalConfig) this).getParent();
            if (parent != null) {
                found = parent.contains(key);
            }
        }
        return found;
    }

    /**
     * Returns {@code true} if the parameter with the key passed as argument
     * has been configured in this instance.
     * 
     * @param key The key of the configuration parameter
     * @return Returns {@code true} if the parameter has been configured
     * @throws ConfigException If an error occurs accessing the configuration
     * property
     */
    protected abstract boolean containsParameter(
            @NotBlank
            String key)
    throws ConfigException;

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public <T> T get(
            @NotBlank
            final String key,
            @NotNull
            final Class<T> type)
    throws ConfigException {
        T value = null;
        if (containsParameter(key)) {
            value = getParameter(key, type);
        } else if (this instanceof HierarchicalConfig) {
            final Config parent = ((HierarchicalConfig) this).getParent();
            if (parent != null) {
                value = parent.get(key, type);
            }
        }
        return value;
    }

    /**
     * Returns the value of the configuration parameter configured in this
     * instance.
     * 
     * @param <T> The target type of the parameter
     * @param key The key of the configuration parameter
     * @param type The target type of the parameter
     * @return The configuration parameter value converted to the target type
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    @Nullable
    protected <T> T getParameter(
            @NotBlank
            final String key,
            @NotNull
            final Class<T> type)
    throws ConfigException {
        final T result;
        if (String.class.equals(type)) {
            result = type.cast(getStringParameter(key));
        } else if (type.isEnum()) {
            result = type.cast(getEnum(type, getStringParameter(key)));
        } else {
            result = type.cast(this.converter.convert(getStringParameter(key), type));
        }
        return result;
    }

    /**
     * Returns the constant of the enumeration type for the name passed as
     * argument.
     * 
     * @param type The enumeration type. Must be a subcass of {¢ode Enum}.
     * @param name The name of the requested constant
     * @return The constant with the requested name
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Nullable
    protected Enum<?> getEnum(
            @NotBlank
            final Class<?> type,
            @Nullable
            final String name)
    throws ConfigException {
        final Class<? extends Enum> enumType = (Class<? extends Enum>) type;
        return Enum.valueOf(enumType, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public Boolean getBoolean(
            @NotBlank
            final String key)
    throws ConfigException {
        Boolean value = null;
        if (containsParameter(key)) {
            value = getBooleanParameter(key);
        } else if (this instanceof HierarchicalConfig) {
            final Config parent = ((HierarchicalConfig) this).getParent();
            if (parent != null) {
                value = parent.getBoolean(key);
            }
        }
        return value;
    }

    /**
     * Returns the value of the configuration parameter configured in this
     * instance as {@code Boolean}.
     * 
     * @param key The key of the configuration parameter
     * @return The configuration parameter value as {@code Boolean}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    @Nullable
    protected abstract Boolean getBooleanParameter(
            @NotBlank
            String key)
    throws ConfigException;

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getString(
            @NotBlank
            final String key)
    throws ConfigException {
        String value = null;
        if (containsParameter(key)) {
            value = getStringParameter(key);
        } else if (this instanceof HierarchicalConfig) {
            final Config parent = ((HierarchicalConfig) this).getParent();
            if (parent != null) {
                value = parent.getString(key);
            }
        }
        return value;
    }

    /**
     * Returns the value of the configuration parameter configured in this
     * instance as {@code String}.
     * 
     * @param key The key of the configuration parameter
     * @return The configuration parameter value as {@code String}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    @Nullable
    protected abstract String getStringParameter(
            @NotBlank
            String key)
    throws ConfigException;

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public Number getNumber(
            @NotBlank
            final String key)
    throws ConfigException {
        Number value = null;
        if (containsParameter(key)) {
            value = getNumberParameter(key);
        } else if (this instanceof HierarchicalConfig) {
            final Config parent = ((HierarchicalConfig) this).getParent();
            if (parent != null) {
                value = parent.getNumber(key);
            }
        }
        return value;
    }

    /**
     * Returns the value of the configuration parameter configured in this
     * instance as {@code Number}.
     * 
     * @param key The key of the configuration parameter
     * @return The configuration parameter value as {@code Number}
     * @throws ConfigException If an error occurs retrieving the configuration
     * property value
     */
    protected abstract Number getNumberParameter(
            @NotBlank
            String key)
    throws ConfigException;
}
