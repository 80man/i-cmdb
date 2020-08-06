package com.xtxb.cmdb.api;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年08月04日-下午7:56
 * <p>
 * <p>
 *
 */
@Service
@Path("/view")
public class ViewAPI extends BaseAPI{

    /**
     * 查询资源类型相关的视图
     * @param user
     * @param modelName
     * @return
     */
    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getViews(@QueryParam("user") String user,@QueryParam("modelName") String modelName){
        Map<String,Object> returnMap=getReturnMap();

        return returnMap;
    }

    /**
     * 查询单个视图
     * @param user
     * @param type
     * @param demo
     * @param scene
     * @param name
     * @param modelName
     * @return
     */
    @Path("/single")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getView(@QueryParam("user") String user,@QueryParam("type") String type,
                                       @QueryParam("demo") String demo,@QueryParam("scene") String scene,
                                       @QueryParam("name") String name,@QueryParam("modelName") String modelName){
        Map<String,Object> returnMap=getReturnMap();

        return returnMap;
    }

    /**
     * 添加视图
     * @param user
     * @param view
     * @return
     */
    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, Object> addView(@QueryParam("user") String user,@RequestBody Map<String,Object> view){
        Map<String,Object> returnMap=getReturnMap();

        return returnMap;
    }

    /**
     * 修改视图
     * @param user
     * @param tree
     * @return
     */
    @Path("/update")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, Object> updateView(@QueryParam("user") String user,@RequestBody Map<String,Object> tree){
        Map<String,Object> returnMap=getReturnMap();

        return returnMap;
    }

    /**
     * 删除视图
     * @param user
     * @param type
     * @param demo
     * @param scene
     * @param name
     * @param modelName
     * @return
     */
    @Path("/delete")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> deleteTree(@QueryParam("user") String user,@QueryParam("type") String type,
                                          @QueryParam("demo") String demo,@QueryParam("scene") String scene,
                                          @QueryParam("name") String name,@QueryParam("modelName") String modelName){
        Map<String,Object> returnMap=getReturnMap();

        return returnMap;
    }

}
