package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apiguardian.api.API;

/**
 * Options of Apache Commons Configuration based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see CommonsConfig
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class CommonsConfigOptions {

    /** The delegated Apache Commons configuration. */
    protected ImmutableConfiguration delegated;

    /**
     * Empty constructor.
     */
    public CommonsConfigOptions() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public CommonsConfigOptions(
            final @NotNull CommonsConfigOptions copy) {
        super();
        this.delegated = copy.delegated;
    }

    /**
     * Returns the delegated Apache Commons configuration.
     * 
     * @return The delegated Apache Commons configuration.
     */
    public ImmutableConfiguration getDelegated() {
        return this.delegated;
    }

    /**
     * Sets the delegated Apache Commons configuration.
     * 
     * @param delegated The delegated Apache Commons configuration.
     */
    public void setDelegated(
            final ImmutableConfiguration delegated) {
        this.delegated = delegated;
    }
}
