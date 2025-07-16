package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Available configuration instances provider builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see ConfigProvider
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface ConfigProviderBuilder {

    /**
     * Adds a configuration to the provider.
     * 
     * @param config The configuration to add
     * @return This builder for method chaining
     */
    @NotNull ConfigProviderBuilder addConfig(
            @NotNull Config config);

    /**
     * Builds the {@code ConfigProvider} instance.
     * 
     * @return The built {@code ConfigProvider} instance
     */
    @NotNull ConfigProvider build();
}
