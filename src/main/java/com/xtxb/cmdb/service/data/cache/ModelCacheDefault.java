package com.xtxb.cmdb.service.data.cache;

import com.xtxb.cmdb.common.model.ModelClass;
import com.xtxb.cmdb.common.model.Property;
import com.xtxb.cmdb.common.model.RelationShip;
import com.xtxb.cmdb.service.data.DBFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月28日-上午9:25
 * <p>
 * <p>
 * 以HashMap模式管理的缓存，不具备集群化能力
 */
@Lazy
@Component("defaultModelCache")
public class ModelCacheDefault implements ModelCache {
    @Autowired
    private DBFactory dbFactory;

    /*资源类型缓存*/
    private HashMap<String, ModelClass> enNameMap=new HashMap<>();
    /*资源属性缓存*/
    private HashMap<String, Map<String,Property>> propertiesMap=new HashMap<>();
    /*资源关系定义缓存*/
    private HashMap<String ,RelationShip> relshipHashMap=new HashMap<>();

    private boolean isInitialized=false;

    /**
     * 负责初始化缓存
     */
    @Override
    public synchronized  void init() {
        if(isInitialized)
            return;
        //资源类型缓存
        for (ModelClass modelClass : dbFactory.getInstanceModel().getModel()) {
            enNameMap.put(modelClass.getName(),modelClass);
        }

        //资源属性缓存
        for (Iterator<String> iterator = enNameMap.keySet().iterator(); iterator.hasNext(); ) {
            String modelName =  iterator.next();
            Map temp=new HashMap();
            for(Property property: dbFactory.getInstanceModel().getProperties(modelName)){
                temp.put(property.getName(),property);
            }
            propertiesMap.put(modelName,temp);
        }

        //资源关系定义缓存
        List<RelationShip> rlist=dbFactory.getInstanceModel().getRelationShip();
        for(RelationShip rs:rlist){
            relshipHashMap.put(rs.getName(),rs);
        }
        isInitialized=true;
    }

    /**
     * 查询所有的资源类型
     *
     * @return
     */
    @Override
    public List<ModelClass> getModels() {
        return new ArrayList<>(enNameMap.values());
    }

    /**
     * 根据资源类型的名称（英文名称），查询类型定义信息
     *
     * @param name 资源类型的名称（英文名称）
     * @return
     */
    @Override
    public ModelClass getModelByName(String name) {
        return enNameMap.get(name);
    }

    /**
     * 根据资源类型的名称（中文名称），查询类型定义信息
     *
     * @param descr 类型的名称（中文名称）
     * @return
     */
    @Override
    public ModelClass getModelByDescr(String descr) {
        for (Iterator<ModelClass> iterator = enNameMap.values().iterator(); iterator.hasNext(); ) {
            ModelClass mc =  iterator.next();
            if(descr.equals(mc.getDescr()))
                return mc;
        }
        return null;
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
        if(enNameMap.containsKey(modelClass.getName())) {
            enNameMap.put(modelClass.getName(), modelClass);
            return true;
        }else
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
        enNameMap.put(modelClass.getName(), modelClass);
        return true;
    }

    /**
     * 删除资源类型，同时会删除相关的属性。当试图删除具有子类型或实例资源的资源类型时会抛出异常
     *
     * @param name
     * @return
     */
    @Override
    public synchronized boolean deleteModel(String name) throws Exception {
        return enNameMap.remove(name)!=null;
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
        if(propertiesMap.get(modelName)!=null)
            return propertiesMap.get(modelName).get(name);
        else
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
        if(propertiesMap.get(modelName)!=null)
            return new ArrayList(propertiesMap.get(modelName).values());
        else
            return null;
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
        List<Property> oldProperties=new ArrayList<>(propertys.size());
        boolean updateSuc=true;
        for(Property property: propertys){
            if(!propertiesMap.containsKey(property.getModelName()) || !propertiesMap.get(property.getModelName()).containsKey(property.getName())){
                updateSuc=false;
                break;
            }
            oldProperties.add(propertiesMap.get(property.getModelName()).put(property.getName(),property));
        }
        if(!updateSuc){
            for(Property property: oldProperties){
                propertiesMap.get(property.getModelName()).put(property.getName(),property);
            }
        }
        return updateSuc;
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
        List<Property> addeds=new ArrayList<>(propertys.size());
        boolean addSuc=true;
        for(Property property: propertys){
            if(propertiesMap.containsKey(property.getModelName()) && propertiesMap.get(property.getModelName()).get(property.getName())!=null){
                addSuc=false;
                break;
            }
            if(!propertiesMap.containsKey(property.getModelName())){
                propertiesMap.put(property.getModelName(),new HashMap<>());
            }
            propertiesMap.get(property.getModelName()).put(property.getName(),property);
        }
        if(!addSuc){
            for(Property property: addeds){
                propertiesMap.get(property.getModelName()).remove(property.getName());
            }
        }
        return addSuc;
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
        for(Property property: propertys){
            if(propertiesMap.containsKey(property.getModelName())){
                propertiesMap.get(property.getModelName()).remove(property.getName());
            }
        }
        return true;
    }


    /**
     * 查询关系类型定义
     *
     * @param name 关系类型的英文名称
     * @return
     */
    @Override
    public RelationShip getRelationShip(String name) {
        return relshipHashMap.get(name);
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
        List<RelationShip> list=new ArrayList<>();
        for (Iterator<RelationShip> iterator = relshipHashMap.values().iterator(); iterator.hasNext(); ) {
            RelationShip next =  iterator.next();
            if(isSource && next.getSourceModel().equals(modelName))
                list.add(next);
            else if(!isSource && next.getTargetModel().equals(modelName))
                list.add(next);
        }
        return list;
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
        if(relshipHashMap.containsKey(relationShip.getName())) {
            relshipHashMap.put(relationShip.getName(), relationShip);
            return true;
        }else
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
        if(!relshipHashMap.containsKey(relationShip.getName())) {
            relshipHashMap.put(relationShip.getName(), relationShip);
            return true;
        }else
            return false;
    }

    /**
     * 删除关系模型，当试图删除具有实例资源的关系模型时，会抛出异常
     *
     * @param name
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteRelationShip(String name) throws Exception {
        if(relshipHashMap.containsKey(name)) {
            relshipHashMap.remove(name);
            return true;
        }else
            return false;
    }
}
