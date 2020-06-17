package com.xtxb.cmdb.common.model;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月17日-上午10:48
 * <p>
 * <p>
 * CMDB中描述资源类型的所具有的属性的声明
 */
public class Property {
    /*属性的唯一标识*/
    private String name;
    /*所属资源类型的名称*/
    private String cModelName;
    /*属性组，用于给属性进行分组*/
    private String group;
    /*属性类型，参见：@PropertyType*/
    private PropertyType type;
    /*属性的默认值*/
    private String defValue;
    /*属性值校验规则类型,当属性类型为字符串或数值是有效，如枚举，正则，或引用*/
    private MatchRule rule;
    /*属性值校验规则值，枚举：以 | 分割的多个值； 正则：正则表达式；引用： 关联资源类型的名称*/
    private String matchRule;
    /*属性的值是否为数组*/
    private boolean multValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getcModelName() {
        return cModelName;
    }

    public void setcModelName(String cModelName) {
        this.cModelName = cModelName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public PropertyType getType() {
        return type;
    }

    public void setType(PropertyType type) {
        this.type = type;
    }

    public String getDefValue() {
        return defValue;
    }

    public void setDefValue(String defValue) {
        this.defValue = defValue;
    }

    public String getMatchRule() {
        return matchRule;
    }

    public void setMatchRule(String matchRule) {
        this.matchRule = matchRule;
    }

    public boolean isMultValue() {
        return multValue;
    }

    public void setMultValue(boolean multValue) {
        this.multValue = multValue;
    }

    public MatchRule getRule() {
        return rule;
    }

    public void setRule(MatchRule rule) {
        this.rule = rule;
    }
}
