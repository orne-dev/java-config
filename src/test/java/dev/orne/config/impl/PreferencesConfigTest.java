package dev.orne.config.impl;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 Orne Developments
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

import java.util.Map;
import java.util.prefs.Preferences;

import javax.validation.constraints.NotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.Config;
import dev.orne.config.ConfigBuilder;

/**
 * Unit tests for {@code PreferencesConfig}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 */
@Tag("ut")
class PreferencesConfigTest
extends AbstractConfigTest {

    /**
     * Restores the NOP root {@code Preferences} nodes.
     */
    @AfterEach
    void restoreTestPreferences() {
        TestPreferencesFactory.setSystemRoot(new TestPreferencesFactory.NopPreferences());
        TestPreferencesFactory.setUserRoot(new TestPreferencesFactory.NopPreferences());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConfigBuilder<?> createBuilder(
            final @NotNull Map<String, String> properties) {
        final TestPreferencesFactory.InMemoryPreferences preferences =
                new TestPreferencesFactory.InMemoryPreferences();
        preferences.setAttributes(properties);
        return Config.fromJavaPreferences()
                .ofNode(preferences);
    }

    /**
     * Tests instance building from preferences node.
     */
    @Test
    void testNodeBuilder() {
        final Preferences preferences = mock(Preferences.class);
        final PreferencesConfigImpl config = assertInstanceOf(
                PreferencesConfigImpl.class,
                Config.fromJavaPreferences()
                    .ofNode(preferences)
                    .build());
        assertSame(preferences, config.getPreferences());
    }

    /**
     * Tests instance building from user root preferences node.
     */
    @Test
    void testUserRootBuilder() {
        final Preferences preferences = mock(Preferences.class);
        TestPreferencesFactory.setUserRoot(preferences);
        final PreferencesConfigImpl config = assertInstanceOf(
                PreferencesConfigImpl.class,
                Config.fromJavaPreferences()
                    .ofUserRoot()
                    .build());
        assertSame(preferences, config.getPreferences());
    }

    /**
     * Tests instance building from user class preferences node.
     */
    @Test
    void testUserClassBuilder() {
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setUserRoot(rootPreferences);
        final Preferences preferences = mock(Preferences.class);
        willReturn(preferences).given(rootPreferences).node("/dev/orne/config/impl");
        final PreferencesConfigImpl config = assertInstanceOf(
                PreferencesConfigImpl.class,
                Config.fromJavaPreferences()
                    .ofUser(PreferencesConfigTest.class)
                    .build());
        assertSame(preferences, config.getPreferences());
    }

    /**
     * Tests instance building from user path preferences node.
     */
    @Test
    void testUserPathBuilder() {
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setUserRoot(rootPreferences);
        final Preferences preferences = mock(Preferences.class);
        final String path = "/test/path";
        willReturn(preferences).given(rootPreferences).node(path);
        final PreferencesConfigImpl config = assertInstanceOf(
                PreferencesConfigImpl.class,
                Config.fromJavaPreferences()
                    .ofUser(path)
                    .build());
        assertSame(preferences, config.getPreferences());
    }

    /**
     * Tests instance building from user class and path preferences node.
     */
    @Test
    void testUserClassPathBuilder() {
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setUserRoot(rootPreferences);
        final Preferences classPreferences = mock(Preferences.class);
        final Preferences preferences = mock(Preferences.class);
        final String classPackage = "/dev/orne/config/impl";
        final String path = "/test/path";
        willReturn(classPreferences).given(rootPreferences).node(classPackage);
        willReturn(preferences).given(classPreferences).node(path);
        final PreferencesConfigImpl config = assertInstanceOf(
                PreferencesConfigImpl.class,
                Config.fromJavaPreferences()
                    .ofUser(PreferencesConfigTest.class, path)
                    .build());
        assertSame(preferences, config.getPreferences());
    }

    /**
     * Tests instance building from system root preferences node.
     */
    @Test
    void testSystemRootBuilder() {
        final Preferences preferences = mock(Preferences.class);
        TestPreferencesFactory.setSystemRoot(preferences);
        final PreferencesConfigImpl config = assertInstanceOf(
                PreferencesConfigImpl.class,
                Config.fromJavaPreferences()
                    .ofSystemRoot()
                    .build());
        assertSame(preferences, config.getPreferences());
    }

    /**
     * Tests instance building from system class preferences node.
     */
    @Test
    void testSystemClassBuilder() {
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setSystemRoot(rootPreferences);
        final Preferences preferences = mock(Preferences.class);
        willReturn(preferences).given(rootPreferences).node("/dev/orne/config/impl");
        final PreferencesConfigImpl config = assertInstanceOf(
                PreferencesConfigImpl.class,
                Config.fromJavaPreferences()
                    .ofSystem(PreferencesConfigTest.class)
                    .build());
        assertSame(preferences, config.getPreferences());
    }

    /**
     * Tests instance building from system path preferences node.
     */
    @Test
    void testSystemPathBuilder() {
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setSystemRoot(rootPreferences);
        final Preferences preferences = mock(Preferences.class);
        final String path = "/test/path";
        willReturn(preferences).given(rootPreferences).node(path);
        final PreferencesConfigImpl config = assertInstanceOf(
                PreferencesConfigImpl.class,
                Config.fromJavaPreferences()
                    .ofSystem(path)
                    .build());
        assertSame(preferences, config.getPreferences());
    }

    /**
     * Tests instance building from system class and path preferences node.
     */
    @Test
    void testSystemClassPathBuilder() {
        final Preferences rootPreferences = mock(Preferences.class);
        TestPreferencesFactory.setSystemRoot(rootPreferences);
        final Preferences classPreferences = mock(Preferences.class);
        final Preferences preferences = mock(Preferences.class);
        final String classPackage = "/dev/orne/config/impl";
        final String path = "/test/path";
        willReturn(classPreferences).given(rootPreferences).node(classPackage);
        willReturn(preferences).given(classPreferences).node(path);
        final PreferencesConfigImpl config = assertInstanceOf(
                PreferencesConfigImpl.class,
                Config.fromJavaPreferences()
                    .ofSystem(PreferencesConfigTest.class, path)
                    .build());
        assertSame(preferences, config.getPreferences());
    }
}
