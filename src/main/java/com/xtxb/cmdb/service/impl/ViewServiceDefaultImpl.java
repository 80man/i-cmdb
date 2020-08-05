package com.xtxb.cmdb.service.impl;

import com.xtxb.cmdb.common.view.View;
import com.xtxb.cmdb.common.view.ViewIterm;
import com.xtxb.cmdb.common.view.ViewType;
import com.xtxb.cmdb.common.view.ins.ViewDetail;
import com.xtxb.cmdb.common.view.ins.ViewList;
import com.xtxb.cmdb.service.ViewService;
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
import java.util.*;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月24日-上午11:02
 * <p>
 * <p>
 * 默认的视图API实现类
 */
@Component
public class ViewServiceDefaultImpl  implements ViewService {

    @Autowired
    private LoggerUtil log;

    private String filePath;
    private Map<String,List<View>> viewMap=new HashMap<>();

    public ViewServiceDefaultImpl (){
        filePath=System.getProperty("user.dir")+File.separator+"view_conf.xml";
        init();
    }

    /**
     * 初始化缓存
     */
    private void init(){
        File file=new File(filePath);
        if(!file.exists()){
            try {
                file.createNewFile();
                Document doc=DocumentFactory.getInstance().createDocument("UTF-8");
                doc.addElement("views");
                writeDocument(doc,file);
            } catch (IOException e) {
                log.error("",e);
            }
            return ;
        }

        try {
            SAXReader reader=new SAXReader();
            Document doc=reader.read(file);
            List<View> list=readViews(doc);
            if(list==null){
                return;
            }
            for (View view : list) {
                if(!viewMap.containsKey(view.getModelName())){
                    viewMap.put(view.getModelName(),new ArrayList<>());
                }
                viewMap.get(view.getModelName()).add(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 解析结构数据，获取视图列表
     * @param doc
     * @return
     */
    private List<View> readViews(Document doc){
        if(doc==null || doc.nodeCount()==0)
            return null;

        List<View> views=new ArrayList<>();
        View view=null;
        List<Node> list=doc.selectNodes("/views/view");
        for (Node node:list ) {
            String type=((Element)node).attributeValue("type");
            if(type.equals("List"))
                view=getListView((Element)node);
            else if(type.equals("Detail"))
                view=getDetailView((Element)node);
            else if(type.equals("Modify"))
                view=getModifyView((Element)node);

            if(view!=null)
                views.add(view);
        }
        return views;
    }

    /**
     * 获取所有列表视图
     * @param node
     * @return
     */
    private View getListView(Element node){
        ViewList view=(ViewList)getViewBase(node,ViewType.LIST);
        ViewIterm iterm=null;
        List<ViewIterm> list=new ArrayList<>();
        for (Element property:node.elements() ) {
            iterm=getViewIterm(property);
            if(iterm!=null)
                list.add(iterm);
        }
        view.setLiterms(list);
        return view;
    }

    /**
     * 获取查看视图
     * @param node
     * @return
     */
    private View getDetailView(Element node){
        ViewDetail view=(ViewDetail)getViewBase(node,ViewType.DETAIL);
        ViewIterm iterm=null;
        Map<String,List<ViewIterm>> list=new HashMap<>();
        for (Element group:node.elements() ) {
            String groupName=group.attributeValue("name");
            for (Element property:group.elements() ) {
                iterm=getViewIterm(property);
                if(!list.containsKey(groupName))
                list.put(groupName,new ArrayList());
                if(iterm!=null)
                    list.get(groupName).add(iterm);
            }
        }
        view.setLiterms(list);
        return view;
    }

    /**
     * 获取更新资源的视图
     * @param node
     * @return
     */
    private View  getModifyView(Element node){
        ViewDetail view=(ViewDetail)getDetailView(node);
        view.setType(ViewType.MODIFY);
        return view;
    }

    /**
     * 获取视图的基本属性
     * @param node
     * @return
     */
    private View getViewBase(Element node ,ViewType type){
        View view=type==ViewType.LIST?new ViewList():new ViewDetail();
        view.setName(node.attributeValue("name"));
        view.setModelName(node.attributeValue("modelName"));
        view.setType(type);
        view.setDemo(node.attributeValue("demo"));
        view.setScene(node.attributeValue("scens"));
        return view;
    }

    /**
     * 获取视图属性元素
     * @param node
     * @return
     */
    private ViewIterm getViewIterm(Element node){
        ViewIterm iterm=new ViewIterm();
        iterm.setIndex(Integer.valueOf(node.attributeValue("index")));
        iterm.setTitle(node.attributeValue("title"));
        iterm.setPropertyName(node.attributeValue("propertyName"));
        iterm.setPtopertyType(Integer.valueOf(node.attributeValue("propertyType")));
        return iterm;
    }

    /**
     * 查询视图
     *
     * @param type      视图类型
     * @param demo      视图所属的应用范围
     * @param scene     视图所属的应用场景
     * @param name      视图的名称
     * @param modelName 视图所属的资源模型
     * @return
     */
    @Override
    public View getView( ViewType type, String demo, String scene, String name, String modelName) {
        if(type==null || demo==null || scene==null || name==null || modelName==null)
        {
            return null;
        }
        if(viewMap.containsKey(modelName)){
            for (View view:viewMap.get(modelName)) {
                if(view.getType()==type && view.getName().equals(name)
                        && view.getDemo().equals(demo) && view.getScene().equals(scene))
                    return view;
            }
        }
        return null;
    }

    /**
     * 根据资源模型查询相关的视图
     *
     * @param modelName
     * @return
     */
    @Override
    public List<View> getViews(String modelName) {
        return viewMap.get(modelName);
    }

    /**
     * 添加视图
     *
     * @param view
     * @return
     */
    @Override
    public boolean addView(View view) {
        if(viewMap.containsKey(view.getModelName())) {
            for (Iterator<View> iterator = viewMap.get(view.getModelName()).iterator(); iterator.hasNext(); ) {
                View temp = iterator.next();
                if (temp.getName().equals(view.getName())
                        && temp.getScene().equals(view.getScene())
                        && temp.getDemo().equals(view.getScene())
                        && temp.getType() == view.getType()) {
                    log.error("视图已经存在!");
                    return false;
                }
            }
        }

        File file=new File(filePath);
        try {
            SAXReader reader=new SAXReader();
            Document doc=reader.read(file);
            addElement(doc,view);

            if(writeDocument(doc,file)) {
                if (!viewMap.containsKey(view.getModelName())) {
                    viewMap.put(view.getModelName(), new ArrayList<>());
                }
                viewMap.get(view.getModelName()).add(view);
                return true;
            }
        } catch (Exception e) {
            log.error("",e);
        }
        return false;
    }

    /**
     * 修改视图
     *
     * @param view
     * @return
     */
    @Override
    public boolean updateView(View view) {
        return deleteView(view.getType(),view.getDemo(),view.getScene(),view.getName(),view.getModelName()) && addView(view);
    }

    /**
     * 删除视图
     * @param type
     * @param demo
     * @param scens
     * @param name
     * @param modelName
     * @return
     */
    @Override
    public boolean deleteView(ViewType type,String demo,String scens,String name,String modelName) {
        int index=-1;
        List<View> list=viewMap.get(modelName);
        if(list==null)
            return false;
        for (int i=0;i<list.size();i++) {
            if(list.get(i).getName().equals(name)
                    && list.get(i).getScene().equals(scens)
                    && list.get(i).getDemo().equals(demo)
                    && list.get(i).getType()==type){
                index=i;
                break;
            }
        }
        if(index>=0){
            File file=new File(filePath);
            try {
                SAXReader reader = new SAXReader();
                Document doc = reader.read(file);
                String typeStr="List";
                if(type==ViewType.DETAIL)
                    typeStr="Detail";
                if(type==ViewType.MODIFY)
                    typeStr="Modify";
                Node node=doc.selectSingleNode("/views/view[@name='"+name+"' " +
                        "and @modelName='" +modelName+"' "+
                        "and @demo='" +demo+"' "+
                        "and @scens='" +scens+"' "+
                        "and @type='" +typeStr+"'"+
                        "]");
                if(node!=null){
                    doc.getRootElement().remove(node);
                    if(writeDocument(doc,file)){
                        list.remove(index);
                        return true;
                    }
                }

            }catch (Exception e){
                log.error("",e);
            }
        }
        return false;
    }


    /**
     * 向XML中增加节点
     * @param doc
     * @param view
     */
    private void addElement(Document doc,View view){
        String type="List";
        if(view.getType()==ViewType.DETAIL)
            type="Detail";
        if(view.getType()==ViewType.MODIFY)
            type="Modify";
        Element ele=doc.getRootElement().addElement("view");
        ele.addAttribute("name",view.getName());
        ele.addAttribute("modelName",view.getModelName());
        ele.addAttribute("type",type);
        ele.addAttribute("demo",view.getDemo());
        ele.addAttribute("scens",view.getScene());

        if(view instanceof ViewList){
            writeProperty(ele,((ViewList)view).getLiterms());
        }else{
            Map<String,List<ViewIterm>> map=((ViewDetail)view).getLiterms();
            Element groupEle;
            for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext(); ) {
                String groupName =  iterator.next();
                groupEle=ele.addElement("group");
                groupEle.attributeValue("name",groupName);
                writeProperty(groupEle,map.get(groupName));
            }
        }
    }

    private void writeProperty(Element ele,List<ViewIterm> list){
        Element property=null;
        for(ViewIterm iterm:list){
            property=ele.addElement("property");
            property.addAttribute("index",""+iterm.getIndex());
            property.addAttribute("title",""+iterm.getTitle());
            property.addAttribute("propertyName",""+iterm.getPropertyName());
            property.addAttribute("propertyType",""+iterm.getPtopertyType());
        }
    }

    /**
     * 将XML写入文件
     * @param doc
     * @param file
     * @return
     */
    private boolean writeDocument(Document doc,File file){
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
