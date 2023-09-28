package com.heyongqiang.zyz.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class UChartsVo {

    // 查询该用户摄像头拍摄的数量集合
    private CharOprtColumnVo cameraCatch;

    // 按照时间分布 最多5场巡检捕捉的人数
    private CharOprtMountVo xunJianCatch;

    // 按照用户捕捉人数
    private CharOprtRoseVo userTop;

}
