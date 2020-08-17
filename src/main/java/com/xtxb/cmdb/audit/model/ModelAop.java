package com.xtxb.cmdb.audit.model;

import com.xtxb.cmdb.audit.AopBase;
import com.xtxb.cmdb.audit.AuditLog;
import com.xtxb.cmdb.common.model.ModelClass;
import com.xtxb.cmdb.service.ModelService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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
            throwable.printStackTrace();
        }
        return result;
    }

//    @Around("execution(* com.xtxb.cmdb.api.ModelAPI.updateProperty(..))")
//    public  Object modifyProperty(ProceedingJoinPoint point){
//        HttpServletRequest request = getRequest();
//        Object[] args=point.getArgs();
//        return null;
//    }
}
