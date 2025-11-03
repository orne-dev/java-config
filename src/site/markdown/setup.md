# Creation of configuration instances

## Configuration sources

Interface `Config` provides static methods for start fluent creation of
configuration instances from different sources.

All configuration builders support additional options described in the
[Shared options](#Shared_options) section.

### Environment variables

Configuration instance that retrieves values from environment variables
(`System.getenv()`) can be created using the
`Config.fromEnvironmentVariables()` method.

Example:

```java
Config config = Config.fromEnvironmentVariables()
        .build();
String path = config.get("PATH");
```

**This configuration source is read-only.**

### System properties

Configuration instance that retrieves values from system properties
(`System.getProperties()`) can be created using the
`Config.fromSystemProperties()` method.

Example:

```java
Config config = Config.fromSystemProperties()
        .build();
String userHome = config.get("user.home");
```

**This configuration source is read-only.**

### Java Properties

Configuration instance that uses Java `Properties` as storage mechanism
can be created using the `Config.fromProperties()` method.

Example:

```java
Map<String, String> localValues = ...;
Config config = Config.fromProperties()
        .load("example/config.properties")
        .add(localValues) // Overrides loaded properties
        .build();
String host = config.get("host");
```

This configuration source supports watchable mutable configurations that
can be persisted on files or output streams.

Example:

```java
Map<String, String> localValues = ...;
FileWatchableConfig config = Config.fromProperties()
        .load("example/config.properties")
        .add(localValues) // Overrides loaded properties
        .mutable()
        .build();
config.set("host", "example.com");
config.remove("timeout");
Path path = ...;
config.save(path);
```

### JSON

Jackson 2 based configuration instance that retrieves values from JSON files
or objects can be created using the `Config.fromJson()` method.

Example:

```java
Map<String, String> localValues = ...;
Config config = Config.fromJson()
        .load("config/application.json")
        .add(localValues) // Overrides loaded properties
        .build();
String host = config.get("host");
```

This configuration source supports watchable mutable configurations that
can be persisted on files or output streams.

Example:

```java
Map<String, String> localValues = ...;
FileWatchableConfig config = Config.fromJson()
        .load("example/config.json")
        .add(localValues) // Overrides loaded properties
        .mutable()
        .build();
config.set("host", "example.com");
config.remove("timeout");
Path path = ...;
config.save(path);
```

### YAML

Jackson 2 based configuration instance that retrieves values from YAML files
or objects can be created using the `Config.fromYaml()` method.

Example:

```java
Map<String, String> localValues = ...;
Config config = Config.fromYaml()
        .load("example/config.yml")
        .add(localValues) // Overrides loaded properties
        .build();
String host = config.get("host");
```

This configuration source supports watchable mutable configurations that
can be persisted on files or output streams.

Example:

```java
Map<String, String> localValues = ...;
FileWatchableConfig config = Config.fromYaml()
        .load("example/config.yml")
        .add(localValues) // Overrides loaded properties
        .mutable()
        .build();
config.set("host", "example.com");
config.remove("timeout");
Path path = ...;
config.save(path);
```

### XML files

Configuration instance that retrieves values from XML files
or documents can be created using the `Config.fromXmlFiles()` method.

**Note:** This configuration source ignores root XML element when converting
XML structure into configuration properties.
When multiple files are loaded expects all files to share first loaded
document's root XML element.

Example:

```java
Map<String, String> localValues = ...;
Config config = Config.fromXmlFiles()
        .load("example/config.xml")
        .add(localValues) // Overrides loaded properties
        .build();
String host = config.get("host");
```

This configuration source supports watchable mutable configurations that
can be persisted on files or output streams.

Example:

```java
Map<String, String> localValues = ...;
FileWatchableConfig config = Config.fromXmlFiles()
        .load("example/config.xml")
        .add(localValues) // Overrides loaded properties
        .mutable()
        .build();
config.set("host", "example.com");
config.remove("timeout");
Path path = ...;
config.save(path);
```

### Spring environment

Configuration instance that retrieves values from Spring environment properties
can be created using the `Config.fromSpringEnvironment()` method.

Example:

```java
Environment env = ...;
Config config = Config.fromSpringEnvironment()
        .ofEnvironment(env)
        .build();
String host = config.get("host");
```

By default Spring `Environment` does not allow iteration over properties keys,
but `ConfigurableEnvironment` does iterating over `EnumerablePropertySource`
properties, which is the most common scenario.
To enable iterable keys support, use the `withIterableKeys()` method:

```java
ConfigurableEnvironment env = ...;
Config config = Config.fromSpringEnvironment()
        .ofEnvironment(env)
        .withIterableKeys()
        .build();
if (config.contains("host")) {
    String host = config.get("host");
}
```

**This configuration source is read-only.**

### Java Preferences

Configuration instance that uses Java `Preferences` as storage mechanism
can be created using the `Config.fromJavaPreferences()` method.

Example:

```java
Properties localValues = ...;
Config config = Config.fromJavaPreferences()
        .add(localValues)
        .load("config/override.properties")
        .build();
String host = config.get("host");
```

This configuration source supports watchable mutable configurations that
can be synchronized with persisted on files or output streams.

Example:

```java
PreferencesMutableConfig config = Config.fromJavaPreferences()
        .load("config/application.properties")
        .mutable()
        .build();
config.set("host", "example.com");
config.remove("timeout");
// Synchronize changes to the underlying Preferences storage
config.sync();
// Flush changes to the persistent storage
config.flush();
```

### Apache Commons Configuration

Configuration instance that delegates on Apache Commons
`ImmutableConfiguration` can be created using the
`Config.fromApacheCommons()` method.

Example:

```java
ImmutableConfiguration delegated = ...;
Config config = Config.fromApacheCommons()
        .ofDelegate(delegated)
        .build();
String host = config.get("host");
```

This configuration source supports watchable mutable configurations
delegated on .

Example:

```java
Configuration delegated = ...;
WatchableConfig config = Config.fromApacheCommons()
        .ofDelegate(delegated)
        .mutable()
        .build();
config.set("host", "example.com");
config.remove("timeout");
```

## Shared options

### Mutable configurations

When supported by the underlying configuration source, configuration
instances can be made mutable using the `mutable()` method,
allowing to set and remove configuration values.

### Configuration hierarchy

A configuration can inherit values from a parent configuration declared
during the building process using the `withParent()` method.

This allows to create a hierarchy of configurations, where child
configurations can inherit values from their parent configurations.

A tipical use case is to have a base configuration for the application,
inhiriting environment variables and system properties,
that can be extended by environment specific configurations, like
development, testing and production configurations.

Example:

```java
Config baseConfig = ConfigProvider.fromProperties()
    .load("example/base.properties")
    .withParent(Config.fromSystemProperties()
        .withParent(Config.fromEnvironmentVariables())
        .build())
    .build();
Config config = ConfigProvider.fromProperties()
    .load("example/dev.properties")
    .withParent(baseConfig)
    .build();
```

#### Merging strategy

By default, when a configuration has a parent, the parent values have
precedence over the child values. This means that if both configurations have
a value for a given key, the parent configuration will provide the value for
that key.

This behavior can be changed during the building process using the
`withOverrideParentProperties()` method. When this method is used, the child
configuration will have precedence over the parent configuration, meaning that
if both configurations have a value for a given key, the child configuration will
override the parent configuration and provide the value for that key.

Example:

```java
Config baseConfig = ConfigProvider.fromProperties()
    .load("example/base.properties")
    .withParent(Config.fromSystemProperties()
        .withParent(Config.fromEnvironmentVariables())
        .build())
    .build();
Config config = ConfigProvider.fromProperties()
    .load("example/dev.properties")
    .withParent(baseConfig)
    .withOverrideParentProperties()
    .build();
```

### Decoders

Decoders allow decoding the configuration property values at runtime,
by applying a transformation function to the values retrieved from the
underlying configuration source.

This is useful for handling sensitive configuration values,
like API keys or passwords, that should be stored in an encoded format
in the configuration source, and decoded when retrieved
for usage.

This is enabled using the `withDecoder()` method
during the building process, allowing to chain multiple decorators.

Example:

```java
ValueDecoder parentDecode = ...;
Config parent = Config.fromProperties()
        .add(Map.of(
            "api.key", "my_encoded_api_key"))
        .withDecoder(parentDecode)
        .build();
ValueDecoder decode = ...;
Config config = Config.fromProperties()
        .add(Map.of(
            "api.child.key", "my_encoded_child_api_key"))
        .withParent(parent)
        .withDecoder(decode)
        .build();
String apiKey = config.get("api.child.key");
// apiKey will be the result of decode("my_encoded_child_api_key")
String parentApiKey = config.get("api.key");
// parentApiKey will be the result of parentDecode("my_encoded_api_key")
```

### Encoders

Encoders allow encoding the configuration property values at runtime,
by applying a transformation function to the values set in the
underlying configuration source.

This is useful for handling sensitive configuration values,
like API keys or passwords, that should be stored in an encoded format
in the configuration source, and encoded when set.

This is enabled using the `withEncoder()` method
during the building process, allowing to chain multiple decorators.

Example:

```java
ValueEncoder parentEncode = ...;
MutableConfig parent = Config.fromProperties()
        .mutable()
        .withEncoder(parentEncode)
        .build();
ValueEncoder encode = ...;
MutableConfig config = Config.fromProperties()
        .withParent(parent)
        .mutable()
        .withEncoder(encode)
        .build();
config.set("api.key", "my_plain_api_key");
// The value stored in the underlying configuration source of config
// will be the result encode("my_plain_api_key")
parent.set("api.key", "my_plain_api_key");
// The value stored in the underlying configuration source of parent
// will be the result of parentEncode("my_plain_api_key")
```

### Cryptographic encoders and decoders

A special type of encoders and decoders are the cryptographic encoders
and decoders, that allow to encode and decode configuration property values
using cryptographic algorithms, like AES or RSA.

This is enabled using the `withEncryption()` methods during the building
process, passing a `ConfigCryptoProvider` instance that provides the
cryptographic transformations.

Example:

```java
ConfigCryptoProvider crypto = ConfigCryptoProvider.builder()
        .withAesGcmEngine("secretSalt".getBytes(StandardCharsets.UTF_8))
        .withSecretKey("secretKey".toCharArray())
        .build();
MutableConfig config = Config.fromProperties()
        .add(Map.of(
            "api.key", "my_encoded_api_key"))
        .withEncryption(crypto)
        .mutable()
        .build();
String apiKey = config.get("api.key");
// apiKey will be the decrypted value of "my_encoded_api_key"
config.set("api.key", "my_plain_api_key");
// The value stored in the underlying configuration source of config
// will be the encrypted value of "my_plain_api_key"
```

Library provides built-in support for AES-GCM
algorithm. Additional algorithms can be implemented by providing custom
`ConfigCryptoEngine` implementations passed to
`CryptoProviderEngineBuilder.withEngine()`:

```java
ConfigCryptoEngine myCustomCryptoEngine = ...;
ConfigCryptoProvider crypto = ConfigCryptoProvider.builder()
        .withEngine(myCustomCryptoEngine)
        .withSecretKey("secretKey".toCharArray())
        .build();
```

Cryptographic encoders can be used in read-only configurations as well,
to decode encrypted configuration property values.
Example:

```java
ConfigCryptoProvider crypto = ...;
Config config = Config.fromProperties()
        .add(Map.of(
            "api.key", "my_encoded_api_key"))
        .withEncryption(crypto)
        .build();
String apiKey = config.get("api.key");
// apiKey will be the decrypted value of "my_encoded_api_key"
```

Cryptographic encoders and decoders can be chained with other
encoders and decoders as well.

Example:

```java
ConfigCryptoProvider crypto = ...;
ValueDecoder decode = ...;
ValueEncoder encode = ...;
MutableConfig config = Config.fromProperties()
        .add(Map.of(
            "api.key", "my_encoded_api_key"))
        .withEncryption(crypto)
        .withDecoder(decode)
        .withEncoder(encode)
        .mutable()
        .build();
String apiKey = config.get("api.key");
// apiKey will be the result of decode(decrypted("my_encoded_api_key"))
config.set("api.key", "my_plain_api_key");
// The value stored in the underlying configuration source of config
// will be the encrypted(encode("my_plain_api_key"))
```

### Decorators

Decorators allow modifying the configuration property values at runtime,
by applying a transformation function to the values retrieved from the
underlying configuration sources.

In oposition to decoders, decorators will modify only final values returned
by the configuration instance, thus a decorator on the parent configuration
will not affect the child configuration values, even if inherited from the
parent.

This is enabled using the `withDecorator()` method
during the building process, allowing to chain multiple decorators.

Example:

```java
ValueDecorator parentDecorate = ...;
Config parent = Config.fromProperties()
        .add(Map.of(
            "host", "example.com"))
        .withDecorator(parentDecorate)
        .build();;
ValueDecorator decorate = ...;
Config config = Config.fromProperties()
        .withParent(parent)
        .withDecorator(decorate)
        .build();
String host = config.get("host");
// host will be the result of decorate("example.com")
String parentHost = parent.get("host");
// parentHost will be the result of parentDecorate("example.com")
```

### Variable resolution

A special type of decorator is the variable resolution decorator,
that allows to use variables in configuration values, that will be
resolved with other configuration values at runtime.
This is enabled using the `withVariableResolution()` method
during the building process, allowing usage of multiple configuration
sources in the configuration hierarchy.

Example:

```java
Config config = Config.fromProperties()
        .add(Map.of(
            "host", "example.com",
            "port", "80"))
        .withParent(Config.fromProperties()
            .add(Map.of(
                "service.url", "http://${host}:${port}/api",
                "host", "localhost",
                "port", "8080")))
        .withOverrideParentProperties()
        .withVariableResolution()
        .build();
String url = config.get("service.url");
// url will be "http://example.com:80/api"
String parentUrl = config.getParent().get("service.url");
// parentUrl will be "http://${host}:${port}/api", as doesn't have variable resolution enabled
```

Variable substitutors can be chained with other decorators as well.

Example:

```java
ValueDecorator decorate = value -> "decorated[" + value + "]";
Config config = Config.fromProperties()
        .add(Map.of(
            "service.url", "http://${host}:${port}/api",
            "host", "localhost",
            "port", "8080"))
        .withVariableResolution()
        .withDecorator(decorate)
        .build();
String url = config.get("service.url");
// url will be "decorated[http://decorated[localhost]:decorated[8080]/api]"
```
