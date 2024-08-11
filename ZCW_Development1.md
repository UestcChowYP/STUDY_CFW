# 众筹网

# 1.准备阶段

## 1.项目简介

- 项目架构

<img src=".\ZCW_MarkDownPhoto\项目架构.png" alt="项目架构" style="zoom:67%;" />

- 瀑布模型
  - 架构不够灵活

<img src=".\ZCW_MarkDownPhoto\瀑布模型.png" alt="瀑布模型" style="zoom:50%;" />

- 敏捷开发：根据需求快速的开发出可运行的代码，看作生物进化
  - 小步快跑：高频小体量的迭代

## 2.项目

- 项目目标

<img src=".\ZCW_MarkDownPhoto\项目目标.png" alt="项目目标" style="zoom:67%;" />

- 项目架构

<img src=".\ZCW_MarkDownPhoto\架构图.png" alt="架构图" style="zoom:67%;" />

- Maven工程依赖关系

<img src=".\ZCW_MarkDownPhoto\Maven依赖关系.png" alt="Maven依赖关系" style="zoom:67%;" />

## 3.创建数据库和数据库表

- 物理建模
- 数据库的三大范式
  - 第一范式:数据库表中的每一列都不可再分，也就是**原子性**。
  - 第二范式:在满足第一范式基础上要求每个字段都和主键**完整相关**，而不是仅和主键部分相关(主要针对联合主键而言)
  - 第三范式:表中的非主键字段和主键字段**直接相关**，不允许间接相关
- 冗余字段：避免表的扩容

### 1.创建

- 创建数据库

```mysql
CREATE DATABASE `project_zcw` CHARACTER SET utf8;
```

- 创建管理员表

```mysql
CREATE TABLE t_admin(
id INT NOT NULL AUTO_INCREMENT,
login_acct VARCHAR(255) NOT NULL,
user_pswd CHAR(32) NOT NULL,
user_name VARCHAR(32) NOT NULL,
email VARCHAR(255) NOT NULL,
create_time CHAR(19),
PRIMARY KEY (id)
)
```

### 2.MyBatis逆向工程

- 逆向工程pom依赖配置(ZWC06-common-reverse项目)

```xml
<build>
        <plugins>
            <plugin>
                <!-- https://mvnrepository.com/artifact/org.mybatis.generator/mybatis-generator-maven-plugin -->
                    <groupId>org.mybatis.generator</groupId>
                    <artifactId>mybatis-generator-maven-plugin</artifactId>
                    <version>1.3.7</version>
<!--                插件的依赖-->
                <dependencies>
                    <!-- https://mvnrepository.com/artifact/org.mybatis.generator/mybatis-generator-core -->
                    <dependency>
                        <groupId>org.mybatis.generator</groupId>
                        <artifactId>mybatis-generator-core</artifactId>
                        <version>1.3.7</version>
                    </dependency>
                    <!-- https://mvnrepository.com/artifact/com.mchange/c3p0 -->
                    <dependency>
                        <groupId>com.mchange</groupId>
                        <artifactId>c3p0</artifactId>
                        <version>0.9.5.2</version>
                    </dependency>
                    <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>5.1.3</version>
                    </dependency>

                </dependencies>
            </plugin>
        </plugins>
    </build>
```

- 逆向工程配置文件:generatorConfig.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
<!--
targetRuntime:执行生成的逆向工程的版本
MyBatis3simple:生成基本的CRUD(清新简洁版)
MyBatis3:生成带条件的CRUD(奢华尊享版)
-->
    <context id="DB2Tables"  targetRuntime="MyBatis3">
        <!-- 数据库连接驱动类,URL，用户名、密码 -->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:4306/project_zcw"
                        userId="root"
                        password="z123456">
        </jdbcConnection>
        <!-- javaBean的生成策略 -->
        <javaModelGenerator targetPackage="com.uestc.crowd.entity" targetProject="./src/main/java">
            <!-- 在targetPackage的基础上，根据数据库的schema再生成一层package，最终生成的类放在这个package下，默认为false -->
            <property name="enableSubPackages" value="true"/>
            <!-- 设置是否在getter方法中，对String类型字段调用trim()方法:去除字符串前后的空格-->
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>
        <!--SQL映射文件的生成策略-->
        <!-- 生成xml映射文件：包名(targetPackage)、位置(targetProject) -->
        <sqlMapGenerator targetPackage="com.uestc.crowd.mapper" targetProject=".\src\main\resources">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>
        <!--Mapper接口的生成策略-->
        <!-- 生成DAO接口：包名(targetPackage)、位置(targetProject) -->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.uestc.mybatis.mapper" targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>

        <!--逆向分析的表-->
        <!-- tableName设置为*号，可以对应所有表，此时不写domainobjectName -->
        <!--domainobjectName属性指定生成出来的实体类的类名-->
        <table tableName="t_admin" domainObjectName="Admin" />
    </context>
</generatorConfiguration>
```

- 执行逆向工程生成：mybatis-generator:generate
- 文件归位：分模块
  - entity实体类：ZWC04-admin-entity
  - Mapper接口：ZWC03-admin-component
  - Mapper配置文件：ZWC02-admin-webui

## 4.父工程依赖管理

## 5.Spring 整合 MyBatis

- 目标：adminMapper通过 IOC容器装配到当前组件中后，就可以直接调用它的方法，享受到框架给我们提供的方便

<img src=".\ZCW_MarkDownPhoto\spring整合mybatis.png" alt="spring整合mybatis" style="zoom: 50%;" />

- 配置思路

![spring整合mybatis_思路](.\ZCW_MarkDownPhoto\spring整合mybatis_思路.png)

![spring整合mybatis_思路2](.\ZCW_MarkDownPhoto\spring整合mybatis_思路2.png)

- 操作步骤清单
  - 在子工程中加入搭建环境所需要的具体依赖
  - 准备jdbc.properties
  - 创建 Spring 配置文件专门配置 Spring 和 MyBatis 整合相关
  - 在 Spring 的配置文件中加载 jdbc.properties 属性文件
  - 配置数据源
  - 测试从数据源中获取数据库连接
  - 配置SqlSessionFactoryBean
  - 装配数据源
  - 指定 XxxMapper.xml配置文件的位置
  - 指定 MyBatis 全局配置文件的位置(可选)
  - 配置MapperScannerConfigurer
  - 测试是否可以装配 XxxMapper接口并通过这个接口操作数据库

### 操作步骤

- 添加依赖: ZWC03-admin-component/pom.xml文件

```xml
    <dependencies>
        <dependency>
            <groupId>com.uestc</groupId>
            <artifactId>ZWC04-admin-entity</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-orm</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>cglib</groupId>
            <artifactId>cglib</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
        </dependency>
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </dependency>
        <dependency>
            <groupId>javax.servlet.jsp.jstl</groupId>
            <artifactId>jstl</artifactId>
        </dependency>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
        </dependency>
    </dependencies>
```

- 创建ZWC02-admin-webui/resources/jdbc.properties文件

```properties
jdbc.user=root
jdbc.password=z123456
jdbc.uel=jdbc:mysql://localhost:4306/project_zcw?useUnicode=true&characterEncoding=UTF-8
jdbc.driver=com.mysql.jdbc.Driver
```

- 创建mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <context:property-placeholder location="classpath:jdbc.properties" />
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>
</beans>
```

```java
// 测试 -- 需要引入junit4 和spring-test的依赖
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mybatis-config.xml"})
public class testMybatis {
    @Autowired
    private DataSource dataSource;
    @Test
    public void TestMybatis() throws IOException, SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}
```

- 配置/连接数据源，配置SqlSessionFactoryBean/MapperScannerConfigurer， 测试

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd">

    <context:property-placeholder location="classpath:jdbc.properties" />
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${jdbc.driver}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </bean>
    <!--配置sqlSessionFactoryBean整合MyBatis-->
    <bean id="sqlSessionFactoryBean" class="org.mybatis.spring.SqlSessionFactoryBean">
<!--指定MyBatis全局配置文件位置-->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
<!--指定Mapper.xml配置文件位置-->
        <property name="mapperLocations" value="classpath:com/uestc/crowd/mapper/*Mapper.xml" />
<!--装配数据源-->
        <property name="dataSource" ref="dataSource" />
    </bean>
    <!--配置MapperScannerConfigurer来扫描Mapper接口所在的包-->
    <bean id="mapperScannerConfigure" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.uestc.crowd.mapper" />
    </bean>
</beans>
```

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mybatis-presist-config.xml"})
public class testMybatis {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private AdminMapper adminMapper;
    @Test
    public void TestMybatis() throws IOException, SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        Admin admin = new Admin(null, "Tom", "123123", "汤姆", "tom@qq.com", null);
        adminMapper.insert(admin);
    }
}
```

## 6.日志系统

- 重要意义
  - 系统在运行过程中出了问题就需要通过日志来进行排查，所以我们在上手任何新技术的时候，都要习惯性的关注一下它如何打印日志。
- 技术选型

![日志系统_技术选型](.\ZCW_MarkDownPhoto\日志系统_技术选型.png)

- 加入slf4j+logback依赖

  ```xml
  <dependency>
              <groupId>org.slf4j</groupId>
              <artifactId>slf4j-api</artifactId>
          </dependency>
          <dependency>
              <groupId>ch.qos.logback</groupId>
              <artifactId>logback-classic</artifactId>
              <scope>test</scope>
          </dependency>
  ```

  - 说明：System.out.print()是IO流的输出非常占用资源，影响性能。通常使用日志系统代替。
  - 测试

  ```java
  @Test
  public void testLog() {
      // 1.获取Logger对象，这里传入的Class对象就是当前打印日志的类
      Logger logger = LoggerFactory.getLogger(testMybatis.class);
      // 2.根据不同日志级别打印日志
      logger.debug("hello I am Debug level");
      logger.info("info");
      logger.warn("warn");
      logger.error("error");
      logger.debug("hello I am Debug level");
      logger.info("info");
      logger.warn("warn");
      logger.error("error");
      logger.debug("hello I am Debug level");
      logger.info("info");
      logger.warn("warn");
      logger.error("error");
  }
  ```

- 排除common-logging(或手动在IDEA的structure project 中剔除)，再添加jcl-over-slf4j依赖
  - 抑制spring使用自己原生的common-logging

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-orm</artifactId>
    <exclusions>
        <exclusion>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>jcl-over-slf4j</artifactId>
</dependency>
```

- logback.xml配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>[%d{HH:mm:ss.SSS}] [%thread] [%-5level] [%logger{36}] - [%msg]%n</pattern>
        </encoder>
    </appender>
    <root level="INFO">
        <appender-ref ref="STDOUT" />
    </root>
    <logger name="com.uestc.crowd.mapper" level="DEBUG" />
</configuration>
```

## 7.声明式事务

- 目标：spring统一管理通用事务的操作

```java
try{
    // 开启事务(关闭事务的自动提交)
    
    // 核心操作：操作数据库
    
    // 提交事务
} catch() {
    // 回滚事务
} finally {
    // 释放数据库连接
}
```

- 思路：

![AOP事务的实现思路](.\ZCW_MarkDownPhoto\AOP事务的实现思路.png)

### 实现代码

- 事务管理配置文件

  - 在spring-presist-tx.xml中添加名称空间

  ```xml
  <!--添加名称空间-->
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns:context="http://www.springframework.org/schema/context"
         xmlns:tx="http://www.springframework.org/schema/tx"
         xmlns:aop="http://www.springframework.org/schema/aop"
         xsi:schemaLocation="http://www.springframework.org/schema/beans
         http://www.springframework.org/schema/beans/spring-beans.xsd
  http://www.springframework.org/schema/context
         http://www.springframework.org/schema/context/spring-context.xsd
  http://www.springframework.org/schema/tx
         http://www.springframework.org/schema/tx/spring-tx.xsd
  http://www.springframework.org/schema/aop
         http://www.springframework.org/schema/aop/spring-aop.xsd">
  ```

  - 创建service包(ZWC03-admin-component)，并开启扫描
  - 完整的配置文件:spring-presist-tx.xml
    - 注意切入点表达式和对应的方法处理的声明都必须有

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--添加名称空间-->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd
http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop.xsd">
    <import resource="mybatis-presist-config.xml"/>
<!--配置自动扫描的包:主要是为了把Service扫描到IOC容器中-->
    <context:component-scan base-package="com.uestc.crowd.service" />
<!--配置事务管理器-->
    <bean id="txManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
<!--装配数据源-->
        <property name="dataSource" ref="dataSource"/>
    </bean>
<!--配置事务切面-->
    <aop:config>
<!--任意访问修饰符/返回值类型 任意包名下任意深度的Service(可能与spring-security某些类冲突)/ServiceImpl结尾的类的所有方法 参数任意-->
        <aop:pointcut id="txPointCut" expression="execution(* *..*ServiceImpl.*(..))"/>
<!--将切入点表达式和事务通知关联起来-->
        <aop:advisor advice-ref="txAdvice" pointcut-ref="txPointCut" />
    </aop:config>
<!--配置事务通知-->
    <tx:advice id="txAdvice" transaction-manager="txManager">
<!--配置事务属性-->
        <tx:attributes>
<!--查询方法:配置只读属性，让数据库知道这是一个查询操作，能够进行一定优化-->
            <tx:method name="get*" read-only="true"/>
            <tx:method name="find*" read-only="true"/>
            <tx:method name="query*" read-only="true"/>
            <tx:method name="count*" read-only="true"/>
<!--增删改方法:配置事务传播行为、回滚异常-->
<!--propagation属性:
    REQUIRED:存在事务就继续使用，不存在则创建(会被原有事务回滚)
    REQUIRES_NEW:自己开新的事务
-->
<!--rollback-for属性:配置事务方法针对什么样的异常回滚
默认:运行时异常回滚
建议:编译时异常和运行时异常都回滚
-->
            <tx:method name="save*" propagation="REQUIRES_NEW" rollback-for="java.lang.Exception" />
            <tx:method name="update*" propagation="REQUIRES_NEW" rollback-for="java.lang.Exception" />
            <tx:method name="remove*" propagation="REQUIRES_NEW" rollback-for="java.lang.Exception" />
            <tx:method name="batch*" propagation="REQUIRES_NEW" rollback-for="java.lang.Exception" />
        </tx:attributes>
    </tx:advice>
