package com.xtxb.cmdb.api;

import com.xtxb.cmdb.common.value.Link;
import com.xtxb.cmdb.common.value.Resource;
import com.xtxb.cmdb.service.LinkService;
import com.xtxb.cmdb.service.ResourceService;
import com.xtxb.cmdb.util.LoggerUtil;
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
 * 日期: 2020年08月04日-下午3:17
 * <p>
 * <p>
 *
 */
@Service
@Path("/link")
public class LinkAPI extends BaseAPI{
    @Autowired
    private LoggerUtil log;

    @Autowired
    private LinkService service;
    @Autowired
    private ResourceService rservice;

    @Path("/single")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,Object> getLink(@QueryParam("type") String type,
                                      @QueryParam("sid") long sid,@QueryParam("tid") long tid,
                                      @QueryParam("user") String user){
        Map<String,Object> value=getReturnMap();
        Link link=service.getLink(type,sid,tid);
        if(link!=null){
            value.putAll(getLinkMap(link,user));
        }else{
            value.put("code",ERROR);
            value.put("message","没有查询到源端ID="+sid+",目的端ID="+tid+",类型为"+type+"的资源关系");
        }
        return value;
    }

    @Path("/multi")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,Object> getLinks(@QueryParam("type") String type,
                                      @QueryParam("id") long id,@QueryParam("isSource") boolean isSource,
                                      @QueryParam("user") String user){
        Map<String,Object> value=getReturnMap();
        List<Link> links= null;
        try {
            links = service.getLink(type,id,isSource);
            if(links!=null && !links.isEmpty()){
                List<Map<String,Object>> linkList=new ArrayList<>();
                value.put("size",links.size());
                value.put("list",linkList);
                for (Link link :links){
                    linkList.add(getLinkMap(link,user));
                }
            }else{
                value.put("code",ERROR);
                value.put("message","没有查询到资源关系");
            }
        } catch (Exception e) {
            log.error("",e);
            value.put("code",ERROR);
            value.put("message","查询资源关系对象失败");
        }
        return value;
    }

    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String,Object> addLinks(@QueryParam("user") String user , @RequestBody List<Map<String,Object>> links){
        Map<String,Object> value=getReturnMap();
        if(links==null || links.isEmpty()){
            value.put("code",ERROR);
            value.put("message","待添加的资源关系对象为空");
            return value;
        }

        Link[] array=new Link[links.size()];
        int i=0;
        for(Map<String,Object> map:links){
            array[i++]=getLink(map);
        }
        try {
            if(service.addLink(array)){
                return value;
            }else{
                value.put("code",ERROR);
                value.put("message","添加资源关系对象失败");
            }

        } catch (Exception e) {
            log.error("",e);
            value.put("code",ERROR);
            value.put("message","添加资源关系对象失败");
        }
        return value;
    }

    @Path("/delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String,Object> deleteLinks(@QueryParam("user") String user , @RequestBody List<Map<String,Object>> links){
        Map<String,Object> value=getReturnMap();
        if(links==null || links.isEmpty()){
            value.put("code",ERROR);
            value.put("message","待删除的资源关系对象为空");
            return value;
        }

        Link[] array=new Link[links.size()];
        int i=0;
        for(Map<String,Object> map:links){
            array[i++]=getLink(map);
        }
        try {
            if(service.deleteLink(array)){
                return value;
            }else{
                value.put("code",ERROR);
                value.put("message","删除资源关系对象失败");
            }

        } catch (Exception e) {
            log.error("",e);
            value.put("code",ERROR);
            value.put("message","删除资源关系对象失败");
        }
        return value;
    }

    private Link getLink(Map<String,Object> map){
        Link link=new Link(
                Long.parseLong(map.get("src_oid")+""),
                Long.parseLong(map.get("target_oid")+""),
                (String)map.get("note"));
        link.setType((String)map.get("type"));
        return link;
    }

    private Map<String,Object> getLinkMap(Link link,String user){
        Map<String,Object> map=new HashMap<>();
        map.put("type",link.getType());
        map.put("src",new HashMap<>());
        ((Map)map.get("src")).put("oid",link.getSid());
        Resource src=rservice.getResource(link.getSid(),user);
        if(src!=null){
            ((Map)map.get("src")).put("sid",src.getSid());
            ((Map)map.get("src")).put("modelName",src.getModelName());
        }
        map.put("target",new HashMap<>());
        ((Map)map.get("target")).put("oid",link.getTid());
        src=rservice.getResource(link.getTid(),user);
        if(src!=null){
            ((Map)map.get("target")).put("sid",src.getSid());
            ((Map)map.get("target")).put("modelName",src.getModelName());
        }
        map.put("note",link.getNote());
        return map;
    }
}
