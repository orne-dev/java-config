package dev.orne.config.crypto;

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

import javax.crypto.SecretKey;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Provider of cryptography transformations for configuration values.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2020-04
 * @since 0.2
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface ConfigCryptoProvider {

    /**
     * Creates a new cryptography transformations provider builder.
     * 
     * @return The provider builder.
     */
    static @NotNull CryptoProviderEngineBuilder builder() {
        return new CryptoProviderBuilderImpl();
    }

    /**
     * Encrypts the specified plain configuration value.
     * 
     * @param value The plain configuration value
     * @return The encrypted configuration value
     * @throws ConfigCryptoProviderException If an exception occurs during the
     * encryption process
     */
    String encrypt(
            String value)
    throws ConfigCryptoProviderException;

    /**
     * Decrypts the specified encrypted configuration value.
     * 
     * @param value The encrypted configuration value
     * @return The plain configuration value
     * @throws ConfigCryptoProviderException If an exception occurs during the
     * decryption process
     */
    String decrypt(
            String value)
    throws ConfigCryptoProviderException;

    /**
     * Destroys all secret information.
     * Any further call to the instance will throw an
     * {@code IllegalStateException}.
     * 
     * @throws ConfigCryptoProviderException If an error occurs destroying the
     * secret information.
     */
    void destroy();

    interface KeyBuilder {

        /**
         * Sets the password used as secret key.
         * 
         * @param password The password.
         * @return
         * @throws ConfigCryptoProviderException If an error occurs creating the
         * secret key.
         */
        @NotNull Builder withSecretKey(
                @NotNull char[] password);

        @NotNull Builder withSecretKey(
                @NotNull SecretKey key);
    }

    interface Builder {

        @NotNull Builder pooled();

        @NotNull Builder withNullValue(
                @NotNull String value);

        @NotNull ConfigCryptoProvider build();
    }
}
