# Netty TCP Server Example

A small Netty TCP server to serve as an example.

To use the project first package it:

```
mvn clean package
```

The JAR will be a runnable Java file. It can be executed like this:

```
java -jar target/server.jar start 8080
```

To show other commands:

```
java -jar target/server.jar -h
```

This project is part of a series of examples:
- [Netty TCP Client Example](https://github.com/Bernardo-MG/netty-tcp-client-example)
- [Reactor Netty TCP Client Example](https://github.com/Bernardo-MG/reactor-netty-tcp-client-example)
- [Netty TCP Server Example](https://github.com/Bernardo-MG/netty-tcp-server-example)
- [Reactor Netty TCP Server Example](https://github.com/Bernardo-MG/reactor-netty-tcp-server-example)

But there are more Netty examples:
- [Netty TCP Proxy Example](https://github.com/Bernardo-MG/netty-tcp-proxy-example)
- [Reactor Netty TCP Proxy Example](https://github.com/Bernardo-MG/reactor-netty-tcp-proxy-example)

[![Release docs](https://img.shields.io/badge/docs-release-blue.svg)][site-release]
[![Development docs](https://img.shields.io/badge/docs-develop-blue.svg)][site-develop]

[![Release javadocs](https://img.shields.io/badge/javadocs-release-blue.svg)][javadoc-release]
[![Development javadocs](https://img.shields.io/badge/javadocs-develop-blue.svg)][javadoc-develop]

## Features

- Netty TCP server
- Command Line Client

## References

- [Netty-Simple-UDP-TCP-server-client](https://github.com/narkhedesam/Netty-Simple-UDP-TCP-server-client)

## Documentation

Documentation is always generated for the latest release, kept in the 'master' branch:

- The [latest release documentation page][site-release].
- The [latest release Javadoc site][javadoc-release].

Documentation is also generated from the latest snapshot, taken from the 'develop' branch:

- The [the latest snapshot documentation page][site-develop].
- The [latest snapshot Javadoc site][javadoc-develop].

### Building the docs

The documentation site is actually a Maven site, and its sources are included in the project. If required it can be generated by using the following Maven command:

```
mvn verify site
```

The verify phase is required, otherwise some of the reports won't be generated.

## Collaborate

Any kind of help with the project will be well received, and there are two main ways to give such help:

- Reporting errors and asking for extensions through the issues management
- or forking the repository and extending the project

### Issues management

Issues are managed at the GitHub [project issues tracker][issues], where any Github user may report bugs or ask for new features.

### Getting the code

If you wish to fork or modify the code, visit the [GitHub project page][scm], where the latest versions are always kept. Check the 'master' branch for the latest release, and the 'develop' for the current, and stable, development version.

## License

The project has been released under the [MIT License][license].

[issues]: https://github.com/bernardo-mg/netty-tcp-server-example/issues
[javadoc-develop]: https://docs.bernardomg.com/development/maven/netty-tcp-server-example/apidocs
[javadoc-release]: https://docs.bernardomg.com/maven/netty-tcp-server-example/apidocs
[license]: https://www.opensource.org/licenses/mit-license.php
[scm]: https://github.com/bernardo-mg/netty-tcp-server-example
[site-develop]: https://docs.bernardomg.com/development/maven/netty-tcp-server-example
[site-release]: https://docs.bernardomg.com/maven/netty-tcp-server-example
