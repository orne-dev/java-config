package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2020 - 2025 Orne Developments
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

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.validation.constraints.NotNull;

/**
 * Abstract base implementation of {@code ConfigCryptoEngine}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2020-08
 * @since 0.2
 */
public abstract class AbstractConfigCryptoEngine
implements ConfigCryptoEngine {

    /** The default secret key salt length. */
    public static final int DEFAULT_SECRET_KEY_SALT_SIZE =
            8;

    /** Message for {@code SecureRandom} creation error. */
    private static final String RANDOM_CREATION_ERROR =
            "Error creating secure random";
    /** Message for {@code SecretKeyFactory} retrieval error. */
    private static final String SECRET_KEY_FACTORY_ALG_ERROR =
            "Cannot retrieve secret key factory for algorithm: %s";
    /** Message for {@code Cipher} creation error. */
    private static final String CIPHER_CREATION_ERROR =
            "Cannot creathe cipher for algorithm: %s";

    /** The {@code SecureRandom} instance. */
    private SecureRandom secureRandom;
    /** The secret key salt length. */
    private int saltLength = DEFAULT_SECRET_KEY_SALT_SIZE;

    /**
     * Returns the {@code SecureRandom} instance of this instance.
     * 
     * @return The {@code SecureRandom} instance
     * @throws ConfigCryptoProviderException If an error occurs creating
     * the {@code SecureRandom} instance
     */
    @NotNull
    public SecureRandom getSecureRandom()
    throws ConfigCryptoProviderException {
        synchronized(this) {
            if (this.secureRandom == null) {
                this.secureRandom = createSecureRandom();
            }
            return this.secureRandom;
        }
    }

    /**
     * Sets the {@code SecureRandom} instance of this instance.
     * 
     * @param secureRandom The {@code SecureRandom} instance
     */
    @NotNull
    public void setSecureRandom(
            final SecureRandom secureRandom) {
        synchronized(this) {
            this.secureRandom = secureRandom;
        }
    }

    /**
     * Creates a new {@code SecureRandom} instance.
     * 
     * @return The new {@code SecureRandom} instance
     * @throws ConfigCryptoProviderException If an error occurs creating
     * the {@code SecureRandom} instance
     */
    @NotNull
    public SecureRandom createSecureRandom()
    throws ConfigCryptoProviderException {
        try {
            return SecureRandom.getInstanceStrong();
        } catch (final NoSuchAlgorithmException nsae) {
            throw new ConfigCryptoProviderException(RANDOM_CREATION_ERROR, nsae);
        }
    }

    /**
     * Returns the secret key salt length.
     * 
     * @return The secret key salt length
     */
    public int getSaltLength() {
        return this.saltLength;
    }

    /**
     * Sets the secret key salt length.
     * 
     * @param length The secret key salt length
     */
    public void setSaltLength(final int length) {
        this.saltLength = length;
    }

    /**
     * Generates random password salt bytes.
     * 
     * @return The generated salt bytes
     * @throws ConfigCryptoProviderException If an error occurs creating the password salt
     */
    @NotNull
    public byte[] createSalt()
    throws ConfigCryptoProviderException {
        return createSalt(this.saltLength);
    }

    /**
     * Generates random password salt bytes.
     * 
     * @param size The size of the generated salt bytes
     * @return The generated salt bytes
     * @throws ConfigCryptoProviderException If an error occurs creating the password salt
     */
    @NotNull
    public byte[] createSalt(final int size)
    throws ConfigCryptoProviderException {
        final byte[] salt = new byte[size];
        getSecureRandom().nextBytes(salt);
        return salt;
    }

    /**
     * Gets a {@code SecretKeyFactory} for the specified algorithm.
     * 
     * @param algorithm The algorithm to use
     * @return The {@code SecretKeyFactory} for the algorithm
     * @throws ConfigCryptoProviderException If an error occurs
     * retrieving the {@code SecretKeyFactory}
     */
    @NotNull
    public SecretKeyFactory getSecretKeyFactory(
            @NotNull
            final String algorithm)
    throws ConfigCryptoProviderException {
        try {
            return SecretKeyFactory.getInstance(algorithm);
        } catch (final NoSuchAlgorithmException nsae) {
            throw new ConfigCryptoProviderException(
                    String.format(SECRET_KEY_FACTORY_ALG_ERROR, algorithm),
                    nsae);
        }
    }

    /**
     * Creates a {@code Cipher} for the specified algorithm.
     * 
     * @param algorithm The encryption algorithm to use
     * @return The created {@code Cipher}
     * @throws ConfigCryptoProviderException If an error occurs
     * creating the {@code Cipher}
     */
    @NotNull
    public Cipher createCipher(
            @NotNull
            final String algorithm)
    throws ConfigCryptoProviderException {
        try {
            return Cipher.getInstance(algorithm);
        } catch (final GeneralSecurityException gse) {
            throw new ConfigCryptoProviderException(
                    String.format(CIPHER_CREATION_ERROR, algorithm),
                    gse);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.secureRandom, this.saltLength);
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
        final AbstractConfigCryptoEngine other = (AbstractConfigCryptoEngine) obj;
        return Objects.equals(this.secureRandom, other.secureRandom)
                && Objects.equals(this.saltLength, other.saltLength);
    }
}
