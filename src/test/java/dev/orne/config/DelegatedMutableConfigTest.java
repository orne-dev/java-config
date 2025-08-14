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

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests of {@link DelegatedMutableConfig}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class DelegatedMutableConfigTest
extends DelegatedConfigTest {

    @Override
    protected DelegatedMutableConfig createInstance() {
        return new DelegatedMutableConfig(mock(MutableConfig.class));
    }

    /**
     * Test of {@link DelegatedMutableConfig#set(String, String)}.
     */
    @Test
    void testSet() {
        final DelegatedMutableConfig instance = createInstance();
        final MutableConfig delegate = instance.getDelegate();
        instance.set("key", "value");
        then(delegate).should().set("key", "value");
    }

    /**
     * Test of {@link DelegatedMutableConfig#set(String, Boolean)}.
     */
    @Test
    void testSetBoolean() {
        final DelegatedMutableConfig instance = createInstance();
        final MutableConfig delegate = instance.getDelegate();
        instance.set("key", true);
        then(delegate).should().set("key", true);
    }

    /**
     * Test of {@link DelegatedMutableConfig#set(String, Integer)}.
     */
    @Test
    void testSetInteger() {
        final DelegatedMutableConfig instance = createInstance();
        final MutableConfig delegate = instance.getDelegate();
        instance.set("key", 20);
        then(delegate).should().set("key", 20);
    }

    /**
     * Test of {@link DelegatedMutableConfig#set(String, Long)}.
     */
    @Test
    void testSetLong() {
        final DelegatedMutableConfig instance = createInstance();
        final MutableConfig delegate = instance.getDelegate();
        instance.set("key", 20L);
        then(delegate).should().set("key", 20L);
    }

    /**
     * Test of {@link DelegatedMutableConfig#remove(String...)}.
     */
    @Test
    void testRemove() {
        final DelegatedMutableConfig instance = createInstance();
        final MutableConfig delegate = instance.getDelegate();
        instance.remove("key", "anotherKey");
        then(delegate).should().remove("key", "anotherKey");
    }
}
