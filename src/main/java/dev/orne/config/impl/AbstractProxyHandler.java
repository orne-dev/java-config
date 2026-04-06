package dev.orne.config.impl;

/*-
 * #%L
 * Orne Config
 * %%
 * Copyright (C) 2019 - 2026 Orne Developments
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
import java.util.Objects;

import org.apiguardian.api.API;
import org.jspecify.annotations.Nullable;

import dev.orne.config.Config;

/**
 * Abstract {@code Config} invocation handler with common
 * utility methods.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2026-04
 * @since 1.1
 */
@API(status = API.Status.INTERNAL, since = "1.0")
public abstract class AbstractProxyHandler
implements InvocationHandler {

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
    protected final Config instance;

    /**
     * Creates a new instance.
     *
     * @param instance The configuration instance to be proxied.
     */
    protected AbstractProxyHandler(
            final Config instance) {
        super();
        this.instance = Objects.requireNonNull(instance);
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
    protected @Nullable Object handleObjectMethod(
            final Method method,
            final @Nullable Object[] args)
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
            final @Nullable Object other) {
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
                this.instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(
            final @Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractProxyHandler other = (AbstractProxyHandler) obj;
        return Objects.equals(this.instance, other.instance);
    }
}
