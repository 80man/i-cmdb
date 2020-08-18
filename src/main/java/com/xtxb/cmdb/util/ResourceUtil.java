package com.xtxb.cmdb.util;

import com.xtxb.cmdb.common.model.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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


    public static Property getProperty(Map<String,Object> map){
        Property property=new Property();
        property.setName((String)map.get("name"));
        property.setDescr((String)map.get("descr"));
        property.setModelName((String)map.get("modelName"));
        property.setGroup((String)map.get("group"));
        property.setDefValue(map.get("defValue")+"");
        if(map.get("rule")!=null && !map.get("rule").toString().equals("")){
            MatchRule rule=MatchRule.valueOf(((String)map.get("rule")).toUpperCase());
            if(rule!=null)
                property.setRule(rule);
        }
        if(map.get("matchRule")!=null && !map.get("matchRule").toString().equals(""))
            property.setMatchRule((String)map.get("matchRule"));
        if(map.get("type")!=null)
            property.setType(PropertyType.valueOf(((String)map.get("type")).toUpperCase()));
        return property;
    }


    public static Map<String,String> getRelationShipMap(RelationShip ship){
        Map<String,String> map=new HashMap<>();
        map.put("name",ship.getName());
        map.put("descr",ship.getDescr());
        map.put("sourceModel",ship.getSourceModel());
        map.put("targetModel",ship.getTargetModel());
        return map;
    }


    public static Map<String,String> getPropertyMap(Property property){
        Map<String,String> map=new HashMap<>();
        map.put("name",property.getName());
        map.put("descr",property.getDescr());
        map.put("modelName",property.getModelName());
        map.put("group",property.getGroup());
        map.put("defValue",property.getDefValue());
        map.put("rule",property.getRule()!=null?property.getRule().name():null);
        map.put("matchRule",property.getMatchRule());
        map.put("type",property.getType().name());
        return map;
    }

    public static Map<String,String> getModelMap(ModelClass model){
        Map<String,String> map=new HashMap<>();
        map.put("name",model.getName());
        map.put("descr",model.getDescr());
        return map;
    }
}
