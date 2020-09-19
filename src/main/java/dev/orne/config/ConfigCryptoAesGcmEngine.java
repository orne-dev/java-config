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

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.codec.binary.Base64;

/**
 * Implementation of {@code ConfigCryptoEngine} based on
 * Java Cryptography Architecture using AES with GCM symmetric algorithm.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-08
 * @since 0.2
 */
public class ConfigCryptoAesGcmEngine
extends AbstractConfigCryptoEngine
implements ConfigCryptoEngine {

    /** The default {@code SecretKeyFactory} algorithm. */
    public static final String DEFAULT_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";
    /** The default {@code SecretKey} algorithm. */
    public static final String DEFAULT_KEY_ALGORITHM = "AES";
    /** The default {@code Cipher} algorithm. */
    public static final String DEFAULT_CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    /** The default {@code SecretKey} salt iterations. */
    public static final int DEFAULT_SECRET_KEY_ITERATIONS = 65536;
    /** The default {@code SecretKey} length. */
    public static final int DEFAULT_SECRET_KEY_LENGTH = 256;
    /** The default GCM initial vector length. */
    public static final int DEFAULT_GCM_IV_LENGTH = 12;
    /** The default GCM tag length. */
    public static final int DEFAULT_GCM_TAG_LENGTH = 16;

    /** Message for {@code SecretKey} creation errors. */
    private static final String SECRET_KEY_CREATION_ERROR =
            "Error creating secret key";
    /** Message for encryption errors. */
    private static final String ENCRYPTION_ERROR =
            "Error encrypting secret value";
    /** Message for decryption errors. */
    private static final String DECRYPTION_ERROR =
            "Error decrypting secret value";
    /** Message for decryption errors caused by invalid GCM tag validations. */
    private static final String DECRYPTION_WRONG_KEY_ERROR = 
            "Error decrypting secret value. Original value encrypted with another key?";

    /** The {@code SecretKeyFactory} algorithm. */
    private final @NotNull String secretKeyFactoryAlgorithm;
    /** The generated {@code SecretKey} salt iterations. */
    private int secretKeyIterations = DEFAULT_SECRET_KEY_ITERATIONS;
    /** The generated {@code SecretKey} length. */
    private int secretKeyLength = DEFAULT_SECRET_KEY_LENGTH;
    /** The {@code SecretKey} algorithm. */
    private final @NotNull String secretKeyAlgorithm;
    /** The {@code Cipher} algorithm. */
    private final @NotNull String cipherAlgorithm;
    /** The GCM initial vector length. */
    private int gcmInitVectorLength = DEFAULT_GCM_IV_LENGTH;
    /** The GCM tag length. */
    private int gcmTagLength = DEFAULT_GCM_TAG_LENGTH;
    /** The salt used for the {@code SecretKey} creations. */
    private byte[] salt;

    /**
     * Creates a new instance with the default {@code SecretKeyFactory},
     * {@code SecretKey} and {@code Cipher} algorithms.
     */
    public ConfigCryptoAesGcmEngine() {
        this(DEFAULT_KEY_FACTORY_ALGORITHM, DEFAULT_KEY_ALGORITHM, DEFAULT_CIPHER_ALGORITHM);
    }

    /**
     * Creates a new instance with the specified {@code SecretKeyFactory},
     * {@code SecretKey} and {@code Cipher} algorithms.
     * 
     * @param secretKeyFactoryAlgorithm The {@code SecretKeyFactory} algorithm
     * @param secretKeyAlgorithm The {@code SecretKey} algorithm
     * @param cipherAlgorithm The {@code Cipher} algorithm
     */
    public ConfigCryptoAesGcmEngine(
            final @NotNull String secretKeyFactoryAlgorithm,
            final @NotNull String secretKeyAlgorithm,
            final @NotNull String cipherAlgorithm) {
        super();
        this.secretKeyFactoryAlgorithm = secretKeyFactoryAlgorithm;
        this.secretKeyAlgorithm = secretKeyAlgorithm;
        this.cipherAlgorithm = cipherAlgorithm;
    }

    /**
     * Returns the {@code SecretKeyFactory} algorithm.
     * 
     * @return The {@code SecretKeyFactory} algorithm.
     */
    public @NotNull String getSecretKeyFactoryAlgorithm() {
        return this.secretKeyFactoryAlgorithm;
    }

    /**
     * Returns the {@code SecretKey} salt iterations.
     * 
     * @return The {@code SecretKey} salt iterations
     */
    public int getSecretKeyIterations() {
        return this.secretKeyIterations;
    }

    /**
     * Sets the generated {@code SecretKey} salt iterations.
     * 
     * @param iterations The generated {@code SecretKey} salt iterations
     */
    public void setSecretKeyIterations(final int iterations) {
        this.secretKeyIterations = iterations;
    }

    /**
     * Returns the generated {@code SecretKey} length.
     * 
     * @return The generated {@code SecretKey} length
     */
    public int getSecretKeyLength() {
        return this.secretKeyLength;
    }

    /**
     * Sets the generated {@code SecretKey} length.
     * 
     * @param length The generated {@code SecretKey} length
     */
    public void setSecretKeyLength(final int length) {
        this.secretKeyLength = length;
    }

    /**
     * Returns the {@code SecretKey} algorithm.
     * 
     * @return The {@code SecretKey} algorithm
     */
    public @NotNull String getSecretKeyAlgorithm() {
        return this.secretKeyAlgorithm;
    }

    /**
     * Returns the {@code Cipher} algorithm.
     * 
     * @return The {@code Cipher} algorithm
     */
    public @NotNull String getCipherAlgorithm() {
        return this.cipherAlgorithm;
    }

    /**
     * Returns the GCM initial vector length.
     * 
     * @return The GCM initial vector length
     */
    public int getGcmInitVectorLength() {
        return this.gcmInitVectorLength;
    }

    /**
     * Sets the GCM initial vector length.
     * 
     * @param length The GCM initial vector length
     */
    public void setGcmInitVectorLength(final int length) {
        this.gcmInitVectorLength = length;
    }

    /**
     * Returns the GCM tag length.
     * 
     * @return The GCM tag length
     */
    public int getGcmTagLength() {
        return this.gcmTagLength;
    }

    /**
     * Sets the GCM tag length.
     * 
     * @param length The GCM tag length
     */
    public void setGcmTagLength(final int length) {
        this.gcmTagLength = length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull SecretKey createSecretKey(
            final @NotBlank String password)
    throws ConfigCryptoProviderException {
        try {
            final SecretKeyFactory factory = getSecretKeyFactory(
                    getSecretKeyFactoryAlgorithm());
            final KeySpec spec = createKeySpec(password);
            final SecretKey tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(),
                    getSecretKeyAlgorithm());
        } catch (final GeneralSecurityException gse) {
            throw new ConfigCryptoProviderException(SECRET_KEY_CREATION_ERROR, gse);
        }
    }

    /**
     * Generates the secret key specification for the specified password
     * and the 
     * 
     * @param password The password
     * @return The secret key specification
     * @throws ConfigCryptoProviderException If an error occurs when creating
     * the specification
     */
    protected @NotNull KeySpec createKeySpec(
            final @NotBlank String password)
    throws ConfigCryptoProviderException {
        try {
            return new PBEKeySpec(
                    password.toCharArray(),
                    getSalt(),
                    getSecretKeyIterations(),
                    getSecretKeyLength());
        } catch (final NullPointerException npe) {
            throw new ConfigCryptoProviderException(SECRET_KEY_CREATION_ERROR, npe);
        } catch (final IllegalArgumentException iae) {
            throw new ConfigCryptoProviderException(SECRET_KEY_CREATION_ERROR, iae);
        }
    }

    /**
     * Returns the salt used for the {@code SecretKey} creations. If no salt
     * has been set a random one is generated and set.
     * 
     * @return The salt used for the {@code SecretKey} creations
     * @throws ConfigCryptoProviderException If an error occurs generating
     * the random salt
     */
    public @NotNull byte[] getSalt()
    throws ConfigCryptoProviderException {
        synchronized (this) {
            if (this.salt == null) {
                this.salt = createSalt();
            }
            return this.salt;
        }
    }

    /**
     * Sets the salt to be used for the {@code SecretKey} creations.
     * 
     * @param salt The salt to be used for the {@code SecretKey} creations
     */
    public void setSalt(
            final @NotNull byte[] salt) {
        synchronized (this) {
            this.salt = new byte[salt.length];
            System.arraycopy(salt, 0, this.salt, 0, salt.length);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Cipher createCipher()
    throws ConfigCryptoProviderException {
        return createCipher(getCipherAlgorithm());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String encrypt(
            final @NotNull String value,
            final @NotNull SecretKey key,
            final @NotNull Cipher cipher)
    throws ConfigCryptoProviderException {
        final byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
        final byte[] initVector = new byte[getGcmInitVectorLength()];
        getSecureRandom().nextBytes(initVector);
        final GCMParameterSpec spec = new GCMParameterSpec(
                getGcmTagLength() * java.lang.Byte.SIZE,
                initVector);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, spec, getSecureRandom());
            final int resultBytes = initVector.length + cipher.getOutputSize(valueBytes.length);
            final byte[] ciphertext = new byte[resultBytes];
            System.arraycopy(initVector, 0, ciphertext, 0, initVector.length);
            cipher.doFinal(valueBytes, 0, valueBytes.length, ciphertext, initVector.length);
            return Base64.encodeBase64String(ciphertext);
        } catch (final GeneralSecurityException gse) {
            throw new ConfigCryptoProviderException(ENCRYPTION_ERROR, gse);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String decrypt(
            final @NotNull String value,
            final @NotNull SecretKey key,
            final @NotNull Cipher cipher)
    throws ConfigCryptoProviderException {
        final byte[] cipherBytes = Base64.decodeBase64(value);
        final GCMParameterSpec gcmSpec = new GCMParameterSpec(
                getGcmTagLength() * java.lang.Byte.SIZE,
                cipherBytes,
                0,
                getGcmInitVectorLength());
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec, getSecureRandom());
            final byte[] valueBytes = cipher.doFinal(
                    cipherBytes,
                    getGcmInitVectorLength(),
                    cipherBytes.length - getGcmInitVectorLength());
            return new String(valueBytes, StandardCharsets.UTF_8);
        } catch (final AEADBadTagException bte) {
            throw new ConfigCryptoWrongKeyException(DECRYPTION_WRONG_KEY_ERROR, bte);
        } catch (final GeneralSecurityException gse) {
            throw new ConfigCryptoProviderException(DECRYPTION_ERROR, gse);
        }
    }
}
