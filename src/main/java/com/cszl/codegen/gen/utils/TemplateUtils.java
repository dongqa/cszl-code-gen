package com.cszl.codegen.gen.utils;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.cszl.codegen.gen.config.GenField;
import com.cszl.codegen.gen.enums.GenFileType;

/**
 * 模板工具
 * create by wdq on 2021/4/10 11:48
 */
public class TemplateUtils {

    private final static String TP_BASE_PATH = "templates";

    public final static String FILE_SEPARATOR = System.getProperty("file.separator");

    /**
     * 获取模板文件相对路径
     * @param genFileType
     * @return
     */
    public static String getTemplatePath(GenFileType genFileType) {
        Assert.notNull(genFileType, "文件类型不能为空");
        String s = genFileType.name().replace("_", ".");
        return TP_BASE_PATH + FILE_SEPARATOR + s + ".vm";

    }

    /**
     * 生成clickhouse xml条件表达式
     * @param c 属性对象
     * @return xml条件表达式
     */
    public static String genChMapperXmlCondition(GenField c) {
        /*
             <if test="m.skuSpec != null and m.skuSpec != ''">
            and skuSpec = #{m.skuSpec}
        </if>
         */
        StringBuilder xml = new StringBuilder();
        switch (c.getColumnType()) {
            case "Int8":
            case "Int16":
            case "Int32":
            case "Int64":
            case "Int128":
            case "UInt8":
            case "UInt16":
            case "UInt32":
            case "UInt64":
            case "UInt128":
            case "Integer":
            case "Long":
            case "Byte":
            case "Short":
            case "Double":
                xml.append("<if test=\"m.").append(c.getPropertyName()).append("!=null\">").append("\n and ").append(c.getColumnName())
                        .append(" = #{m.").append(c.getPropertyName()).append("}\n").append("</if>");
                break;
            case "String":
                xml.append("<if test=\"m.").append(c.getPropertyName()).append("!=null and m.")
                        .append(c.getPropertyName()).append("!= ''\">").append("\n and ").append(c.getColumnName())
                        .append(" = #{m.").append(c.getPropertyName()).append("}\n").append("</if>");
                break;
            default:
                break;
        }

        return xml.toString();
    }

}
