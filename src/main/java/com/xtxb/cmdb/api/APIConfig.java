package com.xtxb.cmdb.api;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月27日-下午5:52
 * <p>
 * <p>
 *
 */
@Configuration
public class APIConfig extends ResourceConfig {
    public APIConfig(){
        register(ResourceAPI.class);
        register(LinkAPI.class);
        register(ModelAPI.class);
    }
}
