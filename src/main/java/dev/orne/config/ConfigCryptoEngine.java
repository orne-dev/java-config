package dev.orne.config;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.validation.constraints.NotBlank;

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
 * Engine of cryptography transformations for configuration values.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-04
 * @since 0.2
 */
public interface ConfigCryptoEngine {

    /**
     * Creates a new {@code SecretKey} for this engine for the
     * specified password.
     * 
     * @param password The password
     * @return The newly created {@code SecretKey}
     * @throws ConfigCryptoProviderException If an exception occurs creating
     * the {@code SecretKey}
     */
    @NotNull
    SecretKey createSecretKey(
            @NotBlank
            String password)
    throws ConfigCryptoProviderException;

    /**
     * Creates a new {@code Cipher} for this engine.
     * 
     * @return The newly created {@code Cipher}
     * @throws ConfigCryptoProviderException If an exception occurs creating
     * the {@code Cipher}
     */
    @NotNull
    Cipher createCipher()
    throws ConfigCryptoProviderException;

    /**
     * Encrypts the specified plain configuration value.
     * 
     * @param value The plain configuration value
     * @param key The {@code SecretKey} to use
     * @param cipher The {@code Cipher} to use
     * @return The encrypted configuration value
     * @throws ConfigCryptoProviderException If an exception occurs during the
     * encryption process
     */
    @NotNull
    String encrypt(
            @NotNull
            String value,
            @NotNull
            SecretKey key,
            @NotNull
            Cipher cipher)
    throws ConfigCryptoProviderException;

    /**
     * Decrypts the specified encrypted configuration value.
     * 
     * @param value The encrypted configuration value
     * @param key The {@code SecretKey} to use
     * @param cipher The {@code Cipher} to use
     * @return The plain configuration value
     * @throws ConfigCryptoProviderException If an exception occurs during the
     * decryption process
     */
    @NotNull
    String decrypt(
            @NotNull
            String value,
            @NotNull
            SecretKey key,
            @NotNull
            Cipher cipher)
    throws ConfigCryptoProviderException;
}
