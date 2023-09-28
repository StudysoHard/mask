package com.heyongqiang.zyz.controller;

import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.params.RequestMapInfoParams;
import com.heyongqiang.zyz.po.User;
import com.heyongqiang.zyz.service.MapInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Api("地图模块")
@RestController
@RequestMapping("map")
public class MapInfoController {

    @Resource
    private MapInfoService mapInfoService;


    @PostMapping("/getInfo")
    @ApiOperation(value = "地图模块-获取地图信息", notes = "地图模块-获取地图信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", value = "用户名", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "longitude", value = "坐标", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "latitude", value = "坐标", required = false, dataType = "String"),
    })
    public Result getMapInfo(@RequestBody RequestMapInfoParams requestMapInfoParams){
        return mapInfoService.getMapInfo(requestMapInfoParams);
    }


}
