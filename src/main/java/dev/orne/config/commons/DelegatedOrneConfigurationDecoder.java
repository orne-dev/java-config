package dev.orne.config.commons;

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

import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.ConfigurationDecoder;
import org.apache.commons.configuration2.ex.ConfigurationRuntimeException;

import dev.orne.config.crypto.ConfigCryptoProvider;
import dev.orne.config.crypto.ConfigCryptoProviderException;

/**
 * {@code ConfigCryptoProvider} based implementation of Apache Commons
 * {@code ConfigurationDecoder}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2020-10
 * @since 0.2
 * @see ConfigCryptoProvider
 */
public class DelegatedOrneConfigurationDecoder
implements ConfigurationDecoder {

    /** The cryptography transformations provider. */
    private final @NotNull ConfigCryptoProvider provider;

    /**
     * Creates a new instance.
     * 
     * @param provider The cryptography transformations provider
     */
    public DelegatedOrneConfigurationDecoder(
            final @NotNull ConfigCryptoProvider provider) {
        super();
        this.provider = Objects.requireNonNull(provider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String decode(final @NotNull String value) {
        try {
            return this.provider.decrypt(value);
        } catch (final ConfigCryptoProviderException ccpe) {
            throw new ConfigurationRuntimeException(ccpe);
        }
    }

    /**
     * Returns the cryptography transformations provider.
     * 
     * @return The cryptography transformations provider
     */
    protected @NotNull ConfigCryptoProvider getProvider() {
        return this.provider;
    }
}
