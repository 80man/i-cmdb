package com.xtxb.cmdb.service.data.dao.mapper;

import com.xtxb.cmdb.common.model.Property;
import com.xtxb.cmdb.common.model.PropertyType;
import com.xtxb.cmdb.common.value.Resource;
import com.xtxb.cmdb.service.data.dao.ModelDB;
import com.xtxb.cmdb.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月06日-下午4:52
 * <p>
 * <p>
 * 负责封装资源实例
 */
@Scope("prototype")
@Component("resRowMapper")
public class ResourceRowMapper implements RowMapper<Resource> {
    private String modelName;
    private List<Property> properties;

    @Autowired
    private SpringContextUtil beanUtil;

    @Override
    public Resource mapRow(ResultSet resultSet, int i) throws SQLException {
        Resource obj=new Resource();
        obj.setOid(resultSet.getLong("P_OID"));
        obj.setSid(resultSet.getString("P_SID"));
        obj.setModelName(modelName);
        Object value=null;
        for (Property property : properties) {
            if(property.getName().equals("oid") || property.getName().equals("sd"))
                continue;

            String columnName=property.getName().toUpperCase().startsWith("P_")?
                    property.getName().toUpperCase():
                    "P_"+property.getName().toUpperCase();
            if(property.getType()== PropertyType.BOOLEAN){
                value=resultSet.getInt(columnName)>0?true:false;
            }else if(property.getType()==PropertyType.LONG){
                value=resultSet.getLong(columnName);
            }else if(property.getType()==PropertyType.FLOAT){
                value=resultSet.getFloat(columnName);
            }else if(property.getType()==PropertyType.DATE ||
                    property.getType()==PropertyType.TIME ||
                    property.getType()==PropertyType.DATETIE){
                value=new Date(resultSet.getLong(columnName));
            }else if(property.getType()==PropertyType.STRING){
                value=resultSet.getString(columnName);
            }else
                value=null;
            obj.setValue(property.getName(),value);
        }
        return obj;
    }

    public ResourceRowMapper setModelName(String name){
        this.modelName=name;
        properties=((ModelDB)beanUtil.getBean("defaultModelDB")).getProperties(modelName);
        return this;
    }
}
