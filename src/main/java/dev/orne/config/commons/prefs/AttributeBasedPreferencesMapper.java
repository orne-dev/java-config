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

/**
 * Implementation of {@code PreferencesMapper} for
 * {@code ImmutableNode} based {@code HierarchicalConfiguration} instances that
 * maps all {@code Preferences} properties to {@code ImmutableNode} attributes,
 * creating child {@code ImmutableNode} instances only for child
 * {@code Preferences} nodes.
 * <p>
 * All {@code Preferences} are mapped as {@code ImmutableNode} attributes.
 * <p>
 * No {@code ImmutableNode} has value.
 * 
 * @author <a href="mailto:https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 */
public class AttributeBasedPreferencesMapper
extends AbstractImmutableNodePreferencesMapper {

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getValue(
            final @NotNull Preferences node) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setValue(
            final @NotNull Preferences node,
            final String value) {
        if (value != null) {
            throw new UnsupportedOperationException(
                    "Node values are not supported with this Preferences configuration settings");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getChildValue(
            final @NotNull Preferences parent,
            final @NotNull String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setChildValue(
            final @NotNull Preferences parent,
            final @NotNull String name,
            final String value) {
        if (value != null) {
            throw new UnsupportedOperationException(
                    "Node values are not supported with this Preferences configuration settings");
        }
    }
}
