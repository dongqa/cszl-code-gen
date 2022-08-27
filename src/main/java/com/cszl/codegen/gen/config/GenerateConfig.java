package com.cszl.codegen.gen.config;

import com.cszl.codegen.base.enums.DataBaseType;
import com.cszl.codegen.base.enums.NamingStrategy;
import com.cszl.codegen.base.utils.StringUtils;
import com.cszl.codegen.gen.enums.GenFileType;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * 全局配置
 * create by wdq on 2021/4/9 17:36
 */
@Data
public class GenerateConfig {

    private String basePackage = "com.cszl";

    /**
     * 作者
     */
    private String author = "wdq";

    /**
     * 邮箱
     */
    private String email = "a13123003060@gmail.com";

    /**
     * 时间
     */
    private String datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    /**
     * 是否开启二级缓存
     */
    private Boolean enableCache = false;

    /**
     * 是否注释
     */
    private Boolean enableComment = false;

    /**
     * 命名策略
     */
    private NamingStrategy namingStrategy = NamingStrategy.underline_to_camel;


    /**
     * 生成的字段
     */
    private List<GenField> genFieldList;

    /**
     * 是否有bigdecimal类型
     */
    private boolean hasBigDeciaml;

    /**
     * 创建的文件类型
     */
    private List<GenFileType> genFileTypes = Arrays.asList(GenFileType.values());

    /**
     * 需要创建的方法
     */
    private List<GenMethod> genMethodList;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表注释
     */
    private String tableComment;

    /**
     * 模块名
     */
    private String module;

    /**
     * 类主关键词名
     */
    private String classMainName = "App";

    /**
     * 数据库类型
     */
    private DataBaseType dataBaseType;

    /**
     * 包路径
     */
    private String packagePath;

    private String entityPath;

    private String requestPath;

    private String controllerPath;

    private String servicePath;

    private String serviceImplPath;

    private String mapperPath;

    private String mapperXmlPath;

    private String entityName;

    private String requestName;

    private String controllerName;

    private String serviceName;

    private String serviceImplName;

    private String mapperName;

    private String mapperXmlName;

    private String entityFullName;

    private String requestFullName;

    private String controllerFullName;

    private String serviceFullName;

    private String serviceImplFullName;

    private String mapperFullName;

    private String mapperXmlFullName;


    public String getPackagePath() {
        return StringUtils.isEmpty(this.module) ? basePackage + "." + this.getClassMainName().toLowerCase() : this.module;
    }

    public String getEntityPath() {
        return getPackagePath() + ".entity";
    }

    public String getRequestPath() {
        return getPackagePath() + ".request";
    }

    public String getControllerPath() {
        return getPackagePath() + ".controller";
    }

    public String getServicePath() {
        return getPackagePath() + ".service";
    }

    public String getServiceImplPath() {
        return getServicePath() + ".impl";
    }

    public String getMapperPath() {
        return getPackagePath() + ".mapper";
    }

    public String getMapperXmlPath() {
        return getPackagePath() + ".xml";
    }

    public String getEntityName() {
        return classMainName;
    }

    public String getRequestName() {
        return classMainName + "Request";
    }

    public String getControllerName() {
        return classMainName + "Controller";
    }

    public String getServiceName() {
        return "I" + classMainName + "Service";
    }

    public String getServiceImplName() {
        return classMainName + "ServiceImpl";
    }

    public String getMapperName() {
        return classMainName + "Mapper";
    }

    public String getMapperXmlName() {
        return classMainName + "Mapper";
    }

    public String getEntityFullName() {
        return getEntityPath() + "." + getEntityName();
    }

    public String getRequestFullName() {
        return getRequestPath() + "." + getRequestName();
    }

    public String getControllerFullName() {
        return getControllerPath() + "." + getControllerName();
    }

    public String getServiceFullName() {
        return getServicePath() + "." + getServiceName();
    }

    public String getServiceImplFullName() {
        return getServiceImplPath() + "." + getServiceImplName();
    }

    public String getMapperFullName() {
        return getMapperPath() + "." + getMapperName();
    }

    public String getMapperXmlFullName() {
        return getMapperXmlPath() + "." + getMapperXmlName();
    }

}
