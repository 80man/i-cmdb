package com.xtxb.cmdb.api;

import com.xtxb.cmdb.common.model.Property;
import com.xtxb.cmdb.common.model.PropertyType;
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
    @Consumes(MediaType.APPLICATION_JSON)
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
    @Consumes(MediaType.APPLICATION_JSON)
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
        if(resList==null)
            return null;

        Resource[] list=new Resource[resList.size()];
        int i=0;
        for(Map<String ,Object> map:resList){
            if(!map.containsKey("modelName") || !map.containsKey("oid") || !map.containsKey("sid") ){
                value.put("code",ERROR);
                value.put("message","添加资源实例时必须声明资源类型!");
                return value;
            }
            list[i]=new Resource();
            list[i].setModelName((String) map.remove("modelName"));
            list[i].setSid((String)map.remove("sid"));
            if(rservice.getResource(list[i].getSid(),user)!=null){
                value.put("code",ERROR);
                value.put("message","资源实例名称重复:"+list[i].getSid());
                return value;
            }
            PropertyType type;
            for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext(); ) {
                String propertyName =  iterator.next();
                if(propertyName.equals("oid") || propertyName.equals("sid")|| propertyName.equals("modelName"))
                    continue;
                type=mservice.getProperty(list[i].getModelName(),propertyName).getType();
                list[i].setValue(propertyName,ResourceUtil.convertValueToStore(map.get(propertyName),type));
            }
            i++;
        }
        try {
            rservice.addResources(user,list);
        } catch (Exception e) {
            log.error("",e);
            value.put("code",ERROR);
            value.put("message","添加资源实例失败!"+e.getMessage());
        }
        return value;
    }


    private Map<String ,Object> getValueMap(Resource res){
        Map<String,Object> value=new HashMap<>();
        List<Property> properties=mservice.getProperties(res.getModelName());
        if(properties==null) {
            value.put("code",ERROR);
            value.put("message","查询OID="+res.getOid()+" 关联的资源类型属性失败");
            return value;
        }

        value.put("id",new HashMap<>());
        ((Map)value.get("id")).put("oid",res.getOid());
        ((Map)value.get("id")).put("sid",res.getSid());
        value.put("type",new HashMap<>());
        ((Map)value.get("type")).put("modelName",res.getModelName());
        ((Map)value.get("type")).put("cnName",mservice.getModelByName(res.getModelName()).getDescr());
        value.put("properties",new ArrayList<Map<String,Object>>());
        Map<String,Object> temp=null;
        for(Property pro:properties){
            if(pro.getName().equals("oid") || pro.getName().equals("sid") || pro.getName().equals("modelName"))
                continue;
            temp=new HashMap<>();
            temp.put("name",pro.getName());
            temp.put("cnName",pro.getDescr());
            temp.put("value", ResourceUtil.convertValueToView(res.getValue(pro.getName()),pro.getType()));
            ((List)value.get("properties")).add(temp);
        }
        return value;
    }
}
