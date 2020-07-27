package com.xtxb.cmdb.service;

import com.xtxb.cmdb.common.tree.ModelClassTree;

import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月27日-上午11:24
 * <p>
 * <p>
 * 管理资源类型组织结构树
 */
public interface ModelTreeService {

    /**
     * 查询所有的资源类型组织结构树
     * @return
     */
    public List<String> getTreeNames();

    /**
     * 获取资源类型组织结构的详细定义
     * @param name
     * @return
     */
    public ModelClassTree getTree(String name);

    /**
     * 添加资源类型组织结构
     * @param tree
     * @return
     */
    public boolean addTree(ModelClassTree tree);

    /**
     * 更新资源类型组织结构
     * @param tree
     * @return
     */
    public boolean updateTree(ModelClassTree tree);

    /**
     * 删除资源类型组织结构
     * @param name
     * @return
     */
    public boolean deleteTree(String name);
}
