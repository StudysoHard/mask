package com.heyongqiang.zyz.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName weixing_camera
 */
@TableName(value ="weixing_camera")
@Data
public class Camera implements Serializable {

    /**
     * 摄像头主键id
     */
    @TableId
    private Long id;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 创建用户id
     */
    private Long srcUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除  0正常 1删除
     */
    private Integer isDelete;

    /**
     * 流地址
     */
    private String streamUrl;

    /**
     * 是否审批过  0 未审批 1  已经审批
     */
    private Integer isRequest;

}