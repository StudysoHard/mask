package com.heyongqiang.zyz.controller.admin;


import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.params.AdminAtitudeParams;
import com.heyongqiang.zyz.service.AdminManangerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
public class AdminAgreeController {

    @Resource
    private AdminManangerService adminManangerService;

    @PostMapping("/atitude")
    public Result getAdminAtitude(@RequestBody AdminAtitudeParams adminAttitudeParams){
        return adminManangerService.getAdminAtitude(adminAttitudeParams);
    }

    @PostMapping("/userInfo")
    public Result getUserInfo(){
        return adminManangerService.getUserInfo();
    }

    @GetMapping("/resetPassord")
    public Result resetPassord(@RequestParam String id){
        // todo 这里重置密码需要设置一天重置次数
        return adminManangerService.resetPassord(id);
    }

    @GetMapping("/xunJian")
    public Result getXunJian(){
        return adminManangerService.getXunJian();
    }



}
