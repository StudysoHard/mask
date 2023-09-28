package com.heyongqiang.zyz.controller;

import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.params.Base64ImageParams;
import com.heyongqiang.zyz.params.FaceMaskImagePathParams;
import com.heyongqiang.zyz.service.ImgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 微信后端图片控制层
 */

@RestController
@RequestMapping("img")
@Api("图片接口")
public class ImgController {

    @Resource
    private ImgService imgService;


    /**
     * 接收前端发送的img 给python端进行人脸识别  返回识别结果
     * @param base64ImageParams
     * @return
     */
    @ApiOperation(value = "图像接口-口罩识别", notes = "图像接口-口罩识别")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "img", value = "上传的base64图片", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "拍摄人员", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "xunJianId", value = "拍摄人员", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "longitude", value = "经度", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "latitude", value = "纬度", required = false, dataType = "String")
    })
    @PostMapping("base64Img")
    public Result disposeXunjianImg(@RequestBody Base64ImageParams base64ImageParams){
        return imgService.disposeXunjianImg(base64ImageParams);
    }


    /**
     * 微信前端用于请求巡检图片识别结果的接口
     * @return.
     */
    @ApiOperation(value = "图像接口-按时间查询识别图像地址接口", notes = "图像接口-按时间查询识别图像地址接口")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "beginData", value = "查询时间", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userName", value = "用户名", required = false, dataType = "String")
    })
    @GetMapping("getXunJianImg")
    public Result getXunJianImg(@RequestParam(value = "beginData") String beginData ,
                                @RequestParam(value = "userName") String userName){
        return imgService.getXunJianImg(beginData,userName);
    }

    /**
     * 获取用户上一次识别的图像结果
     * @param id
     * @return
     */
    @ApiOperation(value = "图像接口-上一次识别后地址", notes = "图像接口-上一次识别后地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "图片id", required = false, dataType = "String")
    })
    @GetMapping("getPreImg")
    public Result getPreImage( @RequestParam(value = "id") String id ){
        return imgService.getPreImage(id);
    }


    @ApiOperation(value = "图像接口-获取历史识别的图片列表", notes = "图像接口-获取历史识别的图片列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userName", value = "用户名", required = false, dataType = "String")
    })
    @GetMapping("list")
    public Result getImgList( @RequestParam(value = "userName") String userName ){
        return imgService.getImgList(userName);
    }


}
