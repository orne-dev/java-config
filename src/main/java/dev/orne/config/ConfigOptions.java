package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.crypto.ConfigCryptoProvider;

/**
 * Options of configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see Config
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class ConfigOptions {

    /** The parent configuration. */
    private Config parent;
    /** The cryptography transformations provider. */
    private ConfigCryptoProvider cryptoProvider;
    /** The configuration values decoder. */
    private ValueDecoder decoder;
    /** If configuration property values variable resolution is enabled. */
    private boolean variableResolutionEnabled;
    /** The configuration properties values decorator. */
    private ValueDecorator decorator;

    /**
     * Empty constructor.
     */
    public ConfigOptions() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public ConfigOptions(
            final @NotNull ConfigOptions copy) {
        super();
        this.parent = copy.parent;
        this.cryptoProvider = copy.cryptoProvider;
        this.decoder = copy.decoder;
        this.variableResolutionEnabled = copy.variableResolutionEnabled;
        this.decorator = copy.decorator;
    }

    /**
     * Returns the parent configuration.
     * 
     * @return The parent configuration.
     */
    public Config getParent() {
        return this.parent;
    }

    /**
     * Sets the parent configuration.
     * 
     * @param parent The parent configuration.
     */
    public void setParent(
            final Config parent) {
        this.parent = parent;
    }

    /**
     * Returns the cryptography transformations provider.
     * 
     * @return The cryptography transformations provider.
     */
    public ConfigCryptoProvider getCryptoProvider() {
        return this.cryptoProvider;
    }

    /**
     * Sets the cryptography transformations provider.
     * 
     * @param provider The cryptography transformations provider.
     */
    public void setCryptoProvider(
            final ConfigCryptoProvider provider) {
        this.cryptoProvider = provider;
    }

    /**
     * Returns the configuration values decoder.
     * 
     * @return The configuration values decoder.
     */
    public ValueDecoder getDecoder() {
        return this.decoder;
    }

    /**
     * Sets the configuration values decoder.
     * 
     * @param decoder The configuration values decoder.
     */
    public void setDecoder(
            final ValueDecoder decoder) {
        this.decoder = decoder;
    }

    /**
     * Returns {@code true} if configuration property values variable
     * resolution is enabled.
     * 
     * @return If configuration property values variable resolution is enabled.
     */
    public boolean isVariableResolutionEnabled() {
        return this.variableResolutionEnabled;
    }

    /**
     * Sets if configuration property values variable resolution is enabled.
     * 
     * @param enabled If configuration property values variable resolution is
     * enabled.
     */
    public void setVariableResolutionEnabled(
            final boolean enabled) {
        this.variableResolutionEnabled = enabled;
    }

    /**
     * Returns the configuration properties values decorator.
     * 
     * @return The configuration properties values decorator.
     */
    public ValueDecorator getDecorator() {
        return this.decorator;
    }

    /**
     * Sets the configuration properties values decorator.
     * 
     * @param decorator The configuration properties values decorator.
     */
    public void setDecorator(
            final ValueDecorator decorator) {
        this.decorator = decorator;
    }
}
