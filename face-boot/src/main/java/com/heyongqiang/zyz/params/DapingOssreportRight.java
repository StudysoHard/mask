package com.heyongqiang.zyz.params;



/**
 **/
public class DapingOssreportRight {

    /**
     * 状态
     */
    private Integer state;

    /**
     * 工单类型
     */
    private String servname;

    /**
     * 工单数量
     */
    private Integer count;

    /**
     * 省份
     */
    private String provinceName;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getServname() {
        return servname;
    }

    public void setServname(String servname) {
        this.servname = servname;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

}

