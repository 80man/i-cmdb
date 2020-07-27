package com.xtxb.cmdb.common.view.ins;

import com.xtxb.cmdb.common.view.View;
import com.xtxb.cmdb.common.view.ViewIterm;

import java.util.List;
import java.util.Map;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月24日-下午2:47
 * <p>司
 * <p>
 * 资源实例详情查看视图
 */
public class ViewDetail extends View {
    private Map<String, List<ViewIterm>> literms;

    public Map<String, List<ViewIterm>> getLiterms() {
        return literms;
    }

    public void setLiterms(Map<String, List<ViewIterm>> literms) {
        this.literms = literms;
    }
}
