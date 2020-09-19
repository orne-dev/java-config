/**
 * 
 */
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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Encrypted {@code MutableConfig} implementation.
 * Decrypts values obtained from delegate {@code MutableConfig} and encrypts
 * values before storing them in the delegate instance.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-04
 * @since 0.2
 */
public class MutableEncryptedConfig
extends EncryptedConfig
implements MutableConfig {

    /**
     * Creates a new instance.
     * 
     * @param delegate  The delegate {@code MutableConfig} instance
     * @param cryptoProvider The provider of cryptography transformations
     */
    public MutableEncryptedConfig(
            final @NotNull MutableConfig delegate,
            final @NotNull ConfigCryptoProvider cryptoProvider) {
        super(delegate, cryptoProvider);
    }

    /**
     * Returns the delegate {@code MutableConfig} instance.
     * 
     * @return The delegate {@code MutableConfig} instance
     */
    @Override
    protected @NotNull MutableConfig getDelegate() {
        return (MutableConfig) super.getDelegate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(
            final @NotBlank String key,
            final Object value)
    throws ConfigException {
        String plainValue = convertValueToString(value);
        final String cryptoValue = getCryptoProvider().encrypt(plainValue);
        getDelegate().set(key, cryptoValue);
    }

    /**
     * Converts specified value to {@code String} value.
     * 
     * @param value The value in any form
     * @return The value in {@code String} form
     * @throws ConfigException If an error occurs converting the value
     */
    protected @NotNull String convertValueToString(
            final Object value)
    throws ConfigException {
        String result = AbstractMutableStringConfig.convertValueToString(
                getConverter(),
                value);
        if (value == null) {
            result = AbstractStringConfig.NULL;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(
            final @NotBlank String key)
    throws ConfigException {
        getDelegate().remove(key);
    }
}
