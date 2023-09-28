package com.heyongqiang.zyz.common.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ImgPathQueryDto {

    // 图片地址
    private String imgPath;

    // 捕捉时间
    private Date times;

}
