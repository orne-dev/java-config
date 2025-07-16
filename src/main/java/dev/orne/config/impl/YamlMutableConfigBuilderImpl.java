package dev.orne.config.impl;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.orne.config.JsonMutableConfigBuilder;
import dev.orne.config.YamlMutableConfigBuilder;


/**
 * Implementation of Jackson {@code ObjectNode} based mutable configuration
 * builder for YAML files.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see JsonMutableConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class YamlMutableConfigBuilderImpl
extends AbstractMutableConfigBuilderImpl<YamlMutableConfigBuilderImpl>
implements YamlMutableConfigBuilder {

    /** The YAML based configuration options. */
    protected final @NotNull YamlConfigOptions yamlOptions;

    /**
     * Copy constructor.
     * 
     * @param options The configuration options to copy.
     * @param mutableOptions The mutable configuration options to copy.
     * @param yamlOptions The YAML based configuration options to copy.
     */
    protected YamlMutableConfigBuilderImpl(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions,
            final @NotNull YamlConfigOptions yamlOptions) {
        super(options, mutableOptions);
        this.yamlOptions = new YamlConfigOptions(yamlOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlMutableConfigBuilderImpl withMapper(
            final @NotNull ObjectMapper mapper) {
        this.yamlOptions.setMapper(mapper);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlMutableConfigBuilderImpl setPropertySeparator(
            final @NotEmpty String separator) {
        this.yamlOptions.setPropertySeparator(separator);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlMutableConfigBuilderImpl add(
            final @NotNull ObjectNode values) {
        this.yamlOptions.add(values);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlMutableConfigBuilderImpl load(
            final @NotNull String path) {
        this.yamlOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlMutableConfigBuilderImpl load(
            final @NotNull Path path) {
        this.yamlOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlMutableConfigBuilderImpl load(
            final @NotNull File file) {
        this.yamlOptions.load(file);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlMutableConfigBuilderImpl load(
            final @NotNull URL url) {
        this.yamlOptions.load(url);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlMutableConfigImpl build() {
        return new YamlMutableConfigImpl(
                this.options,
                this.mutableOptions,
                this.yamlOptions);
    }
}
