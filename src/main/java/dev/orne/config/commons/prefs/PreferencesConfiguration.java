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

import java.util.prefs.Preferences;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.BaseHierarchicalConfiguration;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.io.ConfigurationLogger;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.commons.configuration2.tree.NodeModel;

/**
 * Default implementation of {@code HierarchicalConfiguration} based in Java
 * {@code Preferences} with {@code ImmutableNode} as nodes.
 * <p>
 * By inheriting from {@link BaseHierarchicalConfiguration} this class provides
 * some extended functionality, e.g. interpolation of property values. Like in
 * {@link PropertiesConfiguration} property values can contain delimiter
 * characters (the comma ',' per default) and are then split into multiple
 * values. This works for {@code Preferences} values. The delimiter can be
 * escaped by a backslash. As an example consider the following
 * {@code Preferences} value:
 * <pre>
 * array: 10,20,30,40
 * scalar: \,1415
 * text: To be or not to be\, this is the question!
 * </pre>
 * <p>
 * Here the content of the {@code array} element will be split at the commas, so
 * the {@code array} key will be assigned 4 values. In the {@code scalar}
 * property and the {@code text} attribute of the {@code cite} element the comma
 * is escaped, so that no splitting is performed.
 * <p>
 * The configuration API allows setting multiple values for a single attribute,
 * e.g. something like the following is legal (assuming that the default
 * expression engine is used):
 * <pre>
 * PreferencesConfiguration config = new PreferencesConfiguration();
 * config.addProperty(&quot;test.dir[@name]&quot;, &quot;C:\\Temp\\&quot;);
 * config.addProperty(&quot;test.dir[@name]&quot;, &quot;D:\\Data\\&quot;);
 * </pre>
 * 
 * @author <a href="mailto:https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 0.2
 * @see Preferences
 */
public class PreferencesConfiguration
extends BaseHierarchicalConfiguration
implements PreferencesBased<ImmutableNode> {

    /** Property key for the expression engine. */
    protected static final String PROP_EXPRESSION_ENGINE = "expressionEngine";

    /**
     * Creates a new instance of {@code PreferencesConfiguration}.
     */
    public PreferencesConfiguration() {
        super();
        initLogger(new ConfigurationLogger(PreferencesConfiguration.class));
    }

    /**
     * Creates a new instance of {@code PreferencesConfiguration} and copies
     * the content of the passed in configuration into this object. Note that
     * only the data of the passed in configuration will be copied. If, for
     * instance, the other configuration is a {@code PreferencesConfiguration},
     * too, things like base Preferences node will be lost.
     *
     * @param copy The configuration to copy
     */
    public PreferencesConfiguration(
            final HierarchicalConfiguration<ImmutableNode> copy) {
        super(copy);
        initLogger(new ConfigurationLogger(PreferencesConfiguration.class));
    }

    /**
     * Creates a new instance of {@code PreferencesConfiguration} and
     * initializes it with the given {@code NodeModel}.
     *
     * @param model The {@code NodeModel} to use
     */
    protected PreferencesConfiguration(
            final NodeModel<ImmutableNode> model) {
        super(model);
    }

    /**
     * {@inheritDoc}
     */
    public void load(
            final @NotNull ImmutableNode rootNode) {
        beginWrite(false);
        final ImmutableNode namelessRootNode = rootNode.setName(null);
        getSubConfigurationParentModel().mergeRoot(
                namelessRootNode,
                null,
                null,
                null,
                this);
        endWrite();
    }
}
