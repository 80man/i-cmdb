package com.xtxb.cmdb.service.data.cache;

import com.xtxb.cmdb.common.model.ModelClass;
import com.xtxb.cmdb.common.value.Resource;
import com.xtxb.cmdb.service.data.DBFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月10日-下午3:58
 * <p>
 * <p>
 *
 */
@Lazy
@Component("defaultResCache")
public class ResourceCacheDefault implements ResourceCache{
    private Map<Long,Resource> oidMap;
    private Map<String,Resource> sidMap;

    @Autowired
    private DBFactory dbFactory;

    @Override
    public void init() {
        List<ModelClass> list=dbFactory.getInstanceModel().getModel();
        oidMap=new Hashtable<>();
        sidMap=new Hashtable<>();
        try {
            for (ModelClass mc : list) {
                int page = 1;
                while (true) {
                    List<Resource> rlist = dbFactory.getInstanceResource().getResources(mc.getName(), page++, 1000, "root");
                    if (rlist != null && rlist.size()>0) {
                        for (Resource resource : rlist) {
                            resource.setModelName(mc.getName());
                            oidMap.put(resource.getOid(), resource);
                            sidMap.put(resource.getSid(), resource);
                        }
                    } else
                        break;
                }
                Thread.sleep(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 根据oid查询资源
     *
     * @param oid  资源的唯一标识
     * @param user 操作账号，当前版本仅作记录，不会影响权限
     * @return
     */
    @Override
    public Resource getResource(long oid, String user) {
        return oidMap.get(oid);
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
        return sidMap.get(sid);
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
        for (Resource resource : resources) {
            oidMap.put(resource.getOid(), resource);
            sidMap.put(resource.getSid(), resource);
        }
        return true;
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
        for (Resource resource : resources) {
            oidMap.put(resource.getOid(), resource);
            sidMap.put(resource.getSid(), resource);
        }
        return true;
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
        for (Resource resource : resources) {
            oidMap.remove(resource.getOid());
            sidMap.remove(resource.getSid());
        }
        return true;
    }
}

