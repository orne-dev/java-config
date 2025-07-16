package dev.orne.config.impl;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2020 Orne Developments
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

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

import javax.validation.constraints.NotNull;

/**
 * Mock {@code PreferencesFactory} implementation for testing classes
 * that use {@code Preferences}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2020-08
 * @since 0.2
 * @see Preferences
 * @see PreferencesFactory
 */
public class TestPreferencesFactory
implements PreferencesFactory {

    /** The system preferences root node. */
    private static Preferences systemRoot = new NopPreferences();
    /** The user preferences root node. */
    private static Preferences userRoot = new NopPreferences();

    /**
     * {@inheritDoc}
     */
    @Override
    public Preferences systemRoot() {
        return systemRoot;
    }

    /**
     * Returns the system preferences root node.
     * 
     * @return The system preferences root node
     */
    public static Preferences getSystemRoot() {
        return systemRoot;
    }

    /**
     * Sets the system preferences root node.
     * 
     * @param root The system preferences root node
     */
    public static void setSystemRoot(final Preferences root) {
        systemRoot = root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Preferences userRoot() {
        return userRoot;
    }

    /**
     * Return the user preferences root node.
     * 
     * @return The user preferences root node
     */
    public static Preferences getUserRoot() {
        return userRoot;
    }

    /**
     * Sets the user preferences root node.
     * 
     * @param root The user preferences root node
     */
    public static void setUserRoot(final Preferences root) {
        userRoot = root;
    }

    /**
     * NOP implementation of {@code Preferences}.
     */
    public static class NopPreferences
    extends AbstractPreferences {

        public NopPreferences() {
            super(null, "");
        }

        protected NopPreferences(
                final @NotNull NopPreferences parent,
                final @NotNull String name) {
            super(parent, name);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void putSpi(
                final @NotNull String key,
                final @NotNull String value) {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String getSpi(
                final @NotNull String key) {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void removeSpi(
                final @NotNull String key) {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void removeNodeSpi() {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String[] keysSpi() {
            return new String[0];
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String[] childrenNamesSpi() {
            return new String[0];
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected AbstractPreferences childSpi(
                final @NotNull String name) {
            return new NopPreferences(this, name);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void syncSpi() {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void flushSpi() {
            // NOP
        }
    }

    /**
     * In memory implementation of {@code Preferences}.
     */
    public static class InMemoryPreferences
    extends AbstractPreferences {

        private final Map<String, String> attributes;
        private final Map<String, InMemoryPreferences> childs;

        public InMemoryPreferences() {
            this(null, "");
        }

        protected InMemoryPreferences(
                final @NotNull InMemoryPreferences parent,
                final @NotNull String name) {
            super(parent, name);
            this.attributes = new HashMap<>();
            this.childs = new HashMap<>();
        }

        protected void setAttributes(
                final @NotNull Map<String, String> attributes) {
            this.attributes.clear();
            this.attributes.putAll(attributes);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public InMemoryPreferences parent() {
            return (InMemoryPreferences) super.parent();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void putSpi(
                final @NotNull String key,
                final @NotNull String value) {
            this.attributes.put(key, value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String getSpi(
                final @NotNull String key) {
            return this.attributes.get(key);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void removeSpi(
                final @NotNull String key) {
            this.attributes.remove(key);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void removeNodeSpi() {
            parent().childs.remove(name());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String[] keysSpi() {
            return this.attributes.keySet().toArray(new String[0]);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String[] childrenNamesSpi() {
            return this.childs.keySet().toArray(new String[0]);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected InMemoryPreferences childSpi(
                final @NotNull String name) {
            if (!this.childs.containsKey(name)) {
                this.childs.put(name, new InMemoryPreferences(this, name));
            }
            return this.childs.get(name);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void syncSpi() {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void flushSpi() {
            // NOP
        }
    }
}
