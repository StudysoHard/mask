package com.heyongqiang.zyz.params;

import lombok.Data;

/**
 * python 端返回的数据
 */
@Data
public class FaseMaskInfoParams {

    // 本次落盘的 id
    private String id;

    // 识别的总人数
    private Integer human;

    // 未佩戴口罩人数
    private Integer nomask_human;

    //佩戴口罩人数
    private Integer mask_human;

}
