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
    private List<ViewIterm> literms;

    public List<ViewIterm> getLiterms() {
        return literms;
    }

    public void setLiterms(List<ViewIterm> literms) {
        this.literms = literms;
    }
}
