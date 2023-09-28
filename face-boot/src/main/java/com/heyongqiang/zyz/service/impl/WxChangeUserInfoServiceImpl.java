package com.heyongqiang.zyz.service.impl;

import com.heyongqiang.zyz.common.vo.ErrorCode;
import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.mapper.UserMapper;
import com.heyongqiang.zyz.params.UpdateUserInfoParmas;
import com.heyongqiang.zyz.po.User;
import com.heyongqiang.zyz.service.WxChangeUserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

@Service
public class WxChangeUserInfoServiceImpl implements WxChangeUserInfoService {

    @Resource
    private UserMapper userMapper;

    private static String slat = "123!hyqac5161dsi]";


    @Override
    public Result updateUserInfo(UpdateUserInfoParmas updateUserInfoParams) {
        // 判断原用户信息是否通过
        User user = userMapper.searchUserInfoByName(updateUserInfoParams.getOriginalName());
        if(!DigestUtils.md5DigestAsHex(
                (slat + updateUserInfoParams.getOriginalPassword()).getBytes()
        ).equals(user.getPassword())){
            // 没有通过用户身份验证
            return Result.fail(ErrorCode.USER_NOEXIT.getCode(), ErrorCode.USER_NOEXIT.getMsg());
        }
        if(updateUserInfoParams.getNewPassword() != null && !"".equals(updateUserInfoParams.getNewPassword())) user.setPassword(
                DigestUtils.md5DigestAsHex(
                        (slat + updateUserInfoParams.getNewPassword()).getBytes()
                )
        );
        if(updateUserInfoParams.getTelephone() != null && !"".equals(updateUserInfoParams.getTelephone())) user.setTelephone(updateUserInfoParams.getTelephone());
        if(updateUserInfoParams.getChangeName() != null && !"".equals(updateUserInfoParams.getChangeName())) user.setUserName(updateUserInfoParams.getChangeName());
        int i = userMapper.updateById(user);
        if(i != 1) return Result.fail(ErrorCode.SQL_UPDATE.getCode() , ErrorCode.SQL_UPDATE.getMsg());
        return Result.success();
    }
}
