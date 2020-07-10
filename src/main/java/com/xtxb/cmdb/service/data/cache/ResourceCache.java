package com.xtxb.cmdb.service.data.cache;

import com.xtxb.cmdb.common.query.QueryIterm;
import com.xtxb.cmdb.common.value.Resource;

import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月10日-下午3:55
 * <p>
 * <p>
 * 资源缓存
 */
public interface ResourceCache extends Initializer{

    /**
     * 根据oid查询资源
     * @param oid 资源的唯一标识
     * @param user 操作账号，当前版本仅作记录，不会影响权限
     * @return
     */
    public Resource getResource(long oid, String user);

    /**
     * 根据资源的sid查询资源对象
     * @param sid 资源对象的唯一标识sid
     * @param user 操作账号，当前版本仅作记录，不会影响权限
     * @return
     */
    public Resource getResource(String sid,String user);

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
