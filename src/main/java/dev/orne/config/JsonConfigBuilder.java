package dev.orne.config;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import com.fasterxml.jackson.databind.node.ObjectNode;

import dev.orne.config.crypto.ConfigCryptoProvider;

/**
 * Jackson {@code ObjectNode} based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see ObjectNode
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface JsonConfigBuilder
extends JsonConfigBaseBuilder, MutableCapableConfigBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonConfigBuilder withParent(
            Config parent);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonConfigBuilder withEncryption(
            ConfigCryptoProvider provider);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonConfigBuilder withDecoder(
            ValueDecoder encoder);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonConfigBuilder withVariableResolution();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonConfigBuilder withDecorator(
            ValueDecorator decorator);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonConfigBuilder add(
            @NotNull ObjectNode values);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonConfigBuilder load(
            @NotNull String path);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonConfigBuilder load(
            @NotNull Path path);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonConfigBuilder load(
            @NotNull File file);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonConfigBuilder load(
            @NotNull URL url);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonMutableConfigBuilder mutable();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull JsonConfig build();
}
