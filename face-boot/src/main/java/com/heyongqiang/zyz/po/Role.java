package com.heyongqiang.zyz.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName weixing_role
 */
@TableName(value ="weixing_role")
@Data
public class Role implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 权限集合
     */
    private String permissionIds;

    /**
     * 巡检半径 
     */
    private Integer cicleRadius;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}