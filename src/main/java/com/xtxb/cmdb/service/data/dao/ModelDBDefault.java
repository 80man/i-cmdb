package com.xtxb.cmdb.service.data.dao;

import com.xtxb.cmdb.common.model.ModelClass;
import com.xtxb.cmdb.common.model.Property;
import com.xtxb.cmdb.common.model.PropertyType;
import com.xtxb.cmdb.common.model.RelationShip;
import com.xtxb.cmdb.service.data.dao.mapper.ModelRowMapper;
import com.xtxb.cmdb.service.data.dao.mapper.PropertyRowMapper;
import com.xtxb.cmdb.service.data.dao.mapper.RelationShipRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月28日-下午2:21
 * <p>
 * <p>
 * 负责从数据库进行数据持久化
 */
@Lazy
@Component("defaultModelDB")
public class ModelDBDefault implements ModelDB {

    /*资源类型相关SQL*/
    private static final String SQL_GET_MODEL="SELECT * FROM M_META";
    private static final String SQL_UPDATE_MODEL="UPDATE M_META SET CNNAME=? WHERE ENNAME=?";
    private static final String SQL_ADD_MODEL="INSERT INTO M_META (ENNAME, CNNAME) " +
            "VALUES (?,?)";
    private static final String SQL_CREATE_MODEL_TABLE="CREATE TABLE ${tableName} (\n" +
            "P_OID numeric(20)  not null primary key,\n" +
            "P_SID varchar(32) \n" +
            ")" ;
    private static final String SQL_CREATE_MODEL_INDEX="CREATE INDEX ${tableName}_IND_P_SID ON ${tableName} (P_SID)";
    private static final String SQL_DROP_MODEL_TABLE="DROP TABLE ${tableName}";
    private static final String SQL_DELETE_MODEL="DELETE FROM M_META WHERE ENNAME=?";

    /*资源属性相关SQL*/
    private static final String SQL_GET_PROPERTY="SELECT * FROM P_META WHERE PNAME=?";
    private static final String SQL_UPDATE_PROPERTY="UPDATE P_META SET  " +
            "CNNAME=?,PGROUP=?, DEFVALUE=?, MATCHRULE=?, MATCHRULEVALUE=? WHERE ENNAME=?";
    private static final String SQL_ADD_PROPERTY="INSERT INTO P_META (ENNAME, CNNAME,PNAME,PGROUP,PTYPE, DEFVALUE, MATCHRULE, MATCHRULEVALUE) "+
            "VALUES (?,?,?,?,?,?,?,?)";
    private static final String SQL_DELETE_PROPERTY="DELETE FROM P_META WHERE ENNAME=?";

    /*资源关系相关SQL*/
    private static final String SQL_GET_RELATION="SELECT * FROM R_META";
    private static final String SQL_UPDATE_RELATION="UPDATE R_META SET  " +
            "CNNAME=? WHERE ENNAME=?";
    private static final String SQL_ADD_RELATION="INSERT INTO R_META (ENNAME, CNNAME,SOURCEMODEL,TARGETMODEL) "+
            "VALUES (?,?,?,?)";
    private static final String SQL_DELETE_RELATION="DELETE FROM R_META WHERE ENNAME=?";

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private ModelRowMapper mapper;

    @Autowired
    private PropertyRowMapper propertyRowMapper;


