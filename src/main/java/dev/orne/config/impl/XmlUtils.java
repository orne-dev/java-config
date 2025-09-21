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

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apiguardian.api.API;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import dev.orne.config.ConfigException;

/**
 * Utility class for XML operations.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public final class XmlUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private XmlUtils() {
        // Utility class
    }

    /**
     * Checks if the given XML document contains a specific property.
     * 
     * @param document The XML document to search within.
     * @param property The property name, which may include nested properties.
     * @param propertySeparator The separator used for nested properties.
     * @param attributePrefix The prefix used for attributes.
     * @return {@code true} if the property exists, {@code false} otherwise.
     */
    static boolean contains(
            final @NotNull Document document,
            final @NotNull String property,
            final @NotNull String propertySeparator,
            final @NotNull String attributePrefix) {
        final Element root = document.getDocumentElement();
        final Pair<List<String>, String> split = splitProperty(property, propertySeparator);
        final List<String> parts = split.getLeft();
        final String leaf = split.getRight();
        return findElement(root, parts)
                .flatMap(n -> findLeaf(n, leaf, attributePrefix))
                .isPresent();
    }

    /**
     * Extracts the keys from the XML document.
     * <p>
     * Recursively traverses the XML document, extracting keys
     * from the element's tag name (when element contains text content),
     * its attributes, and its child elements.
     * 
     * @param document The XML document to extract keys from.
     * @param propertySeparator The separator used for nested properties.
     * @param attributePrefix The prefix used for attributes.
     * @return A stream of keys extracted from the document.
     */
    static @NotNull Stream<String> extractKeys(
            final @NotNull Document document,
            final @NotNull String propertySeparator,
            final @NotNull String attributePrefix) {
        final NodeList children = document.getDocumentElement().getChildNodes();
        return IntStream.range(0, children.getLength())
                .mapToObj(children::item)
                .filter(Element.class::isInstance)
                .map(Element.class::cast)
                .flatMap(child -> extractKeys(
                        child,
                        "",
                        propertySeparator,
                        attributePrefix));
    }

    /**
     * Extracts the keys from the XML element and its children.
     * <p>
     * Recursively traverses the XML element, extracting keys
     * from the element's tag name (when element contains text content),
     * its attributes, and its child elements.
     * 
     * @param element The XML element to extract keys from.
     * @param prefix The prefix to prepend to the keys.
     * @return A stream of keys extracted from the element and its children.
     */
    static @NotNull Stream<String> extractKeys(
            final @NotNull Element element,
            final @NotNull String prefix,
            final @NotNull String propertySeparator,
            final @NotNull String attributePrefix) {
        final String value = XmlUtils.getValue(element);
        final Stream<String> elementKey;
        if (value == null) {
            elementKey = Stream.of();
        } else {
            elementKey = Stream.of(prefix + element.getTagName());
        }
        final String childPrefix = prefix + element.getTagName() + propertySeparator;
        final NamedNodeMap attributes = element.getAttributes();
        final Stream<String> attrKeys = IntStream.range(0, attributes.getLength())
                .mapToObj(attributes::item)
                .filter(Attr.class::isInstance)
                .map(node -> childPrefix + attributePrefix + node.getLocalName());
        final NodeList children = element.getChildNodes();
        final Stream<String> childKeys = IntStream.range(0, children.getLength())
                .mapToObj(children::item)
                .filter(Element.class::isInstance)
                .map(Element.class::cast)
                .flatMap(child -> extractKeys(child, childPrefix, propertySeparator, attributePrefix));
        return Stream.concat(
                Stream.concat(
                    elementKey,
                    attrKeys),
                childKeys);
    }

    /**
     * Returns the value of a property in the given XML document.
     * 
     * @param element The XML element to search within.
     * @param property The property name, which may include nested properties.
     * @param propertySeparator The separator used for nested properties.
     * @param attributePrefix The prefix used for attributes.
     * @return An Optional containing the value of the property, or empty if not found.
     */
    static Optional<String> getValue(
            final @NotNull Document document,
            final @NotNull String property,
            final @NotNull String propertySeparator,
            final @NotNull String attributePrefix) {
        final Element root = document.getDocumentElement();
        final Pair<List<String>, String> split = splitProperty(property, propertySeparator);
        final List<String> parts = split.getLeft();
        final String leaf = split.getRight();
        return findElement(root, parts)
                .flatMap(n -> findLeaf(n, leaf, attributePrefix))
                .map(XmlUtils::getLeafValue);
        
    }

    /**
     * Sets the value of a property in the given XML document.
     * If the value is null, it removes the leaf node value.
     * 
     * @param element The XML element to set the property in.
     * @param property The property name, which may include nested properties.
     * @param propertySeparator The separator used for nested properties.
     * @param attributePrefix The prefix used for attributes.
     * @param value The value to set, or null to remove the leaf node.
     */
    static void setValue(
            final @NotNull Document document,
            final @NotNull String property,
            final @NotNull String propertySeparator,
            final @NotNull String attributePrefix,
            final String value) {
        final Element root = document.getDocumentElement();
        final Pair<List<String>, String> split = splitProperty(property, propertySeparator);
        final List<String> parts = split.getLeft();
        final String leaf = split.getRight();
        if (value == null) {
            findElement(root, parts)
                    .flatMap(n -> findLeaf(n, leaf, attributePrefix))
                    .ifPresent(XmlUtils::removeLeafValue);
        } else {
            setLeafValue(
                    getLeaf(
                        getElement(root, parts),
                        leaf,
                        attributePrefix),
                    value);
        }
    }

    /**
     * Returns an Optional containing the element for the given parent and a
     * list of parts representing the path to the element.
     * 
     * @param parent The parent element to search within.
     * @param parts The list of parts representing the path to the element.
     * @return An Optional containing the found element, or empty if not found.
     */
    static @NotNull Optional<Element> findElement(
            final @NotNull Element parent,
            final @NotNull List<String> parts) {
        if (parts.isEmpty()) {
            return Optional.of(parent);
        } else {
            final String childName = parts.get(0);
            final List<String> remainingParts = parts.subList(1, parts.size());
            return getChild(parent, childName)
                    .flatMap(child -> findElement(child, remainingParts));
        }
    }

    /**
     * Returns the element for the given parent and a list of parts.
     * If the element does not exist, it will be created.
     * 
     * @param parent The parent element to which the new child will be added.
     * @param parts The list of parts representing the path to the element.
     * @return The found or newly created element.
     */
    static @NotNull Element getElement(
            final @NotNull Element parent,
            final @NotNull List<String> parts) {
        if (parts.isEmpty()) {
            return parent;
        } else {
            final String childName = parts.get(0);
            final List<String> remainingParts = parts.subList(1, parts.size());
            return getElement(
                    getChild(parent, childName)
                        .orElseGet(() -> createChild(parent, childName)),
                    remainingParts);
        }
    }

    /**
     * Creates a new child element with the specified name, appending it to the
     * given parent element.
     * 
     * @param parent The parent element to which the new child will be added.
     * @param name The local name of the new child element.
     * @return The newly created child element.
     */
    static @NotNull Element createChild(
            final @NotNull Element parent,
            final @NotNull String name) {
        return createChild(parent, parent.getNamespaceURI(), name);
    }

    /**
     * Creates a new child element with the specified name and namespace,
     * appending it to the given parent element.
     * 
     * @param parent The parent element to which the new child will be added.
     * @param namespace The namespace URI for the new child element.
     * @param name The local name of the new child element.
     * @return The newly created child element.
     */
    static @NotNull Element createChild(
            final @NotNull Element parent,
            final String namespace,
            final @NotNull String name) {
        final Element child;
        if (namespace == null) {
            child = parent.getOwnerDocument().createElement(name);
        } else {
            child = parent.getOwnerDocument().createElementNS(namespace, name);
        }
        parent.appendChild(child);
        return child;
    }

    /**
     * Returns an Optional containing the first child element with the specified
     * name from the parent element, or an empty Optional if no such child exists.
     * 
     * @param parent The parent element to search for the child.
     * @param name The local name of the child element to find.
     * @return An Optional containing the found child element, or empty if not found.
     */
    static @NotNull Optional<Element> getChild(
            final @NotNull Element parent,
            final @NotNull String name) {
        final NodeList children = parent.getChildNodes();
        return IntStream.range(0, children.getLength())
                .mapToObj(children::item)
                .filter(Element.class::isInstance)
                .map(Element.class::cast)
                .filter(elem -> elem.getTagName().equals(name))
                .findFirst();
    }

    /**
     * Finds a leaf node (either an attribute or an element) for the given
     * parent element and name.
     * 
     * @param parent The parent element to which the leaf belongs.
     * @param name The name of the leaf node.
     * @param attributePrefix The prefix used to identify attributes.
     * @return An Optional containing the leaf node if found, or empty if not found.
     */
    static Optional<Node> findLeaf(
            final @NotNull Element parent,
            final @NotNull String name,
            final @NotNull String attributePrefix) {
        if (name.startsWith(attributePrefix)) {
            final String attrName = name.substring(attributePrefix.length());
            return Optional.ofNullable(parent.getAttributeNode(attrName));
        } else {
            return getChild(parent, name)
                    .map(Node.class::cast);
        }
    }

    /**
     * Returns a leaf node (either an attribute or an element) for the given
     * parent element and name. If the leaf does not exist, it will be created.
     * 
     * @param parent The parent element to which the leaf belongs.
     * @param name The name of the leaf node.
     * @param attributePrefix The prefix used to identify attributes.
     * @return The leaf node, either an attribute or an element.
     */
    static Node getLeaf(
            final @NotNull Element parent,
            final @NotNull String name,
            final @NotNull String attributePrefix) {
        if (name.startsWith(attributePrefix)) {
            final String attrName = name.substring(attributePrefix.length());
            Attr attr = parent.getAttributeNode(attrName);
            if (attr == null) {
                attr = parent.getOwnerDocument().createAttributeNS(parent.getNamespaceURI(), attrName);
                parent.setAttributeNode(attr);
            }
            return attr;
        } else {
            return getChild(parent, name)
                    .orElseGet(() -> createChild(parent, name));
        }
    }

    /**
     * Returns the value of the given XML node.
     * 
     * @param leaf The XML node to extract the value from.
     * @return The value of the node, or {@code null} if the node is not an
     * attribute or element with a text value.
     */
    static String getLeafValue(
            final @NotNull Node leaf) {
        if (leaf instanceof Attr) {
            return ((Attr) leaf).getValue();
        } else if (leaf instanceof Element) {
            return getValue((Element) leaf);
        } else {
            return null;
        }
    }

    /**
     * Sets the value of the given XML node.
     * 
     * @param leaf The XML node to set the value for.
     * @param value The value to set.
     */
    static void setLeafValue(
            final @NotNull Node leaf,
            final @NotNull String value) {
        if (leaf instanceof Attr) {
            ((Attr) leaf).setValue(value);
        } else if (leaf instanceof Element) {
            setValue((Element) leaf, value);
        }
    }

    /**
     * Removes the value from the given XML node.
     * 
     * @param leaf The XML node from which to remove the value.
     */
    static void removeLeafValue(
            final @NotNull Node leaf) {
        if (leaf instanceof Attr) {
            leaf.getParentNode().removeChild(leaf);
        } else if (leaf instanceof Element) {
            removeValue((Element) leaf);
        }
    }

    /**
     * Returns the text value of the given XML element.
     * 
     * @param element The XML element to extract the value from.
     * @return The trimmed text value of the element, or {@code null} if no text is found.
     */
    static String getValue(
            final @NotNull Element element) {
        final NodeList children = element.getChildNodes();
        final String value = IntStream.range(0, children.getLength())
                .mapToObj(children::item)
                .filter(Text.class::isInstance)
                .map(Node::getNodeValue)
                .filter(Objects::nonNull)
                .collect(Collectors.joining());
        if (StringUtils.isAllBlank(value)) {
            return null;
        } else {
            return value.trim();
        }
    }

    /**
     * Sets the text value of the given XML element.
     * 
     * @param element The XML element to set the value for.
     * @param value The value to set.
     */
    static void setValue(
            final @NotNull Element element,
            final @NotNull String value) {
        removeValue(element);
        final Text textNode = element.getOwnerDocument().createTextNode(value);
        element.appendChild(textNode);
    }

    /**
     * Removes all text nodes from the given XML element.
     * 
     * @param element The XML element from which to remove text nodes.
     */
    static void removeValue(
            final @NotNull Element element) {
        final NodeList children = element.getChildNodes();
        IntStream.range(0, children.getLength())
                .mapToObj(children::item)
                .filter(Text.class::isInstance)
                .forEach(element::removeChild);
    }

    /**
     * Splits a property string into its parent elements' names and
     * leaf node name.
     * 
     * @param property The property string to split.
     * @param propertySeparator The separator used to split the property.
     * @return A pair containing the list of parent elements' names and the
     * leaf node name.
     * @throws IllegalArgumentException If the property is null or empty.
     */
    static @NotNull Pair<List<String>, String> splitProperty(
            final @NotBlank String property,
            final @NotBlank String propertySeparator) {
        final List<String> parts = Arrays.asList(
                StringUtils.splitByWholeSeparator(property, propertySeparator));
        if (parts.isEmpty()) {
            throw new IllegalArgumentException("Property must not be null or empty");
        }
        final String leaf = parts.get(parts.size() - 1);
        return Pair.of(parts.subList(0, parts.size() - 1), leaf);
    }

    /**
     * Converts the given XML document to its string representation.
     * 
     * @param document The XML document to convert.
     * @return The string representation of the XML document.
     * @throws ConfigException If an error occurs during the transformation.
     */
    static @NotNull String getXml(
            final @NotNull Document document) {
        try {
            final TransformerFactory factory = TransformerFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            final Transformer transformer = factory.newTransformer();
            final DOMSource source = new DOMSource(document);
            final StringWriter writer = new StringWriter();
            transformer.transform(source, new StreamResult(writer));
            return writer.toString();
        } catch (final TransformerException e) {
            throw new ConfigException("Error converting XML document to string", e);
        }
    }
}
