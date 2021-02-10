package dev.orne.config;

import javax.validation.constraints.NotNull;

/**
 * Delegated {@code HierarchicalConfig} implementation that also implements
 * {@code MutableConfig}
 * 
 * @author <a href="mailto:wamphiry@orne.dev">(w) Iker Hernaez</a>
 * @version 1.0, 2021-02
 * @since 0.2
 */
public class MutableDelegatedHierarchicalConfig
extends MutableDelegatedConfig
implements HierarchicalConfig {

    /** The parent configuration. */
    private final Config parent;

    /**
     * Creates a new instance.
     * 
     * @param delegate The delegate {@code Config} instance
     * @param parent The parent {@code Config} instance
     */
    public MutableDelegatedHierarchicalConfig(
            final @NotNull MutableConfig delegate,
            final Config parent) {
        super(delegate);
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Config getParent() {
        return this.parent;
    }
}
