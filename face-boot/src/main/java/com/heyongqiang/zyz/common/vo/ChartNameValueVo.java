package com.heyongqiang.zyz.common.vo;

import lombok.Data;

@Data
public class ChartNameValueVo {

    // 坐标名称
    private String name;

    // 索引
    private Integer index;

    // 混合图的线条形式
    private String type;

    // 颜色
    private String color;

    // 数据
    private String data;

    // 值
    private Integer value;

}
