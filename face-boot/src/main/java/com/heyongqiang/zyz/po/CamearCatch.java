package com.heyongqiang.zyz.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName weixing_camear_catch
 */
@TableName(value ="weixing_camear_catch")
@Data
public class CamearCatch implements Serializable {
    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 摄像头id
     */
    private Long camera_id;

    /**
     * 用户名
     */
    private String user_id;

    /**
     * 拍摄图像id
     */
    private Long img_id;

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
        CamearCatch other = (CamearCatch) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getCamera_id() == null ? other.getCamera_id() == null : this.getCamera_id().equals(other.getCamera_id()))
            && (this.getUser_id() == null ? other.getUser_id() == null : this.getUser_id().equals(other.getUser_id()))
            && (this.getImg_id() == null ? other.getImg_id() == null : this.getImg_id().equals(other.getImg_id()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCamera_id() == null) ? 0 : getCamera_id().hashCode());
        result = prime * result + ((getUser_id() == null) ? 0 : getUser_id().hashCode());
        result = prime * result + ((getImg_id() == null) ? 0 : getImg_id().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", camera_id=").append(camera_id);
        sb.append(", user_id=").append(user_id);
        sb.append(", img_id=").append(img_id);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}