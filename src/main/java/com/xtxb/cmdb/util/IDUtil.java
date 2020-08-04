package com.xtxb.cmdb.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年08月03日-下午7:51
 * <p>
 * <p>
 * 全局统一资源OID
 */
@Component
public class IDUtil {
    private long current_OID=0;
    private long ceiling_OID=0;
    private boolean initTable;

    @Autowired
    private JdbcTemplate template;

    public long next() {
        if (current_OID == 0) {
            createTable();
            readTable();
        } else if (current_OID == ceiling_OID){
            readTable();
        }
        return current_OID++;
    }

    private synchronized void createTable(){
        if(initTable)
            return;

        template.execute("CREATE  TABLE  IF NOT EXISTS  ID_META(\n" +
                "MAX_OID numeric(20),\n"+
                "STEP_LEN numeric(20)\n"+
                ")");
        initTable=true;
    }

    private synchronized void readTable(){
        if(current_OID != ceiling_OID)
            return ;

        Map<String,Object> map=null;
        try {
            map=template.queryForMap("SELECT * FROM ID_META");
        } catch (DataAccessException e) {
            ;
        }
        if(map==null || map.size()==0) {
            template.update("INSERT  INTO ID_META(MAX_OID,STEP_LEN) VALUES(100,100)");
            current_OID = 1;
            ceiling_OID = 100;
        }else{
            current_OID=Long.parseLong(map.get("MAX_OID")+"");
            ceiling_OID=current_OID+Long.parseLong(map.get("STEP_LEN")+"");
            template.update("UPDATE ID_META SET MAX_OID="+ceiling_OID);
        }
    }
}
