package com.xtxb.cmdb.util;

import com.alibaba.fastjson.JSON;


import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月31日-上午10:17
 * <p>
 * <p>
 *
 */
@Component
public class JsonUtil {

    public String getJosnStr(Map values){
        return JSON.toJSONString(values,true);
    }
}
