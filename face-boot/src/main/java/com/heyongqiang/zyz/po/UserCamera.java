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
 * @TableName weixing_user_camera
 */
@TableName(value ="weixing_user_camera")
@Data
public class UserCamera implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 摄像头id
     */
    private Long cameraId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 添加时间
     */
    private Date addTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}