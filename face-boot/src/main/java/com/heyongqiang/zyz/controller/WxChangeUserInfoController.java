package com.heyongqiang.zyz.controller;


import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.params.UpdateUserInfoParmas;
import com.heyongqiang.zyz.service.WxChangeUserInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/user")
public class WxChangeUserInfoController {

    @Resource
    private WxChangeUserInfoService wxChangeUserInfoService;

    @PostMapping("/update")
    public Result updateUserInfo(@RequestBody UpdateUserInfoParmas updateUserInfoParams){
        return wxChangeUserInfoService.updateUserInfo(updateUserInfoParams);
    }


}
