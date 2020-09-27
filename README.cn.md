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
我的开发工具，它封装了一些开源项目，并更好地促进了Java Web开发的进行。

## 什么是`lodsve`
`lodsve` 是 `Let our development of Spring very easy!` 这句话的首字母缩写.

## 简单的介绍
1. 基于一些开源框架。 它封装了一些类和方法，以使开发人员更加方便。
2. 包含了以下的模块：

    | 模块 | 描述 |
    | :--- | :--- |
    | lodsve-3rd | 自定义的第三方依赖 |
    | lodsve-cache | 缓存模块，基于ehcache/memcache/oscache/redis等实现 |
    | lodsve-core | 基础核心包 |
    | lodsve-dependencies | 依赖及其版本号的维护 |
    | lodsve-filesystem | 文件上传、下载等，支持阿里云OSS和亚马逊云S3 |
    | lodsve-framework-bom | Lodsve Framework 的组件清单 |
    | lodsve-mongodb | 连接 MongoDB 的组件 |
    | lodsve-mybatis | 扩展mybatis(mybatis 通用DAO/类型处理等) |
    | lodsve-rabbitmq | 连接 RabbitMQ 的组件 |
    | lodsve-rdbms | 关系型数据库数据源 |
    | lodsve-redis | 连接 Redis 的组件 |
    | lodsve-rocketmq | 连接 RocketMQ 的组件 |
    | lodsve-scripts | 脚本语言的支持 |
    | lodsve-search | 全文检索组件(支持solr/lucene) |
    | lodsve-security | 基于Spring MVC的简单认证、鉴权组件 |
    | lodsve-test | 对Spring-Test单元测试的简单扩展(mock and db unit test) |
    | lodsve-validate | 基于Spring AOP的入参校验组件 |
    | lodsve-web | Spring MVC 扩展(支持 swagger) |
    | lodsve-wechat | 微信SDK封装 |

## 如何去使用

    <dependency>
        <groupId>com.lodsve</groupId>
        <artifactId>lodsve-framework-bom</artifactId>
        <version>${lodsve.version}</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>

## 关于发版
版本号格式： `MAJOR.MINOR.PATCH-RELEASE/ALPHA/BETA`.

- `MAJOR`：当做了一些不向后兼容的修改时的版本
- `MINOR`：当以向后兼容的方式添加功能时的版本
- `PATCH` 当进行向后兼容的错误修复时的版本。
- `RELEASE` 表示稳定的发行版本。
- `ALPHA` 表示内部版本。
- `BETA` 意味着仅用于测试。

## 最新版本号
现在的最新版本号是： `2.7.6-RELEASE`。

您还可以在Maven Central中找到最新版本：`http://repo1.maven.org/maven2/com/lodsve/lodsve-framework/` 。

## 文档
查看使用文档 [reference docs][].

查看master分支的API文档 [Api Docs][].

## 检出源码
`git clone git@github.com:lodsve/lodsve-framework.git`

## 查看配置文件模板
`cd lodsve-core/src/main/resources/META-INF/config-template`

## 导入到您的IDE中
在项目根目录运行命令 `mvn idea:idea` 或者 `mvn eclipse:eclipse` 。
> **注意:** 根据上述先决条件，确保已在IDE中正确配置了 `JDK 8`，`Maven 3.3.X` 和 `Lombok插件` 。

1. 配置Git

        git config --global user.name "your name"
        git config --global user.email "your email"
        git config --global core.autocrlf false
        git config --global core.safecrlf true
2. 配置您的IDE
    - Eclipse：打开 Settings-General-Workspace，修改 `New text file line delimiter` 为 `Unix`。
    - Eclipse：打开 Settings-General-Workspace，修改 `Text file encoding` 为 `UTF-8`。
    - IDE：打开 Setting-Editor-Code Style，修改 `line delimiter` 为 `Unix and OS X(\n)`。
    - IDE：打开 Setting-Editor-File encoding，修改所有的 `Encoding` 为 `UTF-8` 和 `with NO BOM`。
3. 必须要安装的IDE插件(Eclipse 和 Intellij IDEA):
    - Alibaba Java Coding Guidelines
    - Lombok plugin

## 更新历史记录
[CHANGELOG][]

## 联系我
1. Email: sunhao.java@gmail.com
2. QQ: [867885140][]
3. Blog: [Blog][] [OSChina][]

## License
`Lodsve Framework` 是在 [GNU General Public License][] 的3.0版下发布的。

## 鸣谢
`Lodsve Framework` 是基于JetBrains IDEA创建的。

![LOGO](.github/JetBrains.png "JetBrains")

[GNU GENERAL PUBLIC LICENSE]: https://opensource.org/licenses/GPL-3.0
[CHANGELOG]: https://github.com/lodsve/lodsve-framework/blob/master/CHANGELOG.md
[Blog]: https://www.crazy-coder.cn
[OSChina]: https://my.oschina.net/sunhaojava
[867885140]: http://wpa.qq.com/msgrd?v=3&uin=867885140&site=qq&menu=yes
[reference docs]: https://helps.lodsve.com/
[Api Docs]: https://apidoc.gitee.com/lodsve/lodsve-framework/
