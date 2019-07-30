package dev.orne.config;

/**
 * Interface for classes suitable for configuration.
 * Allows to be configured by an instance of {@code Config} at runtime.
 * 
 * @version 1.0
 * @author (w) Iker Hernaez<i.hernaez@hif-soft.net>
 * @since 1.0, 2019-07
 * @see Config
 */
public interface Configurable {

    /**
     * Sets the config to apply to this property.
     * 
     * @param config The config to apply
     */
    void configure(Config config);

    /**
     * Returns {@code true} if this instance is already configured.
     * 
     * @return {@code true} if this instance is already configured
     */
    boolean isConfigured();
}
