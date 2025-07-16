package dev.orne.config.impl;

import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.Validate;
import org.apiguardian.api.API;

import dev.orne.config.ConfigException;
import dev.orne.config.MutableConfig;
import dev.orne.config.ValueEncoder;

/**
 * Base abstract implementation of mutable configuration properties provider.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-04
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public abstract class AbstractMutableConfig
extends AbstractConfig
implements MutableConfig {

    /** The configuration properties values encoder. */
    private final @NotNull ValueEncoder encoder;

    /**
     * Returns the configuration properties values encoder.
     * 
     * @return The configuration properties values encoder.
     */
    protected @NotNull ValueEncoder getEncoder() {
        return this.encoder;
    }

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param mutableOptions The mutable configuration builder options.
     */
    protected AbstractMutableConfig(
            final @NotNull ConfigOptions options,
            final @NotNull MutableConfigOptions mutableOptions) {
        super(options);
        Objects.requireNonNull(mutableOptions);
        if (options.getCryptoProvider() != null) {
            if (mutableOptions.getEncoder() == null) {
                mutableOptions.setEncoder(options.getCryptoProvider()::encrypt);
            } else {
                mutableOptions.setEncoder(
                        mutableOptions.getEncoder().andThen(options.getCryptoProvider()::encrypt));
            }
        }
        if (mutableOptions.getEncoder() == null) {
            mutableOptions.setEncoder(ValueEncoder.DEFAULT);
        }
        this.encoder = mutableOptions.getEncoder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(
            final @NotBlank String key,
            final String value) {
        Validate.notBlank(key, KEY_BLANK_ERR);
        String encoded = this.encoder.encode(value);
        if (encoded == null) {
            removeInt(key);
        } else {
            setInt(key, encoded);
        }
        getResolver().ifPresent(r -> r.clearCache());
    }

    /**
     * Sets the value of the specified configuration property.
     * 
     * @param key The configuration property.
     * @param value The value to set
     * @throws ConfigException If an error occurs setting the configuration
     * property value
     */
    protected abstract void setInt(
            @NotBlank String key,
            @NotNull String value);

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotBlank String... keys) {
        for (final String key : keys) {
            Validate.notBlank(key, KEY_BLANK_ERR);
        }
        removeInt(keys);
        getResolver().ifPresent(r -> r.clearCache());
    }
    /**
     * Removes the specified configuration properties.
     * 
     * @param keys The configuration properties.
     * @throws ConfigException If an error occurs removing the configuration
     * properties.
     */
    protected abstract void removeInt(
            @NotBlank String... keys);
}
