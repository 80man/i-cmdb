package com.xtxb.cmdb.service.data.dao;

import com.xtxb.cmdb.common.value.Link;
import com.xtxb.cmdb.service.data.dao.mapper.LinkRowMapper;
import com.xtxb.cmdb.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月16日-下午2:29
 * <p>
 * <p>
 * 默认的资源关系处理类
 */
@Component("defaultLinkDB")
public class LinkDBDefault implements LinkDB {

    private static String  SQL_GET_SINGLE="SELECT * FROM ${tableName} WHERE R_SID=? AND R_TID=?";
    private static String  SQL_GET_LIST="SELECT * FROM ${tableName} WHERE ${column}=? ";
    private static String  SQL_ADD="INSERT INTO ${tableName} VALUES (${SID},${TID},'${NOTE}')";
    private static String  SQL_DELETE="DELETE FROM ${tableName} WHERE R_SID=${SID} AND R_TID=${TID}";

    @Autowired
    private LoggerUtil log;

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private LinkRowMapper mapper;
    /**
     * 根据关系类型和关系两端的oid查询关系对象
     *
     * @param type 关系类型
     * @param sid  源端资源对象
     * @param tid  目的端资源对象
     * @return
     */
    @Override
    public Link getLink(String type, long sid, long tid) {
        String tableName=(type.toUpperCase().startsWith("R_")?type:("R_"+type.toUpperCase()));
        List<Link> list=template.query(SQL_GET_SINGLE.replace("${tableName}",tableName),new  Long[]{sid,tid}, mapper);
        if(list==null || list.size()==0)
            return null;
        else if(list.size()==1) {
            list.get(0).setType(type);
            return list.get(0);
        }else{
            log.error("存在重复的资源关系："+type+list);
            return null;
        }
    }

    /**
     * 根据关系类型和关系一端的oid查询关系对象
     *
     * @param type     关系类型
     * @param cid      源|目的端资源对象
     * @param isSource 声明sid是否是源端资源对象的ois，否则为目的端
     * @return
     */
    @Override
    public List<Link> getLink(String type, long cid, boolean isSource) {
        String tableName=(type.toUpperCase().startsWith("R_")?type:("R_"+type.toUpperCase()));
        String column=isSource?"R_SID":"R_TID";
        List<Link> list=template.query(
                SQL_GET_LIST.replace("${tableName}",tableName).replace("${column}",column),new  Long[]{cid}, mapper);
        for (Link link : list) {
            link.setType(type);
        }
        return list;
    }

    /**
     * 添加关系对象
     *
     * @param links 关系对象列表
     * @return
     * @throws Exception
     */
    @Override
    public boolean addLink(List<Link> links) throws Exception {
        String[] sqls=new String[links.size()];
        int index=0;
        String tableName=null;
        for (Link link : links) {
            tableName=link.getType().toUpperCase().startsWith("R_")?link.getType().toUpperCase():("R_"+link.getType().toUpperCase());
            sqls[index++]=SQL_ADD
                    .replace("${tableName}",tableName)
                    .replace("${SID}",link.getSid()+"")
                    .replace("${TID}",link.getTid()+"")
                    .replace("${NOTE}",link.getNote()+"");
        }
        int[] result=template.batchUpdate(sqls);
        return result!=null && result.length>0;
    }

    /**
     * 删除关系对象
     *
     * @param linkList
     * @return
     * @throws Exception
     */
    @Override
    public boolean deleteLink(List<Link> linkList) throws Exception {
        String[] sqls=new String[linkList.size()];
        int index=0;
        String tableName=null;
        for (Link link : linkList) {
            tableName=link.getType().toUpperCase().startsWith("R_")?link.getType().toUpperCase():("R_"+link.getType().toUpperCase());
            sqls[index++]=SQL_DELETE
                    .replace("${tableName}",tableName)
                    .replace("${SID}",link.getSid()+"")
                    .replace("${TID}",link.getTid()+"");
        }
        int[] result=template.batchUpdate(sqls);
        return result!=null && result.length>0;
    }
}
