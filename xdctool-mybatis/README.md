# ORM(Mybatis) 模块

## 1. 使用方式
### 1.1 引入依赖
```xml
<dependencies>
    <dependency>
        <groupId>com.xdcplus</groupId>
        <artifactId>xdctool-mybatis</artifactId>
        <version>${latestversion}</version>
    </dependency>
</dependencies>
```

### 1.2 条件查询
#### 1.2.1 引入依赖
```xml
<!-- 条件查询封装 -->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper-spring-boot-starter</artifactId>
    <scope>provided</scope>
</dependency>
```

### 1.3 数据库逆向生成
```java
public static void main(String[] args) {
        String database = "xdcweb";
        String username = "root";
        String password = "123456";
        String host = "localhost";
        String port = "3306";
        String author = "Rong.Jia";
        String tablePrefix = "xdc_t_";

        com.xdcplus.mp.generator.CodeGenerator.autoGenerator(database, username, password,host ,port, author, tablePrefix);


    }

```











