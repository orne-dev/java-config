package dev.orne.config.impl;

import java.util.Objects;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;

import dev.orne.config.Config;
import dev.orne.config.NonIterableConfigException;

/**
 * Implementation of {@code Config} based on Spring {@code Environment}.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-09
 * @since 0.1
 * @see Config
 * @see Environment
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class SpringEnvironmentConfigImpl
extends AbstractConfig {

    /** The Spring environment to use as storage of configuration properties. */
    private final @NotNull Environment environment;
    /** If this instance must support property keys iteration. */
    private final boolean iterable;

    /**
     * Creates a new instance.
     * 
     * @param options The configuration builder options.
     * @param springOptions The Spring environment based configuration options.
     */
    public SpringEnvironmentConfigImpl(
            final @NotNull ConfigOptions options,
            final @NotNull SpringEnvironmentConfigOptions springOptions) {
        super(options);
        Objects.requireNonNull(springOptions);
        this.environment = Objects.requireNonNull(springOptions.getEnvironment());
        this.iterable = springOptions.isIterableKeys();
    }

    /**
     * Returns the Spring environment used as storage of configuration
     * properties.
     * 
     * @return The Spring environment.
     */
    public @NotNull Environment getEnvironment() {
        return this.environment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEmptyInt() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean containsInt(
            final @NotBlank String key) {
        return this.environment.containsProperty(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull Stream<String> getKeysInt() {
        if (this.iterable && this.environment instanceof ConfigurableEnvironment) {
            return ((ConfigurableEnvironment) this.environment).getPropertySources()
                    .stream()
                    .filter(EnumerablePropertySource.class::isInstance)
                    .map(ps -> ((EnumerablePropertySource<?>) ps).getPropertyNames())
                    .flatMap(Stream::of)
                    .distinct();
        } else {
            throw new NonIterableConfigException(
                    "Spring Environment configuration property keys cannot be iterated.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getInt(
            final @NotBlank String key) {
        return this.environment.getProperty(key);
    }
}
