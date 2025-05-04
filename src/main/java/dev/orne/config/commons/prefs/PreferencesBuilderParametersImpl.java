package dev.orne.config.commons.prefs;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2021 Orne Developments
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

import java.util.Map;
import java.util.prefs.Preferences;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.builder.BasicBuilderParameters;
import org.apache.commons.configuration2.tree.ExpressionEngine;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.commons.lang3.Validate;

/**
 * A specialized parameters object for preferences configurations.
 * <p>
 * In addition to the basic properties common to all configuration
 * implementations and the properties common to hierarchical configuration
 * implementations, a preferences configuration has some special properties
 * defining the tree, package and/or path of the base preferences node to be
 * managed. This class provides fluent methods for setting these. The
 * {@code getParameters()} method puts all properties defined by the user in a
 * map from where they can be accessed by a builder for preferences
 * configurations.
 * <p>
 * This class is not thread-safe. It is intended that an instance is constructed
 * and initialized by a single thread during configuration of a
 * {@code ConfigurationBuilder}.
 * 
 * @author <a href="mailto:https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2020-09
 * @since 0.2
 */
public class PreferencesBuilderParametersImpl
extends BasicBuilderParameters
implements PreferencesBuilderParameters {

    /** Constant for the key in the parameters map used by this class. */
    protected static final String PARAM_KEY = RESERVED_PARAMETER_PREFIX
            + "PreferencesBuilderParameters";

    /**
     * Looks up an instance of this class in the specified parameters map. This
     * is equivalent to {@code fromParameters(params, false};}
     *
     * @param params the map with parameters (must not be <b>null</b>
     * @return the instance obtained from the map or <b>null</b>
     * @throws IllegalArgumentException if the map is <b>null</b>
     */
    public static PreferencesBuilderParametersImpl fromParameters(
            final @NotNull Map<String, ?> params) {
        return fromParameters(params, false);
    }

    /**
     * Looks up an instance of this class in the specified parameters map and
     * optionally creates a new one if none is found. This method can be used to
     * obtain an instance of this class which has been stored in a parameters
     * map. It is compatible with the {@code getParameters()} method.
     *
     * @param params the map with parameters (must not be <b>null</b>
     * @param createIfMissing determines the behavior if no instance is found in
     *        the map; if <b>true</b>, a new instance with default settings is
     *        created; if <b>false</b>, <b>null</b> is returned
     * @return the instance obtained from the map or <b>null</b>
     * @throws IllegalArgumentException if the map is <b>null</b>
     */
    public static PreferencesBuilderParametersImpl fromParameters(
            final @NotNull Map<String, ?> params,
            final boolean createIfMissing) {
        Validate.notNull(params, "Parameters map must not be null!");
        PreferencesBuilderParametersImpl instance =
                (PreferencesBuilderParametersImpl) params.get(PARAM_KEY);
        if (instance == null && createIfMissing) {
            instance = new PreferencesBuilderParametersImpl();
        }
        return instance;
    }

    /**
     * Creates a new {@code PreferencesBuilderParametersImpl} object from the
     * content of the given map. While {@code fromParameters()} expects that an
     * object already exists and is stored in the given map, this method
     * creates a new instance based on the content of the map. The map can
     * contain properties for settings which are stored directly in the newly
     * created object. If the map is {@code null}, an uninitialized instance is
     * returned.
     *
     * @param map The map with properties (can be {@code null})
     * @return The newly created instance
     * @throws ClassCastException if the map contains invalid data
     */
    public static PreferencesBuilderParametersImpl fromMap(
            final Map<String, ?> map) {
        final PreferencesBuilderParametersImpl params =
                new PreferencesBuilderParametersImpl();
        if (map != null) {
            params.inheritFrom(map);
        }
        return params;
    }

    /**
     * {@inheritDoc} This implementation copies some more properties defined by
     * this class.
     */
    @Override
    public void inheritFrom(final Map<String, ?> source) {
        super.inheritFrom(source);
        copyPropertiesFrom(source,
                PreferencesConfiguration.PROP_EXPRESSION_ENGINE,
                PreferencesConfigurationBuilder.PROP_MAPPER,
                PreferencesHandler.PROP_SYSTEM_TREE,
                PreferencesHandler.PROP_BASE_CLASS,
                PreferencesHandler.PROP_PATH,
                PreferencesHandler.PROP_BASE_NODE,
                PreferencesHandler.PROP_AUTO_LOAD,
                PreferencesHandler.PROP_AUTO_SAVE,
                PreferencesHandler.PROP_EVENT_COORDINATOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreferencesBuilderParametersImpl setExpressionEngine(
            final ExpressionEngine engine) {
        storeProperty(PreferencesConfiguration.PROP_EXPRESSION_ENGINE, engine);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public PreferencesBuilderParametersImpl setPreferencesMapper(
            final PreferencesMapper<ImmutableNode> mapper) {
        storeProperty(PreferencesConfigurationBuilder.PROP_MAPPER, mapper);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreferencesBuilderParametersImpl setSystemScope(
            final boolean system) {
        storeProperty(PreferencesHandler.PROP_SYSTEM_TREE, system);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreferencesBuilderParametersImpl setBaseClass(
            final Class<?> clazz) {
        storeProperty(PreferencesHandler.PROP_BASE_CLASS, clazz);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreferencesBuilderParametersImpl setPath(
            final String path) {
        storeProperty(PreferencesHandler.PROP_PATH, path);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreferencesBuilderParametersImpl setBaseNode(
            final Preferences baseNode) {
        storeProperty(PreferencesHandler.PROP_BASE_NODE, baseNode);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreferencesBuilderParametersImpl setAutoLoad(
            final boolean enable) {
        storeProperty(PreferencesHandler.PROP_AUTO_LOAD, enable);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreferencesBuilderParametersImpl setAutoSave(
            final boolean enable) {
        storeProperty(PreferencesHandler.PROP_AUTO_SAVE, enable);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PreferencesBuilderParametersImpl setEventCoordinationStrategy(
            final EventCoordinationStrategy strategy) {
        storeProperty(PreferencesHandler.PROP_EVENT_COORDINATOR, strategy);
        return this;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation returns a map which contains this
     * object itself under a specific key. The static {@code fromParameters()}
     * method can be used to extract an instance from a parameters map. Of
     * course, the properties inherited from the base class are also added to
     * the result map.
     */
    @Override
    public Map<String, Object> getParameters() {
        final Map<String, Object> params = super.getParameters();
        params.put(PARAM_KEY, this);
        return params;
    }
}
