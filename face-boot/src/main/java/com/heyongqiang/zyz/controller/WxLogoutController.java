package com.heyongqiang.zyz.controller;

import com.heyongqiang.zyz.common.vo.ErrorCode;
import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.params.LoginParams;
import com.heyongqiang.zyz.service.WxLoginService;
import com.heyongqiang.zyz.service.WxLogoutService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/logout")
public class WxLogoutController {

    @Resource
    private WxLogoutService wxLogoutService;

    /**
     * 退出登录  将status状态修改为 'U'
     * @return
     */
    @GetMapping
    public Result wxUserLogout(@RequestBody LoginParams loginParams){
        if(loginParams.getUserName() != null){
            return Result.fail(ErrorCode.MAIN_PARAMS_NULL.getCode() , ErrorCode.MAIN_PARAMS_NULL.getMsg());
        }
        return wxLogoutService.userLogOut(loginParams);
    }

    @PostMapping
    public Result createUser(){
        return wxLogoutService.createUser();
    }

}
