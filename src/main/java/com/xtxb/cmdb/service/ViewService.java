package com.xtxb.cmdb.service;

import com.xtxb.cmdb.common.view.View;
import com.xtxb.cmdb.common.view.ViewType;

import java.util.List;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年07月24日-上午10:52
 * <p>
 * <p>
 * 管理视图
 */
public interface ViewService {

    /**
     * 查询视图
     * @param type 视图类型
     * @param demo 视图所属的应用范围
     * @param scene 视图所属的应用场景
     * @param name 视图的名称
     * @param modelName  视图所属的资源模型
     * @return
     */
    public View getView(ViewType type,String demo, String  scene,String name,String modelName);

    /**
     * 根据资源模型查询相关的视图
     * @param modelName
     * @return
     */
    public List<View> getViews(String modelName);

    /**
     * 添加视图
     * @param view
     * @return
     */
    public boolean addView(View view);

    /**
     * 修改视图
     * @param view
     * @return
     */
    public boolean updateView(View view);

    /**
     * 删除视图
     * @param view
     * @return
     */
    public boolean deleteView(View view);
}
