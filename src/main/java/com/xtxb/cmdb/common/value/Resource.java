package com.xtxb.cmdb.common.value;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月17日-上午10:07
 * <p>
 * <p>
 * CMDB中实际管理的对象，称为配置项或资源
 */
public class Resource {
    /*资源的唯一标识，系统自动生成，不能修改，资源添加时会覆盖用户自行设置的值*/
    private long oid;
    /*资源的唯一标识，由用户制定，长度不应超过32个字符*/
    private String sid;
    /*资源所属的模型类型*/
    private String modelName;
    /*资源属性键值对*/
    private Map<String,Object> values;

    public Resource(){
        values=new HashMap<>();
    }

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public Object getValue(String key) {
        return values.get(key);
    }

    public void setValues(Map<String, Object> values) {
        if(values!=null)
            this.values.putAll(values);
    }

    public void setValue(String key,Object value){
        if(key!=null)
            values.put(key,value);
    }

    @Override
    public Object clone(){
        Resource res=new Resource();
        res.setOid(getOid());
        res.setSid(getSid());
        res.setModelName(getModelName());
        for (Iterator<String> iterator = values.keySet().iterator(); iterator.hasNext(); ) {
            String property =  iterator.next();
            res.setValue(property,values.get(property));
        }
        return res;
    }
}
