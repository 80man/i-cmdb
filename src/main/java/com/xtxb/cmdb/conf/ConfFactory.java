package com.xtxb.cmdb.conf;

import com.xtxb.cmdb.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月24日-下午5:13
 * <p>
 * <p>
 *  根据配置中定义的配置类型取得对应的配置类
 */
@Component
public class ConfFactory {
    private ConfAPI api;

    @Autowired
    private SpringContextUtil util;

    @Value("${cmdb.config.source}")
    private String congType;

    public ConfAPI getConfAPI(){
        if(api==null) {
            if ("local".equals(congType))
                api = (ConfAPI)util.getBean("local_conf");
            else
                api = (ConfAPI)util.getBean("remote_conf");
        }
        return api;
    }
}
