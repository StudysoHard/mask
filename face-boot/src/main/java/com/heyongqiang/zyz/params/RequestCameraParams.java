package com.heyongqiang.zyz.params;


import lombok.Data;

/**
 * 用户通过个人中心申请开通区域摄像头的权限
 */
@Data
public class RequestCameraParams {

    // 申请人
    private String userName;

    // 申请摄像头的id
    private String cameraId;

}
