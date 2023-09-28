package com.heyongqiang.zyz.params;


import lombok.Data;

@Data
public class CameraAddParams {

    // 摄像头经度
    private String longitude;

    // 摄像头纬度
    private String latitude;

    // 摄像头流地址
    private String streamUrl;

    // 用户名
    private String userName;


}
