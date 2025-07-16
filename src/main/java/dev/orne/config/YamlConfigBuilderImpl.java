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
 * Implementation of Jackson {@code ObjectNode} based immutable configuration
 * builder for YAML files.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see YamlConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class YamlConfigBuilderImpl
extends AbstractConfigBuilderImpl<YamlConfigBuilderImpl>
implements YamlConfigBuilder {

    /** The YAML based configuration options. */
    protected final @NotNull YamlConfigOptions yamlOptions;

    /**
     * Empty constructor.
     */
    public YamlConfigBuilderImpl() {
        super();
        this.yamlOptions = new YamlConfigOptions();
    }

    /**
     * Copy constructor.
     * 
     * @param options The configuration options to copy.
     * @param yamlOptions The YAML based configuration options to copy.
     */
    protected YamlConfigBuilderImpl(
            final @NotNull ConfigOptions options,
            final @NotNull YamlConfigOptions yamlOptions) {
        super(options);
        this.yamlOptions = new YamlConfigOptions(yamlOptions);
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    protected YamlConfigBuilderImpl(
            final @NotNull AbstractConfigBuilderImpl<?> copy) {
        super(copy);
        if (copy instanceof YamlConfigBuilderImpl) {
            this.yamlOptions = new YamlConfigOptions(
                    ((YamlConfigBuilderImpl) copy).yamlOptions);
        } else {
            this.yamlOptions = new YamlConfigOptions();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlConfigBuilderImpl withMapper(
            final @NotNull ObjectMapper mapper) {
        this.yamlOptions.setMapper(mapper);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlConfigBuilderImpl setPropertySeparator(
            final @NotEmpty String separator) {
        this.yamlOptions.setPropertySeparator(separator);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlConfigBuilderImpl add(
            final @NotNull ObjectNode values) {
        this.yamlOptions.add(values);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlConfigBuilderImpl load(
            final @NotNull String path) {
        this.yamlOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlConfigBuilderImpl load(
            final @NotNull Path path) {
        this.yamlOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlConfigBuilderImpl load(
            final @NotNull File file) {
        this.yamlOptions.load(file);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlConfigBuilderImpl load(
            final @NotNull URL url) {
        this.yamlOptions.load(url);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlMutableConfigBuilderImpl mutable() {
        return new YamlMutableConfigBuilderImpl(
                this.options,
                new MutableConfigOptions(),
                yamlOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull YamlConfig build() {
        return new YamlConfig(this.options, this.yamlOptions);
    }
}
