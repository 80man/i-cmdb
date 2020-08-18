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
                log(auditLog,request,"资源类型:"+args[0]+" 的名称由:"+descr+"更新为:"+args[1]);
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
        List<Property> properties_old=new ArrayList<>();
        if(properties!=null) {
            for (Map<String, Object> property : properties) {
                properties_old.add(service.getProperty((String) property.get("modelName"), (String) property.get("name")));
            }
        }
        Object result=null;
        try {
            result=point.proceed();
            Map<String,Object> obj=(Map<String,Object>)result;
            if(obj!=null&& "1".equals(obj.get("code")+"") ){
                StringBuilder sb= new StringBuilder("");
                StringBuilder sb_sub=null;
                Property property=null;
                for(int i=0;i<properties_old.size();i++){
                    property=properties_old.get(i);
                    if(property==null)
                        continue;
                    sb_sub= new StringBuilder(property.getName()+"[");
                    if(!property.getDescr().equals(properties.get(i).get("descr"))){
                        sb_sub.append("名称: "+property.getDescr()+" -> "+properties.get(i).get("descr")+", ");
                    }
                    if(!property.getGroup().equals(properties.get(i).get("group"))){
                        sb_sub.append("属性组: "+property.getGroup()+" -> "+properties.get(i).get("group")+", ");
                    }

                    if(property.getRule()==null && properties.get(i).get("rule")!=null){
                        sb_sub.append("约束: null -> "+properties.get(i).get("rule")+", ");
                    }
                    if(property.getRule()!=null && !property.getRule().name().equalsIgnoreCase((String)properties.get(i).get("rule"))){
                        sb_sub.append("约束: "+property.getRule().name()+" -> "+properties.get(i).get("rule")+", ");
                    }

                    if(property.getMatchRule()==null && properties.get(i).get("matchRule")!=null){
                        sb_sub.append("约束值: null -> "+properties.get(i).get("matchRule")+", ");
                    }
                    if(property.getMatchRule()!=null && !property.getMatchRule().equals(properties.get(i).get("matchRule"))){
                        sb_sub.append("约束值: "+property.getMatchRule()+" -> "+properties.get(i).get("matchRule")+", ");
                    }

                    if(property.getDefValue()==null && properties.get(i).get("defValue")!=null){
                        sb_sub.append("默认值: null -> "+properties.get(i).get("defValue")+", ");
                    }
                    if(property.getDefValue()!=null && !property.getDefValue().equals(properties.get(i).get("defValue"))){
                        sb_sub.append("默认值: "+property.getMatchRule()+" -> "+properties.get(i).get("defValue")+", ");
                    }

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
