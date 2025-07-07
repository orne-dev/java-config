package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.Configuration;
import org.apiguardian.api.API;

import dev.orne.config.crypto.ConfigCryptoProvider;

/**
 * Apache Commons {@code Configuration} based mutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see Configuration
 * @see MutableConfig
 * @see WatchableConfig
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface CommonsMutableConfigBuilder
extends MutableConfigBuilder {

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull CommonsMutableConfigBuilder withParent(
            Config parent);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull CommonsMutableConfigBuilder withEncryption(
            ConfigCryptoProvider provider);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull CommonsMutableConfigBuilder withDecoder(
            ValueDecoder encoder);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull CommonsMutableConfigBuilder withVariableResolution();

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull CommonsMutableConfigBuilder withDecorator(
            ValueDecorator decorator);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull CommonsMutableConfig build();
}
