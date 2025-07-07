package dev.orne.config;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;


/**
 * Implementation of {@code Properties} based mutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see PropertiesMutableConfigBuilder
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class PropertiesMutableConfigBuilderImpl
extends AbstractMutableConfigBuilderImpl<PropertiesMutableConfigBuilderImpl>
implements PropertiesMutableConfigBuilder {

    /** The properties based configuration options. */
    protected final @NotNull PropertiesConfigOptions propertyOptions;

    /**
     * Empty constructor.
     */
    protected PropertiesMutableConfigBuilderImpl() {
        super();
        this.propertyOptions = new PropertiesConfigOptions();
    }

    /**
     * Copy constructor.
     * 
     * @param options The configuration options to copy.
     * @param mutableOptions The mutable configuration options to copy.
     * @param propertyOptions The properties based configuration options to
     * copy.
     */
    protected PropertiesMutableConfigBuilderImpl(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions,
            final @NotNull PropertiesConfigOptions propertyOptions) {
        super(options, mutableOptions);
        this.propertyOptions = new PropertiesConfigOptions(propertyOptions);
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    protected PropertiesMutableConfigBuilderImpl(
            final @NotNull AbstractConfigBuilderImpl<?> copy) {
        super(copy);
        if (copy instanceof PropertiesConfigBuilderImpl) {
            this.propertyOptions = new PropertiesConfigOptions(
                    ((PropertiesConfigBuilderImpl) copy).propertyOptions);
        } else if (copy instanceof PropertiesMutableConfigBuilderImpl) {
            this.propertyOptions = new PropertiesConfigOptions(
                    ((PropertiesMutableConfigBuilderImpl) copy).propertyOptions);
        } else {
            this.propertyOptions = new PropertiesConfigOptions();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesMutableConfigBuilderImpl add(
            final @NotNull Properties values) {
        this.propertyOptions.add(values);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesMutableConfigBuilderImpl add(
            final @NotNull Map<String, String> values) {
        this.propertyOptions.add(values);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesMutableConfigBuilderImpl load(
            final @NotNull String path) {
        this.propertyOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesMutableConfigBuilderImpl load(
            final @NotNull Path path) {
        this.propertyOptions.load(path);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesMutableConfigBuilderImpl load(
            final @NotNull File file) {
        this.propertyOptions.load(file);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesMutableConfigBuilderImpl load(
            final @NotNull URL url) {
        this.propertyOptions.load(url);
        return thisBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull PropertiesMutableConfig build() {
        return new PropertiesMutableConfig(
                this.options,
                this.mutableOptions,
                this.propertyOptions);
    }
}
