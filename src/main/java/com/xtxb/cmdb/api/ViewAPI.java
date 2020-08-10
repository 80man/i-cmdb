package com.xtxb.cmdb.api;

import com.xtxb.cmdb.common.view.View;
import com.xtxb.cmdb.common.view.ViewIterm;
import com.xtxb.cmdb.common.view.ViewType;
import com.xtxb.cmdb.common.view.ins.ViewDetail;
import com.xtxb.cmdb.common.view.ins.ViewList;
import com.xtxb.cmdb.service.ViewService;
import com.xtxb.cmdb.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

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

    @Autowired
    private ViewService service;

    @Autowired
    private LoggerUtil log;
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
        List<View> list=service.getViews(modelName);
        if(list==null){
            returnMap.put("code",ERROR);
            returnMap.put("message","没有查询到与"+modelName+"相关的视图");
        }else{
            returnMap.put("size",list.size());
            returnMap.put("views",getViewList(list));
        }
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
        View view=service.getView(ViewType.valueOf(type.toUpperCase()),demo,scene,name,modelName);
        if(view==null){
            returnMap.put("code",ERROR);
            returnMap.put("message","没有查询到视图");
        }else{
            returnMap.put("size",1);
            List<View> list=new ArrayList<>(1);
            list.add(view);
            returnMap.put("views",getViewList(list));
        }
        return returnMap;
    }

    /**
     * 添加视图
     * @param user
     * @param map
     * @return
     */
    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, Object> addView(@QueryParam("user") String user,@RequestBody Map<String,Object> map){
        Map<String,Object> returnMap=getReturnMap();

        if(map==null && map.isEmpty()){
            returnMap.put("code",ERROR);
            returnMap.put("message","待添加的视图为NULL");
        }

        View view=getView(map);
        if(service.getView(view.getType(),view.getDemo(),view.getScene(),view.getName(),view.getModelName())!=null){
            returnMap.put("code",ERROR);
            returnMap.put("message","待添加的视图已经存在");
        }else if(!service.addView(view)){
            returnMap.put("code",ERROR);
            returnMap.put("message","添加视图失败");
        }

        return returnMap;
    }

    /**
     * 修改视图
     * @param user
     * @param map
     * @return
     */
    @Path("/update")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, Object> updateView(@QueryParam("user") String user,@RequestBody Map<String,Object> map){
        Map<String,Object> returnMap=getReturnMap();
        if(map==null && map.isEmpty()){
            returnMap.put("code",ERROR);
            returnMap.put("message","待修改的视图为NULL");
        }

        View view=getView(map);
        if(service.getView(view.getType(),view.getDemo(),view.getScene(),view.getName(),view.getModelName())==null) {
            returnMap.put("code", ERROR);
            returnMap.put("message", "待修改的视图不存在");
        }else if(!service.deleteView(view.getType(),view.getDemo(),view.getScene(),view.getName(),view.getModelName()) ||
                !service.addView(view)){
            returnMap.put("code", ERROR);
            returnMap.put("message", "修改视图失败");
        }
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
        View view=service.getView(ViewType.valueOf(type.toUpperCase()),demo,scene,name,modelName);
        if(view==null){
            returnMap.put("code",ERROR);
            returnMap.put("message","没有查询到视图");
        }else{
           if(!service.deleteView(view.getType(),demo,scene,name,modelName)){
               returnMap.put("code",ERROR);
               returnMap.put("message","删除视图失败");
           }
        }

        return returnMap;
    }


    private View getView(Map<String,Object> map){
        View view=null;
        ViewType type=ViewType.valueOf(((String)map.get("type")).toUpperCase());
        if(type==ViewType.LIST){
            List<ViewIterm> list=new ArrayList<>();
            List<Map<String,Object>> itermList=(List)map.get("properties");
            for(Map<String,Object> itermMap:itermList){
                list.add(getIterm(itermMap));
            }
            view=new ViewList();
            ((ViewList)view).setIterms(list);
        }else{
            view=new ViewDetail();
            Map<String,List<ViewIterm>> listMap=new HashMap<>();
            List<Map<String,Object>> list=(List)map.get("properties");
            for(Map<String,Object> groupmap:list){
                String group=(String)groupmap.get("name");
                List<Map<String,Object>> itermMap=(List)groupmap.get("lists");
                List<ViewIterm> itermList=new ArrayList<>();
                for(Map<String,Object> temp:itermMap){
                    itermList.add(getIterm(temp));
                }
                listMap.put(group,itermList);
            }

            ((ViewDetail)view).setLiterms(listMap);
        }
        view.setName((String)map.get("name"));
        view.setDemo((String)map.get("demo"));
        view.setScene((String)map.get("scene"));
        view.setModelName((String)map.get("modelName"));
        view.setType(type);
        return view;
    }

    private  List<Map<String,Object>> getViewList(List<View> list){
        List<Map<String,Object>> viewList=new ArrayList<>();
        Map<String,Object> viewMap=null;
        for(View view:list) {
            viewMap=new HashMap<>();
            viewMap.put("name",view.getName());
            viewMap.put("modelName",view.getModelName());
            viewMap.put("demo",view.getDemo());
            viewMap.put("scene",view.getScene());
            viewMap.put("type",view.getType().name());
            if(view.getType()== ViewType.LIST){
                List<ViewIterm> iterms=((ViewList)view).getIterms();
                viewMap.put("properties",new ArrayList<Map<String,Object>>());
                for(ViewIterm iterm:iterms){
                    ((List)viewMap.get("properties")).add(getItermMap(iterm));
                }
            }else{
                Map<String,List<ViewIterm>> groupList=((ViewDetail)view).getLiterms();
                viewMap.put("properties",new ArrayList<Map<String,Object>>());
                Map<String,Object> groupMap=null;
                for (Iterator<String> iterator = groupList.keySet().iterator(); iterator.hasNext(); ) {
                    String group =  iterator.next();
                    groupMap=new HashMap<>();
                    groupMap.put("name",group);
                    groupMap.put("lists",new ArrayList<Map<String,String>>());
                    List<ViewIterm> subList=groupList.get(group);
                    for(ViewIterm iterm:subList){
                        ((List)groupMap.get("lists")).add(getItermMap(iterm));
                    }
                    ((List)viewMap.get("properties")).add(groupMap);
                }
            }
            viewList.add(viewMap);
        }
        return viewList;
    }

    private Map<String,Object> getItermMap(ViewIterm iterm){
        Map<String,Object> itermMap=new HashMap<>();
        itermMap.put("index",iterm.getIndex());
        itermMap.put("title",iterm.getTitle());
        itermMap.put("propertyName",iterm.getPropertyName());
        itermMap.put("propertyType",iterm.getPtopertyType());
        return itermMap;
    }

    private ViewIterm getIterm(Map<String,Object> map){
        ViewIterm iterm=new ViewIterm();
        iterm.setIndex(Integer.parseInt(map.get("index").toString()));
        iterm.setTitle((String)map.get("title"));
        iterm.setPropertyName((String)map.get("propertyName"));
        iterm.setPtopertyType(Integer.parseInt(map.get("propertyType").toString()));
        return iterm;
    }
}
