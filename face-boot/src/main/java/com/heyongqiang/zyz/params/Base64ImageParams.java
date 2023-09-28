package com.heyongqiang.zyz.params;

import lombok.Data;

/**
 * 接收小程序端传输的数据
 */

@Data
public class Base64ImageParams {
    // 图片
    private String img;
    // 拍摄人员
    private String name;
    // 巡检id
    private String xunJianId;
    // 经度
    private String longitude;
    // 纬度
    private String latitude;

}
