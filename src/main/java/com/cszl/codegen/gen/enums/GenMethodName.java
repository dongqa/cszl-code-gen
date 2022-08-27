package com.cszl.codegen.gen.enums;

import com.cszl.codegen.gen.strategy.GenMethodNotSupport;
import com.cszl.codegen.gen.strategy.GenMethodStrategy;
import com.cszl.codegen.gen.strategy.methods.*;

/**
 * 创建的方法类型
 * create by wdq on 2021/4/10 11:01
 */
public enum GenMethodName {

    findOneByXX(GenFindOneByXXImpl.class),
    listByXX(GenListByXXImpl.class),
    pageByXX(GenPageByXXImpl.class),
    updateByXX(GenUpdateByXXImpl.class),
    updateXXByXX(GenUpdateXXByXXImpl.class),
    deleteByXX(GenDeleteByXXImpl.class),
    insertBatch(GenInsterBatchImpl.class),
    saveOrUpdateOneByXX(),
    batchSaveOrUpdateByXX();

    private Class<? extends GenMethodStrategy> clazz;


    GenMethodName() {
        this.clazz = GenMethodNotSupport.class;
    }

    GenMethodName(Class<? extends GenMethodStrategy> clazz) {
        this.clazz = clazz;
    }

    public GenMethodStrategy getInstance() {
//        if (this.clazz == GenMethodNotSupport.class) {
//            throw new RuntimeException(String.format("不支持的方法==%s", this.name()));
//        }
        try {
            return this.clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
