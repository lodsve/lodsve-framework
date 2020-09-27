![LOGO](https://raw.githubusercontent.com/lodsve/lodsve-documents/master/images/logo.png "lodsve-framework")

[![Build Status](https://travis-ci.org/lodsve/lodsve-framework.svg?branch=master)](https://travis-ci.org/lodsve/lodsve-framework)
[![License](https://img.shields.io/badge/license-GPLv3-yellowgreen.svg)]()
[![Maven Central](https://img.shields.io/maven-central/v/com.lodsve/lodsve-framework.svg)](https://search.maven.org/artifact/com.lodsve/lodsve-framework)
[![GitHub stars](https://img.shields.io/github/stars/lodsve/lodsve-framework.svg)](https://github.com/lodsve/lodsve-framework/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/lodsve/lodsve-framework.svg)](https://github.com/lodsve/lodsve-framework/network)
[![GitHub issues](https://img.shields.io/github/issues/lodsve/lodsve-framework.svg)](https://github.com/lodsve/lodsve-framework/issues)
[![GitHub pull requests](https://img.shields.io/github/issues-pr/lodsve/lodsve-framework.svg)](https://github.com/lodsve/lodsve-framework/pulls)
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Flodsve%2Flodsve-framework.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2Flodsve%2Flodsve-framework?ref=badge_shield)

## lodsve-framework
My development tools, it encapsulates some open source projects, and better facilitate the conduct of Java Web development.

## What is `lodsve`
`lodsve` is the short of `Let our development of Spring very easy!`.

## Simple Introduction
1. Base on some open source framework. It encapsulates some classes and methods to make more convenient for developers.
2. It consists of the following modules:

    | Module | Description |
    | :--- | :--- |
    | lodsve-3rd | Customized third-party packages |
    | lodsve-cache | Cache management for ehcache/memcache/oscache/redis/... |
    | lodsve-core | Basic and core package |
    | lodsve-dependencies | Lodsve Dependencies Management |
    | lodsve-filesystem | Manage oss/aws s3/... |
    | lodsve-framework-bom | Lodsve Framework (Bill of Materials) |
    | lodsve-mongodb | Connect to MongoDB |
    | lodsve-mybatis | Mybatis component(mybatis common DAO/type handler) |
    | lodsve-rabbitmq | RabbitMQ component |
    | lodsve-rdbms | DataSource for RDBMS |
    | lodsve-redis | Redis component |
    | lodsve-rocketmq | RocketMQ component |
    | lodsve-scripts | Script language support |
    | lodsve-search | Search component(solr/lucene) |
    | lodsve-security | Authentication and certification For Spring MVC |
    | lodsve-test | Spring unit test expand(mock and db unit test) |
    | lodsve-validate | Validate engine via Spring AOP |
    | lodsve-web | Spring MVC expand(include swagger) |
    | lodsve-wechat | WeChat SDK |

## How To Use

    <dependency>
        <groupId>com.lodsve</groupId>
        <artifactId>lodsve-framework-bom</artifactId>
        <version>${lodsve.version}</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>

## About release
Version No. like `MAJOR.MINOR.PATCH-RELEASE/ALPHA/BETA`.

- `MAJOR` version when I make incompatible API changes,
- `MINOR` version when I add functionality in a backwards-compatible manner
- `PATCH` version when I make backwards-compatible bug fixes.
- `RELEASE` means a stable release version.
- `ALPHA` means internal version.
- `BETA` means just for testing.

## Newest version
Now the newest and stable version is `2.7.6-RELEASE`.

You can also find the newest version in maven central: `http://repo1.maven.org/maven2/com/lodsve/lodsve-framework/`.

## Documentation
See the current [reference docs][].

See the master branch [Api Docs][].

## Check out sources
`git clone git@github.com:lodsve/lodsve-framework.git`

## Check out the configurations
`cd lodsve-core/src/main/resources/META-INF/config-template`

## Import sources into your IDE
Run command `mvn idea:idea` or `mvn eclipse:eclipse` in the root folder.
> **Note:** Per the prerequisites above, ensure that you have `JDK 8` and `Maven 3.3.X` and `Lombok Plugin` configured properly in your IDE.

1. Config your Git

        git config --global user.name "your name"
        git config --global user.email "your email"
        git config --global core.autocrlf false
        git config --global core.safecrlf true
2. Config your IDE
    - Eclipse: Open Settings-General-Workspace, modify `New text file line delimiter` as `Unix`
    - Eclipse: Open Settings-General-Workspace, modify `Text file encoding` as `UTF-8`
    - IDE: Open Setting-Editor-Code Style, modify `line delimiter` as `Unix and OS X(\n)`
    - IDE: Open Setting-Editor-File encoding, modify all `Encoding` as `UTF-8` and `with NO BOM`
3. Required IDE Plugins(Both Eclipse and Intellij IDEA):
    - Alibaba Java Coding Guidelines
    - Lombok plugin

## Change History
[CHANGELOG][]

## Contact me
1. Email: sunhao.java@gmail.com
2. QQ: [867885140][]
3. Blog: [Blog][] [OSChina][]

## License
The `Lodsve Framework` is released under version 3.0 of the [GNU General Public License][].

## Thanks
The `Lodsve Framework` was created using awesome [JetBrains IDEA][].

![LOGO](.github/JetBrains.png "JetBrains")

[GNU GENERAL PUBLIC LICENSE]: https://opensource.org/licenses/GPL-3.0
[CHANGELOG]: https://github.com/lodsve/lodsve-framework/blob/master/CHANGELOG.md
[Blog]: https://www.crazy-coder.cn
[OSChina]: https://my.oschina.net/sunhaojava
[867885140]: http://wpa.qq.com/msgrd?v=3&uin=867885140&site=qq&menu=yes
[reference docs]: https://helps.lodsve.com/
[Api Docs]: https://apidoc.gitee.com/lodsve/lodsve-framework/
[JetBrains IDEA]: https://www.jetbrains.com/?from=lodsve-framework
