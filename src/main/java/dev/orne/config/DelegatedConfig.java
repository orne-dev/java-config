package dev.orne.config;

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

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;

/**
 * Delegated {@code Config} implementation.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-04
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0")
public class DelegatedConfig
implements Config {

    /** The delegate {@code Config} instance. */
    private final @NotNull Config delegate;

    /**
     * Creates a new instance.
     * 
     * @param delegate The delegate {@code Config} instance.
     */
    public DelegatedConfig(
            final @NotNull Config delegate) {
        super();
        this.delegate = Validate.notNull(delegate);
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
     * {@inheritDoc}
     */
    @Override
    public boolean contains(
            final @NotBlank String key) {
        return this.delegate.contains(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Iterable<String> getKeys() {
        return this.delegate.getKeys();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Stream<String> getKeys(
            final @NotNull Predicate<String> filter) {
        return this.delegate.getKeys(filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Stream<String> getKeys(
            final @NotNull String prefix) {
        return this.delegate.getKeys(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(
            final @NotBlank String key) {
        return this.delegate.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(
            final @NotBlank String key,
            final String defaultValue) {
        return this.delegate.get(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(
            final @NotBlank String key,
            final @NotNull Supplier<String> defaultValue) {
        return this.delegate.get(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBoolean(
            final @NotBlank String key) {
        return this.delegate.getBoolean(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(
            final @NotBlank String key,
            final boolean defaultValue) {
        return this.delegate.getBoolean(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBoolean(
            final @NotBlank String key,
            final @NotNull Supplier<Boolean> defaultValue) {
        return this.delegate.getBoolean(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getInteger(
            final @NotNull String key) {
        return this.delegate.getInteger(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInteger(
            final @NotNull String key,
            final int defaultValue) {
        return this.delegate.getInteger(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getInteger(
            final @NotBlank String key,
            final @NotNull Supplier<Integer> defaultValue) {
        return this.delegate.getInteger(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getLong(
            final @NotNull String key) {
        return this.delegate.getLong(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(
            final @NotNull String key,
            final long defaultValue) {
        return this.delegate.getLong(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getLong(
            final @NotBlank String key,
            final @NotNull Supplier<Long> defaultValue) {
        return this.delegate.getLong(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.delegate);
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
        final DelegatedConfig other = (DelegatedConfig) obj;
        return Objects.equals(this.delegate, other.delegate);
    }
}