</beans>
```

- 测试

  - 创建对应的Service/ServiceImpl

  ```java
  public class AdminServiceImpl implements AdminService {
      @Autowired
      private AdminMapper adminMapper;
      @Override
      public void saveAdmin(Admin admin) {
          adminMapper.insert(admin);
      }
  }
  ```

  - 测试

  ```java
  @RunWith(SpringJUnit4ClassRunner.class)
  @ContextConfiguration(locations = {"classpath:mybatis-presist-config.xml", "classpath:spring-presist-tx.xml"})
  // 添加新的配置文件 spring-presist-tx.xml
  public class testMybatis {
      @Autowired
      private AdminServiceImpl adminService;
      @Test
      public void testInsertAdmin() {
          adminService.saveAdmin(new Admin(null, "z111", "z123123", "ZYP", "zyp111@qq.com", null));
      }
  }
  ```

  

## 8.表述层配置

- 目标

  - 1：handler 中装配 Service
  - 2：页面能够访问到 handler

  页面→handler(@RequestMapping)→Service→Mapper→数据库

- web.xml和spring配置文件的关系

![SpringMVC的配置思路](.\ZCW_MarkDownPhoto\SpringMVC的配置思路.png)

### web.xml配置

- 配置ContextLoaderlistener

```xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:mybatis-presist-*.xml</param-value>
</context-param>
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

- 配置字符集Filter

```xml
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
<filter>
    <filter-name>characterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
    <init-param>
        <param-name>forceRequestEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
    <init-param>
        <param-name>forceResponseEncoding</param-name>
        <param-value>true</param-value>
    </init-param>
</filter>
<!--字符集-->
<!--这个Filter执行的顺序要在所有其他Filter前面-->
<!--request,setCharacterEncoding(encoding)必须在request.getParameter()前面
response.setCharacterEncoding(encoding)必须在response.getWriter()前面-->
<filter-mapping>
    <filter-name>characterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

- ServletDispatcher配置

```xml
<servlet>
    <servlet-name>springDispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring-web-mvc.xml</param-value>
    </init-param>
    <!--生命周期：默认在第一次请求时创建对象-->
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>springDispatcherServlet</servlet-name>
    <!--url-pattern配置方式一:/表示拦截所有请求-->
    <!--        <url-pattern>/</url-pattern>-->
    <!--url-pattern配置方式二:配置请求扩展名
                    优点1：静态资源(css,js,png等)不经过springMVC 处理
                    优点2：伪静态，表面上访问html静态资源，实际上也是由java代码处理的结果。
                    增加入侵难度
                    利于SEO优化
                    不符合RestFul风格
                    -->
    <url-pattern>*.html/</url-pattern>
    <!--区别html的处理返回json的请求-->
    <!--为了让Aiax请求能够顺利拿到JSON格式的响应数据，我们另外配置json扩展名-->
    <url-pattern>*.json/</url-pattern>
</servlet-mapping>
```

- springMVC配置文件：spring-web-mvc.xml(基本内容)

```xml
<!--配置自动扫描的包:扫描handler-->
    <context:component-scan base-package="com.uestc.crowd.mvc" />
<!--配置SpringMVC的注解驱动-->
    <mvc:annotation-driven />
<!--配置视图解析器-->
    <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver" >
        <property name="prefix" value="/WEB-INF/"/>
        <property name="suffix" value=".jsp" />
    </bean>
```

- web.xml配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <welcome-file-list>
        <welcome-file>index.html</welcome-file>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>

    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:mybatis-presist-*.xml</param-value>
    </context-param>
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <servlet>
        <servlet-name>springDispatcherServlet</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring-web-mvc.xml</param-value>
        </init-param>
<!--生命周期：默认在第一次请求时创建对象-->
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>springDispatcherServlet</servlet-name>
<!--url-pattern配置方式一:/表示拦截所有请求-->
<!--        <url-pattern>/</url-pattern>-->
<!--url-pattern配置方式二:配置请求扩展名
优点1：静态资源(css,js,png等)不经过springMVC 处理
优点2：伪静态，表面上访问html静态资源，实际上也是由java代码处理的结果。
    增加入侵难度
    利于SEO优化
    不符合RestFul风格
-->
        <url-pattern>*.html</url-pattern>
<!--区别html的处理返回json的请求-->
<!--为了让Ajax请求能够顺利拿到JSON格式的响应数据，我们另外配置json扩展名-->
        <url-pattern>*.json</url-pattern>
    </servlet-mapping>
    <filter>
        <filter-name>characterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceRequestEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
        <init-param>
            <param-name>forceResponseEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
<!--字符集-->
<!--这个Filter执行的顺序要在所有其他Filter前面-->
<!--request,setCharacterEncoding(encoding)必须在request.getParameter()前面
response.setCharacterEncoding(encoding)必须在response.getWriter()前面-->
    <filter-mapping>
        <filter-name>characterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
</web-app>
```

- 测试

  - 引入servlet和jsp的依赖

  ```xml
  <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>servlet-api</artifactId>
      <scope>provided</scope>
  </dependency>
  <dependency>
      <groupId>javax.servlet.jsp</groupId>
      <artifactId>jsp-api</artifactId>
      <scope>provided</scope>
  </dependency>
  ```

  

  - 发起请求

  ```jsp
  <%--
    Created by IntelliJ IDEA.
    User: 97359
    Date: 2024/7/3
    Time: 19:10
    To change this template use File | Settings | File Templates.
  --%>
  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <html>
  <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
  <%--以/结尾表示根目录下--%>
  <head>
      <title>Title</title>
  </head>
  <body>
  <a href="test/ssm.html">测试ssm整合</a>
  </body>
  </html>
  ```

  base标签的注意事项：

  1. 端口号前面的冒号不能省略
  2. contextPath(前面自带"/")前面不能写"/"
  3. contextPath后面必须写"/"
  4. 页面上所有参照 base 标签的标签都必须放在 base 标签的后面
  5. 页面上所有参照 base 标签的标签的路径都不能以“/”开头

  

  - 处理请求

  ```java
  @Controller
  public class testHandler {
      @Autowired
      private AdminService adminService;
  
      @RequestMapping("/test/ssm.html")
      public String testSsm(ModelMap modelMap) {
          List<Admin> admins = adminService.getAll();
          modelMap.addAttribute("adminList", admins);
          return "target";
      }
  }
  ```

### SpringMVC 环境下的 Ajax 请求

- 请求说明

![Ajax请求](.\ZCW_MarkDownPhoto\Ajax请求.png)

- 常用注解

  - @ResponseBody和@RequestBody要想正常工作必须有jackson 的支持
  - 同时必须配置了mvc:annotation-driven

  ```xml
  <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
  </dependency>
  <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
  </dependency>
  ```

<img src=".\ZCW_MarkDownPhoto\处理请求和响应的常用注解1.png" alt="处理请求和响应的常用注解1" style="zoom: 67%;" />

- 引入jquery

  - jquery.pagination.js
  - jquery-2.1.1.min.js
  - jquery-form.min.js

  ```jsp
  <script type="text/javascript" src="jquery/jquery.pagination.js" />
  ```

- 发送数组到服务器：方式1
  - 发送请求
  - 处理请求
  - 缺陷：@RequestParam("array[]")请求参数多一个[]

```jsp
<%--
  Created by IntelliJ IDEA.
  User: 97359
  Date: 2024/7/3
  Time: 19:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <title>Title</title>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
<%--    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>--%>
    <script type="text/javascript">
        $(function () {
            $("#btn1").click(function() {
                $.ajax({
                    "url": "send/array.html", // 请求目标地址
                    "type": "post", // 请求方式
                    "data": { // 请求参数
                        "array":[5, 8, 12]},
                    "dataType": "text", // 如何处理服务器端返回的数据
                    "success": function (response) { // 处理成功的回调函数，response是响应体
                        alert(response);
                    },
                    "error": function (response) { // 处理失败的回调函数
                        alert(response);
                    }
                });
                // alert("1");
            });
        });
    </script>
    <%--以/结尾表示根目录下--%>

</head>
<body>
<a href="test/ssm.html">测试ssm整合</a>
<button id="btn1">Send[5, 8, 12]</button>
</body>
</html>
```

```java
@ResponseBody
@RequestMapping("/send/array.html")
public String testReceiverArray1(@RequestParam("array[]") List<Integer> array) {
    for (Integer num: array) {
        System.out.println(num);
    }
    return "success";
}
```

- 发送数组到服务器：方式2

  - 发送请求
  - 处理请求
  - 示意额外的实体类进行接收

  ```java
  @ResponseBody
  @RequestMapping("/send/array/two.html")
  public String testReceiverArray2(ParamArray array) {
      List<Integer> list = array.getArray();
      for (Integer num: list) {
          System.out.println(num);
      }
      System.out.println(array);
      return "success";
  }
  ```

  ```jsp
  <script type="text/javascript">
      $(function () {
          $("#btn2").click(function() {
              $.ajax({
                  "url": "send/array/two.html", // 请求目标地址
                  "type": "post", // 请求方式
                  "data": { // 请求参数
                      "array[0]": 5,
                      "array[1]": 8,
                      "array[2]": 12,},
                  "dataType": "text", // 如何处理服务器端返回的数据
                  "success": function (response) { // 处理成功的回调函数，response是响应体
                      alert(response);
                  },
                  "error": function (response) { // 处理失败的回调函数
                      alert(response);
                  }
              });
              // alert("1");
          });
      });
  </script>
  ```

  ```
  public class ParamArray {
      private List<Integer> array;
  ... get/set/构造器
  }

- 发送数组到服务器：方式3

  - Json字符串作为请求体

  ```jsp
  <script type="text/javascript">
      $(function () {
          $("#btn3").click(function() {
              // 准备数组
              var array = [3, 6, 9];
              // 将json数组转化为json字符串
              var requestBody = JSON.stringify(array);
              $.ajax({
                  "url": "send/array/three.html", // 请求目标地址
                  "type": "post", // 请求方式
                  "data":  // 请求参数
                  requestBody,
                  "contentType": "application/json;charset=UTF-8",
                  "dataType": "text", // 如何处理服务器端返回的数据
                  "success": function (response) { // 处理成功的回调函数，response是响应体
                      alert(response);
                  },
                  "error": function (response) { // 处理失败的回调函数
                      alert(response);
                  }
              });
          });
      });
  </script>
  ```

  ```java
  @ResponseBody
  @RequestMapping("send/array/three.html")
  public String testReceiverArray3(@RequestBody List<Integer> array) {
      for (Integer num: array) {
          System.out.println(num);
      }
      System.out.println(array);
      return "success";
  }
  ```

- 发送复杂对象

  - Json字符串格式发送

  ```jsp
  <script type="text/javascript">
      $(function () {
          $("#btn4").click(function() {
              // 准备发送的数据
              var student = {
                  "stuId": 4,
                  "stuName": "ZYP",
                  "address": {
                      "province":"四川",
                      "city":"成都",
                      "street":"郫县"
                  },
                  "subjectList": [
                      {
                          "subjectName": "JavaSE",
                          "subjectScore": 100
                      }, {
                          "subjectName": "SSM",
                          "subjectScore": 99
                      }
                  ],
                  "map": {
                      "k1": "v1",
                      "k2": "v2"
                  }};
              // 将json数组转化为json字符串
              var requestBody = JSON.stringify(student);
              $.ajax({
                  "url": "send/compose/object.html", // 请求目标地址
                  "type": "post", // 请求方式
                  "data":  // 请求参数
                  requestBody,
                  "contentType": "application/json;charset=UTF-8",
                  "dataType": "text", // 如何处理服务器端返回的数据
                  "success": function (response) { // 处理成功的回调函数，response是响应体
                      alert(response);
                  },
                  "error": function (response) { // 处理失败的回调函数
                      alert(response);
                  }
              });
              // alert("1");
          });
      });
  </script>
  ```

  ```java
  @ResponseBody
  @RequestMapping("/send/compose/object.html")
  public String testReceiveComposeObject(@RequestBody  Student student) {
      System.out.println(student);
      return "success";
  }
  ```

  ```java
  // 对应的实体类/get/set/构造器/toString()
  ```

### 规范Ajax 请求返回结果

- 创建返回数据的工具类(ZWC05-common-util)

```java
package com.uestc.crowd;

