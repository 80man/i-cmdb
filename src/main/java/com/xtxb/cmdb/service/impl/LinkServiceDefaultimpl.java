package com.xtxb.cmdb.service.impl;

import com.xtxb.cmdb.common.value.Link;
import com.xtxb.cmdb.service.LinkService;
import com.xtxb.cmdb.service.data.DBFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月16日-下午4:13
 * <p>
 * <p>
 * 资源关系的服务类
 */
@Component
public class LinkServiceDefaultimpl implements LinkService {

    @Autowired
    private DBFactory dbFactory;
    /**
     * 根据关系类型和关系两端的oid查询关系对象
     *
     * @param type 关系类型
     * @param sid  源端资源对象
     * @param tid  目的端资源对象
     * @return
     */
    @Override
    public Link getLink(String type, long sid, long tid) {
        return dbFactory.getInstanceLink().getLink(type,sid,tid);
    }

    /**
     * 根据关系类型和关系一端的oid查询关系对象
     *
     * @param type     关系类型
     * @param cid      源|目的端资源对象
     * @param isSource 声明sid是否是源端资源对象的ois，否则为目的端
     * @return
     */
    @Override
    public List<Link> getLink(String type, long cid, boolean isSource) {
        return dbFactory.getInstanceLink().getLink(type,cid,isSource);
    }

    /**
     * 添加关系对象
     *
     * @param links 关系对象数组
     * @return
     * @throws Exception
     */
    @Override
    public boolean addLink(Link... links) throws Exception {
        return addLink(Arrays.asList(links));
    }

    /**
     * 添加关系对象
     *
     * @param links 关系对象列表
     * @return
     * @throws Exception
     */
    @Override
    public boolean addLink(List<Link> links) throws Exception {
        if(links==null || links.size()==0)
            return false;
        return dbFactory.getInstanceLink().addLink(links);
    }

    /**
     * 删除关系对象
     *
     * @param link
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteLink(Link... link) throws Exception {
        return deleteLink(link);
    }

    /**
     * 删除关系对象
     *
     * @param linkList
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteLink(List<Link> linkList) throws Exception {
        if(linkList==null || linkList.size()==0)
            return false;
        return dbFactory.getInstanceLink().deleteLink(linkList);
    }
}
