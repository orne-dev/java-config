package dev.orne.config;

import java.util.Objects;

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

import javax.validation.constraints.NotNull;

/**
 * Delegated {@code HierarchicalConfig} implementation that also implements
 * {@code WatchableConfig}
 * 
 * @author <a href="mailto:https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 */
public class WatchableDelegatedHierarchicalConfig
extends WatchableDelegatedConfig
implements HierarchicalConfig {

    /** The parent configuration. */
    private final Config parent;

    /**
     * Creates a new instance.
     * 
     * @param delegate The delegate {@code Config} instance
     * @param parent The parent {@code Config} instance
     */
    public WatchableDelegatedHierarchicalConfig(
            final @NotNull WatchableConfig delegate,
            final Config parent) {
        super(delegate);
        this.parent = parent;
        if (parent instanceof WatchableConfig) {
            ((WatchableConfig) parent).addListener((inst, keys) ->
                    getEvents().notify(this, keys));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Config getParent() {
        return this.parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WatchableDelegatedHierarchicalConfig other = (WatchableDelegatedHierarchicalConfig) obj;
        return super.equals(obj)
                && Objects.equals(this.parent, other.parent);
    }
}
