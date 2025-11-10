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

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Interface of configuration property values decorators.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0")
@FunctionalInterface
public interface ValueDecorator {

    /** Default identity configuration property value decorator. */
    static ValueDecorator DEFAULT = t -> t;

    /**
     * Decorates the configuration property value.
     *
     * @param value The configuration property value.
     * @return The decorated configuration property value.
     */
    String decorate(String value);

    /**
     * Returns a composed decorator that first applies the {@code before}
     * decorator to its input, and then applies this decorator to the result.
     * If evaluation of either decorator throws an exception, it is relayed to
     * the caller of the composed decorator.
     *
     * @param before The decorator to apply before this decorator is applied.
     * @return A composed decorator that first applies the {@code before}
     * decorator and then applies this decorator.
     * @throws NullPointerException If before is null;
     *
     * @see #andThen(ValueDecorator)
     */
    default @NotNull ValueDecorator compose(
            final @NotNull ValueDecorator before) {
        Objects.requireNonNull(before);
        return v -> decorate(before.decorate(v));
    }

    /**
     * Returns a composed decorator that first applies this decorator to
     * its input, and then applies the {@code after} decorator to the result.
     * If evaluation of either decorator throws an exception, it is relayed to
     * the caller of the composed decorator.
     *
     * @param after The decorator to apply after this decorator is applied.
     * @return A composed decorator that first applies this decorator and then
     * applies the {@code after} decorator.
     * @throws NullPointerException If after is null.
     *
     * @see #compose(ValueDecorator)
     */
    default @NotNull ValueDecorator andThen(
            final @NotNull ValueDecorator after) {
        Objects.requireNonNull(after);
        return v -> after.decorate(decorate(v));
    }
}
