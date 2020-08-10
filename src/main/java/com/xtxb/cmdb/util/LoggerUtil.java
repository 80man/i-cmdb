package com.xtxb.cmdb.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月15日-下午2:52
 * <p>
 * <p>
 * 对日志记录进行集中管理，便于后续改造或扩展
 */
@Scope("prototype")
@Component
public class LoggerUtil {
    private Logger log=null;

    public void info(String msg){
        log(Type.INFO,msg);
    }

    public void info(String msg,Throwable e){
        log(Type.INFO,msg,e);
    }

    public void debug(String msg){
        log(Type.DEBUG,msg);
    }

    public void debug(String msg,Throwable e){
        log(Type.DEBUG,msg,e);
    }

    public void error(String msg){
        log(Type.ERROR,msg);
    }

    public void error(String msg,Throwable e){
        log(Type.ERROR,msg,e);
    }

    public void warn(String msg){
        log(Type.ERROR,msg);
    }

    public void warn(String msg,Throwable e){
        log(Type.ERROR,msg,e);
    }


    private void log(Type type,String msg){
        String className="";
        if(log==null){
            className=Thread.currentThread().getStackTrace()[3].getClassName();
            log=LoggerFactory.getLogger(className);
            className=className.substring(className.lastIndexOf(".")+1);
        }
        msg="["+className+"] "+msg;
        if(type==Type.INFO){
            log.info(msg);
        }else if(type==Type.DEBUG){
            log.debug(msg);
        }else if(type==Type.ERROR){
            log.error(msg);
        }else if(type==Type.WARN)
            log.warn(msg);
    }

    private void log(Type type,String msg,Throwable e){
        if(log==null){
            log=LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
        }
        if(type==Type.INFO){
            log.info(msg,e);
        }else if(type==Type.DEBUG){
            log.debug(msg,e);
        }else if(type==Type.ERROR){
            log.error(msg,e);
        }else if(type==Type.WARN)
            log.warn(msg,e);
    }

    enum Type{
        INFO,DEBUG,ERROR,WARN;
    }
}
