package com.xtxb.cmdb.service.dao.springjdbc;

import com.xtxb.cmdb.common.model.RelationShip;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月03日-下午3:30
 * <p>
 * <p>
 * 负责封装资源关系定义
 */
@Component
public class RelationShipRowMapper implements RowMapper<RelationShip> {
    @Override
    public RelationShip mapRow(ResultSet resultSet, int i) throws SQLException {
        return new RelationShip(resultSet.getString("ENNAME"),
                resultSet.getString("CNNAM"),
                resultSet.getString("SOURCEMODEL"),
                resultSet.getString("TARGETMODEL"));
    }
}
