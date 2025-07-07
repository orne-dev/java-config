# Orne Java configuration utilities - Changelog

## 1.0.0 - [Unreleased]

**Major API compatibility break.**

### Changed

- Bump `org.apache.commons:commons-lang3` to 3.17.0.
- Bump `commons-beanutils:commons-beanutils` to 1.10.1.
- **Breaking:** Simplify base configuration API.
    - Make `Config` a functional interface with a `String get(String)` method.
    - Limit supported value types to String, Boolean, Integer and Long.
    - Add `Stream<String> getKeys()` method to `Config` for properties scanning.
- **Breaking:** Adapt basic configuration implementations to base configuration API changes.
    - Rewrite `dev.orne.config.AbstractConfig` class.
    - Rewrite `SystemConfig` class.
    - Rewrite `PropertiesConfig` class.
    - Rewrite `PreferencesConfig` class.
- **Breaking:** Remove `MutableConfig` methods from read only configuration implementations. 
    - Move `PropertiesConfig` methods to `MutablePropertiesConfig`.
    - Move `PreferencesConfig` methods to `MutablePreferencesConfig`.
- **Breaking:** Rename `ConfigurationOptions.preferedConfigs` property to `preferredConfigs`.

### Added

- Add configuration exceptions hierarchy.
    - `dev.orne.config.ConfigException` runtime exception.
    - `dev.orne.config.NonIterableConfigException` runtime exception.
- Add basic configuration implementations.
    - `dev.orne.config.ValueDecoder` interface.
    - `dev.orne.config.ValueDecorator` interface.
    - `dev.orne.config.ValueEncoder` interface.
    - `dev.orne.config.AbstractMutableConfig` class.
    - `dev.orne.config.MutablePropertiesConfig` class.
    - `dev.orne.config.MutablePreferencesConfig` class.
- Add variable resolution support.
    - `dev.orne.config.VariableResolver` class.
- Add watchable mutable configuration support.
    - `dev.orne.config.WatchableConfig` interface.
    - `dev.orne.config.AbstractWatchableConfig` class.
- Add encrypted configuration support.
    - `dev.orne.config.crypto.ConfigCryptoEngine` interface.
    - `dev.orne.config.crypto.AbstractConfigCryptoEngine` class.
    - `dev.orne.config.crypto.ConfigCryptoAesGcmEngine` class.
    - `dev.orne.config.crypto.ConfigCryptoProviderException` exception.
    - `dev.orne.config.crypto.ConfigCryptoWrongKeyException` exception.
    - `dev.orne.config.crypto.ConfigCryptoProvider` interface.
    - `dev.orne.config.crypto.AbstractConfigCryptoProvider` class.
    - `dev.orne.config.crypto.DefaultConfigCryptoProvider` class.
    - `dev.orne.config.crypto.PooledConfigCryptoProvider` class.
- Add apache Commons Configuration 2.x interoperability
    - `dev.orne.config.commons.CommonsConfig` class.
    - `dev.orne.config.commons.CommonsMutableConfig` class.
    - `dev.orne.config.commons.DelegatedOrneConfiguration` class.
    - `dev.orne.config.commons.DelegatedOrneMutableConfiguration` class.
    - `dev.orne.config.commons.DelegatedOrneConfigurationDecoder` class.
- Add fluent configuration API.
    - `dev.orne.config.ConfigBuilder` interface.
    - `dev.orne.config.MutableCapableConfigBuilder` interface.
    - `dev.orne.config.MutableConfigBuilder` interface.
    - `dev.orne.config.ConfigOptions` class.
    - `dev.orne.config.AbstractConfigBuilderImpl` class.
    - `dev.orne.config.MutableConfigOptions` class.
    - `dev.orne.config.AbstractMutableConfigBuilderImpl` class.
    - `dev.orne.config.SystemConfigBuilder` interface.
    - `dev.orne.config.SystemConfigBuilderImpl` class.
    - Added `dev.orne.config.Config.ofSystemProperties()` method.
    - `dev.orne.config.PropertiesConfigBaseBuilder` interface.
    - `dev.orne.config.PropertiesConfigBuilder` interface.
    - `dev.orne.config.PropertiesConfigOptions` class.
    - `dev.orne.config.PropertiesConfigBuilderImpl` class.
    - `dev.orne.config.MutablePropertiesConfigBuilder` interface.
    - `dev.orne.config.MutablePropertiesConfigBuilderImpl` class.
    - Added `dev.orne.config.Config.fromPropertiesFiles()` method.
    - `dev.orne.config.PreferencesConfigBuilder` interface.
    - `dev.orne.config.PreferencesConfigNodeBuilder` interface.
    - `dev.orne.config.PreferencesConfigOptions` class.
    - `dev.orne.config.PreferencesConfigBuilderImpl` class.
    - `dev.orne.config.MutablePreferencesConfigBuilder` interface.
    - `dev.orne.config.MutablePreferencesConfigBuilderImpl` class.
    - Added `dev.orne.config.Config.fromJavaPreferences()` method.
- Add `Path` based resources support to `dev.orne.config.PropertiesConfig`.
- Add `DefaultConfigProvider.registerConfig()` method.

### Removed

- **Breaking:** Removed obsolete classes.
    - `dev.orne.config.AbstractStringConfig` class.
    - `dev.orne.config.AbstractMutableStringConfig` class.
    - `dev.orne.config.HierarchicalConfig` interface.

## 0.1.0 - 2020-04-28

_First experimental release._

### Added

- Add dependency with `org.apache.commons:commons-lang3` 3.9.
- Add dependency with `commons-beanutils:commons-beanutils` 1.9.4.
- Add base configuration API.
    - `dev.orne.config.Config` interface.
    - `dev.orne.config.MutableConfig` interface.
    - `dev.orne.config.HierarchicalConfig` interface.
- Add basic configuration implementations.
    - `dev.orne.config.AbstractConfig` class.
    - `dev.orne.config.AbstractStringConfig` class.
    - `dev.orne.config.SystemConfig` class.
    - `dev.orne.config.AbstractMutableStringConfig` class.
    - `dev.orne.config.PropertiesConfig` class.
    - `dev.orne.config.PreferencesConfig` class.
- Add configurable componentes API.
    - `dev.orne.config.Configurable` annotation.
    - `dev.orne.config.ConfigurableProperty` annotation.
    - `dev.orne.config.ConfigurationOptions` annotation.
- Add configurable componentes management.
    - `dev.orne.config.Configurer` interface.
    - `dev.orne.config.ConfigProvider` interface.
    - `dev.orne.config.DefaultConfigurer` class.
    - `dev.orne.config.DefaultConfigProvider` class.
