package com.xtxb.cmdb.audit;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月06日-下午3:28
 * <p>
 * <p>
 * 用于记录审计日志的API，api包中提供的外部调用api需要进行审计
 */
public interface AuditLog{
    public void log(String hostName,String ip,String user,String sessionid,String requestID,String type,String uri,String note);
}
