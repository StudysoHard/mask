package com.heyongqiang.zyz.controller;


import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.params.CameraAddParams;
import com.heyongqiang.zyz.service.CameraService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/camera")
public class CameraController {

    @Resource
    private CameraService cameraService;

    @PostMapping("/add")
    public Result addCamera(@RequestBody CameraAddParams cameraAddParams){
        return cameraService.addCameraInfo(cameraAddParams);
    }


    @GetMapping("/list")
    public Result getCameraList(){
        return cameraService.getCameraList();
    }


    @PostMapping("/list")
    public Result getUserCameraList(@RequestParam("userName") String userName){
        return cameraService.getUserCameraList(userName);
    }

}
