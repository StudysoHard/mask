package com.heyongqiang.zyz.service.impl;


import com.heyongqiang.zyz.common.vo.ErrorCode;
import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.mapper.UserMapper;
import com.heyongqiang.zyz.params.LoginParams;
import com.heyongqiang.zyz.po.User;
import com.heyongqiang.zyz.service.WxLogoutService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class WxLogoutServiceImpl implements WxLogoutService {

    @Resource
    private UserMapper userMapper;

    private static String slat = "123!hyqac5161dsi]";

    /**
     *  用户登出  使用事务来管理  如何更新表字段失败  那么回滚之前的擦偶作
     * @param loginParams
     * @return
     */
    @Override
    public Result userLogOut(LoginParams loginParams) {
        /**
         * 逻辑 ：
         *  1. 查询当前用户的状态
         *    U  逻辑继续
         *    L  返回用户已经登出了
         *  2. 更新用户的状态  并且保存更新的条数
         *    1  正常返回
         *    0  返回已经登出了 错误信息
         */
        // 判断当前登录用户是否正确


        // 查询用户当前的登录状态
        String status =  userMapper.selectUserStatusByUserName(loginParams.getUserName());
        if("L".equals(status)){
            // 返回当前已经登陆出了
            return Result.fail(ErrorCode.STATUS_IS_UPDATE.getCode() ,ErrorCode.STATUS_IS_UPDATE.getMsg());
        }
        // 更新用户状态
        userMapper.updateLogoutStatus(loginParams.getUserName(),new Date());
        return Result.success();
    }

    @Override
    public Result createUser() {
        User user = new User();
        user.setUserName(String.valueOf(System.currentTimeMillis()));
        user.setPassword(
                DigestUtils.md5DigestAsHex(
                    (slat + "password").getBytes()
                )
        );
        user.setRoleId(2L);
        user.setStatus("U");
        userMapper.insert(user);
        return Result.success();
    }
}
