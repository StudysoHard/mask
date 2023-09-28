package com.heyongqiang.zyz.service;

import com.heyongqiang.zyz.common.vo.Result;
import com.heyongqiang.zyz.params.Base64ImageParams;
import com.heyongqiang.zyz.params.FaceMaskImagePathParams;

public interface ImgService {
    // 处理巡检图片
    Result disposeXunjianImg(Base64ImageParams base64ImageParams);

    Result getXunJianImg(String beginData , String userName);

    Result getPreImage(String id);

    Result getImgList(String userName);
}
