package com.xtxb.cmdb.service;

import com.xtxb.cmdb.CmdbApplicationTest;
import com.xtxb.cmdb.common.value.Link;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月22日-下午6:31
 * <p>
 * <p>
 * 资源关系API测试用例
 */
@Transactional
public class LinkServiceTestCase extends CmdbApplicationTest {
    @Autowired
    private LinkService service;

    @Test
    public void testLinkAddAndGet(){

        Link[] links=new Link[100];
        for(int i=0;i<100;i++) {
            links[i] = new Link(i+1, 100+i, "Link" +(i+1));
            links[i].setType("R_Connection");
        }
        try {
            Assert.assertTrue(service.addLink(links));
            Assert.assertNotNull(service.getLink("R_Connection",2,101));
            Assert.assertEquals(1,service.getLink("R_Connection",101,false).size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLinkDelete(){
        Link[] links=new Link[100];
        for(int i=0;i<100;i++) {
            links[i] = new Link(i+1, 101, "Link" +(i+1));
            links[i].setType("R_Connection");
        }
        try {
            service.addLink(links);
            Assert.assertTrue(service.deleteLink(links));
            Assert.assertNull(service.getLink("R_Connection",1,101));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}