package com.xtxb.cmdb.common.view;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月17日-下午3:07
 * <p>
 * <p>
 * 用于描述资源在UI页面显示是的呈现规则
 */
public class View {
    /*视图的名称*/
    private String name;
    /*视图所属的资源类型*/
    private String modelName;
    /*视图类型*/
    private ViewType type;
    /**
     * 下面两个字段用于控制视图的使用目的，优先级 demo>scene
     */
    /*视图应用的范围*/
    private  String demo;
    /*视图应用的场景*/
    private  String scene;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ViewType getType() {
        return type;
    }

    public void setType(ViewType type) {
        this.type = type;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }
}
