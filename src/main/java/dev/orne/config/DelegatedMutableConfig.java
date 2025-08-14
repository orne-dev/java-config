package dev.orne.config;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * A mutable configuration that delegates all operations to another
 * configuration.
 * This class is useful for creating a proxy or wrapper around an existing
 * configuration instance, allowing for additional behavior or modifications
 * without changing the original configuration.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-08
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0")
public class DelegatedMutableConfig
extends DelegatedConfig
implements MutableConfig {

    /**
     * Creates a new instance.
     *
     * @param delegate The configuration to delegate to.
     */
    public DelegatedMutableConfig(
            final @NotNull MutableConfig delegate) {
        super(delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull MutableConfig getDelegate() {
        return (MutableConfig) super.getDelegate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(
            final @NotBlank String key,
            final String value) {
        getDelegate().set(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(
            final @NotBlank String key,
            final Boolean value) {
        getDelegate().set(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(
            final @NotBlank String key,
            final Integer value) {
        getDelegate().set(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(
            final @NotBlank String key,
            final Long value) {
        getDelegate().set(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(
            final @NotBlank String... keys) {
        getDelegate().remove(keys);
    }
}
