package dev.orne.config.impl;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.orne.config.JsonConfigBuilder;

/**
 * Implementation of Jackson {@code ObjectNode} based immutable configuration
 * builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see JsonConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class JsonConfigBuilderImpl
extends AbstractConfigBuilderImpl<JsonConfigBuilderImpl>
implements JsonConfigBuilder<JsonConfigBuilderImpl> {

    /** The JSON based configuration options. */
    protected final @NotNull JsonConfigOptions jsonOptions;

    /**
     * Empty constructor.
     */
    public JsonConfigBuilderImpl() {
        super();
        this.jsonOptions = new JsonConfigOptions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonConfigBuilderImpl withMapper(
            final @NotNull ObjectMapper mapper) {
        this.jsonOptions.setMapper(mapper);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonConfigBuilderImpl setPropertySeparator(
            final @NotEmpty String separator) {
        this.jsonOptions.setPropertySeparator(separator);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonConfigBuilderImpl add(
            final @NotNull ObjectNode values) {
        this.jsonOptions.add(values);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonConfigBuilderImpl load(
            final @NotNull String path) {
        this.jsonOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonConfigBuilderImpl load(
            final @NotNull Path path) {
        this.jsonOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonConfigBuilderImpl load(
            final @NotNull File file) {
        this.jsonOptions.load(file);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonConfigBuilderImpl load(
            final @NotNull URL url) {
        this.jsonOptions.load(url);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonMutableConfigBuilderImpl mutable() {
        return new JsonMutableConfigBuilderImpl(
                this.options,
                new MutableConfigOptions(),
                jsonOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonConfigImpl build() {
        return new JsonConfigImpl(this.options, this.jsonOptions);
    }
}
