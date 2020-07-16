package com.xtxb.cmdb.service.data.dao.mapper;

import com.xtxb.cmdb.common.value.Link;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月16日-下午3:13
 * <p>
 * <p>
 * 负责封装资源关系实例
 */
@Component
public class LinkRowMapper implements RowMapper<Link> {
    @Override
    public Link mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Link(resultSet.getLong(1),resultSet.getLong(2),resultSet.getString(3));
    }
}
