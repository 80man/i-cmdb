package com.xtxb.cmdb.service.dao;

import com.xtxb.cmdb.common.model.ModelClass;
import com.xtxb.cmdb.common.model.Property;
import com.xtxb.cmdb.common.model.RelationShip;

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


    /**
     * 查询资源属性，根据资源类型查询
     * @param modelName 资源类型的名称（英文名称）
     * @return
     */
    public List<Property> getProperties(String modelName);


    /**
     * 批量更新资源属性
     * @param propertys 资源属性集合
     * @return
     * @throws Exception
     */
    public boolean updateProperty(List<Property> propertys) throws Exception;

    /**
     * 添加资源属性
     * @param propertys 资源属性集合
     * @return
     * @throws Exception
     */
    public boolean addProperty(List<Property> propertys) throws Exception;

    /**
     * 删除属性定义
     * @param property
     * @return
     * @throws Exception
     */
    public boolean deleteProperties(List<Property> property) throws Exception;


    /**
     * 查询关系类型定义
     * @return
     */
    public List<RelationShip> getRelationShip();

    /**
     * 更新关系类型定义
     * @param relationShip
     * @return
     * @throws Exception
     */
    public boolean updateRelationShip(RelationShip relationShip) throws Exception;

    /**
     * 添加关系类型定义
     * @param relationShip
     * @return
     * @throws Exception
     */
    public boolean addRelationShip(RelationShip relationShip) throws Exception;

    /**
     * 删除关系模型，当试图删除具有实例资源的关系模型时，会抛出异常
     * @param name
     * @return
     * @throws Exception
     */
    public boolean deleteRelationShip(String name) throws Exception;

}