public class ResultEntity<T> { // 数据类型 T
    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";
    private String result; // 处理结果
    private String message; // 请求处理失败时返回的错误消息
    private T data; // 返回的数据类型
    // 请求处理成功且不需要返回数据时使用的工具方法
    public static <Type> ResultEntity<Type> successWithoutData() {
        return new ResultEntity<Type>(SUCCESS, null, null);
    }
    public static <Type> ResultEntity<Type> successWithData(Type data) {
        return new ResultEntity<Type>(FAILED, null, data);
    }
    public static <Type> ResultEntity<Type> failed(String message) {
        return new ResultEntity<Type>(FAILED, message, null);
    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public ResultEntity() {
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
```

- 修改请求和响应方法-> json

```java
@ResponseBody
@RequestMapping("/send/compose/object.json")  // 修改请求地址
public ResultEntity<Student> testReceiveComposeObject(@RequestBody  Student student) {
    System.out.println(student);
    //        if (student != null) {
    return ResultEntity.successWithData(student);
    //
}
```

```jsp
$("#btn4").click(function() {
                // 准备发送的数据
                var student = {
                    "stuId": 4,
                    "stuName": "ZYP",
                    "address": {
                        "province":"四川",
                        "city":"成都",
                        "street":"郫县"
                    },
                    "subjectList": [
                        {
                            "subjectName": "JavaSE",
                            "subjectScore": 100
                        }, {
                            "subjectName": "SSM",
                            "subjectScore": 99
                        }
                    ],
                    "map": {
                        "k1": "v1",
                        "k2": "v2"
                }};
                // 将json数组转化为json字符串
                var requestBody = JSON.stringify(student);
                $.ajax({
                    "url": "send/compose/object.json", // 请求目标地址  
                    "type": "post", // 请求方式
                    "data":  // 请求参数
                    requestBody,
                    "contentType": "application/json;charset=UTF-8",
                    "dataType": "text", // 如何处理服务器端返回的数据
                    "success": function (response) { // 处理成功的回调函数，response是响应体
                        console.log(response);
                    },
                    "error": function (response) { // 处理失败的回调函数
                        console.log(response);
                    }
                });
                // alert("1");
            });
```

### 异常映射

- 思路

![异常映射逻辑](.\ZCW_MarkDownPhoto\异常映射逻辑.png)

![异常映射处理](.\ZCW_MarkDownPhoto\异常映射处理.png)



- 基于配置文件的异常处理(spring-web-mvc.xml)

```xml
<!--配置基于XML的异常映射-->
<bean class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver" id="simpleMappingExceptionResolver" >
    <!--配置异常类型和具体视图页面的对应关系-->
    <property name="exceptionMappings">
        <props>
            <!--key属性指定异常全类名-->
            <!--标签体中写对应的视图(前后缀拼接后为具体路径)-->
            <prop key="java.lang.Exception">system-error</prop>
        </props>
    </property>
</bean>
```

- 基于注解的异常处理

  - 判断请求类型的工具方法

    - 添加依赖pom.xml(ZWC05-common-util)

    ```xml
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>servlet-api</artifactId>
        <version>2.5</version>
        <scope>provided</scope>
    </dependency>
    ```

    - 异常处理类

    ```java
    package com.uestc.crowd.config;
    
    import com.google.gson.Gson;
    import com.uestc.crowd.util.CrowdConstant;
    import com.uestc.crowd.util.CrowdUtil;
    import com.uestc.crowd.util.ResultEntity;
    import org.springframework.web.bind.annotation.ControllerAdvice;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.servlet.ModelAndView;
    
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.io.IOException;
    
    // ControllerAdvice基于注解的异常处理器类
    @ControllerAdvice
    public class CrowdExceptionResolver {
        @ExceptionHandler(value = ArithmeticException.class)
        public ModelAndView resolveArithmeticException(NullPointerException exception, HttpServletRequest request,
                                                        HttpServletResponse response) throws IOException {
            String viewName = "system-error";
            return commonResolve(viewName, exception, request, response);
    
        }
    
    
        @ExceptionHandler(value = NullPointerException.class)
        public ModelAndView resolveNullPointerException(NullPointerException exception, HttpServletRequest request,
                                                        HttpServletResponse response) throws IOException {
            String viewName = "system-error";
            return commonResolve(viewName, exception, request, response);
        }
        private ModelAndView commonResolve(String viewName, Exception exception, HttpServletRequest request,
                                           HttpServletResponse response) throws IOException {
            // 请求对象
            boolean judgeResult = CrowdUtil.judgeRequestType(request);
            if (judgeResult) {
                ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());
                // Gson对象处理json
                Gson gson = new Gson();
                String json = gson.toJson(resultEntity);
                response.getWriter().write(json);
                // response 响应不用返回modelAndView了
                return null;
            }
            // 不是ajax请求 返回modelAndView
            ModelAndView modelAndView = new ModelAndView();
            // 存入对象
            modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
            // 设置视图
            modelAndView.setViewName(viewName);
            return modelAndView;
        }
    }
    ```

    

    - 创建工具方法(ZWC05-common-util)

    ```java
    public class CrowdUtil {
        // ture 是 ajax 请求
        public static boolean judgeRequestType(HttpServletRequest request) {
            // 1. 获取请求消息头
            String accept = request.getHeader("Accept");
            String XRequested = request.getHeader("X-Requested-With");
            // 2.判断
            return accept != null && accept.contains("application/json")
                    ||
                    (XRequested != null && XRequested.contains("XMLHttpRequest"));
        }
    }
    ```

- 创建工具类管理常量：CrowdConstant.java(ZWC05-common-util)

  ```java
  public class CrowdConstant {
      public static final String ATTR_NAME_EXCEPTION = "exception";
      public static final String MESSAGE_LOGIN_FAILED = "账号密码不匹配！";
      public static final String MESSAGE_LOGIN_ACCT_ALREADY_IN_USE = "抱歉账号被使用！";
      public static final String MESSAGE_ACCESS_FORBIDDEN = "请登录后访问！";
  }
  ```

## 9.前端配置

### 引入前端静态资源

- 前端资源目录

![前端资源目录](.\ZCW_MarkDownPhoto\前端资源目录.png)

- admin-login.jsp
- 配置对应的视图控制器(spring-web-mvc.xml)

```xml
<!--配置view-controller，直接把请求地址和视图名称关联起来，不必写handler方法-->
    <mvc:view-controller path="admin/to/login/page.html" view-name="admin-login" />
```

### layer 弹层组件

- 添加layer的库文件和样式文件

![Layer的库文件和样式文件](.\ZCW_MarkDownPhoto\Layer的库文件和样式文件.png)

- 页面上添加layer环境

```xml
    <script type="text/javascript" src="layer/layer.js"></script>
```



# 2.后台管理系统

## 登录页面

- 分析

  - 目标
  - 思路

  ![后台管理登录页面_思路](.\ZCW_MarkDownPhoto\后台管理登录页面_思路.png)

- 代码

- 1.MD5工具方法(ZWC05-common-util/CrowdUtil)

  ```java
  /**
       * 使用md5加密字符
       * @param source 待加密字符
       * @return 加密结果
       */
  public static String md5(String source) {
      // 1. 判断字符串是否有效
      if (source == null || source.isEmpty()) {
          // 2. 抛出异常
          throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALID_DATE);
      }
      // 3. 获取MessageDigest对象
      String algorithm = "md5";
      try {
          MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
          // 4. 获取明文字符串对应的字节数组
          byte[] sourceBytes = source.getBytes();
          // 5. 执行加密
          byte[] output = messageDigest.digest(sourceBytes);
          // 6. 创建BigInteger对象
          int signum = 1;
          BigInteger bigInteger = new BigInteger(signum, output);
          // 7. 按照16进制将bigInteger的值转换为字符串
          int radix = 16;
          return bigInteger.toString(radix);
      } catch (NoSuchAlgorithmException e) {
          throw new RuntimeException(e);
      }
  }
  ```

- 自定义异常(ZWC05-common-util/LoginFailedException)

  ```java
  public class LoginFailedException extends RuntimeException{
      private static final long serialVersionUID = 1L;
  
      public LoginFailedException() {
      }
  
      public LoginFailedException(String message) {
          super(message);
      }
  
      public LoginFailedException(String message, Throwable cause) {
          super(message, cause);
      }
  
      public LoginFailedException(Throwable cause) {
          super(cause);
      }
  
      public LoginFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
          super(message, cause, enableSuppression, writableStackTrace);
      }
  }
  ```

  - 在异常处理器类(CrowdExceptionResolver)中增加登录失败异常的处理方法

  ```java
  @ExceptionHandler(value = LoginFailedException.class)
  public ModelAndView resolveLoginFailedException(NullPointerException exception, HttpServletRequest request,
                                                  HttpServletResponse response) throws IOException {
      String viewName = "admin-login";
      return commonResolve(viewName, exception, request, response);
  }
  ```

  - 页面回显异常登录消息(admin-login.jsp)

  ```jsp
  <p>${requestScope.exception.message}</p>
  ```

  - Handler方法

  ```java
  @Controller
  public class AdminHandler {
      @Autowired
      private AdminService adminService;
      @RequestMapping("admin/do/login.html")
      public String doLogin(
              @RequestParam("loginAcct") String loginAcct,
              @RequestParam("userPswd") String userPswd,
              HttpSession session
      ) {
          // 调用Service方法执行登录检查
          // 这个方法如果能够返回admin对象说明登录成功，如果账号、密码不正确则会抛出异常
          Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);
          // 将登录成功返回的admin对象存入Session域
          session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
          return "admin-main";
  
      }
  }
  ```

  - service方法

  ```java
  @Override
      public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {
          // 1.根据登录账号查询Admin对象
          // 创建条件对象
          AdminExample adminExample = new AdminExample();
          // 获取criteria对象
          AdminExample.Criteria criteria = adminExample.createCriteria();
          // 设置查询条件
          criteria.andLoginAcctEqualTo(loginAcct);
          List<Admin> admins = adminMapper.selectByExample(adminExample);
          if (admins == null || admins.isEmpty()) { // 没有查找到
              throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
          }
          if (admins.size() > 1) {
              throw  new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
          }
          // 2.判断Admin对象是否为null
          // 3.如果Admin对象为null则抛出异常
          Admin admin = admins.get(0);
          if (admin == null) {
              throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
          }
          // 4.如果Admin对象不为null则将数据库密码从Admin对象中取出
          String userPswdDB = admin.getUserPswd();
          // 5.将表单提交的明文密码进行加密
          String userPswdForm = CrowdUtil.md5(userPswd);
          // 6.对密码进行比较
          // 7.如果比较结果是不一致则抛出异常
          if (!Objects.equals(userPswdDB, userPswdForm)) {
              throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
          }
          // 8.如果一致则返回Admin对象
          return admin;
      }
  ```

  - 修改前往后台的方式：重定向(避免重复提交表单)

  ```java
  return "redirect:/admin/to/main/page.html";
  ```

  - 并配置对应的视图控制器

  ```xml
  <mvc:view-controller path="redirect:/admin/to/main/page.html" view-name="admin-main"/>
  ```

## 退出登录

- 退出请求

```jsp
<li><a href="admin/do/logout.html"><i class="glyphicon glyphicon-off"></i> 退出系统</a></li>
```

- 处理退出请求

```java
@RequestMapping("admin/do/logout.html")
public String doLogout(HttpSession session) {
    // 强制session失效
    session.invalidate();
    return "redirect:/admin/to/login/page.html";
}
```

## 通用标签的抽取

- include-head.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<%--    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">--%>
<%--    <link rel="stylesheet" href="css/font-awesome.min.css">--%>
<%--    <link rel="stylesheet" href="css/main.css">--%>
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="script/docs.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript">
        $(function () {
            $(".list-group-item").click(function(){
                if ( $(this).find("ul") ) {
                    $(this).toggleClass("tree-closed");
                    if ( $(this).hasClass("tree-closed") ) {
                        $("ul", this).hide("fast");
                    } else {
                        $("ul", this).show("fast");
                    }
                }
            });
        });
    </script>
    <style>
        .tree li {
            list-style-type: none;
            cursor:pointer;
        }
        .tree-closed {
            height : 40px;
        }
        .tree-expanded {
            height : auto;
        }
    </style>
</head>
```

- include-nav.jsp

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 控制面板</a></div>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li style="padding-top:8px;">
                    <div class="btn-group">
                        <button type="button" class="btn btn-default btn-success dropdown-toggle"
                                data-toggle="dropdown">
                            <i class="glyphicon glyphicon-user"></i><security:authentication property="principal.originalAdmin.userName"/><span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="#"><i class="glyphicon glyphicon-cog"></i> 个人设置</a></li>
                            <li><a href="#"><i class="glyphicon glyphicon-comment"></i> 消息</a></li>
                            <li class="divider"></li>
                            <li><a href="security/do/logout.html"><i class="glyphicon glyphicon-off"></i> 退出系统</a></li>
                        </ul>
                    </div>
                </li>
                <li style="margin-left:10px;padding-top:8px;">
                    <button type="button" class="btn btn-default btn-danger">
                        <span class="glyphicon glyphicon-question-sign"></span> 帮助
                    </button>
                </li>
            </ul>
            <form class="navbar-form navbar-right">
                <input type="text" class="form-control" placeholder="查询">
            </form>
        </div>
    </div>
</nav>
```

- include-sidebar.jsp

```jsp
<div class="col-sm-3 col-md-2 sidebar">
    <div class="tree">
        <ul style="padding-left:0px;" class="list-group">
            <li class="list-group-item tree-closed">
                <a href="main.html"><i class="glyphicon glyphicon-dashboard"></i> 控制面板</a>
            </li>
            <li class="list-group-item tree-closed">
                        <span><i class="glyphicon glyphicon glyphicon-tasks"></i> 权限管理 <span class="badge"
                                                                                                 style="float:right">3</span></span>
                <ul style="margin-top:10px;display:none;">
                    <li style="height:30px;">
                        <a href="admin/get/page.html"><i class="glyphicon glyphicon-user"></i> 用户维护</a>
                    </li>
                    <li style="height:30px;">
                        <a href="role/to/page.html"><i class="glyphicon glyphicon-king"></i> 角色维护</a>
                    </li>
                    <li style="height:30px;">
                        <a href="menu/to/page.html"><i class="glyphicon glyphicon-lock"></i> 菜单维护</a>
                    </li>
                </ul>
            </li>
            <li class="list-group-item tree-closed">
                        <span><i class="glyphicon glyphicon-ok"></i> 业务审核 <span class="badge"
                                                                                    style="float:right">3</span></span>
                <ul style="margin-top:10px;display:none;">
                    <li style="height:30px;">
                        <a href="auth_cert.html"><i class="glyphicon glyphicon-check"></i> 实名认证审核</a>
                    </li>
                    <li style="height:30px;">
                        <a href="auth_adv.html"><i class="glyphicon glyphicon-check"></i> 广告审核</a>
                    </li>
                    <li style="height:30px;">
                        <a href="auth_project.html"><i class="glyphicon glyphicon-check"></i> 项目审核</a>
                    </li>
                </ul>
            </li>
            <li class="list-group-item tree-closed">
                <span><i class="glyphicon glyphicon-th-large"></i> 业务管理 <span class="badge" style="float:right">7</span></span>
                <ul style="margin-top:10px;display:none;">
                    <li style="height:30px;">
                        <a href="cert.html"><i class="glyphicon glyphicon-picture"></i> 资质维护</a>
                    </li>
                    <li style="height:30px;">
                        <a href="type.html"><i class="glyphicon glyphicon-equalizer"></i> 分类管理</a>
                    </li>
                    <li style="height:30px;">
                        <a href="process.html"><i class="glyphicon glyphicon-random"></i> 流程管理</a>
                    </li>
                    <li style="height:30px;">
                        <a href="advertisement.html"><i class="glyphicon glyphicon-hdd"></i> 广告管理</a>
                    </li>
                    <li style="height:30px;">
                        <a href="message.html"><i class="glyphicon glyphicon-comment"></i> 消息模板</a>
                    </li>
                    <li style="height:30px;">
                        <a href="project_type.html"><i class="glyphicon glyphicon-list"></i> 项目分类</a>
                    </li>
                    <li style="height:30px;">
                        <a href="tag.html"><i class="glyphicon glyphicon-tags"></i> 项目标签</a>
                    </li>
                </ul>
            </li>
            <li class="list-group-item tree-closed">
                <a href="param.html"><i class="glyphicon glyphicon-list-alt"></i> 参数管理</a>
            </li>
        </ul>
    </div>
