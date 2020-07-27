package com.xtxb.cmdb.common.tree;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月27日-上午11:28
 * <p>
 * <p>
 * 资源类型树的树节点
 */
public class TreeNode {

    /*声明此节点是否关联到资源类型*/
    private boolean isLabel;
    /*当 label 为 true 时，此属性指向节点关联的url页面地址*/
    private String url;
    /*节点关联的资源类型*/
    private String  modelClass;
    /*节点名称*/
    private String  cnName;
    /*资源类型树的上级英文名称*/
    private TreeNode[] childNode;

    public String getModelClass() {
        return modelClass;
    }

    public void setModelClass(String modelClass) {
        this.modelClass = modelClass;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public boolean isLabel() {
        return isLabel;
    }

    public void setLabel(boolean label) {
        isLabel = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TreeNode[] getChildNode() {
        return childNode;
    }

    public void setChildNode(TreeNode[] childNode) {
        this.childNode = childNode;
    }
}
