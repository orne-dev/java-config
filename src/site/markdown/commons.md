# Apache Commons Configuration integration

## Orne config to Apache Commons Configuration

Instances of Orne config can be converted to Apache Commons Configuration
instances using the `DelegatedOrneConfiguration` class.

This allows for seamless integration with systems that utilize Apache Commons
Configuration for configuration management.

```java
// Obtain an Orne config instance
Config config = ...;
// Convert to Apache Commons Configuration
org.apache.commons.configuration2.ImmutableConfiguration apacheConfig =
        new DelegatedOrneConfiguration(config);
```

Mutable Orne config instances can be converted similarly using
the `DelegatedOrneMutableConfiguration` class:

```java
// Obtain a mutable Orne config instance
MutableConfig mutableConfig = ...;
// Convert to Apache Commons Mutable Configuration
org.apache.commons.configuration2.Configuration apacheMutableConfig =
        new DelegatedOrneMutableConfiguration(mutableConfig);
```

## Apache Commons Configuration to Orne config

Instances of Apache Commons Configuration can be converted to Orne config
instances using the `dev.orne.config.Config.fromApacheCommons()` method:

```java
// Obtain an Apache Commons Configuration instance
org.apache.commons.configuration2.ImmutableConfiguration apacheConfig = ...;
// Convert to Orne config
Config config = Config.fromApacheCommons()
        .ofDelegate(apacheConfig)
        .build();
```

Mutable Apache Commons Configuration instances can be converted similarly:

```java
 // Obtain a mutable Apache Commons Configuration instance
org.apache.commons.configuration2.Configuration apacheConfig = ...;
// Convert to Orne mutable config
MutableConfig config = Config.fromApacheCommons()
        .mutable()
        .ofDelegate(apacheConfig)
        .build();
```

[apache commons configuration]: https://commons.apache.org/proper/commons-configuration/
