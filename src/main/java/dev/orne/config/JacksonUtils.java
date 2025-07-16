package dev.orne.config;

import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonNode.OverwriteMode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Utility class for JSON operations based on Jackson.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public final class JacksonUtils {

    /** The JSON node factory. */
    public static final JsonNodeFactory NODE_FACTORY =
            new JsonNodeFactory(false);
    /** JSON pointer segments separator as string. */
    protected static final String POINTER_SEPARATOR = String.valueOf(JsonPointer.SEPARATOR);

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private JacksonUtils() {
        // Utility class
    }

    /**
     * Resolves configuration keys to JSON pointer expressions by replacing
     * properties separator with the JSON pointer segment separator.
     * 
     * @param key The configuration key.
     * @param separator The properties separator to use.
     * @return The JSON pointer expression.
     */
    public static @NotNull JsonPointer propertyToPointer(
            final @NotBlank String key,
            final @NotBlank String separator) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(separator);
        final String pointer = POINTER_SEPARATOR + key.replace(separator, POINTER_SEPARATOR);
        return JsonPointer.compile(pointer);
    }

    /**
     * Sets the value of a JSON node at the specified key.
     * 
     * @param jsonObject The JSON object to modify.
     * @param separator The properties separator.
     * @param key The configuration key.
     * @param value The value to set.
     */
    public static void setNodeValue(
            final @NotNull ObjectNode jsonObject,
            final @NotBlank String separator,
            final @NotBlank String key,
            final @NotNull String value) {
        final JsonPointer pointer = propertyToPointer(key, separator);
        setNodeValue(jsonObject, pointer, value);
    }

    /**
     * Sets the value of a JSON node at the specified pointer.
     * 
     * @param jsonObject The JSON object to modify.
     * @param pointer The JSON pointer to the node.
     * @param value The value to set.
     */
    public static void setNodeValue(
            final @NotNull ObjectNode jsonObject,
            final @NotNull JsonPointer pointer,
            final String value) {
        final JsonPointer lastPointer = pointer.last();
        final JsonNode valueNode = value == null ? NODE_FACTORY.nullNode() : NODE_FACTORY.textNode(value);
        if (lastPointer.mayMatchElement()) {
            final ArrayNode parent = jsonObject.withArray(pointer.head(), OverwriteMode.NULLS, true);
            final int index = lastPointer.getMatchingIndex();
            if (parent.size() <= index) {
                for (int i = parent.size(); i <= index; i++) {
                    parent.add(NODE_FACTORY.nullNode());
                }
            }
            parent.set(lastPointer.getMatchingIndex(), valueNode);
        } else {
            final ObjectNode parent = jsonObject.withObject(pointer.head(), OverwriteMode.NULLS, true);
            parent.set(lastPointer.getMatchingProperty(), valueNode);
        }
    }

    /**
     * Removes a node from the JSON object at the specified key.
     * 
     * @param jsonObject The JSON object to modify.
     * @param separator The properties separator.
     * @param key The configuration key.
     */
    public static void removeNode(
            final @NotNull ObjectNode jsonObject,
            final @NotBlank String separator,
            final @NotBlank String key) {
        final JsonPointer pointer = propertyToPointer(key, separator);
        removeNode(jsonObject, pointer);
    }

    /**
     * Removes a node from the JSON object at the specified pointer.
     * 
     * @param jsonObject The JSON object to modify.
     * @param pointer The JSON pointer to the node to remove.
     */
    public static void removeNode(
            final @NotNull ObjectNode jsonObject,
            final @NotNull JsonPointer pointer) {
        final JsonNode parent = jsonObject.at(pointer.head());
        if (!parent.isMissingNode()) {
            if (parent.isObject()) {
                ((ObjectNode) parent).remove(pointer.last().getMatchingProperty());
            } else if (parent.isArray()) {
                ((ArrayNode) parent).remove(pointer.last().getMatchingIndex());
            }
        }
    }
}
