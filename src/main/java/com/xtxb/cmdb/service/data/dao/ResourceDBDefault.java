package com.xtxb.cmdb.service.data.dao;

import com.xtxb.cmdb.common.query.*;
import com.xtxb.cmdb.common.value.Resource;
import com.xtxb.cmdb.service.data.dao.mapper.ResourceRowMapper;
import com.xtxb.cmdb.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月06日-下午3:33
 * <p>
 * <p>
 * 负责持久化资源实例
 */
public class ResourceDBDefault implements ResourceDB{

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private SpringContextUtil beanUtil;

    private final static String GET_SQL="SELECT * FROM ${tableName} ${where} OFFSET ${pageIndex} FETCH FIRST ${pageLen} ROWS ONLY";

    /**
     * 分页获取资源对象
     *
     * @param modelName 资源类型的英文名称
     * @param pageIndex 页号
     * @param pageLen   页长
     * @param user      操作账号，当前版本仅作记录，不会影响权限
     * @return
     */
    @Override
    public List<Resource> getResources(String modelName, int pageIndex, int pageLen, String user) throws Exception {
        return queryResources(modelName,pageIndex,pageLen,user,null);
    }

    /**
     * 自定义条件查询资源对象
     *
     * @param pageIndex 页号
     * @param pageLen   页长
     * @param user      操作账号，当前版本仅作记录，不会影响权限
     * @param iterm     查询条件,格式为：
     *                  Group(GroupType.LEFT),KeyPair(property,value),Logic(LogicType.OR),KeyPair(property,value),Group(GroupType.RIGHT),Logic(LogicType.AND),KeyPair(property,value)
     * @return
     */
    @Override
    public List<Resource> queryResources(String modelName,int pageIndex, int pageLen, String user, QueryIterm... iterm) throws Exception {
        pageIndex=(pageIndex-1)*pageLen+1;
        StringBuilder whereStr=new StringBuilder("");
        if(iterm!=null){
            whereStr.append("WHERE ");
            for (QueryIterm queryIterm : iterm) {
                if(queryIterm.getType()== Type.GROUP){
                    if(((Group)queryIterm).getGroupType()==GroupType.LEFT)
                        whereStr.append("(");
                    else
                        whereStr.append(")");
                }else if(queryIterm.getType()==Type.LOGIC){
                    if(((Logic)queryIterm).getLogicType()==LogicType.AND)
                        whereStr.append(" AND ");
                    else
                        whereStr.append(" OR ");
                }else{
                    whereStr.append(" "+((KeyPair)queryIterm).getProperty()+"='"+((KeyPair)queryIterm).getValue()+"' ");
                }
            }
        }

        String sql=GET_SQL
                .replace(
                        "${tableName}",getTableName(modelName)
                )
                .replace("${pageIndex}",pageIndex+"")
                .replace("${pageLen}",pageLen+"")
                .replace("${where}",whereStr.toString());
        return template.query(sql, ((ResourceRowMapper)beanUtil.getBean("resRowMapper")).setModelName(modelName));
    }

    /**
     * 更新资源对象
     *
     * @param user      操作账号，当前版本仅作记录，不会影响权限
     * @param resources
     * @return
     * @throws Exception
     */
    @Override
    public boolean updateResources(String user, List<Resource> resources) throws Exception {
        String[] sqls=new String[resources.size()];
        int index=0;
        for (Resource resource : resources) {
            sqls[index++]=getUpdateSql(resource);
        }
        int[] re=template.batchUpdate(sqls);
        return re!=null && re.length>0;
    }

    /**
     * 添加资源对象
     *
     * @param user      操作账号，当前版本仅作记录，不会影响权限
     * @param resources
     * @return
     * @throws Exception
     */
    @Override
    public boolean addResources(String user, List<Resource> resources) throws Exception {
        String[] sqls=new String[resources.size()];
        int index=0;
        for (Resource resource : resources) {
            sqls[index++]=getAddSql(resource);
        }
        int[] re=template.batchUpdate(sqls);
        return re!=null && re.length>0;
    }

    /**
     * 删除资源对象，删除时也会删除相关的关系数据
     *
     * @param user
     * @param resources
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteResources(String user, List<Resource> resources) throws Exception {
        String[] sqls=new String[resources.size()];
        int index=0;
        for (Resource resource : resources) {
            sqls[index++]="SELECT FROM "+getTableName(resource.getModelName()) +" WHERE P_OID="+resource.getOid();
        }

        int[] re=template.batchUpdate(sqls);
        return re!=null && re.length>0;
    }


    private String getUpdateSql(Resource res){
        StringBuilder sb=new StringBuilder("");
        sb.append("UPDATE "+getTableName(res.getModelName())+" SET ");
        for (Iterator<String> iterator = res.getValues().keySet().iterator(); iterator.hasNext(); ) {
            String property =  iterator.next();
            if(property.equals("oid"))
                continue;
            sb.append(getColumnName(property)+"=");
            sb.append(getSqlValue(res.getValue(property))+", ");
            sb.append(", ");
        }
        return sb.substring(0,sb.length()-1)+" WHERE P_0ID="+res.getOid();
    }


    private String getAddSql(Resource res){
        StringBuilder sbcol=new StringBuilder("");
        StringBuilder sbval=new StringBuilder("");
        for (Iterator<String> iterator = res.getValues().keySet().iterator(); iterator.hasNext(); ) {
            String property =  iterator.next();
            sbcol.append(res.getValue(getColumnName(property))+",");
            sbval.append(getSqlValue(res.getValue(property))+", ");
        }
        return "INSERT INTO "+ getTableName(res.getModelName())+
                "("+sbcol.substring(0,sbcol.length()-1)+") VALUES("+
                sbval.substring(0,sbval.length()-1)+")";

    }

    private String getTableName(String modelName){
        if(!modelName.toUpperCase().startsWith("C_"))
            modelName="C_"+modelName;
        return modelName.toUpperCase();
    }

    private String getColumnName(String property){
        if(!property.toUpperCase().startsWith("P_"))
            property="P_"+property;
        return property.toUpperCase();
    }

    private String getSqlValue(Object value){
        if(value==null)
            return "NULL";
        else if(value instanceof String){
           return ("'"+value+"'");
        }else if(value instanceof Number){
            return value+"";
        }else if(value instanceof Date){
            return ((Date)value).getTime()+"";
        }else if(value instanceof Boolean)
            return (((Boolean)value)?1:0)+"";
        else
            return "NULL";
    }
}
