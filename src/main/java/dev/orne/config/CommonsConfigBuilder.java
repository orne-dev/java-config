package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apiguardian.api.API;

/**
 * Apache Commons {@code ImmutableConfiguration} based configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-07
 * @param <S> The concrete type of the builder.
 * @since 1.0
 * @see ImmutableConfiguration
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface CommonsConfigBuilder<S extends CommonsConfigBuilder<S>>
extends MutableCapableConfigBuilder<S> {

    /**
     * Selects the delegated Apache Commons configuration.
     * 
     * @param delegate The delegated Apache Commons configuration.
     * @return Next builder, for method chaining.
     */
    @NotNull S ofDelegate(
            final @NotNull ImmutableConfiguration delegate);

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull CommonsMutableConfigBuilder<?> mutable();
}
