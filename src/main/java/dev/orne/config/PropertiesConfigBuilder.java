package dev.orne.config;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * {@code Properties} based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see Properties
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface PropertiesConfigBuilder
extends PropertiesConfigBaseBuilder, MutableCapableConfigBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesConfigBuilder withParent(
            Config parent);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesConfigBuilder withEncryption(
            ConfigCryptoProvider provider);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesConfigBuilder withDecoder(
            ValueDecoder encoder);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesConfigBuilder withVariableResolution();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesConfigBuilder withDecorator(
            ValueDecorator decorator);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesConfigBuilder add(
            @NotNull Properties values);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesConfigBuilder add(
            @NotNull Map<String, String> values);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesConfigBuilder load(
            @NotNull String path);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesConfigBuilder load(
            @NotNull Path path);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesConfigBuilder load(
            @NotNull File file);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesConfigBuilder load(
            @NotNull URL url);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull PropertiesMutableConfigBuilder mutable();
}
