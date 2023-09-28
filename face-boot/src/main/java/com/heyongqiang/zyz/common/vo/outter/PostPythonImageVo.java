package com.heyongqiang.zyz.common.vo.outter;

import lombok.Data;

@Data
public class PostPythonImageVo {

    // 照片的base64格式
    private String img;

    // 拍摄的人员
    private String name;

    // 巡检id
    private String xunJian_id;

    // index 本次拍摄的图片名
    private String index;

}
