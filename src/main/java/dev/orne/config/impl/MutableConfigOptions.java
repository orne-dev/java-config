package dev.orne.config.impl;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.Config;
import dev.orne.config.ValueEncoder;

/**
 * Options of mutable configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see Config
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class MutableConfigOptions {

    /** The configuration properties values encoder. */
    protected ValueEncoder encoder;

    /**
     * Empty constructor.
     */
    public MutableConfigOptions() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public MutableConfigOptions(
            final @NotNull MutableConfigOptions copy) {
        super();
        this.encoder = copy.encoder;
    }

    /**
     * Returns the configuration properties values encoder.
     * 
     * @return The configuration properties values encoder.
     */
    public ValueEncoder getEncoder() {
        return this.encoder;
    }

    /**
     * Sets the configuration properties values encoder.
     * 
     * @param encoder The configuration properties values encoder.
     */
    public void setEncoder(
            final ValueEncoder encoder) {
        this.encoder = encoder;
    }
}
