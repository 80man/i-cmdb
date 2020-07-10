package com.xtxb.cmdb.common.query;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月24日-下午3:08
 * <p>
 * <p>
 */
public class KeyPair extends QueryIterm {
    private String property;
    private Object value;

    public KeyPair(String property, Object value) {
        this.property = property;
        this.value = value;
    }

    public String getProperty() {
        return property;
    }


    public Object getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return null;
    }
}
