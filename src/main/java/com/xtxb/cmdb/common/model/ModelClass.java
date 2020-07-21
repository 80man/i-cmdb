package com.xtxb.cmdb.common.model;

import com.xtxb.cmdb.common.action.Action;

import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月17日-上午10:10
 * <p>
 * <p>
 * CMDB中描述资源类型的声明，例如虚拟，容器、IP地址等等
 */
public class ModelClass {
    /*类型的唯一标识，以C_开头*/
    private String name;
    /*类型的中文名称*/
    private String descr;

    public ModelClass(String name, String descr) {
        this.name = name;
        this.descr = descr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    @Override
    public Object clone(){
        return new ModelClass(getName(),getDescr());
    }

}
