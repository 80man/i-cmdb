package com.xtxb.cmdb.conf.local;

import com.xtxb.cmdb.conf.ConfAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月24日-下午4:48
 * <p>
 * <p>
 * 读取本地配置文件的API
 */
@Lazy
@Component("local_conf")
public class LocalConf implements ConfAPI {
    @Autowired
    private Environment env;

    @Override
    public String getConf(String key) {
        return env.getProperty(key);
    }
}
