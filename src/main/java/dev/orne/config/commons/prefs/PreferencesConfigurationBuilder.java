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

import org.apache.commons.configuration2.builder.BasicConfigurationBuilder;
import org.apache.commons.configuration2.builder.BuilderParameters;
import org.apache.commons.configuration2.builder.ConfigurationBuilder;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.ImmutableNode;

/**
 * A specialized {@code ConfigurationBuilder} implementation which can handle
 * configurations read from Java {@link Preferences}.
 * <p>
 * This class extends its base class by the support of a
 * {@link PreferencesBuilderParametersImpl} object. When the builder creates a
 * new object the resulting {@code Configuration} instance's root
 * {@code Preferences} node is associated with this builder.
 * <p>
 * The root {@code Preferences} node is kept by this builder and can be queried
 * later on. It can be used for instance to save the current
 * {@code Configuration} after it was modified. Some care has to be taken when
 * changing the location of the {@code Preferences}: The new location is
 * recorded and also survives an invocation of the {@code resetResult()}
 * method. However, when the builder's initialization parameters are reset by
 * calling {@code resetParameters()} the location is reset, too.
 *
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see PreferencesConfiguration
 */
public class PreferencesConfigurationBuilder
extends BasicConfigurationBuilder<PreferencesConfiguration>
implements ConfigurationBuilder<PreferencesConfiguration> {

    /** Prefix for configuration properties. */
    public static final String PROPERTY_PREFIX =
            BuilderParameters.RESERVED_PARAMETER_PREFIX + "PreferencesConfigurationBuilder-";
    /** Property key for the node converter. */
    protected static final String PROP_MAPPER = PROPERTY_PREFIX + "preferencesMapper";

    /** The {@code PreferencesHandler} associated with this builder. */
    private PreferencesHandler<ImmutableNode> handler;

    /**
     * Creates a new instance of {@code PreferencesConfigurationBuilder} and
     * initializes it with the given result class. No initialization properties
     * are set.
     *
     * @param resCls The result class (must not be {@code null})
     * @throws IllegalArgumentException If the result class is {@code null}
     */
    public PreferencesConfigurationBuilder(
            final Class<? extends PreferencesConfiguration> resCls) {
        super(resCls);
    }

    /**
     * Creates a new instance of {@code PreferencesConfigurationBuilder} and
     * initializes it with the given result class and an initial set of builder
     * parameters. The {@code allowFailOnInit} flag is set to {@code false}.
     *
     * @param resCls The result class (must not be {@code null})
     * @param params A map with initialization parameters
     * @throws IllegalArgumentException If the result class is {@code null}
     */
    public PreferencesConfigurationBuilder(
            final Class<? extends PreferencesConfiguration> resCls,
            final Map<String, Object> params) {
        super(resCls, params);
    }

    /**
     * Creates a new instance of {@code BasicConfigurationBuilder} and
     * initializes it with the given result class, an initial set of builder
     * parameters, and the {@code allowFailOnInit} flag. The map with
     * parameters may be {@code null}, in this case no initialization
     * parameters are set.
     *
     * @param resCls The result class (must not be {@code null})
     * @param params A map with initialization parameters
     * @param allowFailOnInit A flag whether exceptions on initializing a newly
     *        created {@code ImmutableConfiguration} object are allowed
     * @throws IllegalArgumentException If the result class is {@code null}
     */
    public PreferencesConfigurationBuilder(
            final Class<? extends PreferencesConfiguration> resCls,
            final Map<String, Object> params,
            final boolean allowFailOnInit) {
        super(resCls, params, allowFailOnInit);
    }

    /**
     * {@inheritDoc}
     * This method is overridden here to change the result type.
     */
    @Override
    public PreferencesConfigurationBuilder configure(
            final BuilderParameters... params) {
        super.configure(params);
        return this;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Also resets the {@code PreferencesHandler} associated with this builder.
     */
    @Override
    public synchronized void resetParameters() {
        super.resetParameters();
        this.handler = null;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation associates the {@code PreferencesHandler} associated
     * with this builder with the created result object and loads the content of
     * the configured {@code Preferences} base node.
     */
    @Override
    protected void initResultInstance(
            final PreferencesConfiguration obj)
    throws ConfigurationException {
        super.initResultInstance(obj);
        getPreferencesHandler().setContent(obj);
        getPreferencesHandler().load();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Also resets the {@code PreferencesBased} instance associated with the
     * {@code PreferencesHandler} associated with this builder.
     */
    @Override
    public void resetResult() {
        super.resetResult();
        if (this.handler != null) {
            this.handler.setContent(null);
        }
    }

    /**
     * Returns the {@code PreferencesHandler} associated with this builder. If already
     * a result object has been created, this {@code FileHandler} can be used to
     * save it. Otherwise, the {@code FileHandler} from the initialization
     * parameters is returned (which is not associated with a {@code FileBased}
     * object). Result is never {@code null}.
     *
     * @return The {@code PreferencesHandler} associated with this builder
     */
    protected synchronized @NotNull PreferencesHandler<ImmutableNode> getPreferencesHandler() {
        if (this.handler == null) {
            this.handler = PreferencesHandler.fromMap(
                    getParameters(),
                    fetchMapperFromParameters());
        }
        return this.handler;
    }

    /**
     * Returns the {@code PreferencesMapper} to be used by this builder. If a
     * custom {@code PreferencesMapper} has been configured in configuration
     * parameters the configured instance is returned. Else the result of
     * {@link #createDefaultPreferencesMapper()} is returned.
     * 
     * @return The {@code PreferencesMapper} to be used by this builder
     */
    protected @NotNull PreferencesMapper<ImmutableNode> fetchMapperFromParameters() {
        @SuppressWarnings("unchecked")
        PreferencesMapper<ImmutableNode> mapper =
                (PreferencesMapper<ImmutableNode>) getParameters().get(PROP_MAPPER);
        if (mapper == null) {
            mapper = createDefaultPreferencesMapper();
        }
        return mapper;
    }

    /**
     * Creates an instance of the default {@code PreferencesMapper}.
     * 
     * @return An instance of the default {@code PreferencesMapper}
     */
    protected @NotNull PreferencesMapper<ImmutableNode> createDefaultPreferencesMapper() {
        return new NodeBasedPreferencesMapper();
    }

    /**
     * Refresh the content of the {@code PreferencesConfiguration} instance
     * with the updated content of the {@code Preferences} base node.
     */
    public void refresh() {
        getPreferencesHandler().refresh();
    }

    /**
     * Updates content of the {@code Preferences} base node with the contents
     * of the {@code PreferencesConfiguration} instance.
     */
    public void save() {
        getPreferencesHandler().save();
    }

    /**
     * Creates a new {@code PreferencesBuilderParameters} instance suitable
     * to configure this builder through {@link #configure(BuilderParameters...)}.
     * 
     * @return The new {@code PreferencesBuilderParameters} instance
     */
    public static PreferencesBuilderParameters params() {
        return new PreferencesBuilderParametersImpl();
    }
}
