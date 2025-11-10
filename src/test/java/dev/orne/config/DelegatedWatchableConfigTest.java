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
import static org.awaitility.Awaitility.*;
import static org.mockito.BDDMockito.*;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests of {@link DelegatedWatchableConfig}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.1
 */
@Tag("ut")
class DelegatedWatchableConfigTest
extends DelegatedMutableConfigTest {

    protected @Mock WatchableConfig.Listener mockListener;
    protected final Duration maxDelay = Duration.ofMillis(1000);
    protected @Captor ArgumentCaptor<Set<String>> changedPropertiesCaptor;
    protected @Mock WatchableConfig.Listener delegateListener;

    /**
     * Initializes the mocks used in the tests.
     */
    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Override
    protected DelegatedWatchableConfig createInstance() {
        return new DelegatedWatchableConfig(mock(WatchableConfig.class));
    }

    /**
     * Test of {@link DelegatedWatchableConfig#set(String, String)}.
     */
    @Test
    void testSetEvent() {
        final DelegatedWatchableConfig instance = new DelegatedWatchableConfig(
                Config.fromProperties()
                    .mutable()
                    .build());
        instance.addListener(mockListener);
        instance.set("key", "newValue");
        assertEventsFired(mockListener, instance, "key");
    }

    /**
     * Test of {@link DelegatedWatchableConfig#set(String, Boolean)}.
     */
    @Test
    void testSetBooleanEvent() {
        final DelegatedWatchableConfig instance = new DelegatedWatchableConfig(
                Config.fromProperties()
                    .mutable()
                    .build());
        instance.addListener(mockListener);
        instance.set("key", true);
        assertEventsFired(mockListener, instance, "key");
    }

    /**
     * Test of {@link DelegatedWatchableConfig#set(String, Integer)}.
     */
    @Test
    void testSetIntegerEvent() {
        final DelegatedWatchableConfig instance = new DelegatedWatchableConfig(
                Config.fromProperties()
                    .mutable()
                    .build());
        instance.addListener(mockListener);
        instance.set("key", 20);
        assertEventsFired(mockListener, instance, "key");
    }

    /**
     * Test of {@link DelegatedWatchableConfig#set(String, Long)}.
     */
    @Test
    void testSetLongEvent() {
        final DelegatedWatchableConfig instance = new DelegatedWatchableConfig(
                Config.fromProperties()
                    .mutable()
                    .build());
        instance.addListener(mockListener);
        instance.set("key", 20L);
        assertEventsFired(mockListener, instance, "key");
    }

    /**
     * Test of {@link DelegatedWatchableConfig#remove(String...)}.
     */
    @Test
    void testRemoveEvent() {
        final DelegatedWatchableConfig instance = new DelegatedWatchableConfig(
                Config.fromProperties()
                    .mutable()
                    .build());
        instance.addListener(mockListener);
        instance.remove("key", "anotherKey");
        assertEventsFired(mockListener, instance, "key", "anotherKey");
    }

    /**
     * Test of {@link DelegatedWatchableConfig#removeListener(WatchableConfig.Listener)}.
     */
    @Test
    void testRemoveListener() {
        final WatchableConfig delegate = Config.fromProperties()
                .mutable()
                .build();
        delegate.addListener(delegateListener);
        final DelegatedWatchableConfig instance = new DelegatedWatchableConfig(delegate);
        instance.addListener(mockListener);
        delegate.set("key", "newValue");
        instance.removeListener(mockListener);
        instance.set("anotherKey", "anotherValue");
        assertEventsFired(mockListener, instance, "key");
        assertEventsFired(delegateListener, delegate, "key", "anotherKey");
    }

    /**
     * Asserts that the listener has received configuration properties change
     * events regarding the specified instance for, at least, the specified
     * properties.
     * 
     * @param listener The listener to check.
     * @param instance The configuration instance of the events.
     * @param properties The expected property changes.
     */
    protected void assertEventsFired(
            final WatchableConfig.Listener listener,
            final MutableConfig instance,
            final String... properties) {
        await()
        .atMost(maxDelay)
        .untilAsserted(() -> {
            then(listener).should(atLeastOnce()).configurationChanged(
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
