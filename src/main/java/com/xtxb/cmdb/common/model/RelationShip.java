package com.xtxb.cmdb.common.model;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月24日-上午11:02
 * <p>
 * <p>
 *  定义关系类型
 */
public class RelationShip {

    /*关系类型英文名称*/
    private String name;
    /*关系类型中文名称*/
    private String descr;
    /*关系源端资源类型*/
    private String sourceModel;
    /*关系目的段资源类型*/
    private String targetModel;

    public RelationShip(String name, String descr, String sourceModel, String targetModel) {
        this.name = name;
        this.descr = descr;
        this.sourceModel = sourceModel;
        this.targetModel = targetModel;
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

    public String getSourceModel() {
        return sourceModel;
    }

    public void setSourceModel(String sourceModel) {
        this.sourceModel = sourceModel;
    }

    public String getTargetModel() {
        return targetModel;
    }

    public void setTargetModel(String targetModel) {
        this.targetModel = targetModel;
    }

    @Override
    public Object clone(){
        return new RelationShip(getName(),getDescr(),getSourceModel(),getTargetModel());
    }
}
