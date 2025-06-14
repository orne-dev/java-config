package dev.orne.config.crypto;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code AbstractConfigCryptoEngine}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 */
@Tag("ut")
class AbstractConfigCryptoEngineTest {

    /**
     * Test for {@link AbstractConfigCryptoEngine#createSecureRandom()}
     * @throws Exception Should not happen
     */
    @Test
    void testCreateSecureRandom()
    throws Exception {
        final AbstractConfigCryptoEngine engine = spy(AbstractConfigCryptoEngine.class);
        final SecureRandom expectedResult = SecureRandom.getInstanceStrong();
        final SecureRandom result = engine.createSecureRandom();
        assertNotNull(result);
        assertEquals(expectedResult.getAlgorithm(), result.getAlgorithm());
    }

    /**
     * Test for {@link AbstractConfigCryptoEngine#createSecureRandom()}
     * @throws Exception Should not happen
     */
    @Test
    void testCreateSecureRandomError()
    throws Exception {
        final String strongAlgorithmsBackup = AccessController.doPrivileged(
                new PrivilegedAction<String>() {
                    @Override
                    public String run() {
                        final String oldValue = Security.getProperty(
                            "securerandom.strongAlgorithms");
                        Security.setProperty(
                                "securerandom.strongAlgorithms",
                                "");
                        return oldValue;
                    }
                });
        try {
            final AbstractConfigCryptoEngine engine = spy(AbstractConfigCryptoEngine.class);
            assertThrows(ConfigCryptoProviderException.class, () -> {
                engine.createSecureRandom();
            });
        } finally {
            AccessController.doPrivileged(
                    new PrivilegedAction<Void>() {
                        @Override
                        public Void run() {
                            Security.setProperty(
                                    "securerandom.strongAlgorithms",
                                    strongAlgorithmsBackup);
                            return null;
                        }
                    });
        }
    }

    /**
     * Test for {@link AbstractConfigCryptoEngine#getSecureRandom()}
     * @throws Exception Should not happen
     */
    @Test
    void testGetSecureRandom()
    throws Exception {
        final SecureRandom expectedResult = mock(SecureRandom.class);
        final AbstractConfigCryptoEngine engine = spy(AbstractConfigCryptoEngine.class);
        
        willReturn(expectedResult).given(engine).createSecureRandom();
        
        final SecureRandom result = engine.getSecureRandom();
        assertNotNull(result);
        assertSame(expectedResult, result);
        final SecureRandom secondResult = engine.getSecureRandom();
        assertSame(expectedResult, secondResult);
        
        then(engine).should(times(1)).createSecureRandom();
    }

    /**
     * Test for {@link AbstractConfigCryptoEngine#setSecureRandom()}
     * @throws Exception Should not happen
     */
    @Test
    void testSetSecureRandom()
    throws Exception {
        final SecureRandom expectedResult = mock(SecureRandom.class);
        final AbstractConfigCryptoEngine engine = spy(AbstractConfigCryptoEngine.class);
        engine.setSecureRandom(expectedResult);
        
        willReturn(expectedResult).given(engine).createSecureRandom();
        
        final SecureRandom result = engine.getSecureRandom();
        assertNotNull(result);
        assertSame(expectedResult, result);
        
        then(engine).should(never()).createSecureRandom();
    }

    /**
     * Test for {@link AbstractConfigCryptoEngine#getSecretKeyFactory(String)}
     * @throws Exception Should not happen
     */
    @Test
    void testGetSecretKeyFactoryAlgorithm()
    throws Exception {
        final String algorithm = "PBEWITHHMACSHA384ANDAES_128";
        final SecretKeyFactory expectedResult = SecretKeyFactory.getInstance(algorithm);
        final AbstractConfigCryptoEngine engine = spy(AbstractConfigCryptoEngine.class);
        final SecretKeyFactory result = engine.getSecretKeyFactory(algorithm);
        assertNotNull(result);
        assertEquals(expectedResult.getAlgorithm(), result.getAlgorithm());
    }

    /**
     * Test for {@link AbstractConfigCryptoEngine#getSecretKeyFactory(String)}
     * @throws Exception Should not happen
     */
    @Test
    void testGetSecretKeyFactoryUnknownAlgorithm()
    throws Exception {
        final AbstractConfigCryptoEngine engine = spy(AbstractConfigCryptoEngine.class);
        assertThrows(ConfigCryptoProviderException.class, () -> {
            engine.getSecretKeyFactory("mock algorithm");
        });
    }

    /**
     * Test for {@link AbstractConfigCryptoEngine#createCipher(String)}
     * @throws Exception Should not happen
     */
    @Test
    void testCreateCipherAlgorithm()
    throws Exception {
        final String algorithm = "AES";
        final Cipher expectedResult = Cipher.getInstance(algorithm);
        final AbstractConfigCryptoEngine engine = spy(AbstractConfigCryptoEngine.class);
        final Cipher result = engine.createCipher(algorithm);
        assertNotNull(result);
        assertEquals(expectedResult.getAlgorithm(), result.getAlgorithm());
    }

    /**
     * Test for {@link AbstractConfigCryptoEngine#createCipher(String)}
     * @throws Exception Should not happen
     */
    @Test
    void testCreateCipherUnknownAlgorithm()
    throws Exception {
        final AbstractConfigCryptoEngine engine = spy(AbstractConfigCryptoEngine.class);
        assertThrows(ConfigCryptoProviderException.class, () -> {
            engine.createCipher("mock algorithm");
        });
    }
}
