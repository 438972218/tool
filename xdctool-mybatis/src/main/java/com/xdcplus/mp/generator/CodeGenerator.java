package com.xdcplus.mp.generator;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 代码生成器
 *
 * @author Rong.Jia
 * @date 2021/04/29 11:22
 */
public class CodeGenerator {

    /**
     * 生成代码
     * @param database 数据库
     * @param username 用户名
     * @param password 密码
     * @param host IP
     * @param port 端口
     * @param author 作者
     */
    public static void autoGenerator(String database, String username,
                                     String password, String host, String port,
                                     String author) {
        autoGenerator(database, username, password, host, port, author, StrUtil.EMPTY);
    }

    /**
     * 生成代码
     * @param database 数据库
     * @param username 用户名
     * @param password 密码
     * @param host IP
     * @param port 端口
     * @param author 作者
     * @param tablePrefix 表前缀
     */
    public static void autoGenerator(String database, String username,
                                     String password, String host, String port,
                                     String author, String tablePrefix) {
        autoGenerator(database, username, password, host, port, author, tablePrefix, "generator");
    }

    /**
     * 生成代码
     * @param database 数据库
     * @param username 用户名
     * @param password 密码
     * @param host IP
     * @param port 端口
     * @param author 作者
     * @param tablePrefix 表前缀
     * @param parent 父包名
     */
    public static void autoGenerator(String database, String username,
                                     String password, String host, String port,
                                     String author, String tablePrefix, String parent) {
        autoGenerator(database, username, password, host, port, author, tablePrefix, parent, StrUtil.EMPTY);
    }

    /**
     * 生成代码
     * @param database 数据库
     * @param username 用户名
     * @param password 密码
     * @param host IP
     * @param port 端口
     * @param author 作者
     * @param tablePrefix 表前缀
     * @param parent 父包名
     * @param moduleName 模块名
     */
    public static void autoGenerator(String database, String username,
                                      String password, String host, String port,
                                     String author, String tablePrefix, String parent,
                                     String moduleName) {

        String url = "jdbc:mysql://"+ host +":"+port+"/"+ database +"?useUnicode=true&useSSL=false&characterEncoding=utf8";
        String projectPath = System.getProperty("user.dir") + "/src/main/java";

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> builder.author(author)
                        .fileOverride()
                        .disableOpenDir()
                        .outputDir(projectPath))
                .packageConfig(builder -> {
                    builder.parent(parent);
                    if (StrUtil.isNotBlank(moduleName)) builder.moduleName(moduleName);
                    builder.pathInfo(Collections.singletonMap(OutputFile.mapperXml, System.getProperty("user.dir") + "/src/main/resources/mappers/"));
                })
                .strategyConfig((scanner, builder) -> {
                    builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                            .addTablePrefix(tablePrefix)
                            .mapperBuilder()
                            .enableBaseColumnList()
                            .enableBaseResultMap()
                            .build();
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();

    }

    /**
     *  处理 all 情况
     * @param tables
     * @return
     */
    private static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }








}
