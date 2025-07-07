package dev.orne.config;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Implementation of {@code System} properties configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see SystemConfig
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class SystemConfigBuilderImpl
extends AbstractConfigBuilderImpl<SystemConfigBuilderImpl>
implements SystemConfigBuilder {

    /**
     * Empty constructor.
     */
    public SystemConfigBuilderImpl() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param options The configuration options to copy.
     * @param propertyOptions The properties based configuration options to
     * copy.
     */
    protected SystemConfigBuilderImpl(
            final @NotNull ConfigOptions options) {
        super(options);
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    protected SystemConfigBuilderImpl(
            final @NotNull AbstractConfigBuilderImpl<?> copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull SystemConfig build() {
        return new SystemConfig(this.options);
    }
}
