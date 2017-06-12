# 更新日志

V2.5.8
1. 修改readme中的博客地址
2. 修改依赖的版本号
3. 运行main方法支持spring
4. 优化springfox的配置，springfox可以配置分组
5. 增加同步取主键
6. 重构配置文件加载
7. 重构autoconfigure，使@ConfigurationProperties可以在@Configuration配置文件中通过@Autowired使用
8. 修改license

## V2.5.7
1. 将配置文件的模板集成到框架中，目录为lodsve-core/resources/config-template
2. 添加maven发布自动close和release的插件
3. mybatis可以自定义拦截器
4. 是否是devMode的conditional
5. 常用的脚本语言在JVM中执行的相关封装
6. 修改关于conditional的webApplication的bug
7. 配置文件中，*.properties和framework/*.properties都加载到SystemConfig中
8. 修改config的目录结构
9. 增加lombok，后续使用
10. 移除dubbo基于注解的配置，使用xml的配置
11. 修改profile的配置

## V2.5.6
1. 前台传枚举的值或者code都支持反序列化

## V2.5.5
1. 修改pom,增加名为release的profile
2. 修复异常处理的bug
3. 升级flyway
4. 主键自增长
5. springfox配置描述信息
6. 系统配置文件路径结构修改
7. 重构validate

## V2.5.4
1. 修改dubbo版本
2. 框架的异常处理
3. 移除OSChina的maven配置,并修复一些依赖的问题

## V2.5.3
1. 实现spring-boot的condition,并修改框架中的一些实现
2. 修改包路径(改为lodsve)
3. 修改mybatis获取数据库类型的方式
4. 修改一些语法警告

## V2.5.2
1. 添加druid支持
2. 合并lodsve-base/lodsve-config/lodsve-logger为lodsve-core

## V2.5.1
1. 修改包路径为lodsve开头

## V2.4.11
1. 删除mybatis的通用repository，并修改相关联的代码
2. 配置: 增加必填项检查(@Required)
3. README.md添加logo

## V2.4.10
1. 完成简单工作流
2. swagger升级为springfox
3. 修改 cosmos-->lodsve
4. 支持spring cache
5. 修复若干bug

## V2.4.9
1. 修复父级pom忘记写入lodsve-logger了
2. mybatis不需要再写Enum的TypeHandler了,只需要在@EnableMyBatis中配置enumsLocations指定枚举在哪即可

## V2.4.8
1. 修改项目名为lodsve
2. 含义: Let our develop Spring very easy!
3. 修改相关文件夹名
4. 将groupId改成com.github.lodsve
5. 准备发布maven中央仓库相关工作

## V2.4.7
1. 将groupId改成com.github.cosmos

## V2.4.6
1. 调整项目结构
2. 添加微信公众号开发API
3. 简化security模块
4. 修改mybatis通用repository的CRUD返回值问题
5. 添加单元测试的(DBUnit Mockito PowerMockito Mock-Server)
6. 修改一些bug

## V2.4.5
1. mybatis支持排序

## V2.4.4
1. 新增dubbo支持
2. 通过配置spring的profile来控制是否启用swagger
3. 解决配置文件乱码问题
4. mybatis主键支持twitter的snowflake的ID生成器
5. 解决跨域问题
6. 修改一些bug

## V2.4.3
1. message-mybatis和message-mongodb配置basePackage时,默认是当前包路径
2. 解决之前spring-data-mongodb的版本冲突
3. swagger支持可配置路径

## V2.4.2
1. 使用Spring BeanWrapper实现配置即对象
2. servlet容器启动即加载properties和ini配置
3. 修改redis mongodb配置加载方式
4. mongodb mybatis等加载配置时,basePackage可以不写,默认是项目ApplicationConfiguration.java所在的包路径
5. domain自动转dto的一个bug修改
6. 关系型数据库使用类型安全的方式配置参数

## V2.4.1
1. 项目进行重构
2. 合并一些基础项目到message-base中
3. 重构spring的加载方式,eg:

	```
	<!-- spring配置 start -->
	...
    <servlet>
        <servlet-name>spring</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextClass</param-name>
            <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
        </init-param>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>...ApplicationConfiguration</param-value>
        </init-param>
    </servlet>
    ...
    <!-- spring配置 start -->

    ApplicationConfiguration.java
    @Configuration
    // ...
    @ComponentScan("your base packages")
    @ImportResource({"classpath*:/META-INF/springWeb/*.xml", "classpath*:/META-INF/spring/*.xml"})
    public class ApplicationConfiguration {
        // ...
    }
	```

## V2.3.4
1. 添加message-mongodb,对mongodb的支持

## V2.3.3
1. 大量使用JavaConfig，抛弃原有的xml配置

## V2.3.2
1. 实现配置即对象

## V2.3.1
1. 删除message-jdbc
2. 修改message-security为mybatis实现
3. 将key和helper移到mybatis中

## V2.3
1. message-mybatis通用DAO完善

## V2.2.1
1. message-mybatis添加通用DAO

## V2.2
1. 封装了一些mybatis的操作 
2. 改进其他的一些功能

## V2.1
1. 重构message-jdbc 
2. 整理message-datasource(分为：关系型数据库、mongoldb、reds三种数据源...) 
3. restful返回json支持枚举(显示value和name) 
4. restful返回json格式化日期类型

## V2.0-GA
1. 模块拆分完成

		message-amqp
		message-base
		message-cache
		message-config
		message-datasource
		message-email
		message-event
		message-exception
		message-jdbc
		message-json
		message-logger
		message-mvc
		message-search
		message-security
		message-tags
		message-template
		message-test
		message-utils
		message-validate
2. 编写使用说明文档

## V1.0-GA
1. 整理代码,按模块分离
2. 初次提交