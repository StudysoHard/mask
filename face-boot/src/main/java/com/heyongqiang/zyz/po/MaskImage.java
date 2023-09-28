package com.heyongqiang.zyz.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "weixing_maskimg")
public class MaskImage {

    // 图片巡检id
    @TableId(value = "id" , type = IdType.ASSIGN_ID)
    private Long id;

    // 佩戴口罩人数
    private Integer mask_number;

    //未佩戴口罩人数
    private Integer nomask_number;

    // 图片地址
    private String imgPath;

    // 巡检用户
    private String userName;

    // 巡检用户id
    private Long userId;

    // 逻辑删除字段
    private Integer isDelete;

    // 拍摄时间
    private Date catchTime;

    // 拍摄经度
    private String longitude;

    // 拍摄纬度
    private String latitude;

    // 是否是摄像头拍摄的  0是 1否
    private int is_camera;

    // 拍摄的摄像头id
    private Long camera_id;

    // 巡检id
    private Long xunjian_id;

}
