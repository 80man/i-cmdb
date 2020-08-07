package com.xtxb.cmdb.common.view.ins;

import com.xtxb.cmdb.common.view.View;
import com.xtxb.cmdb.common.view.ViewIterm;

import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月17日-下午3:14
 * <p>
 * <p>
 * 列表视图
 */
public class ViewList extends View {
    private List<ViewIterm> iterms;

    public List<ViewIterm> getIterms() {
        return iterms;
    }

    public void setIterms(List<ViewIterm> literms) {
        this.iterms = literms;
    }
}
