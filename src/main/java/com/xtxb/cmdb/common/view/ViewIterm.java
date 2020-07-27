package com.xtxb.cmdb.common.view;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月17日-下午3:24
 * <p>
 * <p>
 * 属性视图条目
 */
public class ViewIterm {
    /*索引，属性的展示顺序*/
    private int index;
    /*属性展示名称*/
    private String title;
    /*关联色属性名称*/
    private String propertyName;
    /*属性类型*/
    private int ptopertyType;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public int getPtopertyType() {
        return ptopertyType;
    }

    public void setPtopertyType(int ptopertyType) {
        this.ptopertyType = ptopertyType;
    }
}
