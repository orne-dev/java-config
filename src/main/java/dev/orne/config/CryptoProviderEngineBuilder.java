package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Required engine configuration values cryptography transformations
 * provider builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see ConfigCryptoProvider
 * @see ConfigCryptoEngine
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface CryptoProviderEngineBuilder {

    /**
     * Sets the cryptography transformations engine to use.
     * 
     * @param engine The cryptography transformations engine to use.
     * @return Next builder, for method chaining.
     */
    default @NotNull CryptoProviderKeyBuilder withEngine(
            @NotNull ConfigCryptoEngine engine) {
        return withEngine(engine, false);
    }

    /**
     * Sets the cryptography transformations engine to use.
     * 
     * @param engine The cryptography transformations engine to use.
     * @param destroyEngine If the cryptography engine must be destroyed on
     * provider destruction.
     * @return Next builder, for method chaining.
     */
    @NotNull CryptoProviderKeyBuilder withEngine(
            @NotNull ConfigCryptoEngine engine,
            boolean destroyEngine);

    /**
     * Creates a new cryptography transformations provider builder
     * based on Java Cryptography Architecture using AES with GCM symmetric
     * algorithm.
     * 
     * @param salt The salt to apply to password based secret keys.
     * @return Next builder, for method chaining.
     */
    default @NotNull CryptoProviderKeyBuilder withAesGcmEngine(
            @NotNull byte[] salt) {
        return withAesGcmEngine(salt, true);
    }

    /**
     * Creates a new cryptography transformations provider builder
     * based on Java Cryptography Architecture using AES with GCM symmetric
     * algorithm.
     * 
     * @param salt The salt to apply to password based secret keys.
     * @param destroyEngine If the cryptography engine must be destroyed on
     * provider destruction.
     * @return Next builder, for method chaining.
     */
    @NotNull CryptoProviderKeyBuilder withAesGcmEngine(
            @NotNull byte[] salt,
            boolean destroyEngine);
}
