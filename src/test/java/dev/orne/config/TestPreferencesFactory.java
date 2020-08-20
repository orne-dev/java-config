package dev.orne.config;

import java.io.OutputStream;
import java.util.prefs.NodeChangeListener;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

/**
 * Mock {@code PreferencesFactory} implementation for testing classes
 * that use {@code Preferences}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
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
    extends Preferences {

        /**
         * {@inheritDoc}
         */
        @Override
        public void put(String key, String value) {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String get(String key, String def) {
            // NOP
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void remove(String key) {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void clear() {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void putInt(String key, int value) {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getInt(String key, int def) {
            // NOP
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void putLong(String key, long value) {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public long getLong(String key, long def) {
            // NOP
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void putBoolean(String key, boolean value) {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean getBoolean(String key, boolean def) {
            // NOP
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void putFloat(String key, float value) {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public float getFloat(String key, float def) {
            // NOP
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void putDouble(String key, double value) {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double getDouble(String key, double def) {
            // NOP
            return 0;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void putByteArray(String key, byte[] value) {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public byte[] getByteArray(String key, byte[] def) {
            // NOP
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String[] keys() {
            // NOP
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String[] childrenNames() {
            // NOP
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Preferences parent() {
            // NOP
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Preferences node(String pathName) {
            // NOP
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean nodeExists(String pathName) {
            // NOP
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void removeNode() {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String name() {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String absolutePath() {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isUserNode() {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void flush() {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void sync() {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void addPreferenceChangeListener(PreferenceChangeListener pcl) {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void removePreferenceChangeListener(PreferenceChangeListener pcl) {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void addNodeChangeListener(NodeChangeListener ncl) {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void removeNodeChangeListener(NodeChangeListener ncl) {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void exportNode(OutputStream os) {
            // NOP
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void exportSubtree(OutputStream os) {
            // NOP
        }
    }
}
