package com.xtxb.cmdb.service;

import com.xtxb.cmdb.CmdbApplicationTest;
import com.xtxb.cmdb.common.query.KeyPair;
import com.xtxb.cmdb.common.query.QueryIterm;
import com.xtxb.cmdb.common.value.Resource;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月21日-下午4:29
 * <p>
 * <p>
 * 资源实例相关API
 */
@Transactional
public class ResourceServiceTestCase extends CmdbApplicationTest {
    @Autowired
    ResourceService service;

    @Test
    public void testResourceAddAndGet(){
        Resource res=new Resource();
        res.setSid("sddad");
        res.setModelName("C_Host");
        res.setValue("ip","192.168.0.10");
        res.setValue("cpubit",new Long(32));
        try {
            Assert.assertTrue(service.addResources("root",res));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resource[] list=new Resource[99];
        for(int i=0;i<99;i++){
            res=new Resource();
            res.setOid(new Long(i+1+1));
            res.setSid("sddad"+(i+1));
            res.setModelName("C_Host");
            res.setValue("ip","192.168.0.1"+i);
            res.setValue("cpubit",new Long(32));
            list[i]=res;
        }
        try {
            Assert.assertTrue(service.addResources("root",list));
            Assert.assertNotNull(service.getResource("sddad","C_Host"));
            Assert.assertNotNull(service.getResources("C_Host",1,1000,"root").size());
            Assert.assertNotNull(service.getResources("C_Host",2,2,"root").get(0).getOid());
            Assert.assertNotNull(service.queryResources("C_Host",1,100,"root",
                    new QueryIterm[]{ new KeyPair("ip","192.168.0.12",KeyPair.EQUALS)}).size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResourceUpdateAndDelete(){
        Resource res=new Resource();
        res.setSid("sddad");
        res.setModelName("C_Host");
        res.setValue("ip","192.168.0.10");
        res.setValue("cpubit",new Long(32));
        try {
            service.addResources("root",res);
            res=(Resource) res.clone();
            res.setValue("ip","1.0.0.1");
            Assert.assertTrue(service.updateResources("root",res));
            Assert.assertTrue(service.deleteResources("root",res));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}