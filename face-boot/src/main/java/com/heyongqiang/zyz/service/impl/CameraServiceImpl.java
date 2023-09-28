package com.heyongqiang.zyz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heyongqiang.zyz.common.vo.CameraListInfoVo;
import com.heyongqiang.zyz.common.vo.ErrorCode;
import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.mapper.UserCameraMapper;
import com.heyongqiang.zyz.mapper.UserMapper;
import com.heyongqiang.zyz.mapper.WeixingRequestPermissionsMapper;
import com.heyongqiang.zyz.params.CameraAddParams;
import com.heyongqiang.zyz.po.Camera;
import com.heyongqiang.zyz.po.User;
import com.heyongqiang.zyz.po.UserCamera;
import com.heyongqiang.zyz.po.WeixingRequestPermissions;
import com.heyongqiang.zyz.service.CameraService;
import com.heyongqiang.zyz.mapper.CameraMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class CameraServiceImpl extends ServiceImpl<CameraMapper, Camera>
    implements CameraService{

    @Resource
    private CameraMapper cameraMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserCameraMapper userCameraMapper;

    @Resource
    private WeixingRequestPermissionsMapper weixingRequestPermissionsMapper;

    /**
     * 新增摄像头数据
     * @param cameraAddParams
     * @return
     */
    @Override
    public Result addCameraInfo(CameraAddParams cameraAddParams) {
        // 查询用户信息
        User user = userMapper.searchUserInfoByName(cameraAddParams.getUserName());
        if(user == null){
            return Result.fail(ErrorCode.USER_NOEXIT.getCode() , ErrorCode.USER_NOEXIT.getMsg());
        }
        LambdaQueryWrapper<Camera> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Camera::getLongitude , cameraAddParams.getLongitude())
                .eq(Camera::getLatitude , cameraAddParams.getLatitude())
                .or()
                .eq(Camera::getStreamUrl , cameraAddParams.getStreamUrl());
        Camera camera = cameraMapper.selectOne(queryWrapper);
        if(camera != null){
            // 添加的信息重复
            return Result.fail(ErrorCode.DATA_EXIT.getCode() , ErrorCode.DATA_EXIT.getMsg());
        }
        Camera cameras = new Camera();
        cameras.setSrcUser(user.getId());
        cameras.setStreamUrl(cameraAddParams.getStreamUrl());
        // 默认未审批  需要管理员审批通过才能够在地图上显示
        cameras.setIsDelete(0);
        cameras.setIsRequest(0);
        cameras.setCreateTime(new Date());
        cameras.setLatitude(cameraAddParams.getLatitude());
        cameras.setLongitude(cameraAddParams.getLongitude());
        try {
            cameraMapper.insert(cameras);
            // 添加一条审批记录
            // 新建请求权限类
            WeixingRequestPermissions weixingRequestPermissions = new WeixingRequestPermissions();
            weixingRequestPermissions.setRequestContent("流接入申请|" + cameras.getId().toString());
            weixingRequestPermissions.setFloor(true);
            weixingRequestPermissions.setRequestType(2);
            weixingRequestPermissions.setUserId(user.getId());
            weixingRequestPermissions.setRequestTime(new Date());
            weixingRequestPermissions.setStatus(0);
            weixingRequestPermissionsMapper.insert(weixingRequestPermissions);
        } catch (Exception e){
            return Result.fail(ErrorCode.SQL_UPDATE.getCode() , ErrorCode.SQL_UPDATE.getMsg());
        }
        return Result.success();
    }

    @Override
    public Result getCameraList() {
        List<CameraListInfoVo> listInfoVos = new ArrayList<>();
        listInfoVos.add(new CameraListInfoVo(-9 , "" , "a"));
        listInfoVos.add(new CameraListInfoVo(-9 , "" , "b"));
        // 获取所有摄像头数据
        List<Camera> cameras = cameraMapper.selectList(null);
        int index = 0;
        for (int i = 0; i < cameras.size(); i++) {
            if(!cameras.get(i).getStreamUrl().startsWith("http://") && !cameras.get(i).getStreamUrl().startsWith("https://")){
                continue;
            }
            CameraListInfoVo cameraListInfoVo = new CameraListInfoVo();
            cameraListInfoVo.setId(index++);
            cameraListInfoVo.setVal(cameras.get(i).getStreamUrl());
            cameraListInfoVo.setLabel(i+"");
            listInfoVos.add(cameraListInfoVo);
        }
        listInfoVos.add(new CameraListInfoVo(-9 , "" , "c"));
        listInfoVos.add(new CameraListInfoVo(-9 , "" , "d"));
        return Result.success(listInfoVos);
    }

    /**
     * 拿到用户所有的摄像头列表
     * @param userName
     * @return
     */
    @Override
    public Result getUserCameraList(String userName) {
        List<String> source = new ArrayList<>();
        User user = userMapper.searchUserInfoByName(userName);
        if(user == null) return Result.fail(ErrorCode.USER_NOEXIT.getCode() , ErrorCode.USER_NOEXIT.getMsg());
        // 通过id 查询用户可申请摄像头列表
        LambdaQueryWrapper<Camera> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Camera::getSrcUser , user.getId());
        List<Camera> cameras = cameraMapper.selectList(queryWrapper);
        List<Long> ids = cameras.stream().map(Camera::getId).collect(Collectors.toList());
        LambdaQueryWrapper<UserCamera> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(UserCamera::getUserId,user.getId());
        queryWrapper1.in(UserCamera::getCameraId , ids);
        // 获取用户具备权限的摄像头
        List<UserCamera> userCameras = userCameraMapper.selectList(queryWrapper1);
        List<Long> have = userCameras.stream().map(UserCamera::getCameraId).collect(Collectors.toList());
        ids.forEach(
                e -> {
                    if(!have.contains(e)){
                        // 用户具备权限的摄像头列表中没有出现该
                        source.add(e.toString());
                    }
                }
        );
        return Result.success(source);
    }
}




