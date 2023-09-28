package com.heyongqiang.zyz.params;

import lombok.Data;

import java.util.Date;

@Data
public class AdminAttitudeParams {

    // 申请id
    private String id;

    // 审批时间
    private Date time;

    // 审批状态   0 同意  1不同意
    private Integer attitude;

    // 审批类型   0 普通权限申请   1 摄像头权限申请
    private String permissionType;

    // 摄像头id   如果是摄像头申请，需要携带上id 否则返回错误提示
    private String cameraId;
}
