package com.xtxb.cmdb.common.query;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月24日-下午3:08
 * <p>
 * <p>
 */
public class KeyPair extends QueryIterm {
    public static String EQUALS="=";
    public static String EQUALS_NOT="!=";

    public static String LIKE="like";

    public static String GREATER_THEN_AND=">";
    public static String GREATER_THEN_AND_EQUALS=">=";
    public static String LESS_THEN="<";
    public static String LESS_THEN_AND_EQUALS="<=";
    private String property;
    private Object value;
    private String compareType;

    public KeyPair(String property, Object value,String compareType) {
        this.property = property;
        this.value = value;
        this.compareType=compareType;
    }

    public String getProperty() {
        return property;
    }


    public Object getValue() {
        return value;
    }

    public String getCompareType() {
        return compareType;
    }

    public void setCompareType(String compareType) {
        this.compareType = compareType;
    }

    @Override
    public Type getType() {
        return Type.KEYPAIR;
    }
}
