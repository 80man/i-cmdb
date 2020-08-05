package com.xtxb.cmdb.service.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年08月05日-下午2:52
 * <p>
 * <p>
 *
 */
@Component
public class PreLoad implements ApplicationRunner {
    @Autowired
    private CacheFactory cacheFactory;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        cacheFactory.getModelInstance();
        cacheFactory.getResourceInstance();
    }
}
