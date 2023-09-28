package com.heyongqiang.zyz.service;

import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.params.AdminAtitudeParams;
import com.heyongqiang.zyz.params.AdminAttitudeParams;

public interface AdminManangerService {
    Result getAdminAtitude(AdminAtitudeParams adminAttitudeParams);

    Result getUserInfo();

    Result resetPassord(String id);

    Result getXunJian();
}
