package com.heyongqiang.zyz.po;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("人像数据")
public class CapturedFace {

    private Long id;

    private Long milvusId;

    private Long cameraId;

    private Long catchTime;

    private String imgPath;

    private String faceName;

    private Integer nomask;

    private Integer mask;

}
