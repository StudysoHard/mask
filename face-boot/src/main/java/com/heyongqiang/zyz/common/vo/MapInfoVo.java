package com.heyongqiang.zyz.common.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.heyongqiang.zyz.po.Camera;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MapInfoVo {

    // 摄像头id 列表
    private List<Camera> cameraList;

    // 地图遮罩范围
    private Integer circle;


}
