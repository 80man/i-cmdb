package com.xtxb.cmdb.service.data.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月30日-下午1:43
 * <p>
 * <p>
 *
 */
@Component
public class SecurityRowMapper  implements RowMapper<Map<String,String>> {
    @Override
    public Map<String, String> mapRow(ResultSet resultSet, int i) throws SQLException {
        Map<String,String> map =new HashMap<>();
        map.put("public",resultSet.getString("RSA_PUBLIC_KEY"));
        map.put("private",resultSet.getString("RSA_PRIVATE_KEY"));
        map.put("des",resultSet.getString("DES_KEY"));
        map.put("appName",resultSet.getString("APP_NAME"));
        map.put("user",resultSet.getString("APP_USER"));
        return map;
    }
}
