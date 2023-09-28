package com.heyongqiang.zyz.params;

import lombok.Data;

/**
 * 更新用户信息
 */

@Data
public class UpdateUserInfoParmas {

    // 原来用户名
    private String originalName;

    // 修改后用户名
    private String changeName;

    // 修改后电话号码
    private String telephone;

    // 原密码
    private String originalPassword;

    // 修改后密码
    private String newPassword;

}
