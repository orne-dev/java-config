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
 * Interface of configuration property values encoders.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0")
@FunctionalInterface
public interface ValueEncoder {

    /** Default identity configuration property value encoder. */
    static ValueEncoder DEFAULT = t -> t;

    /**
     * Encodes the configuration property value.
     *
     * @param value The configuration property value.
     * @return The encoded configuration property value.
     */
    String encode(String value);

    /**
     * Returns a composed encoder that first applies the {@code before} encoder
     * to its input, and then applies this encoder to the result.
     * If evaluation of either encoder throws an exception, it is relayed to
     * the caller of the composed encoder.
     *
     * @param before The encoder to apply before this encoder is applied.
     * @return A composed encoder that first applies the {@code before}
     * encoder and then applies this encoder.
     * @throws NullPointerException If before is null;
     *
     * @see #andThen(ValueEncoder)
     */
    default @NotNull ValueEncoder compose(
            final @NotNull ValueEncoder before) {
        Objects.requireNonNull(before);
        return v -> encode(before.encode(v));
    }

    /**
     * Returns a composed encoder that first applies this encoder to
     * its input, and then applies the {@code after} encoder to the result.
     * If evaluation of either encoder throws an exception, it is relayed to
     * the caller of the composed encoder.
     *
     * @param after The encoder to apply after this encoder is applied.
     * @return A composed encoder that first applies this encoder and then
     * applies the {@code after} encoder.
     * @throws NullPointerException If after is null.
     *
     * @see #compose(ValueEncoder)
     */
    default @NotNull ValueEncoder andThen(
            final @NotNull ValueEncoder after) {
        Objects.requireNonNull(after);
        return v -> after.encode(encode(v));
    }
}
