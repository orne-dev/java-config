package dev.orne.config;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * Implementation of Jackson {@code ObjectNode} based mutable configuration
 * builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see JsonMutableConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class JsonMutableConfigBuilderImpl
extends AbstractMutableConfigBuilderImpl<JsonMutableConfigBuilderImpl>
implements JsonMutableConfigBuilder {

    /** The JSON based configuration options. */
    protected final @NotNull JsonConfigOptions jsonOptions;

    /**
     * Empty constructor.
     */
    protected JsonMutableConfigBuilderImpl() {
        super();
        this.jsonOptions = new JsonConfigOptions();
    }

    /**
     * Copy constructor.
     * 
     * @param options The configuration options to copy.
     * @param mutableOptions The mutable configuration options to copy.
     * @param jsonOptions The JSON based configuration options to copy.
     */
    protected JsonMutableConfigBuilderImpl(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions,
            final @NotNull JsonConfigOptions jsonOptions) {
        super(options, mutableOptions);
        this.jsonOptions = new JsonConfigOptions(jsonOptions);
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    protected JsonMutableConfigBuilderImpl(
            final @NotNull AbstractConfigBuilderImpl<?> copy) {
        super(copy);
        if (copy instanceof JsonConfigBuilderImpl) {
            this.jsonOptions = new JsonConfigOptions(
                    ((JsonConfigBuilderImpl) copy).jsonOptions);
        } else if (copy instanceof JsonMutableConfigBuilderImpl) {
            this.jsonOptions = new JsonConfigOptions(
                    ((JsonMutableConfigBuilderImpl) copy).jsonOptions);
        } else {
            this.jsonOptions = new JsonConfigOptions();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonMutableConfigBuilderImpl withMapper(
            final @NotNull ObjectMapper mapper) {
        this.jsonOptions.setMapper(mapper);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonMutableConfigBuilderImpl setPropertySeparator(
            final @NotEmpty String separator) {
        this.jsonOptions.setPropertySeparator(separator);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonMutableConfigBuilderImpl add(
            final @NotNull ObjectNode values) {
        this.jsonOptions.add(values);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonMutableConfigBuilderImpl load(
            final @NotNull String path) {
        this.jsonOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonMutableConfigBuilderImpl load(
            final @NotNull Path path) {
        this.jsonOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonMutableConfigBuilderImpl load(
            final @NotNull File file) {
        this.jsonOptions.load(file);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonMutableConfigBuilderImpl load(
            final @NotNull URL url) {
        this.jsonOptions.load(url);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JsonMutableConfig build() {
        return new JsonMutableConfig(
                this.options,
                this.mutableOptions,
                this.jsonOptions);
    }
}
