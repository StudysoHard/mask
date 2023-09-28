package com.heyongqiang.zyz.common.vo;

import lombok.Data;

@Data
public class ErrorExitXunJianInfoVo {

    // 佩戴口罩人数
    private Integer maskPeople;

    // 未佩戴口罩人数
    private Integer noMaskPeople;

    // 巡检的id
    private String xunJianId;

}
