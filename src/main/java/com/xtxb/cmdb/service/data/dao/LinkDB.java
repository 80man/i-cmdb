package com.xtxb.cmdb.service.data.dao;

import com.xtxb.cmdb.common.value.Link;

import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月16日-下午2:27
 * <p>
 * <p>
 * 处理资源关系数据
 */
public interface LinkDB {
    /**
     * 根据关系类型和关系两端的oid查询关系对象
     * @param type 关系类型
     * @param sid 源端资源对象
     * @param tid 目的端资源对象
     * @return
     */
    public Link getLink(String type, long sid, long tid);

    /**
     * 根据关系类型和关系一端的oid查询关系对象
     * @param type 关系类型
     * @param sid  源|目的端资源对象
     * @param isSource  声明sid是否是源端资源对象的ois，否则为目的端
     * @return
     */
    public List<Link> getLink(String type, long sid, boolean isSource);

    /**
     * 添加关系对象
     * @param links 关系对象列表
     * @return
     * @throws Exception
     */
    public boolean addLink(List<Link> links) throws Exception;

    /**
     * 删除关系对象
     * @param linkList
     * @return
     * @throws Exception
     */
    public boolean deleteLink(List<Link> linkList) throws Exception;
}
