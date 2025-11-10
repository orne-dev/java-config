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
 * Interface of configuration property values decoders.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0")
@FunctionalInterface
public interface ValueDecoder {

    /** Default identity configuration property value decoder. */
    static ValueDecoder DEFAULT = t -> t;

    /**
     * Decodes the configuration property value.
     *
     * @param value The configuration property value.
     * @return The decoded configuration property value.
     */
    String decode(String value);

    /**
     * Returns a composed decoder that first applies the {@code before} decoder
     * to its input, and then applies this decoder to the result.
     * If evaluation of either decoder throws an exception, it is relayed to
     * the caller of the composed decoder.
     *
     * @param before The decoder to apply before this decoder is applied.
     * @return A composed decoder that first applies the {@code before}
     * decoder and then applies this decoder.
     * @throws NullPointerException If before is null;
     *
     * @see #andThen(ValueDecoder)
     */
    default @NotNull ValueDecoder compose(
            final @NotNull ValueDecoder before) {
        Objects.requireNonNull(before);
        return v -> decode(before.decode(v));
    }

    /**
     * Returns a composed decoder that first applies this decoder to
     * its input, and then applies the {@code after} decoder to the result.
     * If evaluation of either decoder throws an exception, it is relayed to
     * the caller of the composed decoder.
     *
     * @param after The decoder to apply after this decoder is applied.
     * @return A composed decoder that first applies this decoder and then
     * applies the {@code after} decoder.
     * @throws NullPointerException If after is null.
     *
     * @see #compose(ValueDecoder)
     */
    default @NotNull ValueDecoder andThen(
            final @NotNull ValueDecoder after) {
        Objects.requireNonNull(after);
        return v -> after.decode(decode(v));
    }
}
