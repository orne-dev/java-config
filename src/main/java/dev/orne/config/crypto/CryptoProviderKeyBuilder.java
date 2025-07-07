package dev.orne.config.crypto;

import javax.crypto.SecretKey;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Required secret key configuration values cryptography transformations
 * provider builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see ConfigCryptoProvider
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface CryptoProviderKeyBuilder {

    /**
     * Sets the password used as secret key.
     * 
     * @param password The password.
     * @return Next builder, for method chaining.
     * @throws ConfigCryptoProviderException If an error occurs creating the
     * secret key.
     */
    @NotNull CryptoProviderBuilder withSecretKey(
            @NotNull char[] password);

    /**
     * Sets the secret key.
     * 
     * @param key The secret key.
     * @return Next builder, for method chaining.
     */
    @NotNull CryptoProviderBuilder withSecretKey(
            @NotNull SecretKey key);
}
