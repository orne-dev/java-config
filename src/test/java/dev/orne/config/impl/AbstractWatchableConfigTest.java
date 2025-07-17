package dev.orne.config.impl;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2025 Orne Developments
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
import static org.awaitility.Awaitility.*;
import static org.mockito.BDDMockito.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import dev.orne.config.MutableConfig;
import dev.orne.config.WatchableConfig;

/**
 * Basic unit tests for {@code AbstractWatchableConfig} subtypes.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
abstract class AbstractWatchableConfigTest
extends AbstractMutableConfigTest {

    protected @Mock WatchableConfig.Listener mockListener;
    protected final Duration maxDelay = Duration.ofMillis(maxEventsDelay());
    protected @Captor ArgumentCaptor<Set<String>> changedPropertiesCaptor;

    /**
     * Returns the maximum allowed property change events delay.
     * 
     * @return the maximum allowed delay in milliseconds.
     */
    protected int maxEventsDelay() {
        return 1000;
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    void testEmpty() {
        super.testEmpty();
        final AbstractWatchableConfig config = assertInstanceOf(AbstractWatchableConfig.class, createBuilder()
                .build());
        assertNotNull(config.getEvents());
        assertTrue(config.getEvents().getListeners().isEmpty());
        config.addListener(mockListener);
        assertFalse(config.getEvents().getListeners().isEmpty());
        assertTrue(config.getEvents().getListeners().contains(mockListener));
        config.removeListener(mockListener);
        assertTrue(config.getEvents().getListeners().isEmpty());
        then(mockListener).shouldHaveNoInteractions();
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    void testSet() {
        super.testSet();
        final HashMap<String, String> properties = new HashMap<>();
        final AbstractWatchableConfig config = assertInstanceOf(AbstractWatchableConfig.class,
                createBuilder(properties)
                    .build());
        config.addListener(mockListener);
        config.set(TEST_KEY, "newValue");
        assertEventsFired(config, TEST_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    void testSetBoolean() {
        super.testSetBoolean();
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        final AbstractWatchableConfig config = assertInstanceOf(AbstractWatchableConfig.class,
                createBuilder(properties)
                    .build());
        config.addListener(mockListener);
        config.set(TEST_KEY, true);
        assertEventsFired(config, TEST_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    void testSetInteger() {
        super.testSetInteger();
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        final AbstractWatchableConfig config = assertInstanceOf(AbstractWatchableConfig.class,
                createBuilder(properties)
                    .build());
        config.addListener(mockListener);
        config.set(TEST_KEY, 1000);
        assertEventsFired(config, TEST_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    void testSetLong() {
        super.testSetLong();
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        final AbstractWatchableConfig config = assertInstanceOf(AbstractWatchableConfig.class,
                createBuilder(properties)
                    .build());
        config.addListener(mockListener);
        config.set(TEST_KEY, 1000L);
        assertEventsFired(config, TEST_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    void testSetNull() {
        super.testSetNull();
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        final AbstractWatchableConfig config = assertInstanceOf(AbstractWatchableConfig.class,
                createBuilder(properties)
                    .build());
        config.addListener(mockListener);
        config.set(TEST_KEY, (String) null);
        assertEventsFired(config, TEST_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    void testRemove() {
        super.testRemove();
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        final AbstractWatchableConfig config = assertInstanceOf(AbstractWatchableConfig.class,
                createBuilder(properties)
                    .build());
        config.addListener(mockListener);
        config.remove(TEST_KEY);
        assertEventsFired(config, TEST_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Test
    @Override
    void testRemoveMultiple() {
        super.testRemoveMultiple();
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        final AbstractWatchableConfig config = assertInstanceOf(AbstractWatchableConfig.class,
                createBuilder(properties)
                    .build());
        config.addListener(mockListener);
        config.remove(TEST_KEY, TEST_DERIVED_KEY);
        assertEventsFired(config, TEST_KEY, TEST_DERIVED_KEY);
    }

    /**
     * Tests properties setting.
     */
    @Test
    void testWatchableParent() {
        final HashMap<String, String> parentProperties = new HashMap<>();
        parentProperties.put(TEST_PARENT_KEY, "testParentValue");
        parentProperties.put(TEST_PARENT_DERIVED_KEY, "Derived parent value: ${" + TEST_KEY + "}");
        final AbstractWatchableConfig parent = assertInstanceOf(AbstractWatchableConfig.class,
                createBuilder(parentProperties)
                    .build());
        final HashMap<String, String> properties = new HashMap<>();
        properties.put(TEST_KEY, "testValue");
        properties.put(TEST_DERIVED_KEY, "Derived value: ${" + TEST_PARENT_KEY + "}");
        final AbstractWatchableConfig config = assertInstanceOf(AbstractWatchableConfig.class,
                createBuilder(properties)
                    .withParent(parent)
                    .withVariableResolution()
                    .build());
        config.addListener(mockListener);
        assertEquals("testValue", config.get(TEST_KEY));
        assertEquals("Derived value: testParentValue", config.get(TEST_DERIVED_KEY));
        assertEquals("testParentValue", config.get(TEST_PARENT_KEY));
        assertEquals("Derived parent value: testValue", config.get(TEST_PARENT_DERIVED_KEY));
        parent.set(TEST_PARENT_KEY, "newParentValue");
        assertEventsFired(config, TEST_PARENT_KEY);
        assertEquals("testValue", config.get(TEST_KEY));
        assertEquals("Derived value: newParentValue", config.get(TEST_DERIVED_KEY));
        assertEquals("newParentValue", config.get(TEST_PARENT_KEY));
        assertEquals("Derived parent value: testValue", config.get(TEST_PARENT_DERIVED_KEY));
        config.set(TEST_KEY, "newValue");
        assertEventsFired(config, TEST_KEY);
        assertEquals("newValue", config.get(TEST_KEY));
        assertEquals("Derived value: newParentValue", config.get(TEST_DERIVED_KEY));
        assertEquals("newParentValue", config.get(TEST_PARENT_KEY));
        assertEquals("Derived parent value: newValue", config.get(TEST_PARENT_DERIVED_KEY));
    }

    /**
     * Asserts that the listener has received configuration properties change
     * events regarding the specified instance for, at least, the specified
     * properties.
     * 
     * @param instance The configuration instance of the events.
     * @param properties The expected property changes.
     */
    protected void assertEventsFired(
            final MutableConfig instance,
            final String... properties) {
        await()
        .atMost(maxDelay)
        .untilAsserted(() -> {
            then(mockListener).should(atLeastOnce()).configurationChanged(
                    same(instance),
                    changedPropertiesCaptor.capture());
            final Set<String> props = changedPropertiesCaptor.getAllValues()
                    .stream()
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
            for (final String property : properties) {
                assertTrue(props.contains(property), () -> String.format(
                        "Property %s not contained in fired events: %s",
                        property,
                        props));
            }
        });
    }
}
