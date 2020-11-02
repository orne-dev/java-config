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

import org.apache.commons.lang3.function.FailableRunnable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.commons.prefs.EventCoordinationStrategy.EventHandler;

/**
 * Unit tests for {@code ByThreadEventCoordinationStrategy}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see ByThreadEventCoordinationStrategy
 */
@Tag("ut")
class ByThreadEventsCoordinatorTest {

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#ByThreadEventsCoordinator()}.
     */
    @Test
    void testConstructor() {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#startPreferencesPrevention()}.
     */
    @Test
    void testStartPreferencesPrevention() {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        
        strategy.startPreferencesPrevention();
        assertTrue(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#finishPreferencesPrevention()}.
     */
    @Test
    void testFinishPreferencesPrevention() {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        
        strategy.startPreferencesPrevention();
        assertTrue(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
        strategy.finishPreferencesPrevention();
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#finishPreferencesPrevention()}.
     */
    @Test
    void testFinishPreferencesPrevention_Multiple() {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        
        strategy.startPreferencesPrevention();
        assertTrue(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
        strategy.startPreferencesPrevention();
        assertTrue(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
        strategy.finishPreferencesPrevention();
        assertTrue(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
        strategy.finishPreferencesPrevention();
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#startConfigurationPrevention()}.
     */
    @Test
    void testStartConfigurationPrevention() {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        
        strategy.startConfigurationPrevention();
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertTrue(strategy.areConfigurationEventsPrevented());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#finishConfigurationPrevention()}.
     */
    @Test
    void testFinishConfigurationPrevention() {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        
        strategy.startConfigurationPrevention();
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertTrue(strategy.areConfigurationEventsPrevented());
        strategy.finishConfigurationPrevention();
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#finishConfigurationPrevention()}.
     */
    @Test
    void testFinishConfigurationPrevention_Multiple() {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        
        strategy.startConfigurationPrevention();
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertTrue(strategy.areConfigurationEventsPrevented());
        strategy.startConfigurationPrevention();
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertTrue(strategy.areConfigurationEventsPrevented());
        strategy.finishConfigurationPrevention();
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertTrue(strategy.areConfigurationEventsPrevented());
        strategy.finishConfigurationPrevention();
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#preventEvents(FailableRunnable)}.
     */
    @Test
    void testPreventEvents() {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        
        strategy.preventEvents(() -> {
            assertTrue(strategy.arePreferencesEventsPrevented());
            assertTrue(strategy.areConfigurationEventsPrevented());
        });
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#preventEvents(FailableRunnable)}.
     */
    @Test
    void testPreventEvents_Error() {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        final Exception mockEx = new Exception();
        
        final Exception result = assertThrows(Exception.class, () -> {
            strategy.preventEvents(() -> {
                assertTrue(strategy.arePreferencesEventsPrevented());
                assertTrue(strategy.areConfigurationEventsPrevented());
                throw mockEx;
            });
        });
        assertSame(mockEx, result);
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#preventEvents(FailableRunnable)}.
     */
    @Test
    void testPreventEvents_Multiple() {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        
        strategy.preventEvents(() -> {
            assertTrue(strategy.arePreferencesEventsPrevented());
            assertTrue(strategy.areConfigurationEventsPrevented());
            strategy.preventEvents(() -> {
                assertTrue(strategy.arePreferencesEventsPrevented());
                assertTrue(strategy.areConfigurationEventsPrevented());
            });
            assertTrue(strategy.arePreferencesEventsPrevented());
            assertTrue(strategy.areConfigurationEventsPrevented());
            strategy.preventPreferencesEvents(() -> {
                assertTrue(strategy.arePreferencesEventsPrevented());
                assertTrue(strategy.areConfigurationEventsPrevented());
            });
            assertTrue(strategy.arePreferencesEventsPrevented());
            assertTrue(strategy.areConfigurationEventsPrevented());
            strategy.preventConfigurationEvents(() -> {
                assertTrue(strategy.arePreferencesEventsPrevented());
                assertTrue(strategy.areConfigurationEventsPrevented());
            });
            assertTrue(strategy.arePreferencesEventsPrevented());
            assertTrue(strategy.areConfigurationEventsPrevented());
        });
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#preventPreferencesEvents(FailableRunnable)}.
     */
    @Test
    void testPreventPreferencesEvents() {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        
        strategy.preventPreferencesEvents(() -> {
            assertTrue(strategy.arePreferencesEventsPrevented());
            assertFalse(strategy.areConfigurationEventsPrevented());
        });
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#preventPreferencesEvents(FailableRunnable)}.
     */
    @Test
    void testPreventPreferencesEvents_Error() {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        final Exception mockEx = new Exception();
        
        final Exception result = assertThrows(Exception.class, () -> {
            strategy.preventPreferencesEvents(() -> {
                assertTrue(strategy.arePreferencesEventsPrevented());
                assertFalse(strategy.areConfigurationEventsPrevented());
                throw mockEx;
            });
        });
        assertSame(mockEx, result);
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#preventPreferencesEvents(FailableRunnable)}.
     */
    @Test
    void testPreventPreferencesEvents_Multiple() {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        
        strategy.preventPreferencesEvents(() -> {
            assertTrue(strategy.arePreferencesEventsPrevented());
            assertFalse(strategy.areConfigurationEventsPrevented());
            strategy.preventEvents(() -> {
                assertTrue(strategy.arePreferencesEventsPrevented());
                assertTrue(strategy.areConfigurationEventsPrevented());
            });
            assertTrue(strategy.arePreferencesEventsPrevented());
            assertFalse(strategy.areConfigurationEventsPrevented());
            strategy.preventPreferencesEvents(() -> {
                assertTrue(strategy.arePreferencesEventsPrevented());
                assertFalse(strategy.areConfigurationEventsPrevented());
            });
            assertTrue(strategy.arePreferencesEventsPrevented());
            assertFalse(strategy.areConfigurationEventsPrevented());
            strategy.preventConfigurationEvents(() -> {
                assertTrue(strategy.arePreferencesEventsPrevented());
                assertTrue(strategy.areConfigurationEventsPrevented());
            });
            assertTrue(strategy.arePreferencesEventsPrevented());
            assertFalse(strategy.areConfigurationEventsPrevented());
        });
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#preventConfigurationEvents(FailableRunnable)}.
     */
    @Test
    void testPreventConfigurationEvents() {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        
        strategy.preventConfigurationEvents(() -> {
            assertFalse(strategy.arePreferencesEventsPrevented());
            assertTrue(strategy.areConfigurationEventsPrevented());
        });
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#preventConfigurationEvents(FailableRunnable)}.
     */
    @Test
    void testPreventConfigurationEvents_Error() {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        final Exception mockEx = new Exception();
        
        final Exception result = assertThrows(Exception.class, () -> {
            strategy.preventConfigurationEvents(() -> {
                assertFalse(strategy.arePreferencesEventsPrevented());
                assertTrue(strategy.areConfigurationEventsPrevented());
                throw mockEx;
            });
        });
        assertSame(mockEx, result);
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#preventConfigurationEvents(FailableRunnable)}.
     */
    @Test
    void testPreventConfigurationEvents_Multiple() {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        
        strategy.preventConfigurationEvents(() -> {
            assertFalse(strategy.arePreferencesEventsPrevented());
            assertTrue(strategy.areConfigurationEventsPrevented());
            strategy.preventEvents(() -> {
                assertTrue(strategy.arePreferencesEventsPrevented());
                assertTrue(strategy.areConfigurationEventsPrevented());
            });
            assertFalse(strategy.arePreferencesEventsPrevented());
            assertTrue(strategy.areConfigurationEventsPrevented());
            strategy.preventPreferencesEvents(() -> {
                assertTrue(strategy.arePreferencesEventsPrevented());
                assertTrue(strategy.areConfigurationEventsPrevented());
            });
            assertFalse(strategy.arePreferencesEventsPrevented());
            assertTrue(strategy.areConfigurationEventsPrevented());
            strategy.preventConfigurationEvents(() -> {
                assertFalse(strategy.arePreferencesEventsPrevented());
                assertTrue(strategy.areConfigurationEventsPrevented());
            });
            assertFalse(strategy.arePreferencesEventsPrevented());
            assertTrue(strategy.areConfigurationEventsPrevented());
        });
        assertFalse(strategy.arePreferencesEventsPrevented());
        assertFalse(strategy.areConfigurationEventsPrevented());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#handlePreferencesEvent(Object, EventCoordinationStrategy.EventHandler)}.
     * @throws Throwable  Shouldn't happen
     */
    @Test
    void testHandlePreferencesEvent()
    throws Throwable {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        @SuppressWarnings("unchecked")
        final EventHandler<Object, ?> handler = mock(EventHandler.class);
        final Object event = new Object();
        
        strategy.handlePreferencesEvent(event, handler);
        
        then(handler).should(times(1)).handle(event);
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#handlePreferencesEvent(Object, EventCoordinationStrategy.EventHandler)}.
     * @throws Throwable  Shouldn't happen
     */
    @Test
    void testHandlePreferencesEvent_Prevented()
    throws Throwable {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        @SuppressWarnings("unchecked")
        final EventHandler<Object, ?> handler = mock(EventHandler.class);
        final Object event = new Object();
        
        strategy.startPreferencesPrevention();
        strategy.handlePreferencesEvent(event, handler);
        
        then(handler).should(never()).handle(any());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#handlePreferencesEvent(Object, EventCoordinationStrategy.EventHandler)}.
     * @throws Throwable  Shouldn't happen
     */
    @Test
    void testHandlePreferencesEvent_Error()
    throws Throwable {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        @SuppressWarnings("unchecked")
        final EventHandler<Object, ?> handler = mock(EventHandler.class);
        final Object event = new Object();
        final Exception mockEx = new Exception();
        
        willThrow(mockEx).given(handler).handle(any());
        
        final Exception result = assertThrows(Exception.class, () -> {
            strategy.handlePreferencesEvent(event, handler);
        });
        assertSame(mockEx, result);
        
        then(handler).should(times(1)).handle(event);
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#handleConfigurationEvent(Object, EventCoordinationStrategy.EventHandler)}.
     * @throws Throwable  Shouldn't happen
     */
    @Test
    void testHandleConfigurationEvent()
    throws Throwable {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        @SuppressWarnings("unchecked")
        final EventHandler<Object, ?> handler = mock(EventHandler.class);
        final Object event = new Object();
        
        strategy.handleConfigurationEvent(event, handler);
        
        then(handler).should(times(1)).handle(event);
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#handleConfigurationEvent(Object, EventCoordinationStrategy.EventHandler)}.
     * @throws Throwable  Shouldn't happen
     */
    @Test
    void testHandleConfigurationEvent_Prevented()
    throws Throwable {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        @SuppressWarnings("unchecked")
        final EventHandler<Object, ?> handler = mock(EventHandler.class);
        final Object event = new Object();
        
        strategy.startConfigurationPrevention();
        strategy.handleConfigurationEvent(event, handler);
        
        then(handler).should(never()).handle(any());
    }

    /**
     * Test for {@link ByThreadEventCoordinationStrategy#handleConfigurationEvent(Object, EventCoordinationStrategy.EventHandler)}.
     * @throws Throwable  Shouldn't happen
     */
    @Test
    void testHandleConfigurationEvent_Error()
    throws Throwable {
        final ByThreadEventCoordinationStrategy strategy = new ByThreadEventCoordinationStrategy();
        @SuppressWarnings("unchecked")
        final EventHandler<Object, ?> handler = mock(EventHandler.class);
        final Object event = new Object();
        final Exception mockEx = new Exception();
        
        willThrow(mockEx).given(handler).handle(any());
        
        final Exception result = assertThrows(Exception.class, () -> {
            strategy.handleConfigurationEvent(event, handler);
        });
        assertSame(mockEx, result);
        
        then(handler).should(times(1)).handle(event);
    }
}
