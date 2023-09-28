package com.heyongqiang.zyz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heyongqiang.zyz.common.vo.*;
import com.heyongqiang.zyz.mapper.*;
import com.heyongqiang.zyz.params.AdminAttitudeParams;
import com.heyongqiang.zyz.params.RequestCameraParams;
import com.heyongqiang.zyz.po.*;
import com.heyongqiang.zyz.service.WeixingRequestPermissionsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
@Service
public class WeixingRequestPermissionsServiceImpl extends ServiceImpl<WeixingRequestPermissionsMapper, WeixingRequestPermissions>
    implements WeixingRequestPermissionsService{

    @Resource
    private UserMapper userMapper;

    @Resource
    private CameraMapper cameraMapper;

    @Resource
    private WeixingRequestPermissionsMapper weixingRequestPermissionsMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserCameraMapper userCameraMapper;

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");



    @Override
    public Result upPermissions(String userName , String requestContent , Integer requestType) {
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(User::getUserName , userName);
        User user = userMapper.selectOne(userQuery);
        if(requestType == 1){
            if(user.getRoleId() == 1 || user.getRoleId() == 5){
                // 如果但钱用户是管理员或者区域主管则权限无法升级
                return Result.fail(ErrorCode.PERMISSION_MAX.getCode(),ErrorCode.PERMISSION_MAX.getMsg());
            }
            // 判断用户之前有没有申请过权限
            LambdaQueryWrapper<WeixingRequestPermissions> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(WeixingRequestPermissions::getUserId , user.getId());
            lambdaQueryWrapper.eq(WeixingRequestPermissions::getRequestType , 1);
            lambdaQueryWrapper.ne(WeixingRequestPermissions::getStatus , 2);
            Integer integer = weixingRequestPermissionsMapper.selectCount(lambdaQueryWrapper);
            if(integer != 0) {
                return Result.fail(ErrorCode.DATA_EXIT.getCode() , ErrorCode.DATA_EXIT.getMsg());
            }
        }
        // 新建请求权限类
        WeixingRequestPermissions weixingRequestPermissions = new WeixingRequestPermissions();
        if(requestType != 1){
            weixingRequestPermissions.setRequestContent(requestContent);
        } else {
            weixingRequestPermissions.setRequestContent(requestContent+"|" + user.getId());
        }
        weixingRequestPermissions.setFloor(true);
        weixingRequestPermissions.setRequestType(requestType);
        weixingRequestPermissions.setUserId(user.getId());
        weixingRequestPermissions.setRequestTime(new Date());
        weixingRequestPermissions.setStatus(0);
        boolean save = this.save(weixingRequestPermissions);
        if(save) {
            // 申请成功
            return Result.success();
        } else {
            return Result.fail(ErrorCode.SESSION_TIME_OUT.getCode(),ErrorCode.SESSION_TIME_OUT.getMsg());
        }
    }

    @Override
    public Result downPermissions(String userName , String requestContent) {
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(User::getUserName , userName);
        User user = userMapper.selectOne(userQuery);
        LambdaQueryWrapper<WeixingRequestPermissions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeixingRequestPermissions::getUserId,user.getId());
        // 查询原来用户权限
        WeixingRequestPermissions one = getOne(queryWrapper);
        // 新建请求权限类
        WeixingRequestPermissions weixingRequestPermissions = new WeixingRequestPermissions();
        weixingRequestPermissions.setRequestContent(requestContent);
        weixingRequestPermissions.setFloor(false);
        weixingRequestPermissions.setRequestTime(new Date());
        weixingRequestPermissions.setUserId(user.getId());
        weixingRequestPermissions.setStatus(0);
        boolean save = this.save(weixingRequestPermissions);
        if(save) {
            // 申请成功
            return Result.success();
        } else {
            return Result.fail(ErrorCode.SESSION_TIME_OUT.getCode(),ErrorCode.SESSION_TIME_OUT.getMsg());
        }
    }

    /**
     *  用户申请摄像头
     *
     *  1.  判断该用户是否正确 ， 并且判断该权限是否以及开放 ， 以及该摄像头是否存在
     *  2. 通过判别则构建一个permission到数据库总
     *  3.   该请求返回  =》 管理员端处理本次申请之后将会返回结果
     * @param requestCameraParams
     * @return
     */
    @Override
    public Result requestCameraPermission(RequestCameraParams requestCameraParams) {
        // 判断请求是否合法
        // 通过用户名查询用户信息
        User user = userMapper.searchUserInfoByName(requestCameraParams.getUserName());
        Camera camera = cameraMapper.selectById(Long.parseLong(requestCameraParams.getCameraId()));
        if(user == null || camera == null){
            return Result.fail(ErrorCode.NO_INFORMATION.getCode() , ErrorCode.NO_INFORMATION.getMsg());
        }
        // 判断该用户是不是创建者
        Long aLong = cameraMapper.searchWatchUserListByCameraId(Long.parseLong(requestCameraParams.getCameraId()));
        if(Objects.equals(aLong, user.getId())){
            // 创建者无法申请当前摄像头
            return Result.fail(ErrorCode.INVALID_REQUEST.getCode() , ErrorCode.INVALID_REQUEST.getMsg());
        }
        // 直接查询 user_camera表中对应camera和user的关系表
        QueryWrapper<UserCamera> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("camera_id").eq("user_id" , user.getId());
        List<UserCamera> cameraList = userCameraMapper.selectList(queryWrapper);
        for (UserCamera userCamera : cameraList) {
            if(userCamera.getCameraId().equals(camera.getId())){
                // 该camera已经有这个用户的权限了
                return Result.fail(ErrorCode.DATABASE_IS_EXIT.getCode() , ErrorCode.DATABASE_IS_EXIT.getMsg());
            }
        }
        // 新增一天申请
        upPermissions(requestCameraParams.getUserName() , "申请该摄像头权限|" + requestCameraParams.getCameraId() , 0);
        return Result.success();
    }

    /**
     * 查询微信中所有的申请
     * @param index  查询的类型   0  摄像头申请  1身份申请  2 流接入
     * @param status  0  全部  1 未读
     * @return
     */
    @Override
    public Result getRequestList(String index,String status) {
        List<WebRequestPermissionVo> source = new ArrayList<>();
        LambdaQueryWrapper<WeixingRequestPermissions> request = new LambdaQueryWrapper<>();
        request.eq(WeixingRequestPermissions::getRequestType , Integer.parseInt(status));
        request.ne(WeixingRequestPermissions::getStatus , 2);
        List<WeixingRequestPermissions> weixingRequestPermissions = weixingRequestPermissionsMapper.selectList(request);
        for (int i = 0; i < weixingRequestPermissions.size(); i++) {
            User user = userMapper.selectById(weixingRequestPermissions.get(i).getUserId());
            Integer status1 = weixingRequestPermissions.get(i).getStatus();
            String temp = "";
            if(status1 == 0){
                status = "未查看";
            } else if(status1 == 1){
                status = "已查看";
            } else {
                status = "已审批";
            }
            WebRequestPermissionVo webRequestPermissionVo = new WebRequestPermissionVo(user.getUserName() , weixingRequestPermissions.get(i).getRequestContent() , weixingRequestPermissions.get(i).getRequestTime(),i + 1 ,status , weixingRequestPermissions.get(i).getId().toString() );
            source.add(webRequestPermissionVo);
            weixingRequestPermissions.get(i).setStatus(1);
            weixingRequestPermissionsMapper.updateById(weixingRequestPermissions.get(i));
        }
        return Result.success(source);

    }

    /**
     * 管理员审批结果判断
     * @param adminAttitudeParams
     * @return
     */
    @Override
    public Result adminAttitude(AdminAttitudeParams adminAttitudeParams) {
        // 判断参数是否为空
        if(adminAttitudeParams.getAttitude() == null || adminAttitudeParams.getPermissionType() == null){
            // 参数不合法
            return Result.fail(ErrorCode.MAIN_PARAMS_NULL.getCode() , ErrorCode.MAIN_PARAMS_NULL.getMsg());
        }
        switch (adminAttitudeParams.getPermissionType()){
            case "0":
                return resolveNormalPermission(adminAttitudeParams.getAttitude(),Long.parseLong(adminAttitudeParams.getId()));
            case "1":
                return resolveCameraPermission(adminAttitudeParams.getAttitude() , Long.parseLong(adminAttitudeParams.getId()) , adminAttitudeParams.getCameraId());
            default:
                return Result.fail(ErrorCode.INVALID_REQUEST.getCode() , ErrorCode.INVALID_REQUEST.getMsg());
        }
    }

    @Override
    public Result getUserRequestList(String userName) {
        LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(User::getUserName , userName);
        User user = userMapper.selectOne(userQuery);
        LambdaQueryWrapper<WeixingRequestPermissions> request = new LambdaQueryWrapper<>();
        request.eq(WeixingRequestPermissions::getUserId , user.getId());
        if(user == null){
            return Result.fail(ErrorCode.USER_NOEXIT.getCode() , ErrorCode.USER_NOEXIT.getMsg());
        }
        // 搜索当前用户申请的权限
        List<WeixingRequestPermissions> weixingRequestPermissions = weixingRequestPermissionsMapper.selectList(request);
        return Result.success(copyList(weixingRequestPermissions));
    }


    /**
     * 脱敏
     * @param weixingRequestPermissions
     * @return
     */
    private String[][] copyList(List<WeixingRequestPermissions> weixingRequestPermissions) {

        int size = weixingRequestPermissions.size() + 1;
        // 索引
        String[] index = new String[size];
        index[0] = "序列";
        String[] content = new String[size];
        content[0] = "申请内容";
        String[] requestDate = new String[size];
        requestDate[0] = "申请时间";
        String[] status = new String[size];
        status[0] = "环节";
        String[] flag = new String[size];
        flag[0]  = "审批状态";
        for (int i = 1; i <= weixingRequestPermissions.size(); i++) {
            WeixingRequestPermissions e = weixingRequestPermissions.get(i-1);
            index[i] = String.valueOf(i);
            content[i] = e.getRequestContent();
            requestDate[i] = format.format(e.getRequestTime());
            String temp = e.getStatus().toString();
            if("0".equals(temp)) {
                status[i] = "未查看";
            } else if("1".equals(temp)) {
                status[i] = "已查看";
            } else {
                status[i] = "已审批";
            }
            flag[i] = e.getThrose()!= null ? e.getRequestContent() : "未审批";
        }
        String[][] source = new String[][]{
                index,
                content,
                requestDate,
                status,
                flag
        };
        return source;
    }

    /**
     * 审批用户的摄像头申请
     * @param attitude
     * @param parseLong
     * @param cameraId
     * @return
     */
    private Result resolveCameraPermission(Integer attitude, long parseLong, String cameraId) {
        // 通过id 查询对应申请
        WeixingRequestPermissions byId = this.getById(parseLong);
        if(attitude == 0){
            // 同意
            UserCamera userCamera = new UserCamera();
            userCamera.setAddTime(new Date());
            userCamera.setCameraId(Long.parseLong(cameraId));
            userCamera.setUserId(byId.getUserId());
            userCameraMapper.insert(userCamera);
        }
        // 更新审批状态
        byId.setStatus(2);
        weixingRequestPermissionsMapper.updateById(byId);
        return Result.success();
    }

    /**
     * 判断用户的普通权限申请
     * @param attitude
     * @param parseLong
     * @return
     */
    private Result resolveNormalPermission(Integer attitude, long parseLong) {
        // 通过id 查询对应申请
        WeixingRequestPermissions byId = this.getById(parseLong);
        byId.setThrose(attitude);
        /**
         * 如果同意则将该用户的权限升级，改变巡检范围
         */
        if(attitude == 0){
            // 同意
            User user = userMapper.selectById(byId.getUserId());
            if(byId.getFloor()){
                // 升级权限
                if(user.getRoleId() == 5L){
                    // 权限最大无法再升级
                    return Result.fail(ErrorCode.PERMISSION_MAX.getCode() , ErrorCode.PERMISSION_MAX.getMsg());
                }
                byId.setStatus(2);
                // 修改状态为已经处理
                weixingRequestPermissionsMapper.updateById(byId);
                user.setRoleId(user.getRoleId()+1L);
                userMapper.updateById(user);
            } else{
                // 降低权限
                if(user.getRoleId() == 2L){
                    // 权限最大无法再升级
                    return Result.fail(ErrorCode.PERMISSION_MAX.getCode() , ErrorCode.PERMISSION_MAX.getMsg());
                }
                byId.setStatus(2);
                // 修改状态为已经处理
                weixingRequestPermissionsMapper.updateById(byId);
                user.setRoleId(user.getRoleId()-1L);
                userMapper.updateById(user);
            }

        } else {
            // 不同意
            return Result.success();
        }
        return Result.success();
    }
}




