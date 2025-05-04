# Orne Java configuration utilities - Changelog

## [Unreleased]

### Changed

- **Breaking:** Simplify base configuration API.
    - Make `Config` a functional interface with a `String get(String)` method.
    - Limit supported value types to String, Boolean and Long.
    - Add `Iterable<String> getKeys()` method to `Config` for properties scanning.
- **Breaking:** Adapt basic configuration implementations to base configuration API changes.
    - Make `SystemConfig` singleton.
    - Replace `PreferencesConfig` constructors by factory methods.
- **Breaking:** Remove `MutableConfig` methods from basic configuration implementations. 
    - Move `PropertiesConfig` methods to `MutablePropertiesConfig`.
    - Move `PreferencesConfig` methods to `MutablePreferencesConfig`.
- **Breaking:** Rename `ConfigurationOptions.preferedConfigs` property to `preferredConfigs`.

### Added

- Dependency with `dev.orne:beans:0.3.0`.
- Configuration exceptions hierarchy.
    - `dev.orne.config.ConfigException` runtime exception.
    - `dev.orne.config.NonIterableConfigException` runtime exception.
- `Path` based resources support to `dev.orne.config.PropertiesConfig`.
- Basic mutable configuration implementations.
    - `dev.orne.config.MutablePropertiesConfig` class.
    - `dev.orne.config.MutablePreferencesConfig` class.
- Delegated configuration system.
    - `dev.orne.config.DelegatedConfig` class.
    - `dev.orne.config.MutableDelegatedConfig` class.
- Hierarchical configuration system.
    - `dev.orne.config.DelegatedHierarchicalConfig` class.
    - `dev.orne.config.MutableDelegatedHierarchicalConfig` class.
- Encrypted configuration system.
    - `dev.orne.config.ConfigCryptoProvider` interface.
    - `dev.orne.config.ConfigCryptoProviderException` exception.
    - `dev.orne.config.ConfigCryptoWrongKeyException` exception.
    - `dev.orne.config.EncryptedConfig` class.
    - `dev.orne.config.MutableEncryptedConfig` class.
    - `dev.orne.config.ConfigCryptoEngine` interface.
    - `dev.orne.config.AbstractConfigCryptoEngine` class.
    - `dev.orne.config.ConfigCryptoAesGcmEngine` class.
    - `dev.orne.config.DefaultConfigCryptoProvider` class.
    - `dev.orne.config.PooledConfigCryptoProvider` class.
- Apache Commons Configuration 2.x interoperability
    - `dev.orne.config.commons.CommonsConfig` class.
    - `dev.orne.config.commons.CommonsMutableConfig` class.
    - `dev.orne.config.commons.CommonsImmutableConfiguration` class.
    - `dev.orne.config.commons.CommonsConfiguration` class.
    - `dev.orne.config.commons.ProviderConfigurationDecoder` class.
- Java Preferences support for Apache Commons Configuration 2.x
    - `dev.orne.config.commons.PreferencesConfiguration` class.
    - `dev.orne.config.commons.prefs.PreferencesNodeDeletedException` exception.
    - `dev.orne.config.commons.prefs.PreferencesMapper` interface.
    - `dev.orne.config.commons.prefs.AbstractPreferencesMapper` class.
    - `dev.orne.config.commons.prefs.AbstractImmutableNodePreferencesMapper` class.
    - `dev.orne.config.commons.prefs.AttributeBasedPreferencesMapper` class.
    - `dev.orne.config.commons.prefs.NodeBasedPreferencesMapper` class.
    - `dev.orne.config.commons.prefs.EventCoordinationStrategy` interface.
    - `dev.orne.config.commons.prefs.ByThreadEventCoordinationStrategy` class.
    - `dev.orne.config.commons.prefs.PreferencesBased` interface.
    - `dev.orne.config.commons.prefs.PreferencesHandler` class.
    - `dev.orne.config.commons.prefs.PreferencesBuilderProperties` interface.
    - `dev.orne.config.commons.prefs.PreferencesBuilderParameters` interface.
    - `dev.orne.config.commons.prefs.PreferencesBuilderParametersImpl` class.
    - `dev.orne.config.commons.prefs.PreferencesConfigurationBuilder` class.
- Support for method chaining in `DefaultConfigProvider.registerConfig()` method.

### Removed

- **Breaking:** Removed obsolete classes.
    - `dev.orne.config.AbstractConfig` class.
    - `dev.orne.config.AbstractStringConfig` class.
    - `dev.orne.config.AbstractMutableStringConfig` class.

## 0.1.0 - 2020-04-28

_First experimental release._

### Added

- Dependency with `org.apache.commons:commons-lang3` 3.9.
- Dependency with `commons-beanutils:commons-beanutils` 1.9.4.
- Base configuration API.
    - `dev.orne.config.Config` interface.
    - `dev.orne.config.MutableConfig` interface.
    - `dev.orne.config.HierarchicalConfig` interface.
- Basic configuration implementations.
    - `dev.orne.config.AbstractConfig` class.
    - `dev.orne.config.AbstractStringConfig` class.
    - `dev.orne.config.SystemConfig` class.
    - `dev.orne.config.AbstractMutableStringConfig` class.
    - `dev.orne.config.PropertiesConfig` class.
    - `dev.orne.config.PreferencesConfig` class.
- Configurable componentes API.
    - `dev.orne.config.Configurable` annotation.
    - `dev.orne.config.ConfigurableProperty` annotation.
    - `dev.orne.config.ConfigurationOptions` annotation.
- Configurable componentes management.
    - `dev.orne.config.Configurer` interface.
    - `dev.orne.config.ConfigProvider` interface.
    - `dev.orne.config.DefaultConfigurer` class.
    - `dev.orne.config.DefaultConfigProvider` class.
