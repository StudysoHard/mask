package com.heyongqiang.zyz.common.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ImgPathQueryVo {
    
    // 图片地址
    private String imgPath;
    
    // 捕捉时间
    private String times;

    public ImgPathQueryVo(String imgPath, String times) {
        this.imgPath = imgPath;
        this.times = times;
    }
}
