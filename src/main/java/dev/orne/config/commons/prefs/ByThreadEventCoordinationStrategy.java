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

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.function.FailableRunnable;

/**
 * Default implementation of {@code EventCoordinationStrategy} that
 * prevents handling of events caused during the handling of a previous
 * event in the same {@code Thread}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 */
public class ByThreadEventCoordinationStrategy
implements EventCoordinationStrategy {

    /** The by {@code Thread} storage of processing events. */
    private final @NotNull ThreadLocal<Integer> preferencesPrevented;
    /** The by {@code Thread} storage of processing events. */
    private final @NotNull ThreadLocal<Integer> configurationPrevented;

    /**
     * Creates a new instance.
     */
    public ByThreadEventCoordinationStrategy() {
        super();
        this.preferencesPrevented = ThreadLocal.withInitial(() -> 0);
        this.configurationPrevented = ThreadLocal.withInitial(() -> 0);
    }

    /**
     * Registers a {@code Preferences} tree events prevention block start.
     */
    protected void startPreferencesPrevention() {
        this.preferencesPrevented.set(this.preferencesPrevented.get() + 1);
    }

    /**
     * Registers a {@code Preferences} tree events prevention block end.
     */
    protected void finishPreferencesPrevention() {
        final int count = this.preferencesPrevented.get() - 1;
        if (count > 0) {
            this.preferencesPrevented.set(count);
        } else {
            this.preferencesPrevented.remove();
        }
    }

    /**
     * Registers a {@code PreferencesBased} configuration events prevention
     * block start.
     */
    protected void startConfigurationPrevention() {
        this.configurationPrevented.set(this.configurationPrevented.get() + 1);
    }

    /**
     * Registers a {@code PreferencesBased} configuration events prevention
     * block end.
     */
    protected void finishConfigurationPrevention() {
        final int count = this.configurationPrevented.get() - 1;
        if (count > 0) {
            this.configurationPrevented.set(count);
        } else {
            this.configurationPrevented.remove();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Throwable> void preventEvents(
            final @NotNull FailableRunnable<E> body)
    throws E {
        Validate.notNull(body);
        startPreferencesPrevention();
        startConfigurationPrevention();
        try {
            body.run();
        } finally {
            finishPreferencesPrevention();
            finishConfigurationPrevention();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Throwable> void preventPreferencesEvents(
            final @NotNull FailableRunnable<E> body)
    throws E {
        Validate.notNull(body);
        startPreferencesPrevention();
        try {
            body.run();
        } finally {
            finishPreferencesPrevention();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean arePreferencesEventsPrevented() {
        return this.preferencesPrevented.get() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T, E extends Throwable> void handlePreferencesEvent(
            final @NotNull T event,
            final @NotNull EventHandler<T, E> handler)
    throws E {
        Validate.notNull(event);
        Validate.notNull(handler);
        if (!arePreferencesEventsPrevented()) {
            handler.handle(event);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends Throwable> void preventConfigurationEvents(
            final @NotNull FailableRunnable<E> body)
    throws E {
        Validate.notNull(body);
        startConfigurationPrevention();
        try {
            body.run();
        } finally {
            finishConfigurationPrevention();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean areConfigurationEventsPrevented() {
        return this.configurationPrevented.get() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T, E extends Throwable> void handleConfigurationEvent(
            final @NotNull T event,
            final @NotNull EventHandler<T, E> handler)
    throws E {
        Validate.notNull(event);
        Validate.notNull(handler);
        if (!areConfigurationEventsPrevented()) {
            handler.handle(event);
        }
    }
}
