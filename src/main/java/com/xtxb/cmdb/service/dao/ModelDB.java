package com.xtxb.cmdb.service.dao;

import com.xtxb.cmdb.common.model.ModelClass;

import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月28日-下午2:16
 * <p>
 * <p>
 * 资源类型的数据库持久化操作接口
 */
public interface ModelDB {

    /***********************资源类型相关API*******************************/
    /**
     * 获取资源类型
     * @return
     */
    public List<ModelClass> getModel();

    /**
     * 更新资源类型
     * @param modelClass
     * @return
     * @throws Exception
     */
    public boolean updateModel(ModelClass modelClass) throws Exception;

    /**
     * 添加资源类型
     * @param modelClass
     * @return
     * @throws Exception
     */
    public boolean addModel(ModelClass modelClass) throws Exception;

    /**
     * 删除资源类型，同时会删除相关的属性。当试图删除具有子类型或实例资源的资源类型时会抛出异常
     * @param name
     * @return
     */
    public boolean deleteModel(String name) throws Exception;
}
