package com.heyongqiang.zyz.common.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserRequestListVo {

    // 索引
    private Integer index;

    // 申请时间
    private String requestDate;

    // 申请内容
    private String data;

    // 请求状态  0未读  1已读  2已处理
    private Integer status;

    // 审批结果
    private Integer flag;

}
