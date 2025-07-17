package dev.orne.config.impl;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2020 Orne Developments
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

import static org.mockito.BDDMockito.*;

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.interpol.ConfigurationInterpolator;
import org.junit.jupiter.api.Tag;

import dev.orne.config.Config;
import dev.orne.config.ConfigBuilder;

/**
 * Unit tests for {@code CommonsConfigImpl}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0
 * @since 1.0
 * @see CommonsConfigImpl
 */
@Tag("ut")
class CommonsConfigTest
extends AbstractConfigTest {

    /**
     * {@inheritDoc}
     */
    @Override
    protected ConfigBuilder<?> createBuilder(
            final @NotNull Map<String, String> properties) {
        final PropertiesConfiguration delegated;
        try {
            delegated = new Configurations()
                    .propertiesBuilder()
                    .getConfiguration();
        } catch (final ConfigurationException e) {
            throw new AssertionError("Error creating delegated configuration", e);
        }
        for (final Map.Entry<String, String> entry : properties.entrySet()) {
            delegated.setProperty(entry.getKey(), entry.getValue());
        }
        final ConfigurationInterpolator interpolator = mock(ConfigurationInterpolator.class);
        given(interpolator.interpolate(any()))
                .willAnswer(invocation -> invocation.getArgument(0));
        delegated.setInterpolator(interpolator);
        return Config.fromApacheCommons()
                .ofDelegate(delegated);
    }
}
