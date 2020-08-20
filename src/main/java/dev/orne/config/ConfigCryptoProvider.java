package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2020 Orne Developments
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
 * Provider of cryptography transformations for configuration values.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-04
 * @since 0.2
 */
public interface ConfigCryptoProvider {

    /**
     * Encrypts the specified plain configuration value.
     * 
     * @param string The plain configuration value
     * @return The encrypted configuration value
     * @throws ConfigCryptoProviderException If an exception occurs during the
     * encryption process
     */
    @NotNull
    String encrypt(
            @NotNull
            String string)
    throws ConfigCryptoProviderException;

    /**
     * Decrypts the specified encrypted configuration value.
     * 
     * @param string The encrypted configuration value
     * @return The plain configuration value
     * @throws ConfigCryptoProviderException If an exception occurs during the
     * decryption process
     */
    @NotNull
    String decrypt(
            @NotNull
            String string)
    throws ConfigCryptoProviderException;
}
