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

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.lang3.function.FailableRunnable;

/**
 * Event coordination strategy for {@code PreferencesHandler}. Prevents
 * {@code Preferences} tree and {@code PreferencesBased} configuration
 * events to fire each other when both
 * {@code PreferencesHandler.isAutoSave()} and
 * {@code PreferencesHandler.isAutoLoad()} are activated.
 * <p>
 * For example:
 * <p>
 * When a property is set in the managed {@code PreferencesBased}
 * configuration a {@code ConfigurationEvent} will be fired, that can cause
 * the change being saved in the underlying {@code Preferences} tree. This
 * will fire a {@code PreferenceChangeEvent}, which would, in normal
 * circumstances, cause the updating of the property in the managed
 * {@code PreferencesBased} configuration, starting an infinite loop.
 * <p>
 * Classes implementing this interface must prevent such cycles and be
 * {@code Thread} safe.
 * 
 * @author <a href="mailto:https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see PreferencesHandler
 * @see ConfigurationEvent
 */
public interface EventCoordinationStrategy {

    /**
     * Prevents both {@code Preferences} tree and {@code PreferencesBased}
     * configuration events handling during body execution.
     * 
     * @param <E> The throwable type thrown by the body
     * @param body The body to execute with events handling prevented
     * @throws E If body execution fails
     */
    <E extends Throwable> void preventEvents(
            @NotNull FailableRunnable<E> body)
    throws E;

    /**
     * Prevents {@code Preferences} tree events handling during body
     * execution.
     * 
     * @param <E> The throwable type thrown by the body
     * @param body The body to execute with {@code Preferences} tree
     * events handling prevented
     * @throws E If body execution fails
     */
    <E extends Throwable> void preventPreferencesEvents(
            @NotNull FailableRunnable<E> body)
    throws E;

    /**
     * Returns {@code true} if {@code Preferences} tree events are
     * prevented.
     * 
     * @return If {@code Preferences} tree events are prevented
     */
    boolean arePreferencesEventsPrevented();

    /**
     * Executes the {@code Preferences} tree event handling if not
     * prevented.
     * 
     * @param <T> The event type
     * @param <E> The throwable type thrown by the handler
     * @param event The {@code Preferences} tree event
     * @param handler The event handler
     * @throws E If {@code Preferences} tree event handling fails
     */
    <T, E extends Throwable> void handlePreferencesEvent(
            @NotNull T event,
            @NotNull EventHandler<T, E> handler)
    throws E;

    /**
     * Prevents {@code PreferencesBased} configuration events handling
     * during body execution.
     * 
     * @param <E> The throwable type thrown by the body
     * @param body The body to execute with {@code PreferencesBased}
     * configuration events handling prevented
     * @throws E If body execution fails
     */
    <E extends Throwable> void preventConfigurationEvents(
            @NotNull FailableRunnable<E> body)
    throws E;

    /**
     * Returns {@code true} if {@code PreferencesBased} configuration
     * events are prevented.
     * 
     * @return If {@code PreferencesBased} configuration events are
     * prevented
     */
    boolean areConfigurationEventsPrevented();

    /**
     * Executes the {@code PreferencesBased} configuration event handling
     * if not prevented.
     * 
     * @param <T> The event type
     * @param <E> The throwable type thrown by the handler
     * @param event The {@code Preferences} tree event
     * @param handler The event handler
     * @throws E If {@code Preferences} tree event handling fails
     */
    <T, E extends Throwable> void handleConfigurationEvent(
            @NotNull T event,
            @NotNull EventHandler<T, E> handler)
    throws E;

    /**
     * Functional interface for event handler.
     * 
     * @author <a href="mailto:https://github.com/ihernaez">(w) Iker Hernaez</a>
     * @version 1.0
     * @param <T> The event type
     * @since 0.2
     */
    @FunctionalInterface
    interface EventHandler<T, E extends Throwable> {

        /**
         * Handles the event.
         * 
         * @param event The event to handle
         * @throws E If event handling fails
         */
        void handle(T event)
        throws E;
    }
}
