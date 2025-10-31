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
