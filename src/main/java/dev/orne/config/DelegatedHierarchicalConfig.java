package dev.orne.config;

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
 * Delegated {@code HierarchicalConfig} implementation.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @since 0.2
 */
public class DelegatedHierarchicalConfig
extends DelegatedConfig
implements HierarchicalConfig {

    /** The parent configuration. */
    private final Config parent;

    /**
     * Creates a new instance.
     * 
     * @param delegate The delegate {@code Config} instance
     * @param parent The parent {@code Config} instance
     */
    public DelegatedHierarchicalConfig(
            final @NotNull Config delegate,
            final Config parent) {
        super(delegate);
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Config getParent() {
        return this.parent;
    }
}
