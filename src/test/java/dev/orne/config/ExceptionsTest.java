package dev.orne.config;

/*-
 * #%L
 * Orne Beans
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

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.crypto.ConfigCryptoProviderException;
import dev.orne.config.crypto.ConfigCryptoWrongKeyException;

/**
 * Unit tests for library exceptions.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.1, 2025-05
 * @since 0.2
 * @see ConfigException
 */
@Tag("ut")
class ExceptionsTest {

    /** Message for exception testing. */
    private static final String TEST_MESSAGE = "Test message";
    /** Cause for exception testing. */
    private static final Throwable TEST_CAUSE = new Exception();

    /**
     * Test for {@link ConfigException}.
     */
    @Test
    void testConfigException() {
        assertEmptyException(new ConfigException());
        assertMessageException(new ConfigException(TEST_MESSAGE));
        assertCauseException(new ConfigException(TEST_CAUSE));
        assertFullException(new ConfigException(TEST_MESSAGE, TEST_CAUSE));
        assertFullException(new ConfigException(TEST_MESSAGE, TEST_CAUSE, false, false));
    }

    /**
     * Test for {@link NonIterableConfigException}.
     */
    @Test
    void testNonIterableConfigException() {
        assertEmptyException(new NonIterableConfigException());
        assertMessageException(new NonIterableConfigException(TEST_MESSAGE));
        assertCauseException(new NonIterableConfigException(TEST_CAUSE));
        assertFullException(new NonIterableConfigException(TEST_MESSAGE, TEST_CAUSE));
        assertFullException(new NonIterableConfigException(TEST_MESSAGE, TEST_CAUSE, false, false));
    }

    /**
     * Test for {@link ConfigCryptoProviderException}.
     */
    @Test
    void testConfigCryptoProviderException() {
        assertEmptyException(new ConfigCryptoProviderException());
        assertMessageException(new ConfigCryptoProviderException(TEST_MESSAGE));
        assertCauseException(new ConfigCryptoProviderException(TEST_CAUSE));
        assertFullException(new ConfigCryptoProviderException(TEST_MESSAGE, TEST_CAUSE));
        assertFullException(new ConfigCryptoProviderException(TEST_MESSAGE, TEST_CAUSE, false, false));
    }

    /**
     * Test for {@link ConfigCryptoWrongKeyException}.
     */
    @Test
    void testConfigCryptoWrongKeyException() {
        assertEmptyException(new ConfigCryptoWrongKeyException());
        assertMessageException(new ConfigCryptoWrongKeyException(TEST_MESSAGE));
        assertCauseException(new ConfigCryptoWrongKeyException(TEST_CAUSE));
        assertFullException(new ConfigCryptoWrongKeyException(TEST_MESSAGE, TEST_CAUSE));
        assertFullException(new ConfigCryptoWrongKeyException(TEST_MESSAGE, TEST_CAUSE, false, false));
    }

    /**
     * Asserts that exception has no message and no cause.
     * 
     * @param exception The exception to test
     */
    private void assertEmptyException(
            final @NotNull Exception exception) {
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    /**
     * Asserts that exception has message but no cause.
     * 
     * @param exception The exception to test
     */
    private void assertMessageException(
            final @NotNull Exception exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }

    /**
     * Asserts that exception has cause but no message.
     * 
     * @param exception The exception to test
     */
    private void assertCauseException(
            final @NotNull Exception exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_CAUSE.toString(), exception.getMessage());
        assertNotNull(exception.getCause());
        assertSame(TEST_CAUSE, exception.getCause());
    }

    /**
     * Asserts that exception has message and cause.
     * 
     * @param exception The exception to test
     */
    private void assertFullException(
            final @NotNull Exception exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertNotNull(exception.getCause());
        assertSame(TEST_CAUSE, exception.getCause());
    }
}
