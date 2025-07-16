package dev.orne.config;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.orne.config.crypto.ConfigCryptoProvider;

/**
 * Jackson {@code ObjectNode} based mutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see ObjectNode
 * @see MutableConfig
 * @see WatchableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface JsonMutableConfigBuilder
extends JsonConfigBaseBuilder, MutableConfigBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonMutableConfigBuilder withParent(
            Config parent);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonMutableConfigBuilder withEncryption(
            ConfigCryptoProvider provider);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonMutableConfigBuilder withDecoder(
            ValueDecoder encoder);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonMutableConfigBuilder withVariableResolution();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonMutableConfigBuilder withDecorator(
            ValueDecorator decorator);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonMutableConfigBuilder add(
            @NotNull ObjectNode values);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonMutableConfigBuilder load(
            @NotNull String path);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonMutableConfigBuilder load(
            @NotNull Path path);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonMutableConfigBuilder load(
            @NotNull File file);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonMutableConfigBuilder load(
            @NotNull URL url);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonMutableConfig build();
}
