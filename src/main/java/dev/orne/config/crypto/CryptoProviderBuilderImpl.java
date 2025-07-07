package dev.orne.config.crypto;

import javax.crypto.SecretKey;
import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * Implementation of configuration values cryptography transformations provider
 * builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see ConfigCryptoProvider
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class CryptoProviderBuilderImpl
implements CryptoProviderEngineBuilder, CryptoProviderKeyBuilder, CryptoProviderBuilder{

    /** The cryptography transformations provider options. */
    private final CryptoProviderOptions options;

    /**
     * Empty constructor.
     */
    public CryptoProviderBuilderImpl() {
        super();
        this.options = new CryptoProviderOptions();
    }

    /**
     * Copy constructor.
     * 
     * @param copy The instance to copy.
     */
    public CryptoProviderBuilderImpl(
            final @NotNull CryptoProviderBuilderImpl copy) {
        super();
        this.options = new CryptoProviderOptions(copy.options);
    }

    /**
     * Copy constructor.
     * 
     * @param options The cryptography transformations provider options to copy.
     */
    public CryptoProviderBuilderImpl(
            final @NotNull CryptoProviderOptions options) {
        super();
        this.options = new CryptoProviderOptions(options);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CryptoProviderBuilderImpl withEngine(
            final @NotNull ConfigCryptoEngine engine,
            final boolean destroyEngine) {
        this.options.setEngine(engine);
        this.options.setDestroyEngine(destroyEngine);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CryptoProviderBuilderImpl withAesGcmEngine(
            final @NotNull byte[] salt,
            final boolean destroyEngine) {
        this.options.setEngine(new ConfigCryptoAesGcmEngine(salt));
        this.options.setDestroyEngine(destroyEngine);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CryptoProviderBuilderImpl withSecretKey(
            final @NotNull char[] password) {
        this.options.setKey(
                this.options.engine.createSecretKey(password));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CryptoProviderBuilderImpl withSecretKey(
            final @NotNull SecretKey key) {
        this.options.setKey(key);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull CryptoProviderBuilderImpl pooled() {
        this.options.setPooled(true);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ConfigCryptoProvider build() {
        final ConfigCryptoProvider instance;
        if (this.options.isPooled()) {
            instance = new PooledConfigCryptoProvider(this.options);
        } else {
            instance = new DefaultConfigCryptoProvider(this.options);
        }
        return instance;
    }
}
