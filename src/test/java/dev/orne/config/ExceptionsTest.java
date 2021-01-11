package dev.orne.config;

/*-
 * #%L
 * Orne Beans
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

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.commons.prefs.PreferencesNodeDeletedException;

/**
 * Unit tests for library exceptions.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-08
 * @since 0.2
 * @see ConfigException
 */
@Tag("ut")
class ExceptionsTest {

    /** Message for exception testing. */
    private static final String TEST_MESSAGE = "Test message";
    /** Cause for exception testing. */
    private static final Throwable TEST_CAUSE = new Exception();
    /** Message format for exception varargs testing. */
    private static final String TEST_VARARGS_FORMAT = "Test %s message %s";
    /** Value #1 for exception varargs testing. */
    private static final String TEST_VARARGS_VALUE1 = "value1";
    /** Value #1 for exception varargs testing. */
    private static final String TEST_VARARGS_VALUE2 = "value2";
    /** Message for exception varargs testing. */
    private static final String TEST_VARARGS_MESSAGE = "Test value1 message value2";

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
     * Test for {@link PreferencesNodeDeletedException}.
     */
    @Test
    void testPreferencesNodeDeletedException() {
        assertEmptyException(new PreferencesNodeDeletedException());
        assertMessageException(new PreferencesNodeDeletedException(TEST_MESSAGE));
        assertStringFormatMessageException(new PreferencesNodeDeletedException(
                TEST_VARARGS_FORMAT,
                TEST_VARARGS_VALUE1,
                TEST_VARARGS_VALUE2));
        assertCauseException(new PreferencesNodeDeletedException(TEST_CAUSE));
        assertFullException(new PreferencesNodeDeletedException(TEST_MESSAGE, TEST_CAUSE));
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
     * Asserts that exception has message formatted by
     * {@link String#format(String, Object...)} but no cause.
     * 
     * @param exception The exception to test
     */
    private void assertStringFormatMessageException(
            final @NotNull Exception exception) {
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
        assertEquals(TEST_VARARGS_MESSAGE, exception.getMessage());
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
