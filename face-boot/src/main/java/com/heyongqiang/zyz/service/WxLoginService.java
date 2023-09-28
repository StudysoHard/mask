package com.heyongqiang.zyz.service;

import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.params.LoginParams;

public interface WxLoginService {
    Result login(LoginParams loginParams);
}
