package dev.orne.config;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2020 Orne Developments
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.PBEKeySpec;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code ConfigCryptoAesGcmEngine}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 */
@Tag("ut")
class ConfigCryptoAesGcmEngineTest {

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#ConfigCryptoAesGcmEngine()}
     * @throws Exception Should not happen
     */
    @Test
    void testConstructor()
    throws Exception {
        final ConfigCryptoAesGcmEngine engine = new ConfigCryptoAesGcmEngine();
        assertNotNull(engine);
        assertEquals(ConfigCryptoAesGcmEngine.DEFAULT_KEY_FACTORY_ALGORITHM, engine.getSecretKeyFactoryAlgorithm());
        assertEquals(ConfigCryptoAesGcmEngine.DEFAULT_SECRET_KEY_ITERATIONS, engine.getSecretKeyIterations());
        assertEquals(ConfigCryptoAesGcmEngine.DEFAULT_SECRET_KEY_LENGTH, engine.getSecretKeyLength());
        assertEquals(ConfigCryptoAesGcmEngine.DEFAULT_KEY_ALGORITHM, engine.getSecretKeyAlgorithm());
        assertEquals(ConfigCryptoAesGcmEngine.DEFAULT_CIPHER_ALGORITHM, engine.getCipherAlgorithm());
        assertEquals(ConfigCryptoAesGcmEngine.DEFAULT_GCM_IV_LENGTH, engine.getGcmInitVectorLength());
        assertEquals(ConfigCryptoAesGcmEngine.DEFAULT_GCM_TAG_LENGTH, engine.getGcmTagLength());
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#ConfigCryptoAesGcmEngine(String, String, String)}
     * @throws Exception Should not happen
     */
    @Test
    void testConstructorAlgotithms()
    throws Exception {
        final String mockKeyFactoryAlg = "mock key factory algorithm";
        final String mockKeyAlg = "mock key algorithm";
        final String mockCipherAlg = "mock cipher algorithm";
        final ConfigCryptoAesGcmEngine engine = new ConfigCryptoAesGcmEngine(
                mockKeyFactoryAlg, mockKeyAlg, mockCipherAlg);
        assertNotNull(engine);
        assertEquals(mockKeyFactoryAlg, engine.getSecretKeyFactoryAlgorithm());
        assertEquals(ConfigCryptoAesGcmEngine.DEFAULT_SECRET_KEY_ITERATIONS, engine.getSecretKeyIterations());
        assertEquals(ConfigCryptoAesGcmEngine.DEFAULT_SECRET_KEY_LENGTH, engine.getSecretKeyLength());
        assertEquals(mockKeyAlg, engine.getSecretKeyAlgorithm());
        assertEquals(mockCipherAlg, engine.getCipherAlgorithm());
        assertEquals(ConfigCryptoAesGcmEngine.DEFAULT_GCM_IV_LENGTH, engine.getGcmInitVectorLength());
        assertEquals(ConfigCryptoAesGcmEngine.DEFAULT_GCM_TAG_LENGTH, engine.getGcmTagLength());
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#getSalt()}
     * @throws Exception Should not happen
     */
    @Test
    void testGetSalt()
    throws Exception {
        final byte[] expectedResult = "mock salt bytes".getBytes();
        final ConfigCryptoAesGcmEngine engine = spy(new ConfigCryptoAesGcmEngine());
        willReturn(expectedResult).given(engine).createSalt();
        
        final byte[] result = engine.getSalt();
        
        assertNotNull(result);
        assertArrayEquals(expectedResult, result);
        
        then(engine).should(times(1)).createSalt();
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#setSalt(byte[])}
     * @throws Exception Should not happen
     */
    @Test
    void testSetSalt()
    throws Exception {
        final byte[] expectedResult = "mock salt bytes".getBytes();
        final ConfigCryptoAesGcmEngine engine = spy(new ConfigCryptoAesGcmEngine());
        engine.setSalt(expectedResult);
        
        final byte[] result = engine.getSalt();
        
        assertNotNull(result);
        assertArrayEquals(expectedResult, result);
        
        then(engine).should(never()).createSalt();
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#setSecretKeyIterations(int)}
     * @throws Exception Should not happen
     */
    @Test
    void testSetSecretKeyIterations()
    throws Exception {
        final int expectedResult = 787;
        final ConfigCryptoAesGcmEngine engine = spy(new ConfigCryptoAesGcmEngine());
        
        engine.setSecretKeyIterations(expectedResult);
        final int result = engine.getSecretKeyIterations();
        
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#setSecretKeyLength(int)}
     * @throws Exception Should not happen
     */
    @Test
    void testSetSecretKeyLength()
    throws Exception {
        final int expectedResult = 787;
        final ConfigCryptoAesGcmEngine engine = spy(new ConfigCryptoAesGcmEngine());
        
        engine.setSecretKeyLength(expectedResult);
        final int result = engine.getSecretKeyLength();
        
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#setGcmInitVectorLength(int)}
     * @throws Exception Should not happen
     */
    @Test
    void testSetGcmInitVectorLength()
    throws Exception {
        final int expectedResult = 787;
        final ConfigCryptoAesGcmEngine engine = spy(new ConfigCryptoAesGcmEngine());
        
        engine.setGcmInitVectorLength(expectedResult);
        final int result = engine.getGcmInitVectorLength();
        
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#setGcmTagLength(int)}
     * @throws Exception Should not happen
     */
    @Test
    void testSetGcmTagLength()
    throws Exception {
        final int expectedResult = 787;
        final ConfigCryptoAesGcmEngine engine = spy(new ConfigCryptoAesGcmEngine());
        
        engine.setGcmTagLength(expectedResult);
        final int result = engine.getGcmTagLength();
        
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#createKeySpec(String)}
     * @throws Exception Should not happen
     */
    @Test
    void testCreateKeySpec()
    throws Exception {
        final byte[] salt = "mock salt bytes".getBytes();
        final int iterations = 100;
        final int keyLength = 128;
        final String password = "mock password";
        
        final ConfigCryptoAesGcmEngine engine = spy(new ConfigCryptoAesGcmEngine());
        engine.setSalt(salt);
        engine.setSecretKeyIterations(iterations);
        engine.setSecretKeyLength(keyLength);
        
        final KeySpec result = engine.createKeySpec(password);
        
        assertNotNull(result);
        assertTrue(result instanceof PBEKeySpec);
        final PBEKeySpec pbeSpec = (PBEKeySpec) result;
        assertArrayEquals(salt, pbeSpec.getSalt());
        assertEquals(iterations, pbeSpec.getIterationCount());
        assertEquals(keyLength, pbeSpec.getKeyLength());
        assertArrayEquals(password.toCharArray(), pbeSpec.getPassword());

        then(engine).should(times(1)).getSalt();
        then(engine).should(times(1)).getSecretKeyIterations();
        then(engine).should(times(1)).getSecretKeyLength();
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#createKeySpec(String)}
     * @throws Exception Should not happen
     */
    @Test
    void testCreateKeySpecNullSalt()
    throws Exception {
        final byte[] salt = null;
        final int iterations = 100;
        final int keyLength = 128;
        final String password = "mock password";
        
        final ConfigCryptoAesGcmEngine engine = spy(new ConfigCryptoAesGcmEngine());
        willReturn(salt).given(engine).getSalt();
        engine.setSecretKeyIterations(iterations);
        engine.setSecretKeyLength(keyLength);
        
        assertThrows(ConfigCryptoProviderException.class, () -> {
            engine.createKeySpec(password);
        });
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#createKeySpec(String)}
     * @throws Exception Should not happen
     */
    @Test
    void testCreateKeySpecEmptySalt()
    throws Exception {
        final byte[] salt = new byte[0];
        final int iterations = 100;
        final int keyLength = 128;
        final String password = "mock password";
        
        final ConfigCryptoAesGcmEngine engine = new ConfigCryptoAesGcmEngine();
        engine.setSalt(salt);
        engine.setSecretKeyIterations(iterations);
        engine.setSecretKeyLength(keyLength);
        
        assertThrows(ConfigCryptoProviderException.class, () -> {
            engine.createKeySpec(password);
        });
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#createKeySpec(String)}
     * @throws Exception Should not happen
     */
    @Test
    void testCreateKeySpecNegativeIterations()
    throws Exception {
        final byte[] salt = "mock salt bytes".getBytes();
        final int iterations = 100;
        final int keyLength = -1;
        final String password = "mock password";
        
        final ConfigCryptoAesGcmEngine engine = new ConfigCryptoAesGcmEngine();
        engine.setSalt(salt);
        engine.setSecretKeyIterations(iterations);
        engine.setSecretKeyLength(keyLength);
        
        assertThrows(ConfigCryptoProviderException.class, () -> {
            engine.createKeySpec(password);
        });
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#createKeySpec(String)}
     * @throws Exception Should not happen
     */
    @Test
    void testCreateKeySpecNegativeLength()
    throws Exception {
        final byte[] salt = "mock salt bytes".getBytes();
        final int iterations = 100;
        final int keyLength = -1;
        final String password = "mock password";
        
        final ConfigCryptoAesGcmEngine engine = new ConfigCryptoAesGcmEngine();
        engine.setSalt(salt);
        engine.setSecretKeyIterations(iterations);
        engine.setSecretKeyLength(keyLength);
        
        assertThrows(ConfigCryptoProviderException.class, () -> {
            engine.createKeySpec(password);
        });
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#createSecretKey(String)}
     * @throws Exception Should not happen
     */
    @Test
    void testCreateSecretKey()
    throws Exception {
        final ConfigCryptoAesGcmEngine realEngine = new ConfigCryptoAesGcmEngine();
        final int iterations = 100;
        final int keyLength = 128;
        final String password = "mock password";
        final byte[] salt = realEngine.getSalt();
        final String secretKeyFactoryAlgorithm = realEngine.getSecretKeyFactoryAlgorithm();
        final String secretKeyAlgorithm = realEngine.getSecretKeyAlgorithm();
        realEngine.setSecretKeyIterations(iterations);
        realEngine.setSecretKeyLength(keyLength);
        final PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                iterations,
                keyLength);
        final ConfigCryptoAesGcmEngine engine = spy(realEngine);
        willReturn(spec).given(engine).createKeySpec(password);
        
        final SecretKeyFactory mySKF = SecretKeyFactory.getInstance(secretKeyFactoryAlgorithm);
        final SecretKey expectedSK = mySKF.generateSecret(new PBEKeySpec(
                password.toCharArray(), salt, iterations, keyLength));
        
        final SecretKey result = engine.createSecretKey(password);
        
        assertNotNull(result);
        assertEquals(secretKeyAlgorithm, result.getAlgorithm());
        assertArrayEquals(expectedSK.getEncoded(), result.getEncoded());
        
        then(engine).should(times(1)).getSecretKeyFactory(secretKeyFactoryAlgorithm);
        then(engine).should(times(1)).createKeySpec(password);
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#createSecretKey(String)}
     * @throws Exception Should not happen
     */
    @Test
    void testCreateSecretKeyWrongSKFAlgorithm()
    throws Exception {
        final String password = "mock password";
        final String mockSKFAlgorithm = "unknown SKF algorithm";
        final ConfigCryptoAesGcmEngine engine = spy(new ConfigCryptoAesGcmEngine(
                mockSKFAlgorithm, "unused", "unused"));
        willThrow(ConfigCryptoProviderException.class).given(engine).getSecretKeyFactory(mockSKFAlgorithm);
        
        assertThrows(ConfigCryptoProviderException.class, () -> {
            engine.createSecretKey(password);
        });
        
        then(engine).should(times(1)).getSecretKeyFactory(mockSKFAlgorithm);
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#createSecretKey(String)}
     * @throws Exception Should not happen
     */
    @Test
    void testCreateSecretKeyWrongParams()
    throws Exception {
        final String password = "mock password";
        final ConfigCryptoAesGcmEngine engine = spy(new ConfigCryptoAesGcmEngine());
        final KeySpec spec = new DESKeySpec(password.getBytes());
        willReturn(spec).given(engine).createKeySpec(password);
        
        assertThrows(ConfigCryptoProviderException.class, () -> {
            engine.createSecretKey(password);
        });
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#createCipher()}
     * @throws Exception Should not happen
     */
    @Test
    void testCreateCipher()
    throws Exception {
        final String cipherAlgorithm = "mock algorithm";
        final Cipher expectedResult = mock(Cipher.class);
        final ConfigCryptoAesGcmEngine engine = spy(new ConfigCryptoAesGcmEngine(
                "unused", "unused", cipherAlgorithm));
        willReturn(expectedResult).given(engine).createCipher(cipherAlgorithm);
        
        final Cipher result = engine.createCipher();
        
        assertNotNull(result);
        assertSame(expectedResult, result);
        
        then(engine).should(times(1)).createCipher(cipherAlgorithm);
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#encrypt(String, SecretKey, Cipher)}
     * @throws Exception Should not happen
     */
    @Test
    void testEncrypt()
    throws Exception {
        final ConfigCryptoAesGcmEngine engine = spy(new ConfigCryptoAesGcmEngine());
        final byte[] salt = "mock salt bytes".getBytes();
        engine.setSalt(salt);
        final SecretKey key = engine.createSecretKey("mock password");
        final Cipher cipher = engine.createCipher();
        final String plainText = "mock plain text";
        
        final String result = engine.encrypt(plainText, key, cipher);
        
        assertNotNull(result);
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#encrypt(String, SecretKey, Cipher)}
     * @throws Exception Should not happen
     */
    @Test
    void testEncryptBadKey()
    throws Exception {
        final ConfigCryptoAesGcmEngine engine = spy(new ConfigCryptoAesGcmEngine());
        final byte[] salt = "mock salt bytes".getBytes();
        engine.setSalt(salt);
        final SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        final KeySpec spec = new DESKeySpec("mock password".getBytes());
        final SecretKey key = skf.generateSecret(spec);
        final Cipher cipher = engine.createCipher();
        final String plainText = "mock plain text";
        
        assertThrows(ConfigCryptoProviderException.class, () -> {
            engine.encrypt(plainText, key, cipher);
        });
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#decrypt(String, SecretKey, Cipher)}
     * @throws Exception Should not happen
     */
    @Test
    void testDecrypt()
    throws Exception {
        final ConfigCryptoAesGcmEngine engine = spy(new ConfigCryptoAesGcmEngine());
        final byte[] salt = "mock salt bytes".getBytes();
        engine.setSalt(salt);
        final SecretKey key = engine.createSecretKey("mock password");
        final Cipher cipher = engine.createCipher();
        final String encryptedText = "oWn2xhVxeNVK9iWUbfab+5Wo/j3TIWW6X1oYMu5ecaFgSmAfHr7VzAX3Qw==";
        final String expectedValue = "mock plain text";
        
        final String result = engine.decrypt(encryptedText, key, cipher);
        
        assertNotNull(result);
        assertEquals(expectedValue, result);
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#decrypt(String, SecretKey, Cipher)}
     * @throws Exception Should not happen
     */
    @Test
    void testDecryptBadKeyType()
    throws Exception {
        final ConfigCryptoAesGcmEngine engine = spy(new ConfigCryptoAesGcmEngine());
        final byte[] salt = "mock salt bytes".getBytes();
        engine.setSalt(salt);
        final SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        final KeySpec spec = new DESKeySpec("mock password".getBytes());
        final SecretKey key = skf.generateSecret(spec);
        final Cipher cipher = engine.createCipher();
        final String encryptedText = "oWn2xhVxeNVK9iWUbfab+5Wo/j3TIWW6X1oYMu5ecaFgSmAfHr7VzAX3Qw==";
        
        assertThrows(ConfigCryptoProviderException.class, () -> {
            engine.decrypt(encryptedText, key, cipher);
        });
    }

    /**
     * Test for {@link ConfigCryptoAesGcmEngine#decrypt(String, SecretKey, Cipher)}
     * @throws Exception Should not happen
     */
    @Test
    void testDecryptWrongPassword()
    throws Exception {
        final ConfigCryptoAesGcmEngine engine = spy(new ConfigCryptoAesGcmEngine());
        final byte[] salt = "mock salt bytes".getBytes();
        engine.setSalt(salt);
        final SecretKey key = engine.createSecretKey("mock wrong password");
        final Cipher cipher = engine.createCipher();
        final String encryptedText = "oWn2xhVxeNVK9iWUbfab+5Wo/j3TIWW6X1oYMu5ecaFgSmAfHr7VzAX3Qw==";
        
        assertThrows(ConfigCryptoWrongKeyException.class, () -> {
            engine.decrypt(encryptedText, key, cipher);
        });
    }
}
