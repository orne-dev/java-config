package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * {@code System} properties based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see System
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface SystemConfigBuilder
extends ConfigBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull SystemConfigBuilder withParent(
            Config parent);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull SystemConfigBuilder withEncryption(
            ConfigCryptoProvider provider);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull SystemConfigBuilder withDecoder(
            ValueDecoder encoder);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull SystemConfigBuilder withVariableResolution();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull SystemConfigBuilder withDecorator(
            ValueDecorator decorator);
}
