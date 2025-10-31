package dev.orne.config.impl;

import java.io.IOException;
import java.io.Writer;

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

import java.util.Objects;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apiguardian.api.API;
import org.w3c.dom.Document;

import dev.orne.config.Config;
import dev.orne.config.FileWatchableConfig;

/**
 * Implementation of {@code WatchableConfig} based on a XML document.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see Config
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class XmlMutableConfigImpl
extends AbstractWatchableConfig
implements FileWatchableConfig {

    /** The XML document with the configuration options. */
    private final @NotNull Document document;
    /** The configuration nested properties separator. */
    private final @NotBlank String propertySeparator;
    /** The XML attributes references prefix. */
    private final @NotBlank String attributePrefix;

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param mutableOptions The mutable configuration builder options.
     * @param xmlOptions The XML based configuration builder options.
     */
    public XmlMutableConfigImpl(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions,
            final @NotNull XmlConfigOptions xmlOptions) {
        super(options, mutableOptions);
        Objects.requireNonNull(xmlOptions);
        Objects.requireNonNull(xmlOptions);
        this.document = Objects.requireNonNull(xmlOptions.getDocument());
        this.propertySeparator = Objects.requireNonNull(xmlOptions.getPropertySeparator());
        this.attributePrefix = Objects.requireNonNull(xmlOptions.getAttributePrefix());
    }

    /**
     * Returns the XML document with the configuration options.
     * 
     * @return The XML document with the configuration options.
     */
    protected @NotNull Document getDocument() {
        return this.document;
    }

    /**
     * Returns the configuration nested properties separator.
     * 
     * @return The configuration nested properties separator.
     */
    protected @NotBlank String getPropertySeparator() {
        return this.propertySeparator;
    }

    /**
     * Returns the XML attributes references prefix.
     * 
     * @return The XML attributes references prefix.
     */
    protected @NotBlank String getAttributePrefix() {
        return this.attributePrefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEmptyInt() {
        return !this.document.getDocumentElement().hasAttributes()
                && !this.document.getDocumentElement().hasChildNodes();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean containsInt(@NotBlank String key) {
        return XmlUtils.contains(
                this.document,
                key,
                this.propertySeparator,
                this.attributePrefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull Stream<String> getKeysInt() {
        return XmlUtils.extractKeys(
                this.document,
                this.propertySeparator,
                this.attributePrefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getInt(@NotBlank String key) {
        return XmlUtils.getValue(
                this.document,
                key,
                this.propertySeparator,
                attributePrefix)
                .orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setInt(
            final @NotBlank String key,
            final @NotNull String value) {
        XmlUtils.setValue(
                this.document,
                key,
                this.propertySeparator,
                this.attributePrefix,
                value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeInt(
            final @NotBlank String... keys) {
        for (final String key : keys) {
            XmlUtils.setValue(
                    this.document,
                    key,
                    this.propertySeparator,
                    this.attributePrefix,
                    null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(
            final @NotNull Writer destination)
    throws IOException {
        try {
            final TransformerFactory factory = TransformerFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            final Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            final DOMSource source = new DOMSource(this.document);
            final StreamResult result = new StreamResult(destination);
            transformer.transform(source, result);
        } catch (final TransformerException e) {
            throw new IOException("Error saving XML configuration", e);
        }
    }
}
