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

import java.util.Iterator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;

/**
 * Delegated {@code Config} implementation.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-04
 * @since 0.2
 */
public class DelegatedConfig
extends AbstractConfig {

    /** The delegate {@code Config} instance. */
    private final @NotNull Config delegate;

    /**
     * Creates a new instance.
     * 
     * @param delegate The delegate {@code Config} instance
     */
    public DelegatedConfig(
            final @NotNull Config delegate) {
        super();
        this.delegate = Validate.notNull(delegate);
    }

    /**
     * Returns the delegate {@code Config} instance.
     * 
     * @return The delegate {@code Config} instance
     */
    @NotNull
    protected @NotNull Config getDelegate() {
        return delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty()
    throws ConfigException {
        return this.delegate.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<String> getKeys()
    throws ConfigException {
        return this.delegate.getKeys();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean containsParameter(
            final @NotBlank String key)
    throws ConfigException {
        return this.delegate.contains(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected <T> T getParameter(
            final @NotBlank String key,
            final @NotNull Class<T> type)
    throws ConfigException {
        return this.delegate.get(key, type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean getBooleanParameter(
            final @NotBlank String key)
    throws ConfigException {
        return this.delegate.getBoolean(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getStringParameter(
            final @NotBlank String key)
    throws ConfigException {
        return this.delegate.getString(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Number getNumberParameter(
            final @NotBlank String key)
    throws ConfigException {
        return this.delegate.getNumber(key);
    }
}
