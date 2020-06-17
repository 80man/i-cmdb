package com.xtxb.cmdb.common.model;

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
    /*关联的上级类型，副类型*/
    private String parent;

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

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
