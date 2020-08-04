package com.xtxb.cmdb.api;

import com.xtxb.cmdb.common.model.Property;
import com.xtxb.cmdb.common.model.PropertyType;
import com.xtxb.cmdb.common.query.*;
import com.xtxb.cmdb.common.value.Resource;
import com.xtxb.cmdb.service.ModelService;
import com.xtxb.cmdb.service.ResourceService;
import com.xtxb.cmdb.util.JsonUtil;
import com.xtxb.cmdb.util.LoggerUtil;
import com.xtxb.cmdb.util.ResourceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月27日-下午5:53
 * <p>
 * <p>
 * 提供对外的资源实例API
 */

@Service
@Path("/ci")
public class ResourceAPI extends BaseAPI{

    @Autowired
    private ResourceService rservice;

    @Autowired
    private ModelService mservice;

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private LoggerUtil log;

    /**
     * 根据OID查询资源实例
     * @param oid URL参数
     * @param user  URL参数
     * @return
     */
    @Path("/single/oid")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getResourceByOid(@QueryParam("oid") long oid,@QueryParam("user") String user){
        Map<String,Object> value=getReturnMap();

        Resource res=rservice.getResource(oid,user);
        if(res==null){
            value.put("code",ERROR);
            value.put("message","查询OID="+oid+" 的资源实例失败");
        }
        value.putAll(getValueMap(res));
        return jsonUtil.getJosnStr(value);
    }

    /**
     * 根据SID查询资源实例
     * @param sid URL参数
     * @param user URL参数
     * @return
     */
    @Path("/single/sid")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getResourceBySid(@QueryParam("sid") String sid,@QueryParam("user") String user){
        Map<String,Object> value=getReturnMap();

        Resource res=rservice.getResource(sid,user);
        if(res==null){
            value.put("code",ERROR);
            value.put("message","查询SID="+sid+" 的资源实例失败");
        }
        value.putAll(getValueMap(res));
        return jsonUtil.getJosnStr(value);
    }

    /**
     * 根据资源类型查询资源实例
     * @param modelName
     * @param user
     * @param pageIndex
     * @param pageLen
     * @return
     */
    @Path("/multi/type")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,Object> queryByModelClass(@QueryParam("modelName") String modelName,@QueryParam("user") String user,
                                                @QueryParam("pageIndex") int pageIndex,@QueryParam("pageLen") int pageLen){
        Map<String,Object> value=getReturnMap();
        if(pageIndex<=0)
            pageIndex=1;
        if(pageLen<=0)
            pageLen=100;
        if(modelName==null){
            value.put("code",ERROR);
            value.put("message","资源类型为NULL");
            return value;
        }

        try {
            List<Resource> list= rservice.getResources(modelName,pageIndex,pageLen,user);
            setReturnMap(value,list,pageIndex,pageLen);
        } catch (Exception e) {
            log.error("",e);
            value.put("code",ERROR);
            value.put("message","添加资源实例时必须声明资源类型!");
        }
        return value;
    }

    /**
     * 根据资源属性查询资源实例
     * @param modelName
     * @param user
     * @param pageIndex
     * @param pageLen
     * @param whereStr
     * @return
     */
    @Path("/multi/property")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Map<String,Object> queryByProperty(@QueryParam("modelName") String modelName,@QueryParam("user") String user,
                                              @QueryParam("pageIndex") int pageIndex,@QueryParam("pageLen") int pageLen,
                                              @RequestBody String whereStr){
        Map<String,Object> value=getReturnMap();
        if(pageIndex<=0)
            pageIndex=1;
        if(pageLen<=0)
            pageLen=100;
        if(modelName==null){
            value.put("code",ERROR);
            value.put("message","资源类型为NULL");
            return value;
        }
        if (whereStr==null|| whereStr.equals(""))
            return queryByModelClass(modelName,user,pageIndex,pageLen);
        try {
            List<Resource> list= rservice.queryResources(modelName,pageIndex,pageLen,user,getQueryIterm(whereStr));
            setReturnMap(value,list,pageIndex,pageLen);
        } catch (Exception e) {
            log.error("",e);
            value.put("code",ERROR);
            value.put("message","添加资源实例时必须声明资源类型!");
        }
        return value;
    }

