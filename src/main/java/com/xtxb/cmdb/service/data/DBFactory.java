package com.xtxb.cmdb.service.data;

import com.xtxb.cmdb.service.data.dao.ModelDB;
import com.xtxb.cmdb.service.data.dao.ResourceDB;
import com.xtxb.cmdb.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月30日-上午10:24
 * <p>
 * <p>
 * 负责根据配置声明的类型获取资源类型数据库
 */
@Component
public class DBFactory {
    @Value("${cmdb.db.model.name}")
    private String dbModelName;

    @Value("${cmdb.db.resource.name}")
    private String dbResourceName;


    @Autowired
    private SpringContextUtil beanUtil;

    private ModelDB dbModel;
    private byte[] lockModel=new byte[1];

    private ResourceDB dbResource;
    private byte[] lockResource=new byte[1];

    public ModelDB getInstanceModel(){
        if(dbModel==null) {
            synchronized (lockModel) {
                dbModel = (ModelDB) beanUtil.getBean(dbModelName);
            }
        }
        return dbModel;
    }

    public ResourceDB getInstanceResource(){
        if(dbResource==null) {
            synchronized (lockResource) {
                dbResource = (ResourceDB) beanUtil.getBean(dbResourceName);
            }
        }
        return dbResource;
    }
}
