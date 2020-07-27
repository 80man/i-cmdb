package com.xtxb.cmdb.service;

import com.xtxb.cmdb.CmdbApplicationTest;
import com.xtxb.cmdb.common.model.ModelClass;
import com.xtxb.cmdb.common.tree.ModelClassTree;
import com.xtxb.cmdb.common.tree.TreeNode;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月27日-下午4:53
 * <p>
 * <p>
 *
 */
@Transactional
public class ModelTreeServiceTestCase extends CmdbApplicationTest {

    @Autowired
    private ModelTreeService service;

    @Test
    public void testTreeGet1(){
        List<String> values=service.getTreeNames();
        Assert.assertNotNull(values);
        if(values!=null) {
            Assert.assertEquals(2, values.size());
        }
    }

    @Test
    public void testTreeGet2(){
        ModelClassTree tree=service.getTree("t1");
        Assert.assertNotNull(tree);
        Assert.assertEquals(2,tree.getNodes().length);
    }

    @Test
    public void testAddAndDeleteTree(){
        ModelClassTree tree=new ModelClassTree();
        tree.setName("aaa");
        tree.setCnName("测试用");
        TreeNode[] nodes=new TreeNode[2];
        nodes[0]=new TreeNode();
        nodes[0].setLabel(true);
        nodes[0].setUrl("www.baidu.com");
        nodes[0].setCnName("测试节点");

        TreeNode[] subNodes=new TreeNode[2];
        subNodes[0]=new TreeNode();
        subNodes[0].setModelClass("C_Host1");
        subNodes[0].setCnName("主机1");
        subNodes[0].setLabel(false);
        subNodes[0].setUrl("www.baidu1.com");
        subNodes[1]=new TreeNode();
        subNodes[1].setModelClass("C_Host2");
        subNodes[1].setCnName("主机2");
        subNodes[1].setLabel(false);
        subNodes[1].setUrl("www.baidu2.com");
        nodes[0].setChildNode(subNodes);

        nodes[1]=new TreeNode();
        nodes[1].setModelClass("C_Host");
        nodes[1].setCnName("主机");
        nodes[1].setLabel(false);
        nodes[1].setUrl("www.baidu1.com");

        tree.setNodes(nodes);
        Assert.assertTrue(service.addTree(tree));
        Assert.assertEquals(2,service.getTree("aaa").getNodes().length);
        Assert.assertTrue(service.deleteTree(tree.getName()));
    }

    @Test
    public void testUpdateTree(){
        ModelClassTree tree=service.getTree("t2");
        String o_name=tree.getCnName();
        String name=o_name+(System.currentTimeMillis()%100);
        tree.setCnName(name);
        service.updateTree(tree);
        Assert.assertEquals(name,service.getTree("t2").getCnName());
        tree.setCnName(o_name);
        service.updateTree(tree);
    }
}