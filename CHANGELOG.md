# Orne Java configuration utilities - Changelog

## 1.0.0 - [Unreleased]

- **Requires Java 11 or newer.**
- **Major API compatibility break.**

### Changed

- Bump `org.apache.commons:commons-lang3` to 3.18.0.
- Bump `commons-beanutils:commons-beanutils` to 1.10.1.
- **Breaking:** Simplify base configuration API.
    - Make `Config` a functional interface with a `String get(String)` method.
    - Limit supported value types to String, Boolean, Integer and Long.
    - Add `Stream<String> getKeys()` method to `Config` for properties scanning.
- **Breaking:** Rename `ConfigurationOptions.preferedConfigs` property to `preferredConfigs`.

### Added

- Add optional dependency with `org.apache.commons:commons-pool2` 2.12.1.
- Add optional dependency with `org.apache.commons:commons-configuration2` 2.12.0.
- Add optional dependency with `com.fasterxml.jackson.core:jackson-databind` 2.19.1.
- Add optional dependency with `com.fasterxml.jackson.dataformat:jackson-dataformat-yaml` 2.19.1.
- Add configuration exceptions hierarchy.
    - `dev.orne.config.ConfigException` runtime exception.
    - `dev.orne.config.NonIterableConfigException` runtime exception.
    - `dev.orne.config.ConfigCryptoProviderException` runtime exception.
    - `dev.orne.config.ConfigCryptoWrongKeyException` runtime exception.
- Add configuration values processing support.
    - `dev.orne.config.ValueDecoder` interface.
    - `dev.orne.config.ValueDecorator` interface.
    - `dev.orne.config.ValueEncoder` interface.
- Add mutable configuration change events support.
    - `dev.orne.config.WatchableConfig` interface.
- Add support and fluent configuration API for configuration properties encryption.
    - `dev.orne.config.ConfigCryptoEngine` interface.
    - `dev.orne.config.ConfigCryptoProvider` interface.
- Add fluent configuration base API.
    - `dev.orne.config.ConfigBuilder` interface.
    - `dev.orne.config.MutableCapableConfigBuilder` interface.
    - `dev.orne.config.MutableConfigBuilder` interface.
- Add variable resolution support.
    - Add `dev.orne.config.ConfigBuilder.withVariableResolution()` method.
- Add fluent configuration API for environment variables based configuration.
    - `dev.orne.config.EnvironmentConfigBuilder` interface.
    - Add `dev.orne.config.ConfigBuilder.fromEnvironmentVariables()` method.
- Add fluent configuration API for System properties based configuration.
    - `dev.orne.config.SystemConfigBuilder` interface.
    - Add `dev.orne.config.ConfigBuilder.fromSystemProperties()` method.
- Add fluent configuration API for Java `Properties` based configuration.
    - `dev.orne.config.PropertiesConfigBaseBuilder` interface.
    - `dev.orne.config.PropertiesConfigBuilder` interface.
    - `dev.orne.config.PropertiesMutableConfigBuilder` interface.
    - Add `dev.orne.config.ConfigBuilder.fromPropertiesFiles()` method.
- Add fluent configuration API for Java `Preferences` based configuration.
    - `dev.orne.config.PreferencesConfigInitialBuilder` interface.
    - `dev.orne.config.PreferencesConfigBuilder` interface.
    - `dev.orne.config.PreferencesMutableConfigBuilder` interface.
    - Add `dev.orne.config.ConfigBuilder.fromJavaPreferences()` method.
- Add support and fluent configuration API for JSON based configuration based on Jackson.
    - `dev.orne.config.JsonConfigBaseBuilder` interface.
    - `dev.orne.config.JsonConfigBuilder` class.
    - `dev.orne.config.JsonMutableConfigBuilder` interface.
    - Add `dev.orne.config.ConfigBuilder.fromJson()` method.
- Add support and fluent configuration API for YAML based configuration based on Jackson.
    - `dev.orne.config.YamlConfigBaseBuilder` interface.
    - `dev.orne.config.YamlConfigBuilder` class.
    - `dev.orne.config.YamlMutableConfigBuilder` interface.
    - Add `dev.orne.config.ConfigBuilder.fromYaml()` method.
- Add support and fluent configuration API for XML based configuration based on Jackson.
    - `dev.orne.config.XmlConfigBaseBuilder` interface.
    - `dev.orne.config.XmlConfigBuilder` class.
    - `dev.orne.config.XmlMutableConfigBuilder` interface.
    - Add `dev.orne.config.ConfigBuilder.fromXml()` method.
- Add support and fluent configuration API for Commons Configuration 2.x based configuration.
    - `dev.orne.config.CommonsConfigBuilder` interface.
    - `dev.orne.config.CommonsConfigNodeBuilder` interface.
    - `dev.orne.config.CommonsMutableConfigBuilder` interface.
    - Add `dev.orne.config.ConfigBuilder.fromJavaPreferences()` method.
- Add support and fluent configuration API for Spring Environment based configuration.
    - `dev.orne.config.SpringEnvironmentConfigInitialBuilder` interface.
    - `dev.orne.config.SpringEnvironmentConfigBuilder` interface.
    - Add `dev.orne.config.ConfigBuilder.fromSpringEnvironment()` method.
- Add support to use `Config` as Apache Commons Configuration 2.x `ImmutableConfiguration`.
    - `dev.orne.config.commons.DelegatedOrneConfiguration` class.
- Add support to use `MutableConfig` as Apache Commons Configuration 2.x `Configuration`.
    - `dev.orne.config.commons.DelegatedOrneMutableConfiguration` class.
- Add support to use `ConfigCryptoProvider` as Apache Commons Configuration 2.x `ConfigurationDecoder`.
    - `dev.orne.config.commons.DelegatedOrneConfigurationDecoder` class.
- Add support for configuration subtypes and proxies.
    - Add `dev.orne.config.Config.as()` method.
    - `dev.orne.config.DelegatedConfig` class.
    - `dev.orne.config.DelegatedMutableConfig` class.
    - `dev.orne.config.DelegatedWatchableConfig` class.
- Add fluent configuration API for configurable componentes management.
    - `dev.orne.config.ConfigProviderBuilder` interface.
    - Add `dev.orne.config.ConfigProvider.builder(Config)` method.
    - Add `dev.orne.config.Configurer.fromProvider(ConfigProvider)` method.
- Add support of usage of `Config` beans as `PropertySource`s on Spring applications.
    - `dev.orne.config.spring.ConfigPropertySource` annotation.
    - `dev.orne.config.spring.ConfigPropertySources` annotation.

### Removed

- **Breaking:** Removed obsolete API.
    - `dev.orne.config.HierarchicalConfig` interface.
- **Breaking:** Removed and made internal implementation classes.
    - `dev.orne.config.AbstractConfig` class.
    - `dev.orne.config.AbstractStringConfig` class.
    - `dev.orne.config.SystemConfig` class.
    - `dev.orne.config.AbstractMutableStringConfig` class.
    - `dev.orne.config.PropertiesConfig` class.
    - `dev.orne.config.PreferencesConfig` class.
    - `dev.orne.config.DefaultConfigurer` class.
    - `dev.orne.config.DefaultConfigProvider` class.

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
