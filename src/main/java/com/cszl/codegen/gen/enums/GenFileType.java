package com.cszl.codegen.gen.enums;

import com.cszl.codegen.gen.config.GenerateConfig;

import java.util.function.Function;

/**
 * 创建的文件类型
 * create by wdq on 2021/4/10 11:44
 */
public enum GenFileType {

    controller_java(GenerateConfig::getControllerPath, GenerateConfig::getControllerName, GenerateConfig::getControllerFullName),
    service_java(GenerateConfig::getServicePath, GenerateConfig::getServiceName, GenerateConfig::getServiceFullName),
    serviceImpl_java(GenerateConfig::getServiceImplPath, GenerateConfig::getServiceImplName, GenerateConfig::getServiceImplFullName),
    mapper_java(GenerateConfig::getMapperPath, GenerateConfig::getMapperName, GenerateConfig::getMapperFullName),
    entity_java(GenerateConfig::getEntityPath, GenerateConfig::getEntityName, GenerateConfig::getEntityFullName),
    request_java(GenerateConfig::getRequestPath, GenerateConfig::getRequestName, GenerateConfig::getRequestFullName),
    mapper_xml(GenerateConfig::getMapperXmlPath, GenerateConfig::getMapperXmlName, GenerateConfig::getMapperXmlFullName);

    public Function<GenerateConfig, String> pathFun;
    public Function<GenerateConfig, String> nameFun;
    public Function<GenerateConfig, String> fullNameFun;

    GenFileType(Function<GenerateConfig, String> pathFun, Function<GenerateConfig, String> nameFun, Function<GenerateConfig, String> fullNameFun) {
        this.pathFun = pathFun;
        this.nameFun = nameFun;
        this.fullNameFun = fullNameFun;
    }
}
