package dev.orne.config;

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

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apiguardian.api.API;

/**
 * Mutable configuration properties provider based on Java Preferences.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-11
 * @since 1.0
 * @see Preferences
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface PreferencesMutableConfig
extends WatchableConfig {

    /**
     * Synchronizes the configuration properties from the source preferences
     * node.
     * 
     * @throws BackingStoreException If an error occurs synchronizing the
     * configuration properties.
     * @see Preferences#sync()
     */
    void sync()
    throws BackingStoreException;

    /**
     * Saves the current configuration properties to the source preferences
     * node.
     * 
     * @throws BackingStoreException If an error occurs writing the
     * configuration properties.
     * @see Preferences#flush()
     */
    void flush()
    throws BackingStoreException;
}
