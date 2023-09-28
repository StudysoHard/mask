package com.heyongqiang.zyz.common.dto;


import lombok.Data;

import java.util.Date;

@Data
public class TopXunJainCountsDto {

    // 时间
    private Date catchTime;

    // 拍摄数量
    private Integer counts;

    // 摄像头id
    private Long xunjianId;

}
