package com.xtxb.cmdb.audit;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年08月11日-下午2:24
 * <p>
 * <p>
 * 对外部API进行统一拦截
 */
public abstract class AopBase {


    public HttpServletRequest getRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        //从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        return request;
    }


    public String getHostName(HttpServletRequest request){
        String hostName=request.getRemoteHost();
        if(hostName==null  || hostName.equals(""))
            return request.getRemoteHost();
        return hostName;
    }

    public String getIP(HttpServletRequest request){
        return request.getRemoteHost();
    }

}
