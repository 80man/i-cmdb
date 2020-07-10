package com.xtxb.cmdb.service.data;

import com.xtxb.cmdb.common.value.Resource;
import com.xtxb.cmdb.service.data.cache.ModelCache;
import com.xtxb.cmdb.service.data.cache.ResourceCache;
import com.xtxb.cmdb.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月28日-上午9:34
 * <p>
 * <p>
 * 负责根据配置声明的类型获取资源类型缓存
 */
@Component
public class CacheFactory {
    @Value("${cmdb.cacha.model.name}")
    private String cacheModelName;

    @Value("${cmdb.cacha.resource.name}")
    private String cacheResourceName;

    @Autowired
    private SpringContextUtil beanUtil;

    private ModelCache modelCache;

    private ResourceCache resCache;

    public ModelCache getModelInstance(){
        if(modelCache==null) {
            modelCache = (ModelCache) beanUtil.getBean(cacheModelName);
            modelCache.init();
        }
        return modelCache;
    }

    public ResourceCache getResourceInstance(){
        if(resCache==null) {
            resCache = (ResourceCache) beanUtil.getBean(cacheResourceName);
            resCache.init();
        }
        return resCache;
    }
}