    /**
     * 添加资源
     * @param resList
     * @return
     */
    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String,Object> addResource(@QueryParam("user") String user, @RequestBody List<Map<String,Object>> resList){
        Map<String,Object> value=getReturnMap();
        if(resList==null) {
            value.put("code",ERROR);
            value.put("message","待添加的资源实例为NULL!");
            return value;
        }

        Resource[] list=new Resource[resList.size()];
        int i=0;

        for(Map<String ,Object> map:resList){
            list[i]=getResource(map,user,value);
            if(list[i]==null)
                return value;
            if(rservice.getResource(list[i].getSid(),user)!=null){
                value.put("code",ERROR);
                value.put("message","资源实例名称重复:"+list[i].getSid());
                return value;
            }
            i++;
        }
        try {
            if(rservice.addResources(user,list))
                return value;
            value.put("code",ERROR);
            value.put("message","添加资源实例失败!");
        } catch (Exception e) {
            log.error("",e);
            value.put("code",ERROR);
            value.put("message","添加资源实例失败!"+e.getMessage());
        }
        return value;
    }

    /**
     * 更新资源
     * @param user
     * @param resources
     * @return
     */
    @Path("/update")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String,Object> updateResource(@QueryParam("user") String user, @RequestBody List<Map<String,Object>> resources){
        Map<String,Object> value=getReturnMap();
        if(resources==null || resources.isEmpty()){
            value.put("code",ERROR);
            value.put("message","待更新的资源实例为NULL!");
        }

        Resource[] list=new Resource[resources.size()];
        int i=0;
        Resource temp=null;
        for(Map<String,Object> resource:resources){
            list[i]=getResource(resource,user,value);
            if(list[i]==null)
                return value;
            temp=rservice.getResource(list[i].getSid(),user);
            if(temp!=null && temp.getOid()!=list[i].getOid() && temp.getSid().equals(list[i].getSid())){
                value.put("code",ERROR);
                value.put("message","资源实例名称重复:"+list[i].getSid());
                return value;
            }
            i++;
        }

        try {
            if(rservice.updateResources(user,list)){
               return value;
            }
            value.put("code",ERROR);
            value.put("message","更新资源实例失败!");
        } catch (Exception e) {
            log.error("",e);
            value.put("code",ERROR);
            value.put("message","更新资源实例失败!"+e.getMessage());
        }
        return value;
    }

    @Path("/delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Map<String,Object> deleteResource(@QueryParam("user") String user, @RequestBody String ids){
        Map<String,Object> value=getReturnMap();
        if(ids==null){
            value.put("code",ERROR);
            value.put("message","待删除的资源实例为NULL!");
        }
        String[] array=ids.split(",");
        Resource[] list=new Resource[array.length];
        int i=0;
        for(String oid:array){
            list[i]=rservice.getResource(Long.parseLong(oid),user);
            if(list[i++]==null){
                value.put("code",ERROR);
                value.put("message","不存在OID="+oid+"的资源对象!");
                return value;
            }
        }

        try {
            if(rservice.deleteResources(user,list)){
                return value;
            }
            value.put("code",ERROR);
            value.put("message","删除资源实例失败");
        } catch (Exception e) {
            log.error("",e);
            value.put("code",ERROR);
            value.put("message","删除资源实例失败"+e.getMessage());
        }

        return value;
    }


    private Resource getResource(Map<String,Object> value,String user,Map<String,Object> returnMap){
        Resource res=new Resource();
        if(!value.containsKey("modelName") || !value.containsKey("oid") || !value.containsKey("sid") ){
            returnMap.put("code",ERROR);
            returnMap.put("message","资源实例时必须声明资源类型和为一标识!");
            return null;
        }
        res.setModelName((String) value.remove("modelName"));
        if(value.containsKey("oid")) {
            res.setOid(Long.parseLong(value.remove("oid")+""));
        }
        res.setSid((String)value.remove("sid"));
        PropertyType type;
        for (Iterator<String> iterator = value.keySet().iterator(); iterator.hasNext(); ) {
            String propertyName =  iterator.next();
            type=mservice.getProperty(res.getModelName(),propertyName).getType();
            res.setValue(propertyName,ResourceUtil.convertValueToStore(value.get(propertyName),type));
        }
        return res;
    }

