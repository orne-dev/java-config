package dev.orne.config;

import java.math.BigDecimal;

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

import org.apache.commons.lang3.Validate;

/**
 * Encrypted {@code Config} implementation. Decrypts values obtained from
 * delegate {@code Config}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-04
 * @since 0.2
 */
public class EncryptedConfig
extends DelegatedConfig {

    /** The cryptography transformations provider. */
    private final ConfigCryptoProvider cryptoProvider;

    /**
     * Creates a new instance.
     * 
     * @param delegate The cryptography transformations provider
     * @param cryptoProvider The provider of cryptography transformations
     */
    public EncryptedConfig(
            @NotNull
            final Config delegate,
            @NotNull
            final ConfigCryptoProvider cryptoProvider) {
        super(delegate);
        this.cryptoProvider = Validate.notNull(cryptoProvider);
    }

    /**
     * Returns the cryptography transformations provider.
     * 
     * @return The cryptography transformations provider
     */
    protected ConfigCryptoProvider getCryptoProvider() {
        return this.cryptoProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected <T> T getParameter(
            @NotBlank
            final String key,
            @NotNull
            final Class<T> type)
    throws ConfigException {
        final String cryptValue = super.getStringParameter(key);
        final String strValue = this.cryptoProvider.decrypt(cryptValue);
        return this.convertValue(strValue, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getStringParameter(
            @NotBlank
            final String key)
    throws ConfigException {
        final String cryptValue = super.getStringParameter(key);
        return this.cryptoProvider.decrypt(cryptValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean getBooleanParameter(
            @NotBlank
            final String key)
    throws ConfigException {
        return getParameter(key, Boolean.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Number getNumberParameter(
            @NotBlank
            final String key)
    throws ConfigException {
        return getParameter(key, BigDecimal.class);
    }
}
