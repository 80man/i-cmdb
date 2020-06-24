package com.xtxb.cmdb.common.query;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月24日-下午3:03
 * <p>
 * <p>
 */
public class Group extends QueryIterm {
    private GroupType groupType;
    public Group(GroupType groupType){
        this.groupType=groupType;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public Type getType(){
        return Type.GROUP;
    }
}
