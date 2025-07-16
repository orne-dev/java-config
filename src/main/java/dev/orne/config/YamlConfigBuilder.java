package dev.orne.config;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.orne.config.crypto.ConfigCryptoProvider;

/**
 * Jackson {@code ObjectNode} based configuration builder
 * for YAML files.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see ObjectNode
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface YamlConfigBuilder
extends YamlConfigBaseBuilder, MutableCapableConfigBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlConfigBuilder withParent(
            Config parent);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlConfigBuilder withEncryption(
            ConfigCryptoProvider provider);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlConfigBuilder withDecoder(
            ValueDecoder encoder);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlConfigBuilder withVariableResolution();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlConfigBuilder withDecorator(
            ValueDecorator decorator);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlConfigBuilder add(
            @NotNull ObjectNode values);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlConfigBuilder load(
            @NotNull String path);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlConfigBuilder load(
            @NotNull Path path);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlConfigBuilder load(
            @NotNull File file);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlConfigBuilder load(
            @NotNull URL url);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlMutableConfigBuilder mutable();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull YamlConfig build();
}
