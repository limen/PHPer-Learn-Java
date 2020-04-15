# PHPer learn Java

# 概述
本文面向需要学习Java的PHP开发者。
本文从一个helloworld程序开始，紧接着一个数据库操作demo（这有别于从易到难的风格），然后回归基础，难度逐步加大。把数据库操作demo放在前面，是希望大家快速对一个Java项目形成感性的认识，请不要深究技术细节。
难度最大的部分在于多线程和线程安全，这对很多PHP开发者来说是全新的领域。
最后部分讲解如何用SpingBoot实现RESTful API。
限于作者水平有限，不准确之处在所难免，敬请指正。


# 准备工作
环境依赖

- JDK 1.8
- IDE Intelij IDEA
- Maven



# Hello, World!
按照惯例先写一个 helloword 程序。
新建文件 HelloWorld.java
```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```


编译源文件，生成同名的.class文件
```bash
$ javac HelloWorld.java
```


执行class文件，无需后缀
```bash
$ java HelloWorld
Hello, World!
```


main 方法是Java类的入口方法，方法名称和格式是约定俗成的。


**作业**

- 写一个PHP版的helloword，对比一下两者的区别。



# 难度提升
这一节用Java做点有难度的事：操作数据库。这一节的目标是对Java项目建立感性的认识。
相关知识点

- 依赖管理
- Mybatis
- XML

maven是Java项目依赖管理工具，类似于PHP的composer。
maven使用pom.xml配置依赖，类似于composer.json。
引入两个依赖：mybatis和mysql connector，pom.xml文件内容如下
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.limengxiang</groupId>
    <artifactId>basics</artifactId>
    <version>1.0-SNAPSHOT</version>

    <!-- 使用阿里云中心仓库 -->
    <repositories>
        <repository>
            <id>aliyun</id>
            <name>aliyun</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public</url>
        </repository>
    </repositories>
    <pluginRepositories>
        <pluginRepository>
            <id>aliyun</id>
            <name>aliyun</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public</url>
        </pluginRepository>
    </pluginRepositories>

    <dependencies>
        <!-- mybatis -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.2</version>
        </dependency>
        <!-- MySQl连接器 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.15</version>
            <scope>runtime</scope>
        </dependency>
    </dependencies>

