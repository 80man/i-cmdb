package com.xtxb.cmdb.service.dao;

import com.xtxb.cmdb.common.model.ModelClass;
import com.xtxb.cmdb.service.dao.springjdbc.ModelRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月28日-下午2:21
 * <p>
 * <p>
 * 负责从数据库进行数据持久化
 */
@Component("defaultDB")
public class ModelDBDefault implements ModelDB {

    private static final String SQL_GET_MODEL="SELECT * FROM M_META";
    private static final String SQL_UPDATE_MODEL="UPDATE M_META SET CNNANE=? WHERE ENNAME=?";
    private static final String SQL_ADD_MODEL="INSERT INTO M_META (ENNANE, CNNAME,PNAME) " +
            "VALUES (?,?,?)";
    private static final String SQL_DELETE_MODEL="DELETE FROM M_META WHERE ENNAME=?";


    @Autowired
    private JdbcTemplate template;

    @Autowired
    private ModelRowMapper mapper;
    /**
     * 获取资源类型
     *
     * @return
     */
    @Override
    public List<ModelClass> getModel() {
        return template.query(SQL_GET_MODEL,mapper);
    }

    /**
     * 更新资源类型
     *
     * @param modelClass
     * @return
     * @throws Exception
     */
    @Override
    public boolean updateModel(ModelClass modelClass) throws Exception {
        return template.update(SQL_UPDATE_MODEL,modelClass.getDescr(),modelClass.getName())>0;
    }

    /**
     * 添加资源类型
     *
     * @param modelClass
     * @return
     * @throws Exception
     */
    @Override
    public boolean addModel(ModelClass modelClass) throws Exception {
        return template.update(SQL_ADD_MODEL,modelClass.getName(),modelClass.getDescr(),modelClass.getParent())>0;
    }

    /**
     * 删除资源类型，同时会删除相关的属性。当试图删除具有子类型或实例资源的资源类型时会抛出异常
     *
     * @param name
     * @return
     */
    @Override
    public boolean deleteModel(String name) throws Exception {
        return template.update(SQL_DELETE_MODEL,name)>0;
    }
}
