package dev.orne.config.impl;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2025 Orne Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.Config;
import dev.orne.config.ConfigException;
import dev.orne.config.MutableConfig;
import dev.orne.config.WatchableConfig;

/**
 * Invocation handler for configuration subsets.
 * Invokes configuration methods adding the specified prefix
 * to configuration keys.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-10
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class ConfigSubset
implements InvocationHandler {

    /** The name of the getKeys methods. */
    private static final String GET_KEYS_METHOD = "getKeys";
    /** The name of the subset methods. */
    private static final String SUBSET_METHOD = "subset";

    /** Cached {@code Object.equals()} for performance optimization. */
    private static final Method OBJECT_EQUALS;
    /** Cached {@code Config.isEmpty()} for performance optimization. */
    private static final Method CONFIG_IS_EMPTY;
    /** Cached {@code Config.getKeys(Predicate)} for performance optimization. */
    private static final Method CONFIG_GET_KEYS_FILTERED;
    /** Cached {@code Config.as(Class)} for performance optimization. */
    private static final Method CONFIG_AS;
    /** Cached {@code Config.subset(String)} for performance optimization. */
    private static final Method CONFIG_SUBSET;
    /** Cached {@code MutableConfig.subset(String)} for performance optimization. */
    private static final Method MUTABLE_SUBSET;
    /** Cached {@code WatchableConfig.addListener(Listener)} for performance optimization. */
    private static final Method WATCHABLE_ADD_LISTENER;
    /** Cached {@code WatchableConfig.removeListener(Listener)} for performance optimization. */
    private static final Method WATCHABLE_REMOVE_LISTENER;
    /** Cached {@code WatchableConfig.subset(String)} for performance optimization. */
    private static final Method WATCHABLE_SUBSET;
    static {
        try {
            OBJECT_EQUALS = Object.class.getMethod(
                    "equals",
                    Object.class);
            CONFIG_IS_EMPTY = Config.class.getMethod(
                    "isEmpty");
            CONFIG_GET_KEYS_FILTERED = Config.class.getMethod(
                    GET_KEYS_METHOD,
                    Predicate.class);
            CONFIG_AS = Config.class.getMethod(
                    "as",
                    Class.class);
            CONFIG_SUBSET = MutableConfig.class.getMethod(
                    SUBSET_METHOD,
                    String.class);
            MUTABLE_SUBSET = MutableConfig.class.getMethod(
                    SUBSET_METHOD,
                    String.class);
            WATCHABLE_ADD_LISTENER = WatchableConfig.class.getMethod(
                    "addListener",
                    WatchableConfig.Listener.class);
            WATCHABLE_REMOVE_LISTENER = WatchableConfig.class.getMethod(
                    "removeListener",
                    WatchableConfig.Listener.class);
            WATCHABLE_SUBSET = WatchableConfig.class.getMethod(
                    SUBSET_METHOD,
                    String.class);
        } catch (final NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /** The configuration instance. */
    private final @NotNull Config instance;
    /** The prefix for configuration keys. */
    private final @NotNull String prefix;
    /** The configuration change events handler. */
    private final EventsHandler events;

    /**
     * Creates a new instance.
     *
     * @param instance The configuration instance to be proxied.
     * @param prefix The prefix for configuration keys.
     */
    protected ConfigSubset(
            final @NotNull Config instance,
            final @NotNull String prefix) {
        this.instance = Objects.requireNonNull(instance);
        this.prefix = Objects.requireNonNull(prefix);
        if (instance instanceof WatchableConfig) {
            this.events = new EventsHandler();
        } else {
            this.events = null;
        }
    }

    /**
     * Creates a new configuration proxy instance with the specified configuration.
     *
     * @param <T> The type of the configuration interface.
     * @param config The proxied configuration instance.
     * @param type The configuration type interface to create a proxy for.
     * @return A new configuration proxy instance.
     */
    public static @NotNull Config create(
            final @NotNull Config config,
            final @NotNull String prefix) {
        return create(
                config.getClass().getClassLoader(),
                Config.class,
                config,
                prefix);
    }

    /**
     * Creates a new configuration proxy instance with the specified configuration.
     *
     * @param <T> The type of the configuration interface.
     * @param config The proxied configuration instance.
     * @param type The configuration type interface to create a proxy for.
     * @return A new configuration proxy instance.
     */
    public static @NotNull MutableConfig create(
            final @NotNull MutableConfig config,
            final @NotNull String prefix) {
        return create(
                config.getClass().getClassLoader(),
                MutableConfig.class,
                config,
                prefix);
    }

    /**
     * Creates a new configuration proxy instance with the specified configuration.
     *
     * @param <T> The type of the configuration interface.
     * @param config The proxied configuration instance.
     * @param type The configuration type interface to create a proxy for.
     * @return A new configuration proxy instance.
     */
    public static @NotNull WatchableConfig create(
            final @NotNull WatchableConfig config,
            final @NotNull String prefix) {
        return create(
                config.getClass().getClassLoader(),
                WatchableConfig.class,
                config,
                prefix);
    }

    /**
     * Creates a new configuration proxy instance with the specified configuration.
     *
     * @param <T> The type of the configuration interface.
     * @param classLoader The class loader to be used for the proxy.
     * @param config The proxied configuration instance.
     * @param prefix The prefix for configuration keys.
     * @return A new configuration proxy instance.
     */
    protected static <T extends Config> @NotNull T create(
            final @NotNull ClassLoader classLoader,
            final @NotNull Class<T> type,
            final @NotNull T config,
            final @NotNull String prefix) {
        Objects.requireNonNull(classLoader, "The class loader must not be null");
        Objects.requireNonNull(config, "The configuration instance must not be null");
        Objects.requireNonNull(classLoader, "The configuration subtype must be an interface.");
        final ConfigSubset handler = new ConfigSubset(config, prefix);
        final T proxy = type.cast(Proxy.newProxyInstance(
                classLoader,
                new Class<?>[] { type },
                handler));
        if (config instanceof WatchableConfig) {
            final WatchableConfig original = (WatchableConfig) config;
            final WatchableConfig proxyConfig = (WatchableConfig) proxy;
            original.addListener((cfg, props) ->
                handler.events.notify(proxyConfig, props.stream()
                        .filter(handler::isSubsetKey)
                        .map(handler::asSubsetKey)
                        .collect(Collectors.toSet())));
        }
        return proxy;
    }

    /**
     * Returns the proxied configuration instance.
     * 
     * @return The proxied configuration instance.
     */
    protected @NotNull Config getInstance() {
        return this.instance;
    }

    /**
     * Returns the prefix for configuration keys.
     * 
     * @return The prefix for configuration keys.
     */
    protected @NotNull String getPrefix() {
        return this.prefix;
    }

    /**
     * Returns the configuration change events handler.
     * 
     * @return The configuration change events handler.
     */
    protected EventsHandler getEvents() {
        return this.events;
    }

    /**
     * Converts a property key of this sub-set to a delegated configuration
     * key by adding the prefix.
     * 
     * @param key The sub-set property key.
     * @return The delegated configuration key.
     */
    protected String asConfigKey(
            final @NotNull String key) {
        return this.prefix + key;
    }

    /**
     * Checks if the specified configuration key belongs to this sub-set by
     * checking the prefix.
     * 
     * @param key The delegated configuration key.
     * @return {@code true} if the key belongs to this sub-set,
     *         {@code false} otherwise.
     */
    protected boolean isSubsetKey(
            final @NotNull String key) {
        return key.startsWith(this.prefix);
    }

    /**
     * Converts a delegated configuration key to a property key of this sub-set
     * by removing the prefix.
     * 
     * @param key The delegated configuration key.
     * @return The sub-set property key.
     */
    protected String asSubsetKey(
            final @NotNull String key) {
        return key.substring(this.prefix.length());
    }

    /**
     * {@inheritDoc}
    */
    @Override
    public Object invoke(
            final Object proxy,
            final @NotNull Method method,
            final Object[] args)
    throws Throwable {
        try {
            final Class<?> declaringClass = method.getDeclaringClass();
            final Object result;
            if (Object.class.equals(declaringClass)) {
                result = handleObjectMethod(method, args);
            } else if (Config.class.equals(declaringClass)) {
                result = handleConfigMethod((Config) proxy, method, args);
            } else if (MutableConfig.class.equals(declaringClass)) {
                result = handleMutableMethod(method, args);
            } else  if (WatchableConfig.class.equals(declaringClass)) {
                result = handleWatchableMethod(method, args);
            } else {
                result = method.invoke(this.instance, args);
            }
            return result;
        } catch (final  ReflectiveOperationException e) {
            if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            } else {
                throw new ConfigException(e.getCause());
            }
        }
    }

    /**
     * Handles {@code Object} methods invocations.
     * 
     * @param method The invoked method.
     * @param args The method arguments.
     * @return The method invocation result.
     * @throws ReflectiveOperationException If an error occurs during method
     * invocation.
     */
    protected Object handleObjectMethod(
            final @NotNull Method method,
            final Object[] args)
    throws ReflectiveOperationException {
        final Object result;
        if (OBJECT_EQUALS.equals(method)) {
            result = proxyEquals(args[0]);
        } else {
            result = method.invoke(this, args);
        }
        return result;
    }

    /**
     * Checks equality with another proxy instance.
     * 
     * @param other The other proxy instance.
     * @return {@code true} if both proxies are equal,
     *         {@code false} otherwise.
     */
    protected boolean proxyEquals(
            final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !Proxy.isProxyClass(other.getClass())) {
            return false;
        }
        return this.equals(Proxy.getInvocationHandler(other));
    }

    /**
     * Handles {@code Config} methods invocations.
     * 
     * @param proxy The proxy instance.
     * @param method The invoked method.
     * @param args The method arguments.
     * @return The method invocation result.
     * @throws ReflectiveOperationException If an error occurs during method
     * invocation.
     */
    protected Object handleConfigMethod(
            final @NotNull Config proxy,
            final @NotNull Method method,
            final Object[] args)
    throws ReflectiveOperationException {
        final Object result;
        if (CONFIG_IS_EMPTY.equals(method)
                && method.getParameterCount() == 0) {
            result = this.instance.getKeys(this.prefix)
                    .findAny()
                    .isEmpty();
        } else if (CONFIG_AS.equals(method)) {
            @SuppressWarnings("unchecked")
            final Class<? extends Config> subtype =
                    (Class<? extends Config>) args[0];
            result = ConfigSubtype.create(
                    proxy,
                    subtype);
        } else if (CONFIG_SUBSET.equals(method)) {
            result = create(
                    this.instance,
                    asConfigKey((String) args[0]));
        } else if (GET_KEYS_METHOD.equals(method.getName())) {
            result = getKeys(method, args);
        } else if (args != null && args.length > 0 && args[0] instanceof String) {
            final Object[] modifiedArgs = Arrays.copyOf(args, args.length);
            modifiedArgs[0] = asConfigKey((String) modifiedArgs[0]);
            result = method.invoke(this.instance, modifiedArgs);
        } else {
            result = method.invoke(this.instance, args);
        }
        return result;
    }

    /**
     * Handles {@code MutableConfig} methods invocations.
     * 
     * @param method The invoked method.
     * @param args The method arguments.
     * @return The method invocation result.
     * @throws ReflectiveOperationException If an error occurs during method
     * invocation.
     */
    protected Object handleMutableMethod(
            final @NotNull Method method,
            final Object[] args)
    throws ReflectiveOperationException {
        final Object result;
        if (MUTABLE_SUBSET.equals(method)) {
            result = create(
                    (MutableConfig) this.instance,
                    asConfigKey((String) args[0]));
        } else if (args != null && args.length > 0 && args[0] instanceof String) {
            final Object[] modifiedArgs = Arrays.copyOf(args, args.length);
            modifiedArgs[0] = asConfigKey((String) modifiedArgs[0]);
            result = method.invoke(this.instance, modifiedArgs);
        } else {
            result = method.invoke(this.instance, args);
        }
        return result;
    }

    /**
     * Handles {@code WatchableConfig} methods invocations.
     * 
     * @param method The invoked method.
     * @param args The method arguments.
     * @return The method invocation result.
     * @throws ReflectiveOperationException If an error occurs during method
     * invocation.
     */
    protected Object handleWatchableMethod(
            final @NotNull Method method,
            final Object[] args)
    throws ReflectiveOperationException {
        final Object result;
        if (WATCHABLE_ADD_LISTENER.equals(method)) {
            this.events.add((WatchableConfig.Listener) args[0]);
            result = null;
        } else if (WATCHABLE_REMOVE_LISTENER.equals(method)) {
            this.events.remove((WatchableConfig.Listener) args[0]);
            result = null;
        } else if (WATCHABLE_SUBSET.equals(method)) {
            result = create(
                    (WatchableConfig) this.instance,
                    asConfigKey((String) args[0]));
        } else {
            result = method.invoke(this.instance, args);
        }
        return result;
    }

    /**
     * Handles {@code Config.getKeys()} and
     * {@code Config.getKeys(String)} and
     * {@code Config.getKeys(Predicate)} method invocations.
     * 
     * @param method The invoked method.
     * @param args The method arguments.
     * @return The method invocation result.
     * @throws ReflectiveOperationException If an error occurs during method
     * invocation.
     */
    protected Object getKeys(
            final @NotNull Method method,
            final Object[] args)
    throws ReflectiveOperationException {
        Object result;
        if (args == null || args.length == 0) {
            result = this.instance.getKeys(this.prefix)
                    .map(key -> key.substring(this.prefix.length()));
        } else if (args.length == 1 && args[0] instanceof String) {
            result = this.instance.getKeys(asConfigKey((String) args[0]))
                    .map(key -> key.substring(this.prefix.length()));
        } else if (CONFIG_GET_KEYS_FILTERED.equals(method)) {
            @SuppressWarnings("unchecked")
            final Predicate<String> predicate = (Predicate<String>) args[0];
            result = this.instance.getKeys(this.prefix)
                    .map(key -> key.substring(this.prefix.length()))
                    .filter(predicate);
        } else {
            result = method.invoke(this.instance, args);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                this.instance,
                this.prefix,
                this.events);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ConfigSubset other = (ConfigSubset) obj;
        return Objects.equals(this.instance, other.instance)
                && Objects.equals(this.prefix, other.prefix)
                && Objects.equals(this.events, other.events);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ConfigSubset [instance=" + this.instance + ", prefix=" + this.prefix + "]";
    }
}
