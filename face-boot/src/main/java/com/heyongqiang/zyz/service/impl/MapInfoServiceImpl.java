package com.heyongqiang.zyz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heyongqiang.zyz.common.dto.CameraLocationDto;
import com.heyongqiang.zyz.common.vo.ErrorCode;
import com.heyongqiang.zyz.common.vo.MapInfoVo;
import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.mapper.*;
import com.heyongqiang.zyz.params.RequestMapInfoParams;
import com.heyongqiang.zyz.po.*;
import com.heyongqiang.zyz.service.MapInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MapInfoServiceImpl implements MapInfoService {

    @Resource
    private CameraMapper cameraMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserCameraMapper userCameraMapper;


    /**
     *    获取用户登录之后地图组件的西悉尼
     *    1. 获取到用户名之后查询用户的权限
     *    2. 查询所有摄像头的经纬度信息 ， 通过之前巡检半径判断那些摄像头是在方位内
     *    3. 通过id 查询所有的摄像头  并且将数据脱敏
     *    4. 封装到vo之前判断当前用户是否有权限
     *    5.  返回
     * @param requestMapInfoParams
     * @return
     */
    @Override
    public Result getMapInfo(RequestMapInfoParams requestMapInfoParams) {
        // todo 这里需要设置管理员查看所有摄像头的点位
        // 查询用户权限
        User user = userMapper.searchUserInfoByName(requestMapInfoParams.getUserName());
        // 判断是否无效请求
        if(user == null ){
            return Result.fail(ErrorCode.INVALID_REQUEST.getCode() , ErrorCode.INVALID_REQUEST.getMsg());
        }
        // 查询当前用户创建的设想头列表
        List<Long> createList = cameraMapper.selectCreateUser(user.getId());
        // 查询用户角色
        Role role = roleMapper.selectById(user.getRoleId());
        List<CameraLocationDto> cameraLocationDto =  cameraMapper.selectCameraLocation();
        // 查询用户的摄像头列表
        LambdaQueryWrapper<UserCamera> cameraIdQuery = new LambdaQueryWrapper<>();
        cameraIdQuery.eq(UserCamera::getUserId, user.getId());
        List<UserCamera> userCameras = userCameraMapper.selectList(cameraIdQuery);
        List<Long> ids = thoseLocateByCircle(role.getCicleRadius() , cameraLocationDto , requestMapInfoParams.getLongitude() , requestMapInfoParams.getLatitude() );
        if(ids.size() == 0 && createList.size() == 0){
            // 没有符合的数据
            return Result.fail(ErrorCode.NO_INFORMATION.getCode() , ErrorCode.NO_INFORMATION.getMsg());
        }
        ids.addAll(
                createList.stream().filter( e-> !ids.contains(e)).collect(Collectors.toList())
        );
        // 通过id 查询camear列表
        LambdaQueryWrapper<Camera> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Camera::getIsRequest , 1);
        queryWrapper.in(Camera::getId,ids);
        List<Camera> cameras = cameraMapper.selectList(queryWrapper);
        // 设置流访问地址
        Set<Long> set = new HashSet<>(
                userCameras.stream().
                        map(UserCamera::getCameraId).
                        collect(Collectors.toList())
        );
        cameras.forEach(item -> {
            if(!createList.contains(item.getId()) &&!set.contains(item.getId())){
                // 没有权限并且也不是创建者
                item.setStreamUrl("");
            }
        });
        MapInfoVo mapInfoVo = new MapInfoVo();
        mapInfoVo.setCameraList(cameras);
        mapInfoVo.setCircle(role.getCicleRadius());
        return Result.success(mapInfoVo);
    }


    /**
     *   通过半径来查看当前摄像头是否是在搜索范围内
     * @param cicle_radius
     * @param cameraLocationDto
     * @return
     */
    private List<Long> thoseLocateByCircle(Integer cicle_radius,
                                           List<CameraLocationDto> cameraLocationDto ,
                                           String longitude ,
                                           String latitude) {
        List<Long> list = new ArrayList<>();
        cameraLocationDto.forEach(
                item -> {
                    // 这里计算的时候  0.001 = 50 长度 需要换算
                    Double weidth = Math.abs(sub(Double.parseDouble(item.getLongitude()) , Double.parseDouble(longitude)));
                    Double height = Math.abs(sub(Double.parseDouble(item.getLatitude()) , Double.parseDouble(latitude)));
                    Double weigth2 = mul(weidth , weidth);
                    Double height2 = mul(height , height);
                    // 相减
                    if((weigth2 + height2) * 50000000 < cicle_radius){
                        list.add(item.getId());
                    }
                }
        );
        return list;
    }



    /**
     * 提供精确的减法运算。
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double sub(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static Double mul(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.multiply(b2).doubleValue();
    }


}
