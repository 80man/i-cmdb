package com.xtxb.cmdb.common.action;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月17日-下午3:33
 * <p>
 * <p>
 *
 */
public interface Action {
    public String getID();
    public String getTarget();
    public ActionType getType();

}
