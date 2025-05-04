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
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Delegated {@code HierarchicalConfig} implementation.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-04
 * @since 0.2
 */
@API(status = API.Status.STABLE, since = "1.0")
public class DelegatedHierarchicalConfig
extends DelegatedConfig
implements HierarchicalConfig {

    /** The parent configuration. */
    private final @NotNull Config parent;

    /**
     * Creates a new instance.
     * 
     * @param delegate The delegate {@code Config} instance
     * @param parent The parent {@code Config} instance
     */
    public DelegatedHierarchicalConfig(
            final @NotNull Config delegate,
            final @NotNull Config parent) {
        super(delegate);
        this.parent = Objects.requireNonNull(parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Config getParent() {
        return this.parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return super.isEmpty() && this.parent.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(
            final @NotBlank String key) {
        return super.contains(key) || this.parent.contains(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Stream<String> getKeys() {
        return Stream.concat(super.getKeys(), this.parent.getKeys());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(
            final @NotBlank String key) {
        if (super.contains(key)) {
            return super.get(key);
        } else {
            return this.parent.get(key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(
            final @NotBlank String key,
            final String defaultValue) {
        if (super.contains(key)) {
            return super.get(key, defaultValue);
        } else {
            return this.parent.get(key, defaultValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(
            final @NotBlank String key,
            final @NotNull Supplier<String> defaultValue) {
        if (super.contains(key)) {
            return super.get(key, defaultValue);
        } else {
            return this.parent.get(key, defaultValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBoolean(
            final @NotBlank String key) {
        if (super.contains(key)) {
            return super.getBoolean(key);
        } else {
            return this.parent.getBoolean(key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(
            final @NotBlank String key,
            final boolean defaultValue) {
        if (super.contains(key)) {
            return super.getBoolean(key, defaultValue);
        } else {
            return this.parent.getBoolean(key, defaultValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBoolean(
            final @NotBlank String key,
            final @NotNull Supplier<Boolean> defaultValue) {
        if (super.contains(key)) {
            return super.getBoolean(key, defaultValue);
        } else {
            return this.parent.getBoolean(key, defaultValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getInteger(
            final @NotNull String key) {
        if (super.contains(key)) {
            return super.getInteger(key);
        } else {
            return this.parent.getInteger(key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInteger(
            final @NotNull String key,
            final int defaultValue) {
        if (super.contains(key)) {
            return super.getInteger(key, defaultValue);
        } else {
            return this.parent.getInteger(key, defaultValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getInteger(
            final @NotBlank String key,
            final @NotNull Supplier<Integer> defaultValue) {
        if (super.contains(key)) {
            return super.getInteger(key, defaultValue);
        } else {
            return this.parent.getInteger(key, defaultValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getLong(
            final @NotNull String key) {
        if (super.contains(key)) {
            return super.getLong(key);
        } else {
            return this.parent.getLong(key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(
            final @NotNull String key,
            final long defaultValue) {
        if (super.contains(key)) {
            return super.getLong(key, defaultValue);
        } else {
            return this.parent.getLong(key, defaultValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getLong(
            final @NotBlank String key,
            final @NotNull Supplier<Long> defaultValue) {
        if (super.contains(key)) {
            return super.getLong(key, defaultValue);
        } else {
            return this.parent.getLong(key, defaultValue);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(getDelegate(), this.parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(
            final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DelegatedHierarchicalConfig other = (DelegatedHierarchicalConfig) obj;
        return super.equals(obj) &&
                Objects.equals(this.parent, other.parent);
    }
}
