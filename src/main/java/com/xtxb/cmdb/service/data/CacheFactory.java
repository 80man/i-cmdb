package com.xtxb.cmdb.service.data;

import com.xtxb.cmdb.service.data.cache.ModelCache;
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
    private String cacheName;
    private byte[] lock=new byte[1];

    @Autowired
    private SpringContextUtil beanUtil;

    private ModelCache cache;

    public ModelCache getInstance(){
        if(cache==null) {
            cache = (ModelCache) beanUtil.getBean(cacheName);
            cache.initCache();
        }
        return cache;
    }
}
