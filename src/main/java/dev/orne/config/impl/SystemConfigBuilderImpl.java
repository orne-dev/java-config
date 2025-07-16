package dev.orne.config.impl;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.SystemConfigBuilder;

/**
 * Implementation of {@code System} properties configuration builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see SystemConfigImpl
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
     * {@inheritDoc}
     */
    @Override
    public @NotNull SystemConfigImpl build() {
        return new SystemConfigImpl(this.options);
    }
}
