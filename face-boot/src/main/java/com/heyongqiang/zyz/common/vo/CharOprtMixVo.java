package com.heyongqiang.zyz.common.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CharOprtMixVo implements Serializable {
    // 纵坐标
    private String[] categories;

    // 实际数据
    private ChartNameValueVo[] series;
}
