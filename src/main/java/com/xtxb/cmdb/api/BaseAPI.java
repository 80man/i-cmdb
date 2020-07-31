package com.xtxb.cmdb.api;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月30日-下午5:38
 * <p>
 * <p>
 * 外部接口的基类，实现通用方法
 */
public  class BaseAPI  {

    protected static int SUCESS=1;
    protected static int ERROR=0;

    /**
     * 生成API返回值的模板
     * code:  SUCESS 失败  ERROR 成功   默认返回 SUCESS
     * message: 执行信息
     * @return
     */
    protected Map<String,Object> getReturnMap(){
        Map<String,Object> map=new HashMap<>();
        map.put("code",SUCESS);
        map.put("message","");
        return map;
    }
}
