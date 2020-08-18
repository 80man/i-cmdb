package com.xtxb.cmdb.audit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年08月11日-下午3:21
 * <p>
 * <p>
 *
 */
@Aspect
@Component
public class GlobalAop extends AopBase {
    @Autowired
    private AuditLog auditLog;

    @Before("@annotation(javax.ws.rs.Path)")
    public void before(JoinPoint point) {
        HttpServletRequest request = getRequest();
        auditLog.log(getHostName(request),
                getIP(request),
                request.getRemoteUser(),
                request.getSession().getId(),
                request.getRequestedSessionId(),
                "Start",
                request.getRequestURI(),
                Arrays.toString(point.getArgs()));
    }

    @AfterReturning(returning="result", pointcut="@annotation(javax.ws.rs.Path)\"")
    public void afterReturning(Object result) {
        HttpServletRequest request = getRequest();
        auditLog.log(getHostName(request),
                getIP(request),
                request.getRemoteUser(),
                request.getSession().getId(),
                request.getRequestedSessionId(),
                "End",
                request.getRequestURI(),
                result.toString());
    }

    @AfterThrowing(throwing="error",pointcut="@annotation(javax.ws.rs.Path)\"")
    public void afterThrowing(JoinPoint point,Throwable error) {
        HttpServletRequest request = getRequest();
        auditLog.log(getHostName(request),
                getIP(request),
                request.getRemoteUser(),
                request.getSession().getId(),
                request.getRequestedSessionId(),
                "Throw",
                request.getRequestURI(),
                Arrays.toString(point.getArgs())+";"+error.getMessage());
    }
}
