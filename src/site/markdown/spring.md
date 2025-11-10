# Spring integration

Spring Framework integration is fully supported.

Features and customizations are enabled on a per Spring context basis,
allowing different configurations to be applied on different contexts in case
of context hierarchies.
This means that child contexts **do not inherit** configurations from parent
contexts, and must be configured independently.
See the [context hierarchies](#Context_hierarchies) section for more details.

## Full features activation

To enable all features provided by the library in Spring Framework add
the `EnableOrneConfig` annotation to the Java configuration class:

```java
@Configuration
@EnableOrneConfig
class AppConfig {
    ...
}
```

This enables the following features:

- Default Spring `Environment` based `Config` instance.
- Detection and registration of additional `Config` beans present on Spring context.
- Usage of `Config` beans as `PropertySource`s on Spring applications.
- `PreferredConfig` based injection of `Config` instances into Spring beans.
- Automatic configuration of `Configurable` beans.

Default and additional `Config` customization can be achieved implementing the
`ConfigProviderCustomizer` interface, as described in the
[customization](#Default_Config_customization) section.

## Independent features activation

Each of the available features can be enabled independently, allowing
fine grained control over the integration.

### Config beans as `PropertySources`

This feature requires no additional configuration.
When the `ConfigPropertySource` annotation is used on a `Configuration` bean,
it is automatically registered as a Spring `PropertySource`:

```java
@Configuration
@ConfigPropertySource("appConfigPropertySource")
class AppConfig {

    @Bean("appConfigPropertySource")
    public Config appConfig() {
        ...
    }
}
```

**Note:** Do not add `Environment` based `Config` instances to the hierarchy
of `PropertySource`s, as this would create a circular dependency and result
in a `StackOverflowError` when resolving properties from the Spring
`Environment`.

### `PreferredConfig` based injection

The `PreferredConfig` based injection of `Config` instances into Spring beans
can be activated through the `EnablePreferredConfigInjection` annotation:

```java
@Configuration
@EnablePreferredConfigInjection
class AppConfig {

    @Bean
    public ConfigSubtype myConfig(
            // Injection of default Config instance is supported too
            Config config) {
        return Config.as(
            ...,
            ConfigSubtype.class);
    }

    @Bean
    public MyComponent myComponent(
        @PreferredConfig(ConfigSubtype.class)
        Config config) {
        ...
    }
}
```

Default `Config` customization can be achieved implementing the
`ConfigProviderCustomizer` interface, as described in the
[customization](#Default_Config_customization) section.

### Configurable components

Automatic configuration of `Configurable` beans can be enabled
through the `EnableConfigurableComponents` annotation.

```java
@Configuration
@EnableConfigurableComponents
class AppConfig {

    @Bean
    public Configurable myConfigurableComponent() {
        ...
    }
}
```

The annotation registers under the hoods a `BeanPostProcessor` that
automatically applies the configuration to all beans implementing
the `Configurable` interface using an internal `Configurer` instance
populated with `Config` instances found on Spring context.

The `Configurer` is not exposed as a bean by default, but it can be
exposed setting the `exposeConfigurer` attribute to `true`:

```java
@Configuration
@EnableConfigurableComponents(exposeConfigurer = true)
class AppConfig {

    @Autowired
    public void setConfigurer(Configurer configurer) {
        ...
    }
}
```

Default `Config` customization can be achieved implementing the
`ConfigProviderCustomizer` interface, as described in the
[customization](#Default_Config_customization) section.

## Default `Config` customization

By default the library creates a `Config` instance based on the Spring
`Environment` and registers all `Config` beans found on the Spring context
as additional configurations.

This means that when a `Config` instance is requested the default
`Environment` based `Config` instance is used, unless a more specific `Config`
type is requested through the `PreferredConfig` annotation.

When additional `Config` beans are registered, they are mapped to the
interfaces extending `Config` that they implement, if no previous mapping
exists.
The order in wich the additional `Config` instances available in the
Spring context are registered is not guaranteed, so if multiple `Config`
instances implementing the same interface are present, the mapped instance
is indeterminate.

The library provides the `ConfigProviderCustomizer` interface to
customize the default `Config` instance and the registered `Config` beans.

A implementation of the interface can be registered as a Spring bean
on each Spring context:

```java
@Configuration
@EnableOrneConfig
class AppConfiguration implements ConfigProviderCustomizer {

    @Override
    public Config configureDefaultConfig(
            Map<String, Config> configs) {
        // Customize and return the default Config instance.
    }

    @Override
    public void registerAdditionalConfigs(
            ConfigRegistry registry,
            Map<String, Config> configs) {
        // Fine tune the registration of additional Config instances.
        // Allows to control the order of registration and/or
        // register instances no present on the Spring context.
        ...
        // If desired, call the default implementation to
        // register all the instances present on the Spring context.
        ConfigProviderCustomizer.super.registerAdditionalConfigs(registry, configs);
    }
}
```

If more control is needed applications can provide their own
`ConfigProvider` bean implementation, bypassing the creation of the
internal `ConfigProvider`:

```java
@Configuration
class AppConfiguration {

    @Bean
    public ConfigProvider configProvider() {
        // Create and return a custom ConfigProvider instance.
    }
}
```

## Context hierarchies

Features and customizations are enabled on a per Spring context basis,
allowing different configurations to be applied on different contexts in case
of context hierarchies.
This means that child context **do not inherit** features enabled in parent
contexts, and must be configured independently.

The following will not work as expected:

```java
@Configuration
@EnableOrneConfig
class ParentConfig {

    @Bean
    public MyComponent parentComponent(
            Config config) {
        // No problem. Default Environment based configuration used
        ...
    }
}

@Configuration
class ChildConfig {

    @Bean
    public MyComponent myComponent(
            @PreferredConfig(ConfigSubtype.class)
            Config config) {
        // No Config instance is available for injection here
        // and no PreferredConfig based injection is supported
        ...
    }
}
```

Thus, if preferred `Config` instances should be injected into beans in a child
context, the child context must also enable the `PreferredConfig` based
injection feature through the `EnableOrneConfig` or
`EnablePreferredConfigInjection` annotation.
The same applies to the automatic configuration of `Configurable`
beans, which must be enabled in each context through the
`EnableOrneConfig` or `EnableConfigurableComponents` annotation.

Instances of `Config` created in a parent context are still visible
to child contexts, so they can be injected or used to configure
`Configurable` beans in child contexts.

```java
@Configuration
@EnableOrneConfig
class ParentConfig {

    @Bean
    public ConfigSubtype myConfig(
            Config config) {
        return Config.as(
            ...,
            ConfigSubtype.class);
    }
}

@Configuration
@EnableOrneConfig
class ChildConfig {

    @Bean
    public MyComponent myComponent(
            @PreferredConfig(ConfigSubtype.class)
            Config config) {
        // Injected preferred Config instance comes from parent context
        ...
    }
}
```

Same applies to `ConfigProviderCustomizer` and application provided
`ConfigProvider` based customizations.
Customizations applied in a parent context do not affect child contexts,
which must provide their own customization if needed.
