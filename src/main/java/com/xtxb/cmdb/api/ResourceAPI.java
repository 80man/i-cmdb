package com.xtxb.cmdb.api;

import com.xtxb.cmdb.common.value.Resource;
import com.xtxb.cmdb.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
    private ResourceService service;

    @Path("/{oid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Resource getResource(@PathParam("oid") long oid){
        Resource res=service.getResource(oid,"");
        return res;
    }
}
