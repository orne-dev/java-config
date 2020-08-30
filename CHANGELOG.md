# :package: 0.2.0

01. :wrench: Update parent POM to 0.1.1
01. :wrench: Added dependency with `dev.orne:beans:0.3.0`
01. :gift: Added exception `dev.orne.config.ConfigException`
    01. :boom: Methods of `dev.orne.config.Config` throw `ConfigException`
    01. :boom: Methods of `dev.orne.config.MutableConfig` throw `ConfigException`
    01. :boom: Methods of `dev.orne.config.AbstractConfig` throw `ConfigException`
    01. :boom: Methods of `dev.orne.config.AbstractStringConfig` throw `ConfigException`
    01. :boom: Methods of `dev.orne.config.AbstractMutableStringConfig` throw `ConfigException`
    01. :boom: Methods of `dev.orne.config.PreferencesConfig` throw `ConfigException`
    01. :boom: Methods of `dev.orne.config.PropertiesConfig` throw `ConfigException`
    01. :boom: Methods of `dev.orne.config.SystemConfig` throw `ConfigException`
01. Simplified interface `dev.orne.config.Config`
    01. :boom: Removed method `Instant getInstant(String)`
01. Simplified class  `dev.orne.config.AbstractConfig`
    01. :boom: Adapted to `dev.orne.config.Config` changes
    01. :gift: Delegated value conversion in instance of Apache Commons BeanUtils `ConvertUtilsBean`
    01. :gift: Added method `convertValue(Object, Class)`
01. Simplified class `dev.orne.config.AbstractStringConfig`
    01. :boom: Adapted to `dev.orne.config.AbstractConfig` changes
    01. :gift: Added support for null values placeholder
01. Simplified class `dev.orne.config.AbstractMutableStringConfig`
    01. :boom: Adapted to `dev.orne.config.AbstractStringConfig` changes
    01. :gift: Delegated value to String conversion in instance of Apache Commons BeanUtils `ConvertUtilsBean`
01. :boom: Adapted `dev.orne.config.SystemConfig` to `dev.orne.config.AbstractStringConfig` changes
01. :gift: Added class `dev.orne.config.DelegatedConfig`
01. :gift: Added class `dev.orne.config.MutableDelegatedConfig`
01. :gift: Added interface `dev.orne.config.ConfigCryptoProvider`
    01. :gift: Added exception `dev.orne.config.ConfigCryptoProviderException`
    01. :gift: Added exception `dev.orne.config.ConfigCryptoWrongKeyException`
01. :gift: Added class `dev.orne.config.EncryptedConfig`
01. :gift: Added class `dev.orne.config.MutableEncryptedConfig`
01. :gift: Added interface `dev.orne.config.ConfigCryptoEngine`
    01. :gift: Added class `dev.orne.config.AbstractConfigCryptoEngine`
    01. :gift: Added class `dev.orne.config.ConfigCryptoAesGcmEngine`

# :package: 0.1.0

01. :gift: Added `dev.orne.config.Config`
01. :gift: Added `dev.orne.config.AbstractConfig`
01. :gift: Added `dev.orne.config.HierarchicalConfig`
01. :gift: Added `dev.orne.config.AbstractStringConfig`
01. :gift: Added `dev.orne.config.SystemConfig`
01. :gift: Added `dev.orne.config.MutableConfig`
01. :gift: Added `dev.orne.config.AbstractMutableStringConfig`
01. :gift: Added `dev.orne.config.PropertiesConfig`
01. :gift: Added `dev.orne.config.Configurable`
01. :gift: Added `dev.orne.config.ConfigurableProperty`
01. :gift: Added `dev.orne.config.ConfigurationOptions`
01. :gift: Added `dev.orne.config.Configurer`
01. :gift: Added `dev.orne.config.ConfigProvider`
01. :gift: Added `dev.orne.config.DefaultConfigurer`
01. :gift: Added `dev.orne.config.DefaultConfigProvider`
