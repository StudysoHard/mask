package com.heyongqiang.zyz.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.common.vo.UserInfoVo;
import com.heyongqiang.zyz.common.vo.XunJianInfoVo;
import com.heyongqiang.zyz.mapper.*;
import com.heyongqiang.zyz.params.AdminAtitudeParams;
import com.heyongqiang.zyz.po.*;
import com.heyongqiang.zyz.service.AdminManangerService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class AdminManangerServiceImpl implements AdminManangerService {

    @Resource
    private WeixingRequestPermissionsMapper weixingRequestPermissionsMapper;

    @Resource
    private UserCameraMapper userCameraMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CameraMapper cameraMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private XunjianMapper xunjianMapper;

    private static String slat = "123!hyqac5161dsi]";

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


    public static void main(String[] args) {
//        String sysId = "16003";
//
//        List<Long> ids = Arrays.asList(sysId.split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
//        System.out.println(ids);
//        int[] ints =  {1,-4,-5,-2,5,0,-1,2};
//        maximumSum(ints);
        List<String> test = new ArrayList<>();
        test.add("1231561561");
        test.add("12315615612.1.12");
        test.add("12315615610");
        test.add("123");
        test.add("1122");
        test.forEach(
                element -> {
                    System.out.println(element.length());
                    if(element.length() > 5){
                        System.out.println("进来了");
                        element = element.substring(0, 5) + "...";
                        System.out.println(element);
                    }
                }
        );
        System.out.println(test);

    }

    public static int maximumSum(int[] arr) {
        int maxValue = arr[0];
        int resource = arr[0];
        int source = arr[0];
        int fast = 0;
        int deleteNum = 0;
        while(fast + 1 < arr.length){
            // 判断整体是否为正
            if(source < 0) {
                source = 0;
                deleteNum = 0;
            }
            fast++;
            maxValue = Math.max(maxValue , arr[fast]);
            if(arr[fast] >= 0){
                source += arr[fast];
                resource = Math.max(resource , source);
            } else {
                // 首先判断是否删除过
                if(deleteNum == 0){
                    deleteNum = arr[fast];
                } else {
                    // int preDelete = deleteNum;
                    deleteNum = Math.min(deleteNum , arr[fast]);
                    source += Math.max(deleteNum , arr[fast]);
                }
            }
        }
        if(maxValue < 0) return maxValue;
        return resource;
    }



    public static  String formatProportion(Integer count,Integer totalCount){
        BigDecimal countBigDecimal = new BigDecimal(count);
        BigDecimal totalCountBigDecimal = new BigDecimal(totalCount);
        BigDecimal divide = countBigDecimal.divide(totalCountBigDecimal, 20, BigDecimal.ROUND_UP);
        for (int i=19;i>=3;i--){
            divide=divide.setScale(i,BigDecimal.ROUND_HALF_UP);
        }
        return divide.toString();
    }

    private static List<String> updateNoticeInterfaceFlagSql(List<String> notificationIds, String type , String nrt) {
        if (notificationIds!=null && !notificationIds.isEmpty()) {
            String sql = "";
            if ("success".equals(type)) {
                if ("".equals(nrt)){
                    sql = "UPDATE NOTIFICATION_INTERFACE SET NOTIFY_FLAG = '1' WHERE NOTIFICATION_INTERFACE_ID IN ";
                } else {
                    sql = "UPDATE HN_NOTIFICATION_INTERFACE_NRT SET NOTIFY_FLAG = '1' WHERE NOTIFICATION_INTERFACE_ID IN ";
                }
            } else {
                if ("".equals(nrt)){
                    sql = "UPDATE NOTIFICATION_INTERFACE SET NOTIFY_FLAG = '-1' WHERE NOTIFICATION_INTERFACE_ID IN ";
                } else {
                    sql = "UPDATE HN_NOTIFICATION_INTERFACE_NRT SET NOTIFY_FLAG = '-1' WHERE NOTIFICATION_INTERFACE_ID IN ";
                }
            }

            List<String> sqlList = new ArrayList<>();
            StringBuilder sb = null;
            int size = 5;
            int total = notificationIds.size();
            int pageTotal = (total / size) + ((total % size > 0) ? 1 : 0); // 根据页码取数据
            for (int i = 0, len = pageTotal - 1; i <= len; i++) { // 分页计算
                int fromIndex = i * size;
                int toIndex = ((i == len) ? total : ((i + 1) * size));
                List<String> subList = notificationIds.subList(fromIndex, toIndex);
                sb = new StringBuilder();
                sb.append(sql);
                sb.append(" ( ");
                sb.append(String.join(",", subList));
                sb.append(" )");
                sqlList.add(sb.toString());
            }
            return sqlList;
        }
        return null;
    }


    private static long noIntern(){
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            int j = i % 100;
            String str = String.valueOf(j);
        }
        return System.currentTimeMillis() - start;
    }

    private static long intern(){
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            int j = i % 100;
            String str = String.valueOf(j).intern();
        }
        return System.currentTimeMillis() - start;
    }



    /**
     * 审批用户权限
     * @param adminAttitudeParams
     * @return
     */
    @Override
    public Result getAdminAtitude(AdminAtitudeParams adminAttitudeParams) {
        WeixingRequestPermissions weixingRequestPermissions = weixingRequestPermissionsMapper.selectById(adminAttitudeParams.getRequestId());
        weixingRequestPermissions.setStatus(2);
        if(adminAttitudeParams.getAtitude()){
            // 同意
            weixingRequestPermissions.setThroseContent("同意");
            weixingRequestPermissions.setThrose(1);
            generateMethod(weixingRequestPermissions.getRequestType(), weixingRequestPermissions.getUserId()  , weixingRequestPermissions);
        } else {
            // 不同意
            weixingRequestPermissions.setThroseContent("不同意");
            weixingRequestPermissions.setThrose(0);
        }
        // 更新申请状态
        weixingRequestPermissionsMapper.updateById(weixingRequestPermissions);
        return Result.success();
    }

    /**
     * 管理员获取用户信息
     * @return
     */
    @Override
    public Result getUserInfo() {
        List<UserInfoVo> source = new ArrayList<>();
        // 查询所有用户
        List<User> users = userMapper.selectList(null);
        LambdaQueryWrapper<Camera> queryWrapperC = new LambdaQueryWrapper<>();
        queryWrapperC.eq(Camera::getIsRequest, 1);
        Integer counts = cameraMapper.selectCount(queryWrapperC);
        AtomicInteger i = new AtomicInteger(1);
        users.forEach(e -> {
            LambdaQueryWrapper<UserCamera> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserCamera::getUserId , e.getId());
            UserInfoVo userInfoVo = new UserInfoVo();
            userInfoVo.setUserName(e.getUserName());
            if(e.getLoginTime() != null){
                userInfoVo.setLoginTime(format.format(e.getLoginTime()));
            }
            userInfoVo.setIndex(i.getAndIncrement());
            Integer ints = cameraMapper.getCreateUserCounts(e.getId());
            // 通过role_id 查询巡检半径
            Role role = roleMapper.selectById(e.getRoleId());
            userInfoVo.setCameraCounts( ints + "/" + counts + "个");
            userInfoVo.setHaveCamera((ints + userCameraMapper.selectCount(queryWrapper)) + "个");
            userInfoVo.setXunJianCircle(role.getCicleRadius());
            userInfoVo.setRoleName(role.getRoleName());
            userInfoVo.setId(e.getId().toString());
            source.add(userInfoVo);
        });
        return Result.success(source);
    }

    /**
     * 重置用户密码
     * @param id
     * @return
     */
    @Override
    public Result resetPassord(String id) {
        User user = userMapper.selectById(Long.parseLong(id));;
        user.setPassword(
                DigestUtils.md5DigestAsHex(
                        (slat + "password").getBytes()
                )
        );
        userMapper.updateById(user);
        return Result.success();
    }

    /**
     * 获取所有巡检信息
     * @return
     */
    @Override
    public Result getXunJian() {
        List<XunJianInfoVo> source = new ArrayList<>();
        // 查询数据库所有巡检
        List<Xunjian> xunjians = xunjianMapper.selectList(null);
        for (int i = 0; i < xunjians.size() ;i++) {
            XunJianInfoVo xunJianInfoVo = new XunJianInfoVo();
            User user = userMapper.selectById(xunjians.get(i).getUserId());
            xunJianInfoVo.setId(xunjians.get(i).getId().toString());
            xunJianInfoVo.setIndex(i+1);
            xunJianInfoVo.setDate(format.format(xunjians.get(i).getCreateTime()));
            xunJianInfoVo.setUserName(user.getUserName());
            xunJianInfoVo.setLongitude(xunjians.get(i).getLongitude());
            xunJianInfoVo.setLatitude(xunjians.get(i).getLatitude());
            source.add(xunJianInfoVo);
        }
        return Result.success(source);
    }

    /**
     * 处理对应的申请
     * @param requestType
     * @param weixingRequestPermissions
     */
    private void generateMethod(Integer requestType , Long userId , WeixingRequestPermissions weixingRequestPermissions ){
        switch (requestType){
            case 0 :
                System.out.println("摄像头申请");
                UserCamera userCamera = new UserCamera();
                userCamera.setCameraId(Long.valueOf(weixingRequestPermissions.getRequestContent().substring(weixingRequestPermissions.getRequestContent().indexOf("|")+1)));
                userCamera.setUserId(userId);
                userCamera.setAddTime(new Date());

                userCameraMapper.insert(userCamera);
                break;
            case 1 :
                System.out.println("身份申请");
                User user = userMapper.selectById(weixingRequestPermissions.getUserId());
                user.setRoleId(user.getRoleId() + 1L);
                userMapper.updateById(user);
                break;
            case 2 :
                System.out.println("流接入申请");
                Camera camera = cameraMapper.selectById(Long.valueOf(weixingRequestPermissions.getRequestContent().substring(weixingRequestPermissions.getRequestContent().indexOf("|") + 1)));
                camera.setIsRequest(1);
                cameraMapper.updateById(camera);
                break;
            default: break;
        }
    }

}
