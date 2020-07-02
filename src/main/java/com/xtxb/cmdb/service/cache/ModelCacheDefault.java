package com.xtxb.cmdb.service.cache;

import com.xtxb.cmdb.common.model.ModelClass;
import com.xtxb.cmdb.service.dao.ModelDBFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月28日-上午9:25
 * <p>
 * <p>
 * 以HashMap模式管理的缓存，不具备集群化能力
 */
@Lazy
@Component("defaultCache")
public class ModelCacheDefault implements ModelCache {
    @Autowired
    private ModelDBFactory dbFactory;

    private HashMap<String, ModelClass> enNameMap=new HashMap<>();
    private boolean isInitialized=false;

    /**
     * 负责初始化缓存
     */
    @Override
    public synchronized  void initCache() {
        if(isInitialized)
            return;
        for (ModelClass modelClass : dbFactory.getInstance().getModel()) {
            enNameMap.put(modelClass.getName(),modelClass);
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
}
