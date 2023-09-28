package com.heyongqiang.zyz.params;

import lombok.Data;

@Data
public class XunJianFinalParams {

    // 巡检id
    private String xunJianId;

    // 未佩戴口罩人数
    private Integer noMask;

    //佩戴口罩人数
    private Integer mask;


}
