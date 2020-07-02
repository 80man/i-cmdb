package com.xtxb.cmdb.service.dao;

import com.xtxb.cmdb.util.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月30日-上午10:24
 * <p>
 * <p>
 * 负责根据配置声明的类型获取资源类型数据库
 */
@Component
public class ModelDBFactory {
    @Value("${cmdb.db.model.name}")
    private String dbName;

    private byte[] lock=new byte[1];

    @Autowired
    private SpringContextUtil beanUtil;

    private ModelDB db;

    public ModelDB getInstance(){
        if(db==null) {
            synchronized (lock) {
                db = (ModelDB) beanUtil.getBean(dbName);
            }
        }
        return db;
    }
}
