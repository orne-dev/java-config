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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import dev.orne.config.ConfigException;
import dev.orne.config.XmlConfigBaseBuilder;

/**
 * Options of XML based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see XmlConfigImpl
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class XmlConfigOptions {

    /** The class logger. */
    private static final Logger LOG =
            LoggerFactory.getLogger(XmlConfigOptions.class);

    /** Error message for not found resources. */
    private static final String RESOURCE_NOT_FOUND_ERR =
            "Configuration resource not found: {}";
    /** JSON file read error message. */
    private static final String READ_ERR =
            "Error reading configuration resource: {}";
    /** XML document parse error message. */
    private static final String PARSE_ERR =
            "Error parsing configuration XML document";
    /** XML documents merge error message. */
    private static final String MERGE_ERR =
            "Error mergin configuration XML documents";

    /** The XML document builder factory. */
    private final @NotNull DocumentBuilderFactory factory;
    /** The XML document builder. */
    private final @NotNull DocumentBuilder builder;
    /** The XML document with the configuration properties. */
    private final @NotNull Document document;
    /** The configuration nested properties separator. */
    private @NotBlank String propertySeparator;
    /** The XML attributes references prefix. */
    private @NotBlank String attributePrefix;

    /**
     * Empty constructor.
     */
    public XmlConfigOptions() {
        super();
        this.factory = DocumentBuilderFactory.newInstance();
        try {
            this.factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        } catch (final ParserConfigurationException e) {
            LOG.warn("Error enabling secure XML processing", e);
        }
        try {
            this.builder = this.factory.newDocumentBuilder();
        } catch (final ParserConfigurationException e) {
            throw new ConfigException("Error creating XML document builder", e);
        }
        this.document = this.builder.newDocument();
        this.propertySeparator = XmlConfigBaseBuilder.DEFAULT_SEPARATOR;
        this.attributePrefix = XmlConfigBaseBuilder.DEFAULT_ATTRIBUTE_PREFIX;
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public XmlConfigOptions(
            final @NotNull XmlConfigOptions copy) {
        super();
        this.factory = copy.factory;
        this.builder = copy.builder;
        this.document = copy.document;
        this.propertySeparator = copy.propertySeparator;
        this.attributePrefix = copy.attributePrefix;
    }

    /**
     * Returns the XML document with the configuration properties.
     * 
     * @return The XML document with the configuration properties.
     */
    public @NotNull Document getDocument() {
        return this.document;
    }

    /**
     * Returns the configuration nested properties separator.
     * 
     * @return The configuration nested properties separator.
     */
    public @NotBlank String getPropertySeparator() {
        return this.propertySeparator;
    }

    /**
     * Sets the configuration nested properties separator.
     * 
     * @param separator The configuration nested properties separator.
     */
    public void setPropertySeparator(
            final @NotBlank String separator) {
        Validate.notBlank(separator, "Property separator cannot be blank");
        this.propertySeparator = separator;
    }

    /**
     * Returns the XML attributes references prefix.
     * 
     * @return The XML attributes references prefix.
     */
    public @NotBlank String getAttributePrefix() {
        return this.attributePrefix;
    }

    /**
     * Sets the XML attributes references prefix.
     * 
     * @param prefix The XML attributes references prefix.
     */
    public void setAttributePrefix(
            final @NotBlank String prefix) {
        Validate.notBlank(prefix, "Property separator cannot be blank");
        this.attributePrefix = prefix;
    }

    /**
     * Creates an empty XML document with the specified root element name
     * as configuration properties container.
     * <p>
     * Note that any XML document loaded through {@code load()} methods
     * must match the same root element name.
     * 
     * @param namespaceURI The XML document namespace URI, or {@code null}
     * if the XML document does not use namespaces.
     * @param localName The root element name of the XML document to create.
     */
    public void setRootElement(
            final String namespaceURI,
            final @NotNull String localName) {
        if (this.document.getDocumentElement() != null) {
            this.document.removeChild(this.document.getDocumentElement());
        }
        final Element root;
        if (namespaceURI == null) {
            root = this.document.createElement(localName);
        } else {
            root = this.document.createElementNS(
                    namespaceURI,
                    localName);
        }
        this.document.appendChild(root);
    }

    /**
     * Adds the specified configuration properties to the configuration
     * properties.
     * 
     * @param values The configuration properties.
     */
    public void add(
            final @NotNull Document values) {
        try {
            final Element docRoot = values.getDocumentElement();
            Element cfgRoot = this.document.getDocumentElement();
            if (cfgRoot == null) {
                if (docRoot.getNamespaceURI() == null) {
                    cfgRoot = this.document.createElement(
                            docRoot.getTagName());
                } else {
                    cfgRoot = this.document.createElementNS(
                            docRoot.getNamespaceURI(),
                            docRoot.getTagName());
                }
                this.document.appendChild(cfgRoot);
            } else {
                if (!docRoot.getNamespaceURI().equals(cfgRoot.getNamespaceURI())
                        || !docRoot.getLocalName().equals(cfgRoot.getLocalName())) {
                    throw new ConfigException(
                            "Configuration root element mismatch: expected "
                                    + cfgRoot.getNamespaceURI()
                                    + ":"
                                    + cfgRoot.getLocalName()
                                    + ", found "
                                    + docRoot.getNamespaceURI()
                                    + ":"
                                    + docRoot.getLocalName());
                }
            }
            while (docRoot.hasChildNodes()) {
                final Node child = docRoot.getFirstChild();
                docRoot.removeChild(child);
                cfgRoot.appendChild(this.document.importNode(child, true));
            }
        } catch (final DOMException e) {
            LOG.warn(MERGE_ERR, e);
        }
    }

    /**
     * Loads the configuration properties from the specified ClassLoader
     * resource.
     * 
     * @param path The ClassLoader resource path.
     */
    public void load(
            final @NotNull String path) {
        try {
            final Enumeration<URL> resources =
                    Thread.currentThread()
                            .getContextClassLoader()
                            .getResources(path);
            if (!resources.hasMoreElements()) {
                LOG.warn(RESOURCE_NOT_FOUND_ERR, path);
            }
            while (resources.hasMoreElements()) {
                load(resources.nextElement());
            }
        } catch (final IOException e) {
            LOG.warn(READ_ERR, path, e);
        }
    }

    /**
     * Loads the configuration properties from the file in the specified
     * path.
     * 
     * @param path The file path.
     */
    public void load(
            final @NotNull Path path) {
        if (!Files.exists(path)) {
            LOG.warn(RESOURCE_NOT_FOUND_ERR, path);
        }
        try (final InputStream fileIS = Files.newInputStream(path)) {
            load(fileIS);
        } catch (final IOException e) {
            LOG.warn(READ_ERR, path, e);
        }
    }

    /**
     * Loads the configuration properties from the specified file.
     * 
     * @param file The file to load.
     */
    public void load(
            final @NotNull File file) {
        try (final InputStream fileIS = new FileInputStream(file)) {
            load(fileIS);
        } catch (final FileNotFoundException e) {
            LOG.warn(RESOURCE_NOT_FOUND_ERR, file, e);
        } catch (final IOException e) {
            LOG.warn(READ_ERR, file, e);
        }
    }

    /**
     * Loads the configuration properties from the specified URL.
     * 
     * @param url The URL to load.
     */
    public void load(
            final @NotNull URL url) {
        try (final InputStream urlIS = url.openStream()) {
            load(urlIS);
        } catch (final IOException e) {
            LOG.warn(READ_ERR, url, e);
        }
    }

    /**
     * Loads the configuration properties from the specified
     * {@code InputStream}.
     * 
     * @param docIS The XML document input stream.
     */
    public void load(
            final @NotNull InputStream docIS) {
        try {
            final Document doc = this.builder.parse(docIS);
            add(doc);
        } catch (final SAXException | IOException e) {
            LOG.warn(PARSE_ERR, e);
        }
    }
}
