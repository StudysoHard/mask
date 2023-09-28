package com.heyongqiang.zyz.controller;


import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.params.AdminAttitudeParams;
import com.heyongqiang.zyz.params.RequestCameraParams;
import com.heyongqiang.zyz.service.WeixingRequestPermissionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api("申请模块")
@RestController
@RequestMapping("/request")
public class RequestPermissionController {

    @Resource
    private WeixingRequestPermissionsService weixingRequestPermissionsService;

    /**
     * 申请升级权限
     * @param userName
     * @return
     */
    @PostMapping("up")
    @ApiOperation(value = "申请模块-权限升级", notes = "申请模块-权限升级")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", value = "用户名", required = false, dataType = "String"),
    })
    public Result upPermissions(@RequestParam String userName){
        return weixingRequestPermissionsService.upPermissions(userName , "升级权限" , 1);
    }

    /**
     * 申请降低权限
     * @param userName
     * @return
     */
    @PostMapping("down")
    public Result downPermissions(@RequestParam String userName){
        return weixingRequestPermissionsService.downPermissions(userName , "降低权限");
    }

    @PostMapping("/camera")
    public Result requestCameraPermission(@RequestBody RequestCameraParams requestCameraParams){
        return weixingRequestPermissionsService.requestCameraPermission(requestCameraParams);
    }

    /**
     *  管理员的查询入口
     */
    @GetMapping("/list")
    public Result requestWebList(@RequestParam(value = "index") String index , @RequestParam(value = "status")String status){
        return weixingRequestPermissionsService.getRequestList(index , status);
    }

    @PostMapping("/attitude")
    public Result adminAttitude(@RequestBody AdminAttitudeParams adminAttitudeParams){
        return weixingRequestPermissionsService.adminAttitude(adminAttitudeParams);
    }

    /**
     * 查看但钱用户所有的申请信息
     */
    @PostMapping("/list")
    public Result getUserRequestList(@RequestParam("userName") String userName){
        return weixingRequestPermissionsService.getUserRequestList(userName);
    }

}
