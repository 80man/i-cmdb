package com.xtxb.cmdb.service.data.dao;

import com.xtxb.cmdb.common.query.QueryIterm;
import com.xtxb.cmdb.common.value.Resource;

import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月06日-下午2:58
 * <p>
 * <p>
 * 负责资源实例数据的持久化
 */
public interface ResourceDB {

    /**
     * 分页获取资源对象
     * @param modelName 资源类型的英文名称
     * @param pageIndex 页号
     * @param pageLen 页长
     * @param user 操作账号，当前版本仅作记录，不会影响权限
     * @return
     */
    public List<Resource> getResources(String modelName, int pageIndex, int pageLen, String user) throws Exception;

    /**
     * 自定义条件查询资源对象
     * @param pageIndex 页号
     * @param pageLen 页长
     * @param user 操作账号，当前版本仅作记录，不会影响权限
     * @param iterm  查询条件,格式为：
     *               Group(GroupType.LEFT),KeyPair(property,value),Logic(LogicType.OR),KeyPair(property,value),Group(GroupType.RIGHT),Logic(LogicType.AND),KeyPair(property,value)
     * @return
     */
    public List<Resource> queryResources(String modelName,int pageIndex, int pageLen , String user, QueryIterm... iterm) throws Exception;

    /**
     * 更新资源对象
     * @param user 操作账号，当前版本仅作记录，不会影响权限
     * @param resources
     * @return
     * @throws Exception
     */
    public boolean updateResources(String user,List<Resource> resources)  throws Exception;

    /**
     * 添加资源对象
     * @param user 操作账号，当前版本仅作记录，不会影响权限
     * @param resources
     * @return
     * @throws Exception
     */
    public boolean addResources(String user,List<Resource> resources)  throws Exception;

    /**
     * 删除资源对象，删除时也会删除相关的关系数据
     * @param user
     * @param resources
     * @return
     * @throws Exception
     */
    public boolean deleteResources(String user,List<Resource> resources)  throws Exception;
}
