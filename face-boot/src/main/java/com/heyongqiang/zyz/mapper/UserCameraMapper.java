package com.heyongqiang.zyz.mapper;

import com.heyongqiang.zyz.po.UserCamera;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.heyongqiang.zyz.po.UserCamera
 */
public interface UserCameraMapper extends BaseMapper<UserCamera> {

    List<Long> selectCameraListsByUserId(@Param("userId") Long userId);

}




