package com.heyongqiang.zyz.service;

import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.params.AdminAttitudeParams;
import com.heyongqiang.zyz.params.RequestCameraParams;
import com.heyongqiang.zyz.po.WeixingRequestPermissions;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface WeixingRequestPermissionsService extends IService<WeixingRequestPermissions> {
    /**
     * 用户升级权限
     * @param userName
     * @return
     */
    Result upPermissions(String userName, String requestContent , Integer requestType);


    Result downPermissions(String userName, String requestContent);


    Result requestCameraPermission(RequestCameraParams requestCameraParams);


    Result getRequestList(String index,String status);

    Result adminAttitude(AdminAttitudeParams adminAttitudeParams);

    Result getUserRequestList(String userName);

}
