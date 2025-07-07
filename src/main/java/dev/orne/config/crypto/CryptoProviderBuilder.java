package dev.orne.config.crypto;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Configuration values cryptography transformations provider builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see ConfigCryptoProvider
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface CryptoProviderBuilder {

    /**
     * Enables {@code Cipher}s pooling.
     * 
     * @return This builder, for method chaining.
     */
    @NotNull CryptoProviderBuilder pooled();

    /**
     * Creates a new cryptography transformations provider.
     * 
     * @return The cryptography transformations provider.
     */
    @NotNull ConfigCryptoProvider build();
}
