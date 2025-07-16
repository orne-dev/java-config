package dev.orne.config.impl;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.PropertiesConfigBuilder;

/**
 * Implementation of {@code Properties} based immutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see PropertiesConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class PropertiesConfigBuilderImpl
extends AbstractConfigBuilderImpl<PropertiesConfigBuilderImpl>
implements PropertiesConfigBuilder {

    /** The properties based configuration options. */
    protected final @NotNull PropertiesConfigOptions propertyOptions;

    /**
     * Empty constructor.
     */
    public PropertiesConfigBuilderImpl() {
        super();
        this.propertyOptions = new PropertiesConfigOptions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesConfigBuilderImpl add(
            final @NotNull Properties values) {
        this.propertyOptions.add(values);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesConfigBuilderImpl add(
            final @NotNull Map<String, String> values) {
        this.propertyOptions.add(values);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesConfigBuilderImpl load(
            final @NotNull String path) {
        this.propertyOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesConfigBuilderImpl load(
            final @NotNull Path path) {
        this.propertyOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesConfigBuilderImpl load(
            final @NotNull File file) {
        this.propertyOptions.load(file);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesConfigBuilderImpl load(
            final @NotNull URL url) {
        this.propertyOptions.load(url);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesMutableConfigBuilderImpl mutable() {
        return new PropertiesMutableConfigBuilderImpl(
                this.options,
                new MutableConfigOptions(),
                propertyOptions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesConfigImpl build() {
        return new PropertiesConfigImpl(this.options, this.propertyOptions);
    }
}
