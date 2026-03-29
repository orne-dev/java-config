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

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

/**
 * A configuration that delegates all operations to another configuration.
 * This class is useful for creating a proxy or wrapper around an existing
 * configuration instance, allowing for additional behavior or modifications
 * without changing the original configuration.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-08
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0")
public class DelegatedConfig
implements Config {

    /** The configuration to delegate to. */
    private final Config delegate;

    /**
     * Creates a new instance.
     *
     * @param delegate The configuration to delegate to.
     */
    public DelegatedConfig(
            final Config delegate) {
        super();
        this.delegate = Objects.requireNonNull(delegate);
    }

    /**
     * Returns the delegate configuration.
     *
     * @return The delegate configuration.
     */
    protected Config getDelegate() {
        return this.delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable Config getParent() {
        return this.delegate.getParent();
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
    public boolean contains(
            final String key) {
        return this.delegate.contains(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<String> getKeys() {
        return this.delegate.getKeys();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<String> getKeys(
            final Predicate<String> filter) {
        return this.delegate.getKeys(filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<String> getKeys(
            final String prefix) {
        return this.delegate.getKeys(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(
            final String key) {
        return this.delegate.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable String getUndecored(
            final String key) {
        return this.delegate.getUndecored(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable String get(
            final String key,
            final @Nullable String defaultValue) {
        return this.delegate.get(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable String get(
            final String key,
            final Supplier<@Nullable String> defaultValue) {
        return this.delegate.get(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable Boolean getBoolean(
            final String key) {
        return this.delegate.getBoolean(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(
            final String key,
            final boolean defaultValue) {
        return this.delegate.getBoolean(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable Boolean getBoolean(
            final String key,
            final Supplier<@Nullable Boolean> defaultValue) {
        return this.delegate.getBoolean(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable Integer getInteger(
            final String key) {
        return this.delegate.getInteger(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInteger(
            final String key,
            final int defaultValue) {
        return this.delegate.getInteger(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable Integer getInteger(
            final String key,
            final Supplier<@Nullable Integer> defaultValue) {
        return this.delegate.getInteger(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable Long getLong(
            final String key) {
        return this.delegate.getLong(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(
            final String key,
            final long defaultValue) {
        return this.delegate.getLong(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @Nullable Long getLong(
            final String key,
            final Supplier<@Nullable Long> defaultValue) {
        return this.delegate.getLong(key, defaultValue);
    }
}
