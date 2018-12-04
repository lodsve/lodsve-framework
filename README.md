![LOGO](https://helps.lodsve.com//images/logo.png "lodsve-framework")

[![Build Status](https://travis-ci.org/yangsenlin/snsdemo.svg?branch=master)](https://travis-ci.org/yangsenlin/snsdemo)
[![License](https://img.shields.io/badge/license-GPLv3-yellowgreen.svg)]()
[![Maven central](https://maven-badges.herokuapp.com/maven-central/com.lodsve/lodsve-framework/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.lodsve/lodsve-framework)
[![Average time to resolve an issue](http://isitmaintained.com/badge/resolution/lodsve/lodsve-framework.svg)](http://isitmaintained.com/project/lodsve/lodsve-framework "Average time to resolve an issue")
[![Percentage of issues still open](http://isitmaintained.com/badge/open/lodsve/lodsve-framework.svg)](http://isitmaintained.com/project/lodsve/lodsve-framework "Percentage of issues still open")

## lodsve-framework
My development tools, it encapsulates some open source projects, and better facilitate the conduct of Java Web development.

## What is `lodsve`
`lodsve` is the short of `Let our development of Spring very easy!`.

## Simple Introduction
1. Base on some open source framework. It encapsulates some classes and methods to make more convenient for developers.
2. It consists of the following modules:
    - lodsve-3rd
    - lodsve-all
    - lodsve-amqp
    - lodsve-cache
    - lodsve-core
    - lodsve-dfs
    - lodsve-mongodb
    - lodsve-mybatis
    - lodsve-rdbms
    - lodsve-redis
    - lodsve-search
    - lodsve-security
    - lodsve-test
    - lodsve-validate
    - lodsve-web
    - lodsve-wechat

## How To Use

    <dependency>
        <groupId>com.lodsve</groupId>
        <artifactId>lodsve-framework</artifactId>
        <version>${lodsve.version}</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
    
## About release
Version No. like `MAJOR.MINOR.PATCH-RELEASE/ALPHA/BETA`.

- `MAJOR` version when I make incompatible API changes,
- `MINOR` version when I add functionality in a backwards-compatible manner, and
- `PATCH` version when I make backwards-compatible bug fixes.
- `RELEASE` means a stable release version.
- `ALPHA` means internal version.
- `BETA` means just for testing.    
    
## Newest version
Now the newest and stable version is `2.7.0-RELEASE`.

You can also find the newest version in maven central: `http://repo1.maven.org/maven2/com/lodsve/lodsve-framework/`.
    
## Documentation
See the current [reference docs][].

See the master branch [Api Docs][].

## Check out sources
`git clone git@github.com:lodsve/lodsve-framework.git`

## Check out demos
`git clone git@github.com:lodsve/lodsve-demo.git`

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

## Donate
![AliPay](https://helps.lodsve.com/images/alipay.png "支付宝")
![WeChat](https://helps.lodsve.com/images/wechat.jpg "微信")

[GNU GENERAL PUBLIC LICENSE]: https://opensource.org/licenses/GPL-3.0
[CHANGELOG]: https://github.com/lodsve/lodsve-framework/blob/master/CHANGELOG.md
[Blog]: https://www.lodsve.com
[OSChina]: https://my.oschina.net/sunhaojava
[867885140]: http://wpa.qq.com/msgrd?v=3&uin=867885140&site=qq&menu=yes
[reference docs]: https://helps.lodsve.com/
[Api Docs]: https://apidoc.gitee.com/lodsve/lodsve-framework/