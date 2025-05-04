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

import java.util.prefs.Preferences;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.convert.ConversionHandler;
import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;

/**
 * Specialization of {@code HierarchicalConfiguration} for configurations
 * based on Java {@code Preferences}.
 * 
 * @author <a href="mailto:https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @param <N> The {@code HierarchicalConfiguration} node type
 * @since 0.2
 * @see Preferences
 * @see HierarchicalConfiguration
 */
public interface PreferencesBased<N>
extends HierarchicalConfiguration<N> {

    /**
     * Returns the {@code ConversionHandler} used by this instance.
     *
     * @return the {@code ConversionHandler}
     * @since 2.0
     */
    ConversionHandler getConversionHandler();

    /**
     * Loads the configuration from the given {@code HierarchicalConfiguration}
     * node.
     * <p>
     * Note that the {@code clear()} method is not called, so the properties
     * contained in the loaded file will be added to the current set of
     * properties.
     * 
     * @param rootNode The {@code HierarchicalConfiguration} node to load the
     * configuration from
     * @throws ConfigurationRuntimeException If an error occurs loading the
     * nodes content
     */
    void load(@NotNull N rootNode);
}
