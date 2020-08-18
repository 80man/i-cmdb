package com.xtxb.cmdb.audit.value;

import com.xtxb.cmdb.audit.AopBase;
import com.xtxb.cmdb.audit.AuditLog;
import com.xtxb.cmdb.common.value.Resource;
import com.xtxb.cmdb.service.ResourceService;
import com.xtxb.cmdb.util.LoggerUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年08月18日-下午3:28
 * <p>
 * <p>
 * 记录资源相关API调用信息信息
 */
@Aspect
@Component
public class ResourceAOP extends AopBase {
    @Autowired
    private LoggerUtil log;

    @Autowired
    private AuditLog auditLog;

    @Autowired
    private ResourceService service;

    @Around("execution(* com.xtxb.cmdb.api.ResourceAPI.updateResource(..))")
    public Object modifyModel(ProceedingJoinPoint point){
        HttpServletRequest request = getRequest();
        Object result=null;
        List<Resource> resList_old=new ArrayList<>();
        List<Map<String,Object>> list=(List<Map<String,Object>>)point.getArgs()[1];
        if(list!=null){
            Resource res=null;
            for (Map<String, Object> stringObjectMap : list) {
                res=service.getResource(Long.parseLong(stringObjectMap.get("oid")+""),(String)point.getArgs()[0]);
                if(res!=null)
                    resList_old.add((Resource) res.clone());
            }
        }

        try {
            result=point.proceed();
            Map<String,Object> obj=(Map<String,Object>)result;
            if(obj!=null&& "1".equals(obj.get("code")+"") ){
                StringBuilder sb= new StringBuilder("");
                for(int i=0;i<resList_old.size();i++) {
                    StringBuilder sb_sub=new StringBuilder("[");
                    Map<String,Object> values= resList_old.get(i).getValues();
                    for (Iterator<String> iterator = values.keySet().iterator(); iterator.hasNext(); ) {
                        String pro =  iterator.next();
                        Object old_value=values.get(pro);
                        if(list.get(i).containsKey(pro)){
                            Object new_value=list.get(i).get(pro);
                            if(old_value==null && new_value!=null){
                                sb_sub.append(pro+": null -> "+new_value+", ");
                            }else if(old_value!=null){
                                if(old_value instanceof Date){
                                    if(!(((Date)old_value).getTime()+"").equals(new_value+""))
                                        sb_sub.append(pro+": "+((Date)old_value).getTime()+" -> "+new_value+", ");
                                }else if(!old_value.toString().equals(new_value+""))
                                    sb_sub.append(pro+": "+old_value+" -> "+new_value+", ");
                            }
                        }
                    }
                    if(!resList_old.get(i).getSid().equals(list.get(i).get("sid")))
                        sb_sub.append("sid: "+resList_old.get(i).getSid()+" -> "+list.get(i).get("sid")+", ");

                    if(!sb_sub.toString().endsWith("[")){
                        sb.append(sb_sub.substring(0,sb_sub.length()-2)+"];");
                    }
                }
                log(auditLog,request,"批量更新:"+(sb.length()>0?sb.substring(0,sb.length()-1):""));
            }
        } catch (Throwable throwable) {
            log.error("",throwable);
        }

        return result;
    }
}
