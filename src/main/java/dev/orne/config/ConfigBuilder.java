package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @param <S> The concrete type of the builder.
 * @since 1.0
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface ConfigBuilder<S extends ConfigBuilder<S>> {

    /**
     * Sets the parent configuration.
     * 
     * @param parent The parent configuration.
     * @return This instance, for method chaining.
     */
    @NotNull S withParent(
            Config parent);

    /**
     * Sets the configuration properties values cryptography
     * transformations provider.
     * 
     * @param provider The cryptography transformations provider.
     * @return This instance, for method chaining.
     */
    @NotNull S withEncryption(
            ConfigCryptoProvider provider);

    /**
     * Sets the configuration properties values decoder.
     * Applied to property values contained in the builded configuration
     * instance (not to the parent configuration properties, if any).
     * 
     * @param encoder The configuration properties values decoder.
     * @return This instance, for method chaining.
     */
    @NotNull S withDecoder(
            ValueDecoder encoder);

    /**
     * Enables configuration property values variable resolution.
     * 
     * @return This instance, for method chaining.
     */
    @NotNull S withVariableResolution();

    /**
     * Sets the configuration properties values decorator.
     * Applied to property values returned by the builded configuration
     * instance (whatever its source).
     * 
     * @param decorator The configuration properties values decorator.
     * @return This instance, for method chaining.
     */
    @NotNull S withDecorator(
            ValueDecorator decorator);

    /**
     * Creates the configuration instance.
     * 
     * @return The configuration instance.
     */
    @NotNull Config build();
}
