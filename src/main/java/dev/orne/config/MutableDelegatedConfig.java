package dev.orne.config;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Delegated {@code MutableConfig} implementation.
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2020-04
 * @since 0.2
 */
public class MutableDelegatedConfig
extends DelegatedConfig
implements MutableConfig {

    /**
     * Creates a new instance.
     * 
     * @param delegate The delegate {@code MutableConfig} instance
     */
    public MutableDelegatedConfig(
            @NotNull
            final MutableConfig delegate) {
        super(delegate);
    }

    /**
     * Returns the delegate {@code MutableConfig} instance.
     * 
     * @return The delegate {@code MutableConfig} instance
     */
    @Override
    @NotNull
    protected MutableConfig getDelegate() {
        return (MutableConfig) super.getDelegate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(
            @NotBlank
            final String key,
            final Object value)
    throws ConfigException {
        getDelegate().set(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(
            @NotBlank
            final String key)
    throws ConfigException {
        getDelegate().remove(key);
    }
}
