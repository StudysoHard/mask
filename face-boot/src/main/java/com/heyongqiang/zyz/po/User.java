package com.heyongqiang.zyz.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.util.DigestUtils;

import java.util.Date;

@Data
@TableName("weixing_user")
public class User {

    // id
    private Long id;

    //姓名
    private String userName;

    // 密码
    private String password;

    //最近登录时间
    private Date loginTime;

    // 加密字段
    private String md5Key;

    //角色id
    private Long roleId;

    //逻辑删除字段
    private int isDelete;

    //手机号
    private String telephone;

    //登录状态  'L'  登录   'U' 未登录
    private String status;

}