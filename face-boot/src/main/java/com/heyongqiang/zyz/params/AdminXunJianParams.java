package com.heyongqiang.zyz.params;

import lombok.Data;

@Data
public class AdminXunJianParams {

    // 同意与否
    private Boolean atitude;

    // 申请id
    private String requestId;

    // 申请内容
    private String requestContent;

}
