package com.xtxb.cmdb.service;

import com.xtxb.cmdb.CmdbApplicationTest;
import com.xtxb.cmdb.common.view.View;
import com.xtxb.cmdb.common.view.ViewIterm;
import com.xtxb.cmdb.common.view.ViewType;
import com.xtxb.cmdb.common.view.ins.ViewDetail;
import com.xtxb.cmdb.common.view.ins.ViewList;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月24日-上午11:00
 * <p>
 * <p>
 * 对视图API进程测试
 */
@Transactional
public class ViewServiceTest extends CmdbApplicationTest {

    @Autowired
    private ViewService service;

    @Test
    public void testViewGet1(){
        View view=service.getView(ViewType.LIST,"d1","s1","测试1","C_Host");
        Assert.assertNotNull(view);
        Assert.assertEquals(ViewType.LIST,view.getType());
    }

    @Test
    public void testViewGet2(){
        List<View> views=service.getViews("C_Host");
        Assert.assertNotNull(views);
        Assert.assertEquals(3,views.size());
    }

    @Test
    public void testViewAddAndDelete1(){
        ViewList view=new ViewList();
        view.setName("ssss");
        view.setModelName("C_Host");
        view.setType(ViewType.LIST);
        view.setDemo("ff");
        view.setScene("dsfsd");
        List<ViewIterm> list=new ArrayList<>();
        ViewIterm iterm=new ViewIterm();
        iterm.setIndex(1);
        iterm.setTitle("aaaaaa");
        iterm.setPtopertyType(1);
        iterm.setPropertyName("sdasdfsda");
        list.add(iterm);
        view.setLiterms(list);
        Assert.assertTrue(service.addView(view));
        Assert.assertTrue(service.deleteView(view.getType(),view.getDemo(),view.getScene(),view.getName(),view.getModelName()));
    }

    @Test
    public void testViewAddAndDelete2(){
        ViewDetail view=new ViewDetail();
        view.setName("ssss");
        view.setModelName("C_Host");
        view.setType(ViewType.MODIFY);
        view.setDemo("ff");
        view.setScene("dsfsd");
        Map<String,List<ViewIterm>> map=new HashMap<>();
        List<ViewIterm> list=new ArrayList<>();
        ViewIterm iterm=new ViewIterm();
        iterm.setIndex(1);
        iterm.setTitle("aaaaaa");
        iterm.setPtopertyType(1);
        iterm.setPropertyName("sdasdfsda");
        list.add(iterm);
        map.put("sfa",list);
        view.setLiterms(map);
        Assert.assertTrue(service.addView(view));
        Assert.assertTrue(service.deleteView(view.getType(),view.getDemo(),view.getScene(),view.getName(),view.getModelName()));
    }
}