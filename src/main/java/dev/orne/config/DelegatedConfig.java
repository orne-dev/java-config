package dev.orne.config;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * A configuration that delegates all operations to another configuration.
 * This class is useful for creating a proxy or wrapper around an existing
 * configuration instance, allowing for additional behavior or modifications
 * without changing the original configuration.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-08
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0")
public class DelegatedConfig
implements Config {

    /** The configuration to delegate to. */
    private final @NotNull Config delegate;

    /**
     * Creates a new instance.
     *
     * @param delegate The configuration to delegate to.
     */
    public DelegatedConfig(
            final @NotNull Config delegate) {
        super();
        this.delegate = Objects.requireNonNull(delegate);
    }

    /**
     * Returns the delegate configuration.
     *
     * @return The delegate configuration.
     */
    protected @NotNull Config getDelegate() {
        return this.delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Config getParent() {
        return this.delegate.getParent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(
            final @NotBlank String key) {
        return this.delegate.contains(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Stream<String> getKeys() {
        return this.delegate.getKeys();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Stream<String> getKeys(
            final @NotNull Predicate<String> filter) {
        return this.delegate.getKeys(filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Stream<String> getKeys(
            final @NotNull String prefix) {
        return this.delegate.getKeys(prefix);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(
            final @NotBlank String key) {
        return this.delegate.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUndecored(
            final @NotBlank String key) {
        return this.delegate.getUndecored(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(
            final @NotBlank String key,
            final String defaultValue) {
        return this.delegate.get(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String get(
            final @NotBlank String key,
            final @NotNull Supplier<String> defaultValue) {
        return this.delegate.get(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBoolean(
            final @NotBlank String key) {
        return this.delegate.getBoolean(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getBoolean(
            final @NotBlank String key,
            final boolean defaultValue) {
        return this.delegate.getBoolean(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getBoolean(
            final @NotBlank String key,
            final @NotNull Supplier<Boolean> defaultValue) {
        return this.delegate.getBoolean(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getInteger(
            final @NotNull String key) {
        return this.delegate.getInteger(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInteger(
            final @NotNull String key,
            final int defaultValue) {
        return this.delegate.getInteger(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getInteger(
            final @NotBlank String key,
            final @NotNull Supplier<Integer> defaultValue) {
        return this.delegate.getInteger(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getLong(
            final @NotNull String key) {
        return this.delegate.getLong(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLong(
            final @NotNull String key,
            final long defaultValue) {
        return this.delegate.getLong(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getLong(
            final @NotBlank String key,
            final @NotNull Supplier<Long> defaultValue) {
        return this.delegate.getLong(key, defaultValue);
    }
}
