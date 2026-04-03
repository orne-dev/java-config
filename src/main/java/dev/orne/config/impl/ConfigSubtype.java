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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Objects;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

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
extends AbstractProxyHandler {

    /** The list of classes proxied to the configuration instance. */
    private static final List<Class<?>> PROXYED_TYPES = List.of(
            Config.class,
            MutableConfig.class,
            WatchableConfig.class);

    /** The type of the extended configuration interface. */
    private final Class<? extends Config> extendedType;
    /** The method handles lookup. */
    private final MethodHandles.Lookup lookup;

    /**
     * Creates a new instance.
     *
     * @param instance The configuration instance to be proxied.
     * @param extendedType The type of extended configuration interface.
     */
    protected ConfigSubtype(
            final Config instance,
            final Class<? extends Config> extendedType) {
        super(instance);
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
    public static <T extends Config> T create(
            final Config config,
            final Class<T> type) {
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
    public static <T extends Config> T create(
            final ClassLoader classLoader,
            final Config config,
            final Class<T> type) {
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
            final Class<?> configInterface) {
        if (!configInterface.isInterface()) {
            throw new ConfigException(
                    "The configuration subtype must be an interface.");
        }
        for (final Method method : configInterface.getDeclaredMethods()) {
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            if (!method.isDefault() && !method.isSynthetic()) {
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
    public @Nullable Object invoke(
            final @Nullable Object proxy,
            final Method method,
            final @Nullable Object[] args)
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
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                super.hashCode(),
                this.extendedType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(
            final @Nullable Object obj) {
        if (obj == null || !super.equals(obj)) {
            return false;
        }
        final ConfigSubtype other = (ConfigSubtype) obj;
        return super.equals(obj)
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