</div>
```

- 通用模板创建

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="UTF-8">
<%@include file="/WEB-INF/include-head.jsp" %>

<body>

<%@include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/include-sidebar.jsp" %>
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            
            </div>
        </div>
    </div>
</div>

</body>
</html>
```

## 登录状态检查

- 将部分资源保护起来，让没有登求的请求不能访问。

- 思路

![登录验证](.\ZCW_MarkDownPhoto\登录验证.png)

- 代码

  - 创建拦截器类

  ```java
  public class LoginInterceptor extends HandlerInterceptorAdapter {
      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
          // 1. 获取session
          HttpSession session = request.getSession();
          // 2. 尝试获取admin对象
          Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);
          // 3. 判断admin对象是否为null
          if (admin == null) {
              // 4.抛出异常
              throw new AccessForbiddenException(CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
          }
          // 5.如果Admin对象不为null，则返回true放行
          return true;
      }
  }
  ```

  

  - 创建对应的异常类：AccessForbiddenException(用户没有登录就访问受保护资源时抛出的异常)

  ```java
  public class AccessForbiddenException extends RuntimeException{
  	...相应的构造器方法
  }
  ```

  - 添加AccessForbiddenException异常的异常处理

  ```xml
  <prop key="com.uestc.crowd.exception.AccessForbiddenException">system-error</prop>
  ```

  - 注册拦截器类(spring-web-mvc.xml)

  ```xml
  <!--注册拦截器-->
  <mvc:interceptors>
      <mvc:interceptor>
          <!--需要拦截的路径
              /* 单层路径
              /** 多层路径
              -->
          <mvc:mapping path="/**" />
          <!--不拦截的页面-->
          <mvc:exclude-mapping path="admin/do/login.html"/>
          <mvc:exclude-mapping path="/admin/to/login/page.html"/>
          <mvc:exclude-mapping path="admin/do/logout.html"/>
          <!--配置器拦截类-->
          <bean class="com.uestc.crowd.mvc.interceptor.LoginInterceptor" />
      </mvc:interceptor>
  </mvc:interceptors>
  ```

  

# 3.管理员维护(crud)

- 功能清单
  - 分页显示 Admin 数据
    - 不带关键词分页
    - 带关键词分页
  - 新增 Admin
  - 更新 Admin
  - 单条删除 Admin

## 分页显示

- 目标：将数据库中的 Admin 数据在页面上以分页形式显示。在后端将“带关键词”和“不带关键词”的分页合并为同一套代码。
- 思路：

![分页流程](.\ZCW_MarkDownPhoto\分页流程.png)

- 代码

  - 引入PageHelper依赖

  ```xml
  <dependency>
      <groupId>com.github.pagehelper</groupId>
      <artifactId>pagehelper</artifactId>
  </dependency>
  ```

  - SqlSessionFactoryBean配置Mybatis插件(mybatis-presist-config.xml)

  ```xml
  <!--配置插件-->
          <property name="plugins">
              <array>
  <!--配置pagehelper插件-->
                  <bean class="com.github.pagehelper.PageHelper">
                      <property name="properties">
                          <props>
                              <!--配置数据库方言，告诉PageHelper当前使用的数据库-->
                              <prop key="dialect">mysql</prop>
                              <!--配置页码的合理化修正，在1~总页数之间修正页码-->
                              <prop key="reasonable">true</prop>
                          </props>
                      </property>
                  </bean>
              </array>
          </property>
  ```

  - AdminMapper.xml中编写sql语句

  ```xml
  <select id="selectAdminByKeyword" resultMap="BaseResultMap">
      select (id, login_acct, user_pswd, user_name, email, create_time) from t_admin
      where login_acct like concat("%", #{keyword},"%") or
        user_name like concat("%", #{keyword},"%") or
        email like concat("%", #{keyword},"%")
    </select>
  ```

  - AdminMapper接口中声明方法

  ```java
  List<Admin> selectAdminByKeyword(String keyword);
  ```

  - AdminService接口和实现类

  ```java
  @Override
      public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
          // 1.调用PageHelper的静态方法开启分页功能
          // PageHelper非侵入式设计
          PageHelper.startPage(pageNum, pageSize);
          // 2.执行查询
          List<Admin> list = adminMapper.selectAdminByKeyword(keyword);
          // 3.封装到PageInfo对象中
          return new PageInfo<>(list);
      }
  ```

  - AdminHandler处理请求

  ```java
  @RequestMapping("/admin/get/page.html")
      public String getPageInfo(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                ModelMap modelMap
                                ) {
          // 调用Service方法获取PageInfo对象
          PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
          modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
          return "admin-page";
      }
  ```

  - 在页面上使用 Pagination 实现页码导航条

    - css/pagination.css
    - jquery/jquery.pagination.js

    ```jsp
    <link rel="stylesheet" href="css/pagination.css"/>
    <script type="text/javascript" src="jquery/jquery.pagination.js"></script>
    ```

    - 页面

    ```jsp
    <%--分页页脚--%>
    <tfoot>
        <tr>
            <td colspan="6" align="center">
                <div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>
            </td>
        </tr>
    </tfoot>
    ```

    ```jsp
    <!--JQuery代码-->
    <script type="text/javascript">
        $(function () {
            //调用后面声明的函数，对页码导航条进行初始化操作
            initPagination();
        });
    
        //生成页码导航条
        function initPagination() {
            // 获取总记录数
            var totalRecoed = ${requestScope.pageInfo.total};
            // 声明一个JSON对象存储Pagination要设置的属性
            var properties = {
                num_edge_entries: 2, // 边缘页数
                num_display_entries: 5, // 主体页数
                callback: pageSelectCallBack,
                items_per_page:${requestScope.pageInfo.pageSize}, // 每页显示1项
                current_page: ${requestScope.pageInfo.pageNum - 1}, // Pagination的当前页数pageIndex从0开始
                    prev_text: "上一页",
                        next_text: "下一页"
        }
        // 生成页码导航条
        $("#Pagination").pagination(totalRecoed, properties);
        }
    
        // 用户点击“123”这样的页码时调用这个函数实现页面跳转
        function pageSelectCallBack(pageIndex,jQuery) {
            //根据pageIndex计算得到pageNum
            var pageNum = pageIndex + 1;
            //跳转页面
            window.location.href = "admin/get/page.html?pageNum="+pageNum+"&keyword=${param.keyword}";
            //由于每一个页码按钮都是超链接，所以我们要在这里取消超链接的默认行为
            return false; // 源代码后面还会再调用回调函数，防止循环
        }
    </script>
    ```

  - 条件查询

  ```jsp
  <form action="admin/get/page.html" method="post" class="form-inline" role="form" style="float:left;">
      <div class="form-group has-feedback">
          <div class="input-group">
              <div class="input-group-addon">查询条件</div>
              <input name="keyword" class="form-control has-success" type="text" placeholder="请输入查询条件">
          </div>
      </div>
      <button type="submit" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询
      </button>
  </form>
  ```

  - 翻页保持查询条件关键字

  ![admin_保持查询条件](.\ZCW_MarkDownPhoto\admin_保持查询条件.png)

```jsp
function pageSelectCallBack(pageIndex,jQuery) {
//根据pageIndex计算得到pageNum
var pageNum = pageIndex + 1;
//跳转页面
window.location.href = "admin/get/page.html?pageNum="+pageNum+"&keyword=${param.keyword}";
//由于每一个页码按钮都是超链接，所以我们要在这里取消超链接的默认行为
return false; // 源代码后面还会再调用回调函数，防止循环
}
```

## 单条删除

- 目标：在页面上点击单条删除按钮，实现 Admin 对应记录的删除
- 思路：

![admin_单条删除](.\ZCW_MarkDownPhoto\admin_单条删除.png)

- 删除按钮发起请求

```jsp
<a href="admin/remove/${admin.id }/${requestScope.pageInfo.pageNum}/${param.keyword }.html" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></a>
```

- AdminHandler处理请求
  - AdminService方法调用
  - AdminService实现类调调用Mapper方法

```java
@RequestMapping("/admin/remove/{id}/{pageNum}/{keyword}.html")
public String removeAdmin(@PathVariable("id") Integer id,
                          @PathVariable("pageNum") Integer pageNum,
                          @PathVariable("keyword") String keyword
                         ) {
    // 删除
    // 考虑更多细节--删除的权限等级--逻辑删除or物理删除
    adminService.removeById(id);
    // 页面跳转
    return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
}
```

## 新增

- 目标
  - 将表单提交的 Admin 对象保存到数据库中。
  - 要求 1:loginAcct 不能重复
  - 要求 2:密码加密

- 思路
  - 新增按钮 -> view-controller 视图转发 -> add.html -> 提交表单 -> AdminHandler处理请求 -> AdminService实现功能

- 给账号添加唯一约束

```sql
ALTER TABLE `project_zcw`.`t_admin` ADD UNIQUE INDEX (`login_acct`); 
```

- 添加按钮发起请求

```jsp
<a style="float: right;" href="admin/to/add/page.html" class="btn btn-primary"><i class="glyphicon glyphicon-plus"></i> 新增
```

- 配置view-controller(spring-web-mvc.xml)

```xml
<mvc:view-controller path="/admin/to/add/page.html" view-name="admin-add" />
```

- 准备add表单页面(admin-add.jsp)

```jsp
admin-add.jsp
```

- Handler方法

```java
@RequestMapping("/admin/save.html")
public String save(Admin admin) {
    adminService.saveAdmin(admin);
    return "redirect:/admin/get/page.html?page=" + Integer.MAX_VALUE;
}
```

- Service方法

```java
@Override
//    @Transactional
public void saveAdmin(Admin admin) {
    // 1. 密码加密
    String userPswd = admin.getUserPswd();
    userPswd = CrowdUtil.md5(userPswd);
    admin.setUserPswd(userPswd);
    // 2.生成创建时间
    Date date = new Date();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String createTime = format.format(date);
    admin.setCreateTime(createTime);
    // 3. 保存
    adminMapper.insert(admin);
    //        throw new RuntimeException();
}
```

- adminMapper
- 新增重名异常(LoginAcctAlreadyInUseException)
- 处理异常(CrowdExceptionResolver)

```java
@ExceptionHandler(value = LoginFailedException.class)
public ModelAndView resolveLoginAcctAlreadyInUseException(LoginAcctAlreadyInUseException exception, HttpServletRequest request,
                                                          HttpServletResponse response) throws IOException {
    String viewName = "admin-add";
    return commonResolve(viewName, exception, request, response);
}
```

## 更新

- 目标：修改现有 Admin 的数据。不修改密码，不修改创建时间。

- 思路：

  ![admin_修改](.\ZCW_MarkDownPhoto\admin_修改.png)

- 代码

  - 发起修改请求(admin-page.jsp)

  ```jsp
  <a href="admin/to/edit/page.html?adminId=${admin.id }&pageNum=${requestScope.pageInfo.pageNum }&keyword=${param.keyword }" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></a>
  ```

  - 处理转发请求/handler/Service/Mapper

  ```java
  @RequestMapping("admin/to/edit/page.html")
  public String toEditPage(@RequestParam("adminId") Integer adminId,
                           ModelMap modelMap
                          ) {
      Admin admin = adminService.getAdminById(adminId);
      modelMap.addAttribute(CrowdConstant.ATTR_NAME_CUR_ADMIN, admin);
      return "admin-edit";
  }
  ```

  - 添加admin-edit.jsp页面
  - 处理更新请求/handler/Service/Mapper

  ```java
  @RequestMapping("admin/update.html")
  public String update(@RequestParam("pageNum") Integer pageNum,
                       @RequestParam("keyword") String keyword,
                       Admin admin
                      ) {
      adminService.update(admin);
      return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
  }
  ```

  - 对应的异常处理方法/异常类

  ```java
  @ExceptionHandler(value = LoginAcctAlreadyInUseUpdateException.class)
  public ModelAndView resolveLoginAcctAlreadyInUseUpdateException(LoginAcctAlreadyInUseException exception, HttpServletRequest request,
                                                                  HttpServletResponse response) throws IOException {
      String viewName = "system-error";
      return commonResolve(viewName, exception, request, response);
  }
  ```

# 4.RBAC权限控制模型

## 简介

- 为什么要进行权限控制
  - 如果没有权限控制，系统的功能完全不设防，全部暴露在所有用户面前。用户春录以后可以使用系统中的所有功能。这是实际运行中不能接受的。
  - 所以权限控制系统的目标就是管理用户行为，保护系统功能。
- 什么是权限控制
  - “权限”=“权力”+“限制”
- 如何实现权限控制
  - 资源就是系统中需要保护起来的功能。具体形式很多:URL 地址、handler 方法、service 方法、页面元素等等都可以定义为资源使用权限控制系统保护起来。

- 创建权限
  - 一个功能复杂的项目会包含很多具体资源，成千上万都有可能。这么多资源逐个进行操作太麻烦了。为了简化操作，可以将相关的几个资源封装到一起，打包成一个“权限”同时分配给有需要的人。
