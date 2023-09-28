package com.heyongqiang.zyz.common.vo;


import lombok.Data;

@Data
public class UserInfoVo {

    // 用户名称
    private String userName;

    // 用户上次登录时间
    private String loginTime;

    // 用户角色
    private String roleName;

    // 用户巡检范围
    private Integer xunJianCircle;

    // 用户接入的摄像头数量
    private String cameraCounts;

    // 用户具备的所有摄像头权限
    private String haveCamera;

    // 索引
    private Integer index;

    // 用户id
    private String id;

}
