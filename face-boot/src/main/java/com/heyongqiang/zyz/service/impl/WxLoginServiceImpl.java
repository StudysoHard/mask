package com.heyongqiang.zyz.service.impl;

import com.heyongqiang.zyz.common.vo.ErrorCode;
import com.heyongqiang.zyz.mapper.UserMapper;
import com.heyongqiang.zyz.po.User;
import com.heyongqiang.zyz.service.WxLoginService;
import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.params.LoginParams;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class WxLoginServiceImpl implements WxLoginService {

    @Resource
    private UserMapper userMapper;

    private static String slat = "123!hyqac5161dsi]";

    private static final SimpleDateFormat format = new SimpleDateFormat("dd");

    @Override
    public Result login(LoginParams loginParams) {
        User user = userMapper.searchUserInfoByName(loginParams.getUserName());
        if(user!= null && user.getPassword().equals(
                DigestUtils.md5DigestAsHex(
                        (slat + loginParams.getPassword()).getBytes()
                )
            )
        ) {
            Integer status = 0;
//            if("L".equals(user.getStatus())){
//                // 已经是登录状态
//                return Result.fail(ErrorCode.USER_IS_LOGIN.getCode() , ErrorCode.USER_IS_LOGIN.getMsg());
//            }
            Date date1 = new Date();
            String date = format.format(date1);
            // 判断是不是今天第一次登录
            if(user.getLoginTime() == null ||  date.equals(format.format(user.getLoginTime()))){
                // 不是第一次登录
                 status = 1;
            }
            // 修改登录状态和登录时间
            userMapper.updateLoginStatus(user.getId(),date1);
            //成功
            return Result.success(status);
        } else {
            //失败
            return Result.fail(ErrorCode.USER_PASSWORD_NO_MATCH.getCode(), ErrorCode.USER_PASSWORD_NO_MATCH.getMsg());
        }
    }


}
