package com.heyongqiang.zyz.service;

import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.params.XunJianBeginParams;
import com.heyongqiang.zyz.params.XunJianFinalParams;
import com.heyongqiang.zyz.po.Xunjian;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface XunjianService extends IService<Xunjian> {

    Result beginXunJian(XunJianBeginParams xunJianBeginParams);

    Result endXunJianNormal(XunJianFinalParams xunJianFinalParams);

    Result loginFindXunJianError(String userName);

    Result getXunJianMove(String id);

    Result getXunJianCounts(String id);

    Result getXunJianInfos(String id);
}
