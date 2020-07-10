package com.xtxb.cmdb.service.impl;

import com.xtxb.cmdb.common.model.ModelClass;
import com.xtxb.cmdb.common.model.Property;
import com.xtxb.cmdb.common.model.RelationShip;
import com.xtxb.cmdb.service.ModelService;
import com.xtxb.cmdb.service.data.CacheFactory;
import com.xtxb.cmdb.service.data.DBFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月28日-上午9:19
 * <p>
 * <p>
 * 默认实现的资源类型管理服务类
 */
@Component
public class ModelServiceDefaultImpl implements ModelService {

    @Autowired
    private CacheFactory cacheFactory;

    @Autowired
    private DBFactory dbFactory;
    /**
     * 根据资源类型的名称（英文名称），查询类型定义信息
     *
     * @param name 资源类型的名称（英文名称）
     * @return
     */
    @Override
    public ModelClass getModelByName(String name) {
        return cacheFactory.getModelInstance().getModelByName(name);
    }

    /**
     * 根据资源类型的名称（中文名称），查询类型定义信息
     *
     * @param descr 类型的名称（中文名称）
     * @return
     */
    @Override
    public ModelClass getModelByDescr(String descr) {
        return cacheFactory.getModelInstance().getModelByDescr(descr);
    }

    /**
     * 查询所有的资源类型
     *
     * @return
     */
    @Override
    public List<ModelClass> getModels() {
        return cacheFactory.getModelInstance().getModels();
    }

    /**
     * 更新资源类型
     *
     * @param modelClass
     * @return
     * @throws Exception
     */
    @Override
    public boolean updateModel(ModelClass modelClass) throws Exception {
        return dbFactory.getInstanceModel().updateModel(modelClass)
                && cacheFactory.getModelInstance().updateModel(modelClass);
    }

    /**
     * 添加资源类型
     *
     * @param modelClass
     * @return
     * @throws Exception
     */
    @Override
    public boolean addModel(ModelClass modelClass) throws Exception {
        return dbFactory.getInstanceModel().addModel(modelClass)
                && cacheFactory.getModelInstance().addModel(modelClass);
    }

    /**
     * 删除资源类型，同时会删除相关的属性。当试图删除具有子类型或实例资源的资源类型时会抛出异常
     *
     * @param name
     * @return
     */
    @Override
    public boolean deleteModel(String name) throws Exception {
        return dbFactory.getInstanceModel().deleteModel(name)
                && cacheFactory.getModelInstance().deleteModel(name);
    }

    /**
     * 查询资源属性，根据资源类型和属性名称查询
     *
     * @param modelName 资源类型的名称（英文名称）
     * @param name      属性名称 （英文名称）
     * @return
     */
    @Override
    public Property getProperty(String modelName, String name) {
        return cacheFactory.getModelInstance().getProperty(modelName,name);
    }

    /**
     * 查询资源属性，根据资源类型查询
     *
     * @param modelName 资源类型的名称（英文名称）
     * @return
     */
    @Override
    public List<Property> getProperties(String modelName) {
        return cacheFactory.getModelInstance().getProperties(modelName);
    }

    /**
     * 批量更新资源属性
     *
     * @param property 资源属性数组
     * @return
     * @throws Exception
     */
    @Override
    public boolean updateProperty(Property... property) throws Exception {
        return updateProperty(Arrays.asList(property));
    }

    /**
     * 批量更新资源属性
     *
     * @param propertys 资源属性集合
     * @return
     * @throws Exception
     */
    @Override
    public boolean updateProperty(List<Property> propertys) throws Exception {
        return dbFactory.getInstanceModel().updateProperty(propertys) &&
                cacheFactory.getModelInstance().updateProperty(propertys);
    }

    /**
     * 添加资源属性
     *
     * @param property 资源属性数组
     * @return
     * @throws Exception
     */
    @Override
    public boolean addProperty(Property... property) throws Exception {
        return addProperty(Arrays.asList(property));
    }

    /**
     * 添加资源属性
     *
     * @param propertys 资源属性集合
     * @return
     * @throws Exception
     */
    @Override
    public boolean addProperty(List<Property> propertys) throws Exception {
        return dbFactory.getInstanceModel().addProperty(propertys) &&
                cacheFactory.getModelInstance().addProperty(propertys);
    }

    /**
     * 删除属性定义
     *
     * @param property
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteProperties(Property... property) throws Exception {
        return deleteProperties(Arrays.asList(property));
    }

    /**
     * 删除属性定义
     *
     * @param propertys
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteProperties(List<Property> propertys) throws Exception {
        return dbFactory.getInstanceModel().deleteProperties(propertys) &&
                cacheFactory.getModelInstance().deleteProperties(propertys);
    }

    /**
     * 查询关系类型定义
     *
     * @param name 关系类型的英文名称
     * @return
     */
    @Override
    public RelationShip getRelationShip(String name) {
        return cacheFactory.getModelInstance().getRelationShip(name);
    }

    /**
     * 查询关系类型定义，根据关联的资源类型进行查询
     *
     * @param modelName 资源类型的名称（英文名称）
     * @param isSource  指定的（modelName）是否是源端
     * @return
     */
    @Override
    public List<RelationShip> getRelationShips(String modelName, boolean isSource) {
        return cacheFactory.getModelInstance().getRelationShips(modelName,isSource);
    }

    /**
     * 更新关系类型定义
     *
     * @param relationShip
     * @return
     * @throws Exception
     */
    @Override
    public boolean updateRelationShip(RelationShip relationShip) throws Exception {
        return cacheFactory.getModelInstance().updateRelationShip(relationShip);
    }

    /**
     * 添加关系类型定义
     *
     * @param relationShip
     * @return
     * @throws Exception
     */
    @Override
    public boolean addRelationShip(RelationShip relationShip) throws Exception {
        return cacheFactory.getModelInstance().addRelationShip(relationShip);
    }

    /**
     * 删除关系模型，当试图删除具有实例资源的关系模型时，会抛出异常
     *
     * @param name 关系类型
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteRelationShip(String name) throws Exception {
        return cacheFactory.getModelInstance().deleteRelationShip(name);
    }
}
