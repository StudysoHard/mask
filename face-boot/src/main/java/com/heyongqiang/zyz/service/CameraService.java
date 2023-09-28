package com.heyongqiang.zyz.service;

import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.params.CameraAddParams;
import com.heyongqiang.zyz.po.Camera;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface CameraService extends IService<Camera> {

    Result addCameraInfo(CameraAddParams cameraAddParams);

    Result getCameraList();

    Result getUserCameraList(String userName);
}
