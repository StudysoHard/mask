package com.heyongqiang.zyz.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName weixing_request_permissions
 */
@TableName(value ="weixing_request_permissions")
@Data
public class WeixingRequestPermissions implements Serializable {
    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 申请时间
     */
    private Date requestTime;

    /**
     * 用户名
     */
    private Long userId;

    /**
     * 升级 true 降级 false
     */
    private Boolean floor;

    /**
     * 内容说明
     */
    private String requestContent;

    /**
     * 申请状态  0 未读 1已读  2已处理
     */
    private Integer status;

    /**
     * 审批状态
     */
    private Integer throse;

    /**
     *  申请类型
     */
    private Integer requestType;

    /**
     * 审批内容
     */
    private String throseContent;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}