- 管理用户
  - 系统中的用户其实是人操作系统时用来登录系统的账号、密码。
- 建立关联关系
  - 权限→资源:单向多对多
    - Java 类之间单向:从权限实体类可以获取到资源对象的集合，但是通过资源获取不到权限
    - 数据库表之间多对多:
      - 一个权限可以包含多个资源
      - 一个资源可以被分配给多个不同权限
  - 角色一权限:单向多对多
    - Java 类之间单向:从角色实体类可以获取到权限对象的集合，但是通过权限获取不到角色
    - 数据库表之间多对多:
      - 一个角色可以包含多个权限
      - 一个权限可以被分配给多个不同角色
  - 用户一角色:双向多对多
    - Java 类之间双向:可以通过用户获取它具备的角色，也可以看一个角色下包含哪些用户
    - 数据库表之间:
      - 一个角色可以包含多个用户
      - 一个用户可以身兼数职

## 多对多关联关系在数据库中的表示

- 没有中间表的情况

  - 如果只能在一个外键列上存储关联关系数据，那么现在这个情况无法使用SQL语句进行关联查询。

  ![没有中间表的情况](.\ZCW_MarkDownPhoto\没有中间表的情况.png)

- 有中间表

![有中间表](.\ZCW_MarkDownPhoto\有中间表.png)

```sql
select t_studet.id,t_student.name from t_student left join t_inner on t_studen.id=t_inner.stuent id left join t_subject on t_inner.subject_id=t_subject.id where t_subjct.id=1
```

- 中间表的主键生成方式

  - 方式一:另外设置字段作为主键

  ![联合查询_单独主键](.\ZCW_MarkDownPhoto\联合查询_单独主键.png)

  - 方式二:使用联合主键

  ![联合查询_联合主键](.\ZCW_MarkDownPhoto\联合查询_联合主键.png)



## RBAC权限控制模型

- 概念
  - 鉴于权限控制的核心是用户通过角色与权限进行关联，所以前面描述的权限控制系统可以提炼为一个模型:RBAC(Role-Based Access Control，其于角色的访问控制)
  - 在 RBAC模型中，一个用户可以对应多个角色，一个角色拥有多个权限，权限具体定义用户可以做哪些事情。



# 5.角色维护

## 前后端流程

- 思路

![角色模块思路](.\ZCW_MarkDownPhoto\角色模块思路.png)

- 代码

- 后端：(逆向实现过程)

  - 1.持久化层

  - 创建数据库表

  ```sql
  CREATE TABLE `project_zcw`.`t_role` ( `id` INT NOT NULL, `name` CHAR(100), PRIMARY KEY (`id`) );
  ```

  - 修改generatorConfig.xml配置文件的实体类名和表名

  ```xml
  <table tableName="t_role" domainObjectName="Role" />
  ```

  - 将逆向工程生成的Entity和Mapping文件转移置对应位置
  - 创建RoleService和实现类/RoleHandler类
  - RoleMapper.xml

  ```xml
  <select id="selectRoleByKeyword" resultMap="BaseResultMap">
      select id, name from t_role
      where name like concat("%",#{keyword},"%")
  </select>
  ```

  - RoleMapper.java

  ```java
  List<Role> selectRoleByKeyword(String keyword);
  ```

  - 2.Service层
  - 抽象方法/实现

  ```java
  PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword);
  ```

  ```java
  @Override
  public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {
      // 1.开启分页功能
      PageHelper.startPage(pageNum, pageSize);
      // 2.执行查询
      List<Role> roles = roleMapper.selectRoleByKeyword(keyword);
      // 3.封装为pageInfo 对象返回
      return new PageInfo<>(roles);
  }
  ```

  - 处理方法Handler/RoleHandler.java

  ```java
  @ResponseBody    
  @RequestMapping("/role/get/page/info.json")
  public ResultEntity<PageInfo<Role>> getPageInfo(
      @RequestParam(value = "keyword", defaultValue = "") String keyword,
      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize
  ){
      // 调用service方法获取分页数据
      PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);
      return ResultEntity.successWithData(pageInfo);
  }
  ```

  - 3.视图层
  - 添加前端页面role-page.jsp
  - 配置视图控制器

  ```xml
  <mvc:view-controller path="/role/to/page.html" view-name="role-page" />
  ```

  - 设置前端页面的超链接地址include-sidebar.jsp

  ```jsp
  <a href="role/to/page.html"><i class="glyphicon glyphicon-king"></i> 角色维护</a>
  ```

- 前端代码

  - 初始化数据

  ```js
  $(function () {
          // 1,为分页操作准备初始化操作
          window.pageNum = 1;
          window.pageSize = 10;
          window.keyword = "";
  });
  ```

  - 创建外部js文件 ：crowd/my-role.js
  - role-page.jsp引入外部js文件

  ```jsp
  <script type="text/javascript" src="crowd/my-role.js"></script>
  ```

  - 查看js代码

## 角色查询

- 目标：把页面上的“查询”表单和已经封装好的执行分页的函数连起来即可。
- 思路：

![角色维护_查询](.\ZCW_MarkDownPhoto\角色维护_查询.png)

### 注意

- 给js设置编码配置web.xml

```xml
<jsp-config>
    <jsp-property-group>
        <display-name>JsConfiguration</display-name>
        <url-pattern>*.js</url-pattern>
        <page-encoding>UTF-8</page-encoding>
    </jsp-property-group>
</jsp-config>
```

- 前端页面看js文件
  - my-role.js
  - role-page.jsp

- 角色信息新增：使用Bootstrap模态框

  - 模态框添加到role-page.jsp页面最后

  ```xml
  <%@include file="/WEB-INF/model-role-add.jsp" %>
  ```

## 角色保存

- 目标：通过在打开的模态框中输入角色名称，执行对新角色的保存。

- 思路：

  ![角色管理_添加](.\ZCW_MarkDownPhoto\角色管理_添加.png)

- 代码
- 页面引入模态框(默认隐藏，放最后)

```jsp
<%@include file="/WEB-INF/model-role-add.jsp" %>
```

- 打开模态框

  - 新增按钮

  ```jsp
  <button id="showAddModelBtn" type="button" class="btn btn-primary" style="float:right;">
      <i class="glyphicon glyphicon-plus"></i> 新增
  </button>
  ```

  ```jsp
  // 4.点击新增按钮打开模态框
  $("#showAddModelBtn").click(function () {
  $("#addModal").modal("show");
  });
  ```

  - 保存按钮

  ```js
  // 5.给新增模态框中的保存按钮绑定单击响应函数
  $("#saveRoleBtn").click(function () {
      // 1.获取用户在文本框中输入的角色名称
      var roleName = $.trim($("#addModal [name=roleName]").val());
      // 2.发送ajax请求
      $.ajax({
          "url": "role/save.json",
          "type": "post",
          "data": {
              "name": roleName
          },
          "dataType": "json",
          "success": function (response) {
              var result = response.result;
              if (result == "SUCCESS") {
                  layer.msg("操作成功!");
                  // 定位最后一页
                  window.pageNum = 9999;
                  generatePage();
              }
              if (result == "FAILED") {
                  layer.msg("操作失败！" + response.message)
              }
              // 重新加载分页
              generatePage();
          },
          "error": function (response) {
              layer.msg(response.status + " " + response.statusText);
          }
      });
      // 关闭模态框
      $("#addModal").modal("hide");
      // 清理模态框
      $("#addModal [name=roleName]").val("");
  
  });
  ```

  - 处理请Handler方法/Service方法

## 角色更新操作

- 目标：修改角色信息
- 思路：

![角色管理_更新](.\ZCW_MarkDownPhoto\角色管理_更新.png)

- 代码

  - 创建jsp文件model-role-edit.jsp
  - 引入模态框

  ```jsp
  <%@include file="/WEB-INF/model-role-edit.jsp" %>
  ```

  - 打开模态框(回显)

    - 修改“铅笔”按钮my-role.js

    ```js
     // 通过button标签的id属性把roleId值传递到button按钮的单击响应函数中
    var pencilBtn = "<button id='" + roleId + "' type='button' class='btn btn-primary btn-xs pencilBtn'><i class='glyphicon glyphicon-pencil'></i></button>";
    ```

    - “铅笔”按钮绑定单击响应函数->通过静态资源(外层的< tbody >)访问动态资源(“铅笔”按钮)

    ```js
    /*        // 6.给页面上的铅笔按钮绑定单击响应函数，目的是打开模态框
    $(".pencilBtn").click(function () {
    
    });*/
    // 使用jq对象的on（）解决上面问题
    // 1.首先找到所有“动态生成“的元素所附着的”静态“元素
    // 2.on函数的第一个参数是事件类型，第二个参数是真正要绑定事件的选择器
    // 3.第三个参数是响应函数
    $("#rolePageBody").on("click", ".pencilBtn", function () {
        // 打开模态框
        $("#editModal").modal("show");
        // 获取表格中当前行中的角色名称
        var roleName = $(this).parent().prev().text();
        // 获取当前角色的 id
        // 依据是：var pencilBtn = "<button id='"+roleId+"' ……这段代码中我们把 roleId 设置到id 属性了
        // 为了让执行更新的按钮能够获取到 roleId 的值，把它放在全局变量上
        window.roleId = this.id;
        // 使用 roleName 的值设置模态框中的文本框
        $("#editModal [name=roleName]").val(roleName);
    });
    // 7.给更新模态框中的更新按钮绑定单击响应函数
    $("#updateRoleBtn").click(function () {
        // ①从文本框中获取新的角色名称
        var roleName = $("#editModal [name=roleName]").val();
        // ②发送 Ajax 请求执行更新
        $.ajax({
            "url": "role/update.json",
            "type": "post",
            "data": {
                "id": window.roleId,
                "name": roleName
            },
            "dataType": "json",
            "success": function (response) {
                var result = response.result;
                if (result == "SUCCESS") {
                    layer.msg("操作成功！");
                    // 重新加载分页数据
                    generatePage();
                }
                if (result == "FAILED") {
                    layer.msg("操作失败！" + response.message);
                }
            },
            "error": function (response) {
                layer.msg(response.status + " " + response.statusText);
            }
        });
        // ③关闭模态框
        $("#editModal").modal("hide");
    });
    ```

## 角色删除

- 目标

  - 前端的“单条删除”和“批量删除”在后端合并为同一套操作。合并的依据是:单
    条删除时 id 也放在数组中，后端完全根据 id的数组进行删除。

- 思路

  ![角色管理_删除](.\ZCW_MarkDownPhoto\角色管理_删除.png)

- 代码

- 后端

  - Service方法

  ```java
  @Override
  public void removeRole(List<Integer> roleIdList) {
      RoleExample roleExample = new RoleExample();
      RoleExample.Criteria criteria = roleExample.createCriteria();
      criteria.andIdIn(roleIdList);
      roleMapper.deleteByExample(roleExample);
  }
  ```

  - Handler方法

  ```java
  @ResponseBody
  @RequestMapping("/role/remove/by/role/id/array.json")
  public ResultEntity<String> removeByRoleIdArray(@RequestBody List<Integer> roleIdList){
      System.out.println("-------------");
      System.out.println(roleIdList.toString());
      roleService.removeRole(roleIdList);
      return ResultEntity.successWithoutData();
  }
  ```

- 前端

  - 模态框显示函数

  ```js
  // 声明专门的函数用来分配Auth的模态框中显示Auth的树形结构数据
  function fillAuthTree() {
      // 1.发送 Ajax 请求查询 Auth 数据
      var ajaxReturn = $.ajax({
          "url":"assgin/get/all/auth.json",
          "type":"post",
          "dataType":"json",
          "async":false
      });
      if(ajaxReturn.status != 200) {
          layer.msg("请求处理出错响应状态码 是 ： "+ajaxReturn.status+" 说 明 是 ："+ajaxReturn.statusText);
          return ;
      }
      // 2.从响应结果中获取 Auth 的 JSON 数据
      // 从服务器端查询到的 list 不需要组装成树形结构，这里我们交给 zTree 去组装
      var authList = ajaxReturn.responseJSON.data;
      // 3.准备对 zTree 进行设置的 JSON 对象
      var setting = {
          "data": {
              "simpleData": {
                  // 开启简单 JSON 功能
                  "enable": true,
                  // 使用 categoryId 属性关联父节点，不用默认的 pId 了
                  "pIdKey": "categoryId"
              },
              "key": {
                  // 使用 title 属性显示节点名称，不用默认的 name 作为属性名了
                  "name": "title"
              }
          },
          "check": {
              "enable": true
          }
      };
  ```

  - 确认删除

  ```js
  // 8.点击确认模态框中的确认删除按钮执行删除
  $("#removeRoleBtn").click(function () {
      var requestBody = JSON.stringify(window.roleIdArray);
      $.ajax({
          "url": "role/remove/by/role/id/array.json",
          "type": "post",
          "data": requestBody,
          "contentType": "application/json;charset=UTF-8",
          "dataType": "json",
          "success": function (response) {
              var result = response.result;
              if (result == "SUCCESS") {
                  layer.msg("操作成功！");
                  generatePage(); // 重新加载分页数据
              }
              if (result == "FAILED") {
                  layer.msg("操作失败！" + response.message);
              }
          },
          "error": function (response) {
              layer.msg(response.status + " " + response.statusText);
          }
      });
      // ③关闭模态框
      $("#confirmModal").modal("hide");
  });
  ```

  - 单条删除确认

  ```java
  // 9.给单条删除按钮绑定单击响应函数
  $("#rolePageBody").on("click", ".removeBtn", function () {
      //从当前按钮出发获取角色名称
      var roleName = $(this).parent().prev().text();
  
      // 创建role对象
      var roleArray = [{
          roleId: this.id,
          roleName: roleName,
      }];
      // 调用函数打开模态框
      showConfirmModel(roleArray);
  });
  ```

  - 多条删除

  ```js
  // 10.给总的checkBox绑定单击响应
  $("#summaryBox").click(function () {
      // 1.获取当前多选框自身的状态
      var currentStatus = this.checked;
      // 2.用当前多选框的状态去设置其他多选框
      $('.itemBox').prop("checked", currentStatus);
  });
  ```

  - 全选/取消全选

  ```java
  // 11.全选、全不选的反向操作
  $("#rolePageBody").on("click", ".itemBox", function () {
      // 获取当前已经选中的.itemBox的数量
      var checkedBoxCount = $(".itemBox:checked").length;
      // 获取全部.itemBox的数量
      var totalBoxCount = $(".itemBox").length;
      // 使用二者的比较结果设置总的checkBox
      $("#summaryBox").prop("checked", checkedBoxCount == totalBoxCount);
  });
  ```

