package dev.orne.config;

import java.util.Objects;

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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;

/**
 * Encrypted {@code Config} implementation.
 * Decrypts values obtained from delegate {@code Config}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2020-04
 * @since 1.0
 */
public class EncryptedConfig
implements Config {

    /** The delegate {@code Config} instance. */
    private final @NotNull Config delegate;
    /** The cryptography transformations provider. */
    private final @NotNull ConfigCryptoProvider cryptoProvider;

    /**
     * Creates a new instance.
     * 
     * @param delegate The delegate {@code Config} instance.
     * @param cryptoProvider The cryptography transformations provider.
     */
    public EncryptedConfig(
            final @NotNull Config delegate,
            final @NotNull ConfigCryptoProvider cryptoProvider) {
        super();
        this.delegate = Validate.notNull(delegate);
        this.cryptoProvider = Validate.notNull(cryptoProvider);
    }

    /**
     * Returns the delegate {@code Config} instance.
     * 
     * @return The delegate {@code Config} instance.
     */
    protected @NotNull Config getDelegate() {
        return delegate;
    }

    /**
     * Returns the cryptography transformations provider.
     * 
     * @return The cryptography transformations provider
     */
    protected @NotNull ConfigCryptoProvider getCryptoProvider() {
        return this.cryptoProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(
            final @NotBlank String key)
    throws ConfigException {
        final String cryptValue = this.delegate.get(key);
        if (cryptValue == null) {
            return null;
        }
        return this.cryptoProvider.decrypt(cryptValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.delegate, this.cryptoProvider);
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
        final EncryptedConfig other = (EncryptedConfig) obj;
        return Objects.equals(this.delegate, other.delegate)
                && Objects.equals(this.cryptoProvider, other.cryptoProvider);
    }
}
