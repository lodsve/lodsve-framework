# 更新日志

## V2.4.6
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