    @Autowired
    private RelationShipRowMapper relshoipRowMapper;
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
        if(template.update(SQL_ADD_MODEL,modelClass.getName(),modelClass.getDescr())>0){
            String tableName=modelClass.getName().toUpperCase();
            tableName=tableName.startsWith("C_")?tableName:("C_"+tableName);
            template.execute(SQL_CREATE_MODEL_TABLE.replace("${tableName}",tableName)) ;
            template.execute(SQL_CREATE_MODEL_INDEX.replace("${tableName}",tableName)) ;
            return true;
        }
        return false;
    }

    /**
     * 删除资源类型，同时会删除相关的属性。当试图删除具有子类型或实例资源的资源类型时会抛出异常
     *
     * @param name
     * @return
     */
    @Override
    public boolean deleteModel(String name) throws Exception {
        if( template.update(SQL_DELETE_MODEL,name)>0) {
            name=name.toUpperCase().startsWith("C_")?name.toUpperCase():("C_"+name.toUpperCase());
            template.execute(SQL_DROP_MODEL_TABLE.replace("${tableName}",name));
            return true;
        }

        return  false;
    }

    /**
     * 查询资源属性，根据资源类型查询
     *
     * @param modelName 资源类型的名称（英文名称）
     * @return
     */
    @Override
    public List<Property> getProperties(String modelName) {
        return template.query(SQL_GET_PROPERTY,new String[]{modelName},propertyRowMapper);
    }

    /**
     * 批量更新资源属性
     *
     * @param propertys 资源属性集合
     * @return
     * @throws Exception
     */
    @Override
    public boolean updateProperty(List<Property> propertys) throws Exception {
        List<Object[]> rows=new ArrayList<>(propertys.size());
        for(Property pro: propertys){
            rows.add(new Object[]{
                    pro.getDescr(),pro.getGroup(),pro.getDefValue(),pro.getRule()!=null?(pro.getRule().ordinal()+1):0,pro.getMatchRule(),pro.getName()
            });
        }
        return template.batchUpdate(SQL_UPDATE_PROPERTY,rows)!=null;
    }

    /**
     * 添加资源属性
     *
     * @param propertys 资源属性集合
     * @return
     * @throws Exception
     */
    @Override
    public boolean addProperty(List<Property> propertys) throws Exception {
        List<Object[]> rows=new ArrayList<>(propertys.size());
        List<String> sqls=new ArrayList();
        Map<String,List<String>> subMap=new HashMap<>();
        for(Property pro: propertys){

            rows.add(new Object[]{
                    pro.getName(),pro.getDescr(),pro.getModelName(),pro.getGroup(),pro.getType().ordinal()+1,
                    pro.getDefValue(),pro.getRule()!=null?(pro.getRule().ordinal()+1):0,pro.getMatchRule()
            });

            String tn= pro.getModelName().toUpperCase();
            tn=tn.startsWith("C_")?tn:"C_"+tn;
            String cn=pro.getName().toUpperCase();
            cn=cn.startsWith("C_")?cn:"C_"+cn;
            String type="VARCHAR(200)";
            if(pro.getType()== PropertyType.DATETIE
                        || pro.getType()== PropertyType.DATE
                        || pro.getType()== PropertyType.TIME
                        || pro.getType()== PropertyType.LONG)
                    type="NUMERIC(20)";
            else if(pro.getType()== PropertyType.FLOAT)
                    type="NUMERIC(20,2)";
            else if(pro.getType()== PropertyType.BOOLEAN)
                    type="NUMERIC(1)";
            sqls.add("ALTER TABLE "+tn+" ADD COLUMN  "+cn+" "+type);
        }
        if(template.batchUpdate(SQL_ADD_PROPERTY,rows)!=null){
            for(String sql : sqls){
                template.execute(sql);
            }
            return true;
        }
        return false;
    }

    /**
     * 删除属性定义
     *
     * @param propertys
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteProperties(List<Property> propertys) throws Exception {
        List<Object[]> rows=new ArrayList<>(propertys.size());
        List<String> sqls=new ArrayList();
        Map<String,List<String>> subMap=new HashMap<>();

        for(Property pro: propertys){
            rows.add(new Object[]{
                    pro.getName(),
            });

            String tn = pro.getModelName().toUpperCase();
            tn = tn.startsWith("C_") ? tn : "C_" + tn;
            String cn = pro.getName().toUpperCase();
            cn = cn.startsWith("C_") ? cn : "C_" + cn;
            sqls.add("ALTER TABLE "+tn+" DROP COLUMN "+cn);
        }
        if(template.batchUpdate(SQL_DELETE_PROPERTY,rows)!=null){
            for(String sql : sqls){
                template.execute(sql);
            }
            return true;
        }
        return false;
    }

    /**
     * 查询关系类型定义
     *
     * @return
     */
    @Override
    public List<RelationShip> getRelationShip() {
        return template.query(SQL_GET_RELATION,relshoipRowMapper);
    }

    /**
     * 更新关系类型定义
     *
     * @param relationShip
     * @return
     * @throws Exception
     */
    @Override
    public boolean updateRelationShip(RelationShip relationShip) throws Exception {
        return template.update(SQL_UPDATE_RELATION,relationShip.getDescr(),relationShip.getName())>0;
    }

    /**
     * 添加关系类型定义
     *
     * @param relationShip
     * @return
     * @throws Exception
     */
    @Override
    public boolean addRelationShip(RelationShip relationShip) throws Exception {
        if(template.update(SQL_ADD_RELATION,
                relationShip.getName(), relationShip.getDescr(),relationShip.getSourceModel(),relationShip.getTargetModel())>0){
            String tn=relationShip.getName().toUpperCase();
            if(!tn.startsWith("R_"))
                tn="R_"+tn;
            String sql1="CREATE TABLE "+tn+" (\n" +
                    "R_SID numeric(20),\n" +
                    "R_TID numeric(20),\n" +
                    "R_NOTE varchar(100)\n" +
                    ")";
            String sql2="CREATE INDEX "+tn+"_IND ON "+tn+" (R_SID,R_TID)";
            template.execute(sql1);
            template.execute(sql2);
            return true;
        }
        return false;
    }

    /**
     * 删除关系模型，当试图删除具有实例资源的关系模型时，会抛出异常
     *
     * @param name
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteRelationShip(String name) throws Exception {
        if( template.update(SQL_DELETE_RELATION,name)>0){
            String tn=name.toUpperCase();
            if(!tn.startsWith("R_"))
                tn="R_"+tn;
            template.execute("DROP TABLE "+tn);
            return true;
        }
        return false;
    }
}