</project>
```


项目的目录结构如下
![选区_081.png](https://cdn.nlark.com/yuque/0/2020/png/514450/1586431803443-3b9b8245-f746-4fa6-a17f-38e7872cdd12.png#align=left&display=inline&height=536&name=%E9%80%89%E5%8C%BA_081.png&originHeight=536&originWidth=394&size=26865&status=done&style=none&width=394)


- UserDAO.java：定义CRUD方法
- UserModel.java：定义数据结构
- UserMapper.xml：定义UserDAO方法对应的SQL

表结构


```sql
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `mobile` varchar(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
```


UserDAO.java
```java
package com.limengxiang.basics.dao;

import com.limengxiang.basics.model.UserModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDAO {

    public Integer insert(@Param("username") String username, @Param("mobile") String mobile);

    public UserModel selectOne(@Param("tableName") String tableName, @Param("id") Integer id);

    public List<UserModel> fuzzySearch(@Param("username") String username, @Param("mobile") String mobile);

    public List<UserModel> searchByUsernameOrMobile(@Param("username") String username, @Param("mobile") String mobile);
}
```


UserModel.java
```java
package com.limengxiang.basics.model;

public class UserModel {
    private String username;
    private String mobile;

    public String toString() {
        return "username:" + username +
                ", mobile:" + mobile;
    }
}
```


UserMapper.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- mapper:根标签，namespace：命名空间，也就是DAO类 -->
<mapper namespace="com.limengxiang.basics.dao.UserDAO">
    <!-- 插入数据 -->
    <insert id="insert" parameterType="com.limengxiang.basics.model.UserModel">
        INSERT INTO users (username,mobile) VALUES (#{username}, #{mobile})
    </insert>
    <!-- 按主键ID查询 -->
    <select id="selectOne" resultType="com.limengxiang.basics.model.UserModel">
        SELECT * FROM ${tableName} WHERE id = #{id}
    </select>
    <!-- 动态SQL，当参数非空时模糊匹配 -->
    <select id="fuzzySearch" resultType="com.limengxiang.basics.model.UserModel">
        SELECT * FROM users
        <!-- 不用担心多余的AND -->
        <where>
            <if test="username!=null and username.trim()!=''">AND username LIKE #{username}</if>
            <if test="mobile!=null and mobile.trim()!=''">AND mobile LIKE #{mobile}</if>
        </where>
    </select>
    <!-- 动态SQL，类似switch语句 -->
    <select id="searchByUsernameOrMobile" resultType="com.limengxiang.basics.model.UserModel">
        SELECT * FROM users
        <where>
            <!-- 不用担心多余的AND -->
            <choose>
                <when test="username!=null and username.trim()!=''">
                    AND username LIKE #{username}
                </when>
                <when test="mobile!=null and mobile.trim()!=''">
                    AND mobile LIKE #{mobile}
                </when>
            </choose>
        </where>
    </select>
</mapper>
```


以上三个文件帮助我们定义好了CRUD的业务逻辑。仔细看一下文件内容，试着发现一些规律。
在开始操作数据库之前，还需要配置连接信息，并加载UserMapper.xml文件。
mybatis-config.xml 
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<!-- 根标签 -->
<configuration>
    <properties>
        <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
    </properties>

    <!-- 环境，可以配置多个，default：指定采用哪个环境 -->
    <environments default="development">
        <!-- id：唯一标识 -->
        <environment id="development">
            <!-- 事务管理器，JDBC类型的事务管理器 -->
            <transactionManager type="JDBC" />
            <!-- 数据源，池类型的数据源 -->
            <dataSource type="POOLED">
                <property name="driver" value="${driver}" /> <!-- 配置了properties，所以可以直接引用 -->
                <property name="url" value="jdbc:mysql://172.168.0.92:3306/mybatis_demo"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>

    <mappers>
        <mapper resource="mapper/UserMapper.xml" />
    </mappers>
</configuration>
```


MybatisDemo.java
```java
package com.limengxiang.basics;

import com.limengxiang.basics.dao.UserDAO;
import com.limengxiang.basics.model.UserModel;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MybatisDemo {
    public static void main(String[] args) {
        try {
            InputStream stream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSession sqlSession = new SqlSessionFactoryBuilder().build(stream).openSession(true);
            UserDAO userDAO = sqlSession.getMapper(UserDAO.class);
            // 插入数据
            Integer inserted = userDAO.insert("allen", "13356986723");
            System.out.println("Insert result:" + inserted);
            inserted = userDAO.insert("alfred", "13856986723");
            System.out.println("Insert result:" + inserted);

            UserModel user = userDAO.selectOne("users", 1);
            System.out.println("Select one:" + user);

            List<UserModel> users = userDAO.fuzzySearch("all%", "");
            System.out.println("Fuzzy search by username:" + users);

            users = userDAO.fuzzySearch("", "133%");
            System.out.println("Fuzzy search by mobile:" + users);


            users = userDAO.fuzzySearch("all%", "133%");
            System.out.println("Fuzzy search by username and mobile:" + users);

            users = userDAO.searchByUsernameOrMobile("all%", "135%");
            System.out.println("Search by username:" + users);

            users = userDAO.searchByUsernameOrMobile("", "135%");
            System.out.println("Search by mobile:" + users);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
```


运行结果如下，重点关注打印出的SQL，和UserMapper.xml定义的SQL进行对比，试着发现一些规律。
```basic
19:45:14.943 [main] DEBUG org.apache.ibatis.logging.LogFactory - Logging initialized using 'class org.apache.ibatis.logging.slf4j.Slf4jImpl' adapter.
19:45:15.094 [main] DEBUG org.apache.ibatis.datasource.pooled.PooledDataSource - PooledDataSource forcefully closed/removed all connections.
19:45:15.094 [main] DEBUG org.apache.ibatis.datasource.pooled.PooledDataSource - PooledDataSource forcefully closed/removed all connections.
19:45:15.094 [main] DEBUG org.apache.ibatis.datasource.pooled.PooledDataSource - PooledDataSource forcefully closed/removed all connections.
19:45:15.094 [main] DEBUG org.apache.ibatis.datasource.pooled.PooledDataSource - PooledDataSource forcefully closed/removed all connections.
19:45:15.171 [main] DEBUG org.apache.ibatis.transaction.jdbc.JdbcTransaction - Opening JDBC Connection
19:45:15.590 [main] DEBUG org.apache.ibatis.datasource.pooled.PooledDataSource - Created connection 2085002312.
19:45:15.592 [main] DEBUG com.limengxiang.basics.dao.UserDAO.insert - ==>  Preparing: INSERT INTO users (username,mobile) VALUES (?, ?) 
19:45:15.630 [main] DEBUG com.limengxiang.basics.dao.UserDAO.insert - ==> Parameters: allen(String), 13356986723(String)
19:45:15.638 [main] DEBUG com.limengxiang.basics.dao.UserDAO.insert - <==    Updates: 1
Insert result:1
19:45:15.639 [main] DEBUG com.limengxiang.basics.dao.UserDAO.insert - ==>  Preparing: INSERT INTO users (username,mobile) VALUES (?, ?) 
19:45:15.639 [main] DEBUG com.limengxiang.basics.dao.UserDAO.insert - ==> Parameters: alfred(String), 13856986723(String)
19:45:15.644 [main] DEBUG com.limengxiang.basics.dao.UserDAO.insert - <==    Updates: 1
Insert result:1
19:45:15.662 [main] DEBUG com.limengxiang.basics.dao.UserDAO.selectOne - ==>  Preparing: SELECT * FROM users WHERE id = ? 
19:45:15.662 [main] DEBUG com.limengxiang.basics.dao.UserDAO.selectOne - ==> Parameters: 1(Integer)
19:45:15.691 [main] DEBUG com.limengxiang.basics.dao.UserDAO.selectOne - <==      Total: 0
Select one:null
19:45:15.705 [main] DEBUG com.limengxiang.basics.dao.UserDAO.fuzzySearch - ==>  Preparing: SELECT * FROM users WHERE username LIKE ? 
19:45:15.705 [main] DEBUG com.limengxiang.basics.dao.UserDAO.fuzzySearch - ==> Parameters: all%(String)
19:45:15.708 [main] DEBUG com.limengxiang.basics.dao.UserDAO.fuzzySearch - <==      Total: 1
Fuzzy search by username:[username:allen, mobile:13356986723]
19:45:15.709 [main] DEBUG com.limengxiang.basics.dao.UserDAO.fuzzySearch - ==>  Preparing: SELECT * FROM users WHERE mobile LIKE ? 
19:45:15.709 [main] DEBUG com.limengxiang.basics.dao.UserDAO.fuzzySearch - ==> Parameters: 133%(String)
19:45:15.710 [main] DEBUG com.limengxiang.basics.dao.UserDAO.fuzzySearch - <==      Total: 1
Fuzzy search by mobile:[username:allen, mobile:13356986723]
19:45:15.710 [main] DEBUG com.limengxiang.basics.dao.UserDAO.fuzzySearch - ==>  Preparing: SELECT * FROM users WHERE username LIKE ? AND mobile LIKE ? 
19:45:15.711 [main] DEBUG com.limengxiang.basics.dao.UserDAO.fuzzySearch - ==> Parameters: all%(String), 133%(String)
19:45:15.712 [main] DEBUG com.limengxiang.basics.dao.UserDAO.fuzzySearch - <==      Total: 1
Fuzzy search by username and mobile:[username:allen, mobile:13356986723]
19:45:15.713 [main] DEBUG com.limengxiang.basics.dao.UserDAO.searchByUsernameOrMobile - ==>  Preparing: SELECT * FROM users WHERE username LIKE ? 
19:45:15.713 [main] DEBUG com.limengxiang.basics.dao.UserDAO.searchByUsernameOrMobile - ==> Parameters: all%(String)
19:45:15.714 [main] DEBUG com.limengxiang.basics.dao.UserDAO.searchByUsernameOrMobile - <==      Total: 1
Search by username:[username:allen, mobile:13356986723]
19:45:15.715 [main] DEBUG com.limengxiang.basics.dao.UserDAO.searchByUsernameOrMobile - ==>  Preparing: SELECT * FROM users WHERE mobile LIKE ? 
19:45:15.715 [main] DEBUG com.limengxiang.basics.dao.UserDAO.searchByUsernameOrMobile - ==> Parameters: 135%(String)
19:45:15.716 [main] DEBUG com.limengxiang.basics.dao.UserDAO.searchByUsernameOrMobile - <==      Total: 0
Search by mobile:[]
```


# 回归基础
我们继续从基础开始学习，打好基础才能走得更远。
## 静态与动态，强类型与弱类型
Java是静态语言，PHP是动态语言；Java是强类型语言，PHP是弱类型语言。
静态的含义是在编译期就能确定变量的数据类型，动态则不能。这也属于强弱类型的范畴。
Java语言在声明变量时需要指定数据类型，这确保了“静态”，PHP则相反。
## 数据类型
基本数据类型

- short
- int
- long
- float
- double
- char
- byte
- boolean

包装器类型

- Short：short
- Integer：int
- Long：long
- Float：float
- Double：double
- Character：char
- Byte：byte
- Boolean：boolean

包装器类封装了基本类型常用的一些API，例如将字符串解析为整数。
除基本数据类型外，一切皆对象！


**思考**

- PHP有哪些数据类型？跟Java对比有什么区别？
## 
## 类与继承
PHP和Java都是面向对象的编程语言。
PHP的面向对象借鉴了Java的设计，例如

- 继承关键词：extends，implements
- 不允许多继承，但可以实现多个接口
- 抽象类、抽象方法

熟悉PHP面向对象的开发者，学习Java面向对象不会有难度。这里不赘述了。


## 包与命名空间
在Java文件的顶部可以看到 package ***，这声明了Java类所属的包，和PHP的命名空间（namespace）类似。
Java对类的命名要求更严格一些，例如类名（public类）必须和文件名保持一致。


# 难度Level 2
## 方法签名
先看一个例子


```java
public class HelloWorld {

    public void hello(String name) {
        System.out.println("Hello, " + name);
    }

    public void hello(int age) {
        if (age < 20) {
            System.out.println("Hello son");
        } else if (age >= 20 && age < 40) {
            System.out.println("Hello buddy");
        } else if (age >= 40) {
            System.out.println("Hello old man");
        } else {
            System.out.println("Hello, you are " + age + " years old");
        }
    }

    /**
     * 声明该方法会导致编译时报错
     * error: method hello(int) is already defined in class HelloWorld
     * 返回值类型不是签名的组成部分
     */
//    public int hello(int age) {
//        return age;
//    }

    public static void main(String[] args) {
        System.out.println("Hello, World!");
        HelloWorld hw = new HelloWorld();
        hw.hello("Tom");
        hw.hello(60);
    }
}
输出结果
Hello, World!
Hello, Tom
Hello old man

```


可以看到HelloWorld类定义了两个public void hello方法，这在PHP中是不允许的。
这涉及到“方法签名”的概念，在Java类中，相同名称但签名不同的方法是可以共存的。
方法名和方法参数类型列表构成了一个方法的签名，上面例子中两个hello方法的签名分别是

- hello(String)
- hello(int)



**作业**

- 编写一个Java类，实现多个构造方法。



## 内部类
看以下栗子。
咦！HelloWorld类里面怎么多了一个Person类！不要惊慌，这就是接下来要说的“内部类”。
```java
public class HelloWorld {

    class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String toString() {
            return name + ", " + age + " years old";
        }
    }

    public void hello(Person person) {
        System.out.println("Hello, " + person);
    }

    public void helloPerson(String name, int age) {
        Person person = new Person(name, age);
        hello(person);
    }

    public static void main(String[] args) {
        System.out.println("Hello, World!");
        HelloWorld hw = new HelloWorld();
        hw.helloPerson("Tom", 25);
    }
}
输出结果
Hello, World!
Hello, Tom, 25 years old
```


在一个Java类中定义另一个类，就形成了内部类。

- 内部类是**依赖**外部类的对象存在的
- 内部类可以**无条件访问**外部类的属性和方法

内部类允许我们更好地封装业务代码，当这些封装仅限于内部使用时。PHP则不支持内部类。


**作业**

- 尝试从外部类访问内部类的属性和方法
- 尝试在main方法中生成Person对象



## 注解与反射
注解的英文名称是 annotation，顾名思义，就是给代码加个标记。
举个例子，当你收到一封秘密信件，信封上写着“请务必亲手交给xxx”，你就知道了，这个信件是要转交给别人的，自己不能打开。注解的作用大致也是如此。
框架管理一个对象时，会检查对象的注解，例如上文中的UserDAO类，被@Mapper注解修饰，框架就知道这里定义的是CRUD方法，需要去xml配置文件中查找对应的SQL。
注解的检查工作是通过“反射”机制实现的，PHP也有反射机制，这里不再赘述。


# 难度Level 3
## 线程与多线程
直接上代码
```java
import java.util.ArrayList;

class ThreadDemo {
    class MyThread extends Thread {

        private int id;

        MyThread(int id) {
            this.id = id;
        }

        /**
         * 线程的业务逻辑
         */
        @Override
        public void run() {
            System.out.println("Thread #" + id);
        }
    }

    public void run(int num) {
        // 声明一个MyThread数组
        ArrayList<MyThread> threads = new ArrayList<MyThread>();
        for (int i=0; i<num; i++) {
            threads.add(new MyThread(i));
        }
        // Java版 foreach
        for (MyThread th : threads) {
            th.start();
        }
    }

    public static void main(String[] args) {
        ThreadDemo demo = new ThreadDemo();
        // 启动10个线程
        demo.run(10);
    }
}
输出
Thread #1
Thread #7
Thread #6
Thread #9
Thread #5
Thread #4
Thread #3
Thread #0
Thread #2
Thread #8
```


在main方法中启动10个MyThread线程。
从输出结果可以看出，多个线程的执行顺序是不固定的，这依赖于系统调度。


**多线程对于一些PHP开发者来说是一个全新的概念。PHP是单线程的，准确的说是一个进程内只包含一个线程。**
**对于Java来说，一个进程内可以运行多个线程。**
**可以把进程想象成“房屋”，线程就是房屋里的“住户”。多个人同住一个房屋时，就可能出现资源抢占的问题。例如大家需要排队用洗手间，排队用洗衣机等。**




## 线程安全
想象一下，大家在排队用洗衣机时，如果有人不守规则，把脏衣服混到别人正在洗的衣服里，那最后洗出来的衣服肯定是不干净的。用专业术语来说，就是产生了“脏数据”。
上代码
```java
import java.util.ArrayList;

class ThreadDemo1 {

    private int counter;

    public ThreadDemo1(int cnt) {
        counter = cnt;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    class MyThread extends Thread {

        private int id;

        MyThread(int id) {
            this.id = id;
        }

        /**
         * 线程的业务逻辑
         */
        @Override
        public void run() {
            System.out.println("Thread #" + id);
            setCounter(getCounter() + 1);
        }
    }

    public void run(int num) {
        ArrayList<MyThread> threads = new ArrayList<MyThread>();
        for (int i=0; i<num; i++) {
            threads.add(new MyThread(i));
        }
        // Java版 foreach
        for (MyThread th : threads) {
            th.start();
        }
        // join使主线程等待线程结束
        for (MyThread th : threads) {
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ThreadDemo1 demo1 = new ThreadDemo1(0);
        // 启动多个线程，计数器的值应等于线程数量，否则就有写冲突
        demo1.run(150);
        System.out.println("Counter final:" + demo1.getCounter());
    }
}
输出
... ...
Counter final:149
```
多试几次，总会出现计数器小于150的情况[捂脸]。如果你的电脑CPU只有一个核心，就不要试了[捂脸]。


**思考**

- 每个线程给计数器加1，为什么计数器最终的值会小于线程数量？



## 如何防止并发问题
PHP开发者也经常需要处理并发问题，但处理的往往是“进程级”的并发。
下面演示如何用Java语言的锁机制防止线程并发问题。
```java
class ThreadDemo1 {

    private int counter;
    private ReentrantLock lock;

    public ThreadDemo1(int cnt) {
        counter = cnt;
        lock = new ReentrantLock();
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    class MyThread extends Thread {

        private int id;

        MyThread(int id) {
            this.id = id;
        }

        /**
         * 线程的业务逻辑
         */
        @Override
        public void run() {
            // 加锁
            lock.lock();
            System.out.println("Thread #" + id);
            setCounter(getCounter() + 1);
            // 解锁
            lock.unlock();
        }
    }
    // 以下代码与上面的demo相同，省略
}
```


ReentrantLock对象的lock方法是阻塞的，当别的线程已经抢占了锁资源时，当前线程进入等待状态。
lock确保同一时间只能有一个线程对计数器加1，这就避免了并发写入的问题，确保计数器的值等于线程数量。


# 实践
## SpringBoot上手
使用Java做web开发，始终绕不开Spring框架。不像PHP有那么多web框架，像Laravel、Yii、ThinkPHP等等，Java开发者好像很容易达成一致，学Spring就对了。这也和Spring的本身就足够经典有关。
SpringBoot是Spring的增强版，降低了初学者学习Srping的成本。Spring本身需要的配置很多，SpringBoot可以认为是配置好的Spring。下面就开始上手吧。


首先需要引入依赖，配置文件如下（新增部分）
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.0.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.limengxiang</groupId>
    <artifactId>basics</artifactId>
    <version>1.0-SNAPSHOT</version>
    ... ... 

    <dependencies>
        <!-- springboot 相关依赖-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
				... ...
        
    </dependencies>

</project>
```


创建应用启动文件，MyApplication.java
```java
package com.limengxiang.basics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * SpringBootApplication注解告知框架这是启动类
 * ComponentScan注解告知框架需要扫描的组件位置
 * 这里告知框架扫描controller包下面的所有组件，确保HelloController被扫描
 */

@SpringBootApplication
@ComponentScan("com.limengxiang.basics.controller")
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class);
    }
}
```


创建Controller，HelloController.java
```java
package com.limengxiang.basics.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * RestController注解告知框架这是一个提供Restful API的组件
 */
@RestController
public class HelloController {

    /**
     * 框架自动获取请求参数传给方法
     * name参数必传
     * RequestMapping是路由映射注解，可以指定请求路径和请求方法
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(@RequestParam("name") String name) {
        return "Hello, " + name;
    }

    /**
     * 手动从request对象中获取参数
     * 框架自动解决依赖，注入request对象
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/params", method = RequestMethod.POST)
    public Map params(HttpServletRequest request) {
        // 获取一个参数
        String name = request.getParameter("name");
        System.out.println("param name:" + name);
        // 获取所有参数
        Map paramMap = request.getParameterMap();
        System.out.println("param map:" + paramMap);

        return paramMap;
    }
}
```


创建应用配置文件 application.yml，我们使用了mybatis，需要在这里配置连接信息。
这里可以随便写，只要有这几项就OK。
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/read_data?useUnicode=true&characterEncoding=UTF-8&useSSL=false
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
```


打开启动文件，运行MyApplication
```
2020-04-13 15:30:52.815  INFO 13600 --- [           main] com.limengxiang.basics.MyApplication     : Starting MyApplication on limengxiang-ubt with PID 13600 (/home/limengxiang/workplace/java/basics/target/classes started by limengxiang in /home/limengxiang/workplace/java/basics)
2020-04-13 15:30:52.820  INFO 13600 --- [           main] com.limengxiang.basics.MyApplication     : No active profile set, falling back to default profiles: default
2020-04-13 15:30:55.181  INFO 13600 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2020-04-13 15:30:55.229  INFO 13600 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2020-04-13 15:30:55.229  INFO 13600 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.27]
2020-04-13 15:30:55.441  INFO 13600 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2020-04-13 15:30:55.441  INFO 13600 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 2548 ms
2020-04-13 15:30:55.623  INFO 13600 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2020-04-13 15:30:55.996  INFO 13600 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2020-04-13 15:30:55.998  INFO 13600 --- [           main] com.limengxiang.basics.MyApplication     : Started MyApplication in 5.192 seconds (JVM running for 6.485)
```


用浏览器访问 [http://127.0.0.1:8080/hello?name=Tom](http://127.0.0.1:8080/hello?name=Tom)
看到“Hello, Tom”就说明成功了。


用curl模拟POST请求，curl -X POST -d 'name=Tom&age=22' 'http://127.0.0.1:8080/params'，返回
```
{"name":["Tom"],"age":["22"]}
```


**作业**

- 结合mybatis demo，尝试在API中操作数据库



## 单元测试
PHP和Java都需要写单元测试，这是个好习惯。我们以UserDAO为例。
打开UserDAO.java，光标定位到类名，按下Alt + Enter键，在弹出的选项中选择“Create test”，勾选需要测试的方法。
![image.png](https://cdn.nlark.com/yuque/0/2020/png/514450/1586764551457-046fded5-1646-4716-b744-cca0e1ed3e4e.png#align=left&display=inline&height=304&name=image.png&originHeight=607&originWidth=994&size=77812&status=done&style=none&width=497)


点“OK”，自动生成测试文件模板，自己填充测试逻辑即可。
```java
package com.limengxiang.basics.dao;

import com.limengxiang.basics.model.UserModel;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private UserDAO dao;

    @BeforeEach
    void setUp() throws IOException {
        if (dao == null) {
            InputStream stream = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSession session = new SqlSessionFactoryBuilder().build(stream).openSession(true);
            dao = session.getMapper(UserDAO.class);
        }
    }

    @Test
    void insert() {
        // 插入数据
        Integer inserted = dao.insert("allen", "13356986723");
        assertEquals(inserted, 1);
    }

    @Test
    void selectOne() {
        UserModel user = dao.selectOne("users", 1);
        System.out.println("Select one:" + user);
    }

    @Test
    void fuzzySearch() {
        List<UserModel> users = dao.fuzzySearch("all%", "");
        System.out.println("Fuzzy search by username:" + users);
    }

    @Test
    void searchByUsernameOrMobile() {
        List<UserModel> users;
        users = dao.searchByUsernameOrMobile("all%", "135%");
        System.out.println("Search by username:" + users);

        users = dao.searchByUsernameOrMobile("", "135%");
        System.out.println("Search by mobile:" + users);
    }
}
```


运行单元测试
![image.png](https://cdn.nlark.com/yuque/0/2020/png/514450/1586764720930-76c4bb0b-3255-4c88-a531-da6bd7f05a31.png#align=left&display=inline&height=138&name=image.png&originHeight=276&originWidth=978&size=45298&status=done&style=none&width=489)


# 总结
恭喜你成为了Java入门级开发者。
由于作者水平有限，只能引领你走到这里。以后的路就靠你自己了。加油。
