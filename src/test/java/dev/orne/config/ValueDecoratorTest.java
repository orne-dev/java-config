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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests of {@link ValueDecorator} default methods.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class ValueDecoratorTest {

    /**
     * Tests for default {@link ValueDecorator#andThen(ValueDecorator)} implementation.
     */
    @Test
    void testAndThen() {
        final ValueDecorator decoder = value -> {
            return "Decorated[" + value + "]";
        };
        assertThrows(NullPointerException.class, () -> decoder.andThen(null));
        final ValueDecorator result = decoder.andThen(value -> {
            return "Final[" + value + "]";
        });
        assertEquals("Final[Decorated[test]]", result.decorate("test"));
    }

    /**
     * Tests for default {@link ValueDecorator#compose(ValueDecorator)} implementation.
     */
    @Test
    void testCompose() {
        final ValueDecorator decoder = value -> {
            return "Decorated[" + value + "]";
        };
        assertThrows(NullPointerException.class, () -> decoder.compose(null));
        final ValueDecorator result = decoder.compose(value -> {
            return "Initial[" + value + "]";
        });
        assertEquals("Decorated[Initial[test]]", result.decorate("test"));
    }
}
