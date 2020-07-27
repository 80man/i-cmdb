package com.xtxb.cmdb.common.tree;

import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月27日-上午11:15
 * <p>
 * <p>
 * 用于描述资源类型的组织结构，不同业务场景下，可以自定义呈现结构
 */
public class ModelClassTree {

    /*资源类型树的英文名称*/
    private String name;
    /*资源类型树的中文名称*/
    private String cnName;

    private TreeNode[]  nodes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public TreeNode[] getNodes() {
        return nodes;
    }

    public void setNodes(TreeNode[] nodes) {
        this.nodes = nodes;
    }
}
