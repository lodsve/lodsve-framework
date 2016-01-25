# 更新日志
---
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