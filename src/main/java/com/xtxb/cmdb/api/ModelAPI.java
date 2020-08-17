package com.xtxb.cmdb.api;

import com.xtxb.cmdb.common.model.*;
import com.xtxb.cmdb.service.ModelService;
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

    /**
     * 添加资源类型
     * @param name
     * @param descr
     * @param user
     * @return
     */
    @Path("/model/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,Object> addModel(@QueryParam("name") String name,@QueryParam("descr") String descr,@QueryParam("user") String user){
        Map<String,Object> returnMap=getReturnMap();
        if(name==null || descr==null){
            returnMap.put("code",ERROR);
            returnMap.put("message","name或descr参数为NULL");
            return returnMap;
        }

        ModelClass model=new ModelClass(name,descr);
        try {
            if(service.getModelByName(name)!=null || service.getModelByDescr(descr)!=null){
                returnMap.put("code",ERROR);
                returnMap.put("message","添加的资源类型重复");
                return returnMap;
            }
            if (!service.addModel(model)) {
                returnMap.put("code",ERROR);
                returnMap.put("message","添加资源类型失败");
            }
        } catch (Exception e) {
            log.error("",e);
            returnMap.put("code",ERROR);
            returnMap.put("message","添加资源类型失败:"+e.getMessage());
        }
        return returnMap;
    }

    /**
     * 添加资源属性
     * @param user
     * @param properties
     * @return
     */
    @Path("/property/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String,Object> addProperty(@QueryParam("user") String user, @RequestBody List<Map<String,Object>> properties){
        Map<String,Object> returnMap=getReturnMap();
        if(properties==null || properties.isEmpty()){
            returnMap.put("code",ERROR);
            returnMap.put("message","待添加的资源属性为NULL");
            return returnMap;
        }

        Property[] array=new Property[properties.size()];
        int i=0;
        for(Map<String,Object> map:properties){
            array[i]=getProperty(map);
            if(service.getProperty(array[i].getModelName(),array[i].getName())!=null){
                returnMap.put("code",ERROR);
                returnMap.put("message","待添加的资源属性与现有属性重复");
                return returnMap;
            }
            i++;
        }

        try {
            if(!service.addProperty(array)){
                returnMap.put("code",ERROR);
                returnMap.put("message","添加资源属性失败");
            }
        } catch (Exception e) {
            log.error("",e);
            returnMap.put("code",ERROR);
            returnMap.put("message","添加资源属性失败:"+e.getMessage());
        }
        return returnMap;
    }

    /**
     * 添加资源关系
     * @param user
     * @param ship
     * @return
     */
    @Path("/relationship/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String,Object> addRelationShip(@QueryParam("user") String user, @RequestBody Map<String,Object> ship){
        Map<String,Object> returnMap=getReturnMap();
        if(ship==null || ship.isEmpty()){
            returnMap.put("code",ERROR);
            returnMap.put("message","待添加的资源资源关系类型为NULL");
            return returnMap;
        }

        String name=(String)ship.get("name");
        String descr=(String)ship.get("descr");
        String sourceModel=(String)ship.get("sourceModel");
        String targetModel=(String)ship.get("targetModel");

        try {
            if(service.getRelationShip(name)!=null){
                returnMap.put("code",ERROR);
                returnMap.put("message","添加资源关系重复");
            }
            else if(!service.addRelationShip(new RelationShip(name,descr,sourceModel,targetModel))){
                returnMap.put("code",ERROR);
                returnMap.put("message","添加资源关系类型失败");
            }
        } catch (Exception e) {
            log.error("",e);
            returnMap.put("code",ERROR);
            returnMap.put("message","添加资源关系类型失败:"+e.getMessage());
        }
        return returnMap;
    }

    /**
     * 修改资源类型
     * @param name
     * @param descr
     * @param user
     * @return
     */
    @Path("/model/update")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String,Object> updateModel(@QueryParam("name") String name,@QueryParam("descr") String descr,@QueryParam("user") String user){
        Map<String,Object> returnMap=getReturnMap();
        if(descr==null){
            returnMap.put("code",ERROR);
            returnMap.put("message","descr参数为NULL");
            return returnMap;
        }

        ModelClass model=service.getModelByName(name);
        if(model==null) {
            returnMap.put("code",ERROR);
            returnMap.put("message","不存在name为"+name+"资源类型");
            return returnMap;
        }
        ModelClass tempc=service.getModelByDescr(descr);
        if(tempc!=null && !tempc.getName().equals(model.getName())){
            returnMap.put("code",ERROR);
            returnMap.put("message","资源类型名称与现有类型重复");
            return returnMap;
        }
        model.setDescr(descr);
        try {
            if(!service.updateModel(model)){
                returnMap.put("code",ERROR);
                returnMap.put("message","更新资源类型失败复");
                return returnMap;
            }
        } catch (Exception e) {
            log.error("",e);
            returnMap.put("code",ERROR);
            returnMap.put("message","更新资源类型失败:"+e.getMessage());
        }
        return returnMap;
    }


    /**
     * 修改资源属性
     * @param user
     * @param properties
     * @return
     */
    @Path("/property/update")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String,Object> updateProperty(@QueryParam("user") String user, @RequestBody List<Map<String,Object>> properties){
        Map<String,Object> returnMap=getReturnMap();
        if(properties==null || properties.isEmpty()){
            returnMap.put("code",ERROR);
            returnMap.put("message","待修改的资源属性为NULL");
            return returnMap;
        }

        Property[] array=new Property[properties.size()];
        int i=0;
        Property temp=null;
        for(Map<String,Object> map:properties){
            array[i]=getProperty(map);
            temp=service.getProperty(array[i].getModelName(),array[i].getName());
            if(temp==null){
                returnMap.put("code",ERROR);
                returnMap.put("message","待修改的资源属性不存在");
                return returnMap;
            }
            i++;
        }

        try {
            if(!service.updateProperty(array)){
                returnMap.put("code",ERROR);
                returnMap.put("message","更新资源属性失败");
            }
        } catch (Exception e) {
            log.error("",e);
            returnMap.put("code",ERROR);
            returnMap.put("message","更新资源属性失败:"+e.getMessage());
        }
        return returnMap;
    }


    /**
     * 修改资源关系
     * @param user
     * @param ship
     * @return
     */
    @Path("/relationship/update")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String,Object> updateRelationShip(@QueryParam("user") String user, @RequestBody Map<String,Object> ship){
        Map<String,Object> returnMap=getReturnMap();
        if(ship==null || ship.isEmpty()){
            returnMap.put("code",ERROR);
            returnMap.put("message","待修改的资源资源关系类型为NULL");
            return returnMap;
        }

        String name=(String)ship.get("name");
        String descr=(String)ship.get("descr");
        String sourceModel=(String)ship.get("sourceModel");
        String targetModel=(String)ship.get("targetModel");

        try {
            if(service.getRelationShip(name)==null){
                returnMap.put("code",ERROR);
                returnMap.put("message","待修改的资源关系不存在");
            }
            else if(!service.updateRelationShip(new RelationShip(name,descr,sourceModel,targetModel))){
                returnMap.put("code",ERROR);
                returnMap.put("message","修改资源关系类型失败");
            }
        } catch (Exception e) {
            log.error("",e);
            returnMap.put("code",ERROR);
            returnMap.put("message","修改资源关系类型失败:"+e.getMessage());
        }
        return returnMap;
    }

    /**
     * 删除资源类型
     * @param name
     * @param user
     * @return
     */
    @Path("/model/delete")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String,Object> deleteModel(@QueryParam("name") String name,@QueryParam("user") String user){
        Map<String,Object> returnMap=getReturnMap();
        if(name==null){
            returnMap.put("code",ERROR);
            returnMap.put("message","name参数为NULL");
            return returnMap;
        }

        try {

            if(service.getModelByName(name)==null){
                returnMap.put("code",ERROR);
                returnMap.put("message","不存在name为"+name+"的资源类型");
                return returnMap;
            }

            List<Property> list=service.getProperties(name);
            if(list!=null){
                service.deleteProperties(list);
            }

            List<RelationShip> list1=service.getRelationShips(name,true);
            if(list1!=null) {
                for(RelationShip ship:list1)
                    service.deleteRelationShip(ship.getName());
            }
            list1=service.getRelationShips(name,false);
            if(list1!=null) {
                for(RelationShip ship:list1)
                    service.deleteRelationShip(ship.getName());
            }
        } catch (Exception e) {
            log.error("",e);
            returnMap.put("code",ERROR);
            returnMap.put("message","删除资源类型关联信息失败:"+e.getMessage());
            return returnMap;
        }

        try{
            if(!service.deleteModel(name)){
                returnMap.put("code",ERROR);
                returnMap.put("message","删除资源类型失败");
                return returnMap;
            }
        } catch (Exception e) {
            log.error("",e);
            returnMap.put("code",ERROR);
            returnMap.put("message","删除资源类型失败:"+e.getMessage());
            return returnMap;
        }
        return returnMap;
    }

    /**
     * 删除资源属性
     * @param user
     * @param properties
     * @return
     */
    @Path("/property/delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String,Object> deleteProperty(@QueryParam("user") String user, @RequestBody List<Map<String,Object>> properties){
        Map<String,Object> returnMap=getReturnMap();
        if(properties==null || properties.isEmpty()){
            returnMap.put("code",ERROR);
            returnMap.put("message","待修改的资源属性为NULL");
            return returnMap;
        }

        Property[] array=new Property[properties.size()];
        int i=0;
        for(Map<String,Object> map:properties){
            array[i++]=getProperty(map);
        }

        try {
            if(!service.deleteProperties(array)){
                returnMap.put("code",ERROR);
                returnMap.put("message","删除资源属性失败");
            }
        } catch (Exception e) {
            log.error("",e);
            returnMap.put("code",ERROR);
            returnMap.put("message","删除资源属性失败:"+e.getMessage());
        }
        return returnMap;
    }

    /**
     * 删除资源关系
     * @param user
     * @param name
     * @return
     */
    @Path("/relationship/delete")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,Object> deleteRelationShip(@QueryParam("user") String user, @QueryParam("name") String name){
        Map<String,Object> returnMap=getReturnMap();
        if(name==null){
            returnMap.put("code",ERROR);
            returnMap.put("message","待修改的资源资源关系类型为NULL");
            return returnMap;
        }
        if(service.getRelationShip(name)==null){
            returnMap.put("code",ERROR);
            returnMap.put("message","不存在name为"+name+"的资源关系");
            return returnMap;
        }

        try {
            if(!service.deleteRelationShip(name)){
                returnMap.put("code",ERROR);
                returnMap.put("message","删除资源关系失败");
            }
        } catch (Exception e) {
            log.error("",e);
            returnMap.put("code",ERROR);
            returnMap.put("message","删除资源关系失败:"+e.getMessage());
        }
        return returnMap;
    }

    private Property getProperty(Map<String,Object> map){
        Property property=new Property();
        property.setName((String)map.get("name"));
        property.setDescr((String)map.get("descr"));
        property.setModelName((String)map.get("modelName"));
        property.setGroup((String)map.get("group"));
        property.setDefValue(map.get("defValue")+"");
        if(map.get("rule")!=null && !map.get("rule").toString().equals("")){
            MatchRule rule=MatchRule.valueOf(((String)map.get("rule")).toUpperCase());
            if(rule!=null)
                property.setRule(rule);
        }
        if(map.get("matchRule")!=null && !map.get("matchRule").toString().equals(""))
            property.setMatchRule((String)map.get("matchRule"));
        if(map.get("type")!=null)
            property.setType(PropertyType.valueOf(((String)map.get("type")).toUpperCase()));
        return property;
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
