package dev.orne.config;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.crypto.ConfigCryptoProvider;

/**
 * {@code Properties} based mutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see Properties
 * @see MutableConfig
 * @see WatchableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface PropertiesMutableConfigBuilder
extends PropertiesConfigBaseBuilder, MutableConfigBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesMutableConfigBuilder withParent(
            Config parent);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesMutableConfigBuilder withEncryption(
            ConfigCryptoProvider provider);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesMutableConfigBuilder withDecoder(
            ValueDecoder encoder);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesMutableConfigBuilder withVariableResolution();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesMutableConfigBuilder withDecorator(
            ValueDecorator decorator);

    /**
     * {@inheritDoc}
     */
    @NotNull PropertiesMutableConfigBuilder add(
            @NotNull Properties values);

    /**
     * {@inheritDoc}
     */
    @NotNull PropertiesMutableConfigBuilder add(
            @NotNull Map<String, String> values);

    /**
     * {@inheritDoc}
     */
    @NotNull PropertiesMutableConfigBuilder load(
            @NotNull String path);

    /**
     * {@inheritDoc}
     */
    @NotNull PropertiesMutableConfigBuilder load(
            @NotNull Path path);

    /**
     * {@inheritDoc}
     */
    @NotNull PropertiesMutableConfigBuilder load(
            @NotNull File file);

    /**
     * {@inheritDoc}
     */
    @NotNull PropertiesMutableConfigBuilder load(
            @NotNull URL url);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesMutableConfig build();
}
