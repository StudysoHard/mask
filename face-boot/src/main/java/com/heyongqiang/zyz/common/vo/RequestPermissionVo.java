package com.heyongqiang.zyz.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RequestPermissionVo implements Serializable {

    // 申请人名字
    private String name;

    // 申请人id
    private String userId;

    // 申请内容
    private String content;

    // 申请时间
    private Date requestTime;

    // 权限id
    private String id;

}
