# Orne Java configuration utilities

Provides utilities for application configuration management.

## Status

[![License][status.license.badge]][status.license]
[![Latest version][status.maven.badge]][status.maven]
[![Javadoc][status.javadoc.badge]][javadoc]
[![Maven site][status.site.badge]][site]

| Latest Release | Develop |
| :------------: | :-------------: |
| [![Build Status][status.latest.ci.badge]][status.latest.ci] | [![Build Status][status.dev.ci.badge]][status.dev.ci] |
| [![Coverage][status.latest.cov.badge]][status.latest.cov] | [![Coverage][status.dev.cov.badge]][status.dev.cov] |

## Features

The library provides the following features:

- Simple usage API
- Environment variables based configuration provider
- System properties based configuration provider
- Java `Properties` based configuration provider
- Java `Preferences` based configuration provider
- JSON based configuration provider
- XML based configuration provider
- YAML based configuration provider
- Secure configuration values
- Fluent setup mechanism
- Configurations hierarchy
- Apache Commons Configuration integration
- Automatic components configuration
- Spring integration

## Usage

The binaries can be obtained from [Maven Central][status.maven] with the
`dev.orne:config` coordinates:

```xml
<dependency>
  <groupId>dev.orne</groupId>
  <artifactId>config</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Further information

For further information refer to the [Maven Site][site] and [Javadoc][javadoc].

[site]: https://orne-dev.github.io/java-config/
[javadoc]: https://javadoc.io/doc/dev.orne/config
[status.license]: http://www.gnu.org/licenses/gpl-3.0.txt
[status.license.badge]: https://img.shields.io/github/license/orne-dev/java-config
[status.maven]: https://central.sonatype.com/artifact/dev.orne/config
[status.maven.badge]: https://img.shields.io/maven-central/v/dev.orne/config.svg?label=Maven%20Central
[status.javadoc.badge]: https://javadoc.io/badge2/dev.orne/config/javadoc.svg
[status.site.badge]: https://img.shields.io/website?url=https%3A%2F%2Forne-dev.github.io%2Fjava-config%2F
[status.latest.ci]: https://github.com/orne-dev/java-config/actions/workflows/release.yml
[status.latest.ci.badge]: https://github.com/orne-dev/java-config/actions/workflows/release.yml/badge.svg?branch=master
[status.latest.cov]: https://sonarcloud.io/dashboard?id=orne-dev_java-config
[status.latest.cov.badge]: https://sonarcloud.io/api/project_badges/measure?project=orne-dev_java-config&metric=coverage
[status.dev.ci]: https://github.com/orne-dev/java-config/actions/workflows/build.yml
[status.dev.ci.badge]: https://github.com/orne-dev/java-config/actions/workflows/build.yml/badge.svg?branch=develop
[status.dev.cov]: https://sonarcloud.io/dashboard?id=orne-dev_java-config&branch=develop
[status.dev.cov.badge]: https://sonarcloud.io/api/project_badges/measure?project=orne-dev_java-config&metric=coverage&branch=develop
