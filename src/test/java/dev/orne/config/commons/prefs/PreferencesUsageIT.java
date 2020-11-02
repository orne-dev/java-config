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

import java.net.URI;
import java.util.prefs.Preferences;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dev.orne.config.TestPreferencesFactory;

/**
 * Integration tests for {@code PreferencesConfiguration},
 * {@code PreferencesBuilderParametersImpl} and {@code PreferencesConfigurationBuilder}.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see PreferencesConfiguration
 */
@Tag("it")
class PreferencesUsageIT {

    /**
     * Restores the in memory root {@code Preferences} nodes.
     */
    @BeforeEach
    public void setTestPreferences() {
        TestPreferencesFactory.setSystemRoot(new TestPreferencesFactory.InMemoryPreferences());
        TestPreferencesFactory.setUserRoot(new TestPreferencesFactory.InMemoryPreferences());
    }

    /**
     * Restores the in memory root {@code Preferences} nodes.
     */
    @AfterEach
    public void restoreTestPreferences() {
        TestPreferencesFactory.setSystemRoot(new TestPreferencesFactory.NopPreferences());
        TestPreferencesFactory.setUserRoot(new TestPreferencesFactory.NopPreferences());
    }

    @Test
    void testLoadInternal() {
        final PreferencesConfiguration config = new PreferencesConfiguration();
        final ImmutableNode rootNode = new ImmutableNode.Builder()
                .name("rootNode")
                .addAttribute("attr", "rootAttrValue")
                .addChild(new ImmutableNode.Builder()
                        .name("child")
                        .value("childValue")
                        .addAttribute("attr", "childAttrValue")
                        .create())
                .create();
        config.load(rootNode);
        
        assertEquals("childValue", config.getString("child", null));
        assertEquals("childAttrValue", config.getString("child[@attr]", null));
        assertEquals("rootAttrValue", config.getString("[@attr]", null));
    }

    @Test
    void testLoadInternal_NullRootName() {
        final PreferencesConfiguration config = new PreferencesConfiguration();
        final ImmutableNode rootNode = new ImmutableNode.Builder()
                .addAttribute("attr", "rootAttrValue")
                .addChild(new ImmutableNode.Builder()
                        .name("child")
                        .value("childValue")
                        .addAttribute("attr", "childAttrValue")
                        .create())
                .create();
        config.load(rootNode);
        
        assertEquals("childValue", config.getString("child", null));
        assertEquals("childAttrValue", config.getString("child[@attr]", null));
        assertEquals("rootAttrValue", config.getString("[@attr]", null));
    }

    @Test
    void testBuilder()
    throws ConfigurationException {
        final Preferences userRoot = TestPreferencesFactory.getUserRoot();
        final Preferences baseNode = userRoot.node("base/prefs/node");
        baseNode.putBoolean("autoload", true);
        baseNode.put("token", "mock token");
        final Preferences child = baseNode.node("my/child/node");
        child.putInt("delay", 1234);
        child.put("host", "http://example.org/base/uri/");
        
        final PreferencesConfigurationBuilder builder =
                new PreferencesConfigurationBuilder(PreferencesConfiguration.class);
        builder.configure(PreferencesConfigurationBuilder.params()
                .setPath("base/prefs/node"));
        final PreferencesConfiguration config = builder.getConfiguration();
        
        assertTrue(config.getBoolean("autoload"));
        assertEquals("mock token", config.getString("token"));
        assertEquals(1234, config.getInt("my.child.node.delay"));
        assertEquals("http://example.org/base/uri/", config.get(URI.class, "my.child.node.host").toString());

        config.addProperty("my.child.node.nested.folder", "/some/folder");
        config.setProperty("my.child.node.host", URI.create("http://example.org/base/uri/new/"));
        config.clearProperty("token");

        builder.save();

        assertEquals("/some/folder", child.node("nested").get("folder", null));
        assertEquals("http://example.org/base/uri/new/", child.get("host", null));
        assertNull(baseNode.get("token", null));
    }
}
