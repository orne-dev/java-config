package dev.orne.config;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Jackson {@code ObjectNode} based mutable configuration builder
 * for YAML files.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see ObjectNode
 * @see MutableConfig
 * @see WatchableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface YamlMutableConfigBuilder
extends YamlConfigBaseBuilder, MutableConfigBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlMutableConfigBuilder withParent(
            Config parent);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlMutableConfigBuilder withEncryption(
            ConfigCryptoProvider provider);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlMutableConfigBuilder withDecoder(
            ValueDecoder encoder);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlMutableConfigBuilder withVariableResolution();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlMutableConfigBuilder withDecorator(
            ValueDecorator decorator);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlMutableConfigBuilder add(
            @NotNull ObjectNode values);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlMutableConfigBuilder load(
            @NotNull String path);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlMutableConfigBuilder load(
            @NotNull Path path);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlMutableConfigBuilder load(
            @NotNull File file);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlMutableConfigBuilder load(
            @NotNull URL url);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull WatchableConfig build();
}
