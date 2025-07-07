package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.crypto.ConfigCryptoProvider;

/**
 * Configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface ConfigBuilder {

    /**
     * Sets the parent configuration.
     * 
     * @param parent The parent configuration.
     * @return This instance, for method chaining.
     */
    @NotNull ConfigBuilder withParent(
            Config parent);

    /**
     * Sets the configuration properties values cryptography
     * transformations provider.
     * 
     * @param provider The cryptography transformations provider.
     * @return This instance, for method chaining.
     */
    @NotNull ConfigBuilder withEncryption(
            ConfigCryptoProvider provider);

    /**
     * Sets the configuration properties values decoder.
     * Applied to property values contained in the builded configuration
     * instance (not to the parent configuration properties, if any).
     * 
     * @param encoder The configuration properties values decoder.
     * @return This instance, for method chaining.
     */
    @NotNull ConfigBuilder withDecoder(
            ValueDecoder encoder);

    /**
     * Enables configuration property values variable resolution.
     * 
     * @return This instance, for method chaining.
     * @see VariableResolver
     */
    @NotNull ConfigBuilder withVariableResolution();

    /**
     * Sets the configuration properties values decorator.
     * Applied to property values returned by the builded configuration
     * instance (whatever its source).
     * 
     * @param decorator The configuration properties values decorator.
     * @return This instance, for method chaining.
     */
    @NotNull ConfigBuilder withDecorator(
            ValueDecorator decorator);

    /**
     * Creates the configuration instance.
     * 
     * @return The configuration instance.
     */
    @NotNull Config build();
}
