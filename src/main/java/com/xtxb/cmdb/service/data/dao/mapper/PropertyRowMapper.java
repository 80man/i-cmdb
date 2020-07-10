package com.xtxb.cmdb.service.data.dao.mapper;

import com.xtxb.cmdb.common.model.MatchRule;
import com.xtxb.cmdb.common.model.Property;
import com.xtxb.cmdb.common.model.PropertyType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月03日-上午9:30
 * <p>
 * <p>
 * 负责封装资源属性定义
 */
@Component
public class PropertyRowMapper implements RowMapper<Property> {
    @Override
    public Property mapRow(ResultSet resultSet, int i) throws SQLException {
        Property property=new Property();
        property.setName(resultSet.getString("ENNAME"));
        property.setDescr(resultSet.getString("CNNAME"));
        property.setGroup(resultSet.getString("PGROUP"));
        property.setModelName(resultSet.getString("PNAME"));
        int type=resultSet.getInt("PTYPE");
        if(type==1)
            property.setType(PropertyType.STRING);
        else if(type==2)
            property.setType(PropertyType.LONG);
        else if(type==3)
            property.setType(PropertyType.FLOAT);
        else if(type==4)
            property.setType(PropertyType.TIME);
        else if(type==5)
            property.setType(PropertyType.DATE);
        else if(type==6)
            property.setType(PropertyType.DATETIE);
        else if(type==6)
            property.setType(PropertyType.BOOLEAN);
        property.setDefValue(resultSet.getString("DEFVALUE"));
        int rule=resultSet.getInt("MATCHRULE");
        if(rule==1){
            property.setRule(MatchRule.ENMU);
        }else if(rule==2){
            property.setRule(MatchRule.MATCH);
        }else if(rule==3){
            property.setRule(MatchRule.REFERENCE);
        }
        if(rule>0)
            property.setMatchRule(resultSet.getString("MATCHRULEVALUE"));

        return property;
    }
}
