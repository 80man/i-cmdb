package com.xtxb.cmdb.service.impl;

import com.xtxb.cmdb.common.query.QueryIterm;
import com.xtxb.cmdb.common.value.Resource;
import com.xtxb.cmdb.service.data.CacheFactory;
import com.xtxb.cmdb.service.data.DBFactory;
import com.xtxb.cmdb.util.IDUtil;
import com.xtxb.cmdb.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月15日-下午2:50
 * <p>
 * <p>
 * 操作资源实例的内部API
 */
@Component
public class ResourceServiceDefaultImpl implements com.xtxb.cmdb.service.ResourceService {
    @Autowired
    private LoggerUtil log;

    @Autowired
    private CacheFactory cacheFactory;

    @Autowired
    private DBFactory dbFactory;

    @Autowired
    private IDUtil idUtil;

    /**
     * 根据oid查询资源
     *
     * @param oid  资源的唯一标识
     * @param user 操作账号，当前版本仅作记录，不会影响权限
     * @return
     */
    @Override
    public Resource getResource(long oid, String user) {
        return cacheFactory.getResourceInstance().getResource(oid,user);
    }

    /**
     * 根据资源的sid查询资源对象
     *
     * @param sid  资源对象的唯一标识sid
     * @param user 操作账号，当前版本仅作记录，不会影响权限
     * @return
     */
    @Override
    public Resource getResource(String sid, String user) {
        return cacheFactory.getResourceInstance().getResource(sid,user);
    }

    /**
     * 分页获取资源对象
     *
     * @param modelName 资源类型的英文名称
     * @param pageIndex 页号
     * @param pageLen   页长
     * @param user      操作账号，当前版本仅作记录，不会影响权限
     * @return
     */
    @Override
    public List<Resource> getResources(String modelName, int pageIndex, int pageLen, String user) throws Exception {
        return dbFactory.getInstanceResource().getResources(modelName,pageIndex,pageLen,user);
    }

    /**
     * 自定义条件查询资源对象
     *
     * @param modelName
     * @param pageIndex 页号
     * @param pageLen   页长
     * @param user      操作账号，当前版本仅作记录，不会影响权限
     * @param iterm     查询条件,格式为：
     *                  Group(GroupType.LEFT),KeyPair(property,value),Logic(LogicType.OR),KeyPair(property,value),Group(GroupType.RIGHT),Logic(LogicType.AND),KeyPair(property,value)
     * @return
     */
    @Override
    public List<Resource> queryResources(String modelName, int pageIndex, int pageLen, String user, QueryIterm... iterm) throws Exception {
        return dbFactory.getInstanceResource().queryResources(modelName,pageIndex,pageLen,user,iterm);
    }

    /**
     * 更新资源对象
     *
     * @param user     操作账号，当前版本仅作记录，不会影响权限
     * @param resource
     * @return
     * @throws Exception
     */
    @Override
    public boolean updateResources(String user, Resource... resource) throws Exception {
        return updateResources(user, Arrays.asList(resource));
    }

    /**
     * 更新资源对象
     *
     * @param user      操作账号，当前版本仅作记录，不会影响权限
     * @param resources
     * @return
     * @throws Exception
     */
    @Override
    public boolean updateResources(String user, List<Resource> resources) throws Exception {
        return dbFactory.getInstanceResource().updateResources(user,resources)
                && cacheFactory.getResourceInstance().updateResources(user,resources);
    }

    /**
     * 添加资源对象
     *
     * @param user     操作账号，当前版本仅作记录，不会影响权限
     * @param resource
     * @return
     * @throws Exception
     */
    @Override
    public boolean addResources(String user, Resource... resource) throws Exception {
        return addResources(user, Arrays.asList(resource));
    }

    /**
     * 添加资源对象
     *
     * @param user      操作账号，当前版本仅作记录，不会影响权限
     * @param resources
     * @return
     * @throws Exception
     */
    @Override
    public boolean addResources(String user, List<Resource> resources) throws Exception {
        for(Resource res:resources){
            res.setOid(idUtil.next());
        }
        return dbFactory.getInstanceResource().addResources(user,resources)
                && cacheFactory.getResourceInstance().addResources(user,resources);
    }

    /**
     * 删除资源对象，删除时也会删除相关的关系数据
     *
     * @param user
     * @param resource
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteResources(String user, Resource... resource) throws Exception {
        return deleteResources(user, Arrays.asList(resource));
    }

    /**
     * 删除资源对象，删除时也会删除相关的关系数据
     *
     * @param user
     * @param resources
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteResources(String user, List<Resource> resources) throws Exception {
        return dbFactory.getInstanceResource().deleteResources(user,resources)
                && cacheFactory.getResourceInstance().deleteResources(user,resources);
    }
}
