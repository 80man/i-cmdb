package com.xtxb.cmdb.api;

import com.xtxb.cmdb.common.model.ModelClass;
import com.xtxb.cmdb.common.model.Property;
import com.xtxb.cmdb.common.model.RelationShip;
import com.xtxb.cmdb.service.ModelService;
import com.xtxb.cmdb.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年08月04日-下午5:04
 * <p>
 * <p>
 * 资源类型外部API
 */
@Service
@Path("/meta")
public class ModelAPI  extends BaseAPI{

    @Autowired
    private ModelService service;

    @Autowired
    private LoggerUtil log;

    /**
     * 根据在英文名称或中文名称查询资源类型
     * @param property
     * @param value
     * @param user
     * @return
     */
    @Path("/model/single")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,Object>  getModel(@QueryParam("property") String property,
                                        @QueryParam("value") String value,
                                        @QueryParam("user") String user){
        Map<String,Object> returnMap=getReturnMap();
        ModelClass model=null;
        if(property.equals("name")){
            model=service.getModelByName(value);
        }else {
            model = service.getModelByDescr(value);
        }
        if(model==null) {
            returnMap.put("code",ERROR);
            returnMap.put("message","无法查询到"+property+"为"+value+"的资源类型");
        }else{
            returnMap.putAll(getModelMap(model));
        }
        return returnMap;
    }

    /**
     * 查询所有的资源类型
     * @param user
     * @return
     */
    @Path("/model/multi")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,Object>  getModels(@QueryParam("user") String user){
        Map<String,Object> returnMap=getReturnMap();
        List<ModelClass> models = service.getModels();
        List<Map<String,String>> list=new ArrayList<>();
        returnMap.put("list",list);
        returnMap.put("size",models.size());
        for(ModelClass model:models){
            list.add(getModelMap(model));
        }
        return returnMap;
    }

    /**
     * 查询资源属性定义
     * @param user
     * @return
     */
    @Path("/property/single")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,Object>  getProperty(@QueryParam("modelName") String modelName,
                                           @QueryParam("name") String name,@QueryParam("user") String user){
        Map<String,Object> returnMap=getReturnMap();

        Property property= service.getProperty(modelName, name);
        if(property!=null)
            returnMap.putAll(getPropertyMap(property));
        else{
            returnMap.put("code",ERROR);
            returnMap.put("message","无法查询到资源类型为"+modelName+",名称为"+name+"的资源属性");
        }
        return returnMap;
    }

    /**
     * 查询资源属性定义
     * @param user
     * @return
     */
    @Path("/property/multi")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,Object>  getPropertys(@QueryParam("modelName") String modelName,@QueryParam("user") String user){
        Map<String,Object> returnMap=getReturnMap();

        List<Property> propertes= null;
        try {
            propertes = service.getProperties(modelName);
            if(propertes!=null) {
                returnMap.put("size",propertes.size());
                List<Map<String,String>> list=new ArrayList<>();
                returnMap.put("list",list);
                for(Property property:propertes)
                    list.add(getPropertyMap(property));
            }else{
                returnMap.put("code",ERROR);
                returnMap.put("message","无法查询到资源类型为"+modelName+"的资源属性");
            }
        } catch (Exception e) {
            log.error("",e);
            returnMap.put("code",ERROR);
            returnMap.put("message","查询资源类型为"+modelName+"的资源属性异常"+e.getMessage());
        }

        return returnMap;
    }

    /**
     * 查询资源关系定义
     * @param name
     * @param user
     * @return
     */
    @Path("/relationship/single")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,Object>  getRelationShip(@QueryParam("name") String name,@QueryParam("user") String user){
        Map<String,Object> returnMap=getReturnMap();

        RelationShip ship= service.getRelationShip(name);
        if(ship!=null)
            returnMap.putAll(getRelationShipMap(ship));
        else{
            returnMap.put("code",ERROR);
            returnMap.put("message","无法查询到名称为"+name+"的资源关系定义");
        }
        return returnMap;
    }

    /**
     * 查询资源关系定义
     * @param modelName
     * @param isSource
     * @param user
     * @return
     */
    @Path("/relationship/multi")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,Object>  getRelationShips(@QueryParam("modelName") String modelName,
                                                @QueryParam("isSource") boolean isSource,
                                                @QueryParam("user") String user){
        Map<String,Object> returnMap=getReturnMap();

        List<RelationShip> ships= null;
        try {
            ships = service.getRelationShips(modelName,isSource);
            if(ships!=null) {
                returnMap.put("size",ships.size());
                List<Map<String,String>> list=new ArrayList<>();
                returnMap.put("list",list);
                for(RelationShip ship:ships)
                    list.add(getRelationShipMap(ship));
            }else{
                returnMap.put("code",ERROR);
                returnMap.put("message","无法查询到资源类型为"+modelName+"的相关资源关系定义");
            }
        } catch (Exception e) {
            log.error("",e);
            returnMap.put("code",ERROR);
            returnMap.put("message","查询资源类型为"+modelName+"的相关资源关系定义异常"+e.getMessage());
        }

        return returnMap;
    }

    private Map<String,String> getRelationShipMap(RelationShip ship){
        Map<String,String> map=new HashMap<>();
        map.put("name",ship.getName());
        map.put("descr",ship.getDescr());
        map.put("sourceModel",ship.getSourceModel());
        map.put("targetModel",ship.getTargetModel());
        return map;
    }


    private Map<String,String> getPropertyMap(Property property){
        Map<String,String> map=new HashMap<>();
        map.put("name",property.getName());
        map.put("descr",property.getDescr());
        map.put("modelName",property.getModelName());
        map.put("group",property.getGroup());
        map.put("defValue",property.getDefValue());
        map.put("rule",property.getRule()!=null?property.getRule().name():null);
        map.put("matchRule",property.getMatchRule());
        map.put("type",property.getType().name());
        return map;
    }

    private Map<String,String> getModelMap(ModelClass model){
        Map<String,String> map=new HashMap<>();
        map.put("name",model.getName());
        map.put("descr",model.getDescr());
        return map;
    }
}
