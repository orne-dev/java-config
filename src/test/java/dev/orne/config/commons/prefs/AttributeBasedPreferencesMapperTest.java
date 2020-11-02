package dev.orne.config.commons.prefs;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2021 Orne Developments
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
import static org.mockito.BDDMockito.*;

import java.util.prefs.Preferences;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code AttributeBasedPreferencesMapper}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see AttributeBasedPreferencesMapper
 */
@Tag("ut")
class AttributeBasedPreferencesMapperTest {

    /**
     * Test for {@link AttributeBasedPreferencesMapper#getValue(Preferences)}.
     */
    @Test
    void testGetValue() {
        final AttributeBasedPreferencesMapper mapper = spy(AttributeBasedPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        
        final String result = mapper.getValue(node);
        assertNull(result);
        
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AttributeBasedPreferencesMapper#setValue(Preferences, String)}.
     */
    @Test
    void testSetValue() {
        final AttributeBasedPreferencesMapper mapper = spy(AttributeBasedPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        final String value = "mockValue";
        
        assertThrows(UnsupportedOperationException.class, () -> {
            mapper.setValue(node, value);
        });
        
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AttributeBasedPreferencesMapper#setValue(Preferences, String)}.
     */
    @Test
    void testSetValue_Null() {
        final AttributeBasedPreferencesMapper mapper = spy(AttributeBasedPreferencesMapper.class);
        final Preferences node = mock(Preferences.class);
        
        mapper.setValue(node, null);
        
        then(node).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AttributeBasedPreferencesMapper#getChildValue(Preferences, String)}.
     */
    @Test
    void testGetChildValue() {
        final AttributeBasedPreferencesMapper mapper = spy(AttributeBasedPreferencesMapper.class);
        final Preferences parent = mock(Preferences.class);
        final String name = "mockName";
        
        final String result = mapper.getChildValue(parent, name);
        assertNull(result);
        
        then(parent).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AttributeBasedPreferencesMapper#setChildValue(Preferences, String, String)}.
     */
    @Test
    void testSetChildValue() {
        final AttributeBasedPreferencesMapper mapper = spy(AttributeBasedPreferencesMapper.class);
        final Preferences parent = mock(Preferences.class);
        final String name = "mockName";
        final String value = "mockValue";
        
        assertThrows(UnsupportedOperationException.class, () -> {
            mapper.setChildValue(parent, name, value);
        });
        
        then(parent).shouldHaveNoInteractions();
    }

    /**
     * Test for {@link AttributeBasedPreferencesMapper#setChildValue(Preferences, String, String)}.
     */
    @Test
    void testSetChildValue_Null() {
        final AttributeBasedPreferencesMapper mapper = spy(AttributeBasedPreferencesMapper.class);
        final Preferences parent = mock(Preferences.class);
        final String name = "mockName";
        
        mapper.setChildValue(parent, name, null);
        
        then(parent).shouldHaveNoInteractions();
    }
}
