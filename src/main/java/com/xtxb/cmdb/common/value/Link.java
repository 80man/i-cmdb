package com.xtxb.cmdb.common.value;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月24日-上午11:06
 * <p>
 * <p>
 * CMDB中的关系对象
 */
public class Link {
    /*关系类型*/
    private String type;
    /*源端ID*/
    private long sid;
    /*目的端ID*/
    private long tid;
    /*说明信息*/
    private String note;

    public Link(long sid, long tid, String note) {
        this.sid = sid;
        this.tid = tid;
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString(){
        return "sid:"+sid+"-tid:"+tid;
    }
}
