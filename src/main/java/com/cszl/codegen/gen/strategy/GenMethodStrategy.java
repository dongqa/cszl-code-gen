package com.cszl.codegen.gen.strategy;

import com.cszl.codegen.base.enums.NamingStrategy;
import com.cszl.codegen.base.utils.StringUtils;
import com.cszl.codegen.gen.config.GenField;
import com.cszl.codegen.gen.config.GenQueryField;
import com.cszl.codegen.gen.config.GenerateConfig;
import com.cszl.codegen.gen.enums.GenFileType;
import com.cszl.codegen.gen.enums.QueryConditionRel;
import com.cszl.codegen.gen.enums.QueryRel;
import lombok.Data;

import java.util.List;
import java.util.Optional;

/**
 * 构建方法策略
 * create by wdq on 2021/4/12 9:40
 */
@Data
public abstract class GenMethodStrategy {

    /**
     * 配置
     */
    protected GenerateConfig generateConfig;

    /**
     * 文件类型
     */
    protected GenFileType genFileType;

    /**
     * 查询字段
     */
    protected List<? extends GenField> queryXX;

    /**
     * 更新字段
     */
    protected List<? extends GenField> updateXX;

    public GenMethodStrategy() {
    }

    public GenMethodStrategy(GenerateConfig generateConfig, GenFileType genFileType, List<GenField> queryXX, List<GenField> updateXX) {
        this.generateConfig = generateConfig;
        this.genFileType = genFileType;
        this.queryXX = queryXX;
        this.updateXX = updateXX;
    }

    /**
     * 构建方法，自己实现
     *
     * @return
     */
    public abstract String build();

    /**
     * 创建查询XX,用String分隔
     *
     * @param xx
     * @return
     */
    protected String genMethodNameParamsXXSeparator(List<? extends GenField> xx, String separator) {
        if (xx.size() == 0) {
            return "";
        }
        return xx.parallelStream().map(m -> NamingStrategy.capitalFirst(m.getPropertyName())).reduce((a, b) -> a + separator + b).get();
    }

    /**
     * 创建方法中的查询XX
     *
     * @param xx
     * @return
     */
    protected String genMethodNameParamsXX(List<? extends GenField> xx) {
        if (xx.size() == 0) {
            return "";
        }
        GenQueryField queryField = (GenQueryField) xx.get(0);
        return xx.parallelStream().map(m -> NamingStrategy.capitalFirst(m.getPropertyName())).reduce(queryField.getQueryConditionRel().concat).get();
    }

    /**
     * 创建方法中的更新XX
     *
     * @param xx
     * @return
     */
    protected String genMethodNameUpdateParamsXX(List<? extends GenField> xx) {
        if (xx.size() == 0) {
            return "";
        }
        return xx.parallelStream().map(m -> NamingStrategy.capitalFirst(m.getPropertyName())).reduce(StringUtils::concatByAnd).get();
    }

    /**
     * 创建方法参数xx
     *
     * @param xx
     * @return
     */
    protected String genMethodParamsXX(List<? extends GenField> xx) {
        if (xx.size() == 0) {
            return "";
        }
        return xx.parallelStream().map(m -> m.getPropertyType() + " " + m.getPropertyName()).reduce((a, b) -> a + " , " + b).get();
    }

    /**
     * 创建方法Lambda查询参数
     *
     * @param xx
     * @return
     */
    protected String genLambdaQueryParams(String entityName, List<? extends GenField> xx) {
        if (xx.size() == 0) {
            return ")";
        }
        GenQueryField queryField = (GenQueryField) xx.get(0);
        String or = QueryConditionRel.OR.equals(queryField.getQueryConditionRel()) ? ".or()" : "";
        String lambda = xx.parallelStream().map(m -> or + "." + ((GenQueryField) m).getQueryRel().d + "(" + entityName + "::get" + NamingStrategy.capitalFirst(m.getPropertyName()) + "," + "params.get" + NamingStrategy.capitalFirst(m.getPropertyName()) + "())").reduce((StringUtils::concat)).get();
        return lambda.substring(or.length(), lambda.length());
    }

    /**
     * 创建方法Lambda更新参数
     *
     * @param xx
     * @return
     */
    protected String genLambdaUpdateParams(String entityName, List<? extends GenField> xx) {
        if (xx.size() == 0) {
            return ")";
        }
        return xx.parallelStream().map(m -> ".set(" + entityName + "::get" + NamingStrategy.capitalFirst(m.getPropertyName()) + "," + "entity.get" + NamingStrategy.capitalFirst(m.getPropertyName()) + "())").reduce((StringUtils::concat)).get();
    }



    /**
     * 通过模板取构建代码
     *
     * @param tpl
     * @return
     */
    protected String genCodeByTpl(String tpl) {
        String entityName = generateConfig.getEntityName();
        return tpl.replace("{entityName}", entityName).
                replace("{requestName}", generateConfig.getRequestName()).
                replace("{byParams}", this.genMethodNameParamsXX(queryXX)).
                replace("{byParamsWithSeparator}", this.genMethodNameParamsXXSeparator(queryXX, ",")).
                replace("{updateParamsWithSeparator}", this.genMethodNameParamsXXSeparator(updateXX, ",")).
                replace("{updateParams}", this.genMethodNameUpdateParamsXX(updateXX)).
                replace("{methodParams}", this.genMethodParamsXX(queryXX))
                .replace("{lambdaQueryParams}", this.genLambdaQueryParams(entityName, queryXX))
                .replace("{lambdaUpdateParams}", this.genLambdaUpdateParams(entityName, updateXX))
                .replace("{tableComment}", Optional.ofNullable(generateConfig.getTableComment()).orElse(""))
                ;
    }
}
