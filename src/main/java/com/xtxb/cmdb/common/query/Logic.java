package com.xtxb.cmdb.common.query;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月24日-下午3:06
 * <p>
 * <p>
 *
 */
public class Logic extends QueryIterm {
    private LogicType logicType;
    public Logic(LogicType logicType){
        this.logicType=logicType;
    }

    public LogicType getLogicType() {
        return logicType;
    }

    @Override
    public Type getType() {
        return Type.LOGIC;
    }
}
