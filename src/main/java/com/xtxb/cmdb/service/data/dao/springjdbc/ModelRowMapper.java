package com.xtxb.cmdb.service.data.dao.springjdbc;

import com.xtxb.cmdb.common.model.ModelClass;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月29日-下午6:05
 * <p>
 * <p>
 * 将数据库数据封装为资源类型
 */
@Component
public class ModelRowMapper implements RowMapper<ModelClass> {
    @Override
    public ModelClass mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ModelClass(resultSet.getString("ENNAME"),
                resultSet.getString("CNNAME"),
                resultSet.getString("PNAME"));
    }
}