    private QueryIterm[] getQueryIterm(String whereStr){
        List<QueryIterm> list=new ArrayList<>();
        String[] array=whereStr.replaceAll("\\("," ( ")
                .replaceAll("\\)"," ) ")
                .replaceAll(KeyPair.EQUALS,  " "+KeyPair.EQUALS+" ")
                .replaceAll(KeyPair.EQUALS_NOT,  " "+KeyPair.EQUALS_NOT+" ")
                .replaceAll(KeyPair.GREATER_THEN_AND,  " "+KeyPair.GREATER_THEN_AND+" ")
                .replaceAll(KeyPair.GREATER_THEN_AND_EQUALS,  " "+KeyPair.GREATER_THEN_AND_EQUALS+" ")
                .replaceAll(KeyPair.LESS_THEN,  " "+KeyPair.LESS_THEN+" ")
                .replaceAll(KeyPair.LESS_THEN_AND_EQUALS,  " "+KeyPair.LESS_THEN_AND_EQUALS+" ")
                .replaceAll(KeyPair.LIKE,  " "+KeyPair.LIKE+" ")
                .trim()
                .split(" ");

        boolean findEqua=false;
        String property=null;
        String compareType=null;
        for(String sec:array){
            if(sec.trim().equals(""))
                continue;
            if(sec.equals("("))
                list.add(new Group(GroupType.LEFT));
            else if(sec.equals(")"))
                list.add(new Group(GroupType.RIGHT));
            else if(sec.toUpperCase().equals("OR"))
                list.add(new Logic(LogicType.OR));
            else if(sec.toUpperCase().equals("AND"))
                list.add(new Logic(LogicType.AND));
            else if(isCompare(sec)){
                findEqua=true;
                compareType=sec;
            }else{
                if(!findEqua)
                    property=sec;
                else {
                    findEqua=false;
                    list.add(new KeyPair(property, sec,compareType));
                }
            }
        }
        return  list.toArray(new QueryIterm[list.size()]);
    }

    private boolean isCompare(String sec){
        if(sec.equals(KeyPair.EQUALS) || sec.equals(KeyPair.EQUALS_NOT)
                || sec.equals(KeyPair.GREATER_THEN_AND) || sec.equals(KeyPair.GREATER_THEN_AND_EQUALS)
                || sec.equals(KeyPair.LESS_THEN) || sec.equals(KeyPair.LESS_THEN_AND_EQUALS)
                || sec.equals(KeyPair.LIKE))
            return true;
        return false;
    }

    private void setReturnMap(Map<String,Object> value,List<Resource> list,int pageIndex,int pageLen){
        if(list!=null){
            value.put("size",list.size());
            value.put("pageIndex",pageIndex);
            value.put("pageLen",pageLen);
            List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
            value.put("list",mapList);
            for( Resource res:list){
                mapList.add(getValueMap(res));
            }
        }
    }

    private Map<String ,Object> getValueMap(Resource res){
        Map<String,Object> value=new HashMap<>();
        List<Property> properties=mservice.getProperties(res.getModelName());
        if(properties==null) {
            value.put("code",ERROR);
            value.put("message","查询OID="+res.getOid()+" 关联的资源类型属性失败");
            return value;
        }

        Map<String,Object> temp=new HashMap<>();
        value.put("id",temp);
        temp.put("oid",res.getOid());
        temp.put("sid",res.getSid());
        temp=new HashMap<>();
        value.put("type",temp);
        temp.put("modelName",res.getModelName());
        temp.put("cnName",mservice.getModelByName(res.getModelName()).getDescr());

        List<Map<String,Object>> plist=new ArrayList<>();
        value.put("properties",plist);
        for(Property pro:properties){
            if(pro.getName().equals("oid") || pro.getName().equals("sid") || pro.getName().equals("modelName"))
                continue;
            temp=new HashMap<>();
            temp.put("name",pro.getName());
            temp.put("cnName",pro.getDescr());
            temp.put("value", ResourceUtil.convertValueToView(res.getValue(pro.getName()),pro.getType()));
            plist.add(temp);
        }
        return value;
    }
}
