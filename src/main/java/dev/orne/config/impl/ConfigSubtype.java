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

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

import dev.orne.config.Config;
import dev.orne.config.ConfigException;
import dev.orne.config.MutableConfig;
import dev.orne.config.WatchableConfig;

/**
 * Invocation handler for configuration proxies.
 * Invokes configuration subtype default methods directly on the extended
 * interface, delegating other method calls to the underlying configuration
 * instance.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-08
 * @since 1.0
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public class ConfigSubtype
implements InvocationHandler {

    /** The list of classes proxied to the configuration instance. */
    private static final List<Class<?>> PROXYED_TYPES = List.of(
            Config.class,
            MutableConfig.class,
            WatchableConfig.class);
    /** Cached {@code Object.equals()} for performance optimization. */
    private static final Method OBJECT_EQUALS;
    static {
        try {
            OBJECT_EQUALS = Object.class.getMethod(
                    "equals",
                    Object.class);
        } catch (final NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /** The configuration instance. */
    private final @NotNull Config instance;
    /** The type of the extended configuration interface. */
    private final @NotNull Class<? extends Config> extendedType;
    /** The method handles lookup. */
    private final @NotNull MethodHandles.Lookup lookup;

    /**
     * Creates a new instance.
     *
     * @param instance The configuration instance to be proxied.
     * @param extendedType The type of extended configuration interface.
     */
    protected ConfigSubtype(
            final @NotNull Config instance,
            final @NotNull Class<? extends Config> extendedType) {
        this.instance = instance;
        this.extendedType = extendedType;
        MethodHandles.Lookup lookupInstance;
        try {
            lookupInstance = MethodHandles.privateLookupIn(extendedType, MethodHandles.lookup());
        } catch (final IllegalAccessException e) {
            lookupInstance = MethodHandles.lookup();
        }
        this.lookup = lookupInstance;
    }

    /**
     * Creates a new configuration proxy instance with the specified configuration.
     *
     * @param <T> The type of the configuration interface.
     * @param config The proxied configuration instance.
     * @param type The configuration type interface to create a proxy for.
     * @return A new configuration proxy instance.
     */
    public static <T extends Config> @NotNull T create(
            final @NotNull Config config,
            final @NotNull Class<T> type) {
        return create(
                type.getClassLoader(),
                config,
                type);
    }

    /**
     * Creates a new configuration proxy instance with the specified configuration.
     *
     * @param <T> The type of the configuration interface.
     * @param classLoader The class loader to be used for the proxy.
     * @param config The proxied configuration instance.
     * @param type The configuration type interface to create a proxy for.
     * @return A new configuration proxy instance.
     */
    public static <T extends Config> @NotNull T create(
            final @NotNull ClassLoader classLoader,
            final @NotNull Config config,
            final @NotNull Class<T> type) {
        Objects.requireNonNull(classLoader, "The class loader must not be null");
        Objects.requireNonNull(config, "The configuration instance must not be null");
        Objects.requireNonNull(type, "The configuration subtype must be an interface.");
        validateSubtypeInterface(type);
        if (WatchableConfig.class.isAssignableFrom(type)
                && !(config instanceof WatchableConfig)) {
            throw new ConfigException(
                    "The proxied configuration instance must extend WatchableConfig.");
        } else if (MutableConfig.class.isAssignableFrom(type)
                && !(config instanceof MutableConfig)) {
            throw new ConfigException(
                    "The proxied configuration instance must extend MutableConfig.");
        }
        final ConfigSubtype handler = new ConfigSubtype(config, type);
        return type.cast(Proxy.newProxyInstance(
                classLoader,
                new Class<?>[] { type },
                handler));
    }

    /**
     * Validates the configuration subtype interface.
     *
     * @param configInterface The configuration interface class to validate.
     * @throws ConfigException If the interface is not valid.
     */
    private static void validateSubtypeInterface(
            final @NotNull Class<?> configInterface) {
        if (!configInterface.isInterface()) {
            throw new ConfigException(
                    "The configuration subtype must be an interface.");
        }
        for (final Method method : configInterface.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            if (!method.isDefault()) {
                throw new ConfigException(
                        "The configuration subtype must only contain default methods. " + method);
            }
        }
        for (final Class<?> iface : configInterface.getInterfaces()) {
            if (!Config.class.equals(iface)
                    && !MutableConfig.class.equals(iface)
                    && !WatchableConfig.class.equals(iface)) {
                validateSubtypeInterface(iface);
            }
        }
    }

    /**
     * {@inheritDoc}
    */
    @Override
    public Object invoke(
            final Object proxy,
            final @NotNull Method method,
            final @NotNull Object[] args)
    throws Throwable {
        final Class<?> declaringClass = method.getDeclaringClass();
        if (Object.class.equals(declaringClass)) {
            return handleObjectMethod(method, args);
        }
        if (!PROXYED_TYPES.contains(declaringClass)) {
            return this.lookup
                    .findSpecial(
                            declaringClass,
                            method.getName(),
                            MethodType.methodType(
                                method.getReturnType(),
                                method.getParameterTypes()),
                            this.extendedType)
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        }
        try {
            return method.invoke(this.instance, args);
        } catch (final  InvocationTargetException e) {
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
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                this.instance,
                this.extendedType);
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
        ConfigSubtype other = (ConfigSubtype) obj;
        return Objects.equals(this.instance, other.instance)
                && Objects.equals(this.extendedType, other.extendedType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ConfigSubtype [instance=" + this.instance + ", extendedType=" + this.extendedType + "]";
    }
}
