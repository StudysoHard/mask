package com.heyongqiang.zyz.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WebRequestPermissionVo implements Serializable {

    // 申请人名字
    private String name;

    // 申请内容
    private String content;

    // 申请时间
    private Date requestTime;

    // 索引
    private Integer index;

    // 申请状态
    private String status;

    // 申请id
    private String id;

    public WebRequestPermissionVo(String name, String content, Date requestTime, Integer index, String status,String id) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.requestTime = requestTime;
        this.index = index;
        this.status = status;
    }
}
