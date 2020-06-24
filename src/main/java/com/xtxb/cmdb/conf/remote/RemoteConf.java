package com.xtxb.cmdb.conf.remote;

import com.xtxb.cmdb.conf.ConfAPI;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月24日-下午5:11
 * <p>
 * <p>
 * 通过配置中心读取配置
 */
@Lazy
@Component("remote_conf")
public class RemoteConf implements ConfAPI {

    @Override
    public String getConf(String key) {
        return null;
    }
}
