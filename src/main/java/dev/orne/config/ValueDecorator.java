package dev.orne.config;

import org.apiguardian.api.API;

/**
 * Interface of configuration property values variable resolvers.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-04
 * @since 1.0
 */
@API(status = API.Status.STABLE, since = "1.0")
@FunctionalInterface
public interface VariableResolver {

    /** Default NOP variable resolver. */
    static VariableResolver DEFAULT = value -> value;

    /**
     * Resolves the variables of the specified configuration property value.
     * <p>
     * {@code null} values must return {@code null}.
     * 
     * @param value The configuration property value.
     * @return The configuration property value with the variables resolved.
     */
    String resolve(
            String value);
}
