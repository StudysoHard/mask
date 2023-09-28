package com.heyongqiang.zyz.mapper;

import com.heyongqiang.zyz.common.dto.CameraLocationDto;
import com.heyongqiang.zyz.po.Camera;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Entity com.heyongqiang.zyz.po.Camera
 */
public interface CameraMapper extends BaseMapper<Camera> {

    Long searchWatchUserListByCameraId(@Param("cameraId") Long cameraId);

    List<CameraLocationDto> selectCameraLocation();

    List<Long> selectCreateUser(@Param("id") Long id);

    Integer getCreateUserCounts(@Param("id") Long id);
}




