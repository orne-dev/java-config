package dev.orne.config;

import java.util.prefs.Preferences;

import javax.validation.constraints.NotNull;

import org.apiguardian.api.API;

/**
 * {@code Preferences} based configuration node selection builder.
 * 
 * @author <a href="https://github.com/ihernaez">(w) Iker Hernaez</a>
 * @version 1.0, 2025-05
 * @since 1.0
 * @see Preferences
 * @see Config
 */
@API(status = API.Status.STABLE, since = "1.0")
public interface PreferencesConfigNodeBuilder
extends ConfigBuilder {

    /**
     * Selects the user preferences tree root node.
     * 
     * @return Next builder, for method chaining.
     * @see Preferences#userRoot()
     */
    @NotNull PreferencesConfigBuilder ofUserRoot();

    /**
     * Selects the node with the specified path on the user preferences tree.
     * 
     * @param path The path of the configuration's preferences, relative to
     * user preference tree's root node
     * @return Next builder, for method chaining.
     * @see Preferences#userRoot()
     * @see Preferences#node(String)
     */
    @NotNull PreferencesConfigBuilder ofUser(
            final @NotNull String path);

    /**
     * Selects the node for the package of the specified class on the user
     * preferences tree.
     * 
     * @param clazz The class which package use when calculating base node
     * @return Next builder, for method chaining.
     * @see Preferences#userNodeForPackage(Class)
     */
    @NotNull PreferencesConfigBuilder ofUser(
            final @NotNull Class<?> clazz);

    /**
     * Selects the node with the specified path relative to the node for the
     * package of the specified class on the user preferences tree.
     * 
     * @param clazz The class which package use when calculating base node
     * @param path The path of the configuration's preferences, relative to
     * base node
     * @return Next builder, for method chaining.
     * @see Preferences#userNodeForPackage(Class)
     * @see Preferences#node(String)
     */
    @NotNull PreferencesConfigBuilder ofUser(
            final @NotNull Class<?> clazz,
            final @NotNull String path);

    /**
     * Selects the system preferences tree root node.
     * 
     * @return Next builder, for method chaining.
     * @see Preferences#systemRoot()
     */
    @NotNull PreferencesConfigBuilder ofSystemRoot();

    /**
     * Selects the node with the specified path on the system preferences tree.
     * 
     * @param path The path of the configuration's preferences, relative to
     * root node.
     * @return Next builder, for method chaining.
     * @see Preferences#systemRoot()
     * @see Preferences#node(String)
     */
    @NotNull PreferencesConfigBuilder ofSystem(
            final @NotNull String path);

    /**
     * Selects the node for the package of the specified class on the system
     * preferences tree.
     * 
     * @param clazz The class which package use when calculating base node.
     * @return Next builder, for method chaining.
     * @see Preferences#systemNodeForPackage(Class)
     */
    @NotNull PreferencesConfigBuilder ofSystem(
            final @NotNull Class<?> clazz);

    /**
     * Selects the node with the specified path relative to the node for the
     * package of the specified class on the system preferences tree.
     * 
     * @param clazz The class which package use when calculating base node
     * @param path The path of the configuration's preferences, relative to
     * base node
     * @return Next builder, for method chaining.
     * @see Preferences#systemNodeForPackage(Class)
     * @see Preferences#node(String)
     */
    @NotNull PreferencesConfigBuilder ofSystem(
            final @NotNull Class<?> clazz,
            final @NotNull String path);

    /**
     * Selects the specified preferences node.
     * 
     * @return Next builder, for method chaining.
     */
    @NotNull PreferencesConfigBuilder ofNode(
            final @NotNull Preferences preferences);
}