# 6.菜单维护

- 树形结构
  - 约定:整个树形结构节点的层次最多只能有3级。

![菜单管理_树状结构](.\ZCW_MarkDownPhoto\菜单管理_树状结构.png)

- 创建菜单表

```sql
CREATE TABLE t_menu(
id INT(11) NOT NULL AUTO_INCREMENT,
pid INT(11),
`name` VARCHAR(200),
`url` VARCHAR(200),
`icon` VARCHAR(200),
PRIMARY KEY (id)
);
```

- 插入数据

```sql
INSERT INTO t_menu (id, pid, `name`, url, icon) VALUES
(1, NULL, '系统权限菜单', NULL, 'glyphicon glyphicon-th-list'),
(2, 1, '控制面板', 'main.htm', 'glyphicon glyphicon-dashboard'),
(3, 1, '权限管理', NULL, 'glyphicon glyphicon-tasks'),
(4, 3, '用户维护', 'user/index.htm', 'glyphicon glyphicon-user'),
(5, 3, '角色维护', 'role/index.htm', 'glyphicon glyphicon-king'),
(6, 3, '权限维护', 'permision/index.htm', 'glyphicon glyphicon-lock'),
(7, 1, '业务审核', NULL, 'glyphicon glyphicon-th-large'),
(8, 7, '实名认证审核', 'auth_cert/index.htm', 'glyphicon glyphicon-check'),
(9, 7, '广告认证审核', 'auth_adv/index.htm', 'glyphicon glyphicon-check'),
(10, 7, '项目审核', 'auth_project/index.htm', 'glyphicon glyphicon-check'),
(11, 1, '业务管理', NULL, 'glyphicon glyphicon-th-list'),
(12, 11, '资质维护', 'cert/index.htm', 'glyphicon glyphicon-picture'),
(13, 11, '资质类型', 'certtype/index.htm', 'glyphicon glyphicon-equalizer'),
(14, 11, '流程管理', 'process/index.htm', 'glyphicon glyphicon-random'),
(15, 11, '广告管理', 'advert/index.htm', 'glyphicon glyphicon-hdd'),
(16, 11, '消息维护', 'message/index.htm', 'glyphicon glyphicon-comment'),
(17, 11, '项目分类', 'projectType/index.htm', 'glyphicon glyphicon-list'),
(18, 11, '标签维护', 'tag/index.htm', 'glyphicon glyphicon-tag'),
(19, 11, '参数管理', 'param/index.htm', 'glyphicon glyphicon-list-alt');
```

- 关联方式
  子节点通过 pid字段关联到父节点的id 字段，建立父子关系。
- 在 Java 类中表示树形结构
  - 基本方式：Menu 类中使用 List< Menu > children 属性存储当前节点的子节点。
  - 为了配合 zTree所需要添加的属性
    pid属性:找到父节点
    name 属性:作为节点名称
    icon 属性:当前节点使用的图标
    open属性:控制节点是否默认打开
    url属性:点击节点时跳转的位置

## 页面显示树形结构

- 目标：将数据库中查询得到的数据到页面上显示出来。

- 思路：数据库查询全部一Java对象组装一页面上使用 zTree 显示

- 代码：

  - 逆向工程generatorConfig.xml

  ```xml
  <table tableName="t_menu" domainObjectName="Menu" />
  ```

  - 新增属性Menu.java

  ```java
  // get/set 方法 构造器
  private List<Menu> children = new ArrayList<>();
  private boolean open = true; // 默认打开
  ```

- 获取所有对象

```java
public ResultEntity<Menu> getWholeTreeNew() {
    // 1.查询全部的 Menu 对象
    List<Menu> menuList = menuService.getAll();
    // 2.声明一个变量用来存储找到的根节点
    Menu root = null;
    // 3.创建 Map 对象用来存储 id 和 Menu 对象的对应关系便于查找父节点
    Map<Integer, Menu> menuMap = new HashMap<>();
    // 4.遍历 menuList 填充 menuMap
    for (Menu menu : menuList) {
        Integer id = menu.getId();
        menuMap.put(id, menu);
    }
    // 5.再次遍历 menuList 查找根节点、组装父子节点
    for (Menu menu : menuList) {
        // 6.获取当前 menu 对象的 pid 属性值
        Integer pid = menu.getPid();
        // 7.如果 pid 为 null，判定为根节点
        if(pid == null) {
            root = menu;
            // 8.如果当前节点是根节点，那么肯定没有父节点，不必继续执行
            continue ;
        }
        // 9.如果 pid 不为 null，说明当前节点有父节点，那么可以根据 pid 到 menuMap 中查找对应的 Menu 对象
        Menu father = menuMap.get(pid);
        // 10.将当前节点存入父节点的 children 集合
        father.getChildren().add(menu);
    }
    // 11.经过上面的运算，根节点包含了整个树形结构，返回根节点就是返回整个树
    return ResultEntity.successWithData(root);
}
```

- 页面跳转：视图控制器

```xml
<mvc:view-controller path="/menu/to/page.html" view-name="menu-page" />
```

- 引入ztree环境

```jsp
```

- 修改默认图标

```js
//修改默认的图标
function myAddDiyDom(treeId, treeNode) {
    //zTree生成id的规则  treeDome_7_ico
    // 根据id的生成规则拼接出来span标签的id
    var spanId = treeNode.tId + "_ico";
    // 根据控制图标的span标签的id找到这个span标签
    //删除旧的
    $("#" + spanId).removeClass().addClass(treeNode.icon);
}
```

- 按钮增删改查的规则
  - level0:根节点
    添加子节点
  - level1:分支节点
    修改
    添加子节点
    没有子节点:可以删除
    有子节点:不能删除
  - level 2:叶子节点
    修改
    删除

## 显示按钮组

- 目标：实现显示按钮组
- 思路
  - 第一步:控制<span>A</span>是否显示
  - 第二步:明确具体按钮的添加规则
  - 第三步:准备好按钮的 HTML 标签
  - 第四步:根据按钮规则把按钮填充到span中
- 代码

```js
// 在鼠标移开节点范围时删除按钮组
function myRemoveHoverDom(treeId, treeNode) {
    var btnGroupId = treeNode.tId + "_btnGrp";
    //移除对应的元素
    $("#" + btnGroupId).remove();
}
```

```js
// 在鼠标移入节点范围时添加按钮组
function myAddHoverDom(treeId, treeNode) {
// 按钮组的标签结构：<span><a><i></i></a><a><i></i></a></span>
// 按钮组出现的位置：节点中 treeDemo_n_a 超链接的后面
// 为了在需要移除按钮组的时候能够精确定位到按钮组所在 span，需要给 span 设置有规律的 id
    var btnGroupId = treeNode.tId + "_btnGrp";
// 判断一下以前是否已经添加了按钮组
    if($("#"+btnGroupId).length > 0) {
        return ;
    }
    // 准备各个按钮的HTML标签
    var addBtn = "<a id='" + treeNode.id + "' class='addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    var removeBtn = "<a id='" + treeNode.id + "' class='removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' title=' 删 除 节 点 '>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
    var editBtn = "<a id='" + treeNode.id + "' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' title=' 修 改 节 点 '>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";

    // 获取当前结点的级别
    var level = treeNode.level;
    // 声明变量存储拼装好的按钮代码
    var btnHTML = "";
    // 判断当前节点的级别
    if(level == 0) {
    // 级别为 0 时是根节点，只能添加子节点
        btnHTML = addBtn;
    }
    if(level == 1) {
    // 级别为 1 时是分支节点，可以添加子节点、修改
        btnHTML = addBtn + " " + editBtn;
    // 获取当前节点的子节点数量
        var length = treeNode.children.length;
    // 如果没有子节点，可以删除
        if(length == 0) {
            btnHTML = btnHTML + " " + removeBtn;
        }
    }
    if(level == 2) {
    // 级别为 2 时是叶子节点，可以修改、删除
        btnHTML = editBtn + " " + removeBtn;
    }
    // 找到附着按钮组的超链接
    var anchorId = treeNode.tId + "_a";
    //执行在超链接后面附加span元素的操作
    $("#" + anchorId).after("<span id='" + btnGroupId + "'>" + btnHTML + "</span>");
}
```

```js
// 树形结构显示
function  generateTree() {
    // 1.准备生成树形结构的JSON数据，数据的来源是发送ajax请求得到的
    $.ajax({
        "url": "menu/get/whole/tree.json",
        "type": "post",
        "dataType": "json",
        "success": function (response) {
            var result = response.result;
            if (result == "SUCCESS") {
                // 2.创建JSON对象用于存储对zTree所做的设置
                var setting = {
                    "view":{
                        "addDiyDom": myAddDiyDom,
                        "addHoverDom":myAddHoverDom,
                        "removeHoverDom":myRemoveHoverDom
                    },
                    "data":{
                        "key":{
                            "url":"maomi"
                        }
                    }
                };
                // 3.从响应体获取数据
                var zNodes = response.data;
                // 4.初始化树形结构
                $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            }
            if (result == "FAILED") {
                layer.msg(response.message);
            }
        }
    });
}
```

- jsp页面显示树menu-page.jsp

```jsp
generateTree();
```

## 添加子节点

- 目标：给当前节点添加子节点，保存到数据库并刷新树形结构的显示。
- 思路

![菜单管理_添加子节点](.\ZCW_MarkDownPhoto\菜单管理_添加子节点.png)

- 代码

  - 模态框显示
    - modal-menu-add.jsp
    - modal-menu-confirm.jsp
    - modal-menu-edit.jsp

  ```jsp
  <%@ include file="/WEB-INF/modal-menu-add.jsp" %>
  <%@ include file="/WEB-INF/modal-menu-edit.jsp" %>
  <%@ include file="/WEB-INF/modal-menu-confirm.jsp" %>
  ```

  ```js
  // 准备各个按钮的HTML标签
  var addBtn = "<a id='" + treeNode.id + "' class='addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
  var removeBtn = "<a id='" + treeNode.id + "' class='removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' title=' 删 除 节 点 '>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
  var editBtn = "<a id='" + treeNode.id + "' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' title=' 修 改 节 点 '>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";
  
  ```

  - 添加保存相应事件

  ```js
  // 给添加子结点的模态框中的保存按钮绑定单击响应函数
  $("#menuSaveBtn").click(function () {
      // 收集表单项中用户输入的数据
      var name = $.trim($("#menuAddModal [name=name]").val());
      var url = $.trim($("#menuAddModal [name=url]").val());
      // 单选按钮要定位到“被选中”的那一个
      var icon = $("#menuAddModal [name=icon]:checked").val();
      // 发送ajax请求
      $.ajax({
          "url": "menu/save.json",
          "type": "post",
          "data": {
              "pid": window.pid,
              "name": name,
              "url": url,
              "icon": icon
          },
          "dataType": "json",
          "success": function (response) {
              var result = response.result;
              if (result == "SUCCESS") {
                  layer.msg("操作成功！");
                  // 重新加载树形结构
                  generateTree();
              }
              if (result == "FAILED") {
                  layer.msg("操作失败！" + response.message);
              }
          },
          "error": function (response) {
              layer.msg(response.status + " " + response.statusText);
          }
      });
      $("#menuAddModal").modal("hide");
      // 清空表单
      $("#menuResetBtn").click();
  });
  ```

  - Handler方法

  ```java
  @ResponseBody
  @RequestMapping("/menu/save.json")
  public ResultEntity<String> saveMenu(Menu menu){
      menuService.saveMenu(menu);
      return ResultEntity.successWithoutData();
  }
  ```

  - Service方法

  ```java
  @Override
  public void saveMenu(Menu menu) {
      menuMapper.insert(menu);
  }
  ```

  - Mapper/Mapper.xml

```java
void deleteOLdRelationship(Integer adminId);
void insertNewRelationship(@Param("adminId") Integer adminId,@Param("roleIdList") List<Integer> roleIdList);
```

```xml
<insert id="insertNewRelationship">
    insert into _role(admin_id,role_id) values
    <foreach collection="roleIdList" item="roleId" separator=",">(#{adminId},#{roleId})</foreach>
</insert>
<delete id="deleteOLdRelationship">
    delete from inner_admin_role where admin_id=#{adminId}
</delete>
```

- 解决下拉列表提交问题

  ![权限控制_下拉列表问题](.\ZCW_MarkDownPhoto\权限控制_下拉列表问题.png)

  - 添加响应函数

  ```jsp
  <button type="submit" style="width: 150px;" class="btn btn-lg btn-success btn-block">保存</button>
  ```

  ```js
  $("#submitBtn").click(function(){
      // 在提交表单前把“已分配”部分的 option 全部选中
      $("select:eq(1)>option").prop("selected","selected");
  });
  ```

  







## 更新节点

- 目标:修改节点的基本属性

- 思路

  ![菜单管理_更新](.\ZCW_MarkDownPhoto\菜单管理_更新.png)

