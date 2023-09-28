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
 * @TableName weixing_xunjian
 */
@TableName(value ="weixing_xunjian")
@Data
public class Xunjian implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 结束时间
     */
    private Date finalTime;

    /**
     * 未佩戴人数
     */
    private Integer nomaskNumber;

    /**
     * 佩戴人数
     */
    private Integer maskNumber;

}