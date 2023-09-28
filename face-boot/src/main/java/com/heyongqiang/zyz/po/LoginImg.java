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
 * @TableName weixing_login_img
 */
@TableName(value ="weixing_login_img")
@Data
public class LoginImg implements Serializable {
    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 登录时间
     */
    private Date login_time;

    /**
     * 用户id
     */
    private Long user_id;

    /**
     * 用户名
     */
    private String user_name;

    /**
     * 登录图片
     */
    private String login_img;

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
        LoginImg other = (LoginImg) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getLogin_time() == null ? other.getLogin_time() == null : this.getLogin_time().equals(other.getLogin_time()))
            && (this.getUser_id() == null ? other.getUser_id() == null : this.getUser_id().equals(other.getUser_id()))
            && (this.getUser_name() == null ? other.getUser_name() == null : this.getUser_name().equals(other.getUser_name()))
            && (this.getLogin_img() == null ? other.getLogin_img() == null : this.getLogin_img().equals(other.getLogin_img()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLogin_time() == null) ? 0 : getLogin_time().hashCode());
        result = prime * result + ((getUser_id() == null) ? 0 : getUser_id().hashCode());
        result = prime * result + ((getUser_name() == null) ? 0 : getUser_name().hashCode());
        result = prime * result + ((getLogin_img() == null) ? 0 : getLogin_img().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", login_time=").append(login_time);
        sb.append(", user_id=").append(user_id);
        sb.append(", user_name=").append(user_name);
        sb.append(", login_img=").append(login_img);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}