- 代码：

  - 回显表单

  ```js
  $("#menuEditBtn").click(function () {
      // 收集表单数据
      var name = $("#menuEditModal [name=name]").val();
      var url = $("#menuEditModal [name=url]").val();
      // 回显 radio 可以这样理解：被选中的 radio 的 value 属性可以组成一个数组，
      // 然后再用这个数组设置回 radio，就能够把对应的值选中
      var icon = $("#menuEditModal [name=icon]:checked").val();
      // 发送ajax请求
      $.ajax({
          "url": "menu/update.json",
          "type": "post",
          "data": {
              "id": window.id,
              "name": name,
              "url": url,
              "icon": icon
          },
          "dataType": "json",
          "success": function (response) {
              var result = response.result;
              if (result == "SUCCESS") {
                  layer.msg("操作成功！");
                  // 重新加载树形结构
                  generateTree();
              }
              if (result == "FAILED") {
                  layer.msg("操作失败！" + response.message);
              }
          },
          "error": function (response) {
              layer.msg(response.status + " " + response.statusText);
          }
      });
      $("#menuEditModal").modal("hide");
  });
  ```

  - handler/Service

  ```java
  @ResponseBody
  @RequestMapping("/menu/update.json")
  public ResultEntity<String> updateMenu(Menu menu){
      menuService.updateMenu(menu);
      return ResultEntity.successWithoutData();
  }
  ```

  ```java
  @Override
  public void updateMenu(Menu menu) {
      // 由于pid没有传入所以要选择有选择的更新
      menuMapper.updateByPrimaryKeySelective(menu);
  }
  ```

## 删除节点

- 目标

- 思路

  ![菜单管理_删除](.\ZCW_MarkDownPhoto\菜单管理_删除.png)

- 代码

  - 前端代码

  ```js
  $("#treeDemo").on("click",".removeBtn",function () {
      // 将当前节点的 id 保存到全局变量
      window.id = this.id;
      // 打开模态框
      $("#menuConfirmModal").modal("show");
      // 获取 zTreeObj 对象
      var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
      // 根据 id 属性查询节点对象
      // 用来搜索节点的属性名
      var key = "id";
      // 用来搜索节点的属性值
      var value = window.id;
      var currentNode = zTreeObj.getNodeByParam(key, value);
      $("#removeNodeSpan").html(" <i class='"+currentNode.icon+"'></i>"+currentNode.name+"");
      return false;
  });
  // 给确认模态框中的按钮绑定单击响应函数
  $("#confirmBtn").click(function(){
      $.ajax({
          "url":"menu/remove.json",
          "type":"post",
          "data":{
              "id":window.id
          },
          "dataType":"json",
          "success":function(response){
              var result = response.result;
              if(result == "SUCCESS") {
                  layer.msg("操作成功！");
                  // 重新加载树形结构，注意：要在确认服务器端完成保存操作后再刷新
                  // 否则有可能刷新不到最新的数据，因为这里是异步的
                  generateTree();
              }
              if(result == "FAILED") {
                  layer.msg("操作失败！"+response.message);
              }
          },
          "error":function(response){
              layer.msg(response.status+" "+response.statusText);
          }
      });
      // 关闭模态框
      $("#menuConfirmModal").modal("hide");
  });
  ```

  - 注解优化

  ```java
  @RestController
  // @RestController 相当于 @Controller + 所有方法加@ResponseBody
  ```

  

# 7.权限控制

- 目标：实现全线管理访问资源

- 思路

  ![权限控制_思路](.\ZCW_MarkDownPhoto\权限控制_思路.png)

## admin分配role

- 目标：通过页面操作把 Admin和 Role 之间的关联关系保存到数据库。
- 思路：

![权限控制_admin添加role](.\ZCW_MarkDownPhoto\权限控制_admin添加role.png)

- 代码

  - 创建保存Admin-Role关联关系的数据库(不对应具体的实体类，不做逆向工程)

  ```sql
  CREATE TABLE `project_zcw`.`inner_admin_role` 
  ( `id` INT NOT NULL AUTO_INCREMENT, 
   `admin_id` INT, `role_id` INT,
   PRIMARY KEY (`id`) ); 
  ```

  - 修改分配按钮

  ```jsp
  <a href="assign/to/assign/role/page.html?adminId=${admin.id }&pageNum=${requestScope.pageInfo.pageNum }&keyword=${param.keyword }" class="btn btn-success btn-xs"><i class="glyphicon glyphicon-check"></i></a>
  ```

  - 创建assignHandler

  ```java
  @Controller
  public class AssignHandler {
      @Autowired
      private AdminService adminService;
      @Autowired
      private RoleService roleService;
      @RequestMapping("/assign/to/assign/role/page.html")
      public String toAssignRolePage(
              @RequestParam("adminId") Integer adminId,
              ModelMap modelMap
      ) {
          // 1.查询已分配角色
          List<Role> assignedRoleList = roleService.getAssignedRole(adminId);
          // 2.查询未分配角色
          List<Role> unAssignedRoleList = roleService.getUnAssignedRole(adminId);
          // 3.存入模型（本质上其实是：request.setAttribute("attrName",attrValue);
          modelMap.addAttribute("assignedRoleList", assignedRoleList);
          modelMap.addAttribute("unAssignedRoleList", unAssignedRoleList);
          return "assign-role";
      }
  
  }
  ```

  - 添加RoleMapper.java/RoleMapper.xml方法和实现

  ```java
  List<Role> getAssignedRole(Integer adminId);
  List<Role> getUnAssignedRole(Integer adminId);
  ```

  ```xml
  <select id="selectAssignedRole" resultMap="BaseResultMap">
      select id,name from t_role where id in (select role_id from inner_admin_role where admin_id=#{adminId})
  </select>
  <select id="selectUnAssignedRole" resultMap="BaseResultMap">
      select id,name from t_role where id not in (select role_id from inner_admin_role where admin_id=#{adminId})
  </select>
  ```

  - assign-role.jsp
  - 显示Role数据

  ```jsp
  assign-role.jsp // 看文件
  ```

  - 执行权限分配：保存

  ```java
  // AssignHandler方法出来请求
  @RequestMapping("/assign/do/role/assign.html")
  public String saveAdminRoleRelationship(
      @RequestParam("adminId") Integer adminId,
      @RequestParam("pageNum") Integer pageNum,
      @RequestParam("keyword") String keyword,
      // 我们允许用户在页面上取消所有已分配角色再提交表单，所以可以不提供roleIdList 请求参数
      // 设置 required=false 表示这个请求参数不是必须的
      @RequestParam(value = "roleIdList", required = false) List<Integer> roleIdList
  ) {
      adminService.saveAdminRoleRelationship(adminId, roleIdList);
      return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
  }
  ```

  ```java
  // service方法
  @Override
  public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
      // 为了简化操作：先根据 adminId 删除旧的数据，再根据 roleIdList 保存全部新的数据
      // 1.根据 adminId 删除旧的关联关系数据
      adminMapper.deleteOLdRelationship(adminId);
      // 2.根据 roleIdList 和 adminId 保存新的关联关系
      if (roleIdList != null && !roleIdList.isEmpty()) {
          adminMapper.insertNewRelationship(adminId, roleIdList);
      }
  }
  ```

  

## role分配auth

- 目标

- 思路

  ![权限控制_用户分配权限](.\ZCW_MarkDownPhoto\权限控制_用户分配权限.png)

- 代码

  - 创建表

  ```sql
  CREATE TABLE `t_auth`(
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) DEFAULT NULL,
  `title` VARCHAR(200) DEFAULT NULL,
  `category_id` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
  );
  INSERT INTO t_auth(id, `name` , title,category_id) VALUES(1,'', '用户模块',NULL);
  INSERT INTO t_auth(id, `name` ,title,category_id) VALUES(2, 'user:delete','删除',1);
  INSERT INTO t_auth( id, `name`, title,category_id) VALUES(3, 'user:get','查询',1);
  INSERT INTO t_auth(id, `name`, title,category_id) VALUES(4,'','角色模块',NULL);
  INSERT INTO t_auth(id, `name`, title,category_id) VALUES(5,'role:delete','删除' ,4);
  INSERT INTO t_auth(id, `name`, title,category_id) VALUES(6,'role:get','查询',4);
  INSERT INTO t_auth(id, `name`, title,category_id) VALUES(7,'role:add' ,'新增',4);
  ```

  - name字段:给资源分配权限或给角色分配权限时使用的具体值，将来做权限验证也是使用name字段的值来进行比对。建议使用英文。
  - title字段:在页面上显示，让用户便于查看的值。建议使用中文。
  - category_id字段:关联到当前权限所属的分类。这个关联不是到其他表关联，而是就在当前表内部进行关联，关联其他记录。

- 给按钮绑定单机响应函数、my-role.js、fillTableBody

  ```js
  var checkBtn = "<button id='" + roleId + "' type='button' class='btn btn-success btn-xs checkBtn'><i class=' glyphicon glyphicon-check'></i></button>";
  ```

```js
// 13.给分配权限按钮绑定单击响应函数
$("#rolePageBody").on("click", ".checkBtn", function () {
    // 把当前角色id存入全局变量
    window.roleId = this.id;
    // 打开模态框
    $("#assignModal").modal("show");
    // 在模态框中装载树 Auth 的形结构数据
    fillAuthTree();
});
```

```js
// 声明专门的函数用来分配Auth的模态框中显示Auth的树形结构数据
function fillAuthTree() {
    // 1.发送 Ajax 请求查询 Auth 数据
    var ajaxReturn = $.ajax({
        "url":"assgin/get/all/auth.json",
        "type":"post",
        "dataType":"json",
        "async":false
    });
    if(ajaxReturn.status != 200) {
        layer.msg("请求处理出错响应状态码 是 ： "+ajaxReturn.status+" 说 明 是 ："+ajaxReturn.statusText);
        return ;
    }
    // 2.从响应结果中获取 Auth 的 JSON 数据
    // 从服务器端查询到的 list 不需要组装成树形结构，这里我们交给 zTree 去组装
    var authList = ajaxReturn.responseJSON.data;
    // 3.准备对 zTree 进行设置的 JSON 对象
    var setting = {
        "data": {
            "simpleData": {
                // 开启简单 JSON 功能
                "enable": true,
                // 使用 categoryId 属性关联父节点，不用默认的 pId 了
                "pIdKey": "categoryId"
            },
            "key": {
                // 使用 title 属性显示节点名称，不用默认的 name 作为属性名了
                "name": "title"
            }
        },
        "check": {
            "enable": true
        }
    };
    // 4.生成树形结构
    // <ul id="authTreeDemo" class="ztree"></ul>
    $.fn.zTree.init($("#authTreeDemo"), setting, authList);
    // 获取 zTreeObj 对象
    var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
    // 调用 zTreeObj 对象的方法，把节点展开
    zTreeObj.expandAll(true);

    // 5.查询已分配的 Auth 的 id 组成的数组
    ajaxReturn = $.ajax({
        "url":"assign/get/assigned/auth/id/by/role/id.json",
        "type":"post",
        "data":{
            "roleId":window.roleId
        },
        "dataType":"json",
        "async":false
    });
    if(ajaxReturn.status != 200) {
        layer.msg("请求处理出错!响应状态码是："+ajaxReturn.status+"说明是："+ajaxReturn.statusText);
        return ;
    }
    // 从响应结果中获取 authIdArray
    var authIdArray = ajaxReturn.responseJSON.data;
    // 6.根据 authIdArray 把树形结构中对应的节点勾选上
    // ①遍历 authIdArray
    for(var i = 0; i < authIdArray.length; i++) {
        var authId = authIdArray[i];
        // ②根据 id 查询树形结构中对应的节点
        var treeNode = zTreeObj.getNodeByParam("id", authId);
        // ③将 treeNode 设置为被勾选
        // checked 设置为 true 表示节点勾选
        var checked = true;
        // checkTypeFlag 设置为 false，表示不“联动”，不联动是为了避免把不该勾选的勾选上
        var checkTypeFlag = false;
        // 执行
        zTreeObj.checkNode(treeNode, checked, checkTypeFlag);
    }
}
```

- 添加ztree的库role-page.jsp

  ```jsp
  <link rel="stylesheet" href="css/pagination.css"/>
  <script type="text/javascript" src="jquery/jquery.pagination.js"></script>
  <link rel="stylesheet" href="ztree/zTreeStyle.css"/>
  <script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
  <script type="text/javascript" charset="UTF-8" src="crowd/my-role.js" ></script>
  ```


- AssignHandler处理请求

  ```java
  @ResponseBody
  @RequestMapping("assgin/get/all/auth.json")
  public ResultEntity<List<Auth>> getAllAuth() {
      List<Auth> authList = authService.getAll();
      return ResultEntity.successWithData(authList);
  }
  @ResponseBody
  @RequestMapping("/assign/get/assigned/auth/id/by/role/id.json")
  public ResultEntity<List<Integer>> getAssignedAuthIdByRoleId(
      @RequestParam("roleId") Integer roleId) {
      List<Integer> authIdList = authService.getAssignedAuthIdByRoleId(roleId);
      return ResultEntity.successWithData(authIdList);
  }
  ```

- 创建角色role和权限auth之间的关联表

  ```sql
  CREATE TABLE `project_zcw`.`inner_role_auth` ( `id` INT NOT NULL AUTO_INCREMENT, `role_id` INT, `auth_id` INT, PRIMARY KEY (`id`) ); 
  ```

- 模态框内：请求分配权限

  ```js
  // 14.给分配权限模态框中的“分配”按钮绑定单击响应函数
  $("#assignBtn").click(function () {
      // ①收集树形结构的各个节点中被勾选的节点
      // [1]声明一个专门的数组存放 id
      var authIdArray = [];
      // [2]获取 zTreeObj 对象
      var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
      // [3]获取全部被勾选的节点
      var checkedNodes = zTreeObj.getCheckedNodes();
      // [4]遍历 checkedNodes
      for (var i = 0; i < checkedNodes.length; i++) {
          var checkedNode = checkedNodes[i];
          var authId = checkedNode.id;
          authIdArray.push(authId);
      }
      // ②发送请求执行分配
      var requestBody = {
          "authIdArray":authIdArray,
          // 为了服务器端 handler 方法能够统一使用 List<Integer>方式接收数据，roleId 也存入数组
          "roleId":[window.roleId]
      }
      requestBody = JSON.stringify(requestBody);
      $.ajax({
          "url": "assign/do/role/assign/auth.json",
          "type": "post",
          "data": requestBody,
          "contentType": "application/json;charset=UTF-8",
          "dataType": "json",
          "success": function (response) {
              var result = response.result;
              if (result == "SUCCESS") {
                  layer.msg("操作成功！");
              }
              if (result == "FAILED") {
                  layer.msg("操作失败！" + response.message);
              }
          },
          "error": function (response) {
              layer.msg(response.status + " " + response.statusText);
          }
      });
      $("#assignModal").modal("hide");
  });
  ```

