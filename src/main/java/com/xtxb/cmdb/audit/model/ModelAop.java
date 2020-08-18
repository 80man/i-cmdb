package com.xtxb.cmdb.audit.model;

import com.xtxb.cmdb.audit.AopBase;
import com.xtxb.cmdb.audit.AuditLog;
import com.xtxb.cmdb.common.model.ModelClass;
import com.xtxb.cmdb.common.model.Property;
import com.xtxb.cmdb.service.ModelService;
import com.xtxb.cmdb.util.LoggerUtil;
import com.xtxb.cmdb.util.ResourceUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年08月17日-上午10:47
 * <p>
 * <p>
 * 记录模型相关API调用信息信息
 */
@Aspect
@Component
public class ModelAop extends AopBase {
    @Autowired
    private LoggerUtil log;

    @Autowired
    private AuditLog auditLog;
    @Autowired
    private ModelService service;

    @Around("execution(* com.xtxb.cmdb.api.ModelAPI.updateModel(..))")
    public Object modifyModel(ProceedingJoinPoint point){
        HttpServletRequest request = getRequest();
        Object[] args=point.getArgs();
        ModelClass model=service.getModelByName((String)args[0]);
        String descr=null;
        if(model!=null)
            descr=model.getDescr();
        Object result=null;
        try {
            result=point.proceed();
            Map<String,Object> obj=(Map<String,Object>)result;
            if(obj!=null&& "1".equals(obj.get("code")+"") ){
                auditLog.log(
                        getHostName(request),
                        getIP(request),
                        request.getRemoteUser(),
                        request.getSession().getId(),
                        request.getRequestedSessionId(),
                        "Value",
                        request.getRequestURI(),
                        "资源类型:"+args[0]+" 的名称由:"+descr+"更新为:"+args[1]);
            }
        } catch (Throwable throwable) {
            log.error("",throwable);
        }
        return result;
    }

    @Around("execution(* com.xtxb.cmdb.api.ModelAPI.updateProperty(..))")
    public  Object modifyProperty(ProceedingJoinPoint point){
        HttpServletRequest request = getRequest();
        Object[] args=point.getArgs();
        List<Map<String,Object>> properties=(List<Map<String,Object>>)args[1];
        List<Map<String,String>> properties_old=new ArrayList<>();
        Property temp=null;
        for (Map<String, Object> property : properties) {
            temp=service.getProperty((String)property.get("modelName"),(String)property.get("name"));
            if(temp!=null)
                properties_old.add(ResourceUtil.getPropertyMap(temp));
            else
                properties_old.add(null);
        }
        Object result=null;
        try {
            result=point.proceed();
            Map<String,Object> obj=(Map<String,Object>)result;
            if(obj!=null&& "1".equals(obj.get("code")+"") ){
                StringBuilder sb= new StringBuilder("");
                StringBuilder sb_sub=null;
                for(int i=0;i<properties_old.size();i++){
                    if(properties_old.get(i)==null)
                        continue;
                    sb_sub= new StringBuilder(properties_old.get(i).get("name")+"[");
                    for (Iterator<String> iterator = properties_old.get(i).keySet().iterator(); iterator.hasNext(); ) {
                        String pro =  iterator.next();
                        if(pro.equals("type"))
                            continue;
                        String old_value=properties_old.get(i).get(pro);
                        if(properties.get(i).containsKey(pro)){
                            Object new_value=properties.get(i).get(pro);
                            if(old_value==null && new_value!=null){
                                sb_sub.append(pro+": null -> "+new_value+", ");
                            }else if(old_value!=null && !old_value.equals(new_value))
                                sb_sub.append(pro+": "+(old_value.equals("")?"null":old_value)+" -> "+new_value+", ");
                        }
                    }
                    if(!sb_sub.toString().endsWith("[")){
                        sb.append(sb_sub.substring(0,sb_sub.length()-2)+"];");
                    }
                }
                auditLog.log(
                        getHostName(request),
                        getIP(request),
                        request.getRemoteUser(),
                        request.getSession().getId(),
                        request.getRequestedSessionId(),
                        "Value",
                        request.getRequestURI(),
                        "批量更新:"+sb.substring(0,sb.length()-1));
            }
        } catch (Throwable throwable) {
            log.error("",throwable);
        }
        return result;
    }
}
