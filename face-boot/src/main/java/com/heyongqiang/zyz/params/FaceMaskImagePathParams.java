package com.heyongqiang.zyz.params;

import lombok.Data;

/**
 * 存储图片地址的参数是
 */
@Data
public class FaceMaskImagePathParams {

    // 插入的主键
    private Long id;

    //插入的图片地址
    private String imagePath;

}