- Handler保存分配结果/Service方法

  ```java
  @ResponseBody
  @RequestMapping("/assign/do/role/assign/auth.json")
  public ResultEntity<String> saveRoleAuthRelathinship(
      @RequestBody Map<String, List<Integer>> map) {
      authService.saveRoleAuthRelathinship(map);
      return ResultEntity.successWithoutData();
  }
  ```

  ```java
  @Override
  public void saveRoleAuthRelathinship(Map<String, List<Integer>> map) {
      // 1.获取 roleId 的值
      List<Integer> roleIdList = map.get("roleId");
      Integer roleId = roleIdList.get(0);
      // 2.删除旧关联关系数据
      authMapper.deleteOldRelationship(roleId);
      // 3.获取 authIdList
      List<Integer> authIdList = map.get("authIdArray");
      // 4.判断 authIdList 是否有效
      if (authIdList != null && !authIdList.isEmpty()) {
          authMapper.insertNewRelationship(roleId, authIdList);
      }
  }
  ```

- Mapper/Mapper.xml

  ```xml
  <delete id="deleteOldRelationship">
      delete from inner_role_auth where role_id=#{roleId}
  </delete>
  <insert id="insertNewRelationship">
      insert into inner_role_auth(auth_id,role_id) values
      <foreach collection="authIdList" item="authId"
               separator=",">(#{authId},#{roleId})
      </foreach>
  </insert>
  ```

  

## menu分配auth

- 和前面一样省略



## SpringSecurity专题

- 用法简介

  - 用户登录系统时我们协助SpringSecurity把用户对应的角色、权限组装好，同时把各个资源所要求的权限信息设定好，剩下的“登录验证”、权限验证”等等工作都交给SpringSecurity。

- 权限管理相关概念

  - 主体：Principal

    - 使用系统的用户或设备或从其他系统远程登录的用户等等。谁使用系统谁就是主体。

  - 认证：authentication

    - 权限管理系统确认一个主体的身份，允许主体进入系统。简单说就是“主体”证明自己是谁。
    - 笼统的认为就是以前所做的登录操作。

  - 授权

    - 将操作系统的“权力”“授予”“主体”，这样主体就具备了操作系统中特定功能的能力。
    - 授权就是给用户分配权限。

    ![SS_授权](.\ZCW_MarkDownPhoto\SS_授权.png)

- 入门

  - 导入环境

  - 加入 SpringSecurity控制权限的Filter

  - 加入配置类

    ```java
    //启用web环境下权限控制功能
    @EnableWebSecurity
    public class webAppSecurityConfig {
        ...
    }
    ```


## 加入SpringSecurity

- 添加依赖

```xml
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-config</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-taglibs</artifactId>
</dependency>
```

- web.xml中配置DelegatingFilterProxy

```xml
<filter>
    <filter-name>springSecurityFilterChain</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
</filter>
<filter-mapping>
    <filter-name>springSecurityFilterChain</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

- 创建基于注解的配置类

```java
@Configuration // 声明配置类
@EnableWebSecurity // 启用web环境下的权限控制功能
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {
	...
}
```

- 谁(那个IOC)来获取WebAppSecurityConfig？针对浏览器请求-> SpringMVC的IOC容器

  - spring的IOC容器

  ![IOC容器_spring](.\ZCW_MarkDownPhoto\IOC容器_spring.png)

  - SpringMVC的IOC容器

    ![IOC容器_springMVC](.\ZCW_MarkDownPhoto\IOC容器_springMVC.png)

  - WebAppSecurityConfig的注入过程

    ![spring组件加载过程](.\ZCW_MarkDownPhoto\spring组件加载过程.png)

    - 三大组件的启动顺序
      - 首先:ContextLoaderListener 初始化，创建 Spring的IOC容器
      - 其次:DelegatingFilterProxy初始化，查找IOC容器、查找 bean
      - 最后:DispatcherServlet 初始化，创建 SpringMvc 的IOC容器
    - 解决方法：
      - 方法1(方便起见使用方法1)：将两个IOC容器合二为一。缺点：破坏现有程序的结构
      - 方法2：修改源码

- SpringSecurity 的工作原理

  - 在初始化时或第一次请求时准备好过滤器链。具体任务由具体过滤器来完成。
  - csrf,跨站请求防伪

- 目标：

### 1：放行静态资源

- 放行(放行方法和SpringSecurity版本有关)

```java
@Override
public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        .antMatchers("/admin/to/login/page.html","/bootstrap/**","/css/**","/fonts/**","/img/**","/jquery/**"," /layer/**","/script/**","/ztree/**");
}
```



### 2：内存登录(同时取消原来的拦截器)

![Security_目标2](C:.\ZCW_MarkDownPhoto\Security_目标2.png)

- 修改表单admin-login.jsp

```jsp
<form action="security/do/login.html" method="post" class="form-signin" role="form">
        <p>${SPRING_SECURITY_LAST_EXCEPTION }</p>
```

- 注释原来的自定义的登录拦截器spring-web-mvc.xml

```xml
<!--&lt;!&ndash;注册拦截器&ndash;&gt;-->
<!--    <mvc:interceptors>-->
<!--        <mvc:interceptor>-->
<!--            &lt;!&ndash;需要拦截的路径-->
<!--            /* 单层路径-->
<!--            /** 多层路径-->
<!--            &ndash;&gt;-->
<!--            <mvc:mapping path="/**" />-->
<!--            &lt;!&ndash;不拦截的页面&ndash;&gt;-->
<!--            <mvc:exclude-mapping path="/admin/do/login.html"/>-->
<!--            <mvc:exclude-mapping path="/admin/to/login/page.html"/>-->
<!--            <mvc:exclude-mapping path="/admin/do/logout.html"/>-->
<!--            &lt;!&ndash;配置器拦截类&ndash;&gt;-->
<!--            <bean class="com.uestc.crowd.mvc.interceptor.LoginInterceptor" />-->
<!--        </mvc:interceptor>-->
<!--    </mvc:interceptors>-->
```

### 3：退出登录

### 4 ：内存登录修改为查数据库的登录

![Security_目标3_内存登录](.\ZCW_MarkDownPhoto\Security_目标3_内存登录.png)

- 根据 adminId 查询已分配的角色/AuthMapper

```java
@Override
public List<String> getAssignedAuthNameByAdminId(Integer adminId) {
    return authMapper.selectAssignedAuthNameByAdminId(adminId);
}
```

```xml
<select id="selectAssignedAuthNameByAdminId" resultType="string">
    SELECT DISTINCT a.`name`FROM t_auth a
    LEFT JOIN inner_role_auth ra ON ra.auth_id = a.id
    LEFT JOIN inner_admin_role ar ON ar.role_id = ra.role_id
    WHERE ar.admin_id = #{adminId} AND a.`name` != "" AND a.`name` is not null
</select>
```

- 创建SecurityAdmin.java类

```java
public class SecurityAdmin extends User {

    private static final long serialVersionUID = 370673149335319595L;
    // 原始admin对象，包含admin对象的全部属性
    private Admin originalAdmin;

    public SecurityAdmin(Admin originalAdmin, List<GrantedAuthority> authorities) {
        super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);
        this.originalAdmin = originalAdmin;
        // 将原始Admin对象中的密码擦除
        this.originalAdmin.setUserPswd(null);
    }
    // 获取原始的Admin对象的get方法
    public Admin getOriginalAdmin(){
        return originalAdmin;
    }
}
```

- 根据账号查询 Admin

```java
@Override
public Admin getAdminByLoginAcct(String username) {
    AdminExample adminExample = new AdminExample();
    AdminExample.Criteria criteria = adminExample.createCriteria();
    criteria.andLoginAcctEqualTo(username);
    List<Admin> list = adminMapper.selectByExample(adminExample);
    Admin admin = list.get(0);
    return admin;
}
```

- 创建UserDetailsservice (UserDetailsService)实现类

```java
@Component
public class CrowdUserDetailsService implements UserDetailsService {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthService authService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.根据账号名称查询Admin对象
        Admin admin = adminService.getAdminByLoginAcct(username);
        // 2.获取adminId
        Integer adminId = admin.getId();
        // 3,根据adminId查询角色信息
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);
        // 4.根据adminId查询权限信息
        List<String> authNameList = authService.getAssignedAuthNameByAdminId(adminId);
        // 5.创建集合对象用来存储GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 6.遍历assignedRoleList存入角色信息
        for (Role role : assignedRoleList) {
            String roleName = "ROLE_" + role.getName();
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);
            authorities.add(simpleGrantedAuthority);
        }
        // 7.遍历authNameList存入权限信息
        for (String authName : authNameList) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authName);
            authorities.add(simpleGrantedAuthority);
        }
        // 8.封装SecurityAdmin对象
        SecurityAdmin securityAdmin = new SecurityAdmin(admin, authorities);

        return securityAdmin;
    }
}
```

- 在配置类中使用UserDetailsservice

```java
@Override
protected void configure(AuthenticationManagerBuilder builder) throws Exception { 	builder.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
}
```

### 5：密码加密(盐值加密)

- 修改 t_admin 表结构(增加密码长度)

```sql
ALTER TABLE `project_zcw`.`t_admin` CHANGE `user_pswd` `user_pswd` CHAR(100) CHARSET utf8 COLLATE utf8_general_ci NOT NULL; 
```

- 准备 BCryptPasswordEncoder 对象/WebAppSecurityConfig.java

```java
@Bean
public BCryptPasswordEncoder getPasswordEncoder(){
    return new BCryptPasswordEncoder();
}
```

- 使用 BCryptPasswordEncoder 对象

```java
@Override
protected void configure(AuthenticationManagerBuilder builder) throws Exception {
     builder.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
}
```

- 保存admin时也需要加密/AdminServiceImpl.java

  ```java
  @Autowired
      private BCryptPasswordEncoder bCryptPasswordEncoder;
  ...
  userPswd = passwordEncoder.encode(userPswd);
  ```

  - 准备BCryptPasswordEncoder对象
    - 注意:如果在SpringSecurity的配置类中用@Bean 注解将BCryptPasswordEncoder对象存入I0C容器，那么 Service 组件将获取不到。(spring和springMVC的容器合二为一就不存在该问题。前面选择第一种方法就不用管)

![Security_目标5_IOC容器问题](.\ZCW_MarkDownPhoto\Security_目标5_IOC容器问题.png)



### 6：显示修改

- 在页面上显示用户昵称/include-nav.jsp
- principal原来是我们自己封装的SecurityAdmin对象(主体)
- SpringSecurity处理完登录操作之后把登录成功的User对象以principal属性名存入了UsernamePasswordAuthenticationToken对象

```jsp
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<security:authentication property="principal.originalAdmin.userName"/>
```

### 7：密码的擦除

- 本身 Springsecurity ,是会自动把 User 对象中的密码部分擦除。

```java
public void eraseCredentials(){
    password = null;
}
```

- 原始的Admin对象没有擦除，需要设置擦除。/SecurityAdmin
  - 擦除密码是在不影响登录认证的情况下，避免密码泄露，增加系统安全性。

```java
public SecurityAdmin(Admin originalAdmin, List<GrantedAuthority> authorities) {
    super(originalAdmin.getLoginAcct(), originalAdmin.getUserPswd(), authorities);
    this.originalAdmin = originalAdmin;
    // 将原始Admin对象中的密码擦除
    this.originalAdmin.setUserPswd(null);
}
```

### 8：权限控制

- Handler访问权限设置

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) 
// 启用全局方法权限控制功能，并且设置prePostEnabled=true
// 保证@PreAuthority、@PostAuthority、@PreFilter、@PostFilter生效
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {...}
```



```java
@PreAuthorize"hasRole('部长')")
@RequestMapping("/role/get/page/info.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(...) {...}
```

- 补充：完善基于注解的异常映射/CrowdExceptionResolver.java
  - 发现:基于注解的异常映射和基于XML的异常映射如果映射同一个异常类型，那么基于注解的方案优先。

```java
@ExceptionHandler(value = Exception.class)
public ModelAndView resolveException(Exception exception,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws IOException {
    String viewName = "admin-login";
    return commonResolve(viewName, exception, request, response);
}
```

- 访问 Admin 保存功能时具备 user:save 权限

```java
 @PreAuthorize("hasAuthority('user:save')")
    @RequestMapping("/admin/save.html")
    public String save(Admin admin) {
        try {
            adminService.saveAdmin(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
        return "redirect:/admin/get/page.html";
    }
```

- 相关注解
  - @PostAuthorize :先执行方法然后根据方法返回值判断是否具备权限
  - @PreFilter:在方法执行前对传入的参数进行过滤。只能对集合类型的数据进行过滤。
  - @PostFilter:在方法执行后对方法返回值进行规律。只能对集合类型的数据进行过滤

### 9：页面元素的权限控制

- 页面控制
  - 使用springSecurity的标签进行操作





# 注意事项

- 某个被使用的Bean没有装配，检查扫描范围或Google

- ajax的异步执行

  - 同步执行

  ```javascript
  $.ajax({
      "url":"test/ajax/async.html"
      "type":"post",
      "dataType":"text",
      "async": false,//关闭异步工作模型，使用同步方式工作。
      "success":function(response){
      //success是接收到服务器端响应后执行
      console.log("ajax函数内部的success函数"+response);
  });
  ```

  ![ajax同步请求](.\ZCW_MarkDownPhoto\ajax同步请求.png)

  - 异步执行

![ajax异步请求](.\ZCW_MarkDownPhoto\ajax异步请求.png)

- 同步:同一个线程内部按顺序执行
- 异步:多个线程同时并行执行，互相不等待
  - Tomcat的线程池，处理不同的请求
  - RabbitMQ消息队列处理微服务之间的异步通信