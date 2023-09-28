package com.heyongqiang.zyz.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName weixing_permissions
 */
@TableName(value ="weixing_permissions")
@Data
public class WeixingPermissions implements Serializable {
    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 权限名称
     */
    private String permissions_name;

    /**
     * 巡检半径
     */
    private Integer circle_radius;

    /**
     * 权限等级
     */
    private Integer level;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        WeixingPermissions other = (WeixingPermissions) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPermissions_name() == null ? other.getPermissions_name() == null : this.getPermissions_name().equals(other.getPermissions_name()))
            && (this.getCircle_radius() == null ? other.getCircle_radius() == null : this.getCircle_radius().equals(other.getCircle_radius()))
            && (this.getLevel() == null ? other.getLevel() == null : this.getLevel().equals(other.getLevel()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPermissions_name() == null) ? 0 : getPermissions_name().hashCode());
        result = prime * result + ((getCircle_radius() == null) ? 0 : getCircle_radius().hashCode());
        result = prime * result + ((getLevel() == null) ? 0 : getLevel().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", permissions_name=").append(permissions_name);
        sb.append(", circle_radius=").append(circle_radius);
        sb.append(", level=").append(level);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}