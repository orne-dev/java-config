# Configurable components

The library provides the `Configurable` interface that can be implemented by
components that require configuration after instantiation.

The interface defines two methods with default implementations,
`configure`, that receives a `Config` object for programmatic
configuration, and `isConfigured`, that can return `true` if the
component has been configured to prevent re-configuration.

```java
class MyComponent implements Configurable {

    private String host;
    private int port;
    private boolean configured = false;

    @Override
    public void configure(Config config) {
        this.host = config.get("host", "localhost");
        this.port = config.getInteger("port", 8080);
        this.configured = true;
    }

    @Override
    public boolean isConfigured() {
        return this.configured;
    }
}
```

The class `Configurer` is the responsible for applying a `Config` to
the `Configurable` components, and is capable of configuring automatically
properties annotated with `@ConfigurableProperty`.
Nested `Configurable` beans are not configured by default.

Example:

```java
class MyComponent implements Configurable {

    @ConfigurableProperty("host")
    private String host;
    @ConfigurableProperty("port")
    private int port;
    // Does not require override configure() method
}
```

**Note:** The configurable bean must have accesible setters for the
annotated properties, or the properties must be public.

## Customization of components configuration

Components can customize the configuration process with the annotations
`@PreferredConfig` and `@ConfigurationOptions`, that can be applied to the
class.

### Preferred configurations

The `PreferredConfig` annotation can be used to specify one or more
preferred `Config` subtypes for the component.
When the `Configurer` configures the component, it will try to find
a `Config` instance of the preferred types in the order specified,
and use the first one found to configure the component.
If none of the preferred configurations is found, the default
configuration provided to the `Configurer` will be used, unless
the `fallbackToDefaultConfig` option is set to `false` in the
annotation.

This allows libraries to provide specific `Config` subtypes for their
configurable components, while still allowing the application to
configure them using the default configuration.
The application that uses the library can use it's default configuration
or provide the library specific configuration to configure the
library components.

Components can convert to the specific `Config` subtype using the
`Config.as(Class<T>)` method in the `configure` method.

Example:

```java
interface MySuperConfig extends Config {}
interface MyConfig extends MySuperConfig {

    final String HOST_PROP = "host";
    final String PORT_PROP = "port";

    default String getHost() {
        return get(HOST_PROP, "localhost");
    }

    default int getPort() {
        return getInteger(PORT_PROP, 8080);
    }
}

@PreferredConfig({ MyConfig.class, MySuperConfig.class })
class MyComponent implements Configurable {

    private String host;
    private int port;

    @Override
    public void configure(Config config) {
        MyConfig myConfig = config.as(MyConfig.class);
        this.host = myConfig.getHost();
        this.port = myConfig.getPort();
   }
}
```

This is useful for external libraries components, that can
be configured using the library custom `Config` subtypes,
providing specific configuration for the library,
or fallback to application's default configuration.

### Configuration options

The `ConfigurationOptions` annotation allows to customize the configuration
process for the component.

By default, the `Configurer` will configure properties annotated
with `@ConfigurableProperty`.
As properties are only configured if annotated with
`@ConfigurableProperty`, setting `configureProperties` to `false` can be
used to skip the properties configuration step, for example in components
extending other configurable components that already configure their
properties.

Example:

```java
class BaseComponent implements Configurable {
    ...
    @ConfigurableProperty("host")
    private String host;
    ...
}
@ConfigurationOptions(configureProperties = false)
class MyExtendedComponent extends BaseComponent {
    ...
    @Override
    public void configure(Config config) {
        ...
        setHost(...);
        ...
    }
    ...
}
```

Nested `Configurable` beans are not configured by default, as independent
configuration is expected for them.
This can be overriden setting `configureNestedBeans` to `true`, to enable
automatic configuration of nested beans.

Example:

```java
class NestedComponent implements Configurable {
    ...
}
@ConfigurationOptions(configureNestedBeans = true)
class MyComponent implements Configurable {
    ...
    private NestedComponent nestedComponent = new NestedComponent();
    ...
}
```

**Note:** The configurable bean must have accesible getters for the
nested configurable beans, or the property must be public.

## Configurer programmatic usage

The `Configurer` can be created programmatically using the static
`Configurer.fromProvider` method, that expectes a `ConfigProvider`
with a mandatory default configuration and optional alternative configurations:

```java
Config config = ...;
AltConfig altConfig = ...;
Configurer configurer = Configurer.fromProvider(
        ConfigProvider.builder(config)
            .addConfig(altConfig)
            .build());

Configurable component = ...;
configurer.configure(component);
```

## Spring integration

Usage of configurable beans in Spring Framework is fully supported.
See the [Spring integration](spring.md) document for details.
