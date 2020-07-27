package com.xtxb.cmdb.service;

import com.xtxb.cmdb.CmdbApplicationTest;
import com.xtxb.cmdb.common.model.*;
import org.junit.Assert;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月17日-上午10:18
 * <p>
 * <p>
 * 资源模型测试类
 */

@Transactional
public class ModelServiceTestCase extends CmdbApplicationTest {

    @Autowired
    protected ModelService model;

    @Test
    public void testModelByDesc1(){
        assertThat(model.getModelByDescr("服务器")).isNotNull();
    }

    @Test
    public void testModelByDesc2(){
        assertThat(model.getModelByDescr("服务器")).hasFieldOrPropertyWithValue("name","C_Host");
    }

    @Test
    public void testModelByName1(){
        assertThat(model.getModelByName("C_Host")).isNotNull();
    }

    @Test
    public void testModelByName2(){
        assertThat(model.getModelByName("C_Host")).hasFieldOrPropertyWithValue("descr","服务器");
    }

    @Test
    public void testModelGet(){
        Assert.assertNotNull(model.getModels());
    }

    @Test
    public void testModelUpdate1(){
        ModelClass m=model.getModelByName("C_Host");
        ModelClass um=new ModelClass(m.getName(),m.getDescr()+"1");
        try {
            assertTrue(model.updateModel(um));
            assertThat(model.getModelByName("C_Host").getDescr().equals("服务器1"));
            model.updateModel(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModelUpdate2(){
        try {
            assertTrue(!model.updateModel(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public  void testModelAddAndDelete(){
        ModelClass m=new ModelClass("AAA","测试");
        try {
            assertThat(model.addModel(m));
            Assert.assertNotNull(model.getModelByName("AAA"));
            assertThat(model.deleteModel(m.getName()));
            assertNull(model.getModelByName("AAA"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPropertyGet1(){
        Property pro=model.getProperty("C_Host","ip");
        Assert.assertNotNull(pro);
        Assert.assertEquals("管理IP",pro.getDescr());
    }

    @Test
    public void testPropertyGet2(){
        List<Property> pros=model.getProperties("C_Host");
        Assert.assertNotNull(pros);
        Assert.assertTrue(pros.size()>0);
    }

    @Test
    public void testPropertyUpdate(){
        try {
            List<Property> old_list=new ArrayList<>();
            old_list.add(model.getProperty("C_Host","ip"));
            old_list.add(model.getProperty("C_Host","weight"));
            List<Property> new_list=new ArrayList<>();
            new_list.add((Property) old_list.get(0).clone());
            new_list.add((Property)old_list.get(1).clone());
            new_list.get(0).setDescr(new_list.get(0).getDescr()+"1");
            new_list.get(1).setDescr(new_list.get(1).getDescr()+"1");
            assertThat(model.updateProperty(new_list));
            Assert.assertEquals("管理IP1",model.getProperty("C_Host","ip").getDescr());
            Assert.assertEquals("重量(kg)1",model.getProperty("C_Host","weight").getDescr());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPropertyAddAndDelete(){
        try {
            List<Property> list=new ArrayList<>();
            for(int i=0;i<20;i++){
                Property pro=new Property();
                pro.setMatchRule("sssss");
                pro.setRule(null);
                pro.setDefValue("100");
                pro.setType(PropertyType.LONG);
                pro.setGroup("三方法");
                pro.setDescr("法"+i);
                pro.setName("sf"+i);
                pro.setModelName("C_Host");
                list.add(pro);
            }
            Assert.assertTrue(model.addProperty(list));
            Assert.assertNotNull(model.getProperty("C_Host","sf1"));
            Assert.assertTrue(model.deleteProperties(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public  void testRelationShipGet1(){
        Assert.assertNotNull(model.getRelationShip("R_Connection"));
    }

    @Test
    public  void testRelationShipGet2(){
        Assert.assertNotNull(model.getRelationShips("R_Host",true));
        List o=model.getRelationShips("R_Host",false);
        Assert.assertTrue(o==null || o.size()==0);
    }

    @Test
    public void testRelationShipUpdate(){
        RelationShip rs=model.getRelationShip("R_Connection");
        rs=(RelationShip)rs.clone();
        rs.setDescr("11111");
        try {
            model.updateRelationShip(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(model.getRelationShip("R_Connection").getDescr().equals("11111"));
    }

    @Test
    public void testRelationShipAddAndDelete(){
        RelationShip rs=new RelationShip("A","斯蒂芬","C_Host","C_Switch");
        try {
            Assert.assertTrue(model.addRelationShip(rs));
            Assert.assertTrue(model.getRelationShip("A")!=null);
            model.deleteRelationShip("A");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
