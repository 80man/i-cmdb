package com.xtxb.cmdb.common.action;

/**
 * 作者: xtxb-fedora
 * <p>
 * 日期: 2020年06月17日-下午3:33
 * <p>
 * 版权说明：北京神州泰岳软件股份有限公司
 * <p>
 * TODO
 */
public interface Action {
    public String getID();
    public String getTarget();
    public ActionType getType();

}
