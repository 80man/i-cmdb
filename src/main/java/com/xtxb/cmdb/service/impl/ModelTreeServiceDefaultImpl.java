package com.xtxb.cmdb.service.impl;

import com.xtxb.cmdb.common.tree.ModelClassTree;
import com.xtxb.cmdb.common.tree.TreeNode;
import com.xtxb.cmdb.common.view.View;
import com.xtxb.cmdb.service.ModelTreeService;
import com.xtxb.cmdb.util.LoggerUtil;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月27日-下午2:37
 * <p>
 * <p>
 * 默认的资源类型组织结构服务实现
 */
@Component
public class ModelTreeServiceDefaultImpl implements ModelTreeService {
    @Autowired
    private LoggerUtil log;

    private String filePath;
    private Map<String,ModelClassTree> treeMap=new HashMap<>();

    public ModelTreeServiceDefaultImpl(){
        filePath=System.getProperty("user.dir")+ File.separator+"tree_conf.xml";
        init();
    }

    private void init(){
        File file=new File(filePath);
        if(!file.exists()){
            try {
                file.createNewFile();
                Document doc= DocumentFactory.getInstance().createDocument("UTF-8");
                doc.addElement("trees");
                writeDocument(doc);
            } catch (IOException e) {
                log.error("",e);
            }
            return ;
        }
    }

    @Override
    public List<String> getTreeNames() {
        Document doc=readDocument();
        List<Node> nodes=doc.selectNodes("/trees/tree");
        if(nodes==null || nodes.size()==0)
            return null;

        List<String>names=new ArrayList();
        for(Node node:nodes){
            names.add(((Element)node).attributeValue("name"));
        }
        return names;
    }

    @Override
    public ModelClassTree getTree(String name) {
        Document doc=readDocument();
        Element treeEle=(Element)doc.selectSingleNode("/trees/tree[@name='"+name+"']");
        ModelClassTree tree =new ModelClassTree();
        tree.setName(name);
        tree.setCnName(treeEle.attributeValue("title"));
        tree.setNodes(getNode(treeEle.elements()));
        return tree;
    }

    /**
     * 添加资源类型组织结构
     *
     * @param tree
     * @return
     */
    @Override
    public boolean addTree(ModelClassTree tree) {
        Document doc=readDocument();
        Element treeEle=(Element)doc.selectSingleNode("/trees/tree[@name='"+tree.getName()+"']");
        if(treeEle!=null){
            log.error("已经存在名称为:"+tree.getName()+"的资源类型组织结构");
            return false;
        }

        Element treeNode=doc.getRootElement().addElement("tree");
        treeNode.addAttribute("name",tree.getName());
        treeNode.addAttribute("title",tree.getCnName());
        addTreeNode(treeNode,tree.getNodes());
        writeDocument(doc);
        return true;
    }

    @Override
    public boolean updateTree(ModelClassTree tree) {
        Document doc=readDocument();
        Element treeEle=(Element)doc.selectSingleNode("/trees/tree[@name='"+tree.getName()+"']");
        if(treeEle==null){
            log.error("不存在名称为:"+tree.getName()+"的资源类型组织结构");
            return false;
        }
        return deleteTree(tree.getName()) && addTree(tree);
    }

    @Override
    public boolean deleteTree(String name) {
        Document doc=readDocument();
        Element treeEle=(Element)doc.selectSingleNode("/trees/tree[@name='"+name+"']");
        doc.getRootElement().remove(treeEle);
        writeDocument(doc);
        return true;
    }


    private void addTreeNode(Element ele,TreeNode[] nodes){
        if(nodes==null)
            return;
        Element temp;
        for (TreeNode node : nodes) {
            temp=ele.addElement("node");
            temp.addAttribute("name",node.isLabel()?"":node.getModelClass());
            temp.addAttribute("title",node.getCnName());
            temp.addAttribute("isLabel",node.isLabel()+"");
            temp.addAttribute("url",node.isLabel()?node.getUrl():"");
            addTreeNode(temp,node.getChildNode());
        }
    }

    private TreeNode[] getNode(List<Element> eles){
        if(eles==null || eles.size()==0)
            return null;
        TreeNode[] nodes=new TreeNode[eles.size()];
        TreeNode node=null;
        for (int i=0;i<nodes.length;i++){
            node=new TreeNode();
            node.setLabel("true".equals(eles.get(i).attributeValue("isLabel")));
            node.setCnName(eles.get(i).attributeValue("title"));
            node.setModelClass(eles.get(i).attributeValue("name"));
            node.setUrl(eles.get(i).attributeValue("url"));
            TreeNode[] subNodes=getNode(eles.get(i).elements());
            node.setChildNode(subNodes);
            nodes[i]=node;
        }
        return nodes;
    }

    private Document readDocument(){
        try {
            SAXReader reader=new SAXReader();
            File file=new File(filePath);
            Document doc=reader.read(file);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将XML写入文件
     * @param doc
     * @return
     */
    private boolean writeDocument(Document doc){
        File file=new File(filePath);
        XMLWriter writer=null;
        try {
            OutputFormat format=OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            writer=new XMLWriter(new FileWriter(file),format);
            writer.write(doc);
            return true;
        } catch (IOException e) {
            log.error("",e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                log.error("",e);
            }
        }

        return false;
    }

}
