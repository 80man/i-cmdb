package com.xtxb.cmdb.service.cache;

import com.xtxb.cmdb.common.model.ModelClass;

import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月28日-上午9:21
 * <p>
 * <p>
 *     资源类型缓存
 */
public interface ModelCache {


    public  void initCache();

    /***********************资源类型相关API*******************************/
    /**
     * 根据资源类型的名称（英文名称），查询类型定义信息
     * @param name  资源类型的名称（英文名称）
     * @return
     */
    public ModelClass getModelByName(String name);

    /**
     * 根据资源类型的名称（中文名称），查询类型定义信息
     * @param descr 类型的名称（中文名称）
     * @return
     */
    public ModelClass getModelByDescr(String descr);

    /**
     * 查询所有的资源类型

     * @return
     */
    public List<ModelClass> getModels();

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
