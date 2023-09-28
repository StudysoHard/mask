package com.heyongqiang.zyz.common.vo;

import lombok.Data;

@Data
public class XunJianInfoVo {

    // 巡检id
    private String id;

    // 索引
    private Integer index;

    // 创建人
    private String userName;

    // 创建时间
    private String date;

    // 巡检开启经度
    private String longitude;

    // 巡检开启纬度
    private String latitude;


}
