# Configuration instances

Interface `Config` is the main access point to application configuration.

In it's simplest form, a `Config` instance can be created using a function
that returns a String value for a given String key:
 
```java
Map<String, String> values = ...;
Config config = values::get;
````

Interface's default methods provide additional functionality, like retrieving
values as Boolean, Interger or Long.

```java
String host = config.get("host");
Integer port = config.getInteger("port");
Boolean enabled = config.getBoolean("enabled");
Long timeout = config.getLong("timeout");
// Or with default values
String host = config.get("host", "localhost");
int port = config.getInteger("port", 8080);
boolean enabled = config.getBoolean("enabled", true);
long timeout = config.getLong("timeout", 3000L);
```

If the underlying configuration storage system supports it, it is possible to
retrieve all property names available in the configuration:

```java
Config config = ...;
config.isEmpty(); // check if configuration has any property
config.getKeys().forEach(key -> { ... }); // iterate all property names
config.getKeys(prefix).forEach(key -> { ... }); // iterate property names with given prefix
config.getKeys(filter).forEach(key -> { ... }); // iterate property names matching given filter
```

## Mutable configurations

Mutable configurations can be accesed through the `MutableConfig`
interface, that extends `Config` with methods to set and remove properties.

```java
MutableConfig config = ...;
config.set("host", "localhost");
config.set("port", 8080);
config.set("enabled", true);
config.set("timeout", 3000L);
config.remove("obsolete.property");
```

If the underlying configuration storage system supports it, listeners can be
registered to be notified of changes to configuration properties.
Those mutable configurations will implement the `WatchableConfig` interface,
that extends `MutableConfig` with methods to add and remove change listeners:

```java
WatchableConfig config = ...;
WatchableConfig.Listener listener = (instance, props) -> {
    System.out.printf("Configuration properties changed on %s%n", instance);
    System.out.prinln("New values:");
    for (String prop : props) {
        String value = instance.get(prop);
        System.out.printf(" - %s = %s%n", prop, value);
    }
};
config.addListener(listener);
// Make some changes to the configuration
config.removeListener(listener);
```

## Configuration subtypes

Applications can provide extensions to the `Config` interface to provide
direct access to specific configuration properties.

Proxies to underlying `Config` instance of subtypes can be created using
`Config.as()` methods:

```java
public interface DatabaseConfig extends Config {
    default String getUrl() {
        return get("db.url");
    }
    default String getUsername() {
        return get("db.username");
    }
    default String getPassword() {
        return get("db.password");
    }
}

Config config = ...;
DatabaseConfig dbConfig = config.as(DatabaseConfig.class);
String url = dbConfig.getUrl();
String username = dbConfig.getUsername();
String password = dbConfig.getPassword();
```
