package com.xtxb.cmdb.util;

import com.xtxb.cmdb.common.model.PropertyType;

import java.util.Date;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月31日-下午2:19
 * <p>
 * <p>
 *  资源实例操作类
 */
public class ResourceUtil {

    public static  Object convertValueToView(Object value, PropertyType type){
        if(value==null)
            return null;
        switch (type){
            case BOOLEAN:
                ;
            case LONG:
                ;
            case FLOAT:
                ;
            case STRING:
                return value;
            case DATE:
                ;
            case TIME:
                ;
            case DATETIE:
                return ((Date)value).getTime();
            default:
                return null;
        }
    }


    public static  Object convertValueToStore(Object value, PropertyType type){
        if(value==null)
            return null;
        switch (type){
            case BOOLEAN:
                return "true".equals(value.toString());
            case LONG:
                return Long.parseLong(value.toString());
            case FLOAT:
                return Float.parseFloat(value.toString());
            case STRING:
                return value;
            case DATE:
                ;
            case TIME:
                ;
            case DATETIE:
                return new Date(Long.parseLong(value.toString()));
            default:
                return null;
        }
    }
}
