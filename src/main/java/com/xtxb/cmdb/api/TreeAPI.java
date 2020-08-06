package com.xtxb.cmdb.api;

import com.xtxb.cmdb.common.tree.ModelClassTree;
import com.xtxb.cmdb.common.tree.TreeNode;
import com.xtxb.cmdb.service.ModelTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年08月04日-下午7:57
 * <p>
 * <p>
 *
 */
@Service
@Path("/tree")
public class TreeAPI extends BaseAPI{

    @Autowired
    private ModelTreeService service;

    /**
     * 查询系统中所有的资源类型树名称
     * @param user
     * @return
     */
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getTreeNames(@QueryParam("user") String user){
        Map<String,Object> returnMap=getReturnMap();
        returnMap.put("list",service.getTreeNames());
        return returnMap;
    }

    /**
     * 查询资源类型树节点信息
     * @param user
     * @param name
     * @return
     */
    @Path("/single")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getTree(@QueryParam("user") String user,@QueryParam("name") String name){
        Map<String,Object> returnMap=getReturnMap();
        ModelClassTree tree=service.getTree(name);
        if(tree==null){
            returnMap.put("code",ERROR);
            returnMap.put("message","没有查询到name为:"+name+"的资源类型树");
            return returnMap;
        }

        Map<String , Object> map=new HashMap<>();
        returnMap.put("tree",map);
        map.put("name",tree.getName());
        map.put("cnName",tree.getCnName());
        map.put("nodes",new ArrayList<Map<String,String>>());
        getNode((List)map.get("nodes"),tree.getNodes());
        return returnMap;
    }

    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, Object> addTree(@QueryParam("user") String user,@RequestBody Map<String,Object> map){
        Map<String,Object> returnMap=getReturnMap();
        if(map==null || map.isEmpty()){
            returnMap.put("code",ERROR);
            returnMap.put("message","待添加的资源类型树为NUL");
            return returnMap;
        }

        ModelClassTree  tree=new ModelClassTree();
        tree.setName((String)map.get("name"));
        tree.setCnName((String)map.get("cnName"));
        TreeNode[] nodes=getNodes((List<Map<String,Object>>)map.get("nodes"));
        tree.setNodes(nodes);
        if(!service.addTree(tree)){
            returnMap.put("code",ERROR);
            returnMap.put("message","添加资源类型树失败");
        }
        return returnMap;
    }

    @Path("/update")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, Object> updateTree(@QueryParam("user") String user,@RequestBody Map<String,Object> map){
        Map<String,Object> returnMap=getReturnMap();
        if(map==null || map.isEmpty()){
            returnMap.put("code",ERROR);
            returnMap.put("message","待修改的资源类型树为NUL");
            return returnMap;
        }

        if(service.getTree((String)map.get("name"))==null){
            returnMap.put("code",ERROR);
            returnMap.put("message","不存在name为"+(String)map.get("name")+"的资源类型树");
            return returnMap;
        }else {
            ModelClassTree tree = new ModelClassTree();
            tree.setName((String) map.get("name"));
            tree.setCnName((String) map.get("cnName"));
            TreeNode[] nodes = getNodes((List<Map<String, Object>>) map.get("nodes"));
            tree.setNodes(nodes);
            if(!service.deleteTree((String)map.get("name")) ||
                !service.addTree(tree)) {
                returnMap.put("code",ERROR);
                returnMap.put("message","修改资源类型树失败");
            }
        }
        return returnMap;
    }

    @Path("/delete")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> deleteTree(@QueryParam("user") String user,@QueryParam("name") String name){
        Map<String,Object> returnMap=getReturnMap();
        if(service.getTree(name)==null){
            returnMap.put("code",ERROR);
            returnMap.put("message","不存在name为"+name+"的资源类型树");
            return returnMap;
        }
        if(!service.deleteTree(name)){
            returnMap.put("code",ERROR);
            returnMap.put("message","删除资源类型树失败");
        }
        return returnMap;
    }

    private TreeNode[] getNodes(List<Map<String,Object>> list){
        if(list==null || list.isEmpty())
            return null;

        TreeNode[] nodes=new TreeNode[list.size()];
        TreeNode[] temp=null;
        int i=0;
        for(Map<String,Object> map:list){
            nodes[i]=new TreeNode();
            nodes[i].setCnName((String)map.get("name"));
            nodes[i].setModelClass((String)map.get("modelName"));
            nodes[i].setLabel((Boolean)map.get("isLabel"));
            nodes[i].setUrl((String)map.get("url"));
            temp=getNodes((List<Map<String,Object>>)map.get("nodes"));
            if(temp!=null)
                nodes[i].setChildNode(temp);
            i++;
        }
        return nodes;
    }

    private void getNode(List<Map<String,Object>> list, TreeNode[] array){
        if(array==null || array.length==0)
            return;

        Map<String,Object>  map=null;
        for(TreeNode node:array){
            map=new HashMap<>();
            map.put("name",node.getCnName());
            map.put("modelName",node.getModelClass());
            map.put("url",node.getUrl()) ;
            map.put("isLabel",node.isLabel());
            list.add(map);
            if(node.getChildNode()==null || node.getChildNode().length==0)
                continue;
            map.put("nodes",new ArrayList<Map<String,String>>());
            getNode((List)map.get("nodes"),node.getChildNode());
        }
    }
}
