package com.xtxb.cmdb.service.cache;

import com.xtxb.cmdb.common.model.ModelClass;
import com.xtxb.cmdb.common.model.Property;
import com.xtxb.cmdb.common.model.RelationShip;

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



    /***********************资源属性相关API*******************************/
    /**
     * 查询资源属性，根据资源类型和属性名称查询
     * @param modelName  资源类型的名称（英文名称）
     * @param name 属性名称 （英文名称）
     * @return
     */
    public Property getProperty(String modelName, String name);

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
     * @param name 关系类型的英文名称
     * @return
     */
    public RelationShip getRelationShip(String name);

    /**
     * 查询关系类型定义，根据关联的资源类型进行查询
     * @param modelName  资源类型的名称（英文名称）
     * @param isSource  指定的（modelName）是否是源端
     * @return
     */
    public List<RelationShip> getRelationShips(String modelName,boolean isSource);

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
