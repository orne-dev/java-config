package dev.orne.config.impl;

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

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;
import org.w3c.dom.Document;

import dev.orne.config.ConfigException;
import dev.orne.config.XmlMutableConfigBuilder;

/**
 * Implementation of XML files based mutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see XmlMutableConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class XmlMutableConfigBuilderImpl
extends AbstractMutableConfigBuilderImpl<XmlMutableConfigBuilder>
implements XmlMutableConfigBuilder {

    /** The XML based configuration options. */
    protected final @NotNull XmlConfigOptions xmlOptions;

    /**
     * Copy constructor.
     * 
     * @param options The configuration options to copy.
     * @param mutableOptions The mutable configuration options to copy.
     * @param xmlOptions The XML based configuration options to copy.
     */
    protected XmlMutableConfigBuilderImpl(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions,
            final @NotNull XmlConfigOptions xmlOptions) {
        super(options, mutableOptions);
        this.xmlOptions = new XmlConfigOptions(xmlOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull XmlMutableConfigBuilder withSeparator(
            final @NotEmpty String separator) {
        this.xmlOptions.setPropertySeparator(separator);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull XmlMutableConfigBuilder withAttributePrefix(
            final @NotEmpty String prefix) {
        this.xmlOptions.setAttributePrefix(prefix);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull XmlMutableConfigBuilder withEmptyDocument(
            final String namespaceURI,
            final @NotNull String rootElementName) {
        this.xmlOptions.setRootElement(namespaceURI, rootElementName);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull XmlMutableConfigBuilder add(
            @NotNull Document values) {
        this.xmlOptions.add(values);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull XmlMutableConfigBuilder add(
            final @NotNull Map<String, String> values) {
        if (this.xmlOptions.getDocument().getDocumentElement() == null) {
            throw new ConfigException(
                    "Cannot add values to an empty XML document. "
                    + "Please set the root element or load a base document first.");
        }
        if (!values.isEmpty()) {
            values.forEach((key, value) -> XmlUtils.setValue(
                    this.xmlOptions.getDocument(),
                    key,
                    this.xmlOptions.getPropertySeparator(),
                    this.xmlOptions.getAttributePrefix(),
                    value));
        }
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull XmlMutableConfigBuilder load(
            final @NotNull String path) {
        this.xmlOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull XmlMutableConfigBuilder load(
            final @NotNull Path path) {
        this.xmlOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull XmlMutableConfigBuilder load(
            final @NotNull File file) {
        this.xmlOptions.load(file);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull XmlMutableConfigBuilder load(
            final @NotNull URL url) {
        this.xmlOptions.load(url);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull XmlMutableConfigImpl build() {
        if (this.xmlOptions.getDocument().getDocumentElement() == null) {
            this.xmlOptions.setRootElement(null, "config");
        }
        return new XmlMutableConfigImpl(
                this.options,
                this.mutableOptions,
                this.xmlOptions);
    }
}
