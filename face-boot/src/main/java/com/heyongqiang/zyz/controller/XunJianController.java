package com.heyongqiang.zyz.controller;

import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.params.XunJianBeginParams;
import com.heyongqiang.zyz.params.XunJianFinalParams;
import com.heyongqiang.zyz.service.XunjianService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/xunjian")
public class XunJianController {

    @Resource
    private XunjianService xunjianService;

    /**
     * 开启巡检的时候插入一条巡检记录
     * @param xunJianBeginParams
     * @return
     */
    @ApiOperation(value = "开启巡检" , notes = "巡检模块 - 开启巡检")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", value = "用户名", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "latitude", value = "纬度", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "longitude", value = "经度", required = false, dataType = "String"),
    })
    @PostMapping("begin")
    public Result beginXunJian(@RequestBody XunJianBeginParams xunJianBeginParams){
        return xunjianService.beginXunJian(xunJianBeginParams);
    }

    @ApiOperation(value = "正常关闭巡检" , notes = "巡检模块 - 正常关闭巡检")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "xunJianId", value = "巡检id", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "noMask", value = "未佩戴口罩人数", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "mask", value = "佩戴口罩人数", required = false, dataType = "String"),
    })
    @PostMapping("end")
    public Result endXunJianNormal(@RequestBody XunJianFinalParams xunJianFinalParams){
        return xunjianService.endXunJianNormal(xunJianFinalParams);
    }


    @ApiOperation(value = "重新登录巡检数据上传" , notes = "巡检模块 - 异常退出数据上传")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", value = "用户名", required = false, dataType = "String"),
    })
    @PostMapping("find/out")
    public Result loginFindXunJianError(@RequestParam String userName){
        return xunjianService.loginFindXunJianError(userName);
    }


    @ApiOperation(value = "获取该巡检拍摄的轨迹" , notes = "巡检模块 - 获取巡检图片拍摄的位置轨迹")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "巡检id", required = false, dataType = "String"),
    })
    @GetMapping("/move/{id}")
    public Result getXunJianMove(@PathVariable String id){
        return xunjianService.getXunJianMove(id);
    }


    @ApiOperation(value = "获取该巡检拍摄的轨迹" , notes = "巡检模块 - 获取巡检图片拍摄的位置轨迹")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "巡检id", required = false, dataType = "String"),
    })
    @GetMapping("/counts/{id}")
    public Result getXunJianCounts(@PathVariable String id){
        return xunjianService.getXunJianCounts(id);
    }

    @ApiOperation(value = "管理员端获取巡检信息", notes = "巡检模块 - 获取巡检信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "巡检id", required = false, dataType = "String"),
    })
    @GetMapping("/getInfos/{id}")
    public Result getXunJianInfos(@PathVariable String id){
        return xunjianService.getXunJianInfos(id);
    }



}
