package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apiguardian.api.API;

/**
 * Apache Commons {@code ImmutableConfiguration} based configuration
 * delegate configuration selection builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @since 1.0
 * @see ImmutableConfiguration
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface CommonsConfigDelegateBuilder  {

    /**
     * Selects the delegated Apache Commons configuration.
     * 
     * @param delegate The delegated Apache Commons configuration.
     * @return Next builder, for method chaining.
     */
    @NotNull CommonsConfigBuilder ofDelegate(
            final @NotNull ImmutableConfiguration delegate);
}
