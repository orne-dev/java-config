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

import java.io.IOException;
import java.io.Writer;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apiguardian.api.API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

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
extends XmlConfigImpl
implements FileWatchableConfig {

    /** The class logger. */
    private static final Logger LOG = LoggerFactory.getLogger(XmlMutableConfigImpl.class);

    /** The transformer factory for XML documents. */
    private static final TransformerFactory TRANS_FACT;
    static {
        TRANS_FACT = TransformerFactory.newInstance();
        try {
            TRANS_FACT.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        } catch (final TransformerConfigurationException e) {
            LOG.warn("Error setting secure processing feature on XML DocumentBuilderFactory", e);
        }
        try {
            TRANS_FACT.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        } catch (final IllegalArgumentException e) {
            LOG.debug("Error disabling external DTD access on XML DocumentBuilderFactory", e);
        }
        try {
            TRANS_FACT.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        } catch (final IllegalArgumentException e) {
            LOG.debug("Error disabling external stylesheet access on XML DocumentBuilderFactory", e);
        }
    }

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param mutableOptions The mutable configuration builder options.
     * @param xmlOptions The XML based configuration builder options.
     */
    public XmlMutableConfigImpl(
            final ConfigOptions options,
            final MutableConfigOptions mutableOptions,
            final XmlConfigOptions xmlOptions) {
        super(options, mutableOptions, xmlOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(
            final String key,
            final @Nullable String value) {
        super.set(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setInt(
            final String key,
            final String value) {
        XmlUtils.setValue(
                getDocument(),
                key,
                getPropertySeparator(),
                getAttributePrefix(),
                value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(
            final String... keys) {
        super.remove(keys);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeInt(
            final String... keys) {
        for (final String key : keys) {
            XmlUtils.setValue(
                    getDocument(),
                    key,
                    getPropertySeparator(),
                    getAttributePrefix(),
                    null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(
            final Listener listener) {
        super.addListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(
            final Listener listener) {
        super.removeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(
            final Writer destination)
    throws IOException {
        try {
            final Transformer transformer = TRANS_FACT.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            final DOMSource source = new DOMSource(getDocument());
            final StreamResult result = new StreamResult(destination);
            transformer.transform(source, result);
        } catch (final TransformerException e) {
            throw new IOException("Error saving XML configuration", e);
        }
    }
}
