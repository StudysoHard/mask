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
 * @TableName weing_log
 */
@TableName(value ="weing_log")
@Data
public class WeingLog implements Serializable {
    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 请求参数
     */
    private String request_params;

    /**
     * 响应参数
     */
    private String respone_message;

    /**
     * 是否成功
     */
    private Boolean status;

    /**
     * 请求模式  // 1-界面, 2-邮件, 3-手机（短信）, 4-语音, 5-小灵通（短信）, 6-钉钉, 7-RTX, 8-企业微信
     */
    private Byte mode;

    /**
     * 请求时间
     */
    private Date create_time;

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
        WeingLog other = (WeingLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRequest_params() == null ? other.getRequest_params() == null : this.getRequest_params().equals(other.getRequest_params()))
            && (this.getRespone_message() == null ? other.getRespone_message() == null : this.getRespone_message().equals(other.getRespone_message()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getMode() == null ? other.getMode() == null : this.getMode().equals(other.getMode()))
            && (this.getCreate_time() == null ? other.getCreate_time() == null : this.getCreate_time().equals(other.getCreate_time()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRequest_params() == null) ? 0 : getRequest_params().hashCode());
        result = prime * result + ((getRespone_message() == null) ? 0 : getRespone_message().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getMode() == null) ? 0 : getMode().hashCode());
        result = prime * result + ((getCreate_time() == null) ? 0 : getCreate_time().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", request_params=").append(request_params);
        sb.append(", respone_message=").append(respone_message);
        sb.append(", status=").append(status);
        sb.append(", mode=").append(mode);
        sb.append(", create_time=").append(create_time);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}