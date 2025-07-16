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
     * Empty constructor.
     */
    protected YamlMutableConfigBuilderImpl() {
        super();
        this.yamlOptions = new YamlConfigOptions();
    }

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
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    protected YamlMutableConfigBuilderImpl(
            final @NotNull AbstractConfigBuilderImpl<?> copy) {
        super(copy);
        if (copy instanceof YamlConfigBuilderImpl) {
            this.yamlOptions = new YamlConfigOptions(
                    ((YamlConfigBuilderImpl) copy).yamlOptions);
        } else if (copy instanceof YamlMutableConfigBuilderImpl) {
            this.yamlOptions = new YamlConfigOptions(
                    ((YamlMutableConfigBuilderImpl) copy).yamlOptions);
        } else {
            this.yamlOptions = new YamlConfigOptions();
        }
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
    public @NotNull YamlMutableConfig build() {
        return new YamlMutableConfig(
                this.options,
                this.mutableOptions,
                this.yamlOptions);
    }
}
