package com.xtxb.cmdb.service.impl;

import com.xtxb.cmdb.common.model.ModelClass;
import com.xtxb.cmdb.common.model.Property;
import com.xtxb.cmdb.common.model.RelationShip;
import com.xtxb.cmdb.service.ModelService;
import com.xtxb.cmdb.service.cache.ModelCacheFactory;
import com.xtxb.cmdb.service.dao.ModelDBFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private ModelCacheFactory cacheFactory;

    @Autowired
    private ModelDBFactory dbFactory;
    /**
     * 根据资源类型的名称（英文名称），查询类型定义信息
     *
     * @param name 资源类型的名称（英文名称）
     * @return
     */
    @Override
    public ModelClass getModelByName(String name) {
        return cacheFactory.getInstance().getModelByName(name);
    }

    /**
     * 根据资源类型的名称（中文名称），查询类型定义信息
     *
     * @param descr 类型的名称（中文名称）
     * @return
     */
    @Override
    public ModelClass getModelByDescr(String descr) {
        return cacheFactory.getInstance().getModelByDescr(descr);
    }

    /**
     * 查询所有的资源类型
     *
     * @return
     */
    @Override
    public List<ModelClass> getModels() {
        return cacheFactory.getInstance().getModels();
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
        ModelClass oldModel=cacheFactory.getInstance().getModelByName(modelClass.getName());
        if(cacheFactory.getInstance().updateModel(modelClass) ){
            if(dbFactory.getInstance().updateModel(modelClass))
                return true;
            else
                cacheFactory.getInstance().updateModel(oldModel);
        }
        return false;
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
        if(cacheFactory.getInstance().addModel(modelClass) ){
            if(dbFactory.getInstance().addModel(modelClass))
                return true;
            else
                cacheFactory.getInstance().deleteModel(modelClass.getName());
        }
        return false;
    }

    /**
     * 删除资源类型，同时会删除相关的属性。当试图删除具有子类型或实例资源的资源类型时会抛出异常
     *
     * @param name
     * @return
     */
    @Override
    public boolean deleteModel(String name) throws Exception {
        ModelClass oldModel=cacheFactory.getInstance().getModelByName(name);
        if(cacheFactory.getInstance().deleteModel(name) ){
            if(dbFactory.getInstance().deleteModel(name))
                return true;
            else
                cacheFactory.getInstance().addModel(oldModel);
        }
        return false;
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
        return null;
    }

    /**
     * 查询资源属性，根据资源类型查询
     *
     * @param modelName 资源类型的名称（英文名称）
     * @return
     */
    @Override
    public List<Property> getProperties(String modelName) {
        return null;
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
        return false;
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
        return false;
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
        return false;
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
        return false;
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
        return false;
    }

    /**
     * 删除属性定义
     *
     * @param property
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteProperties(List<Property> property) throws Exception {
        return false;
    }

    /**
     * 查询关系类型定义
     *
     * @param name 关系类型的英文名称
     * @return
     */
    @Override
    public RelationShip getRelationShip(String name) {
        return null;
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
        return null;
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
        return false;
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
        return false;
    }

    /**
     * 删除关系模型，当试图删除具有实例资源的关系模型时，会抛出异常
     *
     * @param type 关系类型
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteRelationShip(String type) throws Exception {
        return false;
    }
}
