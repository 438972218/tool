# 工具包版本控制模块

## 1. 使用方式
### 1.1 引入依赖
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.xdcplus</groupId>
            <artifactId>xdctool-dependencies</artifactId>
            <version>${latestversion}</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>com.xdcplus</groupId>
        <artifactId>xdctool-dependencies</artifactId>
    </dependency>
</dependencies>
```















